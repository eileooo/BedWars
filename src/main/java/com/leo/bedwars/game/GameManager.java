package com.leo.bedwars.game;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.arena.ArenaManager;
import com.leo.bedwars.game.tasks.StartingGameTask;
import com.leo.bedwars.misc.Utils;
import com.leo.bedwars.scoreboard.FastBoard;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class GameManager {

    BedWars main;
    ArenaManager arenaManager;
    ArrayList<Game> games = new ArrayList<>();
    Random randomizer = new Random();

    public GameManager(BedWars main, ArenaManager arenaManager) {
        this.main = main;
        this.arenaManager = arenaManager;
    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void clearGames() {
        this.games.clear();
    }

    public void handleJoin(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "Encontrando partida...");

        Stream<Game> availableGames = games.stream().filter(game -> game.getState() == GameState.WAITING);
        Optional<Game> optionalGame = availableGames.findAny();

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            player.sendMessage(ChatColor.AQUA + game.getId() + " está em modo de espera, entrando.");
            insertPlayer(player, game);
            return;

        } else {
            if (arenaManager.arenaCache.size() == 0) {
                player.sendMessage(ChatColor.RED + "Algo deu errado, nenhuma arena encontrada.");
                return;
            }

            String id = Utils.generateId();
            List<Arena> arenas = arenaManager.arenaCache;
            Arena arena = arenas.get(randomizer.nextInt(arenas.size()));
            String newArenaWorldName = arena.getWorldName() + "_" + id;

            try {
                copyDirectory(arena.getWorldName(), newArenaWorldName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            File uid = new File(newArenaWorldName + "/uid.dat");
            File originalUID = new File(arena.getWorldName() + "/uid.dat");
            if (uid.delete()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "uid.dat deletado da partida " + ChatColor.AQUA + id);
            }

            if (originalUID.delete()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "uid.dat deletado do arquivo original.");
            }

            World world = new WorldCreator(newArenaWorldName).createWorld();

            arena.setWorld(world);
            arenaManager.setupArena(arena);

            Game game = new Game(id, arena);

            game.setState(GameState.WAITING);
            games.add(game);
            insertPlayer(player, game);
        }

    }

    public void insertPlayer(Player player, Game game) {
        game.players.put(player, game.getAvailableIsland());

        FastBoard board = new FastBoard(player);
        board.updateTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "BedWars");

        game.boards.put(player, board);
        player.sendMessage(ChatColor.GRAY + "Entrando em " + game.getId() + ".");
        player.teleport(game.getArena().getLobby().asBukkitLocation());

        if (game.players.size() == 2) {
            setGameState(game, GameState.STARTING);
        }

    }

    public void setGameState(Game game, GameState state) {
        game.setState(state);
        switch (state) {
            case STARTING -> {
                game.broadcast(ChatColor.GREEN + "Iniciando em 10 segundos...");
                StartingGameTask task = new StartingGameTask(this, game, 5);
                task.runTaskTimer(main, 0, 20);
                break;
            }
            case RUNNING -> {
                for (Map.Entry<Player, Island> entry : game.players.entrySet()) {
                    Player player = entry.getKey();
                    Island island = entry.getValue();

                    player.sendMessage(ChatColor.GRAY + "Você é o time " + island.getTeam().getColoredTranslate() + ChatColor.GRAY + ".");
                    player.teleport(island.getGenerator().getLocation().asBukkitLocation());
                    player.setPlayerListName(island.getTeam().getColor() + "" + ChatColor.BOLD + island.getTeam().toString().toUpperCase() + " " + ChatColor.RESET + island.getTeam().getColor() + player.getName());
                }
                break;
            }
            default -> {
                game.broadcast("idk");
                break;
            }
        }
    }

    public void updateBoards() {
        for (Game game : games) {
            for (FastBoard board : game.boards.values()) {
                board.updateLines(
                        ChatColor.DARK_GRAY + game.getId(),
                        "",
                        ChatColor.WHITE + "Jogadores: " + ChatColor.YELLOW + game.players.size() + ChatColor.WHITE + "/8",
                        ""
                );
            }
        }
    }

    public void unloadBoards() {
        for (Game game : games) {
            for (FastBoard board : game.boards.values()) {
                board.delete();
            }
        }
    }

    public void unloadArenas() {
        for (Game game : games) {
            String worldName = game.getArena().getWorldName() + "_" + game.getId();
            File arena = new File(worldName);

            Bukkit.unloadWorld(worldName, false);
            if (arena.delete()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + arena.getName() + " deletado.");
            }
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}

package com.leo.bedwars.game;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.arena.ArenaManager;
import com.leo.bedwars.misc.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
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

            player.sendMessage(ChatColor.GRAY + "Entrando em " + id + ".");

            Game game = new Game(id, arena);

            game.setState(GameState.WAITING);
            games.add(game);

            player.teleport(arena.getLobby().asBukkitLocation());
        }

    }

    public void insertPlayer(Player player, Game game) {

    }


    public ArrayList<Game> getGames() {
        return games;
    }
}

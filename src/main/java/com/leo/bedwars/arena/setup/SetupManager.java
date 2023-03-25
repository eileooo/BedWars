package com.leo.bedwars.arena.setup;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.arena.GenericLocation;
import com.leo.bedwars.misc.ItemBuilder;
import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.game.generator.Generator;
import com.leo.bedwars.game.generator.GeneratorType;
import com.leo.bedwars.scoreboard.FastBoard;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SetupManager {

    BedWars main;
    HashMap<Player, SetupArena> setupCache = new HashMap<Player, SetupArena>();
    public Map<Player, FastBoard> boards = new HashMap<>();


    public SetupManager(BedWars main) {
        this.main = main;
    }

    public void addToSetup(Player player, String name, String arenaWorld) {
        player.sendMessage(ChatColor.GREEN + "Carregando mundo...");

        File file = new File(arenaWorld);

        if (!file.exists()) {
            player.sendMessage(ChatColor.RED + "Arena não encontrada.");
            return;
        }

        WorldCreator creator = new WorldCreator(arenaWorld);
        World world = Bukkit.createWorld(creator);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "Erro ao carregar arena.");
            return;
        }

        world.setSpawnFlags(false, false);

        SetupArena arena = new SetupArena(name, arenaWorld);
        arena.setPastLocation(player.getLocation());

        player.setGameMode(GameMode.CREATIVE);
        player.teleport(new Location(world, 0, 75, 0));
        setupCache.put(player, arena);
        setItems(player);

        FastBoard board = new FastBoard(player);
        board.updateTitle(ChatColor.AQUA + "BedWars - SETUP");
        this.boards.put(player, board);
    }

    public void updateBoard(FastBoard board) {
        SetupArena arena = getSetupArena(board.getPlayer());
        List<Generator> diamond = arena.getGenerators().stream().filter(gen -> gen.getType() == GeneratorType.DIAMOND).toList();
        List<Generator> emerald = arena.getGenerators().stream().filter(gen -> gen.getType() == GeneratorType.EMERALD).toList();

        ArrayList<String> lines = new ArrayList<>();
        lines.add("");
        lines.add((arena.getLobby() == null ? ChatColor.RED + "x " : ChatColor.GREEN + "✔ ") + "Lobby");
        lines.add("");
        lines.add(ChatColor.GRAY + "Geradores:");
        lines.add(ChatColor.GRAY + "* " + (diamond.isEmpty() ? ChatColor.RED : ChatColor.GREEN) + "Diamante: " + ChatColor.GRAY + diamond.size());
        lines.add(ChatColor.GRAY + "* " + (emerald.isEmpty() ? ChatColor.RED : ChatColor.GREEN) + "Esmeralda: " + ChatColor.GRAY + emerald.size());
        lines.add("");
        lines.add(ChatColor.GRAY + "Time: " + arena.getCurrentTeam().getColoredTranslate().toUpperCase());
        lines.addAll(generateLoreForIsland(arena, arena.getCurrentTeam()));

        board.updateLines(lines);
    }

    void removeFromSetup(Player player) {
        Location location = getSetupArena(player).getPastLocation();
        player.teleport(location);
        player.getInventory().clear();

        boards.remove(player).delete();

        setupCache.remove(player);
    }

    public SetupArena getSetupArena(Player player) {
        return setupCache.get(player);
    }

    public void setItems(Player player) {
        //  wool, emerald, diamond, iron, bed, shears, tnt, lime glass pane
        SetupArena arena = getSetupArena(player);
        Team currentTeam = arena.getCurrentTeam();
        String coloredTeam = currentTeam.getColor() + currentTeam.translate();

        ItemStack team = new ItemBuilder(currentTeam.getWool()).withDisplayName(ChatColor.WHITE + "Selecionar time").build();
        ItemStack emerald = new ItemBuilder(Material.EMERALD).withDisplayName(ChatColor.GREEN + "Gerador de esmeralda").build();
        ItemStack diamond = new ItemBuilder(Material.DIAMOND).withDisplayName(ChatColor.AQUA + "Gerador de diamante").build();
        ItemStack iron = new ItemBuilder(Material.IRON_INGOT).withDisplayName(ChatColor.WHITE + "Gerador do time " + coloredTeam).build();
        ItemStack bed = new ItemBuilder(currentTeam.getBed()).withDisplayName(ChatColor.RED + "Cama").build();
        ItemStack shop = new ItemBuilder(Material.SHEARS).withDisplayName(ChatColor.GREEN + "Aldeão comerciante").build();
        ItemStack upgrades = new ItemBuilder(Material.TNT).withDisplayName(ChatColor.GOLD + "Aldeão de melhorias").build();
        ItemStack save = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Salvar arena").build();
        ItemStack lobby = new ItemBuilder(Material.COMPASS).withDisplayName(ChatColor.GREEN + "Lobby").build();
        player.getInventory().clear();

        Inventory inventory = player.getInventory();
        inventory.setItem(0, team);
        inventory.setItem(5, emerald);
        inventory.setItem(6, diamond);
        inventory.setItem(1, iron);
        inventory.setItem(2, bed);
        inventory.setItem(3, shop);
        inventory.setItem(4, upgrades);
        inventory.setItem(8, save);
        inventory.setItem(7, lobby);
    }

    public void changeCurrentTeam(Player player, Team team) {
        this.setupCache.get(player).setCurrentTeam(team);
        setItems(player);
    }

    public boolean isPlayerOnSetup(Player player) {
        return this.setupCache.containsKey(player);
    }

    public ArrayList<String> generateLoreForIsland(SetupArena arena, Team team) {
        Island island = arena.getIslands().get(team);
        ArrayList<String> lore = new ArrayList<>();

        lore.add((island.getBed() == null ? ChatColor.RED + "x " : ChatColor.GREEN + "✔ ") + "Cama");
        lore.add((island.getGenerator() == null ? ChatColor.RED + "x " : ChatColor.GREEN + "✔ ") + "Gerador");
        lore.add((island.getShop() == null ? ChatColor.RED + "x " : ChatColor.GREEN + "✔ ") + "Aldeão comerciante");
        lore.add((island.getUpgrades() == null ? ChatColor.RED + "x " : ChatColor.GREEN + "✔ ") + "Aldeão de melhorias");
        lore.add(" ");

        return lore;
    }

    public void save(Player player) {
        YamlConfiguration configuration = main.getArenaConfiguration();

        SetupArena arena = getSetupArena(player);
        String name = arena.getName();
        List<Generator> generators = arena.getGenerators();

        configuration.set(name + ".world", arena.getWorld());

        GenericLocation lobby = arena.getLobby();

        if (lobby == null) {
            player.sendMessage(ChatColor.RED + "O lobby não foi configurado!!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 50, 50);
            return;
        }

        lobby.save(name + ".lobby", configuration);

        List<Generator> diamond = generators.stream().filter(gen -> gen.getType() == GeneratorType.DIAMOND).toList();
        List<Generator> emerald = generators.stream().filter(gen -> gen.getType() == GeneratorType.EMERALD).toList();

        int index = 0;

        if (diamond.size() == 0) {
            player.sendMessage(ChatColor.RED + "Nenhum gerador de diamante foi configurado!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 50, 50);

            return;
        }

        if (emerald.size() == 0) {
            player.sendMessage(ChatColor.RED + "Nenhum gerador de esmeralda foi configurado!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 50, 50);
            return;
        }


        for (Generator diamondGenerator : diamond) {
            diamondGenerator.getLocation().save(name + ".generators.diamond." + index, configuration);
            index++;
        }

        index = 0;

        for (Generator emeraldGenerator : emerald) {
            emeraldGenerator.getLocation().save(name + ".generators.emerald." + index, configuration);
            index++;
        }

        boolean eligible = true;

        for (Island island : arena.getIslands().values()) {
            if (!island.isAllSet()) {
                player.sendMessage(ChatColor.RED + "O time " + island.getTeam().getColoredTranslate() + ChatColor.RED + " não está com todos as propriedades configuradas.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 50, 50);
                changeCurrentTeam(player, island.getTeam());
                eligible = false;
                break;
            }
            island.getGenerator().getLocation().save(name + ".islands." + island.getTeam().toString() + ".generator", configuration);
            island.getBed().save(name + ".islands." + island.getTeam().toString() + ".bed", configuration);
            island.getShop().save(name + ".islands." + island.getTeam().toString() + ".shop", configuration);
            island.getUpgrades().save(name + ".islands." + island.getTeam().toString() + ".upgrades", configuration);
        }

        if (eligible) {
            try {
                configuration.save(main.getArenaFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            main.loadArenas();
            player.sendMessage(ChatColor.GREEN + "A Arena " + ChatColor.YELLOW + arena.getName() + ChatColor.GREEN + " foi salva!");
            removeFromSetup(player);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 50);

        }


    }

}

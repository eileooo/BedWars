package com.leo.bedwars.arena.setup;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.ItemBuilder;
import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.arena.generator.Generator;
import com.leo.bedwars.arena.generator.GeneratorType;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetupManager {

    BedWars main;
    HashMap<Player, SetupArena> setupCache = new HashMap<Player, SetupArena>();

    public SetupManager(BedWars main) {
        this.main = main;
    }

    public void joinSetup(Player player, String name, String arenaWorld) {
        player.sendMessage(ChatColor.GREEN + "Carregando mundo...");

        WorldCreator creator = new WorldCreator(arenaWorld);
        World world = Bukkit.createWorld(creator);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "Erro ao carregar arena.");
            return;
        }

        world.setSpawnFlags(false, false);

        SetupArena arena = new SetupArena(name, arenaWorld);
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(new Location(world, 0, 75, 0));

        setupCache.put(player, arena);
        setItems(player);
    }

    public SetupArena getSetupArena(Player player) {
        return setupCache.get(player);
    }

    public void setItems(Player player) {
        //  wool, emerald, diamond, iron, bed, shears, tnt, lime glass pane
        SetupArena arena = getSetupArena(player);
        Team currentTeam = arena.getCurrentTeam();
        String coloredTeam = currentTeam.getColor() + currentTeam.translate();

        ItemStack team = new ItemBuilder(currentTeam.getWool()).withDisplayName(ChatColor.WHITE + "Time: " + coloredTeam).build();
        ItemStack emerald = new ItemBuilder(Material.EMERALD).withDisplayName(ChatColor.GREEN + "Gerador de esmeralda").build();
        ItemStack diamond = new ItemBuilder(Material.DIAMOND).withDisplayName(ChatColor.AQUA + "Gerador de diamante").build();
        ItemStack iron = new ItemBuilder(Material.IRON_INGOT).withDisplayName(ChatColor.WHITE + "Gerador do time " + coloredTeam).build();
        ItemStack bed = new ItemBuilder(currentTeam.getBed()).withDisplayName(ChatColor.WHITE + "Cama").build();
        ItemStack shop = new ItemBuilder(Material.SHEARS).withDisplayName(ChatColor.GREEN + "Aldeão comerciante").build();
        ItemStack upgrades = new ItemBuilder(Material.TNT).withDisplayName(ChatColor.GOLD + "Aldeão de melhorias").build();
        ItemStack save = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Salvar arena").build();

        player.getInventory().clear();

        Inventory inventory = player.getInventory();
        inventory.setItem(0, team);
        inventory.setItem(1, emerald);
        inventory.setItem(2, diamond);
        inventory.setItem(3, iron);
        inventory.setItem(4, bed);
        inventory.setItem(5, shop);
        inventory.setItem(6, upgrades);
        inventory.setItem(8, save);
    }

    public void changeCurrentTeam(Player player, Team team) {
        this.setupCache.get(player).setCurrentTeam(team);
    }

    public boolean isPlayerOnSetup(Player player) {
        return this.setupCache.containsKey(player);
    }

    public List<String> generateLoreForIsland(SetupArena arena, Team team) {
        Island island = arena.getIslands().get(team);
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add((island.getBed() != null ? ChatColor.GREEN : ChatColor.RED) + "Cama");
        lore.add((island.getGenerator() != null ? ChatColor.GREEN : ChatColor.RED) + "Gerador");
        lore.add((island.getShop() != null ? ChatColor.GREEN : ChatColor.RED) + "Aldeão comerciante");
        lore.add((island.getUpgrades() != null ? ChatColor.GREEN : ChatColor.RED) + "Aldeão de melhorias");
        lore.add(" ");

        return lore;
    }

    public void save(Player player) {
        YamlConfiguration configuration = main.getArenaConfiguration();

        SetupArena arena = getSetupArena(player);
        String name = arena.getName();
        List<Generator> generators = arena.getGenerators();

        configuration.set(name + ".world", arena.getWorld());

        List<Generator> diamond = generators.stream().filter(gen -> gen.getType() == GeneratorType.DIAMOND).toList();
        List<Generator> emerald = generators.stream().filter(gen -> gen.getType() == GeneratorType.EMERALD).toList();

        int index = 0;

        for (Generator diamondGenerator : diamond) {
            diamondGenerator.getLocation().save(name + ".generators.diamond." + index, configuration);
            index++;
        }

        index = 0;

        for (Generator emeraldGenerator : emerald) {
            emeraldGenerator.getLocation().save(name + ".generators.emerald." + index, configuration);
            index++;
        }

        for (Island island : arena.getIslands().values()) {
            Bukkit.getConsoleSender().sendMessage(island.getTeam().getColoredTranslate());
            island.getGenerator().getLocation().save(name + ".islands." + island.getTeam().toString() + ".generator", configuration);
            island.getBed().save(name + ".islands." + island.getTeam().toString() + ".bed", configuration);
            island.getShop().save(name + ".islands." + island.getTeam().toString() + ".shop", configuration);
            island.getUpgrades().save(name + ".islands." + island.getTeam().toString() + ".upgrades", configuration);
        }

        try {
            configuration.save(main.getArenaFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setupCache.remove(player);
    }

}

package com.leo.bedwars;

import com.leo.bedwars.arena.*;
import com.leo.bedwars.game.generator.Generator;
import com.leo.bedwars.game.generator.GeneratorType;
import com.leo.bedwars.arena.setup.InventoryEvent;
import com.leo.bedwars.arena.setup.SetupCommand;
import com.leo.bedwars.arena.setup.SetupManager;
import com.leo.bedwars.commands.TestCommand;
import com.leo.bedwars.game.GameManager;
import com.leo.bedwars.game.commands.AdminCommand;
import com.leo.bedwars.game.commands.JoinCommand;
import com.leo.bedwars.scoreboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class BedWars extends JavaPlugin implements Listener {

    GameManager gameManager;
    ArenaManager arenaManager;
    SetupManager setupManager;
    File arenaFile;
    YamlConfiguration arenaConfiguration = new YamlConfiguration();

    @Override
    public void onEnable() {
        this.arenaManager = new ArenaManager(this);
        this.gameManager = new GameManager(this, arenaManager);
        this.setupManager = new SetupManager(this);
        loadArenasConfig();
        loadArenas();
        getCommand("arenas").setExecutor(new TestCommand(arenaManager));
        getCommand("arena").setExecutor(new SetupCommand(setupManager));
        getCommand("join").setExecutor(new JoinCommand(gameManager));
        getCommand("limparjogos").setExecutor(new AdminCommand(gameManager));
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(setupManager), this);
        getServer().getPluginManager().registerEvents(this, this);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : setupManager.boards.values()) {
                setupManager.updateBoard(board);
            }
        }, 0, 20);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

    }


    public void loadArenas() {

        Set<String> arenas = arenaConfiguration.getKeys(false);
        for (String key : arenas) {

            String world = arenaConfiguration.getString(key + ".world");
            GenericLocation lobby = new GenericLocation().fromConfigurationSection(arenaConfiguration.getConfigurationSection(key + ".lobby"), "");

            Arena arena = new Arena(key, world);
            arena.setLobby(lobby);

            ConfigurationSection diamondGenerators = arenaConfiguration.getConfigurationSection(key + ".generators.diamond");
            ConfigurationSection emeraldGenerators = arenaConfiguration.getConfigurationSection(key + ".generators.emerald");

            assert diamondGenerators != null;
            for (String diamondGenerator : diamondGenerators.getKeys(false)) {
                GenericLocation location = new GenericLocation().fromConfigurationSection(diamondGenerators, diamondGenerator);
                Generator generator = new Generator(GeneratorType.DIAMOND, location, diamondGenerator);
                location.printLocation();
                arena.addGenerator(generator);
            }

            assert emeraldGenerators != null;
            for (String emeraldGenerator : emeraldGenerators.getKeys(false)) {
                GenericLocation location = new GenericLocation().fromConfigurationSection(emeraldGenerators, emeraldGenerator);
                Generator generator = new Generator(GeneratorType.EMERALD, location, emeraldGenerator);
                location.printLocation();
                arena.addGenerator(generator);
            }

            ConfigurationSection islandsSection = arenaConfiguration.getConfigurationSection(key + ".islands");
            assert islandsSection != null;
            Set<String> islands = islandsSection.getKeys(false);

            if (islands.size() < 8) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A arena " + ChatColor.WHITE + " tem menos de 8 ilhas. ");
                continue;
            }

            for (String islandKey : islands) {
                Team team = Team.valueOf(islandKey.toUpperCase());

                ConfigurationSection generatorSection = islandsSection.getConfigurationSection(islandKey + ".generator");
                assert generatorSection != null;
                GenericLocation generator = new GenericLocation().fromConfigurationSection(generatorSection, "");

                ConfigurationSection bedSection = islandsSection.getConfigurationSection(islandKey + ".bed");
                assert bedSection != null;
                GenericLocation bed = new GenericLocation().fromConfigurationSection(bedSection, "");

                ConfigurationSection shopSection = islandsSection.getConfigurationSection(islandKey + ".shop");
                assert shopSection != null;
                GenericLocation shop = new GenericLocation().fromConfigurationSection(shopSection, "");

                ConfigurationSection upgradesSection = islandsSection.getConfigurationSection(islandKey + ".upgrades");
                assert upgradesSection != null;
                GenericLocation upgrades = new GenericLocation().fromConfigurationSection(upgradesSection, "");

                Island island = new Island(team);
                island.setGenerator(new Generator(GeneratorType.ISLAND, generator, island.getTeam().toString()));
                island.setBed(bed);
                island.setShop(shop);
                island.setUpgrades(upgrades);
                arena.addIsland(island);

            }
            arenaManager.cacheArena(arena);
        }

    }

    void loadArenasConfig() {
        arenaFile = new File(getDataFolder(), "arenas.yml");

        try {
            if (arenaFile.createNewFile()) {
                saveResource("arenas.yml", false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            arenaConfiguration.load(arenaFile);
        } catch (IOException | InvalidConfigurationException error) {
            error.printStackTrace();
        }

    }


    @Override
    public void onDisable() {

    }

    public File getArenaFile() {
        return arenaFile;
    }

    public YamlConfiguration getArenaConfiguration() {
        return arenaConfiguration;
    }
}

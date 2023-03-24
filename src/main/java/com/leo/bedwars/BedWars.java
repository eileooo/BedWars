package com.leo.bedwars;

import com.leo.bedwars.arena.*;
import com.leo.bedwars.arena.generator.Generator;
import com.leo.bedwars.arena.generator.GeneratorType;
import com.leo.bedwars.arena.setup.InventoryEvent;
import com.leo.bedwars.arena.setup.SetupCommand;
import com.leo.bedwars.arena.setup.SetupManager;
import com.leo.bedwars.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class BedWars extends JavaPlugin {

    ArenaManager arenaManager;
    SetupManager setupManager;
    File arenaFile;
    YamlConfiguration arenaConfiguration = new YamlConfiguration();


    @Override
    public void onEnable() {
        this.arenaManager = new ArenaManager(this);
        this.setupManager = new SetupManager(this);
        loadArenasConfig();
        loadArenas();
        getCommand("arenas").setExecutor(new TestCommand(arenaManager));
        getCommand("arena").setExecutor(new SetupCommand(setupManager));
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(setupManager), this);
    }

    void loadArenas() {
        Set<String> arenas = arenaConfiguration.getKeys(false);
        for (String key : arenas) {

            String world = arenaConfiguration.getString(key + ".map");

            Arena arena = new Arena(key, world);

            ConfigurationSection diamondGenerators = arenaConfiguration.getConfigurationSection(key + ".generators.diamond");
            ConfigurationSection emeraldGenerators = arenaConfiguration.getConfigurationSection(key + ".generators.emerald");

            for (String diamondGenerator : diamondGenerators.getKeys(false)) {
                GenericLocation location = new GenericLocation().fromConfigurationSection(diamondGenerators);
                Generator generator = new Generator(GeneratorType.DIAMOND, location);

                arena.addGenerator(generator);
            }

            for (String emeraldGenerator : emeraldGenerators.getKeys(false)) {
                GenericLocation location = new GenericLocation().fromConfigurationSection(emeraldGenerators);
                Generator generator = new Generator(GeneratorType.EMERALD, location);

                arena.addGenerator(generator);
            }

            ConfigurationSection islandsSection = arenaConfiguration.getConfigurationSection(key + ".islands");
            Set<String> islands = islandsSection.getKeys(false);

            if (islands.size() < 8) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "A arena " + ChatColor.WHITE + " tem menos de 8 ilhas. ");
                continue;
            }

            for (String islandKey : islands) {
                Team team = Team.valueOf(islandKey.toUpperCase());

                ConfigurationSection generatorSection = islandsSection.getConfigurationSection(islandKey + ".generator");
                GenericLocation generator = new GenericLocation().fromConfigurationSection(generatorSection);

                ConfigurationSection bedSection = islandsSection.getConfigurationSection(islandKey + ".bed");
                GenericLocation bed = new GenericLocation().fromConfigurationSection(bedSection);

                ConfigurationSection shopSection = islandsSection.getConfigurationSection(islandKey + ".shop");
                GenericLocation shop = new GenericLocation().fromConfigurationSection(shopSection);

                ConfigurationSection upgradesSection = islandsSection.getConfigurationSection(islandKey + ".upgrades");
                GenericLocation upgrades = new GenericLocation().fromConfigurationSection(upgradesSection);

                Island island = new Island(team);
                island.setGenerator(new Generator(GeneratorType.ISLAND, generator));
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

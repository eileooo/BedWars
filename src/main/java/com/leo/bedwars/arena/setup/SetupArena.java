package com.leo.bedwars.arena.setup;

import com.leo.bedwars.ItemBuilder;
import com.leo.bedwars.arena.GenericLocation;
import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.arena.generator.Generator;
import com.leo.bedwars.arena.generator.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SetupArena {

    String name;
    String world;
    HashMap<Team, Island> islands = new HashMap<>();
    ArrayList<Generator> generators = new ArrayList<>();
    Team currentTeam = Team.RED;

    public SetupArena(String name, String world) {
        this.name = name;
        this.world = world;
        setupIslands();
    }

    private void setupIslands() {
        for (Team team : Team.values()) {
            islands.put(team, new Island(team));
        }
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }


    public void addGenerator(Generator generator) {
        this.generators.add(generator);
    }

    public void setBed(GenericLocation location) {
        islands.get(this.currentTeam).setBed(location);
    }

    public void setShop(GenericLocation location) {
        islands.get(this.currentTeam).setShop(location);
    }

    public void setUpgrades(GenericLocation location) {
        islands.get(this.currentTeam).setUpgrades(location);
    }

    public void setGenerator(Generator generator) {
        islands.get(this.currentTeam).setGenerator(generator);
    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public HashMap<Team, Island> getIslands() {
        return islands;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public ArrayList<Generator> getGenerators() {
        return generators;
    }
}

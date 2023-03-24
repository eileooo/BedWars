package com.leo.bedwars.arena.setup;

import com.leo.bedwars.arena.GenericLocation;
import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.arena.generator.Generator;
import com.leo.bedwars.arena.generator.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

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


    public List<String> generateLoreForIsland(Team team) {
        Island island = islands.get(team);
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add((island.getBed() != null ? ChatColor.GREEN : ChatColor.RED) + "Cama");
        lore.add((island.getGenerator() != null ? ChatColor.GREEN : ChatColor.RED) + "Gerador");
        lore.add((island.getShop() != null ? ChatColor.GREEN : ChatColor.RED) + "Aldeão comerciante");
        lore.add((island.getUpgrades() != null ? ChatColor.GREEN : ChatColor.RED) + "Aldeão de melhorias");
        lore.add(" ");

        return lore;
    }

    public void save(YamlConfiguration configuration, File file) {
        configuration.set(name + ".world", world);

        List<Generator> diamond = generators.stream().filter(gen -> gen.getType() == GeneratorType.DIAMOND).toList();
        List<Generator> emerald = generators.stream().filter(gen -> gen.getType() == GeneratorType.EMERALD).toList();

        for (int i = 0; i < diamond.size(); i++) {
            diamond.get(i).getLocation().save(name + ".generators.diamond." + i, configuration);
        }

        for (int i = 0; i < emerald.size(); i++) {
            diamond.get(i).getLocation().save(name + ".generators.emerald." + i, configuration);
        }

        for (Island island : islands.values()) {
            island.getGenerator().getLocation().save(name + ".islands." + island.getTeam().toString() + ".generator", configuration);
            island.getBed().save(name + ".islands." + island.getTeam().toString() + ".bed", configuration);
            island.getShop().save(name + ".islands." + island.getTeam().toString() + ".shop", configuration);
            island.getUpgrades().save(name + ".islands." + island.getTeam().toString() + ".upgrades", configuration);
        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

package com.leo.bedwars.arena;

import com.leo.bedwars.arena.generator.Generator;
import org.bukkit.World;

import java.util.ArrayList;

public class Arena {

    String name;
    String worldName;
    World world;
    ArrayList<Island> islands = new ArrayList<>();
    ArrayList<Generator> generators = new ArrayList<>();

    public Arena(String name, String world) {
        this.name = name;
        this.worldName = world;
    }

    public void addIsland(Island island) {
        this.islands.add(island);
    }
    public void addGenerator(Generator generator) { this.generators.add(generator);}

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorld(World world) {
        this.world = world;

        for (Generator generator : generators) {
            generator.getLocation().setWorld(world);
        }

        for (Island island : islands) {
            island.getBed().setWorld(world);
            island.getShop().setWorld(world);
            island.getUpgrades().setWorld(world);
        }


    }
}

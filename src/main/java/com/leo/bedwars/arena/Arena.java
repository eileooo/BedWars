package com.leo.bedwars.arena;

import com.leo.bedwars.arena.generator.Generator;

import java.util.ArrayList;

public class Arena {

    String name;
    String world;
    ArrayList<Island> islands = new ArrayList<>();
    ArrayList<Generator> generators = new ArrayList<>();

    public Arena(String name, String world) {
        this.name = name;
        this.world = world;
    }

    public void addIsland(Island island) {
        this.islands.add(island);
    }
    public void addGenerator(Generator generator) { this.generators.add(generator);}

    public String getName() {
        return name;
    }

}

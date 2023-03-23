package com.leo.bedwars.arena.setup;

import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.arena.generator.Generator;

import java.util.ArrayList;

public class SetupArena {

    String name;
    String world;
    ArrayList<Island> islands = new ArrayList<>();
    ArrayList<Generator> generators = new ArrayList<>();
    Team currentTeam = Team.RED;


    public SetupArena(String name, String world) {
        this.name = name;
        this.world = world;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public void save() {

    }

}

package com.leo.bedwars.arena;

import com.leo.bedwars.arena.generator.Generator;
import org.bukkit.Location;

public class Island {

    Team team;
    Generator generator;
    GenericLocation bed;
    GenericLocation shop;
    GenericLocation upgrades;

    public Island(Team team, Generator generator, GenericLocation bed, GenericLocation shop, GenericLocation upgrades) {
        this.team = team;
        this.generator = generator;
        this.bed = bed;
        this.shop = shop;
        this.upgrades = upgrades;
    }
}

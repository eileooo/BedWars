package com.leo.bedwars.game;

import com.leo.bedwars.arena.GenericLocation;
import com.leo.bedwars.game.generator.Generator;

public class Island {

    Team team;
    Generator generator;
    GenericLocation bed;
    GenericLocation shop;
    GenericLocation upgrades;

    public Island(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public GenericLocation getBed() {
        return bed;
    }

    public void setBed(GenericLocation bed) {
        this.bed = bed;
    }

    public GenericLocation getShop() {
        return shop;
    }

    public void setShop(GenericLocation shop) {
        this.shop = shop;
    }

    public GenericLocation getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(GenericLocation upgrades) {
        this.upgrades = upgrades;
    }

    public boolean isAllSet() {
        return (generator != null && bed != null && shop != null && upgrades != null);
    }


}

package com.leo.bedwars.arena;

import org.bukkit.Material;

public enum Team {
    Blue(Material.BLUE_WOOL, Material.BLUE_BED),
    Red(Material.RED_WOOL, Material.RED_BED),
    Green(Material.GREEN_WOOL, Material.GREEN_BED),
    Yellow(Material.YELLOW_WOOL, Material.YELLOW_BED),
    Purple(Material.PURPLE_WOOL, Material.PURPLE_BED),
    Cyan(Material.CYAN_WOOL, Material.CYAN_BED),
    Pink(Material.PINK_WOOL, Material.PINK_BED),
    Orange(Material.ORANGE_WOOL, Material.ORANGE_BED);

    Material wool;
    Material bed;

    Team(Material wool, Material bed) {
        this.wool = wool;
        this.bed = bed;
    }

}

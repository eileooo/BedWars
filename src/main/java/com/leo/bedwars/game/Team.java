package com.leo.bedwars.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Team {
    BLUE(Material.BLUE_WOOL, Material.BLUE_BED),
    RED(Material.RED_WOOL, Material.RED_BED),
    GREEN(Material.GREEN_WOOL, Material.GREEN_BED),
    YELLOW(Material.YELLOW_WOOL, Material.YELLOW_BED),
    PURPLE(Material.PURPLE_WOOL, Material.PURPLE_BED),
    CYAN(Material.CYAN_WOOL, Material.CYAN_BED),
    PINK(Material.PINK_WOOL, Material.PINK_BED),
    ORANGE(Material.ORANGE_WOOL, Material.ORANGE_BED);

    Material wool;
    Material bed;

    Team(Material wool, Material bed) {
        this.wool = wool;
        this.bed = bed;
    }

    public String translate() {
        switch (this) {
            case BLUE:
                return "Azul";
            case RED:
                return "Vermelho";
            case GREEN:
                return "Verde";
            case YELLOW:
                return "Amarelo";
            case PURPLE:
                return "Roxo";
            case CYAN:
                return "Ciano";
            case PINK:
                return "Rosa";
            default:
                return "Laranja";


        }

    }

    public ChatColor getColor() {
        switch (this) {
            case BLUE:
                return ChatColor.BLUE;
            case RED:
                return ChatColor.RED;
            case GREEN:
                return ChatColor.GREEN;
            case YELLOW:
                return ChatColor.YELLOW;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            case CYAN:
                return ChatColor.AQUA;
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            default:
                return ChatColor.GOLD;


        }
    }

    public String getColoredTranslate() {
        return getColor() + translate();
    }

    public Material getBed() {
        switch (this) {
            case BLUE:
                return Material.BLUE_BED;
            case RED:
                return Material.RED_BED;
            case GREEN:
                return Material.GREEN_BED;
            case YELLOW:
                return Material.YELLOW_BED;
            case PURPLE:
                return Material.PURPLE_BED;
            case CYAN:
                return Material.CYAN_BED;
            case PINK:
                return Material.PINK_BED;
            default:
                return Material.ORANGE_BED;


        }
    }

    public Material getWool() {
        switch (this) {
            case BLUE:
                return Material.BLUE_WOOL;
            case RED:
                return Material.RED_WOOL;
            case GREEN:
                return Material.GREEN_WOOL;
            case YELLOW:
                return Material.YELLOW_WOOL;
            case PURPLE:
                return Material.PURPLE_WOOL;
            case CYAN:
                return Material.CYAN_WOOL;
            case PINK:
                return Material.PINK_WOOL;
            default:
                return Material.ORANGE_WOOL;


        }
    }


}

package com.leo.bedwars.arena.generator;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GeneratorType {

    ISLAND(Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT)), DIAMOND(Arrays.asList(Material.DIAMOND)), EMERALD(Arrays.asList(Material.EMERALD));

    List<Material> item;

    GeneratorType(List<Material> item) {
        this.item = item;
    }

    public List<Material> getMaterial() {
        return this.item;
    }

}

package com.leo.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private final Material material;
    private int amount;
    private short damage;
    private String name;
    private List<String> lore;
    private boolean unbreakable;
    private boolean glow;
    private Enchantment enchantment;
    private int level;

    public ItemBuilder(Material material) {
        this.material = material;
        this.amount = 1;
        this.damage = 0;
        this.name = null;
        this.lore = new ArrayList<>();
        this.unbreakable = false;
        this.glow = false;
        this.enchantment = null;
        this.level = 0;
    }

    public ItemBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder withDamage(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder withDisplayName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder withUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder withGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(name);
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        if (glow) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        if (enchantment != null) {
            item.addUnsafeEnchantment(enchantment, level);
        }
        return item;
    }
}

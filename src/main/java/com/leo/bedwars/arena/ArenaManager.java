package com.leo.bedwars.arena;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.game.Island;
import com.leo.bedwars.game.generator.Generator;
import com.leo.bedwars.game.generator.GeneratorType;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    public ArrayList<Arena> arenaCache = new ArrayList<>();

    BedWars main;

    public ArenaManager(BedWars main) {
        this.main = main;

    }
    public void setupArena(Arena arena) {
        List<Island> islands = arena.getIslands();

        for (Generator generator : arena.getGenerators()) {
            Location location = generator.getLocation().asBukkitLocation();
            ArrayList<String> lines = new ArrayList<>();

            lines.add(ChatColor.YELLOW + "Gerador de " + (generator.getType() == GeneratorType.DIAMOND ? ChatColor.AQUA + "Diamante" : ChatColor.GREEN + "Esmeralda"));

            Hologram hologram = DHAPI.createHologram(generator.getIdentifier(), generator.getLocation().asBukkitLocation().add(0, 4, 0), lines);
            DHAPI.addHologramLine(hologram, (generator.getType() == GeneratorType.DIAMOND ? Material.DIAMOND : Material.EMERALD));
        }

        for (Island island : islands) {
            World world = island.getBed().world;

            island.getBed().asBukkitLocation().getBlock().setType(island.getTeam().getBed());
            setPart(island.getBed().asBukkitLocation().getBlock(), Bed.Part.HEAD);

            Villager shop = (Villager) world.spawnEntity(island.getShop().asBukkitLocation(), EntityType.VILLAGER);
            Villager upgrades = (Villager) world.spawnEntity(island.getUpgrades().asBukkitLocation(), EntityType.VILLAGER);

            shop.setProfession(Villager.Profession.SHEPHERD);
            shop.setAI(false);
            shop.setCustomName(ChatColor.GREEN + "Comerciante");
            shop.setCustomNameVisible(true);

            upgrades.setProfession(Villager.Profession.LIBRARIAN);
            upgrades.setAI(false);
            upgrades.setCustomName(ChatColor.AQUA + "Melhorias");
            upgrades.setCustomNameVisible(true);

        }

    }

    public void cacheArena(Arena arena) {
        this.arenaCache.add(arena);

        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Arena " + ChatColor.GREEN + arena.getName() + ChatColor.YELLOW + " foi cacheada.");
    }

    public void setPart(Block block, Bed.Part part) {
        Bed bed = (Bed) block.getBlockData();
        bed.setPart(part);
        block.setBlockData(bed);
    }



}

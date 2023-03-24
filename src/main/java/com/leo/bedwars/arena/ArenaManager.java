package com.leo.bedwars.arena;

import com.leo.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;

import java.sql.Array;
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

        for (Island island : islands) {
            island.getBed().asBukkitLocation().getBlock().setBlockData(island.getTeam().getBed().createBlockData(data -> ((Bed) data).setPart(Bed.Part.HEAD)), false);
        }

    }

    public void cacheArena(Arena arena) {
        this.arenaCache.add(arena);
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Arena " + ChatColor.GREEN + arena.getName() + ChatColor.YELLOW + " foi cacheada.");
    }




}

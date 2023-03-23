package com.leo.bedwars.arena;

import com.leo.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Array;
import java.util.ArrayList;

public class ArenaManager {

    public ArrayList<Arena> arenaCache = new ArrayList<>();

    BedWars main;

    public ArenaManager(BedWars main) {
        this.main = main;

    }

    public void cacheArena(Arena arena) {
        this.arenaCache.add(arena);
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Arena " + ChatColor.GREEN + arena.getName() + ChatColor.YELLOW + " foi cacheada.");
    }


}

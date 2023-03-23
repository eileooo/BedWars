package com.leo.bedwars.commands;

import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    ArenaManager arenaManager;

    public TestCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        for (Arena arena : arenaManager.arenaCache) {
            player.sendMessage(arena.toString());
        }


        return false;
    }
}

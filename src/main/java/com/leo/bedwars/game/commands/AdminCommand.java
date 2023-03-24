package com.leo.bedwars.game.commands;

import com.leo.bedwars.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    GameManager manager;

    public AdminCommand(GameManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        manager.clearGames();
        player.sendMessage("Jogos resetados.");
        return true;
    }
}

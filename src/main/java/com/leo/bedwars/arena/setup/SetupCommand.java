package com.leo.bedwars.arena.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetupCommand implements CommandExecutor {

    SetupManager manager;

    public SetupCommand(SetupManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.setup")) {
            player.sendMessage(ChatColor.RED + "Você não tem permissão para exeuctar este comando!");
            return false;
        }

        if (args.length != 3) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "Está faltando argumentos. Comandos disponíveis:");
            player.sendMessage(ChatColor.GRAY + "* " + ChatColor.RED + "/arena configurar [nome da arena] [arquivo do mundo]");
            player.sendMessage("");
            return false;
        }

        if (!args[0].equalsIgnoreCase("configurar")) return false; // more commands later

        manager.joinSetup(player, args[1], args[2]);
        return false;
    }
}

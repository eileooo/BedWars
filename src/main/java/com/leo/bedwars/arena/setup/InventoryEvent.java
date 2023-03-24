package com.leo.bedwars.arena.setup;

import com.leo.bedwars.ItemBuilder;
import com.leo.bedwars.arena.GenericLocation;
import com.leo.bedwars.arena.Team;
import com.leo.bedwars.arena.generator.Generator;
import com.leo.bedwars.arena.generator.GeneratorType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class InventoryEvent implements Listener {

    SetupManager manager;

    public InventoryEvent(SetupManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onHotBarClick(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if (event.getHand() == EquipmentSlot.OFF_HAND) return;
            if (!manager.isPlayerOnSetup(player)) return;
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) event.setCancelled(true);

            SetupArena arena = manager.getSetupArena(player);
            Team team = arena.getCurrentTeam();
            Inventory inventory = event.getPlayer().getInventory();
            Material item = event.getMaterial();
            Location location = player.getLocation();
            GenericLocation genericLocation = new GenericLocation().fromBukkitLocation(location);

            if (item == arena.currentTeam.getWool()) {
                Inventory teamInventory = Bukkit.createInventory(null, 36, "Selecionar time");
                int i = 11;
                for (Team teamValue : Team.values()) {
                    ItemStack teamItem = new ItemBuilder(teamValue.getWool()).withDisplayName(teamValue.getColoredTranslate()).withLore(manager.generateLoreForIsland(arena, teamValue)).build();
                    teamInventory.setItem(i, teamItem);
                    i++;
                    if (i == 16) {
                        i = 21;
                    }
                }

                player.openInventory(teamInventory);
            }

            if (item == Material.EMERALD) {
                arena.addGenerator(new Generator(GeneratorType.EMERALD, genericLocation));
                player.sendMessage(ChatColor.GREEN + "Gerador de esmeralda adicionado!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == Material.DIAMOND) {
                arena.addGenerator(new Generator(GeneratorType.DIAMOND, genericLocation));
                player.sendMessage(ChatColor.GREEN + "Gerador de diamante adicionado!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == Material.IRON_INGOT) {
                arena.setGenerator(new Generator(GeneratorType.ISLAND, genericLocation));
                player.sendMessage(ChatColor.GREEN + "Gerador do time " + team.getColor() + "" + team.translate() + ChatColor.GREEN + " setado!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == team.getBed()) {
                arena.setBed(genericLocation);
                player.sendMessage(ChatColor.GREEN + "Cama do time " + team.getColor() + "" + team.translate() + ChatColor.GREEN + " setada!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == Material.SHEARS) {
                Bukkit.getConsoleSender().sendMessage("a");
                arena.setShop(genericLocation);
                player.sendMessage(ChatColor.GREEN + "Aldeão comerciante do time " + team.getColor() + "" + team.translate() + ChatColor.GREEN + " setado!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == Material.TNT) {
                arena.setUpgrades(genericLocation);
                player.sendMessage(ChatColor.GREEN + "Aldeão de melhorias do time " + team.getColor() + "" + team.translate() + ChatColor.GREEN + " setado!");
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 20, 20);
            }

            if (item == Material.LIME_STAINED_GLASS_PANE) {
                manager.save(player);
            }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (manager.isPlayerOnSetup(player)) event.setCancelled(true);

        Team team = getTeamFromSlot(event.getSlot());
        if (team != null && team != manager.getSetupArena(player).getCurrentTeam()) {
            manager.changeCurrentTeam(player, team);
            manager.setItems(player);
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Time " + team.getColoredTranslate() + ChatColor.YELLOW + " selecionado.");
        }

    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (manager.isPlayerOnSetup(event.getPlayer())) event.setCancelled(true);
    }

    Team getTeamFromSlot(int slot) {
        switch (slot) {
            case 11:
                return Team.BLUE;
            case 12:
                return Team.RED;
            case 13:
                return Team.GREEN;
            case 14:
                return Team.YELLOW;
            case 15:
                return Team.PURPLE;
            case 21:
                return Team.CYAN;
            case 22:
                return Team.PINK;
            case 23:
                return Team.ORANGE;
            default:
                return null;
        }
    }


}

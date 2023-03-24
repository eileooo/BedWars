package com.leo.bedwars.arena.setup;

import com.leo.bedwars.BedWars;
import com.leo.bedwars.ItemBuilder;
import com.leo.bedwars.arena.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SetupManager {

    BedWars main;

    public SetupManager(BedWars main) {
        this.main = main;
    }

    HashMap<Player, SetupArena> setupCache = new HashMap<Player, SetupArena>();

    public void joinSetup(Player player, String name, String arenaWorld) {
        WorldCreator creator = new WorldCreator(arenaWorld);
        World world = Bukkit.createWorld(creator);

        player.sendMessage(ChatColor.GREEN + "Carregando mundo...");

        if (world == null) {
            player.sendMessage(ChatColor.RED + "Erro ao carregar arena.");
            return;
        }

        SetupArena arena = new SetupArena(name, arenaWorld);
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(new Location(world, 0, 75, 0));

        setupCache.put(player, arena);
        setItems(player);
    }

    public SetupArena getSetupArena(Player player) {
        return setupCache.get(player);
    }

    public void setItems(Player player) {
        //  wool, emerald, diamond, iron, bed, shears, tnt, lime glass pane
        SetupArena arena = getSetupArena(player);
        Team currentTeam = arena.getCurrentTeam();
        String coloredTeam = currentTeam.getColor() + currentTeam.translate();


        ItemStack team = new ItemBuilder(currentTeam.getWool()).withDisplayName(ChatColor.WHITE + "Time: " + coloredTeam).build();
        ItemStack emerald = new ItemBuilder(Material.EMERALD).withDisplayName(ChatColor.GREEN + "Gerador de esmeralda").build();
        ItemStack diamond = new ItemBuilder(Material.DIAMOND).withDisplayName(ChatColor.AQUA + "Gerador de diamante").build();
        ItemStack iron = new ItemBuilder(Material.IRON_INGOT).withDisplayName(ChatColor.WHITE + "Gerador do time " + coloredTeam).build();
        ItemStack bed = new ItemBuilder(currentTeam.getBed()).withDisplayName(ChatColor.WHITE + "Cama").build();
        ItemStack shop = new ItemBuilder(Material.SHEARS).withDisplayName(ChatColor.GREEN + "Aldeão comerciante").build();
        ItemStack upgrades = new ItemBuilder(Material.TNT).withDisplayName(ChatColor.GOLD + "Aldeão de melhorias").build();
        ItemStack save = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Salvar arena").build();

        player.getInventory().clear();

        Inventory inventory = player.getInventory();
        inventory.setItem(0, team);
        inventory.setItem(1, emerald);
        inventory.setItem(2, diamond);
        inventory.setItem(3, iron);
        inventory.setItem(4, bed);
        inventory.setItem(5, shop);
        inventory.setItem(6, upgrades);
        inventory.setItem(8, save);


    }

    public void save(Player player) {
        getSetupArena(player).save(main.getArenaConfiguration(), main.getArenaFile());
        setupCache.remove(player);
    }

    public void changeCurrentTeam(Player player, Team team) {
        this.setupCache.get(player).setCurrentTeam(team);
    }

    public boolean isPlayerOnSetup(Player player) {
        return this.setupCache.containsKey(player);
    }
}

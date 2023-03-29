package com.leo.bedwars.game.tasks;

import com.leo.bedwars.game.Game;
import com.leo.bedwars.game.GameManager;
import com.leo.bedwars.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class StartingGameTask extends BukkitRunnable {

    Game game;
    GameManager manager;
    int seconds;

    public StartingGameTask(GameManager manager, Game game, int duration) {
        this.game = game;
        this.manager = manager;
        seconds = duration;
    }

    @Override
    public void run() {
        if (seconds == 0) {
            manager.setGameState(game, GameState.RUNNING);
            this.cancel();
            return;
        }

        if (game.getState() != GameState.STARTING) {
            game.broadcast(ChatColor.RED + "Temporizador cancelado. Isso pode ser um erro.");
            this.cancel();
            return;
        }

        game.broadcast(ChatColor.YELLOW + "Iniciando em " + ChatColor.GRAY + seconds + ChatColor.YELLOW + " segundo" + (seconds == 1 ? "" : "s"));

        seconds--;
    }
}

package com.leo.bedwars.game;

import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.scoreboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    String id;
    GameState state = GameState.STARTING;
    HashMap<Player, Island> players = new HashMap<Player, Island>();
    int minPlayers = 2;
    HashMap<Player, FastBoard> boards = new HashMap<>();
    Arena arena;

    public Game(String id, Arena arena) {
        this.id = id;
        this.arena = arena;
    }

    public String getId() {
        return id;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public Arena getArena() {
        return arena;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Island getAvailableIsland() {
        ArrayList<Island> result = new ArrayList<>(getArena().getIslands());

        // arena -> islands

        for (Island island : players.values()) {
            result.removeIf(isl -> isl.getTeam() == island.getTeam());
        }
        return result.get(0);
    }

    public void broadcast(String message) {
        for (Player player : players.keySet()) {
            player.sendMessage(message);
        }
    }
}

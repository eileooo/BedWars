package com.leo.bedwars.game;

import com.leo.bedwars.arena.Arena;
import com.leo.bedwars.arena.Island;
import com.leo.bedwars.arena.Team;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Game {

    String id;
    GameState state = GameState.STARTING;
    HashMap<Player, Island> players = new HashMap<Player, Island>();
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
}

package com.leo.bedwars.game.generator;

import com.leo.bedwars.arena.GenericLocation;

public class Generator {

    GeneratorType type;
    GenericLocation location;
    String identifier;

    public Generator(GeneratorType type, GenericLocation location, String identifier) {
        this.type = type;
        this.location = location;
        this.identifier = type.toString() + "_" + identifier;
    }

    public GeneratorType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public GenericLocation getLocation() {
        return location;
    }
}

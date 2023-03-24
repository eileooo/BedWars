package com.leo.bedwars.arena.generator;

import com.leo.bedwars.arena.GenericLocation;

public class Generator {

    GeneratorType type;
    GenericLocation location;

    public Generator(GeneratorType type, GenericLocation location) {
        this.type = type;
        this.location = location;
    }

    public GeneratorType getType() {
        return type;
    }

    public GenericLocation getLocation() {
        return location;
    }
}

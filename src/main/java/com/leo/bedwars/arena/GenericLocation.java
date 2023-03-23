package com.leo.bedwars.arena;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class GenericLocation {

    double x;
    double y;
    double z;
    double yaw;
    double pitch;
    World world;

    public double getX() {
        return x;
    }

    public GenericLocation setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public GenericLocation setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public GenericLocation setZ(double z) {
        this.z = z;
        return this;
    }

    public double getYaw() {
        return yaw;
    }

    public GenericLocation setYaw(double yaw) {
        this.yaw = yaw;
        return this;
    }

    public double getPitch() {
        return pitch;
    }

    public GenericLocation setPitch(double pitch) {
        this.pitch = pitch;
        return this;
    }

    public World getWorld() {
        return world;
    }

    public GenericLocation setWorld(World world) {
        this.world = world;
        return this;
    }

    public GenericLocation fromConfigurationSection(ConfigurationSection section) {
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        double yaw = section.getDouble("yaw");
        double pitch = section.getDouble("pitch");

        return this.setX(x).setY(y).setZ(z).setYaw(yaw).setPitch(pitch);
    }

}

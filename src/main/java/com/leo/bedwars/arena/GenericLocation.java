package com.leo.bedwars.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

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

    public void printLocation() {
        Bukkit.getConsoleSender().sendMessage(getX() + " " +getY() + " " + getZ());
    }

    public GenericLocation fromConfigurationSection(ConfigurationSection section, String path) {
        double x = section.getDouble(path + ".x");
        double y = section.getDouble(path + ".y");
        double z = section.getDouble(path + ".z");
        double yaw = section.getDouble(path + ".yaw");
        double pitch = section.getDouble(path + "pitch");

        return this.setX(x).setY(y).setZ(z).setYaw(yaw).setPitch(pitch);
    }

    public Location asBukkitLocation() {
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }


    public GenericLocation fromBukkitLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        return this;
    }

    public void save(String key, YamlConfiguration configuration) {
        configuration.set(key + ".x", this.x);
        configuration.set(key + ".y", this.y);
        configuration.set(key + ".z", this.z);
        configuration.set(key + ".yaw", this.yaw);
        configuration.set(key + ".pitch", this.pitch);
    }

}

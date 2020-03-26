package com.blank038.fixer;

import com.blank038.fixer.model.EntityListener;
import com.blank038.fixer.model.ReforgeListener;
import com.mc9y.pokemonapi.PokemonAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * This plugin is created for Minecraft-Bukkit related loopholes.
 * You can use this plugin in version 1.12.2+.
 *
 * @Author Blank038, Laotouy
 */
public class Fixer extends JavaPlugin {
    private static Fixer fixer;

    public static Fixer getInstance() {
        return fixer;
    }

    public static FileConfiguration getConfiguration() {
        return fixer.getConfig();
    }

    @Override
    public void onEnable() {
        fixer = this;
        loadConfig();
        // 注册监听
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        if (PokemonAPI.getInstance().isPixelmon()) {
            Bukkit.getPluginManager().registerEvents(new ReforgeListener(), this);
        }
    }

    public void loadConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!file.exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
    }
}
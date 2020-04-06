package com.blank038.fixer;

import com.blank038.fixer.data.CheckList;
import com.blank038.fixer.model.harvestcraft.PamsListener;
import com.blank038.fixer.model.worldguard.BlockListener;
import com.blank038.fixer.model.minecraft.EntityListener;
import com.blank038.fixer.model.pixelmon.ReforgeListener;
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
    private static CheckList checkList;

    public static Fixer getInstance() {
        return fixer;
    }

    public static CheckList getCheckList() {
        return checkList;
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
        // 判断是否含有 Pixelmon 模组
        if (PokemonAPI.getInstance().isPixelmon()) {
            Bukkit.getPluginManager().registerEvents(new ReforgeListener(), this);
        }
        // 判断是否有潘马斯模组
        if (PokemonAPI.getInstance().hasClass("com.pam.harvestcraft.HarvestCraft")) {
            Bukkit.getPluginManager().registerEvents(new PamsListener(), this);
        }
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
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
        checkList = new CheckList();
    }
}
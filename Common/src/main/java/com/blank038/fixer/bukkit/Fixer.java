package com.blank038.fixer.bukkit;

import com.blank038.fixer.bukkit.model.log4j.Log4jBukkitModel;
import com.blank038.fixer.bungee.command.FixerCommander;
import com.blank038.fixer.bungee.data.CheckList;
import com.blank038.fixer.bukkit.model.armourers.ArmourersListener;
import com.blank038.fixer.bukkit.model.crasher.CrasherPacket;
import com.blank038.fixer.bukkit.model.harvestcraft.PamsListener;
import com.blank038.fixer.bukkit.model.minecraft.BlockListener;
import com.blank038.fixer.bukkit.model.multiverse.CommandListener;
import com.blank038.fixer.bukkit.model.sakura.SakuraListener;
import com.blank038.fixer.bukkit.model.slimefun.InventoryListener;
import com.blank038.fixer.bukkit.model.worldguard.WorldGuardListener;
import com.mc9y.blank038api.Blank038API;
import com.mc9y.pokemonapi.api.enums.EnumPixelmon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This plugin is created for Minecraft-Bukkit related loopholes.
 * You can use this plugin in version 1.12.2+.
 *
 * @author Blank038, Laotouy
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
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        // 判断是否含有 Pixelmon 模组
        EnumPixelmon pixelmon = EnumPixelmon.PIXELMON_REFORGED;
        if (Blank038API.getPokemonAPI().getEnumPixelmon() == pixelmon) {
            int versionId = Integer.parseInt(Blank038API.getPokemonAPI().getVersion(pixelmon).replace(".", ""));
            try {
                Class<?> aClass = Class.forName("com.blank038.fixer.model.pixelmon." + (versionId >= 820 ? "NewReforgedListener" : "ReforgedListener"));
                Listener listener = (Listener) aClass.newInstance();
                Bukkit.getPluginManager().registerEvents(listener, this);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 注册通用设定
        Bukkit.getPluginManager().registerEvents(new WorldGuardListener(), this);
        // 判断是否有潘马斯模组
        if (Blank038API.getPokemonAPI().hasClass("com.pam.harvestcraft.HarvestCraft")) {
            Bukkit.getPluginManager().registerEvents(new PamsListener(), this);
        }
        // 判断是否有 Sakura 模组-
        if (Blank038API.getPokemonAPI().hasClass("cn.mcmod.sakura.SakuraMain")) {
            Bukkit.getPluginManager().registerEvents(new SakuraListener(), this);
        }
        // 判断是否加载了时装模组
        if (Blank038API.getPokemonAPI().hasClass("moe.plushie.armourers_workshop.ArmourersWorkshop")) {
            Bukkit.getPluginManager().registerEvents(new ArmourersListener(), this);
        }
        // 判断是否加载了 WorldGuard 插件
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            Bukkit.getPluginManager().registerEvents(new WorldGuardListener(), this);
        }
        // 判断是否加载了 Multiverse-Core 插件
        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null) {
            Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        }
        // 判断是否加载了 Slimefun 插件
        if (Bukkit.getPluginManager().getPlugin("Slimefun") != null) {
            Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        }
        // 加载防止 CrasherServer 卡服崩服模块
        new CrasherPacket();
        // 加载 log4j 修复模块
        if (Blank038API.getPokemonAPI().hasClass("org.apache.logging.log4j.core.filter.AbstractFilter")) {
            new Log4jBukkitModel();
        }
        super.getCommand("fixer").setExecutor(new FixerCommander());
    }

    public void loadConfig() {
        this.getDataFolder().mkdir();
        this.saveDefaultConfig();
        this.reloadConfig();
        checkList = new CheckList();
    }
}
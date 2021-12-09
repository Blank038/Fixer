package com.blank038.fixer.bukkit.model.log4j;


import com.blank038.fixer.bukkit.Fixer;
import com.blank038.fixer.model.log4j.Log4jModel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * 修复 Log4j2 远程执行漏洞
 * Bukkit 模块
 *
 * @author Blank038
 * @since 2021-12-10
 */
public class Log4jBukkitModel extends Log4jModel
        implements Listener {

    public Log4jBukkitModel() {
        Bukkit.getPluginManager().registerEvents(this, Fixer.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (PATTERN.matcher(event.getMessage()).find()) {
            event.setCancelled(true);
        }
    }
}

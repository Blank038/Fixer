package com.blank038.fixer.bukkit.model.log4j2;


import com.blank038.fixer.model.log4j.Log4j2Model;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * 修复 Log4j2 远程执行漏洞
 * Bukkit 模块
 *
 * @author Blank038
 * @since 2021-12-10
 */
public class Log4j2BukkitModel extends Log4j2Model
        implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (PATTERN.matcher(event.getMessage()).find()) {
            event.setCancelled(true);
        }
    }
}

package com.blank038.fixer.bukkit.model.minecraft;

import com.blank038.fixer.bukkit.Fixer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author Blank038
 */
public class FakeBungeeCord implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!event.getHostname().matches("[\\da-zA-Z.]*:\\d{1,5}")) {
            event.setKickMessage(ChatColor.translateAlternateColorCodes('&',
                    Fixer.getConfiguration().getString("message.common.fake_proxy.notify")));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}

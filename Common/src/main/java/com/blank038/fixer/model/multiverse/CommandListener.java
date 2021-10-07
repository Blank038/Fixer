package com.blank038.fixer.model.multiverse;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Blank038
 * @since 2021-10-04
 */
public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPerformCommand(PlayerCommandPreprocessEvent event) {
        String[] split = event.getMessage().split(" ");
        if (split.length > 1 && split[1].matches("\\^(.*)\\^")) {
            event.setCancelled(true);
        }
    }
}

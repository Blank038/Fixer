package com.blank038.fixer.model.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

/**
 * @author Blank038
 */
public class EntityListener implements Listener {

    /**
     * 用于防止载具可撞毁展示框
     */
    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getCause() == HangingBreakEvent.RemoveCause.PHYSICS) {
            event.setCancelled(true);
        }
    }


}
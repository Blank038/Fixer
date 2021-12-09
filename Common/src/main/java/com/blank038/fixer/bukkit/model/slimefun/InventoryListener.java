package com.blank038.fixer.bukkit.model.slimefun;

import com.blank038.fixer.bukkit.Fixer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import java.util.stream.Stream;

/**
 *
 * @author Blank038
 * @since 2021-10-21
 */
public class InventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemMove(InventoryMoveItemEvent event) {
        if (!Fixer.getConfiguration().getBoolean("message.slimefun.block_placer.enable")) {
            return;
        }
        boolean contains = Stream.of(event.getSource().getTitle().replace("§", "&"),
                        event.getDestination().getTitle().replace("§", "&"))
                .anyMatch(Fixer.getConfiguration().getStringList("message.slimefun.block_placer.checks")::contains);
        if (contains) {
            event.setCancelled(true);
        }
    }
}

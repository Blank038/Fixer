package com.blank038.fixer.bukkit.model.worldguard;

import com.blank038.fixer.bukkit.Fixer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Blank038
 */
public class WorldGuardListener implements Listener {

    /**
     * 修复 WorldGuard 无视权限放置方块
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Fixer.getConfiguration().getBoolean("message.worldguard.place.enable")) {
            Player player = event.getPlayer();
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack hand = event.getItemInHand();
            if ((mainHand != null && mainHand.getType() != Material.AIR) && !mainHand.isSimilar(hand)) {
                event.setCancelled(true);
                player.sendMessage(Fixer.getConfiguration().getString("message.worldguard.place.deny")
                        .replace("&", "§"));
            }
        }
    }
}
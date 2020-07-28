package com.blank038.fixer.model.sakura;

import com.blank038.fixer.Fixer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SakuraListener implements Listener {

    /**
     * 防止 Sakura 催熟刷物品
     *
     * @author Blank038
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Fixer.getConfiguration().getBoolean("message.sakura.grow.enable")) {
            Block block = event.getClickedBlock();
            if (Fixer.getConfiguration().getStringList("message.sakura.grow.list").contains(block.getType().name())) {
                Block top = block.getLocation().clone().add(0, 1, 0).getBlock();
                if (top != null && (Fixer.getCheckList().sakuraGrowList.contains("all") || Fixer.getCheckList().sakuraGrowList.contains(top.getType().name()))) {
                    Player player = event.getPlayer();
                    if (isDenyItem(player.getInventory().getItemInMainHand()) || isDenyItem(player.getInventory().getItemInOffHand())) {
                        event.setCancelled(true);
                        if (Fixer.getConfiguration().getBoolean("message.sakura.grow.break")) {
                            BlockBreakEvent e = new BlockBreakEvent(top, player);
                            if (e.isCancelled()) {
                                return;
                            }
                            top.breakNaturally();
                        } else player.sendMessage(Fixer.getConfiguration().getString("message.sakura.grow.deny")
                                .replace("&", "§"));
                    }
                }
            }
        }
    }

    private boolean isDenyItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }
}

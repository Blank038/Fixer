package com.blank038.fixer.model.armourers;

import com.blank038.fixer.Fixer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

/**
 * @author Blank038
 * @since 2021-04-25
 */
public class ArmourersListener implements Listener {

    /**
     * 修复时装模组无视权限无限放置方块
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Fixer.getConfiguration().getBoolean("message.armourers.place.enable")
                || event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null || !itemStack.getType().name().startsWith("ARMOURERS_WORKSHOP")) {
            return;
        }
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTagCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        assert (nbtTagCompound != null);
        if (nbtTagCompound.hasKey("armourersWorkshop") && (nbtTagCompound.getCompound("armourersWorkshop")).hasKey("identifier")
                && "armourers:block".equals(nbtTagCompound.getCompound("identifier").getString("skinType"))) {
            Block block = event.getClickedBlock();
            BlockPlaceEvent e = new BlockPlaceEvent(block, block.getState(), block, itemStack, event.getPlayer(),
                    true, event.getPlayer().getPlayer().getMainHand() == MainHand.LEFT ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND);
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }
}

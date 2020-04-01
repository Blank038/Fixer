package com.blank038.fixer.model.pixelmon;

import com.blank038.fixer.Fixer;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * 宝可梦相关内容修复类
 *
 * @author Laotouy, Blank038
 */
public class ReforgeListener implements Listener {

    /**
     * 修复 Pixelmon 克隆机损坏漏洞
     *
     * @作者 Laotouy
     */
    @EventHandler
    public void onPlayerInteract0(PlayerInteractEvent e) {
        if (e.hasBlock() && e.getClickedBlock().getType().toString().equalsIgnoreCase("PIXELMON_CLONING_MACHINE") && Fixer.getConfiguration().getBoolean("message.pixelmon.clone.enable")) {
            org.bukkit.block.Block bk = e.getClickedBlock();
            for (WorldServer worldServer : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
                if (worldServer.getWorldInfo().getWorldName().equalsIgnoreCase(e.getPlayer().getWorld().getName())) {
                    World world = worldServer;
                    TileEntityCloningMachine tile = BlockHelper.getTileEntity(TileEntityCloningMachine.class, world, new BlockPos(bk.getX(), bk.getY(), bk.getZ()));
                    try {
                        boolean b = tile.isBroken;
                        if (b) {
                            e.setCancelled(true);
                            e.getPlayer().sendMessage(Fixer.getConfiguration().getString("message.pixelmon.clone.broken")
                                    .replace("&", "§"));
                        } else {
                            e.getPlayer().sendMessage(Fixer.getConfiguration().getString("message.pixelmon.clone.normal")
                                    .replace("&", "§"));
                        }
                    } catch (NullPointerException ex) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(Fixer.getConfiguration().getString("message.pixelmon.clone.error")
                                .replace("&", "§"));
                    }
                    break;
                }
            }
        }
    }

    /**
     * 防止宝可梦树果催熟
     *
     * @作者 Blank038
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Fixer.getConfiguration().getBoolean("message.pixelmon.apricorn.enable")) {
            Block block = event.getClickedBlock();
            if (block.getType().name().endsWith("APRICORN_TREE")) {
                Block top = block.getLocation().clone().add(0, 1, 0).getBlock();
                if (top != null && (Fixer.getCheckList().apricornList.contains("all") || Fixer.getCheckList().apricornList.contains(top.getType().name()))) {
                    Player player = event.getPlayer();
                    if (isDenyItem(player.getInventory().getItemInMainHand()) || isDenyItem(player.getInventory().getItemInOffHand())) {
                        event.setCancelled(true);
                        player.sendMessage(Fixer.getConfiguration().getString("message.pixelmon.apricorn.deny")
                                .replace("&", "§"));
                    }
                }
            }
        }
    }

    private boolean isDenyItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        return true;
    }
}
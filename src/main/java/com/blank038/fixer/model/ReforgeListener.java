package com.blank038.fixer.model;

import com.blank038.fixer.Fixer;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
        if (e.hasBlock() && e.getClickedBlock().getType().toString().equalsIgnoreCase("PIXELMON_CLONING_MACHINE")) {
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
}

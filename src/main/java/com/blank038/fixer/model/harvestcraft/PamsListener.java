package com.blank038.fixer.model.harvestcraft;

import com.blank038.fixer.Fixer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用于修复潘马斯机器复制物品的问题
 *
 * @author Blank038
 */
public class PamsListener implements Listener {
    private HashMap<Location, List<String>> guis = new HashMap<>();
    private HashMap<String, Location> players = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Fixer.getConfiguration().getBoolean("message.pams.machinery.enable")) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            Location location = block.getLocation();
            if (Fixer.getCheckList().machineryList.contains(block.getType().name())) {
                if (guis.containsKey(location)) {
                    if (!guis.get(location).contains(player.getName())) {
                        guis.get(location).add(player.getName());
                    }
                    if (players.containsKey(player.getName())) {
                        this.players.replace(player.getName(), location);
                    } else {
                        this.players.put(player.getName(), location);
                    }
                } else {
                    List<String> players = new ArrayList<>();
                    players.add(player.getName());
                    this.players.put(player.getName(), location);
                    guis.put(location, players);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (players.containsKey(player.getName())) {
            Location location = players.get(player.getName());
            if (guis.containsKey(location)) {
                List<String> list = new ArrayList<>(guis.get(location));
                list.remove(player.getName());
                // 判断是否为空, 是则移除
                if (list.isEmpty()) {
                    guis.remove(location);
                } else {
                    guis.replace(location, list);
                }
            }
            players.remove(player.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (guis.containsKey(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Fixer.getConfiguration().getString("message.pams.machinery.deny")
                    .replace("&", "§"));
        }
    }
}
package com.blank038.fixer.bukkit.model.minecraft;

import com.blank038.fixer.bukkit.Fixer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Blank038
 * @since 2021-05-31
 */
public class BlockListener implements Listener {
    private final Fixer INSTANCE;

    public BlockListener() {
        this.INSTANCE = Fixer.getInstance();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE &&
                Fixer.getConfiguration().getBoolean("message.common.interact_limit.enable")) {
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
                if ((action == Action.RIGHT_CLICK_BLOCK ? Fixer.getCheckList().rightCLickLimit : Fixer.getCheckList().leftClickLimit)
                        .contains(event.getClickedBlock().getType().name())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Fixer.getConfiguration().getString("message.common.interact_limit.notify")
                            .replace("&", "§"));
                }
            }
        }
    }
}

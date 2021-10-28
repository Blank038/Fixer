package com.blank038.fixer.model.crasher.protocol;

import com.blank038.fixer.Fixer;
import com.blank038.fixer.model.crasher.CrasherPacket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * @author Blank038
 * @since 2021-10-28
 */
public abstract class AbsorbListenModel implements Listener {
    protected final HashMap<String, CrasherPacket.CrasherData> CRASHER_MAP = new HashMap<>();
    protected final Fixer INSTANCE;

    public AbsorbListenModel() {
        this.INSTANCE = Fixer.getInstance();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.CRASHER_MAP.remove(event.getPlayer().getName());
    }
}

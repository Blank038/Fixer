package com.blank038.fixer.model.crasher;

import com.blank038.fixer.Fixer;
import com.blank038.fixer.model.crasher.protocol.PacketListenerApiModel;
import com.blank038.fixer.model.crasher.protocol.ProtocolLibModel;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * @author Blank038
 * @since 2021-10-27
 */
public class CrasherPacket implements Listener {
    private final Fixer INSTANCE;

    public CrasherPacket() {
        this.INSTANCE = Fixer.getInstance();
        // 判断服务端有什么插件
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            Bukkit.getPluginManager().registerEvents(new ProtocolLibModel(), this.INSTANCE);
        } else if (Bukkit.getPluginManager().getPlugin("PacketListenerApi") != null) {
            Bukkit.getPluginManager().registerEvents(new PacketListenerApiModel(), this.INSTANCE);
        }
    }

    public static class CrasherData {
        private long latestTime;
        private int blockDig = 1, tabComplete = 1;

        public boolean checkStatus() {
            if (System.currentTimeMillis() - latestTime >= 1000L) {
                latestTime = System.currentTimeMillis();
                blockDig = tabComplete = 0;
            }
            // 检测是否嗝屁
            return blockDig >= Fixer.getConfiguration().getInt("message.crash_server.block_dig")
                    || tabComplete >= Fixer.getConfiguration().getInt("message.crash_server.tab_complete");
        }

        public boolean addBlockDig() {
            this.blockDig++;
            return this.checkStatus();
        }

        public boolean addTabComplete() {
            this.tabComplete++;
            return this.checkStatus();
        }
    }
}

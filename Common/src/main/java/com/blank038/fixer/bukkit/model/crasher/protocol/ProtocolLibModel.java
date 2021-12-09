package com.blank038.fixer.bukkit.model.crasher.protocol;

import com.blank038.fixer.bukkit.Fixer;
import com.blank038.fixer.bukkit.model.crasher.CrasherPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Blank038
 * @since 2021-10-28
 */
public class ProtocolLibModel extends AbsorbListenModel {

    public ProtocolLibModel() {
        super();
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.INSTANCE, ListenerPriority.HIGHEST,
                PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (!ProtocolLibModel.this.INSTANCE.getConfig().getBoolean("message.crash_server.enable")) {
                    return;
                }
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                if (!ProtocolLibModel.this.CRASHER_MAP.containsKey(player.getName())) {
                    ProtocolLibModel.this.CRASHER_MAP.put(player.getName(), new CrasherPacket.CrasherData());
                }
                CrasherPacket.CrasherData data = ProtocolLibModel.this.CRASHER_MAP.get(player.getName());
                if ((event.getPacketType() == PacketType.Play.Client.BLOCK_DIG && data.addBlockDig())
                        || (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE && data.addTabComplete())) {
                    event.setCancelled(true);
                    ProtocolLibModel.this.CRASHER_MAP.remove(player.getName());
                    // 踢出玩家
                    Bukkit.getScheduler().runTask(ProtocolLibModel.this.INSTANCE, () -> player.kickPlayer(
                            Fixer.getConfiguration().getString("message.crash_server.kick")
                                    .replace("&", "§"))
                    );
                }
            }
        });
    }
}

package com.blank038.fixer.bukkit.model.crasher.protocol;

import com.blank038.fixer.bukkit.Fixer;
import com.blank038.fixer.bukkit.model.crasher.CrasherPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

/**
 * @author Blank038
 * @since 2021-10-28
 */
public class PacketListenerApiModel extends AbsorbListenModel {

    public PacketListenerApiModel() {
        super();
        PacketListenerAPI.addPacketHandler(new PacketHandler(Fixer.getInstance()) {
            @Override
            public void onSend(SentPacket packet) {
            }

            @Override
            public void onReceive(ReceivedPacket packet) {
                if (!PacketListenerApiModel.this.INSTANCE.getConfig().getBoolean("message.crash_server.enable")) {
                    return;
                }
                if ("PacketPlayInTabComplete".equals(packet.getPacketName()) || "PacketPlayInBlockDig".equals(packet.getPacketName())) {
                    Player player = packet.getPlayer();
                    if (player == null) {
                        return;
                    }
                    if (!PacketListenerApiModel.this.CRASHER_MAP.containsKey(player.getName())) {
                        PacketListenerApiModel.this.CRASHER_MAP.put(player.getName(), new CrasherPacket.CrasherData());
                    }
                    boolean tabComplete = "PacketPlayInTabComplete".equals(packet.getPacketName());
                    CrasherPacket.CrasherData data = PacketListenerApiModel.this.CRASHER_MAP.get(player.getName());
                    if ((!tabComplete && data.addBlockDig()) || (tabComplete && data.addTabComplete())) {
                        packet.setCancelled(true);
                        PacketListenerApiModel.this.CRASHER_MAP.remove(player.getName());
                        // 踢出玩家
                        Bukkit.getScheduler().runTask(PacketListenerApiModel.this.INSTANCE, () -> player.kickPlayer(
                                Fixer.getConfiguration().getString("message.crash_server.kick")
                                        .replace("&", "§"))
                        );
                    }
                }
            }
        });
    }
}

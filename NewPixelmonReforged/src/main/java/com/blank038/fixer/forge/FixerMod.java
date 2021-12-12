package com.blank038.fixer.forge;

import com.blank038.fixer.model.log4j.Log4j2Model;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Blank038
 * @since 2021-12-10
 */
@Mod(modid = FixerMod.MODID, name = FixerMod.NAME, version = "1.6.8-SNAPSHOT")
public class FixerMod {
    public static final String MODID = "fixermode", NAME = "FixerMod";

    private Log4j2Model log4J2Model;

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
        try {
            Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
            log4J2Model = new Log4j2Model();
        } catch (Exception ignored) {
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(value = Side.CLIENT)
    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        if (!event.isCanceled() && this.log4J2Model != null && Log4j2Model.match(event.getMessage())) {
            event.setCanceled(true);
        }
    }

    @SideOnly(value = Side.SERVER)
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (!event.isCanceled() && this.log4J2Model != null && Log4j2Model.match(event.getMessage())) {
            event.setCanceled(true);
        }
    }
}

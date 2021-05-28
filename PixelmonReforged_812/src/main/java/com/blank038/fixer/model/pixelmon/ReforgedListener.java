package com.blank038.fixer.model.pixelmon;

import com.blank038.fixer.Fixer;
import com.mc9y.pokemonapi.api.event.ForgeEvent;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvents;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityCloningMachine;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 宝可梦相关内容修复类
 *
 * @author Laotouy, Blank038
 */
public class ReforgedListener implements Listener {
    protected boolean skillCopyItem;
    /**
     * 检测复制物品的精灵对战
     */
    protected final HashMap<String, BattleControllerBase> BATTLE_CONTROLLER_MAP = new HashMap<>();

    public ReforgedListener() {
        // 检测是否拥有重复精灵
        if (Fixer.getInstance().getConfig().getBoolean("message.pixelmon.repeat-uuid.enable")) {
            int delay = Fixer.getInstance().getConfig().getInt("message.pixelmon.repeat-uuid.delay") * 20;
            Bukkit.getScheduler().runTaskTimerAsynchronously(Fixer.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
                    for (int i = 0; i < 6; i++) {
                        Pokemon sp = storage.get(i);
                        if (sp == null || sp.isEgg()) {
                            continue;
                        }
                        for (int x = 0; x < 6; x++) {
                            if (i == x) {
                                continue;
                            }
                            Pokemon pokemon = storage.get(x);
                            if (pokemon == null || pokemon.isEgg()) {
                                continue;
                            }
                            if (pokemon.getUUID().toString().equals(sp.getUUID().toString())) {
                                storage.set(x, null);
                                player.sendMessage(Fixer.getConfiguration().getString("message.pixelmon.repeat-uuid.deny")
                                        .replace("&", "§"));
                            }
                        }
                    }
                }
            }, delay, delay);
        }
        skillCopyItem = Fixer.getInstance().getConfig().getBoolean("message.pixelmon.skill-copy-item.enable");
        Bukkit.getScheduler().runTaskTimerAsynchronously(Fixer.getInstance(), () -> {
            skillCopyItem = Fixer.getInstance().getConfig().getBoolean("message.pixelmon.skill-copy-item.enable");
            if (skillCopyItem) {
                List<String> removes = new ArrayList<>();
                for (Map.Entry<String, BattleControllerBase> entry : BATTLE_CONTROLLER_MAP.entrySet()) {
                    if (entry.getValue() == null || entry.getValue().battleEnded) {
                        removes.add(entry.getKey());
                    }
                }
                for (String key : removes) {
                    BATTLE_CONTROLLER_MAP.remove(key);
                }
            }
        }, 1200L, 1200L);
    }

    /**
     * 修复 Pixelmon 克隆机损坏漏洞
     *
     * @author Laotouy
     */
    @EventHandler
    public void onPlayerInteract0(PlayerInteractEvent e) {
        if (e.hasBlock() && "PIXELMON_CLONING_MACHINE".equalsIgnoreCase(e.getClickedBlock().getType().toString()) && Fixer.getConfiguration().getBoolean("message.pixelmon.clone.enable")) {
            org.bukkit.block.Block bk = e.getClickedBlock();
            net.minecraft.server.v1_12_R1.WorldServer world = ((CraftWorld) bk.getWorld()).getHandle();
            TileEntityCloningMachine tile = BlockHelper.getTileEntity(TileEntityCloningMachine.class,
                    DimensionManager.getWorld(world.dimension), new BlockPos(bk.getX(), bk.getY(), bk.getZ()));
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
        }
    }

    /**
     * 防止玩家使用渴望、传递礼物复制物品
     */
    @EventHandler
    public void onForge(ForgeEvent event) {
        if (event.getForgeEvent() instanceof AttackEvents && skillCopyItem) {
            AttackEvents e = (AttackEvents) event.getForgeEvent();
            if (e.user.getPlayerOwner() != null && e.target.getPlayerOwner() == null && !e.target.isMega) {
                if ("Covet".equals(e.getAttack().getMove().getAttackName()) && e.target.hasHeldItem() && BATTLE_CONTROLLER_MAP.remove(e.target.getPokemonUUID().toString()) != null) {
                    e.target.setNewHeldItem(null);
                    Fixer.getInstance().getLogger().info("尝试防止玩家技能复制物品, 触发玩家: " + e.user.getPlayerOwner().getName());
                } else if ("Bestow".equals(e.getAttack().getMove().getAttackName())) {
                    BATTLE_CONTROLLER_MAP.put(e.target.getPokemonUUID().toString(), e.target.bc);
                }
            }
        } else if (event.getForgeEvent() instanceof BeatWildPixelmonEvent
                && Fixer.getConfiguration().getBoolean("message.pixelmon.ghost_drop.enable")) {
            BeatWildPixelmonEvent e = (BeatWildPixelmonEvent) event.getForgeEvent();
            if (e.wpp.allPokemon.length < 1 || e.wpp.allPokemon[0].entity == null) {
                return;
            }
            EntityPixelmon entity = e.wpp.allPokemon[0].entity;
            // 判断精灵系别
            if (entity.getBaseStats().getTypeList().contains(EnumType.Ghost)) {
                if (entity.isBossPokemon()) {
                    entity.dropBossItems(e.player);
                } else {
                    entity.dropNormalItems(e.player);
                }
            }
        }
    }

    /**
     * 防止宝可梦树果催熟
     *
     * @author Blank038
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
                        if (Fixer.getConfiguration().getBoolean("message.pixelmon.apricorn.break")) {
                            BlockBreakEvent e = new BlockBreakEvent(top, player);
                            if (e.isCancelled()) {
                                return;
                            }
                            top.breakNaturally();
                        } else {
                            player.sendMessage(Fixer.getConfiguration().getString("message.pixelmon.apricorn.deny")
                                    .replace("&", "§"));
                        }
                    }
                }
            }
        }
    }

    /**
     * 修复合体器复制精灵
     *
     * @author Blank038
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if ("PIXELMON_PIXELMON".equalsIgnoreCase(e.getRightClicked().getType().name())) {
            Player player = e.getPlayer();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
                Entity entity = e.getRightClicked();
                String uuid = entity.getUniqueId().toString();
                for (int i = 0; i < 6; i++) {
                    Pokemon pokemon = storage.get(i);
                    if (pokemon != null && !pokemon.isEgg()) {
                        EntityPixelmon entityPixelmon = pokemon.getPixelmonIfExists();
                        if (entityPixelmon != null && entityPixelmon.isEvolving() && uuid.equals(entityPixelmon.getUniqueID().toString())) {
                            e.setCancelled(true);
                            player.sendMessage(Fixer.getConfiguration().getString("message.pixelmon.evolving.deny")
                                    .replace("&", "§"));
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isDenyItem(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }
}

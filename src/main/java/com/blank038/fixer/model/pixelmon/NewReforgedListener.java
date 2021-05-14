package com.blank038.fixer.model.pixelmon;

import com.blank038.fixer.Fixer;
import com.mc9y.pokemonapi.api.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Blank038
 * @since 2021-05-14
 */
public class NewReforgedListener extends ReforgedListener implements Listener {

    @Override
    @EventHandler
    public void onForge(ForgeEvent event) {
        if (event.getForgeEvent() instanceof AttackEvent.Use && skillCopyItem) {
            AttackEvent.Use e = (AttackEvent.Use) event.getForgeEvent();
            if (e.user.getPlayerOwner() != null && e.target.getPlayerOwner() == null && !e.target.isMega) {
                if ("Covet".equals(e.getAttack().getMove().getAttackName()) && e.target.hasHeldItem() && BATTLE_CONTROLLER_MAP.remove(e.target.getPokemonUUID().toString()) != null) {
                    e.target.setNewHeldItem(null);
                    Fixer.getInstance().getLogger().info("尝试防止玩家技能复制物品, 触发玩家: " + e.user.getPlayerOwner().getName());
                } else if ("Bestow".equals(e.getAttack().getMove().getAttackName())) {
                    BATTLE_CONTROLLER_MAP.put(e.target.getPokemonUUID().toString(), e.target.bc);
                }
            }
        }
    }
}

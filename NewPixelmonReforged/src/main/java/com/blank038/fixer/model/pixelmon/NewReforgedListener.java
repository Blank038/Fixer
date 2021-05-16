package com.blank038.fixer.model.pixelmon;

import com.blank038.fixer.Fixer;
import com.mc9y.pokemonapi.api.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * @author Blank038
 * @since 2021-05-14
 */
public class NewReforgedListener extends ReforgedListener implements Listener {
    private final HashMap<StatsType, Integer> ZACIAN_STATS = new HashMap<>();

    public NewReforgedListener() {
        this.ZACIAN_STATS.put(StatsType.HP, 92);
        this.ZACIAN_STATS.put(StatsType.Attack, 170);
        this.ZACIAN_STATS.put(StatsType.Defence, 115);
        this.ZACIAN_STATS.put(StatsType.SpecialAttack, 80);
        this.ZACIAN_STATS.put(StatsType.SpecialDefence, 115);
        this.ZACIAN_STATS.put(StatsType.Speed, 148);
    }

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
        } else if (event.getForgeEvent() instanceof UseMoveSkillEvent) {
            UseMoveSkillEvent e = (UseMoveSkillEvent) event.getForgeEvent();
            if ("crowned".equals(e.moveSkill.id) && e.pixelmon.getSpecies() == EnumSpecies.valueOf("Zacian")) {
                System.out.println(e.pixelmon.getFormIncludeTransformed());
                System.out.println("----");
                e.pixelmon.getSpecies().getDefaultForms().forEach((k) -> System.out.println(k.getForm()));
                System.out.println("----");
                e.pixelmon.getSpecies().getBaseStats(e.pixelmon.getFormEnum()).evYields.forEach((k, v) -> System.out.println(k.name() + " - " + v));
                System.out.println(e.pixelmon.getFormEnum().isDefaultForm());
            }
        }
    }
}

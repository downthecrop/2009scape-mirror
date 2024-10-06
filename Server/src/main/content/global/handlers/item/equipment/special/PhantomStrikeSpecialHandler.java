package content.global.handlers.item.equipment.special;

import core.api.ContentAPIKt;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.combat.RangeSwingHandler;
import core.game.node.entity.player.Player;
import core.game.system.timer.RSTimer;
import core.game.system.timer.TimerFlag;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import org.rs09.consts.Items;

@Initializable
public final class PhantomStrikeSpecialHandler extends RangeSwingHandler implements Plugin<Object> {
    private static final int SPECIAL_ENERGY = 50;

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.RANGE.getSwingHandler().register(Items.MORRIGANS_JAVELIN_13879, this);
        CombatStyle.RANGE.getSwingHandler().register(Items.MORRIGANS_JAVELINP_13880, this);
        CombatStyle.RANGE.getSwingHandler().register(Items.MORRIGANS_JAVELINP_PLUS_13881, this);
        CombatStyle.RANGE.getSwingHandler().register(Items.MORRIGANS_JAVELINP_PLUS_PLUS_13882, this);
        return this;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        Player p = (Player) entity;
        configureRangeData(p, state);
        if (state.getWeapon() == null || !Companion.hasAmmo(entity, state)) {
            entity.getProperties().getCombatPulse().stop();
            p.getSettings().toggleSpecialBar();
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setStyle(CombatStyle.RANGE);
        int max = calculateHit(entity, victim, 1.0);
        state.setMaximumHit(max);
        int hit = 0;
        if (isAccurateImpact(entity, victim)) {
            hit = RandomFunction.random(max + 1);
        }
        state.setEstimatedHit(hit);
        Companion.useAmmo(entity, state, victim.getLocation());
        final Entity attacker = entity;
        final int initialDamage = hit;
        ContentAPIKt.registerTimer(victim, new RSTimer(3, "phantom-strike", true, false, new TimerFlag[] { TimerFlag.ClearOnDeath } ) {
            int remainingDamage = initialDamage;

            @Override
            public void onRegister(Entity entity) {
                if(entity instanceof Player) {
                    ((Player)entity).sendMessage("You start to bleed as the result of the javelin strike.");
                }
            }

            @Override
            public boolean run(Entity entity) {
                if(entity instanceof Player) {
                    ((Player)entity).sendMessage("You continue to bleed as the result of the javelin strike.");
                }
                int tickDamage = Math.min(remainingDamage, 5);
                remainingDamage -= tickDamage;
                entity.getImpactHandler().manualHit(attacker, tickDamage, ImpactHandler.HitsplatType.NORMAL);
                return remainingDamage > 0;
            }

        });
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }
}

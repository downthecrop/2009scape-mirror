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
public final class HamstringSpecialHandler extends RangeSwingHandler implements Plugin<Object> {
    private static final int SPECIAL_ENERGY = 50;

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        CombatStyle.RANGE.getSwingHandler().register(Items.MORRIGANS_THROWING_AXE_13883, this);
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
        int max = calculateHit(entity, victim, 1.2);
        state.setMaximumHit(max);
        int hit = 0;
        if (isAccurateImpact(entity, victim)) {
            int minDamage = calculateHit(entity, victim, 0.2);
            hit = minDamage + RandomFunction.random(calculateHit(entity, victim, 1.0) + 1);
        }
        state.setEstimatedHit(hit);
        Companion.useAmmo(entity, state, victim.getLocation());
        ContentAPIKt.registerTimer(victim, new RSTimer(100, "hamstrung", true, false, new TimerFlag[] { TimerFlag.ClearOnDeath } ) {
            @Override
            public void onRegister(Entity entity) {
                if(entity instanceof Player) {
                    ((Player)entity).sendMessage("You've been hamstrung! For the next minute, your run energy will drain 4x faster.");
                }
            }

            @Override
            public boolean run(Entity entity) {
                return false;
            }

        });
        return 1 + (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
    }
}


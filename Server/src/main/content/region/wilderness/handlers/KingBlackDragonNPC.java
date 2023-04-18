package content.region.wilderness.handlers;

import content.data.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.combat.equipment.FireType;
import content.global.handlers.item.equipment.special.DragonfireSwingHandler;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.calculateDragonfireMaxHit;

/**
 * Represents the King Black Dragon NPC.
 * @author Emperor
 * @version 1.0
 */
@Initializable
public final class KingBlackDragonNPC extends AbstractNPC {

    /**
     * The default spawn location.
     */
    private static final Location DEFAULT_SPAWN = Location.create(2273, 4698, 0);

    /**
     * The combat swing handler.
     */
    private CombatSwingHandler combatHandler = new KBDCombatSwingHandler();

    
    public KingBlackDragonNPC() {
        super(-1,null);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        BossKillCounter.addtoKillcount((Player) killer, this.getId());
    }

    /**
     * Constructs a new {@code KingBlackDragonNPC} {@code Object}.
     * @param id the id.
     * @param l the l.
     */
    public KingBlackDragonNPC(int id, Location l) {
        super(id, l);
    }

    @Override
    public void init() {
        super.init();
        configureBossData();
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KingBlackDragonNPC(id, location);
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        return 0x2 | 0x4 | 0x8;
    }

    @Override
    public int[] getIds() {
        return new int[] { 50 };
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatHandler;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        return super.newInstance(arg);
    }

    /**
     * Handles the King Black Dragon's combat swings.
     * @author Emperor
     */
    static class KBDCombatSwingHandler extends CombatSwingHandler {

        /**
         * The style.
         */
        private CombatStyle style = CombatStyle.RANGE;

        /**
         * Dragonfire.
         */
        private static final DragonfireSwingHandler DRAGONFIRE = new DragonfireSwingHandler(false, 56, null, true);

        /**
         * The melee attack animation.
         */
        private static final Animation MELEE_ATTACK = new Animation(80, Priority.HIGH);

        /**
         * The fire type.
         */
        private FireType fireType = FireType.FIERY_BREATH;

        /**
         * Constructs a new {@code KBDCombatSwingHandler} {@Code Object}.
         */
        public KBDCombatSwingHandler() {
            super(CombatStyle.RANGE);
        }

        @Override
        public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
            if (style == CombatStyle.RANGE) {
                fireType.getTask().exec(victim, entity);
                state.setStyle(null);
                DRAGONFIRE.adjustBattleState(entity, victim, state);
                state.setStyle(CombatStyle.RANGE);
                return;
            }
            style.getSwingHandler().adjustBattleState(entity, victim, state);
        }

        @Override
        public int calculateAccuracy(Entity entity) {
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateAccuracy(entity);
            }
            return CombatStyle.MAGIC.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateDefence(Entity victim, Entity attacker) {
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateDefence(victim, attacker);
            }
            return CombatStyle.MAGIC.getSwingHandler().calculateDefence(victim, attacker);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateHit(entity, victim, modifier);
            }
            return calculateDragonfireMaxHit(victim, 56, false, fireType != FireType.FIERY_BREATH ? 10 : 0, true);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            if (!isProjectileClipped(entity, victim, false)) {
                return InteractionType.NO_INTERACT;
            }
            if (victim.getCenterLocation().withinMaxnormDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, 9)) && super.canSwing(entity, victim) == InteractionType.STILL_INTERACT) {
                entity.getWalkingQueue().reset();
                return InteractionType.STILL_INTERACT;
            }
            return InteractionType.NO_INTERACT;
        }

        @Override
        public ArmourSet getArmourSet(Entity e) {
            return style.getSwingHandler().getArmourSet(e);
        }

        @Override
        public double getSetMultiplier(Entity e, int skillId) {
            return style.getSwingHandler().getSetMultiplier(e, skillId);
        }

        @Override
        public void impact(Entity entity, Entity victim, BattleState state) {
            style.getSwingHandler().impact(entity, victim, state);
        }

        @Override
        public int swing(Entity entity, Entity victim, BattleState state) {
            style = CombatStyle.RANGE;
            int hit = 0;
            int ticks = 1;
            if (victim.getCenterLocation().withinMaxnormDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, 1)) && RandomFunction.random(10) < 7) {
                style = CombatStyle.MELEE;
            } else {
                ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
            }
            fireType = FireType.values()[RandomFunction.random(FireType.values().length)];
            state.setStyle(style);
            if (isAccurateImpact(entity, victim)) {
                int max = calculateHit(entity, victim, 1.0);
                state.setMaximumHit(max);
                hit = RandomFunction.random(max + 1);
            }
            state.setEstimatedHit(hit);
            ((NPC) entity).getAggressiveHandler().setPauseTicks(2);
            return ticks;
        }

        @Override
        public void visualize(Entity entity, Entity victim, BattleState state) {
            switch (style) {
            case MELEE:
                entity.animate(MELEE_ATTACK);
                break;
            case RANGE:
                Projectile.ranged(entity, victim, fireType.getProjectileId(), 20, 36, 50, 15).send();
                entity.animate(fireType.getAnimation());
                break;
            default:
                break;
            }
        }

        @Override
        public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
            if (style != CombatStyle.MELEE) {
                DRAGONFIRE.visualizeImpact(entity, victim, state);
            } else {
                style.getSwingHandler().visualizeImpact(entity, victim, state);
            }
        }

        /**
         * Gets the fire type.
         * @return the type.
         */
        public FireType getFireType() {
            return fireType;
        }

    }
}

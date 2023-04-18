package content.region.misthalin.varrock.quest.dragonslayer;

import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.ArmourSet;
import core.game.node.entity.combat.equipment.FireType;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import content.global.handlers.item.equipment.special.DragonfireSwingHandler;

import static core.api.ContentAPIKt.calculateDragonfireMaxHit;


/**
 * Represents the Elvarg npc.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class ElvargNPC extends AbstractNPC {

    /**
     * The NPC ids of NPCs using this plugin.
     */
    private static final int[] ID = { 742 };

    /**
     * Represents the animations to use.
     */
    private static final Animation[] ANIMATIONS = new Animation[] { new Animation(6654), new Animation(6655) };

    /**
     * Represents the combat swing handler.
     */
    private final CombatSwingHandler combatHandler = new ElvargCombatSwingHandler();

    /**
     * Constructs a new {@code ElvargNPC} {@code Object}.
     */
    public ElvargNPC() {
        super(0, null);
    }

    /**
     * Constructs a new {@code ElvargNPC} {@code Object}.
     * @param id The NPC id.
     * @param location The location.
     */
    private ElvargNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new ElvargNPC(id, location);
    }

    @Override
    public void commenceDeath(Entity killer) {
        final Direction direction = Direction.getLogicalDirection(getLocation(), killer.getLocation());
        GameWorld.getPulser().submit(new Pulse(1, this) {
            @Override
            public boolean pulse() {
                faceLocation(getCenterLocation().transform(direction.getStepX() * 3, direction.getStepY() * 3, 0));
                return true;
            }
        });
        setDirection(direction);
    }

    @Override
    public void finalizeDeath(final Entity killer) {
        super.finalizeDeath(killer);
        final Scenery object = new Scenery(25202, getLocation(), getRotation());
        SceneryBuilder.add(object);
        killer.faceLocation(object.getCenterLocation());
        killer.lock();
        GameWorld.getPulser().submit(new Pulse(1) {
            int counter = 0;

            @Override
            public boolean pulse() {
                counter++;
                if (counter == 1) {
                    killer.animate(ANIMATIONS[0]);
                } else if (counter == 4) {
                    final Player player = ((Player) killer);
                    SceneryBuilder.replace(object, object.transform(25203));
                    if (!player.getInventory().add(DragonSlayer.ELVARG_HEAD)) {
                        GroundItemManager.create(DragonSlayer.ELVARG_HEAD, player);
                    }
                    killer.animate(ANIMATIONS[1]);
                    killer.unlock();
                } else if (counter == 12) {
                    SceneryBuilder.remove(RegionManager.getObject(object.getLocation()));
                    return true;
                }
                return false;
            }

        });
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatHandler;
    }

    @Override
    public int[] getIds() {
        return ID;
    }

    /**
     * Method used to get the rotation.
     * @return the rotation.
     */
    public int getRotation() {
        switch (getDirection()) {
        case EAST:
            return 3;
        case NORTH:
            return 2;
        case WEST:
            return 1;
        case SOUTH:
            return 0;
        default:
            break;
        }
        return 0;
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style, boolean message) {
        if (!(entity instanceof Player)) {
            return super.isAttackable(entity, style, message);
        }
        final Player player = (Player) entity;
        if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) == 40 && (player.getInventory().containsItem(DragonSlayer.ELVARG_HEAD))) {
            if(message) {
                player.getPacketDispatch().sendMessage("You have already slain the dragon. Now you just need to return to Oziach for");
                player.getPacketDispatch().sendMessage("your reward!");
            }
            return false;
        }
        if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) > 40) {
            if(message) {
                player.getPacketDispatch().sendMessage("You have already slain Elvarg.");
            }
            return false;
        }
        return super.isAttackable(entity, style, message);
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        return 0x2 | 0x4 | 0x8;
    }

    /**
     * Handles the Elvarg combat swing handler.
     * @author Emperor
     */
    static class ElvargCombatSwingHandler extends CombatSwingHandler {

        /**
         * The style.
         */
        private CombatStyle style = CombatStyle.RANGE;

        /**
         * The melee attack animation.
         */
        private static final Animation MELEE_ATTACK = new Animation(80, Priority.HIGH);

        /**
         * The fire type.
         */
        private final FireType fireType = FireType.FIERY_BREATH;

        /**
        * The DragonFire Attack */
        private static final DragonfireSwingHandler DRAGONFIRE = new DragonfireSwingHandler(false, 60, null, true);

        /**
         * Constructs a new {@Code ElvargCombatSwingHandler} {@Code CombatStyle}.
         *  The Combat style.
         */
        public ElvargCombatSwingHandler() {
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
            // If in melee combat, calculate attackers accuracy level.
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateAccuracy(entity);
            }
            // Else calculate attackers accuracy based on their magic level.
            return CombatStyle.MAGIC.getSwingHandler().calculateAccuracy(entity);
        }

        @Override
        public int calculateDefence(Entity victim, Entity attacker) {
            // If in melee combat, calculate players defense level.
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateDefence(victim, attacker);
            }
            // Else calculate players defense against attack based on their magic level.
            return CombatStyle.MAGIC.getSwingHandler().calculateDefence(victim, attacker);
        }

        @Override
        public int calculateHit(Entity entity, Entity victim, double modifier) {
            if (style == CombatStyle.MELEE) {
                return style.getSwingHandler().calculateHit(entity, victim, modifier);
            }

            return calculateDragonfireMaxHit(victim, 60, false, 3, true);
        }

        @Override
        public InteractionType canSwing(Entity entity, Entity victim) {
            if (!isProjectileClipped(entity, victim, false)) {
                return InteractionType.NO_INTERACT;
            }
            if (victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, 9)) && super.canSwing(entity, victim) != InteractionType.NO_INTERACT) {
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

            if (victim.getCenterLocation().withinDistance(entity.getCenterLocation(), getCombatDistance(entity, victim, 1)) ) {
                if ( RandomFunction.random(10) < 7 ){
                    style = CombatStyle.MELEE;
                }
            } else {
                ticks += (int) Math.ceil(entity.getLocation().getDistance(victim.getLocation()) * 0.3);
            }

            state.setStyle(style);

            if (isAccurateImpact(entity, victim)) {
                int max = calculateHit(entity, victim, 1.0);
                state.setMaximumHit(max);
                hit = RandomFunction.random(max + 1);
            }
            state.setEstimatedHit(hit);
            return ticks;
        }

        @Override
        public void visualize(Entity entity, Entity victim, BattleState state) {
            switch (style) {
            case MELEE:
                entity.animate(MELEE_ATTACK);
                break;
            case RANGE:
                Projectile.ranged(entity, victim, 450, 20, 36, 50, 15).send();
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

    }

}

package content.global.handlers.item.equipment.special;

import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.CombatSwingHandler;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.combat.equipment.SwitchAttack;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * Handles dragonfire combat.
 * @author Emperor
 */
public class DragonfireSwingHandler extends CombatSwingHandler {

    /**
     * If the NPC has to be in melee range.
     */
    private boolean meleeRange;

    /**
     * The maximum hit.
     */
    private int maximumHit;

    /**
     * The attack data.
     */
    private SwitchAttack attack;

    /**
     * IF the dragon attack is firey or icey.
     */
    private boolean fire;

    /**
     * Constructs a new {@code DragonfireSwingHandler} {@code Object}.
     * @param meleeRange If the NPC has to be in melee range.
     * @param maximumHit The maximum hit.
     * @param fire if firey.
     */
    public DragonfireSwingHandler(boolean meleeRange, int maximumHit, SwitchAttack attack, boolean fire) {
        super(CombatStyle.MAGIC);
        this.meleeRange = meleeRange;
        this.maximumHit = maximumHit;
        this.attack = attack;
        this.fire = fire;
    }

    /**
     * Gets the switch attack instance for a dragonfire attack.
     * @param meleeRange If the attack is melee range.
     * @param maximumHit The maximum hit.
     * @param animation The animation.
     * @param startGraphic The start graphic.
     * @param endGraphic The end graphic.
     * @param projectile The projectile.
     * @return The switch attack instance.
     */
    public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphic, Graphics endGraphic, Projectile projectile) {
        SwitchAttack attack = new SwitchAttack(null, animation, startGraphic, endGraphic, projectile).setUseHandler(true);
        attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, true));
        return attack;
    }

    /**
     * Gets the switch attack instance for a dragonfire attack.
     * @param meleeRange If the attack is melee range.
     * @param maximumHit The maximum hit.
     * @param animation The animation.
     * @param startGraphic The start graphic.
     * @param endGraphic The end graphic.
     * @param projectile The projectile.
     * @return The switch attack instance.
     */
    public static SwitchAttack get(boolean meleeRange, int maximumHit, Animation animation, Graphics startGraphic, Graphics endGraphic, Projectile projectile, boolean fire) {
        SwitchAttack attack = new SwitchAttack(null, animation, startGraphic, endGraphic, projectile).setUseHandler(true);
        attack.setHandler(new DragonfireSwingHandler(meleeRange, maximumHit, attack, fire));
        return attack;
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        if (meleeRange) {
            return CombatStyle.MELEE.getSwingHandler().canSwing(entity, victim);
        }
        return CombatStyle.MAGIC.getSwingHandler().canSwing(entity, victim);
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        int max = calculateHit(entity, victim, 1.0);
        int hit = RandomFunction.random(max + 1);
        assert state != null;
        state.setMaximumHit(max);
        state.setStyle(CombatStyle.MAGIC);
        state.setEstimatedHit(hit);
        if (meleeRange) {
            return 1;
        }
        int ticks = 2 + (int) Math.floor(entity.getLocation().getDistance(victim.getLocation()) * 0.5);
        entity.setAttribute("fireBreath", GameWorld.getTicks() + (ticks + 2));
        return ticks;
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(attack.getAnimation(), attack.getStartGraphic());
        if (attack.getProjectile() != null) {
            attack.getProjectile().copy(entity, victim, 5).send();
        }
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
        if (entity instanceof NPC && victim instanceof Player) {
            Player p = (Player) victim;
            Item shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
            if (shield != null && (shield.getId() == 11283 || shield.getId() == 11284)) {
                if (shield.getId() == 11284) {
                    p.getEquipment().replace(new Item(11283), EquipmentContainer.SLOT_SHIELD);
                    shield = p.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
                    shield.setCharge(0);
                }
                if (shield.getCharge() < 1000) {
                    shield.setCharge(shield.getCharge() + 20);
                    EquipmentContainer.updateBonuses(p);
                    p.getPacketDispatch().sendMessage("Your dragonfire shield glows more brightly.");
                    playAudio(p, Sounds.DRAGONSLAYER_ABSORB_FIRE_3740);
                    p.faceLocation(entity.getCenterLocation());
                    victim.visualize(Animation.create(6695), Graphics.create(1163));
                } else {
                    p.getPacketDispatch().sendMessage("Your dragonfire shield is already fully charged.");
                }
                return;
            }
        }
        if (!fire && !hasTimerActive(victim, "frozen:immunity") && RandomFunction.random(4) == 2) {
            registerTimer(victim, spawnTimer("frozen", 16, true));
            victim.graphics(Graphics.create(502));
        }
        Graphics graphic = attack != null ? attack.getEndGraphic() : null;
        victim.visualize(victim.getProperties().getDefenceAnimation(), graphic);
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {
        assert state != null;
        int hit = state.getEstimatedHit();
        if (hit > -1) {
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
        }
        hit = state.getSecondaryHit();
        if (hit > -1) {
            victim.getImpactHandler().handleImpact(entity, hit, CombatStyle.MAGIC, state);
        }
    }

    @Override
    public void adjustBattleState(Entity entity, Entity victim, BattleState state) {
        if (victim.isPlayer()  && !fire) {
            Item item = victim.asPlayer().getEquipment().get(EquipmentContainer.SLOT_SHIELD);
            if (item != null && (item.getId() == 2890 || item.getId() == 9731) && state.getEstimatedHit() > 10) {
                state.setEstimatedHit(RandomFunction.random(10));
            }
        }
        CombatStyle style = state.getStyle();
        super.adjustBattleState(entity, victim, state);
        state.setStyle(style);
    }

    @Override
    protected int getFormattedHit(Entity entity, Entity victim, BattleState state, int hit) {
        return formatHit(victim, hit);
    }

    @Override
    public int calculateAccuracy(Entity entity) {
        return 4000;
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        return calculateDragonfireMaxHit(victim, maximumHit, !fire, 0, true);    
    }

    @Override
    public int calculateDefence(Entity victim, Entity attacker) {
        return CombatStyle.MAGIC.getSwingHandler().calculateDefence(victim, attacker);
    }

    @Override
    public double getSetMultiplier(Entity e, int skillId) {
        return 1.0;
    }

}

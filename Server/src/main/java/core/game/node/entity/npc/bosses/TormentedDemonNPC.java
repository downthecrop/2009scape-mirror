package core.game.node.entity.npc.bosses;

import java.util.concurrent.TimeUnit;

import core.game.content.global.BossKillCounter;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.combat.InteractionType;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.tools.RandomFunction;
import rs09.game.node.entity.combat.CombatSwingHandler;

/**
 * Handles the Tormented Demon NPC.
 * @author Vexia
 *
 */
@Initializable
public class TormentedDemonNPC extends AbstractNPC {

	/**
	 * The meele protect npc ids.
	 */
	private static final int[][] MELEE = new int[][] {{8349, 8352, 8355}, {8358, 8361, 8364}};

	/**
	 * The mage protect npc ids.
	 */
	private static final int[][] MAGE = new int[][] {{8350, 8353, 8356}, {8359, 8362, 8365}};

	/**
	 * The range protect npc ids.
	 */
	private static final int[][] RANGE = new int[][] {{8351, 8354, 8357}, {8360, 8363, 8366}};

	/**
	 * The tormented demon swing handler.
	 */
	private final TormentedDemonSwingHandler TD_SWING_HANDLER = new TormentedDemonSwingHandler();
	
	/**
	 * The last combat style switch.
	 */
	private long lastSwitch = System.currentTimeMillis() + 15000;
	
	/**
	 * If the fire shield is enabled.
	 */
	private boolean fireShield = true;
	
	/**
	 * The delay of the shield being non-active.
	 */
	private long shieldDelay;
	
	/**
	 * The damage log of what style is dealing the most damage.
	 */
	private final int[] damageLog = new int[3];

	@Override
	public void init() {
		super.init();
        getAggressiveHandler().setChanceRatio(10);
        getAggressiveHandler().setRadius(64);
        getAggressiveHandler().setAllowTolerance(false);
    }

	/**
	 * Constructs a new {@Code TormentedDemonNPC} {@Code Object}
	 */
	public TormentedDemonNPC() {
		this(-1, null); 
	}

	/**
	 * Constructs a new {@Code TormentedDemonNPC} {@Code Object}
	 * @param id The npc id.
	 * @param location The start location.
	 */
	public TormentedDemonNPC(int id, Location location) {
		super(id, location);
		setWalks(true);
		setAggressive(true);
		this.setDefaultBehavior();
	}

    @Override
    public boolean shouldPreventStacking(Entity other) {
        return other instanceof TormentedDemonNPC;
    }
	
	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (!fireShield && shieldDelay < System.currentTimeMillis() && shieldDelay > 0) {
			Player p = getAttribute("shield-player", null);
			fireShield = true;
			shieldDelay = 0;
			if (p != null && p.isActive() && p.getLocation().withinDistance(getLocation()) && isActive() && !isHidden(p)) {
				p.sendMessage("The Tormented demon regains its strength against your weapon.");
			}
		}
	}

	@Override
	public void sendImpact(BattleState state) {
		int max = 0;
		switch (state.getStyle()) {
		case MAGIC:
		case RANGE:
			max = 26;
			break;
		case MELEE:
			max = 18;
			break;
		}
		
		if (state.getEstimatedHit() > max) {
			state.setEstimatedHit(RandomFunction.random(max - 5));
		}
	}

	@Override
	public void checkImpact(BattleState state) {
        // Use the formatted hit to ensure protection prayers are applied (i.e. can't darklight while the demon is praying melee).
        int formattedHit = (int) state.getAttacker().getFormattedHit(state, state.getEstimatedHit());
		if (state.getAttacker().isPlayer() && formattedHit > 0 && state.getWeapon() != null && (state.getWeapon().getId() == 6746 || state.getWeapon().getId() == 732)) {
            // The message doesn't get sent twice, but additional darklight strikes while the shield is down do delay the shield's return.
            if(fireShield) {
                state.getAttacker().asPlayer().sendMessage("The demon is temporarily weakened by your weapon.");
            }
			shieldDelay = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(60);
			fireShield = false;
			setAttribute("shield-player", state.getAttacker());
		}
		if (fireShield) {
			state.setEstimatedHit((int) (state.getEstimatedHit() * 0.25));
			graphics(Graphics.create(1885));
		}
		if (state.getStyle() == null) {
			return;
		}
        // Use formattedHit for the prayer swap calculation since it's before the fire 
        // shield reduction was applied (a ranged hit of 8 through the shield corresponds to a 
        // pre-shield hit of 32, which should cause the demon to switch to praying range).
		int hit = formattedHit > 0 ? formattedHit : 1;
		damageLog[state.getStyle().ordinal()] = damageLog[state.getStyle().ordinal()] + hit;
	}

    @Override
    public void onImpact(final Entity entity, BattleState state) {
        // Call the parent class's onImpact handler to ensure that retaliation happens if the TD is non-aggressive.
        super.onImpact(entity, state);
        // "The demon will switch prayers after it receives 31 damage from one attack style."
        // This is done in onImpact so that it happens after the damage that caused the switch is dealt.
		CombatStyle damaged = getMostDamagedStyle();
		if (damaged != null && damageLog[damaged.ordinal()] >= 31 && damaged != getProperties().getProtectStyle()) {
			for (int i = 0; i < 3; i++) {
				damageLog[i] = 0;
			}
			transformDemon(null, damaged);
			return;
		} else if (lastSwitch < System.currentTimeMillis()) {
			transformDemon(RandomFunction.getRandomElement(getAlternateStyle(TD_SWING_HANDLER.style)), null);
			lastSwitch = System.currentTimeMillis() + 15000;
            // The roar animation that TDs do when they change attack styles 
            // shouldn't be interrupted by attack/defence animations.
            // https://youtu.be/VcWncVTev1s?t=220
            animate(new Animation(10917, Priority.HIGH));
		}
    }

	@Override
	public void finalizeDeath(Entity killer)  {
		super.finalizeDeath(killer);
		reTransform();
		fireShield = true;
		BossKillCounter.addtoKillcount((Player) killer, this.getId());
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new TormentedDemonNPC(id, location);
	}

	@Override
	public CombatSwingHandler getSwingHandler(boolean hit) {
		return TD_SWING_HANDLER;
	}

	/**
	 * Switches the Tormented Demons style (protection & combat)
	 * @param attackStyle The combat style to switch to.
	 * @param protectionStyle The protection style to switch to.
	 */
	public void transformDemon(CombatStyle attackStyle, CombatStyle protectionStyle) {
		//System.out.println("Transforming demon, selected combat style = " + style + ", the selected protection style = " + protection);

        // If either attackStyle or protectionStyle are null, use the current form's values
        if(attackStyle == null) {
            attackStyle = getProperties().getCombatPulse().getStyle();
        }
        if(protectionStyle == null) {
            protectionStyle = getProperties().getProtectStyle();
        }
        int id = getCombatStyleDemon(protectionStyle, attackStyle);
		int oldHp = getSkills().getLifepoints();
		transform(id);
		getSkills().setLifepoints(oldHp);
		//System.out.println("The outcome demon, id = " + id + " combat style = " + getProperties().getCombatPulse().getStyle() + ", the protection style = " + getProperties().getProtectStyle());
		TD_SWING_HANDLER.style = getProperties().getCombatPulse().getStyle();
	}
	
	/**
	 * Gets the most damaged style.
	 * @return The combat style.
	 */
	public CombatStyle getMostDamagedStyle() {
		int highestDamage = 0;
		CombatStyle style = null;
		for (int i = 0; i < damageLog.length; i++) {
			if (damageLog[i] > highestDamage) {
				highestDamage = damageLog[i];
				style = CombatStyle.values()[i];
			}
		}
		return style;
	}

	/**
	 * Gets a demon id for a combat style.
	 * @param protection The protections tyle.
	 * @param style The style.
	 * @return The id of a demon in a combat style.
	 */
	public int getCombatStyleDemon(CombatStyle protection, CombatStyle style) {
		return getDemonIds(protection)[2  - style.ordinal()];
	}

	/**
	 * Gets a list of demon ids to chose from for a protection style.
	 * @param style The style of protection.
	 * @return The demon ids.
	 */
	public int[] getDemonIds(CombatStyle style) {
		int[][] ids = style == CombatStyle.MELEE ? MELEE : style == CombatStyle.RANGE ? RANGE : MAGE;
		return ids[getStartId() == getIds()[0] ? 0 : 1];
	}

	/**
	 * Gets the alternate styles.
	 * @param style The style to compare to.
	 * @return The combat styles to chose from.
	 */
	public CombatStyle[] getAlternateStyle(CombatStyle style) {
		CombatStyle[] styles = new CombatStyle[2];
		int index = 0;
		for (int i = 0; i < CombatStyle.values().length; i++) {
			if (CombatStyle.values()[i] != style) {
				styles[index] = CombatStyle.values()[i];
				index++;
			}
		}
		return styles;
	}

	/**
	 * Gets the start if of the demon.
	 * @return The start id.
	 */
	public int getStartId() {
		return getId() <= 8357 ? getIds()[0] : getIds()[10];
	}

	@Override
	public int[] getIds() {
		return new int[] { 8349, 8350, 8351, 8352, 8353, 8354, 8355, 8356, 8357, 8358, 8359, 8360, 8361, 8362, 8363, 8364, 8365, 8366 };
	}

	/**
	 * Handles the Tormented Demon's combat.
	 * @author Vexia
	 *
	 */
	public class TormentedDemonSwingHandler extends CombatSwingHandler {

		/**
		 * The current style.
		 */
		private CombatStyle style = CombatStyle.MELEE;

		/**
		 * Constructs a new {@Code TormentedDemonSwingHandler} {@Code Object}
		 */
		public TormentedDemonSwingHandler() {
			super(CombatStyle.MELEE);
		}
		
		@Override
		public InteractionType canSwing(Entity entity, Entity victim) {
			return style.getSwingHandler().canSwing(entity, victim);
		}

		@Override
		public int swing(Entity entity, Entity victim, BattleState state) {
			return style.getSwingHandler().swing(entity, victim, state);
		}

		@Override
		public void impact(Entity entity, Entity victim, BattleState state) {
			style.getSwingHandler().impact(entity, victim, state);
		}

		@Override
		public void visualizeImpact(Entity entity, Entity victim, BattleState state) {
			style.getSwingHandler().visualizeImpact(entity, victim, state);
		}

		@Override
		public void visualize(Entity entity, Entity victim, BattleState state) {
			switch (style) {
			case MELEE:
				entity.animate(entity.getProperties().getAttackAnimation());
				entity.graphics(Graphics.create(1886));
				break;
			case RANGE:
				Projectile.ranged(entity, victim, 1887, 88, 36, 50, 15).send();
				entity.animate(entity.getProperties().getRangeAnimation());
				break;
			case MAGIC:
				Projectile.magic(entity, victim, 1884, 88, 36, 50, 15).send();
				entity.animate(entity.getProperties().getMagicAnimation());
				break;
			}
		}

		@Override
		public int calculateAccuracy(Entity entity) {
			return style.getSwingHandler().calculateAccuracy(entity);
		}

		@Override
		public int calculateHit(Entity entity, Entity victim, double modifier) {
			return style.getSwingHandler().calculateHit(entity, victim, modifier);
		}

		@Override
		public int calculateDefence(Entity victim, Entity attacker) {
			return style.getSwingHandler().calculateDefence(victim, attacker);
		}

		@Override
		public double getSetMultiplier(Entity e, int skillId) {
			return style.getSwingHandler().getSetMultiplier(e, skillId); 
		}

	}
}

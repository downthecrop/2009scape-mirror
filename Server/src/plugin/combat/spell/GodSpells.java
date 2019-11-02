package plugin.combat.spell;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.BattleState;
import org.crandor.game.node.entity.combat.CombatSpell;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.impl.Projectile;
import org.crandor.game.node.entity.impl.Animator.Priority;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Handles the god spells.
 * @author Emperor
 * @author 'Vexia
 */
@InitializablePlugin
public final class GodSpells extends CombatSpell {

	/**
	 * The spell names.
	 */
	private static final String[] NAMES = new String[] { "Saradomin strike", "Guthix claws", "Flames of Zamorak" };

	/**
	 * The start graphic for Air strike.
	 */
	private static final Graphics SARA_START = null;

	/**
	 * The projectile for Air strike.
	 */
	private static final Projectile SARA_PROJECTILE = null;

	/**
	 * The end graphic for Air strike.
	 */
	private static final Graphics SARA_END = new Graphics(76, 0);

	/**
	 * The start graphic for Air strike.
	 */
	private static final Graphics GUTHIX_START = null;

	/**
	 * The projectile for Air strike.
	 */
	private static final Projectile GUTHIX_PROJECTILE = null;

	/**
	 * The end graphic for Air strike.
	 */
	private static final Graphics GUTHIX_END = new Graphics(77, 0);

	/**
	 * The start graphic for Air strike.
	 */
	private static final Graphics ZAM_START = null;

	/**
	 * The projectile for Air strike.
	 */
	private static final Projectile ZAM_PROJECTILE = null;

	/**
	 * The end graphic for Air strike.
	 */
	private static final Graphics ZAM_END = new Graphics(78, 0);

	/**
	 * The cast animation.
	 */
	private static final Animation ANIMATION = new Animation(811, Priority.HIGH);

	/**
	 * Constructs a new {@code AirSpell} {@Code Object}
	 */
	public GodSpells() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code GodSpells} {@code Object}.
	 * @param type the type.
	 * @param sound the sound.
	 * @param start the start.
	 * @param projectile the projectile.
	 * @param end the end.
	 * @param runes the runes.
	 */
	private GodSpells(SpellType type, int sound, Graphics start, Projectile projectile, Graphics end, Item... runes) {
		super(type, SpellBook.MODERN, 60, 35.0, -1, -1, ANIMATION, start, projectile, end, runes);
	}

	@Override
	public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
		if (caster instanceof NPC) {
			return true;
		}
		if (caster instanceof Player) {
			int staffId = ((Player) caster).getEquipment().getNew(EquipmentContainer.SLOT_WEAPON).getId();
			int required = -1;
			int index = 0;
			switch (runes[1].getAmount()) {
			case 2: // Saradomin strike
				required = 2415;
				break;
			case 1: // Guthix claws
				required = 2416;
				index = 1;
				break;
			case 4: // Flames of Zamorak
				required = 2417;
				index = 2;
				break;
			}
			Player p = (Player) caster;
			if (p.getSavedData().getActivityData().getGodCasts()[index] < 100 && !p.getZoneMonitor().isInZone("mage arena")) {
				p.sendMessage("You need to cast " + NAMES[index] + " " + (100 - p.getSavedData().getActivityData().getGodCasts()[index]) + " more times inside the Mage Arena.");
				return false;
			}
			if (staffId == 8841 && index == 1) {// Void Knight Mace
				return true;
			}
			if (staffId != required) {
				if (message) {
					((Player) caster).getPacketDispatch().sendMessage("You need to wear a " + ItemDefinition.forId(required).getName() + "  to cast this spell.");
				}
				return false;
			}
		}
		return super.meetsRequirements(caster, message, remove);
	}

	@Override
	public void visualize(Entity entity, Node target) {
		super.visualize(entity, target);
		if (entity instanceof NPC) {
			NPC n = (NPC) entity;
			if (n.getId() > 911 && n.getId() < 915 || (n.getId() > 906 && n.getId() < 912)) {
				n.getAnimator().forceAnimation(n.getProperties().getAttackAnimation());
			}
		}
	}

	@Override
	public void visualizeImpact(Entity entity, Entity target, BattleState state) {
		if (entity instanceof Player) {
			int index = 0; // Saradomin strike
			switch (runes[1].getAmount()) {
			case 1: // Guthix claws
				index = 1;
				break;
			case 4: // Flames of Zamorak
				index = 2;
				break;
			}
			Player p = (Player) entity;
			if (p.getSavedData().getActivityData().getGodCasts()[index] < 100) {
				p.getSavedData().getActivityData().getGodCasts()[index]++;
				if (p.getSavedData().getActivityData().getGodCasts()[index] >= 100) {
					p.sendMessage("You can now cast " + NAMES[index] + " outside the Arena.");
				}
			}
		}
		super.visualizeImpact(entity, target, state);
	}

	@Override
	public int getMaximumImpact(Entity entity, Entity victim, BattleState state) {
		return getType().getImpactAmount(entity, victim, 0);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType type) throws Throwable {
		SpellBook.MODERN.register(41, new GodSpells(SpellType.GOD_STRIKE, -1, SARA_START, SARA_PROJECTILE, SARA_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(2), Runes.AIR_RUNE.getItem(4)));
		SpellBook.MODERN.register(42, new GodSpells(SpellType.GOD_STRIKE, -1, GUTHIX_START, GUTHIX_PROJECTILE, GUTHIX_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(1), Runes.AIR_RUNE.getItem(4)));
		SpellBook.MODERN.register(43, new GodSpells(SpellType.GOD_STRIKE, -1, ZAM_START, ZAM_PROJECTILE, ZAM_END, Runes.BLOOD_RUNE.getItem(2), Runes.FIRE_RUNE.getItem(4), Runes.AIR_RUNE.getItem(1)));
		return this;
	}

}

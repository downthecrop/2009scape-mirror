package core.game.content.activity.magearena;

import core.game.content.global.GodType;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.CombatSpell;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.world.map.Location;
import core.tools.RandomFunction;

/**
 * A mage arena npc.
 * @author Vexia
 */
public final class MageArenaNPC extends AbstractNPC {

	/**
	 * The god type.
	 */
	private final GodType type;

	/**
	 * Constructs a new {@code MageArenaNPC} {@code Object}.
	 */
	public MageArenaNPC() {
		super(0, null);
		this.type = null;
	}

	/**
	 * Constructs a new {@code MageArenaNPC} {@code Object}.
	 * @param id the id.
	 * @param location the location.
	 */
	public MageArenaNPC(int id, Location location) {
		super(id, location);
		this.setWalks(true);
		this.type = GodType.forId(id);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new MageArenaNPC(id, location);
	}

	@Override
	public void init() {
		super.init();
		CombatSpell spell = (CombatSpell) SpellBook.MODERN.getSpell(41 + type.ordinal());
		getProperties().setSpell(spell);
		getProperties().setAutocastSpell(spell);
		getProperties().getCombatPulse().setStyle(CombatStyle.MAGIC);
	}

	@Override
	public void handleTickActions() {
		super.handleTickActions();
		if (getProperties().getCombatPulse().isAttacking() && RandomFunction.random(20) < 6) {
			sendChat(getWrathMessage());
		} else if (getProperties().getCombatPulse().isInCombat() && RandomFunction.random(60) < 10) {
			sendChat(getHailMessage());
		}
	}

	@Override
	public boolean canSelectTarget(Entity target) {
		if (target instanceof Player) {
			Player p = (Player) target;
			if (type.isFriendly(p)) {
				return false;
			}
			if (p.getZoneMonitor().isInZone("mage arena")) {
				if (MageArenaPlugin.MAGE_ARENA.hasSession(p)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Gets the hail message.
	 * @return the message.
	 */
	public String getHailMessage() {
		return "Hail " + type.getName() + "!";
	}

	/**
	 * Gets the wrath message.
	 * @return the message.
	 */
	public String getWrathMessage() {
		return "Feel the wrath of " + type.getName() + ".";
	}

	@Override
	public int[] getIds() {
		// sara, zammy, guthix
		return new int[] { 913, 912, 914 };
	}
}

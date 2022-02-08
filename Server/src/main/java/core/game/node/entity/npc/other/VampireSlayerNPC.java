package core.game.node.entity.npc.other;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.combat.CombatStyle;
import core.game.node.entity.npc.AbstractNPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.tools.RandomFunction;

/**
 * Handles the Vampie Slayer npc.
 * @author 'Vexia
 */
@Initializable
public class VampireSlayerNPC extends AbstractNPC {

	/**
	 * Represents the stake item
	 */
	private static final Item STAKE = new Item(1549);

	/**
	 * Represents the hammer item.
	 */
	private static final Item HAMMER = new Item(2347);

	/**
	 * Represents the garlic item.
	 */
	private static final Item GARLIC = new Item(1550);

	/**
	 * Represents the locations that a candle is at.
	 */
	private static final Location[] CANDLE_LOCATION = new Location[] { Location.create(3076, 9772, 0), Location.create(3079, 9772, 0), Location.create(3075, 9778, 0), Location.create(3080, 9778, 0) };

	/**
	 * Represents the force chat that the player will say if on fire.
	 */
	private static final String[] FORCE_CHAT = new String[] { "Eeek!", "Oooch!", "Gah!", "Ow!" };

	/**
	 * The NPC ids of NPCs using this plugin.
	 */
	private static final int[] ID = { 757 };

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 */
	public VampireSlayerNPC() {
		super(0, null);
	}

	/**
	 * Constructs a new {@code AlKharidWarriorPlugin} {@code Object}.
	 * @param id The NPC id.
	 * @param location The location.
	 */
	private VampireSlayerNPC(int id, Location location) {
		super(id, location);
	}

	@Override
	public AbstractNPC construct(int id, Location location, Object... objects) {
		return new VampireSlayerNPC(id, location);
	}

	@Override
	public void init() {
		super.init();
		getSkills().setLifepoints(40);
		getSkills().setStaticLevel(Skills.HITPOINTS, 40);
		getSkills().setLevel(Skills.HITPOINTS, 40);
	}

	@Override
	public void tick() {
		final Player p = getAttribute("player", null);
		if (p != null) {
			if (p.getLocation().getDistance(getLocation()) >= 16) {
				clear();
				return;
			}
			if (!getProperties().getCombatPulse().isAttacking()) {
				getProperties().getCombatPulse().attack(p);
			}
			if (p.getProperties().getCombatPulse().isAttacking() && p.getProperties().getCombatPulse().getVictim() == this) {
				for (Location l : CANDLE_LOCATION) {
					if (p.getLocation().equals(l)) {
						p.sendChat(FORCE_CHAT[RandomFunction.random(FORCE_CHAT.length)]);
						p.getPacketDispatch().sendMessage("The candles burn your feet!");
						break;
					}
				}
				if (!p.getInventory().containsItem(HAMMER) || !p.getInventory().containsItem(STAKE)) {
					getSkills().heal(10);
				}
				if (RandomFunction.random(7) == 2) {
					getSkills().heal(RandomFunction.random(1, !p.getInventory().containsItem(GARLIC) ? 12 : 2));
				}
			}
		}
		super.tick();
	}

	@Override
	public void onImpact(Entity entity, BattleState state) {
		if (entity instanceof Player) {
			final Player p = ((Player) entity);
			if (!p.getInventory().containsItem(HAMMER) || !p.getInventory().containsItem(STAKE)) {
				getSkills().heal(10);
			}
		}
		super.onImpact(entity, state);
	}

	@Override
	public void finalizeDeath(final Entity killer) {
		setRespawn(false);
		super.finalizeDeath(killer);
		if (!(killer instanceof Player)) {
			return;
		}
		final Player p = ((Player) killer);
		final Quest quest = p.getQuestRepository().getQuest("Vampire Slayer");
		if (p.getInventory().containsItem(HAMMER) && p.getInventory().remove(STAKE)) {
			if (quest.getStage(p) == 30) {
				quest.finish(p);
				p.getPacketDispatch().sendMessage("You hammer the stake into the vampire's chest!");
			}
		} else {
			p.getPacketDispatch().sendMessage("The vampire returns to his coffin. Next time use a stake and hammer.");
		}
		setRespawn(false);
	}

	@Override
	public void checkImpact(BattleState state) {
		if (state.getAttacker() instanceof Player) {
			Player p = (Player) state.getAttacker();
			if (!p.getInventory().containsItem(HAMMER) || !p.getInventory().containsItem(STAKE)) {
				if (state.getEstimatedHit() > -1) {
					state.setEstimatedHit(0);
				}
				if (state.getSecondaryHit() > -1) {
					state.setSecondaryHit(0);
				}
			}
		}
	}

	@Override
	public boolean isAttackable(final Entity entity, final CombatStyle style, boolean message) {
		final Player player = ((Player) entity);
		final Player pl = getAttribute("player", null);
		return pl == null ? false : pl == player ? true : false;
	}

	@Override
	public int[] getIds() {
		return ID;
	}

}

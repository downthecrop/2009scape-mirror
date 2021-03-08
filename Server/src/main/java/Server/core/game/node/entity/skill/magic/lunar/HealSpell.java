package core.game.node.entity.skill.magic.lunar;

import core.plugin.Initializable;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.magic.MagicSpell;
import core.game.node.entity.skill.magic.Runes;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler.HitsplatType;
import core.game.node.entity.combat.equipment.SpellType;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.SpellBookManager.SpellBook;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Plugin;

import java.util.Iterator;
import java.util.List;

/**
 * Represents the healing spell.
 * @author 'Vexia
 * @version 1.0
 */
@Initializable
public final class HealSpell extends MagicSpell {

	/**
	 * Represents the animation for this spell.
	 */
	private static final Animation ANIMATION = new Animation(4411);

	/**
	 * Represents the graphics of this spell.
	 */
	private static final Graphics GRAPHICS = new Graphics(738, 90);

	/**
	 * Represents the animation used for the group heal.
	 */
	private static final Animation ANIMATION_G = new Animation(1979);

	/**
	 * Represents the graphics used for the group heal.
	 */
	private static final Graphics GRAPHICS_G = new Graphics(734, 90);

	/**
	 * Constructs a new {@code HealSpell} {@code Object}.
	 */
	public HealSpell() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code HealSpell} {@code Object}.
	 */
	public HealSpell(int level, int experience, Item[] runes) {
		super(SpellBook.LUNAR, level, experience, null, null, null, runes);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(29, new HealSpell(92, 101, new Item[] { new Item(Runes.BLOOD_RUNE.getId(), 1), new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3) }));
		SpellBook.LUNAR.register(30, new HealSpell(92, 101, new Item[] { new Item(Runes.BLOOD_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 6), new Item(Runes.ASTRAL_RUNE.getId(), 4) }));
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = (Player) entity;
		boolean group = getSpellId() == 30;
		int eleven = (int) (player.getSkills().getStaticLevel(Skills.HITPOINTS) * 0.11);
		if (player.getSkills().getLifepoints() < eleven) {
			player.sendMessage("You need at least 11 percent of your original hitpoints in order to do this.");
			return false;
		}
		if (!group) {
			if (!(target instanceof Player)) {
				return false;
			}
			final Player o = (Player) target;
			player.face(o);
			if (!o.isActive() || o.getLocks().isInteractionLocked()) {
				player.getPacketDispatch().sendMessage("This player is busy.");
				return false;
			}
			if (!o.getSettings().isAcceptAid()) {
				player.getPacketDispatch().sendMessage("The player is not accepting any aid.");
				return false;
			}
			if (o.getSkills().getLifepoints() == o.getSkills().getLevel(Skills.HITPOINTS)) {
				player.getPacketDispatch().sendMessage("The player already has full hitpoints.");
				return false;
			}
			if (!super.meetsRequirements(player, true, true)) {
				return false;
			}
			int transfer = (int) (player.getSkills().getLifepoints() * 0.75);
			player.getImpactHandler().manualHit(player, transfer, null);
			o.getSkills().heal(transfer);
			player.animate(ANIMATION);
			o.graphics(GRAPHICS);
		} else {
			List<Player> players = RegionManager.getLocalPlayers(player, 1);
			if (!super.meetsRequirements(player, true, true)) {
				return false;
			}
			int percentage = (int) Math.ceil(player.getSkills().getLifepoints() * 0.75);
			for (Iterator<Player> it = players.iterator(); it.hasNext();) {
				Player p = it.next();
				if (p == player || !p.getSettings().isAcceptAid() || !p.isActive() || p.getSkills().getLifepoints() == p.getSkills().getMaximumLifepoints()) {
					it.remove();
				}
			}
			if (players.isEmpty()) {
				player.getPacketDispatch().sendMessage("There are no players around to replenish.");
				return false;
			}
			if (percentage < 1) {
				player.getPacketDispatch().sendMessage("You don't have enough hitpoints left to cast this spell.");
				return false;
			}
			player.getImpactHandler().manualHit(player, percentage, HitsplatType.NORMAL);
			player.animate(ANIMATION_G);
			for (Player p : players) {
				p.graphics(GRAPHICS_G);
				p.getSkills().heal(percentage);
			}
		}
		return true;
	}

}

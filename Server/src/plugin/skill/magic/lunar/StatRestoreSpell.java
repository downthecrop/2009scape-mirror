package plugin.skill.magic.lunar;

import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Drink;
import org.crandor.game.content.global.consumable.Potion;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

import java.util.List;

/**
 * The stat restore spell.
 * @author 'Vexia
 */
@InitializablePlugin
public class StatRestoreSpell extends MagicSpell {

	/**
	 * Represents the animation of this spell.
	 */
	private static final Animation ANIMATION = new Animation(4413);

	/**
	 * Represents the graphics.
	 */
	private static final Graphics GRAPHICS = new Graphics(733, 130);

	/**
	 * The ids of the spell.
	 */
	private static final int[] IDS = new int[] { 2430, 127, 129, 131, 3024, 3026, 3028, 3030 };

	/**
	 * Constructs a new {@code StatRestoreSpell} {@code Object}.
	 */
	public StatRestoreSpell() {
		super(SpellBook.LUNAR, 81, 84, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.EARTH_RUNE.getId(), 10), new Item(Runes.WATER_RUNE.getId(), 10) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(27, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		Item item = ((Item) target);
		final Drink drink = Consumables.forDrink(item);
		player.getInterfaceManager().setViewedTab(6);
		if (drink == null || !(drink instanceof Potion)) {
			player.getPacketDispatch().sendMessage("For use of this spell use only one a potion.");
			return false;
		}
		if (!item.getDefinition().isTradeable() || !isRestore(item.getId())) {
			player.getPacketDispatch().sendMessage("You can't cast this spell on that item.");
			return false;
		}
		final Potion potion = (Potion) drink;
		List<Player> pl = RegionManager.getLocalPlayers(player, 1);
		int plSize = pl.size() - 1;
		int doses = potion.getDose(item);
		if (plSize > doses) {
			player.getPacketDispatch().sendMessage("You don't have enough doses.");
			return false;
		}
		if (doses > plSize) {
			doses = plSize;
		}
		if (pl.size() == 0) {
			return false;
		}
		if (!super.meetsRequirements(player, true, false)) {
			return false;
		}
		int size = 1;
		for (Player players : pl) {
			Player o = (Player) players;
			if (!o.isActive() || o.getLocks().isInteractionLocked() || o == player) {
				continue;
			}
			if (!o.getSettings().isAcceptAid() && !(o instanceof AIPlayer)) {
				continue;
			}
			o.graphics(GRAPHICS);
			potion.effect(o, item);
			size++;
		}
		if (size == 1) {
			player.getPacketDispatch().sendMessage("There is nobody around that has accept aid on to share the potion with you.");
			return false;
		}
		super.meetsRequirements(player, true, true);
		potion.effect(player, item);
		potion.message(player, item, player.getSkills().getLifepoints());
		player.animate(ANIMATION);
		player.graphics(GRAPHICS);
		potion.remove(player, item, size - 1, true);
		return true;
	}

	/**
	 * Checks if it is a restore item.
	 * @param id the id.
	 * @return {@code True} if so.
	 */
	private boolean isRestore(int id) {
		for (int i : IDS) {
			if (i == id) {
				return true;
			}
		}
		return false;
	}

}

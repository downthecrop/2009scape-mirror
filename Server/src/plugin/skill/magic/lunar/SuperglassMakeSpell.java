package plugin.skill.magic.lunar;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.game.world.update.flag.context.Graphics;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * The super glass make spell.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public class SuperglassMakeSpell extends MagicSpell {

	/**
	 * Represents the animation of this spell.
	 */
	private static final Animation ANIMATION = Animation.create(4413);

	/**
	 * Represents the graphics of this spell.
	 */
	private static final Graphics GRAPHIC = new Graphics(729, 120);

	/**
	 * Represents the bucket of sand item.
	 */
	private static final Item BUCKET_OF_SAND = new Item(1783);

	/**
	 * Represents the molten glass item.
	 */
	private static final Item MOLTEN_GLASS = new Item(1775);

	/**
	 * The set of items to be used with sand.
	 */
	private static final Item[] SETS = new Item[] { new Item(1781), new Item(401), new Item(10978) };

	/**
	 * Constructs a new {@code SuperglassMakeSpell} {@code Object}.
	 */
	public SuperglassMakeSpell() {
		super(SpellBook.LUNAR, 77, 78, null, null, null, new Item[] { new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.FIRE_RUNE.getId(), 6), new Item(Runes.AIR_RUNE.getId(), 10) });
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.LUNAR.register(25, this);
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		int setIndex = getSetIndex(player);
		int sand = player.getInventory().getAmount(BUCKET_OF_SAND);
		if (setIndex == -1) {
			player.getPacketDispatch().sendMessage("You don't have the required ingredients.");
			return false;
		}
		if (!super.meetsRequirements(player, true, true)) {
			return false;
		}
		player.lock(4);
		player.graphics(GRAPHIC);
		player.animate(ANIMATION);
		for (int i = 0; i < sand; i++) {
			if (hasSet(player, setIndex)) {
				if (player.getInventory().remove(BUCKET_OF_SAND, SETS[setIndex])) {
					player.getInventory().add(MOLTEN_GLASS);
					player.getSkills().addExperience(Skills.CRAFTING, 10, true);
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the player has the set.
	 * @param player the player.
	 * @param index the index.
	 * @return {@code True} if so.
	 */
	public boolean hasSet(final Player player, final int index) {
		return player.getInventory().containsItem(BUCKET_OF_SAND) && player.getInventory().containsItem(SETS[index]);
	}

	/**
	 * Gets the set index.
	 * @param player the player.
	 * @return the index.s
	 */
	public int getSetIndex(final Player player) {
		if (!player.getInventory().containsItem(BUCKET_OF_SAND)) {
			return -1;
		}
		for (int i = 0; i < SETS.length; i++) {
			if (player.getInventory().containsItem(SETS[i])) {
				return i;
			}
		}
		return -1;
	}
}

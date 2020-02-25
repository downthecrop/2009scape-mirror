package plugin.skill.magic;

import org.crandor.ServerConstants;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.entity.player.link.diary.DiaryType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;

/**
 * Represents the plugin to handle all teleport spells in the modern book.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class ModernTeleportPlugin extends MagicSpell {

	/**
	 * Represents the location to teleport to.
	 */
	private Location location;

	/**
	 * Constructs a new {@code ModernTeleportPlugin} {@code Object}.
	 */
	public ModernTeleportPlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code ModernTeleportPlugin.java} {@code Object}.
	 * @param level the level.
	 * @param experience the experience.
	 * @param animation the animation.
	 * @param items the items.
	 */
	public ModernTeleportPlugin(final int level, final double experience, final Location location, final Item... items) {
		super(SpellBook.MODERN, level, experience, null, null, null, items);
		this.location = location;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		if (entity.getLocks().isTeleportLocked() || !super.meetsRequirements(entity, true, false)) {
			return false;
		}
		if (entity.getTeleporter().send(location.transform(0, RandomFunction.random(3), 0), getSpellId() == 0 ? TeleportType.HOME : TeleportType.NORMAL)) {
			if (!super.meetsRequirements(entity, true, true)) {
				entity.getTeleporter().getCurrentTeleport().stop();
				return false;
			}
			if (entity.isPlayer() && location.getX() == 3213 && !entity.asPlayer().getAchievementDiaryManager().getDiary(DiaryType.VARROCK).isComplete(1, 4)) {
				entity.asPlayer().getAchievementDiaryManager().getDiary(DiaryType.VARROCK).updateTask(entity.asPlayer(), 1, 4, true);
			}
			if (entity.isPlayer() && location.getX() == 3221 && location.getY() == 3219) {
				Player player = entity.asPlayer();
				if (!player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(1, 2)) {
					player.getAchievementDiaryManager().updateTask(player, DiaryType.LUMBRIDGE, 1, 2, true);
				}
			}
			entity.setAttribute("teleport:items", super.runes);
			entity.setAttribute("magic-delay", GameWorld.getTicks() + 5);
			return true;
		}
		return false;
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		// home
		SpellBook.MODERN.register(0, new ModernTeleportPlugin(0, 0, ServerConstants.HOME_LOCATION));
		// varrock
		SpellBook.MODERN.register(15, new ModernTeleportPlugin(25, 35, Location.create(3213, 3424, 0), new Item(Runes.FIRE_RUNE.getId()), new Item(Runes.AIR_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 1)));
		// lumby
		SpellBook.MODERN.register(18, new ModernTeleportPlugin(31, 41, Location.create(3221, 3219, 0), new Item(Runes.EARTH_RUNE.getId()), new Item(Runes.AIR_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 1)));
		// fally
		SpellBook.MODERN.register(21, new ModernTeleportPlugin(37, 47, Location.create(2965, 3378, 0), new Item(Runes.WATER_RUNE.getId()), new Item(Runes.AIR_RUNE.getId(), 3), new Item(Runes.LAW_RUNE.getId(), 1)));
		// camelot
		SpellBook.MODERN.register(26, new ModernTeleportPlugin(45, 55.5, Location.create(2758, 3478, 0), new Item(Runes.AIR_RUNE.getId(), 5), new Item(Runes.LAW_RUNE.getId(), 1)));
		// house
		SpellBook.MODERN.register(23, new HouseTeleportPlugin(40, 50, new Item(Runes.LAW_RUNE.getId()), new Item(Runes.AIR_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 1)));
		// ardougne
		SpellBook.MODERN.register(32, new ModernTeleportPlugin(51, 61, Location.create(2662, 3307, 0), new Item(Runes.WATER_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2)));
		// watchtower
		SpellBook.MODERN.register(37, new ModernTeleportPlugin(58, 68, Location.create(2549, 3112, 0), new Item(Runes.EARTH_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2)));
		// trollheim
		SpellBook.MODERN.register(44, new ModernTeleportPlugin(61, 68, Location.create(2891, 3678, 0), new Item(Runes.FIRE_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2)));
		// ape atol
		SpellBook.MODERN.register(47, new ModernTeleportPlugin(64, 74, Location.create(2754, 2784, 0), new Item(Runes.FIRE_RUNE.getId(), 2), new Item(Runes.WATER_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2), new Item(1963, 1)));
		return this;
	}

}

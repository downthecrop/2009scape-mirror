package plugin.skill.magic;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.magic.MagicSpell;
import org.crandor.game.content.skill.free.magic.Runes;
import org.crandor.game.content.skill.free.smithing.smelting.Bar;
import org.crandor.game.content.skill.free.smithing.smelting.SmeltingPulse;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.combat.equipment.SpellType;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.crandor.game.node.item.Item;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;

/**
 * Represents the super heat spell.
 * @author Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class SuperheatSpell extends MagicSpell {

	/**
	 * Constructs a new {@code SuperheatSpell} {@code Object}.
	 */
	public SuperheatSpell() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code SuperheatSpell} {@code Object}.
	 * @param level the level.
	 * @param runes the runes.
	 */
	public SuperheatSpell(int level, Item... runes) {
		super(SpellBook.MODERN, level, 53.0, null, null, null, runes);
	}

	@Override
	public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
		SpellBook.MODERN.register(25, new SuperheatSpell(43, new Item(Runes.FIRE_RUNE.getId(), 4), new Item(Runes.NATURE_RUNE.getId(), 1)));
		return this;
	}

	@Override
	public boolean cast(Entity entity, Node target) {
		final Player player = ((Player) entity);
		final Item item = ((Item) target);
		player.getInterfaceManager().setViewedTab(6);
		if (player.inCombat()) {
			player.getPacketDispatch().sendMessage("You can't do that during combat.");
			return false;
		}
		if (!item.getName().contains("ore") && !item.getName().toLowerCase().equals("coal")) {
			player.getPacketDispatch().sendMessage("You need to cast superheat item on ore.");
			return false;
		}
		final Bar bar = Bar.forOre(item.getId());
		if (bar == null) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevel()) {
			player.sendMessage("You need a Smithing level of at least " + bar.getLevel() + " to do this.");
			return false;
		}
		for (Item items : bar.getOres()) {
			if (!player.getInventory().contains(items.getId(), item.getAmount())) {
				player.getPacketDispatch().sendMessage("You do not have the required ores to make this bar.");
				return false;
			}
		}
		if (!super.meetsRequirements(entity, true, true)) {
			return false;
		}
		player.getAudioManager().send(117);
		player.getPulseManager().run(new SmeltingPulse(player, item, bar, 1, true));
		return true;
	}

}

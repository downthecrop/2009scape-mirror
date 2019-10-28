package plugin.interaction.item.withobject;

import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.free.smithing.SmithingBuilder;
import org.crandor.game.content.skill.free.smithing.smelting.Bar;
import org.crandor.game.interaction.NodeUsageEvent;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.interaction.UseWithHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.game.node.object.GameObject;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.PluginManager;

/**
 * Represents the option handler used for smithing.
 * @author 'Vexia
 * @version 1.0
 */
@InitializablePlugin
public final class SmithingPlugin extends UseWithHandler {

	/**
	 * The hammer item.
	 */
	private static final Item HAMMER = new Item(2347);

	/**
	 * Constructs a new {@code SmithingPlugin} {@code Object}.
	 */
	public SmithingPlugin() {
		super(2349, 2351, 2353, 2359, 2361, 2363, 2366, 2368, 9467, 11286, 1540, 11710, 11712, 11714, 11686, 11688, 11692);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(2782, OBJECT_TYPE, this);
		addHandler(2783, OBJECT_TYPE, this);
		addHandler(4306, OBJECT_TYPE, this);
		addHandler(6150, OBJECT_TYPE, this);
		addHandler(22725, OBJECT_TYPE, this);
		addHandler(26817, OBJECT_TYPE, this);
		addHandler(37622, OBJECT_TYPE, this);
		addHandler(42027, OBJECT_TYPE, this);
		PluginManager.definePlugin(new OptionHandler() {

			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				ObjectDefinition.forId(42027).getConfigurations().put("option:smith", this);
				return this;
			}

			@Override
			public boolean handle(Player player, Node node, String option) {
				SmithingPlugin.this.handle(new NodeUsageEvent(player, 10, HAMMER, node));
				return true;
			}

		});
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		final Player player = event.getPlayer();
		if (((GameObject) event.getUsedWith()).getId() == 2782 && !player.getQuestRepository().isComplete("Doric's Quest")) {
			player.getDialogueInterpreter().sendDialogue("Property of Doric the Dwarf.");
			return true;
		}
		if (!player.getInventory().contains(2347, 1)) {
			player.getDialogueInterpreter().sendDialogue("You need a hammer to work the metal with.");
			return true;
		} else {
			if (event.getUsedItem().getId() == 2366 || event.getUsedItem().getId() == 2368) {
				if (player.getSkills().getLevel(Skills.SMITHING) < 60) {
					player.getDialogueInterpreter().sendDialogue("You need a smithing level of 60 to smith this.");
					return true;
				}
				if (!player.getInventory().contains(2366, 1) && !player.getInventory().contains(2368, 1)) {
					player.getDialogueInterpreter().sendDialogue("You need a shield left half and a shield right half.");
					return true;
				}
				player.getDialogueInterpreter().open(82127843, 1, event.getUsedItem().getId());
				return true;
			}
			if (event.getUsedItem().getId() == 11286 || event.getUsedItem().getId() == 1540) {
				if (player.getSkills().getLevel(Skills.SMITHING) < 90) {
					player.getDialogueInterpreter().sendDialogue("You need a smithing level of 90 to smith this.");
					return true;
				}
				if (!player.getInventory().contains(1540, 1) && !player.getInventory().contains(11286, 1)) {
					player.getDialogueInterpreter().sendDialogue("You need a draconic visage and a anti-dragon shield.");
					return true;
				}
				player.getDialogueInterpreter().open(82127843, 2, event.getUsedItem().getId());
				return true;
			}
			if (event.getUsedItem().getId() == 11710 || event.getUsedItem().getId() == 11712 || event.getUsedItem().getId() == 11714 || event.getUsedItem().getId() == 11686 || event.getUsedItem().getId() == 11688 || event.getUsedItem().getId() == 11692) {
				if (player.getSkills().getLevel(Skills.SMITHING) < 80) {
					player.getDialogueInterpreter().sendDialogue("You need a smithing level of 80 to smith this.");
					return true;
				}
				player.getDialogueInterpreter().open(62362, event.getUsedItem().getId());
				return true;
			}
			Bar bar = Bar.forId(event.getUsedItem().getId());
			Item item = event.getUsedItem();
			if (event.getUsedItem().getId() == HAMMER.getId()) {
				for (Item i : player.getInventory().toArray())	{
					if (i == null) {
						continue;
					}
					bar = Bar.forId(i.getId());
					if (bar != null) {
						item = i;
						break;
					}
				}
			}
			if (bar == null) {
				return true;
			}
			if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevel()) {
				player.getDialogueInterpreter().sendDialogue("You need a smithing level of at leat " + bar.getLevel() + " to work " + bar.getProduct().getName().toLowerCase() + "s.");
				return true;
			}
			final SmithingBuilder builder = new SmithingBuilder(item);
			builder.build(player);
		}
		return true;
	}

}

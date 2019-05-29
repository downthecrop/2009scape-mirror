package plugin.activity.mta;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.cache.def.impl.NPCDefinition;
import org.crandor.cache.def.impl.ObjectDefinition;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.content.skill.member.agility.AgilityHandler;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.GroundItem;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.ZoneBuilder;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.plugin.Plugin;
import org.crandor.plugin.PluginManager;

import org.crandor.plugin.InitializablePlugin;
import plugin.activity.mta.impl.TelekineticZone;

/**
 * Handles the mage training area interactions.
 * @author Vexia
 */
@InitializablePlugin
public class MageTrainingArenaPlugin extends OptionHandler {

	/**
	 * The progress hat.
	 */
	public static final Item PROGRESS_HAT = new Item(6885);

	/**
	 * The mage training arena shop.
	 */
	public static final MTAShop SHOP = new MTAShop();

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		ItemDefinition.forId(6885).getConfigurations().put("option:destroy", this);
		ItemDefinition.forId(6885).getConfigurations().put("option:talk-to", this);
		ObjectDefinition.forId(10721).getConfigurations().put("option:enter", this);
		NPCDefinition.forId(3103).getConfigurations().put("option:trade-with", this);
		for (MTAType type : MTAType.values()) {
			if (type.getZone() != null) {
				ZoneBuilder.configure(type.getZone());
			}
			ObjectDefinition.forId(type.getObjectId()).getConfigurations().put("option:enter", this);
		}
		ItemDefinition.forId(TelekineticZone.STATUE).getConfigurations().put("option:observe", this);
		ItemDefinition.forId(TelekineticZone.STATUE).getConfigurations().put("option:reset", this);
		NPCDefinition.forId(3102).getConfigurations().put("option:talk-to", this);
		PluginManager.definePlugin(new CharmedWarriorDialogue(), new EntranceGuardianDialogue(), new RewardsGuardianDialogue(), new ProgressHatDialogue(), new EnchantmentGuardianDialogue(), new EnchantSpell(), new BonesConvertingSpells(), new GraveyardGuardianDialogue(), new AlchemyGuardianDialogue(), new AlchemySpell(), new TelekineticGrabSpell(), new TelekineticGuardianDialogue(), new MazeGuardianDialogue());
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		switch (node.getId()) {
		case 10721:
			if (!player.getSkills().hasLevel(Skills.MAGIC, 7)) {
				player.getDialogueInterpreter().sendDialogue("You need a Magic level of at least 7 to enter the guild.");
				break;
			}
			AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(Direction.getDirection(player.getLocation(), node.getLocation()), 2), new Animation(1426), 0.0, null);
			break;
		case 3103:
			if (!player.getSavedData().getActivityData().isStartedMta()) {
				player.getDialogueInterpreter().open(3103, this, true, true);
			} else {
				SHOP.open(player);
			}
			break;
		case 6885:
			player.getDialogueInterpreter().open(3096, option.equals("destroy") ? new Object[] { node, true, true } : new Object[] {});
			break;
		case 3102:
			player.getDialogueInterpreter().open(node.getId(), node);
			break;
		}
		switch (option) {
		case "enter":
			MTAType type = MTAType.forId(node.getId());
			if (type != null) {
				type.enter(player);
			}
			break;
		case "reset":
		case "observe":
			TelekineticZone zone = TelekineticZone.getZone(player);
			if (option.equals("reset")) {
				zone.reset(player);
			} else {
				zone.observe(player);
			}
			break;
		}
		return true;
	}

	@Override
	public boolean isWalk(Player player, Node n) {
		if (!(n instanceof GroundItem)) {
			return true;
		}
		if (n.getId() == TelekineticZone.STATUE) {
			return false;
		}
		return true;
	}

	@Override
	public Location getDestination(Node node, Node n) {
		if (n.getId() == 3102) {
			return n.getLocation().transform(Direction.getDirection(node.getLocation(), n.getLocation()), -1);
		}
		return null;
	}

	@Override
	public boolean isWalk() {
		return false;
	}
}

package content.region.misthalin.varrock.quest.demonslayer;

import core.cache.def.impl.NPCDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.global.action.ClimbActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.*;


/**
 * Represents the demon slayer plugin used to handle relative node interactions.
 * @author 'Vexia
 * @date 3/1/14
 */
public final class DemonSlayerPlugin extends OptionHandler {

	/**
	 * Represents the object id of the drain.
	 */
	public static final int DRAIN_ID = 17424;

	/**
	 * Represents the location of the lumbridge sewers.
	 */
	private static final Location SEWER_LOCATION = new Location(3237, 9858, 0);

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		SceneryDefinition.forId(DRAIN_ID).getHandlers().put("option:search", this);
		SceneryDefinition.forId(17429).getHandlers().put("option:take", this);
		NPCDefinition.forId(DemonSlayerCutscene.DELRITH).getHandlers().put("option:attack", this);
		NPCDefinition.forId(DemonSlayerCutscene.WEAKENED_DELRITH).getHandlers().put("option:banish", this);
		return this;
	}

	@Override
	public boolean handle(Player player, Node node, String option) {
		final Quest quest = player.getQuestRepository().getQuest("Demon Slayer");
		final int id = node instanceof Scenery ? ((Scenery) node).getId() : node instanceof Item ? ((Item) node).getId() : ((NPC) node).getId();
		switch (id) {
		case 880:
			player.getDialogueInterpreter().open(8427322);
			break;
		case 879:
			if (!player.getEquipment().containsItem(DemonSlayer.SILVERLIGHT)) {
				player.getPacketDispatch().sendMessage("I'd better wield Silverlight first.");
				return true;
			}
			player.face(((NPC) node));
			player.getProperties().getCombatPulse().attack(node);
			return true;
		case DRAIN_ID:
			if (quest.getStage(player) == 20) {
				player.getDialogueInterpreter().open(883, 883, "key");
				return true;
			} else {
				player.sendMessage("You search the castle drain and find nothing of value.");
			}
			return true;
		case 17429:
			if (quest.getStage(player) == 20 && player.getInventory().add(DemonSlayer.FIRST_KEY)) {
                                setVarp(player, 222, 4757762, true);
				player.removeAttribute("demon-slayer:poured");
				player.removeAttribute("demon-slayer:just-poured");
				player.getDialogueInterpreter().sendItemMessage(DemonSlayer.FIRST_KEY.getId(), "You pick up an old rusty key.");
			} else {
				if (player.getInventory().freeSlots() == 0) {
					player.getPacketDispatch().sendMessage("Not enough inventory space.");
					return true;
				}
			}
			break;
		}
		return true;
	}

	@Override
	public Location getDestination(final Node node, final Node n) {
		if (n instanceof NPC) {
			return n.getLocation().transform(2, 0, 0);
		}
		if (n.getId() == DRAIN_ID) {
			return Location.create(3226, 3496, 0);
		}
		return null;
	}

}

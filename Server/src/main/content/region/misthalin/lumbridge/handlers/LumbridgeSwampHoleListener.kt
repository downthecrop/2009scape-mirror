package content.region.misthalin.lumbridge.handlers

import core.api.*
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Scenery as Sceneries
import core.game.component.Component
import core.game.global.action.ClimbActionHandler
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation

class LumbridgeSwampHoleListener : InteractionListener {

	val CAVE_ENTRANCE = Sceneries.DARK_HOLE_5947
	val CAVE_EXIT_ROPE = Sceneries.CLIMBING_ROPE_5946
	val WARNING_SIGN = Sceneries.WARNING_SIGN_15566
	val TOOL_STORE_FULL = Sceneries.TOOLS_10375
	val TOOL_STORE_EMPTY = 10373

	override fun defineListeners() {
		on(CAVE_ENTRANCE, IntType.SCENERY, "climb-down") { player, _ ->
			if (!player.getSavedData().getGlobalData().hasTiedLumbridgeRope()) {
				sendDialogue(player, "There is a sheer drop below the hole. You will need a rope.")
			} else {
				val insideCave = Location.create(3168, 9572, 0)
				ClimbActionHandler.climb(player, Animation(Animations.HUMAN_BURYING_BONES_827), insideCave)
			}
			
			return@on true
		}
		on(CAVE_EXIT_ROPE, IntType.SCENERY, "climb") { player, _ ->
			val outsideCave = Location.create(3169, 3173, 0)
			player.getProperties().setTeleportLocation(outsideCave);
			return@on true
		}
		on(WARNING_SIGN, IntType.SCENERY, "read") { player, _ ->
			player.getPacketDispatch().sendString("<col=8A0808>~-~-~ WARNING ~-~-~", 220, 5);
			player.getPacketDispatch().sendString("<col=8A0808>Noxious gases vent into this cave.", 220, 7);
			player.getPacketDispatch().sendString("<col=8A0808>Naked flames may cause an explosion!", 220, 8);
			player.getPacketDispatch().sendString("<col=8A0808>Beware of vicious head-grabbing beasts!", 220, 10);
			player.getPacketDispatch().sendString("<col=8A0808>Contact a Slayer master for protective headgear.", 220, 11);
			player.getInterfaceManager().open(Component(220));
			return@on true
		}
		on(TOOL_STORE_FULL, IntType.SCENERY, "take") { player: Player, node: Node ->
			if(freeSlots(player) < 2) {
				sendDialogue(player, "You do not have enough inventory space.")
			}
			else if (player.getInventory().add(Item(Items.RAKE_5341), Item(Items.SPADE_952))) {
				SceneryBuilder.replace((node as Scenery), (node as Scenery).transform(TOOL_STORE_EMPTY), 300);
			}
			
			return@on true
		}
	}
}
package content.global.skill.construction.decoration.chapel

import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Items

/**
 * Handles the lighting of the torches of the Chapel.
 * @author Splinter
 */
class BurnerListener : InteractionListener {

    val IDs = intArrayOf(13202,13203,13204,13205,13206,13207,13208,13209,13210,13211,13212,13213)

    override fun defineListeners() {
        on(IDs, IntType.SCENERY, "light"){ player, node ->
            if (player.ironmanManager.checkRestriction() && !player.houseManager.isInHouse(player)) {
                return@on true
            }
            if (!player.inventory.containsItem(Item(Items.TINDERBOX_590)) || !player.inventory.containsItem(Item(Items.CLEAN_MARRENTILL_251))) {
                player.dialogueInterpreter.sendDialogue(
                    "You'll need a tinderbox and a clean marrentill herb in order to",
                    "light the burner."
                )
                return@on true
            }
            if (player.inventory.remove(Item(Items.CLEAN_MARRENTILL_251))) {
                player.lock(1)
                player.animate(Animation.create(3687))
                player.sendMessage("You burn some marrentill in the incense burner.")
                SceneryBuilder.replace(
                    node.asScenery(),
                        Scenery(node.asScenery().id + 1, node.location),
                    RandomFunction.random(100, 175)
                )
            }
            return@on true
        }
    }
}
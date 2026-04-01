package content.global.skill.construction.decoration.questhall

import content.data.EnchantedJewellery
import content.global.handlers.item.EnchantedJewelleryListener.EnchantedJewelleryDialogueFile
import core.api.delayScript
import core.api.lock
import core.api.openDialogue
import core.api.playGlobalAudio
import core.api.queueScript
import core.api.resetAnimator
import core.api.stopExecuting
import core.api.teleport
import core.api.visualize
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.Sounds

class MountedGlory : InteractionListener {
    val MOUNTED_GLORY = 13523
    val TELEPORTS = EnchantedJewellery.AMULET_OF_GLORY.locations

    override fun defineListeners() {
        on(MOUNTED_GLORY, IntType.SCENERY, "Edgeville") { player, `object` ->
            mountedGloryAction(player, `object`, 0)
            return@on true
        }
        on(MOUNTED_GLORY, IntType.SCENERY, "Karamja") { player, `object` ->
            mountedGloryAction(player, `object`, 1)
            return@on true
        }
        on(MOUNTED_GLORY, IntType.SCENERY, "Draynor Village") { player, `object` ->
            mountedGloryAction(player, `object`, 2)
            return@on true
        }
        on(MOUNTED_GLORY, IntType.SCENERY, "Al Kharid") { player, `object` ->
            mountedGloryAction(player, `object`, 3)
            return@on true
        }
        on(MOUNTED_GLORY, IntType.SCENERY, "Rub") { player, _ ->
            openDialogue(player,EnchantedJewelleryDialogueFile(EnchantedJewellery.AMULET_OF_GLORY, Item(Items.AMULET_OF_GLORY4_1712), false, false))
            return@on true
        }
    }
    private fun mountedGloryAction(player : Player, `object` : Node, int : Int) {
        if (player.houseManager.isBuildingMode) {
            player.dialogueInterpreter.open("con:removedec", `object` as Scenery)
            return
        }
        if (!player.zoneMonitor.teleport(1, Item(Items.AMULET_OF_GLORY_1704))) {
            return
        }
        queueScript(player, 0, QueueStrength.SOFT) { stage ->
            if (stage == 0) {
                lock(player, 4)
                visualize(player, Animation(714), Graphics(308, 100, 50))
                playGlobalAudio(player.location, Sounds.TELEPORT_ALL_200)
                return@queueScript delayScript(player, 3)
            }
            resetAnimator(player)
            teleport(player, TELEPORTS[int])
            return@queueScript stopExecuting(player)
        }
    }
}

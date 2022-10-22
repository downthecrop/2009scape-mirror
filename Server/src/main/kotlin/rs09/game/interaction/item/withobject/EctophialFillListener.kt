package rs09.game.interaction.item.withobject

import api.*
import core.game.node.entity.player.link.audio.Audio
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener

/**
 * Listener for filling empty ectophial
 * @author Byte
 */
@Suppress("unused")
class EctophialFillListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(1652)
        private val AUDIO = Audio(1132)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, Items.ECTOPHIAL_4252, Scenery.ECTOFUNTUS_5282) { player, used, _ ->
            lock(player, 5)
            animate(player, ANIMATION)
            playAudio(player, AUDIO)

            submitIndividualPulse(player, object: Pulse(3) {
                override fun pulse(): Boolean {
                    if (removeItem(player, used)) {
                        addItem(player, Items.ECTOPHIAL_4251)
                        sendMessage(player, "You refill the ectophial from the Ectofuntus.")
                        unlock(player)
                        return true
                    }
                    return false
                }
            })

            return@onUseWith true
        }
    }
}

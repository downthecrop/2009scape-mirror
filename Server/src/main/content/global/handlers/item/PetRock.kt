package content.global.handlers.item

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile.getLocation
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class PetRockListener : InteractionListener {
    override fun defineListeners() {
        on(Items.PET_ROCK_3695, IntType.ITEM, "interact") { player, _ ->
            openDialogue(player, PetRockDialogue())
            return@on true
        }
    }
}

class PetRockDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> options("Talk", "Stroke", "Feed", "Fetch", "Stay").also { stage++ }
            1 -> when(buttonID) {
                1 -> {
                    sendPlayerDialogue(player!!, "Who's a good rock, then? Yes, you are! You're such a good rock. Ooga booga googa.", FacialExpression.FRIENDLY)
                    sendMessage(player!!, "Your rock seems a little happier.")
                    stage = END_DIALOGUE
                }
                2 -> {
                    end()
                    sendMessage(player!!, "You stroke your pet rock.")
                    animate(player!!, 1333, false)
                    //waits for the animation to finish before sending the message
                    queueScript(player!!, animationDuration(Animation(1333)), QueueStrength.SOFT) {
                        sendMessage(player!!, "Your rock seems much happier.")
                        return@queueScript stopExecuting(player!!)
                    }
                }
                3 -> {
                    end()
                    sendMessage(player!!, "You try and feed the rock.")
                    sendMessage(player!!, "Your rock doesn't seem hungry.")
                }
                4 -> {
                    sendPlayerDialogue(player!!, "Want to fetch the stick, rock? Of course you do.", FacialExpression.FRIENDLY)
                    face(player!!, Location.getRandomLocation(getLocation(player), 2, true))
                    visualize(player!!, 6665, 1157)
                    spawnProjectile(getLocation(player), Location.getRandomLocation(getLocation(player), 5, true), 1158, 40, 0,150, 250, 25)
                    stage = END_DIALOGUE
                }
                5 -> {
                    face(player!!, Location.getRandomLocation(getLocation(player), 2, true))
                    sendPlayerDialogue(player!!, "Be a good rock...", FacialExpression.FRIENDLY)
                    sendMessage(player!!, "You wait a few seconds and pick your rock back up and pet it.")
                    visualize(player!!, anim = 6664, gfx = 1156)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}
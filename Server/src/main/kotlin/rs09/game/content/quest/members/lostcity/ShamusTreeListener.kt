package rs09.game.content.quest.members.lostcity

import api.sendMessage
import api.sendNPCDialogue
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.world.GameWorld


/**
 * Shamus tree listener, to handle when a player chops Shamus's home tree,
 * and to handle some details about the Shamus NPC
 * @author lila
 * @author Vexia
 */
@Initializable
class ShamusTreeListener : InteractionListener {

    init {
        SHAMUS.init()
        SHAMUS.isWalks = true
        SHAMUS.isInvisible = true
    }

    companion object {
        val SHAMUS = NPC(NPCs.SHAMUS_654, Location(3138, 3211, 0))
        fun disappearShamus() {
            SHAMUS.isInvisible = true
        }
    }

    private fun handleShamusTree(player: Player): Boolean {
        if (SkillingTool.getHatchet(player) == null) {
            sendMessage(player,"You do not have an axe which you have the level to use.")
            return true
        }
        showShamus(player)
       return true
    }

    private fun showShamus(player: Player) {
        if(SHAMUS.isInvisible) {
            SHAMUS.isInvisible = false
            SHAMUS.properties.teleportLocation = SHAMUS.properties.spawnLocation
        }
        sendNPCDialogue(player,NPCs.SHAMUS_654,"Hey! Yer big elephant! Don't go choppin' down me house, now!",FacialExpression.FURIOUS)
        GameWorld.Pulser.submit(object : Pulse(100) {
            override fun pulse(): Boolean {
                if (SHAMUS.dialoguePlayer == null) {
                    SHAMUS.isInvisible = true
                    return true
                }
                return false
            }
        })
    }

    override fun defineListeners() {
        on(Scenery.TREE_2409, IntType.SCENERY, "chop") { player, _ ->
            handleShamusTree(player)
            return@on true
        }
    }
}
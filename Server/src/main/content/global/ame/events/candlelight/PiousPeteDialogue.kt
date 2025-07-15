package content.global.ame.events.candlelight

import content.global.ame.RandomEvents
import content.global.ame.events.pillory.PilloryInterface
import content.global.ame.returnPlayer
import core.api.*
import core.game.dialogue.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

// iface 178

@Initializable
class PiousPeteDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return PiousPeteDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, PiousPeteDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PIOUS_PETE_3207, NPCs._6564)
    }
}

class PiousPeteDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        npc(ChatAnim.THINKING, "Have you lit all the tall candles?")
        exec { player, npc ->
            if (CandlelightInterface.areCandlesLit(player)) {
                loadLabel(player, "yeslit")
            } else {
                loadLabel(player, "nolit")
            }
        }

        label("nolit")
        player(ChatAnim.HALF_GUILTY, "Sorry, not yet.")
        npc(ChatAnim.SAD, "Please help me in lighting the candles. I just need you to light all the tall candles, but not the short ones.")
        line("Click on the pillars to open an interface", "to move around and light the candles.")

        label("yeslit")
        player(ChatAnim.FRIENDLY, "Yes, the tall ones are all lit.")
        npc(ChatAnim.HAPPY, "Thank you my brother! I will now return you where you came from with a parting gift.")
        npc(ChatAnim.FRIENDLY, "Take care brother!")
        exec { player, npc ->
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        lock(player, 6)
                        sendGraphics(Graphics(1576, 0, 0), player.location)
                        animate(player,8939)
                        playAudio(player, Sounds.TELEPORT_ALL_200)
                        return@queueScript delayScript(player, 3)
                    }
                    1 -> {
                        returnPlayer(player)
                        sendGraphics(Graphics(1577, 0, 0), player.location)
                        animate(player,8941)
                        closeInterface(player)
                        return@queueScript delayScript(player, 3)
                    }
                    2 -> {
                        val loot = RandomEvents.CERTER.loot!!.roll(player)[0]
                        addItemOrDrop(player, loot.id, loot.amount)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }
}
class PiousPeteStartingDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        npc("I'm sorry to drag you away from your tasks, but I need a little help with something.")
        player(ChatAnim.THINKING,"How can I help?")
        npc("This is a chapel dedicated to our lord, Saradomin, and I'm tasked with maintaining this chapel.")
        npc(ChatAnim.SAD,"My task is to light the chapel candles, but I couldn't reach them myself and I kept getting dazzled by the light whenever I tried.")
        npc("So I need your help in lighting the candles. I need you to light all the tall candles, but not the short ones.")
        npc(ChatAnim.FRIENDLY, "Once all the tall candles are all lit, come back and see me, and I will reward you for your work.")
    }
}
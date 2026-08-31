package content.region.morytania.werewolfagility

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class SkullballTrainerDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return SkullballTrainerDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SkullballTrainerDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKULLBALL_TRAINER_1662)
    }
}
class SkullballTrainerDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.SKULLBALL_TRAINER_1662)

        exec { player, npc ->
            if(!anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) {
                goto("ishuman")
            } else if (getAttribute<NPC?>(player, SkullballCourse.attributeSkullballInstance, null) != null) {
                goto("skullballinprogress")
            } else {
                goto("noskullball")
            }
        }

        label("ishuman")
        npc(ChatAnim.WEREWOLF_SUSPICIOUS, "Grrr - you don't belong in here, human!")

        label("noskullball")
        player(ChatAnim.THINKING, "What is this place?")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "This is the Skullball Course")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "Go talk to the boss at the beginning of the course if you'd like a go.")

        // Eovq4QBY39c
        label("skullballinprogress")
        player(ChatAnim.THINKING, "How many goals have I got left?")
        exec { player, npc ->
            if (getAttribute(player, SkullballCourse.attributeSkullballCurrentGoal, 0) != 10) {
                goto("xgoals")
            } else {
                goto("finalgoal")
            }
        }

        label("xgoals")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "You have ${10 - getAttribute(player!!,
            SkullballCourse.attributeSkullballCurrentGoal, 0)} goals left to complete.")

        label("finalgoal")
        npc(ChatAnim.WEREWOLF_NEUTRAL, "You have the final goal left to complete.")
    }
}
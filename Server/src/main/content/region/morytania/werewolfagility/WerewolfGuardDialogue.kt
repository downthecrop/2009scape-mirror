package content.region.morytania.werewolfagility

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class WerewolfGuardDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, WerewolfGuardDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return WerewolfGuardDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WEREWOLF_1665)
    }
}

class WerewolfGuardDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "What's beneath the trapdoor?")
                .branch { player -> if (anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) { 1 } else { 0 } }
                .let { branch ->
                    branch.onValue(0)
                            .npcl(FacialExpression.WEREWOLF_NEUTRAL, "That's none of your business, human, and I'll never tell.")
                            .playerl(FacialExpression.FRIENDLY, "Oh, come on - I'm only curious.")
                            .npcl(FacialExpression.WEREWOLF_NEUTRAL, "If it wasn't my duty to stand here and guard our agility course from the likes of you, I would be relieving you of your life right now.")
                            .playerl(FacialExpression.THINKING, "So it's an agility course, then?")
                            .npcl(FacialExpression.WEREWOLF_SUSPICIOUS, "No ... yes ... oh blast - you didn't hear me say anything, right?")
                            .playerl(FacialExpression.FRIENDLY, "No problem. Can I come in?")
                            .npcl(FacialExpression.WEREWOLF_NEUTRAL, "No, human - it's werewolves only.")
                            .end()

                    branch.onValue(1)
                            .npcl(FacialExpression.WEREWOLF_NEUTRAL, "It's an agility course designed for lycanthropes like ourselves, my friend.")
                            .playerl(FacialExpression.FRIENDLY, "Can I come in and use it?")
                            .npc(FacialExpression.WEREWOLF_HAPPY, "Certainly. The cavern contains two courses - on the", "west side is a level 60 Agility Course, and on the east", "side is a level 25 Skullball Course.")
                            .end()
                }
    }
}
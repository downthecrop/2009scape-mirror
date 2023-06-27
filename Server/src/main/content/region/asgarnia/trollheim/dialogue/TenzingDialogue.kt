package content.region.asgarnia.trollheim.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.TenzingDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the dialogue plugin used for the tenzing npc.
 * @author 'Vexia
 * @author ovenbread
 */
@Initializable
class TenzingDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        if (isQuestInProgress(player!!, DeathPlateau.questName, 20, 29)) {
            openDialogue(player!!, TenzingDialogueFile(), npc)
            return true
        }
        // Fallback to default.
        when (stage) {
            0 -> player(FacialExpression.FRIENDLY, "Hello Tenzing!").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY,"Hello traveler. What can I do for you?").also{ stage++ }
            2 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "Can I buy some Climbing boots?", 10),
                Topic(FacialExpression.FRIENDLY, "What does a Sherpa do?", 20),
                Topic(FacialExpression.FRIENDLY, "How did you find out about the secret way?", 30),
                Topic(FacialExpression.FRIENDLY, "Nice place you have here.", 40),
                Topic(FacialExpression.FRIENDLY, "Nothing, thanks!", END_DIALOGUE)
            )
            10 -> npc("Sure, I'll sell you some in your size for 12 gold.").also{ stage++ }
            11 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "OK, sounds good.", 12),
                Topic(FacialExpression.FRIENDLY, "No, thanks.", END_DIALOGUE),
            )
            12 -> {
                if (!player.inventory.hasSpaceFor(Item(Items.CLIMBING_BOOTS_3105))) {
                    player("I don't have enough space in my backpack right this second.").also{ stage = END_DIALOGUE }
                }
                if (!inInventory(player, Items.COINS_995)) {
                    player("I don't have enough coins right now.").also{ stage = END_DIALOGUE }
                }
                if (removeItem(player!!, Item(Items.COINS_995, 12))) {
                    player("I don't have enough coins right now.").also{ stage = END_DIALOGUE }
                }
                addItemOrDrop(player, Items.CLIMBING_BOOTS_3105, 1)
                sendItemDialogue(player, Items.CLIMBING_BOOTS_3105, "Tenzing has given you some Climbing boots.").also { stage = END_DIALOGUE }
                sendMessage(player, "Tenzing has given you some Climbing boots.")
            }

            20 -> npcl(FacialExpression.FRIENDLY, "We are expert guides that take adventurers such as yourself, on mountaineering expeditions.").also { stage = END_DIALOGUE }
            30 -> npcl(FacialExpression.FRIENDLY, "I used to take adventurers up Death Plateau and further north before the trolls came. I know these mountains well.").also { stage = END_DIALOGUE }
            40 -> npcl(FacialExpression.FRIENDLY, "Thanks, I built it myself! I'm usually self sufficient but I can't earn any money with the trolls camped on Death Plateau,").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return TenzingDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TENZING_1071)
    }
}

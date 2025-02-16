package content.region.kandarin.ardougne.westardougne.dialogue

import content.region.kandarin.ardougne.westardougne.MournerUtilities
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs


val cats = intArrayOf(
    Items.PET_CAT_1561,
    Items.PET_CAT_1562,
    Items.PET_CAT_1563,
    Items.PET_CAT_1564,
    Items.PET_CAT_1565,
    Items.PET_CAT_1566,
    Items.OVERGROWN_CAT_1567,
    Items.OVERGROWN_CAT_1568,
    Items.OVERGROWN_CAT_1569,
    Items.OVERGROWN_CAT_1570,
    Items.OVERGROWN_CAT_1571,
    Items.OVERGROWN_CAT_1572,
    // todo implement these cats then uncomment this
    // otherwise you get an exception which just steals your cat for no runes
    // Items.LAZY_CAT_6549,
    // Items.LAZY_CAT_6550,
    // Items.LAZY_CAT_6551,
    // Items.LAZY_CAT_6552,
    // Items.LAZY_CAT_6553,
    // Items.LAZY_CAT_6554,
    // Items.WILY_CAT_6555,
    // Items.WILY_CAT_6556,
    // Items.WILY_CAT_6557,
    // Items.WILY_CAT_6558,
    // Items.WILY_CAT_6559,
    // Items.WILY_CAT_6560,
)

@Initializable
class CivilianDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object {
        // Any of these


        const val DIAG1 = 10
        const val DIAG2 = 20
        const val DIAG3 = 30

        const val NO_CAT = 40
        const val BUY_CATS = 50
        const val REJECT_DEAL = 80
        const val WITCH_CAT = 90

        const val MOURNER = 100
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (MournerUtilities.wearingMournerGear(player) > MournerUtilities.JUST_MASK){
            playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = MOURNER }
        }
        else {
            when (npc.id) {
                NPCs.CIVILIAN_785 -> playerl(FacialExpression.NEUTRAL, "Hello there.").also { stage = DIAG1 }
                NPCs.CIVILIAN_786 -> playerl(FacialExpression.NEUTRAL, "Hi there.").also { stage = DIAG2 }
                NPCs.CIVILIAN_787 -> player(FacialExpression.NEUTRAL, "Hello there.").also { stage = DIAG3 }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            DIAG1 -> npcl(FacialExpression.HALF_GUILTY, "Oh hello, I'm sorry, I'm a bit worn out.").also { stage++ }
            DIAG1 + 1 -> playerl(FacialExpression.NEUTRAL, "Busy day?").also { stage++ }
            DIAG1 + 2 -> npcl(FacialExpression.HALF_GUILTY, "Oh, It's those mice! They're everywhere! " +
                    "What I really need is a cat. But they're hard to come by nowadays.").also {
                        stage = checkCat()
                }

            DIAG2 -> npcl(FacialExpression.NEUTRAL, "Good day to you traveller.").also { stage++ }
            DIAG2 + 1 -> playerl(FacialExpression.NEUTRAL, "What are you up to?").also { stage++ }
            DIAG2 + 2 -> npcl(FacialExpression.NEUTRAL, "Chasing mice as usual! It's all I seem to do nowadays.").also { stage++ }
            DIAG2 + 3 -> playerl(FacialExpression.NEUTRAL, "You must waste a lot of time?").also { stage++ }
            DIAG2 + 4 -> npcl(FacialExpression.HALF_WORRIED, "Yes, but what can you do? It's not like there's many cats around here!").also{ stage = checkCat()
            }

            DIAG3 -> npcl(FacialExpression.HALF_WORRIED, "I'm a bit busy to talk right now, sorry.").also { stage++ }
            DIAG3 + 1 -> playerl(FacialExpression.NEUTRAL, "Why? What are you doing?").also { stage++ }
            DIAG3 + 2 -> npcl(FacialExpression.HALF_WORRIED, "Trying to kill these mice! What I really need is a cat!").also { stage = checkCat() }

            NO_CAT -> playerl(FacialExpression.HALF_WORRIED, "No, you're right, you don't see many around.").also { stage = END_DIALOGUE }

            BUY_CATS -> showTopics(
                Topic("I have a cat that I could sell.", BUY_CATS + 1),
                Topic("Nope, they're not easy to get hold of.", END_DIALOGUE)
            )
            // intentional whitespace typo
            BUY_CATS + 1 -> npcl(FacialExpression.ASKING, "You don't say, is that it ?").also { stage++ }
            BUY_CATS + 2 -> playerl(FacialExpression.HAPPY, "Say hello to a real mouse killer!").also { stage++ }
            BUY_CATS + 3 -> npcl(FacialExpression.HALF_ASKING, "Hmmm, not bad, not bad at all. Looks like it's a lively one.").also { stage++ }
            BUY_CATS + 4 -> playerl(FacialExpression.HALF_GUILTY, "Erm...kind of...").also { stage++ }
            BUY_CATS + 5 -> npcl(FacialExpression.FRIENDLY, "I don't have much in the way of money. I do have these!").also { stage++ }
            BUY_CATS + 6 -> sendDialogue("The peasant shows you a sack of Death Runes.").also { stage++ }
            BUY_CATS + 7 -> npcl(FacialExpression.ASKING, "The dwarves bring them from the mine for us. Tell you what, I'll give you 100 Death Runes for the cat.").also { stage++ }
            BUY_CATS + 8 -> showTopics(
                Topic("Nope, I'm not parting for that.", REJECT_DEAL),
                Topic("Ok then, you've got a deal.", BUY_CATS+9)
            )
            BUY_CATS + 9 -> {
                npcl(FacialExpression.HAPPY, "Great! Hand over the cat and I'll give you the runes.").also {
                    stage = END_DIALOGUE
                    for (cat in cats) {
                        if (removeItem(player, cat)){
                            player.familiarManager.removeDetails(cat)
                            addItem(player, Items.DEATH_RUNE_560, 100)
                            break
                        }
                    }
                }
            }

            REJECT_DEAL -> npcl(FacialExpression.HALF_GUILTY, "Well, I'm not giving you anymore!").also { stage = END_DIALOGUE }

            WITCH_CAT -> playerl(FacialExpression.HAPPY, "I have a cat...look!").also { stage++ }
            WITCH_CAT + 1 -> npcl(FacialExpression.HALF_WORRIED, "Hmmm...doesn't look like it's seen daylight in years. That's not going to catch any mice!").also { stage = END_DIALOGUE }


            MOURNER -> npcl(FacialExpression.ANGRY, "If you Mourners really wanna help, why don't you do something about these mice?!").also { stage = END_DIALOGUE }
        }
        return true
    }

    private fun checkCat(): Int {
        return if (anyInInventory(player, *cats)) BUY_CATS
        else if (inInventory(player, Items.WITCHS_CAT_1491)) WITCH_CAT
        else NO_CAT

    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787)
    }
}


class CatTrade : InteractionListener{
    override fun defineListeners() {

        class CatTradeDialogue : DialogueFile(){
            override fun handle(componentID: Int, buttonID: Int) {
                when (stage){
                    0 -> sendDialogue(player!!, "You hand over the cat. You are given 100 Death Runes.").also { stage++  }
                    1 -> npcl(FacialExpression.HAPPY, "Great, thanks for that!").also { stage++ }
                    2 -> playerl(FacialExpression.NEUTRAL, "That's ok, take care.").also { stage = END_DIALOGUE }
                }
            }
        }
        onUseWith(IntType.NPC, cats, NPCs.CIVILIAN_785, NPCs.CIVILIAN_786, NPCs.CIVILIAN_787){ player, used, with ->
            if(removeItem(player, used)){
                val dialogue = CatTradeDialogue()
                // Remove the cat
                player.familiarManager.removeDetails(used.id)
                addItem(player, Items.DEATH_RUNE_560, 100)
                openDialogue(player, dialogue, with as NPC)
            }

            return@onUseWith true
        }
    }
}
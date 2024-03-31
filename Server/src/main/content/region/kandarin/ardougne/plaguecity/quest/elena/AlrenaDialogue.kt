package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class AlrenaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getStage("Plague City") == 1) {
            playerl(FacialExpression.FRIENDLY, "Hello, Edmond has asked me to help find your daughter.").also { stage++ }
        } else {
            playerl(FacialExpression.FRIENDLY, "Hello Madam.").also { stage++ }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            0 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Oh, hello there.").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Are you ok?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Not too bad... I've just got some troubles on my mind...").also { stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Yes he told me. I've begun making your special gas mask, but I need some dwellberries to finish it.").also { stage++ }
                2 -> if (inInventory(player!!, Items.DWELLBERRIES_2126)) {
                    playerl(FacialExpression.NEUTRAL, "Yes I've got some here.").also { stage++ }
                } else {
                    npcl(FacialExpression.FRIENDLY, "The place to look is in McGrubor's Wood to the west of Seers village.").also { stage = 8 }
                }
                3 -> {
                    sendItemDialogue(player!!, Items.DWELLBERRIES_2126, "You give the dwellberries to Alrena.").also { stage++ }
                    removeItem(player!!, Items.DWELLBERRIES_2126)
                }
                4 -> sendDialogue(player!!, "Alrena crushes the berries into a smooth paste. She then smears the paste over a strange mask.").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, "There we go, all done. While in West Ardougne you must wear this at all times, or you could catch the plague. I'll make a spare mask. I'll hide it in the wardrobe in case the mourners come in.").also { stage++ }
                6 -> sendItemDialogue(player!!, Items.GAS_MASK_1506, "Alrena gives you the mask.").also { stage++ }
                7 -> {
                    end()
                    addItem(player!!, Items.GAS_MASK_1506)
                    setQuestStage(player!!, "Plague City", 2)
                    setAttribute(player!!, PlagueCityListeners.BUCKET_USES_ATTRIBUTE, 0)
                    sendNPCDialogue(player!!, NPCs.ALRENA_710, "I'll make a spare mask. I'll hide it in the wardrobe in case the mourners come in.")
                }
                8 -> playerl(FacialExpression.NEUTRAL, "Ok, I'll go and get some.").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hello darling, I think Edmond had a good idea of how to get into West Ardougne, you should hear his idea.").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Alright I'll go and see him now.").also { stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                1 -> if (player.getAttribute(PlagueCityListeners.BUCKET_USES_ATTRIBUTE, 0) == 1) {
                    npcl(FacialExpression.FRIENDLY, "Hello darling, how's that tunnel coming along?").also { stage = 5 }
                } else {
                    npcl(FacialExpression.FRIENDLY, "How's the tunnel going?").also { stage++ }
                }
                2 -> playerl(FacialExpression.FRIENDLY, "I'm getting there.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "One of the mourners has been sniffing around asking questions about you and Edmond, you should keep an eye out for him.").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Ok, thanks for the warning.").also { stage = END_DIALOGUE }
                5 -> playerl(FacialExpression.FRIENDLY, "I just need to soften the soil a little more and then we'll start digging.").also { stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hi, have you managed to get through to West Ardougne?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not yet, but I should be going soon.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Make sure you wear your mask while you're over there! I can't think of a worse way to die.").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Ok, thanks for the warning.").also { stage = END_DIALOGUE }
            }

            5 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hello, any word on Elena?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not yet I'm afraid.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Is there anything else I can do to help?").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "It's alright, I'll get her back soon.").also { stage++ }
                5 -> if(inEquipment(player, Items.GAS_MASK_1506)) {
                    npcl(FacialExpression.FRIENDLY, "That's the spirit, dear.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FacialExpression.FRIENDLY, "That's the spirit, dear. Don't forget that there's a spare gas mask in the wardrobe if you need one.").also { stage = END_DIALOGUE }
                }
            }

            in 6..7 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hi, have you managed to get through to West Ardougne?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not yet, but I should be going soon.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Make sure you wear your mask while you're over there! I can't think of a worse way to die.").also { stage++ }
                4 -> if(inEquipment(player, Items.GAS_MASK_1506)) {
                    playerl(FacialExpression.FRIENDLY, "Okay, thanks for the warning.").also { stage = END_DIALOGUE }
                } else {
                    npcl(FacialExpression.FRIENDLY, "Don't forget, I've got a spare one hidden in the wardrobe if you need it.").also { stage++ }
                }
                5 -> playerl(FacialExpression.FRIENDLY, "Great, thanks Alrena!").also { stage = END_DIALOGUE }
            }

            in 8..14 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hello, any word on Elena?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not yet I'm afraid.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Is there anything else I can do to help?").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Do you have a picture of Elena?").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, " Yes. There should be one in the house somewhere. Let me know if you need anything else.").also { stage = END_DIALOGUE }
            }

            in 15..98 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Hello, any word on Elena?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Not yet I'm afraid, I need to find some Snape grass first, any idea where I'd find some?").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "It's not common round here, though I hear it's easy to find by the coast south west of Falador.").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Thanks, I'll go take a look.").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "I also need to get some chocolate powder for a hangover cure for the city warder.").also { stage++ }
                6 -> npcl(FacialExpression.FRIENDLY, "Well I don't have any chocolate, but this may help.").also { stage++ }
                7 -> sendItemDialogue(player!!, Items.PESTLE_AND_MORTAR_233, "Alrena hands you a pestle and mortar.").also { stage++ }
                8 -> playerl(FacialExpression.FRIENDLY, "Thanks.").also { stage++ }
                9 -> {
                    end()
                    addItem(player!!, Items.PESTLE_AND_MORTAR_233)
                }
            }

            99 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Thank you, thank you! Elena beat you back by minutes.").also { stage = END_DIALOGUE }
            }

            100 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Thank you for rescuing my daughter! Elena has told me of your bravery in entering a house that could have been plague infected. I can't thank you enough!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALRENA_710)
}

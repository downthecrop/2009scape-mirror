package content.region.asgarnia.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.global.Skillcape
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class MasterCrafterDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return MasterCrafterDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(
                FacialExpression.FRIENDLY,
                "Hello, and welcome to the Crafting Guild. Accomplished crafters from all " +
                        "over the land come here to use our top notch workshops."
            ).also { stage++ }

            1 -> {
                if (Skillcape.isMaster(player, Skills.CRAFTING)) {
                    playerl(
                        FacialExpression.ASKING,
                        "Are you the person I need to talk to about buying a Skillcape of Crafting?"
                    ).also { stage = 100 }
                } else {
                    end()
                }
            }

            100 -> npcl(
                FacialExpression.FRIENDLY,
                "I certainly am, and I can see that you are definitely talented enough to own one! " +
                        "Unfortunately, being such a prestigious item, they are appropriately expensive. " +
                        "I'm afraid I must ask you for 99000 gold."
            ).also { stage++ }

            101 -> options(
                "99000 gold! Are you mad?",
                "That's fine."
            ).also { stage++ }

            102 -> {
                when (buttonId) {
                    1 -> playerl(
                        FacialExpression.ASKING,
                        "99000 gold! Are you mad?"
                    ).also { stage++ }

                    2 -> playerl(
                        FacialExpression.FRIENDLY,
                        "That's fine."
                    ).also { stage = 110 }
                }
            }

            103 -> npcl(
                FacialExpression.FRIENDLY,
                "Not at all; there are many other adventurers who would love the opportunity to purchase " +
                        "such a prestigious item! You can find me here if you change your mind."
            ).also { stage = END_DIALOGUE }

            110 -> {
                when {
                    !inInventory(player, Items.COINS_995, 99000) -> playerl(
                        FacialExpression.NEUTRAL,
                        "But, unfortunately, I don't have enough money with me."
                    ).also { stage = 111 }

                    freeSlots(player) < 2 -> npcl(
                        FacialExpression.FRIENDLY,
                        "Unfortunately all Skillcapes are only available with a free hood, it's part " +
                                "of a skill promotion deal; buy one get one free, you know. So you'll need " +
                                "to free up some inventory space before I can sell you one."
                    ).also { stage = END_DIALOGUE }

                    else -> {
                        Skillcape.purchase(player, Skills.CRAFTING)
                        npcl(
                            FacialExpression.FRIENDLY,
                            "Excellent! Wear that cape with pride my friend."
                        ).also { stage = END_DIALOGUE }
                    }
                }
            }

            111 -> npcl(
                FacialExpression.FRIENDLY,
                "Well, come back and see me when you do."
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MASTER_CRAFTER_805)
    }
}
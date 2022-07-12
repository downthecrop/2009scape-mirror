package rs09.game.content.dialogue.region.lunarisle

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.IfTopic
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Handles Sirsal banker dialogue tree.
 *
 * @author vddCore
 */
@Initializable
class SirsalBankerDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> if (hasSealOfPassage(player)) {
                if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                    npcl(
                        FacialExpression.NEUTRAL,
                        "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                        "our services are not available for Ultimate ${if (player.isMale) "Ironmen" else "Ironwomen"}"
                    ).also { stage = END_DIALOGUE }
                } else {

                    npcl(
                        FacialExpression.NEUTRAL,
                        "Good day, how may I help you?"
                    ).also {
                        if (hasAwaitingGrandExchangeCollections(player)) {
                            stage++
                        } else {
                            stage += 2
                        }
                    }
                }
            } else {
                playerl(FacialExpression.HALF_WORRIED, "Hi, I...")
                stage = 30
            }

            1 -> npcl(
                FacialExpression.NEUTRAL,
                "Before we go any further, I should inform you that you " +
                "have items ready for collection from the Grand Exchange."
            ).also { stage++ }

            2 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "I'd like to access my bank account, please.", 10),
                IfTopic(
                    FacialExpression.NEUTRAL,
                    "I'd like to switch to my ${getBankAccountName(player, true)} bank account.",
                    13,
                    hasActivatedSecondaryBankAccount(player)
                ),
                IfTopic(
                    FacialExpression.NEUTRAL,
                    "I'd like to open a secondary bank account.",
                    20,
                    !hasActivatedSecondaryBankAccount(player)
                ),
                Topic(FacialExpression.NEUTRAL, "I'd like to check my PIN settings.", 11),
                Topic(FacialExpression.NEUTRAL, "I'd like to collect items.", 12),
                Topic(FacialExpression.ASKING, "What is this place?", 3),
            )

            3 -> npcl(
                FacialExpression.NEUTRAL,
                "This is a branch of the Bank of Gielinor. We have branches in many towns."
            ).also { stage++ }

            4 -> playerl(
                FacialExpression.ASKING,
                "And what do you do?"
            ).also { stage++ }

            5 -> npcl(
                FacialExpression.NEUTRAL,
                "We will look after your items and money for you. " +
                "Leave your valuables with us if you want to keep them safe."
            ).also { stage = END_DIALOGUE }

            10 -> {
                openBankAccount(player)
                end()
            }

            11 -> {
                openBankPinSettings(player)
                end()
            }

            12 -> {
                openGrandExchangeCollectionBox(player)
                end()
            }

            13 -> {
                toggleBankAccount(player)

                npcl(
                    FacialExpression.NEUTRAL,
                    "Your active bank account has been switched. " +
                    "You can now access your ${getBankAccountName(player)} account."
                ).also { stage = END_DIALOGUE }
            }

            20 -> npcl(
                FacialExpression.NEUTRAL,
                "Certainly. We offer secondary accounts to all our customers."
            ).also { stage++ }

            21 -> npcl(
                FacialExpression.NEUTRAL,
                "The secondary account comes with a standard fee of 5,000,000 coins. The fee is non-refundable " +
                "and account activation is permanent."
            ).also { stage++ }

            22 -> npcl(
                FacialExpression.NEUTRAL,
                "If your inventory does not contain enough money to cover the costs, we will complement " +
                "the amount with the money inside your primary bank account."
            ).also { stage++ }

            23 -> npcl(
                FacialExpression.ASKING,
                "Knowing all this, would you like to proceed with opening your secondary bank account?"
            ).also { stage++ }

            24 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "Yes, I am still interested.", 25),
                Topic(FacialExpression.NEUTRAL, "Actually, I've changed my mind.", 26)
            )

            25 -> {
                when (activateSecondaryBankAccount(player)) {
                    SecondaryBankAccountActivationResult.ALREADY_ACTIVE -> {
                        npcl(
                            FacialExpression.NEUTRAL,
                            "Your bank account was already activated, there is no need to pay twice."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.INTERNAL_FAILURE -> {
                        npcl(
                            FacialExpression.NEUTRAL,
                            "I must apologize, the transaction was not successful. Please check your " +
                            "primary bank account and your inventory - if there's money missing, please " +
                            "screenshot your chat box and contact the game developers."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.NOT_ENOUGH_MONEY -> {
                        npcl(
                            FacialExpression.NEUTRAL,
                            "It appears that you do not have the money necessary to cover the costs " +
                            "associated with opening a secondary bank account. I will be waiting here " +
                            "until you do."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.SUCCESS -> {
                        npcl(
                            FacialExpression.NEUTRAL,
                            "Your secondary bank account has been opened and can be accessed through any " +
                            "of the Bank of Gielinor's employees. Thank you for choosing our services."
                        ).also { stage = END_DIALOGUE }
                    }
                }
            }

            26 -> npcl(
                FacialExpression.NEUTRAL,
                "Very well. Should you decide a secondary bank account is needed, do not hesitate to " +
                "contact any of the Bank of Gielinor's stationary employees. We will be happy to help."
            ).also { stage = END_DIALOGUE }

            30 -> npcl(
                FacialExpression.ANNOYED,
                "What are you doing here, Fremennik?!"
            ).also { stage++ }

            31 -> playerl(
                FacialExpression.WORRIED,
                "I have a Seal of Pass..."
            ).also { stage++ }

            32 -> npcl(
                FacialExpression.ANGRY,
                "No you don't! Begone!"
            ).also { stage = END_DIALOGUE }

            /* TODO: Is the above related to Lunar Diplomacy? */
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIRSAL_BANKER_4519)
    }
}

package rs09.game.content.dialogue.region.worldwide.bank

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import rs09.game.content.dialogue.IfTopic
import rs09.game.content.dialogue.Topic
import rs09.game.node.entity.npc.BankerNPC
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

@Initializable
class BankerDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> when {
                hasIronmanRestriction(player, IronmanMode.ULTIMATE) -> {
                    npcl(
                        FacialExpression.ANNOYED,
                        "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                        "our services are not available for Ultimate ${if (player.isMale) "Ironmen" else "Ironwomen"}"
                    ).also { stage = END_DIALOGUE }
                }

                else -> {
                    npcl(
                        FacialExpression.FRIENDLY,
                        "Good day, how may I help you?"
                    ).also {
                        if (hasAwaitingGrandExchangeCollections(player)) {
                            stage++
                        } else {
                            stage += 2
                        }
                    }
                }
            }

            1 -> npcl(
                FacialExpression.FRIENDLY,
                "Before we go any further, I should inform you that you " +
                "have items ready for collection from the Grand Exchange."
            ).also { stage++ }

            2 -> showTopics(
                Topic(FacialExpression.FRIENDLY, "I'd like to access my bank account, please.", 10),
                IfTopic(
                    FacialExpression.FRIENDLY,
                    "I'd like to switch to my ${getBankAccountName(player, true)} bank account.",
                    13,
                    hasActivatedSecondaryBankAccount(player)
                ),
                IfTopic(
                    FacialExpression.FRIENDLY,
                    "I'd like to open a secondary bank account.",
                    20,
                    !hasActivatedSecondaryBankAccount(player)
                ),
                Topic(FacialExpression.FRIENDLY, "I'd like to check my PIN settings.", 11),
                Topic(FacialExpression.FRIENDLY, "I'd like to collect items.", 12),
                Topic(FacialExpression.ASKING, "What is this place?", 3),
            )

            3 -> npcl(
                FacialExpression.FRIENDLY,
                "This is a branch of the Bank of Gielinor. We have branches in many towns."
            ).also { stage++ }

            4 -> playerl(
                FacialExpression.ASKING,
                "And what do you do?"
            ).also { stage++ }

            5 -> npcl(
                FacialExpression.FRIENDLY,
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
                    FacialExpression.FRIENDLY,
                    "Your active bank account has been switched. " +
                    "You can now access your ${getBankAccountName(player)} account."
                ).also { stage = 2 }
            }

            20 -> npcl(
                FacialExpression.FRIENDLY,
                "Certainly. We offer secondary accounts to all our customers."
            ).also { stage++ }

            21 -> npcl(
                FacialExpression.FRIENDLY,
                "The secondary account comes with a standard fee of 5,000,000 coins. The fee is non-refundable " +
                "and account activation is permanent."
            ).also { stage++ }

            22 -> npcl(
                FacialExpression.FRIENDLY,
                "If your inventory does not contain enough money to cover the costs, we will complement " +
                "the amount with the money inside your primary bank account."
            ).also { stage++ }

            23 -> npcl(
                FacialExpression.ASKING,
                "Knowing all this, would you like to proceed with opening your secondary bank account?"
            ).also { stage++ }

            24 -> showTopics(
                Topic(FacialExpression.HAPPY, "Yes, I am still interested.", 25),
                Topic(FacialExpression.ANNOYED, "Actually, I've changed my mind.", 26)
            )

            25 -> {
                when (activateSecondaryBankAccount(player)) {
                    SecondaryBankAccountActivationResult.ALREADY_ACTIVE -> {
                        npcl(
                            FacialExpression.FRIENDLY,
                            "Your bank account was already activated, there is no need to pay twice."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.INTERNAL_FAILURE -> {
                        npcl(
                            FacialExpression.ANNOYED,
                            "I must apologize, the transaction was not successful. Please check your " +
                            "primary bank account and your inventory - if there's money missing, please " +
                            "screenshot your chat box and contact the game developers."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.NOT_ENOUGH_MONEY -> {
                        npcl(
                            FacialExpression.ANNOYED,
                            "It appears that you do not have the money necessary to cover the costs " +
                            "associated with opening a secondary bank account. I will be waiting here " +
                            "until you do."
                        ).also { stage = END_DIALOGUE }
                    }

                    SecondaryBankAccountActivationResult.SUCCESS -> {
                        npcl(
                            FacialExpression.FRIENDLY,
                            "Your secondary bank account has been opened and can be accessed through any " +
                            "of the Bank of Gielinor's employees. Thank you for choosing our services."
                        ).also { stage = END_DIALOGUE }
                    }
                }
            }

            26 -> npcl(
                FacialExpression.FRIENDLY,
                "Very well. Should you decide a secondary bank account is needed, do not hesitate to " +
                "contact any of the Bank of Gielinor's stationary employees. We will be happy to help."
            ).also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = BankerNPC.NPC_IDS
}

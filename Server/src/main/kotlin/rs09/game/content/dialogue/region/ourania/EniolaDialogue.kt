package rs09.game.content.dialogue.region.ourania

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

@Initializable
class EniolaDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> if (hasIronmanRestriction(player, IronmanMode.ULTIMATE)) {
                npcl(
                    FacialExpression.NEUTRAL,
                    "My apologies, dear ${if (player.isMale) "sir" else "madam"}, " +
                    "our services are not available for Ultimate ${if (player.isMale) "Ironmen" else "Ironwomen"}."
                ).also { stage = END_DIALOGUE }
            }
            else {
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

            1 -> npcl(
                FacialExpression.NEUTRAL,
                "Before we go any further, I should inform you that you " +
                "have items ready for collection from the Grand Exchange."
            ).also { stage++ }

            2 -> playerl(FacialExpression.ASKING, "Who are you?")
                .also { stage++ }

            3 -> npcl(
                FacialExpression.NEUTRAL,
                "How frightfully rude of me, my dear ${if (player.isMale) "sir" else "lady"}. " +
                "My name is Eniola and I work for that excellent enterprise, the Bank of Gielinor."
            ).also { stage++ }

            4 -> showTopics(
                Topic(FacialExpression.HALF_THINKING, "If you work for the bank, what are you doing here?", 10),
                Topic(FacialExpression.NEUTRAL, "I'd like to access my bank account, please.", 30),
                Topic(FacialExpression.NEUTRAL, "I'd like to check my PIN settings.", 31),
                Topic(FacialExpression.NEUTRAL, "I'd like to see my collection box.", 32),
                Topic(FacialExpression.NEUTRAL, "Never mind.", END_DIALOGUE)
            )

            10 -> npcl(
                FacialExpression.NEUTRAL,
                "My presence here is the start of a new enterprise of travelling banks. " +
                "I, and others like me, will provide you with the convenience of having " +
                "bank facilities where they will be of optimum use to you."
            ).also { stage++ }

            11 -> playerl(
                FacialExpression.ASKING,
                "So... What are you doing here?"
            ).also { stage++ }

            12 -> npcl(
                FacialExpression.HALF_GUILTY,
                "The Z.M.I. - that is - the Zamorakian Magical Institute, required my services " +
                "upon discovery of this altar."
            ).also { stage++ }

            13 -> npcl(
                FacialExpression.HALF_GUILTY,
                "We at the Bank of Gielinor are a neutral party and are willing to offer our " +
                "services regardless of affiliation. So that is why I am here."
            ).also { stage++ }

            14 -> playerl(
                FacialExpression.ASKING,
                "Can I access my bank account by speaking to you?"
            ).also { stage++ }

            15 -> npcl(
                FacialExpression.NEUTRAL,
                "Of course, dear ${if (player.isMale) "sir" else "lady"}. However, I must inform you " +
                "that because the Z.M.I. are paying for my services, they require anyone not part of " +
                "the Institute to pay an access fee to open their bank account."
            ).also { stage++ }

            16 -> npcl(
                FacialExpression.NEUTRAL,
                "But, as our goal as travelling bankers is to make our customers' lives more convenient, " +
                "we have accomodated to your needs."
            ).also { stage++ }

            17 -> npcl(
                FacialExpression.NEUTRAL,
                "We know you will be busy creating runes and do not wish " +
                "to carry money with you."
            ).also { stage++ }

            18 -> npcl(
                FacialExpression.NEUTRAL,
                "The charge to open your account is the small amount of twenty of one type of rune. " +
                "The type of rune is up to you."
            ).also { stage++ }

            19 -> npcl(
                FacialExpression.HALF_ASKING,
                "Would you like to pay the price of twenty runes to open " +
                "your bank account?"
            ).also { stage++ }

            20 -> showTopics(
                Topic(FacialExpression.NEUTRAL, "Yes, please.", 30),
                Topic(FacialExpression.SUSPICIOUS, "Let me open my account and then I'll give you the runes.", 21),
                Topic(FacialExpression.ANNOYED, "No way! I'm not paying to withdraw my own stuff.", 22)
            )

            21 -> npcl(
                FacialExpression.NEUTRAL,
                "It's not that I don't trust you, old ${if (player.isMale) "chap" else "girl"}, " +
                "but as the old adage goes: 'Payment comes before friends'."
            ).also { stage = END_DIALOGUE }

            22 -> npcl(
                FacialExpression.NEUTRAL,
                "I'm sorry to hear that, dear ${if (player.isMale) "sir" else "madam"}. "
            ).also { stage++ }

            23 -> npcl(
                FacialExpression.NEUTRAL,
                "Should you reconsider, because I believe this service offers excellent " +
                "value for the price, do not hesitate to contact me."
            ).also { stage = END_DIALOGUE }

            30 -> {
                setAttribute(player, "zmi:bankaction", "open")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
                end()
            }

            31 -> {
                openBankPinSettings(player)
                end()
            }

            32 -> {
                setAttribute(player, "zmi:bankaction", "collect")
                openInterface(player, Components.BANK_CHARGE_ZMI_619)
                end()
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ENIOLA_6362)
}
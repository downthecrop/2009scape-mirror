package content.global.ame.events.quizmaster

import core.ServerConstants
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.Items

class QuizMasterDialogueFile : DialogueFile() {
    companion object {
        const val QUIZMASTER_INTERFACE = Components.MACRO_QUIZSHOW_191
        const val QUIZMASTER_ATTRIBUTE_RETURN_LOC = "/save:original-loc"
        const val QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT = "/save:quizmaster:questions-correct"
        const val QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER = "quizmaster:random-answer"

        /*
        // Golden Models:
        8828 ADAMANT_BATTLEAXE_1371
        8829 SALMON_329
        8830 TROUT_333
        8831 NECKLACE
        8832 WOODEN_SHIELD_1171
        8833 BRONZE_MED_HELM_1139
        8834 RING
        8835 SECATEURS_5329
        8836 BRONZE_SWORD_1277
        8837 GARDENING_TROWEL_5325
         */
        val sets = arrayOf(
                intArrayOf(8828, 8829, 8829),
                intArrayOf(8831, 8837, 8835),
                intArrayOf(8830, 8832, 8833),
                intArrayOf(8835, 8834, 8831),
                intArrayOf(8837, 8836, 8828),
        )

        fun randomQuestion(player: Player): Int {
            val randomSet = intArrayOf(*sets.random())
            val answer = intArrayOf(*randomSet)[0]
            randomSet.shuffle()
            val correctButton = randomSet.indexOf(answer) + 2 // buttons are 3,4,5

            player.packetDispatch.sendModelOnInterface(randomSet[0], QUIZMASTER_INTERFACE, 6, 512)
            player.packetDispatch.sendModelOnInterface(randomSet[1], QUIZMASTER_INTERFACE, 7, 512)
            player.packetDispatch.sendModelOnInterface(randomSet[2], QUIZMASTER_INTERFACE, 8, 512)
            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 6, 512,0,0)
            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 7, 512,0,0)
            player.packetDispatch.sendAngleOnInterface(QUIZMASTER_INTERFACE, 8, 512,0,0)

            return correctButton
        }

        // Random Item should be "Mystery Box", but the current MYSTERY_BOX_6199 is already inauthentically used by Giftmas.
        val tableRoll = WeightBasedTable.create(
                WeightedItem(Items.LAMP_6796, 1, 1, 1.0, false),
                WeightedItem(Items.CABBAGE_1965, 1, 1, 1.0, false),
                WeightedItem(Items.DIAMOND_1601, 1, 1, 1.0, false),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0, false),
                WeightedItem(Items.FLIER_956, 1, 1, 1.0, false),
                WeightedItem(Items.OLD_BOOT_685, 1, 1, 1.0, false),
                WeightedItem(Items.BODY_RUNE_559, 1, 1, 1.0, false),
                WeightedItem(Items.ONION_1957, 1, 1, 1.0, false),
                WeightedItem(Items.MITHRIL_SCIMITAR_1329, 1, 1, 1.0, false),
                WeightedItem(Items.CASKET_405, 1, 1, 1.0, false),
                WeightedItem(Items.STEEL_PLATEBODY_1119, 1, 1, 1.0, false),
                WeightedItem(Items.NATURE_RUNE_561, 20, 20, 1.0, false),
        )
    }


    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FacialExpression.FRIENDLY,"WELCOME to the GREATEST QUIZ SHOW in the", "whole of ${ServerConstants.SERVER_NAME}:", "<col=8A0808>O D D</col>  <col=8A088A>O N E</col>  <col=08088A>O U T</col>").also { stage++ }
            1 -> player(FacialExpression.THINKING, "I'm sure I didn't ask to take part in a quiz show...").also { stage++ }
            2 -> npc(FacialExpression.FRIENDLY,"Please welcome our newest contestant:", "<col=FF0000>${player?.username}</col>!", "Just pick the O D D  O N E  O U T.", "Four questions right, and then you win!").also { stage++ }
            3 -> {
                setAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER, randomQuestion(player!!))
                player!!.interfaceManager.openChatbox(QUIZMASTER_INTERFACE)
                stage++
            }
            4-> {
                if (buttonID == getAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER, 0)) {
                    // Correct Answer
                    setAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, getAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0) + 1)
                    if (getAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT, 0) >= 4) {
                        npc(FacialExpression.FRIENDLY,"<col=08088A>CONGRATULATIONS!</col>", "You are a <col=8A0808>WINNER</col>!", "Please choose your <col=08088A>PRIZE</col>!")
                        stage = 5
                    } else {
                        npc(FacialExpression.FRIENDLY,"Wow, you're a smart one!", "You're absolutely RIGHT!", "Okay, next question!")
                        stage = 3
                    }
                } else {
                    // Wrong Answer
                    npc(FacialExpression.FRIENDLY,"WRONG!", "That's just WRONG!", "Okay, next question!")
                    stage = 3
                }
            }
            // Random Item should be "Mystery Box", but the current MYSTERY_BOX_6199 is already inauthentically used by Giftmas.
            5 -> options("1000 Coins", "Random Item").also { stage++ }
            6 -> {
                resetAnimator(player!!)
                teleport(player!!, getAttribute(player!!, QUIZMASTER_ATTRIBUTE_RETURN_LOC, Location.create(3222, 3218, 0)))
                when (buttonID) {
                    1 -> {
                        queueScript(player!!, 0, QueueStrength.SOFT) { stage: Int ->
                            addItemOrDrop(player!!, Items.COINS_995, 1000)
                            return@queueScript stopExecuting(player!!)
                        }
                    }
                    2 -> {
                        queueScript(player!!, 0, QueueStrength.SOFT) { stage: Int ->
                            addItemOrDrop(player!!, tableRoll.roll()[0].id)
                            return@queueScript stopExecuting(player!!)
                        }
                    }
                }
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_RETURN_LOC)
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_QUESTIONS_CORRECT)
                removeAttribute(player!!, QUIZMASTER_ATTRIBUTE_RANDOM_ANSWER)
                stage = END_DIALOGUE
                end()
            }
        }
    }
}
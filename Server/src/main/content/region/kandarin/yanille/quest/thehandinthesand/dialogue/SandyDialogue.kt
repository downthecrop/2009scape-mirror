package content.region.kandarin.yanille.quest.thehandinthesand.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.getAttribute
import core.api.getQuestStage
import core.api.inInventory
import core.api.openDialogue
import core.api.sendMessage
import core.api.setAttribute
import core.api.setQuestStage
import core.api.setVarbit
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items

private const val SANDY_WRAPPER_3110 = 3110

/**
 * Dialogue between Sandy and the player for The Hand in the Sand quest.
 * @author Edith
 */

@Initializable
class SandyDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return SandyDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SandyDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(SANDY_WRAPPER_3110)
    }
}

class SandyDialogueFile : DialogueLabeller() {
    override fun addConversation() {

        exec { player, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            when {
                stage >= TheHandintheSand.STAGE_SANDY_CONFESSION_55 &&
                        stage < TheHandintheSand.STAGE_QUEST_COMPLETED_100 -> loadLabel(player, "quest_stage_55")

                stage >= TheHandintheSand.STAGE_SANDYS_ROTA_20 -> loadLabel(player, "quest_stage_$stage")

                else -> loadLabel(player, "default_sandy_talk")
            }
        }

        label("default_sandy_talk")
            npc("Nice day for sand isn't it?")

        label("quest_stage_20")
            player("Hello Sir, do you run the Sand Corp?")
            npc(FacialExpression.SUSPICIOUS, "Who wants to know?")
            player("I'm @name. I'm here investigating the possible murder of a wizard.")
            npc(FacialExpression.ANGRY, "I don't care about that, I have far too much work to do. Let the authorities take care of things like murder and stop snooping around my office!")
            line("Sandy seems very keen to get you out of the office, perhaps you", "should take a look around.")

        label(
            "quest_stage_25",
            "quest_stage_30",
            "quest_stage_35",
            "quest_stage_40"
        )
            npc(FacialExpression.ANGRY, "I don't have time to talk to you. Go away!")

        label("quest_stage_45")
            npc(FacialExpression.ANGRY, "I don't have time to talk to you. Go away!")
            exec { player, _ ->
                if (getAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTION_OPTION, 0) == 0) {
                    setAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTION_OPTION, RandomFunction.random(1, 4))
                }
            }
            options(
                DialogueOption("distract_mutant_herring", "There's a herd of huge mutant herring about to drop from the sky!",
                    "There's a herd of huge mutant herring about to drop from the sky!", FacialExpression.AMAZED_TALKING),
                DialogueOption("distract_pygmy_shrews", "But the pygmy shrews have eaten all the sand!",
                    "But the pygmy shrews have eaten all the sand!", FacialExpression.AMAZED_TALKING),
                DialogueOption("distract_pink_banana", "A small parrot with a pink banana is sitting outside your window!",
                    "A small parrot with a pink banana is sitting outside your window!", FacialExpression.AMAZED_TALKING)
            )

        label("distract_mutant_herring")
            exec { player, _ ->
                loadLabel(player, if (getAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTION_OPTION, 0) == 1) "distract_success" else "distract_failed")
            }

        label("distract_pygmy_shrews")
            exec { player, _ ->
                loadLabel(player, if (getAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTION_OPTION, 0) == 2) "distract_success" else "distract_failed")
            }

        label("distract_pink_banana")
            exec { player, _ ->
                loadLabel(player, if (getAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTION_OPTION, 0) == 3) "distract_success" else "distract_failed")
            }

        label("distract_failed")
            npc(FacialExpression.ANGRY, "I'm not falling for that one!")

        label("distract_success")
            npc(FacialExpression.AMAZED_TALKING, "Wow! I must see this!")
            exec { player, _ ->
                setVarbit(player, TheHandintheSand.VARBIT_SANDY_STATE_1535, TheHandintheSand.SANDY_DISTRACTED_1, true)
                setAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTED, true)
                sendMessage(player, "Sandy turns to look out of the window, now is your chance!")
            }

        label("quest_stage_50")
            exec { player, _ ->
                if (inInventory(player, Items.MAGICAL_ORB_A_6951)) {
                    loadLabel(player, "start_interrogating_sandy")
                } else {
                    loadLabel(player, "activate_orb_first")
                }
            }

        label("activate_orb_first")
            line("You need to activate the magical scrying orb, obtained from the",
                "wizard in Yanille, to capture the conversation with Sandy!")

        label("start_interrogating_sandy")
            player("Now, I'm going to ask you some questions and I want you to answer me truthfully...")
            npc(FacialExpression.SCARED, "Ok...")
            goto("ask_sandy_questions")

        label("ask_sandy_questions")
            exec { player, _ ->
                if (getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_ROTA_CHANGE, false) &&
                    getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_BERT_MEMORY, false) &&
                    getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_WIZARD_DEATH, false)
                ) {
                    loadLabel(player, "conclude_the_interrogation")
                }
            }
            options(
                DialogueOption(
                    "ask_rota_change",
                    "Why is Bert's rota different from the original?",
                    "Why is Bert's rota different from the original?",
                    FacialExpression.ASKING,
                    optionIf = { player, _ -> !getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_ROTA_CHANGE, false) }
                ),
                DialogueOption(
                    "ask_bert_memory",
                    "Why doesn't Bert remember the change in his hours?",
                    "Why doesn't Bert remember the change in his hours?",
                    FacialExpression.ASKING,
                    optionIf = { player, _ -> !getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_BERT_MEMORY, false) }
                ),
                DialogueOption(
                    "ask_wizard_death",
                    "What happened to the wizard?",
                    "What happened to the wizard?",
                    FacialExpression.ASKING,
                    optionIf = { player, _ -> !getAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_WIZARD_DEATH, false) }
                ),
                DialogueOption("need_all_questions", "Ok, I'm done with you.", skipPlayer = true)
            )

        label("ask_rota_change")
            npc(FacialExpression.SCARED, "Because... I changed it.")
            exec { player, _ ->
                setAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_ROTA_CHANGE, true)
            }
            goto("ask_sandy_questions")

        label("ask_bert_memory")
            npc(FacialExpression.SCARED, "Because.... because.... I bribed a wizard to put a spell on him so he would believe everything I say!!")
            player(FacialExpression.THINKING, "Why?")
            npc(FacialExpression.SCARED, "So that I could make him work longer without paying him more!")
            exec { player, _ ->
                setAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_BERT_MEMORY, true)
            }
            goto("ask_sandy_questions")

        label("ask_wizard_death")
            npc(FacialExpression.SCARED, "I...I... I KILLED HIM! So I wouldn't have to pay him and no one would know. I put his body in the next load of sand.")
            exec { player, _ ->
                setAttribute(player, TheHandintheSand.ATTRIBUTE_ASKED_WIZARD_DEATH, true)
            }
            goto("ask_sandy_questions")

        label("need_all_questions")
            line("You need to ask Sandy all the questions.")
            goto("ask_sandy_questions")
        
        label("conclude_the_interrogation")
            player(FacialExpression.AMAZED_TALKING, "I think I have enough evidence now, you can go for now, but I think you're up to your neck in it!")
            item(Item(Items.MAGICAL_ORB_A_6951),
                "Sandy has told you all he knows. The magical scrying", "orb is full and needs to be returned to the Wizard in", "Yanille."
            )
            exec { player, _ ->
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_SANDY_CONFESSION_55)
            }

        label("quest_stage_55")
            npc(FacialExpression.ANGRY, "I don't have time to talk to you. Go away!")

    }
}
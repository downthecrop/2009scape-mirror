package content.region.misthalin.lumbridge.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import content.region.misthalin.varrock.quest.dragonslayer.DragonSlayer
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import content.region.misthalin.varrock.quest.dragonslayer.DukeHoracioDSDialogue
import content.region.misthalin.lumbridge.quest.runemysteries.DukeHoracioRMDialogue
import content.region.misthalin.dorgeshuun.quest.thelosttribe.DukeHoracioTLTDialogue
import core.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import core.tools.END_DIALOGUE
import content.data.Quests

/**
 * Core dialogue plugin for Duke Horacio, redirects to more specific DialogueFiles.
 * @author Ceikry
 */
class DukeHoracioDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun getIds(): IntArray {
        return intArrayOf(741)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if ((player.questRepository.getQuest(Quests.DRAGON_SLAYER).getStage(player) == 100 && !player.inventory.containsItem(DragonSlayer.SHIELD) && !player.bank.containsItem(DragonSlayer.SHIELD) )|| (player.questRepository.getQuest(Quests.DRAGON_SLAYER).isStarted(player) && !player.questRepository.getQuest(Quests.DRAGON_SLAYER).isCompleted(player))) {
            addOption("Dragon Slayer", DukeHoracioDSDialogue(player.questRepository.getStage(Quests.DRAGON_SLAYER)))
        }
        if (!player.questRepository.isComplete(Quests.THE_LOST_TRIBE) && player.questRepository.getQuest(Quests.THE_LOST_TRIBE).isStarted(player)) {
            addOption("Lost Tribe", DukeHoracioTLTDialogue(player.questRepository.getStage(Quests.THE_LOST_TRIBE)))
        }
        if (!sendChoices()) {
            interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Greetings. Welcome to my castle.")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            DIALOGUE_INITIAL_OPTIONS_HANDLE -> {
                npc("Greetings. Welcome to my castle.")
                loadFile(optionFiles[buttonId - 1])
            }

            0 -> {
                interpreter.sendOptions("Select an Option", "Have you any quests for me?", "Where can I find money?")
                stage = 1
            }
            1 -> when (buttonId) {
                1 -> {
                    interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?")
                    stage = 20
                }
                2 -> {
                    interpreter.sendDialogues(
                        npc,
                        FacialExpression.HALF_GUILTY,
                        "I hear many of the local people earn money by learning a",
                        "skill. Many people get by in life by becoming accomplished",
                        "smiths, cooks, miners and woodcutters."
                    )
                    stage = END_DIALOGUE
                }
            }
            20 -> {
                npc("Let me see...")
                if (!player.questRepository.isComplete(Quests.RUNE_MYSTERIES)) {
                    loadFile(DukeHoracioRMDialogue(player.questRepository.getStage(Quests.RUNE_MYSTERIES)))
                } else {
                    stage++
                }
            }
            21 -> {
                npc("Nope, I've got everything under control", "in the castle at the moment.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return DukeHoracioDialogue(player)
    }
}
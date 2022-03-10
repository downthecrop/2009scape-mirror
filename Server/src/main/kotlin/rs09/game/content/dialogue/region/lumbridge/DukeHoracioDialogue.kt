package rs09.game.content.dialogue.region.lumbridge

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.content.quest.free.dragonslayer.DragonSlayer
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import rs09.game.content.quest.free.dragonslayer.DukeHoracioDSDialogue
import rs09.game.content.quest.free.runemysteries.DukeHoracioRMDialogue
import rs09.game.content.quest.members.thelosttribe.DukeHoracioTLTDialogue
import rs09.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import rs09.tools.END_DIALOGUE

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
        if ((player.questRepository.getQuest("Dragon Slayer").getStage(player) == 100 && !player.inventory.containsItem(DragonSlayer.SHIELD) && !player.bank.containsItem(DragonSlayer.SHIELD) )|| (player.questRepository.getQuest("Dragon Slayer").isStarted(player) && !player.questRepository.getQuest("Dragon Slayer").isCompleted(player))) {
            addOption("Dragon Slayer", DukeHoracioDSDialogue(player.questRepository.getStage("Dragon Slayer")))
        }
        if (!player.questRepository.isComplete("Lost Tribe") && player.questRepository.getQuest("Lost Tribe").isStarted(player)) {
            addOption("Lost Tribe", DukeHoracioTLTDialogue(player.questRepository.getStage("Lost Tribe")))
        }
        if (!sendChoices()) {
            interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Greetings. Welcome to my castle.")
        }
        // Speak to the Duke of Lumbridge
        player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 0, 2)
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
                if (!player.questRepository.isComplete("Rune Mysteries")) {
                    loadFile(DukeHoracioRMDialogue(player.questRepository.getStage("Rune Mysteries")))
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
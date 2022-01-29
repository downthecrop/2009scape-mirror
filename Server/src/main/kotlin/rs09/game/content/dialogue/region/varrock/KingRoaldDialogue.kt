package rs09.game.content.dialogue.region.varrock

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import rs09.game.content.activity.allfiredup.KingRoaldAFUMiniDialogue
import rs09.game.content.quest.free.shieldofarrav.KingRoaldArravDialogue
import rs09.game.content.quest.members.allfiredup.KingRoaldAFUDialogue
import rs09.game.content.quest.members.priestinperil.KingRoaldPIPDialogue
import rs09.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

/**
 * Central dialogue plugin for King Roald. Reroutes to the more specific DialogueFiles
 * @author Ceikry
 * @version 1.0
 */
class KingRoaldDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return KingRoaldDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC

        if (player.getAttribute("afu-mini:ring", false) || player.getAttribute("afu-mini:gloves", false) || player.getAttribute("afu-mini:adze", false)) {
            player("Your Majesty! I did it!")
            loadFile(KingRoaldAFUMiniDialogue())
            return true
        }

        if(player.questRepository.isComplete("Priest in Peril")) {
            if (!player.questRepository.hasStarted("All Fired Up") || player.questRepository.getQuest("All Fired Up").getStage(player) == 90) {
                addOption("All Fired Up", KingRoaldAFUDialogue(player.questRepository.getStage("All Fired Up")))
            }
        } else {
            addOption("Priest in Peril", KingRoaldPIPDialogue(player.questRepository.getStage("Priest in Peril")))
        }

        if (player.questRepository.getQuest("Shield of Arrav").isStarted(player) && !player.questRepository.getQuest("Shield of Arrav").isCompleted(player)) {
            addOption("Shield of Arrav", KingRoaldArravDialogue())
        }

        if(!sendChoices()){
            player("Greetings, your Majesty.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        //Default (non-specific) dialogue
        when (stage) {

            DIALOGUE_INITIAL_OPTIONS_HANDLE -> {
                player("Greetings, your Majesty.")
                loadFile(optionFiles[buttonId - 1])
            }

            START_DIALOGUE -> {
                interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Do you have anything of importance to say?")
                stage = 1
            }
            1 -> {
                interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "...Not really.")
                stage = 2
            }
            2 -> {
                interpreter.sendDialogues(
                    npc,
                    FacialExpression.HALF_GUILTY,
                    "You will have to excuse me, then. I am very busy as I",
                    "have a kingdom to run!"
                )
                stage = END_DIALOGUE
            }

            END_DIALOGUE -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(648, 2590, 5838)
    }
}
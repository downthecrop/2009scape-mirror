package content.region.misthalin.varrock.dialogue

import content.minigame.allfiredup.KingRoaldAFUMiniDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import content.region.misthalin.varrock.quest.shieldofarrav.KingRoaldArravDialogue
import content.region.misthalin.varrock.quest.allfiredup.KingRoaldAFUDialogue
import content.region.misthalin.quest.priestinperil.KingRoaldPIPDialogue
import core.tools.DIALOGUE_INITIAL_OPTIONS_HANDLE
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import content.data.Quests
import content.region.misthalin.varrock.quest.gardenoftranquility.KingRoaldGardenDialogue
import core.api.getQuestStage
import core.api.isQuestComplete
import core.api.isQuestStarted
import org.rs09.consts.NPCs

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

        if (player.getAttribute("afu-mini:ring", false)
            || player.getAttribute("afu-mini:gloves", false)
            || player.getAttribute("afu-mini:adze", false)
        ) {
            player("Your Majesty! I did it!")
            loadFile(KingRoaldAFUMiniDialogue())
            return true
        }

        if (isQuestComplete(player, Quests.PRIEST_IN_PERIL)) {
            if (!isQuestStarted(player, Quests.ALL_FIRED_UP) || getQuestStage(player, Quests.ALL_FIRED_UP) == 90) {
                addOption("All Fired Up", KingRoaldAFUDialogue(getQuestStage(player, Quests.ALL_FIRED_UP)))
            }
        } else {
            addOption("Priest in Peril", KingRoaldPIPDialogue(getQuestStage(player, Quests.PRIEST_IN_PERIL)))
        }

        if (isQuestStarted(player, Quests.SHIELD_OF_ARRAV) && !isQuestComplete(player, Quests.SHIELD_OF_ARRAV)) {
            addOption("Shield of Arrav", KingRoaldArravDialogue())
        }

        if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 50) {
            addOption("Garden of Tranquility", KingRoaldGardenDialogue())
        }

        if (!sendChoices()) {
            player("Greetings, your Majesty.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Default (non-specific) dialogue
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
        return intArrayOf(NPCs.KING_ROALD_648, NPCs.KING_ROALD_2590, NPCs.KING_ROALD_5838)
    }
}
package content.region.kandarin.witchhaven.quest.seaslug

import content.data.Quests
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression

class HolgartIslandDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(Quests.SEA_SLUG, 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 100)
                .playerl("We'd better get back to the platform so we can see what's going on.")
                .npcl(FacialExpression.SUSPICIOUS, "You're right. It all sounds pretty creepy.")
                .endWith() { df, player ->
                    SeaSlugListeners.seaslugBoatTravel(player, 3)
                }
        b.onQuestStages(Quests.SEA_SLUG, 4)
                .playerl("Where are we?")
                .npc("Someway off mainland still. You'd better see if me old", "matey's okay.")
                .end()

    }
}

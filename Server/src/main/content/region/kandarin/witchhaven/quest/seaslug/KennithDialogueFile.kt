package content.region.kandarin.witchhaven.quest.seaslug

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class KennithDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(SeaSlug.questName, 0,1,2,3)
                .playerl(FacialExpression.THINKING, "Are you okay young one?")
                .npcl(FacialExpression.CHILD_SAD, "No, I want daddy!")
                .playerl("Where is your father?")
                .npcl(FacialExpression.CHILD_SAD, "He went to get help days ago.")
                .npcl(FacialExpression.CHILD_SAD, "The nasty fishermen tried to throw me and daddy into the sea. So he told me to hide here.")
                .playerl("That's good advice, you stay here and I'll go try and find your father.")
                .endWith() { df, player ->
                    if(getQuestStage(player, SeaSlug.questName) == 3) {
                        setQuestStage(player, SeaSlug.questName, 4)
                    }
                }
        b.onQuestStages(SeaSlug.questName, 4,5,6)
                .playerl(FacialExpression.THINKING, "Are you okay?")
                .npcl(FacialExpression.CHILD_SAD, "I want to see daddy!")
                .playerl("I'm working on it.")
                .end()

        b.onQuestStages(SeaSlug.questName, 7)
                .playerl("Hello Kennith, are you okay?")
                .npcl(FacialExpression.CHILD_SAD, "No, I want my daddy.")
                .playerl("You'll be able to see him soon. First we need to get you back to land, come with me to the boat.")
                .npcl(FacialExpression.CHILD_SHOCKED, "No!")
                .playerl("What, why not?")
                .npcl(FacialExpression.CHILD_SHOCKED, "I'm scared of those nasty sea slugs. I won't go near them.")
                .playerl("Okay, you wait here and I'll go figure another way to get you out.")
                .endWith() { df, player ->
                    if(getQuestStage(player, SeaSlug.questName) == 7) {
                        setQuestStage(player, SeaSlug.questName, 8)
                    }
                }

        b.onQuestStages(SeaSlug.questName, 8)
                // This stage is unfortunately left out. You can't interact with Kennith authentically.
                .end()

        b.onQuestStages(SeaSlug.questName, 9)
                .playerl("Kennith, I've made an opening in the wall. You can come out through there.")
                .npcl(FacialExpression.CHILD_THINKING, "Are there any sea slugs on the other side?")
                .playerl("Not one.")
                .npcl(FacialExpression.CHILD_THINKING, "How will I get downstairs?")
                .playerl("I'll figure that out in a moment.")
                .npcl(FacialExpression.CHILD_NORMAL, "Ok, when you have I'll come out.")
                .endWith() { df, player ->
                    if(getQuestStage(player, SeaSlug.questName) == 9) {
                        setQuestStage(player, SeaSlug.questName, 10)
                    }
                }

        b.onQuestStages(SeaSlug.questName, 10)
                // This stage is also unfortunately left out. You can't interact with Kennith authentically.
                .end()

        b.onQuestStages(SeaSlug.questName, 11,100)
                // Kennith is varp swapped out, so is no longer here.
                .end()

    }
}

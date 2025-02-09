package content.region.kandarin.witchhaven.quest.seaslug

import content.data.Quests
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression

class HolgartPlatformDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(Quests.SEA_SLUG, 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 100)
                .playerl(FacialExpression.FRIENDLY, "Hey, Holgart.")
                .npcl("Have you had enough of this place yet? It's really starting to scare me.")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("Okay, let's go back.")
                            .endWith() { df, player ->
                                SeaSlugListeners.seaslugBoatTravel(player, 1)
                            }
                    optionBuilder.option_playerl("No, I'm going to stay a while.")
                            .npcl("Okay... you're the boss.")
                            .end()
                }

        b.onQuestStages(Quests.SEA_SLUG, 4)
                .playerl("Holgart, something strange is going on here.")
                .npcl("You're telling me, none of the sailors seem to remember who I am.")
                .playerl("Apparently Kennith's father left for help a couple of days ago.")
                .npcl("That's a worry, no-one's heard from him on shore. Come on, we'd better go look for him.")
                .endWith() { df, player ->
                    SeaSlugListeners.seaslugBoatTravel(player, 2)
                }

        b.onQuestStages(Quests.SEA_SLUG, 11)
                .playerl("Did you get the kid back to shore?")
                .npcl("Yes, he's safe and sound with his parents. Your turn to return to land now adventurer.")
                .playerl("Looking forward to it.")
                .endWith() { df, player ->
                    SeaSlugListeners.seaslugBoatTravel(player, 1)
                }

    }
}

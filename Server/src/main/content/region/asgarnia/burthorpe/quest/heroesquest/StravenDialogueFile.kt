package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.*
import org.rs09.consts.Items

class StravenDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        // 0 is handled by default in the old StravenDialogue.
        b.onQuestStages(HeroesQuest.questName, 1)
                .playerl("How would I go about getting a Master Thief armband?")
                .npcl("Ooh... tricky stuff. Took me YEARS to get that rank.")
                .npcl("Well, what some of the more aspiring thieves in our gang are working on right now is to steal some very valuable candlesticks from Scarface Pete - the pirate leader on Karamja.")
                .npcl("His security is excellent, and the target very valuable so that might be enough to get you the rank.")
                .npcl("Go talk to our man Alfonse, the waiter in the Shrimp and Parrot.")
                .npcl("Use the secret word 'gherkin' to show you're one of us.")
                .endWith { _, player ->
                    if(getQuestStage(player, HeroesQuest.questName) == 1) {
                        setQuestStage(player, HeroesQuest.questName, 2)
                    }
                }

        b.onQuestStages(HeroesQuest.questName, 2,3,4)
                .playerl("What am I supposed to be doing again?")
                .npcl("You told me you wanted to get a Master thief's armband! Now pay attention.")
                .npcl("Some of the more aspiring thieves in our gang are working on right now is to steal some very valuable candlesticks from Scarface Pete - the pirate leader on Karamja.")
                .npcl("His security is excellent, and the target very valuable so that might be enough to get you the rank.")
                .npcl("Go talk to our man Alfonse, the waiter in the Shrimp and Parrot.")
                .npcl("Use the secret word 'gherkin' to show you're one of us.")
                .end()


        b.onQuestStages(HeroesQuest.questName, 5)
                .branch { player ->
                    return@branch if (inInventory(player, Items.PETES_CANDLESTICK_1577)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("I have retrieved a candlestick!")
                            .npcl("Hmmm. Not bad, not bad. Let's see it, make sure it's genuine.")
                            .linel("You hand Straven the candlestick.")
                            .playerl("So is this enough to get me a Master Thief armband?")
                            .npcl("Hmm... I dunno... Aww, go on then. I suppose I'm in a generous mood today.")
                            .linel("Straven hands you a Master Thief armband.")
                            .endWith { _, player ->
                                if (removeItem(player, Items.PETES_CANDLESTICK_1577)) {
                                    addItemOrDrop(player, Items.THIEVES_ARMBAND_1579)
                                    if (getQuestStage(player, HeroesQuest.questName) == 5) {
                                        setQuestStage(player, HeroesQuest.questName, 6)
                                    }
                                }
                            }

                    branch.onValue(0)
                            .playerl("What am I supposed to be doing again?")
                            .npcl("You told me you wanted to get a Master thief's armband! Now pay attention.")
                            .npcl("Some of the more aspiring thieves in our gang are working on right now is to steal some very valuable candlesticks from Scarface Pete - the pirate leader on Karamja.")
                            .npcl("His security is excellent, and the target very valuable so that might be enough to get you the rank.")
                            .npcl("Go talk to our man Alfonse, the waiter in the Shrimp and Parrot.")
                            .npcl("Use the secret word 'gherkin' to show you're one of us.")
                            .end()
                }

        // I lost the armband and some stupid default shit.
        b.onQuestStages(HeroesQuest.questName, 6)
                .playerl("I'm afraid I've lost my master thief's armband.")
                .npcl("Lucky for you I have a spare. Don't lose it again!")
                .endWith { _, player ->
                    addItemOrDrop(player, Items.THIEVES_ARMBAND_1579)
                }

    }
}
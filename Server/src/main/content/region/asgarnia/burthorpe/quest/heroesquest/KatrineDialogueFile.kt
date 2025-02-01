package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.*
import org.rs09.consts.Items

class KatrineDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        // 0 is handled by default in the old KatrineDialogue.
        b.onQuestStages(HeroesQuest.questName, 1)
                .playerl("Hey.")
                .npcl("Hey.")
                .options()
                .let { optionBuilder ->

                    optionBuilder.option_playerl("Who are all those people in there?")
                            .npcl("They're just various rogues and thieves.")
                            .playerl("They don't say a lot...")
                            .npcl("Nope.")
                            .end()

                    optionBuilder.option_playerl("Is there any way I can get the rank of master thief?")
                            .npcl("Master thief? Ain't we the ambitious one!")
                            .npcl("Well, you're gonna have to do something pretty amazing.")
                            .playerl("Anything you can suggest?")
                            .npcl("Well, some of the MOST coveted prizes in thiefdom right now are in the pirate town of Brimhaven on Karamja.")
                            .npcl("The pirate leader Scarface Pete has a pair of extremely valuable candlesticks.")
                            .npcl("His security is VERY good.")
                            .npcl("We, of course, have gang members in a town like Brimhaven who may be able to help you.")
                            .npcl("Visit our hideout in the alleyway on palm street.")
                            .npcl("To get in you will need to tell them the secret password 'four leaved clover'.")
                            .endWith { _, player ->
                                if(getQuestStage(player, HeroesQuest.questName) == 1) {
                                    setQuestStage(player, HeroesQuest.questName, 2)
                                }
                            }

                }

        // This is not authentic, she falls back to a boring default conversation, but I guess people might need help during the quest
        b.onQuestStages(HeroesQuest.questName, 2,3,4)
                .playerl("What am I supposed to be doing again?")
                .npcl("You told me you wanted to get the rank of master thief! Now pay attention.")
                .npcl("Some of the MOST coveted prizes in thiefdom right now are in the pirate town of Brimhaven on Karamja.")
                .npcl("The pirate leader Scarface Pete has a pair of extremely valuable candlesticks.")
                .npcl("His security is VERY good.")
                .npcl("We, of course, have gang members in a town like Brimhaven who may be able to help you.")
                .npcl("Visit our hideout in the alleyway on palm street.")
                .npcl("To get in you will need to tell them the secret password 'four leaved clover'.")
                .end()

        // As FYI, the fallback for 2,3,4 is
        /**
         *                 .options()
         *                 .let { optionBuilder ->
         *                     optionBuilder.option_playerl("Who are all those people in there?")
         *                             .npcl("They're just various rogues and thieves.")
         *                             .playerl("They don't say a lot...")
         *                             .npcl("Nope.")
         *                             .end()
         *                     optionBuilder.option("Teach me to be a top class criminal!")
         *                             .playerl("Teach me to be a top class criminal.")
         *                             .npcl("Teach yourself.")
         *                             .end()
         *
         *
         *
         *     Player:
         *     I have a candlestick now!
         *     Katrine:
         *     Good for you. I'll give a master thief's armband to the one who retrieved that. I know it wasn't you.
         */


        b.onQuestStages(HeroesQuest.questName, 5)
                .branch { player ->
                    return@branch if (inInventory(player, Items.PETES_CANDLESTICK_1577)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("Who are all those people in there?")
                                        .npcl("They're just various rogues and thieves.")
                                        .playerl("They don't say a lot...")
                                        .npcl("Nope.")
                                        .end()
                                optionBuilder.option("I have a candlestick now.")
                                        .playerl("I have a candlestick now!")
                                        .npcl("Wow... is... it REALLY it?")
                                        .npcl("This really is a FINE bit of thievery.")
                                        .npcl("Us thieves have been trying to get hold of this one for a while!")
                                        .npc("You wanted to be ranked as a master thief didn't you?", "Well, I guess this just about ranks as good enough!")
                                        .linel("Katrine gives you a master thief armband.")
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
                                        .npcl("You told me you wanted to get the rank of master thief! Now pay attention.")
                                        .npcl("Some of the MOST coveted prizes in thiefdom right now are in the pirate town of Brimhaven on Karamja.")
                                        .npcl("The pirate leader Scarface Pete keeps an extremely valuable candlesticks.")
                                        .npcl("His security is VERY good.")
                                        .npcl("We, of course, have gang members in a town like Brimhaven who may be able to help you.")
                                        .npcl("Visit our hideout in the alleyway on palm street.")
                                        .npcl("To get in you will need to tell them the secret password 'four leaved clover'.")
                                        .end()
                            }
                }

        // I lost the armband and some stupid default shit.
        b.onQuestStages(HeroesQuest.questName, 6)
                .playerl("I have lost my master thief's armband...")
                .npcl("Lucky I 'ave a spare ain't it? Don't lose it again.")
                .endWith { _, player ->
                    addItemOrDrop(player, Items.THIEVES_ARMBAND_1579)
                }


    }
}
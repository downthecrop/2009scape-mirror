package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

class ZavisticRarveDialogueFile : DialogueBuilderFile() {

    companion object {
        private fun hasEvidences(player: Player): Int {
            var count = 0
            if (inInventory(player, Items.NECROMANCY_BOOK_4837)) { count++ }
            if (inInventory(player, Items.BOOK_OF_HAM_4829)) { count++ }
            if (inInventory(player, Items.DRAGON_INN_TANKARD_4811)) { count++ }
            if (inInventory(player, Items.SIGNED_PORTRAIT_4816)) { count++ }
            return count
        }

        fun dialogueBlackPrismAndTornPage(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("There's some undead ogre activity over at Jiggig, I've found some clues, I wondered if you'd have a look at them.")
                    .manualStage() { df, player, _, _ ->
                        sendDoubleItemDialogue(player, Items.BLACK_PRISM_4808, Items.TORN_PAGE_4809, "You show the prism and the necromantic half page to the aged wizard.")
                    }
                    .npcl("Hmmm, now this is interesting! Where did you get these from?")
                    .playerl("I got them from a nearby Ogre tomb, it's recently been infested with zombie ogres and I'm trying to work out what happened there.")
                    .npcl("This is very troubling @name, very troubling indeed. While it's permitted for learned members of our order to research the 'dark arts', it's absolutely forbidden to make use of such magic.")
                    .playerl("Do you have any leads on people that I might talk to regarding this?")
                    .npcl("Well a wizard by the name of 'Sithik Ints' was doing some research in this area. He may know something about it. He's lodged at that guest house to the North, though he's ill and isn't able to leave his room.")
                    .npcl("Why not go and talk to him, poke around a bit and see if anything comes up. Let me know how you get on. However, I doubt that 'Sithik' had anything to do with it.")
                    .npcl("Hmm, that black prism seems to have some magical protection. Once you've finished with this item, bring it back to me would you. I may have a reward for you.")
        }

        fun dialogueGoodPortrait(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Look, I made a portrait of Sithik.")
                    .iteml(Items.SITHIK_PORTRAIT_4814, "You show the portrait of Sithik to Zavistic.")
                    .npcl("Hmm, great...but I already know what he looks like!")
        }

        fun dialogueBadPortrait(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .playerl("Look, I made a portrait of Sithik.")
                    .iteml(Items.SITHIK_PORTRAIT_4815, "You show the sketch...")
                    .npcl("Who the demonikin is that? Is it meant to be a portrait of Sithik, it doesn't look anything like him!")
        }

        // Too lazy to do this, but this gets mentioned whenever you've shown him one of the evidence (but not all).
        fun dialogueSeenEvidenceBefore(builder: DialogueBuilder): DialogueBuilder {
            return builder
                    .npcl("Yeah, you've shown me this before...if this is all the evidence you have?")
                    .playerl("Please just look at it again...")
                    .npcl("Ok, let me look then.")
        }
    }

    override fun create(b: DialogueBuilder) {
        b.onQuestStages(ZogreFleshEaters.questName, 2)
                .let { content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.dialogueInitialTalk(it) }
                .branch { player ->
                    return@branch if (inInventory(player, Items.BLACK_PRISM_4808) && inInventory(player, Items.TORN_PAGE_4809) ) {
                        3
                    } else if (inInventory(player, Items.BLACK_PRISM_4808) ) {
                        2
                    } else if (inInventory(player, Items.TORN_PAGE_4809) ) {
                        1
                    } else {
                        0
                    }
                }.let { branch ->
                    branch.onValue(3)
                            .let{ dialogueBlackPrismAndTornPage(it) }
                            .endWith { _, player ->
                                if(getQuestStage(player, ZogreFleshEaters.questName) == 2) {
                                    setQuestStage(player, ZogreFleshEaters.questName, 3)
                                }
                            }

                    branch.onValue(2)
                            .playerl("There's some undead ogre activity over at 'Jiggig', and the ogres have asked me to look into it. I think I've found a clue and I wonder if you could take a look at it for me?")
                            .iteml(Items.BLACK_PRISM_4808, "You show the black prism to the aged wizard.")
                            .npcl("Hmmm, well this is an uncommon spell component. On its own it's useless, but with certain necromantic spells it can be very powerful. Did you find anything else there?")
                            .branch { player ->
                                return@branch if (inInventory(player, Items.DRAGON_INN_TANKARD_4811)) { 1 } else { 0 }
                            }.let { branch2 ->
                                val returnJoin = b.placeholder()
                                branch2.onValue(0)
                                        .goto(returnJoin)
                                branch2.onValue(1)
                                        .iteml(Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic.")
                                        .playerl("Well, I found this...")
                                        .npcl("Hmmm, no, that's not really associated with this to be honest. Did you find anything else there?")
                                        .goto(returnJoin)
                                return@let returnJoin.builder()
                            }
                            .playerl("Not really.")
                            .npcl("I don't know what to say then, there isn't enough to go on with the clues you've shown me so far. I'd suggest going back to search a bit more, but you may just be wasting your time?")
                            .npcl("Hmm, but this prism does seem to have some magical protection. Once you've finished with this item, bring it back to me would you? I may have a reward for you!")
                            .playerl("Sure...I mean, I'll try if I remember.")
                            .end()

                    branch.onValue(1)
                            .playerl("There's some undead ogre activity over at Jiggig, I've found a clue that you may be able to help with.")
                            .iteml(Items.TORN_PAGE_4809, "You show the necromantic half page to the aged wizard.")
                            .npcl("Hmm, this is a half torn spell page, it requires another spell component to be effective. Did you find anything else there?")
                            .branch { player ->
                                return@branch if (inInventory(player, Items.DRAGON_INN_TANKARD_4811)) { 1 } else { 0 }
                            }.let { branch2 ->
                                val returnJoin = b.placeholder()
                                branch2.onValue(0)
                                        .goto(returnJoin)
                                branch2.onValue(1)
                                        .iteml(Items.DRAGON_INN_TANKARD_4811, "You show the tankard to Zavistic.")
                                        .playerl("Well, I found this...")
                                        .npcl("Hmmm, no, that's not really associated with this to be honest. Did you find anything else there?")
                                        .goto(returnJoin)
                                return@let returnJoin.builder()
                            }
                            .playerl("Not really.")
                            .npcl("I don't know what to say then, there isn't enough to go on with the clues you've shown me so far. I'd suggest going back to search a bit more, but you may just be wasting your time?")
                            .end()

                    branch.onValue(0)
                            .let { builder ->
                                content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.defaultTalk(builder)
                            }
                }


        b.onQuestStages(ZogreFleshEaters.questName, 3,4)
                .let { content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.dialogueInitialTalk(it) }
                .let { builder ->
                    val returnJoin = b.placeholder()
                    builder.goto(returnJoin)
                    return@let returnJoin.builder().options()
                            .let { optionBuilder ->
                                val continuePath = b.placeholder()
                                optionBuilder.option("What did you say I should do?")
                                        .playerl("What did you say I should do?")
                                        .npcl("You should go and have a chat with Sithik Ints, he's in that house just to the north. He's a lodger and has a room upstairs. Just tell him that I sent you to see him. He should be fine once you've mentioned my name.")
                                        .goto(returnJoin)
                                optionBuilder.option_playerl("Where is Sithik?")
                                        .npcl("He's in that house just to the north, less than a few seconds walk away. He's a lodger and has a room upstairs...he's not very well though.")
                                        .goto(returnJoin)
                                optionBuilder.optionIf("I have an item that I'd like you to look at.") { player -> return@optionIf hasEvidences(player) == 1 }
                                        .goto(continuePath)
                                optionBuilder.optionIf("I have an item that I'd like you to look at.") { player -> return@optionIf hasEvidences(player) > 1 }
                                        .goto(continuePath)
                                optionBuilder.option("I want to ask about the magic guild")
                                        .let { content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.defaultTalk(it) }
                                optionBuilder.option_playerl("Sorry, I have to go.")
                                        .end()

                                return@let continuePath.builder()
                            }
                }
                .branch { player ->
                    return@branch if (inInventory(player, Items.NECROMANCY_BOOK_4837) ) { 1 } else { 0 }
                }.let { branch ->
                    val continuePath = b.placeholder()
                    branch.onValue(1)
                            .iteml(Items.NECROMANCY_BOOK_4837, "You show the Necromancy book to Zavistic.")
                            .playerl("I have this necromancy book as evidence that Sithik is involved with the undead ogres at Jiggig.")
                            .npcl("Ok, so he's researching necromancy...it doesn't mean anything in itself.")
                            .playerl("Yes, but if you look, you can see that there is a half torn page which matches the page I found at Jiggig.")
                            .npcl("Hmm, yes, but someone could have stolen that from him and then gone and cast it without his permission or to try and deliberately implicate him.")
                            .goto(continuePath)
                    branch.onValue(0)
                            .goto(continuePath)
                    return@let continuePath.builder()
                }
                .branch { player ->
                    return@branch if (inInventory(player, Items.BOOK_OF_HAM_4829) ) { 1 } else { 0 }
                }.let { branch ->
                    val continuePath = b.placeholder()
                    branch.onValue(1)
                            .iteml(Items.BOOK_OF_HAM_4829, "You show the HAM book to Zavistic.")
                            .playerl("Look, this book proves that Sithik hates all monsters and most likely Ogres with a passion.")
                            .npcl("So what, hating monsters isn't a crime in itself...although I suppose that it does give a motive if Sithik was involved. On its own, it's not enough evidence though.")
                            .goto(continuePath)
                    branch.onValue(0)
                            .goto(continuePath)
                    return@let continuePath.builder()
                }
                .branch { player ->
                    return@branch if (inInventory(player, Items.DRAGON_INN_TANKARD_4811) ) { 1 } else { 0 }
                }.let { branch ->
                    val continuePath = b.placeholder()
                    branch.onValue(1)
                            .iteml(Items.DRAGON_INN_TANKARD_4811, "You show the dragon Inn Tankard to Zavistic.")
                            .playerl("This is the tankard I found on the remains of Brentle Vahn!")
                            .npcl("That doesn't mean anything in itself, you could have gotten that from anywhere. Even from the Dragon Inn tavern! There isn't anything to link Brentle Vahn with Sithik Ints.")
                            .goto(continuePath)
                    branch.onValue(0)
                            .goto(continuePath)
                    return@let continuePath.builder()
                }
                .branch { player ->
                    return@branch if (inInventory(player, Items.SIGNED_PORTRAIT_4816) ) { 1 } else { 0 }
                }.let { branch ->
                    val continuePath = b.placeholder()
                    branch.onValue(1)
                            .iteml(Items.SIGNED_PORTRAIT_4816, "You show the signed portrait of Sithik to Zavistic.")
                            .playerl("This is a portrait of Sithik, signed by the landlord of the Dragon Inn saying that he saw Sithik and Brentle Vahn together.")
                            .npcl("Hmmm, well that is interesting.")
                            .goto(continuePath)
                    branch.onValue(0)
                            .goto(continuePath)
                    return@let continuePath.builder()
                }
                .branch { player ->
                    return@branch if (hasEvidences(player) == 4) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .npcl("However, there isn't enough evidence for me to take the issue further at this point. If you find any further evidence bring it to me.")
                            .end()
                    return@let branch
                }
                .onValue(1)
                .npcl("And I'm starting to think that Sithik may be involved. Here, take this potion and give some to Sithik. It'll bring on a change which should solicit some answers - tell him the effects won't revert until he's told the truth.")
                .iteml(Items.STRANGE_POTION_4836 ,"Zavistic hands you a strange looking potion bottle and takes all the evidence you've accumulated so far.")
                .endWith { _, player ->
                    if(getQuestStage(player, ZogreFleshEaters.questName) in 3..4) {
                        if (removeItem(player, Items.NECROMANCY_BOOK_4837) &&
                                removeItem(player, Items.BOOK_OF_HAM_4829) &&
                                removeItem(player, Items.DRAGON_INN_TANKARD_4811) &&
                                removeItem(player, Items.SIGNED_PORTRAIT_4816)) {
                            addItemOrDrop(player, Items.STRANGE_POTION_4836)
                        }
                        setQuestStage(player, ZogreFleshEaters.questName, 5)
                    }
                }
        b.onQuestStages(ZogreFleshEaters.questName, 5)
                .let { content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.dialogueInitialTalk(it) }
                .npcl("Have you used that potion yet?")
                .branch { player ->
                    return@branch if (inInventory(player, Items.STRANGE_POTION_4836) ) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(1)
                            .playerl("No, not yet, what was I supposed to do again?")
                            .npcl("Try to use the potion on Sithik somehow, he should undergo an interesting transformation, though you'll probably want to leave the house in case there are any side effects. Then go back and question Sithik and tell")
                            .npcl("him the effects won't wear off until he tells the truth. In fact, that's not exactly true, but I'm sure it'll be an extra incentive to get him to be honest.")
                            .let { builder ->
                                content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.defaultTalk(builder)
                            }
                    branch.onValue(0)
                            .playerl("Well, actually, I've lost it, could I have another one please?")
                            .npcl("Sure, but don't lose it this time.")
                            .iteml(Items.STRANGE_POTION_4836 ,"Zavistic hands you a bottle of strange potion.")
                            .endWith { _, player ->
                                addItemOrDrop(player, Items.STRANGE_POTION_4836)
                            }
                }


        b.onQuestStages(ZogreFleshEaters.questName, 6,7,8,9)
                .let { content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.dialogueInitialTalk(it) }
                .npcl("Don't you worry about Sithik, he's not likely to be moving from his bed for a long time. When he eventually does get better, he's going to be sent before a disciplinary tribunal, then we'll sort out what's what.")
                .playerl("Thanks for your help with all of this.")
                .npcl("Ooohh, no thanks required. It's I who should be thanking you my friend...your investigative mind has shown how vigilant we really should be for this type of evil use of the magical arts.")
                .let { builder ->
                    content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile.defaultTalk(builder)
                }
    }
}

/** Dialogues when you use stuff on him. */
class ZavisticRarveUseItemsDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        // SITHIK_PORTRAIT_4814
        b.onPredicate { _ -> dialogueNum == 3 }
                .let{ZavisticRarveDialogueFile.dialogueGoodPortrait(it)}
                .end()

        // SITHIK_PORTRAIT_4815
        b.onPredicate { _ -> dialogueNum == 4 }
                .let{ZavisticRarveDialogueFile.dialogueBadPortrait(it)}
                .end()

        // SIGNED_PORTRAIT_4816
        b.onPredicate { _ -> dialogueNum == 5 }
                // That would continue the conversation as above if you normally talk to him.
                .end()
    }
}
package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import org.rs09.consts.Items

class SirTiffyCashienDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(RecruitmentDrive.questName, 1)
                .player(FacialExpression.FRIENDLY, "Sir Amik Varze sent me to meet you here for some", "sort of testing...")
                .npc(FacialExpression.FRIENDLY, "Ah, @name!", "Amik told me all about you, dontchaknow!", "Spliffing job you you did with the old Black Knights there,", "absolutely first class.")
                .playerl(FacialExpression.GUILTY, "...Thanks I think.")
                // .npcl(FacialExpression.FRIENDLY, "Well, not in those exact words, but you get my point, what?")
                .npc(FacialExpression.FRIENDLY, "Well, a top-notch filly like yourself is just the right sort", "we've been looking for for our organisation.")
                .npcl(FacialExpression.FRIENDLY, "So, are you ready to begin testing?")
                .let { path ->
                    val originalPath = b.placeholder()
                    path.goto(originalPath)
                    return@let originalPath.builder().options().let { optionBuilder ->
                        val continuePath = b.placeholder()
                        optionBuilder.option("Testing..?")
                                .playerl(FacialExpression.FRIENDLY, "Testing? What exactly do you mean by testing?")
                                .npcl(FacialExpression.FRIENDLY, "Jolly bad show! Varze was supposed to have informed you about all this before sending you here!")
                                .npcl(FacialExpression.FRIENDLY, "Well, not your fault I suppose, what? Anywho, our organisation is looking for a certain specific type of person to join.")
                                .playerl(FacialExpression.FRIENDLY, "So... You want me to go kill some monster or something for you?")
                                .npcl(FacialExpression.FRIENDLY, "Not at all, old bean. There's plenty of warriors around should we require dumb muscle.")
                                .npcl(FacialExpression.FRIENDLY, "That's really not the kind of thing our organisation is after, what?")
                                .playerl(FacialExpression.FRIENDLY, "So you want me to go and fetch you some kind of common item, and then take it for delivery somewhere on the other side of the country?")
                                .playerl(FacialExpression.FRIENDLY, "Because I really hate doing that!")
                                .npcl(FacialExpression.FRIENDLY, "Haw, haw, haw! What a dull thing to ask of someone, what?")
                                .npcl(FacialExpression.FRIENDLY, "I know what you mean, though. I did my fair share of running errands when I was a young adventurer, myself!")
                                .playerl(FacialExpression.FRIENDLY, "So what exactly will this test consist of?")
                                .npcl(FacialExpression.FRIENDLY, "Can't let just any old riff-raff in, what? The mindless thugs and bully boys are best left in the White Knights or the city guard. We look for the top-shelf brains to join us.")
                                .playerl(FacialExpression.FRIENDLY, "So you want to test my brains? Will it hurt?")
                                .npcl(FacialExpression.FRIENDLY, "Haw, haw, haw! That's a good one!")
                                .npcl(FacialExpression.FRIENDLY, "Not in the slightest.. Well, maybe a bit, but we all have to make sacrifices occasionally, what?")
                                .playerl(FacialExpression.FRIENDLY, "What do you want me to do then?")
                                .npcl(FacialExpression.FRIENDLY, "It's a test of wits, what? I'll take you to our secret training grounds, and you will have to pass through a series of five separate intelligence test to prove you're our sort of adventurer.")
                                .npcl(FacialExpression.FRIENDLY, "Standard puzzle room rules will apply.")
                                .playerl(FacialExpression.THINKING, "Erm... What are standard puzzle room rules exactly?")
                                .npcl(FacialExpression.HAPPY, "Never done this sort of thing before, what?")
                                .npc("The simple rules are:", "No items or equipment to be brought with you.", "Each room is a self-contained puzzle.", "You may quit at any time.")
                                .npcl(FacialExpression.HAPPY, "Of course, if you quit a room, then all your progress up to that point will be cleared, and you'll have to start again from scratch.")
                                .npc(FacialExpression.HAPPY, "Our organisation manages to filter all the top-notch", "adventurers this way.", "So, are you ready to go?")
                                .goto(originalPath)
                        optionBuilder.option("Organisation?")
                                .playerl(FacialExpression.THINKING, "This organisation you keep mentioning.. Perhaps you could tell me a little about it?")
                                .npcl(FacialExpression.FRIENDLY, "Oh, that Amik! Jolly bad form. Did he not tell you anything that he was supposed to?")
                                .playerl(FacialExpression.FRIENDLY, "No. He didn't really tell me anything except to come here and meet you.")
                                .npcl(FacialExpression.FRIENDLY, "Well, now, old sport, let me give you the heads up and the low down, what?")
                                .npcl(FacialExpression.FRIENDLY, "I represent the Temple Knights. We are the premier order of Knights in Asgarnia, if not the world. Saradomin himself personally founded our order centuries ago, and we answer only to him.")
                                .npcl(FacialExpression.FRIENDLY, "Only the very best of the best are permitted to join, and the powers we command are formidable indeed.")
                                .npcl(FacialExpression.FRIENDLY, "You might say that we are the front line of defence for the entire kingdom!")
                                .playerl(FacialExpression.THINKING, "So what's the difference between you and the White Knights?")
                                .npcl(FacialExpression.FRIENDLY, "Well, in simple terms, we're better! Any fool with a sword can manage to get into the White Knights, which is mostly the reason they are so very, very incompetent, what?")
                                .npcl(FacialExpression.FRIENDLY, "The Temple Knights, on the other hand, have to be smarter, stronger and better than all others. We are the elite. No man controls us, for our orders come directly from Saradomin himself!")
                                .npcl(FacialExpression.FRIENDLY, "According to Sir Vey Lance, our head of operations, that is. He claims that everything he tells us to do is done with Saradomin's implicit permission.")
                                .npcl(FacialExpression.FRIENDLY, "It's not every job where you have more authority than the king, though, is it?")
                                .playerl(FacialExpression.THINKING, "Wait... You can order the King around?")
                                .npcl(FacialExpression.FRIENDLY, "Well, not me personally. I'm only in the recruitment side of things, dontchaknow, but the higher ranking members of the organisation have almost absolute power over the kingdom.")
                                .npcl(FacialExpression.FRIENDLY, "Plus a few others, so I hear...")
                                .npcl(FacialExpression.FRIENDLY, "Anyway, this is why we keep our organisation shrouded in secrecy, and why we demand such rigorous testing for all potential recruits. Speaking of which, are you ready to begin your testing?")
                                .goto(originalPath)
                        optionBuilder.option("Yes, let's go!")
                                .player(FacialExpression.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!")
                                .branch { player -> if(player.inventory.isEmpty && player.equipment.isEmpty && !player.familiarManager.hasFamiliar()) { 1 } else { 0 } }
                                .let { branch ->
                                    branch.onValue(0)
                                            .npcl(FacialExpression.NEUTRAL, "Well, bad luck, old @g[guy,gal]. You'll need to have a completely empty inventory and you can't be wearing any equipment before we can accurately test you.")
                                            .npcl(FacialExpression.HAPPY, "Don't want people cheating by smuggling stuff in, what? That includes things carried by familiars, too! Come and see me again after you've been to the old bank to drop your stuff off, what?")
                                            .end()
                                    return@let branch
                                }
                                .onValue(1)
                                .npc(FacialExpression.HAPPY, "Jolly good show!", "Now the training grounds location is a secret, so...")
                                .goto(continuePath)
                        optionBuilder.option("No, I've changed my mind.")
                                .player("No, I've changed my mind.")
                                .end()

                        return@let continuePath.builder()
                    }

                }.endWith { _, player ->
                    if (getQuestStage(player, RecruitmentDrive.questName) == 1) {
                        setQuestStage(player, RecruitmentDrive.questName, 2)
                    }
                    RecruitmentDriveListeners.shuffleStages(player)
                    RecruitmentDriveListeners.StartTestCutscene(player).start()
                }
        b.onQuestStages(RecruitmentDrive.questName, 2)
                .npc(FacialExpression.FRIENDLY, "Ah, what ho!", "Back for another go at the old testing, what?")
                .options().let { optionBuilder ->
                    val continuePath = b.placeholder()
                    optionBuilder.option("Yes, let's go!")
                            .player(FacialExpression.FRIENDLY, "Yeah. this sounds right up my street.", "Let's go!")
                            .branch { player -> if(player.inventory.isEmpty && player.equipment.isEmpty && !player.familiarManager.hasFamiliar()) { 1 } else { 0 } }
                            .let { branch ->
                                branch.onValue(0)
                                        .npcl(FacialExpression.NEUTRAL, "Well, bad luck, old @g[guy,gal]. You'll need to have a completely empty inventory and you can't be wearing any equipment before we can accurately test you.")
                                        .npcl(FacialExpression.HAPPY, "Don't want people cheating by smuggling stuff in, what? That includes things carried by familiars, too! Come and see me again after you've been to the old bank to drop your stuff off, what?")
                                        .end()
                                return@let branch
                            }
                            .onValue(1)
                            .npc(FacialExpression.FRIENDLY, "Jolly good show!", "Now the training grounds location is a secret, so...")
                            .endWith { _, player ->
                                RecruitmentDriveListeners.shuffleStages(player)
                                RecruitmentDriveListeners.StartTestCutscene(player).start()
                            }
                    optionBuilder.option("No, I've changed my mind.")
                            .player("No, I've changed my mind.")
                            .end()
                    return@let continuePath.builder()
                }
        b.onQuestStages(RecruitmentDrive.questName, 3)
                .npc(FacialExpression.HAPPY, "Oh, jolly well done!", "Your performance will need to be evaluated by Sir Vey", "personally, but I don't think it's going too far ahead of", "myself to welcome you to the team!")
                .endWith { _, player ->
                    // Get a voucher and $3000 to change gender if you did do it during the quest.
                    if (getAttribute(player, RecruitmentDrive.attributeOriginalGender, true) != player.isMale) {
                        addItemOrDrop(player, Items.MAKEOVER_VOUCHER_5606)
                        addItemOrDrop(player, Items.COINS_995, 3000)
                    }
                    removeAttribute(player, RecruitmentDrive.attributeOriginalGender)
                    finishQuest(player, RecruitmentDrive.questName)
                }
    }
}

class SirTiffyCashienFailedDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .npc(FacialExpression.SAD, "Oh, jolly bad luck, what?", "Not quite the brainbox you thought you were, eh?")
                .npc(FacialExpression.HAPPY, "Well, never mind!", "You have an open invitation to join our organization, so", "when you're feeling a little smarter, come back and talk", "to me again.")

    }
}

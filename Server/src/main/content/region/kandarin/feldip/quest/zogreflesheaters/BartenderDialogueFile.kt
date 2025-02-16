package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import org.rs09.consts.Items

class BartenderDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onPredicate { _ -> dialogueNum == 1 }
                .branch { player ->
                    return@branch if (getAttribute(player, ZogreFleshEaters.attributeAskedAboutTankard, false)) { 1 } else { 0 }
                }.let { branch ->
                    branch.onValue(0)
                            .playerl("Hello there, I found this tankard in an ogre tomb cavern. It has the emblem of this Inn on it and I wondered if you knew anything about it?")
                            .npcl("Oh yes, this is Brentle's mug...I'm surprised he left it just lying around down some cave. He's quite protective of it.")
                            .playerl("Brentle you say? So you knew him then?")
                            .npcl("Yeah, this belongs to 'Brentle Vahn', he's quite a common customer, though I've not seen him in a while.")
                            .npcl(FacialExpression.THINKING, "He was talking to some shifty looking wizard the other day. I don't know his name, but I'd recognise him if I saw him.")
                            .playerl("Hmm, I'm sorry to tell you this, but Brentle Vahn is dead - I believe he was murdered.")
                            .npcl(FacialExpression.EXTREMELY_SHOCKED, "Noooo! I'm shocked...")
                            .npcl("...but not surprised. He was a good customer...but I knew he would sell his sword arm and do many a dark deed if paid enough.")
                            .npcl(FacialExpression.SAD, "If you need help bringing the culprit to justice, you let me know.")
                            .endWith { _, player ->
                                setAttribute(player, ZogreFleshEaters.attributeAskedAboutTankard, true)
                            }
                    branch.onValue(1)
                            .playerl("Hello again. Can you tell me what you know about this tankard again please?")
                            .npcl("Oh yes, Brentle's tankard. Yeah, you've shown me this already. It belonged to Brentle Vahn, he was quite a common customer, though I've not seen him in a while.")
                            .npcl("He was talking to some shifty looking wizard the other day. I don't know his name, but I'd recognise him if I saw him.")
                            .npcl(FacialExpression.SAD, "If you need help bringing the culprit to justice, you let me know.")
                            .end()
                }

        b.onPredicate { _ -> dialogueNum == 2 }
                .iteml(Items.SITHIK_PORTRAIT_4814, "You show the portrait to the Inn keeper.")
                .npcl("Yeah, that's the guy who was talking to Brentle Vahn the other day! Look at those eyes, never a more shifty looking pair will you ever see!")
                .playerl("Hmm, you've just identified the man who I think sent Brentle Vahn to his death.")
                .playerl("I'm trying to bring him to justice with the Wizards' Guild grand secretary. Do you think you could sign this portrait to say that he was talking to Brentle Vahn.")
                .npcl("I can and I will!")
                .betweenStage { df, player, _, _ ->
                    if (removeItem(player, Items.SITHIK_PORTRAIT_4814)) {
                        addItemOrDrop(player, Items.SIGNED_PORTRAIT_4816)
                    }
                }
                .iteml(Items.SIGNED_PORTRAIT_4816, "The Dragon Inn bartender signs the portrait.")
                .playerl("Many thanks for your help, it's really very good of you.")
                .npcl("Not at all, just doing my part.")
                .end()


        b.onPredicate { _ -> dialogueNum == 3 }
                .iteml(Items.SITHIK_PORTRAIT_4815, "You show the sketch to the Inn keeper.")
                .npcl("Who's that? I mean, I guess it's a picture of a person isn't it? Sorry...you've got me? And before you ask, you're not putting it up on my wall!")
                .playerl("It's a portrait of Sithik Ints...don't you recognise him?")
                .npcl("I'm sorry, I really am, but I just don't see it...can you make a better picture?")
                .playerl("I'll try...")
                .end()
    }
}
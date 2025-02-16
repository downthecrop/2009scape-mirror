package content.region.asgarnia.mudskipperpoint.dialogue

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Vars


@Initializable
class SkippyDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SkippyBucketDialogue(0, getVarbit(player, Vars.VARBIT_MINI_QUEST_MOGRE)))
        return true
    }


    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKIPPY_2795)
    }

}


class SkippyBucketDialogue(override var stage: Int = 0, val questStage: Int = 0) : DialogueFile() {

    companion object{
        // Skippy will progress through these stages as you help him
        const val DRUNK = 0
        const val TENDER = 1
        const val HUNGOVER = 2
        const val DONE_QUEST = 3

        // Dialogue branches
        const val THROW_WATER = 10
        const val DRUNK_RAMBLING = 30
        const val CRAZY_PEOPLE = 40
        const val WHO_ARE_THEY = 50
        // Tea state
        const val LEARN_RECIPE = 170
        const val PLAGUE_CITY_RECIPE = 180
        const val NO_TEA = 190
        // Hangover cure state
        const val CURE = 220
        const val NO_CURE = 250
        const val RECIPE = 260
        const val NAME_ORIGIN = 270

        const val ALL_DONE = 300
    }

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SKIPPY_2796)
        when (questStage){
            DRUNK -> when (stage) {
                0 -> if(hasAnItem(player!!, Items.BUCKET_OF_WATER_1929).exists()){
                    playerl(FacialExpression.THINKING, "Well, I could dump this bucket of water over him. That would sober him up a little.").also { stage = THROW_WATER }
                }
                else {
                    playerl(FacialExpression.ASKING, "Are you all right? You seem a little...incoherent.").also { stage = DRUNK_RAMBLING }
                }
                THROW_WATER -> showTopics(
                    Topic("Throw the water!", THROW_WATER+ 1, true),
                    Topic("I think I'll leave it for now.", END_DIALOGUE)
                )
                THROW_WATER + 1 -> playerl(FacialExpression.NEUTRAL, "Hey, Skippy!").also { stage++ }
                THROW_WATER + 2 -> npcl(FacialExpression.DRUNK, "What?").also {
                    stage = END_DIALOGUE
                    queueScript(player!!, 1){ animationStage : Int ->
                        when(animationStage){
                            1 -> {
                                // todo get the right animation
                                animate(player!!, 2283)
                                removeItem(player!!, Items.BUCKET_OF_WATER_1929)
                                addItem(player!!, Items.BUCKET_1925)
                                setVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE, TENDER, true)
                                return@queueScript delayScript(player!!, 1)
                            }
                            2 -> {
                                openDialogue(player!!, SkippyBucketDialogue(THROW_WATER + 4))
                                return@queueScript delayScript(player!!, 1)
                            }
                            3 -> return@queueScript stopExecuting(player!!)
                            else -> return@queueScript delayScript(player!!, 1)
                        }
                    } }
                THROW_WATER + 4 -> npcl(FacialExpression.ANGRY, "Ahhhhhhhhhhgh! That's cold! Are you trying to kill me?").also { stage++ }
                THROW_WATER + 5 -> playerl(FacialExpression.NEUTRAL, "Nope. Just sober you up. Memory coming back yet?").also { stage++ }
                THROW_WATER + 6 -> npcl(FacialExpression.ANGRY, "No. But I could do with a bowl of tea to warm myself up a bit. Go grab me one and we'll talk.").also { stage++ }
                THROW_WATER + 7 -> playerl(FacialExpression.ASKING, "Any particular type of tea?").also { stage++ }
                THROW_WATER + 8 -> npcl(FacialExpression.ANGRY, "Nettle for preference. Just make sure it's hot.").also { stage++ }
                THROW_WATER + 9 -> npcl(FacialExpression.ANGRY, "And don't throw it at me!").also { stage++ }
                THROW_WATER + 10 -> playerl(FacialExpression.HALF_GUILTY, "What's your problem? You're all clean now.").also { stage = END_DIALOGUE }


                DRUNK_RAMBLING -> npcl(FacialExpression.DRUNK, "Inc'hearnt? Inc'herant! You...you...with yer fancy book- lernin' words. You'd be more than inc'herant if youd seen...").also { stage++ }
                DRUNK_RAMBLING + 1 -> npcl(FacialExpression.DRUNK, "(Dramatic pause)").also { stage++ }
                DRUNK_RAMBLING + 2 -> npcl(FacialExpression.DRUNK, "THEM!").also { stage++ }
                DRUNK_RAMBLING + 3 -> showTopics(
                    Topic("I'm sure I would as well.", CRAZY_PEOPLE),
                    Topic("Who are (Dramatic pause) THEY?", WHO_ARE_THEY, true)
                )

                CRAZY_PEOPLE -> playerl(FacialExpression.NEUTRAL, "I'm going over here to talk to non-crazy people now.").also { stage++ }
                CRAZY_PEOPLE + 1 -> npcl(FacialExpression.DRUNK, "Yeah? Yeah? Well when THEY come floppin' into your house and eat your furniture you'll be sorry!").also { stage = END_DIALOGUE }

                WHO_ARE_THEY -> playerl(FacialExpression.NEUTRAL, "Who are").also { stage++ }
                WHO_ARE_THEY + 1 -> playerl(FacialExpression.NEUTRAL, "(Dramatic pause)").also { stage++ }
                WHO_ARE_THEY + 2 -> playerl(FacialExpression.NEUTRAL, "THEY?").also { stage++ }
                WHO_ARE_THEY + 3 -> npcl(FacialExpression.DRUNK, "They! Those bloodthirsty, flesh-tearing devils! They are the reason I'm out here every day hurlin' bottles into the sea!").also { stage++ }
                WHO_ARE_THEY + 4 -> npcl(FacialExpression.DRUNK, "They are the reason I've lost everything, except the horrifying memory of what THEY look like...").also { stage++ }
                WHO_ARE_THEY + 5 -> playerl(FacialExpression.ASKING, "And what do THEY look like?").also { stage++ }
                WHO_ARE_THEY + 6 -> npcl(FacialExpression.DRUNK, "Mudskippers!").also { stage++ }
                WHO_ARE_THEY + 7 -> playerl(FacialExpression.ASKING, "Mudskippers?").also { stage++ }
                WHO_ARE_THEY + 8 -> npcl(FacialExpression.DRUNK, "Aye, Mudskippers! Those ferocious, ravening, evil, beady-eyed terrors of the deep!").also { stage++ }
                WHO_ARE_THEY + 9 -> playerl(FacialExpression.SUSPICIOUS, "I...see...").also{ stage++ }
                WHO_ARE_THEY + 10 -> npcl(FacialExpression.DRUNK, " I was ambushed by them way back, see. They got the drop on me...I can't remember where, somewhere around here though.").also { stage++ }
                WHO_ARE_THEY + 11 -> playerl(FacialExpression.ASKING, "These would be the mudskippers, right?").also{ stage++ }
                WHO_ARE_THEY + 12 -> npcl(FacialExpression.DRUNK, "Aye! The mudskippers! Huge they were! Ten feet of glistening, muddy flesh floppin' towards me with white foam flying from their gnashing fangs!").also { stage++ }
                WHO_ARE_THEY + 13 -> npcl(FacialExpression.DRUNK, "I fought them up and down the beach, with the tide rising and more of them leaping towards me with cutlasses drawn!").also { stage++ }
                WHO_ARE_THEY + 14 -> playerl(FacialExpression.HALF_GUILTY, "This is fascinating, but I have to be...").also{ stage++ }
                WHO_ARE_THEY + 15 -> npcl(FacialExpression.DRUNK, "Shut yer' word-hole and listen! I can't remember all the details, as I'm sure they must have hit me quite hard, but the last thing I remember before it all went black...").also { stage++ }
                WHO_ARE_THEY + 16 -> npcl(FacialExpression.DRUNK, "...was one of those devils rearing over me, its eyes glowin' red with the fires of hell!").also { stage++ }
                WHO_ARE_THEY + 17 -> playerl(FacialExpression.ROLLING_EYES, "Fires of hell...right. I believe you.").also { stage++ }
                WHO_ARE_THEY + 18 -> npcl(FacialExpression.DRUNK, "No you don't! You think I'm crazy, like all the rest! Well, if I'm crazy, how did I get these?").also { stage++ }
                WHO_ARE_THEY + 19 -> playerl(FacialExpression.ASKING, "Get what?").also { stage++ }
                WHO_ARE_THEY + 20 -> sendDialogue(player!!, "Skippy shows you what appears to be massive bite scars on his legs. You're no expert, but they look like... giant mudskipper bites!").also { stage++ }
                WHO_ARE_THEY + 21 -> playerl(FacialExpression.ASKING, "Giant mudskipper bites! Where did you get those?").also { stage++ }
                WHO_ARE_THEY + 22 -> npcl(FacialExpression.DRUNK, "I can't remember... I've been drinking to forget the horror, and all I seem to have forgotten is where it all happened...").also { stage++ }
                WHO_ARE_THEY + 23 -> playerl(FacialExpression.THINKING, "Hmmm...I suppose if I sober you up you may well start to recall.").also{ stage++ }
                WHO_ARE_THEY + 24 -> npcl(FacialExpression.DRUNK, "You'll have a job. I've been drinking this for a week.").also { stage++ }
                WHO_ARE_THEY + 25 -> playerl(FacialExpression.ASKING, "'Captain Braindeath's Extra Strength Rum/Drain Cleaner. Now 50% more debilitating'?").also { stage++ }
                WHO_ARE_THEY + 26 -> npcl(FacialExpression.DRUNK, "It's the extra sheep tranquilizers that gives it that added kick!").also { stage++ }
                WHO_ARE_THEY + 27 -> playerl(FacialExpression.NEUTRAL, "Stay here and I'll be right back. Try not to move. Or go near any open flames.").also { stage = END_DIALOGUE }
            }

            TENDER -> when(stage){
                0 -> playerl(FacialExpression.HAPPY, "Hey, Skippy!").also { stage++ }
                1 -> npcl(FacialExpression.PANICKED, "Gaah! Don't drench me again!").also { stage++ }
                2 -> playerl(FacialExpression.HALF_GUILTY, "Hey! I only did that once! Try not to be such a big baby!").also { stage++ }
                3 -> npcl(FacialExpression.HALF_THINKING, "So what are you here for then?").also { stage = if (hasAnItem(player!!, Items.NETTLE_TEA_4239).exists()) 4 else NO_TEA }
                4 -> playerl(FacialExpression.HAPPY, "I've come to give you your tea.").also { stage++ }
                5 -> npcl(FacialExpression.HAPPY, "Excellent! I was thinking I was going to freeze out here!").also { stage++ }
                6 -> sendItemDialogue(player!!, Items.NETTLE_TEA_4239, "Skippy drinks the tea and clutches his forehead in pain.").also {
                    stage++
                    removeItem(player!!, Items.NETTLE_TEA_4239)
                    setVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE, HUNGOVER, true)
                }
                7 -> npcl(FacialExpression.ANNOYED, "Ohhhhh...").also { stage++ }
                8 -> playerl(FacialExpression.ASKING, "What? What's wrong?").also { stage++ }
                9 -> npcl(FacialExpression.ANNOYED, "Not so loud...I think I have a hangover...").also { stage++ }
                10 -> playerl(FacialExpression.HALF_WORRIED, "Great...Well, I doubt you can remember anything through a hangover. What a waste of nettle tea...").also { stage++ }
                11 -> npcl(FacialExpression.FURIOUS, "Hey! A little sympathy here?").also { stage++ }
                12 -> npcl(FacialExpression.ANNOYED, "Owwowwoww... must remember not to shout...").also { stage++ }
                13 -> npcl(FacialExpression.ASKING, "Look, I do know a hangover cure. If you can get me a bucket of the stuff I think I'll be okay.").also { stage = if(getQuestStage(player!!, Quests.PLAGUE_CITY) >= 14 ) PLAGUE_CITY_RECIPE else LEARN_RECIPE }

                PLAGUE_CITY_RECIPE -> playerl(FacialExpression.HALF_THINKING, "Wait... is this cure a bucket of chocolate milk and snape grass?").also { stage++ }
                PLAGUE_CITY_RECIPE + 1 -> npcl(FacialExpression.HAPPY, "Yes! That's the stuff!").also { stage++ }
                PLAGUE_CITY_RECIPE + 2 -> playerl(FacialExpression.HALF_THINKING, "Ahhh. Yes, I've made some of that stuff before. I should be able to get you some, no problem.").also { stage = END_DIALOGUE }

                LEARN_RECIPE -> playerl(FacialExpression.ASKING, "So what is it you need?").also { stage++ }
                LEARN_RECIPE + 1 -> npcl(FacialExpression.NEUTRAL, "A bucket of milk with chocolate ground into it, with a handful of snape grass thrown in on top.").also { stage++ }
                LEARN_RECIPE + 2 -> playerl(FacialExpression.ASKING, "What? Run that past me again?").also { stage ++ }
                LEARN_RECIPE + 3 -> npcl(FacialExpression.NEUTRAL, "Take a bucket of milk, a bar of chocolate and some snape grass.").also { stage++ }
                LEARN_RECIPE + 4 -> npcl(FacialExpression.NEUTRAL, "Grind the chocolate with a mortar and pestle.").also { stage++ }
                LEARN_RECIPE + 5 -> npcl(FacialExpression.NEUTRAL, "Add the chocolate powder to the milk, then add the snape grass.").also { stage++ }
                LEARN_RECIPE + 6 -> npcl(FacialExpression.NEUTRAL, "Then bring it here and I will drink it.").also { stage++ }
                LEARN_RECIPE + 7 -> npcl(FacialExpression.NEUTRAL, "The end, and we all live happily ever after. Got it?").also { stage = END_DIALOGUE }

                NO_TEA -> playerl(FacialExpression.HALF_GUILTY, "No real reason. I just thought I would check up on you is all.").also { stage++ }
                NO_TEA + 1 -> npcl(FacialExpression.ANGRY, "Well, I'm still wet, still cold and still waiting on that nettle tea.").also { stage = END_DIALOGUE }
            }
            HUNGOVER -> when(stage){
                0 -> playerl(FacialExpression.HAPPY, "Hey, Skippy!").also{ stage++ }
                1 -> npcl(FacialExpression.ANNOYED, "Egad! Don't you know not to shout around a guy with a hangover?").also { stage++ }
                2 -> npcl(FacialExpression.HALF_GUILTY, "Ahhhhhg...No more shouting for me...").also { stage++ }
                3 -> npcl(FacialExpression.ASKING, "What is it anyway?").also { stage = if (hasAnItem(player!!, Items.HANGOVER_CURE_1504).exists()) CURE else NO_CURE }

                CURE -> playerl(FacialExpression.HAPPY, "Well Skippy, you will no doubt be glad to hear that I got you your hangover cure!").also { stage++ }
                CURE + 1 ->npcl(FacialExpression.HAPPY, "Gimme!").also { stage++ }
                CURE + 2 -> sendDialogue(player!!, "Skippy chugs the hangover cure... very impressive.").also {
                    removeItem(player!!, Items.HANGOVER_CURE_1504)
                    stage++
                    setVarbit(player!!, 1344, DONE_QUEST, true)
                }
                CURE + 3 -> npcl(FacialExpression.HAPPY, "Ahhhhhhhhhhhhhhh...").also { stage ++ }
                CURE + 4 -> npcl(FacialExpression.HAPPY, "Much better...").also { stage ++ }
                CURE + 5 -> playerl(FacialExpression.ASKING, "Feeling better?").also { stage ++ }
                CURE + 6 -> npcl(FacialExpression.HAPPY, "Considerably.").also { stage ++ }
                CURE + 7 -> playerl(FacialExpression.ASKING, "Then tell me where the mudskippers are!").also { stage++ }
                CURE + 8 -> npcl(FacialExpression.NEUTRAL, "I wish you wouldn't go looking for them. Those vicious killers will tear you apart.").also { stage ++ }
                CURE + 9 -> npcl(FacialExpression.HALF_THINKING, "It's all becoming clear to me now...").also { stage ++ }
                CURE + 10 -> npcl(FacialExpression.HAPPY, "I was fishing using a Fishing Explosive...").also { stage ++ }
                CURE + 11 -> playerl(FacialExpression.ASKING, "A Fishing Explosive?").also { stage++ }
                CURE + 12 -> npcl(FacialExpression.NEUTRAL, "Well, Slayer Masters sell these highly volatile potions for killing underwater creatures.").also { stage ++ }
                CURE + 13 -> npcl(FacialExpression.NEUTRAL, "If you don't feel like lobbing a net about all day you can use them to fish with...").also { stage ++ }
                CURE + 14 -> npcl(FacialExpression.NEUTRAL, "But this time I was startled by what I thought was a giant mudskipper.").also { stage ++ }
                CURE + 15 -> npcl(FacialExpression.NEUTRAL, "What it was, in fact, was a...").also { stage ++ }
                CURE + 16 -> npcl(FacialExpression.NEUTRAL, "Dramatic Pause...").also { stage ++ }
                CURE + 17 -> npcl(FacialExpression.PANICKED, "A Mogre!").also { stage ++ }
                CURE + 18 -> playerl(FacialExpression.ASKING, "What exactly is a Mogre?").also { stage++ }
                CURE + 19 -> npcl(FacialExpression.NEUTRAL, "A Mogre is a type of Ogre that spends most of its time underwater.").also { stage ++ }
                CURE + 20 -> npcl(FacialExpression.NEUTRAL, "They hunt giant mudskippers by wearing their skins and swimming close until they can attack them.").also { stage ++ }
                CURE + 21 -> npcl(FacialExpression.NEUTRAL, "When I used the Fishing Explosive I scared off all the fish, and so the Mogre got out of the water to express its extreme displeasure.").also { stage ++ }
                CURE + 22 -> npcl(FacialExpression.NEUTRAL, "With an iron mace.").also { stage ++ }
                CURE + 23 -> playerl(FacialExpression.ASKING, "I take it the head injury is responsible for the staggering and yelling?").also { stage++ }
                CURE + 24 -> npcl(FacialExpression.NEUTRAL, "No, no.").also { stage ++ }
                CURE + 25 -> npcl(FacialExpression.NEUTRAL, "My addiction to almost-lethal alcohol did that.").also { stage ++ }
                CURE + 26 -> npcl(FacialExpression.NEUTRAL, "But if you are set on finding these Mogres just head south from here until you find Mudskipper Point.").also { stage ++ }
                CURE + 27 -> playerl(FacialExpression.ASKING, "The mudskipper-eating monsters are to be found at Mudskipper point?").also { stage++ }
                CURE + 28 -> playerl(FacialExpression.ROLLING_EYES, "Shock!").also { stage++ }
                CURE + 29 -> playerl(FacialExpression.NEUTRAL, "Thanks. I'll be careful.").also { stage = END_DIALOGUE }

                NO_CURE -> playerl(FacialExpression.HALF_GUILTY, "I just came back to ask you something.").also { stage++ }
                NO_CURE + 1 -> npcl(FacialExpression.ANNOYED, "What?").also { stage++ }
                NO_CURE + 2 -> showTopics(
                    Topic("How do I make that hangover cure again?", RECIPE),
                    Topic("Why do they call you 'Skippy'?", NAME_ORIGIN)
                )

                RECIPE -> npcl(FacialExpression.ANGRY, "Give me strength...Here's what you do. Pay attention!").also { stage++ }
                RECIPE + 1 -> npcl(FacialExpression.ANGRY, "You take a bucket of milk, a bar of chocolate and some snape grass.").also { stage++ }
                RECIPE + 2 -> npcl(FacialExpression.ANGRY, "Grind the chocolate with a pestle and mortar.").also { stage++ }
                RECIPE + 3 -> npcl(FacialExpression.ANGRY, "Add the chocolate powder to the milk, then add the snape grass.").also { stage++ }
                RECIPE + 4 -> npcl(FacialExpression.ANGRY, "Then bring it here and I will drink it.").also { stage++ }
                RECIPE + 5 -> npcl(FacialExpression.ANGRY, "Are you likely to remember that or should I go get some crayons and draw you a picture?").also { stage++ }
                RECIPE + 6 -> playerl(FacialExpression.ANGRY, "Hey! I remember it now, ok! See you in a bit.").also { stage = END_DIALOGUE }

                NAME_ORIGIN -> npcl(FacialExpression.THINKING, "I think it may have something to do with my near- constant raving about mudskippers.").also { stage++ }
                NAME_ORIGIN + 1 -> npcl(FacialExpression.THINKING, "That or it's something to do with that time with the dress and the field full of daisies...").also { stage = END_DIALOGUE }
            }
            DONE_QUEST -> when(stage){
                0 -> playerl(FacialExpression.NEUTRAL, "Hey, Skippy.").also { stage++ }
                1 -> npcl(FacialExpression.HAPPY, "Hey you!").also { stage++ }
                2 -> playerl(FacialExpression.ASKING, "How do I annoy the Mogres again?").also { stage++ }
                3 -> npcl(FacialExpression.HAPPY, "Go south to Mudskipper Point and lob a Fishing Explosive into the sea. You can grab them from the Slayer masters.").also { stage++ }
                4 -> playerl(FacialExpression.ASKING, "Thanks! So, what are you going to do now?").also { stage++ }
                5 -> npcl(FacialExpression.HAPPY, "Well, I was planning on continuing my hobby of wandering up and down this bit of coastline, bellowing random threats and throwing bottles.").also { stage++ }
                6 -> npcl(FacialExpression.ASKING, "And you?").also { stage++ }
                7 -> playerl(FacialExpression.NEUTRAL, "I was planning on wandering up and down the landscape, bugging people to see if they had mindblowingly dangerous quests for me to undertake.").also { stage++ }
                8 -> npcl(FacialExpression.HAPPY, "Well, good luck with that!").also { stage++ }
                9 -> playerl(FacialExpression.HAPPY, "You too!").also { stage++ }
                10 -> npcl(FacialExpression.ROLLING_EYES, "Weirdo...").also { stage++ }
                11 -> playerl(FacialExpression.ROLLING_EYES, "Loony..").also { stage = END_DIALOGUE }
            }
        }
    }
}

class SkippyBootDialogue: DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SKIPPY_2796)
        val expression = if (getVarbit(player!!, Vars.VARBIT_MINI_QUEST_MOGRE) > SkippyBucketDialogue.DRUNK) FacialExpression.ROLLING_EYES else FacialExpression.DRUNK
        npcl(expression, "Thanks! Now I have two right boots!").also { stage = END_DIALOGUE }
    }

}

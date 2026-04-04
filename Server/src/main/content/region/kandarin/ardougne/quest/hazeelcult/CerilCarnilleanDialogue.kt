package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.carnilleanArc
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.fakeFinish
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class CerilCarnilleanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)
        when {
            // stage 0 - unstarted
            (questStage == 0) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.ANGRY, "Blooming, thieving, weirdo cultists! Why don't they leave me alone? String 'em all up, that's what I say!").also { stage++ }
                    1 -> options("What's wrong?", "You probably deserve it.", "You seem uptight, I'll leave you alone.").also { stage++ }
                    2 -> when (buttonID) {
                        1 -> playerl(FacialExpression.HALF_ASKING, "What's wrong?").also { stage++ }
                        2 -> playerl(FacialExpression.FRIENDLY, "You probably deserve it.").also { stage = 20 }
                        3 -> playerl(FacialExpression.FRIENDLY, "You seem uptight, I'll leave you alone.").also { stage = 21 }
                    }
                    3 -> npcl(FacialExpression.ANGRY, "It's those blooming cultists from the forest! Those freaks keep breaking into my house!").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "Have they taken much?").also { stage++ }
                    5 -> npcl(FacialExpression.ANGRY, "They first broke in months ago and stole a suit of armour. The strange thing is that they've broken in four times since but taken nothing.").also { stage++ }
                    6 -> playerl(FacialExpression.FRIENDLY, "And you are...?").also { stage++ }
                    7 -> npcl(FacialExpression.ANGRY, "Why, I am Sir Ceril Carnillean! We really are quite a famous bloodline... who've played a vital role in the politics of Ardougne for many generations.").also { stage++ }
                    8 -> npcl(FacialExpression.FRIENDLY, "Perhaps you would be able to assist me in returning the stolen armour? For a modest cash reward of course!").also { stage++ }
                    9 -> options("Yes, of course, I'd be happy to help.", "No thanks. I've got other plans.").also { stage++ }
                    10 -> when (buttonID) {
                        1 -> playerl(FacialExpression.FRIENDLY, "Yes, of course, I'd be happy to help.").also { stage++ }
                        2 -> playerl(FacialExpression.FRIENDLY, "No thanks. I've got other plans.").also { stage = 19 }
                    }
                    11 -> npcl(FacialExpression.FRIENDLY, "That's very noble of you sirrah! I caught a glimpse of the thieves leaving but due to... uh... yeah, my cold I was unable to give chase.").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "They're some kind of crazy cult who dress all in black, and hang out near the cave entrance in the forest south of here.").also { stage++ }
                    13 -> playerl(FacialExpression.FRIENDLY, "How do you know that?").also { stage++ }
                    14 -> npcl(FacialExpression.FRIENDLY, "My old butler, Higson, once followed them to their hideout there. Unfortunately the next night he died in his sleep.").also { stage++ }
                    15 -> playerl(FacialExpression.FRIENDLY, "That's awful!").also { stage++ }
                    16 -> npcl(FacialExpression.FRIENDLY, "No, it's okay. A replacement arrived the next day. He's been great - cooks an excellent broth!").also { stage++ }

                    17 -> playerl(FacialExpression.FRIENDLY, "Ok. I'll see what I can do.").also { stage++ }
                    18 -> {
                        end()
                        setQuestStage(player, Quests.HAZEEL_CULT, 1)
                    }
                    19 -> npcl(FacialExpression.FRIENDLY, "Well no wonder I'm the one with the big house and you're the one on the streets.").also { stage = END_DIALOGUE }
                    20 -> npcl(FacialExpression.FRIENDLY, "And who are you to judge me? You look like a peasant anyway. I'm wasting my time talking to you.").also { stage = END_DIALOGUE }
                    21 -> npcl(FacialExpression.FRIENDLY, "Yes, I doubt you could help anyway.").also { stage = END_DIALOGUE }
                }
            }

            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello again.").also { stage++ }
                1 -> npcl(FacialExpression.ANNOYED, "Oh the inhumanity... the cruelty... the misery... the pain...").also { stage++ }
                2 -> npcl(FacialExpression.ANNOYED, "My son is a good boy, really, but how could he give his dinner to Scruffy without having the servants test it for poison first? How?").also { stage++ }
                3 -> npcl(FacialExpression.ANNOYED, "How could he be so thoughtless and careless? He knows we are all under threat!").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Scruffy?").also { stage++ }
                5 -> npcl(FacialExpression.ANNOYED, "He's been with our family for twenty years... that's 140 in dog years! The poor dog... What did he ever do to deserve such a fate?").also { stage++ }
                6 -> playerl(FacialExpression.FRIENDLY, "Your dog got poisoned? That's not right.").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "I agree! I hope whichever evildoer is responsible gets the full weight of the law brought upon them!").also { stage++ }
                8 -> playerl(FacialExpression.NEUTRAL, "Uh... yeah... me too.").also { stage = END_DIALOGUE }
            }

            // stage 4 - alomone either fought or he tells you he needs scroll
            (questStage == 4 && carnilleanArc(player) && inInventory(player, Items.CARNILLEAN_ARMOUR_2405)) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Look! I've recovered your armour!").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Well done! I must say I am very impressed! Come on, hand it over.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Before we send you on your way with your payment, I'll just get Jones to whip you up a batch of his special broth.").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "I'd rather not if it's all the same to you. I overheard the cultists talking, and apparently Jones is in league with them.").also { stage++ }
                4 -> {
                    // check if we are upstairs
                    if(player.location.z == 1) {
                        npcl(FacialExpression.ANGRY, "W-what? RIGHT! You! We're going to blooming well sort this out right now once and for all!").also { stage++ }
                    } else {
                        npcl(FacialExpression.ANGRY, "W-what? RIGHT! You! Come with me upstairs! We're going to blooming-well sort this out, once and for all!").also { stage = END_DIALOGUE }
                    }
                }

                5 -> npcl(FacialExpression.HALF_ASKING, "Jones! This commoner says you had something to do with the theft of my armour. What do you have to say for yourself about that?").also { stage++ }
                6 -> sendNPCDialogue(player, NPCs.BUTLER_JONES_890, "It wasn't me m'lud. I am, as you know, a loyal servant.").also { stage++ }
                7 -> npcl(FacialExpression.FRIENDLY, "Humph. Quite right too. I cannot fathom why this scoundrel would accuse you of such a crime without evidence to back up @g[his,her] accusations.").also { stage++ }
                8 -> npcl(FacialExpression.FRIENDLY, "Right, I have decided. I have given my word as a nobleman to reward you for your efforts in retrieving my armour.").also { stage++ }
                // it is authentic that the previous line ends in a period and the next line starts with a non-capital 'but'
                9 -> npcl(FacialExpression.FRIENDLY, "but I must also compensate Jones for this terrible slander you have made against him.").also { stage++ }
                10 -> {
                    sendDialogue(player, "Sir Ceril gives you 5 gold. Sir Ceril gives Jones 1995 gold.")
                    addItemOrDrop(player, Items.COINS_995, 5)
                    stage++
                }
                11 -> npcl(FacialExpression.ANGRY, "Now take it and leave you scoundrel! And don't darken my doorstep again!").also { stage++ }
                12 -> sendNPCDialogue(player, NPCs.BUTLER_JONES_890, "Don't worry m'lud, this fool won't be bothering us any more.").also { stage++ }
                13 -> sendDialogue(player, "Jones smirks at you. You are going to need more than words to prove Jones' treachery.").also { stage++ }
                14 -> {
                    end()
                    removeItem(player, Items.CARNILLEAN_ARMOUR_2405)
                    setQuestStage(player, Quests.HAZEEL_CULT, 5)
                    fakeFinish(player)
                }

            }

            // stage 5 - either returning the armour, or finding the scroll
            (questStage == 5 && carnilleanArc(player)) -> when (stage) {
                // The ceril-jones-player dialogue is continued in the listener for the wardrobe where the player finds the evidence that Jones is bad.
                0 -> npcl(FacialExpression.ANGRY, "Leave, you scoundrel! And don't darken my doorstep again!").also { stage = END_DIALOGUE }
            }

            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // also covers stage 4 and 5 if you're playing the mahjarrat side
            (questStage in 1..99) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, Ceril.").also { stage++ }
                1 -> npcl(FacialExpression.ANNOYED, "That's Sir Ceril to you, you impudent scamp. Show a bit of respect to your betters. Now, shouldn't you be recovering my armour?").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Yeah yeah, I'm on it.").also { stage++ }
                3 -> npcl(FacialExpression.ANNOYED, "Good. I suggest you start with that cave south of the city, near the Clock Tower. That is where the cult is based.").also { stage = END_DIALOGUE }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, Ceril.").also { stage++ }
                1 -> {
                    if (mahjarratArc(player)) {
                        npcl(FacialExpression.FRIENDLY, "Oh... I may be wrong... but ever since I asked for your help, things around here have gone from bad to worse...").also { stage = 3 }
                    } else {
                        npcl(FacialExpression.FRIENDLY, "Well hello again adventurer! It's good to see you again! If it wasn't for your quick thinking the treacherous Jones would have poisoned my family and me by now! We are in your debt.").also { stage = 2 }
                    }
                }
                2 -> playerl(FacialExpression.FRIENDLY, "Don't worry about it, a good deed is its own reward. And that 2000 gold didn't hurt either.").also { stage = END_DIALOGUE }

                3 -> npcl(FacialExpression.NEUTRAL, "I think you'd better keep out of my way. And for the last time, it's Sir Ceril! Sir! It's not that hard to blooming remember!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CERIL_CARNILLEAN_885)
}

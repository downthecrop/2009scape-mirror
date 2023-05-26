package content.region.misthalin.lumbridge.quest.sheepshearer

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

class SSFredTheFarmerDialogue(val questStage: Int) : DialogueFile() {
    companion object {
        const val STAGE_BEGIN_QUEST = 1000
        const val STAGE_PENGUIN_SHEEP_SHEARED = 3000
        const val STAGE_CANT_SPIN_WOOL = 20000
        const val STAGE_CAN_SPIN_WOOL = 20100
        const val STAGE_DELIVER_BALLS_OF_WOOL = 30000
        const val STAGE_FINISH_QUEST = 30301
    }

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> if (questStage == 10) {
                if (getAttribute(player!!, SheepShearer.ATTR_IS_PENGUIN_SHEEP_SHEARED, false)) {
                    npc(FacialExpression.ANGRY, "What are you doing on my land?").also { stage = STAGE_PENGUIN_SHEEP_SHEARED }
                } else {
                    npc(FacialExpression.NEUTRAL, "How are you doing getting those balls of wool?").also { stage = STAGE_DELIVER_BALLS_OF_WOOL }
                }
            } else if (questStage == 90) {
                // Player delivered all balls of wool but exited the dialogue before the quest was recorded as complete
                npc(FacialExpression.SAD, "I guess I'd better pay you then.").also { stage = STAGE_FINISH_QUEST }
            } else {
                npc(FacialExpression.NEUTRAL, "You're after a quest, you say? Actually I could do with", "a bit of help.").also { stage = STAGE_BEGIN_QUEST }
            }

            STAGE_BEGIN_QUEST -> npc(FacialExpression.NEUTRAL, "My sheep are getting mighty woolly. I'd be much", "obliged if you could shear them. And while you're at it", "spin the wool for me too.").also { stage++ }
            1001 -> npc(FacialExpression.HAPPY, "Yes, that's it. Bring me 20 balls of wool. And I'm sure", "I could sort out some sort of payment. Of course,", "there's the small matter of The Thing.").also { stage++ }
            1002 -> options("Yes okay. I can do that.", "That doesn't sound a very exciting quest.", "What do you mean, The Thing?").also { stage++ }
            1003 -> when (buttonID) {
                1 -> player(FacialExpression.HAPPY, "Yes okay. I can do that.").also { stage = 2000 }
                2 -> player(FacialExpression.HALF_GUILTY, "That doesn't sound a very exciting quest.").also { stage = 1100 }
                3 -> player(FacialExpression.ASKING, "What do you mean, The Thing?").also { stage = 1200 }
            }

            // TODO: There might be more dialogue here
            1100 -> npc(FacialExpression.HALF_GUILTY, "Well what do you expect if you ask a farmer for a", "quest?").also { stage = END_DIALOGUE }

            1200 -> npc(FacialExpression.SUSPICIOUS, "Well now, no one has ever seen The Thing.  That's", "why we call it The Thing, 'cos we don't know what it is.").also { stage++ }
            1201 -> npc(FacialExpression.SCARED, "Some say it's a black hearted shapeshifter, hungering for", "the souls of hard working decent folk like me.  Others", "say it's just a sheep.").also { stage++ }
            1202 -> npc(FacialExpression.ANGRY, "Well I don't have all day to stand around and gossip.", "Are you going to shear my sheep or what!").also { stage++ }
            1203 -> options("Yes okay. I can do that.", "Erm I'm a bit worried about this Thing.").also { stage++ }
            1204 -> when (buttonID) {
                1 -> player(FacialExpression.HAPPY, "Yes okay. I can do that.").also { stage = 2000 }
                2 -> player(FacialExpression.HALF_GUILTY, "Erm I'm a bit worried about this Thing.").also { stage = 1300 }
            }

            1300 -> npc(FacialExpression.HALF_GUILTY, "I'm sure it's nothing to worry about. Just because", "my last shearer was seen bolting out of the field", "screaming for his life doesn't mean anything.").also { stage++ }
            1301 -> player(FacialExpression.HALF_GUILTY, "I'm not convinced.").also { stage = END_DIALOGUE }

            2000 -> {
                // NOTE: In a July 2009 video, this only happens when the dialogue ends
                startQuest(player!!, "Sheep Shearer")
                npc(FacialExpression.NEUTRAL, "Good! Now one more thing, do you actually know how", "to shear a sheep?").also { stage++ }
            }
            2001 -> options("Of course!", "Err. No, I don't know actually.").also { stage++ }
            2002 -> when (buttonID) {
                1 -> player(FacialExpression.HAPPY, "Of course!").also { stage = 2100 }
                2 -> player(FacialExpression.NEUTRAL, "Err. No, I don't know actually.").also { stage = 2200 }
            }

            2100 -> npc(FacialExpression.NEUTRAL, "And you know how to spin wool into balls?").also { stage++ }
            2101 -> options("I'm something of an expert actually!", "I don't know how to spin wool, sorry.").also { stage++ }
            2102 -> when (buttonID) {
                1 -> player(FacialExpression.HAPPY, "I'm something of an expert actually!").also { stage = STAGE_CAN_SPIN_WOOL }
                2 -> player(FacialExpression.NEUTRAL, "I don't know how to spin wool, sorry.").also { stage = STAGE_CANT_SPIN_WOOL }
            }

            2200 -> {
                if (inInventory(player!!, Items.SHEARS_1735)) {
                    npc(FacialExpression.HAPPY, "Well, you're halfway there already! You have a set of", "shears in your inventory. Just use those on a Sheep to", "shear it.").also { stage = 2300 }
                } else {
                    npc(FacialExpression.NEUTRAL, "Well, first things first, you need a pair of shears, there's", "a pair in the house on the table.").also { stage = 2400 }
                }
            }

            2300 -> player(FacialExpression.NEUTRAL, "That's all I have to do?").also { stage++ }
            2301 -> npc(FacialExpression.NEUTRAL, "Well once you've collected some wool you'll need to spin", "it into balls.").also { stage++ }
            2302 -> npc(FacialExpression.ASKING, "Do you know how to spin wool?").also { stage++ }
            2303 -> options("I don't know how to spin wool, sorry.", "I'm something of an expert actually!").also { stage++ }
            2304 -> when (buttonID) {
                1 -> player(FacialExpression.NEUTRAL, "I don't know how to spin wool, sorry.").also { stage = STAGE_CANT_SPIN_WOOL }
                2 -> player(FacialExpression.HAPPY, "I'm something of an expert actually!").also { stage = STAGE_CAN_SPIN_WOOL }
            }

            2400 -> npc(FacialExpression.NEUTRAL, "Or you could buy your own pair from the General", "Store in Lumbridge.").also { stage++ }
            2401 -> npc(FacialExpression.NEUTRAL, "To get to Lumbridge travel east on the road outside.").also { stage++ }
            // TODO: Add "General Stores are marked on the map by this symbol." message with the general store map icon here
            2402 -> npc(FacialExpression.NEUTRAL, "Once you get some shears use them on the sheep in", "my field.").also { stage++ }
            2403 -> player(FacialExpression.HAPPY, "Sounds easy!").also { stage++ }
            2404 -> npc(FacialExpression.LAUGH, "That's what they all say!").also { stage++ }
            2405 -> npc(FacialExpression.NEUTRAL, "Some of the sheep don't like it and will run away from", "you.  Persistence is the key.").also { stage++ }
            2406 -> npc(FacialExpression.NEUTRAL, "Once you've collected some wool you can spin it into", "balls.").also { stage++ }
            2407 -> npc(FacialExpression.NEUTRAL, "Do you know how to spin wool?").also { stage++ }
            2408 -> options("I don't know how to spin wool, sorry.", "I'm something of an expert actually!").also { stage++ }
            2409 -> when (buttonID) {
                1 -> player(FacialExpression.NEUTRAL, "I don't know how to spin wool, sorry.").also { stage = STAGE_CANT_SPIN_WOOL }
                2 -> player(FacialExpression.HAPPY, "I'm something of an expert actually!").also { stage = STAGE_CAN_SPIN_WOOL }
            }

            STAGE_PENGUIN_SHEEP_SHEARED -> options("I'm back!", "Fred! Fred! I've seen The Thing!").also { stage++ }
            3001 -> when (buttonID) {
                1 -> player(FacialExpression.HAPPY, "I'm back!").also { stage = 3100 }
                2 -> player(FacialExpression.AMAZED, "Fred! Fred! I've seen The Thing!").also { stage = 3200 }
            }

            3100 -> npc(FacialExpression.NEUTRAL, "How are you doing getting those balls of wool?").also { stage = STAGE_DELIVER_BALLS_OF_WOOL }

            3200 -> npc(FacialExpression.SCARED, "You ... you actually saw it?").also { stage++ }
            3201 -> npc(FacialExpression.SCARED, "Run for the hills! ${player!!.username} grab as many chickens as", "you can!  We have to ...").also { stage++ }
            3202 -> player(FacialExpression.AMAZED, "Fred!").also { stage++ }
            3203 -> npc(FacialExpression.SCARED, "... flee! Oh, woe is me! The shapeshifter is coming!", "We're all ...").also { stage++ }
            3204 -> player(FacialExpression.ANGRY, "FRED!").also { stage++ }
            3205 -> npc(FacialExpression.HALF_CRYING, "... doomed. What!").also { stage++ }
            3206 -> player(FacialExpression.NEUTRAL, "It's not a shapeshifter or any other kind of monster!").also { stage++ }
            3207 -> npc(FacialExpression.ASKING, "Well then what is it boy?").also { stage++ }
            3208 -> player(FacialExpression.THINKING, "Well ... it's just two Penguins; Penguins disguised as a", "sheep.").also { stage++ }
            3209 -> npc(FacialExpression.THINKING, "...").also { stage++ }
            3210 -> npc(FacialExpression.AMAZED, "Have you been out in the sun too long?").also { stage = END_DIALOGUE }

            // Common dialogue - doesn't know how to spin wool
            STAGE_CANT_SPIN_WOOL -> npc(FacialExpression.NEUTRAL, "Don't worry, it's quite simple!").also { stage++ }
            20001 -> npc(FacialExpression.NEUTRAL, "The nearest Spinning Wheel can be found on the first", "floor of Lumbridge Castle.").also { stage++ }
            20002 -> npc(FacialExpression.NEUTRAL, "To get to Lumbridge Castle just follow the road east.").also { stage++ }
            // TODO: Add "This icon denotes a Spinning Wheel on the world map." message with the spinning wheel map icon here
            20003 -> player(FacialExpression.HAPPY, "Thank you!").also { stage = END_DIALOGUE }

            // Common dialogue - knows how to spin wool
            STAGE_CAN_SPIN_WOOL -> npc(FacialExpression.NEUTRAL, "Well you can stop grinning and get to work then.").also { stage++ }
            20101 -> npc(FacialExpression.ANGRY, "I'm not paying you by the hour!").also { stage = END_DIALOGUE }

            // Common dialogue - deliver balls of wool
            STAGE_DELIVER_BALLS_OF_WOOL -> {
                if (inInventory(player!!, Items.BALL_OF_WOOL_1759)) {
                    player(FacialExpression.HAPPY, "I have some.").also { stage = 30100 }
                } else {
                    player(FacialExpression.ASKING, "How many more do I need to give you?").also { stage = 31000 }
                }
            }

            30100 -> npc(FacialExpression.NEUTRAL, "Give 'em here then.").also { stage++ }
            30101 -> {
                val ballsOfWoolDelivered = SheepShearer.deliverBallsOfWool(player!!)
                if (SheepShearer.getBallsOfWoolRequired(player!!) == 0) {
                    setQuestStage(player!!, "Sheep Shearer", 90)
                    player(FacialExpression.HAPPY, "That's the last of them.").also { stage = 30300 }
                } else {
                    sendDialogue(player!!, "You give Fred $ballsOfWoolDelivered balls of wool").also { stage = 30200 }
                }
            }

            30200 -> player(FacialExpression.NEUTRAL, "That's all I've got so far.").also { stage++ }
            30201 -> npc(FacialExpression.NEUTRAL, "I need ${SheepShearer.getBallsOfWoolRequired(player!!)} more before I can pay you.").also { stage++ }
            30202 -> player(FacialExpression.NEUTRAL, "Ok I'll work on it.").also { stage = END_DIALOGUE }

            30300 -> npc(FacialExpression.SAD, "I guess I'd better pay you then.").also { stage++ }
            STAGE_FINISH_QUEST -> finishQuest(player!!, "Sheep Shearer").also { stage = END_DIALOGUE }

            31000 -> npc(FacialExpression.NEUTRAL, "You need to collect ${SheepShearer.getBallsOfWoolRequired(player!!)} more balls of wool.").also { stage++ }
            31001 -> {
                if (inInventory(player!!, Items.WOOL_1737)) {
                    player(FacialExpression.NEUTRAL, "Well I've got some wool. I've not managed to make it", "into a ball though.").also { stage = 31100 }
                } else {
                    player(FacialExpression.HALF_GUILTY, "I haven't got any at the moment.").also { stage = 31200 }
                }
            }

            31100 -> npc(FacialExpression.NEUTRAL, "Well go find a spinning wheel then. You can find one", "on the first floor of Lumbridge Castle, just walk east on", "the road outside my house and you'll find Lumbridge.").also { stage = END_DIALOGUE }

            // TODO: There might be more dialogue here
            31200 -> npc(FacialExpression.HALF_GUILTY, "Ah well at least you haven't been eaten.").also { stage = END_DIALOGUE }
        }
    }
}
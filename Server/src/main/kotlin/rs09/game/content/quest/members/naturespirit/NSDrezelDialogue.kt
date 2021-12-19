package rs09.game.content.quest.members.naturespirit

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.stringtools.colorize

class NSDrezelDialogue : DialogueFile() {
    var questStage = 0
    override fun handle(componentID: Int, buttonID: Int) {
        questStage = player!!.questRepository.getStage("Nature Spirit")

        if(questStage <= 5){
            when(stage){
                0 -> options("Sorry, not interested...", "Well, what is it, I may be able to help?").also { stage++ }
                1 -> when(buttonID){
                    1 -> playerl(FacialExpression.NEUTRAL, "Sorry, not interested.").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.FRIENDLY, "Well, what is it, I may be able to help?").also { stage++ }
                }

                2 -> npcl(FacialExpression.HALF_THINKING, "There's a man called Filliman who lives in Mort Myre, I wonder if you could look for him? The swamps of Mort Myre are dangerous though, they're infested with Ghasts!").also { stage++ }
                3 -> options("Who is this Filliman?", "Where's Mort Myre?", "What's a Ghast?", "Yes, I'll go and look for him.", "Sorry, I don't think I can help.").also { stage++ }
                4 -> when(buttonID){
                    1 -> npcl(FacialExpression.NEUTRAL, "Filliman Tarlock is his full name and he's a Druid. He lives in Mort Myre much like a hermit, but there's many a traveller who he's helped.").also { stage-- }
                    2 -> npcl(FacialExpression.NEUTRAL, "Mort Myre is a decayed and dangerous swamp to the south. It was once a beautiful forest but has since become filled with vile emanations from within Morytania.").also { stage = 6 }
                    3 -> npcl(FacialExpression.NEUTRAL, "A Ghast is a poor soul who died in Mort Myre. They're undead of a special class, they're untouchable as far as I'm aware!").also { stage = 5 }
                    4 -> playerl(FacialExpression.FRIENDLY, "Yes, I'll go and look for him.").also { stage = 10 }
                    5 -> playerl(FacialExpression.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                }
                5 -> npcl(FacialExpression.NEUTRAL, "Filliman knew how to tackle them, but I've not heard from him in a long time. Ghasts, when they attack, will devour any food you have. If you have no food, they'll draw their nourishment from you!").also { stage = 3 }
                6 -> npcl(FacialExpression.NEUTRAL, " We put a fence around it to stop unwary travellers going in. Anyone who dies in the swamp is forever cursed to haunt it as a Ghast. Ghasts attack travellers, turning food to rotten filth.").also { stage = 3 }

                10 -> npcl(FacialExpression.NEUTRAL, "That's great, but it is very dangerous. Are you sure you want to do this?").also { stage++ }
                11 -> options("Yes, I'm sure.", "Sorry, I don't think I can help.").also { stage++ }
                12 -> when(buttonID){
                    1 -> playerl(FacialExpression.FRIENDLY, "Yes, I'm sure.").also { stage = 20 }
                    2 -> playerl(FacialExpression.NEUTRAL, "Sorry, I don't think I can help.").also { stage = END_DIALOGUE }
                }

                20 -> npcl(FacialExpression.NEUTRAL, "That's great! Many Thanks! Now then, please be aware of the Ghasts, you cannot attack them, only Filliman knew how to take them on.").also { stage++ }
                21 -> npcl(FacialExpression.NEUTRAL, "Just run from them if you can. If you start to get lost, try to make your way back to the temple.").also { stage++ }
                22 -> {
                    sendDoubleItemDialogue(player!!, Items.MEAT_PIE_2327, Items.APPLE_PIE_2323, "The cleric hands you some food.")
                    if(questStage == 0){
                        repeat(3) { addItemOrDrop(player!!, Items.MEAT_PIE_2327, 1) }
                        repeat(3) { addItemOrDrop(player!!, Items.APPLE_PIE_2323, 1) }
                        player!!.questRepository.getQuest("Nature Spirit").setStage(player!!, 5)
                    }
                    stage++
                }
                23 -> npcl(FacialExpression.NEUTRAL, "Please take this food to Filliman, he'll probably appreciate a bit of cooked food. Now, he's never revealed where he lives in the swamps but I guess he'd be to the south, search for him won't you?").also { stage++ }
                24 -> playerl(FacialExpression.FRIENDLY, "I'll do my very best, don't worry, if he's in there and he's still alive I'll definitely find him.").also { stage = END_DIALOGUE; player!!.questRepository.getQuest("Nature Spirit").start(player!!) }
            }
        }

        else if(questStage == 15) {
            when(stage){
                0 -> playerl(FacialExpression.HALF_GUILTY, "I've found Filliman and you should prepare for some sad news.").also { stage++ }
                1 -> npcl(FacialExpression.HALF_GUILTY, "You mean... he's dead?").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "Well, er sort of. I got to his camp and I encountered a spirit of some kind. I don't think it was a Ghast, it tried to communicate with me, but made no sense, it was all 'ooooh' this and 'oooh' that.").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Hmmm, that's very interesting, I seem to remember Father Aereck in Lumbridge and his predecessor Father Urhney having a similar issue. Though this is probably not related to your problem.").also { stage++ }
                4 -> npcl(FacialExpression.NEUTRAL, " I will pray that it wasn't the spirit of my friend Filliman, but some lost soul who needs some help. Please do let me know how you get on with it.").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage == 35){
            when(stage){
                0 -> playerl(FacialExpression.FRIENDLY, "Hello again! I'm helping Filliman, he plans to become a nature spirit. I have a spell to cast but first I need to be blessed. Can you bless me?").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "But you haven't sneezed!").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "You're so funny! But can you bless me?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Very well my friend, prepare yourself for the blessings of Saradomin. Here we go!").also { stage++ }
                4 -> {
                    end()
                    player!!.lock();
                    submitIndividualPulse(player!!, BlessingPulse(npc!!, player!!))
                }
            }
        }

        else if(questStage == 40){
            npcl(FacialExpression.NEUTRAL, "There you go my friend, you're now blessed. It's funny, now I look at you, there seems to be something of the faith about you. Anyway, good luck with your quest!").also { stage = END_DIALOGUE; player!!.questRepository.getQuest("Nature Spirit").setStage(player!!, 45) }
        }

        else {
            when(stage){
                0 -> npcl(FacialExpression.NEUTRAL, "Hello, friend, how goes your quest with Filliman?").also { stage++ }
                1 -> playerl(FacialExpression.NEUTRAL, "Still working at it.").also { stage++ }
                2 -> npcl(FacialExpression.NEUTRAL, "Well enough! Do let me know when something develops!").also { stage = END_DIALOGUE }
            }
        }
    }

}

private class BlessingPulse(val drezel: NPC, val player: Player) : Pulse(){
    var ticks = 0

    override fun pulse(): Boolean {
        when(ticks){
            0 -> animate(drezel, 1162).also { spawnProjectile(drezel, player, 268); playAudio(player, Audio(2674)) }
            2 -> visualize(player, Animation(645), Graphics(267, 100))
            4 -> unlock(player).also { player.questRepository.getQuest("Nature Spirit").setStage(player, 40); return true }
        }
        ticks++
        return false
    }

    override fun stop() {
        super.stop()
        openDialogue(player, NSDrezelDialogue(), drezel)
    }
}
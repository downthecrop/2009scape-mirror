package content.region.kandarin.ardougne.quest.fightarena.npcs

import content.region.kandarin.ardougne.quest.fightarena.FightArena
import content.region.kandarin.ardougne.quest.fightarena.FightArenaListeners.Companion.General
import content.region.kandarin.ardougne.quest.fightarena.cutscenes.BouncerCutscene
import content.region.kandarin.ardougne.quest.fightarena.cutscenes.HengradCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs.GENERAL_KHAZARD_258

class GeneralKhazardDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {

        val questName = "Fight Arena"
        val questStage = questStage(player!!, questName)

        npc = NPC(GENERAL_KHAZARD_258)

        when {

            // Talking to General Khazard during first fight.
            (questStage == 71) -> {
                when (stage) {
                    0 -> if (!isEquipped(player!!, 74) && !isEquipped(player!!, 75)) {
                        sendPlayerDialogue(player!!, "General Khazard?").also { stage = 2 }
                    } else {
                        sendNPCDialogue(player!!, 258, "Who dares enter my home? You? A feeble traveller?").also { stage = 6 }
                    }
                    2 -> npcl(FacialExpression.FRIENDLY, "What do you want guard? I'm a busy man.").also { stage = 3 }
                    3 -> playerl(FacialExpression.FRIENDLY, "Of course sir.").also { stage = 4 }
                    4 -> npcl(FacialExpression.FRIENDLY, "In the last two hundred years, I have survived all horrors imaginable. Now it is my turn to cover this land in darkness. One day you shall see, all will quake on hearing my name.").also { stage = 5 }
                    5 -> npcl(FacialExpression.FRIENDLY, "Now leave me.").also { stage = END_DIALOGUE }
                    6 -> playerl(FacialExpression.ASKING, "Feeble?!").also { stage = 7 }
                    7 -> npcl(FacialExpression.ANGRY, "Get out! Whoever let you in shall be severely punished for this.").also { stage = END_DIALOGUE }
                }
            }

            // Talking to General Khazard after first fight.
            (questStage == 72) -> {
                when (stage) {
                    0 -> lockInteractions(player!!, 2).also { sendNPCDialogue(player!!, 267,"You saved my life and my son's, I am eternally in your debt brave traveller.") }.also{ player!!.faceLocation(location(2601,3168,0)) }.also{ stage = 1 }
                    1 -> npcl(FacialExpression.EVIL_LAUGH, "Haha, well done, well done that was rather entertaining. I am the great General Khazard, and the two men you just 'saved' are my property.").also { stage = 2 }
                    2 -> playerl(FacialExpression.ANGRY, "They belong to nobody.").also { player!!.faceLocation(npc!!.location) }.also { stage = 3 }
                    3 -> npcl(FacialExpression.FRIENDLY, "Well, I suppose we could find some arrangement for their freedom... hmmm?").also { stage = 4 }
                    4 -> playerl(FacialExpression.ASKING, "What do you mean?").also { stage = 5 }
                    5 -> npcl(FacialExpression.FRIENDLY, "I'll let them go, but you must stay and fight for me. You'll make me double the gold if you manage to last a few fights.").also { stage = 6 }
                    6 -> npcl(FacialExpression.ANGRY_WITH_SMILE, "Guards! Take " + (if (player!!.isMale) "him" else "her") + " to the cells.").also { stage = 7 }
                    7 -> {
                        end()
                        setAttribute(player!!, "spawn-scorpion", true)
                        HengradCutscene(player!!).start()
                    }
                }
            }

            // Talking to General Khazard during the scorpion fight.
            (questStage <= 87) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.ANGRY, "How dare you speak to me, you are a slave of this arena now.").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 88) -> {
                when (stage) {
                    0 -> {
                        end()
                        HengradCutscene(player!!).start()
                        setAttribute(player!!, "spawn-scorpion", true)
                    }
                }
            }

            // Talking to General Khazard after the scorpion fight.
            (questStage == 89) -> {
                when (stage) {
                    0 -> lockInteractions(player!!, 2).also{ npcl(FacialExpression.ANNOYED, "Not bad, not bad at all. I think you need a tougher challenge. Time for my puppy. Guards! Guards! Bring on Bouncer!") }.also { player!!.faceLocation(npc!!.location) }.also { stage = 1 }
                    1 -> sendDialogue(player!!, "From above you hear a voice..... 'Ladies and gentlemen! Today's second round of battle is between the outsider and Bouncer.'").also { stage = 2 }
                    2 -> {
                        end()
                        setAttribute(player!!, "spawn-bouncer", true)
                        BouncerCutscene(player!!).start()
                    }
                }
            }

            // Talking to General Khazard after killing bouncer.
            (questStage == 91) -> {
                when (stage) {
                    0 -> lockInteractions(player!!,4).also { npcl(FacialExpression.SAD, "Nooooo! Bouncer! How dare you? You've taken the life of my old friend. Now you'll suffer traveller, prepare to meet your maker.") }.also { player!!.faceLocation(npc!!.location) }.also { stage = 1 }
                    1 -> playerl(FacialExpression.ANNOYED, "You agreed to let the Servils go if I stayed to fight.").also { stage = 2 }
                    2 -> npcl(FacialExpression.ANGRY_WITH_SMILE, "Indeed. I obviously underestimated you, although you still pale in comparsion to the might of General Khazard.").also { stage = 3 }
                    3 -> npcl(FacialExpression.NEUTRAL, "You shall see that I am not cowardlly enough to make false promises. They may go.").also { stage = 4 }
                    4 -> npcl(FacialExpression.EVIL_LAUGH, "You however have coused me much trouble today. You must remain here so that I may at least have the pleasure of killing you myself.").also { stage = 5 }
                    5 -> {
                        end()
                        setQuestStage(player!!, FightArena.FightArenaQuest, 97)
                        RegionManager.getNpc(player!!.location, GENERAL_KHAZARD_258, 15)
                        General.attack(player!!)
                    }
                }
            }
        }
    }
}
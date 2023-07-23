package content.region.kandarin.ardougne.quest.arena.dialogue

import content.region.kandarin.ardougne.quest.arena.FightArena
import content.region.kandarin.ardougne.quest.arena.FightArenaListeners.Companion.General
import content.region.kandarin.ardougne.quest.arena.cutscenes.JailCutscene
import content.region.kandarin.ardougne.quest.arena.cutscenes.ThirdFightCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class GeneralKhazardDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.GENERAL_KHAZARD_258)
        when (getQuestStage(player!!, FightArena.FightArenaQuest)) {

            in 68..70 -> when(stage){
                0 -> npcl(FacialExpression.FRIENDLY, "Out of the way, guard! I don't tolerate disruption when I'm watching slaves being slaughtered.").also { stage = END_DIALOGUE }
            }

            71 -> when (stage) {
                0 -> if (!anyInEquipment(player!!, Items.KHAZARD_HELMET_74, Items.KHAZARD_ARMOUR_75)) {
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    sendPlayerDialogue(player!!, "General Khazard?").also { stage = 2 }
                } else {
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    face(findNPC(NPCs.GENERAL_KHAZARD_258)!!, player!!,1)
                    sendNPCDialogue(player!!, NPCs.GENERAL_KHAZARD_258, "Who dares enter my home? You? A feeble traveller?", FacialExpression.OLD_DEFAULT).also { stage = 6 }
                }
                2 -> npcl(FacialExpression.OLD_DEFAULT, "What do you want guard? I'm a busy man.").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "Of course sir.").also { stage++ }
                4 -> npcl(FacialExpression.OLD_DEFAULT, "In the last two hundred years, I have survived all horrors imaginable. Now it is my turn to cover this land in darkness. One day you shall see, all will quake on hearing my name.").also { stage++ }
                5 -> npcl(FacialExpression.OLD_DEFAULT, "Now leave me.").also { stage = END_DIALOGUE }
                6 -> playerl(FacialExpression.ASKING, "Feeble?!").also { stage++ }
                7 -> npcl(FacialExpression.OLD_DEFAULT, "Get out! Whoever let you in shall be severely punished for this.").also { stage = END_DIALOGUE }
            }

            72 -> when (stage) {
                0 -> {
                    lockInteractions(player!!, 2)
                    face(player!!, findNPC(NPCs.JUSTIN_SERVIL_267)!!, 1)
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    sendNPCDialogue(player!!, NPCs.JUSTIN_SERVIL_267, "You saved my life and my son's, I am eternally in your debt brave traveller.").also { stage++ }
                }
                1 -> {
                    face(findNPC(NPCs.GENERAL_KHAZARD_258)!!, player!!, 1)
                    npcl(FacialExpression.OLD_DEFAULT, "Haha, well done, well done that was rather entertaining. I am the great General Khazard, and the two men you just 'saved' are my property.").also { stage++ }
                }
                2 -> {
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    playerl(FacialExpression.ANGRY, "They belong to nobody.").also { stage++ }
                }
                3 -> npcl(FacialExpression.OLD_DEFAULT, "Well, I suppose we could find some arrangement for their freedom... hmmm?").also { stage++ }
                4 -> playerl(FacialExpression.ASKING, "What do you mean?").also { stage++ }
                5 -> npcl(FacialExpression.OLD_DEFAULT, "I'll let them go, but you must stay and fight for me. You'll make me double the gold if you manage to last a few fights.").also { stage++ }
                6 -> npcl(FacialExpression.OLD_ANGRY1, "Guards! Take " + (if (player!!.isMale) "him" else "her") + " to the cells.").also { stage++ }
                7 -> {
                    end()
                    JailCutscene(player!!).start()
                    setAttribute(player!!, "spawn-scorpion", true)
                    sendMessage(player!!, "A guard grabs the keys to the cell doors.")
                }
            }

            87 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.GENERAL_KHAZARD_258)!!, player!!)
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    npcl(FacialExpression.OLD_ANGRY1, "How dare you speak to me, you are a slave of this arena now.").also { stage = END_DIALOGUE }
                }
            }

            88 -> when (stage) {
                0 -> {
                    end()
                    JailCutscene(player!!).start()
                    setAttribute(player!!, "spawn-scorpion", true)
                }
            }

            89 -> when (stage) {
                0 -> {
                    lockInteractions(player!!, 2)
                    face(findNPC(NPCs.GENERAL_KHAZARD_258)!!, player!!, 1)
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    npcl(FacialExpression.OLD_ANGRY1, "Not bad, not bad at all. I think you need a tougher challenge. Time for my puppy. Guards! Guards! Bring on Bouncer!").also { stage++ }
                }
                1 -> sendDialogue(player!!, "From above you hear a voice..... 'Ladies and gentlemen! Today's second round of battle is between the outsider and Bouncer.'").also { stage++ }
                2 -> {
                    end()
                    setAttribute(player!!, "spawn-bouncer", true)
                    ThirdFightCutscene(player!!).start()
                }
            }

            91 -> when (stage) {
                0 -> {
                    lockInteractions(player!!, 4)
                    npcl(FacialExpression.OLD_NEARLY_CRYING, "Nooooo! Bouncer! How dare you? You've taken the life of my old friend. Now you'll suffer traveller, prepare to meet your maker.").also { stage++ }
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                }
                1 -> playerl(FacialExpression.ANNOYED, "You agreed to let the Servils go if I stayed to fight.").also { stage++ }
                2 -> npcl(FacialExpression.OLD_DEFAULT, "Indeed. I obviously underestimated you, although you still pale in comparsion to the might of General Khazard.").also { stage++ }
                3 -> npcl(FacialExpression.OLD_NEARLY_CRYING, "You shall see that I am not cowardlly enough to make false promises. They may go.").also { stage++ }
                4 -> npcl(FacialExpression.OLD_EVIL_LAUGH, "You however have coused me much trouble today. You must remain here so that I may at least have the pleasure of killing you myself.").also { stage++ }
                5 -> {
                    end()
                    setQuestStage(player!!, FightArena.FightArenaQuest, 97)
                    RegionManager.getNpc(player!!.location, NPCs.GENERAL_KHAZARD_258, 15)
                    General.attack(player!!)
                }
            }

            100 -> when (stage) {
                0 -> {
                    face(player!!, findNPC(NPCs.GENERAL_KHAZARD_258)!!, 1)
                    playerl(FacialExpression.ANNOYED, "Didn't I kill you?").also { stage++ }
                }
                1 -> {
                    face(findNPC(NPCs.GENERAL_KHAZARD_258)!!, player!!, 1)
                    npcl(FacialExpression.FRIENDLY,"You might not believe, it, young one, but you can't kill me.").also { stage = END_DIALOGUE }
                }

            }
        }
    }
}
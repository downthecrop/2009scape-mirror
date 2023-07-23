package content.region.kandarin.ardougne.quest.arena.dialogue

import content.region.kandarin.ardougne.quest.arena.FightArena
import content.region.kandarin.ardougne.quest.arena.cutscenes.EscapeCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class JeremyServilADialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.JEREMY_SERVIL_265)
        when (getQuestStage(player!!, FightArena.FightArenaQuest)) {

            20 -> when (stage) {
                0 -> {
                    face(player!!, findNPC(NPCs.JEREMY_SERVIL_265)!!, 1)
                    playerl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                }
                1 -> {
                    face(findNPC(NPCs.JEREMY_SERVIL_265)!!, player!!, 1)
                    npcl(FacialExpression.FRIENDLY, "Please " + (if (player!!.isMale) "Sir" else "Madam") + ", don't hurt me.").also { stage++ }
                }
                2 -> playerl(FacialExpression.FRIENDLY, "Sshh. This uniform is a disguise. I'm here to help. Where do they keep the keys?").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "The guard always keeps hold of them.").also { stage++ }
                4 -> playerl(FacialExpression.FRIENDLY, "Don't lose heart, I'll be back.").also { stage++ }
                5 -> {
                    end()
                    setQuestStage(player!!, FightArena.FightArenaQuest, 40)
                }
            }

            in 68..89 -> when (stage) {
                0 -> {
                    face(player!!, findNPC(NPCs.JEREMY_SERVIL_265)!!, 2)
                    playerl(FacialExpression.FRIENDLY, "Jeremy look, I have the keys.").also { stage++ }
                }
                1 -> {
                    face(findNPC(NPCs.JEREMY_SERVIL_265)!!, player!!, 2)
                    npcl(FacialExpression.AMAZED, "Wow! Please set me free, then we can find my dad. I overheard a guard talking. I think they're taken him to the arena.").also { stage++ }
                }
                2 -> playerl(FacialExpression.NEUTRAL, "Ok, we'd better hurry.").also { stage++ }
                3 -> {
                    end()
                    setAttribute(player!!, "spawn-ogre", true)
                    EscapeCutscene(player!!).start()
                }
            }

            in 90..100 -> when (stage) {
                0 -> {
                    face(player!!, findNPC(NPCs.JEREMY_SERVIL_265)!!, 1)
                    npcl(FacialExpression.FRIENDLY, "You need to kill the creatures in the arena").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
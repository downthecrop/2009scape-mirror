package content.region.kandarin.ardougne.quest.fightarena.npcs

import content.region.kandarin.ardougne.quest.fightarena.FightArena.Companion.FightArenaQuest
import content.region.kandarin.ardougne.quest.fightarena.FightArenaListeners.Companion.Jeremy
import content.region.kandarin.ardougne.quest.fightarena.cutscenes.RescueCutscene
import core.api.questStage
import core.api.setAttribute
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE

class JeremyServilDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {

        val questName = "Fight Arena"
        val questStage = questStage(player!!, questName)

        npc = Jeremy

        when {

            // Talking to Jeremy before we get the keys.
            (questStage == 20) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello").also { npc!!.faceLocation(player!!.location) }.also { player!!.faceLocation(npc!!.location) }.also { stage++ }
                    1 -> npcl(FacialExpression.AFRAID, "Please " + (if (player!!.isMale) "Sir" else "Madam") + ", don't hurt me.").also { stage++ }
                    2 -> playerl(FacialExpression.SILENT, "Sshh. This uniform is a disguise. I'm here to help. Where do they keep the keys?").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "The guard always keeps hold of them.").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "Don't lose heart, I'll be back.").also { stage = END_DIALOGUE }.also { setQuestStage(player!!, FightArenaQuest, 40) }

                }
            }

            // Talking to Jeremy after we get the keys.
            (questStage in 88 downTo 68) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.NEUTRAL, "Jeremy look, I have the keys.").also { npc!!.faceLocation(player!!.location) }.also { player!!.faceLocation(npc!!.location) }.also { stage++ }
                    1 -> npcl(FacialExpression.NEUTRAL, "Wow! Please set me free, then we can find my dad. I overheard a guard talking. I think they're taken him to the arena.").also { stage++ }
                    2 -> playerl(FacialExpression.NEUTRAL, "Ok, we'd better hurry.").also { stage = 3 }
                    3 -> {
                        end()
                        setAttribute(player!!, "spawn-ogre", true)
                        RescueCutscene(player!!).start()
                    }
                }
            }

            // Talk with Jeremy after complete the quest. Source: https://runescapeclassic.fandom.com/wiki/Jeremy_Servil
            (questStage == 100) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.NEUTRAL, "You need to kill the creatures in the arena").also { npc!!.faceLocation(player!!.location) }.also { player!!.faceLocation(npc!!.location) }.also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
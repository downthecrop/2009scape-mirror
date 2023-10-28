package content.region.asgarnia.burthorpe.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import content.region.asgarnia.burthorpe.quest.deathplateau.DenulthDialogueFile
import content.region.asgarnia.burthorpe.quest.trollstronghold.TrollStronghold
import core.api.isQuestComplete
import core.api.isQuestInProgress
import core.api.openDialogue
import core.api.setQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Denulth main dialogue.
 * @author ovenbread
 */
@Initializable
class DenulthDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {

        // When Troll Stronghold is complete
        if (isQuestComplete(player!!, TrollStronghold.questName)) {
            when(stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                1 -> npcl(FacialExpression.HAPPY, "Welcome back friend!").also { stage++ }
                2 -> showTopics(
                        Topic("How goes your fight with the trolls?", 10),
                        Topic("I thought the White Knights controlled Asgarnia?", 20),
                        Topic(FacialExpression.HAPPY, "See you about Denulth!", 30)
                )
                10 -> npcl(FacialExpression.FRIENDLY, "We are busy preparing for an attack by night. Godric knows of a secret entrance to the stronghold. Once we destroy the stronghold Burthorpe will be safe! Friend, we are indebted to you!").also { stage++ }
                11 -> playerl(FacialExpression.FRIENDLY, "Good luck!").also { stage = END_DIALOGUE }

                20 -> npcl(FacialExpression.ANGRY, "You are right citizen. The White Knights have taken advantage of the old and weak king, they control most of Asgarnia, including Falador. However they do not control Burthorpe!").also { stage++ }
                21 -> npcl(FacialExpression.EVIL_LAUGH, "We are the prince's elite troops! We keep Burthorpe secure!").also { stage++ }
                22 -> npcl(FacialExpression.ANGRY, "The White Knights have overlooked us until now! They are pouring money into their war against the Black Knights, They are looking for an excuse to stop our funding and I'm afraid they may have found it!").also { stage++ }
                23 -> npcl(FacialExpression.HALF_WORRIED, "If we can not destroy the troll camp on Death Plateau then the Imperial Guard will be disbanded and Burthorpe will come under control of the White Knights. We cannot let this happen!").also { stage++ }
                24 -> npcl(FacialExpression.ASKING, "Is there anything else you'd like to know?").also { stage = 2 }

                30 -> npcl(FacialExpression.FRIENDLY, "Saradomin be with you, friend!").also { stage = END_DIALOGUE }
            }
            return true
        }

        // Troll Stronghold in progress
        if (isQuestInProgress(player!!, TrollStronghold.questName, 1, 99)) {
            openDialogue(player!!, content.region.asgarnia.burthorpe.quest.trollstronghold.DenulthDialogueFile(), npc)
            return true
        }

        // When Death Plateau is completed, start Troll Stronghold
        if (isQuestComplete(player!!, DeathPlateau.questName)) {
            when(stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                1 -> npcl(FacialExpression.HAPPY, "Welcome back friend!").also { stage++ }
                2 -> showTopics(
                        Topic("How goes your fight with the trolls?", 10),
                        Topic("I thought the White Knights controlled Asgarnia?", 20),
                        Topic(FacialExpression.HAPPY, "See you about Denulth!", 30)
                )
                10 -> npcl(FacialExpression.HALF_WORRIED, "I'm afraid I have bad news. We made our attack as planned, but we met unexpected resistance.").also { stage++ }
                11 -> playerl(FacialExpression.HALF_ASKING, "What happened?").also { stage++ }
                12 -> npcl(FacialExpression.WORRIED, "We were ambushed by trolls coming from the north. They captured Dunstan's son, Godric, who we enlisted at your request; we tried to follow but we were repelled at the foot of their stronghold.").also { stage++ }
                13 -> showTopics(
                        Topic(FacialExpression.SAD, "I'm sorry to hear that.", END_DIALOGUE),
                        Topic("Is there anything I can do to help?", 14)
                )
                14 -> npcl(FacialExpression.HALF_WORRIED, "The way to the stronghold is treacherous, friend. Even if you manage to climb your way up, there will be many trolls defending the stronghold.").also{ stage++ }
                15 -> showTopics(
                        Topic("I'll get Godric back!", 16),
                        Topic("I'm sorry, there's nothing I can do.", END_DIALOGUE)
                )
                16 -> npcl(FacialExpression.HAPPY, "God speed friend! I would send some of my men with you, but none of them are brave enough to follow.").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, TrollStronghold.questName, 1)
                }

                20 -> npcl(FacialExpression.ANGRY, "You are right citizen. The White Knights have taken advantage of the old and weak king, they control most of Asgarnia, including Falador. However they do not control Burthorpe!").also { stage++ }
                21 -> npcl(FacialExpression.EVIL_LAUGH, "We are the prince's elite troops! We keep Burthorpe secure!").also { stage++ }
                22 -> npcl(FacialExpression.ANGRY, "The White Knights have overlooked us until now! They are pouring money into their war against the Black Knights, They are looking for an excuse to stop our funding and I'm afraid they may have found it!").also { stage++ }
                23 -> npcl(FacialExpression.HALF_WORRIED, "If we can not destroy the troll camp on Death Plateau then the Imperial Guard will be disbanded and Burthorpe will come under control of the White Knights. We cannot let this happen!").also { stage++ }
                24 -> npcl(FacialExpression.ASKING, "Is there anything else you'd like to know?").also { stage = 2 }

                30 -> npcl(FacialExpression.FRIENDLY, "Saradomin be with you, friend!").also { stage = END_DIALOGUE }
            }
            return true
        }

        // Fallback to default. Always the start of Death Plateau
        openDialogue(player!!, DenulthDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DenulthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DENULTH_1060)
    }
}
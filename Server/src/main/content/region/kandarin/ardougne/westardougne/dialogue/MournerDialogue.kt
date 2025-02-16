package content.region.kandarin.ardougne.westardougne.dialogue

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCity
import content.region.kandarin.ardougne.westardougne.MournerUtilities.EXTRA_GEAR
import content.region.kandarin.ardougne.westardougne.MournerUtilities.JUST_GEAR
import content.region.kandarin.ardougne.westardougne.MournerUtilities.JUST_MASK
import content.region.kandarin.ardougne.westardougne.MournerUtilities.NO_GEAR
import content.region.kandarin.ardougne.westardougne.MournerUtilities.wearingMournerGear
import core.api.getQuestStage
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class MournerDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object{
        const val WHATS_A_MOURNER = 10
        const val NO_PLAGUE = 20
        const val ELENA = 30

        const val CONVO_1 = 40
        const val CONVO_2 = 50
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(wearingMournerGear(player)){
            NO_GEAR, JUST_MASK -> {
                if (npc.id == NPCs.MOURNER_717) {
                    when (stage) {
                        0 -> npcl(
                            FacialExpression.HALF_ASKING,
                            "Hmmm, how did you get over here? You're not one of this rabble. Ah well, you'll have to stay. Can't risk you going back now."
                        ).also { stage++ }

                        1 -> showTopics(
                            Topic("So what's a mourner?", WHATS_A_MOURNER),
                            Topic("I haven't got the plague though... ", NO_PLAGUE),
                            IfTopic(
                                "I'm looking for a woman named Elena.",
                                ELENA,
                                getQuestStage(player, Quests.PLAGUE_CITY) in (6..98)
                            )
                        )

                        WHATS_A_MOURNER -> npcl(
                            FacialExpression.NEUTRAL,
                            "We're working for King Lathas of East Ardougne trying to contain the accursed plague sweeping West Ardougne." +
                                    " We also do our best to ease these people's suffering."
                        ).also { stage++ }

                        WHATS_A_MOURNER + 1 -> npcl(
                            FacialExpression.NEUTRAL, "We're nicknamed mourners because we spend a lot of" +
                                    " time at plague victim funerals, no-one else is allowed to risk the funerals." +
                                    " It's a demanding job, and we get little thanks from the people here."
                        ).also { stage = END_DIALOGUE }

                        ELENA -> npcl(
                            FacialExpression.NEUTRAL,
                            "Ah yes, I've heard of her. A healer I believe. She must be mad coming over here voluntarily."
                        ).also { stage++ }

                        ELENA + 1 -> npcl(
                            FacialExpression.SAD,
                            "I hear rumours she has probably caught the plague now. Very tragic, a stupid waste of life."
                        ).also { stage = END_DIALOGUE }

                        NO_PLAGUE -> npcl(
                            FacialExpression.ANNOYED,
                            "Can't risk you being a carrier. That protective clothing you have isn't regulation issue. It won't meet safety standards."
                        ).also { stage = END_DIALOGUE }
                    }
                }
                else{
                    npcl(FacialExpression.ANNOYED, "Stand back citizen, do not approach me.").also { stage = END_DIALOGUE }
                }

            }
            JUST_GEAR -> {
                when(stage){
                    0 -> playerl(FacialExpression.NEUTRAL, "Hello.").also {
                        stage = if((0..1).random() > 0) CONVO_1 else CONVO_2
                    }

                    CONVO_1 -> npcl(FacialExpression.NEUTRAL, "Good day. Are you in need of assistance?").also { stage++ }
                    CONVO_1 + 1 -> playerl(FacialExpression.NEUTRAL, " Yes, but I don't think you can help.").also { stage++ }
                    CONVO_1 + 2 -> npcl(FacialExpression.NEUTRAL, " You will be surprised at how much help the brute force of the Guard can be.").also { stage++ }
                    CONVO_1 + 3 -> playerl(FacialExpression.NEUTRAL, " Well I'll be sure to ask if I'm in need of some muscle.").also { stage = END_DIALOGUE }

                    CONVO_2 -> npcl(FacialExpression.ANNOYED, " Good day. Are you in need of assistance?").also { stage++ }
                    CONVO_2 + 1 -> playerl(FacialExpression.NEUTRAL, " No, I just wanted to talk to a friendly face.").also { stage++ }
                    CONVO_2 + 2 -> npcl(FacialExpression.ANGRY, " Do I look friendly to you? I really must work on my scowl more.").also { stage = END_DIALOGUE }
                }

            }
            EXTRA_GEAR -> {
                when(stage){
                    0 -> npcl(FacialExpression.ANNOYED, "You should know better than to wear non-regulation gear.").also { stage++ }
                    1 -> playerl(FacialExpression.HALF_GUILTY, "Sorry, I'm new around here.").also { stage++ }
                    2 -> npcl(FacialExpression.ANNOYED, "Well, you know the drill - lose the gear, I will let it pass this time.").also { stage = END_DIALOGUE }
                }
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_717, NPCs.MOURNER_348, NPCs.MOURNER_347, NPCs.MOURNER_371, NPCs.MOURNER_369)
    }
}

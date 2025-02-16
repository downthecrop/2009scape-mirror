package content.region.kandarin.ardougne.westardougne.dialogue

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCity
import content.region.kandarin.ardougne.westardougne.MournerUtilities
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs
import kotlin.properties.Delegates

@Initializable
class HeadMournerDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object {
        const val CLEARANCE = 40
        const val WHATS_A_MOURNER = 10
        const val NO_PLAGUE = 20
        const val ELENA = 30

        const val CRAZY = 50
        const val KIDNAP = 60
        const val MASK = 70

        var mourningGear by Delegates.notNull<Int>()

    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        mourningGear = MournerUtilities.wearingMournerGear(player)
        if(mourningGear < MournerUtilities.JUST_GEAR){
                npcl(FacialExpression.ANGRY, "How did you get into West Ardougne? " +
                "Ah well you'll have to stay, can't risk you spreading the plague outside.").also { stage++ }
        }
        else{
                npcl(FacialExpression.NEUTRAL, "Ahh... A new recruit.").also { stage++ }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(mourningGear){
            MournerUtilities.NO_GEAR, MournerUtilities.JUST_MASK -> when(stage){
                START_DIALOGUE + 1 -> showTopics(
                    IfTopic("I need clearance to enter a plague house.", CLEARANCE,
                        !isQuestComplete(player, Quests.PLAGUE_CITY) && (getQuestStage(player, Quests.PLAGUE_CITY) > 11)),
                    Topic("So what's a mourner?", WHATS_A_MOURNER),
                    Topic("I haven't got the plague though. ", NO_PLAGUE),
                    IfTopic("I'm looking for a woman named Elena.", ELENA, !isQuestComplete(player, Quests.PLAGUE_CITY))
                )

                CLEARANCE -> npcl(FacialExpression.DISGUSTED, "You must be nuts, absolutely not!").also { stage++ }
                CLEARANCE + 1 -> showTopics(
                    Topic("There's a kidnap victim inside!", KIDNAP),
                    Topic("I've got a gas mask though...", MASK),
                    Topic("Yes, I'm utterly crazy.", CRAZY)
                )

                KIDNAP -> npcl(FacialExpression.FRIENDLY, "Well they're as good as dead then, no point in trying to save them.").also { stage = END_DIALOGUE }

                MASK -> npcl(FacialExpression.FRIENDLY, "It's not regulation. Anyway you're not properly trained to deal with the plague.").also { stage++ }
                MASK + 1 -> playerl(FacialExpression.FRIENDLY, "How do I get trained?").also { stage++ }
                MASK + 2 -> npcl(FacialExpression.FRIENDLY, "It requires a strict 18 months of training.").also { stage++ }
                MASK + 3 -> playerl(FacialExpression.FRIENDLY, "I don't have that sort of time.").also { stage = END_DIALOGUE }

                CRAZY -> npcl(FacialExpression.FRIENDLY, "You're wasting my time, I have a lot of work to do!").also { stage = END_DIALOGUE }

                WHATS_A_MOURNER -> npcl(FacialExpression.NEUTRAL,
                    "We're working for King Lathas of East Ardougne trying to contain the accursed plague sweeping West Ardougne." +
                            " We also do our best to ease these people's suffering.").also { stage++ }
                WHATS_A_MOURNER + 1 -> npcl(FacialExpression.NEUTRAL, "We're nicknamed mourners because we spend a lot of" +
                            " time at plague victim funerals, no-one else is allowed to risk the funerals." +
                        " It's a demanding job, and we get little thanks from the people here.").also { stage = END_DIALOGUE }

                NO_PLAGUE -> npcl(FacialExpression.ANNOYED, "Can't risk you being a carrier. That protective clothing you have isn't regulation issue. It won't meet safety standards.").also { stage = END_DIALOGUE }

                ELENA -> npcl(
                    FacialExpression.NEUTRAL,
                    "Ah yes, I've heard of her. A healer I believe. She must be mad coming over here voluntarily."
                ).also { stage++ }

                ELENA + 1 -> npcl(
                    FacialExpression.SAD,
                    "I hear rumours she has probably caught the plague now. Very tragic, a stupid waste of life."
                ).also { stage = END_DIALOGUE }

            }

            MournerUtilities.JUST_GEAR, MournerUtilities.EXTRA_GEAR -> when(stage){
                START_DIALOGUE+1 ->playerl(FacialExpression.ASKING, " How do you know I'm new?").also { stage++ }
                START_DIALOGUE+2 -> npcl(FacialExpression.NEUTRAL, "Because all the old members of the guard know to report to the real Head Mourner, not me.").also { if (mourningGear == MournerUtilities.EXTRA_GEAR) stage++ else stage+=2 }
                START_DIALOGUE+3 -> npcl(FacialExpression.ANNOYED, "Also, none of the old timers use non-regulation gear.").also { stage++ }
                START_DIALOGUE+4 -> playerl(FacialExpression.ASKING, " You're not the real overseer here?").also { stage++ }
                START_DIALOGUE+5 -> npcl(FacialExpression.NEUTRAL, "No, I am just a front man, our true head is far too busy to deal with the requests of the citizens, so I conduct the day to day business here.").also { stage++ }
                START_DIALOGUE+6 -> npcl(FacialExpression.NEUTRAL, "You should go and report in" + if(mourningGear == MournerUtilities.EXTRA_GEAR) ", I would lose the non-regulation gear as well if I were you." else "." ).also { stage++ }
                START_DIALOGUE+7 -> playerl(FacialExpression.FRIENDLY, "Okay, thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }


    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HEAD_MOURNER_716)
    }

}
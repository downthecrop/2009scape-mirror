package content.region.kandarin.ardougne.quest.plaguecity.dialogue

import content.data.Quests
import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ClerkDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object{
        const val TALK_AGAIN = 10

        const val THROUGH_DOOR = 20
        const val PERMISSION = 30
        const val QUESTIONS = 40
        const val URGENT = 60
        const val RUN_EVERYTHING = 70
        const val WHEN_AVAILABLE = 80
        const val DISTURBED = 90
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.PLAGUE_CITY) < 13)
            npcl(FacialExpression.NEUTRAL, "Hello, welcome to the Civic Office of West Ardougne. How can I help you?").also { stage = QUESTIONS }
        else
            npcl(FacialExpression.FRIENDLY, "Bravek will see you now but keep it short!").also { stage = TALK_AGAIN }

        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {

        when(stage){

            QUESTIONS -> showTopics(
                IfTopic("I need permission to enter a plague house.", PERMISSION, getQuestStage(player, Quests.PLAGUE_CITY) > 11),
                Topic("Who is through that door?", THROUGH_DOOR),
                Topic("I'm just looking thanks.", END_DIALOGUE),
            )

            // npcl does not wordwrap right
            PERMISSION -> npc(FacialExpression.NEUTRAL, "Rather you than me! The mourners normally deal with",
                "that stuff, you should speak to them. Their headquarters","are right near the city gate.").also { stage++ }
            PERMISSION + 1 -> showTopics(
                Topic("I'll try asking them then.", END_DIALOGUE),
                Topic("Surely you don't let them run everything for you?", RUN_EVERYTHING),
                Topic("This is urgent though!", URGENT, skipPlayer = true)
            )

            RUN_EVERYTHING -> npcl(FacialExpression.NEUTRAL, " Well, they do know what they're doing here. " +
                    "If they did start doing something badly Bravek, the city warder, would have the power to override them. " +
                    "I can't see that happening though.").also { stage++ }
            RUN_EVERYTHING + 1 -> showTopics(
                Topic("I'll try asking them then.", END_DIALOGUE),
                Topic("Can I speak to Bravek anyway?", DISTURBED)
            )

            URGENT -> playerl(FacialExpression.PANICKED, " This is urgent though! Someone's been kidnapped and is being held in a plague house!").also { stage++ }
            URGENT+ 1 -> npcl(FacialExpression.NEUTRAL, "I'll see what I can do I suppose.").also { stage++ }
            URGENT + 2 -> npcl(FacialExpression.NEUTRAL, " Mr Bravek, there's a ${if (player.isMale) "man" else "lady"} here who really needs to speak to you.").also {
                stage++
                npc = NPC(NPCs.BRAVEK_711)
            }
            URGENT + 3 -> npc(FacialExpression.ANNOYED, " I suppose they can come in then. If they keep it short.").also {
                stage = END_DIALOGUE
                setQuestStage(player, Quests.PLAGUE_CITY, 13)
            }

            THROUGH_DOOR -> npcl(FacialExpression.NEUTRAL, "The city warder Bravek is in there.").also { stage++ }
            THROUGH_DOOR + 1 -> playerl(FacialExpression.ASKING, " Can I go in?").also { stage = DISTURBED }

            DISTURBED -> npcl(FacialExpression.NEUTRAL, " He has asked not to be disturbed.").also { stage++ }
            DISTURBED + 1 -> showTopics(
                IfTopic("This is urgent though!", URGENT, getQuestStage(player, Quests.PLAGUE_CITY) > 11, skipPlayer = true),
                Topic("Ok, I'll leave him alone.", END_DIALOGUE),
                Topic("Do you know when he will be available?", WHEN_AVAILABLE)
            )

            WHEN_AVAILABLE -> npcl(FacialExpression.NEUTRAL, " Oh I don't know, an hour or so maybe.").also { stage = END_DIALOGUE }

            TALK_AGAIN -> playerl(FacialExpression.FRIENDLY, "Thanks, I won't take much of his time.").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLERK_713)
}

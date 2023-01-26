package content.region.fremennik.jatizso.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class CitizenDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    val stages = intArrayOf(0, 100, 200, 300)
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return CitizenDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        stage = stages.random()
        handle(0,0)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "It's a bit grey round here, isn't it?").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "It gets you down after a while, you know. There are 273 shades of grey, you know, and we have them all.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "That's grey-t.").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.SAD, "That attempt at humour merely made me more depressed. Leave me alone.").also { stage = END_DIALOGUE }

            100 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "Cheer up! It's not the end of the world.").also { stage++ }
            101 -> npcl(core.game.dialogue.FacialExpression.SAD, "I'd prefer that, if it meant I didn't have to talk to people as inanely happy as you.").also { stage++ }
            102 -> playerl(core.game.dialogue.FacialExpression.AMAZED, "Whoa! I think you need to get out more.").also { stage = END_DIALOGUE }

            200 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "How's the King treating you then?").also { stage++ }
            201 -> npcl(core.game.dialogue.FacialExpression.SAD, "Like serfs.").also { stage++ }
            202 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING, "Serf?").also { stage++ }
            203 -> npcl(core.game.dialogue.FacialExpression.SAD, "Yes, you know - peons, plebs, the downtrodden. He treats us like his own personal possessions.").also { stage++ }
            204 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL,  "You should leave this place.").also { stage++ }
            205 -> npcl(core.game.dialogue.FacialExpression.SAD,  "I keep trying to save up enough to leave, but the King keeps taxing us! We have no money left." ).also { stage++ }
            206 -> playerl(core.game.dialogue.FacialExpression.SAD, "Oh dear." ).also { stage = END_DIALOGUE }

            300 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING, "How are you today?").also { stage++ }
            301 -> npcl(core.game.dialogue.FacialExpression.SAD, "**sigh**").also { stage++ }
            302 -> playerl(core.game.dialogue.FacialExpression.ASKING, "That good? Everyone around here seems a little depressed. ").also { stage++ }
            303 -> npcl(core.game.dialogue.FacialExpression.SAD, "**sigh**").also { stage++ }
            304 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING, "And not particularly talkative.").also { stage++ }
            305 -> npcl(core.game.dialogue.FacialExpression.SAD, "**sigh**").also { stage++ }
            306 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING,"I'll leave you to your sighing. It looks like you have plenty to do." ).also { stage = END_DIALOGUE }

        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LENSA_5494, NPCs.SASSILIK_5496, NPCs.FREYGERD_5493)
    }

}
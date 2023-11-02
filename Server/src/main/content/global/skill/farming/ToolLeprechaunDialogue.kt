package content.global.skill.farming

import content.minigame.vinesweeper.Vinesweeper
import core.api.openInterface
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.NPCs

/**
 * Tool Leprechaun dialogue.
 * @author ovenbread
 *
 * This is really hard to find anything beyond the first dialogue.
 * https://www.youtube.com/watch?v=2wgWB9U5Ju8 0L25 May 19, 2010
 * https://www.youtube.com/watch?v=gqend8EibPs: 0:02 Nov 28, 2010
 * https://www.youtube.com/watch?v=6cnsQsVGzXI: 4:05 Feb 14, 2012 - The Leprechaun in Troll Stronghold!
 *
 * Note:
 * Ultracompost was introduced in 2017(OSRS) 2018(RS3), so it is not included.
 * Leprechaun Composting was only added in 2016, so it is also not included.
 */
@Initializable
class ToolLeprechaunDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            START_DIALOGUE -> npc(FacialExpression.OLD_HAPPY, "Ah, 'tis a foine day to be sure! Can I help ye with tool", "storage, or a trip to Winkin's Farm, or what?").also { stage++ }

            1 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes, please.", 10),
                    Topic("What tools can you store?", 2, true),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
                    // "Other topics." -> Lost City quest where is Shamus. Evil Tree Reward collection in late 2009.
            )
            2 -> playerl(FacialExpression.THINKING, "What can you store?").also { stage++ }
            3 -> npcl(FacialExpression.OLD_HAPPY, "We'll hold onto yer rake, yer seed dibber, yer spade, yer secateurs, yer waterin' can and yer trowel - but mind it's not one of them fancy trowels only archaeologists use!").also { stage++ }
            4 -> npcl(FacialExpression.OLD_HAPPY, "We'll take a few buckets off yer hands too, and even yer compost and supercompost! There's room in our shed for plenty of compost, so bring it on.").also { stage++ }
            5 -> npcl(FacialExpression.OLD_HAPPY, "Also if ye hands us yer farming produce, we might be able to change it into banknotes.").also { stage++ }
            6 -> npcl(FacialExpression.OLD_HAPPY, "So... do ye want to be using the store?").also { stage++ }
            7 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes, please.", 10),
                    Topic("What do you do with the tools you're storing?", 11, true),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
            )
            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }
            11 -> playerl(FacialExpression.THINKING, "What do you do with the tools you're storing? They can't possibly all fit in your pockets!").also { stage++ }
            12 -> npcl(FacialExpression.OLD_HAPPY, "We leprechauns have a shed where we keep 'em. It's a magic shed, so ye can get yer items back from any of us leprechauns whenever ye want. Saves ye havin' to carry loads of stuff around the country!").also { stage++ }
            13 -> npcl(FacialExpression.OLD_HAPPY, "So... do ye want to be using the store?").also { stage = 1 }

            20 -> npcl(FacialExpression.OLD_NORMAL, "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!").also {
                stage = END_DIALOGUE
            }
            30 -> {
                end()
                Vinesweeper.Companion.VinesweeperTeleport.teleport(npc!!, player!!)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ToolLeprechaunDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOOL_LEPRECHAUN_3021)
    }
}

/**
 * Note:
 * This Leprechaun Larry is a special case where he does NOT transport you to Winkin's farm, but has a store.
 */
@Initializable
class ToolLeprechaunOnVacationDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            START_DIALOGUE -> npc(FacialExpression.OLD_HAPPY, "Aye, top o' th' mornin' to ya!", "Are ye wantin' help with th' tool store?").also {
                stage = 2
            }
            2 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes please.", 10),
                    Topic(FacialExpression.THINKING, "Why are you sunbathing up a mountain?", 3),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Would you like to trade?", 12),
            )
            3 -> npcl(FacialExpression.OLD_HAPPY, "We tool leprechauns work hard, that we do. An'nary a penny do we get in return. So ye cannae begrudge me mah holiday an' a wee drink or twelve!").also { stage++ }
            4 -> playerl(FacialExpression.THINKING, "Yes, very nice, but why are you sunbathing up a mountain? Surely a beach would be more appropriate?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_HAPPY, "Ahh, but I likes th' ruggedy mountain, ye see. Also, I ha' a terrible allergy to sand.").also { stage++ }
            6 -> playerl(FacialExpression.NEUTRAL, "Fair enough, I suppose.").also { stage++ }
            7 -> npcl(FacialExpression.OLD_HAPPY, "So were ye wantin' help with th' tool store?").also { stage++ }
            8 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes, please.", 10),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
            )
            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }
            12 -> npcl(FacialExpression.OLD_HAPPY, "Sure, have a look.").also { stage++ }
            13 -> end().also{
                npc.openShop(player)
            }
            20 -> npcl(FacialExpression.OLD_NORMAL, "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ToolLeprechaunOnVacationDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOOL_LEPRECHAUN_4965)
    }
}

/**
 * Note:
 * Goth chatheads are unfortunately updated and have frozen FacialExpressions. Disabled talk-to for now.
 */
// @Initializable
class ToolLeprechaunGothDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            START_DIALOGUE -> npc(FacialExpression.HAPPY, "Ah, 'tis a foine day to be sure! Can I help ye with tool", "storage, or a trip to Winkin's Farm, or what?").also { stage++ }

            1 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes, please.", 10),
                    Topic("What tools can you store?", 2, true),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
                    // "Other topics." -> Lost City quest where is Shamus. Evil Tree Reward collection in late 2009.
            )
            2 -> playerl(FacialExpression.THINKING, "What can you store?").also { stage++ }
            3 -> npcl(FacialExpression.HAPPY, "We'll hold onto yer rake, yer seed dibber, yer spade, yer secateurs, yer waterin' can and yer trowel - but mind it's not one of them fancy trowels only archaeologists use!").also { stage++ }
            4 -> npcl(FacialExpression.HAPPY, "We'll take a few buckets off yer hands too, and even yer compost and supercompost! There's room in our shed for plenty of compost, so bring it on.").also { stage++ }
            5 -> npcl(FacialExpression.HAPPY, "Also if ye hands us yer farming produce, we might be able to change it into banknotes.").also { stage++ }
            6 -> npcl(FacialExpression.HAPPY, "So... do ye want to be using the store?").also { stage++ }
            7 -> showTopics(
                    Topic(FacialExpression.NEUTRAL, "Yes, please.", 10),
                    Topic("What do you do with the tools you're storing?", 11, true),
                    Topic(FacialExpression.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
            )
            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }
            11 -> playerl(FacialExpression.THINKING, "What do you do with the tools you're storing? They can't possibly all fit in your pockets!").also { stage++ }
            12 -> npcl(FacialExpression.HAPPY, "We leprechauns have a shed where we keep 'em. It's a magic shed, so ye can get yer items back from any of us leprechauns whenever ye want. Saves ye havin' to carry loads of stuff around the country!").also { stage++ }
            13 -> npcl(FacialExpression.HAPPY, "So... do ye want to be using the store?").also { stage = 1 }

            20 -> npcl(FacialExpression.NEUTRAL, "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!").also {
                stage = END_DIALOGUE
            }
            30 -> {
                end()
                Vinesweeper.Companion.VinesweeperTeleport.teleport(npc!!, player!!)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ToolLeprechaunDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOTH_LEPRECHAUN_8000)
    }
}
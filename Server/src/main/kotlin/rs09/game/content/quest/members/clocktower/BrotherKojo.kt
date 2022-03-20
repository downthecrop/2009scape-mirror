package rs09.game.content.quest.members.clocktower

import api.getAttribute
import api.questStage
import api.setAttribute
import api.setQuestStage
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.quest.members.monksfriend.setQuest
import rs09.game.content.quest.members.thefremenniktrials.cleanupAttributes
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

@Initializable
class BrotherKojo(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        when(questStage(player, "Clock Tower")) {
            0 -> player(FacialExpression.FRIENDLY,"Hello, monk.").also { stage = 0 }
            1, 2 -> {
                    when (getAttribute(player, "quest:clocktower-cogsplaced", 0)) {
                        0 -> player(FacialExpression.FRIENDLY, "Hello again.").also { stage = 50 }
                        1 -> player(FacialExpression.FRIENDLY, "I've placed a cog!").also { stage = 60 }
                        2 -> player(FacialExpression.FRIENDLY, "Two down!").also { stage = 65 }
                        3 -> npc(FacialExpression.FRIENDLY, "One left.").also { stage = END_DIALOGUE }
                        4 -> player(FacialExpression.FRIENDLY, "I have replaced all the cogs!").also { stage = 100 }
                    }
            }
            100 -> player(FacialExpression.FRIENDLY, "Hello again Brother Kojo.").also { stage = 400 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {

            0 -> npcl(FacialExpression.FRIENDLY, "Hello adventurer. My name is Brother Kojo. Do you happen to know the time?").also { stage++ }
            1 -> player(FacialExpression.SAD, "No, sorry, I don't.").also { stage++ }
            2 -> npcl(FacialExpression.NEUTRAL, "Exactly! This clock tower has recently broken down, and without it nobody can tell the correct time. I must fix it before the town people become too angry!").also { stage++ }
            3 -> npcl(FacialExpression.ASKING, "I don't suppose you could assist me in the repairs? I'll pay you for your help.").also { stage++ }
            4 -> options("OK old monk, what can I do?", "How much reward are we talking?", "Not now old monk.").also { stage++ }
            5 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Ok old monk, what can I do?").also { stage = 20 }
                2 -> player(FacialExpression.ASKING, "So... how much reward are we talking then?").also { stage = 200 }
                3 -> player(FacialExpression.FRIENDLY, "Not now old monk.").also { stage = 300 }
            }

            20 -> npc(FacialExpression.HAPPY, "Oh, thank you kind ${if (player.isMale) "sir" else "madam"}!",
                "In the cellar below, you'll find four cogs.",
                "They're too heavy for me, but you should be able to",
                "carry them one at a time.").also { stage++ }
            21 -> npcl(FacialExpression.THINKING, "I know one goes on each floor... but I can't exactly remember which goes where specifically. Oh well, I'm sure you can figure it out fairly easily.").also { stage++}
            22 -> player(FacialExpression.FRIENDLY, "Well, I'll do my best.").also { stage++ }
            23 -> npcl(FacialExpression.HAPPY, "Thank you again! And remember to be careful, the cellar is full of strange beasts!").also {
                stage = END_DIALOGUE
                setQuestStage(player, "Clock Tower", 1)
                setAttribute(player, "/save:quest:clocktower-cogsplaced", 0)
            }

            50 -> npcl(FacialExpression.ASKING, "Oh hello, are you having trouble? The cogs are in four rooms below us. Place one cog on a pole on each of the four tower levels.").also { stage++ }
            51 -> player(FacialExpression.FRIENDLY, "Right, gotcha. I'll do that then.").also { stage = END_DIALOGUE }

            60 -> npcl(FacialExpression.FRIENDLY, "That's great. Come see me when you've done the other three.").also { stage = END_DIALOGUE }
            65 -> npc(FacialExpression.FRIENDLY, "Two to go.").also { stage = END_DIALOGUE }

            100 -> npcl(FacialExpression.FRIENDLY, "Really..? Wait, listen! Well done, well done! Yes yes yes, you've done it! You ARE clever!").also { stage++ }
            101 -> npcl(FacialExpression.FRIENDLY, "The townsfolk will all be able to know the correct time now! Thank you so much for all of your help! And as promised, here is your reward!").also { stage++ }
            102 -> {
                end()
                player.questRepository.getQuest("Clock Tower").finish(player)
                player.removeAttribute("quest:clocktower-cogsplaced")
                player.removeAttribute("quest:clocktower-blackcogplaced")
                player.removeAttribute("quest:clocktower-redcogplaced")
                player.removeAttribute("quest:clocktower-bluecogplaced")
                player.removeAttribute("quest:clocktower-whitecogplaced")
                player.removeAttribute("quest:clocktower-poisonplaced")
            }

            200 -> npcl(FacialExpression.STRUGGLE, "Well, I'm only a monk so I'm not exactly rich, but I assure you I will give you a fair reward for the time spent assisting me in repairing the clock.").also { stage = 4 }

            300 -> npcl(FacialExpression.FRIENDLY, "OK then. Come back and let me know if you change your mind.").also { stage = END_DIALOGUE }

            400 -> npcl(FacialExpression.FRIENDLY, "Oh hello there traveller. You've done a grand job with the clock. It's just like new.").also { stage = END_DIALOGUE }

        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BrotherKojo(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_KOJO_223)
    }
}

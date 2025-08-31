package content.region.karamja.tzhaar.handlers

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles the TzHaarMejJal dialogue.
 * @author 'Vexia
 * @author Empathy
 * @author Logg
 */
@Initializable
class TzHaarMejJal(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HALF_GUILTY, "You want help JalYt-Ket-${player.username}?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> showTopics(
                if (inInventory(player, Items.FIRE_CAPE_6570)) {
                    Topic(FacialExpression.HALF_GUILTY, "I have a fire cape here.", 100)
                } else {
                    Topic(FacialExpression.HALF_GUILTY, "What is this place?", 10)
                },
                Topic(FacialExpression.HALF_GUILTY, "What did you call me?", 20),
                IfTopic(FacialExpression.HALF_GUILTY, "About my challenge...", 50, getAttribute(player, "fc_practice_jad", false) && settings!!.jad_practice_enabled),
                IfTopic("I want to challenge Jad directly.", 30, !getAttribute(player, "fc_practice_jad", false) && settings!!.jad_practice_enabled, skipPlayer = true),
                Topic(FacialExpression.HALF_GUILTY, "No I'm fine thanks.", END_DIALOGUE)
            )

            10 -> npc(FacialExpression.HALF_GUILTY,
                "This is the fight caves, TzHaar-Xil made it for practice,",
                "but many JalYt come here to fight too.",
                "Just enter the cave and make sure you're prepared."
                ).also { stage++ }

            11 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "Are there any rules?", 12),
                Topic(FacialExpression.HALF_GUILTY, "Ok thanks.", END_DIALOGUE)
            )

            12 -> npcl(FacialExpression.HALF_GUILTY, "Rules? Survival is the only rule in there.").also { stage++ }
            13 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "Do I win anything?", 14),
                Topic(FacialExpression.HALF_GUILTY, "Sounds good.", END_DIALOGUE)
            )

            14 -> npc(FacialExpression.HALF_GUILTY,
                "You ask a lot of questions.",
                "Might give you TokKul if you last long enough."
                ).also { stage++ }
            15 -> playerl(FacialExpression.HALF_GUILTY, "...").also { stage ++ }
            16 -> npcl(FacialExpression.HALF_GUILTY, "Before you ask, TokKul is like your Coins.").also { stage++ }
            17 -> npc(FacialExpression.HALF_GUILTY,
                "Gold is like you JalYt, soft and easily broken, we use",
                "hard rock forged in fire like TzHaar!"
                ).also{ stage = END_DIALOGUE }

            20 -> npcl(FacialExpression.HALF_GUILTY, "Are you not JalYt-Ket?").also { stage++ }
            21 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "What's a 'JalYt-Ket'?", 22),
                Topic(FacialExpression.HALF_GUILTY, "I guess so...", END_DIALOGUE),
                Topic(FacialExpression.HALF_GUILTY, "No I'm not!", END_DIALOGUE)
            )

            22 -> npcl(FacialExpression.HALF_GUILTY, "That what you are... you tough and strong no?").also { stage++ }
            23 -> playerl(FacialExpression.HALF_GUILTY, "Well yes I suppose I am...").also { stage = END_DIALOGUE }

            30 -> player(FacialExpression.HALF_GUILTY,
                "The challenge is too long.",
                "I want to challenge Jad directly."
            ).also { stage++ }
            31 -> npc(FacialExpression.DISGUSTED_HEAD_SHAKE,
                "I thought you strong and tough",
                "but you want skip endurance training?"
                ).also { stage++ }
            32 -> npcl(FacialExpression.NEUTRAL, "Maybe you not JalYt-Ket afterall.").also { stage++ }
            33 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "I don't have time for it, man.", 35),
                Topic("No, I'm JalYt-Ket!", 34, skipPlayer = true)
            )

            34 -> player(FacialExpression.HALF_GUILTY,
                "No, I'm JalYt-Ket! I swear!",
                "I'll do the training properly."
                ).also { stage = END_DIALOGUE }

            35 -> npc(FacialExpression.DISGUSTED_HEAD_SHAKE,
                "JalYt, you know you not get reward",
                "if you not do training properly, ok?"
                ).also { stage++ }
            36 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "That's okay, I don't need a reward.", 37),
                Topic(FacialExpression.HALF_GUILTY, "Oh, nevermind then.", END_DIALOGUE)
            )

            37 -> playerl(FacialExpression.NEUTRAL, "I just wanna fight the big guy.").also { stage++ }
            38 -> npc(FacialExpression.NEUTRAL,
                "Okay JalYt.",
                "TzTok-Jad not show up for just anyone."
                ).also { stage++ }
            39 -> npc(FacialExpression.NEUTRAL,
                "You give 8000 TokKul, TzTok-Jad know you serious.",
                "You get it back if you victorious."
                ).also { stage++ }
            40 -> showTopics(
                Topic(FacialExpression.HALF_GUILTY, "That's fair, here's 8000 TokKul.", 41),
                Topic(FacialExpression.HALF_GUILTY, "I don't have that much on me, but I'll go get it.", END_DIALOGUE),
                Topic("TzTok-Jad must be old and tired to not just accept my challenge.", 42, skipPlayer = true)
            )

            41 -> if (removeItem(player, Item(Items.TOKKUL_6529, 8000))) {
                    npc(FacialExpression.NEUTRAL,
                        "Okay JalYt. Enter cave when you are prepared.",
                        "You find TzTok-Jad waiting for JalYt challenger."
                    )
                    .also { setAttribute(player, "fc_practice_jad", true) }
                    .also { stage = END_DIALOGUE }
            } else npc(FacialExpression.NEUTRAL,
                "JalYt, you not have the TokKul.",
                "You come back when you are serious."
            ).also { stage = END_DIALOGUE }

            42 -> player(FacialExpression.HALF_GUILTY,
                "TzTok-Jad must be old and tired",
                "to not just accept my challenge."
                ).also { stage++ }
            43 -> npc(FacialExpression.ANGRY,
                "JalYt-Mor, you the old and tired one.",
                "You the one not want to do proper training."
                ).also { stage = END_DIALOGUE }

            50 -> npc(FacialExpression.NEUTRAL,
                "TzTok-Jad is waiting for you.",
                "Do not make TzTok-Jad wait long."
                ).also { stage = END_DIALOGUE }

            100 -> sendDialogueOptions(player, "Sell your fire cape?", "Yes, sell it for 8,000 TokKul.", "No, keep it.").also { stage++ }
            101 -> when (buttonId) {
                1 -> npcl(FacialExpression.OLD_NORMAL, "Hand your cape here, young JalYte.").also { stage++ }
                2 -> end()
            }
            102 -> end().also { if (removeItem(player, Items.FIRE_CAPE_6570)) addItem(player, Items.TOKKUL_6529, 8000) }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return TzHaarMejJal(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TZHAAR_MEJ_JAL_2617)
    }
}

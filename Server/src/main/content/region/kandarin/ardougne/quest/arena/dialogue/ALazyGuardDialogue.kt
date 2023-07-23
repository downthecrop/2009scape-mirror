package content.region.kandarin.ardougne.quest.arena.dialogue

import content.region.kandarin.ardougne.quest.arena.FightArena
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.Items.KHALI_BREW_77
import org.rs09.consts.Items.KHAZARD_CELL_KEYS_76
import org.rs09.consts.NPCs

class ALazyGuardDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.A_LAZY_KHAZARD_GUARD_8498)
        when (getQuestStage(player!!, FightArena.FightArenaQuest)) {

            in 40..49 -> when (stage) {
                0 -> {
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.NEUTRAL, "Long live General Khazard!").also { stage++ }
                }
                1 -> npcl(FacialExpression.FRIENDLY, "Erm.. yes.. quite right.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "Have you come to laugh at the fight slaves? I used to really enjoy it, but after a while they become quite boring. ").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Now I just want a decent drink. Mind you, too much Khali brew and I'll fall asleep.").also { stage++ }
                4 -> {
                    end()
                    setQuestStage(player!!, FightArena.FightArenaQuest, 50)
                }
            }


            in 50..59 -> when (stage) {
                0 -> if (!allInEquipment(player!!, Items.KHAZARD_HELMET_74, Items.KHAZARD_ARMOUR_75)) {
                    player!!.faceLocation(location(2617, 3144, 0))
                    npcl(FacialExpression.DRUNK, "Go away. Thish areash resh... restricted! We don't like strangersh, 'specially ones who sway. Oooh!").also { stage = END_DIALOGUE }
                } else {
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.NEUTRAL, "Hello, how's the job?").also { stage++ }
                }
                1 -> npcl(FacialExpression.DRUNK, "Please, leave me alone. I'm sure the walls never used to sway that much.").also { stage = END_DIALOGUE }
            }


            in 60..67 -> when (stage) {
                0 -> {
                    setVarbit(player!!, 5627, 0)
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.FRIENDLY, "Hello again.").also { stage++ }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "Bored, bored, bored. You'd think the slaves would be more entertaining? Selfish the lot of them.").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Do you still fancy a drink?").also { stage++ }
                3 -> npcl(FacialExpression.HAPPY, "I really shouldn't... oh... ok then just the one.").also { stage++ }
                4 -> if (player!!.inventory.remove(Item(KHALI_BREW_77))) {
                    sendItemDialogue(player!!, KHALI_BREW_77, "You hand a bottle of Khali brew to the guard. He takes a mouthful of the drink.").also { stage = 5 }
                    setVarbit(player!!, 5627, 1, true) // swap beer to Khali brew
                } else {
                    npcl(FacialExpression.NEUTRAL, "Now I just want a decent drink. Mind you, too much Khali brew and I'll fall asleep.").also { stage = END_DIALOGUE }
                }
                5 -> npcl(FacialExpression.DRUNK, "Blimey this stuff is pretty good. It's not too strong is it?").also { stage++ }
                6 -> playerl(FacialExpression.HALF_GUILTY, "No, not at all. You'll be fine.").also { stage++ }
                7 -> sendDialogue(player!!, "The guard quickly finishes the rest of the bottle and begins to sway slightly.").also { stage++ }
                8 -> npcl(FacialExpression.HAPPY, "That is some gooood stuff... yeah... wooh yeah!").also { stage++ }
                9 -> playerl(FacialExpression.HALF_WORRIED, "Are you alright?").also { stage++ }
                10 -> npcl(FacialExpression.DRUNK, "Yeesshh, hiccup! Oooh, maybe I sshould relax for a while.").also { stage++ }
                11 -> playerl(FacialExpression.NEUTRAL, "Good idea. I'll look after the prisoners.").also { stage++ }
                12 -> npcl(FacialExpression.DRUNK, "Yeesh, yes that shounds reasonable. Here, hiccup', take the keysch. Any trouble, you give... you givem a good beating.").also { stage++ }
                13 -> playerl(FacialExpression.NEUTRAL, "No problem. I'll keep them in line.").also { stage++ }
                14 -> {
                    end()
                    setVarbit(player!!, 5627, 2)
                    setQuestStage(player!!, FightArena.FightArenaQuest, 68)
                }
            }


            in 68..99 -> when (stage) {
                0 -> if (player!!.inventory.containItems(KHAZARD_CELL_KEYS_76)) {
                    setVarbit(player!!, 5627, 0)
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.HALF_ASKING, "Hello, how's the job?").also { stage = 1 }
                } else {
                    setVarbit(player!!, 5627, 0)
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.HALF_GUILTY, "Hi, er.. I lost the keys.").also { stage = 2 }
                }
                1 -> npcl(FacialExpression.DRUNK, "Please, leave me alone. I'm sure the walls never used to sway that much.").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.DRUNK, "What?! You're foolish...").also { stage++ }
                3 -> npcl(FacialExpression.DRUNK, "...and I'm drunk. Here, take my spare set.").also { stage++ }
                4 -> {
                    end()
                    setVarbit(player!!, 5627, 3)
                    player!!.inventory.add(Item(KHAZARD_CELL_KEYS_76))
                    sendMessage(player!!, "You pick up the keys from the table.")
                }
            }

            100 -> when(stage){
                0 ->{
                    setVarbit(player!!, 5627, 0)
                    player!!.faceLocation(location(2617, 3144, 0))
                    playerl(FacialExpression.HALF_ASKING, "Hello, how's the job?").also { stage++ }
                }
                1 -> npcl(FacialExpression.DRUNK, "Please, leave me alone. I'm sure the walls never used to sway that much.").also { stage++ }
                2 -> {
                    end()
                    setVarbit(player!!, 5627, 3)
                }
            }
        }
    }
}
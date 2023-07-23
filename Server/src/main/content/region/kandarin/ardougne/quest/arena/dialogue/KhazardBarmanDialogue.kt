package content.region.kandarin.ardougne.quest.arena.dialogue

import content.region.kandarin.ardougne.quest.arena.FightArena
import core.api.addItem
import core.api.getQuestStage
import core.api.removeItem
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.Items.COINS_995
import org.rs09.consts.NPCs

class KhazardBarmanDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.KHAZARD_BARMAN_259)
        when (getQuestStage(player!!, FightArena.FightArenaQuest)) {
            in 0..49 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.HAPPY, "Hello. I'll have a beer please.").also { stage = 1 }
                    1 -> npcl(FacialExpression.FRIENDLY, "There you go, that's two gold coins.").also { stage = 2 }
                    2 -> if (removeItem(player!!, Item(COINS_995, 2))) {
                        end()
                        addItem(player!!, Items.BEER_1917, 1)
                        stage = END_DIALOGUE
                    } else {
                        end()
                        playerl(FacialExpression.STRUGGLE, "Oh, I don't have enough money with me.").also { stage = END_DIALOGUE }
                    }
                }
            }

            in 50..100 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.HAPPY, "Hello.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hi, what can I get you? We have a range of quality brews.").also { stage++ }
                    2 -> options("I'll have a beer please.", "I'd like a Khali brew please.", "Got any news?").also { stage++ }
                    3 -> when (buttonID) {
                        1 -> playerl(FacialExpression.NEUTRAL, "I'll have a beer please.").also { stage = 4 }
                        2 -> playerl(FacialExpression.NEUTRAL, "I'd like a Khali brew please.").also { stage = 7 }
                        3 -> playerl(FacialExpression.ASKING, "Got any news?").also { stage = 5 }
                    }
                    4 -> npcl(FacialExpression.FRIENDLY, "There you go, that's two gold coins.").also { stage = 8 }
                    5 -> npcl(FacialExpression.NEUTRAL, "If you want to see the action around here you should visit the famous Khazard fight arena. I've seen some grand battles in my time. Ogres, goblins, even dragons have fought there.").also { stage++ }
                    6 -> npcl(FacialExpression.WORRIED, "Although you have to feel sorry for some of the slaves sent in there.").also { stage = END_DIALOGUE }
                    7 -> npcl(FacialExpression.FRIENDLY, "There you go, that's five gold coins. I suggest lying down before you drink it. That way you have less distance to collapse.").also { stage = 9 }
                    8 -> if (removeItem(player!!, Item(COINS_995, 2))){
                        end()
                        addItem(player!!, Items.BEER_1917, 1)
                        stage = END_DIALOGUE
                    } else {
                        end()
                        playerl(FacialExpression.STRUGGLE, "Oh, I don't have enough money with me.").also { stage = END_DIALOGUE }
                    }
                    9 -> if (removeItem(player!!, Item(COINS_995, 5))){
                            end()
                            addItem(player!!, Items.KHALI_BREW_77, 1)
                            setQuestStage(player!!, FightArena.FightArenaQuest, 60)
                            stage = END_DIALOGUE
                    } else {
                        end()
                        playerl(FacialExpression.STRUGGLE, "Oh, I don't have enough money with me.").also { stage = END_DIALOGUE }
                    }
                }
            }
        }
    }
}
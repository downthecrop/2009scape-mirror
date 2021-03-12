package rs09.game.content.quest.free.runemysteries

import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class DukeHoracioRMDialogue(val questStage: Int) : DialogueFile() {

    private val TALISMAN = Item(1438)

    override fun handle(componentID: Int, buttonID: Int) {

        if(questStage == 0){

            when(stage){
                START_DIALOGUE -> npc("Well, it's not really a quest but I recently discovered", "this strange talisman.").also{ stage++ }
                1 -> npc("It seems to be mystical and I have never seen anything", "like it before. Would you take it to the head wizard at").also { stage++ }
                2 -> npc("the Wizards' Tower for me? It's just south-west of here", "and should not take you very long at all. I would be", "awfully grateful.").also { stage++ }
                3 -> options("Sure, no problem.", "Not right now.").also { stage++ }
                4 -> when(buttonID){
                    1 -> player("Sure, no problem.").also { stage = 10 }
                    2 -> player("Not right now.").also { stage = 20 }
                }

                //Sure
                10 -> npc("Thank you very much, stranger. I am sure the head", "wizard will reward you for such an interesting find.").also { stage++ }
                11 -> {
                    interpreter!!.sendDialogue("The Duke hands you an " + Quest.BLUE + "air talisman</col>.").also { stage++ }
                    player!!.questRepository.getQuest("Rune Mysteries").start(player)
                    if (!player!!.inventory.add(TALISMAN)) {
                        GroundItemManager.create(TALISMAN, player!!.location, player)
                    }
                    stage = END_DIALOGUE
                }

                //No thanks
                20 -> npc("As you wish, stranger, although I have this strange", "feeling that it is important. Unfortunately, I cannot", "leave my castle unattended.").also { stage = END_DIALOGUE }
            }

        }

        else if(questStage == 10){

            when(stage){
                START_DIALOGUE -> npc("The only task remotely approaching a quest is the", "delivery of the talisman I gave you to the head wizard", "of the Wizards' Tower,").also { stage++ }
                1 -> npc("south-west of here. I suggest you deliver it to him as", "soon as possible. I have the oddest feeling that is", "important...").also { stage = END_DIALOGUE }
            }

        }

        else if(questStage > 10){

            when(stage){
                START_DIALOGUE -> npc("Nope, I've got everything under control", "in the castle at the moment.").also { stage = END_DIALOGUE }
            }

        }

        else {
            abandonFile()
        }

    }
}
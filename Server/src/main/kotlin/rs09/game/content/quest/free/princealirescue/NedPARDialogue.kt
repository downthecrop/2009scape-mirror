package rs09.game.content.quest.free.princealirescue

import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

private const val STAGE_KNIT_SWEATER = 10
private const val STAGE_KNIT_WIG = 20
private const val STAGE_REPAIR_HOLES = 30

class NedPARDialogue(val questStage: Int) : DialogueFile() {

    private val WIG_WOOL = Item(1759, 3)
    private val WIG = Item(2421)

    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> options(
                "Ned, could you make other things from wool?",
                "Yes, I would like some rope.",
                "No thanks Ned, I don't need any."
            ).also { stage++ }

            1 -> when(buttonID){
                1 -> player("Ned could you make other things from wool?").also { stage++ }
                2 -> {
                    player("Yes, I would like some rope.")
                    returnAtStage(10)
                }
                3 -> player("No thanks Ned, I don't need any.").also { stage = END_DIALOGUE }
            }

            2 -> npc("I am sure I can. What are you thinking of?").also { stage++ }
            3 -> options(
                "Could you knit me a sweater?",
                "How about some sort of wig?",
                "Could you repair the arrow holes in the back of my shirt?"
            ).also { stage++ }

            4 -> when(buttonID){
                1 -> player("Could you knit me a sweater?").also { stage = STAGE_KNIT_SWEATER }
                2 -> player("How about some sort of wig?").also { stage = STAGE_KNIT_WIG }
                3 -> player("Could you repair the arrow holes in the","back of my shirt?").also { stage = STAGE_REPAIR_HOLES }
            }

            //Knit Sweater
            STAGE_KNIT_SWEATER -> npc(
                "Do I look like a member of a sewing circle?",
                "Be off wi' you. I have fought monsters.",
                "that would turn your hair blue."
            ).also { stage++ }
            STAGE_KNIT_SWEATER.substage(1) -> npc(
                "I dont't need to be laughted at just 'cos I am getting",
                "a bit old."
            ).also { stage = END_DIALOGUE }

            //Knit Wig
            STAGE_KNIT_WIG -> npc(
                "Well... That's an interesting thought. Yes, I think I",
                "could do something. Give me 3 balls of wool and I",
                "might be able to do it."
            ).also { stage++ }
            STAGE_KNIT_WIG.substage(1) -> options(
                "I have that now. Please, make me a wig.",
                "I will come back when I need you to make me one."
            ).also { stage++ }
            STAGE_KNIT_WIG.substage(2) -> when(buttonID){
                1 -> player("I have that now. Please, make me a wig.").also { stage++ }
                2 -> player("I will come back when I need you to make me one.").also { stage = END_DIALOGUE }
            }
            STAGE_KNIT_WIG.substage(3) -> {
                if(!player!!.inventory.containsItem(WIG_WOOL)){
                    player("Oh, I seem to have forgotten my wool.")
                    stage = END_DIALOGUE
                } else {
                    npc("Okay, I will have a go.")
                    stage++
                }
            }
            STAGE_KNIT_WIG.substage(4) -> interpreter!!.sendDialogue("You hand Ned 3 balls of wool. Ned works with the wool", "His hands move with a speed you couldn't imagine.").also { stage++ }
            STAGE_KNIT_WIG.substage(5) -> {
                if (player!!.inventory.remove(WIG_WOOL)) {
                    if (!player!!.inventory.add(WIG)) {
                        GroundItemManager.create(WIG, player)
                    }
                }
                npc("Here you go, how's that for a quick effort?", "Not bad I think!")
                stage++
            }
            STAGE_KNIT_WIG.substage(6) -> interpreter!!.sendDialogue("Ned gives you a pretty good wig.").also { stage++ }
            STAGE_KNIT_WIG.substage(7) -> player("Thanks Ned, there's more to you than meets the eye.").also { stage = END_DIALOGUE }

            STAGE_REPAIR_HOLES -> interpreter!!.sendDialogue("Ned pulls out a nettle and attacks your shirt.").also { stage++ }
            STAGE_REPAIR_HOLES.substage(1) -> npc("There you go, good as new.").also { stage = END_DIALOGUE }

        }
    }

}
package rs09.game.content.quest.members.thefremenniktrials

import api.*
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

const val COUNCIL_WORKER = 1287

class CouncilWorkerFTDialogue(val questStage: Int,var isBeerInteraction: Boolean = false) : DialogueFile(){

    override fun handle(componentID: Int, buttonID: Int) {

        if(isBeerInteraction){
            when(stage){
                START_DIALOGUE -> {npc(COUNCIL_WORKER,"Oh, thank you much ${if(player!!.isMale) "sir" else "miss"}");stage++}
                1 -> {
                    npc(COUNCIL_WORKER,"Ta very much like. That'll hit the spot nicely.. Here,","You can have this. I picked it up as a souvenir on me","last holz.")
                    addItem(player!!, Items.STRANGE_OBJECT_3713)
                    removeItem(player!!, Items.BEER_1917, Container.INVENTORY)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage in 1..99){
            when(stage){
                START_DIALOGUE ->
                    if(questStage(player!!, "Fremennik Trials") > 0) {
                        player("I know this is an odd question, but are you","a member of the elder council?"); stage = 1
                    } else {
                        end()
                    }
                1 -> {npc(COUNCIL_WORKER,"'fraid not, ${if(player!!.isMale) "sir" else "miss"}."); stage++}
                2 -> {npc(COUNCIL_WORKER,"Say, would you do me a favor? I'm quite parched.","If you bring me a beer, I'll make it worthwhile.");stage++}
                3 -> if(inInventory(player!!,Items.BEER_1917)) {
                        player("Oh, I have one here! Take it.")
                        stage = 0
                        isBeerInteraction = true
                } else {
                    stage = END_DIALOGUE
                }
            }
        }
    }
}
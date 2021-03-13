package rs09.game.content.quest.members.thefremenniktrials

import core.game.node.item.Item
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class CouncilWorkerFTDialogue(val questStage: Int,var isBeerInteraction: Boolean = false) : DialogueFile(){

    override fun handle(componentID: Int, buttonID: Int) {

        if(isBeerInteraction){
            when(stage){
                START_DIALOGUE -> {npc("Oh, thank you much ${if(player?.appearance?.isMale!!) "sir" else "miss"}");stage++}
                1 -> {npc("Ta very much like. That'll hit the spot nicely.. Here,","You can have this. I picked it up as a souvenir on me","last holz");player?.inventory?.add(
                    Item(3713)
                ); player?.inventory?.remove(Item(1917)); stage = END_DIALOGUE}
            }
        }

        else if(questStage in 1..99){
            when(stage){
                START_DIALOGUE -> if(player?.questRepository?.getStage("Fremennik Trials")!! > 0){
                    player("I know this is an odd question, but are you","a member of the elder council?")
                } else {
                    end()
                }
                1 -> {npc("'fraid not, ${if(player?.appearance?.isMale!!) "sir" else "miss"}"); stage++}
                2 -> {npc("Say, would you do me a favor? I'm quite parched.","If you bring me a beer, I'll make it worthwhile.");stage++}
                3 -> if(player?.inventory?.containsItem(Item(1917))!!){
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
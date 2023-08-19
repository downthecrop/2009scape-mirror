package content.region.kandarin.quest.grandtree

import content.global.travel.glider.Gliders
import core.api.getQuestStage
import core.api.teleport
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.world.map.Location
import core.tools.END_DIALOGUE

class CaptainErrdoDialogue: DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(getQuestStage(player!!, TheGrandTree.questName)){
            55 -> {
                if(player!!.location.regionId == 11567){
                    when(stage){
                        0 -> npcl("Sorry about that.").also { stage++ }
                        1 -> npcl("That turbulence over the Karamja Volcano was a bit unexpected, and the area round here isn't well suited for emergency landing.").also { stage++ }
                        2 -> npcl("Still! we're still alive that's the main thing. Are you okay?").also { stage++ }
                        3 -> playerl("I'm fine, I can't say the same for your glider!").also { stage++ }
                        4 -> npcl("I don't think I can fix this. Looks like we'll be heading back by foot. I might see if I can find Penwie while I'm here, I believe he's charting the area.").also { stage++ }
                        5 -> playerl("Where's the shipyard from here?").also { stage++ }
                        6 -> npcl("I think I saw some buildings on the coast east of here while we were crashing. I'd have a look there.").also { stage++ }
                        7 -> npcl("Take care adventurer!").also { stage++ }
                        8 -> playerl("Take care little man.").also { stage = END_DIALOGUE }
                    }
                } else if (player!!.location.regionId == 9782){
                    when(stage){
                        0 -> npcl("Hi. The king said that you need to leave?").also { stage++ }
                        1 -> playerl("Apparently, humans are invading!").also { stage++ }
                        2 -> npcl("I find that hard to believe. I have lots of human friends.").also { stage++ }
                        3 -> playerl("I don't understand it either!").also { stage++ }
                        4 -> npcl("So, where to?").also { stage++ }
                        5 -> options("Take me to Karamja please!", "Not anywhere for now!").also { stage++ }
                        6 -> when(buttonID){
                            1 -> playerl("Take me to Karamja, please.").also { stage++ }
                            2 -> playerl("Not anywhere for now.").also { stage = 10 }
                        }
                        7 -> npcl("Okay, you're the boss! Hold on tight, it'll be a rough ride.").also {
                            teleport(player!!, Location.create(2917, 3054, 0))
                            stage = END_DIALOGUE
                        }
                        10 -> npcl("Okay. I'll be here for when you're ready.").also { stage = END_DIALOGUE }
                    }
                } else {
                    // We are talking to another gnome outside the stronghold during the quest.
                    when(stage) {
                        0 -> playerl("May you fly me somewhere on your glider?").also { stage++ }
                        1 -> npcl("I only fly friends of the gnomes!").also {
                            stage = END_DIALOGUE
                        }
                    }
                }
            }
            100 -> {
                when(stage){
                    0 -> playerl("May you fly me somewhere on your glider?").also { stage++ }
                    1 -> npcl("If you wish.").also {
                        stage = END_DIALOGUE
                        player!!.interfaceManager.open(Component(138))
                        Gliders.sendConfig(npc, player)

                    }
                }
            }
            else -> {
                when(stage){
                    0 -> playerl("May you fly me somewhere on your glider?").also { stage++ }
                    1 -> npcl("I only fly friends of the gnomes!").also {
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }

}
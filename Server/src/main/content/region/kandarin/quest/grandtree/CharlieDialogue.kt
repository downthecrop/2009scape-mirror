package content.region.kandarin.quest.grandtree

import content.region.kandarin.quest.grandtree.TheGrandTree.Companion.questName
import core.ServerConstants
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class CharlieDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, questName)) {
            46 -> {
                when (stage) {
                    0 -> playerl("Tell me. Why would you want to kill the Grand Tree?").also { stage++ }
                    1 -> npcl("What do you mean?").also { stage++ }
                    2 -> playerl("Don't tell me, you just happened to be caught carrying Daconia rocks!").also { stage++ }
                    3 -> npcl("All I know is that I did what I was asked.").also { stage++ }
                    4 -> playerl("I don't understand.").also { stage++ }
                    5 -> npcl("Glough paid me to go to this gnome on a hill. I gave the gnome a seal and he gave me some Daconia rocks to give to Glough.").also { stage++ }
                    6 -> npcl("I've been doing it for weeks, this time though Glough locked me up here! I just don't understand it.").also { stage++ }
                    7 -> playerl("Sounds like Glough is hiding something.").also { stage++ }
                    8 -> npcl("I don't know what he's up to. If you want to find out, you'd better search his home.").also { stage++ }
                    9 -> playerl("OK. Thanks Charlie.").also { stage++ }
                    10 -> npcl("Good luck!").also {
                        setQuestStage(player!!, questName, 47)
                        stage = END_DIALOGUE
                    }
                }
            }
            47 -> {
                when (stage) {
                    0 -> npcl("Hello adventurer. Have you figured out what's going on?").also { stage++ }
                    1 -> playerl("No idea.").also { stage++ }
                    2 -> npcl("To get to the bottom of this you'll need to search Glough's home.").also { stage = END_DIALOGUE }
                }
            }
            50 -> {
                when(stage) {
                    0 -> npcl("So they got you as well?").also { stage++ }
                    1 -> playerl("It's Glough! He's trying to cover something up.").also { stage++ }
                    2 -> npcl("I shouldn't tell you this adventurer. But if you want to get to the bottom of this you should go and talk to the Karamja Shipyard foreman.").also { stage++ }
                    3 -> playerl("Why?").also { stage++ }
                    4 -> npcl("Glough sent me to Karamja to meet him. I delivered a large amount of gold. For what? I don't know. He may be able to tell you what Glough's up to. That's if you can get out of here. You'll find him").also { stage++ }
                    5 -> npcl("in the Karamja Shipyard, east of Shilo Village. Be careful! If he discovers you're not working for Glough, there'll be trouble! The seamen use the password Ka-Lu-Min.").also { stage++ }
                    6 -> playerl("Thanks, Charlie!").also {
                        stage = END_DIALOGUE
                        GameWorld.Pulser.submit(object : Pulse(0) {
                            var count = 0
                            val npc = NPC.create(NPCs.KING_NARNODE_SHAREEN_670, Location.create(2467, 3496, 3), null)
                            override fun pulse(): Boolean {
                                when (count) {
                                    0 -> {
                                        // Spawn in narnode
                                        npc.init()
                                        lock(player!!,10)
                                        forceWalk(npc, player!!.location.transform(Direction.EAST, 2), "SMART")
                                    }

                                    2 -> {
                                        sendNPCDialogue(player!!, npc.id,"Traveller, please accept my apologies! Glough had no right to arrest you! I just think he's scared of humans. Let me get you out of there.")
                                    }

                                    4 -> {
                                        DoorActionHandler.handleAutowalkDoor(player!!,
                                            getScenery(2465,3496,3)
                                        )
                                        openDialogue(player!!,KingNarnodeUpstairsDialogue(), npc)
                                    }
                                    8 -> {
                                        unlock(player!!)
                                        npc.clear()
                                        setQuestStage(player!!, questName, 55)
                                        return true
                                    }
                                }
                                count++
                                return false
                            }
                        })
                    }
                }
            }
            55 -> {
                if(player!!.hasItem(Item(Items.LUMBER_ORDER_787))){
                    when(stage) {
                        0 -> playerl("How are you doing, Charlie?").also { stage++ }
                        1 -> npcl("I've been better.").also { stage++ }
                        2 -> playerl("Glough has some plan to rule ${ServerConstants.SERVER_NAME}!").also { stage++ }
                        3 -> npcl("I wouldn't put it past him, the gnome's crazy!").also { stage++ }
                        4 -> playerl("I need some proof to convince the King.").also { stage++ }
                        5 -> npcl("Hmm...you could be in luck! Before Glough had me locked up I heard him mention that he'd left his chest keys at his girlfriend's.").also { stage++ }
                        6 -> playerl("Where does she live?").also { stage++ }
                        7 -> npcl("Just west of the toad swamp.").also { stage++ }
                        8 -> playerl("OK, I'll see what I can find.").also {
                            setQuestStage(player!!, questName, 60)
                            stage = END_DIALOGUE
                        }
                    }
                } else {
                    when(stage) {
                        0 -> playerl("I can't figure this out Charlie!").also { stage++ }
                        1 -> npcl("Go and see the foreman in the Karamja jungle, there's a shipyard there, you might find some clues. Don't forget the password is Ka-Lu-Min;").also { stage++ }
                        2 -> npcl("Tf they realise that you're not working for Glough there'll be trouble!").also { stage = END_DIALOGUE }

                    }
                }
            } else -> {
                when(stage){
                    0 -> sendDialogue(player!!, "The prisoner is in no mood to talk.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}

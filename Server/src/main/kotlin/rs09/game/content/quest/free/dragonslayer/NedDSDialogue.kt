package rs09.game.content.quest.free.dragonslayer

import core.game.content.quest.free.dragonslayer.DragonSlayer
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

private const val SHIP_DIALOGUE = 2000
class NedDSDialogue(val questStage: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if(stage >= SHIP_DIALOGUE){
            when(stage){
                SHIP_DIALOGUE -> {
                    if (player!!.getSavedData().questData.getDragonSlayerAttribute("ship")) {
                        player("It's the Lady Lumbridge, in Port Sarim.")
                        stage++
                    } else {
                        player("I'm still looking...")
                        stage = END_DIALOGUE
                    }
                }

                //It's the lady lumbridge
                2001 -> npc("That old pile of junk? Last I heard, she wasn't", "seaworthy.").also { stage++ }
                2002 -> {
                    stage = if (player!!.savedData.questData.getDragonSlayerAttribute("repaired")) {
                        player("I fixed her up!")
                        stage + 1
                    } else {
                        player("Oh, I better go inspect her.")
                        END_DIALOGUE
                    }
                }

                //I fixed her up
                2003 -> npc("You did? Excellent!").also { stage++ }
                2004 -> npc("Just show me the map and we can get ready to go!").also { stage++ }
                2005 -> {
                    if (player!!.inventory.containsItem(DragonSlayer.CRANDOR_MAP)) {
                        player("Here you go.")
                        stage++
                    } else {
                        player!!.packetDispatch.sendMessage("You don't have the map to Crandor.")
                        stage = END_DIALOGUE
                    }
                }

                2006 -> {
                    if (player!!.inventory.remove(DragonSlayer.CRANDOR_MAP)) {
                        interpreter!!.sendItemMessage(DragonSlayer.CRANDOR_MAP.id, "You hand the map to Ned.")
                        player!!.questRepository.getQuest("Dragon Slayer").setStage(player, 30)
                        stage++
                    } else stage = END_DIALOGUE
                }

                2007 -> npc("Excellent! I'll meet you at the ship, then.").also { stage = END_DIALOGUE }
            }
        }

        else if(questStage == 20 && player!!.savedData.questData.getDragonSlayerAttribute("ned")){
            when(stage){
                START_DIALOGUE -> player("Will you take me to Crandor now, then?").also{ stage++ }
                1 -> npc("I Said I would and old Ned is a man of his word!").also { stage++ }
                2 -> npc("So, where's your ship?").also { stage = SHIP_DIALOGUE }
            }
        }

        else if(questStage == 20){
            when(stage){
                START_DIALOGUE -> player("You're a sailor? Could you take me to Crandor?").also { stage++ }
                1 -> npc(
                    "Well, I was a sailor. I've not been able to get work at",
                    "sea these days, though. They say I'm too old."
                ).also { stage++ }

                2 -> npc("Sorry, where was it you said you wanted to go?").also { stage++ }
                3 -> player("To the island of Crandor.").also { stage++ }
                4 -> npc("Crandor? You've got to be out of your mind!").also { stage++ }
                5 -> npc(
                    "But... It would be a chance to sail a ship once more.",
                    "I'd sail anywhere if it was a chance to sail again."
                ).also { stage++ }

                6 -> npc("Then again, no captain in his right mind would sail to", "that island...").also { stage++ }
                7 -> {
                    npc("Ah, you only live once! I'll do it!")
                    player!!.savedData.questData.setDragonSlayerAttribute("ned", true)
                    stage++
                }

                8 -> npc("So, where's your ship?").also { stage = SHIP_DIALOGUE }
            }
        }

        else if(questStage == 30){
            when(stage){
                START_DIALOGUE -> player("So will you take me to Crandor now then?").also { stage++ }
                1 -> npc(
                    "I Said I would and old Ned is a man of his word! I'll",
                    "meet you on board the Lady Lumbridge in Port Sarim."
                ).also { stage = END_DIALOGUE }
            }
        }

    }
}
package rs09.game.content.activity.communityevents

import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.system.config.GroundSpawnLoader

class SuperEggEaster2020 : InteractionListener() {
    override fun defineListeners() {
        val egg = GroundSpawnLoader.GroundSpawn(-1,Item(14652,1),Location.create(2926, 3480, 0)).init()

        onDig(Location.create(2188, 3281, 0)){player ->
            val hasKey = player.getAttribute("easter2020:key",false)
            if(!hasKey){
                player.inventory.add(Item(Items.KEY_11039))
                player.dialogueInterpreter.sendDialogue("You dig and find an ancient key!")
                player.setAttribute("/save:easter2020:key",true)
            } else {
                player.sendMessage("You dig and find nothing.")
            }
        }

        on(14652, ITEM,"unlock"){player, node ->
            val item = node.asItem()

            if(player.inventory.contains(Items.KEY_11039,1)){
                player.dialogueInterpreter.sendDialogue("As you approach the egg you feel a great sense of unease.","You feel as though some ancient force lurks within the egg.","The key in your pocket begins to pull towards the egg...")
                player.dialogueInterpreter.addAction { pl, _ ->
                    if(!pl.getAttribute("2020superegg",false)){
                        pl.dialogueInterpreter.sendDialogue("However...","The egg does not recognize your essence.","You have been rejected.")
                    } else {
                        pl.dialogueInterpreter.sendDialogue("The egg recognizes your essence.","Visions of ancient Easterian knowledge fill your mind.","An ancient Easterian relic lurks within the egg.")
                        pl.dialogueInterpreter.addAction { p, _ ->
                            player.inventory.remove(Item(Items.KEY_11039))
                            player.dialogueInterpreter.sendItemMessage(14651,"You pull from within the egg an ancient weapon.")
                            player.inventory.add(Item(14651))
                        }
                    }
                }

            } else {
                player.dialogueInterpreter.sendDialogue("You approach the egg but you sense that it rejects you.")
            }

            return@on true
        }

    }
}



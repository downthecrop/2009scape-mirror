package rs09.game.interaction.item.withnpc

import api.*
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.path.Path
import core.game.world.map.path.Pathfinder
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.world.GameWorld.Pulser

class GCItemOnCat : InteractionListener() {
    override fun defineListeners() {
        val GERTCAT = "Gertrude's Cat"
        val BEND_DOWN = 827

        onUseWith(NPC, Items.BUCKET_OF_MILK_1927, NPCs.GERTRUDES_CAT_2997) {player, used, with ->
            if(questStage(player, GERTCAT) == 20 && removeItem(player, used.asItem())){
                addItem(player, Items.EMPTY_BUCKET_3727)
                animate(player, BEND_DOWN) //bend down
                sendChat(with.asNpc(), "Mew!")
                setQuestStage(player, GERTCAT, 30)
            }
            return@onUseWith true
        }

        onUseWith(NPC, Items.DOOGLE_SARDINE_1552, NPCs.GERTRUDES_CAT_2997){player, used, with ->
            if(questStage(player, GERTCAT) == 30 && removeItem(player, used.asItem())){
                animate(player, BEND_DOWN)
                sendChat(with.asNpc(), "Mew!")
                setQuestStage(player, GERTCAT, 40)
            }
            return@onUseWith true
        }

        onUseWith(NPC, Items.RAW_SARDINE_327, NPCs.GERTRUDES_CAT_2997){player, _, _ ->
            sendMessage(player, "The cat doesn't seem interested in that.")
            return@onUseWith true
        }

        onUseWith(NPC, Items.THREE_LITTLE_KITTENS_13236, NPCs.GERTRUDES_CAT_2997){player, used, with ->
            if(removeItem(player, used.asItem())){
                setQuestStage(player, GERTCAT, 60)
                //below copied verbatim from original, I don't like it.
                Pulser.submit(object : Pulse(1) {
                    var count = 0
                    val kitten = core.game.node.entity.npc.NPC.create(761, player.location)
                    override fun pulse(): Boolean {
                        when (count) {
                            0 -> {
                                kitten.init()
                                kitten.face(with.asNpc())
                                with.asNpc().face(kitten)
                                with.asNpc().sendChat("Pur...")
                                kitten.sendChat("Pur...")
                                val path: Path = Pathfinder.find(with.asNpc(), Location(3310, 3510, 1))
                                path.walk(with.asNpc())
                                val pathh = Pathfinder.find(kitten, Location(3310, 3510, 1))
                                pathh.walk(kitten)
                            }
                            5 -> {
                                kitten.clear()
                                player.setAttribute("hidefluff", System.currentTimeMillis() + 60000)
                            }
                        }
                        count++
                        return count == 6
                    }
                })
            }

            return@onUseWith true
        }
    }
}
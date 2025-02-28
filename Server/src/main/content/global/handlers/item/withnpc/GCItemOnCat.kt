package content.global.handlers.item.withnpc

import core.api.*
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.path.Path
import core.game.world.map.path.Pathfinder
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.world.GameWorld.Pulser
import content.data.Quests

class GCItemOnCat : InteractionListener {
    override fun defineListeners() {
        val BEND_DOWN = 827

        onUseWith(IntType.NPC, Items.BUCKET_OF_MILK_1927, NPCs.GERTRUDES_CAT_2997) { player, used, with ->
            if(getQuestStage(player, Quests.GERTRUDES_CAT) == 20 && removeItem(player, used.asItem())){
                addItem(player, Items.BUCKET_1925)
                animate(player, BEND_DOWN) //bend down
                sendChat(with.asNpc(), "Mew!")
                setQuestStage(player, Quests.GERTRUDES_CAT, 30)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.DOOGLE_SARDINE_1552, NPCs.GERTRUDES_CAT_2997){ player, used, with ->
            if(getQuestStage(player, Quests.GERTRUDES_CAT) == 30 && removeItem(player, used.asItem())){
                animate(player, BEND_DOWN)
                sendChat(with.asNpc(), "Mew!")
                setQuestStage(player, Quests.GERTRUDES_CAT, 40)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.RAW_SARDINE_327, NPCs.GERTRUDES_CAT_2997){ player, _, _ ->
            sendMessage(player, "The cat doesn't seem interested in that.")
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.THREE_LITTLE_KITTENS_13236, NPCs.GERTRUDES_CAT_2997){ player, used, with ->
            if(removeItem(player, used.asItem())){
                setQuestStage(player, Quests.GERTRUDES_CAT, 60)
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
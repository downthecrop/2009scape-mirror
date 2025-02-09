package content.region.kandarin.witchhaven.quest.seaslug

import content.data.Quests
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class SeaSlugListeners : InteractionListener {

    companion object {

        // The boat travels are animated nicely for you by un-hiding child 10 - 13
        val BOAT_TRAVEL_CHILD = arrayOf(10, 11, 12, 13)
        val BOAT_TRAVEL_TICKS = arrayOf(5, 9, 8, 7)
        val BOAT_TRAVEL_DESTINATION = arrayOf(
                Location(2782, 3273), // From LAND to PLATFORM
                Location(2721, 3304), // From PLATFORM to LAND
                Location(2800, 3320), // From PLATFORM to ISLAND
                Location(2782, 3273), // From ISLAND to PLATFORM
        )
        val BOAT_TRAVEL_DIALOGUE = arrayOf(
                "You arrive at the fishing platform.", // From LAND to PLATFORM
                "The boat arrives at Witchaven.", // From PLATFORM to LAND
                "You arrive on a small island.", // From PLATFORM to ISLAND
                "You arrive at the fishing platform.", // From ISLAND to PLATFORM
        )

        fun seaslugBoatTravel(player: Player, travelIndex: Int) {
            if (travelIndex == 0) {
                // Prevent bringing lit torches.
                while(removeItem(player, Items.LIT_TORCH_594)) {
                    sendMessage(player, "Your torch goes out on the crossing.")
                    addItemOrDrop(player, Items.UNLIT_TORCH_596)
                }
            }
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when(stage){
                    0 -> {
                        closeOverlay(player)
                        openOverlay(player, Components.FADE_TO_BLACK_120)
                        lock(player, 2)
                        return@queueScript delayScript(player, 1)
                    }
                    1 -> {
                        teleport(player, BOAT_TRAVEL_DESTINATION[travelIndex])
                        openOverlay(player, Components.SEASLUG_BOAT_TRAVEL_461)
                        setComponentVisibility(player, Components.SEASLUG_BOAT_TRAVEL_461, BOAT_TRAVEL_CHILD[travelIndex], false)
                        lock(player, BOAT_TRAVEL_TICKS[travelIndex])
                        return@queueScript delayScript(player, BOAT_TRAVEL_TICKS[travelIndex])
                    }
                    2 -> {
                        sendDialogue(player, BOAT_TRAVEL_DIALOGUE[travelIndex])
                        player.interfaceManager.closeOverlay()
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript stopExecuting(player)
            }
        }

    }
    override fun defineListeners() {
        // https://www.youtube.com/watch?v=VR91Rbyuou4
        // Your tinderbox is damp from the sea crossing. It won't light here.
        // Your torch goes out on the crossing.

        onUseWith(IntType.ITEM, Items.SWAMP_TAR_1939, Items.POT_OF_FLOUR_1933){ player, used, with ->
            if(removeItem(player, used) && removeItem(player, with)) {
                sendMessage(player, "You mix the flour with the swamp tar.")
                sendMessage(player, "It mixes into a paste.")
                addItemOrDrop(player, Items.EMPTY_POT_1931)
                addItemOrDrop(player, Items.RAW_SWAMP_PASTE_1940)
            }
            return@onUseWith true
        }

        // You can only cook it using firewood.
        // sendMessage(player, "You can't cook that in a range.")
        onUseWith(SCENERY, Items.RAW_SWAMP_PASTE_1940, Scenery.FIRE_2732) { player, used, with ->
            if(removeItem(player, used)) {
                sendMessage(player, "You warm the paste over the fire. It thickens into a sticky goo.")
                addItemOrDrop(player, Items.SWAMP_PASTE_1941)
            }
            return@onUseWith true
        }


        on(Scenery.LADDER_18324, IntType.SCENERY, "climb-up") { player, _ ->
            if (getQuestStage(player, Quests.SEA_SLUG) in 5..6) {
                if (getQuestStage(player, Quests.SEA_SLUG) == 6 && inInventory(player, Items.LIT_TORCH_594)) {
                    setQuestStage(player, Quests.SEA_SLUG, 7)
                    ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(2784, 3285, 1))
                } else {
                    animate(player, 4785)
                    sendMessage(player, "You attempt to climb up the ladder.")
                    sendMessage(player, "The fisherman approach you...")
                    sendMessage(player, "and smack you on the head with a fishing rod!")
                    sendMessage(player, "Ouch!")
                    sendChat(player, "Ouch!")
                    player.impactHandler.manualHit(player, 4, ImpactHandler.HitsplatType.NORMAL)
                }
            } else {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location(2784, 3285, 1))
            }
            return@on true
        }
        on(Scenery.LADDER_18325, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, Location(2784, 3287, 0))
            return@on true
        }

        on(Scenery.BADLY_REPAIRED_WALL_18381, IntType.SCENERY, "kick") { player, _ ->
            if(getQuestStage(player, Quests.SEA_SLUG) == 8) {
                animate(player, 4804)
                sendMessage(player, "You kick the loose panel.")
                sendMessage(player, "The wood is rotted and crumbles away...")
                sendMessage(player, "... leaving an opening big enough for Kennith to climb through.")
                setQuestStage(player, Quests.SEA_SLUG, 9)
            } else {
                // https://youtu.be/OM-akv7oIZ0 2:41
                sendMessage(player, "You kick the loose panel...")
                sendMessage(player, "... but nothing interesting happens.")
            }
            return@on true
        }

        on(Scenery.CRANE_18327, IntType.SCENERY, "rotate") { player, node ->
            if(getQuestStage(player, Quests.SEA_SLUG) == 10) {
                // This is supposed to be a cutscene, but goddamn do I hate programming cutscenes.
                lock(player, 6)
                player.dialogueInterpreter.sendPlainMessage(true, "Kennith scrambles through the broken wall...")
                replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CRANE_18326, 6)
                animateScenery(node as core.game.node.scenery.Scenery, 4798)
                setQuestStage(player, Quests.SEA_SLUG, 11)
                queueScript(player, 6, QueueStrength.SOFT) { stage: Int ->
                    sendDialogue(player, "Down below, you see Holgart collect the boy from the crane and lead him away to safety.")
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "You rotate the crane around.")
                animateScenery(node as core.game.node.scenery.Scenery, 4796)
            }
            return@on true
        }

        onUseWith(IntType.ITEM, Items.DAMP_STICKS_1467, Items.BROKEN_GLASS_1469){ player, used, with ->
            if(removeItem(player, used)) {
                visualize(player, 4809, 791)
                addItemOrDrop(player, Items.DRY_STICKS_1468)
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.DRY_STICKS_1468, Items.UNLIT_TORCH_596){ player, bolt, tip ->
            addItemOrDrop(player, Items.LIT_TORCH_594)
            return@onUseWith true
        }

        on(Items.DRY_STICKS_1468, ITEM, "rub-together") { player, _ ->
            sendMessage(player, "You rub together the dry sticks and the sticks catch alight.")
            if(removeItem(player, Items.UNLIT_TORCH_596)) {
                sendMessage(player, "You place the smouldering twigs to your torch.")
                sendMessage(player, "Your torch lights.")
                addItemOrDrop(player, Items.LIT_TORCH_594)
            }
            return@on true
        }


        on(NPCs.SEA_SLUG_1006, IntType.NPC, "take") { player, _ ->
            sendMessage(player, "You pick up the sea slug.")
            sendMessage(player, "It sinks its teeth deep into your hand.")
            sendMessage(player, "You drop the sea slug.")
            sendChat(player, "Ouch!")
            impact(player, 3, HitsplatType.NORMAL)
            return@on true
        }

    }


}
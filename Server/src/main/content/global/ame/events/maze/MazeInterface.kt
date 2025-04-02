package content.global.ame.events.maze

import content.global.ame.returnPlayer
import core.api.*
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.*

class MazeInterface : InteractionListener, EventHook<TickEvent>, MapArea {
    companion object {
        const val MAZE_TIMER_INTERFACE = Components.MAZETIMER_209
        const val MAZE_TIMER_VARP = 531 // Interface 209 child 2 config: [531, 0]
        const val MAZE_ATTRIBUTE_TICKS_LEFT = "maze:percent-ticks-left"
        const val MAZE_ATTRIBUTE_CHESTS_OPEN = "/save:maze:chests-opened"

        val STARTING_POINTS = arrayOf(
                Location(2928, 4553, 0),
                Location(2917, 4553, 0),
                Location(2908, 4555, 0),
                Location(2891, 4589, 0),
                Location(2891, 4595, 0),
                Location(2891, 4595, 0),
                Location(2926, 4597, 0),
                Location(2931, 4597, 0),
                // There's 2 more, but there isn't a door for them...
        )

        val REWARD_ITEM = intArrayOf(
                Items.COINS_995,
                Items.FEATHER_314,
                Items.IRON_ARROW_884,
                Items.CHAOS_RUNE_562,
                Items.STEEL_ARROW_886,
                Items.DEATH_RUNE_560,
                Items.COAL_454,
                Items.NATURE_RUNE_561,
                Items.MITHRIL_ORE_448
        )

        val ITEM_DIVISOR = arrayOf(
                1.0,
                2.0,
                3.0,
                9.0,
                12.0,
                18.0,
                45.0,
                162.0,
                180.0,
        )

        val CHEST_REWARDS = WeightBasedTable.create(
                WeightedItem(Items.AIR_RUNE_556,15,15,1.0),
                WeightedItem(Items.WATER_RUNE_555,10,10,1.0),
                WeightedItem(Items.EARTH_RUNE_557,10,10,1.0),
                WeightedItem(Items.FIRE_RUNE_554,10,10,1.0),
                WeightedItem(Items.BRONZE_ARROW_882,20,20,1.0),
                WeightedItem(Items.BRONZE_BOLTS_877,10,10,1.0),
                WeightedItem(Items.IRON_ARROW_884,15,15,1.0),
                WeightedItem(Items.ATTACK_POTION2_123,1,1,1.0),
                WeightedItem(Items.STRENGTH_POTION2_117,1,1,1.0),
                WeightedItem(Items.DEFENCE_POTION2_135,1,1,1.0),
        )

        fun initMaze(player: Player) {
            setAttribute(player, MAZE_ATTRIBUTE_TICKS_LEFT, 300)
            setVarp(player, MAZE_TIMER_VARP, (getAttribute<Int>(player, MAZE_ATTRIBUTE_TICKS_LEFT, 0) / 3),false)
            openOverlay(player, MAZE_TIMER_INTERFACE)
            sendMessage(player, "You need to reach the maze center, then you'll be returned to where you were.")
            sendNPCDialogue(player, NPCs.MYSTERIOUS_OLD_MAN_410, "You need to reach the maze center, then you'll be returned to where you were.")
        }

        fun calculateLoot(player: Player) {
            val randomNumber = (0..8).random()
            val totalLevel = player.getSkills().totalLevel.toDouble()
            val rewardPotential = getAttribute(player, MAZE_ATTRIBUTE_TICKS_LEFT, 0).toDouble() / 300.0
            val itemDivisor = ITEM_DIVISOR[randomNumber]
            val itemQuantity = (totalLevel * rewardPotential * 3.33) / itemDivisor
            // sendMessage(player, "Maze reward calculation: $totalLevel * $rewardPotential * 3.33 / $itemDivisor = $itemQuantity")
            if (itemQuantity.toInt() > 0) {
                addItemOrDrop(player, REWARD_ITEM[randomNumber], itemQuantity.toInt())
            }
        }

        /**
         * Chest Location to rotation mapping.
         * This is needed as it is impossible to obtain the underlying chest scenery for the rotation.
         * 0: Facing North, 1: Facing East, 2: Facing South, 3: Facing West
         */
        val chestLocationRotationMap = mapOf(
                Location(2930, 4595, 0).toString() to 2,
                Location(2924, 4572, 0).toString() to 2,
                Location(2925, 4573, 0).toString() to 0,
                Location(2900, 4578, 0).toString() to 2,
                Location(2901, 4560, 0).toString() to 1,
                Location(2890, 4599, 0).toString() to 2,
                Location(2896, 4591, 0).toString() to 2,
                Location(2895, 4592, 0).toString() to 1,
                Location(2901, 4560, 0).toString() to 3,
                Location(2918, 4590, 0).toString() to 1,
                Location(2917, 4590, 0).toString() to 3,
        )

        /**
         * Chest Interaction workaround
         *
         * The issue here is that the walls(3626) of the Maze are overlapping some(not all) chest sceneries.
         *
         * The types for the wallScenery:
         * Type 0 - flat panel |
         * Type 2 - right angle panel with rotation 0:r 1:7 2:> 3:L
         * Type 3 - corner post . for angle edges of walls
         */
        fun overrideScenery(wallScenery: core.game.node.scenery.Scenery, chestSceneryId: Int): core.game.node.scenery.Scenery {
            if (wallScenery.id == chestSceneryId) {
                replaceScenery(wallScenery, Scenery.CHEST_3636, 30)
                wallScenery.isActive = true
                return wallScenery // Return the chest scenery as the wallScenery isn't there.
            }

            addScenery(Scenery.CHEST_3636, wallScenery.location, chestLocationRotationMap[wallScenery.location.toString()] ?: 0, 10)
            addScenery(wallScenery)
            // replaceScenery(newChestScenery, Scenery.CHEST_3636, 3) // didn't work for an underlying scenery
            // I did a world pulse since everyone will get to see the chest open.
            Pulser.submit(object : Pulse(30) {
                override fun pulse(): Boolean {
                    addScenery(Scenery.CHEST_3635, wallScenery.location, chestLocationRotationMap[wallScenery.location.toString()] ?: 0, 10)
                    addScenery(wallScenery)
                    return true
                }
            })
            // Return the chest scenery to replace PacketProcessor so that MISMATCH will not happen.
            return core.game.node.scenery.Scenery(
                    chestSceneryId,
                    wallScenery.location,
                    chestLocationRotationMap[wallScenery.location.toString()] ?: 0
            )
        }
    }

    override fun defineListeners() {

        // This somehow doesn't trigger as the scenery.id != objId (3626 != 3635)
        on(Scenery.CHEST_3635, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, MAZE_ATTRIBUTE_TICKS_LEFT, 0) > 0 && getAttribute(player, MAZE_ATTRIBUTE_CHESTS_OPEN, 0) < 10) {
                animate(player, 536)
    //          val actualScenery = RegionManager.getObject(node.location.z, node.location.x, node.location.y, 3626)
                val tableRoll = CHEST_REWARDS.roll()
                addItemOrBank(player, tableRoll[0].id)
                when (tableRoll[0].id){
                    Items.AIR_RUNE_556 -> sendItemDialogue(player, Items.AIR_RUNE_556, "You've found some air runes!")
                    Items.WATER_RUNE_555 -> sendItemDialogue(player, Items.WATER_RUNE_555, "You've found some water runes!")
                    Items.EARTH_RUNE_557 -> sendItemDialogue(player, Items.EARTH_RUNE_557, "You've found some earth runes!")
                    Items.FIRE_RUNE_554 -> sendItemDialogue(player, Items.FIRE_RUNE_554, "You've found some fire runes!")
                    Items.BRONZE_ARROW_882 -> sendItemDialogue(player, Items.BRONZE_ARROW_882, "You've found some bronze arrows!")
                    Items.BRONZE_BOLTS_877 -> sendItemDialogue(player, Items.BRONZE_BOLTS_877, "You've found some bronze bolts!")
                    Items.IRON_ARROW_884 -> sendItemDialogue(player, Items.IRON_ARROW_884, "You've found some iron arrows!")
                    Items.ATTACK_POTION2_123 -> sendItemDialogue(player, Items.ATTACK_POTION2_123, "You've found an attack potion!")
                    Items.STRENGTH_POTION2_117 -> sendItemDialogue(player, Items.STRENGTH_POTION2_117, "You've found a strength potion!")
                    Items.DEFENCE_POTION2_135 -> sendItemDialogue(player, Items.DEFENCE_POTION2_135, "You've found a defence potion!")
                }
                setAttribute(player, MAZE_ATTRIBUTE_CHESTS_OPEN, getAttribute(player, MAZE_ATTRIBUTE_CHESTS_OPEN, 0))
            } else {
                sendMessage(player,"You find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.CHEST_3636, SCENERY, "search") { player, node ->
            sendMessage(player,"You find nothing of interest.")
            return@on true
        }

        on(Scenery.WALL_3626, IntType.SCENERY, "open") { player, node ->
            sendMessage(player, "That bit doesn't open.") // 0xBrLo9woIY
            return@on true
        }

        on(Scenery.WALL_3628, IntType.SCENERY, "open") { player, node ->
            // Door opening workaround
            // Ignore 3629(WALL_3629) and 3630(WALL_3630) in handleAutowalkDoor ignoreSecondDoor
            DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            return@on true
        }

        on(Scenery.STRANGE_SHRINE_3634, IntType.SCENERY, "touch") { player, node ->
            player.unhook(this)
            lock(player, 12)
            closeOverlay(player)
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendGraphics(Graphics(86, 0, 3), player.location)
                        animate(player,862)
                        return@queueScript delayScript(player, 6)
                    }
                    1 -> {
                        sendGraphics(Graphics(1576, 0, 0), player.location)
                        animate(player,8939)
                        playAudio(player, Sounds.TELEPORT_ALL_200)
                        return@queueScript delayScript(player, 3)
                    }
                    2 -> {
                        returnPlayer(player)
                        sendGraphics(Graphics(1577, 0, 0), player.location)
                        animate(player,8941)
                        closeOverlay(player)
                        return@queueScript delayScript(player, 1)
                    }
                    3 -> {
                        calculateLoot(player)
                        removeAttribute(player, MAZE_ATTRIBUTE_TICKS_LEFT)
                        removeAttribute(player, MAZE_ATTRIBUTE_CHESTS_OPEN)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }
    }

    override fun process(entity: Entity, event: TickEvent) {
        if (entity is Player) {
            if (getAttribute(entity, MAZE_ATTRIBUTE_TICKS_LEFT, 0) > 0) {
                setAttribute(entity, MAZE_ATTRIBUTE_TICKS_LEFT, getAttribute(entity, MAZE_ATTRIBUTE_TICKS_LEFT, 0) - 1)
            }
            setVarp(entity, MAZE_TIMER_VARP, (getAttribute(entity, MAZE_ATTRIBUTE_TICKS_LEFT, 0) / 3), false)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(11591))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT, ZoneRestriction.OFF_MAP)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            sendMessage(entity, "Head for the center of the maze.")
            entity.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 12)
            openOverlay(entity, MAZE_TIMER_INTERFACE)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.interfaceManager.restoreTabs()
            closeOverlay(entity)
            entity.unhook(this)
        }
    }
    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            entity.hook(Event.Tick, this)
        }
    }

}

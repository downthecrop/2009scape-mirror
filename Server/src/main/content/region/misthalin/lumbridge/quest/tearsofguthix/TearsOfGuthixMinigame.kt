package content.region.misthalin.lumbridge.quest.tearsofguthix

import content.data.Quests
import core.api.*
import core.game.component.Component
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.Scenery

/**
 * // ::setvarbit 454 (varp 449 7-11) 0-10 where its just time bar length
 * // ::setvarbit 455 (varp 449 12-20) X where x is number of points he gets
 *
 * anim 2040 - Hold bowl
 * anim 2041 - Walk with ToG Bowl
 * anim 2042 - Run with ToG Bowl
 * anim 2043 - Fill ToG bowl
 * anim 2044 - Finish filling from ToG
 * anim 2045 - Drink from bowl
 *
 */
class TearsOfGuthixMinigame : InteractionListener, EventHook<TickEvent>, MapArea  {
    companion object {

        const val varbitTimeBar = 454
        const val varbitPoints = 455
        const val attributeTicksRemaining = "minigame:tearsofguthix-ticksremaining"
        const val attributeTearsCollected = "minigame:tearsofguthix-tearscollected"
        const val attributeIsCollecting = "minigame:tearsofguthix-iscollecting"

        // In order specified by RS
        private val rewardArray = arrayOf(
                Skills.COOKING,
                Skills.CRAFTING,
                Skills.FIREMAKING,
                Skills.FISHING,
                Skills.MAGIC,
                Skills.MINING,
                Skills.PRAYER,
                Skills.RANGE,
                Skills.RUNECRAFTING,
                Skills.SMITHING,
                Skills.WOODCUTTING,
                Skills.AGILITY,
                Skills.HERBLORE,
                Skills.FLETCHING,
                Skills.THIEVING,
                Skills.SLAYER,
                Skills.ATTACK,
                Skills.DEFENCE,
                Skills.STRENGTH,
                Skills.HITPOINTS,
                Skills.FARMING,
                Skills.CONSTRUCTION,
                Skills.HUNTER,
                Skills.SUMMONING, // This isn't but lmao.
        )
        // In order specified by RS
        val rewardText = arrayOf(
                "You have a brief urge to cook some food.",
                "Your fingers feel nimble and suited to delicate work.",
                "You have a brief urge to set light to something.",
                "You gain a deep understanding of the creatures of the sea.",
                "You feel the power of the runes surging through you. ",
                "You gain a deep understanding of the stones of the earth.",
                "You suddenly feel very close to the gods.",
                "Your aim improves.",
                "You gain a deep understanding of runes.",
                "You gain a deep understanding of all types of metal.",
                "You gain a deep understanding of the trees in the wood.",
                "You feel very nimble.",
                "You gain a deep understanding of all kinds of strange plants.",
                "You gain a deep understanding of wooden sticks.",
                "You feel your respect for others' property slipping away.",
                "You gain a deep understanding of many strange creatures.",
                "You feel a brief surge of aggression.",
                "You feel more able to defend yourself.",
                "Your muscles bulge.",
                "You feel very healthy.",
                "You gain a deep understanding of the cycles of nature.",
                "You feel homesick.",
                "You briefly experience the joy of the hunt.",
                "You feel at one with nature.",
        )

        /** Calculates the XP to reward. */
        fun rewardTears(player: Player) {
            val lowestSkill = rewardArray.reduce{ acc, curr ->
                // If you don't have construction, you cannot earn xp on it.
                if (curr == Skills.CONSTRUCTION && !hasHouse(player)) {
                    acc
                }
                // If you don't have Druidic Ritual completed, you cannot earn xp on it.
                else if (curr == Skills.HERBLORE && !isQuestComplete(player, Quests.DRUIDIC_RITUAL)) {
                    acc
                }
                // If you don't have Rune Mysteries, you cannot earn xp on it.
                else if (curr == Skills.RUNECRAFTING && !isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    acc
                }
                // If you don't have Wolf Whistle, you cannot earn xp on it.
                else if (curr == Skills.SUMMONING && !isQuestComplete(player, Quests.WOLF_WHISTLE)) {
                    acc
                }
                else if (player.skills.getExperience(acc) <= player.skills.getExperience(curr)) {
                    acc
                } else {
                    curr
                }
            }

            var perTearXP = 60.0 // Caps at level 30, giving 60 per XP.
            if (getStatLevel(player, lowestSkill) < 30) {
                perTearXP = (getStatLevel(player, lowestSkill) - 1) * 1.724137 // From 50/29
                perTearXP += 10
            }

            sendMessage(player, rewardText[rewardArray.indexOf(lowestSkill)])

            val tearsCollected = getAttribute(player, attributeTearsCollected, 0)
            rewardXP(player, lowestSkill, perTearXP * tearsCollected)
        }

        /** Opens interface, walks the player to the center and start game. */
        fun startGame(player: Player) {
            lock(player, 15)
            // Opens the Tears of Guthix Interface in the tab.
            player.interfaceManager.openSingleTab(Component(Components.TOG_WATER_BOWL_4))
            // Sets up the interface varbits.
            setAttribute(player, attributeTicksRemaining, getQuestPoints(player) + 15) // 15 to offset the stupid walking.
            setAttribute(player, attributeTearsCollected, 0)
            setVarbit(player, varbitTimeBar, 10)
            setVarbit(player,varbitPoints, 0)

            // Forces the player to hold a bowl and animate accordingly.
            replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.STONE_BOWL_4704), null, Container.EQUIPMENT)
            // Change the player's SET ANIMATIONS to the bowl holding set. Found using ::ranim
            player.appearance.setAnimations(Animation(357)) // THIS WAS TRIAL AND ERROR AND WAS FUCKING HARD TO FIND
            player.appearance.sync()

            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        val distance = player.location.getDistance(Location(3251, 9516, 2)).toInt() + 1// Per tick?
                        forceMove(player, player.location, Location(3251, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    1 -> {
                        face(player, Location(3252, 9516, 2))
                        val junaScenery = getScenery(Location(3252, 9516, 2))
                        if (junaScenery != null) {
                            animateScenery(junaScenery, 2055)
                        }
                        return@queueScript delayScript(player, 2)
                    }
                    2 -> {
                        val distance = player.location.getDistance(Location(3253, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    3 -> {
                        face(player, Location(3253, 9517, 2))
                        return@queueScript delayScript(player, 2)
                    }
                    4 -> {
                        val distance = player.location.getDistance(Location(3253, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    5 -> {
                        face(player, Location(3257, 9517, 2))
                        return@queueScript delayScript(player, 2)
                    }
                    6 -> {
                        val distance = player.location.getDistance(Location(3257, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3257, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    7 -> {
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        fun endGame(player: Player) {
            lock(player, 22)
            queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "Your time in the cave is up.")
                        val distance = player.location.getDistance(Location(3253, 9517, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9517, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    1 -> {
                        face(player, Location(3253, 9516, 2))
                        return@queueScript delayScript(player, 2)
                    }
                    2 -> {
                        val distance = player.location.getDistance(Location(3253, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3253, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    3 -> {
                        face(player, Location(3251, 9516, 2))
                        val junaScenery = getScenery(Location(3252, 9516, 2))
                        if (junaScenery != null) {
                            animateScenery(junaScenery, 2055)
                        }
                        return@queueScript delayScript(player, 2)
                    }
                    4 -> {
                        val distance = player.location.getDistance(Location(3251, 9516, 2)).toInt() + 1 // Per tick?
                        forceMove(player, player.location, Location(3251, 9516, 2), 0, distance * 15, null, 2041)
                        return@queueScript delayScript(player, distance)
                    }
                    5 -> {
                        sendMessage(player, "You drink the liquid...")
                        animate(player, 2045)
                        return@queueScript delayScript(player, 3)
                    }
                    6 -> {
                        rewardTears(player)
                        setAttribute(player, TearsOfGuthix.attributePreviousDate, System.currentTimeMillis())
                        setAttribute(player, TearsOfGuthix.attributePreviousXPAmount, player.skills.totalXp)
                        setAttribute(player, TearsOfGuthix.attributePreviousQuestPoints, getQuestPoints(player))
                        removeAttribute(player, attributeTearsCollected)
                        if (player.interfaceManager.singleTab?.id == 4) {
                            player.interfaceManager.closeSingleTab()
                        }
                        player.interfaceManager.restoreTabs()
                        removeItem(player, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

    }

    override fun defineListeners() {

        on(Scenery.WEEPING_WALL_6660, SCENERY, "collect-from") { player, node ->
            animate(player, 2043)
            val index = TearsOfGuthixGlobalTick.allWalls.indexOf(node.location)
            setAttribute(player, attributeIsCollecting, index)
            return@on true
        }

    }

    // Timer step per tick while you are in the minigame.
    override fun process(entity: Entity, event: TickEvent) {
        if (entity is Player) {
            if (getAttribute(entity, attributeTicksRemaining, -1) > 0) {
                setAttribute(entity, attributeTicksRemaining, getAttribute(entity, attributeTicksRemaining, 0) - 1)
                setVarbit(entity, varbitTimeBar, (getAttribute(entity, attributeTicksRemaining, 0) * 10 / getQuestPoints(entity)), false)
                if (getAttribute(entity, attributeIsCollecting, 0) != 0) {
                    val currentArrayIndex = getAttribute(entity, attributeIsCollecting, 0)
                    val currentTearState = TearsOfGuthixGlobalTick.globalWallState[currentArrayIndex]
                    if (currentTearState == 1) {
                        setAttribute(entity, attributeTearsCollected, getAttribute(entity, attributeTearsCollected, 0) + 1)
                    } else if (currentTearState == 2 && getAttribute(entity, attributeTearsCollected, 0) > 0){
                        setAttribute(entity, attributeTearsCollected, getAttribute(entity, attributeTearsCollected, 0) - 1)
                    }
                    setVarbit(entity, varbitPoints, getAttribute(entity, attributeTearsCollected, 0))
                }
            } else if (getAttribute(entity, attributeTicksRemaining, -1) == 0) {
                removeAttribute(entity, attributeTicksRemaining)
                endGame(entity)
            }
        }
    }


    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(3253, 9513, 3262, 9522, 2))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            if (getAttribute(entity, attributeTicksRemaining, 0) <= 0) {
                removeItem(entity, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                teleport(entity, Location(3251, 9516, 2))
            } else {
                entity.hook(Event.Tick, this)
            }
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.unhook(this)
            if (logout) {
                removeItem(entity, Items.STONE_BOWL_4704, Container.EQUIPMENT)
                removeAttribute(entity, attributeTearsCollected)
                removeAttribute(entity, attributeTicksRemaining)
                teleport(entity, Location(3251, 9516, 2))
            }
        }
    }
    override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
        if (entity is Player) {
            entity.hook(Event.Tick, this)
            setAttribute(entity, attributeIsCollecting, 0) // If you move, you ain't collecting
        }
    }
}

/**
 * Global Tick class to randomize the walls consistently for everyone.
 */
class TearsOfGuthixGlobalTick : TickListener {

    companion object {
        var ticks = 0
        var globalWallState = intArrayOf(0, 0, 2, 1, 2, 1, 0, 0, 2, 1)
        val allWalls = arrayOf(
                // Blank
                Location(0, 0, 0),
                // Left Walls
                Location(3258, 9520, 2),
                Location(3261, 9516, 2),
                Location(3261, 9518, 2),
                Location(3257, 9514, 2),
                Location(3259, 9514, 2),
                // Right Walls
                Location(3257, 9520, 2),
                Location(3259, 9520, 2),
                Location(3261, 9517, 2),
                Location(3258, 9514, 2),
        )
    }

    override fun tick() {
        // Do this every 10 ticks.
        if (ticks++ > 10) { ticks = 0 } else { return }
        // Shuffle the walls
        val wallStates = intArrayOf(0, 0, 0, 1, 1, 1, 2, 2, 2) // 0 is absent, 1 is blue, 2 is green
        wallStates.shuffle()
        globalWallState = intArrayOf(0) + wallStates

        /*
         * Explanation: The walls are layered sceneries, which makes it rabidly fucked to change them.
         * What I did was to add the tears scenery first (essentially overriding the tears scenery),
         * then add the WEEPING_WALL_6660 right after it so that the interactions are still there.
         * this is how a layer is like:
         * 1 - WEEPING_WALL_6660 - No model, but holds the option "collect-from"
         * 2 - BLUE/GREEN/ABSENT - Model of the blue/green/absent waterfall.
         * 3 - WEEPING_WALL_6664 - The actual model, but not interactive.
         * 6661 - 6664 is left side, 6665 to 6668 is right side
         */
        wallStates.forEachIndexed { index, state ->
            val scenery = getScenery(allWalls[index + 1])!!
            val newSceneryId = if (state == 2) {
                if (index + 1 <= 5) {
                    Scenery.GREEN_TEARS_6662
                } else {
                    Scenery.GREEN_TEARS_6666
                }
            } else if (state == 1) {
                if (index + 1 <= 5) {
                    Scenery.BLUE_TEARS_6661
                } else {
                    Scenery.BLUE_TEARS_6665
                }
            } else {
                if (index + 1 <= 5) {
                    Scenery.ABSENCE_OF_TEARS_6663
                } else {
                    Scenery.ABSENCE_OF_TEARS_6667
                }
            }
            addScenery(core.game.node.scenery.Scenery(
                    newSceneryId,
                    scenery.location,
                    4,
                    scenery.rotation
            ))
            addScenery(core.game.node.scenery.Scenery(Scenery.WEEPING_WALL_6660, scenery.location, 0, scenery.rotation))
        }
    }
}
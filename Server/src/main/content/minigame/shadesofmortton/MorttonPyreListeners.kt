package content.minigame.shadesofmortton

import content.data.Quests
import content.global.handlers.npc.DecantListener
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.*

/**
 * Enum defining all the data for pyre logs.
 */
enum class PyreLog(
    val logId: Int,
    val pyreLogId: Int,
    val oilDoseReq: Int,
    val makeXp: Double,
    val levelReq: Int,
    val fmXp: Double,
    val prayerXp: DoubleArray // loar, phrin, riyl, asyn, fyir (-1.0 if log is incompatible)
) {
    NORMAL(Items.LOGS_1511, Items.PYRE_LOGS_3438, 2, 10.0, 5, 50.0, doubleArrayOf(25.0, 37.5, -1.0, -1.0, -1.0)),
    OAK(Items.OAK_LOGS_1521, Items.OAK_PYRE_LOGS_3440, 2, 10.0, 20, 70.0, doubleArrayOf(33.0, 45.5, -1.0, -1.0, -1.0)),
    WILLOW(Items.WILLOW_LOGS_1519, Items.WILLOW_PYRE_LOGS_3442, 3, 15.0, 35, 100.0, doubleArrayOf(33.5, 46.0, 61.0, -1.0, -1.0)),
    TEAK(Items.TEAK_LOGS_6333, Items.TEAK_PYRE_LOGS_6211, 3, 15.0, 40, 120.0, doubleArrayOf(33.7, 46.2, 61.2, -1.0, -1.0)),
    ARCTIC_PINE(Items.ARCTIC_PINE_LOGS_10810, Items.ARCTIC_PYRE_LOGS_10808, 2, 10.0, 47, 158.0, doubleArrayOf(33.9, 46.4, 61.4, -1.0, -1.0)),
    MAPLE(Items.MAPLE_LOGS_1517, Items.MAPLE_PYRE_LOGS_3444, 3, 15.0, 50, 175.0, doubleArrayOf(34.0, 46.5, 61.5, -1.0, -1.0)),
    MAHOGANY(Items.MAHOGANY_LOGS_6332, Items.MAHOGANY_PYRE_LOG_6213, 3, 15.0, 55, 210.0, doubleArrayOf(34.3, 46.8, 61.8, -1.0, -1.0)),
    EUCALYPTUS(Items.EUCALYPTUS_LOGS_12581, Items.EUCALYPTUS_PYRE_LOGS_12583, 3, 15.0, 63, 246.5, doubleArrayOf(34.4, 46.9, 61.9, 79.4, -1.0)),
    YEW(Items.YEW_LOGS_1515, Items.YEW_PYRE_LOGS_3446, 4, 20.0, 65, 255.0, doubleArrayOf(34.5, 47.0, 62.0, 79.5, -1.0)),
    MAGIC(Items.MAGIC_LOGS_1513, Items.MAGIC_PYRE_LOGS_3448, 4, 20.0, 80, 404.5, doubleArrayOf(35.0, 47.5, 62.5, 80.0, 100.0));

    companion object {
        fun forLog(id: Int): PyreLog? = values().find { it.logId == id }
        fun forPyreLog(id: Int): PyreLog? = values().find { it.pyreLogId == id }
    }
}

/**
 * Listeners related to burning the Mort'ton shades and accessing the shade catacombs.
 */

class MorttonPyreListeners : InteractionListener {

    override fun defineListeners() {

        // --------------------------------------------
        // MAKING PYRE LOGS
        // --------------------------------------------

        // using sacred oil on various types of regular logs
        val allLogs = PyreLog.values().map { it.logId }.toIntArray()
        onUseWith(IntType.ITEM, intArrayOf(Items.SACRED_OIL4_3430, Items.SACRED_OIL3_3432, Items.SACRED_OIL2_3434, Items.SACRED_OIL1_3436), *allLogs) { player, used, with ->
            val oil = used as Item
            val logs = with as Item

            val pyreLog = PyreLog.forLog(logs.id) ?: return@onUseWith false
            createPyreLog(player, oil, logs, pyreLog.pyreLogId, pyreLog.oilDoseReq, pyreLog.makeXp)
            return@onUseWith true
        }

        // --------------------------------------------
        // PYRES: https://youtu.be/WbfkudIt2-s?si=Bbb7R2nGraEmQCS9&t=489
        // --------------------------------------------

        // using logs on the pyre
        for (pyreLog in PyreLog.values()) {
            onUseWith(IntType.SCENERY, pyreLog.pyreLogId, Scenery.FUNERAL_PYRE_4093) { player, used, with ->

                // get log data for the pyre
                val logData = PyreLog.forPyreLog(used.id) ?: return@onUseWith true

                // level req check
                if (getDynLevel(player, Skills.FIREMAKING) < logData.levelReq) {
                    sendMessage(player, "You need a Firemaking level of ${logData.levelReq} to light this pyre.")
                    return@onUseWith true
                }

                // check if the player already has one pyre
                if (getAttribute<Location?>(player, ATTR_PYRE_LOC, null) == null) {

                    // record this pyre's location
                    setAttribute(player, ATTR_PYRE_LOC, with.location)

                    // remove pyre log and place logs on the pyre
                    if (removeItem(player, used.asItem())) {

                        // record the log type
                        setAttribute(player, ATTR_PYRE_LOG, pyreLog.pyreLogId)
                        replaceScenery(with.asScenery(), Scenery.FUNERAL_PYRE_4094, -1)

                        // start the timer that will reset the pyre and remove player pyre tracking attributes
                        startPyreTimeout(player)

                        sendMessage(player, "You put some logs on the pyre.")
                    }

                } else sendMessage(player, "You can only use one pyre at a time.")

                return@onUseWith true
            }
        }

        // using shade remains on the pyre
        for (shadeIndex in shadeRemains.indices) {
            val shadeId = shadeRemains[shadeIndex]
            onUseWith(IntType.SCENERY, shadeId, Scenery.FUNERAL_PYRE_4094) { player, used, with ->

                // check that the player is placing the remains on their pyre
                val loc = getAttribute<Location?>(player, ATTR_PYRE_LOC, null)
                if (loc == with.location) {

                    // check the logs can burn this shade
                    val pyreLogId = getAttribute(player, ATTR_PYRE_LOG, -1)
                    val logData = PyreLog.forPyreLog(pyreLogId)

                    if (logData == null || logData.prayerXp[shadeIndex] == -1.0) {
                        sendMessage(player, "These logs are not powerful enough to burn this shade.")
                        return@onUseWith true
                    }

                    // remove the shade remains
                    if (removeItem(player, used.asItem())) {

                        // record the shade type
                        setAttribute(player, ATTR_PYRE_SHADE, shadeId)
                        replaceScenery(with.asScenery(), Scenery.FUNERAL_PYRE_4100, -1)

                        // reset the timer
                        val pyrePulse = getAttribute<Pulse?>(player, ATTR_PYRE_PULSE, null)
                        if (pyrePulse != null) pyrePulse.delay = PYRE_TIMEOUT

                        sendMessage(player, "You place the shades remains on the logs.") // the lack of an apostrophe on "shades" is authentic.
                    }

                } else sendMessage(player, "This is not your pyre.")

                return@onUseWith true
            }
        }

        // lighting the pyre
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.FUNERAL_PYRE_4100) { player, _, with ->
            lightPyre(player, with)
            return@onUseWith true
        }

        // lighting the pyre
        on(Scenery.FUNERAL_PYRE_4100, IntType.SCENERY, "light") { player, node ->
            lightPyre(player, node)
            return@on true
        }

        // --------------------------------------------
        // SHADE CATACOMBS
        // --------------------------------------------

        // doors to the shade catacombs
        on(Scenery.WOODEN_DOORS_31294, IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.SHADES_OF_MORTTON)) {
                if (anyInInventory(player, *shadeKeysBronze, *shadeKeysSteel, *shadeKeysBlack, *shadeKeysSilver)) {
                    queueScript(player, 0, QueueStrength.SOFT) { counter ->
                        when (counter) {
                            0 -> {
                                animate(player, Animations.HUMAN_OPEN_WARDROBE_542)
                                playAudio(player, Sounds.DOOR_OPEN_62)
                                replaceScenery(node.asScenery(), Scenery.WOODEN_DOORS_31295, 5)
                                sendMessage(player, "You use your Shade key to unlock the doors.")
                                return@queueScript delayScript(player, animationDuration(Animation(Animations.HUMAN_OPEN_WARDROBE_542)) + 1)
                            }

                            1 -> {
                                teleport(player, Location(3493, 9725, 0))
                                return@queueScript delayScript(player, 1)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else sendMessage(player, "You need a Shade key to unlock the doors.")
            } else sendMessage(player, "You need to complete the Shades of Mort'ton Quest to access this.")
            return@on true
        }

        // exit from the shade catacombs
        on(Scenery.SOLID_BRONZE_DOOR_34947, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You make your way back out of the Shade tombs.")
            teleport(player, Location(3484, 3323, 0))
            return@on true
        }

        /**
         * Good source of a player using doors and keys.
         * https://www.youtube.com/watch?v=t5Ll-S1R1JE
         */

        // the doors inside the catacombs. they require shade keys to enter, better keys the deeper you go.
        for (i in tombDoors.indices) {
            val doorType = keyTypes[i]
            on(tombDoors[i], IntType.SCENERY, "open") { player, node ->
                if (anyInInventory(player, *tombDoorKeys[i])) {
                    sendMessage(player, "You open the ominous tomb door.")
                    handleAutowalkDoor(player, node.asScenery())
                } else {
                    sendMessage(player, "You need a $doorType key or better to open this door.")
                }
                return@on true
            }
        }

        // covers all the chest and key interactions
        for (typeIndex in keyTypes.indices) {
            val chestArray = allChests[typeIndex]
            val keyArray = allKeys[typeIndex]
            val keyTypeName = keyTypes[typeIndex]

            on(chestArray, IntType.SCENERY, "open") { player, node ->
                // get chest color
                val colorIndex = chestArray.indexOf(node.id)

                if (colorIndex != -1) {
                    // find key for the above chest color
                    val requiredKey = keyArray[colorIndex]
                    val trimColor = keyColors[colorIndex]

                    if (inInventory(player, requiredKey)) {

                        when (node.id) { // who the fuck made the first bronze chest the only one in the array to not be lined up?!?!
                            Scenery.BRONZE_CHEST_4111 -> replaceScenery(node as core.game.node.scenery.Scenery, Scenery.BRONZE_CHEST_4131, 2)
                            else -> replaceScenery(node as core.game.node.scenery.Scenery, node.id + 6495, 2)
                        }
                        playAudio(player, Sounds.CHEST_OPEN_52)
                        shadeChestReward(player, requiredKey)
                    } else {
                        sendMessage(player, "You need a $keyTypeName key with a $trimColor trim to open this chest.")
                    }
                }
                return@on true
            }
        }
    }

    // --------------------------------------------
    // COMPANION CUBE
    // --------------------------------------------

    companion object {

        val keyTypes = arrayOf("bronze", "steel", "black", "silver")
        val keyColors = arrayOf("red", "brown", "crimson", "black", "purple")

        val shadeKeysBronze = intArrayOf(Items.BRONZE_KEY_RED_3450, Items.BRONZE_KEY_BROWN_3451, Items.BRONZE_KEY_CRIMSON_3452, Items.BRONZE_KEY_BLACK_3453, Items.BRONZE_KEY_PURPLE_3454)
        val shadeChestBronze = intArrayOf(Scenery.BRONZE_CHEST_4111, Scenery.BRONZE_CHEST_4112, Scenery.BRONZE_CHEST_4113, Scenery.BRONZE_CHEST_4114, Scenery.BRONZE_CHEST_4115) // red, brown, crimson, black, purple

        val shadeKeysSteel = intArrayOf(Items.STEEL_KEY_RED_3455, Items.STEEL_KEY_BROWN_3456, Items.STEEL_KEY_CRIMSON_3457, Items.STEEL_KEY_BLACK_3458, Items.STEEL_KEY_PURPLE_3459)
        val shadeChestSteel = intArrayOf(Scenery.STEEL_CHEST_4116, Scenery.STEEL_CHEST_4117, Scenery.STEEL_CHEST_4118, Scenery.STEEL_CHEST_4119, Scenery.STEEL_CHEST_4120)

        val shadeKeysBlack = intArrayOf(Items.BLACK_KEY_RED_3460, Items.BLACK_KEY_BROWN_3461, Items.BLACK_KEY_CRIMSON_3462, Items.BLACK_KEY_BLACK_3463, Items.BLACK_KEY_PURPLE_3464)
        val shadeChestBlack = intArrayOf(Scenery.BLACK_CHEST_4121, Scenery.BLACK_CHEST_4122, Scenery.BLACK_CHEST_4123, Scenery.BLACK_CHEST_4124, Scenery.BLACK_CHEST_4125)

        val shadeKeysSilver = intArrayOf(Items.SILVER_KEY_RED_3465, Items.SILVER_KEY_BROWN_3466, Items.SILVER_KEY_CRIMSON_3467, Items.SILVER_KEY_BLACK_3468, Items.SILVER_KEY_PURPLE_3469)
        val shadeChestSilver = intArrayOf(Scenery.SILVER_CHEST_4126, Scenery.SILVER_CHEST_4127, Scenery.SILVER_CHEST_4128, Scenery.SILVER_CHEST_4129, Scenery.SILVER_CHEST_4130)

        // all the shit tied together so the door and chest interacts can use FOR loops instead of rewriting this shit a million times but subtly different each time
        val allChests = arrayOf(shadeChestBronze, shadeChestSteel, shadeChestBlack, shadeChestSilver)
        val allKeys = arrayOf(shadeKeysBronze, shadeKeysSteel, shadeKeysBlack, shadeKeysSilver)

        val tombDoors = intArrayOf(
            Scenery.SOLID_BRONZE_DOOR_4106,
            Scenery.SOLID_STEEL_DOOR_4107,
            Scenery.SOLID_BLACK_DOOR_4108,
            Scenery.SOLID_SILVER_DOOR_4109
        )

        val tombDoorKeys = arrayOf(
            shadeKeysBronze + shadeKeysSteel + shadeKeysBlack + shadeKeysSilver, // bronze door unlocks with bronze/steel/black/silver
            shadeKeysSteel + shadeKeysBlack + shadeKeysSilver,                   // steel door unlocks with steel/black/silver
            shadeKeysBlack + shadeKeysSilver,                                    // black door unlocks with black/silver
            shadeKeysSilver                                                      // silver door unlocks with silver
        )

        private const val ATTR_PYRE_LOG   = "pyre_log_item"   // the type of log used
        private const val ATTR_PYRE_SHADE = "pyre_shade_item" // the type of shade remains used
        private const val ATTR_PYRE_LOC   = "pyre_location"   // which pyre the player is at
        private const val ATTR_PYRE_PULSE = "pyre_pulse"      // tracks the pyre pulse timeout timer

        const val PYRE_TIMEOUT = 50                               // 50 ticks == 30 seconds
        // wiki quote: "Light the fire promptly, the funeral pyre will despawn after about 30 seconds."

        val shadeRemains = intArrayOf(
            Items.LOAR_REMAINS_3396,
            Items.PHRIN_REMAINS_3398,
            Items.RIYL_REMAINS_3400,
            Items.ASYN_REMAINS_3402,
            Items.FIYR_REMAINS_3404
        )

        // TODO: probably move this into a companion object in the shades of mortton listeners or just delete them later if they're not used
        val serum207Doses = intArrayOf(Items.SERUM_207_4_3408, Items.SERUM_207_3_3410, Items.SERUM_207_2_3412, Items.SERUM_207_1_3414)
        val serum208Doses = intArrayOf(Items.SERUM_208_4_3416, Items.SERUM_208_3_3417, Items.SERUM_208_2_3418, Items.SERUM_208_1_3419)
        val oliveOilDoses = intArrayOf(Items.OLIVE_OIL4_3422, Items.OLIVE_OIL3_3424, Items.OLIVE_OIL2_3426, Items.OLIVE_OIL1_3428)
        val sacredOilDoses = intArrayOf(Items.SACRED_OIL4_3430, Items.SACRED_OIL3_3432, Items.SACRED_OIL2_3434, Items.SACRED_OIL1_3436)

        // this resets the pyre scenery and removes player attributes used to save pyre info after a timeout
        private fun startPyreTimeout(player: Player) {
            val loc = getAttribute<Location?>(player, ATTR_PYRE_LOC, null) ?: return
            // TODO: replace pulse with queueWorldScript
            val pyrePulse = object : Pulse(PYRE_TIMEOUT) {
                // runs after 50 ticks
                override fun pulse(): Boolean {
                    // clear the pyre state for the player
                    clearPyreState(player)

                    // reset the pyre scenery if player is gone
                    val pyre = getScenery(loc)
                    if (pyre != null) replaceScenery(pyre, Scenery.FUNERAL_PYRE_4093, -1)

                    // return true stops the pulse
                    return true
                }
            }

            setAttribute(player, ATTR_PYRE_PULSE, pyrePulse)
            Pulser.submit(pyrePulse)
        }

        // creates pyre logs
        private fun createPyreLog(player: Player, oil: Item, logs: Item, pyreId: Int, dose: Int, xp: Double) {
            val pyreLogs = pyreId.asItem()

            if (DecantListener.reduceDose(player, oil, dose)) {
                replaceSlot(player, logs.slot, pyreLogs)
                rewardXP(player, Skills.FIREMAKING, xp)
                sendMessage(player, "You use the ${oil.name} on the ${logs.name} and get ${pyreLogs.name}.") // not using the lowercase names is authentic (wow)

                // advance quest // TODO: uncomment when pulling back to Shades of Mort'ton
                //if (getQuestStage(player, Quests.SHADES_OF_MORTTON) <= 65) setQuestStage(player, Quests.SHADES_OF_MORTTON, 70)

            } else {
                sendMessage(player, "Making ${pyreLogs.name} requires $dose doses of sacred oil.")
            }
        }

        // handles lighting the pyres
        private fun lightPyre(player: Player, node: Node) {

            // this was fun to discover, without locking I can spam click "Light" and get a bunch of keys from one remains!
            lockInteractions(player, 15)

            val logId = getAttribute(player, ATTR_PYRE_LOG, -1)
            val shadeId = getAttribute(player, ATTR_PYRE_SHADE, -1)

            // need a tinderbox
            if (!inInventory(player, Items.TINDERBOX_590)) {
                sendMessage(player, "You do not have the required items to light this.") // message taken from FireMakingPulse
                return
            }

            if (logId == -1 || shadeId == -1) {
                sendMessage(player, "This is not your pyre.")
                clearPyreState(player)
                return
            }

            // get log data for the pyre
            val logData = PyreLog.forPyreLog(logId) ?: return

            // get xp
            val fmXp = logData.fmXp
            val prayerXp = logData.prayerXp[shadeRemains.indexOf(shadeId)]

            val spirit = NPC.create(NPCs.SHADE_SPIRIT_1242, node.centerLocation)
            spirit.isWalks = false

            // queuescript sequence to process the pyre
            queueScript(player, 0, QueueStrength.SOFT) { counter ->
                when (counter) {
                    0 -> {
                        animate(player, Animations.HUMAN_LIGHT_FIRE_WITH_TINDERBOX_733)
                        sendMessage(player, "You attempt to light the funeral pyre.") // authentically there is no "You successfully light the funeral pyre" message
                        return@queueScript delayScript(player, 4)
                    }

                    1 -> {
                        // burning pyre - gfx 292
                        resetAnimator(player)
                        visualize(spirit, -1, org.rs09.consts.Graphics.MORTTON_PYRE_BURN_292)
                        return@queueScript delayScript(player, 2)
                    }

                    2 -> {
                        // shade rising up - spawn npc 1242
                        spirit.init()
                        playAudio(player, Sounds.SHADE_SIGH748)
                        return@queueScript delayScript(player, 4)
                    }

                    3 -> {
                        // key spawns, clear pyre
                        shadeKeyReward(player, shadeId, node.location)
                        return@queueScript delayScript(player, 1)
                    }

                    4 -> {
                        // despawn shade
                        spirit.clear()

                        // clear pyre
                        clearPyreState(player)

                        // reward xp
                        rewardXP(player, Skills.FIREMAKING, fmXp)
                        rewardXP(player, Skills.PRAYER, prayerXp)

                        // advance quest // TODO: uncomment when pulling back to Shades of Mort'ton
                        //if (getQuestStage(player, Quests.SHADES_OF_MORTTON) <= 70) setQuestStage(player, Quests.SHADES_OF_MORTTON, 80)

                        return@queueScript delayScript(player, 1)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }

        // clears the pyre
        fun clearPyreState(player: Player) {

            // clears the scenery
            val loc = getAttribute<Location?>(player, ATTR_PYRE_LOC, null) ?: return
            val pyre = getScenery(loc)
            if (pyre != null) replaceScenery(pyre, Scenery.FUNERAL_PYRE_4093, -1)

            // stop the pulse (if pyre gets cleared early due to you burning it)
            val pyrePulse = getAttribute<Pulse?>(player, ATTR_PYRE_PULSE, null)
            pyrePulse?.stop()

            // removes attributes
            removeAttribute(player, ATTR_PYRE_LOG)
            removeAttribute(player, ATTR_PYRE_SHADE)
            removeAttribute(player, ATTR_PYRE_LOC)
            removeAttribute(player, ATTR_PYRE_PULSE)
        }

        /**
         *     / 00 \
         *    /      \
         *   01      03
         *    \      /
         *     \ 02 /
         *                   PYRE LOCATIONS AND NUMBERING
         *     / 04 \
         *    /      \                MORT'TON
         *   05      07
         *    \      /
         *     \ 06 /
         *
         *     / 08 \         / 12 \         / 16 \         / 20 \
         *    /      \       /      \       /      \       /      \
         *   09      11     13      15     17      19     21      23
         *    \      /       \      /       \      /       \      /
         *     \ 10 /         \ 14 /         \ 18 /         \ 22 /
         */

        // pyre location data
        val pyreLocs = arrayOf(
            // first loc is pyre location, second loc is its stone stand location
            arrayOf(Location(3465, 3295, 0), Location(3465, 3298, 0)), // pyre 00
            arrayOf(Location(3462, 3294, 0), Location(3461, 3294, 0)), // pyre 01
            arrayOf(Location(3465, 3291, 0), Location(3465, 3290, 0)), // pyre 02
            arrayOf(Location(3466, 3294, 0), Location(3469, 3294, 0)), // pyre 03

            arrayOf(Location(3465, 3283, 0), Location(3465, 3286, 0)), // pyre 04
            arrayOf(Location(3462, 3282, 0), Location(3461, 3282, 0)), // pyre 05
            arrayOf(Location(3465, 3279, 0), Location(3465, 3278, 0)), // pyre 06
            arrayOf(Location(3466, 3282, 0), Location(3469, 3282, 0)), // pyre 07

            arrayOf(Location(3467, 3271, 0), Location(3467, 3274, 0)), // pyre 08
            arrayOf(Location(3464, 3270, 0), Location(3463, 3270, 0)), // pyre 09
            arrayOf(Location(3467, 3267, 0), Location(3467, 3266, 0)), // pyre 10
            arrayOf(Location(3468, 3270, 0), Location(3471, 3270, 0)), // pyre 11

            arrayOf(Location(3476, 3269, 0), Location(3476, 3272, 0)), // pyre 12
            arrayOf(Location(3473, 3268, 0), Location(3472, 3268, 0)), // pyre 13
            arrayOf(Location(3476, 3265, 0), Location(3476, 3264, 0)), // pyre 14
            arrayOf(Location(3477, 3268, 0), Location(3480, 3268, 0)), // pyre 15

            arrayOf(Location(3500, 3269, 0), Location(3500, 3272, 0)), // pyre 16
            arrayOf(Location(3497, 3268, 0), Location(3496, 3268, 0)), // pyre 17
            arrayOf(Location(3500, 3265, 0), Location(3500, 3264, 0)), // pyre 18
            arrayOf(Location(3501, 3268, 0), Location(3504, 3268, 0)), // pyre 19

            arrayOf(Location(3507, 3276, 0), Location(3507, 3279, 0)), // pyre 20
            arrayOf(Location(3504, 3275, 0), Location(3503, 3275, 0)), // pyre 21
            arrayOf(Location(3507, 3272, 0), Location(3507, 3271, 0)), // pyre 22
            arrayOf(Location(3508, 3275, 0), Location(3511, 3275, 0)), // pyre 23
        )

        // rewards the shade key or coins from burning the shade
        // My source was OSRS (ugh) and sanity checking against period youtube videos. https://oldschool.runescape.wiki/w/Shades_of_Mort%27ton_(minigame)
        fun shadeKeyReward(player: Player, shadeId: Int, pyreLoc: Location) {
            val roll = RandomFunction.random(1000)
            var rewardId = -1
            var rewardAmount = 1

            // note: all of these random selects do NOT go to the top number. (200, 301) gives you a number from 200-300

            when (shadeId) {
                Items.LOAR_REMAINS_3396 -> {
                    // rewards: 21% chance of 200-300 coins, 79% chance of bronze key red/brown/crimson
                    if (roll < 210) {
                        rewardId = Items.COINS_995
                        rewardAmount = RandomFunction.random(200, 301)
                    } else {
                        rewardId = shadeKeysBronze[RandomFunction.random(0, 3)]
                    }
                }

                Items.PHRIN_REMAINS_3398 -> {
                    // rewards: 21% chance of 400-500 coins, 12.5% chance of bronze key black/purple, 66.5% chance of steel key red/brown/crimson
                    if (roll < 210) {
                        rewardId = Items.COINS_995
                        rewardAmount = RandomFunction.random(400, 501)
                    } else if (roll < 335) { // 210 + 125 = 335
                        rewardId = shadeKeysBronze[RandomFunction.random(3, 5)]
                    } else {
                        rewardId = shadeKeysSteel[RandomFunction.random(0, 3)]
                    }
                }

                Items.RIYL_REMAINS_3400 -> {
                    // rewards: 21% chance of 600-700 coins, 12.5% chance of steel key black/purple, 66.5% chance of black key red/brown/crimson
                    if (roll < 210) {
                        rewardId = Items.COINS_995
                        rewardAmount = RandomFunction.random(600, 701)
                    } else if (roll < 335) { // 210 + 125 = 335
                        rewardId = shadeKeysSteel[RandomFunction.random(3, 5)]
                    } else {
                        rewardId = shadeKeysBlack[RandomFunction.random(0, 3)]
                    }
                }

                Items.ASYN_REMAINS_3402 -> {
                    // rewards: 21% chance of 800-900 coins, 28.2% chance of black key crimson/black/purple, 50.8% chance of silver key red/brown
                    if (roll < 210) {
                        rewardId = Items.COINS_995
                        rewardAmount = RandomFunction.random(800, 901)
                    } else if (roll < 492) { // 210 + 282 = 492
                        rewardId = shadeKeysBlack[RandomFunction.random(2, 5)]
                    } else {
                        rewardId = shadeKeysSilver[RandomFunction.random(0, 2)]
                    }
                }

                Items.FIYR_REMAINS_3404 -> {
                    // rewards: 21% chance of 2000-4000 coins, 79% chance of silver key red/brown/black/crimson/purple
                    if (roll < 210) {
                        rewardId = Items.COINS_995
                        rewardAmount = RandomFunction.random(2000, 4001)
                    } else {
                        rewardId = shadeKeysSilver[RandomFunction.random(0, 5)]
                    }
                }
            }

            // using the pyre location, get the stone stand location from the array
            val standLoc = pyreLocs.find { it[0] == pyreLoc }?.get(1) ?: pyreLoc // fallback to pyre location (should never trigger because I completed my fucking array)

            // spawn the grounditem reward
            if (rewardId != -1) {
                // Authentically, this ground item might have a shorter timeout than global. Investigate, or just leave as is because who wants their fucking stuff disappearing sooner?
                produceGroundItem(player, rewardId, rewardAmount, standLoc)
                sendGraphics(Graphics(org.rs09.consts.Graphics.GIVE_THANKS_86, 100), standLoc) // this is the poofclear gfx, but I can't find the authentic one. It's pretty close.
            }
        }

        // rewards the chest
        fun shadeChestReward(player: Player, keyId: Int) {
            // determine tier
            val (tier, loop) = when (keyId) {
                Items.BRONZE_KEY_RED_3450 ->     Pair(ChestTier.BRONZE, KeyLoop.RED)
                Items.BRONZE_KEY_BROWN_3451 ->   Pair(ChestTier.BRONZE, KeyLoop.BROWN)
                Items.BRONZE_KEY_CRIMSON_3452 -> Pair(ChestTier.BRONZE, KeyLoop.CRIMSON)
                Items.BRONZE_KEY_BLACK_3453 ->   Pair(ChestTier.BRONZE, KeyLoop.BLACK)
                Items.BRONZE_KEY_PURPLE_3454 ->  Pair(ChestTier.BRONZE, KeyLoop.PURPLE)

                Items.STEEL_KEY_RED_3455 ->      Pair(ChestTier.STEEL, KeyLoop.RED)
                Items.STEEL_KEY_BROWN_3456 ->    Pair(ChestTier.STEEL, KeyLoop.BROWN)
                Items.STEEL_KEY_CRIMSON_3457 ->  Pair(ChestTier.STEEL, KeyLoop.CRIMSON)
                Items.STEEL_KEY_BLACK_3458 ->    Pair(ChestTier.STEEL, KeyLoop.BLACK)
                Items.STEEL_KEY_PURPLE_3459 ->   Pair(ChestTier.STEEL, KeyLoop.PURPLE)

                Items.BLACK_KEY_RED_3460 ->      Pair(ChestTier.BLACK, KeyLoop.RED)
                Items.BLACK_KEY_BROWN_3461 ->    Pair(ChestTier.BLACK, KeyLoop.BROWN)
                Items.BLACK_KEY_CRIMSON_3462 ->  Pair(ChestTier.BLACK, KeyLoop.CRIMSON)
                Items.BLACK_KEY_BLACK_3463 ->    Pair(ChestTier.BLACK, KeyLoop.BLACK)
                Items.BLACK_KEY_PURPLE_3464 ->   Pair(ChestTier.BLACK, KeyLoop.PURPLE)

                Items.SILVER_KEY_RED_3465 ->     Pair(ChestTier.SILVER, KeyLoop.RED)
                Items.SILVER_KEY_BROWN_3466 ->   Pair(ChestTier.SILVER, KeyLoop.BROWN)
                Items.SILVER_KEY_CRIMSON_3467 -> Pair(ChestTier.SILVER, KeyLoop.CRIMSON)
                Items.SILVER_KEY_BLACK_3468 ->   Pair(ChestTier.SILVER, KeyLoop.BLACK)
                Items.SILVER_KEY_PURPLE_3469 ->  Pair(ChestTier.SILVER, KeyLoop.PURPLE)

                else -> return
            }

            // remove key and give rewards
            if (removeItem(player, Item(keyId))) {
                val generatedLoot = ShadeChestRewards.generateLoot(tier, loop)

                // add items
                generatedLoot.forEach { item ->
                    addItemOrDrop(player, item.id, item.amount)
                }

                // send dialogue
                val mainItem = generatedLoot[0]
                val coins = generatedLoot.find { it.id == Items.COINS_995 }
                val paste = generatedLoot.find { it.id == Items.SWAMP_PASTE_1941 }

                // if you get coins
                if (coins != null) {
                    // if you get swamp paste
                    if (paste != null) {
                        openDialogue(player, object : DialogueLabeller() {
                            override fun addConversation() {
                                item(mainItem, Item(Items.COINS_6964), "You find some treasure and ${coins.amount} gold coins. The key strangely dissolves in the chest lock.")
                                item(Item(Items.SWAMP_PASTE_1941), "You find ${paste.amount} doses of swamp paste at the bottom of the chest.")
                            }
                        })
                    // coins with no swamp paste
                    } else {
                        sendDoubleItemDialogue(player, mainItem, Item(Items.COINS_6964), "You find some treasure and ${coins.amount} gold coins. The key strangely dissolves in the chest lock.")
                    }
                // no coins
                } else {
                    // no coins and swamp paste
                    if (paste != null) {
                        openDialogue(player, object : DialogueLabeller() {
                            override fun addConversation() {
                                item(mainItem, "You find some treasure. The key strangely dissolves in the chest lock.")
                                item(Item(Items.SWAMP_PASTE_1941), "You find ${paste.amount} doses of swamp paste at the bottom of the chest.")
                            }
                        })
                    // no coins and no paste. shit tier >:(
                    } else {
                        sendItemDialogue(player, mainItem, "You find some treasure. The key strangely dissolves in the chest lock.")
                    }
                }
            }
        }
    }
}
package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class DesertTreasureListeners : InteractionListener {

    companion object {
        fun allDiamondsInserted(player: Player): Boolean{
            return getAttribute(player, DesertTreasure.attributeBloodDiamondInserted, 0) == 1 &&
                    getAttribute(player, DesertTreasure.attributeSmokeDiamondInserted, 0) == 1 &&
                    getAttribute(player, DesertTreasure.attributeIceDiamondInserted, 0) == 1 &&
                    getAttribute(player, DesertTreasure.attributeShadowDiamondInserted, 0) == 1
        }
    }
    var temp = 6517

    override fun defineListeners() {

        // THE MIRRORS
        on(Scenery.MYSTICAL_MIRROR_6423, SCENERY, "look-into") { player, node ->
            SouthMirrorLookCutscene(player).start()
            return@on true
        }
        on(Scenery.MYSTICAL_MIRROR_6425, SCENERY, "look-into") { player, node ->
            SouthWestMirrorLookCutscene(player).start()
            return@on true
        }
        on(Scenery.MYSTICAL_MIRROR_6427, SCENERY, "look-into") { player, node ->
            NorthWestMirrorLookCutscene(player).start()
            return@on true
        }
        on(Scenery.MYSTICAL_MIRROR_6429, SCENERY, "look-into") { player, node ->
            NorthMirrorLookCutscene(player).start()
            return@on true
        }
        on(Scenery.MYSTICAL_MIRROR_6431, SCENERY, "look-into") { player, node ->
            NorthEastMirrorLookCutscene(player).start()
            return@on true
        }
        on(Scenery.MYSTICAL_MIRROR_6433, SCENERY, "look-into") { player, node ->
            SouthEastMirrorLookCutscene(player).start()
            return@on true
        }


        // THE OBELISKS
        onUseWith(IntType.SCENERY, intArrayOf(Items.ICE_DIAMOND_4671, Items.SMOKE_DIAMOND_4672, Items.SHADOW_DIAMOND_4673), Scenery.OBELISK_6483) { player, used, with ->
            sendMessage(player, "That doesn't appear to be the correct diamond...")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.BLOOD_DIAMOND_4670, Scenery.OBELISK_6483) { player, used, with ->
            if (getDynLevel(player, Skills.MAGIC) > 50) {
                if (removeItem(player, used)) {
                    sendMessage(player, "The diamond is absorbed into the pillar.")
                    setVarbit(player, DesertTreasure.varbitBloodObelisk, 1)
                    setAttribute(player, DesertTreasure.attributeBloodDiamondInserted, 1)
                    if (allDiamondsInserted(player)) {
                        sendMessage(player, "The force preventing access to the Pyramid has now vanished.")
                        if (getQuestStage(player, DesertTreasure.questName) == 9) {
                            setQuestStage(player, DesertTreasure.questName, 10)
                        }
                    }
                }
            } else {
                sendMessage(player, "You are not a powerful enough mage to breach the protective aura.")
                sendMessage(player, "You need a magic level of at least 50 to enter the Pyramid.")
            }
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, intArrayOf(Items.BLOOD_DIAMOND_4670, Items.ICE_DIAMOND_4671, Items.SHADOW_DIAMOND_4673), Scenery.OBELISK_6486) { player, used, with ->
            sendMessage(player, "That doesn't appear to be the correct diamond...")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.SMOKE_DIAMOND_4672, Scenery.OBELISK_6486) { player, used, with ->
            if (getDynLevel(player, Skills.MAGIC) > 50) {
                if (removeItem(player, used)) {
                    sendMessage(player, "The diamond is absorbed into the pillar.")
                    setVarbit(player, DesertTreasure.varbitSmokeObelisk, 1)
                    setAttribute(player, DesertTreasure.attributeSmokeDiamondInserted, 1)
                    if (allDiamondsInserted(player)) {
                        sendMessage(player, "The force preventing access to the Pyramid has now vanished.")
                        if (getQuestStage(player, DesertTreasure.questName) == 9) {
                            setQuestStage(player, DesertTreasure.questName, 10)
                        }
                    }
                }
            } else {
                sendMessage(player, "You are not a powerful enough mage to breach the protective aura.")
                sendMessage(player, "You need a magic level of at least 50 to enter the Pyramid.")
            }
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, intArrayOf(Items.BLOOD_DIAMOND_4670, Items.SMOKE_DIAMOND_4672, Items.SHADOW_DIAMOND_4673), Scenery.OBELISK_6489) { player, used, with ->
            sendMessage(player, "That doesn't appear to be the correct diamond...")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.ICE_DIAMOND_4671, Scenery.OBELISK_6489) { player, used, with ->
            if (getDynLevel(player, Skills.MAGIC) > 50) {
                if (removeItem(player, used)) {
                    sendMessage(player, "The diamond is absorbed into the pillar.")
                    setVarbit(player, DesertTreasure.varbitIceObelisk, 1)
                    setAttribute(player, DesertTreasure.attributeIceDiamondInserted, 1)
                    if (allDiamondsInserted(player)) {
                        sendMessage(player, "The force preventing access to the Pyramid has now vanished.")
                        if (getQuestStage(player, DesertTreasure.questName) == 9) {
                            setQuestStage(player, DesertTreasure.questName, 10)
                        }
                    }
                }
            } else {
                sendMessage(player, "You are not a powerful enough mage to breach the protective aura.")
                sendMessage(player, "You need a magic level of at least 50 to enter the Pyramid.")
            }
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, intArrayOf(Items.BLOOD_DIAMOND_4670, Items.ICE_DIAMOND_4671, Items.SMOKE_DIAMOND_4672), Scenery.OBELISK_6492) { player, used, with ->
            sendMessage(player, "That doesn't appear to be the correct diamond...")
            return@onUseWith true
        }
        onUseWith(IntType.SCENERY, Items.SHADOW_DIAMOND_4673, Scenery.OBELISK_6492) { player, used, with ->
            if (getDynLevel(player, Skills.MAGIC) > 50) {
                if (removeItem(player, used)) {
                    sendMessage(player, "The diamond is absorbed into the pillar.")
                    setVarbit(player, DesertTreasure.varbitShadowObelisk, 1)
                    setAttribute(player, DesertTreasure.attributeShadowDiamondInserted, 1)
                    if (allDiamondsInserted(player)) {
                        sendMessage(player, "The force preventing access to the Pyramid has now vanished.")
                        if (getQuestStage(player, DesertTreasure.questName) == 9) {
                            setQuestStage(player, DesertTreasure.questName, 10)
                        }
                    }
                }
            } else {
                sendMessage(player, "You are not a powerful enough mage to breach the protective aura.")
                sendMessage(player, "You need a magic level of at least 50 to enter the Pyramid.")
            }
            return@onUseWith true
        }

        // THE DOOR
        on(intArrayOf(Scenery.PYRAMID_ENTRANCE_6545, Scenery.PYRAMID_ENTRANCE_6547), SCENERY, "open") { player, node ->
            if (allDiamondsInserted(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "A mystical power has sealed this door...")
            }
            return@on true
        }

        // THE LADDERS
        on(Scenery.LADDER_6497, SCENERY, "climb-down") { player, node ->
            teleport(player, Location(2913, 4954, 3))
            return@on true
        }
        on(Scenery.LADDER_6504, SCENERY, "climb-up") { player, node ->
            teleport(player, Location(3233, 2898, 0))
            return@on true
        }

        on(Scenery.LADDER_6498, SCENERY, "climb-down") { player, node ->
            teleport(player, Location(2846, 4964, 2))
            return@on true
        }
        on(Scenery.LADDER_6503, SCENERY, "climb-up") { player, node ->
            teleport(player, Location(2909, 4963, 3))
            return@on true
        }

        on(Scenery.LADDER_6499, SCENERY, "climb-down") { player, node ->
            teleport(player, Location(2782, 4972, 1))
            return@on true
        }
        on(Scenery.LADDER_6502, SCENERY, "climb-up") { player, node ->
            teleport(player, Location(2845, 4973, 2))
            return@on true
        }

        on(Scenery.LADDER_6500, SCENERY, "climb-down") { player, node ->
            teleport(player, Location(3233, 9293, 0))
            return@on true
        }
        on(Scenery.LADDER_6501, SCENERY, "climb-up") { player, node ->
            teleport(player, Location(2783, 4941, 1))
            return@on true
        }

        on((6512..6517).toIntArray(), SCENERY, "search") { player, node ->
            // Technically there is loot, but I'm lazy as hell.
            sendMessage(player, "You don't find anything interesting.")
            return@on true
        }

        // After Quest

        // Backdoor
        on(Scenery.TUNNEL_6481, SCENERY, "enter") { player, node ->
            if (isQuestComplete(player, DesertTreasure.questName)){
                teleport(player, Location(3233, 9313, 0))
            } else {
                sendMessage(player, "This passage does not seem to lead anywhere...") // https://youtu.be/uNkBucGaqac
            }
            return@on true
        }

        // Portal, which only appears after DT
        on(Scenery.PORTAL_6551, SCENERY, "use") { player, node ->
                teleport(player, Location(3233, 2887, 0))
            return@on true
        }

    }
}


// https://www.youtube.com/watch?v=yMwp78OI2y8

// Ice Troll
class NorthMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(11322)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 5, 27)
                moveCamera(5, 27, 1000)
                rotateCamera(6, 27, 1000)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}

// Canifis
class NorthEastMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13878)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 47, 31)
                moveCamera(47, 31, 1000)
                rotateCamera(43, 31, 1000)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}

// Smoke Dungeon
class SouthEastMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13102)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 36, 20)
                moveCamera(36, 20, 1000)
                rotateCamera(44, 20, 1000)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}

// Pyramid
class SouthMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(12845)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 33, 41)
                moveCamera(33, 41, 1000)
                rotateCamera(33, 39, 990)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}

// Bedabin
class SouthWestMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(12590)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 40, 39)
                moveCamera(40, 39, 1200)
                rotateCamera(10, 39, 1200)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}

// Rasolo
class NorthWestMirrorLookCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(10037)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }
            1 -> {
                teleport(player, 56, 40)
                moveCamera(56, 40, 1400)
                rotateCamera(56, 35, 1000)
                timedUpdate(1)
            }
            2 -> {
                openInterface(player, 155)
                closeOverlay()
                timedUpdate(6)
            }
            3-> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }
            4-> {
                end(false){
                    fadeFromBlack()
                    // resetCamera()
                }
            }
        }
    }
}
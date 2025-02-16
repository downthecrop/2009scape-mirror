package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.timer.RSTimer
import core.game.world.map.Location
import core.tools.secondsToTicks
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class DiamondOfSmokeListeners : InteractionListener {
    companion object {
        const val timerIdentifierTorchNE = "deserttreasureNEtorch"
        const val timerIdentifierTorchSE = "deserttreasureSEtorch"
        const val timerIdentifierTorchSW = "deserttreasureSWtorch"
        const val timerIdentifierTorchNW = "deserttreasureNWtorch"

        fun checkAllTorchesLit(player: Player) : Boolean {
            return getVarbit(player, DesertTreasure.varbitStandingTorchNorthEast) == 1 &&
                    getVarbit(player, DesertTreasure.varbitStandingTorchSouthEast) == 1 &&
                    getVarbit(player, DesertTreasure.varbitStandingTorchSouthWest) == 1 &&
                    getVarbit(player, DesertTreasure.varbitStandingTorchNorthWest) == 1
        }
    }

    override fun defineListeners() {
        on(Scenery.BURNT_CHEST_6420, SCENERY, "open") { player, node ->
            if (checkAllTorchesLit(player)) {
                if (DesertTreasure.getSubStage(player, DesertTreasure.attributeSmokeStage) == 0) {
                    DesertTreasure.setSubStage(player, DesertTreasure.attributeSmokeStage, 1)
                }
            }
            if (DesertTreasure.getSubStage(player, DesertTreasure.attributeSmokeStage) >= 1) {
                replaceScenery(node as core.game.node.scenery.Scenery, 6421, 2)
                addItemOrDrop(player, Items.WARM_KEY_4656)
                sendMessage(player, "You open the chest and take a key.")
            } else {
                sendDialogueLines(player, "There seems to be no way to open this chest. Engraved where the", "keyhole should be is a message:", "'Light the path, and find the key...'")
            }
            return@on true
        }

        on(intArrayOf(Scenery.GATE_6451, Scenery.GATE_6452), SCENERY, "open") { player, node ->
            if (getQuestStage(player, DesertTreasure.questName) == 9) {
                // Set attributeUnlockedGate to true if warm key and first time unlocking the gate.
                if (!getAttribute(player, DesertTreasure.attributeUnlockedGate, false) && inInventory(player, Items.WARM_KEY_4656)) {
                    if(removeItem(player, Items.WARM_KEY_4656)) {
                        sendMessage(player, "You unlock the gate and enter the room.")
                        setAttribute(player, DesertTreasure.attributeUnlockedGate, true)
                    }
                }
                // Fight if unlocked gate, drop recovery diamond if done.
                if (getAttribute(player, DesertTreasure.attributeUnlockedGate, false)) {
                    if (DesertTreasure.getSubStage(player, DesertTreasure.attributeSmokeStage) == 1 &&
                            getAttribute<NPC?>(player, DesertTreasure.attributeFareedInstance, null) == null) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        val npc = core.game.node.entity.npc.NPC.create(NPCs.FAREED_1977, Location(3315, 9376, 0))
                        setAttribute(player, DesertTreasure.attributeFareedInstance, npc)
                        setAttribute(npc, "target", player)
                        npc.isRespawn = false
                        npc.init()
                        npc.attack(player)
                        sendChat(npc, "You dare trespass in my realm?")
                    } else if (DesertTreasure.getSubStage(player, DesertTreasure.attributeSmokeStage) >= 100) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        if (!inInventory(player, Items.SMOKE_DIAMOND_4672) && !inBank(player, Items.SMOKE_DIAMOND_4672)) {
                            sendMessage(player, "The Diamond of Smoke seems to have mystically found its way back here...")
                            GroundItemManager.create(Item(Items.SMOKE_DIAMOND_4672), Location(3315, 9376, 0), player)
                        }
                    }
                } else {
                    sendMessage(player, "The gate is locked.")
                }

            } else if (getQuestStage(player, DesertTreasure.questName) in 9..100) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The gate is locked.")
            }
            return@on true
        }


        on(Scenery.A_DARK_HOLE_31367, SCENERY, "enter") { player, node ->
            // You have to come from the Pollnivneach Slayer Dungeon, then this will tele you back.
            sendDialogueLines(player, "This hole leads to a maze of other passages. Without knowing where", "you are headed, there's no chance of reaching anywhere interesting.")
            return@on true
        }

        // NE Standing Torch
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.STANDING_TORCH_6406, Scenery.STANDING_TORCH_6413) { player, used, with ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchNorthEast, 1)
            sendMessage(player, "You light the torch.")
            if (checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchNE)
            registerTimer(player, StandingTorchTimer(timerIdentifierTorchNE, DesertTreasure.varbitStandingTorchNorthEast))
            return@onUseWith true
        }

        // SE Standing Torch
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.STANDING_TORCH_6408, Scenery.STANDING_TORCH_6414) { player, used, with ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchSouthEast, 1)
            sendMessage(player, "You light the torch.")
            if (checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchSE)
            registerTimer(player, StandingTorchTimer(timerIdentifierTorchSE, DesertTreasure.varbitStandingTorchSouthEast))
            return@onUseWith true
        }

        // SW Standing Torch
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.STANDING_TORCH_6410, Scenery.STANDING_TORCH_6415) { player, used, with ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchSouthWest, 1)
            sendMessage(player, "You light the torch.")
            if (checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchSW)
            registerTimer(player, StandingTorchTimer(timerIdentifierTorchSW, DesertTreasure.varbitStandingTorchSouthWest))
            return@onUseWith true
        }

        // NW Standing Torch
        onUseWith(IntType.SCENERY, Items.TINDERBOX_590, Scenery.STANDING_TORCH_6412, Scenery.STANDING_TORCH_6416) { player, used, with ->
            if (getDynLevel(player, Skills.FIREMAKING) < 50) {
                sendMessage(player, "You need a firemaking level of 50 to light this torch.")
                return@onUseWith true
            }
            setVarbit(player, DesertTreasure.varbitStandingTorchNorthWest, 1)
            sendMessage(player, "You light the torch.")
            if (checkAllTorchesLit(player)) {
                sendMessage(player, "The path is lit, now claim the key...")
            }
            removeTimer(player, timerIdentifierTorchNW)
            registerTimer(player, StandingTorchTimer(timerIdentifierTorchNW, DesertTreasure.varbitStandingTorchNorthWest))
            return@onUseWith true
        }
    }
}

class StandingTorchTimer(private val timerIdentifier: String = "deserttreasureunknowntimer", private val torchVarbit: Int = 0) : RSTimer(secondsToTicks(150), timerIdentifier) {
    override fun run(entity: Entity): Boolean {
        if (entity is Player) {
            when(timerIdentifier) {
                DiamondOfSmokeListeners.timerIdentifierTorchNE -> sendMessage(entity, "The North-east torch burns out...")
                DiamondOfSmokeListeners.timerIdentifierTorchSE -> sendMessage(entity, "The South-east torch burns out...")
                DiamondOfSmokeListeners.timerIdentifierTorchSW -> sendMessage(entity, "The South-west torch burns out...")
                DiamondOfSmokeListeners.timerIdentifierTorchNW -> sendMessage(entity, "The North-west torch burns out...")
                else -> sendMessage(entity, "The torch burns out...")
            }
            setVarbit(entity, torchVarbit, 0)
        }
        entity.timers.removeTimer(this)
        return true
    }
}
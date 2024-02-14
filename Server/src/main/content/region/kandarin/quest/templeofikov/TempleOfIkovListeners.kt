package content.region.kandarin.quest.templeofikov

import content.global.ame.events.drilldemon.DrillDemonUtils
import content.global.ame.events.drilldemon.SeargentDamienDialogue
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.global.action.PickupHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

class TempleOfIkovListeners : InteractionListener {

    companion object {
        val chestLocations =
                arrayOf(Location(2710, 9850, 0),
                        Location(2719, 9838, 0),
                        Location(2729, 9850, 0),
                        Location(2747, 9848, 0),
                        Location(2738, 9835, 0),
                        Location(2745, 9821, 0))
    }

    override fun defineDestinationOverrides() {
        // B - C (fucking lever is on an angle)
        setDest(IntType.SCENERY, intArrayOf(Scenery.LEVER_87), "pull") { _, _ ->
            return@setDest Location.create(2671, 9805, 0)
        }
    }

    override fun defineListeners() {

        // This is the trap door falling area.
        addClimbDest(Location.create(2682, 9849, 0), Location.create(2665, 9849, 0))

        // Shiny key to back of McGrubor's Wood. THIS MUST BE LOCKED UP
        on(Scenery.DOOR_99, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.SHINY_KEY_85)){
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
        onUseWith(SCENERY, Items.SHINY_KEY_85, Scenery.DOOR_99) { player, used, with ->
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        // 1 - 2 Walk past the gate. You must always wear the pendant to get past this gate.
        on(intArrayOf(Scenery.GATE_94, Scenery.GATE_95), SCENERY, "open") { player, node ->
            if (inEquipment(player, Items.PENDANT_OF_LUCIEN_86)){
                if(getQuestStage(player, TempleOfIkov.questName) == 1) {
                    setQuestStage(player, TempleOfIkov.questName, 2)
                }
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "As you reach to open the door a great terror overcomes you!")
            }
            return@on true
        }

        // 2: Trapdoor exit
        on(Scenery.TRAPDOOR_100, SCENERY, "open") { player, node ->
            sendDialogueLines(player, "You try to open the trapdoor but it won't budge! It looks like the", "trapdoor can only be opened from the other side.")
            sendMessage(player, "You try to open the trapdoor but it won't budge!")
            sendMessage(player, "It looks like the trapdoor can only be opened from the other side.")
            return@on true
        }


        // 2 - 3: Lever with trapdoor pull.
        on(Scenery.LEVER_91, SCENERY, "pull") { player, node ->
            replaceScenery(node.asScenery(), 88, 3)
            animate(player, Animation(2140))
            if (getAttribute(player, TempleOfIkov.attributeDisabledTrap, false)) {
                if(getQuestStage(player, TempleOfIkov.questName) == 2) {
                    setQuestStage(player, TempleOfIkov.questName, 3)
                }
            } else {
                AgilityHandler.fail(player, 2, Location.create(2682, 9855, 0), Animation(770), 20, "You slip and fall to the pit below.")
            }
            return@on true
        }

        // 2: Lever with trapdoor search. If you do not have level 42 thieving, you cannot proceed.
        on(Scenery.LEVER_91, SCENERY, "Search for traps") { player, node ->
            if (getDynLevel(player, Skills.THIEVING) >= 42) {
                sendDialogueLines(player, "You find a trap on the lever! You disable the trap.")
                setAttribute(player, TempleOfIkov.attributeDisabledTrap, true)
            } else {
                // This is the best video. It shows that you can start the quest even if you didn't meet reqs.
                // It also shows what happens if you tried to disarm without level 42 Thieving.
                // https://www.youtube.com/watch?v=I28nZxZAd58
                sendDialogueLines(player, "You find nothing.")
            }
            return@on true
        }

        // A: Obtain boots.
        // Note that the room is "dark". There are two areas.
        // You switch between them if you have light or not.
        // The light lets you see further into the room
        // https://www.youtube.com/watch?v=6ZGpJNeGLJ0
        // sendDialogue("Hmm...bit dark down here! I'm not going to venture far!")


        // B: Attach lever, authentic if you log out, the lever is lost, and you have to do that bridge again
        onUseWith(SCENERY, Items.LEVER_83, Scenery.LEVER_BRACKET_86) { player, used, with ->
            removeItem(player, used)
            replaceScenery(with.asScenery(), Scenery.LEVER_87, 20)
            sendDialogue(player, "You fit the lever into the bracket.")
            return@onUseWith true
        }

        // B to C: Pull down on lever
        on(Scenery.LEVER_87, SCENERY, "pull") { player, node ->
            face(player, Location.create(2671, 9803, 0))
            animate(player, Animation(2140))
            replaceScenery(node.asScenery(), 88, 3) // Scenery.88 is the downward lever.
            setAttribute(player, TempleOfIkov.attributeIceChamberAccess, true)
            queueScript(player, 6, QueueStrength.NORMAL) {
                replaceScenery(node.asScenery(), Scenery.LEVER_BRACKET_86, -1)
                return@queueScript stopExecuting(player)
            }
            sendDialogue(player, "You hear the clunking of some hidden machinery.")
            return@on true
        }

        // C: Gate opens after attached lever
        on(intArrayOf(Scenery.GATE_89, Scenery.GATE_90), SCENERY, "open") { player, node ->
            if (getAttribute(player, TempleOfIkov.attributeIceChamberAccess, false) || getQuestStage(player, TempleOfIkov.questName) >= 4){
                // To be nice, you can "reset" the chest location by opening the gate.
                // This is a failsafe if the attribute gets "stuck", although I doubt it will happen.
                setAttribute(player, TempleOfIkov.attributeRandomChest, chestLocations.random())
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "The door won't open!")
            }
            return@on true
        }

        // D: Chest open
        on(Scenery.CLOSED_CHEST_35122, SCENERY, "open") { player, node ->
            animate(player, 536)
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.OPEN_CHEST_35123, -1)
            return@on true
        }

        // D: Chest shut
        on(Scenery.OPEN_CHEST_35123, SCENERY, "shut") { player, node ->
            animate(player, 536)
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CLOSED_CHEST_35122, -1)
            return@on true
        }

        // D: Chest search. I assure you this is the worst feature of this goddamn quest.
        // 1/6 of the chests are randomly chosen to have 1-5 ice arrows, and it REMEMBERS...
        // So clicking on the same one doesn't randomize it. Good luck and fuck you.
        on(Scenery.OPEN_CHEST_35123, SCENERY, "search") { player, node ->
            if (getAttribute(player, TempleOfIkov.attributeRandomChest, null) == null) {
                setAttribute(player, TempleOfIkov.attributeRandomChest, chestLocations.random())
            }
            if (getAttribute(player, TempleOfIkov.attributeRandomChest, null) == node.location){
                removeAttribute(player, TempleOfIkov.attributeRandomChest)
                val randomAmount = (1..5).random()
                addItemOrDrop(player, Items.ICE_ARROWS_78, randomAmount)
                sendItemDialogue(player, Item(Items.ICE_ARROWS_78, randomAmount), "You found some ice arrows!")
                setAttribute(player, TempleOfIkov.attributeIceArrows, true)
            } else {
                sendMessage(player, "You search the chest, but find nothing.")
            }
            return@on true
        }

        // 3: Allow access to Fire Warrior Door after pulling the lever
        on(Scenery.DOOR_92, SCENERY, "open") { player, node ->
            removeAttribute(player, TempleOfIkov.attributeWarriorInstance)
            if (getQuestStage(player, TempleOfIkov.questName) >= 3){
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door won't open.")
            }
            return@on true
        }


        // 3 - 4: Calls for the Fire Warrior, allows passing when Fire Warrior is defeated.
        on(Scenery.DOOR_93, SCENERY, "open") { player, node ->
            if (getQuestStage(player, TempleOfIkov.questName) >= 4){
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                if (getAttribute(player, TempleOfIkov.attributeWarriorInstance, null) == null) {
                    val npc = FireWarriorOfLesarkusNPC(NPCs.FIRE_WARRIOR_OF_LESARKUS_277, player, Location(2646, 9866, 0))
                    npc.init()
                    npc.isRespawn = false
                    npc.isWalks = false
                    npc.location = Location(2646, 9866, 0)
                    npc.direction = Direction.NORTH
                    submitIndividualPulse(player, object : Pulse() {
                        var counter = 0
                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    sendChat(npc, "You will not pass!")
                                }
                                2 -> {
                                    sendChat(npc, "Amitus! Setitii!")
                                    spawnProjectile(npc, player, 127) // Launch fire projectile
                                }
                                4 -> {
                                    npc.isWalks = true
                                    return true
                                }
                            }
                            return false
                        }
                    })

                    setAttribute(player, TempleOfIkov.attributeWarriorInstance, npc)
                    // This npc doesn't attack you immediately nor is aggressive at the start.
                } else {
                    sendMessage(player, "The door won't open.")
                }
            }
            return@on true
        }

        on(Items.STAFF_OF_ARMADYL_84, IntType.GROUNDITEM,"take") { player, node ->
            if (getQuestStage(player, TempleOfIkov.questName) >= 6 && getAttribute(player, TempleOfIkov.attributeChosenEnding, 0) == 1){
                sendMessage(player, "You decide not to steal the staff as you have agreed to help the Guardians")
            }
            val npcs = findLocalNPCs(player, intArrayOf(NPCs.GUARDIAN_OF_ARMADYL_274, NPCs.GUARDIAN_OF_ARMADYL_275), 4)
            if (npcs.isNotEmpty()) {
                sendChat(npcs[0], "That is not thine to take!")
                npcs[0].attack(player)
            } else {
                if(getQuestStage(player, TempleOfIkov.questName) == 5) {
                    setQuestStage(player, TempleOfIkov.questName, 6)
                    setAttribute(player, TempleOfIkov.attributeChosenEnding, 2)
                }
                PickupHandler.take(player, node as GroundItem)
            }
            return@on true
        }

    }
}
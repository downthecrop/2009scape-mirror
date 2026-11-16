package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.data.Quests
import core.api.*
import core.game.global.action.DoorHelper
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import kotlin.math.ceil

class TrollStrongholdListeners: InteractionListener {
    override fun defineListeners() {
        // Entrance to arena with Dad in it.
        on(intArrayOf(Scenery.ARENA_ENTRANCE_3782, Scenery.ARENA_ENTRANCE_3783), IntType.SCENERY, "open") { player, node ->
            // Only get the dialogue once.
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 1) {
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            }
            // Only allow players through when they start Troll Stronghold.
            // No one is allowed to go to GWD unless they start the Troll Stronghold quest.
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) > 0) {
                handleDoor(player, node.asScenery())
            } else {
                sendMessage(player, "You need to start the Troll Stronghold quest.")
            }
            return@on true;
        }

        // Not allowed to exit arena into the troll stronghold until Dad is defeated.
        on(intArrayOf(Scenery.ARENA_EXIT_3785, Scenery.ARENA_EXIT_3786), IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) < 5) {
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            } else {
                handleDoor(player, node.asScenery())
            }
            return@on true;
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.ARENA_ENTRANCE_3782, Scenery.ARENA_ENTRANCE_3783, Scenery.ARENA_EXIT_3785, Scenery.ARENA_EXIT_3786), "open") { entity, node ->
            DoorHelper.getDestination(entity, node.asScenery())
        }

        // Key to unlock the prison door
        on(Scenery.PRISON_DOOR_3780, IntType.SCENERY, "unlock") { player, node ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) >= 8) {
                handleDoor(player, node.asScenery())
            } else {
                if (inInventory(player, Items.PRISON_KEY_3135)) {
                    if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 5) {
                        setQuestStage(player, Quests.TROLL_STRONGHOLD, 8)
                    }
                    if (removeItem(player, Items.PRISON_KEY_3135)) {
                        handleDoor(player, node.asScenery())
                        sendMessage(player, "You unlock the prison door.")
                    }
                } else {
                    sendMessage(player, "The prison door is locked.")
                }
            }
            return@on true;
        }

        fun registerPickpocket(npcId: Int, itemId: Int) {
            on(npcId, IntType.NPC, "pickpocket") { player, node ->
                val btnpc = node as BerryAndTwigNpc
                lock(player, 3)
                queueScript(player, 0, QueueStrength.WEAK) { stage ->
                    when (stage) {
                        0 -> {
                            if (hasLevelDyn(player, Skills.THIEVING, 30)) {
                                animate(player, Animation(881))
                            } else {
                                sendMessage(player, "You need to be a level 30 thief to pickpocket ${btnpc.name}.")
                                return@queueScript stopExecuting(player)
                            }
                        }
                        3 -> {
                            val success: Boolean = success(player, Skills.THIEVING)
                            if (success) {
                                if (isQuestInProgress(player, Quests.TROLL_STRONGHOLD, 8, 10)) {
                                    addItemOrDrop(player, itemId)
                                    sendMessage(player, "You find a small key on ${btnpc.name}.")
                                } else {
                                    sendMessage(player, "You find nothing on ${btnpc.name}.")
                                }
                            } else {
                                sendChat(btnpc, "What you think you doing?")
                                btnpc.changeState(true, 50)
                                btnpc.attack(player)
                            }
                            return@queueScript stopExecuting(player)
                        }
                    }
                    return@queueScript keepRunning(player)
                }
                return@on true
            }
        }
        registerPickpocket(NPCs.TWIG_1128, Items.CELL_KEY_1_3136)
        registerPickpocket(NPCs.BERRY_1129, Items.CELL_KEY_2_3137)

        fun registerCellDoorUnlock(npcId: Int, message: String, itemId: Int, doorId: Int, yval: Int, firstStage: Int, secondStage: Int) {
            val DO_NOT_DISTURB = "I don't want players to talk to me because they will stop my cobbled-together autowalk script"
            fun doUnlock(player: Player, node: Node) {
                if (inInventory(player, itemId)) {
                    sendMessage(player, "You unlock the cell door.")
                    if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == 8) {
                        setQuestStage(player, Quests.TROLL_STRONGHOLD, firstStage)
                    } else if (getQuestStage(player, Quests.TROLL_STRONGHOLD) == secondStage) {
                        setQuestStage(player, Quests.TROLL_STRONGHOLD, 11)
                    }
                    // Animate npc leaving cell.
                    val npc = findNPC(npcId)!!
                    sendNPCDialogue(player, npcId, message)
                    npc.walkRadius = 40
                    npc.isWalks = false
                    setAttribute(npc, DO_NOT_DISTURB, true)
                    // This is basically chaining a series of walks + door handler.
                    var counter = 0
                    var trigger = true
                    var targetLocation = npc.location
                    fun walkTo(dest: Location) {
                        forceWalk(npc, dest, "dumb")
                        targetLocation = dest
                    }
                    queueScript(npc, 0, QueueStrength.STRONG) { stage ->
                        if (npc.location.equals(targetLocation)) {
                            trigger = true
                        }
                        if (trigger) {
                            when (counter) {
                                0 -> walkTo(Location(2831, yval, 0))
                                1 -> {
                                    handleDoor(npc, node.asScenery())
                                    targetLocation = Location(2832, yval, 0)
                                }
                                2 -> walkTo(Location(2836, yval, 0))
                                3 -> walkTo(Location(2836, 10061, 0))
                                4 -> walkTo(Location(2824, 10050, 0))
                                5 -> {
                                    // Out of sight death to trigger respawn.
                                    npc.teleport(Location(2823, 10035, 0))
                                    npc.startDeath(null)
                                    npc.walkRadius = 11
                                    npc.isWalks = true
                                    removeAttribute(npc, DO_NOT_DISTURB)
                                    return@queueScript stopExecuting(npc)
                                }
                            }
                            trigger = false
                            counter += 1
                        }
                        // Keep counting, if more than 100 ticks, end this script as a failsafe.
                        if (stage > 100) {
                            npc.startDeath(null)
                            npc.walkRadius = 11
                            npc.isWalks = true
                            removeAttribute(npc, DO_NOT_DISTURB)
                            return@queueScript stopExecuting(npc)
                        }
                        return@queueScript keepRunning(npc)
                    }
                } else {
                    sendMessage(player, "The cell door is locked.")
                }
            }
            onUseWith(IntType.SCENERY, itemId, doorId) { player, _, with ->
                doUnlock(player, with)
                return@onUseWith true
            }
            on(doorId, IntType.SCENERY, "unlock") { player, node ->
                doUnlock(player, node)
                return@on true
            }
            on(npcId, IntType.NPC, "talk-to") { player, node ->
                val npc = node.asNpc()
                if (!getAttribute(npc, DO_NOT_DISTURB, false)) {
                    openDialogue(player, npc.id, npc) // The dialogue is not implemented but if it ever is, it will work
                }
                return@on true
            }
        }
        registerCellDoorUnlock(NPCs.EADGAR_1113, "Thanks! I'm off back home!", Items.CELL_KEY_1_3136, Scenery.CELL_DOOR_3765, 10082, 9, 10)
        registerCellDoorUnlock(NPCs.GODRIC_1114, "Thank you, my friend.", Items.CELL_KEY_2_3137, Scenery.CELL_DOOR_3767, 10078, 10, 9)

        // Exit
        on(Scenery.EXIT_3761, IntType.SCENERY, "open") { player, _ ->
            player.properties.teleportLocation = Location.create(2827, 3646, 0)
            return@on true
        }

        // Reentry Secret Door
        on(Scenery.SECRET_DOOR_3762, IntType.SCENERY, "open") { player, _ ->
            if (getQuestStage(player, Quests.TROLL_STRONGHOLD) >= 8) {
                player.properties.teleportLocation = Location.create(2824, 10050, 0)
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
    }

    /**
     * Method used to determine the success of a player when thieving.
     * Copied from ThievingGuidePlugin
     * @param player the player.
     * @return `True` if successful, `False` if not.
     */
    fun success(player: Player, skill: Int): Boolean {
        val level = player.getSkills().getLevel(skill).toDouble()
        val req = 30.0
        val successChance = ceil((level * 50 - req) / req / 3 * 4)
        val roll = RandomFunction.random(99)
        return successChance >= roll
    }
}
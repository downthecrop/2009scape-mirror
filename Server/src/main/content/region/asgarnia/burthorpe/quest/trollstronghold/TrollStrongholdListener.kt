package content.region.asgarnia.burthorpe.quest.trollstronghold

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import kotlin.math.ceil

class TrollStrongholdListener: InteractionListener {

    override fun defineListeners() {
        // Entrance to arena with Dad in it.
        on(intArrayOf(Scenery.ARENA_ENTRANCE_3782, Scenery.ARENA_ENTRANCE_3783), IntType.SCENERY, "open"){ player, node ->
            // Only get the dialogue once.
            if (getQuestStage(player, TrollStronghold.questName) == 1) {
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            }
            // Only allow players through when they start Troll Stronghold.
            // No one is allowed to go to GWD unless they start the Troll Stronghold quest.
            if (getQuestStage(player, TrollStronghold.questName) > 0) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "You need to start the Troll Stronghold quest.")
            }
            return@on true;
        }

        // Not allowed to exit arena into the troll stronghold until Dad is defeated.
        on(intArrayOf(Scenery.ARENA_EXIT_3785, Scenery.ARENA_EXIT_3786), IntType.SCENERY, "open"){ player, node ->
            if (getQuestStage(player, TrollStronghold.questName) < 5){
                openDialogue(player, DadDialogueFile(1), findNPC(NPCs.DAD_1125)!!)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true;
        }

        // Key to unlock the prison door
        on(Scenery.PRISON_DOOR_3780, IntType.SCENERY, "unlock"){ player, node ->
            if (getQuestStage(player, TrollStronghold.questName) >= 8){
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                if (inInventory(player, Items.PRISON_KEY_3135)) {
                    if (getQuestStage(player, TrollStronghold.questName) == 5) {
                        setQuestStage(player, TrollStronghold.questName, 8)
                    }
                    if (removeItem(player, Items.PRISON_KEY_3135)) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        sendMessage(player, "You unlock the prison door.")
                    }
                } else {
                    sendMessage(player, "The prison door is locked.")
                }
            }
            return@on true;
        }

        // Pickpocket Twig
        on(NPCs.TWIG_1128, IntType.NPC, "pickpocket") { player, node ->
            val npc = node.asNpc()
            player.lock()
            submitWorldPulse(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> {
                            if (hasLevelDyn(player, Skills.THIEVING, 30)){
                                animate(player, Animation(881))
                            } else {
                                sendMessage(player, "You need to be a level 30 thief to pickpocket Twig.")
                                return true
                            }
                        }
                        3 -> {
                            val success: Boolean = success(player, Skills.THIEVING)
                            if(success){
                                if(isQuestInProgress(player, TrollStronghold.questName, 8, 10)) {
                                    addItem(player, Items.CELL_KEY_1_3136)
                                    sendMessage(player, "You find a small key on Twig.")
                                } else {
                                    sendMessage(player, "You find nothing on Twig.")
                                }
                            } else {
                                sendChat(npc, "What you think you doing?")
                                transformNpc(npc, NPCs.TWIG_1126, 50)
                                npc.attack(player)
                            }
                            player.unlock()
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }

        // Pickpocket Berry
        on(NPCs.BERRY_1129, IntType.NPC, "pickpocket") { player, node ->
            val npc = node.asNpc()
            player.lock()
            submitWorldPulse(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> {
                            if (hasLevelDyn(player, Skills.THIEVING, 30)){
                                animate(player, Animation(881))
                            } else {
                                sendMessage(player, "You need to be a level 30 thief to pickpocket Berry.")
                                return true
                            }
                        }
                        3 -> {
                            val success: Boolean = success(player, Skills.THIEVING)
                            if(success){
                                if(isQuestInProgress(player, TrollStronghold.questName, 8, 10)) {
                                    addItem(player, Items.CELL_KEY_2_3137)
                                    sendMessage(player, "You find a small key on Berry.")
                                } else {
                                    sendMessage(player, "You find nothing on Berry.")
                                }
                            } else {
                                sendChat(npc, "What you think you doing?")
                                transformNpc(npc, NPCs.BERRY_1127, 50)
                                npc.attack(player)
                            }
                            player.unlock()
                            return true
                        }
                    }
                    return false
                }
            })
            return@on true
        }

        // Key to unlock Mad Eadgar's cell
        fun unlockMadEadgarCellDoor(player: Player, node: Node) {
            if (inInventory(player, Items.CELL_KEY_1_3136)){
                sendMessage(player, "You unlock the cell door.")
                if (getQuestStage(player, TrollStronghold.questName) == 8) {
                    setQuestStage(player, TrollStronghold.questName, 9)
                } else if (getQuestStage(player, TrollStronghold.questName) == 10) {
                    setQuestStage(player, TrollStronghold.questName, 11)
                }
                // Animate Mad Eadgar leaving cell.
                val npc = findNPC(NPCs.EADGAR_1113)!!
                sendNPCDialogue(player, NPCs.EADGAR_1113, "Thanks! I'm off back home!")
                npc.walkRadius = 40
                submitWorldPulse(object : Pulse(0) {
                    // This is basically chaining a series of walks + and door handler.
                    var count = 0
                    var stage = 0
                    var trigger = true
                    var targetLocation = Location(0, 0, 0)
                    override fun pulse(): Boolean {
                        if (npc.location.equals(targetLocation)){
                            stage++
                            trigger = true
                        }
                        if (trigger) {
                            trigger = false
                            when (stage) {
                                0 -> forceWalk(npc, Location(2831, 10082, 0), "dumb").also {
                                    npc.walkRadius = 11
                                    npc.setWalks(false)
                                    targetLocation = Location(2831, 10082, 0)
                                }
                                1 -> DoorActionHandler.handleAutowalkDoor(npc, node.asScenery()).also { targetLocation = Location(2832, 10082, 0) }
                                // Delay 2 ticks for DoorActionHandler to finish
                                4 -> forceWalk(npc, Location(2836, 10082, 0), "dumb").also { targetLocation = Location(2836, 10082, 0) }
                                5 -> forceWalk(npc, Location(2836, 10061, 0), "dumb").also { targetLocation = Location(2836, 10061, 0) }
                                6 -> forceWalk(npc, Location(2824, 10050, 0), "dumb").also { targetLocation = Location(2824, 10050, 0) }
                                7 -> {
                                    // Out of sight death to trigger respawn.
                                    npc.teleport(Location(2823, 10035, 0))
                                    npc.startDeath(null)
                                    npc.setWalks(true)
                                    return true
                                }
                            }
                        }
                        // Keep counting, if more than 100 ticks, end this pulse as a failsafe.
                        if (count > 100) {
                            npc.startDeath(null)
                            npc.setWalks(true)
                            return true
                        }
                        return false
                    }
                })

            } else {
                sendMessage(player, "The cell door is locked.")
            }
        }
        onUseWith(IntType.SCENERY, Items.CELL_KEY_1_3136, Scenery.CELL_DOOR_3765) { player, used, with ->
            unlockMadEadgarCellDoor(player, with)
            return@onUseWith true
        }
        on(Scenery.CELL_DOOR_3765, IntType.SCENERY, "unlock"){ player, node ->
            unlockMadEadgarCellDoor(player, node)
            return@on true
        }


        // Key to unlock Godric's cell
        fun unlockGodricCellDoor(player: Player, node: Node) {
            if (inInventory(player, Items.CELL_KEY_2_3137)){
                sendMessage(player, "You unlock the cell door.")
                if (getQuestStage(player, TrollStronghold.questName) == 8) {
                    setQuestStage(player, TrollStronghold.questName, 10)
                } else if (getQuestStage(player, TrollStronghold.questName) == 9) {
                    setQuestStage(player, TrollStronghold.questName, 11)
                }
                // Animate Godric leaving cell.
                val npc = findNPC(NPCs.GODRIC_1114)!!
                sendNPCDialogue(player, NPCs.GODRIC_1114, "Thank you, my friend.")
                submitWorldPulse(object : Pulse(0) {
                    // This is basically chaining a series of walks + and door handler.
                    var count = 0
                    var stage = 0
                    var trigger = true
                    var targetLocation = Location(0, 0, 0)
                    override fun pulse(): Boolean {
                        if (npc.location.equals(targetLocation)){
                            stage++
                            trigger = true
                        }
                        if (trigger) {
                            trigger = false
                            when (stage) {
                                0 -> forceWalk(npc, Location(2831, 10078, 0), "dumb").also {
                                    npc.walkRadius = 11
                                    npc.setWalks(false)
                                    targetLocation = Location(2831, 10078, 0)
                                }
                                1 -> DoorActionHandler.handleAutowalkDoor(npc, node.asScenery()).also { targetLocation = Location(2832, 10078, 0) }
                                // Delay 2 ticks for DoorActionHandler to finish
                                4 -> forceWalk(npc, Location(2836, 10078, 0), "dumb").also { targetLocation = Location(2836, 10078, 0) }
                                5 -> forceWalk(npc, Location(2836, 10061, 0), "dumb").also { targetLocation = Location(2836, 10061, 0) }
                                6 -> forceWalk(npc, Location(2824, 10050, 0), "dumb").also { targetLocation = Location(2824, 10050, 0) }
                                7 -> {
                                    // Out of sight death to trigger respawn.
                                    npc.teleport(Location(2823, 10035, 0))
                                    npc.startDeath(null)
                                    npc.setWalks(true)
                                    return true
                                }
                            }
                        }
                        // Keep counting, if more than 100 ticks, end this pulse as a failsafe.
                        count++
                        if (count > 100) {
                            npc.startDeath(null)
                            npc.setWalks(true)
                            return true
                        }
                        return false
                    }
                })

            } else {
                sendMessage(player, "The cell door is locked.")
            }
        }
        onUseWith(IntType.SCENERY, Items.CELL_KEY_2_3137, Scenery.CELL_DOOR_3767) { player, used, with ->
            unlockGodricCellDoor(player, with)
            return@onUseWith true
        }
        on(Scenery.CELL_DOOR_3767, IntType.SCENERY, "unlock"){ player, node ->
            unlockGodricCellDoor(player, node)
            return@on true;
        }

        // Exit
        on(Scenery.EXIT_3761, IntType.SCENERY, "open"){ player, _ ->
            player.properties.teleportLocation = Location.create(2827, 3646, 0)
            return@on true
        }

        // Reentry Secret Door
        on(Scenery.SECRET_DOOR_3762, IntType.SCENERY, "open"){ player, _ ->
            if (getQuestStage(player, TrollStronghold.questName) >= 8) {
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
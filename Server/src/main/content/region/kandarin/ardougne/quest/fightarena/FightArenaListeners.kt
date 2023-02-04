package content.region.kandarin.ardougne.quest.fightarena

import content.region.kandarin.ardougne.quest.fightarena.npcs.*
import content.region.kandarin.ardougne.quest.fightarena.npcs.enemies.GeneralNPC
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class FightArenaListeners : InteractionListener {
    companion object {

        const val GENERAL = 258
        const val BARMAN = 259
        const val KELVIN = 260
        const val JOE = 261
        const val FIGHTSLAVE = 262
        const val HENGARD = 263
        const val LADY = 264
        const val JEREMY = 265
        const val JEREMYRESCUE = 266
        const val JUSTIN = 267

        const val OTHERPRISONDOORS = Scenery.PRISON_DOOR_79
        const val JEREMYPRISONDOOR = Scenery.PRISON_DOOR_80
        const val MAINDOORS = Scenery.DOOR_81
        const val CENTERDOOR = Scenery.DOOR_82
        const val LAZYGUARD = Scenery.A_LAZY_KHAZARD_GUARD_41494

        const val ARMOR_ONLY_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41506
        const val HELM_ONLY_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41507
        const val EMPTY_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41508
        const val FULL_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41490


        private const val HELMET = Items.KHAZARD_HELMET_74
        private const val ARMOUR = Items.KHAZARD_ARMOUR_75
        private const val CELLKEY = Items.KHAZARD_CELL_KEYS_76

    //-----------------------------------------------------------------------------------------------------

        val Jeremy = NPC(NPCs.JEREMY_SERVIL_265, Location.create(2616,3167,0))
        val General = GeneralNPC(NPCs.GENERAL_KHAZARD_258, Location.create(2605,3156,0))
    }

    init {
        Jeremy.init()
        Jeremy.isWalks = true

        General.init()
        General.isWalks = true
    }

    override fun defineListeners() {

        //-------------------------  Npcs  -------------------------

        on(GENERAL, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, GeneralKhazardDialogue(), npc)
            return@on true
        }

        on(BARMAN, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, KhazardBarmanDialogue(), npc)
            return@on true
        }

        on(HENGARD, IntType.NPC, "talk-to") { player, npc ->
                openDialogue(player, HengradDialogue(), npc)
            return@on true
        }

        on(LADY, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, LadyServilDialogue(), npc)
            return@on true
        }

        on(JEREMY, NPC, "talk-to") { player, npc ->
            openDialogue(player, JeremyServilDialogue(), npc)
            return@on true
        }

        on(JEREMYRESCUE, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JeremyServilRescuedDialogue(), npc)
            return@on true
        }

        on(JUSTIN, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JustinServilDialogue(), npc)
            return@on true
        }

        on(LAZYGUARD, SCENERY, "talk-to") { player, node ->
            openDialogue(player, KhazardGuardDialogue(), node)
            return@on true
        }

        //-------------------------  Objects  -------------------------


        // Steal Khazard set from suit of Armour.
        on(FULL_ARMOR_STAND, IntType.SCENERY, "borrow") { player, _ ->
            if (player.questRepository.getStage("Fight Arena") == 10 && player.inventory.containItems(HELMET) && player.inventory.containItems(ARMOUR)) {
                sendMessage(player, "Nothing interesting happens.")
                return@on false
            } else if (player.questRepository.getStage("Fight Arena") == 10 && player.equipment.containItems(HELMET) || player.bank.containItems(HELMET) || player.equipment.containItems(ARMOUR) || player.bank.containItems(ARMOUR)) {
                sendMessage(player, "Nothing interesting happens.")
                return@on false
            } else if (player.questRepository.getStage("Fight Arena") == 10 && player.inventory.containItems(ARMOUR) || player.inventory.containItems(HELMET)) {
                sendMessage(player, "Nothing interesting happens.")
                return@on false
            } else if (player.questRepository.getStage("Fight Arena") == 10 && player.inventory.isFull) {
                sendMessage(player, "You could borrow this suit of armour if you had space in your inventory.")
                return@on false
            } else {
                sendMessage(player, "You borrow the suit of armour. It looks like it's just your size.")
                addItem(player, HELMET, 1)
                addItem(player, ARMOUR, 1)
            }
        }


        // Doors leading to the prison.
        // Source: https://youtu.be/-wV5dIyM0YM?t=60
        on(MAINDOORS, IntType.SCENERY, "open") { player, maingate ->
            when (player.location.y) {
                3171 -> DoorActionHandler.handleAutowalkDoor(player, maingate.asScenery())
                3172 -> {
                    if (isEquipped(player, HELMET) && isEquipped(player, ARMOUR)) {
                        openDialogue(player, EastDoorSupportDialogue())
                    } else {
                        sendPlayerDialogue(player, "This door appears to be locked.")
                    }
                    return@on true
                }
            }
            when (player.location.x) {
                2585 -> DoorActionHandler.handleAutowalkDoor(player, maingate.asScenery())
                2584 -> {
                    if (isEquipped(player, HELMET) && isEquipped(player, ARMOUR)) {
                        openDialogue(player, WestDoorSupportDialogue())
                    } else {
                        sendPlayerDialogue(player, "This door appears to be locked.")
                    }
                    return@on true
                }
            }
            return@on true
        }

        // Using key on cell door to freeing Jeremy.
        onUseWith(IntType.SCENERY, CELLKEY, JEREMYPRISONDOOR) { player, _, _ ->
            if (player.inventory.containsAtLeastOneItem(CELLKEY) && player.questRepository.getStage("Fight Arena") == 68 || player.questRepository.getStage("Fight Arena") == 88) {
                openDialogue(player, JeremyServilDialogue())
            } else {
                sendMessage(player, "The cell gate is securely locked.")
                return@onUseWith false
            }
            return@onUseWith true
        }


        // Using key on other cell doors.
        onUseWith(IntType.SCENERY, CELLKEY, OTHERPRISONDOORS) { player, _, _ ->
            if (player.inventory.containsAtLeastOneItem(CELLKEY) && player.questRepository.getStage("Fight Arena") in 68..72) {
                sendPlayerDialogue(player, "I don't want to attract too much attention by freeing all the prisoners. I need to find Jeremy and he's not in this cell.")
            } else {
                sendMessage(player, "The cell gate is securely locked.")
            }
            return@onUseWith false
        }


        // Doors to rest of the cell.
        on(OTHERPRISONDOORS, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }

        // Block Jeremy doors.
        on(JEREMYPRISONDOOR, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }


        // Exit arena.
        on(CENTERDOOR, IntType.SCENERY, "open") { player, centerdoor ->
            if (player.questRepository.getStage("Fight Arena") >= 91) {
                DoorActionHandler.handleAutowalkDoor(player, centerdoor.asScenery())
                return@on true
            } else if (player.questRepository.getStage("Fight Arena") < 91) {
                sendNPCDialogue(player, 255, "And where do you think you're going? Only General Khazard decides who fights in the arena. Get out of here.", FacialExpression.ANNOYED)
                return@on false
            } else {
                sendMessage(player, "The gate is locked.")
                return@on false
            }
        }
    }

    override fun defineDestinationOverrides() {

        //----------------------  Improvements  ----------------------

        // Access to talk with Jeremy through the prison bars.
        setDest(IntType.NPC, intArrayOf(JEREMY), "talk-to") { _, _ ->
            return@setDest Location.create(2617, 3167, 0)
        }

        // Access to talk Khazard barman through counter at the bar.
        setDest(IntType.NPC, intArrayOf(BARMAN), "talk-to") { _, _ ->
            return@setDest Location.create(2566, 3142, 0)
        }

        // Access to talk Fightslave through the prison bars.
        setDest(IntType.NPC, intArrayOf(FIGHTSLAVE), "talk-to") { _, _ ->
            return@setDest Location.create(2617, 3162, 0).transform(0, 1, 0)
        }

        // Access to talk Kelvin through the prison bars.
        setDest(IntType.NPC, intArrayOf(KELVIN), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

        // Access to talk Joe through the prison bars.
        setDest(IntType.NPC, intArrayOf(JOE), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

    }

    //-------------------------  Dialogue support  -------------------------

    // Support for prison doors.
    // Source: https://youtu.be/dCJhnLk1vMk?t=145
    class EastDoorSupportDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.KHAZARD_GUARD_257)

            when (stage) {
                0 -> player!!.faceLocation(location(2617, 3170, 0)).also { playerl(FacialExpression.NEUTRAL, "This door appears to be locked.") }.also { stage++ }
                1 -> player!!.faceLocation(location(2603, 3155, 0)).also { npcl(FacialExpression.NEUTRAL, "Nice observation guard. You could have just asked to be let in like a normal person.") }.also { stage++ }
                2 -> {
                    end()
                    setQuestStage(player!!, FightArena.FightArenaQuest, 20)
                    DoorActionHandler.handleAutowalkDoor(player, getScenery(2617, 3172, 0)).also { player!!.unlock() }
                }
            }
        }
    }

    class WestDoorSupportDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.KHAZARD_GUARD_257)

            when (stage) {
                0 -> player!!.faceLocation(location(2585, 3141, 0)).also { playerl(FacialExpression.NEUTRAL, "This door appears to be locked.") }.also { stage++ }
                1 -> player!!.faceLocation(location(2603, 3155, 0)).also { npcl(FacialExpression.NEUTRAL, "Nice observation guard. You could have just asked to be let in like a normal person.") }.also { stage++ }

                2 -> {
                    end()
                    setQuestStage(player!!, FightArena.FightArenaQuest, 20)
                    DoorActionHandler.handleAutowalkDoor(player, getScenery(2584, 3141, 0)).also { player!!.unlock() }
                }
            }
        }
    }
}
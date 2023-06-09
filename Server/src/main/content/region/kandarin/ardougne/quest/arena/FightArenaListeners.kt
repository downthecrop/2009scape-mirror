package content.region.kandarin.ardougne.quest.arena

import content.region.kandarin.ardougne.quest.arena.dialogue.*
import content.region.kandarin.ardougne.quest.arena.npc.GeneralNPC
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

        const val GENERAL = NPCs.GENERAL_KHAZARD_258
        const val BARMAN = NPCs.KHAZARD_BARMAN_259
        const val KELVIN = NPCs.KELVIN_260
        const val JOE = NPCs.JOE_261
        const val FIGHTSLAVE = NPCs.FIGHTSLAVE_262
        const val HENGARD = NPCs.HENGRAD_263
        const val JEREMY_A = NPCs.JEREMY_SERVIL_265
        const val JEREMY_B = NPCs.JEREMY_SERVIL_266
        const val JUSTIN = NPCs.JUSTIN_SERVIL_267

        const val CELL_DOOR_1 = Scenery.PRISON_DOOR_79
        const val CELL_DOOR_2 = Scenery.PRISON_DOOR_80
        const val MAIN_DOOR = Scenery.DOOR_81
        const val CENTER_DOOR = Scenery.DOOR_82

        const val A_LAZY_GUARD_1 = Scenery.A_LAZY_KHAZARD_GUARD_41494
        const val A_LAZY_GUARD_2 = Scenery.A_LAZY_KHAZARD_GUARD_41496
        const val A_LAZY_GUARD_3 = Scenery.A_LAZY_KHAZARD_GUARD_41497

        val FULL_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41490
        val FULL_ARMOR_STAND_1 = getScenery(2619, 3196, 0)
        val ONLY_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41506
        val ONLY_HELM_STAND = Scenery.A_SUIT_OF_ARMOUR_41507
        val EMPTY_STAND = Scenery.A_SUIT_OF_ARMOUR_41508

        private const val HELMET = Items.KHAZARD_HELMET_74
        private const val ARMOR = Items.KHAZARD_ARMOUR_75
        private const val CELL_KEY = Items.KHAZARD_CELL_KEYS_76

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

        on(GENERAL, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, GeneralKhazardDialogue(), npc)
            return@on true
        }

        on(BARMAN, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, KhazardBarmanDialogue(), npc)
            return@on true
        }

        on(JEREMY_A, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JeremyServilADialogue(), npc)
            return@on true
        }

        on(JEREMY_B, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JeremyServilBDialogue(), npc)
            return@on true
        }

        on(HENGARD, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, HengradDialogue(), npc)
            return@on true
        }

        on(A_LAZY_GUARD_1, SCENERY, "talk-to") { player, node ->
            openDialogue(player, ALazyGuardDialogue(), node)
            return@on true
        }
        on(A_LAZY_GUARD_3, SCENERY, "talk-to") { player, node ->
            openDialogue(player, ALazyGuardDialogue(), node)
            return@on true
        }

        on(JUSTIN, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JustinServilDialogue(), npc)
            return@on true
        }

        on(FULL_ARMOR_STAND, IntType.SCENERY, "borrow") { player, _ ->
            if (player.questRepository.getStage("Fight Arena") >= 10 && !inEquipmentOrInventory(player, HELMET) && !inEquipmentOrInventory(player, ARMOR) && freeSlots(player) >= 2) {
                replaceScenery(FULL_ARMOR_STAND_1!!.asScenery(), EMPTY_STAND, 10,location(2619, 3196, 0))
                sendMessage(player, "You borrow the suit of armour. It looks like it's just your size.")
                addItem(player, ARMOR, 1)
                addItem(player, HELMET, 1)
            } else if (!inEquipmentOrInventory(player, ARMOR) && freeSlots(player) >= 1){
                replaceScenery(FULL_ARMOR_STAND_1!!.asScenery(), ONLY_HELM_STAND, 10,location(2619, 3196, 0))
                addItem(player, ARMOR, 1)
                sendMessage(player, "You borrow the suit of helmet. It looks like it's just your size.")
            } else if (!inEquipmentOrInventory(player, HELMET) && freeSlots(player) >= 1){
                replaceScenery(FULL_ARMOR_STAND_1!!.asScenery(), ONLY_ARMOR_STAND, 10,location(2619, 3196, 0))
                addItem(player, HELMET, 1)
                sendMessage(player, "You could borrow this suit of armour if you had space in your inventory.")
            } else if (freeSlots(player) == 0) {
                sendMessage(player, "You could borrow this suit of armour if you had space in your inventory.")
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        on(A_LAZY_GUARD_2, IntType.SCENERY, "steal-keys") { player, _ ->
            player.faceLocation(location(2618, 3144, 0))
            setVarbit(player,5627,3, true)
            animate(player, 2286)
            addItemOrDrop(player, CELL_KEY)
            sendMessage(player, "You pick up the keys from the table.")
            return@on true
        }

        on(MAIN_DOOR, IntType.SCENERY, "open") { player, maingate ->
            when (player.location.y) {
                3171 -> DoorActionHandler.handleAutowalkDoor(player, maingate.asScenery())
                3172 -> {
                    if (allInEquipment(player, HELMET, ARMOR)) {
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
                    if (allInEquipment(player, HELMET, ARMOR)) {
                        openDialogue(player, WestDoorSupportDialogue())
                    } else {
                        sendPlayerDialogue(player, "This door appears to be locked.")
                    }
                    return@on true
                }
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, CELL_KEY, CELL_DOOR_2) { player, _, _ ->
            openDialogue(player, JeremyServilADialogue())
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, CELL_KEY, CELL_DOOR_1) { player, _, _ ->
        if (player.questRepository.getStage("Fight Arena") >= 68){
                sendDialogue(player, "I don't want to attract too much attention by freeing all the prisoners. I need to find Jeremy and he's not in this cell.")
            } else {
                sendMessage(player, "The cell gate is securely locked.")
            }
            return@onUseWith false
        }

        on(CELL_DOOR_1, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }

        on(CELL_DOOR_2, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }

        on(CENTER_DOOR, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getStage("Fight Arena") >= 91) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (player.questRepository.getStage("Fight Arena") < 91) {
                sendNPCDialogue(player, NPCs.KHAZARD_GUARD_255, "And where do you think you're going? Only General Khazard decides who fights in the arena. Get out of here.", FacialExpression.ANNOYED)
            } else {
                sendMessage(player, "The gate is locked.")
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {

        setDest(IntType.NPC, intArrayOf(JEREMY_A), "talk-to") { _, _ ->
            return@setDest Location.create(2617, 3167, 0)
        }

        setDest(IntType.NPC, intArrayOf(BARMAN), "talk-to") { _, _ ->
            return@setDest Location.create(2566, 3142, 0)
        }

        setDest(IntType.NPC, intArrayOf(FIGHTSLAVE), "talk-to") { player, _ ->
            if (inBorders(player, 2585, 3139, 2605, 3148)) {
                return@setDest Location.create(2595, 3141, 0)
            } else if (inBorders(player, 2616, 3162, 2617, 3165)) {
                return@setDest Location.create(2617, 3163, 0)
            } else {
                return@setDest Location.create(2617, 3159, 0)
            }
        }

        setDest(IntType.NPC, intArrayOf(KELVIN), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

        setDest(IntType.NPC, intArrayOf(JOE), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

        setDest(IntType.NPC, intArrayOf(HENGARD), "talk-to") { _, _ ->
            return@setDest Location.create(2600, 3142, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(CENTER_DOOR), "open") { player, _ ->
            if (inBorders(player, 2604, 3152, 2607, 3155)) {
                return@setDest Location.create(2605, 3153, 0)
            } else {
                return@setDest Location.create(2607, 3151, 0)
            }
        }

        setDest(IntType.SCENERY, intArrayOf(A_LAZY_GUARD_2), "steal-keys") { _, _ ->
            return@setDest Location.create(2619, 3143, 0)
        }

    }

    class EastDoorSupportDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.KHAZARD_GUARD_257)
            when (stage) {
                0 -> {
                    face(player!!, location(2617, 3170, 0))
                    playerl(FacialExpression.NEUTRAL, "This door appears to be locked.").also { stage++ }
                }

                1 -> {
                    face(player!!, location(2603, 3155, 0))
                    npcl(FacialExpression.NEUTRAL, "Nice observation guard. You could have just asked to be let in like a normal person.").also { stage++ }
                }
                2 -> {
                    end()
                    lock(player!!, 2)
                    setQuestStage(player!!, FightArena.FightArenaQuest, 20)
                    DoorActionHandler.handleAutowalkDoor(player, getScenery(2617, 3172, 0))
                }
            }
        }
    }

    class WestDoorSupportDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            npc = NPC(NPCs.KHAZARD_GUARD_257)
            when (stage) {
                0 -> {
                    face(player!!, location(2585, 3141, 0))
                    playerl(FacialExpression.NEUTRAL, "This door appears to be locked.").also { stage++ }
                }
                1 -> {
                    face(player!!, location(2603, 3155, 0))
                    npcl(FacialExpression.NEUTRAL, "Nice observation guard. You could have just asked to be let in like a normal person.").also { stage++ }
                }
                2 -> {
                    end()
                    lock(player!!, 2)
                    setQuestStage(player!!, FightArena.FightArenaQuest, 20)
                    DoorActionHandler.handleAutowalkDoor(player, getScenery(2584, 3141, 0))
                }
            }
        }
    }
}
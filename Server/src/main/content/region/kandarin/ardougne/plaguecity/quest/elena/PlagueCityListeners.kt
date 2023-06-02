package content.region.kandarin.ardougne.plaguecity.quest.elena

import content.region.kandarin.ardougne.plaguecity.dialogue.ManDialogue
import content.region.kandarin.ardougne.plaguecity.dialogue.WomanDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class PlagueCityListeners : InteractionListener {
    companion object {

        const val BUCKET_USES_ATTRIBUTE = "/save:elena:bucket"

        const val BRAVEK = NPCs.BRAVEK_711
        const val HEAD_MOURNER = NPCs.HEAD_MOURNER_716
        const val BILLI = 723

        val MANS = intArrayOf(NPCs.MAN_728,NPCs.MAN_729, NPCs.MAN_351)
        val WOMANS = intArrayOf(NPCs.WOMAN_352, NPCs.WOMAN_353, NPCs.WOMAN_354, NPCs.WOMAN_360, NPCs.WOMAN_362, NPCs.WOMAN_363)

        const val MUD_PILE = Scenery.MUD_PILE_2533
        const val GRILL = Scenery.GRILL_11423
        const val MUD_PATCH = Scenery.MUD_PATCH_11418
        const val PIPE = Scenery.PIPE_2542
        const val WARDROBE = Scenery.WARDROBE_2525
        const val LEFT_DOOR = Scenery.ARDOUGNE_WALL_DOOR_9738
        const val RIGHT_DOOR = Scenery.ARDOUGNE_WALL_DOOR_9330
        const val PLAGUE_TED_DOORS = Scenery.DOOR_2537
        const val HEAD_DOORS = Scenery.DOOR_35991
        const val BARREL = Scenery.BARREL_2530
        const val SPOOKY_STAIRS_DOWN = Scenery.SPOOKY_STAIRS_2522
        const val SPOOKY_STAIRS_UP = Scenery.SPOOKY_STAIRS_2523
        const val PRISON_DOORS = Scenery.DOOR_2526
        const val BRAVEK_DOORS = Scenery.DOOR_2528
        const val MANHOLE_CLOSED = Scenery.MANHOLE_2543
        const val MANHOLE_OPEN = Scenery.MANHOLE_2544
        const val MANHOLE_COVER = Scenery.MANHOLE_COVER_2545

        private const val SNAPE_GRASS = Items.SNAPE_GRASS_231
        private const val SPADE = Items.SPADE_952
        private const val ROPE = Items.ROPE_954
        private const val HANGOVER_CURE = Items.HANGOVER_CURE_1504
        private const val MAGIC_SCROLL = Items.A_MAGIC_SCROLL_1505
        private const val GAS_MASK = Items.GAS_MASK_1506
        private const val SMALL_KEY = Items.A_SMALL_KEY_1507
        private const val SCRUFFY_NOTE = Items.A_SCRUFFY_NOTE_1508
        private const val BOOK = Items.BOOK_1509
        private const val EMPTY_BUCKET = Items.BUCKET_1925
        private const val BUCKET_OF_MILK = Items.BUCKET_OF_MILK_1927
        private const val BUCKET_OF_WATER = Items.BUCKET_OF_WATER_1929
        private const val CHOCOLATE_DUST = Items.CHOCOLATE_DUST_1975
        private const val CHOCOLATE_MILK = Items.CHOCOLATEY_MILK_1977

        private const val TRYING_TO_OPEN_GRILL = 3192
        private const val POUR_THE_WATER = 2283
        private const val CLIMB_LADDER = 828
        private const val GO_INTO_PIPE = 10580
        private const val TIE_THE_ROPE = 3191
        private const val DIG_WITH_SPADE = 830

    }

    override fun defineListeners() {

        on(BILLI, IntType.NPC, "talk-to") { player, _ ->
            sendMessage(player, "Billy isn't interested in talking.")
            return@on true
        }

        on(HEAD_MOURNER, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, HeadMournerDialogue())
            return@on true
        }

        on(MANS, IntType.NPC, "talk-to") { player, _ ->
            if(inBorders(player, 2496, 3280,2557, 3336)) {
                openDialogue(player, ManDialogue())
            }
            return@on true
        }

        on(WOMANS, IntType.NPC, "talk-to") { player, _ ->
            if(inBorders(player, 2496, 3280,2557, 3336)) {
                openDialogue(player, WomanDialogue())
            }
            return@on true
        }

        on(LEFT_DOOR, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getQuest("Plague City").isCompleted(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if(inBorders(player, 2556, 3298, 2557, 3301)){
                lock(player,2)
                sendMessage(player, "You pull on the large wooden doors...")
                runTask(player,2){
                    sendMessage(player, "...But they will not open.")
                }
            } else {
                face(player, Location.create(2559, 3302, 0))
                sendNPCDialogue(player, NPCs.MOURNER_2349, "Oi! What are you doing? Get away from there!")
            }
            return@on true
        }

        on(RIGHT_DOOR, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getQuest("Plague City").isCompleted(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if(inBorders(player, 2556, 3298, 2557, 3301)){
                lock(player,2)
                sendMessage(player, "You pull on the large wooden doors...")
                runTask(player,2){
                    sendMessage(player, "...But they will not open.")
                }
            } else {
                face(player, Location.create(2559, 3302, 0))
                sendNPCDialogue(player, NPCs.MOURNER_2349, "Oi! What are you doing? Get away from there!")
            }
            return@on true
        }

        on(MANHOLE_CLOSED, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node.asScenery(), MANHOLE_OPEN, -1)
            addScenery(MANHOLE_COVER, Location(2529, 3302, 0),0,10)
            sendMessage(player, "You pull back the manhole cover.")
            return@on true
        }

        on(MANHOLE_COVER, IntType.SCENERY, "close") { player, node ->
            removeScenery(node.asScenery())
            getScenery(location(2529, 3303, 0))?.let { replaceScenery(it, MANHOLE_CLOSED, -1) }
            sendMessage(player, "You close the manhole cover.")
            return@on true
        }

        on(MANHOLE_OPEN, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2514, 9739, 0))
            sendMessage(player, "You climb down through the manhole.")
            return@on true
        }

        on(BRAVEK_DOORS, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getStage("Plague City") >= 13) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendNPCDialogue(player,BRAVEK,"Go away, I'm busy! I'm... Umm... In a meeting!")
            }
            return@on true
        }

        on(MUD_PILE, IntType.SCENERY, "climb") { player, _ ->
            animate(player, CLIMB_LADDER)
            runTask(player, 2){
                teleport(player, Location(2566, 3332))
                sendDialogue(player, "You climb up the mud pile.")
            }
            return@on true
        }

        on(MAGIC_SCROLL, IntType.ITEM, "read") { player, _ ->
            sendItemDialogue(player, MAGIC_SCROLL, "You memorise what is written on the scroll.")
            removeItem(player, MAGIC_SCROLL)
            sendDialogue(player, "You can now cast the Ardougne Teleport spell provided you have the required runes and magic level.")
            return@on true
        }

        on(SCRUFFY_NOTE, IntType.ITEM, "read") { player, _ ->
            sendMessage(player, "You guess it really says something slightly different.")
            openInterface(player, 222).also { scruffyNote(player) }
            return@on true
        }

        on(HEAD_DOORS, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getStage("Plague City") == 11) {
                openDialogue(player, HeadMournerDialogue())
            } else if (player.questRepository.getStage("Plague City") == 16) {
                openDialogue(player, MournerDialogue())
            } else if (player.questRepository.getStage("Plague City") > 16) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, MournerDialogue())
            }
            return@on true
        }

        on(WARDROBE, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) == 0 && !inEquipmentOrInventory(player, GAS_MASK)) {
                sendItemDialogue(player, GAS_MASK, "You find a protective mask but you don't have enough room to take it.")
            } else if (inEquipmentOrInventory(player, GAS_MASK)) {
                sendMessage(player, "You search the wardrobe but you find nothing.")
            } else if (player.questRepository.getStage("Plague City") >= 2) {
                sendItemDialogue(player, GAS_MASK, "You find a protective mask.")
                addItem(player, GAS_MASK)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, BUCKET_OF_WATER, MUD_PATCH) { player, _, _ ->
            if (player.getAttribute(BUCKET_USES_ATTRIBUTE, 0) in 0..2 && removeItem(player, BUCKET_OF_WATER)) {
                animate(player, POUR_THE_WATER)
                player.dialogueInterpreter.sendDialogue(
                    "You pour water onto the soil.",
                    "The soil softens slightly."
                )
                player.incrementAttribute(BUCKET_USES_ATTRIBUTE, 1)
                addItem(player, EMPTY_BUCKET)
                return@onUseWith true
            } else if (player.getAttribute(BUCKET_USES_ATTRIBUTE, 0) == 3 && removeItem(player, BUCKET_OF_WATER)) {
                animate(player, POUR_THE_WATER)
                player.dialogueInterpreter.sendDialogue(
                    "You pour water onto the soil.",
                    "The soil is now soft enough to dig into."
                )
                player.setAttribute("/save:elena:dig", true)
                addItem(player, EMPTY_BUCKET)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, SPADE, MUD_PATCH) { player, _, _ ->
            if (player.getAttribute("/save:elena:dig", false) == true) {
                player.pulseManager.run(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> sendItemDialogue(player, SPADE,"You dig deep into the soft soil... Suddenly it crumbles away!")
                            1 -> animate(player, DIG_WITH_SPADE)
                            3 -> {
                                teleport(player, Location(2518, 9759))
                                setQuestStage(player, "Plague City", 4)
                                player.dialogueInterpreter.sendDialogue(
                                    "You fall through...",
                                    "...you land in the sewer.",
                                    "Edmond follows you down the hole."
                                )
                                return true
                            }
                        }
                        return false
                    }
                })
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(GRILL, IntType.SCENERY, "open") { player, _ ->
            if (player.questRepository.getStage("Plague City") == 4) {
                sendDialogue(player, "The grill is too secure. You can't pull it off alone.")
                animate(player, TRYING_TO_OPEN_GRILL)
                setQuestStage(player, "Plague City", 5)
            } else {
                sendDialogue(player, "There is a grill blocking your way")
            }
            return@on true
        }

        on(PIPE, IntType.SCENERY, "climb-up") { player, _ ->
            if (player.questRepository.getStage("Plague City") >= 7 && inEquipment(player, GAS_MASK)) {
                animate(player, GO_INTO_PIPE,true)
                forceMove(player, Location(2514, 9739, 0), Location(2514, 9734, 0), 0, 4,Direction.SOUTH)
                runTask(player, 3) {
                    teleport(player, Location(2529, 3304, 0))
                    sendDialogue(player, "You climb up through the sewer pipe.")
                }
            } else if (player.questRepository.getStage("Plague City") >= 7 && !inEquipment(player, GAS_MASK)) {
                sendNPCDialogue(player, NPCs.EDMOND_714, "I can't let you enter the city without your gasmask on.")
            } else {
                sendDialogue(player, "There is a grill blocking your way")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, ROPE, PIPE) { player, _, _ ->
            sendPlayerDialogue(player, "Maybe I should try opening it first.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, ROPE, GRILL) { player, _, _ ->
            if(removeItem(player, ROPE)) {
                player.pulseManager.run(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> forceWalk(player, Location.create(2514, 9740, 0), "SMART")
                            2 -> face(player, Location.create(2514, 9739, 0), -1)
                            3 -> {
                                animate(player, TIE_THE_ROPE)
                                setVarbit(player, 1787, 5, true) // Tied rope to the grill.
                            }
                            4 -> {
                                setQuestStage(player, "Plague City", 6)
                                sendItemDialogue(player, ROPE, "You tie the end of the rope to the sewer pipe's grill.")
                            }
                        }
                        return false
                    }
                })
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        class TedRehnisonDoors : DialogueFile() {
            override fun handle(componentID: Int, buttonID: Int) {
                npc = NPC(NPCs.TED_REHNISON_721)
                when (stage) {
                    0 -> if(removeItem(player!!, BOOK)){
                        playerl(FacialExpression.NEUTRAL, "I'm a friend of Jethick's, I have come to return a book he borrowed.").also { stage++ }
                    } else {
                        npcl(FacialExpression.FRIENDLY, "Go away. We don't want any.").also { stage = END_DIALOGUE }
                    }
                    1 -> npcl(FacialExpression.FRIENDLY, "Oh... why didn't you say, come in then.").also { stage++ }
                    2 -> sendItemDialogue(player!!, BOOK, "You hand the book to Ted as you enter.").also { stage++ }
                    3 -> npcl(FacialExpression.NEUTRAL, "Thanks, I've been missing that.").also { stage++ }
                    4 -> {
                        end()
                        DoorActionHandler.handleAutowalkDoor(player, getScenery(2531, 3328, 0))
                        setQuestStage(player!!, "Plague City", 9)
                    }
                }
            }
        }

        on(PLAGUE_TED_DOORS, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getStage("Plague City") >= 9) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, TedRehnisonDoors())
            }
            return@on true
        }

        on(BARREL, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, SMALL_KEY)) {
                sendMessage(player, "You don't find anything interesting.")
                return@on true
            } else {
                sendItemDialogue(player, SMALL_KEY, "You find a small key in the barrel.")
                addItem(player, SMALL_KEY)
            }
        }

        onUseWith(IntType.ITEM, CHOCOLATE_DUST, BUCKET_OF_MILK) { player, _, _ ->
            if (player.questRepository.hasStarted("Plague City") && removeItem(player, CHOCOLATE_DUST) && removeItem(player, BUCKET_OF_MILK)) {
                sendItemDialogue(player, CHOCOLATE_MILK, "You mix the chocolate into the bucket.")
                addItem(player, CHOCOLATE_MILK)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, SNAPE_GRASS, CHOCOLATE_MILK) { player, _, _ ->
            if (player.questRepository.hasStarted("Plague City") && removeItem(player, SNAPE_GRASS) && removeItem(player, CHOCOLATE_MILK)) {
                sendItemDialogue(player, HANGOVER_CURE, "You mix the snape grass into the bucket.")
                addItem(player, HANGOVER_CURE)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(SPOOKY_STAIRS_DOWN, IntType.SCENERY, "walk-down") { player, _ ->
            sendMessage(player, "You walk down the stairs...")
            teleport(player, Location.create(2537, 9671))
            return@on true
        }

        on(SPOOKY_STAIRS_UP, IntType.SCENERY, "walk-up") { player, _ ->
            teleport(player, Location.create(2536, 3271, 0))
            sendMessage(player, "You walk up the stairs...")
            return@on true
        }

        class ElenaDoorDialogue : DialogueFile() {
            override fun handle(componentID: Int, buttonID: Int) {
                npc = NPC(NPCs.ELENA_3215)
                when (stage) {
                    0 -> sendDialogue(player!!, "The door is locked.").also { stage++ }
                    1 -> npcl(FacialExpression.CRYING, "Hey get me out of here please!").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I would do but I don't have a key.").also { stage++ }
                    3 -> npcl(FacialExpression.SAD, "I think there may be one around somewhere. I'm sure I heard them stashing it somewhere.").also { stage++ }
                    4 -> options("Have you caught the plague?", "Okay, I'll look for it.").also { stage++ }
                    5 -> when (buttonID) {
                        1 -> playerl(FacialExpression.FRIENDLY, "Have you caught the plague?").also { stage = 6 }
                        2 -> playerl(FacialExpression.FRIENDLY, "Okay, I'll look for it.").also { stage = END_DIALOGUE }
                    }
                    6 -> npcl(FacialExpression.HALF_WORRIED, "No, I have none of the symptoms.").also { stage++ }
                    7 -> playerl(FacialExpression.THINKING, "Strange, I was told this house was plague infected.").also { stage++ }
                    8 -> playerl(FacialExpression.THINKING, "I suppose that was a cover up by the kidnappers.").also { stage = 4 }
                }
            }
        }

        onUseWith(IntType.SCENERY, SMALL_KEY, PRISON_DOORS) { player, _, _ ->
            if (player.questRepository.getStage("Plague City") >= 16) {
                DoorActionHandler.handleAutowalkDoor(player, core.game.world.map.RegionManager.getObject(Location(2539, 9672, 0))!!.asScenery())
                sendDialogue(player, "You unlock the door.")
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(PRISON_DOORS, IntType.SCENERY, "open") { player, node ->
            if (player.questRepository.getStage("Plague City") >= 99) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, ElenaDoorDialogue())
            }
            return@on true
        }
    }

    private fun scruffyNote(player: Player) {
        val scruffynotes =
            arrayOf(
                "Got a bncket of nnilk",
                "Tlen grind sorne lhoculate",
                "vnith a pestal and rnortar",
                "ald the grourd dlocolate to tho milt",
                "finales add 5cme snape gras5",
            )
        setInterfaceText(player, scruffynotes.joinToString("<br>"), 222, 5)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(GRILL), "open") { _, _ ->
            return@setDest Location.create(2514, 9739, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(PIPE), "climb-up") { _, _ ->
            return@setDest Location.create(2514, 9739, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(MANHOLE_OPEN), "climb-down") { _, _ ->
            return@setDest Location.create(2529, 3304, 0)
        }
    }
}
package content.region.kandarin.ardougne.quest.plaguecity

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.dialogue.mourners.MournerKidnapDialogueFile
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs09.consts.*
import core.game.node.scenery.Scenery as SceneryNode

class PlagueCityListeners : InteractionListener {
    companion object {

        const val BUCKET_USES_ATTRIBUTE = "/save:elena:bucket"
        const val ARDOUGNE_TELE_ATTRIBUTE = "/save:Ardougne:teleport"

        private const val TRYING_TO_OPEN_GRILL = 3192
        private const val POUR_THE_WATER = 2283
        private const val CLIMB_LADDER = 828
        private const val GO_INTO_PIPE = 10580
        private const val TIE_THE_ROPE = 3191
        private const val DIG_WITH_SPADE = 830

    }

    override fun defineListeners() {

        on(NPCs.BILLY_REHNISON_723, IntType.NPC, "talk-to") { player, _ ->
            sendMessage(player, "Billy isn't interested in talking.")
            return@on true
        }

        on(Scenery.MANHOLE_2543, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node.asScenery(), Scenery.MANHOLE_2544, -1)
            addScenery(Scenery.MANHOLE_COVER_2545, Location(node.location.x, node.location.y-1, 0),0,10)
            sendMessage(player, "You pull back the manhole cover.")
            return@on true
        }

        on(Scenery.MANHOLE_COVER_2545, IntType.SCENERY, "close") { player, node ->
            removeScenery(node.asScenery())
            getScenery(location(node.location.x, node.location.y+1, 0))?.let { replaceScenery(it, Scenery.MANHOLE_2543, -1) }
            sendMessage(player, "You close the manhole cover.")
            return@on true
        }

        on(Scenery.MANHOLE_2544, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location(2514, 9739, 0))
            face(player, Location.create(2514, 9740))
            sendMessage(player, "You climb down through the manhole.")
            return@on true
        }

        on(Scenery.DOOR_2528, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 13) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendNPCDialogue(player, NPCs.BRAVEK_711,"Go away, I'm busy! I'm... Umm... In a meeting!")
                // This typo is authentic
                sendMessage(player, "The door won't open")
            }
            return@on true
        }

        on(Scenery.MUD_PILE_2533, IntType.SCENERY, "climb") { player, _ ->
            animate(player, CLIMB_LADDER)
            queueScript(player, 2){
                teleport(player, Location(2566, 3332))
                sendDialogue(player, "You climb up the mud pile.")
                return@queueScript true
            }
            return@on true
        }

        on(Scenery.DUG_HOLE_11417, IntType.SCENERY, "Climb-down") { player, _ ->
            teleport(player, Location(2518, 9759))
            sendDialogue(player, "You climb down the tunnel into the sewer.")
            return@on true
        }

        on(Items.A_MAGIC_SCROLL_1505, IntType.ITEM, "read") { player, item ->
            if (removeItem(player, item)){
                if (getAttribute(player, ARDOUGNE_TELE_ATTRIBUTE, false)){
                    sendGraphics(Graphics(157,96), player.location)
                    impact(player, 0)
                    sendMessage(player, "The scroll explodes.")
                    addItem(player, Items.ASHES_592)
                }
                else{
                    sendItemDialogue(player, Items.A_MAGIC_SCROLL_1505, "You memorise what is written on the scroll.")
                    sendDialogue(player, "You can now cast the Ardougne Teleport spell provided you have the required runes and magic level.")
                    setAttribute(player, ARDOUGNE_TELE_ATTRIBUTE, true)
                }
                return@on true
            }
            return@on false
        }

        on(Items.A_SCRUFFY_NOTE_1508, IntType.ITEM, "read") { player, _ ->
            sendMessage(player, "You guess it really says something slightly different.")
            openInterface(player, 222).also { scruffyNote(player) }
            return@on true
        }

        class KidnapDoorDialogue : DialogueFile(){
            override fun handle(componentID: Int, buttonID: Int) {

                when(stage) {
                    0 -> {
                        // Face the door
                        face(player!!, Location.create(player!!.location.x, 3270, 0))
                        sendDialogue(player!!, "The door won't open. You notice a black cross on the door.").also { stage++ }
                    }
                    1 -> openDialogue(player!!, MournerKidnapDialogueFile(), NPC(NPCs.MOURNER_3216))
                }
            }

        }

        on(Scenery.DOOR_35991, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) > 16) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            else {
                // Make sure we are standing in front of the door
                forceWalk(player, Location.create(node.location.x, node.location.y), "smart")
                openDialogue(player, KidnapDoorDialogue())
            }
            return@on true
        }

        on(Scenery.WARDROBE_2525, IntType.SCENERY, "search") { player, _ ->
            if(getQuestStage(player, Quests.PLAGUE_CITY) >= 2){
                if (!hasAnItem(player, Items.GAS_MASK_1506).exists()){
                    if(freeSlots(player) == 0) {
                        sendItemDialogue(player, Items.GAS_MASK_1506, "You find a protective mask but you don't have enough room to take it.")
                        return@on true
                    }
                    else {
                        sendItemDialogue(player, Items.GAS_MASK_1506, "You find a protective mask.")
                        addItem(player, Items.GAS_MASK_1506)
                        return@on true
                    }
                }
            }
            // The player should not be given a mask for whatever reason
            sendMessage(player, "You search the wardrobe but you find nothing.")
            return@on false
        }

        onUseWith(IntType.SCENERY, Items.BUCKET_OF_WATER_1929, Scenery.MUD_PATCH_11418) { player, _, _ ->
            if (getAttribute(player, BUCKET_USES_ATTRIBUTE, 0) in 0..2 && removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                animate(player, POUR_THE_WATER)
                sendDialogueLines(player, "You pour water onto the soil.", "The soil softens slightly."
                )
                player.incrementAttribute(BUCKET_USES_ATTRIBUTE, 1)
                addItem(player, Items.BUCKET_1925)
                return@onUseWith true
            } else if (getAttribute(player, BUCKET_USES_ATTRIBUTE, 0) == 3 && removeItem(player, Items.BUCKET_OF_WATER_1929)) {
                animate(player, POUR_THE_WATER)
                player.incrementAttribute(BUCKET_USES_ATTRIBUTE, 1)
                sendDialogueLines(player,
                    "You pour water onto the soil.",
                    "The soil is now soft enough to dig into."
                )
                setAttribute(player, "/save:elena:dig", true)
                addItem(player, Items.BUCKET_1925)
            } else {
                sendDialogue(player, "You don't need to pour on any more water the soil is soft enough already.")
            }
            return@onUseWith true
        }


        onDig(Location.create(2566, 3332, 0)){ player: Player ->
            dig(player)
        }

        onUseWith(IntType.SCENERY, Items.SPADE_952, Scenery.MUD_PATCH_11418) { player, _, _ ->
            dig(player)
            return@onUseWith true
        }

        on(Scenery.GRILL_11423, IntType.SCENERY, "open") { player, _ ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) == 4) {
                sendDialogue(player, "The grill is too secure. You can't pull it off alone.")
                animate(player, TRYING_TO_OPEN_GRILL)
                setQuestStage(player, Quests.PLAGUE_CITY, 5)
            } else {
                sendDialogue(player, "There is a grill blocking your way")
            }
            return@on true
        }

        on(Scenery.PIPE_2542, IntType.SCENERY, "climb-up") { player, _ ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 7) {
                if (inEquipment(player, Items.GAS_MASK_1506)){
                    animate(player, GO_INTO_PIPE,true)
                    queueScript(player, 3) {
                        teleport(player, Location(2529, 3304, 0))
                        sendDialogue(player, "You climb up through the sewer pipe.")
                        return@queueScript stopExecuting(player)
                    }
                    forceMove(player, Location(2514, 9739, 0), Location(2514, 9734, 0), 0, 5,Direction.SOUTH)
                }
                else {
                    sendNPCDialogue(player, NPCs.EDMOND_714, "I can't let you enter the city without your gas mask on.")
                }
            }
            else {
                sendDialogue(player, "There is a grill blocking your way")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.PIPE_2542) { player, _, _ ->
            sendPlayerDialogue(player, "Maybe I should try opening it first.")
            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.GRILL_11423) { player, _, _ ->
            if(removeItem(player, Items.ROPE_954)) {
                queueScript(player, 1) { stage: Int ->
                    when (stage) {
                        0 -> forceWalk(player, Location.create(2514, 9740, 0), "SMART")
                        2 -> face(player, Location.create(2514, 9739, 0), -1)
                        3 -> {
                            animate(player, TIE_THE_ROPE)
                            setVarbit(player, 1787, 5, true) // Tied rope to the grill.
                        }

                        4 -> {
                            setQuestStage(player, Quests.PLAGUE_CITY, 6)
                            sendItemDialogue(
                                player,
                                Items.ROPE_954,
                                "You tie the end of the rope to the sewer pipe's grill."
                            )
                            return@queueScript stopExecuting(player)
                        }
                    }
                    return@queueScript delayScript(player, 1)
                }
                return@onUseWith true
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        class TedRehnisonDoors : DialogueFile() {
            override fun handle(componentID: Int, buttonID: Int) {
                npc = NPC(NPCs.TED_REHNISON_721)
                when (stage) {
                    0 -> {
                        npcl(FacialExpression.FRIENDLY, "Go away. We don't want any.").also {
                            if(hasAnItem(player!!, Items.BOOK_1509).exists()){
                                stage++
                            } else {
                                stage = END_DIALOGUE }
                        }
                    }
                    1 -> playerl(FacialExpression.NEUTRAL, "I'm a friend of Jethick's, I have come to return a book he borrowed.").also { stage++ }
                    // todo change this back after sendItemDialogue is fixed for having a single line before See #1885
                    2 -> npc(FacialExpression.FRIENDLY, "", "Oh... why didn't you say, come in then.", "").also { stage++ }
                    3 -> sendItemDialogue(player!!, Items.BOOK_1509, "You hand the book to Ted as you enter.").also {
                        DoorActionHandler.handleAutowalkDoor(player, getScenery(2531, 3328, 0))
                        setQuestStage(player!!, Quests.PLAGUE_CITY, 9)
                        removeItem(player!!, Items.BOOK_1509)
                        stage++
                    }
                    4 -> npcl(FacialExpression.NEUTRAL, "Thanks, I've been missing that.").also { stage = END_DIALOGUE }
                }
            }
        }

        on(Scenery.DOOR_2537, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 9) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, TedRehnisonDoors())
            }
            return@on true
        }

        on(Scenery.BARREL_2530, IntType.SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.A_SMALL_KEY_1507)) {
                sendMessage(player, "You don't find anything interesting.")
                return@on true
            } else {
                sendItemDialogue(player, Items.A_SMALL_KEY_1507, "You find a small key in the barrel.")
                addItem(player, Items.A_SMALL_KEY_1507)
            }
        }

        on(Scenery.SPOOKY_STAIRS_2522, IntType.SCENERY, "walk-down") { player, _ ->
            sendMessage(player, "You walk down the stairs...")
            teleport(player, Location.create(2537, 9671))
            return@on true
        }

        on(Scenery.SPOOKY_STAIRS_2523, IntType.SCENERY, "walk-up") { player, _ ->
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
                        1 -> playerl(FacialExpression.WORRIED, "Have you caught the plague?").also { stage = 6 }
                        2 -> playerl(FacialExpression.NEUTRAL, "Okay, I'll look for it.").also { stage = END_DIALOGUE }
                    }
                    6 -> npcl(FacialExpression.HALF_WORRIED, "No, I have none of the symptoms.").also { stage++ }
                    7 -> playerl(FacialExpression.THINKING, "Strange, I was told this house was plague infected.").also { stage++ }
                    8 -> npcl(FacialExpression.THINKING, "I suppose that was a cover up by the kidnappers.").also { stage = END_DIALOGUE }
                }
            }
        }

        onUseWith(IntType.SCENERY, Items.A_SMALL_KEY_1507, Scenery.DOOR_2526) { player, _, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            sendDialogue(player, "You unlock the door.")
            return@onUseWith true
        }

        on(Scenery.DOOR_2526, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, Quests.PLAGUE_CITY) >= 99 || hasAnItem(player, Items.A_SMALL_KEY_1507).exists()) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, ElenaDoorDialogue())
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.HANGOVER_CURE_1504, NPCs.BRAVEK_711) { player, _, _ ->
            openDialogue(player, NPCs.BRAVEK_711)
            return@onUseWith true
        }

        on(Scenery.DOOR_2054, IntType.SCENERY, "open"){ player, node ->
            if (isQuestComplete(player, Quests.PLAGUE_CITY)){
                DoorActionHandler.handleDoor(player, node as SceneryNode)
            }
            else{
                sendMessage(player, "This door is locked")
            }
            return@on true
        }

    }

    private fun dig(p : Player) {
        if (getAttribute(p, "/save:elena:dig", false)) {
            // Only remove the counter now that you have dug a hole
            removeAttribute(p, BUCKET_USES_ATTRIBUTE)
            queueScript(p, 1) { stage: Int ->
                when (stage) {
                    0 -> sendItemDialogue(
                        p, Items.SPADE_952,
                        "You dig deep into the soft soil... Suddenly it crumbles away!"
                    )

                    1 -> animate(p, DIG_WITH_SPADE)
                    3 -> {
                        teleport(p, Location(2518, 9759))
                        setQuestStage(p, Quests.PLAGUE_CITY, 4)
                        setVarbit(p, Vars.VARBIT_QUEST_PLAGUE_CITY_EDMOND_TUNNELS, 1)
                        setVarbit(p, Vars.VARBIT_QUEST_PLAGUE_CITY_MUD_PILE, 1)
                        sendDialogueLines(
                            p,
                            "You fall through...",
                            "...you land in the sewer.",
                            "Edmond follows you down the hole."
                        )
                        // We've dug the hole so don't keep track
                        removeAttribute(p, "/save:elena:dig")
                        return@queueScript stopExecuting(p)
                    }
                }
                return@queueScript delayScript(p, 1)

            }
        } else {
            sendMessage(p, "Nothing interesting happens.")
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
        setDest(IntType.SCENERY, intArrayOf(Scenery.GRILL_11423), "open") { _, _ ->
            return@setDest Location.create(2514, 9739, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.PIPE_2542), "climb-up") { _, _ ->
            return@setDest Location.create(2514, 9739, 0)
        }

        setDest(IntType.SCENERY, intArrayOf(Scenery.MANHOLE_2544), "climb-down") { _, node ->
            return@setDest Location.create(node.location.x, node.location.y+1, 0)
        }
    }
}

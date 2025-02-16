package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class ZogreFleshEatersListeners : InteractionListener {

    companion object {
        @JvmStatic
        fun ladderMakesSithikTurnIntoOgre(player: Player) {
            if(getQuestStage(player, ZogreFleshEaters.questName) == 6) {
                setQuestStage(player, ZogreFleshEaters.questName, 7)
                setVarbit(player, ZogreFleshEaters.varbitSithikOgre, 1)
            }
        }
    }

    override fun defineListeners() {
        // Stairs and doors
        on(Scenery.STAIRS_6841, SCENERY, "climb-down") { player, node ->
            sendMessage(player, "You climb down the steps.")
            if (node.location == Location(2443, 9417, 2)) {
                teleport(player, Location(2442, 9417, 0))
            } else {
                teleport(player, Location(2477, 9437, 2))
            }
            return@on true
        }
        on(Scenery.STAIRS_6842, SCENERY, "climb-up") { player, node ->
            sendMessage(player, "You climb up the steps.")
            if (node.location == Location(2443, 9417, 0)) {
                teleport(player, Location(2447, 9417, 2))
            } else {
                teleport(player, Location(2485, 3045, 0))
            }
            return@on true
        }
        on(Scenery.OGRE_STONE_DOOR_6871, SCENERY, "open") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) >= 9) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (inInventory(player, Items.OGRE_GATE_KEY_4839)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You use the Ogre Tomb Key to unlock the door.")
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        on(Scenery.OGRE_STONE_DOOR_6872, SCENERY, "open") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) >= 9) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (inInventory(player, Items.OGRE_GATE_KEY_4839)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                sendMessage(player, "You use the Ogre Tomb Key to unlock the door.")
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        // Stage 2
        on(Scenery.CRUSHED_BARRICADE_6881, SCENERY, "climb-over") { player, node ->
            if (player.location.x < 2456) {
                val distance = player.location.getDistance(Location(2457, 3049, 0)).toInt()
                forceMove(player, player.location, Location(2457, 3049, 0), 0, distance * 15, null, 1236)
            } else {
                val distance = player.location.getDistance(Location(2455, 3049, 0)).toInt()
                forceMove(player, player.location, Location(2455, 3049, 0), 0, distance * 15, null, 1236)
            }
            return@on true
        }
        on(Scenery.CRUSHED_BARRICADE_6882, SCENERY, "climb-over") { player, node ->
            if (player.location.x < 2456) {
                val distance = player.location.getDistance(Location(2457, 3048, 0)).toInt()
                forceMove(player, player.location, Location(2457, 3048, 0), 0, distance * 15, null, 1236)
            } else {
                val distance = player.location.getDistance(Location(2455, 3048, 0)).toInt()
                forceMove(player, player.location, Location(2455, 3048, 0), 0, distance * 15, null, 1236)
            }
            return@on true
        }

        // Stage 2A
        on(Scenery.OGRE_COFFIN_6844, SCENERY, "search") { player, node ->
            if (getVarbit(player, ZogreFleshEaters.varbitOgreCoffin) == 0) {
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> sendDialogueLines(player, "You search the coffin and find a small geometrically shaped hole in", "the side. It looks as if this hole was made with a considerable amount", "of force, maybe the thing which made the hole is still inside?").also { stage++ }
                            1 -> sendDialogueLines(player, "The lock looks quite crude, with some skill and a slender blade, you", "may be able to force it.").also { stage = END_DIALOGUE }
                        }
                    }
                })
            } else if (getVarbit(player, ZogreFleshEaters.varbitOgreCoffin) == 1) {
                openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        when (stage) {
                            0 -> sendDialogueLines(player, "The lid looks heavy, but now that you've unlocked it, you may be", "able to lift it. You prepare yourself.").also { stage++ }
                            1 -> playerl("Urrrgggg.").also { sendChat(player, "Urrrgggg."); stage++ }
                            2 -> playerl("Aarrrgghhh!").also { sendChat(player, "Aarrrgghhh!"); stage++ }
                            // Supposed to have a failure state here.
                            3 -> playerl("Raarrrggggg! Yes!").also { sendChat(player, "Raarrrggggg! Yes!"); stage++ }
                            4 -> sendDialogueLines(player, "You eventually manage to lift the lid.").also {
                                setVarbit(player, ZogreFleshEaters.varbitOgreCoffin, 3, true)
                                stage = END_DIALOGUE
                            }
                        }
                    }
                })
            }
            return@on true
        }
        onUseWith(IntType.SCENERY, Items.KNIFE_946, Scenery.OGRE_COFFIN_6844) { player, _, _ ->
            sendItemDialogue(player, Items.KNIFE_946,
                    "With some skill you manage to slide the blade along the lock edge and click into place the teeth of the primitive mechanism.")
            setVarbit(player, ZogreFleshEaters.varbitOgreCoffin, 1, true)
            return@onUseWith true
        }

        on(Scenery.OGRE_COFFIN_6845, SCENERY, "search") { player, node ->
            sendItemDialogue(player, Items.BLACK_PRISM_4808,
                    "You find a creepy looking black prism inside.")
            addItemOrDrop(player, Items.BLACK_PRISM_4808)
            return@on true
        }

        on(Items.BLACK_PRISM_4808, ITEM, "look-at") { player, node ->
            sendItemDialogue(player, Items.BLACK_PRISM_4808,
                    "It looks like a smokey black gem of some sort...very creepy. Some magical force must have prevented it from being shattered when it hit the coffin.")
            return@on true
        }

        // Stage 2B
        on(Scenery.BROKEN_LECTURN_6846, SCENERY, "search") { player, node ->
            sendMessage(player, "You search the broken down lecturn.")
            if (inInventory(player, Items.TORN_PAGE_4809)) {
                sendMessage(player, "You find nothing.")
            } else {
                sendItemDialogue(player, Items.TORN_PAGE_4809,
                        "You find a half torn page...it has spidery writing all over it.")
                addItemOrDrop(player, Items.TORN_PAGE_4809)
            }
            return@on true
        }

        on(Items.TORN_PAGE_4809, ITEM, "read") { player, node ->
            sendDialogue(player, "You don't manage to understand all of it as there is only a half page here. But it seems the spell was used to place a curse on an area and for all time raise the dead.")
            return@on true
        }

        // Stage 2C
        on(Scenery.SKELETON_6893, SCENERY, "search") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) >= 2){
                if (getAttribute(player, ZogreFleshEaters.attributeFoughtZombie, false)) {

                    if (inInventory(player, Items.RUINED_BACKPACK_4810)) {
                        sendMessage(player, "You find nothing on the corpse.")
                    } else {
                        sendMessage(player, "You find another backpack.")
                        addItemOrDrop(player, Items.RUINED_BACKPACK_4810)
                    }
                } else {
                    // Zombie time.
                    sendMessage(player, "Something screams into life right in front of you.")
                    val npc = NPC(NPCs.ZOMBIE_1826)
                    npc.isRespawn = false
                    npc.isWalks = false
                    npc.location = Location(2442, 9459, 2)
                    npc.direction = Direction.NORTH
                    npc.init()
                    npc.attack(player)
                }
            } else {
                // At no point should this be reached since you need to start the quest anyway.
                sendMessage(player, "You find nothing on the corpse.")
            }
            return@on true
        }
        on(Items.RUINED_BACKPACK_4810, ITEM, "open") { player, node ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> {
                            sendItemDialogue(player, Items.RUINED_BACKPACK_4810,
                                    "Just before you open the backpack, you notice a small leather patch with the moniker: 'B.Vahn', on it.").also { stage++ }
                            sendItemZoomOnInterface(player, 241, 1, Items.RUINED_BACKPACK_4810,  230)
                        }
                        1 -> sendItemDialogue(player, Items.DRAGON_INN_TANKARD_4811,
                                "You find an interesting looking tankard.").also {
                            setAttribute(player, ZogreFleshEaters.attributeFoundTankard, true)
                            if(removeItem(player, node)) {
                                addItem(player, Items.ROTTEN_FOOD_2959)
                                addItem(player, Items.KNIFE_946)
                                addItem(player, Items.DRAGON_INN_TANKARD_4811)
                            }
                            stage++
                        }
                        2 -> sendDoubleItemDialogue(player, Items.KNIFE_946, Items.ROTTEN_FOOD_2959, "You find a knife and some rotten food, the backpack is ripped to shreds.").also {
                            sendMessage(player, "You find a knife and some rotten food.")
                            sendMessage(player, "You find an interesting looking tankard.")
                            stage = END_DIALOGUE
                        }
                    }
                }
            })
            return@on true
        }


        on(Items.DRAGON_INN_TANKARD_4811, ITEM, "look-at") { player, node ->
            sendItemDialogue(player, Items.DRAGON_INN_TANKARD_4811,
                    "A stout ceramic tankard with a Dragon Emblem on the side, the words, 'Ye Olde Dragon Inn' are inscribed in the bottom.")
            return@on true
        }

        // Zavistic Bell, Stage 2,3,4,5,6 and on (Actually should be standalone.
        on(Scenery.BELL_6847, SCENERY, "ring") { player, node ->
            sendMessage(player, "You ring the bell.")
            // TODO: Make Zavistic appear at the bell area.
            openDialogue(player, content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile(), NPC(NPCs.ZAVISTIC_RARVE_2059))
            return@on true
        }

        // Stage 4A
        onUseWith(IntType.NPC, Items.DRAGON_INN_TANKARD_4811, NPCs.BARTENDER_739) { player, used, with ->
            openDialogue(player, BartenderDialogueFile(1), with as NPC)
            return@onUseWith true
        }
        onUseWith(IntType.NPC, Items.SITHIK_PORTRAIT_4814, NPCs.BARTENDER_739) { player, used, with ->
            // The good portrait
            openDialogue(player, BartenderDialogueFile(2), with as NPC)
            return@onUseWith true
        }
        onUseWith(IntType.NPC, Items.SITHIK_PORTRAIT_4815, NPCs.BARTENDER_739) { player, used, with ->
            // The bad portrait
            openDialogue(player, BartenderDialogueFile(3), with as NPC)
            return@onUseWith true
        }
        on(Items.SIGNED_PORTRAIT_4816, ITEM, "look-at") { player, node ->
            sendItemDialogue(player, Items.SIGNED_PORTRAIT_4816,
                    "You see an image of Sithik with a message underneath 'I, the bartender of the Dragon Inn, do swear that this is a true likeness of the wizzy who was talking to Brentle Vahn, my customer the other day.'")
            return@on true
        }
        onUseWith(IntType.NPC, Items.SITHIK_PORTRAIT_4814, NPCs.ZAVISTIC_RARVE_2059) { player, used, with ->
            // The good portrait
            openDialogue(player, ZavisticRarveUseItemsDialogueFile(3), with as NPC)
            return@onUseWith true
        }
        onUseWith(IntType.NPC, Items.SITHIK_PORTRAIT_4815, NPCs.ZAVISTIC_RARVE_2059) { player, used, with ->
            // The bad portrait
            openDialogue(player, ZavisticRarveUseItemsDialogueFile(4), with as NPC)
            return@onUseWith true
        }


        // Stage 4B
        on(Items.BOOK_OF_PORTRAITURE_4817, ITEM, "read") { player, node ->
            sendDialogueLines(player,
                    "All interested artisans should really consider taking up the hobby of",
                    "portraiture. To do so, one uses a piece of papyrus on the intended",
                    "subject to initiate a likeness drawing activity.")
            return@on true
        }

        // Stage 4C
        on(Items.BOOK_OF_HAM_4829, ITEM, "read") { player, node ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> {
                            sendDialogue(player,
                                    "You read this book for a while, it seems to be some sort of political "+
                                            "manifesto about how the king doesn't do enough to safeguard the "+
                                            "citizens of the realm from the monsters that still thrive within the "+
                                            "borders. ").also { stage++ }
                            // This is the original, but it is too big for this to handle.
//                            sendDialogueLines(player,
//                                    "You read this book for a while, it seems to be some sort of political",
//                                    "manifesto about how the king doesn't do enough to safeguard the",
//                                    "citizens of the realm from the monsters that still thrive within the",
//                                    "borders. It sends out a rallying to all people who would want to",
//                                    "stop monsters, to join the HAM movement.").also { stage++ }
                        }
                        1 -> sendDialogue(player, "It sends out a rallying to all people who would want to stop monsters, to join the HAM movement.").also { stage++ }
                        2 -> sendPlayerDialogue(player, "Hmm, Sithik must really hate monsters then, I wonder if he hates ogres in particular?").also {
                            stage = END_DIALOGUE
                        }
                    }
                }
            })
            return@on true
        }

        // Stage 4D
        on(Items.NECROMANCY_BOOK_4837, ITEM, "read") { player, node ->
            sendDialogueLines(player,
                    "This book uses very strange language and some",
                    "incomprehensible symbols. It has a very dark and evil feeling to",
                    "it. As you're looking through the book, you notice that",
                    "one of the pages has been torn and half of it is missing.")
            return@on true
        }

        onUseWith(IntType.ITEM, Items.NECROMANCY_BOOK_4837, Items.TORN_PAGE_4809) { player, _, _ ->
            sendDoubleItemDialogue(player, Items.NECROMANCY_BOOK_4837, Items.TORN_PAGE_4809,
                    "The torn page matches exactly the part where a torn out page is missing from the book. You feel sure that this page came from this book.")
            return@onUseWith true
        }

        // Uglug Nar Shop
        onUseWith(IntType.NPC, intArrayOf(Items.RELICYMS_BALM1_4848, Items.RELICYMS_BALM2_4846, Items.RELICYMS_BALM3_4844, Items.RELICYMS_BALM4_4842), NPCs.UGLUG_NAR_2039) { player, used, with ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        0 -> sendItemDialogue(player, used.id,"You show the potion to Uglug Nar.").also { stage++ }
                        1 -> playerl("Hey, here you go! I brought you some of the potion which should cure the disease. You said that you would buy some from me.").also {
                            if (getAttribute(player, ZogreFleshEaters.attributeOpenUglugNarShop, false)) {
                                stage = 2
                            } else {
                                stage = 3
                            }
                        }
                        2 -> sendNPCDialogue(player, NPCs.UGLUG_NAR_2039, "Yous creatures is da funny ones... yous already solds me's ones now..and us can now sell un to yous!", FacialExpression.OLD_NORMAL).also { stage = END_DIALOGUE }
                        3 -> sendNPCDialogue(player, NPCs.UGLUG_NAR_2039, "Yous creatures done da good fing...yous get many bright pretties for dis...!", FacialExpression.OLD_NORMAL).also { stage++ }
                        4 -> sendDoubleItemDialogue(player, Item(Items.COINS_995, 1000), used as Item, "You sell the potion and get 1000 coins in return.").also {
                            if (removeItem(player, used)) {
                                addItemOrDrop(player, Items.COINS_995, 1000)
                                setAttribute(player, ZogreFleshEaters.attributeOpenUglugNarShop, true)
                            }
                            stage = END_DIALOGUE
                        }
                    }
                }
            })
            return@onUseWith true
        }

        // Stage 8 to 9
        on(Scenery.STAND_6897, SCENERY, "search") { player, node ->
            if (getQuestStage(player, ZogreFleshEaters.questName) == 8 &&
                    getAttribute<NPC?>(player, ZogreFleshEaters.attributeSlashBashInstance, null) == null
            ) {
                // Zombie time.
                sendMessage(player, "Something stirs behind you!")
                val npc = NPC(NPCs.SLASH_BASH_2060)
                setAttribute(player, ZogreFleshEaters.attributeSlashBashInstance, npc)
                setAttribute(npc, "target", player)
                npc.isRespawn = false
                npc.isWalks = false
                npc.location = Location(2478, 9446, 0)
                npc.direction = Direction.EAST
                npc.init()
                npc.attack(player)
            } else if (getQuestStage(player, ZogreFleshEaters.questName) > 8) {
                if (inInventory(player, Items.OGRE_ARTEFACT_4818)) {
                    sendMessage(player, "You find nothing on the stand.")
                } else {
                    sendMessage(player, "You find another artifact.")
                    addItemOrDrop(player, Items.OGRE_ARTEFACT_4818)
                }
            } else {
                // At no point should this be reached since you need to start the quest anyway.
                sendMessage(player, "You find nothing on the stand.")
            }
            return@on true
        }
    }
}

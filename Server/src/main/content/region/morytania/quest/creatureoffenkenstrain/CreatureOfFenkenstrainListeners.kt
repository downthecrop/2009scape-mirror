package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.global.action.PickupHandler
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Scenery
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class CreatureOfFenkenstrainListeners : InteractionListener {
    companion object {
        private val itemToAttribute = hashMapOf(
                Items.ARMS_4195 to CreatureOfFenkenstrain.attributeArms,
                Items.LEGS_4196 to CreatureOfFenkenstrain.attributeLegs,
                Items.TORSO_4194 to CreatureOfFenkenstrain.attributeTorso,
                Items.DECAPITATED_HEAD_4197 to CreatureOfFenkenstrain.attributeHead
        )

        enum class Graves(val location: Location, val graveName: String, val unearthText: String, val unearthItem: Int?) {
            GRAVE1(Location(3541, 3541), "Anton Hayes", "...but the grave is empty.", null),
            GRAVE2(Location(3542, 3486), "Callum Elding", "...but the grave is empty.", null),
            GRAVE3(Location(3585, 3497), "Domin O'Raleigh", "...but the grave is empty.", null),
            GRAVE4(Location(3608, 3491), "Ed Lestwit", "...and you unearth a decapitated head.", Items.DECAPITATED_HEAD_4197),
            GRAVE5(Location(3588, 3472), "Elena Frey", "...but the grave is empty.", null),
            GRAVE6(Location(3593, 3509), "Eryn Treforest", "...but the grave is empty.", null),
            GRAVE7(Location(3594, 3491), "Isla Skye", "...but the grave is empty.", null),
            GRAVE8(Location(3596, 3479), "Jayna Harrow", "...but the grave is empty.", null),
            GRAVE9(Location(3604, 3466), "Jayne Corbo", "...but the grave is empty.", null),
            GRAVE10(Location(3608, 3466), "Kandik Kludge", "...but the grave is empty.", null),
            GRAVE11(Location(3616, 3478), "Korvic Frey", "...but the grave is empty.", null),
            GRAVE12(Location(3619, 3469), "Marabella Kludge", "...but the grave is empty.", null),
            GRAVE13(Location(3626, 3495), "Marcus Harrow", "...but the grave is empty.", null),
            GRAVE14(Location(3629, 3483), "Petrik Corbo", "...but the grave is empty.", null),
            GRAVE15(Location(3631, 3476), "Serra Alcanthric", "...but the grave is empty.", null),
            GRAVE16(Location(3631, 3500), "Toran Alcanthric", "...but the grave is empty.", null),
            GRAVE17(Location(3572, 3527), "Unknown", "...but the grave is empty.", null),
            GRAVE18(Location(3576, 3526), "Unknown", "...but the grave is empty.", null),
            GRAVE19(Location(3639, 3470), "Lord Rologray", "...but the grave is empty.", null),
            GRAVE20(Location(3634, 3503), "Lord Rologarth", "...but the grave is empty.", null),
            GRAVE21(Location(3502, 3576), "Lady Rolobrae", "...and you unearth a torso.", Items.TORSO_4194),
            GRAVE22(Location(3504, 3577), "Lord Rolomere", "...and you unearth a pair of arms.", Items.ARMS_4195),
            GRAVE23(Location(3506, 3576), "Lord Rolovanne", "...and you unearth a pair of legs.", Items.LEGS_4196);

            companion object {
                @JvmField
                val locationMap = Graves.values().associateBy { it.location }
            }
        }
    }
    override fun defineListeners() {

        // Climbing ladders
        addClimbDest(Location.create(3504, 9970, 0), Location.create(3504, 3571, 0))

        // 1: Reading Signpost to start the quest
        on(Items.NULL_5164, SCENERY, "read") { player, _ ->
            if (getQuestStage(player, CreatureOfFenkenstrain.questName) < 7 ) {
                sendDialogueLines(
                        player,
                        "The signpost has a note pinned onto it. The note says:",
                        "'---- Braindead Butler Wanted ----",
                        " Gravedigging skills essential - Hunchback advantageous",
                        "See Dr Fenkenstrain at the castle NE of Canifis'",
                )
            } else {
                sendDialogueLines(
                        player,
                        "The signpost has a note pinned onto it. The note says:",
                        "'AAARRGGGHHHHH!!!!!'",
                )
            }
            if(getQuest(player, CreatureOfFenkenstrain.questName).hasRequirements(player) && getQuestStage(player, CreatureOfFenkenstrain.questName) == 0) {
                setQuestStage(player, CreatureOfFenkenstrain.questName, 1)
            }
            return@on true
        }

        // 2 Reading a grave
        on(intArrayOf(Scenery.GRAVE_5168, Scenery.GRAVE_5169), SCENERY, "read") { player, node ->
            val grave = Graves.locationMap[node.location]
            sendMessage(player, "The grave says:")
            sendMessage(player, "  'Here lies ${grave?.graveName ?: "Unknown"} - REST IN PEACE'")
            return@on true
        }

        // 2: Digging a grave
        on(intArrayOf(Scenery.GRAVE_5168, Scenery.GRAVE_5169), SCENERY, "dig") { player, node ->
            if(!inInventory(player, Items.SPADE_952)) {
                sendMessage(player, "You need a spade to do that.")
                return@on true
            }
            val grave = Graves.locationMap[node.location]
            sendMessage(player, "You start digging...")
            animate(player, Animation(831))
            player.pulseManager.run(object : Pulse(5) {
                override fun pulse(): Boolean {
                    player.animate(Animation(-1))
                    if(grave?.unearthItem != null &&
                            !hasAnItem(player, grave.unearthItem).exists()
                            /* && !getAttribute(player, itemToAttribute[grave.unearthItem] ?: "", false) */) {
                        sendItemDialogue(player, grave.unearthItem, grave.unearthText)
                        addItemOrDrop(player, grave.unearthItem)
                    } else {
                        sendMessage(player, "...but the grave is empty.")
                    }
                    return true
                }
            })
            return@on true
        }

        // 2: Find amulets in bookcases. Note: even after the quest, these can still be accessed, but nothing falls out.
        on(Scenery.BOOKCASE_5166, SCENERY, "search") { player, node ->
            if (node.location.equals(Location(3555, 3558, 1))) {
                openDialogue(player, BookcaseEastDialogueFile())
                return@on true
            } else if(node.location.equals(Location(3542, 3558, 1))) {
                openDialogue(player, BookcaseWestDialogueFile())
                return@on true
            } else {
                sendMessage(player, "It is a bookcase full of books")
                return@on true
            }
        }

        // 2: Snap together Amulets
        onUseWith(ITEM, Items.MARBLE_AMULET_4187, Items.OBSIDIAN_AMULET_4188) { player, used, with ->
            if(removeItem(player, Items.MARBLE_AMULET_4187) && removeItem(player, Items.OBSIDIAN_AMULET_4188)) {
                sendItemDialogue(player, Items.STAR_AMULET_4183, "The marble and obsidian amulets snap together tightly to form a six-pointed amulet.")
                addItemOrDrop(player, Items.STAR_AMULET_4183)
            }
            return@onUseWith true
        }

        // 2: Fit Star Amulet on Memorial
        onUseWith(SCENERY, Items.STAR_AMULET_4183, Items.NULL_5167) { player, used, with ->
            if (removeItem(player, Items.STAR_AMULET_4183)) {
                sendItemDialogue(player, Items.STAR_AMULET_4183, "The star amulet fits exactly into the depression on the coffin lid.")
                setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, true)
            }
            return@onUseWith true
        }

        // 2: Opening Cavern Entrance
        on(Scenery.ENTRANCE_5170, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.CAVERN_KEY_4184) && removeItem(player, Items.CAVERN_KEY_4184)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
        // 2: Using Cavern Key on the Cavern Entrance
        onUseWith(SCENERY, Items.CAVERN_KEY_4184, Scenery.ENTRANCE_5170) { player, used, with ->
            if (removeItem(player, used)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            }
            return@onUseWith true
        }


        // 2: Searching for Cavern Key out of the Chest
        on(Scenery.CHEST_5163, SCENERY, "search") { player, node ->
            sendItemDialogue(player, Items.CAVERN_KEY_4184, "You take a key out of the chest.")
            addItemOrDrop(player, Items.CAVERN_KEY_4184)
            return@on true
        }

        // 2: Taking the brain from the table (telekinetic grab is allowed)
        on(Items.PICKLED_BRAIN_4199, GROUNDITEM, "take") { player, node ->
            if(node.location.equals(3492, 3474, 0)) {
                openDialogue(player, RoavarDialogueFile(2), findLocalNPC(player, NPCs.ROAVAR_1042)!!)
            } else {
                PickupHandler.take(player, node as GroundItem)
            }
            return@on true
        }

        // 2: Fit Brain into Decapitated Head
        onUseWith(ITEM, Items.PICKLED_BRAIN_4199, Items.DECAPITATED_HEAD_4197) { player, used, with ->
            if(removeItem(player, used) && removeItem(player, with)) {
                sendItemDialogue(player, Items.DECAPITATED_HEAD_4198, "You squeeze the pickled brain into the decapitated head.")
                addItemOrDrop(player, Items.DECAPITATED_HEAD_4198)
            }
            return@onUseWith true
        }

        // 2: Searching the Memorial (Scenery.MEMORIAL_5167)
        on(Items.NULL_5167, SCENERY, "search") { player, node ->
            val scenery = node.asScenery()
            if(getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, false) ||
                    getQuestStage(player, CreatureOfFenkenstrain.questName) > 2) {
                animateScenery(player, scenery, 1620)
                var dest: Location? = null
                if(scenery.location.equals(Location(3505, 3571))) {
                    dest = Location(3504, 9969)
                } else if(scenery.location.equals(Location(3578, 3527))) {
                    dest = Location(3577, 9927)
                }
                if(dest != null) {
                    player.pulseManager.run(object : Pulse(3) {
                        override fun pulse(): Boolean {
                            resetAnimator(player)
                            teleport(player, dest!!)
                            return true
                        }
                    })
                }
            } else {
                if (scenery.location.equals(Location(3505, 3571))) {
                    sendMessage(player, "You find a depression in the memorial stone in the shape of a six-pointed star.")
                } else {
                    sendMessage(player, "You find nothing remarkable about the memorial stone.")
                }
            }
            return@on true
        }

        // 2: Pushing open the Memorial (Scenery.MEMORIAL_5167)
        on(Items.NULL_5167, SCENERY, "push") { player, node ->
            val scenery = node.asScenery()
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedMemorial, false) ||
                    getQuestStage(player, CreatureOfFenkenstrain.questName) > 2) {
                animateScenery(player, scenery, 1620)
                var dest: Location? = null
                if(scenery.location.equals(Location(3505, 3571))) {
                    dest = Location(3504, 9969)
                } else if(scenery.location.equals(Location(3578, 3527))) {
                    dest = Location(3577, 9927)
                }
                if(dest != null) {
                    player.pulseManager.run(object : Pulse(3) {
                        override fun pulse(): Boolean {
                            resetAnimator(player)
                            teleport(player, dest!!)
                            return true
                        }
                    })
                }
            } else {
                player.sendMessage("The coffin is incredibly heavy, and does not budge.")
            }
            return@on true
        }

        // 5: Garden Shed Door
        on(Scenery.DOOR_5174, SCENERY, "open") { player, node ->
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, false) ||
                    getQuestStage(player, CreatureOfFenkenstrain.questName) >= 5) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else if (inInventory(player, Items.SHED_KEY_4186)) {
                if (removeItem(player, Items.SHED_KEY_4186)) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, true)
                }
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        // 5: Garden Shed Door
        onUseWith(SCENERY, Items.SHED_KEY_4186, Scenery.DOOR_5174) { player, used, with ->
            if (getAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, false) ||
                    getQuestStage(player, CreatureOfFenkenstrain.questName) >= 5) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            } else if (removeItem(player, used)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
                setAttribute(player, CreatureOfFenkenstrain.attributeUnlockedShed, true)
            }
            return@onUseWith true
        }

        // 5: Pile of Canes
        on(Items.NULL_5158, SCENERY, "take-from") { player, node ->
            sendMessage(player, "You take a garden cane from the pile.")
            addItemOrDrop(player, Items.GARDEN_CANE_4189)
            return@on true
        }

        // 5: Cupboard Garden Brush
        on(Items.NULL_5157, SCENERY, "search") { player, node ->
            sendItemDialogue(player, Items.GARDEN_BRUSH_4190, "You find a garden brush in the cupboard.")
            // player.packetDispatch.sendAngleOnInterface(241, 1, 1000, 510, 0)
            addItemOrDrop(player, Items.GARDEN_BRUSH_4190)
            return@on true
        }

        // 5: Extended Brush up to 3 Fused
        onUseWith(ITEM, intArrayOf(Items.GARDEN_BRUSH_4190, Items.EXTENDED_BRUSH_4191, Items.EXTENDED_BRUSH_4192), Items.GARDEN_CANE_4189) { player, used, with ->
            if (!inInventory(player, Items.BRONZE_WIRE_1794)) {
                sendMessage(player, "You need some bronze wire to tie them together.")
                return@onUseWith true
            }
            if (removeItem(player, Items.BRONZE_WIRE_1794) && removeItem(player, used) && removeItem(player, with)){
                sendMessage(player, "You attach the cane to the brush.")
                when (used.id) {
                    Items.GARDEN_BRUSH_4190 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4191)
                    }
                    Items.EXTENDED_BRUSH_4191 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4192)
                    }
                    Items.EXTENDED_BRUSH_4192 -> {
                        addItemOrDrop(player, Items.EXTENDED_BRUSH_4193)
                    }
                }
            }
            return@onUseWith true
        }

        // 5: Cupboard Garden Brush
        on(Scenery.FIREPLACE_5165, SCENERY, "examine") { player, node ->
            if (!inInventory(player, Items.CONDUCTOR_4201)) {
                sendMessage(player, "You give the chimney a jolly good clean out.")
                sendItemDialogue(player, Items.CONDUCTOR_MOULD_4200, "A lightning conductor mould falls down out of the chimney.")
                addItemOrDrop(player, Items.CONDUCTOR_MOULD_4200)
                return@on true
            }
            sendMessage(player, "It looks like it needs a good sweep out.")
            return@on true
        }

        // 5: Brush Fireplace
        onUseWith(SCENERY, Items.EXTENDED_BRUSH_4193, Scenery.FIREPLACE_5165) { player, used, with ->
            sendMessage(player, "You give the chimney a jolly good clean out.")
            sendItemDialogue(player, Items.CONDUCTOR_MOULD_4200, "A lightning conductor mould falls down out of the chimney.")
            addItemOrDrop(player, Items.CONDUCTOR_MOULD_4200)
            return@onUseWith true
        }

        // 5: Repair the lightning rod
        on(Scenery.LIGHTNING_CONDUCTOR_5176, SCENERY, "repair") { player, node ->
            if (!inInventory(player, Items.CONDUCTOR_4201)) {
                sendMessage(player, "You need to repair it with a conductor.")
                return@on true
            }
            if (removeItem(player, Items.CONDUCTOR_4201)) {
                if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 4) {
                    setQuestStage(player, CreatureOfFenkenstrain.questName, 5)
                }
                sendDialogue(player, "You repair the lightning conductor not one moment too soon - a tremendous bold of lightning melts the new lightning conductor, and power blazes throughout the castle, if only briefly.")
                val scenery = node.asScenery()
                replaceScenery(scenery, Items.NULL_5177, 3, node.location)
                animateScenery(player, scenery, 1632)
            }
            return@on true
        }

        // 6: Enter jail above
        on(Scenery.DOOR_5172, SCENERY, "open") { player, node ->
            if (inInventory(player, Items.TOWER_KEY_4185) || getQuestStage(player, CreatureOfFenkenstrain.questName) > 7) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }
        // 2: Using Cavern Key on the Cavern Entrance
        onUseWith(SCENERY, Items.TOWER_KEY_4185, Scenery.DOOR_5172) { player, used, with ->
            if (inInventory(player, Items.TOWER_KEY_4185)) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            }
            return@onUseWith true
        }


        // 7: Pickpocket Ring of Charos from Fenkenstrain
        on(NPCs.DR_FENKENSTRAIN_1670, NPC, "pickpocket") { player, node ->
            if (getQuestStage(player, CreatureOfFenkenstrain.questName) == 7) {
                sendMessage(player, "You steal the Ring of Charos from Fenkenstrain.")
                finishQuest(player, CreatureOfFenkenstrain.questName)
            } else if (getQuestStage(player, CreatureOfFenkenstrain.questName) > 7 && hasAnItem(player, Items.RING_OF_CHAROS_4202).container == null) {
                // Allow Fenkenstrain to be pickpocketed beyond the quest if the ring is lost.
                addItemOrDrop(player, Items.RING_OF_CHAROS_4202, 1)
                sendMessage(player, "You steal the Ring of Charos from Fenkenstrain.")
            } else {
                player.dialogueInterpreter.sendDialogues(NPCs.DR_FENKENSTRAIN_1670, FacialExpression.NEUTRAL ,"What do you think you're doing???")
            }
            return@on true
        }
    }
}

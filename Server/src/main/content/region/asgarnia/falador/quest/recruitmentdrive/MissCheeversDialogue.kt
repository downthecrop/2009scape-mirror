package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

@Initializable
class MissCheeversDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MissCheeversDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return MissCheeversDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MISS_CHEEVERS_2288)
    }
}

class MissCheeversDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> dialogueNum == 0 }
                .playerl(FacialExpression.FRIENDLY,"Can you give me any help?")
                .npcl(FacialExpression.FRIENDLY,"No, I am sorry, but that is forbidden by our rules.")
                .npcl(FacialExpression.FRIENDLY,"If you are having a particularly tough time of it, I suggest you leave and come back later when you are in a more receptive frame of mind.")
                .npcl(FacialExpression.FRIENDLY,"Sometimes a break from concentration will yield fresh insight. Our aim is to test you, but not to the point of frustration!")
                .playerl(FacialExpression.FRIENDLY,"Okay, thanks!")
                .end()


        b.onPredicate { _ -> dialogueNum == 1 }
                .betweenStage { _, player, _, _ ->
                    setVarbit(player, MissCheeversRoomListeners.doorVarbit, 0)
                    removeAttribute(player, MissCheeversRoomListeners.attributebook)
                    removeAttribute(player, MissCheeversRoomListeners.attributemagnet)
                    removeAttribute(player, MissCheeversRoomListeners.attributeKnife)
                    removeAttribute(player, MissCheeversRoomListeners.attributeShears)
                    removeAttribute(player, MissCheeversRoomListeners.attributeTin)
                    removeAttribute(player, MissCheeversRoomListeners.attributeChisel)
                    removeAttribute(player, MissCheeversRoomListeners.attributeWire)

                    removeAttribute(player, MissCheeversRoomListeners.attribute3VialsOfLiquid)

                    MissCheeversRoomListeners.Companion.Vials.vialMap.map {
                        removeAttribute(player, it.value.attribute)
                    }

                    MissCheeversRoomListeners.Companion.DoorVials.doorVialsRequiredMap.map {
                        removeAttribute(player, it.value.attribute)
                    }
                }
                .npcl(FacialExpression.FRIENDLY,"Greetings, @name. Welcome to my challenge.")
                .npcl(FacialExpression.FRIENDLY,"All you need to do is leave from the opposite door to where you came in by.")
                .npcl(FacialExpression.FRIENDLY,"I will warn you that this is more complicated than it may at first appear.")
                .npcl(FacialExpression.FRIENDLY,"I should also warn you that there are limited supplies of the items in this room, so think carefully before using them, you may find yourself stuck and have to leave to start again!")
                .npcl(FacialExpression.FRIENDLY,"Best of luck!")
                .end()
    }
}

class MissCheeversRoomListeners : InteractionListener {
    companion object {

        const val doorVarbit = 686

        const val attributebook = "quest:recruitmentdrive-book"
        const val attributemagnet = "quest:recruitmentdrive-magnet"
        const val attributeKnife = "quest:recruitmentdrive-knife"
        const val attributeShears = "quest:recruitmentdrive-shears"
        const val attributeTin = "quest:recruitmentdrive-tin"
        const val attributeChisel = "quest:recruitmentdrive-chisel"
        const val attributeWire = "quest:recruitmentdrive-wire"

        const val attribute3VialsOfLiquid = "quest:recruitmentdrive-3vialsofliquid"

        /** Enums to map canoes to related properties. */
        enum class Vials(val itemId: Int, val attribute: String) {
            CUPRIC_SULPHATE_5577(Items.CUPRIC_SULPHATE_5577, "quest:recruitmentdrive-cupricsulphate"),
            ACETIC_ACID_5578(Items.ACETIC_ACID_5578, "quest:recruitmentdrive-aceticacid"),
            GYPSUM_5579(Items.GYPSUM_5579, "quest:recruitmentdrive-gypsum"),
            SODIUM_CHLORIDE_5580(Items.SODIUM_CHLORIDE_5580, "quest:recruitmentdrive-sodiumchloride"),
            NITROUS_OXIDE_5581(Items.NITROUS_OXIDE_5581, "quest:recruitmentdrive-nitrousoxide"),
            VIAL_OF_LIQUID_5582(Items.VIAL_OF_LIQUID_5582, "quest:recruitmentdrive-vialofliquid"),
            TIN_ORE_POWDER_5583(Items.TIN_ORE_POWDER_5583, "quest:recruitmentdrive-tinorepowder"),
            CUPRIC_ORE_POWDER_5584(Items.CUPRIC_ORE_POWDER_5584, "quest:recruitmentdrive-cupricorepowder");

            companion object {
                @JvmField
                val vialMap = Vials.values().associateBy { it.itemId }
            }
        }


        /** Enums to map canoes to related properties. */
        enum class DoorVials(val itemId: Int, val attribute: String) {
            CUPRIC_SULPHATE_5577(Items.CUPRIC_SULPHATE_5577, "quest:recruitmentdrive-doorcupricsulphate"),
            ACETIC_ACID_5578(Items.ACETIC_ACID_5578, ""),
            SODIUM_CHLORIDE_5580(Items.SODIUM_CHLORIDE_5580, ""),
            VIAL_OF_LIQUID_5582(Items.VIAL_OF_LIQUID_5582, "quest:recruitmentdrive-doorvialofliquid");

            companion object {
                @JvmField
                val doorVialsArray = DoorVials.values().map { it.itemId }.toIntArray()
                val doorVialsMap = DoorVials.values().associateBy { it.itemId }
                val doorVialsRequiredMap = DoorVials.values().associateBy { it.itemId }.filter { it.value.attribute != "" }
            }
        }


        fun searchingHelper(player: Player, attributeCheck: String, item: Int, searchingDescription: String, objectDescription: String) {
            queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendMessage(player, searchingDescription)
                        return@queueScript delayScript(player, 2)
                    }
                    1 -> {
                        if (attributeCheck != "" && !getAttribute(player, attributeCheck, false)) {
                            setAttribute(player, attributeCheck, true)
                            addItem(player, item)
                            sendMessage(player, objectDescription)
                        } else {
                            sendMessage(player, "You don't find anything interesting.")
                        }
                        return@queueScript stopExecuting(player)
                    }
                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }

    override fun defineListeners() {

        /** Obtainable Items */

        on(Scenery.OLD_BOOKSHELF_7327, IntType.SCENERY, "search") { player, _ ->
            searchingHelper(player, attributemagnet, Items.MAGNET_5604, "You search the bookshelves...", "Hidden amongst the books you find a magnet.")
            return@on true
        }

        on(Scenery.OLD_BOOKSHELF_7328, IntType.SCENERY, "search") { player, _ ->
            searchingHelper(player, attributebook, Items.ALCHEMICAL_NOTES_5588, "You search the bookshelves...", "You find a book that looks like it might be helpful.")
            return@on true
        }

        on(Scenery.OLD_BOOKSHELF_7329, IntType.SCENERY, "search") { player, _ ->
            searchingHelper(player, attributeKnife, Items.KNIFE_5605, "You search the bookshelves...", "Hidden amongst the books you find a knife.")
            return@on true
        }

        on(Scenery.OLD_BOOKSHELF_7330, IntType.SCENERY, "search") { player, _ ->
            searchingHelper(player, "", 0, "You search the bookshelves...", "")
            return@on true
        }

        on(Scenery.SHELVES_7333, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.ACETIC_ACID_5578]!!.attribute, false)) { vialList.add(Items.ACETIC_ACID_5578) }
            if (!getAttribute(player, Vials.vialMap[Items.VIAL_OF_LIQUID_5582]!!.attribute, false)) { vialList.add(Items.VIAL_OF_LIQUID_5582) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7334, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.CUPRIC_SULPHATE_5577]!!.attribute, false)) { vialList.add(Items.CUPRIC_SULPHATE_5577) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7335, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.GYPSUM_5579]!!.attribute, false)) { vialList.add(Items.GYPSUM_5579) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7336, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.SODIUM_CHLORIDE_5580]!!.attribute, false)) { vialList.add(Items.SODIUM_CHLORIDE_5580) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7337, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.NITROUS_OXIDE_5581]!!.attribute, false)) { vialList.add(Items.NITROUS_OXIDE_5581) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7338, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.TIN_ORE_POWDER_5583]!!.attribute, false)) { vialList.add(Items.TIN_ORE_POWDER_5583) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7339, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            if (!getAttribute(player, Vials.vialMap[Items.CUPRIC_ORE_POWDER_5584]!!.attribute, false)) { vialList.add(Items.CUPRIC_ORE_POWDER_5584) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray()))
            return@on true
        }

        on(Scenery.SHELVES_7340, IntType.SCENERY, "search") { player, _ ->
            val vialList = ArrayList<Int>()
            val total = getAttribute(player, attribute3VialsOfLiquid, 3)
            for (i in 1..total) { vialList.add(Items.VIAL_OF_LIQUID_5582) }
            openDialogue(player, VialShelfDialogueFile(vialList.toIntArray(), attribute3VialsOfLiquid))
            return@on true
        }

        on(Scenery.CRATE_7347, IntType.SCENERY, "search") { player, node ->
            if (node.location == Location(2476, 4943)) {
                searchingHelper(player, attributeTin, Items.TIN_5600, "You search the crate...", "Inside the crate you find a tin.")
            } else {
                searchingHelper(player, "", 0, "You search the crate...", "")
            }
            return@on true
        }

        on(Scenery.CRATE_7348, IntType.SCENERY, "search") { player, node ->
            if (node.location == Location(2476, 4937)) {
            searchingHelper(player, attributeChisel, Items.CHISEL_5601, "You search the crate...", "Inside the crate you find a chisel.")
            } else {
                searchingHelper(player, "", 0, "You search the crate...", "")
            }
            return@on true
        }

        on(Scenery.CRATE_7349, IntType.SCENERY, "search") { player, node ->
            if (node.location == Location(2475, 4943)) {
                searchingHelper(player, attributeWire, Items.BRONZE_WIRE_5602, "You search the crate...", "Inside the crate you find some wire.")
            } else {
                searchingHelper(player, "", 0, "You search the crate...", "")
            }
            return@on true
        }

        on(Scenery.CLOSED_CHEST_7350, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.OPEN_CHEST_7351, 100)
            return@on true
        }

        on(Scenery.OPEN_CHEST_7351, IntType.SCENERY, "search") { player, _ ->
            searchingHelper(player, attributeShears, Items.SHEARS_5603, "You search the chest...", "Inside the chest you find some shears.")
            return@on true
        }

        on(Scenery.OPEN_CHEST_7351, IntType.SCENERY, "close") { player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CLOSED_CHEST_7350, -1)
            return@on true
        }

        /** Combining Items the correct way */

        onUseWith(ITEM, Items.TIN_5600, Items.GYPSUM_5579) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You empty the vial into the tin.")
                addItemOrDrop(player, Items.TIN_5592)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5592, Items.VIAL_OF_LIQUID_5582) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You empty the vial into the tin.")
                sendMessage(player, "You notice the tin gets quite warm as you do this.")
                sendMessage(player, "A lumpy white mixture is made, that seems to be hardening.")
                addItemOrDrop(player, Items.TIN_5593)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.TIN_5593, Scenery.KEY_7346) { player, used, _ ->
            if(removeItem(player, used.id)) {
                sendMessage(player, "You make an impression of the key as the white mixture hardens.")
                addItemOrDrop(player, Items.TIN_5594)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5594, Items.TIN_ORE_POWDER_5583) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You pour the vial into the impression of the key.")
                addItemOrDrop(player, Items.TIN_5595)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5595, Items.CUPRIC_ORE_POWDER_5584) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You pour the vial into the impression of the key.")
                addItemOrDrop(player, Items.TIN_5597)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5594, Items.CUPRIC_ORE_POWDER_5584) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You pour the vial into the impression of the key.")
                addItemOrDrop(player, Items.TIN_5596)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5596, Items.TIN_ORE_POWDER_5583) { player, used, with ->
            if(removeItem(player, used.id) && removeItem(player, with.id)) {
                sendMessage(player, "You pour the vial into the impression of the key.")
                addItemOrDrop(player, Items.TIN_5597)
                addItemOrDrop(player, Items.VIAL_229)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.TIN_5597, Scenery.BUNSEN_BURNER_7332) { player, used, _ ->
            if(removeItem(player, used.id)) {
                sendMessage(player, "You heat the two powdered ores together in the tin.")
                sendMessage(player, "You make a duplicate of the key in bronze.")
                addItemOrDrop(player, Items.TIN_5598)
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.TIN_5598, Items.BRONZE_WIRE_5602, Items.CHISEL_5601, Items.KNIFE_5605) { player, used, with ->
            if(removeItem(player, used.id)) {
                sendMessage(player, "You prise the duplicate key out of the tin.")
                addItemOrDrop(player, Items.TIN_5594)
                addItemOrDrop(player, Items.BRONZE_KEY_5585)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.METAL_SPADE_5586, Scenery.BUNSEN_BURNER_7332) { player, used, _ ->
            if(removeItem(player, used.id)) {
                sendMessage(player, "You burn the wooden handle away from the spade...")
                sendMessage(player, "...and are left with a metal spade with no handle.")
                addItemOrDrop(player, Items.ASHES_592)
                addItemOrDrop(player, Items.METAL_SPADE_5587)
            }
            return@onUseWith true
        }


        on(Scenery.STONE_DOOR_7343, SCENERY, "study") { player, node ->
            sendDialogueLines(player, "There is a stone slab here obstructing the door.", "There is a small hole in the slab that looks like it might be for a handle.")
            sendMessage(player, "It's nearly a perfect fit!")
            return@on true
        }

        onUseWith(SCENERY, Items.METAL_SPADE_5587, Scenery.STONE_DOOR_7343) { player, used, _ ->
            if(removeItem(player, used.id)) {
                sendMessage(player, "You slide the spade into the hole in the stone...")
                sendMessage(player, "It's nearly a perfect fit!")
                setVarbit(player, doorVarbit, 1)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, DoorVials.doorVialsArray, Scenery.STONE_DOOR_7344) { player, used, _ ->
            if(removeItem(player, used.id)) {
                setAttribute(player, DoorVials.doorVialsMap[used.id]!!.attribute, true)
                sendMessage(player, "You pour the vial onto the flat part of the spade.")
            }
            if (DoorVials.doorVialsRequiredMap.all { getAttribute(player, it.value.attribute, false) }) {
                sendMessage(player, "Something caused a reaction when mixed!")
                sendMessage(player, "The spade gets hotter, and expands slightly.")
                setVarbit(player, doorVarbit, 2)
            }
            return@onUseWith true
        }

        on(Scenery.STONE_DOOR_7344, SCENERY, "pull-spade") { player, node ->
            if (DoorVials.doorVialsRequiredMap.all { getAttribute(player, it.value.attribute, false) }) {
                sendMessage(player, "You pull on the spade...")
                sendMessage(player, "It works as a handle, and you swing the stone door open.")
                setVarbit(player, doorVarbit, 3)
            } else {
                sendMessage(player, "You pull on the spade...")
                sendMessage(player, "It comes loose, and slides out of the hole in the stone.")
                addItemOrDrop(player, Items.METAL_SPADE_5587)
                setVarbit(player, doorVarbit, 0)
            }
            return@on true
        }

        on(Scenery.OPEN_DOOR_7345, SCENERY, "walk-through") { player, node ->
            if(player.location.x <= 2477) {
                player.walkingQueue.addPath(2477, 4940)
                player.walkingQueue.addPath(2478, 4940)
            } else {
                player.walkingQueue.addPath(2477, 4940)
            }
            return@on true
        }


    }

}

private class VialShelfDialogueFile(private val flaskIdsArray: IntArray, private val specialAttribute: String? = null) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }.branch { _ -> flaskIdsArray.size }.let { branch ->

            branch.onValue(3)
                    // This is the only shelf with 3 vials of water.
                    .line("There are three vials on this shelf.")
                    .options("Take the vials?").let { optionBuilder ->
                        optionBuilder.option("Take one vial.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 1)
                                        print(getAttribute(player, specialAttribute, 3))
                                    }
                                }
                        optionBuilder.option("Take two vials.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    addItemOrDrop(player, flaskIdsArray[1])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 2)
                                    }
                                }
                        optionBuilder.option("Take all three vials.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    addItemOrDrop(player, flaskIdsArray[1])
                                    addItemOrDrop(player, flaskIdsArray[2])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 3) - 3)
                                    }
                                }
                        optionBuilder.option("Don't take a vial.")
                                .end()
                    }
            branch.onValue(2)
                    .line("There are two vials on this shelf.")
                    .options("Take the vials?").let { optionBuilder ->
                        optionBuilder.option("Take the first vial.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 1)
                                    } else {
                                        setAttribute(player, MissCheeversRoomListeners.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                                    }
                                }
                        optionBuilder.option("Take the second vial.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[1])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 1)
                                    } else {
                                        setAttribute(player, MissCheeversRoomListeners.Companion.Vials.vialMap[flaskIdsArray[1]]!!.attribute, true)
                                    }
                                }
                        optionBuilder.option("Take both vials.")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    addItemOrDrop(player, flaskIdsArray[1])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 2) - 2)
                                    } else {
                                        setAttribute(player, MissCheeversRoomListeners.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                                        setAttribute(player, MissCheeversRoomListeners.Companion.Vials.vialMap[flaskIdsArray[1]]!!.attribute, true)
                                    }
                                }
                    }

            branch.onValue(1)
                    .line("There is a vial on this shelf.")
                    .options("Take the vial?").let { optionBuilder ->
                        optionBuilder.option("YES")
                                .endWith { _, player ->
                                    addItemOrDrop(player, flaskIdsArray[0])
                                    if (specialAttribute != null) {
                                        setAttribute(player, specialAttribute, getAttribute(player, specialAttribute, 1) - 1)
                                    } else {
                                        setAttribute(player, MissCheeversRoomListeners.Companion.Vials.vialMap[flaskIdsArray[0]]!!.attribute, true)
                                    }
                                }
                        optionBuilder.option("NO")
                                .end()
                    }

            branch.onValue(0)
                    .line("There is nothing of interest on these shelves.")
        }
    }
}

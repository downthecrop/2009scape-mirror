package content.region.kandarin.yanille.quest.thehandinthesand.quest

import content.data.Quests
import content.global.skill.thieving.ThievingListeners
import content.region.kandarin.yanille.quest.thehandinthesand.dialogue.GuardCaptainDialogueFile
import core.api.addDialogueAction
import core.api.addItem
import core.api.amountInInventory
import core.api.animate
import core.api.face
import core.api.freeSlots
import core.api.getAttribute
import core.api.getQuestStage
import core.api.getScenery
import core.api.getVarbit
import core.api.hasAnItem
import core.api.openDialogue
import core.api.openInterface
import core.api.playAudio
import core.api.queueScript
import core.api.removeItem
import core.api.sendDialogue
import core.api.sendItemDialogue
import core.api.sendMessage
import core.api.sendPlayerDialogue
import core.api.setAttribute
import core.api.setInterfaceText
import core.api.setQuestStage
import core.api.setVarbit
import core.api.stopExecuting
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

private const val SANDYS_DESK_10805 = 10805
private const val SANDY_COFFEE_MUG_10807 = 10807
private const val BLANK_SCROLL_222 = 222
private const val BETTYS_COUNTER_WITH_VIAL_10813 = 10813
private const val BETTYS_SHOP_OPEN_DOOR_40109 = 40109
private val BETTYS_SHOP_DOORWAY_3016_3259 = Location.create(3016, 3259, 0)

/**
 * Listeners for The Hand in the Sand quest.
 * @author Edith
 */

class TheHandintheSandListeners : InteractionListener {

    companion object {
        private val BERTS_ROTA_TEXT = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
            "Bert's Rota - Copy",
            "Week 1   -   6am-10pm   -   50gps",
            "Week 2   -   6am-10pm   -   50gps",
            "Week 3   -   6am-10pm   -   50gps",
            "Week 4   -   6am-10pm   -   50gps",
            "Week 5   -   6am-10pm   -   50gps",
            "Week 6   -   6am-10pm   -   50gps",
        )

        private val SANDYS_ROTA_TEXT = arrayOf(
            "Sandy's Sand Corp - Brimhaven",
            "Bert's Rota - Original",
            "Week 1   -   9am-6pm   -   50gps",
            "Week 2   -   9am-6pm   -   50gps",
            "Week 3   -   9am-6pm   -   50gps",
            "Week 4   -   9am-6pm   -   50gps",
            "Week 5   -   9am-6pm   -   50gps",
            "Week 6   -   6am-10pm  -   50gps",
        )
    }

    override fun defineListeners() {

        /** Use beer on the Guard Captain. */
        onUseWith(IntType.NPC, Items.BEER_1917, NPCs.GUARD_CAPTAIN_3109) { player, _, npc ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) != TheHandintheSand.STAGE_QUEST_STARTED_5) {
                return@onUseWith false
            }

            openDialogue(player, GuardCaptainDialogueFile(), npc.asNpc())
            return@onUseWith true
        }

        /** Search Sandy's desk for his original rota. */
        on(SANDYS_DESK_10805, IntType.SCENERY, "search") { player, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            if (stage < TheHandintheSand.STAGE_SANDYS_ROTA_20 ||
                stage >= TheHandintheSand.STAGE_MAGIC_SCROLL_30
            ) {
                sendMessage(player, "You find nothing of interest.")
                return@on true
            }

            if (hasAnItem(player, Items.SANDYS_ROTA_6948).exists()) {
                sendDialogue(player, "You already have Sandy's original work rota.")
                return@on true
            }

            if (freeSlots(player) < 1) {
                sendPlayerDialogue(player, "I'd better make room in my inventory first!")
                return@on true
            }

            sendDialogue(
                player,
                "You quickly sift through some of the papers on Sandy's desk and find a work rota for Bert."
            )
            addItem(player, Items.SANDYS_ROTA_6948)
            setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_BOTH_ROTAS_25)
            return@on true
        }

        /** Read Bert's copy of the rota. */
        on(Items.BERTS_ROTA_6947, IntType.ITEM, "read") { player, _ ->
            openInterface(player, BLANK_SCROLL_222)
            BERTS_ROTA_TEXT.forEachIndexed { index, line ->
                setInterfaceText(player, line, BLANK_SCROLL_222, index + 1)
            }
            return@on true
        }

        /** Read Sandy's original rota. */
        on(Items.SANDYS_ROTA_6948, IntType.ITEM, "read") { player, _ ->
            openInterface(player, BLANK_SCROLL_222)
            SANDYS_ROTA_TEXT.forEachIndexed { index, line ->
                setInterfaceText(player, line, BLANK_SCROLL_222, index + 1)
            }
            return@on true
        }

        /** Pickpocket Sandy for sand. */
        on(NPCs.SANDY_3112, IntType.NPC, "pickpocket") { player, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            if (stage < TheHandintheSand.STAGE_SANDYS_ROTA_20 ||
                stage >= TheHandintheSand.STAGE_DRUG_SANDY_45
            ) {
                return@on false
            }

            if (freeSlots(player) < 1) {
                sendDialogue(player, "I'd better make room in my inventory first!")
                return@on true
            }

            val success = RandomFunction.roll(3)

            animate(player, ThievingListeners.PICKPOCKET_ANIM)
            sendDialogue(player, "You rummage around in Sandy's pockets.....")

            queueScript(player, ThievingListeners.PICKPOCKET_ANIM.duration, QueueStrength.NORMAL) { _: Int ->
                if (success) {
                    addItem(player, Items.SAND_6958)
                }
                return@queueScript stopExecuting(player)
            }

            player.dialogueInterpreter.actions.clear()
            addDialogueAction(player) { _, _ ->
                if (success) {
                    sendDialogue(player, "You find a small amount of sand.")
                } else {
                    sendDialogue(player, "You felt something but it slipped through your fingers...")
                }
                return@addDialogueAction
            }
            return@on true
        }

        /** Block adding white berries before red berries. */
        onUseWith(IntType.ITEM, Items.WHITE_BERRIES_239, Items.BOTTLED_WATER_6953) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_TRUTH_SERUM_40) {
                return@onUseWith false
            }
            sendMessage(player, "You'll need to use red berries first.")
            return@onUseWith true
        }

        /** Add redberries to bottled water to make redberry juice. */
        onUseWith(IntType.ITEM, Items.REDBERRIES_1951, Items.BOTTLED_WATER_6953) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_TRUTH_SERUM_40) {
                return@onUseWith false
            }
            if (amountInInventory(player, Items.REDBERRIES_1951) >= 1 &&
                amountInInventory(player, Items.BOTTLED_WATER_6953) >= 1
            ) {
                removeItem(player, Item(Items.REDBERRIES_1951, 1))
                removeItem(player, Item(Items.BOTTLED_WATER_6953, 1))
                addItem(player, Items.REDBERRY_JUICE_6954, 1)
                sendMessage(player, "Now you just need to add white berries to make the pink dye.")
            }
            return@onUseWith true
        }

        /** Add white berries to redberry juice to finish the pink dye. */
        onUseWith(IntType.ITEM, Items.WHITE_BERRIES_239, Items.REDBERRY_JUICE_6954) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_TRUTH_SERUM_40) {
                return@onUseWith false
            }
            if (amountInInventory(player, Items.WHITE_BERRIES_239) >= 1 &&
                amountInInventory(player, Items.REDBERRY_JUICE_6954) >= 1
            ) {
                removeItem(player, Item(Items.WHITE_BERRIES_239, 1))
                removeItem(player, Item(Items.REDBERRY_JUICE_6954, 1))
                addItem(player, Items.PINK_DYE_6955, 1)
            }
            return@onUseWith true
        }

        /** Dye the lantern lens to make a rose-tinted lens. */
        onUseWith(IntType.ITEM, Items.PINK_DYE_6955, Items.LANTERN_LENS_4542) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_TRUTH_SERUM_40) {
                return@onUseWith false
            }
            if (amountInInventory(player, Items.PINK_DYE_6955) >= 1 &&
                amountInInventory(player, Items.LANTERN_LENS_4542) >= 1
            ) {
                removeItem(player, Item(Items.PINK_DYE_6955, 1))
                removeItem(player, Item(Items.LANTERN_LENS_4542, 1))
                addItem(player, Items.ROSE_TINTED_LENS_6956, 1)
                sendItemDialogue(player, Items.ROSE_TINTED_LENS_6956, "You have successfully made the rose tinted lens!")
            }
            return@onUseWith true
        }

        /** Prevent players from adding sand to the truth serum themselves. */
        onUseWith(IntType.ITEM, Items.SAND_6958, Items.TRUTH_SERUM_6952) { player, _, _ ->
            sendDialogue(player, "Perhaps you should let Betty do that, it looks tricky.")
            return@onUseWith true
        }

        /** Pour truth serum into Sandy's coffee once he is distracted. */
        onUseWith(IntType.SCENERY, Items.TRUTH_SERUM_6952, SANDY_COFFEE_MUG_10807) { player, _, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            if (stage < TheHandintheSand.STAGE_DRUG_SANDY_45) {
                sendDialogue(player, "You'll need to have completed the truth serum before doing that!")
                return@onUseWith true
            }

            if (stage > TheHandintheSand.STAGE_DRUG_SANDY_45) {
                return@onUseWith false
            }

            if (!getAttribute(player, TheHandintheSand.ATTRIBUTE_SANDY_DISTRACTED, false)) {
                sendDialogue(player, "You'll need to distract Sandy before doing that.")
                return@onUseWith true
            }

            if (removeItem(player, Item(Items.TRUTH_SERUM_6952, 1))) {
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_INTERROGATE_SANDY_50)
                setVarbit(player, TheHandintheSand.VARBIT_SANDY_STATE_1535, TheHandintheSand.SANDY_NORMAL_0, true)
                sendDialogue(player, "You pour the serum into Sandy's coffee, then a little while later watch him drink it.")
                playAudio(player, Sounds.HANDSAND_SERUM_1592)
            }
            return@onUseWith true
        }

        /** Activate the magical orb before interrogating Sandy. */
        on(Items.MAGICAL_ORB_6950, IntType.ITEM, "activate") { player, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_INTERROGATE_SANDY_50) {
                sendMessage(player, "Nothing interesting happens.")
                return@on false
            }

            if (removeItem(player, Item(Items.MAGICAL_ORB_6950, 1))) {
                addItem(player, Items.MAGICAL_ORB_A_6951, 1)
                sendItemDialogue(player, Items.MAGICAL_ORB_A_6951,
                    "You rub the magical scrying orb as the Wizard told you, it starts to glow, recording everything it sees and hears, now you can talk to Sandy in Brimhaven.")
                playAudio(player, Sounds.HANDSAND_ORB_1590)
            }
            return@on true
        }

    }

}

/**
 * Instant-use listener for using the rose-tinted lens on Betty's counter from the shop doorway.
 */

class TruthSerumListener : InteractionListener {

    override fun defineListeners() {
        flagInstant()
        onUseWith(IntType.SCENERY, Items.ROSE_TINTED_LENS_6956, BETTYS_COUNTER_WITH_VIAL_10813) { player, _, _ ->
            if (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND) < TheHandintheSand.STAGE_TRUTH_SERUM_40) {
                return@onUseWith false
            }

            if (getVarbit(player, TheHandintheSand.VARBIT_BETTYS_COUNTER_1537) != TheHandintheSand.BETTYS_COUNTER_SHOW_VIAL_1) {
                return@onUseWith false
            }

            if (player.location != BETTYS_SHOP_DOORWAY_3016_3259 || getScenery(BETTYS_SHOP_DOORWAY_3016_3259)?.id != BETTYS_SHOP_OPEN_DOOR_40109) {
                sendMessage(player, "You need to be standing in the doorway.")
                return@onUseWith true
            }
            face(player, Location.create(3013, 3259, 0))

            if (removeItem(player, Item(Items.ROSE_TINTED_LENS_6956, 1))) {
                addItem(player, Items.TRUTH_SERUM_6952, 1)
                setVarbit(player, TheHandintheSand.VARBIT_BETTYS_COUNTER_1537, TheHandintheSand.BETTYS_COUNTER_NO_VIAL_0, true)
                setAttribute(player, TheHandintheSand.ATTRIBUTE_TRUTH_SERUM_MADE, true)
                sendItemDialogue(player, Items.TRUTH_SERUM_6952, "As you focus the light on the vial and Betty pours the potion in, the lens heats up and shatters. After a few seconds Betty hands you the vial of Truth Serum.")
                playAudio(player, Sounds.HANDSAND_VIALSHATTER_1587)
            }
            return@onUseWith true
        }
    }

}
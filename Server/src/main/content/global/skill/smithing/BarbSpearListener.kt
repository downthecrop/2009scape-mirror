package content.global.skill.smithing

import content.global.skill.fletching.log.GrammarHelpers
import content.global.skill.skillcapeperks.SkillcapePerks
import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.StringUtils
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import kotlin.math.min

/**
 * Listener for Barbarian smithing (spears and hastas).
 * @author Bishop
 */

data class BarbSpears(val barId: Int, val logId: Int, val spearId: Int, val hastaId: Int, val level: Int, val xp: Double)

class BarbSpearListener : InteractionListener {

    companion object {
        val SPEARS = listOf(
            BarbSpears(Items.BRONZE_BAR_2349, Items.LOGS_1511, Items.BRONZE_SPEAR_1237, Items.BRONZE_HASTA_11367, 5,  25.0),
            BarbSpears(Items.IRON_BAR_2351, Items.OAK_LOGS_1521, Items.IRON_SPEAR_1239, Items.IRON_HASTA_11369, 20, 50.0),
            BarbSpears(Items.STEEL_BAR_2353, Items.WILLOW_LOGS_1519, Items.STEEL_SPEAR_1241, Items.STEEL_HASTA_11371, 35, 75.0),
            BarbSpears(Items.MITHRIL_BAR_2359, Items.MAPLE_LOGS_1517, Items.MITHRIL_SPEAR_1243, Items.MITHRIL_HASTA_11373, 55, 100.0),
            BarbSpears(Items.ADAMANTITE_BAR_2361, Items.YEW_LOGS_1515, Items.ADAMANT_SPEAR_1245, Items.ADAMANT_HASTA_11375, 75, 125.0),
            BarbSpears(Items.RUNITE_BAR_2363, Items.MAGIC_LOGS_1513, Items.RUNE_SPEAR_1247, Items.RUNE_HASTA_11377, 90, 150.0),
        )
        val BAR_IDS = intArrayOf(Items.BRONZE_BAR_2349, Items.IRON_BAR_2351, Items.STEEL_BAR_2353, Items.MITHRIL_BAR_2359, Items.ADAMANTITE_BAR_2361, Items.RUNITE_BAR_2363)
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, BAR_IDS, Scenery.BARBARIAN_ANVIL_25349) { player, used, _ ->
            val spearEntry = SPEARS.find { it.barId == used.id } ?: return@onUseWith true
            openBarbSmith(player, spearEntry)
            return@onUseWith true
        }
    }

    private fun openBarbSmith(player: Player, spearEntry: BarbSpears) {
        if (!inInventory(player, Items.HAMMER_2347) && !SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING, player)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return
        }

        val spearStage = getAttribute(player, BarbarianTraining.attributeSpear, 0)
        val hastaStage = getAttribute(player, BarbarianTraining.attributeHasta, 0)
        if (spearStage < 1 && hastaStage < 1) {
            sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
            return
        }

        if (!inInventory(player, spearEntry.logId)) {
            sendDialogue(player, "You need some wood to make the shaft of this weapon.")
            return
        }

        if (getDynLevel(player, Skills.SMITHING) < spearEntry.level) {
            sendDialogue(player, "You need a Smithing level of ${spearEntry.level} to smith this.")
            return
        }

        val itemObjects = listOfNotNull(
            spearEntry.spearId.takeIf { spearStage >= 1 },
            spearEntry.hastaId.takeIf { hastaStage >= 1 }
        ).map { Item(it) }.toTypedArray()

        val type = SkillDialogueHandler.SkillDialogue.forLength(itemObjects.size) ?: return
        object : SkillDialogueHandler(player, type, *itemObjects) {
            override fun create(amount: Int, index: Int) {
                player.pulseManager.run(BarbSpearPulse(player, spearEntry, isHasta = itemObjects[index].id == spearEntry.hastaId, amount = amount))
            }
            override fun getAll(index: Int): Int {
                return min(amountInInventory(player, spearEntry.barId), amountInInventory(player, spearEntry.logId))
            }
            override fun getName(item: Item): String {
                return GrammarHelpers.aOrAn(item.name) + " " + super.getName(item)
            }
        }.open()

        // Force authentic spear name formatting
        if (type == SkillDialogueHandler.SkillDialogue.ONE_OPTION) {
            val item = itemObjects[0]
            val label = GrammarHelpers.aOrAn(item.name) + " " + StringUtils.formatDisplayName(item.name)
            player.packetDispatch.sendString("<br><br><br><br>$label", 309, 6)
            // Override the static cache prompt "How many would you like to make?"
            player.packetDispatch.sendString("What would you like to make?", 309, 7)
        }

        // Apply authentic rotation to hasta display in the dialogue
        val hastaIndex = itemObjects.indexOfFirst { it.id == spearEntry.hastaId }
        if (hastaIndex != -1) {
            player.packetDispatch.sendAngleOnInterface(type.interfaceId, 2 + hastaIndex, 1300, 500, 900)
        }
    }
}

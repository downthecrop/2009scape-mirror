package content.global.skill.smithing

import content.global.skill.skillcapeperks.SkillcapePerks
import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items

/**
 * Handles the pulse for Barbarian smithing (spears and hastas)
 * @author Bishop
 */

class BarbSpearPulse(
    player: Player,
    private val spearEntry: BarbSpears,
    private val isHasta: Boolean,
    var amount: Int
) : SkillPulse<Item>(player, Item(if (isHasta) spearEntry.hastaId else spearEntry.spearId)) {

    override fun checkRequirements(): Boolean {
        player.interfaceManager.close()

        if (!inInventory(player, Items.HAMMER_2347) && !SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING, player)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }

        if (getDynLevel(player, Skills.SMITHING) < spearEntry.level) {
            sendDialogue(player, "You need a Smithing level of ${spearEntry.level} to smith this.")
            return false
        }

        if (!inInventory(player, spearEntry.barId)) {
            sendDialogue(player, "You don't have the required bar to make this.")
            return false
        }

        if (!inInventory(player, spearEntry.logId)) {
            sendDialogue(player, "You need some wood to make the shaft of this weapon.")
            return false
        }

        return true
    }

    override fun animate() {
        if (SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING, player)) {
            animate(player, Animation(2068))
        } else {
            animate(player, Animation(6712))
        }
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }

        if (!removeItem(player, Item(spearEntry.barId)) || !removeItem(player, Item(spearEntry.logId))) {
            return false
        }

        val productId = if (isHasta) spearEntry.hastaId else spearEntry.spearId
        addItem(player, productId)
        rewardXP(player, Skills.SMITHING, spearEntry.xp)
        sendMessage(player, "You make a ${ItemDefinition.forId(productId).name}.")

        if (!isHasta && getAttribute(player, BarbarianTraining.attributeSpear, 0) == 1) {
            setAttribute(player, BarbarianTraining.attributeSpear, 2)
            sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
        } else if (isHasta && getAttribute(player, BarbarianTraining.attributeHasta, 0) == 1) {
            setAttribute(player, BarbarianTraining.attributeHasta, 2)
            sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
        }

        amount--
        return amount < 1
    }
}

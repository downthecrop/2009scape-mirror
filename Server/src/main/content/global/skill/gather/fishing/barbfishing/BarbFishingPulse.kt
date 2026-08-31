package content.global.skill.gather.fishing.barbfishing

import content.global.skill.fishing.Fish
import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_FISH
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Pulse used for barbarian fishing
 * @author Ceikry
 * @author Bishop
 */
class BarbFishingPulse(player: Player) : SkillPulse<NPC>(player,NPC(NPCs.FISHING_SPOT_2722)) {
    override fun checkRequirements(): Boolean {
        if(!hasLevelDyn(player, Skills.FISHING, 48)){
            sendDialogue(player, "You need a Fishing level of at least 48 to catch these fish.")
            return false
        }
        if(!hasLevelDyn(player, Skills.AGILITY, 15) || !hasLevelDyn(player, Skills.STRENGTH, 15)){
            sendDialogue(player, "You need a strength and agility level of at least 15 to fish here.")
            return false
        }
        if(!inInventory(player, Items.BARBARIAN_ROD_11323)){
            sendDialogue(player, "You need a barbarian fishing rod to fish here.")
            return false
        }
        if(freeSlots(player) == 0){
            sendDialogue(player, "You can't carry any more fish.")
            return false
        }
        if(BAITS.none { inInventory(player, it) }){
            sendDialogue(player, "You don't have any bait with which to fish.")
            return false
        }
        return true
    }

    companion object {
        private val BAITS = intArrayOf(
            Items.FISH_OFFCUTS_11334,
            Items.FEATHER_314,
            Items.FISHING_BAIT_313,
            Items.ROE_11324,
            Items.CAVIAR_11326,
        )
    }

    override fun animate() {
        animate(player, Animation(622))
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            super.setDelay(5)
            return false
        }
        val fishingLevel = getDynLevel(player, Skills.FISHING)
        val boostedLevel = fishingLevel + getFamiliarBoost(player, Skills.FISHING)
        val strength = getDynLevel(player, Skills.STRENGTH)
        val agility = getDynLevel(player, Skills.AGILITY)

        var caught: Fish? = null
        for (f in arrayOf(Fish.LEAPING_TROUT, Fish.LEAPING_SALMON, Fish.LEAPING_STURGEON)) {
            if (f.level > fishingLevel) continue
            if (f == Fish.LEAPING_SALMON && (strength < 30 || agility < 30)) continue
            if (f == Fish.LEAPING_STURGEON && (strength < 45 || agility < 45)) continue
            if (RandomFunction.random(0.0, 1.0) < f.getSuccessChance(boostedLevel)) {
                caught = f
                break
            }
        }

        if (caught != null) {
            BAITS.firstOrNull { removeItem(player, Item(it, 1)) }
            addItem(player, caught.id, 1)
            rewardXP(player, Skills.FISHING, caught.experience)
            val strAgiXP = when (caught) {
                Fish.LEAPING_TROUT    -> 5.0
                Fish.LEAPING_SALMON   -> 6.0
                Fish.LEAPING_STURGEON -> 7.0
                else -> 5.0
            }
            rewardXP(player, Skills.AGILITY, strAgiXP)
            rewardXP(player, Skills.STRENGTH, strAgiXP)
            var fishCaught = getAttribute(player, "$STATS_BASE:$STATS_FISH", 0)
            setAttribute(player, "/save:$STATS_BASE:$STATS_FISH", ++fishCaught)
            sendMessage(player, "You catch a ${getItemName(caught.id).lowercase()}.")
            // Progress Barbarian training if this is your first heavy rod catch
            if (getAttribute(player, BarbarianTraining.attributeRod, 0) == 1) {
                setAttribute(player, BarbarianTraining.attributeRod, 2)
                sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
            }
        }
        super.setDelay(5)
        return freeSlots(player) == 0
    }
}
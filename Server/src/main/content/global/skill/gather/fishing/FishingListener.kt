package content.global.skill.gather.fishing

import content.global.handlers.item.equipment.fistofguthixgloves.FOGGlovesManager
import content.global.skill.fishing.Fish
import content.global.skill.fishing.FishingOption
import content.global.skill.fishing.FishingSpot
import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.skill.skillcapeperks.SkillcapePerks.Companion.isActive
import content.global.skill.summoning.familiar.Forager
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_FISH
import core.game.world.GameWorld
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import core.tools.colorize
import org.rs09.consts.Items

class FishingListener : InteractionListener{
    override fun defineListeners() {
        val SPOT_IDS = FishingSpot.values().flatMap { it.ids.toList() }.toIntArray()
        defineInteraction(
                IntType.NPC,
                SPOT_IDS,
                "net", "lure", "bait", "harpoon", "cage", "fish",
                persistent = true,
                allowedDistance = 1,
                handler = ::handleFishing
        )
    }

    private fun handleFishing(player: Player, node: Node, state: Int) : Boolean {
        val npc = node as? NPC ?: return clearScripts(player)
        val spot = FishingSpot.forId(npc.id) ?: return clearScripts(player)
        val op = spot.getOptionByName(getUsedOption(player)) ?: return clearScripts(player)

        var forager: Forager? = null

        if (player.familiarManager.hasFamiliar() && player.familiarManager.familiar is Forager) {
            forager = player.familiarManager.familiar as Forager
        }

        if (!finishedMoving(player)) {
            return restartScript(player)
        }

        if (state == 0) {
            if (!checkRequirements(player, op, node)) return clearScripts(player)
            forager?.let {
                val dest = player.location.transform(player.direction)
                Pathfinder.find(it, dest).walk(it)
            }
            when (op.option) {
                "cage" -> if (spot.name == "CAGE_HARPOON") {
                    sendMessage(player, "You attempt to catch a lobster.")
                } else sendMessage(player, "You attempt to catch a crayfish.")
                "harpoon" -> sendMessage(player, "You start harpooning fish.")
                "net" -> sendMessage(player, "You cast out your net...")
                in arrayOf("bait", "lure") -> {
                    sendMessage(player, "You cast out your line...")
                    sendMessage(player, "You attempt to catch a fish.")
                }
                else -> { // Probably not authentic, but covers unknown cases.
                    sendMessage(player, "You attempt to catch some fish...")
                }
            }
        }

        if (clockReady(player, Clocks.SKILLING)) {
            anim(player, op)
            forager?.handlePassiveAction()

            val fish = op?.rollFish(player) ?: return delayClock(player, Clocks.SKILLING, 5)
            if (!hasSpaceFor(player, Item(fish.id)) || !op.removeBait(player)) return restartScript(player)
            player.dispatch(ResourceProducedEvent(fish.id, fish.getItem().amount, node))
            val item = fish.getItem()
            val bigFishId = Fish.getBigFish(fish)
            val bigFishChance = if (GameWorld.settings?.isDevMode == true) 10 else 5000
            if (bigFishId != null && RandomFunction.roll(bigFishChance)) {
                sendMessage(player, "You catch an enormous" + getItemName(fish.id).lowercase().replace("raw", "") + "!")
                addItemOrDrop(player, bigFishId, 1)
            } else {
                var msg = when (fish) {
                    in arrayOf(Fish.ANCHOVIE, Fish.SHRIMP, Fish.SEAWEED) -> "You catch some "
                    in arrayOf(Fish.OYSTER) -> "You catch an "
                    else -> "You catch a "
                }
                msg += getItemName(fish.id).lowercase().replace("raw ", "").replace("big ", "")
                msg += if (fish == Fish.SHARK) "!" else "."
                sendMessage(player, msg)
                addItemOrDrop(player, item.id, item.amount)
            }

            if (isActive(SkillcapePerks.GREAT_AIM, player) && RandomFunction.roll(20)) {
                addItemOrDrop(player, item.id, item.amount)
                sendMessage(player, colorize("%RYour expert aim catches you a second fish."))
                player.incrementAttribute("/save:$STATS_BASE:$STATS_FISH")
            }

            player.incrementAttribute("/save:$STATS_BASE:$STATS_FISH")
            var xp = fish.experience
            if ((item.id == Items.RAW_SWORDFISH_371 && inEquipment(player, Items.SWORDFISH_GLOVES_12860))
                || (item.id == Items.RAW_SHARK_383 && inEquipment(player, Items.SHARK_GLOVES_12861))) {
                xp += 100
                FOGGlovesManager.updateCharges(player)
            }
            rewardXP(player, Skills.FISHING, xp)
            delayClock(player, Clocks.SKILLING, 5)
            if (!checkRequirements(player, op, node)) return clearScripts(player)
        }
        return keepRunning(player)
    }

    private fun anim(player: Player, option: FishingOption) {
        if (animationFinished(player))
            animate(player, option.animation)
    }

    private fun checkRequirements(player: Player, option: FishingOption, node: Node) : Boolean {
        if (!inInventory(player, option.tool) && !hasBarbTail(player, option)) {
            // The fly fishing rod & net dialogue is confirmed from videos. Others are assumptions based upon this.
            var msg = "You need a "
            msg += if (getItemName(option.tool).contains("net", true)) "net to " else "${getItemName(option.tool).lowercase()} to "
            msg += if (option.option in arrayOf("lure", "bait")) "${option.option} these fish." else "catch these fish."
            sendDialogue(player, msg)
            return false
        }
        if (!option.hasBait(player)) {
            var msg = "You don't have any " + option.getBaitName().lowercase()
            msg += if (option.getBaitName() == getItemName(Items.FISHING_BAIT_313)) " left." else "s left."
            sendDialogue(player, msg)
            return false
        }
        if (!hasLevelDyn(player, Skills.FISHING, option.level)) {
            sendDialogue(player, "You need a Fishing level of at least ${option.level} to ${option.option} these fish.")
            return false
        }
        if (freeSlots(player) == 0) {
            if (option.fish.contains(Fish.LOBSTER)) {
                sendDialogue(player, "You can't carry any more lobsters.")
            } else {
                sendDialogue(player, "You can't carry any more fish.")
            }
            return false
        }
        return node.isActive && node.location.withinDistance(player.location, 1)
    }


    private fun hasBarbTail(player: Player, option: FishingOption): Boolean {
        val bh = FishingOption.BARB_HARPOON.tool
        if (option == FishingOption.HARPOON || option == FishingOption.SHARK_HARPOON) {
            if (inInventory(player, bh) || inEquipment(player, bh)) return true
        }
        return false
    }
}

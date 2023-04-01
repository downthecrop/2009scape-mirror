package content.global.skill.gather.fishing

import content.global.skill.fishing.Fish
import content.global.skill.fishing.FishingOption
import content.global.skill.fishing.FishingSpot
import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.skill.skillcapeperks.SkillcapePerks.Companion.isActive
import content.global.skill.summoning.familiar.Forager
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.Clocks
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_FISH
import core.game.world.map.path.Pathfinder
import core.tools.RandomFunction
import core.tools.colorize

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

        if (state == 1) {
            if (!checkRequirements(player, op, node)) return clearScripts(player)
            forager?.let {
                val dest = player.location.transform(player.direction)
                Pathfinder.find(it, dest).walk(it)
            }
            sendMessage(player, "You attempt to catch some fish...")
        }

        if (clockReady(player, Clocks.SKILLING)) {
            anim(player, op)
            forager?.handlePassiveAction()

            val fish = op?.rollFish(player) ?: return delayClock(player, Clocks.SKILLING, 5)
            if (!hasSpaceFor(player, Item(fish.id)) || !op.removeBait(player)) return restartScript(player)
            player.dispatch(ResourceProducedEvent(fish.id, fish.getItem().amount, node))
            val item = fish.getItem()

            if (isActive(SkillcapePerks.GREAT_AIM, player) && RandomFunction.roll(20)) {
                addItemOrDrop(player, item.id, item.amount)
                sendMessage(player, colorize("%RYour expert aim catches you a second fish."))
            }

            addItemOrDrop(player, item.id, item.amount)
            player.incrementAttribute("$STATS_BASE:$STATS_FISH")
            rewardXP(player, Skills.FISHING, fish.experience)
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
            player.dialogueInterpreter.sendDialogue("You need a " + getItemName(option.tool).lowercase() + " to catch these fish.")
            return false
        }
        if (!option.hasBait(player)) {
            player.dialogueInterpreter.sendDialogue("You don't have any " + option.getBaitName().lowercase() + "s left.")
            return false
        }
        if (player.skills.getLevel(Skills.FISHING) < option!!.level) {
            val f = option!!.fish[option!!.fish.size - 1]
            player.dialogueInterpreter.sendDialogue("You need a fishing level of " + f.level + " to catch " + (if (f == Fish.SHRIMP || f == Fish.ANCHOVIE) "" else "a") + " " + getItemName(f.id).lowercase() + ".".trim { it <= ' ' })
            return false
        }
        if (player.inventory.freeSlots() == 0) {
            player.dialogueInterpreter.sendDialogue("You don't have enough space in your inventory.")
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
package content.global.skill.gather.fishing

import content.global.handlers.item.equipment.fistofguthixgloves.FOGGlovesManager
import content.global.skill.fishing.Fish
import content.global.skill.fishing.FishingOption
import content.global.skill.fishing.FishingSpot
import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.skill.skillcapeperks.SkillcapePerks.Companion.isActive
import content.global.skill.summoning.familiar.Forager
import content.region.fremennik.diary.FremennikAchievementDiary.Companion.HardTasks
import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
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
        val isBarehand = canBarehandFish(player, op)
        var forager: Forager? = null
        if (player.familiarManager.hasFamiliar() && player.familiarManager.familiar is Forager) {
            forager = player.familiarManager.familiar as Forager
        }
        if (!finishedMoving(player)) {
            return restartScript(player)
        }
        if (state == 0) {
            if (!checkRequirements(player, op, node, isBarehand)) return clearScripts(player)
            forager?.let {
                val dest = player.location.transform(player.direction)
                Pathfinder.find(it, dest).walk(it)
            }
            if (isBarehand) {
                animate(player, BAREHAND_FISHING_START_ANIM)
            }
            when (op.option) {
                "cage" -> if (spot.name == "CAGE_HARPOON") {
                    sendMessage(player, "You attempt to catch a lobster.")
                } else sendMessage(player, "You attempt to catch a crayfish.")
                "harpoon" -> {
                    if (isBarehand) {
                        sendMessage(player, "You start to lure the fish.")
                    } else {
                        sendMessage(player, "You start harpooning fish.")
                    }
                }
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
            anim(player, op, isBarehand)
            forager?.handlePassiveAction()
            val fish = op?.rollFish(player) ?: return delayClock(player, Clocks.SKILLING, 5)
            // For barehand fishing, check if player meets the specific requirements for this fish
            if (isBarehand && !meetsBarehandRequirements(player, fish)) {
                // Player doesn't meet requirements for this specific fish, skip this catch
                return delayClock(player, Clocks.SKILLING, 5)
            }
            if (!hasSpaceFor(player, Item(fish.id)) || !op.removeBait(player)) return restartScript(player)
            if (isBarehand) {
                animate(player, getBarehandCatchAnimation(fish))
                if (fish == Fish.TUNA) {
                    player.achievementDiaryManager.finishTask(player, DiaryType.FREMENNIK,2, HardTasks.CATCH_TUNA_WITHOUT_HARPOON)
                }
            }
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
            // Award bonus Strength XP for barehand fishing
            // Progress Barbarian training if this is your first barehanded catch
            if (isBarehand) {
                rewardXP(player, Skills.STRENGTH, Fish.barehandStrengthXp[fish]!!)
                if (getAttribute(player, BarbarianTraining.attributeFist, 0) == 1) {
                    setAttribute(player, BarbarianTraining.attributeFist, 2)
                    sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
                }
            }

            delayClock(player, Clocks.SKILLING, 5)
            if (!checkRequirements(player, op, node, isBarehand)) return clearScripts(player)
        } else if (isBarehand) {
            anim(player, op, isBarehand)
        }
        return keepRunning(player)
    }

    private fun anim(player: Player, option: FishingOption, isBarehand: Boolean = false) {
        if (isBarehand) {
            if (player.clocks[Clocks.ANIMATION_END] <= GameWorld.ticks) {
                animate(player, BAREHAND_FISHING_LOOP_ANIM)
            }
        } else {
            if (animationFinished(player)) {
                animate(player, option.animation)
            }
        }
    }

    private fun checkRequirements(player: Player, option: FishingOption, node: Node, isBarehand: Boolean = false) : Boolean {
        if (!inInventory(player, option.tool) && !hasBarbTail(player, option) && !isBarehand) {
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
        // For barehand fishing, level requirements are already checked in canBarehandFish
        if (!isBarehand && !hasLevelDyn(player, Skills.FISHING, option.level)) {
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
	return node.isActive && node.location.withinMaxnormDistance(player.location, 1)
    }

    private fun hasBarbTail(player: Player, option: FishingOption): Boolean {
        val bh = FishingOption.BARB_HARPOON.tool
        if (option == FishingOption.HARPOON || option == FishingOption.SHARK_HARPOON) {
            if (inInventory(player, bh) || inEquipment(player, bh)) return true
        }
        return false
    }

    /**
     * Checks if player has unlocked barehand fishing from Otto Godblessed.
     */
    private fun hasBarehandUnlock(player: Player): Boolean {
        return getAttribute(player, BarbarianTraining.attributeFist, 0) >= 1
    }

    /**
     * Checks if player has any harpoon (regular or barb-tail) in inventory or equipment.
     */
    private fun hasAnyHarpoon(player: Player): Boolean {
        return inInventory(player, Items.HARPOON_311) ||
               inInventory(player, Items.BARB_TAIL_HARPOON_10129) ||
               inEquipment(player, Items.BARB_TAIL_HARPOON_10129)
    }

    /**
     * Checks if player can use barehand fishing for this fishing spot.
     * Requires: harpoon option, no harpoon in inventory/equipment, barehand unlock, minimum levels.
     * Source: https://runescape.wiki/w/Barbarian_Training?oldid=834497#Bare_Hand_Fishing
     */
    private fun canBarehandFish(player: Player, option: FishingOption): Boolean {
        if (option != FishingOption.HARPOON && option != FishingOption.SHARK_HARPOON) return false
        if (hasAnyHarpoon(player)) return false
        if (!hasBarehandUnlock(player)) return false
        // Check minimum levels for barehand fishing
        // Tuna/Swordfish spot: 55 Fishing, 35 Strength minimum
        // Shark spot: 96 Fishing, 76 Strength minimum
        return when (option) {
            FishingOption.HARPOON -> hasLevelDyn(player, Skills.FISHING, Fish.barehandFishingReq[Fish.TUNA]!!) && hasLevelDyn(player, Skills.STRENGTH, Fish.barehandStrengthReq[Fish.TUNA]!!)
            FishingOption.SHARK_HARPOON -> hasLevelDyn(player, Skills.FISHING, Fish.barehandFishingReq[Fish.SHARK]!!) && hasLevelDyn(player, Skills.STRENGTH, Fish.barehandStrengthReq[Fish.SHARK]!!)
            else -> false
        }
    }

    /**
     * Checks if player meets the specific Strength and Fishing requirements to catch a specific fish barehanded.
     */
    private fun meetsBarehandRequirements(player: Player, fish: Fish): Boolean {
        val fishingReq = Fish.barehandFishingReq[fish] ?: return false
        val strengthReq = Fish.barehandStrengthReq[fish] ?: return false
        return hasLevelDyn(player, Skills.FISHING, fishingReq) && hasLevelDyn(player, Skills.STRENGTH, strengthReq)
    }

    /**
     * Gets the barehand fishing animation for a specific fish.
     * Animations from game data:
     * - 6710/6711: barehand tuna fishing
     * - 6707/6708: barehand swordfish fishing
     * - 6705/6706: barehand shark fishing
     * - 6703/6704: generic barehand fishing
     */
    private fun getBarehandCatchAnimation(fish: Fish): Int {
        return when (fish) {
            Fish.TUNA -> 6710
            Fish.SWORDFISH -> 6707
            Fish.SHARK -> 6705
            else -> 6703 // Generic barehand animation
        }
    }

    /**
     * Barehand fishing animations.
     * 6703 = start animation (hand goes into water)
     * 6704 = loop animation (hand stays in water waiting for catch)
     */
    private val BAREHAND_FISHING_START_ANIM = 6703
    private val BAREHAND_FISHING_LOOP_ANIM = 6704
}

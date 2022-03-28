package rs09.game.node.entity.skill.gather.fishing

import api.events.ResourceProducedEvent
import core.game.content.global.SkillingPets
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.skillertasks.SkillTasks
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.fishing.Fish
import core.game.node.entity.skill.fishing.FishingOption
import core.game.node.entity.skill.summoning.familiar.Forager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import rs09.game.node.entity.player.info.stats.STATS_BASE
import rs09.game.node.entity.player.info.stats.STATS_FISH
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks.Companion.isActive
import rs09.game.world.GameWorld.Pulser
import rs09.tools.stringtools.colorize

/**
 * Handles a fishing pulse.
 *
 * @author Ceikry
 */
class FishingPulse(player: Player?, npc: NPC, private val option: FishingOption?) : SkillPulse<NPC?>(player, npc) {
    /**
     * Represents the fish type.
     */
    private var fish: Fish? = null

    /**
     * Represents the base location the npc was at.
     */
    private val location: Location = npc.location

    override fun start() {
        if (player.familiarManager.hasFamiliar() && player.familiarManager.familiar is Forager) {
            val forager = player.familiarManager.familiar as Forager
            val dest = player.location.transform(player.direction)
            Pathfinder.find(forager.location, dest).walk(forager)
        }
        super.start()
    }

    override fun checkRequirements(): Boolean {
        if (option == null) {
            return false
        }
        player.debug(player.inventory.containsItem(option.tool).toString())
        if (!player.inventory.containsItem(option.tool) && !hasBarbTail()) {
            //System.out.println(isBareHanded(player));
            player.dialogueInterpreter.sendDialogue("You need a " + option.tool.name.toLowerCase() + " to catch these fish.")
            stop()
            return false
        }
        if (!option.hasBait(player.inventory)) {
            player.dialogueInterpreter.sendDialogue("You don't have any " + option.getBaitName().toLowerCase() + "s left.")
            stop()
            return false
        }
        if (player.skills.getLevel(Skills.FISHING) < option!!.level) {
            val f = option!!.fish[option!!.fish.size - 1]
            player.dialogueInterpreter.sendDialogue("You need a fishing level of " + f.level + " to catch " + (if (f == Fish.SHRIMP || f == Fish.ANCHOVIE) "" else "a") + " " + f.item.name.toLowerCase() + ".".trim { it <= ' ' })
            stop()
            return false
        }
        if (player.inventory.freeSlots() == 0) {
            player.dialogueInterpreter.sendDialogue("You don't have enough space in your inventory.")
            stop()
            return false
        }
        if (location !== node!!.location || !node!!.isActive || node!!.isInvisible) {
            stop()
            return false
        }
        return true
    }

    override fun animate() {
        if (isBareHanded(player)) {
            player.animate(Animation(6709))
            Pulser.submit(object : Pulse(1) {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        5 -> getCatchAnimationAndLoot(player)
                    }
                    return false
                }
            })
        } else {
            player.animate(option!!.animation)
        }
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            super.setDelay(5)
            return false
        }
        if (player.familiarManager.hasFamiliar() && player.familiarManager.familiar is Forager) {
            val forager = player.familiarManager.familiar as Forager
            forager.handlePassiveAction()
        }
        if (success()) {
            if (player.inventory.hasSpaceFor(fish!!.item) && option!!.removeBait(player.inventory)) {
                if (player.skillTasks.hasTask()) {
                    updateSkillTask()
                }
                player.dispatch(ResourceProducedEvent(fish!!.item.id, fish!!.item.amount, node!!))
                SkillingPets.checkPetDrop(player, SkillingPets.HERON)
                val item = fish!!.item
                if (isActive(SkillcapePerks.GREAT_AIM, player) && RandomFunction.random(100) <= 5) {
                    player.inventory.add(item)
                    player.sendMessage(colorize("%RYour expert aim catches you a second fish."))
                }
                player.inventory.add(item)
                var fishCaught = player.getAttribute(STATS_BASE + ":" + STATS_FISH, 0)
                player.setAttribute("/save:$STATS_BASE:$STATS_FISH", ++fishCaught)
                player.skills.addExperience(Skills.FISHING, fish!!.experience, true)
                message(2)
            }
        }
        return player.inventory.freeSlots() == 0
    }

    fun updateSkillTask() {
        when (fish) {
            Fish.ANCHOVIE -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FANCHOVIES1)
                player.skillTasks.decreaseTask(player, SkillTasks.FANCHOVIES2)
            }
            Fish.HERRING -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FHERRING1)
                player.skillTasks.decreaseTask(player, SkillTasks.FHERRING2)
            }
            Fish.LOBSTER -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FLOBSTER1)
                player.skillTasks.decreaseTask(player, SkillTasks.FLOBSTER2)
            }
            Fish.SALMON -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FSALMON1)
                player.skillTasks.decreaseTask(player, SkillTasks.FSALMON2)
            }
            Fish.SHARK -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FSHARK1)
                player.skillTasks.decreaseTask(player, SkillTasks.FSHARK2)
                player.skillTasks.decreaseTask(player, SkillTasks.FSHARK3)
            }
            Fish.SHRIMP -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FSHRIMP1)
                player.skillTasks.decreaseTask(player, SkillTasks.FSHRIMP2)
            }
            Fish.SWORDFISH -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FSWORD1)
                player.skillTasks.decreaseTask(player, SkillTasks.FSWORD2)
            }
            Fish.TROUT -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FTROUT1)
                player.skillTasks.decreaseTask(player, SkillTasks.FTROUT2)
            }
            Fish.TUNA -> {
                player.skillTasks.decreaseTask(player, SkillTasks.FTUNA1)
                player.skillTasks.decreaseTask(player, SkillTasks.FTUNA2)
            }
        }
    }

    private fun isBareHanded(p: Player): Boolean {
        if (option == FishingOption.HARPOON) {
            if (checkFish(p) > 0 && !(player.inventory.containsItem(
                    option.tool
                ) || player.equipment.containsItem(option.tool))
            ) {
                return true
            }
            if (checkFish(p) > 2 && !(player.inventory.containsItem(
                    option.tool
                ) || player.equipment.containsItem(option.tool))
            ) {
                return true
            }
        }
        return false
    }

    private fun getCatchAnimationAndLoot(p: Player): Int {
        val fishingFor = checkFish(p)
        when (node!!.id) {
            324 -> when (fishingFor) {
                1 -> {
                    p.animate(Animation(6710))
                    p.skills.addExperience(Skills.FISHING, 80.0)
                    p.skills.addExperience(Skills.STRENGTH, 8.0)
                    p.inventory.add(Item(359))
                }
                2, 3 -> if (RandomFunction.random(1) == 1) {
                    p.animate(Animation(6710))
                    p.skills.addExperience(Skills.FISHING, 80.0)
                    p.skills.addExperience(Skills.STRENGTH, 8.0)
                    p.inventory.add(Item(359))
                } else {
                    p.animate(Animation(6707))
                    p.skills.addExperience(Skills.FISHING, 100.0)
                    p.skills.addExperience(Skills.STRENGTH, 10.0)
                    p.inventory.add(Item(371))
                }
            }
            313 -> {
                p.animate(Animation(6705))
                p.skills.addExperience(Skills.FISHING, 110.0)
                p.skills.addExperience(Skills.STRENGTH, 11.0)
                p.inventory.add(Item(383))
            }
        }
        return 0
    }

    /**
     * Checks if they have the barb tail harpoon.
     *
     * @return `True` if so.
     */
    private fun hasBarbTail(): Boolean {
        if (option == FishingOption.HARPOON) {
            if (player.inventory.containsItem(FishingOption.BARB_HARPOON.tool) || player.equipment.containsItem(
                    FishingOption.BARB_HARPOON.tool
                )
            ) {
                return true
            }
        }
        return false
    }

    override fun message(type: Int) {
        when (type) {
            0 -> player.packetDispatch.sendMessage(option!!.startMessage)
            2 -> {
                player.packetDispatch.sendMessage(
                    if (fish == Fish.ANCHOVIE || fish == Fish.SHRIMP) "You catch some " + fish!!.item.name.toLowerCase()
                        .replace("raw", "")
                        .trim { it <= ' ' } + "." else "You catch a " + fish!!.item.name.toLowerCase()
                        .replace("raw", "").trim { it <= ' ' } + ".")
                if (player.inventory.freeSlots() == 0) {
                    player.dialogueInterpreter.sendDialogue("You don't have enough space in your inventory.")
                    stop()
                }
            }
        }
    }

    /**
     * Method used to check if the catch was a success.
     *
     * @return `True` if so.
     */
    private fun success(): Boolean {
        if (delay == 1) {
            return false
        }
        fish = option!!.rollFish(player)
        return fish != null
    }

    companion object {
        fun checkFish(p: Player): Int {
            return if (p.skills.getLevel(Skills.FISHING) >= 55 && p.skills.getLevel(Skills.STRENGTH) >= 35) {
                if (p.skills.getLevel(Skills.FISHING) >= 70 && p.skills.getLevel(Skills.STRENGTH) >= 50) {
                    if (p.skills.getLevel(Skills.FISHING) >= 96 && p.skills.getLevel(Skills.STRENGTH) >= 76) {
                        3
                    } else 2
                } else 1
            } else 0
        }
    }

    /**
     * Constructs a new `FishingPulse` `Object`.
     *
     * @param player the player.
     * @param npc    the fishing spot NPC.
     * @param option The fishing option.
     */
    init {
    }
}

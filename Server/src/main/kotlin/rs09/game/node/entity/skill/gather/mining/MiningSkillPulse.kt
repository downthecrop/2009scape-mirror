package rs09.game.node.entity.skill.gather.mining

import api.*
import api.events.ResourceProducedEvent
import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.content.dialogue.FacialExpression
import core.game.content.global.SkillingPets
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.entity.skill.gather.mining.MiningNode
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs09.consts.Items
import rs09.game.node.entity.player.info.stats.STATS_BASE
import rs09.game.node.entity.player.info.stats.STATS_ROCKS
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks

/**
 * Mining skill pulse
 * @author ceik
 */
class MiningSkillPulse(private val player: Player, private val node: Node) : Pulse(1, player, node) {
    private var resource: MiningNode? = null
    private var isMiningEssence = false
    private var isMiningGems = false
    private var ticks = 0
    protected var resetAnimation = true
    fun message(type: Int) {
        if (type == 0) {
            player.packetDispatch.sendMessage("You swing your pickaxe at the rock...")
        }
    }

    override fun pulse(): Boolean {
        if (!checkRequirements()) {
            return true
        }
        animate()
        return reward()
    }

    override fun stop() {
        if (resetAnimation) {
            player.animate(Animation(-1, Animator.Priority.HIGH))
        }
        super.stop()
        message(1)
    }

    override fun start() {
        resource = MiningNode.forId(node.id)
        if (MiningNode.isEmpty(node.id)) {
            player.packetDispatch.sendMessage("This rock contains no ore.")
        }
        if (resource == null) {
            return
        }
        if (resource!!.id == 2491) {
            isMiningEssence = true
        }
        if (resource!!.identifier == MiningNode.GEM_ROCK_0.identifier) {
            isMiningGems = true
        }
        if (checkRequirements()) {
            super.start()
            message(0)
        }
    }

    fun checkRequirements(): Boolean {
        if (player.skills.getLevel(Skills.MINING) < resource!!.getLevel()) {
            player.packetDispatch.sendMessage("You need a mining level of " + resource!!.getLevel() + " to mine this rock.")
            return false
        }
        if (SkillingTool.getPickaxe(player) == null) {
            player.packetDispatch.sendMessage("You do not have a pickaxe to use.")
            return false
        }
        if (player.inventory.freeSlots() < 1) {
            player.dialogueInterpreter.sendDialogue("Your inventory is too full to hold any more " + ItemDefinition.forId(resource!!.getReward()).name.toLowerCase() + ".")
            return false
        }
        return true
    }

    fun animate() {
        player.animate(SkillingTool.getPickaxe(player).animation)
    }

    fun reward(): Boolean {
        if (++ticks % (if (isMiningEssence) 3 else 4) != 0) {
            return false
        }
        if (node.id == 10041) {
            player.dialogueInterpreter.sendDialogues(2574, FacialExpression.FURIOUS, if (RandomFunction.random(2) == 1) "You'll blow my cover! I'm meant to be hidden!" else "Will you stop that?")
            return true
        }
        if (!checkReward()) {
            return false
        }

        //actual reward calculations
        var reward = resource!!.getReward()
        var rewardAmount = 0
        if (reward > 0) {
            reward = calculateReward(reward) // calculate rewards
            rewardAmount = calculateRewardAmount(reward) // calculate amount

            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))
            SkillingPets.checkPetDrop(player, SkillingPets.GOLEM) // roll for pet

            //add experience
            val experience = resource!!.getExperience() * rewardAmount
            player.skills.addExperience(Skills.MINING, experience, true)

            //Handle bracelet of clay
            if(reward == Items.CLAY_434){
                val bracelet = player.equipment.get(EquipmentContainer.SLOT_HANDS)
                if(bracelet != null && bracelet.id == Items.BRACELET_OF_CLAY_11074){
                    if(bracelet.charge > 28) bracelet.charge = 28
                    bracelet.charge--
                    reward = Items.SOFT_CLAY_1761
                    player.sendMessage("Your bracelet of clay softens the clay for you.")
                    if(bracelet.charge <= 0){
                        player.sendMessage("Your bracelet of clay crumbles to dust.")
                        player.equipment.remove(bracelet)
                    }
                }
            }

            //send the message for the resource reward
            if (isMiningGems) {
                val gemName = ItemDefinition.forId(reward).name.toLowerCase()
                player.sendMessage("You get " + (if (StringUtils.isPlusN(gemName)) "an" else "a") + " " + gemName + ".")
            } else {
                player.packetDispatch.sendMessage("You get some " + ItemDefinition.forId(reward).name.toLowerCase() + ".")
            }
            //give the reward
            player.inventory.add(Item(reward, rewardAmount))
            var rocksMined = player.getAttribute("$STATS_BASE:$STATS_ROCKS",0)
            player.setAttribute("/save:$STATS_BASE:$STATS_ROCKS",++rocksMined)

            //calculate bonus gem for mining
            if (!isMiningEssence) {
                var chance = 282
                var altered = false
                if (Item(player.equipment.getId(12)).name.toLowerCase().contains("ring of wealth") || inEquipment(player, Items.RING_OF_THE_STAR_SPRITE_14652)) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                val necklace = player.equipment[EquipmentContainer.SLOT_AMULET]
                if (necklace != null && necklace.id in 1705..1713) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                if (RandomFunction.roll(chance)) {
                    val gem = GEM_REWARDS.random()
                    player.packetDispatch.sendMessage("You find a " + gem.name + "!")
                    if (!player.inventory.add(gem, player)) {
                        player.packetDispatch.sendMessage("You do not have enough space in your inventory, so you drop the gem on the floor.")
                    }
                }
            }

            //transform to depleted version
            if (!isMiningEssence && resource!!.getRespawnRate() != 0) {
                SceneryBuilder.replace(node as Scenery, Scenery(resource!!.emptyId, node.getLocation(), node.type, node.rotation), resource!!.respawnDuration)
                node.setActive(false)
                return true
            }
        }
        return false
    }

    private fun calculateRewardAmount(reward: Int): Int {
        var amount = 1

        //checks for varrock armor from varrock diary and rolls chance at extra ore
        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK).level != -1) {
            when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211, Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 -> if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    player.sendMessage("The Varrock armour allows you to mine an additional ore.")
                }
                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 -> if (player.achievementDiaryManager.armour >= 1 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    player.sendMessage("The Varrock armour allows you to mine an additional ore.")
                }
                Items.ADAMANTITE_ORE_449 -> if (player.achievementDiaryManager.armour >= 2 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    player.sendMessage("The Varrock armour allows you to mine an additional ore.")
                }
            }
        }

        //check for bonus ore from shooting star buff
        if (player.hasActiveState("shooting-star")) {
            if (RandomFunction.getRandom(5) == 3) {
                player.packetDispatch.sendMessage("...you manage to mine a second ore thanks to the Star Sprite.")
                amount += 1
            }
        }
        return amount
    }

    private fun calculateReward(reward: Int): Int {
        // If the player is mining sandstone or granite, then get size of sandstone/granite and xp reward for that size
        var reward = reward
        if (resource == MiningNode.SANDSTONE || resource == MiningNode.GRANITE) {
            val value = RandomFunction.randomize(if (resource == MiningNode.GRANITE) 3 else 4)
            reward += value shl 1
            player.skills.addExperience(Skills.MINING, value * 10.toDouble(), true)
        } else if (isMiningEssence && player.skills.getLevel(Skills.MINING) >= 30) {
            reward = 7936
        } else if (isMiningGems) {
            reward = RandomFunction.rollWeightedChanceTable(MiningNode.gemRockGems).id
        }
        return reward
    }

    /**
     * Checks if the player gets rewarded.
     * @return `True` if so.
     */
    private fun checkReward(): Boolean {
        val skill = Skills.MINING
        val level = 1 + player.skills.getLevel(skill) + player.familiarManager.getBoost(skill)
        val hostRatio = Math.random() * (100.0 * resource!!.getRate())
        var toolRatio = SkillingTool.getPickaxe(player).ratio
        if(SkillcapePerks.isActive(SkillcapePerks.PRECISION_MINER,player)){
            toolRatio += 0.075
        }
        val clientRatio = Math.random() * ((level - resource!!.getLevel()) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

    companion object {
        private val GEM_REWARDS = arrayOf(ChanceItem(1623, 1, DropFrequency.COMMON), ChanceItem(1621, 1, DropFrequency.COMMON), ChanceItem(1619, 1, DropFrequency.UNCOMMON), ChanceItem(1617, 1, DropFrequency.RARE))
    }

    init {
        super.stop()
    }
}
package rs09.game.node.entity.skill.gather.mining

import api.*
import api.events.ResourceProducedEvent
import core.cache.def.impl.ItemDefinition
import core.game.content.dialogue.FacialExpression
import core.game.content.global.SkillingPets
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.gather.SkillingTool
import core.game.node.entity.skill.gather.mining.MiningNode
import core.game.node.item.ChanceItem
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.node.entity.player.info.stats.STATS_BASE
import rs09.game.node.entity.player.info.stats.STATS_ROCKS
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks
import rs09.tools.stringtools.*

/**
 * Mining skill pulse
 * @author Ceikry
 * @author bushtail -> maintenance July 2022
 */
class MiningSkillPulse(private val player: Player, private val node: Node) : Pulse(1, player, node) {
    private var resource: MiningNode? = null
    private var isMiningEssence = false
    private var isMiningGems = false
    private var ticks = 0
    private var resetAnimation = true

    // Perfect Gold Ore in Witchhaven Dungeon (Family Crest)
    private val perfectGoldOreLocations = listOf(
            Location(2735, 9695, 0),
            Location(2737, 9689, 0),
            Location(2740, 9684, 0),
            Location(2737, 9683, 0),
    )

    fun message(type: Int) {
        if (type == 0) {
            sendMessage(player, "You swing your pickaxe at the rock...")
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
            animate(player, Animation(-1, Animator.Priority.HIGH))
        }
        super.stop()
        message(1)
    }

    override fun start() {
        resource = MiningNode.forId(node.id)
        if (MiningNode.isEmpty(node.id)) {
            sendMessage(player, "This rock contains no ore.")
        }
        if (resource == null) {
            return
        }
        if (resource!!.id == 2099 &&
            !perfectGoldOreLocations.contains(node.location) ) {
            // Perfect Gold Ore IDs outside Witchhaven are replaced with a normal gold rock.
            resource = MiningNode.forId(2098)
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
        if (getDynLevel(player, Skills.MINING) < resource!!.level) {
            sendMessage(player, "You need a mining level of ${resource!!.level} to mine this rock.")
            return false
        }
        if (SkillingTool.getPickaxe(player) == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (freeSlots(player) == 0) {
            if(resource!!.identifier == 13.toByte()) {
                sendDialogue(player,"Your inventory is too full to hold any more gems.")
                return false
            }
            sendDialogue(player,"Your inventory is too full to hold any more ${ItemDefinition.forId(resource!!.reward).name.lowercase()}.")
            return false
        }
        return true
    }

    fun animate() {
        animate(player, SkillingTool.getPickaxe(player).animation)
    }

    fun reward(): Boolean {
        if (++ticks % (if (isMiningEssence) 3 else 4) != 0) {
            return false
        }
        if (!checkReward()) {
            return false
        }

        // Reward logic
        var reward = resource!!.reward
        var rewardAmount : Int
        if (reward > 0) {
            reward = calculateReward(reward) // calculate rewards
            rewardAmount = calculateRewardAmount(reward) // calculate amount

            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))
            SkillingPets.checkPetDrop(player, SkillingPets.GOLEM) // roll for pet

            // Reward mining experience
            val experience = resource!!.experience * rewardAmount
            rewardXP(player, Skills.MINING, experience)

            // If player is wearing Bracelet of Clay, soften
            if(reward == Items.CLAY_434){
                val bracelet = getItemFromEquipment(player, EquipmentSlot.HANDS)
                if(bracelet != null && bracelet.id == Items.BRACELET_OF_CLAY_11074){
                    var charges = player.getAttribute("jewellery-charges:bracelet-of-clay", 28);
                    charges--
                    reward = Items.SOFT_CLAY_1761
                    sendMessage(player, "Your bracelet of clay softens the clay for you.")
                    if(charges <= 0) {
                        if(removeItem(player, bracelet, Container.EQUIPMENT)) {
                            sendMessage(player, "Your bracelet of clay crumbles to dust.")
                            charges = 28
                        }
                    }
                    player.setAttribute("/save:jewellery-charges:bracelet-of-clay", charges)
                }
            }
            val rewardName = getItemName(reward).lowercase()

            // Send the message for the resource reward
            if (isMiningGems) {
                sendMessage(player, "You get ${prependArticle(rewardName)}.")
            } else {
                sendMessage(player, "You get some ${rewardName.lowercase()}.")
            }

            // Give the mining reward, increment 'rocks mined' attribute
            if(addItem(player, reward, rewardAmount)) {
                var rocksMined = getAttribute(player, "$STATS_BASE:$STATS_ROCKS", 0)
                setAttribute(player, "/save:$STATS_BASE:$STATS_ROCKS", ++rocksMined)
            }

            // Calculate bonus gem chance while mining
            if (!isMiningEssence) {
                var chance = 282
                var altered = false
                val ring = getItemFromEquipment(player, EquipmentSlot.RING)
                if (ring != null && ring.name.lowercase().contains("ring of wealth") || inEquipment(player, Items.RING_OF_THE_STAR_SPRITE_14652)) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                val necklace = getItemFromEquipment(player, EquipmentSlot.AMULET)
                if (necklace != null && necklace.id in 1705..1713) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                if (RandomFunction.roll(chance)) {
                    val gem = GEM_REWARDS.random()
                    sendMessage(player,"You find a ${gem.name}!")
                    if (freeSlots(player) == 0) {
                        sendMessage(player,"You do not have enough space in your inventory, so you drop the gem on the floor.")
                    }
                    addItemOrDrop(player, gem.id)
                }
            }

            // Transform ore to depleted version
            if (!isMiningEssence && resource!!.respawnRate != 0) {
                SceneryBuilder.replace(node as Scenery, Scenery(resource!!.emptyId, node.getLocation(), node.type, node.rotation), resource!!.respawnDuration)
                node.setActive(false)
                return true
            }
        }
        return false
    }

    private fun calculateRewardAmount(reward: Int): Int {
        var amount = 1

        // If player is wearing Varrock armour from diary, roll chance at extra ore
        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK).level != -1) {
            when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211, Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 -> if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    sendMessage(player,"The Varrock armour allows you to mine an additional ore.")
                }
                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 -> if (player.achievementDiaryManager.armour >= 1 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                }
                Items.ADAMANTITE_ORE_449 -> if (player.achievementDiaryManager.armour >= 2 && RandomFunction.random(100) <= 10) {
                    amount += 1
                    sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                }
            }
        }

        // If player has mining boost from Shooting Star, roll chance at extra ore
        if (player.hasActiveState("shooting-star")) {
            if (RandomFunction.getRandom(5) == 3) {
                sendMessage(player, "...you manage to mine a second ore thanks to the Star Sprite.")
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
            rewardXP(player, Skills.MINING, value * 10.toDouble())
        } else if (isMiningEssence && getDynLevel(player, Skills.MINING) >= 30) {
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
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        var toolRatio = SkillingTool.getPickaxe(player).ratio
        if(SkillcapePerks.isActive(SkillcapePerks.PRECISION_MINER,player)){
            toolRatio += 0.075
        }
        val clientRatio = Math.random() * ((level - resource!!.level) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

    companion object {
        private val GEM_REWARDS = arrayOf(ChanceItem(1623, 1, DropFrequency.COMMON), ChanceItem(1621, 1, DropFrequency.COMMON), ChanceItem(1619, 1, DropFrequency.UNCOMMON), ChanceItem(1617, 1, DropFrequency.RARE))
    }

    init {
        super.stop()
    }
}

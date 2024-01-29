package content.global.skill.gather.mining

import content.data.skill.SkillingPets
import content.data.skill.SkillingTool
import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.activity.shootingstar.StarBonus
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.event.ResourceProducedEvent
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_ROCKS
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import core.tools.prependArticle
import org.rs09.consts.Items

class MiningListener : InteractionListener {
    override fun defineListeners() {
        defineInteraction(
                IntType.SCENERY,
                MiningNode.values().map { it.id }.toIntArray(),
                "mine",
                persistent = true, allowedDistance = 1,
                handler = ::handleMining
        )
    }
    private val GEM_REWARDS = arrayOf(ChanceItem(1623, 1, DropFrequency.COMMON), ChanceItem(1621, 1, DropFrequency.COMMON), ChanceItem(1619, 1, DropFrequency.UNCOMMON), ChanceItem(1617, 1, DropFrequency.RARE))

    private fun handleMining(player: Player, node: Node, state: Int) : Boolean {
        val resource = MiningNode.forId(node.id)
        val tool = SkillingTool.getPickaxe(player)
        val isEssence = resource.id == 2491
        val isGems = resource.identifier == MiningNode.GEM_ROCK_0.identifier

        if (!finishedMoving(player))
            return true

        if (state == 0) {
             if (!checkRequirements(player, resource, node)) {
                 player.scripts.reset()
                 return true
             }
            anim(player, tool)
            sendMessage(player, "You swing your pickaxe at the rock...")
            return delayScript(player, getDelay(resource))
        }

        anim(player, tool)
        if (!checkReward(player, resource, tool))
            return delayScript(player, getDelay(resource))

        // Reward logic
        var reward = resource!!.reward
        var rewardAmount : Int
        if (reward > 0) {
            reward = calculateReward(player, resource, isEssence, isGems, reward) // calculate rewards
            rewardAmount = calculateRewardAmount(player, isEssence, reward) // calculate amount

            player.dispatch(ResourceProducedEvent(reward, rewardAmount, node))
            SkillingPets.checkPetDrop(player, SkillingPets.GOLEM) // roll for pet

            // Reward mining experience
            val experience = resource!!.experience * rewardAmount
            rewardXP(player, Skills.MINING, experience)

            // If player is wearing Bracelet of Clay, soften
            if(reward == Items.CLAY_434){
                val bracelet = getItemFromEquipment(player, EquipmentSlot.HANDS)
                if(bracelet != null && bracelet.id == Items.BRACELET_OF_CLAY_11074){
                    var charges = player.getAttribute("jewellery-charges:bracelet-of-clay", 28)
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

            //If the player is mining gold in the witchaven dungeon, reward family crest perfect gold ore
            val familyCrestGoldOreArea = ZoneBorders(2733, 9695, 2741, 9683)
            if (reward == Items.GOLD_ORE_444 && inBorders(player, familyCrestGoldOreArea)) {
                reward = Items.PERFECT_GOLD_ORE_446
            }
            val rewardName = getItemName(reward).lowercase()

            // Send the message for the resource reward
            if (isGems) {
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
            if (!isEssence) {
                var chance = 282
                var altered = false
                val ring = getItemFromEquipment(player, EquipmentSlot.RING)
                if (ring != null && ring.name.lowercase().contains("ring of wealth") || inEquipment(player, Items.RING_OF_THE_STAR_SPRITE_14652)) {
                    chance = (chance / 1.5).toInt()
                    altered = true
                }
                val necklace = getItemFromEquipment(player, EquipmentSlot.NECK)
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
            if (!isEssence && resource!!.respawnRate != 0) {
                SceneryBuilder.replace(node as Scenery, Scenery(resource!!.emptyId, node.getLocation(), node.type, node.rotation), resource!!.respawnDuration)
                node.setActive(false)
                return true
            }
        }
        return true
    }

    private fun calculateRewardAmount(player: Player, isMiningEssence: Boolean, reward: Int): Int {
        var amount = 1

        // If player is wearing Varrock armour from diary, roll chance at extra ore
        if (!isMiningEssence && player.achievementDiaryManager.getDiary(DiaryType.VARROCK).level != -1) {
            when (reward) {
                Items.CLAY_434, Items.COPPER_ORE_436, Items.TIN_ORE_438, Items.LIMESTONE_3211, Items.BLURITE_ORE_668, Items.IRON_ORE_440, Items.ELEMENTAL_ORE_2892, Items.SILVER_ORE_442, Items.COAL_453 -> if (player.achievementDiaryManager.armour >= 0 && RandomFunction.random(100) < 4) {
                    amount += 1
                    sendMessage(player,"The Varrock armour allows you to mine an additional ore.")
                }
                Items.GOLD_ORE_444, Items.GRANITE_500G_6979, Items.GRANITE_2KG_6981, Items.GRANITE_5KG_6983, Items.MITHRIL_ORE_447 -> if (player.achievementDiaryManager.armour >= 1 && RandomFunction.random(100) < 3) {
                    amount += 1
                    sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                }
                Items.ADAMANTITE_ORE_449 -> if (player.achievementDiaryManager.armour >= 2 && RandomFunction.random(100) < 2) {
                    amount += 1
                    sendMessage(player, "The Varrock armour allows you to mine an additional ore.")
                }
            }
        }

        // If player has mining boost from Shooting Star, roll chance at extra ore
        if (hasTimerActive<StarBonus>(player)) {
            if (RandomFunction.getRandom(5) == 3) {
                sendMessage(player, "...you manage to mine a second ore thanks to the Star Sprite.")
                amount += 1
            }
        }
        return amount
    }

    private fun calculateReward(player: Player, resource: MiningNode, isMiningEssence: Boolean, isMiningGems: Boolean, reward: Int): Int {
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

    private fun checkReward(player: Player, resource: MiningNode?, tool: SkillingTool): Boolean {
        val level = 1 + getDynLevel(player, Skills.MINING) + getFamiliarBoost(player, Skills.MINING)
        val hostRatio = Math.random() * (100.0 * resource!!.rate)
        var toolRatio = tool.ratio
        if(SkillcapePerks.isActive(SkillcapePerks.PRECISION_MINER,player)){
            toolRatio += 0.075
        }
        val clientRatio = Math.random() * ((level - resource.level) * (1.0 + toolRatio))
        return hostRatio < clientRatio
    }

    fun getDelay(resource: MiningNode) : Int {
        return if (resource.id == 2491) 3 else 4
    }

    fun anim(player: Player, tool: SkillingTool) {
        if (animationFinished(player))
            animate(player, tool.animation)
    }

    fun checkRequirements(player: Player, resource: MiningNode, node: Node): Boolean {
        if (getDynLevel(player, Skills.MINING) < resource.level) {
            sendMessage(player, "You need a mining level of ${resource.level} to mine this rock.")
            return false
        }
        if (SkillingTool.getPickaxe(player) == null) {
            sendMessage(player, "You do not have a pickaxe to use.")
            return false
        }
        if (freeSlots(player) == 0) {
            if(resource.identifier == 13.toByte()) {
                sendDialogue(player,"Your inventory is too full to hold any more gems.")
                return false
            }
            sendDialogue(player,"Your inventory is too full to hold any more ${ItemDefinition.forId(resource!!.reward).name.lowercase()}.")
            return false
        }
        return node.isActive
    }
}

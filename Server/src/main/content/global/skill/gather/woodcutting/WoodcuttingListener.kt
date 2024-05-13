package content.global.skill.gather.woodcutting

import content.data.skill.SkillingPets
import content.data.skill.SkillingTool
import content.data.tables.BirdNest
import content.global.skill.farming.FarmingPatch.Companion.forObject
import content.global.skill.firemaking.Log
import content.global.skill.skillcapeperks.SkillcapePerks
import content.global.skill.skillcapeperks.SkillcapePerks.Companion.isActive
import content.region.misc.miscellania.dialogue.KjallakOnChopDialogue
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.container.impl.EquipmentContainer
import core.game.event.ResourceProducedEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_LOGS
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds
import org.rs09.consts.Sounds.TREE_FALLING_2734
import java.util.*
import kotlin.streams.toList

class WoodcuttingListener : InteractionListener {
    private val woodcuttingSounds = intArrayOf(
            Sounds.WOODCUTTING_HIT_3038,
            Sounds.WOODCUTTING_HIT_3039,
            Sounds.WOODCUTTING_HIT_3040,
            Sounds.WOODCUTTING_HIT_3041,
            Sounds.WOODCUTTING_HIT_3042
    )

    override fun defineListeners() {
        defineInteraction(
                IntType.SCENERY,
                ids = WoodcuttingNode.values().map { it.id }.toIntArray(),
                "chop-down", "chop", "chop down", "cut down",
                persistent = true,
                allowedDistance = 1,
                handler = ::handleWoodcutting
        )
    }

    private fun handleWoodcutting(player: Player, node: Node, state: Int) : Boolean {
        val resource = WoodcuttingNode.forId(node.id)
        val tool = SkillingTool.getHatchet(player)

        if (!finishedMoving(player))
            return restartScript(player) 

        if (state == 0) {
            if (!checkWoodcuttingRequirements(player, resource, node)) {
                return clearScripts(player)
            }
            sendMessage(player, "You swing your axe at the tree...")
        }

        if (clockReady(player, Clocks.SKILLING)) {
            animateWoodcutting(player)
            if (!checkReward(player, resource, tool))
                return delayClock(player, Clocks.SKILLING, 3)

            val reward = resource.getReward()
            val rewardAmount: Int

            if (tool.id == Items.INFERNO_ADZE_13661 && reward != Items.BARK_3239 && RandomFunction.roll(4)) {
                sendMessage(player, "You chop some logs. The heat of the inferno adze incinerates them.")
                Projectile.create(
                        player, null,
                        1776,
                        35, 30,
                        20, 25
                ).transform(
                        player,
                        player.location.transform(2, 0, 0),
                        true,
                        25, 25
                ).send()

                //add woodcutting experience
                player.getSkills().addExperience(Skills.WOODCUTTING, resource.getExperience())

                //nullcheck the fire, and only if it exists award the firemaking XP
                val fire = Log.forId(reward)
                if (fire != null) {
                    player.getSkills().addExperience(Skills.FIREMAKING, fire.getXp())
                }

                delayClock(player, Clocks.SKILLING, 3)
                return rollDepletion(player, node.asScenery(), resource)
            }

            if (reward > 0) {
                rewardAmount = calculateRewardAmount(player, reward) // calculate amount
                SkillingPets.checkPetDrop(player, SkillingPets.BEAVER) // roll for pet

                //add experience
                val experience: Double = calculateExperience(player, resource, rewardAmount)
                player.getSkills().addExperience(Skills.WOODCUTTING, experience, true)

                //send the message for the resource reward
                if (resource == WoodcuttingNode.DRAMEN_TREE) {
                    player.packetDispatch.sendMessage("You cut a branch from the Dramen tree.")
                } else if (reward == Items.BARK_3239 && rewardAmount == 0) {
                    player.packetDispatch.sendMessage("You chop away some bark, but it falls to pieces before you can pick it up.")
                } else {
                    player.packetDispatch.sendMessage("You get some " + ItemDefinition.forId(reward).name.lowercase(Locale.getDefault()) + ".")
                }

                //give the reward
                player.inventory.add(Item(reward, rewardAmount))
                player.dispatch(ResourceProducedEvent(reward, rewardAmount, node, -1))
                var cutLogs = player.getAttribute("$STATS_BASE:$STATS_LOGS", 0)
                player.setAttribute("/save:$STATS_BASE:$STATS_LOGS", ++cutLogs)

                //calculate bonus bird nest for mining
                val chance = 282
                if (RandomFunction.random(chance) == chance / 2) {
                    if (isActive(SkillcapePerks.NEST_HUNTER, player)) {
                        if (!player.inventory.add(BirdNest.getRandomNest(false).nest)) {
                            BirdNest.drop(player)
                        }
                    } else {
                        BirdNest.drop(player)
                    }
                }
            }

            delayClock(player, Clocks.SKILLING, 3)
            rollDepletion(player, node.asScenery(), resource)
            if (!checkWoodcuttingRequirements(player, resource, node)) {
                return clearScripts(player)
            }
        }
        return keepRunning(player)
    }

    private fun rollDepletion(player: Player, node: Scenery, resource: WoodcuttingNode): Boolean {
        //transform to depleted version
        //OSRS and RS3 Wikis both agree: All trees present in 2009 are a 1/8 fell chance, aside from normal trees/dead trees which are 100%
        //OSRS: https://oldschool.runescape.wiki/w/Woodcutting scroll down to the mechanics section
        //RS3 : https://runescape.wiki/w/Woodcutting scroll down to the mechanics section, and expand the tree felling chances table
        if (resource.getRespawnRate() > 0) {
            if (RandomFunction.roll(8) || listOf(1, 2, 3, 4, 6).contains(resource.identifier.toInt())){
                if (resource.isFarming()) {
                    val fPatch = forObject(node.asScenery())
                    if (fPatch != null) {
                        val patch = fPatch.getPatchFor(player)
                        patch.setCurrentState(patch.getCurrentState() + 1)
                        node.isActive = false
                        playAudio(player, TREE_FALLING_2734)
                        queueScript(player, 2, QueueStrength.SOFT) {
                            node.isActive = true
                            return@queueScript stopExecuting(player)
                        }
                    }
                    return true
                }
                if (resource.getEmptyId() > -1) {
                    SceneryBuilder.replace(node, node.transform(resource.getEmptyId()), resource.getRespawnDuration())
                } else {
                    SceneryBuilder.replace(node, node.transform(0), resource.getRespawnDuration())
                }
                node.setActive(false)
                playAudio(player, TREE_FALLING_2734)
                return true
            }
        }
        return false
    }

    private fun checkReward(player: Player, resource: WoodcuttingNode, tool: SkillingTool): Boolean {
        val skill = Skills.WOODCUTTING
        val level: Int = player.getSkills().getLevel(skill) + player.getFamiliarManager().getBoost(skill)
        val hostRatio = RandomFunction.randomDouble(100.0)
        val lowMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModLow / 2 else resource.tierModLow
        val low: Double = resource.baseLow + tool.ordinal * lowMod
        val highMod: Double = if (tool == SkillingTool.BLACK_AXE) resource.tierModHigh / 2 else resource.tierModHigh
        val high: Double = resource.baseHigh + tool.ordinal * highMod
        val clientRatio = RandomFunction.getSkillSuccessChance(low, high, level)
        return hostRatio < clientRatio
    }

    fun animateWoodcutting(player: Player) {
        if (!player.animator.isAnimating) {
            player.animate(SkillingTool.getHatchet(player).animation)
            val playersAroundMe: List<Player> = RegionManager.getLocalPlayers(player, 2)
                    .stream()
                    .filter { p: Player -> p.username != player.username }
                    .toList()
            val soundIndex = RandomFunction.random(0, woodcuttingSounds.size)

            for (p in playersAroundMe) {
                playAudio(p, woodcuttingSounds[soundIndex])
            }
        }
    }

    fun checkWoodcuttingRequirements(player: Player, resource: WoodcuttingNode, node: Node): Boolean {
        var regionId = player.location.regionId
        if (regionId == 10300 || regionId == 10044) { //miscellania then etceteria, respectively.
            var npc = if (regionId == 10300) NPCs.CARPENTER_KJALLAK_3916 else NPCs.LUMBERJACK_LEIF_1395
            openDialogue(player, KjallakOnChopDialogue(), NPC(npc, player.location))
            return false
        }
        if (player.getSkills().getLevel(Skills.WOODCUTTING) < resource.getLevel()) {
            player.getPacketDispatch().sendMessage("You need a woodcutting level of " + resource.getLevel() + " to chop this tree.")
            return false
        }
        if (SkillingTool.getHatchet(player) == null) {
            player.packetDispatch.sendMessage("You do not have an axe to use.")
            return false
        }
        if (player.inventory.freeSlots() < 1 && node.isActive) {
            player.sendMessage("Your inventory is too full to hold any more " + ItemDefinition.forId(resource.getReward()).name.lowercase(Locale.getDefault()) + ".")
            return false
        }
        return node.isActive
    }


    private fun calculateRewardAmount(player: Player, reward: Int): Int {
        var amount = 1

        // 3239: Hollow tree (bark) 10% chance of obtaining
        if (reward == 3239 && RandomFunction.random(100) >= 10) {
            amount = 0
        }

        // Seers village medium reward - extra normal log while in seer's village
        if (reward == 1511 && player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(1) && player.getViewport().getRegion().getId() == 10806) {
            amount = 2
        }
        return amount
    }

    private fun calculateExperience(player: Player, resource: WoodcuttingNode, amount: Int): Double {
        var amount = amount
        var experience: Double = resource.getExperience()
        val reward = resource.reward
        if (player.getLocation().getRegionId() == 10300) {
            return 1.0
        }

        // Bark
        if (reward == Items.BARK_3239) {
            // If we receive the item, give the experience points
            if (amount >= 1) {
                experience = 82.5
            } else {
                return 0.0
            }
        }

        // Seers village medium reward - extra 10% xp from maples while wearing headband
        if (reward == 1517 && player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(1) && player.getEquipment().get(EquipmentContainer.SLOT_HAT) != null && player.getEquipment().get(EquipmentContainer.SLOT_HAT).getId() == 14631) {
            experience *= 1.10
        }
        return experience * amount
    }
}

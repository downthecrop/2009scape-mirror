package content.region.morytania.werewolfagility

import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class AgilityCourse : InteractionListener, MapArea {

    companion object {

        private const val LAST_VISITED_STONE_TILE_KEY = "lastVisitedStoneTile"

        val steppingStones = listOf(
                Location(3538, 9873, 0),
                Location(3538, 9875, 0), // 1
                Location(3538, 9877, 0), // 2
                Location(3540, 9877, 0), // 3
                Location(3540, 9879, 0), // 4
                Location(3540, 9881, 0), // 5
                Location(3540, 9882, 0),
        )

        // https://www.youtube.com/watch?v=JN_1c7r9PVo - Popular courses GOOD!
        // https://www.youtube.com/watch?v=fmzzLy5fXK4 - a fall location on the other side
        // https://www.youtube.com/watch?v=RnhjHPuae3Q - best with 2 fall locations nearest and furthest.
        // https://www.youtube.com/watch?v=_Re3MRhHbZk - middle location fall.
        // 180 160 140 exp for the 3 failure locations
        // 200 exp for success
        val startTile: Location = Location(3528, 9910, 0)
        val midwayTile1: Location = Location(3528, 9890, 0)
        val midwayTile2: Location = Location(3528, 9885, 0)
        val midwayTile3: Location = Location(3528, 9880, 0)
        val failureTileLeft1: Location = Location(3526, 9888, 0)
        val failureTileLeft2: Location = Location(3526, 9883, 0)
        val failureTileLeft3: Location = Location(3526, 9878, 0)
        val failureTileRight1: Location = Location(3530, 9888, 0)
        val failureTileRight2: Location = Location(3530, 9883, 0)
        val failureTileRight3: Location = Location(3530, 9878, 0)
        val endTile: Location = Location(3528, 9873, 0)

        // anim 767 - Landing on stomach

        fun nearestWerewolfSay(loc: Location, chatText: String) {
            var werewolfNpc = findLocalNPCs(loc, NPCs.AGILITY_TRAINER_1663).sortedWith { a, b ->
                (a.location.getDistance(loc) - b.location.getDistance(loc)).toInt()
            }.getOrNull(0)
            if (werewolfNpc != null) {
                sendChat(werewolfNpc, chatText)
            }
        }

        fun randomWerewolfSay(): String {
            return listOf(
                "Remember - a slow wolf is a hungry wolf!!",
                "Get on with it - you need your whiskers perking!!!!",
                "Claws first - think later.",
                "Imagine the smell of blood in your nostrils!!!",
                "I never really wanted to be an agility trainer...",
                "It'll be worth it when you hunt!!",
                "Let's see those powerful backlegs at work!!",
                "Let the bloodlust take you!!",
                "You're the slowest wolf I've ever had the misfortune to witness!!",
                "When you're done there's a human with your name on it!!",
            ).random()
        }

        fun randomZiplineWerewolfSay(): String {
            return listOf(
                "Give my regards to the ground...",
                "Don't let the spikes or the blood put you off...",
                "Now for a true test of teeth...",
            ).random()
        }

    }

    override fun defineListeners() {

        on(Scenery.TRAPDOOR_5131, IntType.SCENERY, "open") { player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.TRAPDOOR_5132, -1)
            return@on true
        }

        // Ladder Down
        on(Scenery.TRAPDOOR_5132, IntType.SCENERY, "climb-down") { player, node ->
            if (!anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) {
                sendNPCDialogue(player, NPCs.WEREWOLF_1665, "You can't go down there, human. If it wasn't my duty to guard this trapdoor, I would be relieving you of the burden of your life right now.", FacialExpression.WEREWOLF_NEUTRAL)
            } else {
                sendNPCDialogue(player, NPCs.WEREWOLF_1665, "Good luck down there, my friend. Remember, to the west is the main agility course, while to the east is a skullball course.", FacialExpression.WEREWOLF_NEUTRAL)
                teleport(player, Location(3549, 9865, 0))
            }
            return@on true
        }

        // Please note the previous teleport on close was because there is a stupid code in DoorManagingPlugin
        on(Scenery.TRAPDOOR_5132, IntType.SCENERY, "close") { player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.TRAPDOOR_5131, -1)
            return@on true
        }

        // Ladder Up
        on(Scenery.LADDER_5130, IntType.SCENERY, "climb-up") { player, node ->
            teleport(player, Location(3543, 3463, 0))
            return@on true
        }


        // Stepping stones (destination overrides below)
        on(Scenery.STEPPING_STONE_35996, IntType.SCENERY, "jump-to") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 60)) {
                sendDialogue(player, "You need an Agility level of at least 60 to do this.")
                return@on false
            }
            val arrIndex = steppingStones.indexOf(node.location)
            AgilityHandler.forceWalk(player, -1, steppingStones[arrIndex-1], steppingStones[arrIndex], Animation.create(741), 20, 10.0, null, 0).endAnimation = Animation.RESET

            if (arrIndex == 1 && !inInventory(player, Items.STICK_4179)) {
                val agilityBoss = findLocalNPC(player, NPCs.AGILITY_BOSS_1661)
                if (agilityBoss != null) {
                    sendChat(agilityBoss, "FETCH!!!!!")
                    face(agilityBoss, Location(3540, 9877, 0))
                    animate(agilityBoss, 6547)
                    produceGroundItem(player, Items.STICK_4179, 1, Location((3542..3544).random(), (9911..9913).random()))
                    spawnProjectile(Location(3540, 9873), Location(3540, 9883), 1158, 0, 0, 1, 60, 0)
                }
            }
            return@on true
        }

        // Hurdles
        on(intArrayOf(Scenery.HURDLE_5133, Scenery.HURDLE_5134, Scenery.HURDLE_5135), IntType.SCENERY, "jump") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 60)) {
                sendDialogue(player, "You need an Agility level of at least 60 to do this.")
                return@on false
            }
            if (player.location.y < node.location.y) {
                AgilityHandler.forceWalk(player, -1, player.location, player.location.transform(0, 2, 0), Animation.create(1603), 10, 20.0, null, 0).endAnimation = Animation.RESET
            } else {
                sendMessage(player, "You've already jumped over the hurdle.")
            }
            return@on true
        }

        // Pipes
        on(intArrayOf(Scenery.PIPE_5152), IntType.SCENERY, "squeeze-through") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 60)) {
                sendDialogue(player, "You need an Agility level of at least 60 to do this.")
                return@on false
            }
            if (player.location.y < node.location.y) {
                nearestWerewolfSay(Location(3540, 9902), randomWerewolfSay())
                AgilityHandler.forceWalk(player, -1, player.location, player.location.transform(0, 5, 0), Animation.create(10580), 10, 15.0, null, 0).endAnimation = Animation(10579)
            } else {
                sendMessage(player, "You've already squeezed through the pipe.")
            }
            return@on true
        }

        // Skull slopes
        on(intArrayOf(Scenery.SKULL_SLOPE_5136), IntType.SCENERY, "climb-up") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 60)) {
                sendDialogue(player, "You need an Agility level of at least 60 to do this.")
                return@on false
            }
            if (player.location.x > node.location.x) {
                nearestWerewolfSay(Location(3536, 9912), randomWerewolfSay())
                AgilityHandler.forceWalk(player, -1, player.location, player.location.transform(-2, 0, 0), Animation.create(2049), 10, 25.0, null, 0).endAnimation = Animation.RESET
            } else {
                sendMessage(player, "You've already climbed the skull wall.")
            }
            return@on true
        }

        // Zip line
        on(intArrayOf(Scenery.ZIP_LINE_5139, Scenery.ZIP_LINE_5140, Scenery.ZIP_LINE_5141), IntType.SCENERY, "teeth-grip") { player, node ->
            if (!hasLevelDyn(player, Skills.AGILITY, 60)) {
                sendDialogue(player, "You need an Agility level of at least 60 to do this.")
                return@on false
            }

            var successChancePercent = 100.0
            // "With level 80 in Agility and Strength and a weight of 2 kg or lower, this obstacle will never be failed."
            // Otherwise, this is up to my decision on whether to torture you
            if (!(getDynLevel(player, Skills.AGILITY) >= 80 && getDynLevel(player, Skills.STRENGTH) >= 80 && player.settings.weight <= 2.0)) {
                // All the successes are between 0.0 to 1.0 range
                val agilitySuccess = RandomFunction.getSkillSuccessChance(0.0, 320.0, 60) / 100// 0 at lvl1, 256 at lvl80 extrapolate to 320 at lvl 99
                val strengthSuccess = RandomFunction.getSkillSuccessChance(0.0, 320.0, 60) / 100 // 0 at lvl1, 256 at lvl80 extrapolate to 320 at lvl 99
                val weightSuccess =  Math.max(80.0 - player.settings.weight, 0.0) / 100 // 80% chance, minus 1% per weight gain.
                successChancePercent *= agilitySuccess
                successChancePercent *= strengthSuccess
                successChancePercent *= weightSuccess
            }

            // Align player up on the zipline
            forceMove(player, player.location, Location(3528, 9910, 0), 0,10)
            face(player, Location(3528, 9915, 0))
            lock(player, 6)
            // roll a number, between 0-totalSuccess means you succeed, otherwise between totalSuccess-100 you fail.
            if(RandomFunction.random(0.0, 100.0) < successChancePercent) { // Success
                nearestWerewolfSay(Location(3527, 9909), randomZiplineWerewolfSay())
                queueScript(player, 2, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            face(player, Location(3528, 9915, 0))
                            animate(player, 1601)
                            sendMessage(player, "You bravely cling on to the death slide by your teeth ...")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            sendChat(player, "WAAAAAARRRGGGHHH!!!!!!")
                            ForceMovement.run(player, startTile, endTile, Animation(1602), Animation(1602), Direction.SOUTH, 60).endAnimation = Animation.RESET
                            return@queueScript delayScript(player, 8)
                        }
                        2 -> {
                            rewardXP(player, Skills.AGILITY, 200.0)
                            sendMessage(player, ".. and land safely on your feet.")
                            teleport(player, endTile)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                // Based on total success, find where to land. If you had a lower chance, you get the early drop and lower XP.
                var finalTile = endTile
                var animTicks = 6
                var fallTile = endTile
                var rewardXP = 200.0
                if (successChancePercent <= 20.0) {
                    finalTile = midwayTile1
                    fallTile = arrayOf(failureTileLeft1, failureTileRight1).random()
                    animTicks = 4
                    rewardXP = 140.0
                } else if (successChancePercent <= 40.0 ) {
                    finalTile = midwayTile2
                    fallTile = arrayOf(failureTileLeft2, failureTileRight2).random()
                    animTicks = 5
                    rewardXP = 160.0
                } else {
                    finalTile = midwayTile3
                    fallTile = arrayOf(failureTileLeft3, failureTileRight3).random()
                    animTicks = 6
                    rewardXP = 180.0
                }

                nearestWerewolfSay(Location(3527, 9909), randomZiplineWerewolfSay())
                queueScript(player, 2, QueueStrength.SOFT) { stage ->
                    when (stage) {
                        0 -> {
                            face(player, Location(3528, 9915, 0))
                            animate(player, 1601)
                            sendMessage(player, "You bravely cling on to the death slide by your teeth ...")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            sendChat(player, "WAAAAAARRRGGGHHH!!!!!!")
                            ForceMovement.run(player, startTile, finalTile, Animation(1602), Animation(1602), Direction.SOUTH, 60).endAnimation = Animation(767)
                            return@queueScript delayScript(player, animTicks)
                        }
                        2 -> {
                            rewardXP(player, Skills.AGILITY, rewardXP)
                            sendMessage(player, ".. only to fall from a great height!")
                            teleport(player, fallTile)
                            // Can't get this to chain animations.
                            //ForceMovement.run(player, finalTile, fallTile, Animation(767), Animation(767), Direction.SOUTH, 60).endAnimation = Animation(767)
                            return@queueScript delayScript(player, 2)
                        }
                        3 -> {
                            teleport(player, fallTile)
                            player.impactHandler.manualHit(player, (1..30).random(), ImpactHandler.HitsplatType.NORMAL)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }

            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.STEPPING_STONE_35996),"jump-to"){ player, node ->
            val arrIndex = steppingStones.indexOf(node.location)
            return@setDest steppingStones[arrIndex - 1]
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        // Area of the zipline.
        return arrayOf(ZoneBorders(3527, 9876, 3529, 9907))
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        // In case you log out during the zipline of death slide, you won't be left on it.
        // You lose that XP though...
        if (entity is Player) {
            if (logout) {
                teleport(entity, endTile)
            }
        }
    }
}
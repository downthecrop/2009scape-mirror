package content.global.ame.events.evilbob

import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

object EvilBobUtils {
    const val prevLocation = "/save:original-loc"
    const val eventComplete = "/save:evilbob:eventcomplete"
    const val assignedFishingZone = "/save:evilbob:fishingzone"
    const val attentive = "/save:evilbob:attentive"
    const val servantHelpDialogueSeen = "/save:evilbob:servantdialogeseen"
    const val attentiveNewSpot = "/save:evilbob:attentivenewspot"
    const val startingDialogueSeen = "/save:evilbob:startingdialogueseen"

    const val evilBob = NPCs.EVIL_BOB_2479
    const val servant = NPCs.SERVANT_2481
    const val exitPortal = Scenery.PORTAL_8987

    const val uncookingPot = Scenery.UNCOOKING_POT_8985
    const val fishingSpot = 8986

    val fishlikeThings = intArrayOf(Items.FISHLIKE_THING_6202, Items.FISHLIKE_THING_6206)
    val rawFishlikeThings = intArrayOf(Items.RAW_FISHLIKE_THING_6200, Items.RAW_FISHLIKE_THING_6204)

    val cookAnim = Animation(897)
    val fishAnim = Animation(620)
    val teleAnim = Animation(714)
    val telegfx = Graphics(308, 100, 50)

    val northFishingZone = ZoneBorders(3421, 4789, 3427, 4792)
    val eastFishingZone = ZoneBorders(3437, 4774, 3440, 4780)
    val southFishingZone = ZoneBorders(3419, 4763, 3426, 4765)
    val westFishingZone = ZoneBorders(3405, 4773, 3408, 4779)

    fun giveEventFishingSpot(player: Player) {
        when (RandomFunction.getRandom(3)) {
            0 -> setAttribute(player, assignedFishingZone, northFishingZone.toString())
            1 -> setAttribute(player, assignedFishingZone, southFishingZone.toString())
            2 -> setAttribute(player, assignedFishingZone, eastFishingZone.toString())
            3 -> setAttribute(player, assignedFishingZone, westFishingZone.toString())
            else -> setAttribute(player, assignedFishingZone, northFishingZone.toString())
        }
    }

    fun teleport(player: Player) {
        if (getAttribute(player, prevLocation, null) == null) {
            setAttribute(player, prevLocation, player.location)
        }
        player.properties.teleportLocation = Location.create(3419, 4776, 0)
    }

    fun cleanup(player: Player) {
        player.locks.unlockTeleport()
        player.properties.teleportLocation = getAttribute(player, prevLocation, ServerConstants.HOME_LOCATION)
        removeAttributes(player, assignedFishingZone, eventComplete, prevLocation, attentive, servantHelpDialogueSeen, attentiveNewSpot, startingDialogueSeen)
        removeAll(player, Items.FISHLIKE_THING_6202)
        removeAll(player, Items.FISHLIKE_THING_6202, Container.BANK)
        removeAll(player, Items.FISHLIKE_THING_6206)
        removeAll(player, Items.FISHLIKE_THING_6206, Container.BANK)
        removeAll(player, Items.RAW_FISHLIKE_THING_6200)
        removeAll(player, Items.RAW_FISHLIKE_THING_6200, Container.BANK)
        removeAll(player, Items.RAW_FISHLIKE_THING_6204)
        removeAll(player, Items.RAW_FISHLIKE_THING_6204, Container.BANK)
    }

    fun reward(player: Player) {
        val experience = 650.0
        if (getStatLevel(player, Skills.MAGIC) > 50 ) {
            when (RandomFunction.getRandom(1)) {
                0 -> {
                    player.skills.addExperience(Skills.FISHING, experience)
                    openDialogue(player, EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.FISHING), NPCs.EVIL_BOB_2479)
                }
                1 -> {
                    player.skills.addExperience(Skills.MAGIC, experience)
                    openDialogue(player, EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.MAGIC), NPCs.EVIL_BOB_2479)
                }
            }
        } else {
            player.skills.addExperience(Skills.FISHING, experience)
            openDialogue(player, EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.FISHING), NPCs.EVIL_BOB_2479)
        }
    }
}
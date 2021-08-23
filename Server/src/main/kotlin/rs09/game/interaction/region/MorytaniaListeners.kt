package rs09.game.interaction.region

import api.ContentAPI
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import rs09.game.interaction.InteractionListener
import kotlin.random.Random

/**
 * File to be used for anything Morytania related.
 * Handles the summoning/altar cave enter and exit in Morytania.
 * @author Sir Kermit
 */

class MorytaniaListeners : InteractionListener() {

    val GROTTO_ENTRANCE = 3516
    val GROTTO_EXIT = intArrayOf(3525, 3526)
    val GROTTO_BRIDGE = 3522
    val outside = Location.create(3439, 3337, 0)
    val inside = Location.create(3442, 9734, 1)

    private val swimAnim = Animation(6988)
    private val jumpAnim = Animation(1603)
    private val failWater = Location(3439,3330)
    private val failMessage = "You nearly drown in the disgusting swamp."
    private val splashGFX = Graphics(68)

    private var failLanding = Location(3438,3328)
    private var fallAnim = Animation(770)

    override fun defineListeners() {
/*        on(GROTTO_ENTRANCE,SCENERY,"enter"){ player, node ->
            player.teleport(inside)
            return@on true
        }*/

        on(GROTTO_EXIT,SCENERY,"exit"){ player, node ->
            player.teleport(outside)
            return@on true
        }

        on(GROTTO_BRIDGE,SCENERY,"jump"){ player, node ->
            var end = node.location
            if (AgilityHandler.hasFailed(player, 1, 1.0)) {
                if (player.location.y == 3332) {
                    fallAnim = Animation(771)
                    failLanding = Location(3438,3331)
                    end = end.transform(-1,-1,0)
                }
                AgilityHandler.forceWalk(player, -1, node.location, end, fallAnim, 15, 0.0, null,0).endAnimation = swimAnim
                AgilityHandler.forceWalk(player, -1, failWater, failLanding, swimAnim, 15, 2.0, failMessage,3)
                ContentAPI.submitIndividualPulse(player, object : Pulse(2){
                    override fun pulse(): Boolean {
                        ContentAPI.visualize(player,fallAnim,splashGFX)
                        ContentAPI.teleport(player,failWater,TeleportManager.TeleportType.INSTANT)
                        ContentAPI.impact(player,Random.nextInt(1,7), ImpactHandler.HitsplatType.NORMAL)
                        return true
                    }
                })
            }
            else{
                end = if (player.location.y == 3332) end.transform(0,-3,0) else end.transform(0,3,0)
                AgilityHandler.forceWalk(player, -1, node.location, end, jumpAnim, 15, 15.0, null,0)
            }
            return@on true
        }
    }
}
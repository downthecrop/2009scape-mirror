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
            val start = node.location
            var failLand = Location(3438,3331)
            var failAnim = Animation(770)
            var fromGrotto = false

            // Switch to south facing animations if jumping from Grotto
            if (start.y == 3331) {
                fromGrotto = true
                failAnim = Animation(771)
                failLand = Location(3438,3328)
            }

            if (AgilityHandler.hasFailed(player, 1, 0.1)) {
                val end = if (fromGrotto) failWater else start
                AgilityHandler.forceWalk(player, -1, start, end, failAnim, 15, 0.0, null,0).endAnimation = swimAnim
                AgilityHandler.forceWalk(player, -1, failWater, failLand, swimAnim, 15, 2.0, failMessage,3)
                var counter = 0
                ContentAPI.submitIndividualPulse(player, object : Pulse(2){
                    override fun pulse(): Boolean {
                        if (counter == 0){
                            ContentAPI.visualize(player,failAnim,splashGFX)
                            ContentAPI.teleport(player,failWater,TeleportManager.TeleportType.INSTANT)
                        }
                        // Deal 1-6 damage but wait until the player is back on land
                        else if (counter >= 2){
                            ContentAPI.impact(player,Random.nextInt(1,7), ImpactHandler.HitsplatType.NORMAL)
                            return true
                        }
                        counter++
                        return false
                    }
                })
            }
            else{
                val end = if (fromGrotto) start.transform(0,-3,0) else start.transform(0,3,0)
                AgilityHandler.forceWalk(player, -1, start, end, jumpAnim, 15, 15.0, null,0)
            }
            return@on true
        }
    }
}
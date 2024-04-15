package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Scenery
import core.game.interaction.IntType
import core.plugin.Initializable
import kotlin.collections.HashMap


@Initializable
class CatherbyGrappleShortcut : AbstractOneWayGrapple(){

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 32, Skills.RANGE to 35, Skills.STRENGTH to 35)

    override val grappleStartLocation: Location  = Location.create(2866, 3429, 0)

    override val grappleEndLocation: Location  = Location.create(2869,3430,0)

    // todo this is the wrong animation
    override val animation: Animation = Animation(4455)

    override val animationDuration: Int  = 9

    override val grappleScenery: List<core.game.node.scenery.Scenery?> = listOf(
            getScenery(Location.create(2869,3429, 0)) // rocks
        )

    override fun defineListeners() {
        flagInstant()

        on(Scenery.ROCKS_17042, IntType.SCENERY, "grapple"){ player, _ ->
            if (!canGrapple(player, grappleStartLocation, 1)) {
                return@on true
            }
            grapple(player, message)
            return@on true
        }
    }

}

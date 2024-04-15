package content.global.skill.agility.shortcuts.grapple

import core.api.inBorders
import core.api.teleport
import core.api.unlock
import core.api.visualize
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs09.consts.Scenery

@Initializable
class YanilleGrapple(private var wallGrappleInterface: WallGrappleInterface = WallGrappleInterfaceImpl()): AbstractTwoWayGrapple() {

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 11, Skills.RANGE to 19, Skills.STRENGTH to 37)

    override val grappleStartLocation: Location = Location.create(2556, 3072, 0)

    override val grappleEndLocation: Location = Location.create(2556, 3073, 1)

    override var direction: Direction? = null
    override var startLoc: Location? = null
    override var endLoc: Location? = null
    override fun setStartEndSide(player: Player, margin: Int) {
        // Start location is where you end after jumping in the opposite way
        if (player.location.y > 3073 ){
            // We are north of the middle of the wall
            startLoc = Location.create(2556, 3075, 0) // This is where you grapple from/land after jumping
            endLoc = Location.create(2556, 3074, 1) //
        }
        else {
            // We are south of the middle of the wall
            startLoc = Location.create(2556, 3072, 0)
            endLoc = Location.create(2556, 3073, 1)
        }
    }

    override fun getGrappleScenery(direction: Direction): List<core.game.node.scenery.Scenery?> {
        return emptyList()
    }

    override var grappleScenery: List<core.game.node.scenery.Scenery?> = listOf()

    override val animation: Animation = Animation(4455)
    override val animationDuration: Int = 9
    override fun animation(animationStage: Int, player: Player): Boolean {
        when (animationStage) {
            1 -> {
                player.faceLocation(endLoc)
                visualize(player, animation, Graphics(760, 100))
            }

            8 -> {
                wallGrappleInterface.fadeToBlack(player)
            }

            13 -> teleport(player, endLoc!!)
            14 -> {
                wallGrappleInterface.showGame(player)
                unlock(player)
                updateDiary(player)
                return true
            }
        }
        return false
    }


    override fun defineListeners() {
        // Do not use flagListeners here
        // The player needs to be able to touch the target
        on(Scenery.WALL_17047, IntType.SCENERY, "grapple") { player, _ ->
            setStartEndSide(player, 0)
            if(!canGrapple(player, startLoc!!, 4)){
                return@on true
            }
            grapple(player, message)
            return@on true
        }

        on(Scenery.WALL_17048, IntType.SCENERY, "jump") { player, _ ->
            setStartEndSide(player, 0)
            wallGrappleInterface.jump(player, startLoc!!)
            return@on true
        }
    }

    override fun isPlayerInRangeToGrapple(player: Player, startLoc: Location, range: Int): Boolean {
        // Do not let the player grapple from the other side of the wall
        return inBorders(
            player, startLoc.x - range, startLoc.y,
            startLoc.x + range, startLoc.y - 2)
    }
}

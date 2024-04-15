package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs09.consts.Scenery

abstract class AbstractFaladorGrapple(private var wallGrappleInterface: WallGrappleInterface = WallGrappleInterfaceImpl()): AbstractOneWayGrapple() {

    override val animation: Animation = Animation(4455)

    override val animationDuration: Int = 14

    // There are no scenery items to hook so don't let children override this
    final override val grappleScenery: List<core.game.node.scenery.Scenery?> = listOf()

    protected fun jump(player: Player, destination: Location): Boolean {
        return wallGrappleInterface.jump(player, destination)
    }

    override fun animation(animationStage: Int, player: Player): Boolean {
        when (animationStage) {
            1 -> {
                player.faceLocation(grappleEndLocation)
                visualize(player, animation, Graphics(760, 100))
            }

            8 -> {
                wallGrappleInterface.fadeToBlack(player)
            }

            13 -> teleport(player, grappleEndLocation)
            14 -> {
                wallGrappleInterface.showGame(player)
                unlock(player)
                updateDiary(player)
                return true
            }
        }
        return false
    }

}
@Initializable
class FaladorGrappleNorth : AbstractFaladorGrapple() {

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 11, Skills.RANGE to 19, Skills.STRENGTH to 37)

    override val grappleStartLocation: Location = Location.create(3033, 3390, 0)


    override val grappleEndLocation: Location = Location.create(3033, 3389, 1)

    override fun defineListeners() {
        flagInstant()
        on(Scenery.WALL_17049, IntType.SCENERY, "grapple") { player, _ ->
            if(!canGrapple(player, grappleStartLocation, 4)) {
                return@on true
            }
            grapple(player, message)
            return@on true
        }

        on(Scenery.WALL_17051, IntType.SCENERY, "jump"){ player, _ ->
            jump(player, grappleStartLocation)
            return@on true
        }
    }

    override fun isPlayerInRangeToGrapple(player: Player, startLoc: Location, range: Int): Boolean {
        // Do not let the player grapple from the other side of the wall
        return inBorders(player, startLoc.x - range, startLoc.y,
            startLoc.x + range, startLoc.y + 2)
    }

    override fun updateDiary(player: Player): Boolean {
        player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 1, 2)
        return true
    }
}

@Initializable
class FaladorGrappleSouth : AbstractFaladorGrapple() {

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 11, Skills.RANGE to 19, Skills.STRENGTH to 37)

    override val grappleStartLocation: Location = Location.create(3032, 3388, 0)

    override val grappleEndLocation: Location = Location.create(3032, 3389, 1)

    override fun defineListeners() {
        flagInstant()
        on(Scenery.WALL_17050, IntType.SCENERY, "grapple") { player, _ ->
            if(!canGrapple(player, grappleStartLocation, 4)) {
                return@on true
            }
            grapple(player, message)
            return@on true
        }

        on(Scenery.WALL_17052, IntType.SCENERY, "jump"){ player, _ ->
            jump(player, grappleStartLocation)
            return@on true
        }
    }

    override fun isPlayerInRangeToGrapple(player: Player, startLoc: Location, range: Int): Boolean {
        // Do not let the player grapple from the other side of the wall
        return inBorders(player, startLoc.x - range, startLoc.y,
            startLoc.x + range, startLoc.y - 2)
    }

    override fun updateDiary(player: Player): Boolean {
        player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 1, 2)
        return true
    }
}

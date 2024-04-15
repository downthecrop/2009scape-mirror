package content.global.skill.agility.shortcuts.grapple

import core.api.getScenery
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import org.rs09.consts.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable

@Initializable
class WaterOrbGrapple : AbstractOneWayGrapple(){

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 36, Skills.RANGE to 39, Skills.STRENGTH to 22)

    override val grappleStartLocation: Location = Location.create(2841, 3427, 0)

    override val grappleEndLocation: Location = Location.create(2841, 3433, 0)

    override val animation: Animation = Animation(4230)

    override val animationDuration: Int = 9

    override val grappleScenery: List<core.game.node.scenery.Scenery?> = listOf(
            getScenery(Location.create(2841, 3426, 0)), // rock
            getScenery(Location.create(2841, 3434, 0)) // tree
        )

    override fun defineListeners() {
        flagInstant()

        on(Scenery.CROSSBOW_TREE_17062, IntType.SCENERY, "grapple"){ player, _ ->
            if (!canGrapple(player, grappleStartLocation, 1)) {
                return@on true
            }
            grapple(player, message)
            return@on true
        }
    }

    override fun updateDiary(player: Player): Boolean {
        player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 10)
        return true
    }

}
package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.Scenery

@Initializable
class KaramjaGrapple : AbstractTwoWayGrapple(){

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 53, Skills.RANGE to 42, Skills.STRENGTH to 21)

    // South
    override val grappleStartLocation: Location = Location.create(2874, 3127, 0)


    // North
    override val grappleEndLocation: Location = Location.create(2874,3142,0)

    override var direction: Direction? = null
    override var startLoc: Location? = null
    override var endLoc: Location? = null

    override var grappleScenery: List<core.game.node.scenery.Scenery?> = listOf()

    override val animation: Animation = Animation(4230)
    override val animationDuration: Int = 9

    override fun animation(animationStage: Int, player: Player): Boolean {
        when (animationStage) {
            1 -> {
                face(player, endLoc!!)
                animate(player, animation)
            }

            5 -> {
                for (tgt in grappleScenery) {
                    replaceScenery(tgt!!, tgt.id + 1, 10)
                }
            }

            5 + animationDuration -> {
                teleport(player, endLoc!!)
            }
            5 + animationDuration + 1 -> {
                unlock(player)
                updateDiary(player)
                return true
            }
        }
        return false
    }

    override fun defineListeners() {
        flagInstant()

        on(Scenery.STRONG_TREE_17074, IntType.SCENERY, "grapple"){ player, _ ->
            setStartEndSide(player)
            if(!canGrapple(player, startLoc!!, 1)){
                return@on true
            }
            grapple(player, message)
            return@on true
        }

    }

    override fun setStartEndSide(player: Player, margin: Int) {
        if (player.location.y > grappleEndLocation.y - margin){ // we're on the north side
            direction = Direction.SOUTH // got to jump south
            startLoc = grappleEndLocation
            endLoc = grappleStartLocation
        }
        else {
            direction = Direction.NORTH // got to jump north
            startLoc = grappleStartLocation
            endLoc = grappleEndLocation
        }

        grappleScenery = getGrappleScenery(direction!!)
    }

    override fun getGrappleScenery(direction: Direction): List<core.game.node.scenery.Scenery?> {
        val startTree : core.game.node.scenery.Scenery?
        val endTree : core.game.node.scenery.Scenery?
        val islandTree = getScenery(Location(2873, 3134, 0))
        if (direction == Direction.NORTH){
            startTree = getScenery(Location(2874, 3144, 0))
            endTree = getScenery(Location(2873, 3125, 0))
        }
        else{
            startTree = getScenery(Location(2874, 3144, 0))
            endTree = getScenery(Location(2873, 3125, 0))
        }
        return listOf(startTree,endTree, islandTree)
    }

    override fun updateDiary(player: Player): Boolean {
        player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 2, 6)
        return true
    }
}
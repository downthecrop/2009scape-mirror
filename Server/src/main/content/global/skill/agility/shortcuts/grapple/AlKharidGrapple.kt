package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Scenery

class AlKharidGrapple : AbstractTwoWayGrapple(){

    override val REQUIREMENTS: HashMap<Int, Int> = hashMapOf(Skills.AGILITY to 8, Skills.RANGE to 37, Skills.STRENGTH to 19)

    // Lumbridge
    override val grappleStartLocation: Location = Location(3246, 3179, 0)

    // Al Kharid
    override val grappleEndLocation: Location = Location(3259, 3179, 0)

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
                    if ((tgt!!.id == 17068)) {
                        // This is the raft
                        continue
                    }
                    replaceScenery(tgt, tgt.id + 1, 10)
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
        flagInstant() // execute listeners instantly without determining path

        on(Scenery.BROKEN_RAFT_17068, IntType.SCENERY, "grapple") { player, target ->
            // Check if we are on the east or the west of the broken raft
            // East = Lum
            setStartEndSide(player)
            if (!canGrapple(player, startLoc!!, 2)){
                    return@on true
            }
            grapple(player,message)
            return@on true
        }
    }


    override fun setStartEndSide(player: Player, margin: Int) {
        if (player.location.x < grappleStartLocation.x + margin){
            // We're on the west side
            direction = Direction.EAST // got to jump east
            startLoc = grappleStartLocation
            endLoc = grappleEndLocation
        }
        else {
            // we're on the east side
            direction = Direction.WEST // got to jump west
            startLoc = grappleEndLocation
            endLoc = grappleStartLocation
        }

        grappleScenery = getGrappleScenery(direction!!)
    }

    override fun getGrappleScenery(direction: Direction): List<core.game.node.scenery.Scenery?> {
        val lumbridgeTree = getScenery(Location(3244, 3179, 0))
        val alKharidTree = getScenery(Location(3260, 3178, 0))
        val startTree : core.game.node.scenery.Scenery?
        val endTree : core.game.node.scenery.Scenery?
        val raft = getScenery(Location(3252, 3179, 0))
        if (direction == Direction.EAST){
            startTree = lumbridgeTree
            endTree = alKharidTree
        }
        else{
            startTree = alKharidTree
            endTree = lumbridgeTree
        }
        return listOf(startTree,endTree, raft)
    }
}
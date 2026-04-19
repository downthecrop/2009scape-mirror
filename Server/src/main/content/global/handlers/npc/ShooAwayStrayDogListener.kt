package content.global.handlers.npc

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.NPCs
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.update.flag.context.Animation
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Animations
import org.rs09.consts.Sounds

private val strayDogsIds = intArrayOf(
    NPCs.STRAY_DOG_4766,
    NPCs.STRAY_DOG_4767,
    NPCs.STRAY_DOG_5917,
    NPCs.STRAY_DOG_5918
)

/**
 * Represents the option Listener used to 'shoo-away' a stray dog.
 * Convert from legacy plugin src/main/content/region/misthalin/varrock/handlers/ShooAwayStrayDogPlugin.java
 * @author Vexia + beckrickert
 *
 */
class ShooAwayStrayDogListener : NPCBehavior(*strayDogsIds), InteractionListener {
    override fun defineListeners() {

        on(strayDogsIds,IntType.NPC, "shoo-away") { player, node ->

            val theDog = node as NPC

            //Pause dog location
            lock(theDog, 2)
            stopWalk(theDog)
            theDog.faceTemporary(player, 1)

            sendChat(player,"Thbbbbt!")
            animate(player, Animations.HUMAN_BLOW_RASPBERRY_2110)

            //Dog whine sound
            playAudio(player, Sounds.WOLF_DEATH_911)
            sendChat(theDog,"Whine!")

            //Dog whine kneeling down animation
            animate(theDog, Animation(4774))

            //run away from player
            dogWhineRunAway(player, theDog)

            return@on true
        }
    }
}

fun dogWhineRunAway(player: Player, theDog: NPC) {
    val playerCurLoc = player.getLocation()
    val dogCurLoc = theDog.getLocation()

    //Get dog direction is facing from player's location
    val dogDirection = Direction.getDirection(dogCurLoc, playerCurLoc)
    val dogDirectionOpp = dogDirection.opposite

    val xWalkLoc = dogCurLoc.x + (dogDirectionOpp.stepX * 12)
    val yWalkLoc = dogCurLoc.y + (dogDirectionOpp.stepY * 12)
    val dogWalkToNewLoc = Location(xWalkLoc, yWalkLoc, dogCurLoc.z)

    //unlock entity movement so it can run away
    unlock(theDog)

    //allow to "moon-walk" to the new location
    //Does weird stuff or ignores this when in 'following' mode
    //theDog.face(player)

    forceWalk(theDog, dogWalkToNewLoc, "dumb")

}
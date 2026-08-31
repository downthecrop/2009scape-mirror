package content.region.morytania.quest.hauntedmine

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.NPCs
import core.game.world.map.Location
import core.tools.colorize

/**
 * Haunted Mine cutscene for the start of the Treus Dayth boss fight.
 */

// Dayth cutscene: https://youtu.be/PMn0LRo4MCo?si=pi6gGDFnlgW_TPX7&t=498
// The key is supposed to authentically move back a bit and twirl.
// I had trouble getting move() to do that, so it just jiggles on the crate. I think this is fine.

class TreusDaythCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location(2788, 4454))
        loadRegion(11077)
        addNPC(NPCs.INNOCENT_LOOKING_KEY_1543, 36, 39, Direction.NORTH)
        setAttribute(player, HauntedMine.ATTR_KEEP_FUNGUS, true)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 36, 38)
                face(player, getNPC(NPCs.INNOCENT_LOOKING_KEY_1543)!!)
                // put camera at start spot
                moveCamera(40, 41, 300, 100)
                rotateCamera(36, 39, 300, 100)
                timedUpdate(1)
            }

            1 -> {
                animate(player, 832)
                animate(getNPC(NPCs.INNOCENT_LOOKING_KEY_1543)!!, 1451) // Key's linked anims are 1450 and 1451.
                timedUpdate(3)
            }

            2 -> {
                resetCamera()
                playerDialogueUpdate(FacialExpression.SUSPICIOUS, "What the... ?")
            }

            3 -> {
                dialogueLinesUpdate(
                    "You hear a disembodied voice laughing at you.... ",
                    colorize("%RPoor puny mortal. I'm going to have fun with you.")
                )
            }

            4 -> {
                // these camera moves are so awful to debug.
                moveCamera(36, 38, 300, 1000)
                rotateCamera(36, 38, 300, 1000)
                timedUpdate(1)
            }

            5 -> {
                addNPC(NPCs.TREUS_DAYTH_1540, 36, 42, Direction.SOUTH)
                val treus = getNPC(NPCs.TREUS_DAYTH_1540)!!
                setAttribute(treus, HauntedMine.ATTR_HAUNTED_TARGET, player.name)
                lock(treus, 10) // lock this little shit because otherwise he tries to move.
                animate(getNPC(NPCs.TREUS_DAYTH_1540)!!, 1437)
                timedUpdate(4)
            }

            6 -> {
                animate(getNPC(NPCs.TREUS_DAYTH_1540)!!, 5545)
                lock(getNPC(NPCs.TREUS_DAYTH_1540)!!, 10)
                timedUpdate(5)
            }

            7 -> {
                endWithoutFade {
                    clearNPCs()
                    startBossFight(player)
                    removeAttribute(player, HauntedMine.ATTR_KEEP_FUNGUS)
                }
            }
        }
    }
}
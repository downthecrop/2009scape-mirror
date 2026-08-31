package content.region.morytania.quest.lairoftarn

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.NPCs
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics

/**
 * Lair of Tarn Razorlor boss fight.
 * Cutscene Source: https://youtu.be/niPSlUd8OHI?si=Aj3oxogxyur056iB&t=185
 * Some dialogue during the cutscene: https://www.youtube.com/watch?v=4QtJRexA3Xo
 */

class LairOfTarnCutscene(player: Player) : Cutscene(player) {

    companion object {
        const val TARN = NPCs.TARN_5419
        const val MUTANT = NPCs.MUTANT_TARN_5421
        const val DOGSTATUE = NPCs.TERROR_DOG_STATUE_5415
        const val TERRORDOG = NPCs.TERROR_DOG_5417
        const val TRANSFORM = 5611
        const val CAST = 5633
        const val SPELL = 1006
        const val SPELLPROJ = 1007
        const val SPELLIMPACT = 1008
    }

    override fun setup() {
        setAttribute(player, LairOfTarn.ATTR_TARN_CUTSCENE, true)
        setExit(Location.create(3186, 4612, 0))
        loadRegion(12616)
        addNPC(TARN, 50, 15, Direction.SOUTH)
        addNPC(DOGSTATUE, 47, 13, Direction.SOUTH)
        addNPC(DOGSTATUE, 52, 13, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                teleport(player, 50, 4)
                // put camera at start spot
                moveCamera(50, 9, 300, 100)
                rotateCamera(50, 4, 300, 100)
                timedUpdate(5)
            }

            1 -> {
                //rotate camera to face Tarn
                rotateCamera(50, 15, 300, 5)
                timedUpdate(10)
            }

            2 -> {
                dialogueUpdate(TARN, FacialExpression.ANGRY, "Who? How did you get here? Never mind. I'll deal with you myself!")
            }

            3 -> {
                // tarn does magic anim, projectile from tarn to left statue
                sendGraphics(SPELL, getNPC(TARN)!!.location)
                animate(getNPC(TARN)!!, CAST)
                timedUpdate(4)
            }

            4 -> {
                // camera pans to left terror dog statue. Projectile.
                rotateCamera(48, 13, 300, 100)
                spawnProjectile(getNPC(TARN)!!, getNPC(DOGSTATUE)!!, SPELLPROJ)
                timedUpdate(4)
            }

            5 -> {
                // both statues transform to terror dogs. the dogs should have some transformation graphic/anim but I can't find it.
                sendGraphics(Graphics(SPELLIMPACT, 200, 0), location(47, 13, 0))
                getNPCs(DOGSTATUE)
                    .filter { it.id == DOGSTATUE }
                    .forEach { transformNpc(it, TERRORDOG, 100) }

                timedUpdate(5)
            }

            6 -> {
                // tarn transforms
                rotateCamera(50, 15, 300, 10)
                animate(getNPC(TARN)!!, TRANSFORM)
                timedUpdate(5)
            }

            7 -> {
                // tarn transforms
                transformNpc(getNPC(TARN)!!, MUTANT, 100)
                timedUpdate(5)
            }

            8 -> {
                // zoom in on top of tarn (camera to the right)
                moveCamera(51, 13, 400, 200)
                timedUpdate(5)
            }

            9 -> {
                // zoom in on mid of tarn (camera to the right)
                moveCamera(51, 13, 300, 200)
                timedUpdate(5)
            }

            10 -> {
                // zoom in on tarn's face (camera to the right)
                moveCamera(50, 13, 300, 200)
                timedUpdate(5)
            }

            11 -> {
                // zoom in on tarn's face (camera to the left)
                moveCamera(49, 13, 300, 200)
                timedUpdate(5)
            }

            12 -> {
                // camera backs out and NPCs start moving forward
                moveCamera(50, 8, 400)
                timedUpdate(5)
            }

            13 -> {
                endWithoutFade {
                    clearNPCs()
                    removeAttribute(player, LairOfTarn.ATTR_TARN_CUTSCENE)
                    startBossFight(player)
                }
            }
        }
    }
}
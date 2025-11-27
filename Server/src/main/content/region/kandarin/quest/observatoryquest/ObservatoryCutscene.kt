package content.region.kandarin.quest.observatoryquest

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.Animations
import org.rs09.consts.NPCs
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeFinishedCutscene
import core.game.world.map.Location

class ObservatoryCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2438, 3163))
        loadRegion(9777)
        addNPC(PROFESSOR, 7, 25, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            1 -> {
                teleport(player, 6, 27)
                face(player, getNPC(PROFESSOR)!!.location)
                rotateCamera(9, 25, 300)
                moveCamera(25, 22, 1800)
                timedUpdate(5)
            }
            2 -> {
                fadeFromBlack()
                dialogueLinesUpdate("<col=8A0808>~ The Observatory ~</col>", "The great eye into the heavens.")
            }
            3 -> {
                moveCamera(14, 10, 1800, 10)
                timedUpdate(3)
            }
            4 -> {
                moveCamera(6, 10, 1800, 10)
                timedUpdate(3)
            }
            5 -> {
                moveCamera(0, 32, 1800, 10)
                timedUpdate(3)
            }
            6 -> {
                moveCamera(4, 36, 1600, 10)
                timedUpdate(3)
            }
            7 -> {
                rotateCamera(7, 25, 300, 5)
                moveCamera(13, 40, 1800, 10)
                timedUpdate(3)
            }
            8 -> {
                moveCamera(12, 36, 1800, 5)
                timedUpdate(5)
            }
            9 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            10 -> {
                moveCamera(5, 21, 500)
                rotateCamera(6, 27, 100)
                timedUpdate(3)
            }
            11 -> {
                fadeFromBlack()
                timedUpdate(5)
            }
            12 -> {
                playerDialogueUpdate(FacialExpression.NEUTRAL, "Hi professor!")
            }
            13 -> {
                face(getNPC(PROFESSOR)!!, player.location)
                timedUpdate(3)
            }
            14 -> {
                animate(getNPC(PROFESSOR)!!, WAVE)
                dialogueUpdate(PROFESSOR, FacialExpression.HAPPY, "Oh, hi there.")
            }
            15 -> {
                dialogueUpdate(PROFESSOR, FacialExpression.HAPPY, "I'm just adding the finishing touches.")
            }
            16 -> {
                playerDialogueUpdate(FacialExpression.HAPPY, "Okay, don't let me interrupt.")
            }
            17 -> {
                dialogueUpdate(PROFESSOR, FacialExpression.NEUTRAL, "Thank you.")
            }
            18 -> {
                face(getNPC(PROFESSOR)!!, Location(136, 25))
                dialogueUpdate(PROFESSOR, FacialExpression.NEUTRAL, "Right, let's get this finished.")
            }
            19 -> {
                animate(getNPC(PROFESSOR)!!, THINKING)
                sendChat(getNPC(PROFESSOR)!!, "Hmmmm...")
                timedUpdate(5)
            }
            20 -> {
                move(getNPC(PROFESSOR)!!, 7, 23)
                rotateCamera(9, 23, 100)
                moveCamera(12, 30, 300)
                timedUpdate(3)
            }
            21 -> {
                move(getNPC(PROFESSOR)!!, 10, 26)
                timedUpdate(10)
            }
            22 -> {
                face(getNPC(PROFESSOR)!!, Location(135, 25))
                animate(getNPC(PROFESSOR)!!, FIX)
                sendChat(getNPC(PROFESSOR)!!, "Bit of a tap here...")
                timedUpdate(3)
            }
            23 -> {
                move(getNPC(PROFESSOR)!!, 11, 26)
                timedUpdate(5)
            }
            24 -> {
                face(getNPC(PROFESSOR)!!, player.location)
                dialogueUpdate(PROFESSOR, FacialExpression.HAPPY, "@name, I'm just going upstairs to finish off.")
            }
            25 -> {
                playerDialogueUpdate(FacialExpression.HAPPY, "Right-oh.")
            }
            26 -> {
                face(getNPC(PROFESSOR)!!, Location(140, 25))
                timedUpdate(3)
            }
            27 -> {
                teleport(getNPC(PROFESSOR)!!, 11, 22, 1)
                teleport(player, 14,29,1)
                face(getNPC(PROFESSOR)!!, Location(2442, 3158, 1))
                moveCamera(6, 20, 400)
                rotateCamera(10, 24, 400)
                timedUpdate(5)
            }
            28 -> {
                move(getNPC(PROFESSOR)!!, 7, 27)
                moveCamera(10, 20, 400, 1)
                rotateCamera(7, 27, 50, 20)
                timedUpdate(10)
            }
            29 -> {
                sendChat(getNPC(PROFESSOR)!!, "In goes the lens.")
                timedUpdate(3)
            }
            30 -> {
                animate(getNPC(PROFESSOR)!!, TUNEUP)
                timedUpdate(20)
            }
            31 -> {
                sendChat(getNPC(PROFESSOR)!!, "And one final adjustment.")
                move(getNPC(PROFESSOR)!!, 10, 29)
                timedUpdate(6)
            }
            32 -> {
                move(getNPC(PROFESSOR)!!, 10, 25)
                face(getNPC(PROFESSOR)!!, Location(137, 26))
                timedUpdate(6)
            }
            33 -> {
                face(getNPC(PROFESSOR)!!, Location(137, 26))
                animate(getNPC(PROFESSOR)!!, CRANK)
                sendChat(getNPC(PROFESSOR)!!, "And all our work pays off.")
                timedUpdate(6)
            }
            34 -> {
                animate(getNPC(PROFESSOR)!!, CHEER)
                timedUpdate(10)
            }
            35 -> {
                moveCamera(6, 27, 500, 5)
                rotateCamera(10, 25, 50, 5)
                timedUpdate(3)
            }
            36 -> {
                end {
                    setAttribute(player, attributeFinishedCutscene, true)
                }
            }
        }
    }

    companion object {
        val PROFESSOR = NPCs.OBSERVATORY_PROFESSOR_6121
        val WAVE = Animations.HUMAN_WAVE_863
        val THINKING = 6844
        val FIX = 6847
        val TUNEUP = 6848
        val CRANK = 6845
        val CHEER = Animations.HUMAN_CHEER_862
    }
}
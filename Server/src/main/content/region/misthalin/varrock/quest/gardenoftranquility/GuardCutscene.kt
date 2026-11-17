package content.region.misthalin.varrock.quest.gardenoftranquility

import core.api.*
import core.api.setAttribute
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Animations
import org.rs09.consts.NPCs

class GuardCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2964, 3381, 0))
        loadRegion(11829)
        teleport(player, 18, 2)
        addNPC(BILLY, 20, 4, Direction.NORTH)
        addNPC(BOB, 23, 4, Direction.NORTH)
        addNPC(PKMASTER, 22, 17, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                moveCamera(16, 15, 900)
                rotateCamera(22, 2, 300)
                timedUpdate(3)
            }
            1 -> {
                fadeFromBlack()
                dialogueUpdate("Meanwhile, Billy - a new recruit of the Falador Guard - has just arrived for his first shift, guarding the Northern Falador gates... ")

            }
            2 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.ASKING, "So what are you doing this weekend?")
                animate(getNPC(BILLY)!!, Animations.HUMAN_YAWN_2111)
            }
            3 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                dialogueUpdate(BOB, FacialExpression.LAUGH, "This weekend?")
                animate(getNPC(BOB)!!, Animations.HUMAN_GOT_LAUGH_2573)
            }
            4 -> {
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "You haven't been here long I take it.")
            }
            5 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.ASKING, "What do you mean?")
            }
            6 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                timedUpdate(10)
                dialogueUpdate(BOB, FacialExpression.ASKING, "Do you know the life expectancy of a Falador guard?")
            }
            7 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.ASKING, "No...")
                animate(getNPC(BILLY)!!, Animations.HUMAN_SHAKE_HEAD_NO_856)
            }
            8 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "About thirty seconds, I reckon.")
            }
            9 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.AMAZED, "Thirty seconds???")
            }
            10 -> {
                dialogueUpdate(BILLY, FacialExpression.ANGRY, "They didn't tell me that when I signed up!!!")
            }
            11 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "Any second now someone is going to try and kill us, for no apparent reason, completely unprovoked.")
            }
            12 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.AMAZED, "But why, Bob???")
            }
            13 -> {
                dialogueUpdate(BILLY, FacialExpression.SAD, "What have we ever done to them?")
            }
            14 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                dialogueUpdate(BOB, FacialExpression.SAD, "I don't know, Billy.")
            }
            15 -> {
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "But I'm not wasting my final seconds worrying about it.")
            }
            16 -> {
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "Uh-oh, someone's coming...")
            }
            17 -> {
                teleport(player, 24, 10)
                moveCamera(22, 10, 300)
                rotateCamera(22, 20, 100)
                timedUpdate(2)
            }
            18 -> {
                move(getNPC(PKMASTER)!!, 21, 15)
                timedUpdate(2)
            }
            19 -> {
                setAttribute(getNPC(PKMASTER)!!, "1hko", true)
                teleport(player, 18, 2)
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.SCARED, "Bob???")
            }
            20 -> {
                moveCamera(22, 8, 400)
                rotateCamera(24, 20, 100)
                move(getNPC(PKMASTER)!!, 21, 12)
                timedUpdate(4)
            }
            21 -> {
                moveCamera(25, 6, 350)
                rotateCamera(23, 4, 300)
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "Steady, Billy - remember you are a guard of Falador.")
            }
            22 -> {
                moveCamera(22, 0, 300)
                rotateCamera(21, 15, 100)
                move(getNPC(PKMASTER)!!, 23, 9)
                timedUpdate(4)
            }
            23 -> {
                moveCamera(19, 6, 225)
                rotateCamera(20, 4, 300)
                dialogueUpdate(BILLY, FacialExpression.SCARED, "But...")
            }
            24 -> {
                moveCamera(22, 0, 300)
                rotateCamera(21, 15, 100)
                move(getNPC(PKMASTER)!!, 23, 5)
                timedUpdate(4)
            }
            25 -> {
                moveCamera(21, 8, 300)
                rotateCamera(23, 4, 100)
                dialogueUpdate(BOB, FacialExpression.NEUTRAL, "See you in the next world...")
            }
            26 -> {
                animate(getNPC(PKMASTER)!!, ATTACK_ANIM)
                impact(getNPC(BOB)!!, 22, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(2)
            }
            27 -> {
                animate(getNPC(BOB)!!, Animations.HUMAN_DEATH_836)
                timedUpdate(2)
            }
            28 -> {
                moveCamera(19, 7, 225)
                rotateCamera(20, 4, 150)
                move(getNPC(PKMASTER)!!, 21, 5)
                face(getNPC(PKMASTER)!!, getNPC(BILLY)!!)
                timedUpdate(4)
            }
            29 -> {
                face(getNPC(PKMASTER)!!, getNPC(BILLY)!!)
                animate(getNPC(PKMASTER)!!, ATTACK_ANIM)
                impact(getNPC(BILLY)!!, 22, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(2)
            }
            30 -> {
                animate(getNPC(BILLY)!!, Animations.HUMAN_DEATH_836)
                timedUpdate(2)
            }
            31 -> {
                moveCamera(24, 5, 225)
                timedUpdate(3)
            }
            32 -> {
                moveCamera(24, 15, 800, 10)
                timedUpdate(3)
            }
            33 -> {
                animate(getNPC(PKMASTER)!!, Animations.HUMAN_BURYING_BONES_827)
                timedUpdate(3)
            }
            34 -> {
                move(getNPC(PKMASTER)!!, 23, 5)
                timedUpdate(3)
            }
            35 -> {
                animate(getNPC(PKMASTER)!!, Animations.HUMAN_BURYING_BONES_827)
                timedUpdate(3)
            }
            36 -> {
                move(getNPC(PKMASTER)!!, 23, 0)
                timedUpdate(3)
            }

            37 -> end {
                setVarbit(player, GardenOfTranquility.VB_GUARD_CUTSCENE, 1, true)
                resetCamera()
            }
        }
    }

    companion object {
        const val BILLY = NPCs.BILLY_A_GUARD_OF_FALADOR_2586
        const val BOB = NPCs.BOB_ANOTHER_GUARD_OF_FALADOR_2587
        const val PKMASTER = NPCs.PKMASTER0036_2589

        const val ATTACK_ANIM = 390
    }
}
package rs09.game.content.quest.members.anma

import Cutscene
import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs09.consts.Animations
import org.rs09.consts.NPCs

class AnmaCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0,0,0)) //create copy of player's location
        loadRegion(14391)
        addNPC(FARMER, 36, 9, Direction.EAST, 0)
        addNPC(CHICKEN, 44, 10, Direction.WEST, 0)
        addNPC(ALICE, 43, 5, Direction.WEST)
        addNPC(COWKILLER, 52, 8, Direction.WEST)
        addNPC(COW, 37, 8, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }
            1 -> {
                teleport(player, 46, 13)
                face(player, getNPC(CHICKEN)!!.location)
                face(getNPC(FARMER)!!, getNPC(CHICKEN)!!)
                move(getNPC(FARMER)!!, 43, 10)
                timedUpdate(5)
            }
            2 -> {
                fadeFromBlack()
                moveCamera(48,16, 500)
                rotateCamera(44, 0)
                dialogueUpdate(FARMER, FacialExpression.NEUTRAL, "Here, chicky chicky!")
            }
            3 -> {
                move(getNPC(FARMER)!!, 44, 10)
                animate(getNPC(FARMER)!!, ATTEMPT_CATCH)
                move(getNPC(CHICKEN)!!, 45, 9)
                timedUpdate(2)
            }
            4 -> {
                move(getNPC(FARMER)!!, 53, 8)
                move(getNPC(CHICKEN)!!, 53, 8)
                timedUpdate(10)
            }
            5 -> {
                move(getNPC(CHICKEN)!!, 47, 8)
                timedUpdate(1)
            }
            6 -> {
                move(getNPC(FARMER)!!, 48, 8)
                timedUpdate(8)
            }
            7 -> {
                move(getNPC(CHICKEN)!!, 45, 10)
                animate(getNPC(FARMER)!!, ATTEMPT_CATCH)
                timedUpdate(1)
            }
            8 -> {
                move(getNPC(FARMER)!!, 45, 10)
                move(getNPC(CHICKEN)!!, 38, 10)
                animate(getNPC(CHICKEN)!!, CHICKEN_JUMP)
                timedUpdate(1)
            }
            9 -> {
                move(getNPC(FARMER)!!, 38, 10)
                timedUpdate(9)
            }
            10 -> {
                face(player, getNPC(FARMER)!!.location)
                dialogueUpdate(FARMER, FacialExpression.NEUTRAL, "Git 'ere yer pesky bird!")
            }
            11 -> {
                move(getNPC(FARMER)!!, 44, 10)
                move(getNPC(CHICKEN)!!, 45, 10)
                timedUpdate(8)
            }
            12 -> {
                animate(getNPC(FARMER)!!, ATTEMPT_CATCH)
                animate(getNPC(CHICKEN)!!, CHICKEN_JUMP)
                face(player, getNPC(CHICKEN)!!.location)
                move(getNPC(CHICKEN)!!, 47, 7)
                timedUpdate(3)
            }
            13 -> {
                move(getNPC(FARMER)!!, 52, 8)
                timedUpdate(6)
            }
            14 -> {
                face(getNPC(CHICKEN)!!, getNPC(CHICKEN)!!.location.transform(0, 1, 0))
                timedUpdate(6)
            }
            15 -> {
                move(getNPC(CHICKEN)!!, 47, 11)
                face(getNPC(COW)!!, getNPC(COW)!!.location.transform(Direction.EAST))
                timedUpdate(8)
            }
            16 -> {
                face(getNPC(CHICKEN)!!, getNPC(FARMER)!!.location)
                teleport(getNPC(COW)!!, 41, 10)
                dialogueUpdate(FARMER, FacialExpression.NEUTRAL, "Where'd she go?")
            }
            17 -> {
                setAttribute(getNPC(COWKILLER)!!, "1hko", true)
                move(getNPC(COWKILLER)!!, 46, 9)
                move(getNPC(COW)!!, 46, 10)
                move(getNPC(FARMER)!!, 44, 10)
                move(getNPC(ALICE)!!, 48, 8)
                timedUpdate(11)
            }
            18 -> {
                face(getNPC(FARMER)!!, player.location)
                face(player, getNPC(FARMER)!!.location)
                move(getNPC(CHICKEN)!!, 45, 9)
                dialogueUpdate(FARMER, FacialExpression.ANNOYED, "Git oof my laaaaaand!")
            }
            19 -> {
                face(getNPC(FARMER)!!, getNPC(ALICE)!!.location)
                face(getNPC(ALICE)!!, getNPC(COWKILLER)!!.location)
                dialogueUpdate(ALICE, FacialExpression.ANNOYED, "You heard my husband: leave now!")
            }
            20 -> {
                face(getNPC(ALICE)!!, getNPC(COWKILLER)!!.location)
                face(getNPC(COWKILLER)!!, getNPC(ALICE)!!.location)
                dialogueUpdate(COWKILLER, FacialExpression.HALF_THINKING, "Always the same, I can never get these animals to myself.")
            }
            21 -> {
                face(getNPC(COWKILLER)!!, getNPC(COW)!!.location)
                animate(getNPC(COWKILLER)!!, ATTACK_ANIM)
                impact(getNPC(COW)!!, 8, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(3)
            }
            22 -> {
                dialogueUpdate(ALICE, FacialExpression.SAD, "You killed Bessie!")
            }
            23 -> {
                dialogueUpdate(COWKILLER, FacialExpression.FRIENDLY, "Buying cowhides and feathers - ahh, that chicken is next, feathers for me!")
            }
            24 -> {
                move(getNPC(COWKILLER)!!, 46, 10)
                timedUpdate(3)
            }
            25 -> {
                animate(getNPC(COWKILLER)!!, Animations.HUMAN_BURYING_BONES_827)
                timedUpdate(1)
            }
            26 -> {
                face(getNPC(COWKILLER)!!, getNPC(COWKILLER)!!.location.transform(-1,0,0))
                timedUpdate(2)
            }
            27 -> {
                face(getNPC(CHICKEN)!!, getNPC(CHICKEN)!!.location.transform(0,1,0))
                sendChat(getNPC(CHICKEN)!!, "Woo woo!")
                timedUpdate(2)
            }
            28 -> {
                move(getNPC(CHICKEN)!!, 45, 10)
                timedUpdate(2)
            }
            29 -> {
                animate(getNPC(COWKILLER)!!, ATTACK_ANIM)
                animate(getNPC(CHICKEN)!!, CHICKEN_JUMP)
                impact(getNPC(CHICKEN)!!, 0, ImpactHandler.HitsplatType.MISS)
                timedUpdate(2)
            }
            30 -> {
                face(getNPC(CHICKEN)!!, getNPC(FARMER)!!)
                face(getNPC(FARMER)!!, getNPC(CHICKEN)!!)
                visualize(getNPC(FARMER)!!, GRAB_CHICKEN_ANIM, GRAB_CHICKEN_GFX)
                timedUpdate(6)
            }
            31 -> {
                getNPC(CHICKEN)!!.clear()
                playerDialogueUpdate(FacialExpression.FRIENDLY, "Well, that's one way to catch a chicken, I suppose.")
            }
            32 -> {
                end {
                    setQuestStage(player, "Animal Magnetism", 20)
                }
            }
        }
    }

    companion object {
        val FARMER = NPCs.ALICES_HUSBAND_5205
        val ALICE = NPCs.ALICE_5212
        val CHICKEN = NPCs.UNDEAD_CHICKEN_1692
        val COWKILLER = NPCs.COW1337KILLR_5210
        val COW = NPCs.UNDEAD_COW_5211
        val ATTEMPT_CATCH = 5377
        val ATTACK_ANIM = 2066
        val GRAB_CHICKEN_ANIM = 5376
        val GRAB_CHICKEN_GFX = 973
        val CHICKEN_JUMP = 5380
    }
}
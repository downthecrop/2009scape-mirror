package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import core.api.animate
import core.api.face
import core.api.impact
import core.api.setQuestStage
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Animations
import org.rs09.consts.NPCs

class GardenEndingCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location(3228, 3476, 0))
        loadRegion(12854)
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        addNPC(KINGROALD, 30, 35, Direction.SOUTH)
        addNPC(QUEENELLAMARIA, 31, 25, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }

            1 -> {
                teleport(player, 30, 34)
                moveCamera(26, 38, 2000)
                rotateCamera(30, 30)
                timedUpdate(10)
            }

            2 -> {
                fadeFromBlack()
                moveCamera(30, 38, 800, 10)
                move(getNPC(QUEENELLAMARIA)!!, 31, 28)
                timedUpdate(1)
            }

            3 -> {
                face(getNPC(KINGROALD)!!, player)
                move(player, 30, 29)
                move(getNPC(KINGROALD)!!, 30, 30)
                dialogueUpdate(KINGROALD, FacialExpression.NEUTRAL, "This is murder on my hay fever you know...")
            }

            4 -> {
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.HAPPY, "Ah, Rollie my love!")
                face(getNPC(QUEENELLAMARIA)!!, Location(30, 29, 0))
            }

            5 -> {
                dialogueUpdate(KINGROALD, FacialExpression.SCARED, "What have you been up to now, pumpkin?")
            }

            6 -> {
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.HAPPY, "Behold your new garden!!")
            }

            7 -> {
                dialogueUpdate(KINGROALD, FacialExpression.SCARED, "Have you been spending my money again?")
            }

            8 -> {
                move(player, 29, 29)
                face(player, Location(157, 30, 0))
                face(getNPC(KINGROALD)!!, player)
                playerDialogueUpdate(FacialExpression.HAPPY, "Here's some orchids from Morytania.")
            }

            9 -> {
                dialogueUpdate(KINGROALD, FacialExpression.ANGRY, "Orchids bring me out in a rash.")
            }

            10 -> {
                move(player, 28, 27)
                face(player, Location(155, 27, 0))
                move(getNPC(KINGROALD)!!, 28, 28)
                face(getNPC(KINGROALD)!!, player)
                timedUpdate(3)
            }

            11 -> {
                moveCamera(32, 32, 400)
                rotateCamera(27, 27)
                playerDialogueUpdate(FacialExpression.HAPPY, "Vines from Burthorpe!")
            }

            12 -> {
                dialogueUpdate(KINGROALD, FacialExpression.NEUTRAL, "Oh great, let's hope they don't eat into the walls.")
            }

            13 -> {
                move(player, 31, 24)
                face(player, Location(159, 25, 0))
                move(getNPC(KINGROALD)!!, 30, 24)
                timedUpdate(3)
            }

            14 -> {
                moveCamera(27, 29, 400)
                rotateCamera(32, 24)
                face(getNPC(KINGROALD)!!, Location(159, 24, 0))
                playerDialogueUpdate(FacialExpression.HAPPY, "Snowdrops from Ardougne!")
            }

            15 -> {
                dialogueUpdate(KINGROALD, FacialExpression.NEUTRAL, "What a boring colour.")
            }

            16 -> {
                move(player, 31, 21)
                face(player, Location(159, 20, 0))
                move(getNPC(KINGROALD)!!, 31, 22)
                timedUpdate(3)
            }

            17 -> {
                moveCamera(27, 25, 400)
                rotateCamera(31, 20)
                face(getNPC(KINGROALD)!!, player)
                playerDialogueUpdate(FacialExpression.HAPPY, "The last surviving White Tree!")
            }

            18 -> {
                dialogueUpdate(KINGROALD, FacialExpression.NEUTRAL, "Should make a nice table and chairs.")
            }

            19 -> {
                move(player, 28, 19)
                face(player, Location(155, 19, 0))
                move(getNPC(KINGROALD)!!, 28, 20)
                move(getNPC(QUEENELLAMARIA)!!, 28, 22)
                face(getNPC(QUEENELLAMARIA)!!, getNPC(KINGROALD)!!)
                timedUpdate(3)
            }

            20 -> {
                moveCamera(32, 14, 400)
                rotateCamera(27, 19)
                face(getNPC(KINGROALD)!!, player)
                playerDialogueUpdate(FacialExpression.HAPPY, "Delphiniums from Falador!")
            }

            21 -> {
                dialogueUpdate(KINGROALD, FacialExpression.NEUTRAL, "I hate Delphiniums.")
            }

            22 -> {
                moveCamera(32, 24, 400)
                rotateCamera(27, 21)
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.HAPPY, "Rollie?")
            }

            23 -> {
                move(player, 28, 20)
                move(getNPC(KINGROALD)!!, 28, 21)
                face(getNPC(KINGROALD)!!, getNPC(QUEENELLAMARIA)!!)
                dialogueUpdate(KINGROALD, FacialExpression.HAPPY, "Yes pumpkin?")
            }

            24 -> {
                moveCamera(31, 22, 300)
                rotateCamera(27, 21)
                animate(getNPC(QUEENELLAMARIA)!!, Animations.HUMAN_GOT_SLAP_2579)
                impact(getNPC(KINGROALD)!!, 0, ImpactHandler.HitsplatType.MISS)
                timedUpdate(3)
            }

            25 -> {
                animate(getNPC(KINGROALD)!!, SCRATCH_HEAD)
                timedUpdate(3)
            }

            26 -> {
                moveCamera(29, 18, 300)
                rotateCamera(22, 30)
                timedUpdate(3)
            }

            27 -> {
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.ANGRY, "One has gone to great pains to provide you with this beautiful garden!")
            }

            28 -> {
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.ANGRY, "YOU CAN SHOW ONE A LITTLE GRATITUDE!!")
            }

            29 -> {
                moveCamera(31, 22, 300)
                rotateCamera(27, 21)
                playerDialogueUpdate(FacialExpression.SUSPICIOUS, "Actually I think I did most of-")
            }

            30 -> {
                moveCamera(29, 18, 300)
                rotateCamera(22, 30)
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.ANGRY, "SILENCE!!")
            }

            31 -> {
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.ANGRY, "Well? Are you going to thank one, Rollie?")
            }

            32 -> {
                moveCamera(31, 22, 300)
                rotateCamera(27, 21)
                dialogueUpdate(KINGROALD, FacialExpression.SCARED, "Pumpkin I-")
            }

            33 -> {
                moveCamera(29, 18, 300)
                rotateCamera(22, 30)
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.ANGRY, "SPEAK UP, I CAN'T HEAR YOU!!")

            }

            34 -> {
                moveCamera(31, 22, 300)
                rotateCamera(27, 21)
                dialogueUpdate(KINGROALD, FacialExpression.SCARED, "Thank you very much my darling.")
            }

            35 -> {
                moveCamera(29, 18, 300)
                rotateCamera(22, 30)
                dialogueUpdate(QUEENELLAMARIA, FacialExpression.NEUTRAL, "Much better. Now get back to the castle and rule our kingdom.")
            }

            36 -> {
                moveCamera(31, 22, 300)
                rotateCamera(27, 21)
                dialogueUpdate(KINGROALD, FacialExpression.SCARED, "Yes pumpkin.")
            }

            37 -> end {
                setQuestStage(player, Quests.GARDEN_OF_TRANQUILITY, 60)
                resetCamera()
            }
        }
    }

    companion object {
        private const val KINGROALD = NPCs.KING_ROALD_2590
        private const val QUEENELLAMARIA = NPCs.ELLAMARIA_2581

        const val SCRATCH_HEAD = 857

    }
}
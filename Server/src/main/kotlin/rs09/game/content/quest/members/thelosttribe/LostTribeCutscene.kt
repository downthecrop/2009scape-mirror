package rs09.game.content.quest.members.thelosttribe

import Cutscene
import api.animate
import api.face
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.Direction
import core.game.world.map.Location

class LostTribeCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(3207, 3221, 0))
        if(player.settings.isRunToggled){
            player.settings.toggleRun()
        }
        loadRegion(12850)
        addNPC(DUKE, 6, 23, Direction.SOUTH)
        addNPC(MISTAG, 8, 17, Direction.NORTH)
        addNPC(URTAG, 8, 15, Direction.NORTH)
        addNPC(SIGMUND, 13, 22, Direction.NORTH_WEST)
    }

    override fun runStage(stage: Int) {
        when(stage)
        {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                teleport(player, 7, 17)
                timedUpdate(2)
            }
            2 -> {
                fadeFromBlack()
                moveCamera(8, 26)
                rotateCamera(5, 18)
                timedUpdate(6)
            }
            3 -> {
                DoorActionHandler.handleDoor(player, getObject(7, 17))
                timedUpdate(3)
            }
            4 -> {
                move(player, 7, 22)
                move(getNPC(MISTAG)!!, 7, 19)
                move(getNPC(URTAG)!!, 7, 18)
                timedUpdate(5)
            }
            5 -> {
                move(getNPC(MISTAG)!!, 7, 20)
                move(getNPC(URTAG)!!, 6, 21)
                timedUpdate(3)
            }
            6 -> {
                player.faceLocation(getNPC(DUKE)!!.location)
                playerDialogueUpdate(FacialExpression.FRIENDLY, "Your grace, I present Ur-tag, headman of the Dorgeshuun.")
                Emotes.BOW.play(player)
            }
            7 -> {
                move(player, 7, 24)
                timedUpdate(5)
            }
            8 -> {
                animate(getNPC(DUKE)!!, Emotes.BOW.animation)
                animate(getNPC(URTAG)!!, URTAG_BOW_ANIM)
                dialogueUpdate(DUKE, FacialExpression.FRIENDLY, "Welcome, Ur-tag. I am sorry that your race came under suspicion.")
            }
            9 -> {
                dialogueUpdate(DUKE, FacialExpression.ANGRY, "I assure you that the warmongering element has been dealt with.")
                moveCamera(9, 22)
                rotateCamera(6, 22)
            }
            10 -> dialogueUpdate(URTAG, FacialExpression.OLD_NORMAL, "I apologize for the damage to your cellar. I will send workers to repair the hole.")
            11 -> dialogueUpdate(DUKE, FacialExpression.FRIENDLY, "No, let it stay. It can be a route of commerce between our lands.")
            12 -> {
                val duke = getNPC(DUKE)!!
                face(duke, player.location)
                face(player, duke.location)
                rotateCamera(6, 22, 300, 3)
                moveCamera(11, 22, 325, 3)
                dialogueUpdate(DUKE, FacialExpression.FRIENDLY, "${player.username}, Lumbridge is in your debt. Please accept this ring as a token of my thanks.")
            }
            13 -> dialogueUpdate(DUKE, FacialExpression.FRIENDLY, "It is enchanted to save you in your hour of need.")
            14 -> {
                move(getNPC(URTAG)!!, 7, 23)
                dialogueUpdate(URTAG, FacialExpression.OLD_NORMAL,"I too thank you. Accept the freedom of the Dorgeshuun mines." )
            }
            15 -> {
                dialogueUpdate(URTAG, FacialExpression.OLD_NORMAL, "These are strange times. I never dreamed that I would see the surface, still less that I would be on friendly terms with its people." )
                moveCamera(16, 21, 300, 3)
                rotateCamera(6, 22, 300, 2)
            }
            16 -> dialogueUpdate(SIGMUND, FacialExpression.ANGRY, "Prattle on, goblin.")
            17 -> {
                animate(getNPC(SIGMUND)!!, Emotes.LAUGH.animation)
                dialogueUpdate(SIGMUND, FacialExpression.EVIL_LAUGH, "Soon you will be destroyed!")
            }
            18 -> {
                move(getNPC(SIGMUND)!!, 16, 17)
                timedUpdate(4)
            }
            19 -> {
                end {
                    player.questRepository.getQuest("Lost Tribe").setStage(player, 100)
                    player.questRepository.getQuest("Lost Tribe").finish(player)
                }
            }
        }
    }

    companion object {
        private const val DUKE = 2088
        private const val MISTAG = 2089
        private const val SIGMUND = 2090
        private const val URTAG = 5858
        private const val URTAG_BOW_ANIM = 6000
    }
}
package content.region.kandarin.quest.grandtree

import core.ServerConstants
import core.api.sendChat
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.NPCs

// Source video https://www.youtube.com/watch?v=LS1Xtuz0sLA
class BlackDemonCutscene(player: Player) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2491, 9864, 0))
        if(player.settings.isRunToggled){
            player.settings.toggleRun()
        }
        loadRegion(9882)
        addNPC(NPCs.GLOUGH_671, 48, 8, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when(stage)
        {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                fadeFromBlack()
                teleport(player, 59, 8)
                moveCamera(55, 8)
                rotateCamera(0, 0)
                sendChat(player, "Hello?")
                player.face(getNPC(NPCs.GLOUGH_671)!!)
                addNPC(NPCs.BLACK_DEMON_677, 39, 8, Direction.EAST)
                move(getNPC(NPCs.BLACK_DEMON_677)!!, 41, 8)
                timedUpdate(3)
            }
            2 -> {
                moveCamera(48, 8)
                sendChat(player, "Anybody?")
                timedUpdate(3)
            }
            3 -> {
                sendChat(player, "Glough?")
                move(getNPC(NPCs.GLOUGH_671)!!, 52, 8)
                timedUpdate(10)
            }
            4 -> {
                moveCamera(55, 4, 2000)
                rotateCamera(55, 6)
                timedUpdate(4)
            }
            5 -> {
                move(player, 53, 8)
                playerDialogueUpdate(FacialExpression.SCARED, "Glough?")
            }
            6 -> {
                dialogueUpdate(NPCs.GLOUGH_671, FacialExpression.OLD_ANGRY1, "You really are becoming a headache! Well, at least now you can die knowing you were right, it will save me having to hunt you down like all the other human filth of " + ServerConstants.SERVER_NAME + "!")
            }
            7 -> {
                playerDialogueUpdate(FacialExpression.SCARED, "You're crazy, Glough!")
            }
            8 -> {
                dialogueUpdate(NPCs.GLOUGH_671, FacialExpression.OLD_ANGRY1, "Bah! Well, soon you'll see, the gnomes are ready to fight. In three weeks this tree will be dead wood, in ten weeks it will be 30 battleships! Finally we will rid the world of the disease called humanity!")
            }
            9 -> {
                playerDialogueUpdate(FacialExpression.SCARED, "What makes you think I'll let you get away with it?")
            }
            10 -> {
                moveCamera(55, 8, 700)
                rotateCamera(48, 8)
                val demon = getNPC(NPCs.BLACK_DEMON_677)!!
                move(demon, 46, 8)
                demon.animate(Animation(64, 140))
                dialogueUpdate(NPCs.GLOUGH_671, FacialExpression.OLD_ANGRY1, "Fool...meet my little friend!")
            }
            11 -> {
                end(fade = false){
                    BlackDemonNPC(NPCs.BLACK_DEMON_677, Location.create(2485, 9864, 0)).init()
                }
            }
        }
    }
}
package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location

class UndergroundCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2514, 9740,0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10136)
        addNPC(EDMOND, 18, 13, Direction.SOUTH)
    }


    override fun runStage(stage: Int) {
        when (stage) {

            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }

            1 -> {
                teleport(player, 18, 13)
                timedUpdate(1)
            }

            2 -> {
                move(player, 18, 12)
                fadeFromBlack()
                moveCamera(21, 16)
                rotateCamera(18, 13)
                timedUpdate(4)
            }

            3 -> {
                visualize(getNPC(EDMOND)!!, ROPE_PULL, 2270)
                visualize(player, ROPE_PULL, 2270)
                sendChat(player, "1... 2... 3... Pull!")
                timedUpdate(6)
            }

            4 -> {
                setVarbit(player, 1787, 6, true) // Grill removal.
                face(player, getNPC(EDMOND)!!.location)
                timedUpdate(2)
            }

            5 -> {
                dialogueUpdate(EDMOND, FacialExpression.FRIENDLY, "Once you're in the city look for a man called Jethick, he's an old friend and should help you. Send")
                sendChat(getNPC(EDMOND)!!, "Once you're in the city")
                timedUpdate(4)
            }

            6 -> {
                sendChat(getNPC(EDMOND)!!, "look for a man called")
                timedUpdate(4)
            }

            7 -> {
                sendChat(getNPC(EDMOND)!!, "Jethick, he's an old friend")
                timedUpdate(4)
            }

            8 -> {
                sendChat(getNPC(EDMOND)!!, "and should help you. Send")
                timedUpdate(4)
            }

            9 -> {
                dialogueUpdate(EDMOND, FacialExpression.FRIENDLY, "him my regards, I Haven't seen him since before Elena was born.")
                sendChat(getNPC(EDMOND)!!, "him my regards, I haven't")
                timedUpdate(4)
            }

            10 -> {
                sendChat(getNPC(EDMOND)!!, "seen him since before")
                timedUpdate(4)
            }

            11 -> {
                sendChat(getNPC(EDMOND)!!, "Elena was born.")
                timedUpdate(4)
            }

            12 -> {
                sendChat(player, "Alright, thanks I will.")
                timedUpdate(3)
            }

            13 -> {
                sendPlayerDialogue(player,"Alright, thanks I will.")
                timedUpdate(4)
            }

            14 -> {
                end {
                    setQuestStage(player, "Plague City", 7)
                }
            }
        }
    }

    companion object {
        private const val EDMOND = 714
        private val ROPE_PULL = 3187
    }
}

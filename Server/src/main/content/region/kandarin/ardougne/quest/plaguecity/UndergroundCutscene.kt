package content.region.kandarin.ardougne.quest.plaguecity

import content.data.Quests
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
                teleport(player, 18, 12)
                setVarbit(player, 1787, 2, true) // Grill removal.
                timedUpdate(1)
            }

            2 -> {
                move(player, 18, 12)
                face(player, base.transform(18, 11, 0))
                // You have to remove the stubborn previous sceneries before adding your own.
                removeScenery(getScenery(base.transform(18, 11, 0))!!)
                removeScenery(getScenery(base.transform(18, 12, 0))!!)
                removeScenery(getScenery(base.transform(18, 13, 0))!!)
                // Add the rope sceneries to animate.
                addScenery(11416, base.transform(18, 11, 0),2,10)
                addScenery(11412, base.transform(18, 12, 0),2,10)
                addScenery(11414, base.transform(18, 13, 0),2,10)
                fadeFromBlack()
                moveCamera(21, 16)
                rotateCamera(18, 13)
                timedUpdate(4)
            }

            3 -> {
                animate(getNPC(EDMOND)!!, ROPE_PULL)
                animate(player, ROPE_PULL)
                animateScenery(player, getScenery(base.transform(18, 11, 0))!!, 3189)
                animateScenery(player, getScenery(base.transform(18, 12, 0))!!, 3188)
                animateScenery(player, getScenery(base.transform(18, 13, 0))!!, 3188)
                sendChat(player, "1... 2... 3... Pull!")
                timedUpdate(6)
            }

            4 -> {
                face(player, getNPC(EDMOND)!!.location)
                timedUpdate(2)
            }

            5 -> {
                dialogueUpdate(EDMOND, FacialExpression.FRIENDLY, "Once you're in the city look for a man called Jethick, he's an old friend and should help you. Send", hide=true)
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
                dialogueUpdate(EDMOND, FacialExpression.FRIENDLY, "him my regards, I haven't seen him since before Elena was born.", hide=true)
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
                // todo change this to true once no continue button player model size is fixed
                sendPlayerDialogue(player,"Alright, thanks I will.", hide = false)
                timedUpdate(2)
            }
            13 -> {
                end {
                    setQuestStage(player, Quests.PLAGUE_CITY, 7)
                }
            }
        }
    }

    companion object {
        private const val EDMOND = 714
        private const val ROPE_PULL = 3187
    }
}

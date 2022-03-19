package rs09.game.content.tutorial

import api.*
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.ClimbActionHandler
import core.game.content.global.action.DoorActionHandler
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

/**
 * Handles tutorial-specific node interactions
 * @author Ceikry
 */
class TutorialListeners : InteractionListener() {
    val GUIDE_HOUSE_DOOR = 3014
    val COOKS_DOOR = 3017
    val COOKS_EXIT = 3018
    val QUEST_ENTER = 3019
    val QUEST_LADDER = 3029
    val COMBAT_EXIT = 3030
    val BANK_EXIT = 3024
    val FINANCE_EXIT = 3025
    val CHURCH_EXIT = 3026
    val FIRST_GATE = intArrayOf(3015,3016)
    val COMBAT_GATES = intArrayOf(3020,3021)
    val RAT_GATES = intArrayOf(3022, 3023)

    override fun defineListeners() {
        on(GUIDE_HOUSE_DOOR, SCENERY, "open"){player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 3)
                return@on true


            setAttribute(player, "tutorial:stage", 4)
            TutorialStage.load(player, 4)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery, Location.create(3098, 3107, 0))
            return@on true
        }

        on(FIRST_GATE, SCENERY, "open"){player, gate ->
            if(getAttribute(player, "tutorial:stage", 0) != 16)
                return@on true

            setAttribute(player, "tutorial:stage", 17)
            TutorialStage.load(player, 17)
            DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
            return@on true
        }

        on(COOKS_DOOR, SCENERY, "open"){player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 17)
                return@on true

            setAttribute(player, "tutorial:stage", 18)
            TutorialStage.load(player, 18)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        on(COOKS_EXIT, SCENERY, "open"){player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 22)
                return@on true

            setAttribute(player, "tutorial:stage", 23)
            TutorialStage.load(player, 23)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        on(QUEST_ENTER, SCENERY, "open") {player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 26)
                return@on true

            setAttribute(player, "tutorial:stage", 27)
            TutorialStage.load(player, 27)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        on(QUEST_LADDER, SCENERY, "climb-down") {player, ladder ->
            if(getAttribute(player, "tutorial:stage", 0) != 29)
                return@on true

            setAttribute(player, "tutorial:stage", 30)
            TutorialStage.load(player, 30)
            ClimbActionHandler.climbLadder(player, ladder.asScenery(), "climb-down")
        }

        on(COMBAT_GATES, SCENERY, "open"){player, gate ->
            if(getAttribute(player, "tutorial:stage", 0) != 43)
                return@on true

            setAttribute(player, "tutorial:stage", 44)
            TutorialStage.load(player, 44)
            DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
        }

        on(RAT_GATES, SCENERY, "open") {player, gate ->
            val stage = getAttribute(player, "tutorial:stage", 0)
            if(stage !in 50..53){
                player.dialogueInterpreter.sendDialogues(NPCs.COMBAT_INSTRUCTOR_944, FacialExpression.ANGRY, "Oi, get away from there!","Don't enter my rat pen unless I say so!")
                return@on true
            }

            if(stage == 50) {
                setAttribute(player, "tutorial:stage", 51)
                TutorialStage.load(player, 51)
            }
            DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
            return@on true
        }

        on(COMBAT_EXIT, SCENERY, "climb-up") {player, ladder ->
            if(getAttribute(player, "tutorial:stage", 0) != 55)
                return@on true

            setAttribute(player, "tutorial:stage", 56)
            TutorialStage.load(player, 56)
            ClimbActionHandler.climbLadder(player, ladder.asScenery(), "climb-up")
        }

        on(BANK_EXIT, SCENERY, "open") {player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 57)
                return@on true

            setAttribute(player, "tutorial:stage", 58)
            TutorialStage.load(player, 58)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

        on(FINANCE_EXIT, SCENERY, "open") {player,door ->
            if(getAttribute(player, "tutorial:stage", 0) != 59)
                return@on true

            setAttribute(player, "tutorial:stage", 60)
            TutorialStage.load(player, 60)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

        on(CHURCH_EXIT, SCENERY, "open") {player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 66)
                return@on true

            setAttribute(player, "tutorial:stage", 67)
            TutorialStage.load(player, 67)
            DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

    }
}
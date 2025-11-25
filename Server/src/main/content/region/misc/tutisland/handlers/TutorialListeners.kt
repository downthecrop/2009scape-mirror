package content.region.misc.tutisland.handlers

import content.global.skill.smithing.smelting.Bar
import content.region.misc.tutisland.dialogue.RatPenDialogue
import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Sounds

/**
 * Handles tutorial-specific node interactions
 * @author Ceikry
 */
class TutorialListeners : InteractionListener {
    val GUIDE_HOUSE_DOOR = 3014
    val COOKS_DOOR = 3017
    val RANGE = 3039
    val COOKS_EXIT = 3018
    val QUEST_ENTER = 3019
    val QUEST_LADDER = 3029
    val QUEST_EXIT_LADDER = 3028
    val TIN_ROCK = 3043
    val COPPER_ROCK = 3042
    val COMBAT_EXIT = 3030
    val BANK_EXIT = 3024
    val FINANCE_EXIT = 3025
    val CHURCH_EXIT = 3026
    val FIRST_GATE = intArrayOf(3015,3016)
    val COMBAT_GATES = intArrayOf(3020,3021)
    val RAT_GATES = intArrayOf(3022, 3023)
    val FURNACE = 3044

    override fun defineListeners() {
        on(GUIDE_HOUSE_DOOR, IntType.SCENERY, "open"){ player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 3)
                return@on true


            setAttribute(player, "tutorial:stage", 4)
            TutorialStage.load(player, 4)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery, Location.create(3098, 3107, 0))
            return@on true
        }

        on(FIRST_GATE, IntType.SCENERY, "open"){ player, gate ->
            if(getAttribute(player, "tutorial:stage", 0) != 16)
                return@on true

            setAttribute(player, "tutorial:stage", 17)
            TutorialStage.load(player, 17)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
            return@on true
        }

        on(COOKS_DOOR, IntType.SCENERY, "open"){ player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 17)
                return@on true

            setAttribute(player, "tutorial:stage", 18)
            TutorialStage.load(player, 18)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        fun cookBread(player: Player, dough: Item): Boolean {
            if (getAttribute(player, "tutorial:stage", 0) < 20) {
                return true
            }
            // Need to reinvent the wheel of cooking. Yes, I do. On tutorial island, we don't want the default stuff like asking the player what dough they want to make.
            queueScript(player, 0, QueueStrength.WEAK) { stage ->
                if (stage == 0) {
                    val RANGE_ANIMATION = Animation(883, Animator.Priority.HIGH)
                    lock(player, RANGE_ANIMATION.duration)
                    lockInteractions(player, RANGE_ANIMATION.duration)
                    animate(player, RANGE_ANIMATION)
                    playAudio(player, Sounds.FRY_2577)
                    return@queueScript delayScript(player, RANGE_ANIMATION.duration)
                } else {
                    replaceSlot(player, dough.slot, Item(Items.BREAD_2309), dough)
                    setAttribute(player, "tutorial:stage", 21)
                    TutorialStage.load(player, 21)
                    return@queueScript stopExecuting(player)
                }
            }
            return true
        }
        on(RANGE, IntType.SCENERY, "use") { player, _ ->
            val dough = player.inventory.get(Item(Items.BREAD_DOUGH_2307)) ?: return@on true
            return@on cookBread(player, dough)
        }
        onUseWith(IntType.SCENERY, Items.BREAD_DOUGH_2307, RANGE) { player, dough, _ ->
            return@onUseWith cookBread(player, dough as Item)
        }

        on(COOKS_EXIT, IntType.SCENERY, "open"){ player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 22)
                return@on true

            setAttribute(player, "tutorial:stage", 23)
            TutorialStage.load(player, 23)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        on(QUEST_ENTER, IntType.SCENERY, "open") { player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 26)
                return@on true

            setAttribute(player, "tutorial:stage", 27)
            TutorialStage.load(player, 27)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
            return@on true
        }

        on(QUEST_LADDER, IntType.SCENERY, "climb-down") { player, ladder ->
            if(getAttribute(player, "tutorial:stage", 0) < 29)
                return@on true

            if (getAttribute(player, "tutorial:stage", 0) == 29) {
                setAttribute(player, "tutorial:stage", 30)
                TutorialStage.load(player, 30)
            }
            core.game.global.action.ClimbActionHandler.climbLadder(player, ladder.asScenery(), "climb-down")
        }

        on(QUEST_EXIT_LADDER, IntType.SCENERY, "climb-up") { player, ladder ->
            core.game.global.action.ClimbActionHandler.climbLadder(player, ladder.asScenery(), "climb-up")

            submitWorldPulse(object : Pulse(2) {
                override fun pulse(): Boolean {
                    val questTutor = Repository.findNPC(NPCs.QUEST_GUIDE_949) ?: return true
                    sendChat(questTutor, "What are you doing, ${player.username}? Get back down the ladder.")
                    return true
                }
            })

            return@on true
        }

        on(TIN_ROCK, IntType.SCENERY, "prospect") { player, _ ->
            if (getAttribute(player, "tutorial:stage", 0) != 31) {
                return@on true
            }
            setAttribute(player, "tutorial:stage", 32)
            TutorialStage.load(player, 32)
            return@on true
        }
        on(COPPER_ROCK, IntType.SCENERY, "prospect") { player, _ ->
            if (getAttribute(player, "tutorial:stage", 0) != 33) {
                return@on true
            }
            setAttribute(player, "tutorial:stage", 34)
            TutorialStage.load(player, 34)
            return@on true
        }

        on(COMBAT_GATES, IntType.SCENERY, "open"){ player, gate ->
            if (getAttribute(player, "tutorial:stage", 0) < 43) {
                return@on true
            }
            if (getAttribute(player, "tutorial:stage", 0) == 43) {
                setAttribute(player, "tutorial:stage", 44)
                TutorialStage.load(player, 44)
            }
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
        }

        on(RAT_GATES, IntType.SCENERY, "open") { player, gate ->
            val stage = getAttribute(player, "tutorial:stage", 0)
            if (stage !in 50..53) {
                openDialogue(player, RatPenDialogue(), NPC(NPCs.COMBAT_INSTRUCTOR_944))
                return@on true
            }
            if (stage == 50) {
                setAttribute(player, "tutorial:stage", 51)
                TutorialStage.load(player, 51)
            }
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, gate as Scenery)
            return@on true
        }

        on(COMBAT_EXIT, IntType.SCENERY, "climb-up") { player, ladder ->
            val stage = getAttribute(player, "tutorial:stage", 0)
            if (stage < 55) {
                return@on true
            }
            if (stage == 55) {
                setAttribute(player, "tutorial:stage", 56)
                TutorialStage.load(player, 56)
            }
            core.game.global.action.ClimbActionHandler.climbLadder(player, ladder.asScenery(), "climb-up")
        }

        on(BANK_EXIT, IntType.SCENERY, "open") { player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 57)
                return@on true

            setAttribute(player, "tutorial:stage", 58)
            TutorialStage.load(player, 58)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

        on(FINANCE_EXIT, IntType.SCENERY, "open") { player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 59)
                return@on true

            setAttribute(player, "tutorial:stage", 60)
            TutorialStage.load(player, 60)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

        on(CHURCH_EXIT, IntType.SCENERY, "open") { player, door ->
            if(getAttribute(player, "tutorial:stage", 0) != 66)
                return@on true

            setAttribute(player, "tutorial:stage", 67)
            TutorialStage.load(player, 67)
            core.game.global.action.DoorActionHandler.handleAutowalkDoor(player, door as Scenery)
        }

        fun smeltBronzeBar(player: Player): Boolean {
            if (getAttribute(player, "tutorial:stage", 0) < 38) {
                return true
            }
            if (!inInventory(player, Items.COPPER_ORE_436) || !inInventory(player, Items.TIN_ORE_438)) {
                return true
            }
            animate(player, 833)
            queueScript(player, 2, QueueStrength.WEAK) {
                if (removeItem(player, Items.COPPER_ORE_436) && removeItem(player, Items.TIN_ORE_438)) {
                    addItem(player, Items.BRONZE_BAR_2349)
                    rewardXP(player, Skills.SMITHING, Bar.BRONZE.experience)
                    player.dispatch(ResourceProducedEvent(Items.BRONZE_BAR_2349, 1, player, Items.COPPER_ORE_436))
                    TutorialStage.load(player, 39)
                }
                return@queueScript stopExecuting(player)
            }
            return true
        }
        on(FURNACE, IntType.SCENERY, "use") { player, _ -> smeltBronzeBar(player) }
        for (item in arrayOf(Items.COPPER_ORE_436, Items.TIN_ORE_438)) {
            onUseWith(IntType.SCENERY, item, FURNACE) { player, _, _ -> smeltBronzeBar(player) }
        }
    }
}

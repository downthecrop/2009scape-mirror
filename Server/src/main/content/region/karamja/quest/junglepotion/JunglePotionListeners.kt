package content.region.karamja.quest.junglepotion

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Listeners for the Jungle Potion quest.
 */

class JunglePotionListeners : InteractionListener {

    companion object {
        // search anims
        const val JP_SEARCH_1 = 2094
        const val JP_SEARCH_2 = 2097

        // you can give turned in herbs to Trufitus in return for a few coins
        fun giveHerb(player: Player, item: Item) {
            if (removeItem(player, item)) {
                val amt = RandomFunction.random(1, 5)
                val coin = if (amt == 1) "a coin" else "$amt coins"
                val herb = when (item.id) {
                    Items.CLEAN_SNAKE_WEED_1526 -> "snake weed"
                    Items.CLEAN_ARDRIGAL_1528 -> "adrigal"
                    Items.CLEAN_SITO_FOIL_1530 -> "sito foil"
                    Items.CLEAN_VOLENCIA_MOSS_1532 -> "volencia moss"
                    else -> "rogue's purse"
                }
                addItemOrDrop(player, Items.COINS_995, amt)
                sendNPCDialogue(player, NPCs.TRUFITUS_740, "Many thanks for the herb, Bwana, but you've already given me one of these. I'll add it to my collection.")
                sendMessage(player, "Trufitus gives you $coin for the $herb.")
            }
        }
    }

    override fun defineListeners() {

        // snakeweed
        on(Scenery.MARSHY_JUNGLE_VINE_2575, IntType.SCENERY, "search") { player, node ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            if (stage >= 1) {
                findHerb(player, Items.GRIMY_SNAKE_WEED_1525, node, "vine", JP_SEARCH_1)
            } else sendMessage(player, "Unfortunately, you find nothing of interest.") // the search fail messages are sourced from Lost City
            return@on true
        }

        // ardrigal
        on(Scenery.PALM_TREE_2577, IntType.SCENERY, "search") { player, node ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            if (stage >= 3) {
                instaHerb(player, Items.GRIMY_ARDRIGAL_1527, node, "palm")
                if (stage == 3) setQuestStage(player, Quests.JUNGLE_POTION, 4)
            } else sendMessage(player, "You find nothing of significance.")
            return@on true
        }

        // sito foil
        on(Scenery.SCORCHED_EARTH_2579, IntType.SCENERY, "search") { player, node ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            if (stage >= 5) {
                instaHerb(player, Items.GRIMY_SITO_FOIL_1529, node, "scorched earth")
                if (stage == 5) setQuestStage(player, Quests.JUNGLE_POTION, 6)
            } else sendMessage(player, "You find nothing of significance.")
            return@on true
        }

        // volencia moss
        on(Scenery.ROCK_2581, IntType.SCENERY, "search") { player, node ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            if (stage >= 7) {
                instaHerb(player, Items.GRIMY_VOLENCIA_MOSS_1531, node, "rock")
                if (stage == 7) setQuestStage(player, Quests.JUNGLE_POTION, 8)
            } else sendMessage(player, "You find nothing of significance.")
            return@on true
        }

        // rogue's purse
        on(Scenery.FUNGUS_COVERED_CAVERN_WALL_32106, IntType.SCENERY, "search") { player, node ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            if (stage >= 9) {
                findHerb(player, Items.GRIMY_ROGUES_PURSE_1533, node, "wall", JP_SEARCH_2)
            } else sendMessage(player, "Unfortunately, you find nothing of interest.")
            return@on true
        }

        // entrance to cavern
        on(Scenery.ROCKS_2584, IntType.SCENERY, "search") { player, _ ->
            openDialogue(player, object : DialogueLabeller() {
                override fun addConversation() {
                    line("You search the rocks and find an entrance into some caves.")
                    options(
                        DialogueOption("yes", "Yes, I'll enter the cave.", skipPlayer = true),
                        DialogueOption("no", "No thanks, I'll give it a miss.", skipPlayer = true),
                        title = "Would you like to enter the caves?"
                    )

                    label("yes")
                    line("You decide to enter the caves. You climb down several steep rock", "faces into the cavern below.")
                    exec { player, _ ->
                        teleport(player, Location(2830, 9523, 0))
                    }
                    goto("end")

                    label("no")
                    line("You decide to stay where you are!")
                    goto("end")
                }
            }, NPC(NPCs.TRUFITUS_740)) // pay no attention to the passed NPC, it's passed so the exec stage doesn't freak out
            return@on true
        }

        // exit from cavern
        on(Scenery.HAND_HOLDS_2585, IntType.SCENERY, "climb") { player, _ ->
            openDialogue(player, object : DialogueLabeller() {
                override fun addConversation() {
                    line("You climb the rocks and get back out.")
                    exec { player, _ ->
                        teleport(player, Location(2823, 3120, 0))
                    }
                }
            }, NPC(NPCs.TRUFITUS_740)) // pay no attention to the passed NPC, it's passed so the exec stage doesn't freak out
            return@on true
        }
    }

    // helper to search for herbs (chance)
    fun findHerb(player : Player, item : Int, scenery : Node, name : String, anim : Int) {
        queueScript(player, 0) { stage ->
            when (stage) {
                // animate
                0 -> {
                    animate(player, anim)
                    sendMessage(player, "You search the $name...")
                    return@queueScript delayScript(player, Animation(anim).duration)
                }

                // roll for herb. I believe authentically this is a skill roll, but have no idea what the odds are.
                1 -> {
                    if (RandomFunction.roll(4)) {
                        addItemOrDrop(player, item)
                        sendItemDialogue(player, item, "You find a grimy herb.")
                        replaceScenery(scenery.asScenery(), scenery.id+1, 80)

                        // advance stage
                        val stage = getQuestStage(player, Quests.JUNGLE_POTION)
                        if (stage == 1) setQuestStage(player, Quests.JUNGLE_POTION, 2)  // finding snakeweed
                        if (stage == 9) setQuestStage(player, Quests.JUNGLE_POTION, 10) // finding rogues purse
                    }
                    return@queueScript stopExecuting(player)
                }
            }
            return@queueScript stopExecuting(player)
        }
    }

    // helper to search for herbs (instant)
    fun instaHerb(player : Player, item : Int, scenery : Node, name : String) {
        sendMessage(player, "You search the $name...")
        addItemOrDrop(player, item)
        sendItemDialogue(player, item, "You find a grimy herb.")
        replaceScenery(scenery.asScenery(), scenery.id+1, 80)
    }
}
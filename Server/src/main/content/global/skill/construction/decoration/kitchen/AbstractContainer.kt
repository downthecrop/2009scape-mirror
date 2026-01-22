package content.global.skill.construction.decoration.kitchen

import core.api.addItem
import core.api.freeSlots
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE

abstract class AbstractContainer(containerId: Int, containers: Map<Int,List<Pair<String, Int>>>, private val containerName: String) : DialogueFile() {

    private val container = containers[containerId]!!
    private val containerItemsNames = container.map { it.first }
    private val containerItemsIds = container.map { it.second }
    private val size = container.size
    private val itemsPerPage = 4

    private fun checkItem(player: Player, idx : Int){
        val item = containerItemsIds[idx]
        if (addItem(player, item)){
            end()
        }
        else{
            sendDialogue(player, "You need at least one free inventory space to take from the $containerName.").also { stage = END_DIALOGUE }
        }
    }

    private fun createTopics(page: Int): MutableList<Topic<Int>> {
        val startIndex = (page - 1) * itemsPerPage
        val endIndex = (startIndex + itemsPerPage).coerceAtMost(size)

        val topics = containerItemsNames.subList(startIndex, endIndex)
            .mapIndexed { index, item -> Topic(item, startIndex + index + 1, skipPlayer = true) }
            .toMutableList()

        // Add "More" button if there's another page or to loop back
        if (endIndex < size || page > 1) {
            topics.add(Topic("More", endIndex + 1, skipPlayer = true))
        }

        return topics
    }



    override fun handle(componentID: Int, buttonID: Int) {
        if (freeSlots(player!!) == 0) {
            sendDialogue(player!!, "You need at least one free inventory space to take from the $containerName.").also { stage = END_DIALOGUE }
            return
        }
        val itemsPerPage = 4

        when (stage) {
            0, size + 1 -> { // Initial stage or final more button clicked
                val topics = createTopics(1)
                showTopics(*topics.toTypedArray()).also { stage = 1 }
            }
            in 1..size -> {
                val currentPage = (stage-1) / itemsPerPage
                val startIndex = currentPage * itemsPerPage

                if (buttonID == itemsPerPage + 1) { // "More" button clicked (but not the last one)
                    val topics = createTopics(currentPage + 1)
                    showTopics(*topics.toTypedArray()).also { stage = currentPage*itemsPerPage + 1 }
                } else { // Handle item selection
                    val itemIndex = startIndex + (buttonID - 1)
                    if (itemIndex < size) {
                        checkItem(player!!, itemIndex)
                    }
                }
            }
            else -> end()
        }
    }


}
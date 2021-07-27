package rs09.game.content.global.worldevents.penguinhns

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Plugin

class NotebookHandler : OptionHandler(){
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        val total = player?.getAttribute("phns:points",0)
        player?.dialogueInterpreter?.sendDialogue("Total points: $total")
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(13732).handlers["option:read"] = this
        return this
    }

}
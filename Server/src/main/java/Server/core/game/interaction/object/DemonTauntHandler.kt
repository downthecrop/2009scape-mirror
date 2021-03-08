package core.game.interaction.`object`

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
/**
 * Handles taunting of the demon in the wizard's tower
 * @author afaroutdude / Ceikry
 */
class DemonTauntHandler : OptionHandler(){
    override fun newInstance(arg: Any?): Plugin<Any> {
        ObjectDefinition.forId(37668).handlers["option:taunt-through"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        player.packetDispatch.sendMessage("You taunt the demon, making it growl.")
        val localNpcs = RegionManager.getLocalNpcs(player)
        player.animator.animate(Emotes.RASPBERRY.animation)
        for (npc in localNpcs) {
            if (npc.id == 82) {
                npc.sendChat("Graaaagh!", 1)
                player.faceLocation(npc.location)
            }
        }
        player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 1, 13)
        return true
    }
}
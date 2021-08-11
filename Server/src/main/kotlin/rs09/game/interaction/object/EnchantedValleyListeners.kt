package rs09.game.interaction.`object`

import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.gather.SkillingTool
import core.game.system.task.Pulse
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class EnchantedValleyListeners : InteractionListener() {
    val ENCHANTED_V_TREE = 16265
    val TREE_SPIRIT_IDS = intArrayOf(NPCs.TREE_SPIRIT_438,NPCs.TREE_SPIRIT_439,NPCs.TREE_SPIRIT_440,NPCs.TREE_SPIRIT_441,NPCs.TREE_SPIRIT_442,NPCs.TREE_SPIRIT_443)
    override fun defineListeners() {

        on(ENCHANTED_V_TREE,SCENERY,"chop-down"){ player, node ->
            val tool: SkillingTool? = SkillingTool.getHatchet(player)
            tool ?: player.sendMessage("You lack an axe which you have the Woodcutting level to use.").also { return@on true }

            player.pulseManager.run(object : Pulse(){
                var counter = 0
                val n = getSpirit(player)
                override fun pulse(): Boolean {
                    when(counter++){
                        0 -> player.animator.animate(tool?.animation)
                        3 -> {
                            n.location = player.location
                            n.init()
                            n.moveStep()
                            n.isRespawn = false
                        }
                        4 -> n.attack(player)
                    }
                    return false
                }
            })
            return@on true
        }

    }

    fun getSpirit(player: Player): NPC {
        val level = player.properties.currentCombatLevel
        var index = Math.ceil(level / 20.0).toInt()
        if(index >= TREE_SPIRIT_IDS.size) index = TREE_SPIRIT_IDS.size - 1
        return NPC(TREE_SPIRIT_IDS[index])
    }
}
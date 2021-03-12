package rs09.game.node.entity.skill.skillcapeperks

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class SkillcapeOperatePlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(cape in core.game.content.global.SkillcapePerks.values()){
            cape.skillcapeIds.forEach {
                ItemDefinition.forId(it).handlers["option:operate"] = this
            }
        }
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val skillcape = Skillcape.forId(node?.id ?: 0)
        val perk = SkillcapePerks.forSkillcape(skillcape)

        perk.operate(player)
        return true
    }

}
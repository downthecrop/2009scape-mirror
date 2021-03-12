package rs09.game.node.entity.skill.skillcapeperks

import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class SkillcapeEquipmentPlugin : Plugin<Item> {
    override fun newInstance(arg: Item?): Plugin<Item> {
        for(cape in core.game.content.global.SkillcapePerks.values()){
            cape.skillcapeIds.forEach {
                ItemDefinition.forId(it).handlers["equipment"] = this
            }
        }
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        val player = args[0] as Player
        val id = (args[1] as Item).id

        val skillcape = Skillcape.forId(id)
        val perk = SkillcapePerks.forSkillcape(skillcape)

        when(identifier){

            "equip" -> {
                perk.activate(player)
            }

            "unequip" -> {
                perk.deactivate(player)
            }
        }
        return true;
    }

}
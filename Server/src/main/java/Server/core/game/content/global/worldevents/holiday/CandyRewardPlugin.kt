package core.game.content.global.worldevents.holiday

import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.tools.RandomFunction
import core.plugin.CorePluginTypes.XPGainPlugin
import core.game.node.entity.skill.Skills
import core.tools.stringtools.colorize

class CandyRewardPlugin : XPGainPlugin(){
    override fun run(player: Player, skill: Int, amount: Double) {
        val awardCandy = RandomFunction.random(1,200) == 55
        val candy = Item(14084)

        if(awardCandy){
            if(!player.inventory.add(candy)){
                GroundItemManager.create(candy,player)
            }
            player.sendMessage(colorize("%OYou receive a candy while training ${Skills.SKILL_NAME[skill]}!"))
        }
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

}
package plugin.ai.general.scriptrepository

import core.game.node.item.Item
import core.tools.Items
import plugin.ai.skillingbot.SkillingBotAssembler
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.fletching.Fletching
import core.game.node.entity.skill.fletching.FletchingPulse

class FletchingBankstander : Script(){
    var state = State.FLETCHING
    override fun tick() {
        val bank = scriptAPI.getNearestNode("Bank booth")
        bot.faceLocation(bank?.location)
        state = when(state){
            State.FLETCHING -> {
                bot.inventory.add(Item(Items.KNIFE_946))
                bot.inventory.add(Item(Items.LOGS_1511,27))
                bot.pulseManager.run(FletchingPulse(bot, Item(Items.LOGS_1511),27,Fletching.FletchingItems.ARROW_SHAFT))
                State.BANKING
            }

            State.BANKING -> {
                bot.inventory.clear()
                State.FLETCHING
            }
        }
    }

    init {
        skills[Skills.FLETCHING] = 99
    }

    override fun newInstance(): Script {
        val script = FletchingBankstander()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE,bot.startLocation)
        return script
    }

    enum class State {
        FLETCHING,
        BANKING
    }
}
package plugin.ai.general.scriptrepository

import core.game.node.item.Item
import core.tools.Items
import plugin.ai.skillingbot.SkillingBotAssembler
import core.game.node.entity.skill.Skills

class NonBankingMiner : Script() {
    override fun tick() {
        val rock = scriptAPI.getNearestNode(11957,true)
        if(rock != null){
            rock.interaction.handle(bot,rock.interaction[0])
            if(bot.inventory.isFull)
                bot.inventory.clear()
        }
    }

    override fun newInstance(): Script {
        val script = NonBankingMiner()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }

    init {
        skills[Skills.ATTACK] = 99
        equipment.add(Item(Items.MITHRIL_PICKAXE_1273))
        inventory.add(Item(Items.MITHRIL_PICKAXE_1273))
        skills[Skills.MINING] = 50
    }
}
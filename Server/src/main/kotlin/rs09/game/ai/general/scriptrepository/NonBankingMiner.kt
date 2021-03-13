package rs09.game.ai.general.scriptrepository

import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.ai.skillingbot.SkillingBotAssembler

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
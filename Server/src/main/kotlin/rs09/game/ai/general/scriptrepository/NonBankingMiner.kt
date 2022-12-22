package rs09.game.ai.general.scriptrepository

import api.produceGroundItem
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.ai.skillingbot.SkillingBotAssembler
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners

class NonBankingMiner : Script() {
    override fun tick() {
        val rock = scriptAPI.getNearestNode(11957,true)
        if(rock != null && !bot.inventory.isFull){
            InteractionListeners.run(rock.id, IntType.SCENERY,"mine",bot,rock)
        }
        //checks if the bot has tin ore in his inventory and drops it if he does
        if(bot.inventory.containsAtLeastOneItem(Items.TIN_ORE_438)){
            produceGroundItem(null,438,1,bot.location)
            bot.inventory.remove(Item(Items.TIN_ORE_438,1))
        }
        //The following is to prevent lucky bots from breaking by having a full inventory of gems
        if(bot.inventory.isFull && (!bot.inventory.containsAtLeastOneItem(Items.TIN_ORE_438))){
            bot.inventory.clear()
            bot.inventory.add(Item(Items.MITHRIL_PICKAXE_1273))
        }
    }

    override fun newInstance(): Script {
        val script = NonBankingMiner()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }

    init {
        skills[Skills.ATTACK] = 99
        inventory.add(Item(Items.MITHRIL_PICKAXE_1273))
        skills[Skills.MINING] = 50
    }
}
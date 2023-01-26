package content.global.bots

import core.game.node.item.Item
import core.game.bots.SkillingBotAssembler
import core.game.bots.Script
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import java.util.*

class ManThiever : Script() {
    override fun tick() {
        val man = scriptAPI.getNearestNode("Man")
        bot.interfaceManager.close()
        man?.let { InteractionListeners.run(man.id,
            IntType.NPC,"Pickpocket",bot,man) }
    }

    override fun newInstance(): Script? {
        val script = ManThiever()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR, bot.startLocation)
        return script
    }

    init {
        equipment.addAll(Arrays.asList(Item(1103), Item(1139), Item(1265)))
    }
}
package content.global.bots

import content.global.skill.crafting.glass.GlassCraftingPulse
import content.global.skill.crafting.glass.GlassProduct
import core.game.bots.Script
import core.game.bots.SkillingBotAssembler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

class GlassBlowingBankstander : Script(){
    var state = State.BLOWING
    override fun tick() {
        val bank = scriptAPI.getNearestNode("Bank booth")
        bot.faceLocation(bank?.location)
        state = when(state){
            State.BLOWING -> {
                bot.inventory.add(Item(Items.GLASSBLOWING_PIPE_1785))
                bot.inventory.add(Item(Items.MOLTEN_GLASS_1775,27))
                bot.pulseManager.run(GlassCraftingPulse(bot, GlassProduct.ORB, 27))
                State.BANKING
            }

            State.BANKING -> {
                bot.inventory.clear()
                State.BLOWING
            }
        }
    }

    override fun newInstance(): Script {
        val script = GlassBlowingBankstander()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE,bot.startLocation)
        return script
    }

    init {
        skills[Skills.CRAFTING] = 99
    }

    enum class State {
        BLOWING,
        BANKING
    }
}
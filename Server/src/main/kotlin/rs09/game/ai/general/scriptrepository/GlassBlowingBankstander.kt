package rs09.game.ai.general.scriptrepository

import core.game.interaction.inter.GlassInterface
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.crafting.GlassProduct
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.ai.skillingbot.SkillingBotAssembler

class GlassBlowingBankstander : Script(){
    var state = State.BLOWING
    override fun tick() {
        val bank = scriptAPI.getNearestNode("Bank booth")
        bot.faceLocation(bank?.location)
        state = when(state){
            State.BLOWING -> {
                bot.inventory.add(Item(Items.GLASSBLOWING_PIPE_1785))
                bot.inventory.add(Item(Items.MOLTEN_GLASS_1775,27))
                GlassInterface.make(bot,GlassProduct.ORB,27)
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
package rs09.game.ai.general.scriptrepository

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.smithing.Bars
import core.game.node.entity.skill.smithing.SmithingPulse
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.ai.skillingbot.SkillingBotAssembler

class VarrockSmither : Script() {
    var state = State.SMITHING
    override fun tick() {
        when(state) {
            State.SMITHING -> {
                for (i in inventory) {
                    bot.inventory.add(i)
                }
                val anvil = scriptAPI.getNearestNode("anvil", true)
                if (anvil != null) {
                    bot.pulseManager.run(object : MovementPulse(bot, anvil, DestinationFlag.OBJECT) {
                        override fun pulse(): Boolean {
                            bot.faceLocation(anvil.location)
                            bot.pulseManager.run(SmithingPulse(bot, Item(2353), Bars.STEEL_ARROW_TIPS, 27))
                            state = State.BANKING
                            return true
                        }
                    })
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode("Bank booth")
                if(bank != null)
                    bot.pulseManager.run(object: MovementPulse(bot,bank, DestinationFlag.OBJECT){
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            bot.inventory.clear()
                            state = State.SMITHING
                            return true
                        }
                    })
            }
        }
    }

    override fun newInstance(): Script {
        val script = VarrockSmither()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.RICH, Location.create(3189, 3436, 0))
        return script
    }

    init {
        skills[Skills.SMITHING] = RandomFunction.random(33,99)
        inventory.add(Item(Items.HAMMER_2347))
        inventory.add(Item(Items.STEEL_BAR_2353,27))
    }

    enum class State {
        SMITHING,
        BANKING
    }
}
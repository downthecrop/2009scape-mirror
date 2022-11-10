package rs09.game.ai.general.scriptrepository
import core.game.world.map.zone.ZoneBorders
import rs09.game.ai.skillingbot.SkillingBotAssembler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners

class FarmerThiever : Script() {
    val pickpocketZone = ZoneBorders(3074,3245,3086,3255)
    val bankZone = ZoneBorders(3092, 3245,3092, 3242)
    val food = Items.JUG_OF_WINE_1993
    val foodAmount = 10
    var state = State.PICKPOCKETING


    override fun tick() {
        when(state) {

            State.PICKPOCKETING -> {
                if (!pickpocketZone.insideBorder(bot))
                    scriptAPI.walkTo(pickpocketZone.randomLoc)
                if (bot.inventory.isFull)
                    state = State.BANKING
                else {
                    val farmer = scriptAPI.getNearestNode(NPCs.MASTER_FARMER_2234, false)
                    bot.interfaceManager.close()
                    scriptAPI.eat(food)
                   if (farmer != null) {
                        InteractionListeners.run(farmer.id, IntType.NPC, "Pickpocket", bot, farmer)
                    }
                }
            }
            State.BANKING -> {
                val bank = scriptAPI.getNearestNode("Bank Booth",true)
                bank ?: return
                if (!bankZone.insideBorder(bot))
                    scriptAPI.walkTo(bankZone.randomLoc)
                else {
                    bot.faceLocation(bank.location)
                    bot.inventory.clear()
                    bot.inventory.add(Item(food, foodAmount))
                    state = State.PICKPOCKETING
                }
            }
        }
    }




    override fun newInstance(): Script {
        val script = FarmerThiever()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }
    enum class State {
        BANKING,
        PICKPOCKETING
    }

    init {
        inventory.add(Item(food,foodAmount))
        skills[Skills.THIEVING] = 80
        skills[Skills.HITPOINTS] = 10
    }


}
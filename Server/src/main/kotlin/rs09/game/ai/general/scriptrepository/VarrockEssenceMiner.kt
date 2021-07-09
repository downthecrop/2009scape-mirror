package rs09.game.ai.general.scriptrepository

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.ai.skillingbot.SkillingBotAssembler
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners

@PlayerCompatible
@ScriptDescription("Start in varrock bank with rune mysteries complete and a pickaxe equipped/in inventory")
@ScriptName("Varrock Essence Miner")
@ScriptIdentifier("essence_miner")
class VarrockEssenceMiner : Script(){

    var state = State.TO_ESSENCE
    val auburyZone = ZoneBorders(3252, 3398, 3254, 3402)
    val bankZone = ZoneBorders(3251, 3420,3254, 3422)
    override fun tick() {

        when(state){
            State.TO_ESSENCE -> {
                bot.interfaceManager.close()
                if(!auburyZone.insideBorder(bot))
                    scriptAPI.walkTo(auburyZone.randomLoc)
                else {
                    val aubury = scriptAPI.getNearestNode("Aubury")
                    aubury?.interaction?.handle(bot,aubury.interaction[3])
                    state = State.MINING
                }
                /*val bank = scriptAPI.getNearestNode("Bank booth",true)
                if(bank != null && bank.location?.withinDistance(bot.location,2) == true){
                    Pathfinder.find(bot, Location.create(3259, 3405, 0)).walk(bot)
                } else {
                    when(bot.location){
                        Location.create(3165, 3487, 0) -> scriptAPI.teleport(Location.create(3254, 3421, 0))
                        Location.create(3259, 3405, 0) -> Pathfinder.find(bot,Location.create(3253, 3400, 0)).walk(bot)
                        Location.create(3253, 3400, 0) -> {
                            val aubury = scriptAPI.getNearestNode("Aubury")
                            aubury?.interaction?.handle(bot,aubury.interaction[3])
                        }
                        Location.create(2922, 4820, 0) -> {
                            state = State.MINING
                        }
                    }
                }*/
            }

            State.MINING -> {
                val essence = scriptAPI.getNearestNode(2491,true)
                essence?.let { InteractionListeners.run(essence.id, InteractionListener.SCENERY,"mine",bot,essence) }
                if(bot.inventory.isFull)
                    state = State.TO_BANK
            }

            State.TO_BANK -> {
                val portal = scriptAPI.getNearestNode("Portal",true)
                if(portal != null && portal.location.withinDistance(bot.location,20))
                    portal.interaction.handle(bot,portal.interaction[0])
                else {
                    if(!bankZone.insideBorder(bot)){
                        scriptAPI.walkTo(bankZone.randomLoc)
                    } else {
                        state = State.BANKING
                    }
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode("bank booth",true)
                val item =
                if(bot.inventory.getAmount(Items.RUNE_ESSENCE_1436) > 0) Items.RUNE_ESSENCE_1436 else Items.PURE_ESSENCE_7936
                if(bank != null){
                    bot.pulseManager.run(object : MovementPulse(bot,bank, DestinationFlag.OBJECT){
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            scriptAPI.bankItem(item)
                            state = State.TO_ESSENCE
                            return true
                        }
                    })
                }
            }

            State.TELE_GE -> {
                if(bot.location != Location.create(3165, 3482, 0))
                    scriptAPI.walkTo(Location.create(3165, 3482, 0))
                else
                    state = State.SELL_GE
            }

            State.SELL_GE -> {
                scriptAPI.sellOnGE(Items.PURE_ESSENCE_7936)
                state = State.TO_ESSENCE
            }

        }

    }

    enum class State{
        TO_ESSENCE,
        TO_BANK,
        MINING,
        BANKING,
        TELE_GE,
        SELL_GE
    }

    override fun newInstance(): Script {
        val script = VarrockEssenceMiner()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }
}
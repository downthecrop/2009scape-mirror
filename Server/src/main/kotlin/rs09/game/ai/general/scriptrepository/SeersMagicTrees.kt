package rs09.game.ai.general.scriptrepository

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.ai.general.ScriptAPI
import rs09.game.ai.skillingbot.SkillingBotAssembler
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners

@PlayerCompatible
@ScriptName("Seers Magics")
@ScriptDescription("Start in Seers Bank with an axe equipped or in your inventory.")
@ScriptIdentifier("seers_magics")
class SeersMagicTrees : Script(){
    var state = State.INIT
    var stage = 0
    val bankZone  = ZoneBorders(2722,3490,2727,3493)
    val magicsZone = ZoneBorders(2700, 3396,2704, 3399)
    var overlay: ScriptAPI.BottingOverlay? = null
    var logCounter = 0

    override fun tick() {
        when(state){

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Woodcutting")
                overlay!!.setTaskLabel("Logs cut:")
                overlay!!.setAmount(0)
                state = State.RETURN_TO_TREES
            }

            State.CHOPPING -> {
                val tree = scriptAPI.getNearestNode(1306,true)
                bot.interfaceManager.close()
                tree?.let { InteractionListeners.run(tree.id, InteractionListener.SCENERY,"Chop down",bot,tree) }
                if(bot.inventory.isFull){
                    state = State.FIND_BANK
                }
                overlay!!.setAmount(logCounter + bot.inventory.getAmount(Items.MAGIC_LOGS_1513))
            }

            State.FIND_BANK -> {
                if(!bankZone.insideBorder(bot)){
                    scriptAPI.walkTo(bankZone.randomLoc)
                } else {
                    state = State.BANKING
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode(25808,true)
                if(bank != null)
                    bot.pulseManager.run(object: MovementPulse(bot,bank, DestinationFlag.OBJECT){
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            logCounter += bot.inventory.getAmount(Items.MAGIC_LOGS_1513)
                            scriptAPI.bankItem(Items.MAGIC_LOGS_1513)
                            state = State.RETURN_TO_TREES
                            return true
                        }
                    })
            }

            State.RETURN_TO_TREES -> {
                bot.interfaceManager.close()
                if(!magicsZone.insideBorder(bot)){
                    scriptAPI.walkTo(magicsZone.randomLoc)
                } else {
                    state = State.CHOPPING
                }
            }

            State.TELE_GE -> {
                state = State.SELL_GE
                scriptAPI.teleportToGE()
            }

            State.SELL_GE -> {
                state = State.TELE_SEERS
                scriptAPI.sellOnGE(Items.MAGIC_LOGS_1513)
            }

            State.TELE_SEERS -> {
                state = State.RETURN_TO_TREES
                scriptAPI.teleport(Location.create(2756, 3478, 0))
            }
        }
    }

    init {
        inventory.add(Item(Items.RUNE_AXE_1359))
        skills[Skills.WOODCUTTING] = RandomFunction.random(75,99)
    }

    enum class State {
        CHOPPING,
        FIND_BANK,
        BANKING,
        RETURN_TO_TREES,
        TELE_GE,
        SELL_GE,
        TELE_SEERS,
        INIT
    }

    override fun newInstance(): Script {
        val script = SeersMagicTrees()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.AVERAGE,bot.startLocation)
        return script
    }
}
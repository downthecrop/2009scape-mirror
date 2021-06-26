package rs09.game.ai.general.scriptrepository

<<<<<<< refs/remotes/2009scape/master
=======

>>>>>>> Player script fixes, and bankAll scriptAPI function added
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.ai.general.ScriptAPI
import rs09.game.ai.skillingbot.SkillingBotAssembler
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners
<<<<<<< refs/remotes/2009scape/master
=======
import api.ContentAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.logging.Handler
>>>>>>> Player script fixes, and bankAll scriptAPI function added

@PlayerCompatible
@ScriptName("Falador Coal Miner")
@ScriptDescription("Start in Falador East Bank with a pick equipped","or in your inventory.")
@ScriptIdentifier("fally_coal")
<<<<<<< refs/remotes/2009scape/master
class CoalMiner() : Script() {
    var state = State.INIT
    var ladderSwitch = false

=======
class CoalMiner : Script() {
    var state = State.INIT
    var ladderSwitch = false
>>>>>>> Player script fixes, and bankAll scriptAPI function added
    val bottomLadder = ZoneBorders(3016,9736,3024,9742)
    val topLadder = ZoneBorders(3016,3336,3022,3342)
    val mine = ZoneBorders(3027,9733,3054,9743)
    val bank = ZoneBorders(3009,3355,3018,3358)
    var overlay: ScriptAPI.BottingOverlay? = null
<<<<<<< refs/remotes/2009scape/master
    var coalAmount = 0
=======
	var coalAmount = 0
>>>>>>> Player script fixes, and bankAll scriptAPI function added

    override fun tick() {
        when(state){

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                ladderSwitch = true
                overlay!!.init()
                overlay!!.setTitle("Mining")
                overlay!!.setTaskLabel("Coal Mined:")
                overlay!!.setAmount(0)

                if (mine.insideBorder(bot)){
                    ladderSwitch = false
                    state = State.MINING
                } else {
                    state = State.TO_MINE
                }
            }

            State.MINING -> {
<<<<<<< refs/remotes/2009scape/master
=======
                bot.interfaceManager.close()
>>>>>>> Player script fixes, and bankAll scriptAPI function added
                if(bot.inventory.freeSlots() == 0){
                    state = State.TO_BANK
                }
                if(!mine.insideBorder(bot)){
                    scriptAPI.walkTo(mine.randomLoc)
                } else {
                    val rock = scriptAPI.getNearestNode("rocks",true)
                    rock?.let { InteractionListeners.run(rock.id, InteractionListener.OBJECT,"mine",bot,rock) }
                }
<<<<<<< refs/remotes/2009scape/master
                overlay!!.setAmount(bot.inventory.getAmount(Items.COAL_453) + coalAmount)
=======
                overlay!!.setAmount(ContentAPI.amountInInventory(bot, Items.COAL_453) + coalAmount)
>>>>>>> Player script fixes, and bankAll scriptAPI function added
            }

            State.TO_BANK -> {
                if(bank.insideBorder(bot)){
                    val bank = scriptAPI.getNearestNode("bank booth",true)
                    if(bank != null) {
                        bot.pulseManager.run(object : BankingPulse(this, bank){
                            override fun pulse(): Boolean {
                                state = State.BANKING
                                return super.pulse()
                            }
                        })
                    }

                } else {
                    if(!ladderSwitch) {
                        val ladder = scriptAPI.getNearestNode(30941, true)
                        ladder ?: scriptAPI.walkTo(bottomLadder.randomLoc).also { return }
                        ladder?.interaction?.handle(bot, ladder.interaction[0]).also { ladderSwitch = true }
                    } else {
                        if (!bank.insideBorder(bot)) scriptAPI.walkTo(bank.randomLoc).also { return }
                    }
                }
            }

            State.BANKING -> {
<<<<<<< refs/remotes/2009scape/master
                coalAmount += bot.inventory.getAmount(Items.COAL_453)
                scriptAPI.bankItem(Items.COAL_453)
=======
				coalAmount += bot.inventory.getAmount(Items.COAL_453)
                scriptAPI.bankAll()
>>>>>>> Player script fixes, and bankAll scriptAPI function added
                state = State.TO_MINE
            }

            State.TO_MINE -> {
                if(ladderSwitch){
<<<<<<< refs/remotes/2009scape/master
=======
                    bot.interfaceManager.close()
>>>>>>> Player script fixes, and bankAll scriptAPI function added
                    if(!topLadder.insideBorder(bot.location)){
                        scriptAPI.walkTo(topLadder.randomLoc)
                    } else {
                        val ladder = scriptAPI.getNearestNode("Ladder",true)
                        if(ladder != null){
                            ladder.interaction.handle(bot,ladder.interaction[0])
                            ladderSwitch = false
                        } else {
                            scriptAPI.walkTo(topLadder.randomLoc)
                        }
                    }
                } else {
                    if(!mine.insideBorder(bot)){
                        scriptAPI.walkTo(mine.randomLoc)
                    } else {
                        state = State.MINING
                    }
                }
            }

            State.TO_GE -> {
                scriptAPI.teleportToGE()
                state = State.SELLING
            }

            State.SELLING -> {
                scriptAPI.sellOnGE(Items.COAL_453)
                state = State.GO_BACK
            }

            State.GO_BACK -> {
                scriptAPI.teleport(bank.randomLoc)
                state = State.TO_MINE
            }
<<<<<<< refs/remotes/2009scape/master

=======
>>>>>>> Player script fixes, and bankAll scriptAPI function added
        }
    }

    open class BankingPulse(val script: Script, val bank: Node) : MovementPulse(script.bot,bank, DestinationFlag.OBJECT){
        override fun pulse(): Boolean {
            script.bot.faceLocation(bank.location)
            return true
        }
    }

    override fun newInstance(): Script {
        val script = CoalMiner()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }

    enum class State {
        MINING,
        TO_MINE,
        TO_BANK,
        BANKING,
        TO_GE,
        SELLING,
        GO_BACK,
        INIT
    }

    init {
        equipment.add(Item(Items.IRON_PICKAXE_1267))
        skills.put(Skills.MINING,75)
    }
<<<<<<< refs/remotes/2009scape/master

=======
>>>>>>> Player script fixes, and bankAll scriptAPI function added
}
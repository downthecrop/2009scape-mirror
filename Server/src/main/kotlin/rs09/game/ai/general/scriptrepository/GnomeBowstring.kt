package rs09.game.ai.general.scriptrepository

import api.*
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.crafting.spinning.SpinningItem
import core.game.node.entity.skill.crafting.spinning.SpinningPulse
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.ai.general.ScriptAPI

@PlayerCompatible
@ScriptName("Gnome Stronghold Bowstring")
@ScriptDescription("Start in Gnome Stronghold, South of the Agility Course")
@ScriptIdentifier("gnome_bowstring")
class GnomeBowstring : Script() {
    var state = State.PICKING
    var stage = 0
    var bLadderSwitch = false
    var sLadderSwitch = false
    val flaxzone = ZoneBorders(2457,3391,2493,3413,0)
    val bankbottomLadder = ZoneBorders(2444, 3413, 2445, 3414, 0)
    val banktopLadder = ZoneBorders(2445, 3415, 2446, 3414, 1)
    val spinnerbottomLadder = ZoneBorders (2475, 3400, 2476, 3399, 0)
    val spinnertopLadder = ZoneBorders (2474, 3397, 2476, 3399, 1)
    val pick = ZoneBorders(2478, 3394, 339, 9)
    val bank = ZoneBorders(2447, 3415, 2444, 3434)
    var overlay: ScriptAPI.BottingOverlay? = null
    override fun tick() {

        when(state){

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Picking")
                overlay!!.setTaskLabel("Flax Picked")
                overlay!!.setAmount(0)
            }

            State.PICKING -> {
                bot.interfaceManager.close()
                if(!flaxzone.insideBorder(bot)){
                    scriptAPI.walkTo(flaxzone.randomLoc)
                } else if(flaxzone.insideBorder(bot)) {
                    var flax = scriptAPI.getNearestNode(2646, true)
                    scriptAPI.interact(bot,flax,"pick")
                }
                if(bot.inventory.getAmount(Items.FLAX_1779) > 27){
                    sLadderSwitch = true
                    state = State.TO_SPINNER
                }
            }

            State.TO_SPINNER -> {
                if(sLadderSwitch){
                    if(!spinnerbottomLadder.insideBorder(bot.location)){
                        scriptAPI.walkTo(spinnerbottomLadder.randomLoc)
                    } else {
                        val ladder = scriptAPI.getNearestNode("Staircase", true)
                        if(ladder !=  null){
                            ladder.interaction.handle(bot, ladder.interaction[0])
                            sLadderSwitch = false
                        } else {
                            scriptAPI.walkTo(spinnerbottomLadder.randomLoc)
                        }
                    }
                }
                if(stage == 0)
                    Pathfinder.find(bot, Location.create(2475,3399, 1)).walk(bot).also { stage++ }
                when(bot.location){
                    Location.create(2475,3399, 1) -> Pathfinder.find(bot, Location.create(2477, 3399,1)).walk(bot)
                    Location.create(2477,3399, 1) -> Pathfinder.find(bot, Location.create(2477,3398, 1)).walk(bot)
                    Location.create(2477,3398, 1) -> Pathfinder.find(bot, Location.create(2476,3398, 1)).walk(bot)
                    Location.create(2476,3398, 1) -> {
                        val spinner = scriptAPI.getNearestNode(2644, true)
                        bot.faceLocation(spinner?.location)
                        bot.pulseManager.run(object: MovementPulse(bot,spinner, DestinationFlag.OBJECT){
                            override fun pulse(): Boolean {
                                bot.faceLocation(spinner?.location)
                                state = State.SPINNING
                                return true
                            }
                        })
                    }
                }
            }

            State.SPINNING -> {
                bot.pulseManager.run(SpinningPulse(bot, Item(Items.FLAX_1779),bot.inventory.getAmount(Items.FLAX_1779),SpinningItem.FLAX))
                sLadderSwitch = true
                state = State.FIND_BANK
            }

            State.FIND_BANK -> {
                if(sLadderSwitch){
                    val ladder = scriptAPI.getNearestNode("staircase", true)
                    if(ladder != null){
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        sLadderSwitch = false
                    }
                }
                if(!bankbottomLadder.insideBorder(bot.location) && !spinnertopLadder.insideBorder(bot)){
                    scriptAPI.walkTo(bankbottomLadder.randomLoc)
                }else if(bankbottomLadder.insideBorder(bot)){
                    bLadderSwitch = true
                }
                if(bLadderSwitch){
                    val ladder = scriptAPI.getNearestNode("staircase", true)
                    if(ladder != null){
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        bLadderSwitch = false
                    }
                }
                if(banktopLadder.insideBorder(bot)){
                    state = State.BANKING
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode(2213, true)
                if(bank != null) {
                    bot.pulseManager.run(object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            scriptAPI.bankItem(Items.BOW_STRING_1777)
                            return true
                        }
                    })
                }
                if(freeSlots(bot) > 27){
                    bLadderSwitch = true
                    state = State.RETURN_TO_FLAX
                }
            }

            State.RETURN_TO_FLAX -> {
                if(!banktopLadder.insideBorder(bot.location) && bLadderSwitch){
                    scriptAPI.walkTo(banktopLadder.randomLoc)
                } else if(bLadderSwitch){
                    val ladder = scriptAPI.getNearestNode("Staircase", true)
                    if(ladder != null){
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        bLadderSwitch = false
                    } else {
                        scriptAPI.walkTo(banktopLadder.randomLoc)
                    }
                }
                if(!flaxzone.insideBorder(bot)){
                    scriptAPI.walkTo(flaxzone.randomLoc)
                } else if(flaxzone.insideBorder(bot)){
                    state = State.PICKING
                }
            }
        }
    }
    override fun newInstance(): Script {
        TODO("Not yet implemented")
    }
}
enum class State {
    PICKING,
    MINING,
    SPINNING,
    TO_SPINNER,
    FIND_BANK,
    RETURN_TO_FLAX,
    BANKING,
    INIT
}

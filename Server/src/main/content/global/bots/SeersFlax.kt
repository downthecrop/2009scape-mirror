package content.global.bots

import content.global.skill.crafting.spinning.SpinningItem
import content.global.skill.crafting.spinning.SpinningPulse
import core.game.bots.*
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import org.rs09.consts.Items

@PlayerCompatible
@ScriptName("Seers' Village Flax Spinning")
@ScriptDescription("Pick flax and spin it into string. Start in the flax field near Seers' Village.")
@ScriptIdentifier("seers_flax")
class SeersFlax : Script(){
    val waypoint1 = Location(2730, 3460, 0) // South of Seers' Village
    val waypoint2 = Location(2737, 3441, 0)  // Outside flax field
    var state = State.PICKING
    var stage = 0
    var doorOpen = false
    override fun tick() {

        when(state){
            State.PICKING -> {
                if (bot.inventory.freeSlots() == 0) {
                    state = State.TO_SPINNER
                    return
                }

                val flax = scriptAPI.getNearestNode("Flax", true)
                scriptAPI.interact(bot, flax, "Pick")
            }

            State.TO_SPINNER -> {
                if(stage == 0)
                    Pathfinder.find(bot, Location.create(2736, 3442, 0)).walk(bot).also { stage++ }
                when(bot.location){
                    Location.create(2736, 3442, 0) -> Pathfinder.find(bot,Location.create(2722, 3456, 0)).walk(bot)
                    Location.create(2722, 3456, 0) -> Pathfinder.find(bot,Location.create(2716, 3472, 0)).walk(bot)
                    Location.create(2716, 3472, 0) -> {
                        val door = scriptAPI.getNearestNode(25819,true)
                        if(door != null && door.location?.withinDistance(bot.location,2)!!){
                            door.interaction?.handle(bot, door.interaction[0])
                            doorOpen = true
                        } else {
                            val ladder = scriptAPI.getNearestNode(25938,true)
                            ladder?.interaction?.handle(bot,ladder.interaction[0])                        }
                    }
                    Location.create(2714, 3470, 1) -> {
                        val spinner = scriptAPI.getNearestNode(25824,true)
                        bot.faceLocation(spinner?.location)
                        bot.pulseManager.run(object: MovementPulse(bot,spinner, DestinationFlag.OBJECT){
                            override fun pulse(): Boolean {
                                bot.faceLocation(spinner?.location)
                                state = State.SPINNING.also { stage = 0; doorOpen = false }
                                return true
                            }
                        })
                    }
                }
            }

            State.SPINNING -> {
                bot.pulseManager.run(SpinningPulse(bot, Item(Items.FLAX_1779),bot.inventory.getAmount(Items.FLAX_1779), SpinningItem.FLAX))
                state = State.FIND_BANK
            }

            State.FIND_BANK -> {
                when(bot.location){
                    Location.create(2711, 3471, 1) -> {
                        val ladder = scriptAPI.getNearestNode(25939,true) ?: return
                        ladder.interaction?.handle(bot,ladder.interaction[0])
                    }
                    Location.create(2714, 3470, 0) -> Pathfinder.find(bot,Location.create(2715, 3472, 0)).walk(bot)
                    Location.create(2715, 3472, 0) -> {
                        val door = scriptAPI.getNearestNode(25819,true)
                        if(door != null && door.location?.withinDistance(bot.location,2)!!){
                            door.interaction?.handle(bot, door.interaction[0])
                            doorOpen = true
                        } else {
                            Pathfinder.find(bot,Location.create(2726, 3481, 0)).walk(bot)
                        }
                    }
                    Location.create(2726, 3481, 0) -> Pathfinder.find(bot,Location.create(2724, 3491, 0)).walk(bot)
                    Location.create(2724, 3491, 0) -> state = State.BANKING
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode(25808,true)
                if(bank != null)
                    bot.pulseManager.run(object: MovementPulse(bot,bank, DestinationFlag.OBJECT){
                        override fun pulse(): Boolean {
                            bot.faceLocation(bank.location)
                            scriptAPI.bankItem(Items.BOW_STRING_1777)
                            if(bot.bank.getAmount(Items.BOW_STRING_1777) > 500){
                                state = State.TELE_GE
                                return true
                            }
                            state = State.RETURN_TO_FLAX
                            return true
                        }
                    })
            }

            State.RETURN_TO_FLAX -> {
                val flax = scriptAPI.getNearestNode("Flax", true)
                if (flax != null) {
                    state = State.PICKING
                } else if (bot.location.withinMaxnormDistance(waypoint1, 5)) {
                    Pathfinder.find(bot, waypoint2).walk(bot)
                } else {
                    Pathfinder.find(bot, waypoint1).walk(bot)
                }
            }

            State.TELE_GE -> {
                scriptAPI.teleportToGE()
                state = State.SELL_GE
            }

            State.SELL_GE -> {
                scriptAPI.sellOnGE(Items.BOW_STRING_1777)
                state = State.TELE_CAMELOT
            }

            State.TELE_CAMELOT -> {
                scriptAPI.teleport(Location.create(2756, 3478, 0))
                state = State.RETURN_TO_FLAX
            }

        }
    }

    enum class State {
        PICKING,
        SPINNING,
        TO_SPINNER,
        FIND_BANK,
        BANKING,
        TELE_GE,
        SELL_GE,
        RETURN_TO_FLAX,
        TELE_CAMELOT
    }

    init {
        skills[Skills.CRAFTING] = 10
    }

    override fun newInstance(): Script {
        val script = SeersFlax()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }
}
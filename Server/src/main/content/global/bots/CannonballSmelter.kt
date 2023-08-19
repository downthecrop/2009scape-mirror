package content.global.bots

import content.global.skill.gather.mining.MiningNode
import content.global.skill.smithing.smelting.Bar
import content.global.skill.smithing.smelting.SmeltingPulse
import core.api.*
import core.game.bots.*
import core.game.ge.GrandExchange
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items

@PlayerCompatible
@ScriptName("Falador Cannonball Smelter")
@ScriptDescription("Start in Falador East Bank with a pick equipped","or in your inventory.")
@ScriptIdentifier("fally_cballs")
class CannonballSmelter : Script() {
    var state = State.INIT
    val bottomLadder = ZoneBorders(3016,9736,3024,9742)
    val topLadder = ZoneBorders(3016,3336,3022,3342)
    val coalMine = ZoneBorders(3027,9733,3054,9743)
    //val coalMine = ZoneBorders(3056, 9729, 3018, 9758)
    val ironMine = ZoneBorders(3052,9768,3035,9777)
    val northMineEntrance = ZoneBorders(3062, 3375, 3058, 3381)
    val bank = ZoneBorders(3009,3355,3018,3358)
    var overlay: ScriptAPI.BottingOverlay? = null
    var coalAmount = 0

    override fun tick() {
        when(state){

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Mining")
                overlay!!.setTaskLabel("Coal Mined:")
                overlay!!.setAmount(0)

                if (coalMine.insideBorder(bot)){
                    state = State.MINING
                } else {
                    state = State.TO_BANK
                }
            }

            State.MINING -> {
                bot.interfaceManager.close()
                if(bot.inventory.freeSlots() == 0){
                    state = State.TO_BANK
                } else if(amountInInventory(bot, Items.COAL_453) >= 18) {
                    state = State.TO_IRONMINE
                } else if(!coalMine.insideBorder(bot)){
                    scriptAPI.walkTo(coalMine.randomLoc)
                } else {
                    val rock = scriptAPI.getNearestObjectByPredicate({node -> node?.name?.equals("rocks", true)!! && MiningNode.forId(node?.id!!).reward == Items.COAL_453 })
                    if(rock != null) {
                        scriptAPI.interact(bot, rock, "mine")
                    } else {
                        scriptAPI.walkTo(coalMine.randomLoc)
                    }
                }
                overlay!!.setAmount(amountInInventory(bot, Items.COAL_453))
            }
            State.MINING_IRON -> {
                bot.interfaceManager.close()
                if(bot.inventory.freeSlots() == 0 || amountInInventory(bot, Items.IRON_ORE_440) >= 9) {
                    state = State.TO_BANK
                } else if(!ironMine.insideBorder(bot)){
                    var loc = ironMine.randomLoc
                    scriptAPI.walkTo(loc)
                } else {
                    val rock = scriptAPI.getNearestObjectByPredicate({node -> node?.name?.equals("rocks", true)!! && MiningNode.forId(node?.id!!).reward == Items.IRON_ORE_440 })
                    //rock?.let { InteractionListeners.run(rock.id, IntType.SCENERY,"mine",bot,rock) }
                    if(rock != null) {
                        scriptAPI.interact(bot, rock, "mine")
                    } else {
                        scriptAPI.walkTo(ironMine.randomLoc)
                    }
                }
                overlay!!.setAmount(amountInInventory(bot, Items.IRON_ORE_440))
            }

            State.TO_BANK -> {
                if(bank.insideBorder(bot)){
                    val bank = scriptAPI.getNearestNode("bank booth",true)
                    if(bank != null) {
                        state = State.BANKING
                        bot.pulseManager.run(object : BankingPulse(this, bank){
                            override fun pulse(): Boolean {
                                return super.pulse()
                            }
                        })
                    }
                } else {
                    if(bot.location.y > 3400) {
                        if(bot.location.y < 9757) {
                            val ladder = scriptAPI.getNearestNode(30941, true)
                            //ladder ?: scriptAPI.walkTo(bottomLadder.randomLoc).also { return }
                            //ladder?.interaction?.handle(bot, ladder.interaction[0]).also { ladderSwitch = true }
                            scriptAPI.interact(bot, ladder, "climb-up")
                        } else {
                            val stairs = scriptAPI.getNearestNode(30943, true)
                            scriptAPI.interact(bot, stairs, "climb-up")

                        }
                    } else {
                        if(northMineEntrance.insideBorder(bot)) {
                            val door = scriptAPI.getNearestNode(11714, true)
                            if(door != null) {
                                scriptAPI.interact(bot, door, "open")
                            } else {
                                scriptAPI.walkTo(bank.randomLoc)
                            }
                        } else {
                            scriptAPI.walkTo(bank.randomLoc)
                        }
                    }
                }
            }

            State.BANKING -> {
                scriptAPI.bankAll({
                    if(amountInBank(bot, Items.CANNONBALL_2) >= 500) {
                        val total = GrandExchange.getBotstockForId(Items.CANNONBALL_2)
                        bot.interfaceManager.close()
                        if(total < 5000) {
                            state = State.TO_GE
                        }
                    }
                    if(state != State.TO_GE) {
                        if(amountInBank(bot, Items.IRON_ORE_440) >= 9 && amountInBank(bot, Items.COAL_453) >= 18) {
                            scriptAPI.withdraw(Items.IRON_ORE_440, 9)
                            scriptAPI.withdraw(Items.COAL_453, 18)
                            scriptAPI.withdraw(Items.AMMO_MOULD_4, 1)
                            state = State.TO_FURNACE
                            bot.interfaceManager.close()
                        } else {
                            state = State.TO_MINE
                        }
                    }
                })
            }

            State.TO_FURNACE -> {
                if(bot.location.x > 2978) {
                    scriptAPI.walkTo(Location.create(2974, 3369, 0))
                } else if(amountInInventory(bot, Items.STEEL_BAR_2353) < 9) {
                    val furnace = scriptAPI.getNearestNode(11666, true)
                    scriptAPI.interact(bot, furnace, "smelt")
                    // TODO: should bots use real interfaces?
                    bot.pulseManager.run(SmeltingPulse(bot, null, Bar.STEEL, 9))
                } else {
                    state = State.SMELTING_CBALLS
                }
            }

            State.SMELTING_CBALLS -> {
                if(amountInInventory(bot, Items.STEEL_BAR_2353) > 0) {
                    val furnace = scriptAPI.getNearestNode(11666, true)
                    scriptAPI.useWith(bot, Items.STEEL_BAR_2353, furnace)
                    if(bot.dialogueInterpreter.dialogue != null) {
                        bot.dialogueInterpreter.handle(309, 32)
                    }
                } else {
                    state = State.TO_BANK
                }
            }

            State.TO_MINE -> {
                if(bot.location.y < 3400) {
                    bot.interfaceManager.close()
                    if(!topLadder.insideBorder(bot.location)){
                        scriptAPI.walkTo(topLadder.randomLoc)
                    } else {
                        val ladder = scriptAPI.getNearestNode("Ladder",true)
                        if(ladder != null){
                            ladder.interaction.handle(bot,ladder.interaction[0])
                        } else {
                            scriptAPI.walkTo(topLadder.randomLoc)
                        }
                    }
                } else {
                    if(!coalMine.insideBorder(bot)){
                        scriptAPI.walkTo(coalMine.randomLoc)
                    } else {
                        state = State.MINING
                    }
                }
            }

            State.TO_IRONMINE -> {
                if(ironMine.insideBorder(bot.location)) {
                    state = State.MINING_IRON
                } else if(bot.location.y < 9757) {
                    if (bot.location.regionId == ((47 shl 8) or 152)) {
                        val door = scriptAPI.getNearestNode(2112,true)
                        if(door != null) {
                            scriptAPI.interact(bot, door, "open")
                        } else {
                            scriptAPI.walkTo(Location.create(3046, 9756, 0))
                        }
                    } else {
                        scriptAPI.walkTo(Location.create(3046, 9756, 0))
                    }
                } else {
                    scriptAPI.walkTo(ironMine.randomLoc)
                }
            }

            State.TO_GE -> {
                scriptAPI.teleportToGE()
                state = State.SELLING
            }

            State.SELLING -> {
                scriptAPI.sellOnGE(Items.CANNONBALL_2)
                state = State.GO_BACK
            }

            State.GO_BACK -> {
                scriptAPI.teleport(bank.randomLoc)
                state = State.TO_MINE
            }
        }
    }

    open class BankingPulse(val script: Script, val bank: Node) : MovementPulse(script.bot,bank, DestinationFlag.OBJECT){
        override fun pulse(): Boolean {
            script.bot.faceLocation(bank.location)
            return true
        }
    }

    override fun newInstance(): Script {
        val script = CannonballSmelter()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }

    enum class State {
        MINING,
        TO_MINE,
        TO_BANK,
        TO_FURNACE,
        SMELTING_CBALLS,
        BANKING,
        TO_GE,
        SELLING,
        GO_BACK,
        TO_IRONMINE,
        MINING_IRON,
        INIT
    }

    init {
        equipment.add(Item(Items.RUNE_PICKAXE_1275))
        inventory.add(Item(Items.AMMO_MOULD_4))
        if(false) {
            // spawn initial iron/coal to debug smelting
            inventory.add(Item(Items.COAL_453, 18))
            inventory.add(Item(Items.IRON_ORE_440, 9))
        }
        skills.put(Skills.ATTACK,40)
        skills.put(Skills.STRENGTH,60)
        skills.put(Skills.MINING,75)
        skills.put(Skills.HITPOINTS,99)
        skills.put(Skills.DEFENCE,99)
        skills.put(Skills.SMITHING,35)
        quests.add("Dwarf Cannon")
    }
}

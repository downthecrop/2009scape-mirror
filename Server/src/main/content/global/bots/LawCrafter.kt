package content.global.bots

import content.global.travel.ship.Ships
import core.cache.def.impl.ItemDefinition
import core.game.bots.*
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items


@PlayerCompatible
@ScriptName("Law Rune Crafter")
@ScriptDescription("Crafts law runes. Start near Draynor bank w/ law tiara.")
@ScriptIdentifier("law_crafter")
class LawCrafter : Script() {
    var state = State.INIT
    var runeCounter = 0
    var overlay: ScriptAPI.BottingOverlay? = null
    var startLocation = Location(0,0,0)
    var timer = 0
    var bank = ZoneBorders(3092, 3242, 3092, 3245)
    var boatNPC = Location(3047, 3234, 0)
    var ruinsZone = ZoneBorders(2850, 3375, 2860, 3382)
    var ruinPoint = Location(2857, 3380, 0)
    var onBoat = ZoneBorders(2824, 3328, 2840, 3333)
    var offBoat = ZoneBorders(2827, 3335, 2836, 3336)
    var lawLocation = Location(2464, 4830, 0)
    var lawZone = ZoneBorders(2439, 4808, 2488, 4855)
    var returnNPC = Location(2835, 3335, 0)
    var halfBank = Location(3069, 3275, 0)

    override fun tick() {
        if (timer-- > 0) {
            return
        }
        if (bot.settings.runEnergy > 10.0) {
            bot.settings.isRunToggled = true
        }

        when(state){
            State.INIT -> {
                if (!bot.equipment.containsAtLeastOneItem(Items.LAW_TIARA_5545) || !ItemDefinition.canEnterEntrana(bot)) {
                    bot.sendMessage("Please equip a law tiara first.")
                    bot.sendMessage("AND REMOVE ALL WEAPONS AND ARMOR.")
                    state = State.INVALID
                } else {
                    overlay = scriptAPI.getOverlay()
                    overlay!!.init()
                    overlay!!.setTitle("Law Runes")
                    overlay!!.setTaskLabel("Runes Crafted:")
                    overlay!!.setAmount(0)
                    startLocation = bot.location
                    state = State.BANKING
                }
            }

            State.BANKING -> {
                endDialogue = true
                bot.interfaceManager.closeChatbox()
                bot.interfaceManager.openChatbox(137)
                bot.interfaceManager.closeChatbox()
                bot.dialogueInterpreter.close()
                if(!bank.insideBorder(bot)) {
                    scriptAPI.walkTo(bank.randomLoc)
                    return
                }
                val runes = bot.inventory.getAmount(Item(Items.LAW_RUNE_563))
                if (runes > 0) {
                    runeCounter += runes
                    overlay!!.setAmount(runeCounter)
                    bot.sendMessage("You have crafted a total of: $runeCounter runes.")
                    scriptAPI.bankItem(Items.LAW_RUNE_563)
                } else {
                    scriptAPI.withdraw(Items.PURE_ESSENCE_7936, 28)
                    state = State.HALF_BANK
                }
            }

            State.TO_BOAT_GUY -> {
                var boatGuy = scriptAPI.getNearestNode(2729, false)
                if (boatGuy != null){
                    if (boatGuy.location.withinDistance(bot.location,2)) {
                        if (ItemDefinition.canEnterEntrana(bot)) {
                            endDialogue = false
                            Ships.PORT_SARIM_TO_ENTRANA.sail(bot)
                            state = State.CROSS_GANGPLANK
                        } else {
                            state = State.INVALID
                        }
                    } else {
                        scriptAPI.walkTo(boatGuy.location)
                    }

                } else {
                    scriptAPI.walkTo(boatNPC)
                }
            }

            State.CROSS_GANGPLANK -> {
                if (onBoat.insideBorder(bot)) {
                    var gangplank = scriptAPI.getNearestNode(2415, true)
                    if (gangplank != null) {
                        scriptAPI.interact(bot, gangplank, "cross")
                    }
                } else if (offBoat.insideBorder(bot)) {
                    state = State.RUNNING_TO_ALTER
                    endDialogue = true
                }
            }

            State.RUNNING_TO_ALTER -> {
                if (lawZone.insideBorder(bot))
                    state = State.CRAFTING

                val ruins = scriptAPI.getNearestNode(2459,true)
                if (!ruinsZone.insideBorder(bot)) {
                    scriptAPI.walkTo(ruinPoint)
                } else if (ruins != null && ruins.location.withinDistance(bot.location, 20)) {
                    val ruinsChild = (ruins as Scenery).getChild(bot)
                    scriptAPI.interact(bot, ruinsChild, "enter")
                    timer = 4
                }
            }

            State.CRAFTING -> {
                if (!lawZone.insideBorder(bot)) {
                    return
                }

                if (bot.location != lawLocation) {
                    scriptAPI.walkTo(lawLocation)
                }

                val alter = scriptAPI.getNearestNode(2485,true)
                scriptAPI.interact(bot, alter, "craft-rune")
                if(bot.inventory.containsAtLeastOneItem(Item(Items.LAW_RUNE_563)))
                    state = State.LEAVING_ALTER
            }

            State.LEAVING_ALTER -> {
                var portalOut = scriptAPI.getNearestNode(2472, true)
                scriptAPI.interact(bot, portalOut, "use")
                if (ruinsZone.insideBorder(bot)) {
                    state = State.RETURN_TO_BOAT_GUY
                    timer = 2
                }
            }

            State.RETURN_TO_BOAT_GUY -> {
                var boatGuy = scriptAPI.getNearestNode(2730, false)
                if (boatGuy != null){
                    if (boatGuy.location.withinDistance(bot.location,2)) {
                        endDialogue = false
                        Ships.ENTRANA_TO_PORT_SARIM.sail(bot)
                        state = State.HALF_BANK
                    } else {
                        scriptAPI.walkTo(boatGuy.location)
                    }

                } else {
                    scriptAPI.walkTo(returnNPC)
                }
            }

            // Splits up the journey into two parts.
            // This is because the dialogue can't be ended until you are safely on the
            // mainland side. But new chunks won't load while dialogue is still displayed.
            State.HALF_BANK -> {
                if (bot.inventory.containsAtLeastOneItem(Items.PURE_ESSENCE_7936)) {
                    if ( (bot.location.x - 2) > halfBank.x) {
                        scriptAPI.walkTo(halfBank)
                    } else {
                        state = State.TO_BOAT_GUY
                    }
                } else {
                    if ( (bot.location.x + 2) < halfBank.x) {
                        scriptAPI.walkTo(halfBank)
                    } else {
                        state = State.BANKING
                    }
                }
            }

            State.INVALID -> {
                timer = 25
                state = State.INIT
            }
        }
    }

    override fun newInstance(): Script {
        return this
    }

    enum class State {
        INIT,
        BANKING,
        TO_BOAT_GUY,
        CROSS_GANGPLANK,
        RUNNING_TO_ALTER,
        CRAFTING,
        LEAVING_ALTER,
        RETURN_TO_BOAT_GUY,
        HALF_BANK,
        INVALID
    }

}

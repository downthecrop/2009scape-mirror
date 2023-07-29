package content.global.bots

import content.global.skill.runecrafting.MysteriousRuin
import core.cache.def.impl.SceneryDefinition
import core.game.bots.*
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.zone.ZoneBorders
import core.net.packet.PacketProcessor
import org.rs09.consts.Items


@PlayerCompatible
@ScriptName("Cosmic Rune Crafter")
@ScriptDescription("Crafts cosmic runes. Start in Zanaris w/ cosmic tiara.")
@ScriptIdentifier("cosmic_crafter")
class CosmicCrafter : Script() {
    var state = State.INIT
    var runeCounter = 0
    var overlay: ScriptAPI.BottingOverlay? = null
    var startLocation = Location(0,0,0)
    var timer = 0
    var bank_deposit = true
    var bank = Location(2384, 4457)
    var squeezeZone = ZoneBorders(2413, 4400, 2416, 4406)
    var endZone = ZoneBorders(2405, 4392, 2410, 4397)
    var agility_part = 0


    var ruinsZone = ZoneBorders(2400, 4370, 2410, 4385)
    var cosmicZone = ZoneBorders(2160, 4830, 2170, 4840)

    var ruinPoint = Location(2407, 4379, 0)
    var agility2ExitPoint = Location(2408, 4394, 0)
    var agility2EntryPoint = Location(2408, 4396, 0)
    var agility1ExitPoint = Location(2415, 4401, 0)
    var agility1EntryPoint = Location(2415, 4403, 0)

    override fun tick() {
        if (timer-- > 0) {
            return
        }
        if (bot.settings.runEnergy > 10.0) {
            bot.settings.isRunToggled = true
        }

        when(state){
            State.INIT -> {
                if (!bot.equipment.containsAtLeastOneItem(Items.COSMIC_TIARA_5539)) {
                    bot.sendMessage("Please equip a cosmic tiara first.")
                    state = State.INVALID
                } else {
                    overlay = scriptAPI.getOverlay()
                    overlay!!.init()
                    overlay!!.setTitle("Cosmic Runes")
                    overlay!!.setTaskLabel("Runes Crafted:")
                    overlay!!.setAmount(0)
                    startLocation = bot.location
                    state = State.BANKING
                }
            }

            State.BANKING -> {
                if(bot.location != bank) {
                    scriptAPI.walkTo(bank)
                    return
                }
                val runes = bot.inventory.getAmount(Item(Items.COSMIC_RUNE_564))
                if (runes > 0) {
                    runeCounter += runes
                    overlay!!.setAmount(runeCounter)
                    bot.sendMessage("You have crafted a total of: $runeCounter runes.")
                    scriptAPI.bankItem(Items.COSMIC_RUNE_564)
                } else {
                    scriptAPI.withdraw(Items.PURE_ESSENCE_7936, 28)
                    state = State.FIRST_AGILITY
                }
            }

            State.FIRST_AGILITY -> {
                val squeezeWall = scriptAPI.getNearestNode(12127, true)
                if (agility_part == 0) {
                    if (!squeezeZone.insideBorder(bot))
                        scriptAPI.walkTo(agility1EntryPoint)
                    else {
                        scriptAPI.interact(bot, squeezeWall, "squeeze-past")
                        agility_part = 1
                        timer = 6
                    }
                } else if (agility_part == 1) {
                    if (!endZone.insideBorder(bot))
                        scriptAPI.walkTo(agility2EntryPoint)
                    else {
                        scriptAPI.interact(bot, squeezeWall, "squeeze-past")
                        state = State.RUNNING_TO_ALTER
                        timer = 6
                    }
                }
            }

            State.RUNNING_TO_ALTER -> {
                if (cosmicZone.insideBorder(bot))
                    state = State.CRAFTING

                val ruins = scriptAPI.getNearestNode(2458,true)
                if (!ruinsZone.insideBorder(bot)) {
                    scriptAPI.walkTo(ruinPoint)
                } else if (ruins != null && ruins.location.withinDistance(bot.location, 20)) {
                    if (!bot.equipment.containsAtLeastOneItem(Items.COSMIC_TIARA_5539)) {
                        bot.sendMessage("Please equip a cosmic tiara first.")
                        state = State.INVALID
                    } else {
                        val ruinsChild = (ruins as Scenery).getChild(bot)
                        scriptAPI.interact(bot, ruinsChild, "enter")
                        timer = 4
                    }
                }
            }

            State.CRAFTING -> {
                val alter = scriptAPI.getNearestNode(2484,true)
                scriptAPI.interact(bot, alter, "craft-rune")
                if(bot.inventory.containsAtLeastOneItem(Item(Items.COSMIC_RUNE_564)))
                    state = State.LEAVING_ALTER
            }

            State.LEAVING_ALTER -> {
                var portalOut = scriptAPI.getNearestNode(2471, true)
                scriptAPI.interact(bot, portalOut, "use")
                if (ruinsZone.insideBorder(bot)) {
                    state = State.RETURN_AGILITY
                    timer = 2
                }
            }

            State.RETURN_AGILITY -> {
                val squeezeWall = scriptAPI.getNearestNode(12127, true)
                if (agility_part == 1) {
                    if (!endZone.insideBorder(bot))
                        scriptAPI.walkTo(agility2ExitPoint)
                    else {
                        scriptAPI.interact(bot, squeezeWall, "squeeze-past")
                        agility_part = 0
                        timer = 6
                    }
                } else if (agility_part == 0) {
                    if (!squeezeZone.insideBorder(bot))
                        scriptAPI.walkTo(agility1ExitPoint)
                    else {
                        scriptAPI.interact(bot, squeezeWall, "squeeze-past")
                        state = State.BANKING
                        timer = 6
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
        FIRST_AGILITY,
        RETURN_AGILITY,
        RUNNING_TO_ALTER,
        CRAFTING,
        LEAVING_ALTER,
        INVALID
    }

}

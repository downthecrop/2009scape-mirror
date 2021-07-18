package rs09.game.ai.general.scriptrepository

import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import rs09.game.ai.general.ScriptAPI

@PlayerCompatible
@ScriptName("Chicken Killer")
@ScriptDescription("Kills chickens and loots feathers. Start in any chicken area.")
@ScriptIdentifier("chicken_killer")
class ChickenKiller : Script(){
    var state = State.INIT
    var chickenCounter = 0
    var overlay: ScriptAPI.BottingOverlay? = null
    var startLocation = Location(0,0,0)
    var timer = 3
    var lootFeathers = false
    var featherNearby = false
    var currentFeathers = 0
    val chickenPen = ZoneBorders(3231,3300,3235,3287)

    override fun tick() {
        when(state){
            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Chickens")
                overlay!!.setTaskLabel("Chickens KO'd:")
                overlay!!.setAmount(0)
                state = State.CONFIG
                bot.dialogueInterpreter.sendOptions("Loot Feathers and bury bones?","Yes","No")
                bot.dialogueInterpreter.addAction{player,button ->
                    lootFeathers = button == 2
                    state = State.KILLING
                }
                startLocation = bot.location
            }

            State.KILLING -> {
                val chicken = scriptAPI.getNearestNode("Chicken")
                if(chicken == null){
                    scriptAPI.randomWalkTo(startLocation,3)
                } else {
                    scriptAPI.attackNpcInRadius(bot,"Chicken",10)
                    if(lootFeathers) {
                        state = State.IDLE
                        timer = 4
                    }
                    chickenCounter++
                    overlay!!.setAmount(chickenCounter)
                }
            }

            State.IDLE -> {
                if(timer-- <= 0){
                    featherNearby = scriptAPI.takeNearestGroundItem(Items.FEATHER_314)
                    currentFeathers = 0
                    if (featherNearby) {
                        state = State.LOOTFEATHER
                    }else{
                        state = State.LOOTBONES
                    }
                }
            }

            State.LOOTFEATHER -> {
                timer = 1
                if(timer-- >= 0) {
                    currentFeathers = bot.inventory.getAmount(Items.FEATHER_314)
                    scriptAPI.takeNearestGroundItem(Items.FEATHER_314)
                    featherNearby = false
                }
                state = State.LOOTBONES
            }

            State.LOOTBONES -> {
                timer = 1
                if(timer-- >= 0){
                    scriptAPI.takeNearestGroundItem(Items.BONES_526)
                }
                state = State.BURYBONES
            }

            State.BURYBONES -> {
                timer = 1
                var hasBone = bot.hasItem(Item(Items.BONES_526))
                var bone = bot.inventory.getItem(Item(Items.BONES_526))
                if (hasBone) {
                    bone.interaction.handleItemOption(bot,bone.interaction.get(0),bot.inventory)
                }
                state = State.KILLING
            }
        }
    }

    override fun newInstance(): Script {
        return this
    }

    enum class State {
        IDLE,
        INIT,
        KILLING,
        LOOTING,
        RETURN,
        CONFIG,
        LOOTFEATHER,
        LOOTBONES,
        BURYBONES
    }
}
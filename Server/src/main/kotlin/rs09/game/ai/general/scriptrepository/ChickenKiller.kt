package rs09.game.ai.general.scriptrepository

import core.game.world.map.Location
import org.rs09.consts.Items
import rs09.game.ai.general.ScriptAPI

@PlayerCompatible
@ScriptName("Chicken Killer")
@ScriptDescription("Kills chickens and loots feathers. Start in any chicken area.")
@ScriptIdentifier("chicken_killer")
class ChickenKiller : Script(){
    var state = State.INIT
    var chickenCounter = 0
    var overlay: ScriptAPI.BottingOverlay?= null
    var startLocation = Location(0,0,0)
    var timer = 3
    var lootFeathers = false

    override fun tick() {
        when(state){

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Chickens")
                overlay!!.setTaskLabel("Chickens KO'd:")
                overlay!!.setAmount(0)
                state = State.CONFIG
                bot.dialogueInterpreter.sendOptions("Loot Feathers?","Yes","No")
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
                    state = State.LOOTING
                }
            }

            State.LOOTING -> {
                scriptAPI.takeNearestGroundItem(Items.FEATHER_314)
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
        CONFIG
    }
}
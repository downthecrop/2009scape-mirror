package rs09.game.ai.general

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import core.game.world.map.Location
import core.tools.RandomFunction
import rs09.game.ai.AIPBuilder
import rs09.game.ai.AIPlayer
import rs09.game.ai.AIRepository
import rs09.game.ai.general.scriptrepository.Idler
import rs09.game.ai.general.scriptrepository.Script

class GeneralBotCreator {
    //org/crandor/net/packet/in/InteractionPacket.java <<< This is a very useful class for learning to code bots
    constructor(loc: Location?, botScript: Script) {
        botScript.bot = AIPBuilder.create(loc)
        GameWorld.Pulser.submit(BotScriptPulse(botScript))
    }

    constructor(botScript: Script, bot: AIPlayer?) {
        botScript.bot = bot
        GameWorld.Pulser.submit(BotScriptPulse(botScript).also { AIRepository.PulseRepository.add(it) })
    }

    constructor(botScript: Script, player: Player, isPlayer: Boolean){
        botScript.bot = player
        val pulse = BotScriptPulse(botScript,isPlayer)
        GameWorld.Pulser.submit(pulse)
        player.setAttribute("/save:not_on_highscores",true)
        player.setAttribute("botting:script",pulse)
    }

    inner class BotScriptPulse(public val botScript: Script, val isPlayer: Boolean = false) : Pulse(1){
        var ticks = 0
        init {
            botScript.init(isPlayer)
        }
        var randomDelay = 0

        override fun pulse(): Boolean {
            if(randomDelay > 0){
                randomDelay -= 1
                return false
            }
            if (!botScript.bot.pulseManager.hasPulseRunning()) {

                /*if (ticks++ >= RandomFunction.random(90000,120000)) {
                    AIPlayer.deregister(botScript.bot.uid)
                    ticks = 0
                    SystemLogger.log("Submitting transition pulse from ticks")
                    GameWorld.Pulser.submit(TransitionPulse(botScript))
                    return true
                }*/
                if(!botScript.running) return true //has to be separated this way or it double-submits the respawn pulse.


                val idleRoll = RandomFunction.random(50)
                if(idleRoll == 2 && botScript !is Idler){
                    randomDelay += RandomFunction.random(2,20)
                    return false
                }
                botScript.tick()
            }
            return false
        }

        override fun stop() {
            ticks = Integer.MAX_VALUE - 20 //Sets the ticks as high as they can go (safely) and then runs pulse again
            pulse()                        //to trigger the transition pulse to be submitted.
            super.stop()
            AIRepository.PulseRepository.remove(this)
        }
    }

    inner class TransitionPulse(val script: Script) : Pulse(RandomFunction.random(60,200)){
        override fun pulse(): Boolean {
            GameWorld.Pulser.submit(BotScriptPulse(script.newInstance()).also { AIRepository.PulseRepository.add(it) })
            return true
        }
    }
}
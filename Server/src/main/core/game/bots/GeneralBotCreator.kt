package core.game.bots

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.RandomFunction
import core.Server
import content.global.bots.Idler
import core.api.*
import core.game.interaction.MovementPulse
import core.tools.Log

class GBCTick : TickListener {
    override fun tick() {
        GeneralBotCreator.botPulsesTriggeredThisTick = 0
    }
}

class GeneralBotCreator {
    //org/crandor/net/packet/in/InteractionPacket.java <<< This is a very useful class for learning to code bots
    constructor(botScript: Script, loc: Location?) {
        botScript.bot = AIPBuilder.create(loc)
        GameWorld.Pulser.submit(BotScriptPulse(botScript))
    }

    constructor(botScript: Script, bot: AIPlayer?) {
        botScript.bot = bot
        GameWorld.Pulser.submit(BotScriptPulse(botScript).also { AIRepository.PulseRepository[it.botScript.bot.username.lowercase()] = it })
    }

    constructor(botScript: Script, player: Player, isPlayer: Boolean){
        botScript.bot = player
        val pulse = BotScriptPulse(botScript,isPlayer)
        GameWorld.Pulser.submit(pulse)
        player.setAttribute("/save:not_on_highscores",true)
        player.setAttribute("botting:script",pulse)
    }

    companion object {
        var botPulsesTriggeredThisTick = 0
    }

    inner class BotScriptPulse(public val botScript: Script, val isPlayer: Boolean = false) : Pulse(1) {
        var ticks = 0
        init {
            botScript.init(isPlayer)
        }
        var randomDelay = 0
        var lastBotLocation: Location = botScript.bot.location.transform(0,0,0)
        var lastBotMoveTicks = getWorldTicks()
        override fun pulse(): Boolean {
            if(randomDelay > 0){
                randomDelay -= 1
                return false
            }
            if (botScript.bot.pulseManager.hasPulseRunning()) {
                if (botScript.bot.pulseManager.current is MovementPulse) {
                    if (botScript.bot.location != lastBotLocation) {
                        lastBotLocation = botScript.bot.location.transform(0,0,0)
                        lastBotMoveTicks = getWorldTicks()
                    }
                    if (lastBotLocation == botScript.bot.location && getWorldTicks() - lastBotMoveTicks > 5) {
                        botScript.bot.pulseManager.current.stop()
                    }
                }
            }

            /*
             * Chatboxes and interfaces will cause the authentic interaction subsystem
             * to pause any currently running authentically-implemented interactions.
             *
             * When this happens, if the interfaces are not handled by the script and closed,
             * execution will remain paused as the game believes the bot is still doing something
             * (because they still have an authentic interaction in the queue, that is not advancing
             * because it is paused) and so the script does not execute, but the pulse is waiting on botscript input.
             *
             * This deadlock can be worked around by just closing these.
             *
             * Set endDialogue to FALSE if you want
             * to avoid automatic dialogue termination (useful for, for example, boat travel)
             */
            if (botScript.bot.scripts.getActiveScript() != null && botScript.bot.hasModalOpen() && botScript.endDialogue) {
                botScript.bot.interfaceManager.closeChatbox()
                botScript.bot.interfaceManager.openChatbox(137)
                botScript.bot.interfaceManager.close()
                botScript.bot.dialogueInterpreter.close()
            }

            if (!botScript.bot.pulseManager.hasPulseRunning() && botScript.bot.scripts.getActiveScript() == null) {

                /*if (ticks++ >= RandomFunction.random(90000,120000)) {
                    AIPlayer.deregister(botScript.bot.uid)
                    ticks = 0
                    SystemLogger.log("Submitting transition pulse from ticks")
                    GameWorld.Pulser.submit(TransitionPulse(botScript))
                    return true
                }*/
                if(!botScript.running) return true //has to be separated this way or it double-submits the respawn pulse.

                if (botPulsesTriggeredThisTick++ >= 75)
                    return false

                val idleRoll = RandomFunction.random(10)
                if(idleRoll == 2 && botScript !is Idler){
                    randomDelay += RandomFunction.random(20,50)
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
            if (Server.running) AIRepository.PulseRepository.remove(this.botScript.bot.username.lowercase())
        }
    }

    inner class TransitionPulse(val script: Script) : Pulse(RandomFunction.random(60,200)){
        override fun pulse(): Boolean {
            // This does not get called and should be removed
            GameWorld.Pulser.submit(BotScriptPulse(script.newInstance()).also { AIRepository.PulseRepository[it.botScript.bot.username.lowercase()] = it })
            return true
        }
    }
}

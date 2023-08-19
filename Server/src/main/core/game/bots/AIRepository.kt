package core.game.bots

import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.ge.GrandExchangeOffer
import content.global.bots.*

/**
 * A repository for bots to make use of that can contain any info that might be useful to them.
 * @author Ceikry
 */
class AIRepository {
    companion object {
        val groundItems = HashMap<Player,ArrayList<GroundItem>>()
        val GEOffers = HashMap<Player, GrandExchangeOffer>()

        @JvmStatic
        val PulseRepository = HashMap<String, GeneralBotCreator.BotScriptPulse>() // Lowercase username, pulse

        @JvmStatic
        fun addItem(item: GroundItem){
            if(groundItems[item.dropper] == null){
                val list = ArrayList<GroundItem>()
                list.add(item)
                groundItems[item.dropper] = list
                return
            }
            groundItems[item.dropper]!!.add(item)
        }

        @JvmStatic
        fun getItems(player: Player): ArrayList<GroundItem>?{
            return groundItems[player]
        }

        @JvmStatic
        fun addOffer(player: Player, offer: GrandExchangeOffer){
            GEOffers[player] = offer
        }

        @JvmStatic
        fun getOffer(player: Player): GrandExchangeOffer? {
            return GEOffers[player]
        }

        @JvmStatic fun clearAllBots() {
            PulseRepository.toList().forEach { (_, it) ->
                it.stop()
                it.botScript.bot.clear()
                AIPlayer.deregister((it.botScript.bot as AIPlayer).uid)
            }
        }

        @JvmStatic fun sendBotInfo (player: Player, bot: AIPlayer) {
            val pulse = PulseRepository[bot.username.lowercase()] ?: return
            bot.setAttribute("tracked", true) 
            player.debug("[Bot: ${bot.username}][${pulse.botScript::class.simpleName}]=================")
            player.debug("PM Pulse Running? ${if(bot.pulseManager.hasPulseRunning()) bot.pulseManager.getCurrent()::class.java.name else "No"}")
            player.debug("Interaction Running? ${bot.scripts.getActiveScript() != null} ${bot.scripts.getActiveScript()?.let { " : " + it.execution!!::class.java.name } ?: ""}")
            player.debug("Botscript Running? ${pulse.botScript.running}")
            player.debug("Random Delay? ${pulse.randomDelay}")
            player.debug("Delayed? ${bot.scripts.delay}")
            if (pulse.botScript is Adventurer) {
                player.debug("State: ${pulse.botScript.state.name}")
            }
            player.debug("==========================================")
        }
    }
}

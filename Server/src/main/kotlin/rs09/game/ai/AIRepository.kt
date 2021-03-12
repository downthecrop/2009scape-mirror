package rs09.game.ai

import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.ge.GrandExchangeOffer

/**
 * A repository for bots to make use of that can contain any info that might be useful to them.
 * @author Ceikry
 */
class AIRepository {
    companion object {
        val groundItems = HashMap<Player,ArrayList<GroundItem>>()
        val GEOffers = HashMap<Player, GrandExchangeOffer>()

        @JvmStatic
        val PulseRepository = ArrayList<GeneralBotCreator.BotScriptPulse>()

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
    }
}
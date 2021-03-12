package rs09.game.ai.skillingbot

import core.game.node.item.Item
import core.game.world.map.Location
import rs09.game.ai.AIPlayer

class SkillingBotAssembler {
    fun produce(type: Wealth,loc: Location): AIPlayer{
        return assembleBot(AIPlayer(loc),type)
    }

    fun assembleBot(bot: AIPlayer, type: Wealth): AIPlayer{
        return when(type){
            Wealth.POOR -> equipSet(bot,POORSETS.random())
            Wealth.AVERAGE -> equipSet(bot,AVGSETS.random())
            Wealth.RICH -> equipSet(bot,RICHSETS.random())
        }
    }

    fun equipSet(bot: AIPlayer,set: List<Int>): AIPlayer{
        for(i in set){
            val item = Item(i)
            val configs = item.definition.handlers
            val slot = configs["equipment_slot"] ?: continue
            bot.equipment.add(item, slot as Int,
                    false,false)
            val reqs = configs["requirements"]
            if(reqs != null)
                for(req in configs["requirements"] as HashMap<Int,Int>)
                    bot.skills.setStaticLevel(req.key, req.value)
        }
        bot.skills.updateCombatLevel()
        return bot
    }

    enum class Wealth {
        POOR,
        AVERAGE,
        RICH
    }

    val POORSETS = arrayOf(listOf(542,544), listOf(581), listOf(6654,6655,6656), listOf(6654,6656), listOf(636,646), listOf(638,648), listOf(), listOf(), listOf())
    val AVGSETS = arrayOf(listOf(2649,342,344), listOf(2651,542,544), listOf(6654,6655,6656), listOf(6139,6141), listOf(9923,9924,9925), listOf(10400,10402,2649), listOf(10404,10406), listOf(12971,12978))
    val RICHSETS = arrayOf(listOf(10330,10332,2649), listOf(12873,12880,1046), listOf(13858,13861,13864), listOf(13887,13893), listOf(3481,3483), listOf(2653,2655), listOf(2661,2663), listOf(2591,2593), listOf(14490,14492))
}
package content.global.skill.summoning.pet

import core.api.*
import core.tools.*
import core.game.system.timer.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import java.util.*
import org.json.simple.*

class IncubatorTimer : PersistTimer (500, "incubation") {
    val incubatingEggs = HashMap<Int, IncubatingEgg>()

    override fun getInitialRunDelay() : Int {
        return 50
    }

    override fun parse (root: JSONObject, entity: Entity) {
        val eggs = root["eggs"] as? JSONArray ?: return
        for (eggData in eggs) {
            val eggInfo = eggData as JSONArray
            val egg = IncubatingEgg (
                eggInfo[0].toString().toInt(),
                IncubatorEgg.values()[eggInfo[1].toString().toInt()],
                eggInfo[2].toString().toLong(),
                eggInfo[3].toString().toBoolean()
            )
            incubatingEggs[egg.region] = egg
        }
    }

    override fun save (root: JSONObject, entity: Entity) {
        val arr = JSONArray()
        for ((_, eggInfo) in incubatingEggs) {
            val eggArr = JSONArray()
            eggArr.add(eggInfo.region.toString())
            eggArr.add(eggInfo.egg.ordinal.toString())
            eggArr.add(eggInfo.endTime.toString())
            eggArr.add(eggInfo.finished)
            arr.add(eggArr)
        }
        root["eggs"] = arr
    }

    override fun run (entity: Entity) : Boolean {
        if (entity !is Player) return false
        for ((_, egg) in incubatingEggs) {
            if (egg.finished) continue
            if (egg.isDone()) {
                sendMessage(entity, colorize("%RYour ${egg.egg.product.name.lowercase()} egg has finished hatching."))
                egg.finished = true
            }
        }
        return !incubatingEggs.isEmpty()
    }

    data class IncubatingEgg (val region: Int, val egg: IncubatorEgg, var endTime: Long, var finished: Boolean = false)
    fun IncubatingEgg.isDone() : Boolean {
        return endTime < System.currentTimeMillis()
    }

    companion object {
        val TAVERLY_REGION = 11573
        val TAVERLY_VARBIT = 4277
        val YANILLE_REGION = 10288
        val YANILLE_VARBIT = 4221

        fun varbitForRegion (region: Int) : Int {
            return when (region) {
                TAVERLY_REGION -> TAVERLY_VARBIT
                YANILLE_REGION -> YANILLE_VARBIT
                else -> -1
            }
        }

        fun getEggFor (player: Player, region: Int) : IncubatingEgg? {
            val playerTimer = getTimer<IncubatorTimer>(player) ?: return null
            return playerTimer.incubatingEggs[region]
        }

        fun registerEgg (player: Player, region: Int, egg: IncubatorEgg) {
            val timer = getTimer<IncubatorTimer>(player) ?: IncubatorTimer()
            timer.incubatingEggs [region] = IncubatingEgg (region, egg, System.currentTimeMillis() + (ticksToSeconds(egg.inucbationTime * 100) * 1000))

            if (!hasTimerActive<IncubatorTimer>(player))
                registerTimer(player, timer)
            setVarbit(player, varbitForRegion(region), 1, true)
        }

        fun removeEgg (player: Player, region: Int) : IncubatorEgg? {
            val egg = getEggFor (player, region) ?: return null
            val timer = getTimer<IncubatorTimer>(player) ?: return null
            timer.incubatingEggs.remove(region)
            setVarbit(player, varbitForRegion(region), 0, true)
            return egg.egg
        }
    }
}

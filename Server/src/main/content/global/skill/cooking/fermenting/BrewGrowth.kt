package content.global.skill.cooking.fermenting

import core.api.log
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.PersistTimer
import core.tools.Log
import core.tools.secondsToTicks
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * The timer for regular brewing operations
 */

const val brewCycleTime = 6 /*hours*/ * 60 /*minutes per hour*/ * 100 /*ticks per minute*/

class BrewGrowth : PersistTimer(brewCycleTime, "cooking:brewing", isSoft = true) {
    private val vatMap = HashMap<Int, Vat>()
    lateinit var player: Player

    override fun onRegister(entity: Entity) {
        player = (entity as? Player)!!
        for (vat in vatMap.values.toList()){
            catchUp(vat.nextBrew)
        }
    }

    override fun save(root: JSONObject, entity: Entity) {
        player = (entity as? Player)!!
        val vats = JSONArray()
        for ((key, vat) in vatMap.entries.toList()){
            val isBrewingStage = vat.stage in listOf(
                BrewingStage.MIXED,
                BrewingStage.BREWING1,
                BrewingStage.BREWING2,
                BrewingStage.DONE,
                BrewingStage.MATURE,
                BrewingStage.BAD
            )

            if (isBrewingStage) {
                val v = JSONObject()
                v["brew-ordinal"] = key
                v["brew-stuff"] = vat.theStuff
                v["brewing-nextBrew"] = vat.nextBrew
                v["brewing-stage"] = vat.stage.ordinal
                v["brewing-item"] = vat.brewingItem?.ordinal
                vats.add(v)
            }
        }
        root["brewing"] = vats
    }

    override fun parse(root: JSONObject, entity: Entity) {
        player = (entity as? Player)!!
        val data = root["brewing"] as JSONArray
        for (d in data){
            val v = d as JSONObject
            val vatOrdinal = v["brew-ordinal"].toString().toInt()
            val vatStuff = v["brew-stuff"].toString().toBoolean()
            val vatNextBrew = v["brewing-nextBrew"].toString().toLong()
            val vatStage = BrewingStage.values()[v["brewing-stage"].toString().toInt()]
            val vatItem = v["brewing-item"]?.toString()?.toIntOrNull()?.let {
                Brewable.values().getOrNull(it)
            }

            if (vatMap[vatOrdinal] == null){
                val brewingVat = BrewingVat.values()[vatOrdinal]
                val vat = Vat(player, brewingVat, vatStuff, vatNextBrew, vatStage, vatItem)
                vatMap[vatOrdinal] = vat
                vat.updateVat()
            }
        }
    }

    fun getVat(brewingVat: BrewingVat, addVat: Boolean = true) : Vat {
        return vatMap[brewingVat.ordinal] ?: (Vat(player, brewingVat).also { if (addVat) vatMap[brewingVat.ordinal] = it })
    }

    private fun catchUp(timeTillGrow: Long){
        if (timeTillGrow < System.currentTimeMillis()){
            val seconds = (System.currentTimeMillis() - timeTillGrow)/1000
            if (seconds > Int.MAX_VALUE){
                execute(1024)
            }
            else{
                val cyclesToGrow = secondsToTicks(seconds.toInt()) / brewCycleTime
                execute(cyclesToGrow)
            }
        }

    }

    override fun run(entity: Entity): Boolean {
        player = (entity as? Player)!!
        return execute(0)
    }

    private fun execute(count: Int) : Boolean {
        for (vat in vatMap.entries.toList()){
            if (vat.value.canBrew())
                for (i in 0..count){
                    val resp = vat.value.brew()
                    if (!resp){
                        break
                    }
                }
            else {
                vatMap.entries.removeIf { it.value.isVatEmpty() && it.value.isBarrelEmpty()}
            }
        }
        return vatMap.isNotEmpty()
    }

    fun getVats(): MutableCollection<Vat>{
        return vatMap.values
    }
}
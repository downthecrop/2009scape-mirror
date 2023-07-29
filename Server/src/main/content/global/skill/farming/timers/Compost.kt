package content.global.skill.farming.timers

import core.api.*
import core.game.system.timer.*
import org.json.simple.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import content.global.skill.farming.*

class Compost : PersistTimer (500, "farming:compost", isSoft = true) { 
    private val binMap = HashMap<CompostBins, CompostBin>()
    lateinit var player: Player

    override fun onRegister (entity: Entity) {
        player = (entity as? Player)!!
    }

    override fun getInitialRunDelay() : Int {
        return 1 //run once immediately after log in to complete any pending-but-enough-time-has-passed bins.
    }

    override fun run (entity: Entity) : Boolean {
        val removeList = ArrayList <CompostBins> ()
        for((cBin,bin) in binMap){
            if(bin.isReady() && !bin.isFinished){
                bin.finish()
            }
            else if (bin.isDefaultState())
                removeList.add(cBin)
        }
        removeList.forEach { binMap.remove(it) }
        removeList.clear()

        return binMap.isNotEmpty()
    }

    fun getBin (bin: CompostBins) : CompostBin{
        return binMap[bin] ?: (CompostBin (player, bin).also { binMap[bin] = it })
    }

    fun getBins(): MutableCollection<CompostBin>{
        return binMap.values
    }

    override fun save (root: JSONObject, entity: Entity) {
        val bins = JSONArray()
        for((key,bin) in binMap){
            val b = JSONObject()
            b.put("bin-ordinal",key.ordinal)
            bin.save(b)
            bins.add(b)
        }
        root.put("bins", bins)
    }

    override fun parse (root: JSONObject, entity: Entity) {
        (root["bins"] as JSONArray).forEach {
            val bin = it as JSONObject
            val binOrdinal = bin["bin-ordinal"].toString().toInt()
            val cBin = CompostBins.values()[binOrdinal]
            val b = CompostBin ((entity as? Player)!!, cBin).also { binMap[cBin] = it }
            b.parse(bin["binData"] as JSONObject)
        }
    }
}

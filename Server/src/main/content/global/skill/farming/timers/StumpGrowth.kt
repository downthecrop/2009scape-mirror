package content.global.skill.farming.timers

import core.game.node.entity.Entity
import core.game.system.timer.*
import core.game.node.entity.player.Player
import content.global.skill.farming.*
import core.tools.ticksToSeconds
import java.util.concurrent.TimeUnit
import org.json.simple.*

class StumpGrowth : PersistTimer (1, "farming:stump", isSoft = true) {
    val stumps = ArrayList<Stump>()
    lateinit var player: Player

    fun addStump(varbit: Int, ttl: Int){
        stumps.add(
            Stump(
                varbit,
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ticksToSeconds(ttl).toLong())
            )
        )
    }

    override fun onRegister (entity: Entity) {
        player = (entity as? Player)!!
    }

    override fun run (entity: Entity) : Boolean {
        val removeList = ArrayList<Stump>()
        for (stump in stumps) {
            if (System.currentTimeMillis() > stump.TTL) {
                FarmingPatch.patches[stump.varbit]?.getPatchFor(player)?.regrowIfTreeStump()
                removeList.add(stump)
            }
        }
        stumps.removeAll(removeList)
        return stumps.isNotEmpty()
    }

    override fun save(root: JSONObject, entity: Entity) {
        val stumpArray = JSONArray()
        for(s in stumps){
            val seed = JSONObject()
            seed["patch"] = s.varbit
            seed["ttl"] = s.TTL
            stumpArray.add(seed)
        }
        root.put("stumps",stumpArray)
    }

    override fun parse(root: JSONObject, entity: Entity) {
        (root["stumps"] as JSONArray).forEach {
            val s = it as JSONObject
            val id = s["patch"].toString().toInt()
            val ttl = s["ttl"].toString().toLong()
            stumps.add(Stump(id,ttl))
        }
    }
}

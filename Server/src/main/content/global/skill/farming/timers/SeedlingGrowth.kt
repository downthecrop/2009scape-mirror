package content.global.skill.farming.timers

import core.game.node.entity.Entity
import core.game.node.item.Item
import core.game.system.timer.*
import core.game.node.entity.player.Player
import content.global.skill.farming.*
import java.util.concurrent.TimeUnit
import org.json.simple.*
import java.time.*

class SeedlingGrowth : PersistTimer (1, "farming:seedling", isSoft = true) { 
    val seedlings = ArrayList<Seedling>()
    lateinit var player: Player

    fun addSeedling(seedling: Int){
        seedlings.add(
            Seedling(
                seedling, 
                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5),
                seedling + if(seedling > 5400) 8 else 6
            )
        )
    }

    override fun onRegister (entity: Entity) {
        player = (entity as? Player)!!
    }

    override fun run (entity: Entity) : Boolean {
        val removeList = ArrayList<Seedling>()
        for (seed in seedlings) {
            if (System.currentTimeMillis() > seed.TTL) {
                val inInventory = player.inventory.get(Item(seed.id))
                if (inInventory != null) {
                    player.inventory.replace(Item(seed.sapling), inInventory.slot)
                    removeList.add(seed)
                } else {
                    val inBank = player.bank.get(Item(seed.id))
                    if(inBank == null) removeList.add(seed)
                    else {
                        player.bank.remove(Item(inBank.id,1))
                        player.bank.add(Item(seed.sapling))
                        removeList.add(seed)
                    }
                }
            }
        }
        seedlings.removeAll(removeList)
        return seedlings.isNotEmpty()
    }

    override fun save(root: JSONObject, entity: Entity) {
        val seedArray = JSONArray()
        for(s in seedlings){
            val seed = JSONObject()
            seed.put("id",s.id)
            seed.put("ttl",s.TTL)
            seed.put("sapling",s.sapling)
            seedArray.add(seed)
        }
        root.put("seedlings",seedArray)
    }

    override fun parse(root: JSONObject, entity: Entity) {
        (root["seedlings"] as JSONArray).forEach {
            val s = it as JSONObject
            val id = s["id"].toString().toInt()
            val ttl = s["ttl"].toString().toLong()
            val sapling = s["sapling"].toString().toInt()
            seedlings.add(Seedling(id,ttl,sapling))
        }
    }
}

package rs09.game.node.entity.state.newsys.states

import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import rs09.game.node.entity.skill.farming.Seedling
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import java.util.concurrent.TimeUnit

@PlayerState("seedling")
class SeedlingState(player: Player? = null) : State(player) {
    val seedlings = ArrayList<Seedling>()

    fun addSeedling(seedling: Int){
        seedlings.add(Seedling(seedling, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5),seedling + if(seedling > 5400) 8 else 6))
    }

    override fun save(root: JSONObject) {
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

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("seedlings")){
            (_data["seedlings"] as JSONArray).forEach {
                val s = it as JSONObject
                val id = s["id"].toString().toInt()
                val ttl = s["ttl"].toString().toLong()
                val sapling = s["sapling"].toString().toInt()
                seedlings.add(Seedling(id,ttl,sapling))
            }
        }
    }

    override fun newInstance(player: Player?): State {
        return SeedlingState(player)
    }

    override fun createPulse() {
        if(seedlings.isEmpty()) return
        player ?: return

        pulse = object : Pulse(5){
            override fun pulse(): Boolean {
                val removeList = ArrayList<Seedling>()
                GlobalScope.launch {
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
                }
                return false
            }
        }

    }

}
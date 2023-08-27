package content.minigame.blastfurnace

import core.api.animate
import core.api.queueScript
import core.api.teleport
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.json.simple.JSONObject

class BFBeltOre (val player: Player, val id: Int, val amount: Int, var location: Location, var npcInstance: NPC? = null) {
    val state = BlastFurnace.getPlayerState(player)

    fun tick(): Boolean {
        if (location == ORE_END_LOC && npcInstance != null) {
            state.container.addOre(id, amount)
            npcInstance?.clear()
            npcInstance = null
            return true
        }
        else if (npcInstance != null) {
            location = location.transform(Direction.SOUTH, 1)
            teleport(npcInstance!!, location)

            if (location == ORE_END_LOC) {
                val npc = npcInstance!!
                queueScript(npc, 1, QueueStrength.STRONG) {_ ->
                    animate(npc, ORE_DEPOSIT_ANIM)
                    return@queueScript true
                }
            }
        }
        return false
    }

    fun createNpc() {
        if (npcInstance != null) return
        val npcId = BlastFurnace.getNpcForOre(id)
        val npc = NPC.create(npcId, location)
        npc.isWalks = false
        npc.init()

        npcInstance = npc
    }

    fun toJson(): JSONObject {
        val root = JSONObject()
        root["id"] = id.toString()
        root["amount"] = amount.toString()
        root["location"] = location.toString()
        return root
    }

    companion object {
        val ORE_START_LOC = Location.create(1942, 4966, 0)
        val ORE_END_LOC = Location.create(1942, 4963, 0)
        val ORE_DEPOSIT_ANIM = 2434
    }
}
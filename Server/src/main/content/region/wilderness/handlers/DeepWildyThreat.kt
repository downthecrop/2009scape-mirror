package content.region.wilderness.handlers

import content.region.wilderness.handlers.revenants.RevenantType
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.system.timer.PersistTimer
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import core.tools.colorize
import org.json.simple.JSONObject

object DeepWildyThreat {
    @JvmStatic fun getThreat (player: Player) : Int {
        return getOrStartTimer<DWThreatTimer>(player).ticksLeft
    }

    @JvmStatic fun adjustThreat (player: Player, amount: Int) {
        val timer = getOrStartTimer<DWThreatTimer>(player)
        timer.ticksLeft += amount
        if (timer.ticksLeft >= 2500 && timer.lastMessage < 2000) {
            sendMessage(player, colorize("%RYou sense a great wrath upon you."))
            timer.lastMessage = 2000
        } else if (timer.ticksLeft >= 1000 && timer.lastMessage < 1000) {
            sendMessage(player, colorize("%RYou sense watchful eyes upon you."))
            timer.lastMessage = 1000
        } else if (timer.ticksLeft >= 500 && timer.lastMessage < 500) {
            sendMessage(player, colorize("%RYou sense a dark presence."))
            timer.lastMessage = 500
        }
    }
}

class DWThreatTimer : PersistTimer(1, "dw-threat"), Commands {
    var ticksLeft = 0
    var lastMessage = 0
    var currentRev: NPC? = null
    var chats = arrayOf("Leave this place!", "Suffer!", "Death to you!", "Flee, coward!", "Leave my resting place!", "Let me rest in peace!", "You belong to me!")

    override fun run(entity: Entity): Boolean {
        if (ticksLeft-- <= 0) return false
        if (ticksLeft > 3000) ticksLeft = 3000
        if (ticksLeft % 5 != 0) return true
        if (entity !is Player) return false
        if (!entity.skullManager.isWilderness) return true

        val rollchance =
            if      (ticksLeft >= 2500) 10
            else if (ticksLeft >= 2000) 200
            else if (ticksLeft >= 1500) 400
            else if (ticksLeft >= 1000) 800
            else if (ticksLeft >= 500)  1500
            else 2_000_000

        if ((currentRev == null || DeathTask.isDead(currentRev)) && RandomFunction.roll(rollchance)) {
            val type = RevenantType.getClosestHigherOrEqual(entity.properties.currentCombatLevel)
            val npc = NPC.create(type.ids.random(), entity.location)
            npc.isRespawn = false
            npc.init()
            npc.attack(entity)
            Graphics.send(Graphics(86), npc.location)
            sendChat(npc, chats.random())
            currentRev = npc
        } else if (currentRev != null && !currentRev!!.location.withinDistance(entity.location, 25)) {
            poofClear(currentRev!!)
            currentRev = null
        }

        return true
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["threat-time-remaining"] = ticksLeft
    }

    override fun parse(root: JSONObject, entity: Entity) {
        ticksLeft = root.getOrDefault("threat-time-remaining", 3000) as? Int ?: 3000
    }

    override fun defineCommands() {
        define("dwthreat", Privilege.ADMIN, "", "") {player, _ ->
            val timer = getOrStartTimer<DWThreatTimer>(player)
            notify(player, "Current Threat: ${timer.ticksLeft}")
        }
    }
}
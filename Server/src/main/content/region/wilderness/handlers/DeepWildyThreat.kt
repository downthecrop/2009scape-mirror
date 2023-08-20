package content.region.wilderness.handlers

import content.region.wilderness.handlers.revenants.RevenantController
import content.region.wilderness.handlers.revenants.RevenantNPC
import content.region.wilderness.handlers.revenants.RevenantType
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.system.timer.PersistTimer
import core.game.system.timer.impl.Disease
import core.game.world.map.zone.impl.WildernessZone
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

        if ((currentRev == null || DeathTask.isDead(currentRev) || !currentRev!!.isActive) && RandomFunction.roll(rollchance)) {
            val type = RevenantType.getClosestHigherOrEqual(entity.properties.currentCombatLevel)
            val npc = NPC.create(type.ids.random(), entity.location)
            npc.isRespawn = false
            npc.behavior = RevGuardianBehavior()
            npc.init()
            npc.setAttribute("dw-threat-target", entity)
            RevenantController.unregisterRevenant(npc as RevenantNPC, false)
            currentRev = npc
        } else if (currentRev != null && !currentRev!!.location.withinDistance(entity.location, 25) && currentRev!!.properties.teleportLocation == null) {
            poofClear(currentRev!!)
            currentRev = null
        }

        return true
    }

    override fun save(root: JSONObject, entity: Entity) {
        root["threat-time-remaining"] = ticksLeft.toString()
    }

    override fun parse(root: JSONObject, entity: Entity) {
        ticksLeft = root["threat-time-remaining"]?.toString()?.toIntOrNull() ?: 0
    }

    override fun defineCommands() {
        define("dwthreat", Privilege.ADMIN, "", "") {player, _ ->
            val timer = getOrStartTimer<DWThreatTimer>(player)
            notify(player, "Current Threat: ${timer.ticksLeft}")
        }
    }
}

class RevGuardianBehavior : NPCBehavior() {
    val deathMessages = arrayOf("Curses upon thee!", "Rot in blight!", "Suffer my wrath!", "Nevermore!", "May ye be undone!")
    var chats = arrayOf("Leave this place!", "Suffer!", "Death to thee!", "Flee, coward!", "Leave my resting place!", "Let me rest in peace!", "Thou belongeth to me!")

    override fun tick(self: NPC): Boolean {
        val target = getAttribute<Player?>(self, "dw-threat-target", null) ?: return true
        if (!target.isActive || DeathTask.isDead(target)) {
            self.clear()
            return true
        }
        if (target.properties.teleportLocation != null && self.properties.teleportLocation == null) {
            if (WildernessZone.isInZone(target.properties.teleportLocation))
                self.properties.teleportLocation = target.properties.teleportLocation
        }
        self.attack(target)
        return true
    }

    override fun onCreation(self: NPC) {
        Graphics.send(Graphics(86), self.location)
        sendChat(self, chats.random())
    }

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        val target = getAttribute<Player?>(self, "dw-threat-target", null)
        if (attacker != target) {
            if (shouldSendMessage && attacker is Player)
                sendMessage(attacker, "This revenant is focused on someone else.")
            return false
        }
        return super.canBeAttackedBy(self, attacker, style, shouldSendMessage)
    }

    override fun onDeathStarted(self: NPC, killer: Entity) {
        val target = getAttribute<Player?>(self, "dw-threat-target", null) ?: return
        sendChat(self, deathMessages.random())
        val disease = getOrStartTimer<Disease>(target, 25)
        disease.hitsLeft = 25
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        val target = getAttribute<Player?>(self, "dw-threat-target", null) ?: return
        if (killer != target) drops.clear()
        val timer = getOrStartTimer<DWThreatTimer>(target)
        timer.ticksLeft = 0
    }
}
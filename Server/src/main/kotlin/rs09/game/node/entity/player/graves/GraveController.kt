package rs09.game.node.entity.player.graves

import api.*
import core.game.interaction.Interaction
import core.game.node.Node
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneRestriction
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.ServerStore
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.system.command.Privilege
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.tools.secondsToTicks
import rs09.tools.stringtools.colorize
import rs09.tools.ticksToSeconds
import java.util.Map
import kotlin.math.min

class GraveController : PersistWorld, TickListener, InteractionListener, Commands {
    override fun defineListeners() {
        on(GraveType.ids, IntType.NPC, "read", handler = this::onGraveReadOption)
        on(GraveType.ids, IntType.NPC, "bless", handler = this::onGraveBlessed)
        on(GraveType.ids, IntType.NPC, "repair", handler = this::onGraveRepaired)
        on(GraveType.ids, IntType.NPC, "demolish", handler = this::onGraveDemolished)
    }

    override fun defineCommands() {
        define("forcegravedeath", Privilege.ADMIN, "", "Forces a death that should produce a grave.") {player, _ ->
            player.details.rights = Rights.REGULAR_PLAYER
            setAttribute(player, "tutorial:complete", true)
            player.impactHandler.manualHit(player, player.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
            notify(player, "Grave created at ${player.location}")
            GameWorld.Pulser.submit(object : Pulse(15) {
                override fun pulse(): Boolean {
                    player.details.rights = Rights.ADMINISTRATOR
                    sendMessage(player, "Rights restored")
                    return true
                }
            })
        }
    }


    override fun tick() {
        for (grave in activeGraves.values.toTypedArray()) {
            if (grave.ticksRemaining == -1) return

            if (grave.ticksRemaining == secondsToTicks(30) || grave.ticksRemaining == secondsToTicks(90)) {
                grave.transform(grave.id + 1)
            }

            if (grave.ticksRemaining == 0) {
                grave.collapse()
            }

            grave.ticksRemaining--
        }
    }

    private fun onGraveReadOption(player: Player, node: Node) : Boolean {
        val grave = node as? Grave ?: return false

        var isGraniteBackground = false

        when (grave.type) {
            in GraveType.SMALL_GS..GraveType.ANGEL_DEATH -> isGraniteBackground = true
        }

        if (isGraniteBackground)
            setVarbit(player, 4191, 1)
        else
            setVarbit(player, 4191, 0)

        openInterface(player, 266)
        setInterfaceText(player, grave.retrieveFormattedText(), 266, 23)

        sendMessage(player, "It looks like it'll survive another ${grave.getFormattedTimeRemaining()}.")
        if (player.details.uid == grave.ownerUid) {
            sendMessage(player, "Isn't there something a bit odd about reading your own gravestone?")
        }
        return true
    }

    private fun onGraveBlessed(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (getAttribute(g, "blessed", false)) {
            sendMessage(player, "This grave has already been blessed.")
            return true
        }

        if (player.details.uid == g.ownerUid) {
            sendMessage(player, "The gods don't seem to approve of people attempting to bless their own gravestones.")
            return true
        }

        if (getStatLevel(player, Skills.PRAYER) < 70) {
            sendMessage(player, "You need a Prayer level of 70 to bless a grave.")
            return true
        }

        val blessAmount = min(60, player.skills.prayerPoints.toInt() - 10)

        if (blessAmount <= 0) {
            sendMessage(player, "You do not have enough prayer points to do that.")
            return true
        }

        g.addTime(secondsToTicks(blessAmount * 60))
        player.skills.prayerPoints -= blessAmount
        setAttribute(g, "blessed", true)

        playAudio(player, Audio(2674))
        animate(player, 645)

        val gOwner = Repository.uid_map[g.ownerUid]
        if (gOwner != null) {
            sendMessage(gOwner, colorize("%RYour grave has been blessed."))
        }
        return true
    }

    private fun onGraveRepaired(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (getAttribute(g, "repaired", false)) {
            sendMessage(player, "This grave has already been repaired.")
            return true
        }

        if (getStatLevel(player, Skills.PRAYER) < 2) {
            sendMessage(player, "You need a Prayer level of 2 to bless a grave.")
            return true
        }

        if (player.skills.prayerPoints < 1.0) {
            sendMessage(player, "You do not have enough prayer points to do that.")
            return true
        }

        val restoreAmount = min(5, player.skills.prayerPoints.toInt())
        g.addTime(secondsToTicks(restoreAmount * 60))
        player.skills.prayerPoints -= restoreAmount
        setAttribute(g, "repaired", true)

        playAudio(player, Audio(2674))
        animate(player, 645)
        return true
    }

    private fun onGraveDemolished(player: Player, node: Node) : Boolean {
        val g = node as? Grave ?: return false

        if (player.details.uid != g.ownerUid) {
            sendMessage(player, "You cannot demolish someone else's gravestone!")
            return true
        }

        g.demolish()
        return true
    }

    override fun save() {
        serializeToServerStore()
    }

    override fun parse() {
        deserializeFromServerStore()
    }

    companion object {
        val activeGraves = HashMap<Int, Grave>()
        var childCounter = 0
        val ATTR_GTYPE = "/save:gravetype"

        @JvmStatic fun produceGrave(type: GraveType): Grave {
            val g = Grave()
            g.configureType(type)
            return g
        }

        @JvmStatic fun shouldCrumble(item: Int) : Boolean {
            when (item) {
                Items.ECTOPHIAL_4251 -> return true
                in Items.SMALL_POUCH_5509..Items.GIANT_POUCH_5515 -> return true
            }

            return itemDefinition(item).hasAction("destroy")
        }

        @JvmStatic fun shouldRelease(item: Int) : Boolean {
            when (item) {
                Items.CHINCHOMPA_9976 -> return true
                Items.CHINCHOMPA_10033 -> return true
                in Items.BABY_IMPLING_JAR_11238..Items.DRAGON_IMPLING_JAR_11257 -> return itemDefinition(item).isUnnoted
            }

            return false
        }

        @JvmStatic fun checkTransform(item: Item) : Item {
            if (item.hasItemPlugin())
                return item.plugin.getDeathItem(item)
            return item
        }

        @JvmStatic fun allowGenerate(player: Player) : Boolean {
            if (player.skullManager.isSkulled)
                return false
            if (player.skullManager.isWilderness)
                return false
            if (player.ironmanManager.mode == IronmanMode.HARDCORE)
                return false
            if (player.zoneMonitor.isRestricted(ZoneRestriction.GRAVES))
                return false
            return true
        }

        @JvmStatic fun getGraveType(player: Player) : GraveType {
            return GraveType.values()[getAttribute(player, ATTR_GTYPE, 0)]
        }

        @JvmStatic fun updateGraveType(player: Player, type: GraveType) {
            setAttribute(player, ATTR_GTYPE, type.ordinal)
        }

        @JvmStatic fun hasGraveAt(loc: Location) : Boolean {
            return activeGraves.values.toTypedArray().any { it.location == loc }
        }

        fun serializeToServerStore() {
            val archive = ServerStore.getArchive("active-graves")
            for ((uid,grave) in activeGraves) {
                val g = JSONObject()
                g["ticksRemaining"] = grave.ticksRemaining
                g["location"] = grave.location.toString()
                g["type"] = grave.type.ordinal
                g["username"] = grave.ownerUsername
                val items = JSONArray()
                for (item in grave.getItems()) {
                    val i = JSONObject()
                    i["id"] = item.id
                    i["amount"] = item.amount
                    i["charge"] = item.charge
                    items.add(i)
                }
                g["items"] = items
                archive["$uid"] = g
            }
        }

        fun deserializeFromServerStore() {
            val archive = ServerStore.getArchive("active-graves")
            for (entry in archive.entries as Set<Map.Entry<String, JSONObject>>) {
                val g = entry.value as JSONObject
                val uid = (entry.key as String).toInt()
                val type = g["type"].toString().toInt()
                val ticks = g["ticksRemaining"].toString().toInt()
                val location = Location.fromString(g["location"].toString())
                val username = g["username"].toString()

                val items = ArrayList<Item>()
                val itemsRaw = g["items"] as JSONArray
                for (itemRaw in itemsRaw) {
                    val item = itemRaw as JSONObject
                    val id = item["id"].toString().toInt()
                    val amount = item["amount"].toString().toInt()
                    val charge = item["charge"].toString().toInt()
                    items.add(Item(id, amount, charge))
                }

                val grave = produceGrave(GraveType.values()[type])
                grave.setupFromJsonParams(uid, ticks, location, items.toTypedArray(), username)
                activeGraves[uid] = grave
            }
        }
    }
}
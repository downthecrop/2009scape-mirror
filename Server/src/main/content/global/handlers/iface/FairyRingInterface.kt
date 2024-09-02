package content.global.handlers.iface

import core.api.*
import core.cache.def.impl.DataMap
import core.game.component.Component
import core.game.event.FairyRingDialEvent
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import core.tools.RandomFunction



/**
 * Handles the fairy ring interface
 * @author Ceikry
 */
class FairyRingInterface : InterfaceListener {
    companion object {
        const val RINGS_IFACE = 734
        const val LOG_IFACE_ID = 735
        const val TRANSMIT_NOREDRAW_CHILD = 64000
        const val MAP_LOG_ID_TO_CHILD = 1467
        const val LOG_INV_HOOK = 816
        const val VB_LOG_SORT_ORDER = 4618
        val LOG_ID_BUFFER = ArrayList<Int>()
        val LOG_ID_CHILD_MAP = DataMap.get(MAP_LOG_ID_TO_CHILD)
        val RING_1 = arrayOf('a','d','c','b')
        val RING_2 = arrayOf('i','l','k','j')
        val RING_3 = arrayOf('p','s','r','q')
    }

    override fun defineInterfaceListeners() {

        onOpen(RINGS_IFACE){ player, _ ->
            player.interfaceManager.openSingleTab(Component(LOG_IFACE_ID))
            player.setAttribute("fr:ring1", 0)
            player.setAttribute("fr:ring2", 0)
            player.setAttribute("fr:ring3", 0)
            drawLog(player)
            return@onOpen true
        }

        onClose(RINGS_IFACE){ player, _ ->
            closeTabInterface(player)
            player.removeAttribute("fr:ring1")
            player.removeAttribute("fr:ring2")
            player.removeAttribute("fr:ring3")
            setVarp(player, 816, 0)
            closeTabInterface(player)
            return@onClose true
        }

        on(RINGS_IFACE){ player, _, _, buttonID, _, _ ->
            if(player.getAttribute("fr:time",0L) > System.currentTimeMillis()) return@on true
            var delayIncrementer = 1750L
            when(buttonID){
                23 -> delayIncrementer += increment(player,1)
                25 -> delayIncrementer += increment(player,2)
                27 -> delayIncrementer += increment(player,3)
                24 -> decrement(player,1)
                26 -> decrement(player,2)
                28 -> decrement(player,3)
                21 -> confirm(player)
            }
            player.setAttribute("fr:time",System.currentTimeMillis() + delayIncrementer)
            return@on true
        }

        on(LOG_IFACE_ID,12){ player, _, _, _, _, _ ->
            toggleSortOrder(player)
            return@on true
        }

    }

    /**
     * Draws the travel log interface
     * Currently, the visited logs is a bool array in globalData. Someone should migrate this to prefs or something at some point.
     * The child hash is used to lookup the log entry "ID" from the cache's enums. This ID is then populated in a list and sent as an update for container 816.
     * Transmitting an update for container 816 then triggers the relevant CS2 to fire (don't ask me), causing the ring codes to be inserted above the log entry and the entries to be properly rearranged.
     * @param player The player to draw the interface for
     */
    private fun drawLog (player: Player)
    {
        for (i in FairyRing.values().indices) {
            if (!player.savedData.globalData.hasTravelLog(i)) {
                continue
            }
            val ring = FairyRing.values()[i]
            if (ring.childId == -1) {
                continue
            }
            val compHash = (LOG_IFACE_ID shl 16) or (ring.childId and 0xFFFF)
            for ((key, value) in LOG_ID_CHILD_MAP.dataStore) {
                if (value == compHash) {
                    LOG_ID_BUFFER.add(key)
                    break
                }
            }
            setInterfaceText(player, "<br>${ring.tip}", LOG_IFACE_ID, ring.childId)
        }
        if (LOG_ID_BUFFER.size > 0)
        {
            val ctx = ContainerContext (
                player, LOG_IFACE_ID,
                TRANSMIT_NOREDRAW_CHILD, LOG_INV_HOOK,
                LOG_ID_BUFFER.toIntArray()
            )
            PacketRepository.send(ContainerPacket::class.java, ctx)
        }
        LOG_ID_BUFFER.clear()
    }

    private fun toggleSortOrder(player: Player) {
        val curSort = getVarbit(player, VB_LOG_SORT_ORDER) == 0
        setVarbit(player, VB_LOG_SORT_ORDER, if (curSort) 1 else 0)
        drawLog(player)
    }

    fun increment(player: Player,ring: Int): Long{
        val curIndex = player.getAttribute("fr:ring$ring",0)
        var nextIndex = 0
        if(curIndex == 3) nextIndex = 0
        else if(curIndex == 1) nextIndex = 3
        else if(curIndex == 2) nextIndex = 2
        else nextIndex = curIndex + 1
        player.setAttribute("fr:ring$ring",nextIndex)
        return if (curIndex == 1) 1750L else 0L
    }

    fun decrement(player: Player,ring: Int){
        val curIndex = player.getAttribute("fr:ring$ring",0)
        var nextIndex = 0
        if(curIndex == 0) nextIndex = 3
        else nextIndex = curIndex - 1
        player.setAttribute("fr:ring$ring",nextIndex)
    }

    private fun confirm(player: Player){
        val ring1index = player.getAttribute("fr:ring1",0)
        val ring2index = player.getAttribute("fr:ring2",0)
        val ring3index = player.getAttribute("fr:ring3",0)
        val code = "${RING_1[ring1index]}${RING_2[ring2index]}${RING_3[ring3index]}"
        val ring: FairyRing? = try {
            FairyRing.valueOf(code.uppercase())
        } catch (e: Exception) { null }

        var tile = ring?.tile
        if(ring?.checkAccess(player) != true){
            sendDialogue(player, "The ring seems to reject you.")
            tile = null
        }
        if (ring == null || tile == null) {
            val center = Location(2412, 4434, 0)
            tile = if (RandomFunction.random(2) == 1) {
                center.transform(RandomFunction.random(2, 6), RandomFunction.random(2, 6), 0)
            } else {
                center.transform(RandomFunction.random(-2, -6), RandomFunction.random(-2, -6), 0)
            }
            if (!RegionManager.isTeleportPermitted(tile) || RegionManager.getObject(tile) != null) {
                tile = Location.create(2412, 4431, 0)
            }
            GameWorld.Pulser.submit(object : Pulse(4, player) {
                override fun pulse(): Boolean {
                    sendPlayerDialogue(player, "Wow, fairy magic sure is useful, I hardly moved at all!", core.game.dialogue.FacialExpression.AMAZED)
                    return true
                }
            })
        } else {
            if (!player.savedData.globalData.hasTravelLog(ring.ordinal)) {
                player.savedData.globalData.setTravelLog(ring.ordinal)
            }
        }
        closeInterface(player)

        ring.let {
            if (it == null) {
                return@let
            }

            player.dispatch(FairyRingDialEvent(it))
            teleport(player, tile!!, TeleportManager.TeleportType.FAIRY_RING)
        }
    }
}

enum class FairyRing(val tile: Location?, val tip: String = "", val childId: Int = -1) {
    AIQ(Location.create(2996, 3114, 0), "Asgarnia: Mudskipper Point", 15),
    AIR(Location.create(2700, 3247, 0), "Islands: South of Witchaven", 16),
    AJQ(Location.create(2735, 5221, 0), "Dungeons: Dark cave south of Dorgesh-Kaann", 19),
    ALR(Location.create(3059, 4875, 0), "Other realms: Abyssal Area", 28),
    AJR(Location.create(2780, 3613, 0), "Kandarin: Slayer cave south-east of Rellekka", 20),
    AJS(Location.create(2500, 3896, 0), "Islands: Penguins near Miscellania", 21),
    AKQ(Location.create(2319, 3619, 0), "Kandarin: Piscatoris Hunter area", 23),
    AKS(Location.create(2571, 2956, 0), "Feldip Hills: Jungle Hunter area", 25),
    ALQ(Location.create(3597, 3495, 0), "Morytania: Haunted Woods east of Canifis", 27) {
      override fun checkAccess(player: Player) : Boolean {
          return requireQuest(player, "Priest in Peril", "to use this ring.")
      }
    },
    ALS(Location.create(2644, 3495, 0), "Kandarin: McGrubor's Wood", 29),
    BIP(Location.create(3410, 3324, 0), "Islands: River Salve", 30),
    BIQ(Location.create(3251, 3095, 0), "Kharidian Desert: Near Kalphite hive", 31),
    BIS(Location.create(2635, 3266, 0), "Kandarin: Ardougne Zoo unicorns", 33),
    BJR(null, "Other Realms: Realm of the Fisher King", 36),
    BKP(Location.create(2385, 3035, 0), "Feldip Hills: South of Castle Wars", 38),
    BKQ(Location.create(3041, 4532, 0), "Other realms: Enchanted Valley", 39),
    BKR(Location.create(3469, 3431, 0), "Morytania: Mort Myre, south of Canifis", 40) {
      override fun checkAccess(player: Player) : Boolean {
          return requireQuest(player, "Priest in Peril", "to use this ring.")
      }
    },
    BLP(Location.create(2437, 5126, 0), "Dungeons: TzHaar area", 42),
    BLQ(null, "Yu'biusk", 43),//Location.create(2229, 4244, 1)
    BLR(Location.create(2740, 3351, 0), "Kandarin: Legends' Guild", 44),
    CIP(Location.create(2513, 3884, 0), "Islands: Miscellania", 46) {
        override fun checkAccess(player: Player): Boolean {
            return requireQuest(player, "Fremennik Trials", "to use this ring.")
        }
    },
    CIQ(Location.create(2528, 3127, 0), "Kandarin: North-west of Yanille", 47),
    CJR(Location.create(2705, 3576, 0), "Kandarin: Sinclair Mansion", 52),
    CKP(Location.create(2075, 4848, 0), "Other realms: Cosmic Entity's plane", 54),
    CKR(Location.create(2801, 3003, 0), "Karamja: South of Tai Bwo Wannai Village", 56),
    CKS(Location.create(3447, 3470, 0), "Morytania: Canifis", 57) {
      override fun checkAccess(player: Player) : Boolean {
          return requireQuest(player, "Priest in Peril", "to use this ring.")
      }
    },
    CLP(Location.create(3082, 3206, 0), "Islands: South of Draynor Village", 58),
    CLS(Location.create(2682, 3081, 0), "Islands: Jungle spiders near Yanille", 61),
    DIR(Location.create(3038, 5348, 0), "Other realms: Goraks' Plane", 64),
    DIS(Location.create(3108, 3149, 0), "Misthalin: Wizards' Tower", 65),
    DJP(Location.create(2658, 3230, 0), "Kandarin: Tower of Life", 66),
    DJR(Location.create(2676, 3587, 0), "Kandarin: Sinclair Mansion (west)", 68),
    DKP(Location.create(2900, 3111, 0), "Karamja: South of Musa Point", 70),
    DKR(Location.create(3129, 3496, 0), "Misthalin: Edgeville", 72),
    DKS(Location.create(2744, 3719, 0), "Kandarin: Snowy Hunter area", 73),
    DLQ(Location.create(3423, 3016, 0), "Kharidian Desert: North of Nardah", 75),
    DLR(Location.create(2213, 3099, 0), "Islands: Poison Waste south of Isafdar", 76),
    AIS(null), AIP(null), AKP(null), FAIRY_HOME(Location.create(2412, 4434, 0));

    open fun checkAccess(player: Player) : Boolean {
        return true
    }
}

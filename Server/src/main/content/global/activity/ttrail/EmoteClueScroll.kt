package content.global.activity.ttrail

import content.global.activity.ttrail.TreasureTrailPlugin.*
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs

/**
 * Handles an emote clue scroll.
 */

// TODO: This plugin should be converted to a listener as part of a larger rewrite of Treasure Trails.

abstract class EmoteClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val emote: Emotes?,
    val commenceEmote: Emotes?,
    val equipment: Array<IntArray>?,
    val emptySlots: IntArray?,
    private val clue: String?,
    vararg borders: ZoneBorders
) : ClueScrollPlugin(name, clueId, level, 345, *borders) {

    /**
     * Constructs a new [EmoteClueScroll] instance without empty slots.
     * @param name the name.
     * @param clueId the clue id.
     * @param level the level.
     * @param emote the emote.
     * @param commenceEmote the emote.
     * @param equipment the equipment.
     * @param clue the clue.
     * @param borders the borders.
     */
    constructor(
        name: String?,
        clueId: Int,
        level: ClueLevel?,
        emote: Emotes?,
        commenceEmote: Emotes?,
        equipment: Array<IntArray>?,
        clue: String?,
        vararg borders: ZoneBorders
    ) : this(name, clueId, level, emote, commenceEmote, equipment, null, clue, *borders)

    override fun actionButton(player: Player, interfaceId: Int, buttonId: Int, slot: Int, itemId: Int, opcode: Int): Boolean {
        if (!inInventory(player, clueId)) {
            return false
        }

        val triggeredEmote = Emotes.forId(buttonId) ?: return false

        if (triggeredEmote == emote) {
            val oldUri: NPC? = getAttribute(player, "uri", null)
            if (oldUri != null && oldUri.isActive) {
                return false
            }
            spawnUri(player)
        } else if (triggeredEmote == commenceEmote) {
            setAttribute(player, "commence-emote", true)
        }

        return if (emote == triggeredEmote) {
            super.actionButton(player, interfaceId, buttonId, slot, itemId, opcode)
        } else {
            false
        }
    }

    override fun read(player: Player) {
        for (i in 1 until 9) {
            player.packetDispatch.sendString("", interfaceId, i)
        }
        super.read(player)
        player.packetDispatch.sendString(clue?.replace("<br>", "<br><br>") ?: "", interfaceId, 1)
    }

    /**
     * Spawns the uri npc for the player.
     * @param player the player.
     */
    fun spawnUri(player: Player) {
        val doubleAgent = level == ClueLevel.HARD && getAttribute(player, ATTR_TT_KILLED_AGENT, 0) != clueId
        val id = if (doubleAgent) NPCs.DOUBLE_AGENT_5145 else NPCs.URI_5141
        val uri = NPC.create(id, player.location.transform(1, 0, 0))

        setAttribute(player, ATTR_TT_URI, uri)
        removeAttribute(player, ATTR_TT_COMMENCE_EMOTE)
        setAttribute(uri, ATTR_TT_DOUBLE_AGENT, doubleAgent)
        setAttribute(uri, "player", player) // These two strings are defined as consts in !2361. Swap them in once that merges.
        setAttribute(uri, "clue", this)
        uri.init()
        uri.graphics(Graphics.create(org.rs09.consts.Graphics.GIVE_THANKS_86))
        uri.faceTemporary(player, 1)

        if (doubleAgent) {
            sendChat(uri, "I expect you to die!")
            uri.attack(player)
        }
    }

    /**
     * Checks if a commence emote is needed.
     * @return `true` if so.
     */
    fun hasCommencEmote(): Boolean {
        return commenceEmote != null
    }
}
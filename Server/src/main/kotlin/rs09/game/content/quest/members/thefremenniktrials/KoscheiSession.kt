package rs09.game.content.quest.members.thefremenniktrials

import core.game.node.entity.player.Player
import org.rs09.consts.NPCs

/**
 * Represents a session with Koschei.
 * @author NixWigton
 */
class KoscheiSession(
    /**
     * The player.
     */
    val player: Player
) {
    /**
     * The Koschei npc.
     */
    private val koschei: KoscheiNPC = KoscheiNPC(
        NPCs.KOSCHEI_THE_DEATHLESS_1290,
        player.location?.transform(1,0,0),
        this
    )

    /**
     * Constructs a new `KoscheiSession` `Object`.
     * @param player the player.
     */
    init {
        if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
            player.removeExtension(KoscheiSession::class.java)
        }
        player.addExtension(KoscheiSession::class.java, this)
    }

    /**
     * Starts the session.
     */
    fun start() {
        koschei.init()
        player.unlock()
    }

    /**
     * Closes the session.
     */
    fun close() {
        koschei.clear()
        player.removeExtension(KoscheiSession::class.java)
    }

    companion object {
        /**
         * Creates the Koschei session.
         * @param player the player.
         * @return the session.
         */
        fun create(player: Player): KoscheiSession {
            return KoscheiSession(player)
        }

        /**
         * Gets the Koschei session.
         * @param player the player.
         * @return the session.
         */
        fun getSession(player: Player): KoscheiSession {
            return player.getExtension(KoscheiSession::class.java)
        }
    }
}
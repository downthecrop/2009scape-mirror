package api

import core.game.node.entity.player.Player
import org.json.simple.JSONObject

/**
 * An interface for writing content that allows data to be saved and loaded from player saves.
 *
 * Parsing is called *after* any [LoginListener] is executed.
 *
 * Saving is called *after* any [LogoutListener] is executed.
 */
interface PersistPlayer : ContentInterface {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     * You need to fetch a player's specific instance of the data and save from that.
     * For reference, see [rs09.game.node.entity.skill.slayer.SlayerManager]
     */
    fun savePlayer(player: Player, save: JSONObject)
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     * You need to fetch a player's specific instance of the data and parse to that.
     * For reference, see [rs09.game.node.entity.skill.slayer.SlayerManager]
     */
    fun parsePlayer(player: Player, data: JSONObject)
}
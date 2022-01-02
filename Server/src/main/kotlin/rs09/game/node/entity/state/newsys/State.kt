package rs09.game.node.entity.state.newsys

import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.json.simple.JSONObject
import rs09.game.world.GameWorld.Pulser

/**
 * A class representing a state that the player or some associated thing can be in.
 * @param player The player the state is for
 * @author Ceikry
 */
abstract class State(val player: Player? = null) {
    var pulse: Pulse? = null

    /**
     * Saves any additional data the state might need to the player's save.
     */
    abstract fun save(root: JSONObject)

    /**
     * Parses any additional saved data the state might have.
     */
    abstract fun parse(_data: JSONObject)

    /**
     * Returns a new instance of the class constructed for the player.
     */
    abstract fun newInstance(player: Player? = null) : State

    /**
     * Method used to define the pulse the state uses.
     * Called during the init method of the state, which is done during save parsing and done
     * manually when first creating a state.
     */
    abstract fun createPulse()
    fun init() {
        createPulse()
        pulse ?: return
        Pulser.submit(pulse!!)
    }
}
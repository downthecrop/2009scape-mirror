package core.game.system.command.sets

import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import core.plugin.Initializable

/**
 * In game setting of simple attributes. Not for complex objects or enums.
 * @author ovenbread
 */
@Initializable
class PlayerAttributeCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {

        /**
         * Gets the value of an attribute on a player.
         */
        define("getattribute") { player, args ->
            if (args.size < 2) {
                reject(player, "Usage ::getattribute [playername] attributename")
            }
            var attributeName = ""

            // If stats is called without a username, return self.
            var queryPlayer: Player? = null
            if(args.size == 2){
                queryPlayer = player
                attributeName = args[1]
            }
            // If stats is called with a username, find the player or set to null.
            if(args.size == 3) {
                queryPlayer = Repository.getPlayerByName(args[1])
                attributeName = args[2]
            }
            // If player is not found, return error.
            if(queryPlayer == null){
                reject(player,"Invalid player or player not online.")
                return@define
            }

            notify(player,"Attribute: " + attributeName + " Value: " + getAttribute(queryPlayer!!, attributeName, "").toString())
            return@define
        }

        /**
         * Sets the value of an attribute on a player.
         */
        define("setattribute") { player, args ->
            if (args.size < 2) {
                reject(player, "Usage ::setattribute [playername] attributename value")
            }
            var attributeName = ""
            var value = ""

            // If stats is called without a username, return self.
            var queryPlayer: Player? = null
            if(args.size == 3){
                queryPlayer = player
                attributeName = args[1]
                value = args[2]
            }
            // If stats is called with a username, find the player or set to null.
            if(args.size == 4) {
                queryPlayer = Repository.getPlayerByName(args[1])
                attributeName = args[2]
                value = args[3]
            }
            // If player is not found, return error.
            if(queryPlayer == null){
                reject(player,"Invalid player or player not online.")
                return@define
            }
            // Please be careful when editing attributes.
            // If string input is an integer, convert to an integer and set it to the attribute.
            if (value.toIntOrNull() != null){
                setAttribute(queryPlayer!!, attributeName, value.toIntOrNull())
                return@define
            }
            // If string input is a float(decimal number), convert to a float and set it to the attribute.
            if (value.toFloatOrNull() != null){
                setAttribute(queryPlayer!!, attributeName, value.toFloatOrNull())
                return@define
            }
            // If string matches the text "true", set the attribute value to the boolean true.
            if (value == "true"){
                setAttribute(queryPlayer!!, attributeName, true)
                return@define
            }
            // If string matches the text "false", set the attribute value to the boolean false.
            if (value == "false"){
                setAttribute(queryPlayer!!, attributeName, false)
                return@define
            }
        }
    }
}

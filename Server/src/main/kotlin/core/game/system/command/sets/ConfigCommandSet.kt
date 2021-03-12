package core.game.system.command.sets

import core.plugin.Initializable
import core.game.system.command.Command

@Initializable
class ConfigCommandSet : CommandSet(Command.Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Sets a range of configs to a maximum value
         */
        define("sconfigrange"){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange idlo idhi")
                return@define
            }
            val idlo = args[1].toIntOrNull() ?: return@define
            val idhi = args[2].toIntOrNull() ?: return@define
            for (idsend in idlo until idhi) {
                player.configManager.set(idsend, Integer.MAX_VALUE)
                player.packetDispatch.sendMessage("Config: $idsend value: " + Integer.MAX_VALUE)
            }
        }

        /**
         * Sets a range of configs to 0
         */
        define("sconfigrange0"){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange0 idlo idhi")
                return@define
            }
            val idlo = args[1].toIntOrNull() ?: return@define
            val idhi = args[2].toIntOrNull() ?: return@define
            for (idsend in idlo until idhi) {
                player.configManager.set(idsend, 0)
                player.packetDispatch.sendMessage("Config: $idsend value: 0")
            }
        }

        /**
         * Opens an interface component
         */
        define("iface"){player, args ->
            if (args.size < 2) {
                reject(player, "usage: iface id")
                return@define
            }
            val id = args[1].toIntOrNull() ?: return@define
            player.interfaceManager.openComponent(id)
        }
    }
}
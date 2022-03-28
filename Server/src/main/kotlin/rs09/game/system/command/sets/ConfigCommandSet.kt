package rs09.game.system.command.sets

import core.plugin.Initializable
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege

@Initializable
class ConfigCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Sets a range of configs to a maximum value
         */
        define("sconfigrange"){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange idlo idhi")
            }
            val idlo = args[1].toIntOrNull() ?: reject(player, "INCORRECT ID LOW")
            val idhi = args[2].toIntOrNull() ?: reject(player, "INCORRECT ID HIGH")
            for (idsend in (idlo as Int) until (idhi as Int)) {
                player.configManager.set(idsend, Integer.MAX_VALUE)
                notify(player,"Config: $idsend value: " + Integer.MAX_VALUE)
            }
        }

        /**
         * Sets a range of configs to 0
         */
        define("sconfigrange0"){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange0 idlo idhi")
            }
            val idlo = args[1].toIntOrNull() ?: reject(player, "INCORRECT ID LOW")
            val idhi = args[2].toIntOrNull() ?: reject(player, "INCORRECT ID HIGH")
            for (idsend in (idlo as Int) until (idhi as Int)) {
                player.configManager.set(idsend, 0)
                notify(player,"Config: $idsend value: 0")
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
            val id = args[1].toIntOrNull() ?: reject(player, "INVALID INTERFACE ID")
            player.interfaceManager.openComponent(id as Int)
        }
    }
}
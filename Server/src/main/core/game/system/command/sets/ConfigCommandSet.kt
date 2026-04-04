package core.game.system.command.sets

import core.plugin.Initializable
import core.game.system.command.Command
import core.game.system.command.Privilege
import core.api.*
import core.game.interaction.QueueStrength

@Initializable
class ConfigCommandSet : CommandSet(Privilege.ADMIN){
    override fun defineCommands() {

        /**
         * Sets a range of configs to a maximum value
         */
        define("sconfigrange", usage = "::sconfigrange <lt>start-id<gt> <lt>end-id<gt>", description = "Sets each varp from <lt>start-id<gt> (inclusive) up to <lt>end-id<gt> (exclusive) to Integer.MAX_VALUE."){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange idlo idhi")
            }
            val idlo = args[1].toIntOrNull() ?: reject(player, "INCORRECT ID LOW")
            val idhi = args[2].toIntOrNull() ?: reject(player, "INCORRECT ID HIGH")
            for (idsend in (idlo as Int) until (idhi as Int)) {
                setVarp(player, idsend, Integer.MAX_VALUE)
                notify(player,"Config: $idsend value: " + Integer.MAX_VALUE)
            }
        }

        /**
         * Sets a range of configs to 0
         */
        define("sconfigrange0", usage = "::sconfigrange0 <lt>start-id<gt> <lt>end-id<gt>", description = "Sets each varp from <lt>start-id<gt> (inclusive) up to <lt>end-id<gt> (exclusive) to 0."){player, args ->
            if (args.size < 3) {
                reject(player, "usage: sconfigrange0 idlo idhi")
            }
            val idlo = args[1].toIntOrNull() ?: reject(player, "INCORRECT ID LOW")
            val idhi = args[2].toIntOrNull() ?: reject(player, "INCORRECT ID HIGH")
            for (idsend in (idlo as Int) until (idhi as Int)) {
                setVarp(player, idsend, 0)
                notify(player,"Config: $idsend value: 0")
            }
        }

        /**
         * Opens an interface component
         */
        define("iface", Privilege.ADMIN, "::iface <lt>Interface ID<gt>", "Opens the interface with the given ID."){player, args ->
            if (args.size < 2) {
                reject(player, "usage: ::iface <id>")
                return@define
            }
            val id = args[1].toIntOrNull() ?: reject(player, "INVALID INTERFACE ID")
            player.interfaceManager.openComponent(id as Int)
        }

        define("ifaces", Privilege.ADMIN, "::ifaces <From Interface ID> <To Interface ID> <(opt) Duration Per Interface>", "Opens interfaces from the From ID to the To ID, with a delay of 3 between each, unless specified otherwise"){ player, args ->

            if (args.size < 3) {
                reject(player, "Syntax error: ::anims <From Interface ID> <To Interface ID> <(opt) Duration Per Interface>")
            }
            val ifaceFrom = args[1].toInt()
            val ifaceTo = args[2].toInt()
            val ifaceDelay = (args.getOrNull(3) ?: "3").toInt()

            queueScript(player, 1, QueueStrength.WEAK) { stage: Int ->
                val ifaceId = ifaceFrom + stage
                player.interfaceManager.openComponent(ifaceId)
                notify(player, "Opening interface $ifaceId")

                if (ifaceId == ifaceTo) {
                    return@queueScript stopExecuting(player)
                }

                return@queueScript delayScript(player, ifaceDelay)
            }
        }
    }
}

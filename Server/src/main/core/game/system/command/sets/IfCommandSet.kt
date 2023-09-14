package core.game.system.command.sets

import core.api.Commands
import core.api.getChildren
import core.cache.def.impl.ComponentType
import core.cache.def.impl.IfaceDefinition
import core.game.system.command.Privilege

class IfCommandSet : Commands {
    override fun defineCommands() {
        define ("iftriggers", Privilege.ADMIN, "::iftriggers <lt>id<gt>", "Lists all known triggers for the given interface.") {player, args ->
            val id = args.getOrNull(1)?.toIntOrNull() ?: -1
            if (id == -1) reject(player, "Must supply a valid interface ID!")

            val def = IfaceDefinition.forId(id)
            notify(player, logToConsole = true, message = "Triggers for $def:")

            for (child in def.children) {
                if (child.scripts == null) continue
                if (child.triggers == null) continue

                if (child.scripts.onVarpTransmit != null) {
                    notify(player, logToConsole = true, message = "$child [VARP]:")
                    notify(player, logToConsole = true, message = "  Transmit ${child.triggers.varpTriggers.joinToString(",")} triggers script ${child.scripts.onVarpTransmit.id}")
                    notify(player, logToConsole = true, message = "  Default script args: ${child.scripts.onVarpTransmit.args.joinToString(",")}")
                }
                if (child.scripts.onVarcTransmit != null) {
                    notify(player, logToConsole = true, message = "$child [VARC]:")
                    notify(player, logToConsole = true, message = "  Transmit ${child.triggers.varcTriggers.joinToString(",")} triggers script ${child.scripts.onVarcTransmit.id}")
                    notify(player, logToConsole = true, message = "  Default script args: ${child.scripts.onVarcTransmit.args.joinToString(",")}")
                }
            }
        }

        define ("listiftext", Privilege.ADMIN, "::listiftext <lt>id<gt>", "Prints all text values and their child index on an interface.") {player, args ->
            val id = args.getOrNull(1)?.toIntOrNull() ?: -1
            if (id == -1) reject(player, "Must supply a valid interface ID!")

            val def = IfaceDefinition.forId(id)
            notify(player, logToConsole = true, message = "Text for $def:")

            for (child in def.children) {
                if (child.type != ComponentType.TEXT) continue
                notify(player, logToConsole = true, message = "$child - ${child.text} - ${child.activeText}")
            }
        }

        define ("listifmodels", Privilege.ADMIN, "::listifmodels <lt>id<gt>", "Prints all default model values and their child index.") {player, args ->
            val id = args.getOrNull(1)?.toIntOrNull() ?: -1
            if (id == -1) reject(player, "Must supply a valid interface ID!")

            val def = IfaceDefinition.forId(id)
            notify(player, logToConsole = true, message = "Models for $def:")

            for (child in def.children) {
                if (child.type != ComponentType.MODEL) continue
                notify(player, logToConsole = true, message = "$child - ${child.modelId}/${child.activeModelId} - Anim: ${child.modelAnimId}/${child.activeModelAnimId}")
            }
        }
    }
}
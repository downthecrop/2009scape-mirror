package rs09.game.system.command.sets

import core.cache.Cache
import core.cache.def.Definition
import core.cache.def.impl.ItemDefinition
import core.game.node.entity.player.info.login.Response
import core.game.node.entity.player.info.portal.PlayerSQLManager
import core.game.node.item.Item
import core.game.system.SystemManager
import core.game.system.SystemState
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.system.command.Command
import rs09.game.system.command.Privilege
import rs09.game.world.repository.Repository
import kotlin.system.exitProcess

@Initializable
class SystemCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        /**
         * Start an update countdown
         */
        define("update") { player, args ->
            if (args.size > 1) {
                SystemManager.getUpdater().setCountdown(args[1].toInt())
            }
            SystemManager.flag(SystemState.UPDATING)
        }

        /**
         * Cancel an update countdown
         */
        define("cancelupdate") { player, _ ->
            SystemManager.getUpdater().cancel()
        }


        /**
         * Allows a player to reset their password
         */
        define("resetpassword", Privilege.STANDARD) { player, args ->
            if (args.size != 3) {
                reject(player, "Usage: ::resetpassword current new", "WARNING: THIS IS PERMANENT.", "WARNING: PASSWORD CAN NOT CONTAIN SPACES.")
            }
            val oldPass = args[1]
            var newPass = args[2]

            if (PlayerSQLManager.getCredentialResponse(player.details.username, oldPass) != Response.SUCCESSFUL) {
                reject(player, "INVALID PASSWORD!")
            }

            if (newPass.length < 5 || newPass.length > 20) {
                reject(player, "NEW PASSWORD MUST BE BETWEEN 5 AND 20 CHARACTERS")
            }

            if (newPass == player.username) {
                reject(player, "PASSWORD CAN NOT BE SAME AS USERNAME.")
            }

            if (newPass == oldPass) {
                reject(player, "PASSWORDS CAN NOT BE THE SAME")
            }

            newPass = SystemManager.getEncryption().hashPassword(newPass)
            PlayerSQLManager.updatePassword(player.username.toLowerCase().replace(" ", "_"), newPass)
            notify(player, "Password updated successfully.")
        }

        /**
         * Allows an Administrator to reset a password
         */
        define("setpasswordother", Privilege.ADMIN) { player, args ->
            if (args.size != 3) {
                reject(player, "Usage: ::resetpasswordother user new", "WARNING: THIS IS PERMANENT.", "WARNING: PASSWORD CAN NOT CONTAIN SPACES.")
            }
            val otherUser = args[1]
            var newPass = args[2]

            if (PlayerSQLManager.hasSqlAccount(otherUser, "username")) {

                if (newPass.length < 5 || newPass.length > 20) {
                    reject(player, "NEW PASSWORD MUST BE BETWEEN 5 AND 20 CHARACTERS")
                }

                if (newPass == otherUser) {
                    reject(player, "PASSWORD CAN NOT BE SAME AS USERNAME.")
                }

                newPass = SystemManager.getEncryption().hashPassword(newPass)
                PlayerSQLManager.updatePassword(otherUser.toLowerCase().replace(" ","_"),newPass)
                notify(player, "Password updated successfully.")

            } else {
                reject(player, "USER DOES NOT EXIST!")
            }
        }

        define("giveitem", Privilege.ADMIN) { player, args ->
            if (args.size == 3 || args.size == 4) {
                val victim = Repository.getPlayerByName(args[1])
                val itemID = args[2].toIntOrNull()
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1

                if (victim == null) {
                    reject(player, "INVALID TARGET USERNAME.")
                }

                if (itemID == null || itemID <= 0 || itemID > ItemDefinition.getDefinitions().size) {
                    reject(player, "INVALID ITEM ID ENTERED.")
                }

                if (amount > Int.MAX_VALUE || amount <= 0) {
                    reject(player, "INVALID ITEM ID ENTERED.")
                }

                val item = Item(itemID!!, amount)
                val invFull = victim!!.inventory.isFull
                val syntax = if (amount > 1) "items" else "item"

                if (invFull) {
                    victim.bank.add(item)
                } else {
                    victim.inventory.add(item)
                }

                notify(player, "Successfully gave ${victim.username} $amount ${item.name}. ${if (invFull) "The $syntax were sent to their bank." else ""}")
                notify(victim, "You received $amount ${item.name} from ${player.username}. ${if (invFull) "The $syntax were placed in your bank." else ""}")

            } else {
                reject(player, "WRONG USAGE. USE giveitem target itemID || giveitem target itemID amt")
            }
        }

        define("removeitem", Privilege.ADMIN) { player, args ->
            if (args.size == 4 || args.size == 5) {
                val itemLoc = args[1].toLowerCase()
                val victim = Repository.getPlayerByName(args[2])
                val itemID = args[3].toIntOrNull()
                var amount = args.getOrNull(4)?.toIntOrNull() ?: 1

                if (victim == null) {
                    reject(player, "INVALID TARGET USERNAME.")
                }

                if (itemID == null || itemID <= 0 || itemID > ItemDefinition.getDefinitions().size) {
                    reject(player, "INVALID ITEM ID ENTERED.")
                }

                if (amount > Int.MAX_VALUE || amount <= 0) {
                    reject(player, "INVALID ITEM AMOUNT ENTERED.")
                }

                val item = Item(itemID!!, amount)
                var totalItemAmount = 0

                when (itemLoc) {
                    "i", "inv", "inventory" -> {
                        totalItemAmount = victim!!.inventory.getItem(item).amount
                        victim.inventory.remove(item)
                    }
                    "b", "bk", "bank" -> {
                        totalItemAmount = victim!!.bank.getItem(item).amount
                        victim.bank.remove(item)
                    }
                    "e", "equip", "equipment" -> {
                        totalItemAmount = victim!!.equipment.getItem(item).amount
                        victim.equipment.remove(item)
                    }
                    else -> reject(player, "INVALID ITEM LOCATION ENTERED. USE: ", "i, inv, inventory | b, bk, bank | e, equip, equipment")
                }

                if (amount > totalItemAmount) {
                    amount = totalItemAmount
                }

                notify(player, "Successfully removed $amount ${item.name} from ${victim!!.username}.")
                notify(victim, "${player.username} removed $amount ${item.name} from your inventory.")

            } else {
                reject(player, "WRONG USAGE. USE removeitem itemLoc target itemID || removeitem itemLoc target itemID amt",
                    "ItemLoc: inv = inventory | equip = equipment | bank |")
            }
        }

        define("removeitemall", Privilege.ADMIN) { player, args ->
            if (args.size == 3) {
                val victim = Repository.getPlayerByName(args[1])
                val itemID = args[2].toIntOrNull()

                if (victim == null) {
                    reject(player, "INVALID TARGET USERNAME.")
                }

                if (itemID == null || itemID <= 0 || itemID > ItemDefinition.getDefinitions().size) {
                    reject(player, "INVALID ITEM ID ENTERED.")
                }

                /* Handles removing the non noted version */
                val itemInv = Item(itemID!!)
                /* Handles removing the noted version */
                val itemNote = Item(itemInv.noteChange)

                val untotal = victim!!.inventory.getAmount(itemID) + victim.bank.getAmount(itemID) +
                        victim.equipment.getAmount(itemID)

                val nototal = victim.inventory.getAmount(itemNote) + victim.bank.getAmount(itemNote) +
                        victim.equipment.getAmount(itemNote)

                val eqtotal = victim.equipment.getAmount(itemID) + victim.equipment.getAmount(itemNote)

                val total = untotal + nototal + eqtotal

                if (total == 0) {
                    reject(player, "USER HAS NONE OF THOSE ITEMS.")
                }

                victim.inventory.remove(Item(itemID, victim.inventory.getAmount(itemID)))
                victim.bank.remove(Item(itemID, victim.bank.getAmount(itemID)))
                victim.equipment.remove(Item(itemID, victim.equipment.getAmount(itemID)))

                victim.inventory.remove(Item(itemInv.noteChange, victim.inventory.getAmount(itemNote)))
                victim.bank.remove(Item(itemInv.noteChange, victim.bank.getAmount(itemNote)))
                victim.equipment.remove(Item(itemInv.noteChange, victim.equipment.getAmount(itemNote)))

                notify(player, "Successfully removed $total ${itemInv.name} from ${victim.username}.")
                notify(victim, "${player.username} removed $total ${itemInv.name} from your account.")

            } else {
                reject(player, "WRONG USAGE. USE removeitemall target itemID")
            }
        }

        define("potato") { player, _ ->
            player.inventory.add(Item(Items.ROTTEN_POTATO_5733))
        }

        define("shutdown", Privilege.ADMIN) { player, _ ->
            exitProcess(0)
        }

    }
}
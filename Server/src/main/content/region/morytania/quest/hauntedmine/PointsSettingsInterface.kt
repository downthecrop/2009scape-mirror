package content.region.morytania.quest.hauntedmine

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player

/**
 * Haunted Mine interface for the levers and minecarts.
 */

class PointsSettingsInterface : InterfaceListener {

    // ::tele 2769 4521 to check the points settings
    override fun defineInterfaceListeners() {
        on(HauntedMine.POINTS_SETTINGS_IFACE) { player, _, _, buttonID, _, _ ->
            when (buttonID) {
                // button 147 is START.
                // Only allow cart to be sent if there is a fungus in it.
                147 -> checkCartRoute(player)
            }
            return@on true
        }
    }

    private fun checkCartRoute(player: Player) {
        // check if the player has already sent a fungus to the end
        if (getAttribute(player, HauntedMine.ATTR_CART_SENT_SUCCESS, false)) {
            sendPlayerDialogue(player, "I wonder if that cart is anywhere useful now?", FacialExpression.THINKING)
        } else {
            /*
             * Starting at default points settings, the correct points settings are:
             * A - 1
             * B - 1
             * C - 0
             * D - 0
             * E - 1
             * F NON-OPERABLE
             * G NON-OPERABLE
             * H NON-OPERABLE
             * I - 1
             * J - 0
             * K - either
             */
            if (
                getVarbit(player, HauntedMine.LEVER_A_VARBIT) == 1
                && getVarbit(player, HauntedMine.LEVER_B_VARBIT) == 1
                && getVarbit(player, HauntedMine.LEVER_C_VARBIT) == 0
                && getVarbit(player, HauntedMine.LEVER_D_VARBIT) == 0
                && getVarbit(player, HauntedMine.LEVER_E_VARBIT) == 1
                && getVarbit(player, HauntedMine.LEVER_I_VARBIT) == 1
                && getVarbit(player, HauntedMine.LEVER_J_VARBIT) == 0
            ) {
                PointsSettingsCutscene(player).start()
                // if there was a fungus in this cart, record that the cart was sent successfully
                if (getAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED, false)) {
                    setAttribute(player, HauntedMine.ATTR_CART_SENT_SUCCESS, true)
                }

                /*
                 * The cart can come back to the station if:
                 * A - 0
                 * B - 1
                 * C - either
                 * D - 0
                 * E - 1
                 * F NON-OPERABLE
                 * G NON-OPERABLE
                 * H NON-OPERABLE
                 * I - either
                 * J - either
                 * K - either
                 */
            } else if (
                getVarbit(player, HauntedMine.LEVER_A_VARBIT) == 0
                && getVarbit(player, HauntedMine.LEVER_B_VARBIT) == 1
                && getVarbit(player, HauntedMine.LEVER_D_VARBIT) == 0
                && getVarbit(player, HauntedMine.LEVER_E_VARBIT) == 1
            ) {
                // the fungus does not get cleared
                sendMessage(player, "How useful, it's come right back to where it started.")

                /*
                 * Else the cart crashes into the water.
                 */
            } else {
                // clear the fungus out of the cart
                removeAttribute(player, HauntedMine.ATTR_FUNGUS_PLACED)
                sendMessage(player, "Oh dear, the mine cart seems to have sunk.")
            }
        }
    }

}
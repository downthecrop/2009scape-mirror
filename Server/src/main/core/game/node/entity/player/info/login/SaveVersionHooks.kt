package core.game.node.entity.player.info.login

import content.global.skill.summoning.pet.Pets
import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Runs one-time save-version-related hooks.
 * @author Player Name
 */

class SaveVersionHooks : LoginListener {
    override fun login(player: Player) {
        if (player.version < ServerConstants.CURRENT_SAVEFILE_VERSION) {

            if (player.version < 1) { // GL !1811
                // Give out crafting hoods if the player bought any crafting capes when the hoods were not obtainable
                var hasHoods = 0
                var hasCapes = 0
                val searchSpace = arrayOf(player.inventory, player.bankPrimary, player.bankSecondary)
                for (container in searchSpace) {
                    for (hood in container.getAll(Item(Items.CRAFTING_HOOD_9782))) {
                        hasHoods += hood.amount
                    }
                    for (id in arrayOf(Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)) {
                        for (cape in container.getAll(Item(id))) {
                            hasCapes += cape.amount
                        }
                    }
                }
                val need = hasCapes - hasHoods
                if (need > 0) {
                    sendMessage(player, "<col=CC6600>You are being given $need crafting hood(s), because we think you bought $need crafting cape(s) when the hoods were still unobtainable.</col>")
                    addItemOrBank(player, Items.CRAFTING_HOOD_9782, need)
                }

                // Unlock Surok's Theme if eligible
                if (getQuestStage(player, "What Lies Below") > 70) {
                    player.musicPlayer.unlock(250, false)
                }

                // Migrate random-event saved location attributes to the uniform naming scheme
                for (old in arrayOf("/save:drilldemon:original-loc","/save:evilbob:prevlocation","/save:freakyf:location","supexam:loc")) {
                    val oldloc = player.getAttribute(old, player.location)
                    if (oldloc != player.location) {
                        player.setAttribute("/save:original-loc", oldloc)
                    }
                    player.removeAttribute(old)
                }

                // Set the missing tutorial island varp if eligible
                if (getAttribute(player, "/save:tutorial:complete", false)) {
                    setVarp(player, 281, 1000, true)
                }
            }

            if (player.version < 2) { //GL !1799
                // Most of the migration for this MR happens in FamiliarManager.java, but we fix up any pet items here
                val pets = Pets.values()
                for (pet in pets) {
                    for (id in arrayOf(pet.babyItemId, pet.grownItemId, pet.overgrownItemId)) {
                        replaceAllItems(player, id, id)
                        // The trick here is that replaceAllItems ignores the item charge value, and will hence cause it to be lost, making all pets authentically stack again
                    }
                }
            }

            player.version = ServerConstants.CURRENT_SAVEFILE_VERSION
        }
    }
}

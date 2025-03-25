package core.game.node.entity.player.info.login

import content.data.Quests
import content.global.skill.summoning.pet.Pets
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCity
import core.ServerConstants
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Log
import org.rs09.consts.Items
import org.rs09.consts.Vars

/**
 * Runs one-time save-version-related hooks.
 * @author Player Name
 */

class SaveVersionHooks : LoginListener {
    override fun login(player: Player) {
        if (player.version < ServerConstants.CURRENT_SAVEFILE_VERSION) {
            log(this::class.java, Log.FINE, "Upgrading ${player.name} from ${player.version} to ${ServerConstants.CURRENT_SAVEFILE_VERSION}")

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
                if (getQuestStage(player, Quests.WHAT_LIES_BELOW) > 70) {
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

            if (player.version < 3) {
                // Damage control on Plague city. There are a few varbits that should have been set for spawning
                when (getQuestStage(player, Quests.PLAGUE_CITY)){
                    in 6..98 -> setVarbit(player, Vars.VARBIT_QUEST_PLAGUE_CITY_EDMOND_TUNNELS, 1) // Edmond is in the tunnel
                    in 99..100 ->setVarbit(player, Vars.VARBIT_QUEST_PLAGUE_CITY_RESCUE_ELENA, 1)  // Elena is free
                }
            }

            if (player.version < 4) { //GL !2065
                replaceAllItems(player, Items.BLURBERRY_SPECIAL_9520, Items.BLURBERRY_SPECIAL_2064)
                replaceAllItems(player, Items.BLURBERRY_SPECIAL_9521, Items.BLURBERRY_SPECIAL_2065)
                replaceAllItems(player, Items.LAMP_6796, Items.LAMP_2528)
                replaceAllItems(player, Items.RUNE_SHIELDH1_10667, Items.RUNE_SHIELDH1_7336)
                replaceAllItems(player, Items.RUNE_SHIELDH2_10670, Items.RUNE_SHIELDH2_7342)
                replaceAllItems(player, Items.RUNE_SHIELDH3_10673, Items.RUNE_SHIELDH3_7348)
                replaceAllItems(player, Items.RUNE_SHIELDH4_10676, Items.RUNE_SHIELDH4_7354)
                replaceAllItems(player, Items.RUNE_SHIELDH5_10679, Items.RUNE_SHIELDH5_7360)
                replaceAllItems(player, Items.ADAMANT_SHIELDH1_10666, Items.ADAMANT_SHIELDH1_7334)
                replaceAllItems(player, Items.ADAMANT_SHIELDH2_10669, Items.ADAMANT_SHIELDH2_7340)
                replaceAllItems(player, Items.ADAMANT_SHIELDH3_10672, Items.ADAMANT_SHIELDH3_7346)
                replaceAllItems(player, Items.ADAMANT_SHIELDH4_10675, Items.ADAMANT_SHIELDH4_7352)
                replaceAllItems(player, Items.ADAMANT_SHIELDH5_10678, Items.ADAMANT_SHIELDH5_7358)
            }

            // Finish up
            player.version = ServerConstants.CURRENT_SAVEFILE_VERSION
        }
    }
}

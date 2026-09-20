package content.global.skill.construction.decoration.workshop

import content.global.skill.construction.CrestType
import content.global.skill.crafting.HeraldicProduct
import content.region.asgarnia.falador.dialogue.SirReniteeDialogueFile
import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Scenery

/**
 * Handles easel type furniture in a POH, with which players may make heraldic items.
 * @author Bishop
 */

class EaselListener : InteractionListener {

    private val easels = intArrayOf(
        Scenery.HELMET_PLUMING_STAND_13716,
        Scenery.PAINTING_STAND_13717,
        Scenery.BANNER_MAKING_STAND_13718
    )

    private fun checkRequirements(player: Player, product: HeraldicProduct) : Boolean {
        when {
            (!getAttribute(player, SirReniteeDialogueFile.ATTRIBUTE_CREST, false) || player.houseManager.crest == CrestType.NULL) -> {
                sendDialogueLines(player, "You must speak to the chief herald of Falador before you can make", "heraldic items.") // OSRS
                return false
            }
            (!inInventory(player, product.primaryMaterialId)) -> {
                sendDialogue(player, "You need a ${product.primaryMaterialId.asItem().name.lowercase()} to make this.")
                return false
            }
            (product.secondaryMaterialId != null && !inInventory(player, product.secondaryMaterialId)) -> {
                sendDialogue(player, "You need a ${product.secondaryMaterialId.asItem().name.lowercase()} to make this.")
                return false
            }
            (!hasLevelDyn(player, Skills.CONSTRUCTION, product.constructionReq)) -> {
                sendDialogue(player, "You need a Construction level of ${product.constructionReq} to make this.")
                return false
            }
            (!hasLevelDyn(player, Skills.CRAFTING, product.craftingReq)) -> {
                sendDialogue(player, "You need a Crafting level of ${product.craftingReq} to make this.")
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun giveProduct(player: Player, product: HeraldicProduct) {
        val crest = player.houseManager.crest
        if (removeItem(player, product.primaryMaterialId)) {
            if (product.secondaryMaterialId == null || removeItem(player, product.secondaryMaterialId)) {
                addItemOrDrop(player, product.productForCrest(crest))
                rewardXP(player, Skills.CRAFTING, product.craftingXp)
                animate(player, Animations.HUMAN_CRAFT_POH_CREST_3654)
                when (product) {
                    HeraldicProduct.STEEL_HELM, HeraldicProduct.RUNE_HELM ->
                        sendMessage(player, "You make a helmet in your colours.")
                    HeraldicProduct.STEEL_SHIELD, HeraldicProduct.RUNE_SHIELD ->
                        sendMessage(player, "You make a shield with your symbol on.")
                    HeraldicProduct.BANNER ->
                        sendMessage(player, "You make a banner with your symbol on.")
                }
            }
        }
    }

    override fun defineListeners() {
        on(easels, SCENERY, "use", "make-helmet") { player, node ->
            val options = when (node.id) {
                Scenery.HELMET_PLUMING_STAND_13716 ->
                    arrayOf("Plumed steel helmet", "Plumed rune helmet")
                Scenery.PAINTING_STAND_13717 ->
                    arrayOf("Plumed steel helmet", "Plumed rune helmet", "Steel heraldic shield", "Runite heraldic shield")
                Scenery.BANNER_MAKING_STAND_13718 ->
                    arrayOf("Plumed steel helmet", "Plumed rune helmet", "Steel heraldic shield", "Runite heraldic shield", "Heraldic banner")
                else ->
                    return@on false
            }
            val buttonsToProducts = mapOf(
                2 to HeraldicProduct.STEEL_HELM,
                3 to HeraldicProduct.RUNE_HELM,
                4 to HeraldicProduct.STEEL_SHIELD,
                5 to HeraldicProduct.RUNE_SHIELD,
                6 to HeraldicProduct.BANNER
            )
            sendDialogueOptions(player, "What do you want to make?", *options)
            addDialogueAction(player) { player, button ->
                if (checkRequirements(player, buttonsToProducts[button] ?: return@addDialogueAction)) {
                    giveProduct(player, buttonsToProducts[button] ?: return@addDialogueAction)
                }
                return@addDialogueAction
            }
            return@on true
        }

        onUseWith(SCENERY, Items.BOLT_OF_CLOTH_8790, Scenery.BANNER_MAKING_STAND_13718) { player, _, _ ->
            if (checkRequirements(player, HeraldicProduct.BANNER)) {
                giveProduct(player, HeraldicProduct.BANNER)
            }
            return@onUseWith true
        }
    }

}
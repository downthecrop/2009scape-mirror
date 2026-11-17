package content.global.handlers.item.withitem

import core.api.*
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.world.GameWorld
import core.tools.END_DIALOGUE

class AAAAvasOnCape : InteractionListener {
    override fun defineListeners() {
        for (item in setOf(Items.AVAS_ATTRACTOR_10498, Items.AVAS_ACCUMULATOR_10499)) {
            onUseWith(IntType.ITEM, item, Items.FLETCHING_CAPE_9783, Items.FLETCHING_CAPET_9784) { player, used, _ ->
                if (GameWorld.settings?.skillcape_perks != true) {
                    return@onUseWith false
                }
                val alreadyHasAvas = getAttribute(player, ATTRIBUTE, false)
                if (alreadyHasAvas) {
                    sendDialogue(player, "You have already stored the components of Ava's device into your cape.")
                } else {
                    openDialogue(player, object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> dialogue("This will destroy this ${used.name} permanently.").also { stage++ }
                                1 -> interpreter!!.sendOptions("Continue?", "Yes", "No").also { stage++ }
                                2 -> when (buttonID) {
                                    1 -> {
                                        if (removeItem(player, used)) {
                                            dialogue(*splitLines("You disassemble the ${used.name} and place the components into their respective pockets on your cape. The undead chicken looks pleased with its new home."))
                                            setAttribute(player, ATTRIBUTE, true)
                                            stage = END_DIALOGUE
                                        }
                                    }
                                    2 -> end()
                                }
                            }
                        }
                    })
                }
                return@onUseWith true
            }
        }
    }

    companion object {
        const val ATTRIBUTE = "/save:cape_perks:an-arrow-attractor:stored"
    }
}
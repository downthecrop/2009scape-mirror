package rs09.game.content.activity.allfiredup

import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class KingRoaldAFUMiniDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> npc("Did what?").also { stage++ }
            1 -> {
                if (player!!.getAttribute("afu-mini:adze", false)) {
                    player("I lit all 14 beacons at once!")
                } else if (player!!.getAttribute("afu-mini:gloves", false)) {
                    player("I lit 10 beacons at once!")
                } else if (player!!.getAttribute("afu-mini:ring", false)) {
                    player("I lit 6 beacons at once!")
                }
                stage++
            }

            2 -> {
                npc("Oh, wonderful! Here is your reward then.")
                if (player!!.getAttribute(
                        "afu-mini:adze",
                        false
                    )
                ) if (player!!.inventory.add(Item(Items.INFERNO_ADZE_13661))) player!!.removeAttribute("afu-mini:adze")
                if (player!!.getAttribute(
                        "afu-mini:gloves",
                        false
                    )
                ) if (player!!.inventory.add(Item(Items.FLAME_GLOVES_13660))) player!!.removeAttribute("afu-mini:gloves")
                if (player!!.getAttribute(
                        "afu-mini:ring",
                        false
                    )
                ) if (player!!.inventory.add(Item(Items.RING_OF_FIRE_13659))) player!!.removeAttribute("afu-mini:ring")
                stage = END_DIALOGUE
            }

            END_DIALOGUE -> end()
        }
    }
}
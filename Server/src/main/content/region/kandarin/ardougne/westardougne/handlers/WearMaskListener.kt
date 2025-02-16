package content.region.kandarin.ardougne.westardougne.handlers

import content.data.Quests
import core.api.inBorders
import core.api.isQuestComplete
import core.api.openDialogue
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class WearMaskListener : InteractionListener {
    override fun defineListeners() {
        onUnequip(Items.GAS_MASK_1506){ player, _ ->
            if (isQuestComplete(player, Quests.BIOHAZARD)){
                return@onUnequip true
            }
            else{
                if(
                    inBorders(player, 2511, 3266, 2556, 3334) ||
                    inBorders(player, 2464, 3281, 2511, 3334) ||
                    inBorders(player, 2461, 3281,2463, 3322) ||
                    inBorders(player, 2435, 3307, 2463, 3322)
                ){
                    openDialogue(player, MaskChat())
                    return@onUnequip false
                }
                return@onUnequip true
            }
        }
    }
}

class MaskChat : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        playerl(FacialExpression.WORRIED, "I should probably keep the gas mask on whilst I'm in West Ardougne.").also { stage = END_DIALOGUE }
    }

}


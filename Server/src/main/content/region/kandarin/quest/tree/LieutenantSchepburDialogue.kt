package content.region.kandarin.quest.tree

import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.tools.END_DIALOGUE

class LieutenantSchepburDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage) {
            0 -> npcl(FacialExpression.OLD_NORMAL, "Move into position lads! eh? Who are you and what do you want?").also { stage++ }
            1 -> playerl("Who are you then?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_NORMAL, "Lieutenant Schepbur, commanding officer of the new Armoured Tortoise Regiment.").also { stage++ }
            3 -> playerl("There's only two tortoises here, that's hardly a regiment.").also { stage++ }
            4 -> npcl(FacialExpression.OLD_NORMAL, "This is just the beginning! Gnome breeders and trainers are already working to expand the number of units. Soon we'll have hundreds of these beauties, nay thousands! And they will not only carry mages and").also { stage++ }
            5 -> npcl(FacialExpression.OLD_NORMAL, "archers but other fiendish weapons of destruction of gnome devising. An army of giant tortoises will march upon this battlefield and rain the fire of our wrath upon all our enemies! Nothing will be able to stop us!").also { stage++ }
            6 -> playerl("Oooookayy...... I'll leave you to it then....").also { stage = END_DIALOGUE }
        }
    }
}
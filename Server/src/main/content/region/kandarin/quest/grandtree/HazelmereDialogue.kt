package content.region.kandarin.quest.grandtree

import content.region.kandarin.quest.grandtree.TheGrandTree.Companion.questName
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items

class HazelmereDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, questName)) {
            10 -> {
                if(player!!.hasItem(Item(Items.BARK_SAMPLE_783))){
                    when (stage) {
                        0 -> sendDialogue(player!!,"The mage starts to speak but all you hear is").also { stage++ }
                        1 -> npcl("Blah. Blah, blah, blah, blah...blah!").also { stage++ }
                        2 -> sendDialogue(player!!,"You give the bark sample to Hazelmere. The mage carefully examines the sample.").also { stage++ }
                        3 -> npcl("Blah, blah...Daconia...blah, blah.").also { stage++ }
                        4 -> playerl("Can you write this down and I'll try and translate it?").also { stage++ }
                        5 -> npcl("Blah, blah?").also { stage++ }
                        6 -> sendDialogue(player!!,"You make a writing motion. The mages scribbles something down on a scroll. Hazelmere has given you the scroll.").also {
                            if(removeItem(player!!, Items.BARK_SAMPLE_783)){
                                addItemOrDrop(player!!, Items.HAZELMERES_SCROLL_786)
                            }
                            setQuestStage(player!!, questName, 20)
                            stage = END_DIALOGUE
                        }
                    }
                }
            }
            20 -> {
                when (stage) {
                    0 -> npcl("Blah, blah....Daconia...blah, blah.").also { stage++ }
                    1 -> sendDialogue(player!!,"You still can't understand Hazelmere. The mage wrote it down for you on a scroll.").also {
                        if(!player!!.hasItem(Item(Items.HAZELMERES_SCROLL_786))){
                            addItemOrDrop(player!!, Items.HAZELMERES_SCROLL_786)
                        }
                        stage = END_DIALOGUE
                    }
                }
            }
            else -> npcl("Blah, blah...blah, blah.").also { stage = END_DIALOGUE }
        }
    }
}
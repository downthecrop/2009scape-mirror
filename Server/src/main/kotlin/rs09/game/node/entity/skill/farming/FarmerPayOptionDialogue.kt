package rs09.game.node.entity.skill.farming

import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class FarmerPayOptionDialogue(val patch: Patch): DialogueFile() {
    var item: Item? = null
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            START_DIALOGUE -> {
                item = patch.plantable?.protectionItem
                val protectionText = when(item?.id){
                    Items.COMPOST_6032 -> if(item?.amount == 1) "bucket of compost" else "buckets of compost"
                    Items.POTATOES10_5438 -> if(item?.amount == 1) "sack of potatoes" else "sacks of potatoes"
                    Items.ONIONS10_5458 -> if(item?.amount == 1) "sack of onions" else "sacks of onions"
                    Items.CABBAGES10_5478 -> if(item?.amount == 1) "sack of cabbages" else "sacks of cabbages"
                    Items.JUTE_FIBRE_5931 -> "jute fibres"
                    Items.APPLES5_5386 -> if(item?.amount == 1) "basket of apples" else "baskets of apples"
                    Items.MARIGOLDS_6010 -> "harvest of marigold"
                    Items.TOMATOES5_5968 -> if(item?.amount == 1) "basket of tomatoes" else "baskets of tomatoes"
                    Items.ORANGES5_5396 -> if(item?.amount == 1) "basket of oranges" else "baskets of oranges"
                    Items.COCONUT_5974 -> "coconuts"
                    Items.STRAWBERRIES5_5406 -> if(item?.amount == 1) "basket of strawberries" else "baskets of strawberries"
                    Items.BANANAS5_5416 -> if(item?.amount == 1) "basket of bananas" else "baskets of bananas"
                    else -> item?.name?.toLowerCase()
                }
                if(item == null) npc("Sorry, I won't protect that.").also { stage = END_DIALOGUE }
                else{
                    npc("I would like ${item?.amount} $protectionText","to protect that patch.")
                    stage++
                }
            }

            1 -> options("Sure!","No, thanks.").also { stage++ }
            2 -> {
                if(player!!.inventory.containsItem(item)){
                    player("Here you go.").also { stage = 10 }
                } else {
                    item = Item(item!!.noteChange,item!!.amount)
                    if(player!!.inventory.containsItem(item)){
                        player("Here you go.").also { stage = 10 }
                    } else {
                        player("I don't have that to give.").also { stage = 20 }
                    }
                }
            }

            10 -> {
                if(player!!.inventory.remove(item)){
                    npc("Thank you! I'll keep an eye on this patch.").also { stage = END_DIALOGUE }
                    patch?.protectionPaid = true
                } else {
                    npc("That stuff just... vanished....").also { stage = END_DIALOGUE }
                }
            }

            20 -> {
                npc("Come back when you do.")
                stage = END_DIALOGUE
            }

        }
    }
}
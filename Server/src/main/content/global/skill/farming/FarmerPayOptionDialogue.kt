package content.global.skill.farming

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items

class FarmerPayOptionDialogue(val patch: Patch, val quickPay: Boolean = false): DialogueFile() {
    var item: Item? = null
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> {
                if (patch.patch.type == PatchType.TREE_PATCH && patch.plantable != null && patch.isGrown()) {
                    // This is for the right-click "Pay" option; full dialogue is in GardenerDialoguePlugin
                    showTopics(
                        Topic("Yes, get rid of the tree.", 300, true),
                        Topic("No thanks.", END_DIALOGUE, true),
                        title = "Pay 200 gp to have the tree chopped down?"
                    )
                } else if (patch.protectionPaid) {
                    npc("I don't know what you're talking about - I'm already", "looking after that patch for you.").also { stage = 100 }
                } else if (patch.isDead) {
                    npc("That patch is dead - it's too late for me to do", "anything about it now.").also { stage = END_DIALOGUE }
                } else if (patch.isDiseased) {
                    npc("That patch is diseased - I can't look after it", "until it has been cured.").also { stage = END_DIALOGUE } // this dialogue is not authentic
                } else if (patch.isWeedy() || patch.isEmptyAndWeeded()) {
                    npc(FacialExpression.NEUTRAL, "You don't have anything planted in that patch. Plant", "something and I might agree to look after it for you.").also { stage = END_DIALOGUE }
                } else if (patch.isGrown()) {
                    npc("That patch is already fully grown!", "I don't know what you want me to do with it!").also { stage = END_DIALOGUE }
                } else {
                    item = patch.plantable?.protectionItem
                    val protectionText = when (item?.id) {
                        Items.COMPOST_6032 -> if (item?.amount == 1) "bucket of compost" else "buckets of compost"
                        Items.POTATOES10_5438 -> if (item?.amount == 1) "sack of potatoes" else "sacks of potatoes"
                        Items.ONIONS10_5458 -> if (item?.amount == 1) "sack of onions" else "sacks of onions"
                        Items.CABBAGES10_5478 -> if (item?.amount == 1) "sack of cabbages" else "sacks of cabbages"
                        Items.JUTE_FIBRE_5931 -> "jute fibres"
                        Items.APPLES5_5386 -> if (item?.amount == 1) "basket of apples" else "baskets of apples"
                        Items.MARIGOLDS_6010 -> "harvest of marigold"
                        Items.TOMATOES5_5968 -> if (item?.amount == 1) "basket of tomatoes" else "baskets of tomatoes"
                        Items.ORANGES5_5396 -> if (item?.amount == 1) "basket of oranges" else "baskets of oranges"
                        Items.COCONUT_5974 -> "coconuts"
                        Items.CACTUS_SPINE_6016 -> "cactus spines"
                        Items.STRAWBERRIES5_5406 -> if (item?.amount == 1) "basket of strawberries" else "baskets of strawberries"
                        Items.BANANAS5_5416 -> if (item?.amount == 1) "basket of bananas" else "baskets of bananas"
                        else -> item?.name?.lowercase()
                    }
                    if (item == null) {
                        npc("Sorry, I won't protect that.").also { stage = END_DIALOGUE }
                    } else if (quickPay && !(inInventory(player!!, item!!.id, item!!.amount) || inInventory(player!!, note(item!!).id, note(item!!).amount))) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc(FacialExpression.HAPPY, "I want $amount $protectionText for that.")
                        stage = 200
                    } else if (quickPay) {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        showTopics(
                            Topic("Yes", 20, true),
                            Topic("No", END_DIALOGUE, true),
                            title = "Pay $amount $protectionText?"
                        )
                    } else {
                        val amount = if (item?.amount == 1) "one" else item?.amount
                        npc("If you like, but I want $amount $protectionText for that.")
                        stage++
                    }
                }
            }

            1 -> {
                if (!(inInventory(player!!, item!!.id, item!!.amount) || inInventory(player!!, note(item!!).id, note(item!!).amount))) {
                    player("I'm afraid I don't have any of those at the moment.").also { stage = 10 }
                } else {
                    showTopics(
                        Topic(FacialExpression.NEUTRAL, "Okay, it's a deal.", 20),
                        Topic(FacialExpression.NEUTRAL, "No, that's too much.", 10)
                    )
                }
            }

            10 -> npc("Well, I'm not wasting my time for free.").also { stage = END_DIALOGUE }

            20 -> {
                if (removeItem(player!!, item) || removeItem(player!!, note(item!!))) {
                    patch.protectionPaid = true
                    // Note: A slight change in this dialogue was seen in a December 2009 video - https://youtu.be/7gVh42ylQ48?t=138
                    npc("That'll do nicely, ${if (player!!.isMale) "sir" else "madam"}. Leave it with me - I'll make sure", "those crops grow for you.").also { stage = END_DIALOGUE }
                } else {
                    npc("This shouldn't be happening. Please report this.").also { stage = END_DIALOGUE }
                }
            }

            100 -> player("Oh sorry, I forgot.").also { stage = END_DIALOGUE }

            // Right-click "Pay" - protect patch - player doesn't have payment
            200 -> player(FacialExpression.NEUTRAL, "Thanks, maybe another time.").also { stage = END_DIALOGUE }

            // Right-click "Pay" - chop down tree
            300 -> {
                if (removeItem(player!!, Item(Items.COINS_995, 200))) {
                    patch.clear()
                    dialogue("The gardener obligingly removes your tree.").also { stage = END_DIALOGUE }
                } else {
                    dialogue("You need 200 gp to pay for that.").also { stage = END_DIALOGUE } // not authentic
                }
            }

        }
    }
}

package content.region.misc.keldagrim.dialogue

import core.api.*
import core.game.interaction.InteractionListener
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Hirko, the crossbow salesdwarf in Keldagrim. Also sells the bolt pouch.
 */

class HirkoDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.HIRKO_4558, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, HirkoDialogueFile(), node as NPC)
            return@on true
        }
    }

}

// Note that Hirko needs to use the FacialExpression.OLD_* chatAnims, otherwise his head is tiny.
class HirkoDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        npc(FacialExpression.OLD_HAPPY, "Hello there!")
        player(ChatAnim.FRIENDLY, "Hello, what's in those boxes?")

        npc(FacialExpression.OLD_NORMAL, "Aha, interested in my crossbows are you?")
        player(ChatAnim.HALF_ASKING, "Are they any good?")

        npc(
            FacialExpression.OLD_NORMAL,
            "They're dwarven engineering at its best. If you've got enough skill in crafting, smithing and fletching, you can even make them yourself. You can buy some of the parts here and make the rest yourself."
        )

        goto("main_opts")

        label("main_opts")
        options(
            DialogueOption(
                "make_one_myself",
                "How do I make one for myself?",
                expression = ChatAnim.HALF_ASKING
            ),
            DialogueOption(
                "what_about_ammo",
                "What about ammo?",
                expression = ChatAnim.HALF_ASKING
            ),
            DialogueOption(
                "thanks_for_telling_me",
                "Thanks for telling me. Bye!",
                expression = ChatAnim.FRIENDLY
            )
        )

        label("make_one_myself") {
            npc(
                FacialExpression.OLD_NORMAL,
                "Well, firstly you'll need to chop yourself some wood, then use a knife on the wood to whittle out a nice crossbow stock like these here."
            )
            player(ChatAnim.NEUTRAL, "Wood fletched into stock... check")

            npc(
                FacialExpression.OLD_NORMAL,
                "Then get yourself some metal and a hammer and smith yourself some limbs for the bow, mind that you use the right metals and woods though as some wood is too light to use with some metal and vice versa."
            )
            player(ChatAnim.ASKING, "Which goes with which?")

            npc(
                FacialExpression.OLD_NORMAL,
                "Wood and Bronze as they're basic materials, Oak and Blurite, Willow and Iron, Steel and Teak, Mithril and Maple, Adamantite and Mahogany and finally Runite and Yew."
            )
            player(ChatAnim.ASKING, "Ok, so I have my stock and a pair of limbs... what now?")

            npc(
                FacialExpression.OLD_NORMAL,
                "Simply take a hammer and smack the limbs firmly onto the stock. You'll then need a string, only they're not the same as normal bows. You'll need to dry some large animal's meat to get sinew,"
            )
            npc(
                FacialExpression.OLD_NORMAL,
                "then spin that on a spinning wheel, it's the only thing we've found to be strong enough for a crossbow."
            )

            goto("magic_logs")
        }

        label("what_about_ammo") {
            npc(
                FacialExpression.OLD_NORMAL,
                "You can smith yourself lots of different bolts, don't forget to flight them with feathers like you do arrows though. You can poison any untipped bolt but there's also"
            )
            npc(
                FacialExpression.OLD_NORMAL,
                "the option of tipping them with gems then enchanting them with runes. This can have some pretty powerful effects."
            )

            player(ChatAnim.STRUGGLE, "Oh my poor bank, how will I store all those?!")
            npc(
                FacialExpression.OLD_NORMAL,
                "Don't you worry, I have just the thing here, a bolt pouch designed specially for holding 4 different types of crossbow bolt."
            )

            player(ChatAnim.ASKING, "Wow, how much?")

            //check if player as a bolt pouch already
            exec { player, _ ->
                if (hasAnItem(player, Items.BOLT_POUCH_9433).exists()) {
                    goto("with_bolt_pouch")
                } else {
                    goto("without_bolt_pouch")
                }
            }

        }

        label("with_bolt_pouch") {
            npc(
                FacialExpression.OLD_NORMAL,
                "Hmm... didn't I see you down here buying one the other day? You can only have one at a time I'm afraid."
            )
            goto("nowhere")
        }

        label("without_bolt_pouch") {
            npc(
                FacialExpression.OLD_NORMAL,
                "Well let's see now... for you? 1500 gold pieces should do the trick."
            )
            goto("bolt_pouch_purchase")
        }

        label("bolt_pouch_purchase") {
            options(
                DialogueOption(
                    "pay_bolt_pouch",
                    "Yes I'll pay the 1500gp.",
                    expression = ChatAnim.NOD_YES
                ),
                DialogueOption(
                    "thanks_for_telling_me",
                    "Thanks for telling me. Bye!",
                    expression = ChatAnim.FRIENDLY
                )
            )
        }

        label("pay_bolt_pouch") {
            npc(FacialExpression.OLD_NORMAL, "Hand it over then.")

            exec { player, _ ->
                // case where player has exactly 1500 coins and no free slots
                if (amountInInventory(player, Items.COINS_995) == 1500 && freeSlots(player) == 0) {
                    if (removeItem(player, Item(Items.COINS_995, 1500))) {
                        addItem(player, Items.BOLT_POUCH_9433)
                        goto("nowhere")
                    }
                }
                // all other cases
                if (inInventory(player, Items.COINS_995, 1500)) {
                    if (freeSlots(player) != 0 && removeItem(player, Item(Items.COINS_995, 1500))) {
                        addItem(player, Items.BOLT_POUCH_9433)
                    } else {
                        sendMessage(
                            player,
                            "You don't have enough inventory space to hold that item."
                        )
                    }

                    goto("nowhere")

                } else {
                    goto("player_broke_af")
                }
            }
        }

        label("player_broke_af") {
            player(ChatAnim.SAD, "Oh, I don't have that much on me.")
            npc(
                FacialExpression.OLD_NORMAL,
                "Best go make some money then, shouldn't take you too long."
            )

            goto("nowhere")
        }

        label("magic_logs") {
            options(
                DialogueOption(
                    "what_about_magic_logs",
                    "What about magic logs?",
                    expression = ChatAnim.ASKING
                ),
                DialogueOption(
                    "thanks_for_telling_me",
                    "Thanks for telling me. Bye!",
                    expression = ChatAnim.FRIENDLY
                )
            )
        }

        label("what_about_magic_logs") {
            npc(
                FacialExpression.OLD_NORMAL,
                "Well.. I don't rightly know... us dwarves don't work with magic, we prefer gold and rock. Much more stable. I guess you could ask the humans at the rangers guild to see if they can do something but I don't want"
            )
            npc(FacialExpression.OLD_HAPPY, "anything to do with it!")
            goto("thanks_for_telling_me")
        }

        label("thanks_for_telling_me") {
            npc(FacialExpression.OLD_HAPPY, "Take care, straight shooting.")
            goto("nowhere")
        }
    }
}
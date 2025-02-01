package content.region.kandarin.yanille.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class LeonDialogue (player: Player? = null) : DialoguePlugin(player) {

    companion object {
        const val WHAT_IS_THIS_PLACE = 10
        const val BUY_GEAR = 20
        const val ABOUT_CBOW = 30
        const val ABOUT_AMMO = 40
        const val BYE = 50
        const val CRAZY = 60
        const val TRADE = 70
        const val MAKE_OWN_AMMO = 80
        const val CRAFT_AMMO = 90
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (hasAnItem(player, Items.HUNTERS_CROSSBOW_10156).exists()) npcl(FacialExpression.HAPPY, "Oh, hey, you have one of my crossbows! How's it working for you?")
        else sendItemDialogue(player, Items.HUNTERS_CROSSBOW_10156,"Leon is gazing intently at the crossbow in his hands.")
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> showTopics(
                IfTopic("It's okay, thanks.", 0, hasAnItem(player, Items.HUNTERS_CROSSBOW_10156).exists()),
                Topic("What is this place?", WHAT_IS_THIS_PLACE),
                Topic("Can you tell me about your crossbow?", ABOUT_CBOW),
                Topic("Tell me about the ammo for your crossbow.", ABOUT_AMMO),
                Topic("I'll be off now, excuse me.", BYE),

            )

            WHAT_IS_THIS_PLACE -> npcl(FacialExpression.NEUTRAL, "This is Aleck's Hunter Emporium. Basically, it's just a shop with a fancy name; you can buy various weapons and traps here.").also { stage++ }
            WHAT_IS_THIS_PLACE + 1 -> showTopics(
                Topic("Can I buy some equipment from the shop then?", BUY_GEAR),
                Topic("Can you tell me about your crossbow?", ABOUT_CBOW),
                Topic("Tell me about the ammo for your crossbow.", ABOUT_AMMO),
                Topic("I'll be off now, excuse me.", BYE)
            )

            BUY_GEAR -> npcl(FacialExpression.NEUTRAL, "Oh, this isn't my shop; the owner is Aleck over there behind the counter.").also { stage++ }
            BUY_GEAR + 1 -> npcl(FacialExpression.NEUTRAL, "I experiment with weapon designs. I'm here because I've been trying to convince people to back my research and maybe sell some of my products - like this one I'm holding - in their shops.").also { stage++ }
            BUY_GEAR + 2 -> npcl(FacialExpression.SAD, "Aleck doesn't seem to be interested, though.").also { stage++ }
            BUY_GEAR + 3 -> showTopics(
                Topic("Can you tell me about your crossbow?", ABOUT_CBOW),
                Topic("Tell me about the ammo for your crossbow.", ABOUT_AMMO),
                Topic("I'll be off now, excuse me.", BYE)
            )

            ABOUT_CBOW -> npcl(FacialExpression.HAPPY, "It's good, isn't it? I designed it to incorporate the bones of various animals in its construction.").also { stage++ }
            ABOUT_CBOW + 1 -> npcl(FacialExpression.HAPPY, "It's a fair bit faster than an ordinary crossbow too; it'll take you far less time to reload between shots.").also { stage++ }
            ABOUT_CBOW + 2 -> showTopics(
                Topic("That's crazy, it'll never work!", CRAZY),
                Topic("That sounds good. Let's trade.", TRADE)
            )

            CRAZY  -> npcl(FacialExpression.HALF_THINKING, "That's what they said about my wind-powered mouse traps, too.").also { stage++ }
            CRAZY + 1 -> playerl(FacialExpression.HALF_WORRIED, "And did they work?").also { stage++ }
            CRAZY + 2 -> npcl(FacialExpression.HALF_THINKING, "Well, they only ran into problems because people kept insisting on trying to use them indoors.").also { stage++ }
            CRAZY + 3 -> npcl(FacialExpression.HAPPY, "Anyway, I think my crossbow invention is showing a lot more promise.").also { stage = 0 }

            TRADE -> {
                end()
                openNpcShop(player, npc.id)
            }

            ABOUT_AMMO -> npcl(FacialExpression.NEUTRAL, "Ah, I admit as a result of its... er... unique construction, it won't take just any old bolts.").also { stage++ }
            ABOUT_AMMO + 1 -> npcl(FacialExpression.NEUTRAL, "If you can supply the materials and a token fee, I'd be happy to make some for you.").also { stage++ }
            ABOUT_AMMO + 2 -> npcl(FacialExpression.NEUTRAL, "I need kebbit spikes, lots of 'em. Not all kebbits have spikes, mind you. You'll be wanting prickly kebbits or, even better, razor-backed kebbits to get material hard enough.").also { stage++ }
            ABOUT_AMMO + 3 -> showTopics(
                Topic("Can't I just make my own?", MAKE_OWN_AMMO),
                Topic("Okay, can you make ammo for me?", CRAFT_AMMO)
            )

            MAKE_OWN_AMMO -> npcl(FacialExpression.HALF_THINKING, "Yes, I suppose you could, although you'll need a steady hand with a knife and a chisel.").also { stage++ }
            MAKE_OWN_AMMO + 1 -> npcl(FacialExpression.HALF_THINKING, "The bolts have an unusual diameter, but basically you'll just need to be able to carve kebbit spikes into straight shafts.").also { stage++ }
            MAKE_OWN_AMMO + 2 -> npcl(FacialExpression.NEUTRAL, "Meanwhile, since you're here, I can make some for you if you have the materials.").also { stage = 0 }

            // OSRS suggests this may be inauthentic and there should be a make x interface.
            // No 2009 sources found saying that though
            CRAFT_AMMO -> npcl(FacialExpression.NEUTRAL, "Sure what type of bolts do you want?").also { stage++ }
            CRAFT_AMMO + 1 -> showTopics(
                Topic("Kebbit bolts.", CRAFT_AMMO + 2),
                Topic("Long kebbit bolts.", CRAFT_AMMO + 3)
            )
            CRAFT_AMMO + 2 -> {
                if (hasAnItem(player!!, Items.KEBBIT_SPIKE_10105).exists() && inInventory(player, Items.COINS_995, 20)){
                    if(removeItem(player, Items.KEBBIT_SPIKE_10105) && removeItem(player, Item(Items.COINS_995, 20))){
                        addItem(player, Items.KEBBIT_BOLTS_10158, 6)
                        sendItemDialogue(player, Items.KEBBIT_BOLTS_10158, "You hand the weapon designer one spike and 20 coins. In return he presents you with 6 bolts.").also { stage = END_DIALOGUE }
                    }
                }
                else{
                    sendItemDialogue(player, Items.KEBBIT_SPIKE_10105, "You need 1 kebbit spike and 20 coins to make 6 kebbit bolts.").also { stage = END_DIALOGUE }
                }
            }
            CRAFT_AMMO + 3 -> {
                if (hasAnItem(player!!, Items.LONG_KEBBIT_SPIKE_10107).exists() && inInventory(player, Items.COINS_995, 24)){
                    if(removeItem(player, Items.LONG_KEBBIT_SPIKE_10107) && removeItem(player, Item(Items.COINS_995, 40))){
                        addItem(player, Items.LONG_KEBBIT_BOLTS_10159, 6)
                        sendItemDialogue(player, Items.LONG_KEBBIT_BOLTS_10159, "You hand the weapon designer one long spike and 40 coins. In return he presents you with 6 long bolts.").also { stage = END_DIALOGUE }
                    }
                }
                else{
                    sendItemDialogue(player, Items.LONG_KEBBIT_SPIKE_10107, "You need 1 long kebbit spike and 40 coins to make 6 kebbit bolts.").also { stage = END_DIALOGUE }
                }
            }

            BYE -> npcl(FacialExpression.NEUTRAL, "Well, if you ever find yourself in need of that innovative edge, you can always find me here.").also { stage++ }
            BYE + 1 -> playerl(FacialExpression.HALF_ROLLING_EYES, "...thanks").also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LEON_5111)
    }
}
package content.region.misthalin.wiztower.handlers.rcguild

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Wizard Korvak in the Runecrafting Guild
 *
 * Sources: https://www.youtube.com/watch?v=hEMS7v7y_PU, https://www.youtube.com/watch?v=o4ZDpuCcIcQ
 * Source for pouch dialogue: https://runescape.wiki/w/Transcript:Wizard_Korvak?oldid=35238175
 * Source: https://runescape.wiki/w/Wizard_Korvak?oldid=708002
 */

class WizardKorvakDialogue : InteractionListener {
    companion object {
        const val medPouchObtainedAttr = "/save:med-pouch-obtained"
    }

    override fun defineListeners() {
        on(intArrayOf(NPCs.WIZARD_KORVAK_8029), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WizardKorvakDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WizardKorvakDialogueFile : DialogueLabeller() {
    override fun addConversation() {

        npc(ChatAnim.SCARED, "AAAAAAAAAAH!") // 10 "A"s, I counted the source lol
        npc("Don't sneak up on me like that.")
        player("Uh, sorry.")
        exec { player, _ ->
            if (inInventory(player, Items.OMNI_TALISMAN_13649)) {
                loadLabel(player, "omni options")
            } else {
                loadLabel(player, "no omni options")
            }
        }

        label("omni options")
        options(
            DialogueOption("pouch", "Can you restore my essence pouches for me?"),
            DialogueOption("omni", "I have an omni-talisman that I would like to attach to a tiara or staff."),
            DialogueOption("jumpy", "Why are you so jumpy?"),
            DialogueOption("nvm", "Never mind.")
        )

        label("no omni options")
        options(
            DialogueOption("pouch", "Can you restore my essence pouches for me?"),
            DialogueOption("jumpy", "Why are you so jumpy?"),
            DialogueOption("nvm", "Never mind.")
        )

        label("pouch")
        exec { player, _ ->
            // degraded large or giant pouches
            if (inInventory(player, Items.LARGE_POUCH_5513)
                || inInventory(player, Items.GIANT_POUCH_5515)) {
                loadLabel(player, "repair")
            } else {
                loadLabel(player, "sell 1")
            }
        }

        label("sell 1")
        npc("You don't have any pouches that need repair...I could sell you a pouch, but only if you don't tell!")
        options(
            DialogueOption("sell", "I'd like to buy a pouch."),
            DialogueOption("nvm", "Never mind.")
        )

        label("repair")
        npc("I can restore them for a price - for a price, indeed. Muahahahahaahaha!")
        player(FacialExpression.ASKING, "Uh, what kind of a price?")
        npc("Whatever the voices tell me to ask for. Currently, they require 9,000 gp for a large pouch and 12,000 gp for a giant pouch.")
        npc("Shhhh, don't tell any one else: I have a connection on the inside and I can sell you pouches too.")
        npc("For a mere 25,000 gp, you can have a large pouch. A reasonable 50,000 gp will get you a giant pouch.")
        options(
            DialogueOption("really repair", "I'd like to have a pouch repaired."),
            DialogueOption("sell", "I'd like to buy a pouch.")
        )

        label("really repair")
        npc("Very well. Let's have a look at it.")
        options(
            DialogueOption("repair large","Repair large pouch for 9,000 gp.", "I'd like to repair my large pouch."),
            DialogueOption("repair giant", "Repair giant pouch for 12,000 gp.", "I'd like to repair my giant pouch."),
            DialogueOption("nvm", "Never mind.")
        )

        label("repair large")
        exec { player, _ ->
            if (inInventory(player, Items.LARGE_POUCH_5513)
                && inInventory(player, Items.COINS_995, 9000)) {
                loadLabel(player, "happy large repair")
            } else {
                loadLabel(player, "no repair for you")
            }
        }

        label("repair giant")
        exec { player, _ ->
            if (inInventory(player, Items.GIANT_POUCH_5515)
                && inInventory(player, Items.COINS_995, 12000)) {
                loadLabel(player, "happy giant repair")
            } else {
                loadLabel(player, "no repair for you")
            }
        }

        label("happy large repair")
        npc("Magic makes me happy, magic makes me glad, magic makes the voices quiet, and nothing rhymes with purple.")
        exec { player, _ ->
            // repair the large pouch
            if (removeItem(player, Item(Items.COINS_995, 9000))) {
                player.pouchManager.repair(Items.LARGE_POUCH_5513)
            }
        }
        goto("nowhere")

        label("happy giant repair")
        npc("Ahhh, the simple act of a transformation spell. So soothing. It makes the voices quiet. Your pouch is repaired.")
        exec { player, _ ->
            // repair the giant pouch
            if (removeItem(player, Item(Items.COINS_995, 12000))) {
                player.pouchManager.repair(Items.GIANT_POUCH_5515)
            }
        }
        goto("nowhere")

        label("no repair for you")
        npc("The voices are angry at you! You have nothing to repair. Leave us be.")
        goto("nowhere")

        label("sell")
        exec { player, _ ->
            // can't have a med pouch, but must have gotten one before
            if (!hasAnItem(player, Items.MEDIUM_POUCH_5510, true).exists()
                && !hasAnItem(player, Items.MEDIUM_POUCH_5511, true).exists()
                && getAttribute(player, WizardKorvakDialogue.medPouchObtainedAttr, false)) {
                loadLabel(player, "med pouch")
            } else {
                loadLabel(player, "really buy")
            }
        }

        // get a free medium pouch
        label("med pouch")
        player("I've lost my medium-sized pouch. Could you replace it?")
        exec { player, _ ->
            if (hasSpaceFor(player, Item(Items.MEDIUM_POUCH_5510))) {
                addItem(player, Items.MEDIUM_POUCH_5510)
            } else sendMessage(player, "You need at least one free inventory space.")
        }
        npc("Lost it, did you? Or did they take it back? CLAIM IT BACK! Quick, better have another before the spoons come.")
        goto("nowhere")

        label("really buy")
        npc("Ah, coins to fund my rock collection.")
        options(
            DialogueOption("large pouch", "Buy a large pouch for 25,000 gp.", "I'd like to buy a large pouch."),
            DialogueOption("giant pouch", "Buy a giant pouch for 50,000 gp.", "I'd like to buy a giant pouch."),
            title = "Which pouch would you like to buy?"
        )

        // buy a large pouch
        label("large pouch")
        exec { player, _ ->
            if (!hasAnItem(player, Items.LARGE_POUCH_5512, true).exists()
                && !hasAnItem(player, Items.LARGE_POUCH_5513, true).exists()) {
                if (amountInInventory(player, Items.COINS_995) == 25000 && freeSlots(player) == 0) {
                    if (removeItem(player, Item(Items.COINS_995, 25000))) {
                        addItem(player, Items.LARGE_POUCH_5512)
                    }
                } else if (inInventory(player, Items.COINS_995, 25000)) {
                    if (hasSpaceFor(player, Item(Items.LARGE_POUCH_5512))
                        && removeItem(player, Item(Items.COINS_995, 25000))) {
                        addItem(player, Items.LARGE_POUCH_5512)
                    } else sendMessage(player, "You need at least one free inventory space.")
                } else sendMessage(player, "You need at least 25,000 coins.")
            } else loadLabel(player, "only 1 large")
        }
        goto("nowhere")

        // buy a giant pouch
        label("giant pouch")
        exec { player, _ ->
            if (!hasAnItem(player, Items.GIANT_POUCH_5514, true).exists()
                && !hasAnItem(player, Items.GIANT_POUCH_5515, true).exists()) {
                if (amountInInventory(player, Items.COINS_995) == 50000 && freeSlots(player) == 0) {
                    if (removeItem(player, Item(Items.COINS_995, 50000))) {
                        addItem(player, Items.GIANT_POUCH_5514)
                    }
                } else if (inInventory(player, Items.COINS_995, 50000)) {
                    if (hasSpaceFor(player, Item(Items.GIANT_POUCH_5514))
                        && removeItem(player, Item(Items.COINS_995, 50000))) {
                        addItem(player, Items.GIANT_POUCH_5514)
                    } else sendMessage(player, "You need at least one free inventory space.")
                } else sendMessage(player, "You need at least 50,000 coins.")
            } else loadLabel(player, "only 1 giant")
        }
        goto("nowhere")

        label("only 1 large")
        npc("You already own a large pouch! Don't you know you'll unbalance the world by having two? Dogs will hate cats, mice will dance, and they will all come for me. No, no pouch for you.")
        goto("nowhere")

        label("only 1 giant")
        npc("No, no, no. They told me when I was there. Only one precious pouch, per size, per person. No more. I cannot sell you this pouch, or they might make me return there for punishments.")
        goto("nowhere")

        label("omni")
        options(
            DialogueOption("tiara", "Omni-tiara.", "I'd like to attach my omni-talisman to a tiara. Do you know how to do that?"),
            DialogueOption("staff", "Omni-staff.", "I'd like to attach my omni-talisman to a staff. Do you know how to do that?"),
            title = "Which would you like to make?"
        )

        label("tiara")
        exec { player, _ ->
            if (inInventory(player, Items.OMNI_TALISMAN_13649)
                && inInventory(player, Items.TIARA_5525)) {
                loadLabel(player, "make tiara")
            } else {
                loadLabel(player, "no tiara")
            }
        }

        label("make tiara")
        npc("Perhaps...transform...carry the three and divide by the alteration factor - chickens.")
        npc("There! A pretty tiara for a pretty lady.")
        exec { player, _ ->
            if (removeItem(player, Items.OMNI_TALISMAN_13649)
                && removeItem(player, Items.TIARA_5525)) {
                addItem(player, Items.OMNI_TIARA_13655)
            }
        }
        player(if (player!!.isMale) "Oi, I'm a bloke!" else "Thanks!") // not sure what the girl option is.
        npc(if (player!!.isMale) "So?" else "You're welcome.") // not sure what the girl option is.
        goto("nowhere")

        label("no tiara")
        line("You need a silver tiara and an omni-talisman to make this.")
        goto("nowhere")

        label("staff")
        exec { player, _ ->
            if (inInventory(player, Items.OMNI_TALISMAN_13649)
                && inInventory(player, Items.RUNECRAFTING_STAFF_13629)) {
                loadLabel(player, "make staff")
            } else {
                loadLabel(player, "no staff")
            }
        }

        label("make staff")
        npc("A little twist here, a little adhesive spell here. Kapow!")
        npc("A staff for all your runecrafting needs.")
        exec { player, _ ->
            if (removeItem(player, Items.OMNI_TALISMAN_13649)
                && removeItem(player, Items.RUNECRAFTING_STAFF_13629)) {
                addItem(player, Items.OMNI_TALISMAN_STAFF_13642)
            }
        }
        goto("nowhere")

        label("no staff")
        line("You need a runecrafting staff and an omni-talisman to make this.")
        goto("nowhere")

        label("jumpy")
        npc("I am not jumpy! I am insane. There is a difference.")
        player(FacialExpression.ASKING, "If you are insane, wouldn't you think you were sane and be so insensible to the insanity that is you?")
        npc("Yebno.") // authentic yebno
        player(FacialExpression.ASKING, "Beg your pardon?")
        npc("Yebno. It's a fast way of saying Yes Maybe No. Since I didn't know the answer to your question I gave you all of the answers.")
        player(FacialExpression.ASKING, "How did you end up in your rather peculiar mindset?")
        npc("They sent me to the place. They knew the dark wizards were there and someone had betrayed them. Us. So they sent me to spy. To the place.")
        npc("They sent me and would not let me come back.")
        player(FacialExpression.SUSPICIOUS, "Who sent you? What betrayal?")
        npc("He sits there with the spinning light, always thinking. One of us led them to the place where the pickaxe hammers and so the betrayal happened.")
        npc("Please don't make me talk about it.")
        player(FacialExpression.GUILTY,"Okay, we don't have to talk about it.")
        goto("nowhere")

        label("nvm")
        goto("nowhere")
    }
}
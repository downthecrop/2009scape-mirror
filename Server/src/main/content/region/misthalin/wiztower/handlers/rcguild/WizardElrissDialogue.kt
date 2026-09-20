package content.region.misthalin.wiztower.handlers.rcguild

import content.region.misthalin.wiztower.handlers.rcguild.OmniPiece.Companion.ATTR_SHOW_TALISMANS
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Wizard Elriss of the Runecrafting Guild
 *
 * Partial dialogue: https://www.youtube.com/watch?v=hEMS7v7y_PU
 * Omni-tally dialogue: https://www.youtube.com/watch?v=qcf1_9beoX8 (this video has the best sign-off lmao)
 * The partial dialogue matches with older wiki source, so I have used that as fill-in: https://runescape.wiki/w/Transcript:Wizard_Elriss?oldid=6676213
 */

class WizardElrissDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.WIZARD_ELRISS_8032), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WizardElrissDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WizardElrissDialogueFile : DialogueLabeller() {

    // var used when showing elriss talismans. normally it's the item ID
    var lastShownItem: Int = 0

    override fun addConversation() {

        npc("","Welcome to the Runecrafting Guild.","") // extra empty lines to fix some weird chathead bug, thx Bishop.
        options(
            DialogueOption("tokens", "I have some tokens I'd like to cash in."),
            DialogueOption("what place", "What is this place?", expression = ChatAnim.ASKING),
            DialogueOption("what do", "What can I do here?", expression = ChatAnim.ASKING),
            DialogueOption("nvm", "Never mind.")
        )

        label("tokens")
        exec { player, _ ->
            openInterface(player, Components.RCGUILD_REWARDS_779)
        }
        goto("nowhere")

        label("what place")
        npc(ChatAnim.FRIENDLY, "This is the Runecrafting Guild, as I said. After the secret of Runecrafting was re-discovered, I set up the guild as a place for the most advanced runecrafters to work together.")
        options(
            DialogueOption("work towards", "Work together towards what?", expression = ChatAnim.ASKING),
            DialogueOption("where are", "Where are we, exactly?", expression = ChatAnim.ASKING),
            DialogueOption("what do", "What can I do here?", expression = ChatAnim.ASKING),
            DialogueOption("nvm", "Never mind.")
        )

        label("work towards")
        npc(ChatAnim.FRIENDLY, "Towards a greater understanding of Runecrafting, of course. The basics of Runecrafting may have been re-discovered, but many of the secrets of the first Wizards' Tower remain unknown.")
        options(
            DialogueOption("what secrets", "What secrets?", expression = ChatAnim.ASKING),
            DialogueOption("acantha", "Acantha and Vief are hardly working together!"),
            DialogueOption("where are", "Where are we, exactly?", expression = ChatAnim.ASKING),
            DialogueOption("what do", "What can I do here?", expression = ChatAnim.ASKING),
            DialogueOption("nvm", "Never mind.")
        )

        label("what secrets")
        npc("Oh, nothing to interest an adventurer such as yourself, I'm sure.")
        options(
            DialogueOption("interested", "I am interested."),
            DialogueOption("!interested", "Yeah, I'm not really interested.")
        )

        label("interested")
        npc("We all have our projects, adventurer. Acantha and Vief are happy to involve junior runecrafters in their feud, but others prefer to keep their research private until it is revealed.")
        npc(ChatAnim.SAD, "An idea may be subjected to cruel ridicule if it is aired prematurely, as I have learned to my cost. You must forgive me if I am not so forthcoming again.")
        options(
            DialogueOption("nvm", "Never mind, then."),
            DialogueOption("go on", "Go on, tell me.", expression = ChatAnim.FRIENDLY)
        )

        label("go on")
        npc(ChatAnim.ANNOYED, "Leave me be!")
        goto("nowhere")

        label("!interested")
        npc("Was there something else you wanted?")
        options(
            DialogueOption("tokens", "I have some tokens I'd like to cash in."),
            DialogueOption("what place", "What is this place?", expression = ChatAnim.ASKING),
            DialogueOption("what do", "What can I do here?", expression = ChatAnim.ASKING),
            DialogueOption("nvm", "Never mind.")
        )

        label("acantha")
        npc("Aren't they? They think that their debate about orb colour is so important, but do you really think it matters which team you join?")
        goto("matters options")

        label("matters options")
        options(
            DialogueOption("matters", "Of course it matters."),
            DialogueOption("!matters", "No, I suppose not."),
            DialogueOption("nvm", "Never mind.")
        )

        label("matters")
        npc("Of course it does, of course it does. Be careful which team you join, then. I'll accept your reward tokens, either way.")
        goto("orb proj Qs")

        label("!matters")
        npc("No. The important thing is that the orbs get pushed back into the altars, whatever colour they are.")
        goto("orb proj Qs")

        label("where are")
        npc("You will notice that, whenever you use a Runecrafting altar, you enter another plane: a self-contained island, or cave, or some other place, which contains the true altar.")
        npc("These temples are not exactly in RuneScape. They are pocket dimensions unto themselves: areas of folded space created by the energy of the rune altar.")
        options(
            DialogueOption("similar", "So we're in something similar?"),
            DialogueOption("what has to do", "What does that have to do with the guild?"),
            DialogueOption("astral", "Not the Astral Altar. That has no pocket dimension.")
        )

        label("similar")
        npc("Quite right. This is a shadow of Wizard's Tower, created by our own magic. what better place to study the mysteries of Runecrafting?")
        goto("nowhere")

        label("what has to do")
        npc("Don't you see? The Runecrafting Guild exists in a similar pocket dimension, created by our own magic. What better place to study the mysteries of Runecrafting?")
        goto("nowhere")

        label("astral")
        npc("Quite right. I have heard of the Astral Altar, although I have not been there myself. The lunar wizards have found a way to keep the altar open.")
        npc("Their magic has flattened out the space around the altar so the pocket dimension becomes part of normal space.")
        goto("nowhere")

        label("what do")
        npc("Wizard Acantha and Wizard Vief are running The Great Orb Project. It requires large numbers of runecrafters, so you should speak with them if you want something to do.")
        npc("Wizard Korvak has visited the Abyss and can repair abyssal pouches, I, myself, am working on a new kind of talisman: the omni-talisman.")
        options(
            DialogueOption("orb", "Tell me about Acantha and Vief's project."),
            DialogueOption("omni", "Tell me about the omni-talisman."),
            DialogueOption("sack", "Tell me about Wizard Korvak's pouch repairs."),
        )

        label("orb")
        npc("The Orb Proj...I beg your pardon, The Great Orb Project? It's truly fascinating. Wizards Acantha and Vief have found that energy leaks out of some of the Runecrafting altars.")
        npc("They are recruiting teams of experienced runecrafters such as yourself, to force the energy back in.")
        goto("instructions")

        label("instructions")
        npc("Join one of the teams by speaking to Wizard Acantha of Wizard Vief.")
        npc("When the wizards have enough helpers, I will open a portal to the Air Altar. The energy appears in the form of floating orbs. These can be moved by means of wands that attract or repel them.")
        npc("Acantha or Vief will give you one of each wand. Your goal is to move the correct colour orb to the altar stone, while keeping the other orbs away.")
        npc("Wizard Acantha favours green orbs, while Wizard Vief favours yellow ones. You will also have a third magic wand, which allows you to create magical barriers to block the opposing team's orbs.")
        npc("After two minutes the team that absorbed the most orbs wins that altar. I then open the portal to the next altar in the sequence. After you have visited all eight altars, you will be returned here.")
        goto("orb proj Qs")

        label("orb proj Qs")
        options(
            DialogueOption("me me me", "What's in it for me?"),
            DialogueOption("instructions", "Could you go over the instructions again?"),
            DialogueOption("top orb", "Which colour orb is best?"),
            DialogueOption("ty", "Thanks."),
        )

        label("me me me")
        npc("A fair question. We have agreed on a token scheme that allows you to choose from several rewards. When you return from the last altar, your senior wizard will give you a number of tokens.")
        npc("You will get 50 tokens per altar that your team captured, provided that you contributed to the capture in some way.")
        npc("You will get an extra 100 tokens if your team captured more altars overall, or 50 extra if it is a draw. You can exchange the tokens for rewards by speaking to me.")
        npc("You may also find rune essence appearing in your inventory at the end of each round. This is a side-product of the absorption process and you are free to use it as you wish.")
        player("What rewards are there?")
        npc("The rewards include runemaster robes, designed to protect you while Runecrafting. These robes also let you move orbs a little further - if you wear robes of the same colour as the orb.")
        npc("Another reward is the Runecrafting staff. This can be combined with a talisman, in the same way that a tiara can.")
        npc("I also offer teleport tablets to the various altars. You may also trade your tokens in for talismans and certificates you can exchange at a bank for rune essence.")
        goto("orb proj Qs")

        label("top orb")
        npc("Wizard Acantha believes that the green orbs are best. Wizard Vief believe that the yellow ones are. You should help out the wizard whose team you join.")
        player("But what do you think?")
        npc("Does it matter?")
        goto("matters options")

        label("ty")
        goto("nowhere")

        label("omni")
        npc("Ever since the Duke of Lumbridge sent the first air talisman to Sedridor, I have studied the talismans in great detail. I believe I can create a new form of talisman that combines the properties of all of them.")
        npc("The omni-talisman will allow you to access any of the Runecrafting altars. It can be combined with a tiara or a staff, just like an ordinary talisman.")
        npc("If you show me each type of known talisman, I will create an omni-talisman for you. For each talisman you show me, I will also teach you a bit about Runecrafting.")
        options(
            DialogueOption("tally", "I have a talisman to show you."),
            DialogueOption("nvm", "Never mind.")
        )

        label("tally")
        exec { player, _ ->
            // first check if omni tally reqs are already met
            if (checkOmni(player)) {
                loadLabel(player, "all gud")
            } else {
                // else check for more tallys to show
                lastShownItem = tallyCheck(player)
                if (lastShownItem != 0) {
                    loadLabel(player, "show item")
                } else {
                    loadLabel(player, "gib moar")
                }
            }
        }

        label("show item")
        manual { _, _ ->
            interpreter!!.sendItemMessage(lastShownItem, "You show Elriss the ${getItemName(lastShownItem).lowercase()}.")
        }
        exec { player, _ ->
            if (checkOmni(player)) {
                loadLabel(player, "all gud")
            } else {
                loadLabel(player, "tally")
            }
        }

        label("gib moar")
        npc("You'll need to show me more talismans before I can give you an omni-talisman.")
        goto("nowhere")

        label("all gud")
        npc(FacialExpression.HAPPY, "Excellent! You've shown me enough talismans. I can give you an omni-talisman now.")
        exec { player, _ ->
            rewardOmni(player)
        }
        npc("Take good care of it. It is a powerful artefact and may only be used by experienced runecrafters, such as yourself.")
        goto("nowhere")

        label("sack")
        npc("Wizard Korvak is the only one of us to have visited the Abyss. He learned about rune pouches and how to repair them.")
        npc("None of us quite knows how he does it, and I'm not sure he does either, but it seems to work.")
        goto("nowhere")

        label("nvm")
        goto("nowhere")
    }
}

// once you show a talisman or tiara to Elriss, it gets saved in a bitmask
enum class OmniPiece(val talisman: Int, val tiara: Int, val bit: Int) {
    AIR(Items.AIR_TALISMAN_1438,       Items.AIR_TIARA_5527,    1 shl 0), // 1
    MIND(Items.MIND_TALISMAN_1448,     Items.MIND_TIARA_5529,   1 shl 1), // 2
    WATER(Items.WATER_TALISMAN_1444,   Items.WATER_TIARA_5531,  1 shl 2), // 4
    EARTH(Items.EARTH_TALISMAN_1440,   Items.EARTH_TIARA_5535,  1 shl 3), // 8
    FIRE(Items.FIRE_TALISMAN_1442,     Items.FIRE_TIARA_5537,   1 shl 4), // 16
    BODY(Items.BODY_TALISMAN_1446,     Items.BODY_TIARA_5533,   1 shl 5), // 32
    COSMIC(Items.COSMIC_TALISMAN_1454, Items.COSMIC_TIARA_5539, 1 shl 6), // 64
    CHAOS(Items.CHAOS_TALISMAN_1452,   Items.CHAOS_TIARA_5543,  1 shl 7), // 128
    NATURE(Items.NATURE_TALISMAN_1462, Items.NATURE_TIARA_5541, 1 shl 8), // 256
    LAW(Items.LAW_TALISMAN_1458,       Items.LAW_TIARA_5545,    1 shl 9), // 512
    DEATH(Items.DEATH_TALISMAN_1456,   Items.DEATH_TIARA_5547,  1 shl 10),// 1024
    BLOOD(Items.BLOOD_TALISMAN_1450,   Items.BLOOD_TIARA_5549,  1 shl 11);// 2048

    companion object {
        // calculates the target int when all bits are combined
        val FULL_MASK = values().fold(0) { acc, piece -> acc or piece.bit }

        // attribute to track which talismans you've shown. bits packed into a bitmask
        const val ATTR_SHOW_TALISMANS = "/save:rc_guild_tally"
    }
}

// Omni-talisman ref: https://runescape.wiki/w/Omni-talisman?direction=prev&oldid=1077576
fun tallyCheck(player: Player): Int {
    // load the bitmask of what talismans have been shown so far
    var unlockMask = getAttribute(player, ATTR_SHOW_TALISMANS, 0)

    for (piece in OmniPiece.values()) {
        // AND the player's bitmask and the current bit of what we are checking. If the result is 1, this talisman has already been shown and you can go to the next one.
        if ((unlockMask and piece.bit) != 0) continue

        // Check for the item
        val hasTalisman = inInventory(player, piece.talisman)
        val hasTiara = inInventory(player, piece.tiara)

        if (hasTalisman || hasTiara) {
            val itemFound = if (hasTalisman) piece.talisman else piece.tiara

            // OR the player's bitmask with the new bit and save it
            unlockMask = unlockMask or piece.bit
            setAttribute(player, ATTR_SHOW_TALISMANS, unlockMask)

            // I couldn't find any data for how much each tally rewarded, so I just took the total and divided it equally per talisman.
            rewardXP(player, Skills.RUNECRAFTING, 1134.75)
            return itemFound
        }
    }
    return 0
}

fun checkOmni(player: Player): Boolean {
    // load the bitmask
    val unlockMask = getAttribute(player, ATTR_SHOW_TALISMANS, 0)

    // if bitmask matches the full mask, you've shown all the talismans and the return check is true
    return unlockMask == OmniPiece.FULL_MASK
}

fun rewardOmni(player: Player) {
    if (freeSlots(player) >= 1) {
        addItem(player, Items.OMNI_TALISMAN_13649)
        sendItemDialogue(player, Items.OMNI_TALISMAN_13649, "Wizard Elriss gives you an omni-talisman.")
    } else {
        sendDialogue(player, "You need at least one free inventory slot.")
    }
}
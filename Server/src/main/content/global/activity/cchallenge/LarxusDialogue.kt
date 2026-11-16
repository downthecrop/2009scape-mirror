package content.global.activity.cchallenge

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the dialogue plugin used for the Larxus npc.
 */

@Initializable
class LarxusDialogue(val challengeStart: Boolean = false) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.LARXUS_3050)
        if (challengeStart) {
            when (stage) {
                0 -> {
                    face(findNPC(NPCs.LARXUS_3050)!!, player!!, 1)
                    when {
                        inInventory(player!!,Items.CHAMPION_SCROLL_6798) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed to use any Prayers. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6799) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're only allowed to take Weapons, no other items are allowed. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6800) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're only allowed to use Melee attacks, no Ranged or Magic. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6801) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're only allowed to use Magic attacks, no Melee or Ranged. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6802) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed to use any Melee attacks. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6803) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed to use any Special Attacks. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6804) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed to use any Ranged attacks. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6805) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed any Weapons or Armour. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6806) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're only allowed to use Ranged attacks, no Melee or Magic. Do you still want to proceed?").also { stage = 1 }
                        inInventory(player!!,Items.CHAMPION_SCROLL_6807) -> npcl("So you want to accept the challenge huh? Well there are some specific rules for these Champion fights. For this fight you're not allowed to use any Magic attacks. Do you still want to proceed?").also { stage = 1 }
                        else -> sendMessage(player!!, "Nothing interesting happens.").also { stage = END_DIALOGUE }
                    }
                }
                1 -> options("Yes, let me at him!", "No thanks, I'll pass.").also { stage++ }
                2 -> when (buttonID) {
                    1 -> playerl("Yes, let me at him!").also { stage++ }
                    2 -> playerl("No thanks, I'll pass.").also { stage = END_DIALOGUE }
                }
                3 -> if (player!!.familiarManager.hasFamiliar() || player!!.familiarManager.hasPet()) {
                    npcl("Sorry, but I'm not going to let you down there with that follower.")
                    stage = END_DIALOGUE
                } else {
                    npcl("Your challenger is ready, please go down through the trapdoor when you're ready.")
                    stage = 4
                }
                4 -> {
                    end()
                    setAttribute(player!!, "championsarena:start", true)
                }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> {
                    face(findNPC(NPCs.LARXUS_3050)!!, player!!, 1)
                    npcl(FacialExpression.NEUTRAL, "Is there something I can help you with?").also { stage = 1 }
                }
                1 -> options("I was given a challenge, what now?", "What is this place?", "Nothing thanks.").also { stage = 2 }
                2 -> when (buttonID) {
                    1 -> playerl("I was given a challenge, what now?").also { stage = 3 }
                    2 -> playerl("What is this place?").also { stage = 4 }
                    3 -> playerl("Nothing thanks.").also { stage = END_DIALOGUE }
                }
                3 -> npcl("Well pass it here and we'll get you started.").also { stage = END_DIALOGUE }
                4 -> npcl("This is the champions' arena. The champions of various races use it to duel those they deem worthy of the honour. If you find a challenge scroll in your travels, bring it here to face your challenger.").also { stage = END_DIALOGUE }
            }
        }
    }
}
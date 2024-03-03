package content.region.karamja.brimhaven.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.global.Skillcape
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class CapnIzzyDialogue(private val it: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (it) {
            0 -> when (stage) {
                0 -> playerl(FacialExpression.HAPPY, "Ahoy Cap'n!").also { stage++ }
                1 -> npcl(FacialExpression.HAPPY, "Ahoy there!").also { stage++ }
                2 -> npcl(NPCs.PARROT_4535, FacialExpression.ANGRY, "Avast ye scurvy swabs!").also { stage++ }
                3 -> playerl(FacialExpression.THINKING, "Huh?").also { stage++ }
                4 -> npcl(FacialExpression.HAPPY, "Don't mind me parrot, he's Cracked Jenny's Tea Cup!").also { stage++ }

                5 -> {
                    if (Skillcape.isMaster(player!!, Skills.AGILITY)) {
                        options(
                            "What is this place?",
                            "What do I do in the arena?",
                            "I'd like to use the Agility Arena, please.",
                            "Is it true you sell the Skillcape of Agility?",
                            "See you later."
                        ).also { stage++ }
                    } else {
                        options(
                            "What is this place?",
                            "What do I do in the arena?",
                            "I'd like to use the Agility Arena, please.",
                            "Can you tell me a bit about the Skillcape of Agility, please?",
                            "See you later."
                        ).also { stage = 7 }
                    }
                }

                6 -> when (buttonID) {
                    1 -> playerl(FacialExpression.NEUTRAL, "What is this place?").also { stage = 10 }
                    2 -> playerl(FacialExpression.NEUTRAL, "What do I do in the arena?").also { stage = 20 }
                    3 -> playerl(FacialExpression.NEUTRAL, "I'd like to use the Agility Arena, please.").also { stage = 30 }
                    4 -> playerl(FacialExpression.HALF_ASKING, "May I buy a Skillcape of Agility, please?").also { stage = 40 }
                    5 -> playerl(FacialExpression.NEUTRAL, "See you later.").also { stage = END_DIALOGUE }
                }

                7 -> when (buttonID) {
                    1 -> playerl(FacialExpression.NEUTRAL, "What is this place?").also { stage = 10 }
                    2 -> playerl(FacialExpression.NEUTRAL, "What do I do in the arena?").also { stage = 20 }
                    3 -> playerl(FacialExpression.NEUTRAL, "I'd like to use the Agility Arena, please.").also { stage = 30 }
                    4 -> playerl(FacialExpression.NEUTRAL, "Can you tell me a bit about the Skillcape of Agility, please?").also { stage = 50 }
                    5 -> playerl(FacialExpression.NEUTRAL, "See you later.").also { stage = END_DIALOGUE }
                }

                10 -> npcl(FacialExpression.NEUTRAL, "This, me hearty, is the entrance to the Brimhaven, Agility Arena!").also { stage++ }
                11 -> npcl(FacialExpression.NEUTRAL, "I were diggin for buried treasure when I found it! Amazed I was! It was a sight to behold!").also { stage++ }
                12 -> npcl(FacialExpression.NEUTRAL, "It were the biggest thing I'd ever seen! it must've been at least a league from side to side!").also { stage++ }
                13 -> npcl(FacialExpression.NEUTRAL, "It made me list, I were that excited!").also { stage++ }
                14 -> npcl(FacialExpression.NEUTRAL, "I'd found a huge cave with all these platforms. I reckon it be an ancient civilisation that made it. I had " +
                        "to be mighty careful as there was these traps everywhere! Dangerous it was!").also { stage++ }

                15 -> npcl(FacialExpression.NEUTRAL, "Entrance is only 200 coins!").also { stage = 5 }
                20 -> npcl(FacialExpression.NEUTRAL, "Well, me hearty, it's simple. Ye can cross between two platforms by using the traps or obstacles " +
                            "strung across 'em. Try and make your way to the pillar that is indicated by the flashing arrow.").also { stage++ }
                21 -> npcl(FacialExpression.NEUTRAL, "Ye receive tickets for tagging more than one pillar in a row. So ye won't get a ticket from the " +
                            "first pillar but ye will for every platform ye tag in a row after that.").also { stage++ }
                22 -> npcl(FacialExpression.NEUTRAL, "If ye miss a platform ye will miss out on the next ticket so try and get every platform you can! " +
                            "When ye be done, take the tickets to Jackie over there and she'll exchange them for more stuff!").also { stage++ }
                23 -> playerl(FacialExpression.NEUTRAL, "Thanks!").also { stage = 5 }
                30 -> npcl(FacialExpression.NEUTRAL, "Aye, entrance be 200 coins.").also { stage++ }
                31 -> npcl(NPCs.PARROT_4535, FacialExpression.ANGRY, "Pieces of eight!").also { stage++ }
                32 -> npcl(FacialExpression.AMAZED, "A word of warning me hearty! There are dangerous traps down there!").also { stage++ }
                33 -> options("Ok, here's 200 coins.", "Never mind.").also { stage++ }
                34 -> when (buttonID) {
                    1 -> playerl(FacialExpression.NEUTRAL, "Ok, here's 200 coins.").also { stage++ }
                    2 -> playerl(FacialExpression.NEUTRAL, "Never mind.").also { stage = END_DIALOGUE }
                }

                35 -> {
                    if (!getAttribute(player!!, "capn_izzy", false)) {
                        if (inInventory(player!!, Items.COINS_995, 200) && removeItem(player!!, Item(Items.COINS_995, 200))) {
                            sendItemDialogue(player!!, Items.COINS_6964, "You give Cap'n Izzy 200 coins.").also { stage++ }
                            npcl(FacialExpression.HAPPY, "May the wind be in ye sails!").also { stage = END_DIALOGUE }
                            sendMessage(player!!, "You give Cap'n Izzy 200 coins.")
                            setAttribute(player!!, "/save:capn_izzy", true)
                        } else {
                            end()
                            sendMessage(player!!, "You don't have the 200 coin entrance fee.")
                        }
                    } else {
                        npcl(FacialExpression.NEUTRAL, "Avast there, ye've already paid!").also { stage = END_DIALOGUE }
                    }
                }

                40 -> npcl(FacialExpression.NEUTRAL, "Most certainly, I think it'll really suit you. All that remains to be done is pay me 99000 gold!").also { stage++ }
                41 -> options("I'm afraid I can't afford that.", "Certainly, here you go.").also { stage++ }
                42 -> when (buttonID) {
                    1 -> playerl(FacialExpression.NEUTRAL, "I'm afraid I can't afford that.").also { stage++ }
                    2 -> playerl(FacialExpression.HAPPY, "Certainly, here you go.").also { stage = 44 }
                }

                43 -> npcl(FacialExpression.NEUTRAL, "So sad, too bad. Go and pickpocket some wealthy people and come back here once you're richer.").also { stage = END_DIALOGUE }
                44 -> {
                    when {
                        (!inInventory(player!!, Items.COINS_995, 99000)) -> {
                            playerl(FacialExpression.NEUTRAL, "But, unfortunately, I don't have enough money with me.").also { stage = 45 }
                        }

                        (freeSlots(player!!)) < 2 -> {
                            npcl(FacialExpression.NEUTRAL, "Unfortunately all Skillcapes are only available with a free hood, it's part of a skill promotion deal; " +
                                    "buy one get one free, you know. So you'll need to free up some inventory space before I can sell you one.").also { stage = END_DIALOGUE }
                        }

                        else -> {
                            Skillcape.purchase(player!!, Skills.AGILITY)
                            npcl(FacialExpression.HAPPY, "Excellent! That cape really does suit you.").also { stage = END_DIALOGUE }
                        }
                    }
                }

                45 -> npcl(FacialExpression.NEUTRAL, "Well, come back and see me when you do.").also { stage = END_DIALOGUE }
                // The below text was too long when using npcl's automatic line splitting.
                50 -> sendNormalDialogue(
                    NPC(NPCs.CAPN_IZZY_NO_BEARD_437),
                    FacialExpression.NEUTRAL,
                    "Aye, to be sure! The Skillcape of Agility be the symbol",
                    "of the master of dexterity! One who wears it can climb",
                    "like a cat, run like the wind and jump like...err, well",
                    "jump like a jumping thing!"
                ).also { stage++ }
                51 -> npcl(FacialExpression.NEUTRAL, "Now, be there anything else ye'd like to know?").also { stage = 5 }
            }

            1 -> when (stage) {
                0 -> npcl(NPCs.PARROT_4535, FacialExpression.ANGRY, "Clap him in irons!").also { stage++ }
                1 -> npcl(NPCs.CAPN_IZZY_NO_BEARD_437, FacialExpression.NEUTRAL, "Ahoy there! Pay up first!").also { stage = END_DIALOGUE }
            }

            2 -> {
                if (!getAttribute(player!!, "capn_izzy", false)) {
                    if (inInventory(player!!, Items.COINS_995, 200) && removeItem(player!!, Item(Items.COINS_995, 200))) {
                        sendItemDialogue(player!!, Items.COINS_6964, "You give Cap'n Izzy the 200 coin entrance fee.").also { stage = END_DIALOGUE }
                        sendMessage(player!!, "You give Cap'n Izzy the 200 coin entrance fee.")
                        setAttribute(player!!, "/save:capn_izzy", true)
                    } else {
                        sendMessage(player!!, "You don't have the 200 coin entrance fee.")
                    }
                } else {
                    npcl(NPCs.CAPN_IZZY_NO_BEARD_437, FacialExpression.NEUTRAL, "Avast there, ye've already paid!").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
package rs09.game.content.global.worldevents.holiday.easter

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.global.worldevents.WorldEvents
import rs09.tools.END_DIALOGUE

class EasterBunnyDialogueFile(val NEED_BASKET : Boolean) : DialogueFile() {
    val EGG_ATTRIBUTE = "/save:easter:eggs"

    override fun handle(componentID: Int, buttonID: Int) {
        if(NEED_BASKET){
            when(stage++){
                0 -> npc("Hello, adventurer! Thank goodness you're here!")
                1 -> player(FacialExpression.THINKING,"Thanks goodness I'M here?")
                2 -> npc("Yes, yes, I need your help, you see!")
                3 -> npc("I have lost ALL of my eggs. What a terrible thing.")
                4 -> npc("Us easter bunnies rely on EGGS to live.")
                5 -> npc("Take this basket, please, and do me a kindness.")
                6 -> player(FacialExpression.THINKING,"What kindness might that be?")
                7 -> npc("I need you to try and gather up as many of my","lost eggs as you can.")
                8 -> npc("Please, for me? I will reward you for your time.")
                9 -> player("Fine, I suppose I will.")
                10 -> {
                    player!!.inventory.add(Item(Items.BASKET_OF_EGGS_4565))
                    player!!.dialogueInterpreter.sendItemMessage(Items.BASKET_OF_EGGS_4565,"The Easter Bunny gives you a basket.")
                    if(!player!!.musicPlayer.hasUnlocked(273)) {
                        player!!.musicPlayer.unlock(273, true)
                    }
                    stage = END_DIALOGUE
                }
            }
        } else {

            when (stage) {
                0 -> options("Ask about rewards", "Ask about egg location").also { stage++ }
                1 -> when (buttonID) {
                    1 -> player(FacialExpression.THINKING, "What kind of rewards can I claim?").also { stage = 10 }
                    2 -> player(FacialExpression.THINKING, "Where were some eggs last seen?").also { stage = 20 }
                }

                10 -> {
                    if (!player!!.emoteManager.isUnlocked(Emotes.BUNNY_HOP)) {
                        options("A credit (5 eggs)", "A Book of Knowledge (20 eggs)", "Bunny Hop Emote (30 eggs)")
                    } else {
                        options("A credit (5 eggs)", "A Book of Knowledge (20 eggs)")
                    }
                    stage++
                }

                11 -> {
                    end()
                    when (buttonID) {
                        1 -> {
                            if (player!!.getAttribute(EGG_ATTRIBUTE, 0) < 5) {
                                player!!.dialogueInterpreter.sendDialogue("You need 5 eggs to afford that.")
                            } else {
                                player!!.incrementAttribute(EGG_ATTRIBUTE, -5)
                                player!!.details.credits += 1
                                player!!.dialogueInterpreter.sendDialogue(
                                    "You turn in 5 eggs in exchange for a credit.",
                                    "You now have ${player!!.getAttribute(EGG_ATTRIBUTE, 0)} eggs."
                                )
                            }
                        }

                        2 -> {
                            if (player!!.getAttribute(EGG_ATTRIBUTE, 0) < 20) {
                                player!!.dialogueInterpreter.sendDialogue("You need 20 eggs to afford that.")
                            } else {
                                player!!.incrementAttribute(EGG_ATTRIBUTE, -20)
                                val item = Item(Items.BOOK_OF_KNOWLEDGE_11640)
                                if (!player!!.inventory.add(item)) {
                                    GroundItemManager.create(item, player)
                                }
                                player!!.dialogueInterpreter.sendDialogue("You spend 20 eggs on a Book of Knowledge.")
                            }
                        }

                        3 -> {
                            if (player!!.getAttribute(EGG_ATTRIBUTE, 0) < 30) {
                                player!!.dialogueInterpreter.sendDialogue("You need 30 eggs to afford that.")
                            } else {
                                player!!.incrementAttribute(EGG_ATTRIBUTE, -30)
                                player!!.emoteManager.unlock(Emotes.BUNNY_HOP)
                                player!!.dialogueInterpreter.sendDialogue(
                                    "The Easter Bunny teaches you how to bunny hop in exchange",
                                    "for 30 eggs."
                                )
                            }
                        }

                    }
                }
                20 ->{
                    val event = WorldEvents.get("easter") as EasterEvent
                    npc("The last known location of some eggs is ${event.recentLoc}.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}
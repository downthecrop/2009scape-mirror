package rs09.game.content.global.worldevents.holiday.halloween

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.item.Item
import rs09.ServerStore
import rs09.ServerStore.Companion.getInt
import rs09.tools.END_DIALOGUE

/**
 * Handles grim's dialogue for the 2021 halloween event.
 */
class GrimDialogue(player: Player? = null) : DialoguePlugin(player){
    var firstSpeak = true
    val candy = Item(14084)
    override fun newInstance(player: Player?): DialoguePlugin {
        return GrimDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        firstSpeak = !player.getAttribute("hween2:grim_spoken",false)

        if(firstSpeak){
            npc("YOU! Yes.... you! Come here!")
            player.musicPlayer.unlock(571)
            stage = 0
        } else {
            npc("Hello, again, adventurer...")
            stage = 100
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.AFRAID,"W-what... what do you want with","me?").also { stage++ }
            1 -> npc("I want you.... I NEED you....").also { stage++ }
            2 -> npc("TO BRING ME CANDY! Yes, candy...").also { stage++ }
            3 -> player(FacialExpression.THINKING,"Candy...? You want me to bring","you... candy?").also { stage++ }
            4 -> npc("Yes, candy! Did I not speak clearly","enough?").also { stage++ }
            5 -> player(FacialExpression.ASKING,"Well how do I even get candy?").also { stage++ }
            6 -> npcl(FacialExpression.NEUTRAL,"It's Hallowe'en, you fool. Everyone's giving out candy.").also { stage++ }
            7 -> npc("You'll need to go and ask them for it each day.").also { stage++ }
            8 -> npc("You'll also need to be careful...","Something vile is on the prowl.").also { stage++ }
            9 -> player(FacialExpression.THINKING,"And what will I get in exchange?").also { stage++ }
            10 -> npc("Well I won't KILL YOU for starters.").also { stage++ }
            11 -> player(FacialExpression.ANGRY_WITH_SMILE, "Is that it?!").also { stage++ }
            12 -> npc("I've prepared a few... rewards, as well.").also { stage++ }
            13 -> player(FacialExpression.AMAZED, "YOU MEAN CREDITS?!").also { stage++ }
            14 -> npc("No, we're not doing that again.").also { stage++ }
            15 -> player(FacialExpression.SAD,"Oh.").also { stage++ }
            16 -> player(FacialExpression.NEUTRAL, "Ok, so what kind of rewards?").also { stage++ }
            17 -> npcl(FacialExpression.NEUTRAL, "I've fashioned a special-made staff to give out for this year's event.").also { stage++ }
            18 -> npcl(FacialExpression.NEUTRAL, "It is a decorative symbol of your engagement in this activity. Bring me candies, and it can be yours.").also { player.setAttribute("/save:hween2:grim_spoken",true); stage++ }
            19 -> npc("NOW GET TO WORK!").also { player.setAttribute("/save:hween2:grim_spoken",true); stage = 1000 }


            100 -> npc("I do hope you have... candy for me?").also { stage++ }
            101 -> if(player.inventory.containsItem(candy)){
                    player("Yes, I do! Here you go.").also { stage = 150 }
                } else {
                    player(FacialExpression.SAD, "No, I don't.").also { stage++ }
                }
            102 -> npc("A shame, indeed. Your candy totals right now are ${getCandyTotals(player)}.").also { stage++ }
            103 -> player(FacialExpression.FRIENDLY, "I'd like to see the reward shop, please.").also { stage++ }
            104, 105 -> handleRewardShop(player, buttonId)


            150 -> {
                val candies = player.inventory.getAmount(candy)
                player.inventory.remove(Item(candy.id,candies))
                addToCandyTotal(player, candies)
                npcl(FacialExpression.NEUTRAL, "Excellent, you now have ${getCandyTotals(player)} candies.")
                stage = 103
            }


            1000 -> end()
        }
        return true
    }

    fun handleRewardShop(player: Player, buttonId: Int){
        val title = "You have ${getCandyTotals(player)} candies."
        val hasUnlocked = player.getAttribute("sotr:purchased",false)
        val hasRecolor1 = player.getAttribute("sotr:recolor1", false)
        val hasRecolor2 = player.getAttribute("sotr:recolor2", false)
        val hasEmote = player.emoteManager.isUnlocked(Emotes.TRICK)

        if(!hasUnlocked && !hasEmote){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff of the Raven (40c)", "Trick Emote (10c)").also { stage++ }
                105 -> when(buttonId) {
                    1 -> if (canPurchase(player, 40)) buyStaff(player) else npc(
                        FacialExpression.NEUTRAL,
                        "You can't afford that."
                    )
                    2 -> if (canPurchase(player, 10)) buyEmote(player) else npc(
                        FacialExpression.NEUTRAL,
                        "You can't afford that"
                    )
                }
            }
        }
        else if(hasUnlocked && !hasEmote && !hasRecolor1 && !hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Purple Recolor (10c)", "Staff Orange Recolor (10c)", "Trick Emote (10c)").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor1(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> if(canPurchase(player, 10)) buyRecolor2(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    3 -> if(canPurchase(player, 10)) buyEmote(player)    else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                }
            }
        }
        else if(hasUnlocked && !hasEmote && hasRecolor1 && !hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Orange Recolor (10c)", "Trick Emote (10c)").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor2(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> if(canPurchase(player, 10)) buyEmote(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                }
            }
        }
        else if(hasUnlocked && !hasEmote && !hasRecolor1 && hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Purple Recolor (10c)", "Trick Emote (10c)").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor1(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> if(canPurchase(player, 10)) buyEmote(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                }
            }
        }
        else if(!hasUnlocked && hasEmote){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff of the Raven (40c)", "").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 40)) buyStaff(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> npc(FacialExpression.NEUTRAL, "Huhwuh").also { stage = 104 }
                }
            }
        }
        else if(hasUnlocked && hasEmote && !hasRecolor1 && !hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Purple Recolor (10c)", "Staff Orange Recolor (10c)").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor1(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> if(canPurchase(player, 10)) buyRecolor2(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                }
            }
        }
        else if(hasUnlocked && hasEmote && !hasRecolor1 && hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Purple Recolor (10c)", "").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor1(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> npc(FacialExpression.NEUTRAL, "Huhwuh").also { stage = 104 }
                }
            }
        }
        else if(hasUnlocked && hasEmote && hasRecolor1 && !hasRecolor2){
            when(stage){
                104 -> player.dialogueInterpreter.sendOptions(title, "Staff Orange Recolor (10c)", "").also { stage++ }
                105 -> when(buttonId){
                    1 -> if(canPurchase(player, 10)) buyRecolor2(player) else npc(FacialExpression.NEUTRAL, "You can't afford that.")
                    2 -> npc(FacialExpression.NEUTRAL, "Huhwuh").also { stage = 104 }
                }
            }
        }
        else {
            npcl(FacialExpression.NEUTRAL, "You've already bought everything.")
            stage = END_DIALOGUE
        }
    }

    fun buyStaff(player: Player){
        player.setAttribute("/save:sotr:purchased", true)
        removeCandies(player, 40)
        addItem(player, 14654, 1)
        stage = 104
        handleRewardShop(player, -1)
    }

    fun buyRecolor1(player: Player){
        player.setAttribute("/save:sotr:recolor1", true)
        removeCandies(player, 10)
        stage = 104
        handleRewardShop(player, -1)
    }

    fun buyRecolor2(player: Player){
        player.setAttribute("/save:sotr:recolor2", true)
        removeCandies(player, 10)
        stage = 104
        handleRewardShop(player, -1)
    }

    fun buyEmote(player: Player){
        player.emoteManager.unlock(Emotes.TRICK)
        removeCandies(player, 10)
        stage = 104
        handleRewardShop(player, -1)
    }

    fun canPurchase(player: Player, cost: Int) : Boolean {
        return getCandyTotals(player) >= cost
    }

    fun removeCandies(player: Player, amount: Int){
        addToCandyTotal(player, -amount)
    }

    fun getCandyTotals(player: Player): Int {
        return ServerStore.getArchive("hween2021-candies").getInt(player.username.toLowerCase())
    }

    fun addToCandyTotal(player: Player, amount: Int){
        ServerStore.getArchive("hween2021-candies").put(player.username.toLowerCase(), getCandyTotals(player) + amount)
    }

    override fun getIds(): IntArray {
        return intArrayOf(6390)
    }

}
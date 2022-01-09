package rs09.game.content.dialogue.region.grandtree

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.content.activity.gnomecooking.GC_BASE_ATTRIBUTE
import rs09.game.content.activity.gnomecooking.GC_TUT_FIN
import rs09.game.content.activity.gnomecooking.GC_TUT_PROG

@Initializable
class BlurberryDialogue(player: Player? = null): DialoguePlugin(player) {
    var tutorialProgress = -1
    var tutorialComplete = false

    override fun newInstance(player: Player?): DialoguePlugin {
        return BlurberryDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        tutorialComplete = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_FIN",false)
        tutorialProgress = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_TUT_PROG",-1)

        if(tutorialProgress == 18){
            npc("Yes, you, Aluft said you would be coming.")
            stage = 0
            return true
        }

        if(tutorialComplete){
            npc("I do hope you're enjoying your work!")
            stage = 1000
            return true
        }

        npc("Hello, have you made that drink for me?")
        stage = tutorialProgress
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player("Yes! What is it I need to do?").also { stage++ }
            1 -> npc("Well, I'd like to show you how to make","a drink.").also { stage++ }
            2 -> player("Woah, sounds like fun!").also{ stage++ }
            3 -> npc("Alright, then, let's get to it!").also { stage++ }
            4 -> npc("I want you to make me a fruit blast. Simple drink!").also{ stage++ }
            5 -> npc("Here's everything you need.").also { stage++ }
            6 -> {
                end()
                val items = arrayOf(Item(Items.KNIFE_946),Item(Items.COCKTAIL_SHAKER_2025),Item(Items.COCKTAIL_GLASS_2026),Item(Items.PINEAPPLE_2114),Item(Items.LEMON_2102,2), Item(Items.ORANGE_2108))
                if(player.inventory.hasSpaceFor(*items)){
                    player.inventory.add(*items)
                    player.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG",20)
                } else {
                    player.dialogueInterpreter.sendDialogue("You don't have enough room for the items.")
                }
            }

            20 -> {
                if(player.inventory.containsItem(Item(Items.FRUIT_BLAST_9514))){
                    player("Yes, yes I have! Here you go.").also { stage++ }
                } else {
                    player("No I have not.").also { stage = 1000 }
                }
            }

            21 -> {
                player.dialogueInterpreter.sendDialogue("You hand over the fruit blast.")
                player.inventory.remove(Item(Items.FRUIT_BLAST_9514))
                player.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_TUT_PROG",22)
                stage++
            }

            22 -> npc("Excellent, I think you're ready to go on the job!").also { stage++ }
            23 -> {
                npc("Go back and speak with Aluft now.")
                player.setAttribute("/save:$GC_BASE_ATTRIBUTE:$GC_TUT_FIN",true)
                stage = 1000
            }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(848)
    }

}
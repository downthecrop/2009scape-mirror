package rs09.game.content.global.worldevents.penguinhns

import api.*
import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import rs09.game.world.GameWorld
import rs09.tools.END_DIALOGUE

class LarryHandler(player: Player? = null) : DialoguePlugin(player){
    override fun open(vararg args: Any?): Boolean {
        options("Can I have a spy notebook?","Can I have a hint?","I'd like to turn in my points.").also { stage = 0; return true }
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return LarryHandler(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        class HintPulse : Pulse(){
            override fun pulse(): Boolean {
                val hint = Penguin.values()[PenguinManager.penguins.random()].hint
                player.sendMessage("Here, I know one is...")
                player.sendMessage(hint)
                return true
            }
        }
        when(stage){
            0 -> when(buttonId){
                1 -> player("Can I have a spy notebook?").also { stage++ }
                2 -> player("Can I have a hint?").also { stage = 10 }
                3 -> player("I'd like to turn in my points.").also { stage = 20 }
            }

            //Spy notebook
            1 -> npc("Sure!").also { player.inventory.add(Item(13732));stage = 1000 }

            //Hint
            10 -> npc("Yes, give me just one moment...").also { stage++ }

            11 -> {
                val hint = Penguin.values()[PenguinManager.penguins.random()].hint
                npcl(FacialExpression.FRIENDLY, "One is $hint")
                stage = END_DIALOGUE
            }

            //Point turn-in
            20 -> if(player.getAttribute("phns:points",0) > 0) npc("Sure thing, what would you like to be","rewarded with?").also { stage++ } else npc("Uh, you don't have any points","to turn in.").also{stage = 1000}

            21 -> options("Coins","Experience").also { stage++ }
            22 -> when(buttonId){
                1 -> player.inventory.add(Item(995, 6500 * player.getAttribute("phns:points",0))).also { player("Thanks!"); player.removeAttribute("phns:points");stage = 1000 }
                2 -> {
                    player.setAttribute("caller",this)
                    player.interfaceManager.open(Component(134).setCloseEvent { player1: Player?, c: Component? ->
                        player.interfaceManager.openDefaultTabs()
                        player.removeAttribute("lamp")
                        player.unlock()
                        true
                    }).also { end() }
                }
            }

            1000 -> end()
        }
        return true
    }

    override fun handleSelectionCallback(skill: Int, player: Player) {
        val points = player.getAttribute("phns:points",0)
        if(points == 0){
            player.sendMessage("Sorry, but you have no points to redeem.")
            return
        }

        val level = getStatLevel(player, skill)
        System.out.println("Level: $level")
        val expGained = points?.toDouble()?.times((level * 25))
        System.out.print("exp: $expGained")
        player.skills.addExperience(skill,expGained!!)
        player.setAttribute("/save:phns:points",0)
    }

    override fun getIds(): IntArray {
        return intArrayOf(5424)
    }

}
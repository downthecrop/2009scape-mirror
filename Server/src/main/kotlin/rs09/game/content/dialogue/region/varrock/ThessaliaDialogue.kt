package rs09.game.content.dialogue.region.varrock

import core.game.component.Component
import core.plugin.Initializable
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.link.diary.DiaryType
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

@Initializable
class ThessaliaDialogue(player: Player? = null): DialoguePlugin(player) {
    override fun open(vararg args: Any): Boolean {

        //The trade argument is handled elsewhere
        if (args.size == 3) { //Right-Click 'Change-Clothes' Option
            if (player.equipment.isEmpty) {
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 0)
                if (player.isMale) {
                    end()
                    player.interfaceManager.open(Component(591))
                } else {
                    end()
                    player.interfaceManager.open(Component(594))
                }
            } else { //Has some armour equipped
                interpreter.sendDialogues(548, FacialExpression.WORRIED, "You can't try them on while wearing armour. Take", "it off and speak to me again.")
                stage = 52
            }
            return true
        }

        npc = args[0] as NPC

        //Default Talk
        interpreter.sendDialogues(npc, FacialExpression.ASKING, "Would you like to buy any fine clothes?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                interpreter.sendOptions("Choose an option:", "What do you have?", "No, thank you.")
                stage++
            }
            1 -> when (buttonId) {
                1 -> {
                    interpreter.sendDialogues(player, FacialExpression.ASKING, "What do you have?")
                    stage = 10
                }
                2 -> {
                    interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "No, thank you.")
                    stage = 51
                }
            }
            10 -> {
                interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I have a number of fine pieces of clothing on sale or,", "if you prefer, I can offer you an exclusive", "total clothing makeover?")
                stage++
            }
            11 -> {
                interpreter.sendOptions("Select an Option", "Tell me more about this makeover.", "I'd just like to buy some clothes.")
                stage++
            }
            12 -> when (buttonId) {
                1 -> {
                    interpreter.sendDialogues(player, FacialExpression.THINKING, "Tell me more about this makeover.")
                    stage = 20
                }
                2 -> {
                    interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "I'd just like to buy some clothes.")
                    stage = 50
                }
            }
            20 -> {
                interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Certainly!")
                stage++
            }
            21 -> {
                interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Here at Thessalia's fine clothing boutique, we offer a", "unique service where we will totally revamp your outfit", "to your choosing, for... wait for it...")
                stage++
            }
            22 -> {
                interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "A fee of only 500 gold coins! Tired of always wearing", "the same old outfit, day in, day out? This is the service", "for you!")
                stage++
            }
            23 -> {
                interpreter.sendDialogues(npc, FacialExpression.ASKING, "So what do you say? Interested? We can change either", "your top, or your legwear for only 500 gold a item!")
                stage++
            }
            24 -> {
                interpreter.sendOptions("Select an Option", "I'd like to change my outfit, please.", "I'd just like to buy some clothes.")
                stage++
            }
            25 -> when (buttonId) {
                1 -> {
                    interpreter.sendDialogues(player, FacialExpression.HAPPY, "I'd like to change my outfit, please.")
                    stage = 30
                }
                2 -> {
                    interpreter.sendDialogues(player, FacialExpression.NEUTRAL, "I'd just like to buy some clothes.")
                    stage = 50
                }
            }
            30 -> if (player.equipment.isEmpty) {
                interpreter.sendDialogues(npc, FacialExpression.HAPPY, "Just select what style and colour you would like from", "this catalogue, and then give me the 1000 gold when", "you've picked.")
                stage++
            } else { //Has some armour equipped
                interpreter.sendDialogues(npc, FacialExpression.WORRIED, "You can't try them on while wearing armour. Take", "it off and speak to me again.")
                stage = 52
            }
            31 -> if (player.equipment.isEmpty) {
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, 0)
                if (player.isMale) {
                    end()
                    player.interfaceManager.open(Component(591))
                } else {
                    end()
                    player.interfaceManager.open(Component(594))
                }
            }
            49 -> {
                interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "That's ok! Just come back when you do have it!")
                stage = 52
            }
            50 -> {
                end()
                npc.openShop(player)
            }
            51 -> {
                npc("Well, please return if you change your mind.")
                stage++
            }
            52 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return ThessaliaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(548)
    }
}
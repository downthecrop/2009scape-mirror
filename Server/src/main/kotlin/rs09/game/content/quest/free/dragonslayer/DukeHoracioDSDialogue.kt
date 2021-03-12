package rs09.game.content.quest.free.dragonslayer

import core.game.content.quest.free.dragonslayer.DragonSlayer
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class DukeHoracioDSDialogue(val questStage: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if(questStage in 20..99 && !hasShield(player!!)){

            when(stage){
                START_DIALOGUE -> player("I seek a shield that will protect me from dragonbreath.").also { stage++ }
                1 -> npc("A knight going on a dragon quest, hmm? What", "dragon do you intend to slay?").also{ stage++ }
                2 -> player("Elvarg, the dragon of Crandor island!").also { stage++ }
                3 -> npc("Elvarg? Are you sure?").also { stage++ }
                4 -> player("Yes!").also { stage++ }
                5 -> npc("Well, you're a braver man than I!").also { stage++ }
                6 -> player("Why is everyone scared of this dragon?").also { stage++ }
                7 -> npc(
                    "Back in my father's day, Crandor was an important",
                    "city-state. Politically, it was important as Falador or",
                    "Varrock and its shipes traded with every port."
                ).also { stage++ }

                8 -> npc(
                    "But, one day when I was little, all contact was lost. The",
                    "trading ships and diplomatic envoys just stopped",
                    "coming."
                ).also { stage++ }

                9 -> npc(
                    "I remember my father being very scared. He posted",
                    "lookouts on the roof to warn if the dragon was",
                    "approaching. All the city rulers worried that",
                    "Elvarg would devastate the whole continent."
                ).also { stage++ }

                10 -> player("So, are you going to give me the shield or not?").also { stage++ }
                11 -> npc(
                    "If you really think you're up to it then perhaps you",
                    "are the one who can kill this dragon."
                ).also { stage++ }

                12 -> {
                    if (!player!!.inventory.add(DragonSlayer.SHIELD)) {
                        GroundItemManager.create(DragonSlayer.SHIELD, player)
                    }
                    interpreter!!.sendItemMessage(DragonSlayer.SHIELD, "The Duke hands you a heavy orange shield.")
                    // Obtain an Anti-dragonbreath shield from Duke Horacio
                    player!!.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 2, 5)
                    stage = END_DIALOGUE
                }
            }

        }

        else if(questStage == 20) {

            when(stage){
                START_DIALOGUE -> npc("Take care out there. If you kill it...").also { stage++ }
                1 -> npc("If you kill it, for Saradomin's sake make sure it's really","dead!").also { stage = END_DIALOGUE }
            }

        }

        else if(questStage == 100 && !hasShield(player!!)){

            when(stage){
                START_DIALOGUE -> player("I seek a shield that will protect me from dragonbreath.").also { stage++ }
                1 -> npc("A knight going on a dragon quest, hmm? What", "dragon do you intend to slay?").also { stage++ }
                2 -> player("Oh, no dragon in particular. I just feel like killing a", "dragon.").also { stage++ }
                3 -> npc("Of course. Now you've slain Elvarg, you've earned", "the right to call the shield your own!").also { stage++ }
                4 -> {
                    if (!player!!.inventory.add(DragonSlayer.SHIELD)) {
                        GroundItemManager.create(DragonSlayer.SHIELD, player)
                    }
                    interpreter!!.sendItemMessage(DragonSlayer.SHIELD, "The Duke hands you the shield.")
                    // Obtain an Anti-dragonbreath shield from Duke Horacio
                    player!!.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 2, 5)
                    stage = END_DIALOGUE
                }
            }

        }

        else {
            abandonFile()
        }
    }

    fun hasShield(player: Player): Boolean{
        return player.inventory.containsItem(DragonSlayer.SHIELD) || player.bank.containsItem(DragonSlayer.SHIELD) || player.equipment.containsItem(
            DragonSlayer.SHIELD)
    }
}
package rs09.game.content.ame.events.supriseexam

import core.game.node.entity.npc.NPC
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import rs09.game.interaction.InterfaceListener

class SEPatternInterface : InterfaceListener() {

    val COMPONENT = Components.PATTERN_NEXT_103

    override fun defineListeners() {

        on(COMPONENT){player, component, opcode, buttonID, slot, itemID ->
            val index = buttonID - 10
            val correctIndex = player.getAttribute(SurpriseExamUtils.SE_KEY_INDEX,0)
            player.interfaceManager.close()

            if(index == correctIndex){
                player.incrementAttribute(SurpriseExamUtils.SE_KEY_CORRECT)
                val done = player.getAttribute(SurpriseExamUtils.SE_KEY_CORRECT,0) == 3
                player.dialogueInterpreter.open(MordautDialogue(examComplete = done,questionCorrect = true,fromInterface = true), NPC(NPCs.MR_MORDAUT_6117))
            } else {
                player.dialogueInterpreter.open(MordautDialogue(examComplete = false,questionCorrect = false,fromInterface = true),NPC(NPCs.MR_MORDAUT_6117))
            }
            return@on true
        }

        onOpen(COMPONENT){player, component ->
            SurpriseExamUtils.generateInterface(player)
            return@onOpen true
        }

    }

}
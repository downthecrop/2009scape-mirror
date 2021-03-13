package rs09.game.node.entity.skill.gather

import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingSkillPulse
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.KjallakOnChopDialogue
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.skill.gather.mining.MiningSkillPulse

class GatheringSkillOptionPlugin : InteractionListener() {

    val ETCETERIA_REGION = 10300

    override fun defineListeners() {
        on(OBJECT,"chop-down","chop down","cut down"){player,node ->
            if(player.location.regionId == ETCETERIA_REGION){
                player.dialogueInterpreter.open(KjallakOnChopDialogue(), NPC(NPCs.CARPENTER_KJALLAK_3916))
                return@on true
            }
            player.pulseManager.run(WoodcuttingSkillPulse(player, node.asObject()))
            return@on true
        }

        on(OBJECT,"mine"){player,node ->
            player.pulseManager.run(MiningSkillPulse(player, node.asObject()))
            return@on true
        }
    }
}
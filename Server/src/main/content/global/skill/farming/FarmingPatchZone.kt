package content.global.skill.farming

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile

class FarmingPatchZone : MapArea, TickListener {
    private val playersInZone = hashMapOf<Player,Int>()

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
            getRegionBorders(12083),
            getRegionBorders(10548),
            ZoneBorders(3594,3521,3608,3532)
        )
    }

    override fun areaEnter(entity: Entity) {
        if(entity is Player && playersInZone[entity] == null && getStatLevel(entity, Skills.FARMING) <= 15) {
            playersInZone[entity] = 0
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if(entity is Player)
            playersInZone.remove(entity)
    }

    override fun tick() {
        playersInZone.toList().forEach { (player, ticks) ->
            if(!player.isArtificial) {
                if (ticks == 500) {
                    spawnGithan(player, true)
                } else if (ticks == 1000) {
                    spawnGithan(player, false)
                }
                playersInZone[player] = ticks + 1
            }
        }
    }

    /**
     * Spawns a Githan at a player if they've been standing in a farming patch zone for long enough.
     * @param player The player to spawn the Githan at.
     * @param firstDialogue Whether the Githan should play the first dialogue or not (and playing the second dialogue instead).
     */
    private fun spawnGithan(player: Player, firstDialogue: Boolean) {
        val npc = NPC(NPCs.GITHAN_7122)
        npc.location = player.location
        npc.init()
        npc.moveStep()
        npc.face(player)
        openDialogue(player, SpiritDialogue(firstDialogue), npc)
    }

    internal class SpiritDialogue(private val firstDialogue: Boolean) : DialogueFile(){
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage){
                0 -> {
                    if(firstDialogue) npcl(FacialExpression.NEUTRAL, "In case you didn't know, you don't have to stand by your Farming patch. Your crops will grow even if you're not around.").also { stage++ }
                    else npcl(FacialExpression.NEUTRAL, "Did you know that if your Farming patch has fully grown, it will never catch disease or die? It will be perfectly safe until you choose to harvest it.").also { stage++ }
                }
                1 -> end()
            }
        }

        override fun end(){
            super.end()
            poofClear(npc ?: return)
        }
    }
}
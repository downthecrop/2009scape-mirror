package content.region.kandarin.quest.templeofikov

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class LucienEndingNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return LucienEndingNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUCIEN_272)
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        val player = entity.asPlayer()
        if (inEquipment(player, Items.ARMADYL_PENDANT_87)) {
            return attackable
        }
        sendNPCDialogue(player, this.ids[0], "You don't want to attack me. I am your friend.")
        return false
    }

    override fun finalizeDeath(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            openDialogue(player, object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> npcl("You have defeated me for now! I shall reappear in the North!").also { stage++ }
                        1 -> end().also {
                            if(getQuestStage(player, TempleOfIkov.questName) == 6) {
                                finishQuest(player, TempleOfIkov.questName)
                            }
                        }
                    }
                }
            }, NPC(NPCs.LUCIEN_272))

            super.finalizeDeath(player)
        }
    }
}
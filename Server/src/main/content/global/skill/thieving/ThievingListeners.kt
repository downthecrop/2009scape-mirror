package content.global.skill.thieving

import content.global.skill.skillcapeperks.SkillcapePerks
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Sounds

class ThievingListeners : InteractionListener {

    companion object {
        val PICKPOCKET_ANIM = Animation(881,Animator.Priority.HIGH)
        val NPC_ANIM = Animation(422)

        /** Standalone pickpocketing function. For thieving other stuff outside of normal pickpocketing tables. */
        fun pickpocketRoll(player: Player, low: Double, high: Double, table: WeightBasedTable): ArrayList<Item>? {
            // Able to pickpocket
            var successMod = 0.0
            if(SkillcapePerks.isActive(SkillcapePerks.SMOOTH_HANDS, player)) {
                successMod += 25
            }
            if (player.equipment.contains(Items.GLOVES_OF_SILENCE_10075,1)){
                successMod += 3
            }

            val chance = RandomFunction.randomDouble(1.0, 100.0)
            val failThreshold = RandomFunction.getSkillSuccessChance(low, high ,player.skills.getLevel(Skills.THIEVING)) + successMod

            if (chance > failThreshold) {
                // Fail Pickpocket
                return null // Returns a null, different from an empty table.
            } else {
                // Success Pickpocket
                return table.roll() // You could also successfully pickpocket nothing when the table returns a blank array.
            }
        }
    }

    override fun defineListeners() {

        on(IntType.NPC,"pickpocket","pick-pocket"){ player, node ->
            val pickpocketData = Pickpockets.forID(node.id) ?: return@on false

            if(player.inCombat()){
                player.sendMessage("You can't pickpocket while in combat.")
                return@on true
            }

            if(player.skills.getLevel(Skills.THIEVING) < pickpocketData.requiredLevel){
                player.sendMessage("You need a Thieving level of ${pickpocketData.requiredLevel} to do that.")
                return@on true
            }

            if(!pickpocketData.table.canRoll(player)){
                player.sendMessage("You don't have enough inventory space to do that.")
                return@on true
            }

            player.animator.animate(PICKPOCKET_ANIM)
            val lootTable = pickpocketRoll(player, pickpocketData.low, pickpocketData.high, pickpocketData.table)
            if(lootTable == null){
                node.asNpc().face(player)
                node.asNpc().animator.animate(NPC_ANIM)

                playHurtAudio(player, 20)

                stun(player, pickpocketData.stunTime)

                player.impactHandler.manualHit(node.asNpc(),RandomFunction.random(pickpocketData.stunDamageMin,pickpocketData.stunDamageMax),ImpactHandler.HitsplatType.NORMAL)

                node.asNpc().face(null)
            } else {
                playAudio(player, Sounds.PICK_2581)
                player.lock(2)
                lootTable.forEach { player.inventory.add(it) }
                player.skills.addExperience(Skills.THIEVING,pickpocketData.experience)
            }

            return@on true
        }
    }
}

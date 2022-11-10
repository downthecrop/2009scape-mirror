package rs09.game.node.entity.skill.thieving

import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.game.node.entity.state.EntityState
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.tools.secondsToTicks

class ThievingListeners : InteractionListener {

    private val PICKPOCKET_ANIM = Animation(881,Animator.Priority.HIGH)
    private val NPC_ANIM = Animation(422)
    private val SUCCESS = Audio(2581, 1, 0)

    override fun defineListeners() {

        on(IntType.NPC,"pickpocket","pick-pocket"){player, node ->
            val pickpocketData = Pickpockets.forID(node.id) ?: return@on false
            var successMod = 0

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

            if(pickpocketData == Pickpockets.FEMALE_HAM_MEMBER || pickpocketData == Pickpockets.MALE_HAM_MEMBER){
                successMod += getHAMItemCount(player)
            }

            if(player.equipment.contains(Items.GLOVES_OF_SILENCE_10075,1)){
                successMod += 3
            }

            player.animator.animate(PICKPOCKET_ANIM)
            val chance = RandomFunction.randomDouble(1.0,100.0)
            val failThreshold = pickpocketData.getSuccessChance(player) + successMod

            if(chance > failThreshold){
                node.asNpc().face(player)
                node.asNpc().animator.animate(NPC_ANIM)

                val hitSoundId = 518 + RandomFunction.random(4) // choose 1 of 4 possible hit noises
                player.audioManager.send(hitSoundId, 1, 20) // OSRS defines a delay of 20

                player.stateManager.set(EntityState.STUNNED, secondsToTicks(pickpocketData.stunTime))
                player.lock(secondsToTicks(pickpocketData.stunTime))

                player.impactHandler.manualHit(node.asNpc(),RandomFunction.random(pickpocketData.stunDamageMin,pickpocketData.stunDamageMax),ImpactHandler.HitsplatType.NORMAL)

                node.asNpc().face(null)
            } else {
                player.audioManager.send(SUCCESS)
                player.lock(2)
                pickpocketData.table.roll().forEach { player.inventory.add(it) }
                player.skills.addExperience(Skills.THIEVING,pickpocketData.experience)
            }

            return@on true
        }

    }

    fun getHAMItemCount(player: Player): Int{
        var counter = 0
        for(item in player.equipment.toArray()){
            item ?: continue
            counter += when(item.id){
                Items.HAM_LOGO_4306 -> 1
                Items.HAM_ROBE_4300 -> 1
                Items.HAM_HOOD_4302 -> 1
                Items.HAM_CLOAK_4304 -> 1
                Items.BOOTS_4310 -> 1
                Items.GLOVES_4308 -> 1
                else -> 0
            }
        }
        return counter
    }

}
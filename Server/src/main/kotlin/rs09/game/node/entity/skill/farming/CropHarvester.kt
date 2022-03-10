package rs09.game.node.entity.skill.farming

import api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.familiar.GiantEntNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs09.consts.Items

val livesBased = arrayOf(PatchType.HERB, PatchType.CACTUS, PatchType.BELLADONNA, PatchType.HOPS, PatchType.ALLOTMENT,PatchType.EVIL_TURNIP)

val spadeAnim = Animation(830)

@Initializable
class CropHarvester : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("harvest",this)
        SceneryDefinition.setOptionHandler("pick",this)
        return this
    }

    companion object {
        @JvmStatic
        fun harvestPulse(player: Player?, node: Node?, crop: Int): Pulse? {
            player ?: return null
            node ?: return null
            val fPatch = FarmingPatch.forObject(node.asScenery())
            fPatch ?: return null
            val patch = fPatch.getPatchFor(player)
            val plantable = patch.plantable
            plantable ?: return null

            return object : Pulse(0) {
                override fun pulse(): Boolean {
                    var reward = Item(crop, 1)

                    val familiar = player.familiarManager.familiar
                    if(familiar != null && familiar is GiantEntNPC) {
                        familiar.modifyFarmingReward(fPatch, reward)
                    }
                    if(!player.inventory.hasSpaceFor(reward)){
                        player.sendMessage("You don't have enough inventory space for that.")
                        return true
                    }
                    var requiredItem = when(fPatch.type){
                        PatchType.HERB -> Items.SECATEURS_5329
                        else -> Items.SPADE_952
                    }
                    if(requiredItem == Items.SECATEURS_5329){
                        if(player.inventory.contains(Items.MAGIC_SECATEURS_7409,1)){
                            requiredItem = Items.MAGIC_SECATEURS_7409
                        }
                    }
                    val anim = when(requiredItem){
                        Items.SPADE_952 -> Animation(830)
                        Items.SECATEURS_5329 -> Animation(7227)
                        Items.MAGIC_SECATEURS_7409 -> Animation(7228)
                        else -> Animation(0)
                    }
                    val sound = when(requiredItem){
                        Items.SPADE_952 -> Audio(1470, 1, 1)
                        Items.SECATEURS_5329 -> Audio(2437, 1, 1)
                        Items.MAGIC_SECATEURS_7409 -> Audio(2437, 1, 1)
                        else -> Audio(0)
                    }
                    if(!player.inventory.containsItem(Item(requiredItem))){
                        player.sendMessage("You lack the needed tool to harvest these crops.")
                        return true
                    }
                    player.animator.animate(anim)
                    player.audioManager.send(sound)
                    delay = 2
                    player.inventory.add(reward)
                    player.skills.addExperience(Skills.FARMING,plantable.harvestXP)
                    if(patch.patch.type in livesBased){
                        patch.rollLivesDecrement(
                            getDynLevel(player, Skills.FARMING),
                            requiredItem == Items.MAGIC_SECATEURS_7409
                        )
                    } else {
                        patch.harvestAmt--
                        if(patch.harvestAmt <= 0 && crop == plantable.harvestItem){
                            patch.clear()
                        }
                    }
                    return patch.cropLives <= 0 || patch.harvestAmt <= 0
                }
            }
        }
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val fPatch = FarmingPatch.forObject(node.asScenery())
        fPatch ?: return false
        val patch = fPatch.getPatchFor(player)
        val plantable = patch.plantable
        plantable ?: return false

        if(patch.isWeedy()){
            player.sendMessage("Something seems to have gone wrong here. Report this.")
            return true
        }

        val pulse = harvestPulse(player, node, plantable.harvestItem) ?: return false
        player.pulseManager.run(pulse)

        return true
    }

}

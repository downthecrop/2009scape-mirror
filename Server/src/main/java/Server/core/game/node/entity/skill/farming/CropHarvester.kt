package core.game.node.entity.skill.farming

import core.cache.def.impl.ObjectDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class CropHarvester : OptionHandler() {

    val spadeAnim = Animation(830)

    override fun newInstance(arg: Any?): Plugin<Any> {
        ObjectDefinition.setOptionHandler("harvest",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        val fPatch = FarmingPatch.forObject(node.asObject())
        fPatch ?: return false
        val patch = fPatch.getPatchFor(player)
        val plantable = patch.plantable
        plantable ?: return false

        if(patch.isWeedy()){
            player.sendMessage("Something seems to have gone wrong here. Report this.")
            return true
        }

        player.pulseManager.run(object : Pulse(0){
            override fun pulse(): Boolean {
                if(!player.inventory.hasSpaceFor(Item(plantable.harvestItem,1))){
                    return true
                }
                player.animator.animate(spadeAnim)
                delay = 2
                player.inventory.add(Item(plantable.harvestItem,1))
                player.skills.addExperience(Skills.FARMING,plantable.harvestXP)
                patch.harvestAmt--

                if(patch.harvestAmt <= 0){
                    patch.clear()
                }
                return patch.harvestAmt <= 0
            }
        })

        return true
    }

}
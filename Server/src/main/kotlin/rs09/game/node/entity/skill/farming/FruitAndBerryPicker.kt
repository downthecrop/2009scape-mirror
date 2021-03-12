package rs09.game.node.entity.skill.farming

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
import java.util.concurrent.TimeUnit

@Initializable
class FruitAndBerryPicker : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ObjectDefinition.setOptionHandler("pick-coconut",this)
        ObjectDefinition.setOptionHandler("pick-banana",this)
        ObjectDefinition.setOptionHandler("pick-apple",this)
        ObjectDefinition.setOptionHandler("pick-orange",this)
        ObjectDefinition.setOptionHandler("pick-pineapple",this)
        ObjectDefinition.setOptionHandler("pick-papaya",this)
        ObjectDefinition.setOptionHandler("pick-leaf",this)
        ObjectDefinition.setOptionHandler("pick-from",this)
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

        val animation = Animation(2281)
        val reward = Item(plantable.harvestItem)

        if(patch.getFruitOrBerryCount() <= 0){
            player.sendMessage("This shouldn't be happening. Please report this.")
            return true
        }

        if(!player.inventory.hasSpaceFor(Item(plantable.harvestItem))){
            player.sendMessage("You do not have enough inventory space for this.")
            return true
        }

        if(System.currentTimeMillis() - patch.nextGrowth > TimeUnit.MINUTES.toMillis(45)){
            patch.nextGrowth = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(45)
        }

        player.pulseManager.run(object : Pulse(animation.duration){
            override fun pulse(): Boolean {
                player.animator.animate(animation)
                player.inventory.add(reward)
                player.skills.addExperience(Skills.FARMING,plantable.harvestXP)
                patch.setCurrentState(patch.getCurrentState() - 1)

                return patch.getFruitOrBerryCount() == 0
            }
        })

        return true
    }

}
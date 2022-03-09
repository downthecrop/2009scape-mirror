package rs09.game.node.entity.skill.farming

import api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.familiar.GiantEntNPC
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
        SceneryDefinition.setOptionHandler("pick-coconut",this)
        SceneryDefinition.setOptionHandler("pick-banana",this)
        SceneryDefinition.setOptionHandler("pick-apple",this)
        SceneryDefinition.setOptionHandler("pick-orange",this)
        SceneryDefinition.setOptionHandler("pick-pineapple",this)
        SceneryDefinition.setOptionHandler("pick-papaya",this)
        SceneryDefinition.setOptionHandler("pick-leaf",this)
        SceneryDefinition.setOptionHandler("pick-from",this)
        SceneryDefinition.setOptionHandler("pick-fruit",this)
        SceneryDefinition.setOptionHandler("pick-spine",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false

        val fPatch = FarmingPatch.forObject(node.asScenery())
        fPatch ?: return false

        val patch = fPatch.getPatchFor(player)

        val plantable = patch.plantable
        plantable ?: return false

        val animation = Animation(2281)

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
				val reward = Item(plantable.harvestItem, 1)

				val familiar = player.familiarManager.familiar
				if(familiar != null && familiar is GiantEntNPC) {
					familiar.modifyFarmingReward(fPatch, reward)
				}

                player.animator.animate(animation)
                player.audioManager.send(2437)        
                addItemOrDrop(player,reward.id,reward.amount)
                player.skills.addExperience(Skills.FARMING,plantable.harvestXP)
                patch.setCurrentState(patch.getCurrentState() - 1)

                return patch.getFruitOrBerryCount() == 0
            }
        })

        return true
    }

}

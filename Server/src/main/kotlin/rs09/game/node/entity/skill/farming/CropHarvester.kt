package rs09.game.node.entity.skill.farming

import api.ContentAPI
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
class CropHarvester : OptionHandler() {

    val spadeAnim = Animation(830)

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("harvest",this)
        SceneryDefinition.setOptionHandler("pick",this)
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

        if(patch.isWeedy()){
            player.sendMessage("Something seems to have gone wrong here. Report this.")
            return true
        }

        player.pulseManager.run(object : Pulse(0){
            override fun pulse(): Boolean {
                if(!player.inventory.hasSpaceFor(Item(plantable.harvestItem,1))){
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
                if(!player.inventory.containsItem(Item(requiredItem))){
                    player.sendMessage("You lack the needed tool to harvest these crops.")
                    return true
                }
                player.animator.animate(anim)
                delay = 2
                player.inventory.add(Item(plantable.harvestItem,1))
                player.skills.addExperience(Skills.FARMING,plantable.harvestXP)
                patch.rollLivesDecrement(ContentAPI.getDynLevel(player, Skills.FARMING), requiredItem == Items.MAGIC_SECATEURS_7409)
                return patch.cropLives <= 0
            }
        })

        return true
    }

}
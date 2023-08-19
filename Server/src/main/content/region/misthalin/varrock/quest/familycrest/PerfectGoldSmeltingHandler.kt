package content.region.misthalin.varrock.quest.familycrest


import core.api.addItem
import core.api.animate
import core.api.removeItem
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import content.global.skill.smithing.FurnaceOptionPlugin
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import core.game.world.GameWorld.Pulser


@Initializable
class PerfectGoldSmeltingHandler : UseWithHandler(Items.PERFECT_GOLD_ORE_446){


    private val furnaceIDs: IntArray = FurnaceOptionPlugin.SmeltUseWithHandler.furnaceIDS

    override fun newInstance(arg: Any?): Plugin<Any> {
        for(furnace in furnaceIDs){
            addHandler(furnace, OBJECT_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {

        event ?: return false
        val player = event.player

            Pulser.submit(object : Pulse(2, player) {
                override fun pulse(): Boolean {
                    if(removeItem(player,Items.PERFECT_GOLD_ORE_446)){
                        animate(player,3243)
                        addItem(player,Items.PERFECT_GOLD_BAR_2365)
                        player.skills.addExperience(Skills.SMITHING,22.5)
                    }
                    return true
                }
            })
            return true
    }
}

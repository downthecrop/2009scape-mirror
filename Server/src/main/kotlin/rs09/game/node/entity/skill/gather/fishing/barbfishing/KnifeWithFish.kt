package rs09.game.node.entity.skill.gather.fishing.barbfishing

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
/**
 * Handles using a knife with barbarian fishing fish
 * @author Ceikry
 */
class KnifeWithFish : UseWithHandler(11328,11330,11332){
    override fun handle(event: NodeUsageEvent?): Boolean {
        event?.player ?: return false
        event.player.pulseManager.run(FishCuttingPulse(event.player,event.usedItem.id))
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.KNIFE_946, ITEM_TYPE,this)
        return this
    }

}

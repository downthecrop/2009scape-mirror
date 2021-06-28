package rs09.game.node.entity.skill.farming

import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class InspectionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("inspect",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return false
        player ?: return false
        val patch = FarmingPatch.forObject(node.asScenery())
        if(patch == null){
            player.sendMessage("This is an improperly handled inspect option. Report this please.")
        } else {
            val p = patch.getPatchFor(player)
            val status1 = if(p.getCurrentState() <= 2) "This patch needs weeding." else if(p.getCurrentState() == 3) "This patch is weed-free." else {
                if(p.isDiseased && !p.isDead) "This patch has become diseased."
                else if(p.isDead) "The crops in this patch are dead."
                else if(p.plantable == Plantable.SCARECROW) "There is a scarecrow in this patch."
                else "This patch has something growing in it."
            }
            val status2 = if(patch.type == PatchType.ALLOTMENT || patch.type == PatchType.FLOWER || patch.type == PatchType.HOPS){
                if(p.isWatered) "This patch has been watered." else "This patch could use some water."
            } else ""
            val status3 = if(p.compost == CompostType.NONE) "This patch has not been treated." else "This patch has been treated with ${p.compost.name.toLowerCase()} compost."
            player.sendMessage("$status1 $status2")
            player.sendMessage(status3)
            val varpValue = player.varpManager.get(patch.varpIndex).getBitRangeValue(patch.varpOffset,patch.varpOffset + 7)
        }
        return true
    }

}
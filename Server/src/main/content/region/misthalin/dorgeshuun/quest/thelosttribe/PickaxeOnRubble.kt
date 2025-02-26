package content.region.misthalin.dorgeshuun.quest.thelosttribe

import content.data.Quests
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import content.data.skill.SkillingTool
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import core.api.*

@Initializable
/**
 * Handles the usage of a pickaxe on the rubble for lost tribe
 * @author Ceikry
 */
class PickaxeOnRubble : UseWithHandler(1265,1267,1269,1271,1273,1275){
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(id in arrayOf(6898, 6903, 6904, 6905)) {
            addHandler(id, OBJECT_TYPE,this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        val player = event?.player ?: return false
        val stage = player.questRepository.getQuest(Quests.THE_LOST_TRIBE).getStage(player)
        if(stage < 30){
            player.dialogueInterpreter.sendItemMessage(event.usedItem.id,"I should probably figure out what happened","before vandalizing the castle more.")
            return true
        }

        val tool = SkillingTool.getPickaxe(player)
        if(tool == null){
            player.dialogueInterpreter.sendDialogue("You don't have a pick which you have the level to use.")
            return true
        }

        if(player.skills.getLevel(Skills.MINING) < 13){
            player.dialogueInterpreter.sendDialogue("You need 13 mining to break through.")
            return true
        }

        player.lock()
        GameWorld.Pulser.submit(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> {
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(3219,9618,true)
                    }
                    1 -> {
                        player.animator.animate(tool.animation)
                        delay = tool.animation.duration
                    }
                    2 -> {
                        player.dialogueInterpreter.sendItemMessage(tool.id,"You dig a narrow tunnel through the rocks.")
                        player.setAttribute("/save:tlt-hole-cleared",true)
                        setVarbit(player, 532, 4, true)
                        player.unlock()
                        return true
                    }
                }
                return false
            }
        })
        return true
    }

}

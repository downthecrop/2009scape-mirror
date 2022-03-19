package rs09.game.content.tutorial

import api.*
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import rs09.game.world.GameWorld

/**
 * Login listener that checks for tutorial completion and loads the current tutorial stage
 * @author Ceikry
 */
class TutorialLoginCheck : LoginListener {
    override fun login(player: Player) {
        if(!getAttribute(player, "tutorial:complete", false)) {
            //Disable need to do tutorial for anyone who has already started their account (gained levels or using it as a mule)
            if(getAttribute(player, "tutorial:stage", 0) == 0 && (player.skills.totalLevel > 33 || player.bank.itemCount() > 0 || player.inventory.itemCount() > 0)){
                setAttribute(player, "/save:tutorial:complete", true)
                return
            }
            GameWorld.Pulser.submit(object : Pulse() {
                override fun pulse(): Boolean {
                    TutorialStage.load(player, getAttribute(player, "tutorial:stage", 0), true)
                    return true
                }
            })
        }
    }
}
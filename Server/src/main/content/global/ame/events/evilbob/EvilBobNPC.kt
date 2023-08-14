package content.global.ame.events.evilbob

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class EvilBobNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.EVIL_BOB_2478) {

    override fun init() {
        super.init()
        sendChat("meow")
        lock(player, 10)
        runTask(player, 6) {
            sendChat(player, "No... what? Nooooooooooooo!")
            player.animate(EvilBobUtils.teleAnim)
            player.graphics(EvilBobUtils.telegfx)
            playAudio(player, Sounds.TELEPORT_ALL_200)
            EvilBobUtils.giveEventFishingSpot(player)
            runTask(player, 3) {
                sendMessage(player, "Welcome to Scape2009.")
                EvilBobUtils.teleport(player)
                resetAnimator(player)
                openDialogue(player, EvilBobDialogue(), NPCs.EVIL_BOB_2479)
                AntiMacro.terminateEventNpc(player)
            }
        }
    }

    override fun talkTo(npc: NPC) {
        player.dialogueInterpreter.open(EvilBobDialogue(), this.asNpc())
    }
}
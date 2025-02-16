package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class OgreGuardDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return OgreGuardDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, OgreGuardDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OGRE_GUARD_2042)
    }
}
class OgreGuardDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(ZogreFleshEaters.questName, 0)
                .npcl(FacialExpression.OLD_NORMAL, "Yous needs ta stay away from dis place... yous get da sickies and mebe yous goes to dead if yous da unlucky fing.")
                .playerl("Ok, thanks.")
                .end()

        b.onQuestStages(ZogreFleshEaters.questName, 1)
                .npcl(FacialExpression.OLD_NORMAL, "Yous needs ta stay away from dis place... yous get da sickies and mebe yous goes to dead if yous da unlucky fing.")
                .playerl(FacialExpression.FRIENDLY, "But Grish has asked me to look into this place and find out why all the undead ogres are here.")
                .npcl(FacialExpression.OLD_NORMAL, "Ok, dat is da big, big scary, danger fing! You's sure you's wants to go in?")
                .playerl(FacialExpression.FRIENDLY, "Yes, I'm sure.")
                .npcl(FacialExpression.OLD_NORMAL, "Ok, I opens da stoppa's for yous creature.")
                .endWith { _, player ->
                    lock(player, 4)
                    face(npc!!, Location(2456, 3049, 0))
                    // Lesson learnt here, endWith kills the npc object so tie the queueScript with the npc instead, not the player.
                    queueScript(npc!!, 2, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animate(npc!!, 2102)
                                return@queueScript delayScript(npc!!, Animation(2102).duration)
                            }
                            1 -> {
                                if(getQuestStage(player, ZogreFleshEaters.questName) == 1) {
                                    setQuestStage(player, ZogreFleshEaters.questName, 2)
                                }
                                setVarbit(player, ZogreFleshEaters.varbitGateBashed, 1)
                                unlock(player)
                                face(npc!!, player.location)
                                sendNPCDialogue(player, NPCs.OGRE_GUARD_2042, "Ok der' yous goes!", FacialExpression.OLD_NORMAL)
                                return@queueScript stopExecuting(npc!!)
                            }
                            else -> return@queueScript stopExecuting(npc!!)
                        }
                    }
                }

        b.onQuestStages(ZogreFleshEaters.questName, 2,3,4,5,6,7,8,9,10,100)
                .npcl(FacialExpression.OLD_NORMAL, "Hey yous tryin' not to get da sickies else yous be da sick-un and mebe get to be a dead-un if yous be da unlucky fing.")
                .playerl(FacialExpression.FRIENDLY, "Don't worry, I know how to take care of myself.")
                .end()

    }
}
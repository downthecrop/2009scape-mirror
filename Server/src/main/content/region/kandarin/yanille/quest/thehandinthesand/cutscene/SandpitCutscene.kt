package content.region.kandarin.yanille.quest.thehandinthesand.cutscene

import content.data.Quests
import content.region.kandarin.yanille.dialogue.ZavisticRarveDialogueFile
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.animate
import core.api.animateScenery
import core.api.face
import core.api.openDialogue
import core.api.playAudio
import core.api.playJingle
import core.api.sendChat
import core.api.setQuestStage
import core.game.activity.Cutscene
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs09.consts.Jingles
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * Sandpit cutscene for The Hand in the Sand quest.
 * @author Edith
 */

class SandpitCutscene(player: Player, private val zavistic: NPC) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(player.location))
        loadRegion(10032)
        addNPC(NPCs.BERT_3108, 50, 27, Direction.SOUTH)
    }

    private fun bert() = getNPC(NPCs.BERT_3108)!!
    private fun sandpit() = getObject(46, 31)!!

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }

            1 -> {
                teleport(player, 40, 37, 0)
                moveCamera(40, 37, 850)
                rotateCamera(46, 31, 200)
                timedUpdate(2)
            }

            2 -> {
                fadeFromBlack()
                playJingle(player, Jingles.HANDSAND_CUTSCENE_114)
                timedUpdate(2)
            }

            3 -> {
                dialogueUpdate("The Wizard chants and your attention is taken to the sandpit where Bert found the hand.")
            }

            4 -> {
                move(getNPC(NPCs.BERT_3108)!!, 46, 29)
                face(bert(), sandpit().location)
                timedUpdate(7)
            }

            5 -> {
                animate(bert(), 860)
                timedUpdate(1)
            }

            6 -> {
                animateScenery(player, sandpit(), 3037)
                playAudio(player, Sounds.HANDSAND_SANDSWIRL_1591)
                sendChat(bert(), "My sand! My lovely sand!")
                timedUpdate(4)
            }

            7 -> {
                dialogueUpdate(
                    "Something very strange happens to the Sandpit, it looks like it has filled itself up!",
                    onContinue = { dialogueClose() }
                )
                timedUpdate(9)
            }

            8 -> {
                dialogueClose()
                fadeToBlack()
                timedUpdate(3)
            }

            9 -> {
                endWithoutFade {
                    fadeFromBlack()
                    setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_SANDPIT_FILLED_65)
                    openDialogue(player, ZavisticRarveDialogueFile(startLabel = "quest_stage_65"), zavistic)
                }
            }
        }
    }

}
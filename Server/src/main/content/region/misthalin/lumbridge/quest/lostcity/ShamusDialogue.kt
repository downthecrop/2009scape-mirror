package content.region.misthalin.lumbridge.quest.lostcity

import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.api.getQuestStage
import core.api.setQuestStage

/**
 * ShamusDialogue, to handle the dialogue of Shamus the Leprechaun from the Lost City quest
 * @author lila
 * @author Vexia
 */
@Initializable
class ShamusDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    val quest = "Lost City"

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.ANNOYED,"Ay yer big elephant! Yer've caught me, to be sure! What would an elephant like yer be wanting wid ol' Shamus then?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getQuestStage(player,quest)) {
            0 -> when(stage++) {
                0 -> playerl(core.game.dialogue.FacialExpression.THINKING, "I'm not sure.")
                1 -> npcl(core.game.dialogue.FacialExpression.ANNOYED,"Well you'll have to be catchin' me again when yer are, elephant!")
                2 -> end().also { ShamusTreeListener.disappearShamus() }
            }
            10 -> when(stage++) {
                0 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL,"I want to find Zanaris.")
                1 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Zanaris is it now? Well well well... Yer'll be needing to be going to that funny little shed out there in the swamp, so you will.")
                2 -> playerl(core.game.dialogue.FacialExpression.THINKING,"...but... I thought... Zanaris was a city?")
                3 -> npcl(core.game.dialogue.FacialExpression.HAPPY,"Aye that it is!")
                4 -> playerl(core.game.dialogue.FacialExpression.THINKING,"...How does it fit in a shed then?")
                5 -> npcl(core.game.dialogue.FacialExpression.LAUGH,"Ah yer stupid elephant! The city isn't IN the shed! The doorway to the shed is being a portal to Zanaris, so it is.")
                6 -> playerl(core.game.dialogue.FacialExpression.HALF_THINKING,"So I just walk into the shed and end up in Zanaris then?")
                7 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Oh, was I fergetting to say? Yer need to be carrying a Dramenwood staff to be getting there! Otherwise Yer'll just be ending up in the shed.")
                8 -> playerl(core.game.dialogue.FacialExpression.ASKING,"So where would I get a staff?")
                9 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"Dramenwood staffs are crafted from branches of the Dramen tree, so they are. I hear there's a Dramen tree over on the island of Entrana in a cave.")
                10 -> npcl(core.game.dialogue.FacialExpression.HALF_THINKING,"or some such. There would be probably be a good place for an elephant like yer to be starting looking I reckon.")
                11 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL,"The monks are running a ship from Port Sarim to Entrana, I hear too. Now leave me alone yer elephant!")
                12 -> end().also {
                    ShamusTreeListener.disappearShamus()
                    sendDialogue("The leprechaun magically disappears.")
                    setQuestStage(player,quest,20)
                }
            }
            else -> when(stage++) {
                0 -> playerl(core.game.dialogue.FacialExpression.THINKING, "I'm not sure.")
                1 -> {
                    val pronoun = if(player.isMale) "he" else "she"
                    npcl(core.game.dialogue.FacialExpression.ANNOYED,"HA! Look at yer! Look at the stupid elephant who tries to go catching a leprechaun when $pronoun don't even be knowing what $pronoun wants!")
                }
                2 -> end().also { ShamusTreeListener.disappearShamus() }
            }

        }
        return true
    }

    override fun newInstance(player: Player): core.game.dialogue.DialoguePlugin {
        return ShamusDialogue(player)
    }
    override fun getIds(): IntArray = intArrayOf(NPCs.SHAMUS_654)
}

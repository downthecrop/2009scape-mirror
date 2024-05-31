package content.region.misthalin.lumbridge.dialogue

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Sir Vant Dialogue
 *
 * @comments
 * I'll be honest, this is a waste of time.
 * Sir Vant is part of the new tutorial between 14 July 2008 and 17 Sept 2009, a.k.a. Tutorial 2(Learing the Ropes).
 * Learning the Ropes was no longer in use by the time 530 came around. They were already working on Tutorial 3(Unstable Foundations) with a different dialogue that was fully voiced. Learning the Ropes was removed eventually.
 * I'm doing this to preserve the dialogue during this period, as there are sceneries left around for this.
 * So much effort was put into Tutorial 1(Tutorial Island), that this shouldn't replace it. In fact, modern RS3 has tried to multiple times between 2016 and 2022, but they always end up going back to Tutorial Island. For example, Tutorial Island was removed on 28 November 2012, returned as a special underwater version on 14 December 2015, and was fully reinstated on 14 May 2018.
 */
@Initializable
class SirVantDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirVantDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirVantDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_VANT_7942)
    }
}

class SirVantDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onPredicate { true }
                .npcl(FacialExpression.HAPPY,"Hello there. I'm Sir Vant.")
                .playerl(FacialExpression.THINKING, "Why are you down in this cave?")
                .npcl("I'm guarding the entrance to the dragon's lair.")
                .playerl(FacialExpression.WORRIED, "Dragon?")
                .npcl("Yes, but not just any dragon - this one has three heads.")
                .playerl("So why don't you kill it?")
                .npc(FacialExpression.SAD,"I'm too exhausted, but I can hold it off. I've sent for", "more help from Falador, but it might be a while before", "the message gets there.")
                .playerl("I could kill the dragon for you.") // Technically you have a list of options, but they are all a repeat of tutorial with an action cutscene.
                .npc("Thank you very much for the offer; however, I would", "be remiss in my duties as a White Knight of Falador if", "I were to let you do that.")
                //.npc("Thank you very much for the offer; however, I would", " be remiss in my duties as a White Knight of Falador if", "I were to let you do that. Here, for your kind thoughts", "you may have these old lamps.")
                // He gives you 2 exp lamps of 250XP each, but I didn't want to implement it.
                .end()
    }
}
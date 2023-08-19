package content.region.asgarnia.burthorpe.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Burthorpe Soldier Dialogue
 * @author 'Vexia
 * @author ovenbread
 *
 * https://www.youtube.com/watch?v=5RfEUmIdH10 for full interaction
 * https://www.youtube.com/watch?v=ew0wTgv-2Kc for insults
 */
@Initializable
class BurthorpeSoldierDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object {
        val latinInsults = arrayOf(
                "Mihi ignosce. Cum homine de cane debeo congredi.",
                "Errare humanum est.",
                "Die dulci freure.",
                "Carpe Diem!",
                "Te audire non possum. Musa sapientum fixa est in aure.",
                "Furnulum pani nolo.",
                "Fac ut gaudeam.",
                "Utinam barbari spatium proprium tuum invadant!",
                "Quantum materiae materietur marmota monax si marmota monax materiam possit materiari?",
                "Sona si Latine loqueris.",
                "Raptus Regaliter",
                "Nemo dat quod non habet.",
                "Ne auderis delere orbem rigidum meum!",
                "Da mihi sis bubulae frustum assae, solana tuberosa in modo gallico fricta, ac quassum lactatum coagulatum crassum.",
                "Cogito ergo sum.",
                "Vacca foeda.",
                "Di! Ecce hora! Uxor mea me necabit!",
                "Latine loqui coactus sum.",
                "Cave ne ante ullas catapultas ambules.",
                "Fac ut vivas!",
                "Noli me vocare, ego te vocabo.",
                "Meliora cogito.",
                "Braccae tuae aperiuntur.",
                "Vescere bracis meis.",
                "Corripe cervisiam!",
        )

        val randomStages = arrayOf(10, 20, 30, 40, 50)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
            1 -> npcl(FacialExpression.ANGRY, latinInsults.random()).also { stage = randomStages.random() }

            10 -> playerl(FacialExpression.THINKING, "What?!").also { stage = END_DIALOGUE }

            20 -> playerl(FacialExpression.THINKING, "Huh?!").also { stage = END_DIALOGUE }

            30 -> playerl(FacialExpression.HALF_GUILTY, "Er...").also { stage = END_DIALOGUE }

            40 -> playerl(FacialExpression.HALF_GUILTY, "OK...").also { stage = END_DIALOGUE }

            50 -> playerl(FacialExpression.THINKING, "Are you insulting me in Latin?").also { stage++ }
            51 -> npcl(FacialExpression.FRIENDLY, "Yes!").also { stage++ }
            52 -> playerl(FacialExpression.HALF_GUILTY, "Hmm...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BurthorpeSoldierDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SOLDIER_1065)
    }
}
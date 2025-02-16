package content.region.kandarin.ardougne.quest.biohazard.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs


/**
 * For some reason the level 13 mourner upstairs is called the boss
 * And despite the fact that other NPCs say there's one sick person upstairs they're 2 up there.
 *
 * We should be using key 423 but that got incorrectly used for Lost Tribe
 */

@Initializable
class MournerBossDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object{
        const val HOLD_BREATH = 10
        const val PRAY = 20
        const val FATAL = 30
        const val HAVE_KEY = 40
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipment(player!!, Items.DOCTORS_GOWN_430)){
            playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = if (hasAnItem(player!!, Items.KEY_5010).exists()) HAVE_KEY else START_DIALOGUE + 1 }
            return true
        }
        else {
            sendDialogue("The mourner doesn't feel like talking.").also { stage = END_DIALOGUE }
            return false
        }
    }


    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE + 1  -> npcl(FacialExpression.ASKING, "A doctor? At last! I don't know what I've eaten but I feel like I'm on death's door.").also { stage++ }
            START_DIALOGUE + 2 -> playerl(FacialExpression.NEUTRAL, "Hmm... interesting, sounds like food poisoning.").also { stage++ }
            // Jagex didn't include a question mark here
            START_DIALOGUE + 3 -> npcl(FacialExpression.ASKING, "Yes, I'd figured that out already. What can you give me to help.").also { stage++ }
            START_DIALOGUE + 4 -> showTopics(
                Topic("Just hold your breath and count to ten.", HOLD_BREATH),
                Topic("The best I can do is pray for you.", PRAY),
                Topic("There's nothing I can do, it's fatal.", FATAL)
            )


            HOLD_BREATH -> npcl(FacialExpression.SUSPICIOUS, "What? How will that help? What kind of doctor are you?").also { stage++ }
            HOLD_BREATH + 1 -> player(FacialExpression.HALF_GUILTY, "Erm... I'm new, I just started.").also { stage++ }
            HOLD_BREATH + 2 -> npcl(FacialExpression.ANGRY, "You're no doctor!").also {
                stage = END_DIALOGUE
                fight(player)
            }

            PRAY -> npcl(FacialExpression.ANGRY, "Pray for me? You're no doctor... You're an imposter!").also {
                stage = END_DIALOGUE
                fight(player)
            }

            FATAL -> npcl(FacialExpression.PANICKED, "No, I'm too young to die! I've never even had a girlfriend.").also { stage++ }
            FATAL + 1 -> playerl(FacialExpression.SAD, "That's life for you.").also { stage++ }
            FATAL + 2 -> npcl(FacialExpression.ASKING, "Wait a minute, where's your equipment?").also { stage++ }
            FATAL + 3 -> playerl(FacialExpression.HALF_GUILTY, "It's erm... at home.").also { stage++ }
            FATAL + 4 -> npcl(FacialExpression.ANGRY, "You're no doctor!").also {
                stage = END_DIALOGUE
                fight(player)
            }

            HAVE_KEY -> npcl(FacialExpression.NEUTRAL, "Sorry, I'd like to be left in peace.").also { stage = END_DIALOGUE }

        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_370)
    }

    private fun fight(player: Player){
        queueScript(player) { stage: Int ->
            if (stage == 1){
                npc.attack(player)
                return@queueScript stopExecuting(player)
            }
            return@queueScript delayScript(player, 1)
        }
    }
}

/**
 * Handles the key drop from the mourner boss
 */
@Initializable
class MournerBossNPC : AbstractNPC {
    var target: Player? = null
    private val supportRange: Int = 5

    //Constructor spaghetti because Arios I guess
    constructor() : super(NPCs.MOURNER_370, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return MournerBossNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_370)
    }

    override fun finalizeDeath(killer: Entity?) {
        val p = killer as Player
        if (!hasAnItem(p, Items.KEY_5010).exists()){
            queueScript(p, 1, QueueStrength.NORMAL){ stage: Int ->
                when(stage){
                    0 -> {
                        sendMessage(p, "You search the mourner...")
                    }
                    2 ->{
                        sendMessage(p, "and find a key.")
                        // If the player doesn't have space bad luck
                        // They can kill another mourner boss
                        // todo change this key and fix lost tribe key at the same time
                        // It should be 423 here and 5010 over there
                        // addItemOrDrop(p, Items.KEY_5010)
                        return@queueScript stopExecuting(p)
                    }
                }
                return@queueScript delayScript(p, 1)
            }
        }
        super.finalizeDeath(killer)
    }

}

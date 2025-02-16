package content.region.asgarnia.burthorpe.dialogue

import content.data.Quests
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Dunstan main dialogue.
 * @author 'ovenbread
 *
 * https://www.youtube.com/watch?v=gbqcJp99Zd8
 * https://www.youtube.com/watch?v=ujtIALS1L7A
 */
@Initializable
class DunstanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // When Troll Stronghold is complete
        if (isQuestComplete(player!!, Quests.TROLL_STRONGHOLD)) {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Hi! What can I do for you?").also { stage++ }
                2 -> showTopics(
                        Topic(FacialExpression.THINKING, "Can you put some spikes on my Climbing boots?", 30),
                        Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                        Topic(FacialExpression.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                        Topic(FacialExpression.FRIENDLY, "How is your son getting on?", 15),
                        // Sleds topic when Troll Romance is implemented.
                )
                10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
                12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
                14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
                15 -> npcl(FacialExpression.FRIENDLY, "He is getting on fine! He has just been promoted to Sergeant! I'm really proud of him!").also { stage++ }
                16 -> playerl(FacialExpression.FRIENDLY, "I'm happy for you!").also { stage++ }
                17 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
                30 -> playerl(FacialExpression.FRIENDLY, "Can you put some spikes on my Climbing boots?").also { stage++ }
                31 -> npcl(FacialExpression.NEUTRAL,"For you, no problem.").also { stage++ }
                32 -> npc(FacialExpression.THINKING, "Do you realise that you can only use the Climbing", "boots right now? The Spiked boots can only be used in", "the Icelands but no ones been able to get there for", "years!").also { stage++ }
                33 -> showTopics(
                        Topic(FacialExpression.NEUTRAL, "Yes, but I still want them.", 40, true),
                        Topic(FacialExpression.NEUTRAL, "Oh OK, I'll leave them thanks.", 43),
                )
                40 -> {
                    if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)  && inInventory(player!!, Items.IRON_BAR_2351)) {
                        sendDoubleItemDialogue(player!!, Items.IRON_BAR_2351, Items.CLIMBING_BOOTS_3105, "You give Dunstan an Iron bar and the climbing boots.")
                        sendMessage(player!!, "You give Dunstan an Iron bar and the climbing boots.")
                        if (removeItem(player!!, Item(Items.CLIMBING_BOOTS_3105)) && removeItem(player!!, Item(Items.IRON_BAR_2351))) {
                            addItemOrDrop(player!!, Items.SPIKED_BOOTS_3107)
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    } else if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)){
                        npcl(FacialExpression.NEUTRAL,"Sorry, I'll need an iron bar to make the spikes.")
                        stage = 2
                    } else {
                        playerl(FacialExpression.NEUTRAL,"I don't have them on me.")
                        stage = 2
                    }
                }
                41 -> sendItemDialogue(player!!, Items.SPIKED_BOOTS_3107, "Dunstan has given you the spiked boots.").also { stage++
                    sendMessage(player!!, "Dunstan has given you the spiked boots.")
                }
                43 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also {
                    stage = 2
                }
            }
            return true
        }

        // Troll Stronghold in progress
        if (isQuestInProgress(player!!, Quests.TROLL_STRONGHOLD, 1, 99)) {
            openDialogue(player!!, content.region.asgarnia.burthorpe.quest.trollstronghold.DunstanDialogueFile(), npc)
            return true
        }

        // When Death Plateau is complete
        if (isQuestComplete(player!!, Quests.DEATH_PLATEAU)) {
            when (stage) {
                START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Hi! What can I do for you?").also { stage++ }
                2 -> showTopics(
                        Topic(FacialExpression.THINKING, "Can you put some spikes on my Climbing boots?", 30),
                        Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                        Topic(FacialExpression.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
                        Topic(FacialExpression.FRIENDLY, "How is your son getting on?", 15),
                )
                10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
                11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
                12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
                13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
                14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
                15 -> npcl(FacialExpression.SAD, "He was captured by those cursed trolls! I don't know what to do. Even the imperial guard are too afraid to go rescue him.").also { stage++ }
                16 -> playerl(FacialExpression.ASKING, "What happened?").also { stage++ }
                17 -> npcl(FacialExpression.SAD, "Talk to Denulth, he can tell you all about it. Anything else before I get on with my work?").also { stage = 2 }
                30 -> npcl(FacialExpression.NEUTRAL,"For you, no problem.").also { stage++ }
                31 -> npc(FacialExpression.THINKING, "Do you realise that you can only use the Climbing", "boots right now? The Spiked boots can only be used in", "the Icelands but no ones been able to get there for", "years!").also { stage++ }
                32 -> showTopics(
                        Topic(FacialExpression.NEUTRAL, "Yes, but I still want them.", 40, true),
                        Topic(FacialExpression.NEUTRAL, "Oh OK, I'll leave them thanks.", 43),
                )
                40 -> {
                    if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)  && inInventory(player!!, Items.IRON_BAR_2351)) {
                        sendDoubleItemDialogue(player!!, Items.IRON_BAR_2351, Items.CLIMBING_BOOTS_3105, "You give Dunstan an Iron bar and the climbing boots.")
                        sendMessage(player!!, "You give Dunstan an Iron bar and the climbing boots.")
                        if (removeItem(player!!, Item(Items.CLIMBING_BOOTS_3105)) && removeItem(player!!, Item(Items.IRON_BAR_2351))) {
                            addItemOrDrop(player!!, Items.SPIKED_BOOTS_3107)
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    } else if (inInventory(player!!, Items.CLIMBING_BOOTS_3105)){
                        npcl(FacialExpression.NEUTRAL,"Sorry, I'll need an iron bar to make the spikes.")
                        stage = 2
                    } else {
                        playerl(FacialExpression.NEUTRAL,"I don't have them on me.")
                        stage = 2
                    }
                }
                41 -> sendItemDialogue(player!!, Items.SPIKED_BOOTS_3107, "Dunstan has given you the spiked boots.").also {
                    stage = 43
                    sendMessage(player!!, "Dunstan has given you the spiked boots.")
                }
                43 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also {
                    stage = 2
                }
            }
            return true
        }

        // Death Plateau in progress
        if (isQuestInProgress(player!!, Quests.DEATH_PLATEAU, 21, 24)) {
            // Call the dialogue file for Dunstan from the deathplateau quest folder.
            openDialogue(player!!, content.region.asgarnia.burthorpe.quest.deathplateau.DunstanDialogueFile(), npc)
            return true
        }

        // Default
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "Hi! Did you want something?").also { stage++ }
            2 -> showTopics(
                    Topic(FacialExpression.THINKING, "Is it OK if I use your anvil?", 10),
                    Topic(FacialExpression.FRIENDLY, "Nothing, thanks.", END_DIALOGUE),
            )
            10 -> npcl(FacialExpression.FRIENDLY, "So you're a smithy are you?").also { stage++ }
            11 -> playerl(FacialExpression.FRIENDLY, "I dabble.").also { stage++ }
            12 -> npcl(FacialExpression.FRIENDLY, "A fellow smith is welcome to use my anvil!").also { stage++ }
            13 -> playerl(FacialExpression.FRIENDLY, "Thanks!").also { stage++ }
            14 -> npcl(FacialExpression.FRIENDLY, "Anything else before I get on with my work?").also { stage = 2 }
        }
        return true
    }

    override fun newInstance(player: Player): DialoguePlugin {
        return DunstanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DUNSTAN_1082)
    }
}

package content.region.kandarin.quest.waterfall

import content.data.Quests
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

class AlmeraDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.ALMERA_304, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, AlmeraDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class AlmeraDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            when (getQuestStage(player, Quests.WATERFALL_QUEST)) {
                0 ->    loadLabel(player, "quest_not_started")
                10 ->   loadLabel(player, "quest_stage_10")
                20 ->   loadLabel(player, "quest_stage_20")
                30 ->   loadLabel(player, "quest_stage_30")
                100 ->  loadLabel(player, "quest_complete")
            }
        }

        label("quest_not_started")
            player(ChatAnim.NEUTRAL, "Hello.")
            npc(ChatAnim.NEUTRAL, "Ah, hello there. Nice to see an outsider for a change,", "are you busy? I have a problem.")
            options(
                DialogueOption("rush", "I'm afraid I'm in a rush.", expression = ChatAnim.NEUTRAL),
                DialogueOption("help", "How can I help?", expression = ChatAnim.NEUTRAL)
            )

        label("rush")
            npc(ChatAnim.HALF_GUILTY, "Oh okay, never mind.")

        label("help")
            npc(ChatAnim.THINKING, "It's my son Hudon, he's always getting into trouble, the", "boy's convinced there's hidden treasure in the river and",
                "I'm a bit worried about his safety, the poor lad can't", "even swim.")
            player(ChatAnim.THINKING, "I could go and take a look for you if you like?")
            exec { player, _ ->
                startQuest(player, Quests.WATERFALL_QUEST)
            }
            npc(ChatAnim.HAPPY, "Would you? You are kind. You can use the small raft", "out back if you wish, do be careful, the current down",
                "stream is very strong.")

        label("quest_stage_10")
            npc(ChatAnim.NEUTRAL, "Hello brave adventurer, have you seen my boy yet?")
            player(ChatAnim.NEUTRAL, "I'm afraid not, but I'm sure he hasn't gone far.")
            npc(ChatAnim.NEUTRAL, "I do hope so, you can't be too careful these days.")

        label("quest_stage_20")
            player(ChatAnim.NEUTRAL, "Hello again.")
            npc(ChatAnim.NEUTRAL, "Well hello, you're still around then.")
            player(ChatAnim.NEUTRAL, "I saw Hudon by the river but he refused to come back", "with me.")
            npc(ChatAnim.NEUTRAL, "Yes he told me, the foolish lad came in drenched to the", "bone, he had fallen into the waterfall, lucky he wasn't",
                "killed! Now he can spend the rest of the summer in his", "room.")
            player(ChatAnim.NEUTRAL, "Any ideas on what I could do while I'm here?")
            npc(ChatAnim.NEUTRAL, "Why don't you visit the tourist centre south of the", "waterfall?")

        label("quest_stage_30")
            player(ChatAnim.NEUTRAL, "Hello Almera.")
            npc(ChatAnim.NEUTRAL, "Hello adventurer, how's your treasure hunt going?")
            player(ChatAnim.NEUTRAL, "Oh, I'm just sight seeing.")
            npc(ChatAnim.NEUTRAL, "No adventurer stays this long just to sight see. But", "your business is yours alone, if you need to use the",
                "raft go ahead. But please try not to crash it this time!")
            player(ChatAnim.NEUTRAL, "Thanks Almera.")

        label("quest_complete")
            player(ChatAnim.NEUTRAL, "Hello Almera.")
            player(ChatAnim.NEUTRAL, "I did it, I found the treasure under the waterfall!")
            npc(ChatAnim.NEUTRAL, "Ah, very well done, adventurer!", "My boy Hudon was searching for that treasure too.")
            npc(ChatAnim.NEUTRAL, "Maybe you could share it with him, he's just a boy.")
            player(ChatAnim.NEUTRAL, "On second thought, I really have to go.")

    }
}
package content.region.kandarin.quest.waterfall

import content.data.Quests
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller

class HudonDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            when (getQuestStage(player, Quests.WATERFALL_QUEST)) {
                10 ->  loadLabel(player, "quest_stage_10")
                20 ->  loadLabel(player, "quest_stage_20")
                30 ->  loadLabel(player, "quest_stage_30")
                100 -> loadLabel(player, "quest_complete")
            }
        }

        label("quest_stage_10")
            player(ChatAnim.NEUTRAL, "Hello son, are you okay? You need help?")
            npc(ChatAnim.CHILD_NORMAL, "It looks like you need the help.")
            player(ChatAnim.NEUTRAL, "Your mum sent me to find you.")
            npc(ChatAnim.CHILD_NORMAL, "Don't play nice with me, I know you're looking for the", "treasure too.")
            player(ChatAnim.NEUTRAL, "Where is this treasure you talk of?")
            npc(ChatAnim.CHILD_NORMAL, "Just because I'm small doesn't mean I'm dumb! If I", "told you, you would take it all for yourself.")
            player(ChatAnim.NEUTRAL, "Maybe I could help.")
            exec { player, _ ->
                setQuestStage(player, Quests.WATERFALL_QUEST, 20)
            }
            npc(ChatAnim.CHILD_NORMAL, "I'm fine alone.")

        label("quest_stage_20")
            player(ChatAnim.NEUTRAL, "So you're still here.")
            npc(ChatAnim.CHILD_NORMAL, "I'll find that treasure soon, just you wait and see.")

        label("quest_stage_30")
            player(ChatAnim.NEUTRAL, "Hello again.")
            npc(ChatAnim.CHILD_NORMAL, "Not you still, why don't you give up?")
            player(ChatAnim.NEUTRAL, "And miss all the fun!")
            npc(ChatAnim.CHILD_NORMAL, "You do understand that anything you find you have to share with me.")
            player(ChatAnim.NEUTRAL, "Why's that?")
            npc(ChatAnim.CHILD_NORMAL, "Because I told you about the treasure.")
            player(ChatAnim.NEUTRAL, "Well, I wouldn't count on it.")
            npc(ChatAnim.CHILD_NORMAL, "That's not fair.")
            player(ChatAnim.NEUTRAL, "Neither is life, kid.")

        label("quest_complete")
            player(ChatAnim.NEUTRAL, "Hello again.")
            npc(ChatAnim.CHILD_NORMAL, "You stole my treasure. I saw you!")
            player(ChatAnim.NEUTRAL, "I'll make sure it goes to a good cause.")
            npc(ChatAnim.CHILD_NORMAL, "Hmmmm!")

    }
}
package content.region.fremennik.barbarianoutpost.dialogue

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Gunnjorn, the barbarian inside the Barbarian Outpost Agility Course.
 */

class GunnjornDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.GUNNJORN_607), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, GunnjornDialogueFile(), node.asNpc())
            return@on true
        }
    }
}

class GunnjornDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            if (getQuestStage(player, Quests.HORROR_FROM_THE_DEEP) >= 1
                && !inInventory(player, Items.LIGHTHOUSE_KEY_3848)
            ) {
                loadLabel(player, "options_horror")
            } else {
                loadLabel(player, "options_standard")
            }
        }

        label("options_horror")
        options(
            DialogueOption("horror_branch", "Talk about Horror from the Deep.", skipPlayer = true),
            DialogueOption("agility_branch", "Talk about the Agility course.", skipPlayer = true),
            DialogueOption("wall_branch", "Talk about the wall after the log balance.", skipPlayer = true),
            DialogueOption("exit_bye", "Nothing.", skipPlayer = true)
        )

        label("options_standard")
        options(
            DialogueOption("agility_branch", "Talk about the Agility course.", skipPlayer = true),
            DialogueOption("wall_branch", "Talk about the wall after the log balance.", skipPlayer = true),
            DialogueOption("rewards_branch", "Can I talk about rewards?", skipPlayer = true),
            DialogueOption("exit_bye", "Nothing.", skipPlayer = true)
        )

        // Horror from the Deep
        label("horror_branch")
        player(ChatAnim.HALF_ASKING, "Hi, are you called Gunnjorn?")
        npc(ChatAnim.FRIENDLY, "Why, indeed I am. I own this agility course, it can be", "very dangerous!")
        player(ChatAnim.FRIENDLY, "Yeah, that's great. Anyway, I understand you have a", "cousin named Larrissa who gave you a key...?")
        npc(ChatAnim.FRIENDLY, "Yes, she did! How do you know of this? She said she", "probably wouldn't need it, but gave it to me for safe", "keeping just in case.")
        player(ChatAnim.FRIENDLY, "Well, something has happened at the lighthouse, and she", "has been locked out. I need you to give me her key.")
        exec { player, _ ->
            if (freeSlots(player) == 0) {
                loadLabel(player, "horror_no_space")
            } else {
                loadLabel(player, "horror_give_key")
            }
        }

        label("horror_no_space")
        npc(ChatAnim.HALF_GUILTY, "Well, I would give you the key, but apparently you", "don't have any room.")
        goto("end")

        label("horror_give_key")
        npc("Sure. Here you go.")
        exec { player, _ ->
            addItem(player, Items.LIGHTHOUSE_KEY_3848)
            setVarbit(player, HorrorFromTheDeep.HFTD_LIGHTHOUSE_KEY, 1, true)
        }
        goto("end")

        // Agility Course
        label("agility_branch")
        player(ChatAnim.FRIENDLY, "Hey there. What is this place?")
        npc(ChatAnim.FRIENDLY, "Haha welcome to my obstacle course. Have fun, but", "remember this isn't a child's playground. People have", "died here.")
        npc(ChatAnim.FRIENDLY, "This course starts at the ropeswings to the east. When you've done the swing, head across the slippery log to the building. When you've traversed the obstacles inside, you'll come out on the other side.")
        npc(ChatAnim.FRIENDLY, "From there, head across the low walls to finish. If you've done all the obstacles as I've described, and in order, you'll get a lap bonus.")
        goto("end")

        label("wall_branch")
        player(ChatAnim.FRIENDLY, "What's wrong with the wall after the log balance?")
        npc(ChatAnim.FRIENDLY, "The wall after the log balance? Nothing, really. I just put some tough material on it, giving some people something to grip hold of.")
        player(ChatAnim.FRIENDLY, "Why would you do that?")
        npc(ChatAnim.FRIENDLY, "So people like you can have a tougher route round this course. Me and a mate get together and set up a new challenge that only the truly agile will conquer.")
        npc(ChatAnim.FRIENDLY, "The extra stuff starts at that wall; so, if you think you're up to it, I suggest you scramble up there after the log balance.")
        player(ChatAnim.FRIENDLY, "Sounds interesting. Anything else I should know?")
        npc(ChatAnim.FRIENDLY, "Nothing, really. Just make sure you complete the other obstacles before ya do. If you finish a full lap, you'll get an increased bonus for doing the tougher route.")
        npc(ChatAnim.FRIENDLY, "If you manage to do 250 laps of this advanced route without a single mistake, I'll let you have a special item.")
        npc(ChatAnim.FRIENDLY, "I'll keep track of your lap tallies, so you can check how you're getting on with me at any time.")
        player(ChatAnim.FRIENDLY, "That's all I need for now. Bye.")
        goto("exit_bye")

        label("rewards_branch")
        player(ChatAnim.FRIENDLY, "Can I talk about rewards?")
        npc(ChatAnim.FRIENDLY, "There's no reward for you just yet. Your lap count is only 0. It's 250 successful laps or no reward.") // todo add the lap counter
        goto("end")

        label("exit_bye")
        npc(ChatAnim.FRIENDLY, "Bye for now. Come back if you need any help.")
        goto("end")

        label("end")
    }
}
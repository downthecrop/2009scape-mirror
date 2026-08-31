package content.region.fremennik.lighthouse.quest.horror

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.handlers.DagannothBossCutscene
import content.region.fremennik.lighthouse.quest.horror.handlers.DagannothCutscene
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.NPCs

/**
 * The injured Jossik below the lighthouse during Horror From The Deep quest.
 */

class JossikLighthouseDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.JOSSIK_1335), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, JossikLighthouseDialogueFile(), node.asNpc())
            return@on true
        }
    }
}

class JossikLighthouseDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->

            val stage = getQuestStage(player, Quests.HORROR_FROM_THE_DEEP)
            val inBossFight = getAttribute(player, HorrorFromTheDeep.HFTD_COMBAT, false)
            val babyKiller = getAttribute(player, HorrorFromTheDeep.HFTD_BABY_DEAD, false)

            if (player.inCombat()
                || inBossFight) {
                sendMessage(player, "You are too busy to talk to Jossik.")
                loadLabel(player, "end")
            } else {
                when (stage) {
                    // stage 5 is the boss fight
                    5 -> {
                        if (!babyKiller) {
                            loadLabel(player, "s5")
                        } else {
                            loadLabel(player, "s5_mom")
                        }
                    }
                    // quest complete
                    100 -> loadLabel(player, "s100")
                    else -> loadLabel(player, "end")
                }
            }
        }

        // start of dag fight
        label("s5")
        npc(ChatAnim.SCARED, "*cough*", "Please... please help me...", "I think my leg is broken, and those creatures will be", "back any minute now!")
        player(ChatAnim.STRUGGLE, "I guess you're Jossik then...", "What creatures are you talking about?")
        npc(ChatAnim.SCARED, "I... I do not know.", "I have never seen their like before!")
        npc(ChatAnim.SCARED, "I was searching for information about my uncle Silas,", "who vanished mysteriously from this lighthouse many", "months ago. I found the secret of that strange wall, and", "discovered that I could use it as a door, but when I")
        npc(ChatAnim.SCARED, "came down here I was attacked by...")
        npc(ChatAnim.SCARED, "Well, I do not know what they are, but they are very", "strong! They hurt me badly enough to trap me here,", "and I have been fearing for my life ever since!")
        player(ChatAnim.FRIENDLY, "Don't worry, I'm here now.", "Larrissa was worried about you and asked for my help.")
        player(ChatAnim.FRIENDLY, "I'll go back upstairs and let her know that I've found", "you and that you're still alive, and then we can work", "out some way of getting you out of here, okay?")
        npc(ChatAnim.SCARED, "NO! No, you can't leave me now!", "Look! They're coming again! Do something!")
        exec { player, _ ->
            DagannothCutscene(player).start()
        }
        goto("end")

        // killed dag, now fight dag mother
        label("s5_mom")
        player(ChatAnim.FRIENDLY, "Okay, now that the creature's dead we can get you out", "of here.")
        npc(ChatAnim.SCARED, "No... you do not understand...")
        npc(ChatAnim.SCARED, "That was not the creature that attacked me...")
        npc(ChatAnim.SCARED, "That was one of its babies...")
        exec { player, _ ->
            DagannothBossCutscene(player).start()
        }
        goto("end")

        // quest complete
        label("s100")
        player(ChatAnim.FRIENDLY, "Okay, it's dead! Let's get out of here!")
        npc(ChatAnim.FRIENDLY, "Yes, quickly, the mother might be dead, but its children are not!")
        npc(ChatAnim.FRIENDLY, "Follow me upstairs, I might be able to help you with that casket you found.")
        npc(ChatAnim.FRIENDLY, "Bring it to my library, it looks familiar somehow...")
        goto("end")
    }
}
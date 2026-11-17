package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class BrotherAlthricDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.BROTHER_ALTHRIC_2588), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, BrotherAlthricDialogueFile(), node as NPC)
            return@on true
        }
    }
}

// when the player talks to brother althric
class BrotherAlthricDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
           if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) <= 40) {
               // varbit is 0 to start, 1 after you've first talked to brother althric, and 2 after you've gotten rid of the ring of charos
               when(getVarbit(player, GardenOfTranquility.VB_BRO_ALTHRIC)) {
                   // before you talk to brother althric
                   0 -> loadLabel(player, "quest_options")

                   // after you talk to him and have tried to charm him
                   1 -> {
                       // if ring is destroyed
                       if (!hasAnItem(player, Items.RING_OF_CHAROSA_6465).exists()) {
                           loadLabel(player, "i_have_destroyed_the_one_ring_the_shire_is_saved")
                       } else {
                           loadLabel(player, "ask_seeds_post_charm")
                       }
                   }

                   // 2 is handled properly in the 'Take-seed' listener when you interact with the rose bush
                   2 -> loadLabel(player, "nice_bush")
               }
            } else {
                loadLabel(player, "nice_bush")
            }
        }

        label("quest_options")
        options(
            DialogueOption("ask_seeds", "Can I have some rosebush seeds, please?"),
            DialogueOption("charm_path", "[Charm] These are the most beautiful rosebushes I've ever seen.",
                spokenText = "These are the most beautiful rosebushes I've ever seen.") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("nice_bush")
        player("Very nice rosebushes you have here.")
        npc("Yes, it has taken me many long hours in this garden to bring them to this state of near-perfection.")
        goto("end")

        label("ask_seeds")
        npc(ChatAnim.ANGRY, "Are you messing about with my rosebushes?")
        player(ChatAnim.SUSPICIOUS, "Um..I'm just... savouring their sweet perfume...")
        npc("No you're not - you're trying to steal some seeds. Well, you can leave them alone. It's take me many months of tending to bring them to this fine state of near-perfection.")
        player("Please?")
        npc("No. Now leave me in peace.")
        goto("end")

        label("charm_path")
        npc(ChatAnim.ANGRY, "Nice of you to say so, but I sense the use of some unnatural power underneath your fine words. You are concealing something of great evil, an evil that cannot touch someone of my purity.")
        player(ChatAnim.SUSPICIOUS, "Oh ... no, that's not true ...")
        npc(ChatAnim.ANGRY, "Nonsense, you reek of this evil power! I suggest that you throw whatever dark talisman it is that you are using down the Edgeville well, and never think of it again! Either that or just simply destroy it!")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_BRO_ALTHRIC, 1, true)
        }
        goto("end")

        label("ask_seeds_post_charm")
        npc(ChatAnim.ANGRY, "Are you messing about with my rosebushes?")
        player(ChatAnim.SUSPICIOUS, "Um..I'm just... savouring their sweet perfume...")
        npc(ChatAnim.ANGRY, "You still have not gotten rid of the evil thing that you carried! Do as I said to you before, and throw the thing down the Edgeville well, or simply destroy it!")
        goto("end")

        label("i_have_destroyed_the_one_ring_the_shire_is_saved")
        npc("Are you messing about with my rosebushes?")
        player(ChatAnim.SUSPICIOUS, "Er ... no ... I was just admiring them.")
        npc("Ah, I sense that you speak so without the use of the evil power you carried before. That is good. As you have taken the road back towards the light, you may take any seeds that you wish.")
        exec { player, _ ->
            if (getVarbit(player, GardenOfTranquility.VB_RING_WELL) == 1) {
                loadLabel(player, "well_path")
            } else {
                loadLabel(player, "destroyed_path")
            }
        }

        label("well_path")
        player("I threw it down the Edgeville well like you told me.")
        npc("That is good. Although it could always be regained by someone using an implement long enough to reach the bottom of the well, something with a hook on the end to catch the ring with.")
        npc("I hope I haven't put any ideas into your head.")
        goto("finalize_quest")

        label("destroyed_path")
        player("I destroyed it like you told me.")
        npc("That is good. Although I sense that an evil talisman like that cannot truly be destroyed - it will probably try to find it's way back to it's master.")
        npc("I pray that you don't try and seek it out again.")
        goto("finalize_quest")

        label("finalize_quest")
        player(ChatAnim.SUSPICIOUS, "Oh no, I definitely wouldn't try something like that...")
        exec { player, _ ->
            setVarbit(player, GardenOfTranquility.VB_BRO_ALTHRIC, 2, true)
        }
        goto("end")
    }
}
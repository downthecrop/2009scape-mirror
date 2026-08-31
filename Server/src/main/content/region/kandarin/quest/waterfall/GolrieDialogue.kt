package content.region.kandarin.quest.waterfall

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class GolrieDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.GOLRIE_306, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, GolrieDialogueFile(null), node as NPC)
            return@on true
        }
    }
}

class GolrieDialogueFile(val startLabel: String?) : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            loadLabel(player, startLabel?: "init")
            return@exec
        }

        label("init")
            player(ChatAnim.NEUTRAL, "Hello, is your name Golrie?")
            npc(ChatAnim.OLD_CALM_TALK2, "That's me. I've been stuck in here for weeks, those", "goblins are trying to steal my family's heirlooms. My",
                "grandad gave me all sorts of old junk.")
            player(ChatAnim.NEUTRAL, "Do you mind if I have a look?")
            afterClose { player ->
                queueScript(player, 0, QueueStrength.NORMAL) { stage ->
                    when (stage) {
                        0 -> {
                            lock(player, 4)
                            sendMessage(player, "You look amongst the junk on the floor")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            if (hasAnItem(player, Items.GLARIALS_PEBBLE_294).exists()) {
                                sendMessage(player, "You find nothing of interest.")
                                return@queueScript stopExecuting(player)
                            } else {
                                sendMessage(player, "Mixed with the junk on the floor you find Glarial's pebble.")
                                return@queueScript delayScript(player, 2)
                            }
                        }
                        2 -> {
                            DialogueLabeller.open(player, GolrieDialogueFile("got_pebble"),
                                findLocalNPC(player, NPCs.GOLRIE_306)?: findNPC(NPCs.GOLRIE_306)!!)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
                return@afterClose
            }
            npc(ChatAnim.OLD_HAPPY, "No, of course not.")

        label("got_pebble")
            player(ChatAnim.NEUTRAL, "Could I take this old pebble?")
            afterClose { player ->
                addItemOrDrop(player, Items.GLARIALS_PEBBLE_294)
                if (removeItem(player, Items.A_KEY_293)) {
                    sendMessage(player, "You give Golrie the key.")
                    queueScript(player, 2, QueueStrength.NORMAL) {
                        DialogueLabeller.open(player, GolrieDialogueFile("thanks"),
                            findLocalNPC(player, NPCs.GOLRIE_306)?: findNPC(NPCs.GOLRIE_306)!!)
                        return@queueScript stopExecuting(player)
                    }
                }
                return@afterClose
            }
            npc(ChatAnim.OLD_CALM_TALK1, "Oh that, yes have it, it's just some old elven junk I", "believe.")

        label("thanks")
            npc(ChatAnim.OLD_CALM_TALK1, "Thanks a lot for the key traveller. I think I'll wait in", "here until those goblins get bored and leave.")
            player(ChatAnim.NEUTRAL, "OK... Take care Golrie.")

        label("door_no_business_here")
            npc(ChatAnim.OLD_CALM_TALK1, "What are you doing down here?", "Leave before you get yourself into trouble.")

        label("door_on_quest")
            player(ChatAnim.NEUTRAL, "Hello, are you ok?")
            npc(ChatAnim.OLD_CALM_TALK1, "It's just those blasted hobgoblins. I locked myself in here for protection. But I've left the key somewhere and now I'm stuck.")
            exec { player, _ ->
                if (inInventory(player, Items.A_KEY_293)) {
                    loadLabel(player, "door_yes_key")
                } else {
                    loadLabel(player, "door_no_key")
                }
                return@exec
            }

        label("door_no_key")
            player(ChatAnim.NEUTRAL, "OK... I'll have a look for a key.")

        label("door_yes_key")
            player(ChatAnim.NEUTRAL, "I found a key.")
            npc(ChatAnim.OLD_CALM_TALK2, "Well don't wait all day, give it a try!")
            player(ChatAnim.NEUTRAL, "OK...")

    }
}
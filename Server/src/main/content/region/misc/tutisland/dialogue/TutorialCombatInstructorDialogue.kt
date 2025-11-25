package content.region.misc.tutisland.dialogue

import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import content.region.misc.tutisland.handlers.TutorialStage
import content.region.misc.tutisland.handlers.sendStageDialog
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item

class CombatInstructorDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.COMBAT_INSTRUCTOR_944, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, CombatInstructorDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class CombatInstructorDialogueFile : DialogueLabeller() {
    fun lostWeapon(player: Player, id: Int): Boolean {
        return !inInventory(player, id) && !inEquipment(player, id)
    }

    override fun addConversation() {
        assignToIds(NPCs.COMBAT_INSTRUCTOR_944)

        exec { player, _ ->
            when (getAttribute(player, "tutorial:stage", 0)) {
                44 -> loadLabel(player, "hello")
                47 -> loadLabel(player, "butter")
                48, 49, 50, 51, 52 -> loadLabel(player, if (lostWeapon(player, Items.BRONZE_SWORD_1277)) "lost sword" else if (lostWeapon(player, Items.WOODEN_SHIELD_1171)) "lost shield" else "nowhere")
                53 -> loadLabel(player, "killed rat")
                54 -> loadLabel(player, if (lostWeapon(player, Items.SHORTBOW_841)) "lost bow" else if (lostWeapon(player, Items.BRONZE_ARROW_882)) "lost arrows" else "nowhere")
                else -> loadLabel(player, "nowhere")
            }
        }

        label("hello")
        player(ChatAnim.FRIENDLY, "Hi! My name's ${player?.username}.", unclosable = true)
        npc(ChatAnim.ANGRY, "Do I look like I care? To me you're just another newcomer who thinks they're ready to fight.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "I'm Vannaka, the greatest swordsman alive.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "Let's get started by teaching you to wield a weapon.", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 45)
            TutorialStage.load(player, 45)
        }

        label("butter")
        npc(ChatAnim.FRIENDLY, "Very good, but that little butter knife isn't going to protect you much. Here, take these.", unclosable = true)
        exec { player, _ ->
            addItemOrDrop(player, Items.BRONZE_SWORD_1277)
            addItemOrDrop(player, Items.WOODEN_SHIELD_1171)
        }
        item(Item(Items.BRONZE_SWORD_1277), Item(Items.WOODEN_SHIELD_1171), "The Combat Guide gives you a <col=08088A>bronze sword</col> and a", "<col=08088A>wooden shield</col>!", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 48)
            TutorialStage.load(player, 48)
        }
        goto("nowhere")

        label("lost sword")
        exec { player, _ -> addItemOrDrop(player, Items.BRONZE_SWORD_1277) }
        item(Item(Items.BRONZE_SWORD_1277), "The Combat Guide gives you a spare sword.", unclosable = true)
        exec { player, _ -> if (lostWeapon(player, Items.WOODEN_SHIELD_1171)) loadLabel(player, "lost shield") }
        goto("nowhere")

        label("lost shield")
        exec { player, _ -> addItemOrDrop(player, Items.WOODEN_SHIELD_1171) }
        item(Item(Items.WOODEN_SHIELD_1171), "The Combat Guide gives you a spare shield.", unclosable = true)
        goto("nowhere")

        label("killed rat")
        player(ChatAnim.FRIENDLY, "I did it! I killed a giant rat!", unclosable = true)
        npc(ChatAnim.FRIENDLY, "I saw, ${player?.username}. You seem better at this than I thought. Now that you have grasped basic swordplay, let's move on.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "Let's try some ranged attacking, with this you can kill foes from a distance. Also, foes unable to reach you are as good as dead. You'll be able to attack the rats, without entering the pit.", unclosable = true)
        exec { player, _ ->
            addItemOrDrop(player, Items.SHORTBOW_841)
            addItemOrDrop(player, Items.BRONZE_ARROW_882, 30)
        }
        item(Item(Items.SHORTBOW_841), Item(Items.BRONZE_ARROW_882), "The Combat Guide gives you some <col=08088A>bronze arrows</col> and", "a <col=08088A>shortbow</col>!", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 54)
            TutorialStage.load(player, 54)
        }
        goto("nowhere")

        label("lost bow")
        exec { player, _ -> addItemOrDrop(player, Items.SHORTBOW_841) }
        item(Item(Items.SHORTBOW_841), "The Combat Guide gives you a spare bow.", unclosable = true)
        goto("nowhere")

        label("lost arrows")
        exec { player, _ -> addItemOrDrop(player, Items.BRONZE_ARROW_882, 10) }
        item(Item(Items.BRONZE_ARROW_882), "You receive some spare arrows.", unclosable = true)
        goto("nowhere")

        label("nowhere")
        exec { player, _ -> sendStageDialog(player) }
    }
}

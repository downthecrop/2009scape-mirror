package content.region.morytania.werewolfagility

import core.api.*
import core.game.dialogue.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * https://www.youtube.com/watch?v=mIKPpc30XBQ - What stick
 */
class AgilityTrainerDialogue : InteractionListener {

    override fun defineListeners() {
        on(NPCs.AGILITY_TRAINER_1664, IntType.NPC, "give-stick") { player, node ->
            if (inInventory(player, Items.STICK_4179)) {
                removeItem(player, Items.STICK_4179)
                rewardXP(player, Skills.AGILITY, 190.0)
                sendMessage(player, "You give the stick to the werewolf.")
                return@on true
            }
            DialogueLabeller.open(player, object : DialogueLabeller() {
                override fun addConversation() {
                    assignToIds(NPCs.AGILITY_TRAINER_1664)
                    // shorts/TO-vdlyOa3E
                    npc(ChatAnim.WEREWOLF_NEUTRAL, "Have you brought the stick yet?")
                    player("What stick?")
                    npc(ChatAnim.WEREWOLF_NEUTRAL, "Come on, get round that course - I need something to chew!")
                }
            }, node as NPC)
            return@on true
        }
    }
}
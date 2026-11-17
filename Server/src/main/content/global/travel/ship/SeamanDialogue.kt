package content.global.travel.ship

import content.data.Quests
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.Jingles
import org.rs09.consts.NPCs

/**
 * Represents the dialogue used to handle the sailing from and to Karamja.
 * @author Vexia
 * @author Ported to Kotlin by oftheshire
 */
@Initializable
class SeamanDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return SeamanDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        // Handle Quick-Pay option
        if (args.size > 1) {
            if (isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                // Cheaper fare if you've completed Karamja diary
                val price = if (player.achievementDiaryManager.getDiary(DiaryType.KARAMJA).isComplete(0)) 15 else 30

                if (removeItem(player, Item(Items.COINS_995, price))) {
                    sendMessage(player, "You pay $price coins and board the ship.")
                    travelToKaramja(player)
                } else {
                    // Start dialogue at the "no money" sequence
                    openDialogue(player, SeamanDialogueFile(quickPayFailure = true), npc)
                }
            } else {
                sendMessage(player, "You may only use the Pay-fare option after completing Pirate's Treasure.")
            }
            return true
        }

        // Handle standard Talk-To option
        openDialogue(player, SeamanDialogueFile(quickPayFailure = false), npc)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_TOBIAS_376, NPCs.SEAMAN_LORRIS_377, NPCs.SEAMAN_THRESNOR_378)
    }
}

class SeamanDialogueFile(private val quickPayFailure: Boolean) : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            if (quickPayFailure) {
                loadLabel(player, "no_money")
            } else {
                loadLabel(player, "start")
            }
        }

        label("start")
        npc(ChatAnim.HALF_GUILTY, "Do you want to go on a trip to Karamja?")
        npc(ChatAnim.HALF_GUILTY, "The trip will cost you 30 coins.")

        options(
            DialogueOption("pay_fare", "Yes, please."),
            DialogueOption("end", "No, thank you."),
            DialogueOption("charm_free", "(Charm) Or I could pay you nothing at all...",
                spokenText = "Or I could pay you nothing at all...") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("pay_fare")
        exec { player, _ ->
            if (player.achievementDiaryManager.getDiary(DiaryType.KARAMJA).isComplete(0)) {
                loadLabel(player, "discount")
            } else {
                loadLabel(player, "full_price")
            }
        }

        label("discount")
        npc(ChatAnim.HALF_GUILTY, "Wait a minute... Aren't those Karamja gloves?", "Thought I'd seen you helping around the island.", "You can go on half price - 15 coins.")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, 15))) {
                sendMessage(player, "You pay 15 coins and board the ship.")
                travelToKaramja(player)
                loadLabel(player, "end")
            } else {
                loadLabel(player, "no_money")
            }
        }

        label("full_price")
        exec { player, _ ->
            if (removeItem(player, Item(Items.COINS_995, 30))) {
                sendMessage(player, "You pay 30 coins and board the ship.")
                travelToKaramja(player)
                loadLabel(player, "end")
            } else {
                loadLabel(player, "no_money")
            }
        }

        label("charm_free")
        npc(ChatAnim.HALF_GUILTY, "Mmmm ... Nothing at all you say ...")
        npc(ChatAnim.HALF_GUILTY, "Yes, why not - jump aboard then.")
        exec { player, _ ->
            if (!player.achievementDiaryManager.getDiary(DiaryType.FALADOR).isComplete(1, 10)) {
                player.achievementDiaryManager.getDiary(DiaryType.FALADOR).updateTask(player, 1, 10, true)
            }
            travelToKaramja(player)
        }
        goto("end")

        label("no_money")
        player(ChatAnim.HALF_GUILTY, "Sorry, I don't have enough coins for that.")
        goto("end")
    }
}

/**
 * Top-level function used to handle the sailing cutscene/travel logic.
 */
fun travelToKaramja(player: Player) {
    Ships.PORT_SARIM_TO_KARAMAJA.sail(player)
    playJingle(player, Jingles.SAILING_JOURNEY_172)
}
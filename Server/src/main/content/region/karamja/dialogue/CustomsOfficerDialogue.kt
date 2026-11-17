package content.region.karamja.dialogue

import content.data.Quests
import content.global.travel.ship.Ships
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.Jingles
import org.rs09.consts.NPCs

/**
 * Represents the dialogue plugin used to handle the customs officer.
 * @author Vexia
 * @author Ported to Kotlin by oftheshire
 */

@Initializable
class CustomsOfficerDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player): DialoguePlugin {
        return CustomsOfficerDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val isQuickPay = args.size > 1

        if (isQuickPay) {
            if (isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                openDialogue(player, CustomsOfficerDialogueFile(quickPay = true), npc)
            }
            return true
        }

        openDialogue(player, CustomsOfficerDialogueFile(quickPay = false), npc)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CUSTOMS_OFFICER_380)
    }
}

class CustomsOfficerDialogueFile(private val quickPay: Boolean) : DialogueLabeller() {
    
    override fun addConversation() {
        exec { player, _ ->
            if (quickPay) {
                if (inInventory(player, Items.KARAMJAN_RUM_431)) {
                    loadLabel(player, "smuggle_fail")
                } else {
                    loadLabel(player, "pay_prompt")
                }
            } else {
                loadLabel(player, "start")
            }
        }

        label("start")
        npc(ChatAnim.HALF_GUILTY, "Can I help you?")
        options(
            DialogueOption("ask_journey", "Can I journey on this ship?"),
            DialogueOption("ask_customs", "Does Karamja have unusual customs then?")
        )

        label("ask_customs")
        npc(ChatAnim.HALF_GUILTY, "I'm not that sort of customs officer.")
        goto("end")

        label("ask_journey")
        npc(ChatAnim.HALF_GUILTY, "You need to be searched before you can board.")
        options(
            DialogueOption("why_search", "Why?"),
            DialogueOption("search_away", "Search away, I have nothing to hide."),
            DialogueOption("no_search", "You're not putting your hands on my things!")
        )

        label("why_search")
        npc(ChatAnim.HALF_GUILTY, "Because Asgarnia has banned the import of intoxicating", "spirits.")
        goto("end")

        label("no_search")
        npc(ChatAnim.HALF_GUILTY, "You're not getting on this ship then.")
        goto("end")

        label("search_away")
        exec { player, _ ->
            if (inInventory(player, Items.KARAMJAN_RUM_431)) {
                loadLabel(player, "smuggle_fail")
            } else {
                loadLabel(player, "pay_prompt")
            }
        }

        label("smuggle_fail")
        npc("Aha, trying to smuggle rum are we?")
        player(ChatAnim.HALF_GUILTY, "Umm... it's for personal use?")
        exec { player, _ ->
            if (removeAll(player, Items.KARAMJAN_RUM_431)) {
                sendMessage(player, "The customs officer confiscates your rum.")
                sendMessage(player, "You will need to find some way to smuggle it off the island...")
            }
        }
        goto("end")

        label("pay_prompt")
        exec { player, _ ->
            if (player.achievementDiaryManager.karamjaGlove != -1) {
                // price is 15 gp if you are wearing the Karamja gloves
                loadLabel(player, "pay_prompt_15")
            } else {
                // price is 30 gp
                loadLabel(player, "pay_prompt_30")
            }
        }

        label("pay_prompt_15")
        npc(ChatAnim.HALF_GUILTY, "Well you've got some odd stuff, but it's all legal. Now", "you need to pay a boarding charge of 15 coins.")
        goto("pay_options")

        label("pay_prompt_30")
        npc(ChatAnim.HALF_GUILTY, "Well you've got some odd stuff, but it's all legal. Now", "you need to pay a boarding charge of 30 coins.")
        goto("pay_options")

        label("pay_options")
        options(
            DialogueOption("pay_ok", "Ok."),
            DialogueOption("end", "Oh, I'll not bother then."),
            DialogueOption("pay_charm", "[Charm] Or perhaps you could let me travel for free?", 
                spokenText = "Or perhaps you could let me travel for free?") { player, _ ->
                inEquipment(player, Items.RING_OF_CHAROSA_6465)
            }
        )

        label("pay_ok")
        exec { player, _ ->
            val price = if (player.achievementDiaryManager.karamjaGlove != -1) 15 else 30
            if (removeItem(player, Item(Items.COINS_995, price))) {
                sendMessage(player, "You pay $price coins and board the ship.")
                sailFromCustoms(player)
                loadLabel(player, "end")
            } else {
                loadLabel(player, "no_money")
            }
        }

        label("pay_charm")
        npc(ChatAnim.HALF_GUILTY, "Yes, perhaps I could. Okay, jump aboard then.")
        exec { player, _ ->
            sailFromCustoms(player)
        }
        goto("end")

        label("no_money")
        player("Sorry, I don't seem to have enough coins.")
        goto("end")

        label("end")
    }

    private fun sailFromCustoms(player: Player) {
        val brimhavenLoc = Location(2771, 3227, 0)
        val karamjaLoc = Location(2954, 3147, 0)

        var ship: Ships? = null
        if (player.location.getDistance(brimhavenLoc) < 40) {
            ship = Ships.BRIMHAVEN_TO_ARDOUGNE
            playJingle(player, Jingles.SAILING_JOURNEY_SHORT_171)
        } else if (player.location.getDistance(karamjaLoc) < 40) {
            ship = Ships.KARAMJAMA_TO_PORT_SARIM
            playJingle(player, Jingles.SAILING_JOURNEY_172)
        }

        ship?.sail(player)
    }
}
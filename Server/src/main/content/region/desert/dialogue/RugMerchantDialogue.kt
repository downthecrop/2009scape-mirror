package content.region.desert.dialogue

import content.data.Quests
import content.global.skill.agility.AgilityHandler
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.colorize
import org.rs09.consts.Items
import org.rs09.consts.Jingles
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * The dialogue plugin used for the rug merchant.
 * @author Vexia
 * @author Ported to Kotlin by oftheshire
 */

class RugMerchantDialogue : InteractionListener {

    companion object {
        val merchants = intArrayOf(
            NPCs.RUG_MERCHANT_2291, NPCs.RUG_MERCHANT_2292,
            NPCs.RUG_MERCHANT_2293, NPCs.RUG_MERCHANT_2294,
            NPCs.RUG_MERCHANT_2296, NPCs.RUG_MERCHANT_2298,
            NPCs.RUG_MERCHANT_3020)
    }

    override fun defineListeners() {
        on(merchants, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, RugMerchantDialogueFile(false), node.asNpc())
            return@on true
        }

        on(merchants, IntType.NPC, "travel") { player, node ->
            DialogueLabeller.open(player, RugMerchantDialogueFile(true), node.asNpc())
            return@on true
        }
    }
}

class RugMerchantDialogueFile(private val quickTravel: Boolean) : DialogueLabeller() {

    private var chosenDestination: RugDestination? = null

    override fun addConversation() {
        val currentDest = RugDestination.forId(npc!!.id) ?: RugDestination.SHANTAY_PASS
        val destinations = getDestinations(npc!!.id)

        exec { player, _ ->
            if (quickTravel) {
                loadLabel(player, "show_destinations")
            } else {
                loadLabel(player, "start")
            }
        }

        label("start")
        player("Hello.")
        npc("Greetings, desert traveller. Do you require the services", "of Ali Morrisane's flying carpet fleet?")
        options(
            DialogueOption("show_destinations", "Yes, please."),
            DialogueOption("end", "No thanks.")
        )

        if (destinations.size == 1) {
            val dest = destinations[0]
            label("show_destinations")
            npc("Travel back to ${dest.destinationName}?")
            options(
                DialogueOption("pay_fare_single", "Yes.", skipPlayer = true),
                DialogueOption("end", "No.", skipPlayer = true)
            )

            label("pay_fare_single")
            exec { player, _ ->
                chosenDestination = dest
                loadLabel(player, "pay_fare")
            }
        } else {
            label("show_destinations")
            val opts = destinations.map { dest ->
                DialogueOption("pay_fare_dest_${dest.name}", dest.destinationName, skipPlayer = true)
            }.toTypedArray()
            options(*opts)

            destinations.forEach { dest ->
                label("pay_fare_dest_${dest.name}")
                exec { player, _ ->
                    chosenDestination = dest
                    loadLabel(player, "pay_fare")
                }
            }
        }

        label("pay_fare")
        exec { player, _ ->
            val dest = chosenDestination ?: return@exec

            if (dest == RugDestination.UZER && !isQuestComplete(player, Quests.THE_GOLEM)) {
                sendMessage(player, "You need to complete The Golem quest to access this.") // Needs authentic dialogue.
                return@exec
            }
            if (dest == RugDestination.BEDABIN_CAMP && !isQuestComplete(player, Quests.THE_TOURIST_TRAP)) {
                sendMessage(player, "You need to complete The Tourist Trap quest to access this.") // Needs authentic dialogue.
                return@exec
            }
            if (dest == RugDestination.SOPHANEM && !hasRequirement(player, Quests.ICTHLARINS_LITTLE_HELPER)) {
                return@exec
            }

            if (getItemFromEquipment(player, EquipmentSlot.WEAPON) != null) {
                sendMessage(player, colorize("%RYou must unequip all your weapons before you can fly on a carpet."))
                loadLabel(player, "end")
                return@exec
            }

            val cost = if (inEquipment(player, Items.RING_OF_CHAROSA_6465)) 100 else 200

            if (!inInventory(player, Items.COINS_995, cost)) {
                loadLabel(player, "no_money_$cost")
            } else {
                if (removeItem(player, Item(Items.COINS_995, cost))) {
                    dest.travel(currentDest, player)
                    loadLabel(player, "end")
                }
            }
        }

        label("no_money_100")
        npc("A travel on one of my rugs costs 100 gold", "coins.")
        goto("end")

        label("no_money_200")
        npc("A travel on one of my rugs costs 200 gold", "coins.")
        goto("end")
    }

    private fun getDestinations(npcId: Int): Array<RugDestination> {
        return when (npcId) {
            NPCs.RUG_MERCHANT_2291 -> arrayOf(RugDestination.UZER, RugDestination.BEDABIN_CAMP, RugDestination.NORTH_POLLNIVNEACH)

            NPCs.RUG_MERCHANT_2292,
            NPCs.RUG_MERCHANT_2293,
            NPCs.RUG_MERCHANT_2294 -> arrayOf(RugDestination.SHANTAY_PASS)

            NPCs.RUG_MERCHANT_3020 -> arrayOf(RugDestination.NARDAH, RugDestination.SOPHANEM)

            else -> arrayOf(RugDestination.SOUTH_POLLNIVNEACH)
        }
    }
}

/**
 * A destination for a rug.
 */

// TODO: This is mostly still Arios spaghetti, and could do with a better rewrite. This was ported to Kotlin during Garden of Tranquility.

enum class RugDestination(
    val npc: Int,
    val location: Location,
    val destinationName: String,
    vararg val locData: Location
) {
    SHANTAY_PASS(NPCs.RUG_MERCHANT_2291, Location(3308, 3110, 0), "Shantay Pass"),
    BEDABIN_CAMP(NPCs.RUG_MERCHANT_2292, Location(3180, 3045, 0), "Bedabin Camp", Location(3305, 3107, 0), Location(3299, 3107, 0), Location(3285, 3088, 0), Location(3285, 3073, 0), Location(3268, 3073, 0), Location(3263, 3068, 0), Location(3246, 3068, 0), Location(3246, 3057, 0), Location(3232, 3057, 0), Location(3215, 3057, 0), Location(3200, 3057, 0), Location(3179, 3057, 0), Location(3179, 3047, 0), Location(3180, 3045, 0)),
    NORTH_POLLNIVNEACH(NPCs.RUG_MERCHANT_2294, Location(3349, 3003, 0), "North Pollnivneach", Location(3308, 3096, 0), Location(3308, 3079, 0), Location(3308, 3066, 0), Location(3311, 3057, 0), Location(3319, 3042, 0), Location(3332, 3033, 0), Location(3341, 3020, 0), Location(3350, 3009, 0), Location(3351, 3003, 0), Location(3349, 3003, 0)),
    UZER(NPCs.RUG_MERCHANT_2293, Location(3469, 3113, 0), "Uzer", Location(3308, 3105, 0), Location(3325, 3105, 0), Location(3332, 3105, 0), Location(3332, 3080, 0), Location(3341, 3080, 0), Location(3341, 3082, 0), Location(3358, 3082, 0), Location(3370, 3082, 0), Location(3382, 3082, 0), Location(3396, 3082, 0), Location(3432, 3082, 0), Location(3432, 3093, 0), Location(3440, 3093, 0), Location(3454, 3107, 0), Location(3469, 3107, 0), Location(3469, 3113, 0)),
    NARDAH(NPCs.RUG_MERCHANT_2296, Location(3401, 2916, 0), "Nardah", Location(3351, 2942, 0), Location(3350, 2936, 0), Location(3362, 2936, 0), Location(3380, 2928, 0), Location(3392, 2920, 0), Location(3397, 2916, 0), Location(3401, 2916, 0)),
    SOPHANEM(NPCs.RUG_MERCHANT_2298, Location(3285, 2813, 0), "Sophanem", Location(3351, 2934, 0), Location(3351, 2928, 0), Location(3351, 2919, 0), Location(3346, 2902, 0), Location(3339, 2884, 0), Location(3328, 2877, 0), Location(3328, 2862, 0), Location(3328, 2845, 0), Location(3318, 2838, 0), Location(3307, 2828, 0), Location(3292, 2817, 0), Location(3285, 2818, 0), Location(3285, 2813, 0)),
    SOUTH_POLLNIVNEACH(NPCs.RUG_MERCHANT_3020, Location(3351, 2942, 0), "South Pollnivneach");

    companion object {
        fun forId(id: Int): RugDestination? {
            return values().find { it.npc == id }
        }

        const val ATTR_CARPET_LOGOUT = "magic-carpet"
        const val CARPET_VARP = 499 // Set to 1 to hide the rug that's on the ground.
    }

    fun travel(current: RugDestination, player: Player) {
        lock(player, 250)
        setVarp(player, CARPET_VARP, 0)
        player.impactHandler.disabledTicks = GameWorld.ticks + 200
        player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
        replaceSlot(player, EquipmentSlot.WEAPON.ordinal, Item(Items.MAGIC_CARPET_5614), container = Container.EQUIPMENT)
        playAudio(player, Sounds.CARPET_RISE_1196)
        playJingle(player, Jingles.MAGIC_CARPET_RIDE_132)

        registerLogoutListener(player, ATTR_CARPET_LOGOUT) { player ->
            removeItem(player, Items.MAGIC_CARPET_5614, Container.EQUIPMENT)
        }

        var count = 0
        var index = 0
        var locs = locData

        queueScript(player, 1, QueueStrength.STRONG) { _ ->
            count++
            when (count) {
                1 -> {
                    if (this@RugDestination == SHANTAY_PASS || this@RugDestination == SOUTH_POLLNIVNEACH) {
                        locs = (current.locData.reversedArray().toList() + this@RugDestination.location).toTypedArray()
                    }
                    face(player, current.location)
                }
                2 -> AgilityHandler.walk(player, -1, player.location, current.location, null, 0.0, null)
                3 -> face(player, location)
                4 -> setVarp(player, CARPET_VARP, 1)
                200 -> {} // Do nothing
                901 -> {
                    removeItem(player, Item(Items.MAGIC_CARPET_5614), Container.EQUIPMENT)
                    player.interfaceManager.restoreTabs()
                    player.impactHandler.disabledTicks = 0
                    unlock(player)
                    animate(player, -1)
                    setVarp(player, CARPET_VARP, 0)
                    playAudio(player, Sounds.CARPET_DESCEND_1195)
                    clearLogoutListener(player, ATTR_CARPET_LOGOUT)
                }
                902 -> {
                    player.moveStep()
                    return@queueScript stopExecuting(player)
                }
                else -> {
                    if (index > locs.size - 1) {
                        count = 900
                    } else if (index == 0 || player.location == locs[index - 1]) {
                        AgilityHandler.walk(player, -1, player.location, locs[index++], null, 0.0, null, true)
                    }
                }
            }

            return@queueScript keepRunning(player)
        }
    }
}
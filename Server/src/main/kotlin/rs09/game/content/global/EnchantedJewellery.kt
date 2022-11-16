package rs09.game.content.global

import api.*
import api.events.TeleportEvent
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.audio.Audio
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.node.entity.skill.magic.TeleportMethod
import rs09.game.node.entity.skill.slayer.SlayerManager.Companion.getInstance
import rs09.game.world.GameWorld.Pulser
import java.util.*

/**
 * Represents an enchanted jewellery.
 * @author Vexia & downthecrop
 */
enum class EnchantedJewellery(

        val options: Array<String>,
        val locations: Array<Location>, crumble: Boolean, vararg val ids: Int) {
    RING_OF_SLAYING(
            arrayOf(
                    "Sumona in Pollnivneach.",
                    "Morytania Slayer Tower.",
                    "Rellekka Slayer Caves.",
                    "Nowhere. Give me a slayer update."
            ),
            arrayOf(
                    Location.create(3361, 2994, 0),
                    Location.create(3428, 3535, 0),
                    Location.create(2792, 3615, 0),
            ),
            true,
            Items.RING_OF_SLAYING8_13281,
            Items.RING_OF_SLAYING7_13282,
            Items.RING_OF_SLAYING6_13283,
            Items.RING_OF_SLAYING5_13284,
            Items.RING_OF_SLAYING4_13285,
            Items.RING_OF_SLAYING3_13286,
            Items.RING_OF_SLAYING2_13287,
            Items.RING_OF_SLAYING1_13288
    ),

    RING_OF_DUELING(
            arrayOf(
                    "Al Kharid Duel Arena.",
                    "Castle Wars Arena.",
                    "Fist of Guthix", "Nowhere."
            ),
            arrayOf(
                    Location.create(3314, 3235, 0),
                    Location.create(2442, 3089, 0),
                    Location.create(1693, 5600, 0)
            ),
            true,
            Items.RING_OF_DUELLING8_2552,
            Items.RING_OF_DUELLING7_2554,
            Items.RING_OF_DUELLING6_2556,
            Items.RING_OF_DUELLING5_2558,
            Items.RING_OF_DUELLING4_2560,
            Items.RING_OF_DUELLING3_2562,
            Items.RING_OF_DUELLING2_2564,
            Items.RING_OF_DUELLING1_2566
    ),
    AMULET_OF_GLORY(
            arrayOf(
                    "Edgeville",
                    "Karamja",
                    "Draynor Village",
                    "Al-Kharid",
                    "Nowhere."
            ),
            arrayOf(
                    Location.create(3087, 3495, 0),
                    Location.create(2919, 3175, 0),
                    Location.create(3104, 3249, 0),
                    Location.create(3304, 3124, 0)
            ),
            Items.AMULET_OF_GLORY4_1712,
            Items.AMULET_OF_GLORY3_1710,
            Items.AMULET_OF_GLORY2_1708,
            Items.AMULET_OF_GLORY1_1706,
            Items.AMULET_OF_GLORY_1704
    ),
    AMULET_OF_GLORY_T(
            AMULET_OF_GLORY.options,
            AMULET_OF_GLORY.locations,
            Items.AMULET_OF_GLORYT4_10354,
            Items.AMULET_OF_GLORYT3_10356,
            Items.AMULET_OF_GLORYT2_10358,
            Items.AMULET_OF_GLORYT1_10360,
            Items.AMULET_OF_GLORYT_10362
    ),
    GAMES_NECKLACE(
            arrayOf(
                    "Burthorpe Games Room.",
                    "Barbarian Outpost.",
                    "Clan Wars.",
                    "Wilderness Volcano.",
                    "Corporeal Beast"
            ),
            arrayOf(
                    Location.create(2899, 3563, 0),
                    Location.create(2520, 3571, 0),
                    Location.create(3266, 3686, 0),
                    Location.create(3179, 3685, 0),
                    Location.create(2885, 4372, 2)
            ),
            true,
            Items.GAMES_NECKLACE8_3853,
            Items.GAMES_NECKLACE7_3855,
            Items.GAMES_NECKLACE6_3857,
            Items.GAMES_NECKLACE5_3859,
            Items.GAMES_NECKLACE4_3861,
            Items.GAMES_NECKLACE3_3863,
            Items.GAMES_NECKLACE2_3865,
            Items.GAMES_NECKLACE1_3867
    ),
    DIGSITE_PENDANT(arrayOf<String>(),
            arrayOf(
                    Location.create(3342, 3445, 0)
            ),
            true,
            Items.DIGSITE_PENDANT_5_11194,
            Items.DIGSITE_PENDANT_4_11193,
            Items.DIGSITE_PENDANT_3_11192,
            Items.DIGSITE_PENDANT_2_11191,
            Items.DIGSITE_PENDANT_1_11190
    ),
    COMBAT_BRACELET(
            arrayOf(
                    "Champions' Guild",
                    "Monastery",
                    "Ranging Guild",
                    "Warriors' Guild",
                    "Nowhere."
            ),
            arrayOf(
                    Location.create(3191, 3365, 0),
                    Location.create(3052, 3472, 0),
                    Location.create(2657, 3439, 0),
                    Location.create(2878, 3546, 0)
            ),
            Items.COMBAT_BRACELET4_11118,
            Items.COMBAT_BRACELET3_11120,
            Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET1_11124,
            Items.COMBAT_BRACELET_11126
    ),
    SKILLS_NECKLACE(
            arrayOf(
                    "Fishing Guild",
                    "Mining Guild",
                    "Crafting Guild",
                    "Cooking Guild",
                    "Nowhere."
            ),
            arrayOf(
                    Location.create(2611, 3392, 0),
                    Location.create(3016, 3338, 0),
                    Location.create(2933, 3290, 0),
                    Location.create(3143, 3442, 0)
            ),
            Items.SKILLS_NECKLACE4_11105,
            Items.SKILLS_NECKLACE3_11107,
            Items.SKILLS_NECKLACE2_11109,
            Items.SKILLS_NECKLACE1_11111,
            Items.SKILLS_NECKLACE_11113
    ),
    RING_OF_WEALTH(
            arrayOf(
                    "Grand Exchange",
                    "Nowhere."
            ),
            arrayOf(
                    Location.create(3163, 3464, 0)
            ),
            Items.RING_OF_WEALTH4_14646,
            Items.RING_OF_WEALTH3_14644,
            Items.RING_OF_WEALTH2_14642,
            Items.RING_OF_WEALTH1_14640,
            Items.RING_OF_WEALTH_14638
    );

    val isCrumble: Boolean = crumble

    /**
     * Constructs a new `EnchantedJewelleryPlugin` `Object`.
     * @param options the dialogue options.
     * @param locations the teleport locations.
     * @param ids the ordered item ids.
     */
    constructor(options: Array<String>, locations: Array<Location>, vararg ids: Int) : this(options, locations, false, *ids)

    /**
     * Method used to teleport the player to the desired location.
     * @param player the player.
     * @param item the used jewellery item.
     * @param buttonID the button id.
     * @param isEquipped If the player is operating.
     */
    fun use(player: Player, item: Item, buttonID: Int, isEquipped: Boolean) {
        if (buttonID > locations.size - 1) {
            if (isSlayerRing(item)) {
                slayerProgressDialogue(player)
            }
            return
        }
        val itemIndex = getItemIndex(item)
        val nextJewellery = Item(getNext(itemIndex))
        if (canTeleport(player, nextJewellery)) {
            Pulser.submit(object : Pulse(0) {
                private var count = 0
                private var location = getLocation(buttonID)
                override fun pulse(): Boolean {
                    when (count) {
                        0 -> {
                            lock(player,4)
                            visualize(player, ANIMATION, GRAPHICS)
                            playAudio(player, AUDIO, true)
                            player.impactHandler.disabledTicks = 4
                        }
                        3 -> {
                            teleport(player,location)
                            resetAnimator(player)
                            if (isLastItemIndex(itemIndex)) {
                                if (isCrumble) crumbleJewellery(player, item, isEquipped)
                            } else {
                                replaceJewellery(player, item, nextJewellery, isEquipped)
                            }
                            unlock(player)
                            player.dispatch(TeleportEvent(TeleportManager.TeleportType.NORMAL, TeleportMethod.JEWELRY, item, location))
                            return true
                        }
                    }
                    count += 1
                    return false
                }
            })
        }
    }

    private fun replaceJewellery(player: Player, item: Item, nextJewellery: Item, isEquipped: Boolean) {
        if (isEquipped) {
            replaceSlot(player, item.slot, nextJewellery, Container.EQUIPMENT)
        } else {
            replaceSlot(player, item.slot, nextJewellery)
        }
    }

    private fun crumbleJewellery(player: Player, item: Item, isEquipped: Boolean) {
        if (isEquipped) {
            removeItem(player, item, Container.EQUIPMENT)
        } else {
            removeItem(player, item)
        }
        if (isSlayerRing(item)) {
            addItem(player, Items.ENCHANTED_GEM_4155)
            sendMessage(player, "Your Ring of Slaying reverts back into a regular enchanted gem.")
        }
    }

    private fun isSlayerRing(item: Item): Boolean {
        return (item.id in RING_OF_SLAYING.ids)
    }

    private fun slayerProgressDialogue(player: Player) {
        val slayerManager = getInstance(player)
        if (!slayerManager.hasTask()) {
            sendNPCDialogue(player, slayerManager.master!!.npc, "You need something new to hunt. Come and " +
                    "see me When you can and I'll give you a new task.", FacialExpression.HALF_GUILTY)
            return
        }
        sendNPCDialogue(player, slayerManager.master!!.npc, "You're currently " +
                "assigned to kill ${getSlayerTaskName(player).lowercase(Locale.getDefault())}'s; " +
                "only ${getSlayerTaskKillsRemaining(player)} more to go.", FacialExpression.FRIENDLY)
        // Slayer tracker UI
        setVarbit(player, 2502, 0, slayerManager.flags.taskFlags shr 4)
    }

    private fun canTeleport(player: Player, item: Item): Boolean {
        return player.zoneMonitor.teleport(1, item)
    }

    private fun getNext(index: Int): Int {
        val i = index + 1
        if (i > ids.size - 1) {
            return ids[ids.size - 1]
        }
        return ids[i]
    }

    private fun getLocation(index: Int): Location {
        if (index > locations.size - 1) {
            return locations[locations.size - 1]
        }
        return locations[index]
    }

    fun getJewelleryName(item: Item): String {
        return item.name.replace(""" ?\(t?[0-9]?\)""".toRegex(), "")
    }

    fun getJewelleryType(item: Item): String {
        return when {
            this == GAMES_NECKLACE -> "games necklace"
            this == DIGSITE_PENDANT -> "necklace"
            this == COMBAT_BRACELET -> "bracelet"
            this == SKILLS_NECKLACE -> "necklace"
            else -> item.name.split(" ")[0].lowercase(Locale.getDefault())
        }
    }

    fun isLastItemIndex(index: Int): Boolean = index == ids.size - 1

    fun getItemIndex(item: Item): Int {
        return ids.indexOf(item.id)
    }

    companion object {
        private val ANIMATION = Animation(714)
        private val AUDIO = Audio(200)
        private val GRAPHICS = Graphics(308, 100, 50)
        val idMap = HashMap<Int, EnchantedJewellery>()

        init {
            values().forEach { entry ->
                entry.ids.forEach { idMap[it] = entry }
            }
        }
    }
}
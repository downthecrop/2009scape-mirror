package content.global.skill.construction.decoration.study

import content.global.handlers.item.TeleTabsListener
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import content.global.skill.construction.Decoration
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCityListeners.Companion.ARDOUGNE_TELE_ATTRIBUTE
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.game.world.GameWorld
import core.game.interaction.InterfaceListener
import org.rs09.consts.Items

/**
 * Handles the lectern
 * LecternPlugin
 * @author Ceikry
 */
@Initializable
class LecternPlugin : OptionHandler() {
    /**
     * TeleTabButton
     */
    private enum class TeleTabButton(
            /**
             * The button id
             */
            val buttonId: Int,
            /**
             * The level and xp
             */
            val level: Int,
            val xp: Double,
            /**
             * The item
             */
            val tabItem: Item,
            /**
             * The required decoration (lectern)
             */
            private val requiredDecorations: Array<Decoration>, vararg requiredItems: Item) {
        ARDOUGNE(2, 51, 61.0, Item(TeleTabsListener.TeleTabs.ADDOUGNE_TELEPORT.item), arrayOf<Decoration>(Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563, 2), Item(Items.WATER_RUNE_555, 2)),
        BONES_TO_BANANNAS(3, 15, 25.0, Item(Items.BONES_TO_BANANAS_8014), arrayOf<Decoration>(Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.NATURE_RUNE_561, 1), Item(Items.EARTH_RUNE_557, 2), Item(Items.WATER_RUNE_555, 2)),
        BONES_TO_PEACHES(4, 60, 35.5, Item(Items.BONES_TO_PEACHES_8015), arrayOf<Decoration>(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.NATURE_RUNE_561, 2), Item(Items.EARTH_RUNE_557, 4), Item(Items.WATER_RUNE_555, 4)),
        CAMELOT(5, 45, 55.5, Item(TeleTabsListener.TeleTabs.CAMELOT_TELEPORT.item), arrayOf<Decoration>(Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.AIR_RUNE_556, 5)),
        ENCHANT_DIAMOND(6, 57, 67.0, Item(Items.ENCHANT_DIAMOND_8019), arrayOf<Decoration>(Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 10)),
        ENCHANT_DRAGONSTONE(7, 68, 78.0, Item(Items.ENCHANT_DRAGONSTN_8020), arrayOf<Decoration>(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 15), Item(Items.WATER_RUNE_555, 15)),
        ENCHANT_EMERALD(8, 27, 37.0, Item(Items.ENCHANT_EMERALD_8017), arrayOf<Decoration>(Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.AIR_RUNE_556, 3)),
        ENCHANT_ONYX(9, 87, 97.0, Item(Items.ENCHANT_ONYX_8021), arrayOf<Decoration>(Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.EARTH_RUNE_557, 20), Item(Items.FIRE_RUNE_554, 20)),
        ENCHANT_RUBY(10, 49, 59.0, Item(Items.ENCHANT_RUBY_8018), arrayOf<Decoration>(Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.FIRE_RUNE_554, 5)),
        ENCHANT_SAPPHIRE(11, 7, 17.5, Item(Items.ENCHANT_SAPPHIRE_8016), arrayOf<Decoration>(Decoration.OAK_LECTERN, Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN, Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.COSMIC_RUNE_564), Item(Items.WATER_RUNE_555)),
        FALADOR(12, 37, 48.0, Item(TeleTabsListener.TeleTabs.FALADOR_TELEPORT.item), arrayOf<Decoration>(Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555), Item(Items.AIR_RUNE_556, 3)),
        LUMBRIDGE(13, 31, 41.0, Item(TeleTabsListener.TeleTabs.LUMBRIDGE_TELEPORT.item), arrayOf<Decoration>(Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557), Item(Items.AIR_RUNE_556, 3)),
        HOUSE(14, 40, 30.0, Item(Items.TELEPORT_TO_HOUSE_8013), arrayOf<Decoration>(Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.EARTH_RUNE_557), Item(Items.AIR_RUNE_556)),
        VARROCK(15, 25, 35.0, Item(TeleTabsListener.TeleTabs.VARROCK_TELEPORT.item), arrayOf<Decoration>(Decoration.OAK_LECTERN, Decoration.EAGLE_LECTERN, Decoration.TEAK_EAGLE_LECTERN, Decoration.MAHOGANY_EAGLE_LECTERN, Decoration.DEMON_LECTERN, Decoration.TEAK_DEMON_LECTERN, Decoration.MAHOGANY_DEMON_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563), Item(Items.FIRE_RUNE_554), Item(Items.AIR_RUNE_556, 3)),
        WATCHTOWER(16, 58, 68.0, Item(TeleTabsListener.TeleTabs.WATCH_TOWER_TELEPORT.item), arrayOf<Decoration>(Decoration.MAHOGANY_EAGLE_LECTERN), SOFT_CLAY, Item(Items.LAW_RUNE_563, 2), Item(Items.EARTH_RUNE_557, 2));

        /**
         * The required items
         */
        val requiredItems: ArrayList<Item> = arrayListOf(*requiredItems)

        /**
         * Checks if the player can make this tab
         * @param player
         * @return
         */
        fun canMake(player: Player): Boolean {
            val objectId = player.getAttribute<Int>("ttb:objectid")
            if (player.skills.getLevel(Skills.MAGIC) < level && player.spellBookManager.spellBook == 192) {
                player.sendMessage("You need a magic level of $level to make that!")
                return false
            }
            if (this == BONES_TO_PEACHES && !player.savedData.activityData.isBonesToPeaches) {
                player.sendMessages("You need the Bones to Peaches ability purchased from MTA before making these.", "This requirement doesn't apply to actually using the tabs.")
                return false
            }
            if(this == ARDOUGNE && !getAttribute(player, ARDOUGNE_TELE_ATTRIBUTE, false)){
                sendMessage(player, "You need to unlock Ardougne teleport before you can make a tablet.")
                return false

            }
            var found = false
            for (d in requiredDecorations) if (d.objectId == objectId) found = true
            if (!found) {
                player.sendMessage("You're unable to make this tab on this specific lectern.")
                return false
            }
            for (item in requiredItems) {
                val staff = MagicStaff.forId(item.id)
                if (staff != null && player.equipment.containsAtLeastOneItem(staff.staves)) {
                    continue
                }
                if (!player.inventory.containsItem(item)) {
                    //TODO staffs
                    player.sendMessage("You don't have enough materials.")
                    return false
                }
            }
            return true
        }

        companion object {
            /**
             * Gets the button for the id
             * @param id
             * @return
             */
            fun forId(id: Int): TeleTabButton? {
                for (ttb in values()) {
                    if (ttb.buttonId == id) return ttb
                }
                return null
            }
        }

    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any>? {
        for (i in 13642..13648) {
            SceneryDefinition.forId(i).handlers["option:study"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val id = node.asScenery().id
        player.setAttribute("ttb:objectid", id)
        GameWorld.Pulser.submit(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                when(counter++){
                    0 -> player.animator.animate(Animation(1894)).also { player.lock() }
                    1 -> player.interfaceManager.open(Component(400)).also { player.unlock(); return true }
                }
                return false
            }
        })
        return true
    }

    /**
     * TeleTabInterface
     * @author Ceikry
     */
    class TeleTabInterface : InterfaceListener {
        val decorationVarps = hashMapOf(
            Decoration.OAK_LECTERN to Pair(0, 0),
            Decoration.EAGLE_LECTERN to Pair(1, 0),
            Decoration.DEMON_LECTERN to Pair(0, 1),
            Decoration.TEAK_EAGLE_LECTERN to Pair(2, 0),
            Decoration.TEAK_DEMON_LECTERN to Pair(0, 2),
            Decoration.MAHOGANY_EAGLE_LECTERN to Pair(3, 0),
            Decoration.MAHOGANY_DEMON_LECTERN to Pair(0, 3),
        )
        override fun defineInterfaceListeners() {
            onOpen(400) { player, component -> 
                val id = player.getAttribute("ttb:objectid", 0)
                val deco = Decoration.forObjectId(id)
                val values = decorationVarps[deco] ?: Pair(0, 0)
                setVarp(player, 261, values.first)
                setVarp(player, 262, values.second)
                return@onOpen true
            }
            on(400) { player, _, _, buttonID, _, _ ->
                val ttb = TeleTabButton.forId(buttonID)
                if (ttb != null && ttb.canMake(player)) {
                    player.interfaceManager.close()
                    var requiredItemsCountingStaves = ttb.requiredItems.filter({ item ->
                        val staff = MagicStaff.forId(item.id)
                        !(staff != null && player.equipment.containsAtLeastOneItem(staff.staves))
                    }).toTypedArray()
                    player.pulseManager.run(object : Pulse(1) {
                        var counter = 0
                        override fun pulse(): Boolean {
                            when(counter++) {
                                0 -> {
                                    if (!ttb.canMake(player)) {
                                        return true
                                    }
                                    //TODO Add correct lectern animation (should look like raising arms progressively higher 3 times).
                                    player.animate(Animation(782))
                                }
                                2 -> {
                                    if (player.inventory.remove(*requiredItemsCountingStaves)) {
                                        // Should never drop, since soft clay was successfully removed
                                        addItemOrDrop(player, ttb.tabItem.id)
                                        player.skills.addExperience(Skills.MAGIC, ttb.xp, true)
                                        if (ttb == TeleTabButton.VARROCK
                                                && (player.getAttribute("ttb:objectid", 0) == Decoration.MAHOGANY_EAGLE_LECTERN.objectId
                                                        || player.getAttribute("ttb:objectid", 0) == Decoration.MAHOGANY_DEMON_LECTERN.objectId)) {
                                            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 2, 8)
                                        }
                                    } else {
                                        return true
                                    }
                                }
                            }
                            counter %= 6
                            return false
                        }
                    })
                }
                player.animate(Animation(-1))
                return@on true
            }
        }
    }

    companion object {
        /**
         * Soft clay
         */
        private val SOFT_CLAY = Item(Items.SOFT_CLAY_1761, 1)
    }
}

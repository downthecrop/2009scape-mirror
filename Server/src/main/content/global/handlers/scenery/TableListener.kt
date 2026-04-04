package content.global.handlers.scenery

import content.global.skill.summoning.pet.Pets
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.graves.GraveController
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.entity.player.info.login.PlayerParser
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.config.ItemConfigParser
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

/** To allow players to place items on tables. Copies DropListener for restrictions. */
class TableListener : InteractionListener {
    companion object {
        private val DROP_COINS_SOUND = Sounds.EYEGLO_COIN_10
        private val DROP_ITEM_SOUND = Sounds.PUT_DOWN_2739
    }
    override fun defineListeners() {
        onUseAnyWith(
            IntType.SCENERY,
            Scenery.TABLE_10258,
            Scenery.TABLE_10414,
            Scenery.TABLE_10490,
            Scenery.TABLE_10509,
            Scenery.TABLE_10808,
            Scenery.TABLE_11202,
            Scenery.TABLE_11203,
            Scenery.TABLE_11500,
            Scenery.TABLE_11501,
            Scenery.TABLE_11502,
            Scenery.TABLE_11503,
            Scenery.TABLE_11504,
            Scenery.TABLE_11551,
            Scenery.TABLE_11619,
            Scenery.TABLE_11660,
            Scenery.TABLE_11682,
            Scenery.TABLE_11743,
            Scenery.TABLE_11748,
            Scenery.TABLE_11749,
            Scenery.TABLE_11849,
            Scenery.TABLE_11892,
            Scenery.TABLE_12106,
            Scenery.TABLE_12108,
            Scenery.TABLE_12386,
            Scenery.TABLE_12387,
            Scenery.TABLE_12388,
            Scenery.TABLE_12450,
            Scenery.TABLE_12541,
            Scenery.TABLE_12544,
            Scenery.TABLE_12734,
            Scenery.TABLE_12855,
            Scenery.TABLE_12884,
            Scenery.TABLE_12962,
            Scenery.TABLE_12971,
            Scenery.TABLE_12972,
            Scenery.TABLE_14044,
            Scenery.TABLE_14049,
            Scenery.TABLE_14163,
            Scenery.TABLE_14363,
            Scenery.TABLE_14762,
            Scenery.TABLE_14763,
            Scenery.TABLE_14920,
            Scenery.TABLE_15044,
            Scenery.TABLE_15092,
            Scenery.TABLE_15137,
            Scenery.TABLE_15680,
            Scenery.TABLE_15684,
            Scenery.TABLE_15719,
            Scenery.TABLE_15982,
            Scenery.TABLE_16169,
            Scenery.TABLE_16170,
            Scenery.TABLE_16562,
            Scenery.TABLE_16737,
            Scenery.TABLE_16738,
            Scenery.TABLE_16739,
            Scenery.TABLE_16740,
            Scenery.TABLE_16741,
            Scenery.TABLE_16742,
            Scenery.TABLE_16815,
            Scenery.TABLE_16827,
            Scenery.TABLE_16828,
            Scenery.TABLE_16874,
            Scenery.TABLE_16974,
            Scenery.TABLE_17099,
            Scenery.TABLE_17145,
            Scenery.TABLE_17235,
            Scenery.TABLE_17299,
            Scenery.TABLE_17348,
            Scenery.TABLE_17545,
            Scenery.TABLE_17546,
            Scenery.TABLE_17661,
            Scenery.TABLE_17662,
            Scenery.TABLE_17666,
            Scenery.TABLE_17762,
            Scenery.TABLE_18051,
            Scenery.TABLE_18059,
            Scenery.TABLE_18062,
            Scenery.TABLE_18314,
            Scenery.TABLE_18486,
            Scenery.TABLE_18902,
            Scenery.TABLE_1901,
            Scenery.TABLE_19158,
            Scenery.TABLE_19928,
            Scenery.TABLE_20058,
            Scenery.TABLE_20060,
            Scenery.TABLE_20351,
            Scenery.TABLE_20388,
            Scenery.TABLE_20996,
            Scenery.TABLE_21359,
            Scenery.TABLE_21360,
            Scenery.TABLE_21388,
            Scenery.TABLE_21389,
            Scenery.TABLE_21608,
            Scenery.TABLE_21609,
            Scenery.TABLE_21610,
            Scenery.TABLE_21793,
            Scenery.TABLE_22159,
            Scenery.TABLE_22160,
            Scenery.TABLE_22431,
            Scenery.TABLE_22672,
            Scenery.TABLE_22677,
            Scenery.TABLE_22678,
            Scenery.TABLE_22690,
            Scenery.TABLE_22693,
            Scenery.TABLE_22694,
            Scenery.TABLE_22705,
            Scenery.TABLE_22830,
            Scenery.TABLE_22831,
            Scenery.TABLE_22832,
            Scenery.TABLE_22844,
            Scenery.TABLE_22845,
            Scenery.TABLE_22846,
            Scenery.TABLE_22847,
            Scenery.TABLE_22848,
            Scenery.TABLE_22849,
            Scenery.TABLE_23177,
            Scenery.TABLE_23942,
            Scenery.TABLE_24011,
            Scenery.TABLE_24012,
            Scenery.TABLE_24013,
            Scenery.TABLE_24014,
            Scenery.TABLE_24028,
            Scenery.TABLE_24076,
            Scenery.TABLE_24084,
            Scenery.TABLE_24088,
            Scenery.TABLE_24104,
            Scenery.TABLE_24301,
            Scenery.TABLE_24302,
            Scenery.TABLE_24303,
            Scenery.TABLE_24307,
            Scenery.TABLE_24308,
            Scenery.TABLE_24309,
            Scenery.TABLE_24320,
            Scenery.TABLE_24335,
            Scenery.TABLE_24336,
            Scenery.TABLE_24667,
            Scenery.TABLE_24684,
            Scenery.TABLE_24900,
            Scenery.TABLE_24922,
            Scenery.TABLE_24927,
            Scenery.TABLE_25047,
            Scenery.TABLE_25603,
            Scenery.TABLE_25615,
            Scenery.TABLE_25733,
            Scenery.TABLE_25734,
            Scenery.TABLE_25778,
            Scenery.TABLE_25928,
            Scenery.TABLE_25930,
            Scenery.TABLE_25931,
            Scenery.TABLE_26020,
            Scenery.TABLE_26074,
            Scenery.TABLE_26142,
            Scenery.TABLE_2650,
            Scenery.TABLE_26783,
            Scenery.TABLE_26807,
            Scenery.TABLE_27014,
            Scenery.TABLE_27052,
            Scenery.TABLE_27056,
            Scenery.TABLE_27083,
            Scenery.TABLE_27245,
            Scenery.TABLE_27850,
            Scenery.TABLE_28199,
            Scenery.TABLE_28262,
            Scenery.TABLE_28508,
            Scenery.TABLE_28621,
            Scenery.TABLE_28628,
            Scenery.TABLE_28629,
            Scenery.TABLE_28660,
            Scenery.TABLE_2906,
            Scenery.TABLE_29131,
            Scenery.TABLE_2940,
            Scenery.TABLE_29467,
            Scenery.TABLE_29468,
            Scenery.TABLE_30074,
            Scenery.TABLE_30075,
            Scenery.TABLE_30076,
            Scenery.TABLE_30077,
            Scenery.TABLE_30078,
            Scenery.TABLE_30079,
            Scenery.TABLE_30080,
            Scenery.TABLE_30081,
            Scenery.TABLE_30222,
            Scenery.TABLE_30734,
            Scenery.TABLE_30827,
            Scenery.TABLE_30828,
            Scenery.TABLE_30830,
            Scenery.TABLE_30939,
            Scenery.TABLE_32121,
            Scenery.TABLE_33052,
            Scenery.TABLE_33054,
            Scenery.TABLE_33129,
            Scenery.TABLE_33159,
            Scenery.TABLE_33201,
            Scenery.TABLE_33202,
            Scenery.TABLE_33233,
            Scenery.TABLE_34204,
            Scenery.TABLE_34429,
            Scenery.TABLE_34431,
            Scenery.TABLE_34469,
            Scenery.TABLE_34470,
            Scenery.TABLE_34473,
            Scenery.TABLE_34474,
            Scenery.TABLE_34518,
            Scenery.TABLE_34519,
            Scenery.TABLE_34520,
            Scenery.TABLE_34521,
            Scenery.TABLE_34799,
            Scenery.TABLE_34927,
            Scenery.TABLE_34968,
            Scenery.TABLE_36255,
            Scenery.TABLE_36350,
            Scenery.TABLE_36366,
            Scenery.TABLE_36573,
            Scenery.TABLE_36574,
            Scenery.TABLE_36575,
            Scenery.TABLE_36576,
            Scenery.TABLE_36577,
            Scenery.TABLE_36578,
            Scenery.TABLE_36579,
            Scenery.TABLE_36580,
            Scenery.TABLE_36581,
            Scenery.TABLE_36582,
            Scenery.TABLE_36583,
            Scenery.TABLE_36584,
            Scenery.TABLE_36585,
            Scenery.TABLE_36586,
            Scenery.TABLE_36727,
            Scenery.TABLE_36728,
            Scenery.TABLE_36730,
            Scenery.TABLE_36731,
            Scenery.TABLE_36820,
            Scenery.TABLE_36821,
            Scenery.TABLE_36910,
            Scenery.TABLE_37160,
            Scenery.TABLE_37161,
            Scenery.TABLE_37162,
            Scenery.TABLE_37163,
            Scenery.TABLE_37164,
            Scenery.TABLE_37165,
            Scenery.TABLE_37166,
            Scenery.TABLE_37167,
            Scenery.TABLE_37350,
            Scenery.TABLE_37419,
            Scenery.TABLE_37815,
            Scenery.TABLE_38020,
            Scenery.TABLE_38021,
            Scenery.TABLE_38102,
            Scenery.TABLE_38316,
            Scenery.TABLE_38317,
            Scenery.TABLE_38318,
            Scenery.TABLE_38319,
            Scenery.TABLE_38320,
            Scenery.TABLE_39167,
            Scenery.TABLE_39168,
            Scenery.TABLE_40030,
            Scenery.TABLE_40031,
            Scenery.TABLE_40818,
            Scenery.TABLE_40819,
            Scenery.TABLE_40820,
            Scenery.TABLE_41105,
            Scenery.TABLE_41106,
            Scenery.TABLE_41107,
            Scenery.TABLE_41196,
            Scenery.TABLE_41289,
            Scenery.TABLE_41462,
            Scenery.TABLE_41469,
            Scenery.TABLE_4272,
            Scenery.TABLE_4372,
            Scenery.TABLE_4458,
            Scenery.TABLE_4459,
            Scenery.TABLE_4460,
            Scenery.TABLE_4461,
            Scenery.TABLE_4462,
            Scenery.TABLE_4463,
            Scenery.TABLE_4464,
            Scenery.TABLE_4651,
            Scenery.TABLE_4652,
            Scenery.TABLE_4653,
            Scenery.TABLE_5349,
            Scenery.TABLE_5879,
            Scenery.TABLE_593,
            Scenery.TABLE_594,
            Scenery.TABLE_595,
            Scenery.TABLE_596,
            Scenery.TABLE_597,
            Scenery.TABLE_598,
            Scenery.TABLE_599,
            Scenery.TABLE_600,
            Scenery.TABLE_601,
            Scenery.TABLE_602,
            Scenery.TABLE_603,
            Scenery.TABLE_604,
            Scenery.TABLE_605,
            Scenery.TABLE_606,
            Scenery.TABLE_607,
            Scenery.TABLE_6075,
            Scenery.TABLE_613,
            Scenery.TABLE_614,
            Scenery.TABLE_616,
            Scenery.TABLE_6196,
            Scenery.TABLE_6197,
            Scenery.TABLE_6198,
            Scenery.TABLE_6199,
            Scenery.TABLE_620,
            Scenery.TABLE_6200,
            Scenery.TABLE_6201,
            Scenery.TABLE_621,
            Scenery.TABLE_6211,
            Scenery.TABLE_622,
            Scenery.TABLE_623,
            Scenery.TABLE_6231,
            Scenery.TABLE_624,
            Scenery.TABLE_6246,
            Scenery.TABLE_7331,
            Scenery.TABLE_8700,
            Scenery.TABLE_8701,
            Scenery.TABLE_8751,
            Scenery.TABLE_8768,
            Scenery.TABLE_8769,
            Scenery.TABLE_9082,
            Scenery.TABLE_9430,
            Scenery.TABLE_9431,
            Scenery.TABLE_9432,
            Scenery.TABLE_9433,
            Scenery.TABLE_9434,
            Scenery.TABLE_9614,
            Scenery.TABLE_9685,
            Scenery.TABLE_9702
        ) { player, used, with ->

            used as Item
            with as core.game.node.scenery.Scenery

            // Similar to DropListener.handleDropAction to keep it consistent
            if (Pets.forId(used.id) != null) {
                player.familiarManager.summon(used, true, true)
                return@onUseAnyWith true
            }
            if (player.locks.equipmentLock != null) {
                return@onUseAnyWith false
            }

            // determine if item can be dropped
            var canDrop = false;
            for (option in used.getDefinition().getOptions()) {
                if (option != null && (option.contains("drop"))) {
                    canDrop = true
                }
            }
            if (!canDrop) {
                sendMessage(player, "You cannot place this item on the table!")
                return@onUseAnyWith false
            }

            // Calculate nearest
            val minX = with.location.x
            val maxX = with.location.x + with.getSizeX() - 1
            val minY = with.location.y
            val maxY = with.location.y + with.getSizeY() - 1
            val nearestX = when {
                player.location.x < minX -> minX
                player.location.x > maxX -> maxX
                else -> player.location.x
            }
            val nearestY = when {
                player.location.y < minY -> minY
                player.location.y > maxY -> maxY
                else -> player.location.y
            }

            // Similar to DropListener.handleDropAction to keep it consistent
            closeAllInterfaces(player)
            player.animate(Animation(537))
            queueScript(player, strength = QueueStrength.SOFT) { //do this as a script to allow dropping multiple items in the same tick (authentic)
                // It's possible for state to change between queueing the script and executing it at the end of the tick (https://forum.2009scape.org/viewtopic.php?f=8&t=1195-lost-bandos-chestplate-whilst-making-iron-titans&p=5292). So, sanity check:
                val current = player.inventory.get(used.slot)
                if (current == null || current !== used) {
                    return@queueScript stopExecuting(player)
                }
                if (player.inventory.replace(null, used.slot) !== used) {
                    PlayerMonitor.log(player, LogType.DUPE_ALERT, "Potential exploit attempt when player ${player.name} tried to place on table ${used.amount}x ${used.id}. The item has been lost and will need to be refunded to the player, but how did they get this to happen?")
                    return@queueScript stopExecuting(player)
                }
                val droppedItem = used.dropItem
                if (droppedItem.id == Items.COINS_995) playAudio(player, DROP_COINS_SOUND) else playAudio(player, DROP_ITEM_SOUND)
                GroundItemManager.create(droppedItem, Location(nearestX, nearestY), player)

                setAttribute(player, "droppedItem:${droppedItem.id}", getWorldTicks() + 2)
                PlayerParser.save(player)
                return@queueScript stopExecuting(player)
            }

            return@onUseAnyWith true
        }
    }
}
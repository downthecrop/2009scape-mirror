package content.region.kandarin.handlers

import content.data.skill.SkillingTool
import content.global.skill.firemaking.Log
import content.region.fremennik.diary.FremennikAchievementDiary.Companion.HardTasks
import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.diary.DiaryLevel
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery as Objects

/**
 * Listener for barbarian pyre ship building.
 * @author Bishop
 */

class PyreSiteListener : InteractionListener {

    companion object {
        internal val REWARDS = arrayOf(
            // For odds, divide chanceRate by 256
            // Potions
            ChanceItem(Items.RANARR_POTIONUNF_100, 2, 2, 16.0),
            ChanceItem(Items.ANTIFIRE_MIX1_11507, 1, 1, 8.0),
            ChanceItem(Items.ANTIFIRE_MIX2_11505, 1, 1, 8.0),
            ChanceItem(Items.FISHING_MIX2_11477, 1, 1, 8.0),
            ChanceItem(Items.PRAYER_MIX1_11467, 1, 1, 8.0),
            ChanceItem(Items.PRAYER_MIX2_11465, 1, 1, 8.0),
            ChanceItem(Items.ANTI_P_SUPERMIX2_11473, 1, 1, 8.0),
            ChanceItem(Items.SUPERATTACK_MIX1_11471, 1, 1, 8.0),
            ChanceItem(Items.SUPERATTACK_MIX2_11469, 1, 1, 8.0),
            ChanceItem(Items.SUP_DEF_MIX1_11499, 1, 1, 8.0),
            ChanceItem(Items.SUP_DEF_MIX2_11497, 1, 1, 8.0),
            ChanceItem(Items.SUP_STR_MIX1_11487, 1, 1, 8.0),
            ChanceItem(Items.SUP_STR_MIX2_11485, 1, 1, 8.0),
            // Runes
            ChanceItem(Items.DEATH_RUNE_560, 8, 15, 36.0),
            ChanceItem(Items.BLOOD_RUNE_565, 4, 7, 20.0),
            // Ammo
            ChanceItem(Items.SILVER_BOLTS_9145, 5, 5, 12.0),
            ChanceItem(Items.ADAMANT_KNIFE_867, 20, 20, 12.0),
            ChanceItem(Items.ADAMANT_DARTP_816, 20, 20, 12.0),
            ChanceItem(Items.RUNE_BOLTS_9144, 10, 10, 12.0),
            ChanceItem(Items.RUNE_ARROW_892, 10, 10, 12.0),
            // Misc.
            ChanceItem(Items.DIAMOND_1602, 2, 2, 16.0),
            ChanceItem(Items.MITH_GRAPPLE_9419, 2, 2, 12.0)
        )

        private val USED_LOCATIONS = ArrayList<Location>(20)
    }

    override fun defineListeners() {
        on(Objects.PYRE_SITE_25286, IntType.SCENERY, "construct") { player, node ->
            for (l in USED_LOCATIONS) {
                if (l.withinDistance(node.location, 3)) {
                    sendDialogue(player, "This pyre site is in use currently.")
                    return@on true
                }
            }
            if (getAttribute(player, BarbarianTraining.attributePyreShip, 0) < 1) {
                sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
                return@on true
            }
            if (!inInventory(player, Items.CHEWED_BONES_11338) && !inInventory(player, Items.MANGLED_BONES_11337)) {
                sendDialogue(player, "You need chewed bones or mangled bones in order to do this.")
                return@on true
            }
            if (!inInventory(player, Items.TINDERBOX_590)) {
                sendDialogue(player, "You need a tinderbox in order to do this.")
                return@on true
            }
            val tool = SkillingTool.getHatchet(player) ?: run {
                sendDialogue(player, "You will need some sort of woodcutting axe to carve the pyreship.")
                return@on true
            }
            val type = LogType.getType(player) ?: run {
                sendDialogue(player, "You don't have any logs.")
                return@on true
            }
            val ferociousSpirit = getAttribute(player, BarbarianTraining.attributeFerociousSpirit, null as NPC?)
            if (ferociousSpirit != null && ferociousSpirit.isActive) {
                sendDialogue(player, "You must defeat the barbarian spirit first.")
                return@on true
            }
            ritual(player, node.asScenery(), type, tool)
            return@on true
        }

        setDest(IntType.SCENERY, intArrayOf(Objects.PYRE_SITE_25286)) { _, node ->
            node.location.transform(node.asScenery().direction, 1)
        }
    }

    private fun ritual(player: Player, obj: Scenery, logType: LogType, tool: SkillingTool) {
        val bones = if (inInventory(player, Items.CHEWED_BONES_11338)) {
            Item(Items.CHEWED_BONES_11338)
        }
        else {
            Item(Items.MANGLED_BONES_11337)
        }
        USED_LOCATIONS.add(obj.location)
        val spiritHolder = arrayOfNulls<PeacefulBarbarianNPC>(1)
        val shipHolder = arrayOfNulls<Scenery>(1)
        val southernPyreSite = player.location.withinDistance(Location(2503, 3498, 0), 4)
        queueScript(player, 0, QueueStrength.SOFT) { stage ->
            when (stage) {
                0 -> {
                    lock(player, 30)
                    face(player, obj)
                    getAnimation(tool)?.let { animate(player, it) }
                    shipHolder[0] = replace(Objects.CARVED_LOG_25288, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                1 -> {
                    getAnimation(tool)?.let { animate(player, it) }
                    shipHolder[0] = replace(Objects.CARVED_LOG_25289, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                2 -> {
                    getAnimation(tool)?.let { animate(player, it) }
                    shipHolder[0] = replace(Objects.PYRE_BOAT_25290, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                3 -> {
                    shipHolder[0] = replace(Objects.PYRE_BOAT_25291, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                4 -> {
                    if (removeItemsIfPlayerHasEnough(player, Item(logType.log.logId), bones)) {
                        resetAnimator(player)
                        rewardXP(player, Skills.CRAFTING, logType.craftingXp)
                        rewardXP(player, Skills.FIREMAKING, logType.firemakingXp)
                        if (bones.id == Items.CHEWED_BONES_11338) {
                            val spiritLocation = if (southernPyreSite) getLocation(obj).transform(2, 0, 0) else getLocation(obj)
                            val peaceSpirit = PeacefulBarbarianNPC(NPCs.PEACEFUL_BARBARIAN_SPIRIT_754, spiritLocation)
                            peaceSpirit.init()
                            peaceSpirit.face(player)
                            animate(peaceSpirit, Animation.create(6724))
                            spiritHolder[0] = peaceSpirit

                            sendMessage(player, "The ancient barbarian is laid to rest. Your future prayer training is blessed,")
                            sendMessage(player, "as his spirit ascends to a glorious afterlife. Spirits drop an object into your")
                            sendMessage(player, "pack.")

                            val reward = getRandomItem(player)
                            addItemOrDrop(player, reward.id, reward.amount)

                            if (getAttribute(player, BarbarianTraining.attributePyreShip, 0) == 1) {
                                setAttribute(player, BarbarianTraining.attributePyreShip, 2)
                            }
                            val existing = getAttribute(player, BarbarianTraining.attributePyrePrayerBonus, 0)
                            setAttribute(player, BarbarianTraining.attributePyrePrayerBonus, existing + logType.bonusCharges)

                        } else {
                            val ferociousSpirit = FerociousBarbarianNPC(NPCs.FEROCIOUS_BARBARIAN_SPIRIT_752, obj.location.transform(obj.direction, 1))
                            ferociousSpirit.target = player
                            ferociousSpirit.init()
                            ferociousSpirit.moveStep()
                            setAttribute(player, BarbarianTraining.attributeFerociousSpirit, ferociousSpirit)
                            unlock(player)
                        }
                    }
                    if (logType == LogType.ARCTIC_PINE) {
                        finishTask(player, DiaryType.FREMENNIK, DiaryLevel.HARD, HardTasks.MAKE_BARBARIAN_PYRE_SHIP_ARCTIC_PINE)
                    }
                    shipHolder[0] = replace(Objects.PYRE_BOAT_25292, obj, shipHolder[0])
                    return@queueScript delayScript(player, Animation(6724).duration)
                }
                5 -> {
                    spiritHolder[0]?.clear()
                    shipHolder[0] = replace(Objects.PYRE_BOAT_25293, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                6 -> {
                    shipHolder[0] = replace(Objects.PYRE_BOAT_25294, obj, shipHolder[0])
                    return@queueScript delayScript(player, 4)
                }
                else -> {
                    unlock(player)
                    shipHolder[0]?.let { SceneryBuilder.remove(it) }
                    // Preserves the pyre site scenery
                    if (getLocation(obj) == obj.location && getScenery(obj.location)?.id != Objects.PYRE_SITE_25286) {
                        SceneryBuilder.add(Scenery(Objects.PYRE_SITE_25286, obj.location, obj.type, obj.rotation))
                    }
                    USED_LOCATIONS.remove(obj.location)
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }

    private fun replace(newId: Int, ship: Scenery, previousShip: Scenery?): Scenery {
        val newShip = Scenery(newId, getLocation(ship), 10, if (ship.location.x == 2503) 4 else 1)
        if (previousShip == null) {
            SceneryBuilder.add(newShip)
        } else {
            SceneryBuilder.replace(previousShip, newShip)
        }
        return newShip
    }

    private fun getLocation(ship: Scenery): Location {
        val location = ship.location.transform(ship.direction, -2)
        return when {
            ship.location.x == 2507 || ship.location.x == 2519 -> location.transform(-1, 0, 0)
            ship.location.x == 2503 && ship.location.y == 3498 -> location.transform(-2, 0, 0)
            ship.location.x == 2503 -> location.transform(-2, -1, 0)
            else -> location
        }
    }

    private fun getRandomItem(player: Player): Item {
        if (RandomFunction.roll(256)) {
            sendNews(player.username + " has just received a Dragon Full Helm from a pyre ship.")
            return Item(Items.DRAGON_FULL_HELM_11335)
        }
        return RandomFunction.getChanceItem(REWARDS)
    }

    private fun getAnimation(tool: SkillingTool): Animation? {
        return when (tool) {
            SkillingTool.BRONZE_AXE -> Animation.create(3291)
            SkillingTool.IRON_AXE -> Animation.create(3290)
            SkillingTool.STEEL_AXE -> Animation.create(3289)
            SkillingTool.BLACK_AXE -> Animation.create(3288)
            SkillingTool.MITHRIL_AXE -> Animation.create(3287)
            SkillingTool.ADAMANT_AXE -> Animation.create(3286)
            SkillingTool.RUNE_AXE -> Animation.create(3285)
            SkillingTool.DRAGON_AXE -> Animation.create(3292)
            else -> null
        }
    }

    enum class LogType(val log: Log, val level: Int, val craftingXp: Double, val firemakingXp: Double, val bonusCharges: Int) {
        NORMAL(Log.NORMAL, 11, 10.0, 40.0, 1),
        ACHEY(Log.ACHEY, 11, 10.0, 40.0, 1),
        OAK(Log.OAK, 25, 15.0, 60.0, 2),
        WILLOW(Log.WILLOW, 40, 22.5, 90.0, 2),
        TEAK(Log.TEAK, 45, 26.2, 105.0, 3),
        ARCTIC_PINE(Log.ARCTIC_PINE, 52, 31.2, 125.0, 3),
        MAPLE(Log.MAPLE, 55, 33.7, 135.0, 3),
        MAHOGANY(Log.MAHOGANY, 60, 39.3, 157.5, 4),
        EUCALYPTUS(Log.EUCALYPTUS, 68, 48.4, 193.5, 4),
        YEW(Log.YEW, 70, 50.6, 202.5, 4),
        MAGIC(Log.MAGIC, 85, 75.9, 303.8, 5);

        companion object {
            fun getType(player: Player): LogType? {
                return values().firstOrNull { inInventory(player, it.log.logId) }
            }
        }
    }
}

internal class FerociousBarbarianNPC : AbstractNPC {

    var target: Player? = null

    constructor(id: Int, location: Location?) : super(id, location) {
        isRespawn = false
        isAggressive = true
    }

    constructor() : this(-1, null)

    override fun handleTickActions() {
        if (target == null) return
        if (!target!!.isActive || !target!!.location.withinDistance(location)) {
            clear()
        }
        if (!properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(target)
        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        if (entity is Player && entity != target) {
            if (message) {
                entity.packetDispatch.sendMessage("It's not after you.")
            }
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player && killer == target) {
            removeAttribute(killer, BarbarianTraining.attributeFerociousSpirit)
        }
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return FerociousBarbarianNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FEROCIOUS_BARBARIAN_SPIRIT_752)
    }
}

internal class PeacefulBarbarianNPC : AbstractNPC {

    constructor(id: Int, location: Location?) : super(id, location) {
        isRespawn = false
    }

    constructor() : this(-1, null)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return PeacefulBarbarianNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PEACEFUL_BARBARIAN_SPIRIT_754)
    }
}

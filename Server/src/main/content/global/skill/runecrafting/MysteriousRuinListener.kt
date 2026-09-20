package content.global.skill.runecrafting

import content.data.Quests
import content.global.skill.runecrafting.Altar.ASTRAL
import content.global.skill.runecrafting.Altar.BLOOD
import content.global.skill.runecrafting.Altar.COSMIC
import content.global.skill.runecrafting.Altar.DEATH
import content.global.skill.runecrafting.Altar.LAW
import content.region.misthalin.varrock.diary.VarrockAchivementDiary.Companion.EasyTasks.ENTER_EARTH_ALTAR
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Items

class MysteriousRuinListener : InteractionListener {

    companion object {
        const val ALTAR_VARP = 491
        const val AIR_VARBIT = 607
        const val MIND_VARBIT = 608
        const val WATER_VARBIT = 609
        const val EARTH_VARBIT = 610
        const val FIRE_VARBIT = 611
        const val BODY_VARBIT = 612
        const val COSMIC_VARBIT = 613
        const val LAW_VARBIT = 614
        const val NATURE_VARBIT = 615
        const val CHAOS_VARBIT = 616
        const val DEATH_VARBIT = 617
        //const val SOUL_VARBIT = 618
        const val BLOOD_VARBIT = 619

        // checks requirements for accessing an altar
        fun checkReq(player: Player, altar: Altar) : Boolean {
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                sendMessage(player, "You need to finish the Rune Mysteries Quest in order to do this.")
                return false
            }

            when(altar) {
                ASTRAL -> if (!hasRequirement(player, Quests.LUNAR_DIPLOMACY)) return false
                DEATH -> if (!hasRequirement(player, Quests.MOURNINGS_END_PART_II)) return false
                BLOOD -> if (!hasRequirement(player, Quests.LEGACY_OF_SEERGAZE)) return false
                LAW -> if (!ItemDefinition.canEnterEntrana(player)) {
                    sendMessage(player, "The power of Saradomin prevents you from taking armour or weaponry to Entrana.")
                    return false
                }
                COSMIC -> if (!isQuestComplete(player, Quests.LOST_CITY)) {
                    sendMessage(player, "You need to have completed the Lost City quest in order to do that.")
                    return false
                }
                else -> return true
            }
            return true
        }
    }

    private val animation = Animation(Animations.HUMAN_BURYING_BONES_827)
    private val allowedUsed = Talisman.values().map { it.item.id }.toIntArray()
    private val allowedWith = MysteriousRuins.values().flatMap { ruins -> ruins.objectIds.asList() }.toIntArray()
    private val tiaras = Tiara.values().map { it.item.id }.toIntArray()
    private val staffs = Staff.values().map { it.item.id }.toIntArray()
    private val rcItems = tiaras + staffs
    private val nothingInteresting = "Nothing interesting happens."
    private val omniEquip = intArrayOf(Items.OMNI_TIARA_13655, Items.OMNI_TALISMAN_STAFF_13642)

    // builds the ids out of values mapped elsewhere instead of copying and pasting a million times (Vexia...)
    private val equipmentIds by lazy {
        val tiaras = Tiara.values().map { it.item.id }.toIntArray()
        val staffs = Staff.values().map { it.item.id }.toIntArray()
        tiaras + staffs
    }

    override fun defineListeners() {

        // using talisman with ruins
        onUseWith(IntType.SCENERY, allowedUsed, *allowedWith) { player, used, with ->
            return@onUseWith handleTalisman(player, used, with)
        }

        // entering ruins (tiara or staff equipped)
        on(allowedWith, IntType.SCENERY, "enter") { player, node ->
            return@on handleTiaraOrStaff(player, node)
        }

        // recalculates the varp state when equipping a talisman item
        // note that the listener triggers before the item is equipped
        onEquip(rcItems) { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                updateVarbits(player)
                return@queueScript stopExecuting(player)
            }
            return@onEquip true
        }

        // recalculates the varp state when unequipping a talisman item
        // note that the listener triggers before the item is unequipped
        onUnequip(rcItems) { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                updateVarbits(player)
                return@queueScript stopExecuting(player)
            }
            return@onUnequip true
        }
    }

    private fun updateVarbits(player: Player) {
        // what is currently equipped
        val tiara = getItemFromEquipment(player, EquipmentSlot.HEAD)
        val staff = getItemFromEquipment(player, EquipmentSlot.WEAPON)

        // list of all the equipped rc items (both a staff and a tiara)
        val activeItems = mutableListOf<Item>()
        if (tiara != null) activeItems.add(tiara.asItem())
        if (staff != null) activeItems.add(staff.asItem())

        // set varp to 0
        setVarp(player, ALTAR_VARP, 0, true)

        // filter the activeitems down to just rc items
        val rcItems = activeItems.filter { equipmentIds.contains(it.id) }

        // recalculate the varp
        for (item in rcItems) {
            // omni staff/tiara unlock all ruins, so varp is maxed
            if (item.id in omniEquip) {
                setVarp(player, ALTAR_VARP, 6143, true)
                break
            } else {
                // else flip the specific bit for this ruin type
                getRuinForItem(item)?.let { ruin ->
                    val varbitId = ruin.objectIds[0]
                    setVarbit(player, varbitId, 1, true)
                }
            }
        }
    }

    private fun getRuinForItem(item: Item): MysteriousRuins? {
        return Tiara.forItem(item)?.talisman?.ruin ?: Staff.forItem(item)?.talisman?.ruin
    }

    private fun handleTalisman(player: Player, used: Node, with: Node): Boolean {
        val ruin = MysteriousRuins.forObject(with.asScenery()) ?: return false
        val altar = ruin.mapped<Altar>() ?: return false

        if (!checkReq(player, altar)) {
            return true
        }

        val talisman = Talisman.forItem(used.asItem())
        if (talisman != Talisman.OMNI && talisman != ruin.talisman && talisman != Talisman.ELEMENTAL) {
            sendMessage(player, nothingInteresting)
            return false
        }
        if (talisman == Talisman.ELEMENTAL && (ruin.talisman != Talisman.AIR && ruin.talisman != Talisman.WATER && ruin.talisman != Talisman.FIRE && ruin.talisman != Talisman.EARTH)) {
            sendMessage(player, nothingInteresting)
            return false
        }

        teleportToRuinTalisman(player, used.asItem(), ruin)
        return true
    }

    private fun handleTiaraOrStaff(player: Player, node: Node): Boolean {
        val ruin = MysteriousRuins.forObject(node.asScenery()) ?: return false
        val altar = ruin.mapped<Altar>() ?: return false

        if (!checkReq(player, altar)) {
            return true
        }

        val tiara = Tiara.forItem(getItemFromEquipment(player, EquipmentSlot.HEAD))
        val staff = Staff.forItem(getItemFromEquipment(player, EquipmentSlot.WEAPON))

        val hasValidTiara = tiara != null && (tiara == ruin.tiara || tiara == Tiara.OMNI)
        val hasValidStaff = staff != null && (staff == ruin.staff || staff == Staff.OMNI)

        if (!hasValidTiara && !hasValidStaff) {
            sendMessage(player, nothingInteresting)
            return false
        }

        submitTeleportScript(player, ruin, 0)
        return true
    }

    private fun teleportToRuinTalisman(player: Player, talisman: Item, ruin: MysteriousRuins) {
        lock(player, 4)
        animate(player, animation)
        sendMessage(player, "You hold the ${talisman.name} towards the mysterious ruins.")
        submitTeleportScript(player, ruin, 3)
    }

    private fun submitTeleportScript(player: Player, ruin: MysteriousRuins, delay: Int) {
        sendMessage(player, "You feel a powerful force take hold of you.")
        queueScript(player, delay) { _ ->
            teleport(player, ruin.end)
            if (ruin == MysteriousRuins.EARTH) {
                player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 0, ENTER_EARTH_ALTAR)
            }
            return@queueScript true
        }
    }
}
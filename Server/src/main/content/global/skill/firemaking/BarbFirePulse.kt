package content.global.skill.firemaking

import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.event.LitFireEvent
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import content.global.skill.firemaking.FireMakingPulse.getAsh
import org.rs09.consts.Items
import kotlin.math.ceil

/**
 * Pulse used for barbarian firemaking.
 * @author Bishop
 */

class BarbFirePulse(player: Player, node: Item?, groundItem: GroundItem?, private val usedBow: Item? = null) : SkillPulse<Item>(player, node) {

    companion object {
        private const val attributeRemoveLog = "remove-log"
        private const val attributeLastFiremake = "last-firemake"
    }

    private val bowsAndAnimations = mapOf(
        Items.SHORTBOW_841        to 6714,
        Items.LONGBOW_839         to 6714,
        Items.OAK_SHORTBOW_843    to 6715,
        Items.OAK_LONGBOW_845     to 6715,
        Items.WILLOW_SHORTBOW_849 to 6716,
        Items.WILLOW_LONGBOW_847  to 6716,
        Items.MAPLE_SHORTBOW_853  to 6717,
        Items.MAPLE_LONGBOW_851   to 6717,
        Items.YEW_SHORTBOW_857    to 6718,
        Items.YEW_LONGBOW_855     to 6718,
        Items.MAGIC_SHORTBOW_861  to 6719,
        Items.MAGIC_LONGBOW_859   to 6719
    )

    private val fire: Log? = Log.forId(node!!.id)
    private var ticks = 0
    private var groundItem: GroundItem = groundItem ?: run {
        setAttribute(player, attributeRemoveLog, true)
        GroundItem(node!!, player.location, player)
    }
    init {
        if (groundItem != null) {
            removeAttribute(player, attributeRemoveLog)
        }
    }

    override fun checkRequirements(): Boolean {
        // Check for lightable
        if (fire == null || fire.logId == Items.CURSED_MAGIC_LOGS_13567 || fire.logId == Items.JOGRE_BONES_3125) {
            return false
        }

        // Check for fire-disallowed location
        if (RegionManager.getObject(player.location) != null || player.zoneMonitor.isInZone("bank")) {
            sendMessage(player, "You can't light a fire here.")
            return false
        }

        // Check for bow (if player was able to use a tinderbox this pulse shouldn't have fired)
        if (!anyInInventory(player, *bowsAndAnimations.keys.toIntArray())) {
            sendMessage(player, "You do not have the required items to light this.")
            return false
        }

        // Check training
        if (getAttribute(player, BarbarianTraining.attributeBowFire, 0) == 0) {
            sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
            return false
        }

        // Check barbarian firemaking level requirement
        if (getDynLevel(player, Skills.FIREMAKING) < fire.barbLevel) {
            sendMessage(player, "You need a firemaking level of ${fire.barbLevel} to light this log using barbarian firemaking.")
            return false
        }

        // Handle log removal from inventory
        if (getAttribute(player, attributeRemoveLog, false)) {
            removeAttribute(player, attributeRemoveLog)
            if (inInventory(player, node!!.id, 1)) {
                replaceSlot(player, node!!.slot, Item(node!!.id, node!!.amount - 1), node!!, Container.INVENTORY)
                groundItem = produceGroundItem(player, groundItem.id, groundItem.amount, groundItem.location)
            }
        }

        return true
    }

    override fun animate() {
        // Animations are handled independently
    }

    fun getBowAnimation(): Int {
        // If player used a specific bow, selects the corresponding animation
        if (usedBow != null) {
            bowsAndAnimations[usedBow.id]?.let { return it }
        }
        // If player didn't use a specific bow, animates according to bows the player has in inventory
        for ((bowId, animation) in bowsAndAnimations) {
            if (inInventory(player, bowId)) {
                return animation
            }
        }
        return bowsAndAnimations.values.first()
    }

    override fun reward(): Boolean {
        // Check if we need to wait before creating another fire
        if (getLastFire() >= getWorldTicks()) {
            createFire()
            return true
        }

        // Play animation every 12 ticks
        if (ticks % 12 == 0) {
            animate(player, getBowAnimation())
        }

        // Only check for success every 3 ticks
        if (++ticks % 3 != 0) {
            return false
        }

        // Check if firemaking succeeds
        if (!success()) {
            return false
        }

        // On success
        createFire()

        // Progress Barbarian training if this is your first oak log lit with a bow
        if (getAttribute(player, BarbarianTraining.attributeBowFire, 0) == 1 && fire!!.logId == Items.OAK_LOGS_1521) {
            setAttribute(player, BarbarianTraining.attributeBowFire, 2)
            sendDialogue(player, "You feel you have learned more of barbarian ways. Otto might wish to talk to you more.")
        }
        return true
    }


    private fun createFire() {
        if (!groundItem.isActive) {
            return
        }

        val fireObject = Scenery(fire!!.fireId, player.location)
        SceneryBuilder.add(fireObject, fire.life, getAsh(player, fire, fireObject))
        removeGroundItem(groundItem)
        player.moveStep()
        face(player, fireObject)
        rewardXP(player, Skills.FIREMAKING, fire.xp)

        setLastFire()
        player.dispatch(LitFireEvent(fire.logId))
    }


    override fun message(type: Int) {
        val name = if (node!!.id == Items.JOGRE_BONES_3125) "bones" else "logs"
        when (type) {
            0 -> sendMessage(player, "You attempt to light the $name..")
            1 -> sendMessage(player, "The fire catches and the $name begin to burn.")
        }
    }


    private fun getLastFire(): Int { // Time in ticks
        return getAttribute(player, attributeLastFiremake, 0)
    }


    private fun setLastFire() { // Time in ticks
        setAttribute(player, attributeLastFiremake, getWorldTicks() + 2)
    }

    /**
     * Checks if the player gets rewarded.
     * @return {@code True} if so.
     */
    private fun success(): Boolean {
        val level = 1 + getDynLevel(player, Skills.FIREMAKING)
        val req = fire!!.barbLevel.toDouble()
        val successChance = ceil((level * 50 - req * 15) / req / 3 * 4)
        val roll = RandomFunction.random(99)
        return successChance >= roll

    }
}

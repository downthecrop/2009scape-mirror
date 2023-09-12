package content.global.handlers.item

import content.global.handlers.item.equipment.special.DragonfireSwingHandler
import core.ServerConstants
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.global.action.DropListener
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatPulse.Companion.swing
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.item.ItemPlugin
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.minutesToTicks
import core.tools.secondsToTicks
import org.rs09.consts.Items
import org.rs09.consts.Sounds

/**
 * Handles the dragonfire shield options.
 */

class DragonfireShieldListener: InteractionListener {

        val dragonfireShields = intArrayOf(Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11284)
        val dfsEmptyAnim = 6700
        val dfsEmptyGfx = 1160
        val dfsRecharge = if (ServerConstants.BETTER_DFS) secondsToTicks(30) else minutesToTicks(2)

    override fun defineListeners() {

        on(dragonfireShields, IntType.ITEM, "operate") { player, node ->
            val usingAttack = !getAttribute(player, "dfs_spec", false)
            if (!usingAttack) {
                if (!player.settings.isSpecialToggled) {
                    setVarp(player, 301, 0)
                }
                player.removeAttribute("dfs_spec")
                player.properties.combatPulse.temporaryHandler = null
                return@on true
            }
            val notCharged = node.asItem().id == Items.DRAGONFIRE_SHIELD_11284 || node.asItem().charge < 20
            if (notCharged) {
                sendMessage(player, "Your shield has no charges left.")
                return@on true
            }
            if (player.locks.isLocked("dfs_recharge")) {
                sendMessage(player, "Your dragonfire shield is recharging.")
                return@on true
            }
            setVarp(player, 301, 1)
            player.setAttribute("dfs_spec", true)
            val attack = SwitchAttack(null, Animation.create(6696), Graphics.create(1165), Graphics(1167, 96), Projectile.create(player, null, 1166, 36, 36, 80, 70, 0, 11))
            val handler: DragonfireSwingHandler = object : DragonfireSwingHandler(false, 25, attack, true) {
                override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                    if (entity is Player) {
                        if (!player.settings.isSpecialToggled) {
                            setVarp(player, 301, 0)
                        }
                        removeAttribute(player, "dfs_spec")
                        val shield = player.equipment[EquipmentContainer.SLOT_SHIELD]
                        if (shield == null || shield.id != Items.DRAGONFIRE_SHIELD_11283) {
                            return -1
                        }
                        playGlobalAudio(entity.getLocation(), Sounds.DRAGONSLAYER_SHIELDFIRE_3761)
                        delayEntity(player, 3)
                        shield.charge -= 20
                        if (shield.charge < 20 && node.asItem().slot == EquipmentContainer.SLOT_SHIELD) {
                            replaceSlot(player, node.asItem().slot, Item(Items.DRAGONFIRE_SHIELD_11284), Item(Items.DRAGONFIRE_SHIELD_11283), Container.EQUIPMENT)
                        }
                        EquipmentContainer.updateBonuses(player)
                        player.locks.lock("dfs_recharge", dfsRecharge)
                    }
                    return super.swing(entity, victim, state)
                }

                override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
                    playGlobalAudio(victim!!.location, Sounds.FIRESTRIKE_HIT_161, 20)
                    super.visualizeImpact(entity, victim, state)
                }
            }
            attack.handler = handler
            val victim = player.properties.combatPulse.getVictim()
            if (player.properties.combatPulse.isAttacking && handler.canSwing(player, victim!!) == InteractionType.STILL_INTERACT) {
                swing(player, victim, handler)
                return@on true
            }
            player.properties.combatPulse.temporaryHandler = handler
            return@on true
        }

        on(dragonfireShields, IntType.ITEM, "empty") { player, node ->
            replaceSlot(player, node.asItem().slot, Item(Items.DRAGONFIRE_SHIELD_11284), Item(Items.DRAGONFIRE_SHIELD_11283))
            visualize(player, dfsEmptyAnim, dfsEmptyGfx)
            sendMessage(player, "You release the charges.")
            playGlobalAudio(player.location, Sounds.DRAGONSLAYER_SHIELD_EMPTY_3760)
            return@on true
        }

        on(dragonfireShields, IntType.ITEM, "inspect") { player, node ->
            if (node.asItem().id == Items.DRAGONFIRE_SHIELD_11284) {
                sendMessage(player, "The shield has no charges.")
                return@on true
            }
            sendMessage(player, "The shield has " + node.asItem().charge / 20 + " charges.")
            return@on true
        }

        on(dragonfireShields, IntType.ITEM, "drop") { player, node ->
            var shield = node.asItem()
            val slot = shield.slot
            if (shield.id == Items.DRAGONFIRE_SHIELD_11283) {
                replaceSlot(player, shield.slot, Item(Items.DRAGONFIRE_SHIELD_11284), Item(Items.DRAGONFIRE_SHIELD_11283))
                shield = player.inventory.get(slot)
            }
            DropListener.drop(player, shield)
            return@on true
        }
    }
}

/**
 * Drops the uncharged dragonfire shield item id on death.
 */

@Initializable
class DFSItemPlugin : ItemPlugin() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(Items.DRAGONFIRE_SHIELD_11283)
        return this
    }

    override fun getDeathItem(item: Item?): Item {
        return Item(Items.DRAGONFIRE_SHIELD_11284)
    }
}
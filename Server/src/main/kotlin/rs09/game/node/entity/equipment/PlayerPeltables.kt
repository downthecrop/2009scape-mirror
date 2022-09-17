package rs09.game.node.entity.equipment

import api.*
import core.game.interaction.Interaction
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType


class PlayerPeltables : InteractionListener {
    companion object {
        private const val PROJECTILE_DELAY = 41
        private const val PROJECTILE_SPEED = 60
        private const val PROJECTILE_TIME_CONST = .02857
        private const val PROJECTILE_DISTANCE_MULT = 5
        private val PELTABLES = intArrayOf(Items.ROTTEN_TOMATO_2518, Items.SNOWBALL_11951)
    }


    override fun defineListeners() {
        onEquip(PELTABLES, ::setPlayerOps)
        onUnequip(PELTABLES, ::removePlayerOps)

        on("pelt", IntType.PLAYER, ::handlePeltInteraction)
        onUseWithPlayer(*PELTABLES) {player, used, with -> handlePeltInteraction(player, with, used) }
        flagInstant()
    }

    private fun setPlayerOps(player: Player, _node: Node) : Boolean {
        player.interaction.set(Option("Pelt", 0))
        return true
    }

    private fun removePlayerOps(player: Player, _node: Node) : Boolean {
        Interaction.sendOption(player, 0, "null")
        return true
    }

    private fun handlePeltInteraction(player: Player, node: Node, usedPeltable: Node? = null) : Boolean {
        val peltable = usedPeltable?.asItem() ?: getPeltable(player) ?: return removePlayerOps(player, node)
        val gfx = getPeltableGfx(peltable.id)

        val other = node.asPlayer()

        if (!Pathfinder.find(player, other, false, Pathfinder.PROJECTILE).isSuccessful) {
            sendDialogue(player, "You can't reach them!")
            return true
        }

        val distance = player.location.getDistance(other.location)
        val projectileSpeed = PROJECTILE_DELAY + PROJECTILE_SPEED + distance * PROJECTILE_DISTANCE_MULT
        val hitDelay = (projectileSpeed * PROJECTILE_TIME_CONST).toInt()

        if (removeItem(player, peltable, Container.INVENTORY) || removeItem(player, peltable, Container.EQUIPMENT)) {
            lock(player, hitDelay)
            submitWorldPulse(PeltingPulse(player, other, gfx, hitDelay, peltable.id))
        }

        return true
    }

    class PeltingPulse(val player: Player, val other: Player, val gfx: IntArray, val hitDelay: Int, val peltable: Int) : Pulse() {
        private val throwAnimation = getPeltableAnim(peltable)
        private var ticks = 0
        override fun pulse(): Boolean {
            when (ticks++) {
                0 -> {
                    player.face(other)
                    visualize(player, throwAnimation, gfx[0])
                }
                1 -> {
                    Projectile.create(player, other, gfx[1], 30, 10).send()
                    face(player, player) //reset face flag lel
                    unlock(player)
                }
                hitDelay -> {
                    if (gfx[2] != -1) other.graphics(Graphics(gfx[2]))
                    sendMessage(other, "${player.username} has hit you with a ${getItemName(peltable).toLowerCase()}.")
                    return true
                }
            }
            return false
        }

        private fun getPeltableAnim(id: Int) : Int {
            return when (id) {
                Items.SNOWBALL_11951 -> 7530
                Items.ROTTEN_TOMATO_2518 -> 385
                else -> -1
            }
        }
    }

    private fun getPeltableGfx(id: Int): IntArray {
        return when (id) {
            Items.SNOWBALL_11951 -> intArrayOf(-1, 861, 1282)
            Items.ROTTEN_TOMATO_2518 -> intArrayOf(-1, 29, 31)
            else -> IntArray(3) {-1}
        }
    }

    private fun getPeltable(player: Player): Item? {
        val equipped = getItemFromEquipment(player, EquipmentSlot.WEAPON) ?: return null
        val id = equipped.id

        if (id !in PELTABLES) return null

        return equipped
    }
}
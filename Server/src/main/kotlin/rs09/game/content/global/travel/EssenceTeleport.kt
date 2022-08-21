package rs09.game.content.global.travel

import api.events.TeleportEvent
import api.lock
import api.teleport
import api.unlock
import core.game.node.Node
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import rs09.game.node.entity.skill.magic.TeleportMethod
import rs09.game.world.GameWorld

/**
 * Represents a utilitity class for rune essence teleporting.
 * @author 'Vexia
 */
object EssenceTeleport {
    /**
     * Array of all the possible `Location` locations.
     */
    val LOCATIONS = arrayOf(
        Location.create(2911, 4832, 0),
        Location.create(2913, 4837, 0),
        Location.create(2930, 4850, 0),
        Location.create(2894, 4811, 0),
        Location.create(2896, 4845, 0),
        Location.create(2922, 4820, 0),
        Location.create(2931, 4813, 0)
    )

    /**
     * Represents constants used for gfx/animation
     */
    private const val CURSE_PROJECTILE = 109
    private val ANIMATION = Animation(437)
    private val GLOWING_HANDS_GFX = Graphics(108)
    private val TELEPORT_GFX = Graphics(110,150)


    /**
     * Method used to teleport a player.
     * @param npc the npc.
     * @param player the player.
     */
    @JvmStatic
    fun teleport(npc: NPC, player: Player) {
        npc.animate(ANIMATION)
        npc.faceTemporary(player, 1)
        npc.graphics(GLOWING_HANDS_GFX)
        lock(player,4)
        player.audioManager.send(125)
        Projectile.create(npc, player, CURSE_PROJECTILE).send()
        npc.sendChat("Senventior Disthinte Molesko!")
        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> player.graphics(TELEPORT_GFX)
                    1 -> {
                        if (getStage(player) == 2 && player.inventory.contains(5519, 1)) {
                            val item = player.inventory[player.inventory.getSlot(Item(5519))]
                            if (item != null) {
                                if (item.charge == 1000) {
                                    player.savedData.globalData.resetAbyss()
                                }
                                val wizard = Wizard.forNPC(npc.id)
                                if (!player.savedData.globalData.hasAbyssCharge(wizard.ordinal)) {
                                    player.savedData.globalData.setAbyssCharge(wizard.ordinal)
                                    item.charge = item.charge + 1
                                    if (item.charge == 1003) {
                                        player.sendMessage("Your scrying orb has absorbed enough teleport information.")
                                        player.inventory.remove(Item(5519))
                                        player.inventory.add(Item(5518))
                                    }
                                }
                            }
                        }
                        player.savedData.globalData.essenceTeleporter = npc.id
                        player.graphics(TELEPORT_GFX)

                        val loc = LOCATIONS[RandomFunction.random(0, LOCATIONS.size)]
                        teleport(player,loc)

                        player.dispatch(
                            TeleportEvent(
                                TeleportManager.TeleportType.TELE_OTHER,
                                TeleportMethod.NPC,
                                npc,
                                loc
                            )
                        )
                    }

                    2 -> {
                        unlock(player)
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Method used to teleport back to the home.
     * @param player the prayer.
     */
    @JvmStatic
    fun home(player: Player, node: Node) {
        val wizard = Wizard.forNPC(player.savedData.globalData.essenceTeleporter)
        Projectile.create(node.location, player.location, CURSE_PROJECTILE, 15, 10, 0, 10, 0, 2).send()
        GameWorld.Pulser.submit(object : Pulse(1) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    0 -> {
                        lock(player,2)
                        player.graphics(TELEPORT_GFX)
                    }
                    1 -> {
                        teleport(player,wizard.location)
                        player.graphics(TELEPORT_GFX)
                        unlock(player)
                        return true
                    }
                }
                return false
            }
        })
    }

    /**
     * Gets the stage.
     * @return the stage.
     */
    fun getStage(player: Player): Int {
        return player.varpManager.get(492).getValue()
    }

    /**
     * Method used to get a random location.
     * @return the location.
     */
    val location: Location
        get() {
            val count = RandomFunction.random(LOCATIONS.size)
            return LOCATIONS[count]
        }

    /**
     * Represents the wizard npc who can teleport.
     * @author 'Vexia
     */
    enum class Wizard
    /**
     * Constructs a new `WizardTowerPlugin` `Object`.
     * @param npc the npc.
     * @param location the location.
     */(
            /**
             * Represents the npc of this wizard.
             */
            val npc: Int,
            /**
             * The mask.
             */
            val mask: Int,
            /**
             * Represents the returining location.
             */
            val location: Location) {
        AUBURY(553, 0x2, Location(3253, 3401, 0)),
        SEDRIDOR(300, 0x4, Location(3107, 9573, 0)),
        DISTENTOR(462, 0x8, Location(2591, 3085, 0)),
        CROMPERTY(2328, 0x12, Location.create(2682, 3323, 0));

        /**
         * Gets the npc.
         * @return The npc.
         */

        /**
         * Gets the mask.
         * @return The mask.
         */

        /**
         * Gets the location.
         * @return The location.
         */

        companion object {
            /**
             * Method used to get a wizard by the npc.
             * @param npc the npc.
             * @return the wizard.
             */
            fun forNPC(npc: Int): Wizard {
                for (wizard in values()) {
                    if (npc == 844) {
                        return CROMPERTY
                    }
                    if (wizard.npc == npc) {
                        return wizard
                    }
                }
                return AUBURY
            }
        }

    }
}

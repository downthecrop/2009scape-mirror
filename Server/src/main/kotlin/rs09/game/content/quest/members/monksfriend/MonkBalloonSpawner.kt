package rs09.game.content.quest.members.monksfriend

import core.cache.def.impl.SceneryDefinition
import core.game.content.activity.partyroom.PartyRoomPlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.isTeleportPermitted
import core.plugin.Plugin
import core.tools.RandomFunction
import rs09.game.world.GameWorld.Pulser

/**
 * Manages the dropped party balloons.
 * @author Kya
 */
class MonkBalloonSpawner : OptionHandler() {
/**
 * Constructs a new {@Code BalloonManager} {@Code Object}
 */


    @Throws(Throwable::class)
    override fun newInstance(arg: Any): Plugin<Any> {
        for (balloon in PartyBalloon.values()) {
            SceneryDefinition.forId(balloon.balloonId).handlers["option:burst"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "burst" -> {
                PartyBalloon.forId(node.id)!!.burst(player, node)
                return true
            }
        }
        return true
    }

    override fun getDestination(node: Node, n: Node): Location {
        return n.location
    }

    /**
     * Drops the balloons.
     */
    fun drop() {
        balloons.clear()
        Pulser.submit(object : Pulse(1) {
            var waves = 0
            override fun pulse(): Boolean {
                if (waves == 0 || waves == 3 || waves == 5 || waves == 8 || waves == 10 || waves == 12 || waves == 15 || waves == 18 || waves == 20) {
                    for (i in 0..10) {
                        val balloon = balloon
                        if (balloon != null) {
                            balloons.add(balloon)
                            SceneryBuilder.add(balloon, RandomFunction.random(20, 30))
                        }
                    }
                }
                return ++waves > 20
            }
        })
    }

    /**
     * Gets the balloon drop.
     * @return the balloon.
     */
    private val balloon: Scenery?
        get() {
            val location = Location(2602 + RandomFunction.randomSign(RandomFunction.getRandom(8)), 3208 + RandomFunction.randomSign(RandomFunction.getRandom(6)), 0)
            return if (!isTeleportPermitted(location) || getObject(location) != null) {
                null
            } else Scenery(PartyBalloon.values()[RandomFunction.random(PartyBalloon.values().size)].balloonId, location)
        }
    /**
     * A party balloon.
     * @author Vexia
     */
    internal enum class PartyBalloon(
            /**
             * The balloon id.
             */
            val balloonId: Int,
            /**
             * The popping id.
             */
            private val popId: Int) {
        YELLOW(115, 123), RED(116, 124), BLUE(117, 125), GREEN(118, 126), PURPLE(119, 127), WHITE(120, 128), GREEN_BLUE(121, 129), TRI(122, 130);
        /**
         * Gets the balloonId.
         * @return the balloonId
         */
        /**
         * Gets the popId.
         * @return the popId
         */

        /**
         * Constructs a new {@Code PartyBalloon} {@Code Object}
         * @param balloonId the balloon id.
         * @param popId the pop id.
         */

        /**
         * Bursts a party balloon.
         * @param player the player.
         * @param object the object.
         */
        fun burst(player: Player, balloon: Node) {
            player.sendChat("woopsie!")
/*

			val popped = `object`.transform(popId)
			if (!balloons.contains(`object`)) {
				player.lock(2)
				SceneryBuilder.remove(`object`)
				SceneryBuilder.remove(popped)
				player.animate(Animation.create(10017))
				player.sendMessage("Error! Balloon not registered.")
				return
			}
			player.lock(2)
			SceneryBuilder.remove(`object`)
			SceneryBuilder.add(popped)
			balloons.remove(`object`)
			player.animate(Animation.create(10017))

			// Pop a party balloon
			Pulser.submit(object : Pulse(1) {
				var counter = 0
				override fun pulse(): Boolean {
					when (++counter) {
						1 -> {
							SceneryBuilder.remove(popped)
							return true
						}
					}
					return false
				}
			})

 */
        }

        /**
         * Gets the ground item.
         * @param location the location.
         * @param player the player.
         * @return the ground item.
         */
        private fun getGround(location: Location, player: Player): GroundItem? {
            val item = PartyRoomPlugin.getPartyChest().toArray()[RandomFunction.random(PartyRoomPlugin.getPartyChest().itemCount())]
                    ?: return null
            if (PartyRoomPlugin.getPartyChest().remove(item)) {
                val dropItem: Item
                val newamt: Int
                if (item.amount > 1) {
                    newamt = RandomFunction.random(1, item.amount)
                    if (item.amount - newamt > 0) {
                        val newItem = Item(item.id, item.amount - newamt)
                        PartyRoomPlugin.getPartyChest().add(newItem)
                    }
                    dropItem = Item(item.id, newamt)
                } else {
                    dropItem = item
                }
                return GroundItem(dropItem, location, player)
            }
            return null
        }

        companion object {
            /**
             * Gets a party balloon.
             * @param id the id.
             * @return the balloon.
             */
            fun forId(id: Int): PartyBalloon? {
                for (balloon in values()) {
                    if (balloon.balloonId == id) {
                        return balloon
                    }
                }
                return null
            }
        }
    }

    companion object {
        /**
         * Gets the balloons.
         * @return the balloons
         */
        /**
         * The list of dropped balloons.
         */
        val balloons: MutableList<Scenery> = ArrayList(20)
    }
}
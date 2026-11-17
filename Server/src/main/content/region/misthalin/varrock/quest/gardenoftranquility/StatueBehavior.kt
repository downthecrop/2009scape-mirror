package content.region.misthalin.varrock.quest.gardenoftranquility

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository.getPlayerByName
import core.tools.minutesToTicks
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

private const val ATTR_CLEAR_TIME = "cleartime" // tracks statue despawn time
private const val ATTR_GUARD_SCRIPT = "firstentry" // stops the garden tele script from re-triggering every tick

class StatueBehavior : NPCBehavior(NPCs.TROLLEY_2582, NPCs.TROLLEY_2583) {

    override fun onCreation(self: NPC) {
        super.onCreation(self)
        setAttribute(self, ATTR_CLEAR_TIME, 0)
        setAttribute(self, ATTR_GUARD_SCRIPT, true)
    }

    override fun tick(self: NPC): Boolean {
        val player: Player? = getPlayerByName(getAttribute(self, GardenOfTranquility.ATTR_STATUE_OWNER, null))
        val zone: ZoneBorders
        val varbit: Int
        val statue: String
        val loc: String

        when (self.id) {
            // Saradomin
            NPCs.TROLLEY_2582 -> {
                zone = ZoneBorders(2937, 3321, 3007, 3391) // a zone covering the square in Falador where you get the statue. Push the statue out the north gate.
                varbit = GardenOfTranquility.VB_SARA_STATUE
                statue = "Saradomin"
                loc = "Falador"
            }

            // King (NPCs.TROLLEY_2583)
            else -> {
                zone = ZoneBorders(3201, 3190, 3258, 3263) // a zone covering the front of Lumbridge Castle where you get the statue. Push the statue across the bridge.
                varbit = GardenOfTranquility.VB_KING_STATUE
                statue = "a king"
                loc = "Lumbridge"
            }
        }

        if (player == null) {
            self.clear()
        } else {
            // check if the statue needs to be moved to the garden
            teleToGarden(self, zone)

            // clear statue after 5 minutes
            val clearTime = getAttribute(self, ATTR_CLEAR_TIME, 0) + 1
            setAttribute(self, ATTR_CLEAR_TIME, clearTime)

            if (clearTime > minutesToTicks(5)) {
                setAttribute(self, ATTR_CLEAR_TIME, 0)
                if (getVarbit(player, varbit) == 1) {
                    sendMessage(player, "The guards have recaptured the statue of $statue, and placed it back in $loc.")
                    setVarbit(player, varbit, 0)
                    clearHintIcon(player)
                    addItemOrDrop(player, Items.TROLLEY_6478)
                    clearLogoutListener(player, GardenOfTranquility.STATUE_LOGOUT)
                }
                self.clear()
            }

            // clear statue if player logs out. There is also a logout listener that will reset the varbit
            if (!player.isActive) self.clear()
        }

        return true
    }
}

fun teleToGarden(self: NPC, zoneBorders: ZoneBorders) {
    val player: Player? = getPlayerByName(getAttribute(self, GardenOfTranquility.ATTR_STATUE_OWNER, null))

    // if statue is in the garden, continue
    val gardenZone = ZoneBorders(3171, 3457, 3233, 3519) // a zone covering the Garden of Tranquility and some of Varrock Castle
    if (inBorders(self, gardenZone)) return

    // if statue has left the zone where you got it, tele to garden
    val firstEntry = getAttribute(self, ATTR_GUARD_SCRIPT, true)
    val insideZone = when (self.id) {
        // Saradomin
        NPCs.TROLLEY_2582 -> {
            inBorders(self, zoneBorders)
        }

        // King (NPCs.TROLLEY_2583)
        else -> {
            // the extra !borders is for the Lumbridge bridge. Statue should trigger when it gets to the bridge.
            inBorders(self, zoneBorders) && !inBorders(self, ZoneBorders(3242, 3225, 3251, 3226))
        }
    }

    if (!insideZone && firstEntry) {
        setAttribute(self, ATTR_GUARD_SCRIPT, false)
        queueScript(player!!, 0, QueueStrength.SOFT) { counter ->
            when (counter) {
                0 -> {
                    openOverlay(player, Components.FADE_TO_BLACK_120)
                    setInterfaceText(player, "Much puffing and panting later...", Components.FADE_TO_BLACK_120, 1)
                    return@queueScript delayScript(player, 4)
                }

                1 -> {
                    teleport(player, location(3213, 3500, 0))
                    teleport(self, location(3214, 3500, 0))
                    return@queueScript delayScript(player, 4)
                }

                2 -> {
                    openOverlay(player, Components.FADE_FROM_BLACK_170)
                    return@queueScript delayScript(player, 4)
                }

                3 -> {
                    closeOverlay(player)
                    setAttribute(self, ATTR_GUARD_SCRIPT, true)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }
}

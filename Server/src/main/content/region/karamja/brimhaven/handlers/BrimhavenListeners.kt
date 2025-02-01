package content.region.karamja.brimhaven.handlers

import content.region.karamja.brimhaven.dialogue.CapnIzzyDialogue
import content.region.karamja.brimhaven.dialogue.PirateJackieDialogue
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.repository.Repository
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class BrimhavenListeners : InteractionListener {
    companion object {
        /**
         * Represents the ladder that exits the Agility Arena.
         */
        private const val AGILITY_ARENA_EXIT_LADDER = Scenery.LADDER_3618

        /**
         * Represents the ladder that enters the Agility Arena.
         */
        private const val AGILITY_ARENA_ENTRANCE_LADDER = Scenery.LADDER_3617

        /**
         * Represents the area inside Brimhaven Agility Arena.
         */
        private val AGILITY_ARENA = location(2805, 9589, 3)

        /**
         * Represents the area within the hut outside the Brimhaven Agility Arena.
         */
        private val AGILITY_ARENA_HUT = location(2809, 3193, 0)

        /**
         * Represents the agility ticket exchange interface/component.
         */
        private const val TICKET_EXCHANGE_IFACE = Components.AGILITYARENA_TRADE_6

        /**
         * Represents the back door of the Shrimp and Parrot restaurant used in the Heroes' Quest.
         */
        private const val RESTAURANT_REAR_DOOR = Scenery.DOOR_1591

        /**
         * Represents Lubufu's karambwan fishing spot unlocked in Tai Bwo Wannai Trio.
         */
        private const val KARAMBWAN_FISHING_SPOT = NPCs.FISHING_SPOT_1178
    }

    override fun defineListeners() {
        on(AGILITY_ARENA_EXIT_LADDER, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, AGILITY_ARENA_HUT)
            return@on true
        }

        on(AGILITY_ARENA_ENTRANCE_LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            if (!getAttribute(player, "capn_izzy", false)) {
                openDialogue(player, CapnIzzyDialogue(1))
                return@on true
            } else {
                ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, AGILITY_ARENA)
                removeAttribute(player, "capn_izzy")
            }
            return@on true
        }

        on(NPCs.CAPN_IZZY_NO_BEARD_437, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, CapnIzzyDialogue(0), node)
            return@on true
        }

        on(NPCs.CAPN_IZZY_NO_BEARD_437, IntType.NPC, "pay") { player, node ->
            openDialogue(player, CapnIzzyDialogue(2), node)
            return@on true
        }

        on(NPCs.PIRATE_JACKIE_THE_FRUIT_1055, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, PirateJackieDialogue(), node)
            return@on true
        }

        on(NPCs.PIRATE_JACKIE_THE_FRUIT_1055, IntType.NPC, "trade") { player, _ ->
            openInterface(player, TICKET_EXCHANGE_IFACE)
            return@on true
        }

        on(RESTAURANT_REAR_DOOR, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You try and open the door...")
            sendMessage(player, "The door is locked tight, I can't open it.")
            return@on true
        }

        on(KARAMBWAN_FISHING_SPOT, IntType.NPC, "fish") { player, _ ->
            sendNPCDialogue(
                player,
                NPCs.LUBUFU_1171,
                "Keep off my fishing spot, whippersnapper!",
                FacialExpression.FURIOUS
            )
            return@on true
        }
    }
}
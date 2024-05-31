package content.region.kandarin.quest.scorpioncatcher

import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_TAVERLY
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_BARB
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_MONK
import core.api.addItem
import core.game.node.item.Item
import core.api.removeItem
import core.api.runTask
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.config.NPCConfigParser
import core.game.world.GameWorld
import org.rs09.consts.Items
import org.rs09.consts.NPCs


class ScorpionCatcherUseListener : InteractionListener {

    override fun defineListeners() {
        /**
         * List of cages
         *  Talvery Barbarian Monk
         *     TBM
         * 456 ---
         * 457 O--
         * 458 00-
         * 459 -0-
         * 460 -00
         * 461 --0
         * 462 0-0
         * 463 000
         *
         * Scorpions
         * 385 - Barbarian
         * 386 - Taverly
         * 387 - Monastery
         */

        /**
         * Good captures
         */

        /**
         * Empty cage on Taverly scorpion
         */

        // todo check this message
        val haveAlready = "You already have this scorpion in this cage."
        val catchMessage = "You catch a scorpion!"

        onUseWith(IntType.NPC, Items.SCORPION_CAGE_456, NPCs.KHARID_SCORPION_386){ player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_457)
            player.sendMessage(catchMessage)
            // This is the first time taverly has been caught
            if (!player.getAttribute(ATTRIBUTE_TAVERLY, false)){
                player.setAttribute("/save:$ATTRIBUTE_TAVERLY", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }
        /**
         * Barbarian cage on Taverly scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_459, NPCs.KHARID_SCORPION_386){ player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_458)
            player.sendMessage(catchMessage)
            // This is the first time taverly has been caught
            if (!player.getAttribute(ATTRIBUTE_TAVERLY, false)){
                player.setAttribute("/save:$ATTRIBUTE_TAVERLY", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }
        /**
         * Monk cage on Taverly scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_461, NPCs.KHARID_SCORPION_386){ player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_462)
            player.sendMessage(catchMessage)
            // This is the first time taverly has been caught
            if (!player.getAttribute(ATTRIBUTE_TAVERLY, false)){
                player.setAttribute("/save:$ATTRIBUTE_TAVERLY", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }
        /**
         * Others on Taverly scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_460, NPCs.KHARID_SCORPION_386){ player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_463)
            player.sendMessage(catchMessage)
            // This is the first time taverly has been caught
            if (!player.getAttribute(ATTRIBUTE_TAVERLY, false)){
                player.setAttribute("/save:$ATTRIBUTE_TAVERLY", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }

        /**
         * Empty cage on barbarian agility course scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_456, NPCs.KHARID_SCORPION_385){ player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_459)
            player.sendMessage(catchMessage)
            // This is the first time barbarian has been caught
            if (!player.getAttribute(ATTRIBUTE_BARB, false)){
                player.setAttribute("/save:$ATTRIBUTE_BARB", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }
        /**
         * Cage with Taverly scorpion on barbarian scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_457, NPCs.KHARID_SCORPION_385) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_458)
            player.sendMessage(catchMessage)
            // This is the first time barbarian has been caught
            if (!player.getAttribute(ATTRIBUTE_BARB, false)){
                player.setAttribute("/save:$ATTRIBUTE_BARB", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }
        /**
         * Cage with Monk scorpion on barbarian scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_461, NPCs.KHARID_SCORPION_385) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_460)
            player.sendMessage(catchMessage)
            // This is the first time barbarian has been caught
            if (!player.getAttribute(ATTRIBUTE_BARB, false)){
                player.setAttribute("/save:$ATTRIBUTE_BARB", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }

        /**
         * Others on barbarian scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_462, NPCs.KHARID_SCORPION_385) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_463)
            player.sendMessage(catchMessage)
            // This is the first time barbarian has been caught
            if (!player.getAttribute(ATTRIBUTE_BARB, false)){
                player.setAttribute("/save:$ATTRIBUTE_BARB", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }


        /**
         * Empty on Monk scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_456, NPCs.KHARID_SCORPION_387) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_461)
            player.sendMessage(catchMessage)
            // This is the first time the monastery has been caught
            if (!player.getAttribute(ATTRIBUTE_MONK, false)){
                player.setAttribute("/save:$ATTRIBUTE_MONK", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }

        /**
         * Taverly cage on Monk scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_457, NPCs.KHARID_SCORPION_387) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_462)
            player.sendMessage(catchMessage)
            // This is the first time the monastery has been caught
            if (!player.getAttribute(ATTRIBUTE_MONK, false)){
                player.setAttribute("/save:$ATTRIBUTE_MONK", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }

        /**
         * Barbarian cage on Monk scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_459, NPCs.KHARID_SCORPION_387) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_460)
            player.sendMessage(catchMessage)
            // This is the first time the monastery has been caught
            if (!player.getAttribute(ATTRIBUTE_MONK, false)){
                player.setAttribute("/save:$ATTRIBUTE_MONK", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }

        /**
         * Others on Monk scorpion
         */
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_458, NPCs.KHARID_SCORPION_387) { player, used, with ->
            removeItem(player, Item(used.id, 1))
            addItem(player, Items.SCORPION_CAGE_463)
            player.sendMessage(catchMessage)
            // This is the first time the monastery has been caught
            if (!player.getAttribute(ATTRIBUTE_MONK, false)){
                player.setAttribute("/save:$ATTRIBUTE_MONK", true)
            }
            runTask(player, 2) {
                with.asNpc().respawnTick =
                    GameWorld.ticks + with.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
            }
            return@onUseWith true
        }


        /**
         * Player being stupid and trying to recatch one they have already
         */

        /**
         * Taverly
         */
        // Just Taverly
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_457, NPCs.KHARID_SCORPION_386){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Taverly and Barb
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_458, NPCs.KHARID_SCORPION_386){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Taverly and Monk
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_462, NPCs.KHARID_SCORPION_386){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // All
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_463, NPCs.KHARID_SCORPION_386){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }


        /**
         * Barbarian
         */
        // Just Barb
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_459, NPCs.KHARID_SCORPION_385){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Barb and Taverly
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_458, NPCs.KHARID_SCORPION_385){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Barb and Monk
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_460, NPCs.KHARID_SCORPION_385){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // All
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_463, NPCs.KHARID_SCORPION_385){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }

        /**
         * Monastery
         */
        // Just Monk
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_461, NPCs.KHARID_SCORPION_387){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Monk and Taverly
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_462, NPCs.KHARID_SCORPION_387){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // Monk and Barb
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_460, NPCs.KHARID_SCORPION_387){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }
        // All
        onUseWith(IntType.NPC, Items.SCORPION_CAGE_463, NPCs.KHARID_SCORPION_387){ player, _, _ ->
            player.sendMessage(haveAlready)
            return@onUseWith true
        }

    }


}
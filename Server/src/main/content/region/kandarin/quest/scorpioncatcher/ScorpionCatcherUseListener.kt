package content.region.kandarin.quest.scorpioncatcher

import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_TAVERLEY
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_BARB
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher.Companion.ATTRIBUTE_MONK
import core.api.*
import core.game.node.item.Item
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.config.NPCConfigParser
import core.game.world.GameWorld
import core.tools.Log
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class ScorpionCatcherUseListener : InteractionListener {
    override fun defineListeners() {
        val scorpToAttr = mapOf(
            /* 385 - Barbarian
             * 386 - Taverley
             * 387 - Monastery
             */
            NPCs.KHARID_SCORPION_385 to ATTRIBUTE_BARB,
            NPCs.KHARID_SCORPION_386 to ATTRIBUTE_TAVERLEY,
            NPCs.KHARID_SCORPION_387 to ATTRIBUTE_MONK
        )
        val cageToScorps = mapOf(
            /*  Taverley(386) Barbarian(385) Monastery(387)
             *     TBM
             * 456 ---
             * 457 O--
             * 458 00-
             * 459 -0-
             * 460 -00
             * 461 --0
             * 462 0-0
             * 463 000
             */
            Items.SCORPION_CAGE_456 to setOf<Int>(),
            Items.SCORPION_CAGE_457 to setOf(NPCs.KHARID_SCORPION_386),
            Items.SCORPION_CAGE_458 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_385),
            Items.SCORPION_CAGE_459 to setOf(NPCs.KHARID_SCORPION_385),
            Items.SCORPION_CAGE_460 to setOf(NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_461 to setOf(NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_462 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_387),
            Items.SCORPION_CAGE_463 to setOf(NPCs.KHARID_SCORPION_386, NPCs.KHARID_SCORPION_385, NPCs.KHARID_SCORPION_387)
        )

        fun catchScorpion(player: Player, item: Node, scorpion: Node): Boolean {
            val haveInCage = cageToScorps[item.id] ?: return false
            if (scorpion.id in haveInCage) {
                sendMessage(player, "You already have this scorpion in this cage.") //TODO check this message
                return true
            }
            val newScorpionSet = haveInCage + setOf(scorpion.id)
            var newItem: Int? = null
            for ((cage, scorps) in cageToScorps) {
                if (scorps == newScorpionSet) {
                    newItem = cage
                }
            }
            if (newItem == null) {
                log(this::class.java, Log.ERR, "Error looking up new scorpion cage item - this isn't possible")
                return false
            }
            val attribute = scorpToAttr[scorpion.id]
            if (removeItem(player, Item(item.id, 1)) && addItem(player, newItem)) {
                sendMessage(player, "You catch a scorpion!")
                setAttribute(player, "/save:$attribute", true)
                runTask(player, 2) {
                    scorpion.asNpc().respawnTick = GameWorld.ticks + scorpion.asNpc().definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 34)
                }
                return true
            }
            return false
        }

        for (scorp in scorpToAttr.keys) {
            for (cage in cageToScorps.keys) {
                onUseWith(IntType.NPC, cage, scorp) { player, usedCage, usedScorp ->
                    return@onUseWith catchScorpion(player, usedCage, usedScorp)
                }
            }
        }
    }
}

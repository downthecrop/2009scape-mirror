package content.global.ame.events.pillory

import content.global.ame.RandomEvents
import core.api.*
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

/**
 * Pillory Unlocking Interface PILLORY_189
 *
 * https://www.youtube.com/watch?v=caWn7pE2mkE
 * https://www.youtube.com/watch?v=TMVR5cZZwZ0
 * https://www.youtube.com/watch?v=Ym9LCDP-Q74
 * https://www.youtube.com/watch?v=_vn0QZTtI6U (Failure)
 * https://www.youtube.com/watch?v=zmXDikQIua4
 *
 * Child IDs
 *  4 - Rotating Lock Model
 *  5 6 7 - Swinging Keys Models
 *  8 9 10 - Buttons for the Swinging Keys Models
 *  11 12 13 14 15 16 - Padlocks at the Top
 *  17 18 19 20 21 22 - Padlocks stars? Model 15272, Anim 4135
 *
 * Model IDs
 *  Using the amazeballs ::listifmodels
 *  9749, 9750, 9751, 9752 - Swinging Keys Models
 *  9753, 9754, 9755, 9756 - Rotating Lock Models
 *  9757 9758 locked unlock
 */
class PilloryInterface : InterfaceListener, InteractionListener, MapArea {
    companion object {
        const val PILLORY_LOCK_INTERFACE = 189
        const val PILLORY_ATRRIBUTE_RETURN_LOC = "/save:original-loc"
        const val PILLORY_ATTRIBUTE_EVENT_KEYS = "pillory:event-keys"
        const val PILLORY_ATTRIBUTE_EVENT_LOCK = "pillory:event-lock"
        const val PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT = "/save:pillory:target-correct"
        const val PILLORY_ATRRIBUTE_CORRECT_COUNTER = "/save:pillory:num-correct"

        val LOCATIONS = arrayOf(
                // Varrock Cages
                Location(3226, 3407, 0),
                Location(3228, 3407, 0),
                Location(3230, 3407, 0),
                // Seers Village Cages
                Location(2681, 3489, 0),
                Location(2683, 3489, 0),
                Location(2685, 3489, 0),
                // Yannile Cages
                Location(2604, 3105, 0),
                Location(2606, 3105, 0),
                Location(2608, 3105, 0),
        )

        fun initPillory(player: Player) {
            setAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 3)
            setAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0)
            player.dialogueInterpreter.sendPlainMessage(true, "", "Solve the pillory puzzle to be returned to where you came from.")
        }

        fun randomPillory(player: Player) {
            // Shuffle all 4 kinds of keys in, pick 3 for the keys, pick 1 from the 3 as the lock.
            val keys = (0..3).toIntArray().let{ keys -> keys.shuffle(); return@let keys }
            val lock = intArrayOf(keys[1], keys[2], keys[3]).random() // Last 3 as there are 4 keys. key[0] is fallback.

            setAttribute(player, PILLORY_ATTRIBUTE_EVENT_KEYS, keys)
            setAttribute(player, PILLORY_ATTRIBUTE_EVENT_LOCK, lock)

            player.packetDispatch.sendModelOnInterface(9753 + lock, PILLORY_LOCK_INTERFACE, 4, 0)
            player.packetDispatch.sendModelOnInterface(9749 + keys[1], PILLORY_LOCK_INTERFACE, 5, 0)
            player.packetDispatch.sendModelOnInterface(9749 + keys[2], PILLORY_LOCK_INTERFACE, 6, 0)
            player.packetDispatch.sendModelOnInterface(9749 + keys[3], PILLORY_LOCK_INTERFACE, 7, 0)

            val numberToGetCorrect = getAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 3)
            val correctCount = getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0)
            for (i in 1.. 6) {
                // Set if lock is red or green.
                if (i <= correctCount) {
                    player.packetDispatch.sendModelOnInterface(9758, PILLORY_LOCK_INTERFACE, 10 + i, 0)
                } else {
                    player.packetDispatch.sendModelOnInterface(9757, PILLORY_LOCK_INTERFACE, 10 + i, 0)
                }
                // Set if hide or show lock.
                player.packetDispatch.sendInterfaceConfig(PILLORY_LOCK_INTERFACE, 10 + i, i > numberToGetCorrect)
            }
        }

        fun selectedKey(player: Player, buttonID: Int) {
            val keys = getAttribute(player, PILLORY_ATTRIBUTE_EVENT_KEYS, intArrayOf(0, 0, 0))
            val lock = getAttribute(player, PILLORY_ATTRIBUTE_EVENT_LOCK, -1)
            if (keys[buttonID] == lock) {
                // CORRECT ANSWER
                setAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0) + 1)
                if (getAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 3) <= getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, -1)) {
                    player.dialogueInterpreter.sendPlainMessage(true, "", "You've escaped!")
                    sendMessage(player, "You've escaped!")
                    removeAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT)
                    removeAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER)
                    closeInterface(player)
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                lock(player, 6)
                                sendGraphics(Graphics(1576, 0, 0), player.location)
                                animate(player,8939)
                                playAudio(player, Sounds.TELEPORT_ALL_200)
                                return@queueScript delayScript(player, 3)
                            }
                            1 -> {
                                val loot = RandomEvents.CERTER.loot!!.roll(player)[0]
                                addItemOrDrop(player, loot.id, loot.amount)
                                teleport(player, getAttribute(player, PILLORY_ATRRIBUTE_RETURN_LOC, Location.create(3222, 3218, 0)))
                                sendGraphics(Graphics(1577, 0, 0), player.location)
                                animate(player,8941)
                                removeAttribute(player, PILLORY_ATRRIBUTE_RETURN_LOC)
                                closeInterface(player)
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                    return
                }
                randomPillory(player)
                player.dialogueInterpreter.sendPlainMessage(
                        true,
                        "",
                        "Correct!",
                        "" + getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0) + " down, " +
                                (getAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 3) - getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0)) + " to go!")
                // Animation for the star, but it doesn't work.
                player.packetDispatch.sendInterfaceConfig(PILLORY_LOCK_INTERFACE, 16 + getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 1), false)
                sendAnimationOnInterface(player, 4135, PILLORY_LOCK_INTERFACE, 16 + getAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 1))
            } else {
                // WRONG ANSWER
                player.dialogueInterpreter.close()
                player.dialogueInterpreter.sendDialogues(NPCs.TRAMP_2794 , FacialExpression.OLD_ANGRY1, "Bah, that's not right.","Use the key that matches the hole", "in the spinning lock.")
                if (getAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 0) < 6) {
                    setAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, getAttribute(player, PILLORY_ATRRIBUTE_NEEDED_TO_GET_CORRECT, 0) + 1)
                }
                setAttribute(player, PILLORY_ATRRIBUTE_CORRECT_COUNTER, 0)
                closeInterface(player)
            }
        }
    }

    override fun defineInterfaceListeners() {
        on(PILLORY_LOCK_INTERFACE){ player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                8 -> selectedKey(player, 1)
                9 -> selectedKey(player, 2)
                10 -> selectedKey(player, 3)
            }
            return@on true
        }

        onOpen(PILLORY_LOCK_INTERFACE){ player, component ->
            return@onOpen true
        }
    }

    override fun defineListeners() {
        on(Scenery.CAGE_6836, IntType.SCENERY, "unlock") { player, node ->
            if (player.location in LOCATIONS) { // When you aren't inside.
                randomPillory(player)
                openInterface(player, PILLORY_LOCK_INTERFACE)
                player.dialogueInterpreter.sendPlainMessage(true, "", "Pick the <col=8A0808>swinging key</col> that matches the", "hole in the <col=8A0808>spinning lock</col>.")
            } else {
                sendMessage(player, "You can't unlock the pillory, you'll let all the prisoners out!")
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(
                // Varrock Cages
                ZoneBorders(3226, 3407, 3226, 3407),
                ZoneBorders(3228, 3407, 3228, 3407),
                ZoneBorders(3230, 3407, 3230, 3407),
                // Seers Village Cages
                ZoneBorders(2681, 3489, 2681, 3489),
                ZoneBorders(2683, 3489, 2683, 3489),
                ZoneBorders(2685, 3489, 2685, 3489),
                // Yannile Cages
                ZoneBorders(2604, 3105, 2604, 3105),
                ZoneBorders(2606, 3105, 2606, 3105),
                ZoneBorders(2608, 3105, 2608, 3105),
        )
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            entity.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 12)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.interfaceManager.restoreTabs()
        }
    }


}
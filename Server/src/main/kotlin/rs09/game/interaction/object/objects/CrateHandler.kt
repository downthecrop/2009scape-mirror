package rs09.game.interaction.`object`.objects

import api.openDialogue
import api.sendMessage
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.`object`.dialogues.CrateDialogues

/**
 * @author qmqz
 */

class CrateHandler : InteractionListener {

    private val monkeyAmuletMouldCrate = 4724
    private val threadCrate = 4718
    private val monkeyTalkingDentures = 4715
    private val bananaCrate =  4723
    private val monkeyMadnessEntranceDown = 4714
    private val tinderboxCrate = 4719
    private val slimyGnomeEyesCrate = 4716
    private val hammersCrate = 4726

    override fun defineListeners() {
        //searching crates with monkey amulet moulds
        on(monkeyAmuletMouldCrate, IntType.SCENERY, "search") { player, node ->
            player.animator.animate(Animation(536))
            sendMessage(player, "You search the crate.")
            openDialogue(player!!, CrateDialogues(0))
            return@on true
        }

        on(threadCrate, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(1))
                    }
                    return false
                }
            })
            return@on true
        }

        on(monkeyTalkingDentures, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(2))
                    }
                    return false
                }
            })
            return@on true
        }

        on(bananaCrate, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(3))
                    }
                    return false
                }
            })
            return@on true
        }

        on(monkeyMadnessEntranceDown, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            openDialogue(player!!, CrateDialogues(4))
            return@on true
        }

        on(tinderboxCrate, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(5))
                    }
                    return false
                }
            })
            return@on true
        }

        on(slimyGnomeEyesCrate, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(6))
                    }
                    return false
                }
            })
            return@on true
        }

        on(hammersCrate, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the crate.")
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        3 -> openDialogue(player!!, CrateDialogues(7))
                    }
                    return false
                }
            })
            return@on true
        }

    }
}
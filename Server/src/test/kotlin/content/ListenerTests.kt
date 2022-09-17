package content

import TestUtils
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners

class ListenerTests : InteractionListener {
    @Test fun doubleDefinedListenerShouldThrowIllegalStateException() {
        on(0, IntType.ITEM, "touch") {_,_ -> return@on true}
        Assertions.assertThrows(IllegalStateException::class.java) {
            on(0, IntType.ITEM, "touch") {_,_ -> return@on true}
        }
    }

    @Test fun doubleDefinedUseWithShouldThrowIllegalStateException() {
        onUseWith(IntType.SCENERY, 0, 1) {_,_,_ -> return@onUseWith true}
        Assertions.assertThrows(IllegalStateException::class.java) {
            onUseWith(IntType.SCENERY, 0, 1) {_,_,_ -> return@onUseWith true}
        }
    }

    @Test fun conflictingCatchallShouldThrowIllegalStateException() {
        on(IntType.ITEM, "boop"){_,_ -> return@on true}
        Assertions.assertThrows(IllegalStateException::class.java) {
            on(IntType.ITEM, "boop") {_,_ -> return@on true}
        }
    }

    @Test fun specificListenerShouldOverrideCatchallListener() {
        var specificRan = false
        var catchAllRan = false
        on(IntType.ITEM, "zap") { _, _ -> catchAllRan = true; return@on true}
        on(1, IntType.ITEM, "zap") {_,_ -> specificRan = true; return@on true}

        InteractionListeners.run(1, IntType.ITEM, "zap", TestUtils.getMockPlayer("bilbots"), Item(1))

        Assertions.assertEquals(false, catchAllRan)
        Assertions.assertEquals(true, specificRan)
    }

    override fun defineListeners() {}
}
package core.net

import MockSession
import TestUtils
import core.game.node.entity.player.info.Rights
import core.game.world.repository.Repository
import core.net.packet.PacketProcessor
import core.net.packet.`in`.Packet
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SessionClearingTests {
    companion object {
        init { TestUtils.preTestSetup() }
    }

    @Test
    fun afkLogoutIsClearedWhenClientIgnoresLogoutPacket() {
        val player = TestUtils.getMockPlayer("stale_logout", rights = Rights.REGULAR_PLAYER)
        val session = player.session as MockSession

        try {
            PacketProcessor.enqueue(Packet.TrackingAfkTimeout(player))
            PacketProcessor.processQueue()

            assertTrue(session.receivedPackets.isNotEmpty())
            assertFalse(session.disconnected)
            assertTrue(Repository.disconnectionQueue.contains(player.name))
            assertNotNull(Repository.getPlayerByName(player.name))

            TestUtils.advanceTicks(4)

            assertTrue(session.disconnected)
            assertFalse(Repository.disconnectionQueue.contains(player.name))
            assertFalse(Repository.players.contains(player))
            assertNull(Repository.getPlayerByName(player.name))
            assertNull(Repository.getPlayerByUid(player.details.accountInfo.uid))
        } finally {
            Repository.disconnectionQueue.remove(player.name)
            if (Repository.players.contains(player)) player.close()
        }
    }

    @Test
    fun finishClearFailureDoesNotLeavePlayerRegistered() {
        val player = TestUtils.getMockPlayer("failed_finish_clear")
        val session = player.session as MockSession
        player.logoutListeners["test-failure"] = { throw IllegalStateException("simulated logout listener failure") }

        try {
            Repository.disconnectionQueue.add(player)
            TestUtils.advanceTicks(4)

            assertTrue(session.disconnected)
            assertFalse(Repository.disconnectionQueue.contains(player.name))
            assertFalse(Repository.players.contains(player))
            assertNull(Repository.getPlayerByName(player.name))
            assertNull(Repository.getPlayerByUid(player.details.accountInfo.uid))
        } finally {
            player.logoutListeners.clear()
            Repository.disconnectionQueue.remove(player.name)
            if (Repository.players.contains(player)) player.close()
            else if (player.isActive) player.finishClear()
        }
    }
}

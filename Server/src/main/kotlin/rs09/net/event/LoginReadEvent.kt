package rs09.net.event

import core.game.node.entity.player.info.ClientInfo
import core.game.node.entity.player.info.PlayerDetails
import core.game.system.communication.CommunicationInfo
import core.net.IoReadEvent
import core.net.IoSession
import rs09.auth.AuthResponse
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import rs09.net.packet.`in`.Login
import java.nio.ByteBuffer

/**
 * Handles login reading events.
 * @author Ceikry
 */
class LoginReadEvent(session: IoSession?, buffer: ByteBuffer?) : IoReadEvent(session, buffer) {
    override fun read(session: IoSession, buffer: ByteBuffer) {
        SystemLogger.logInfo("--A")
        val (response, info) = Login.decodeFromBuffer(buffer)
        SystemLogger.logInfo("A")
        if(response != AuthResponse.Success || info == null) {
            session.write(response)
            return
        }
        SystemLogger.logInfo("B")
        val (authResponse, accountInfo) = GameWorld.authenticator.checkLogin(info.username, info.password)

        if(authResponse != AuthResponse.Success || accountInfo == null) {
            session.write(authResponse)
            return
        }
        SystemLogger.logInfo("C")
        val details = PlayerDetails(info.username)
        details.accountInfo = accountInfo
        details.communication.parse(accountInfo)
        session.clientInfo = ClientInfo(info.displayMode, info.windowMode, info.screenWidth, info.screenHeight)
        session.isaacPair = info.isaacPair
        SystemLogger.logInfo("D")
        Login.proceedWith(session, details, info.opcode)
    }
}
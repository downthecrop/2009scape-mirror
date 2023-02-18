package core.net.event

import core.game.node.entity.player.info.ClientInfo
import core.game.node.entity.player.info.PlayerDetails
import core.net.IoReadEvent
import core.net.IoSession
import core.auth.AuthResponse
import core.game.world.GameWorld
import core.net.packet.`in`.Login
import java.nio.ByteBuffer

/**
 * Handles login reading events.
 * @author Ceikry
 */
class LoginReadEvent(session: IoSession?, buffer: ByteBuffer?) : IoReadEvent(session, buffer) {
    override fun read(session: IoSession, buffer: ByteBuffer) {
        try {
            val (response, info) = Login.decodeFromBuffer(buffer)
            if (response != AuthResponse.Success || info == null) {
                session.write(response)
                return
            }
            val (authResponse, accountInfo) = GameWorld.authenticator.checkLogin(info.username, info.password)

            if (authResponse != AuthResponse.Success || accountInfo == null) {
                session.write(authResponse)
                return
            }
            val details = PlayerDetails(info.username)
            details.accountInfo = accountInfo
            details.communication.parse(accountInfo)
            session.clientInfo = ClientInfo(info.displayMode, info.windowMode, info.screenWidth, info.screenHeight)
            session.isaacPair = info.isaacPair
            session.associatedUsername = info.username
            Login.proceedWith(session, details, info.opcode)
        } catch (e: Exception) {
            e.printStackTrace()
            session.write(AuthResponse.UnexpectedError)
        }
    }
}
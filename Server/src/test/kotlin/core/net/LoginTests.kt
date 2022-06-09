package core.net

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.AuthResponse
import rs09.net.packet.`in`.Login
import java.nio.ByteBuffer

class LoginTests {
    @Test fun shouldDecodeCorrectlyEncodedPacket() {
        val localCopy = validLoginPacket.copyOf(validLoginPacket.size)
        val loginInfo = Login.decodeFromBuffer(ByteBuffer.wrap(localCopy))
        Assertions.assertEquals(AuthResponse.Success, loginInfo.first, "Info: ${loginInfo.second}")
    }

    @Test fun shouldNeverThrowExceptionButReturnUnexpectedErrorResponseInstead() {
        val localCopy = validLoginPacket.copyOf(validLoginPacket.size)
        for(i in 15 until validLoginPacket.size) {
            localCopy[i] = 0 //corrupt some data on purpose
        }
        var response: AuthResponse = AuthResponse.Updating
        Assertions.assertDoesNotThrow {
            val (res, _) = Login.decodeFromBuffer(ByteBuffer.wrap(localCopy))
            response = res
        }

        Assertions.assertEquals(AuthResponse.UnexpectedError, response)
    }

    @Test fun loginPacketWithInvalidOpcodeShouldReturnAHelpfulResponse() {
        val localCopy = validLoginPacket.copyOf(validLoginPacket.size)
        localCopy[0] = 2 //set to invalid login opcode
        val (response, _) = Login.decodeFromBuffer(ByteBuffer.wrap(localCopy))
        Assertions.assertEquals(AuthResponse.InvalidLoginServer, response)
    }

    companion object {
        val validLoginPacket = byteArrayOf(16,1,80,0,0,2,18,0,1,1,2,2,-3,1,-9,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,107,75,109,111,107,51,107,74,113,79,101,78,54,68,51,109,68,100,105,104,99,111,51,111,80,101,89,78,50,75,70,121,54,87,53,45,45,118,90,85,98,78,65,0,0,0,0,0,1,81,-73,-29,0,0,-47,-49,34,92,-124,110,-97,46,-33,-18,122,8,76,93,-82,16,53,23,-113,-73,101,126,-93,125,-65,115,-59,19,90,-54,-102,-76,-99,-5,-68,-51,95,-20,-27,10,60,60,108,5,76,9,-63,97,106,74,116,0,58,0,75,-111,-128,-34,12,64,47,-92,-33,-120,-109,-7,23,124,122,40,-107,56,-34,-93,64,-82,58,-90,7,127,-15,-85,125,43,15,0,112,60,4,75,72,55,18,83,-119,-39,32,-113,21,104,-49,66,-102,104,-13,32,117,-106,94,-30,37,-56,-67,21,77,70,-128,68,112,-78,-49,-58,-48,74,70,31,12,64,84,124,2,-61,71,104,55,73,62,-27,111,122,-56,52,93,1,-52,32,102,73,-21,-46,-79,45,58,88,-36,-113,31,78,77,-56,-126,-103,-33,21,110,47,-81,-70,-15,14,114,51,-41,-49,6,-112,-28,-28,-112,113,120,68,29,-123,-108,24,-58,6,113,54,55,21,-58,32,99,-120,13,8,-94,-83,121,123,20,16,-89,43,-51,-20,74,-114,-126,-86,-12,38,-107,12,25,-40,-79,-116,-18,-85,-83,95,-106,-81,-64,31,110,-65,-128,-74,70,80,-20,11,117,28,39,-102,75,67,98,-121,-28)
    }
}
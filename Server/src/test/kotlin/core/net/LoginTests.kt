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
        val validLoginPacket = byteArrayOf(16, 1, 80, 0, 0, 2, 18, 0, 1, 1, 2, 2, -3, 1, -9, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 107, 75, 109, 111, 107, 51, 107, 74, 113, 79, 101, 78, 54, 68, 51, 109, 68, 100, 105, 104, 99, 111, 51, 111, 80, 101, 89, 78, 50, 75, 70, 121, 54, 87, 53, 45, 45, 118, 90, 85, 98, 78, 65, 0, 0, 0, 0, 0, 1, 89, -73, -29, 0, 0, -47, -49, 34, 92, -124, 110, -97, 46, -33, -18, 122, 8, 76, 93, -82, 16, 53, 23, -113, -73, 101, 126, -93, 125, -65, 115, -59, 19, 90, -54, -102, -76, -99, -5, -68, -51, 95, -20, -27, 10, 60, 60, 108, 5, 76, 9, -63, 97, 106, 74, 116, 0, 58, 0, 75, -111, -128, -34, 12, 64, 47, -92, -33, -120, -109, -7, 23, 124, 122, 40, -107, 56, -34, -93, 64, -82, 58, -90, 7, 127, -15, -85, 125, 43, 15, 0, 112, 60, 4, 75, 72, 55, 18, 83, -119, -39, 32, -113, 21, 104, -49, 66, -102, 104, -13, 32, 117, -106, 94, -30, 37, -56, -67, 21, 77, 70, -128, 113, 86, 84, 83, 115, 3, 55, -106, 127, -14, -15, 6, 42, -92, -56, 114, 24, 83, 32, -127, 78, 14, -98, -30, -38, -115, 9, -106, 120, 101, -117, -50, -40, -64, -50, -106, -98, 5, -86, 28, -127, -5, 109, -107, 49, -107, 63, 81, -81, -109, -21, 25, -68, 63, 102, -5, 8, -96, 126, -128, -116, -32, 26, 76, 54, -63, -37, 41, 57, 65, -53, -22, 83, 61, -128, -17, -21, 87, 76, -8, -95, -45, -80, -60, -62, 96, 49, 26, 49, 94, 80, 11, -12, -95, -58, -116, -54, -107, 91, 104, 58, 33, 20, -93, -68, -83, -116, 63, -18, 36, 30, -98, 77, -107, 122, 79, -27, 67, -94, 125, -81, -21, -75, -71, -45, -39, 112, 40)
    }
}
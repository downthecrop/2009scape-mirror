package core.auth

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.InMemoryAuthProvider
import rs09.auth.LoginResponse
import rs09.auth.UserAccountInfo
import rs09.storage.InMemoryStorageProvider

class AuthenticatorTests {
    private val authProvider = InMemoryAuthProvider()
    private val storageProvider = InMemoryStorageProvider()

    init {
        authProvider.configureFor(storageProvider)
    }

    @Test fun shouldAllowCheckingIfAccountExists() {
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        Assertions.assertEquals(true, authProvider.canCreateAccountWith(info))
        authProvider.createAccountWith(info)
        Assertions.assertEquals(false, authProvider.canCreateAccountWith(info))
    }

    @Test fun loginWithValidAccountInfoReturnsSuccess() {
        Assertions.assertEquals(LoginResponse.Success, authProvider.checkLogin("Billy", ""))
    }

    @Test fun loginWithInvalidAccountInfoReturnsFailure() {
        Assertions.assertNotEquals(LoginResponse.Success, authProvider.checkLogin("Bilbo", "ebbeb"))
    }
}
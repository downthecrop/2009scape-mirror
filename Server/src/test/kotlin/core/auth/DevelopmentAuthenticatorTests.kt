package core.auth

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.DevelopmentAuthenticator
import rs09.auth.LoginResponse
import rs09.auth.UserAccountInfo
import rs09.storage.InMemoryStorageProvider

class DevelopmentAuthenticatorTests {
    private val authProvider = DevelopmentAuthenticator()
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
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        authProvider.createAccountWith(info)
        Assertions.assertEquals(LoginResponse.Success, authProvider.checkLogin("Billy", ""))
    }

    @Test fun loginWithInvalidAccountInfoReturnsFailure() {
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        authProvider.createAccountWith(info)
        Assertions.assertNotEquals(LoginResponse.Success, authProvider.checkLogin("Bilbo", "ebbeb"))
    }

    @Test fun loginUsernameIsNotCaseSensitive() {
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        authProvider.createAccountWith(info)
        Assertions.assertEquals(LoginResponse.Success, authProvider.checkLogin("Billy", ""))
        Assertions.assertEquals(LoginResponse.Success, authProvider.checkLogin("billy", ""))
    }
}
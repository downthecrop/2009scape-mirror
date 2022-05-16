package core.auth

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.DevelopmentAuthenticator
import rs09.auth.AuthResponse
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
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("Billy", "").first)
    }

    //Development authenticator should work regardless if account exists or not.
    @Test fun loginWithInvalidAccountInfoReturnsSuccess() {
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        authProvider.createAccountWith(info)
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("Bilbo", "ebbeb").first)
    }

    @Test fun loginUsernameIsNotCaseSensitive() {
        val info = UserAccountInfo.createDefault()
        info.username = "Billy"
        authProvider.createAccountWith(info)
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("Billy", "").first)
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("billy", "").first)
    }

    //Development authenticator should basically bypass needing/creating an account entirely. useful for SP too.
    @Test fun loginToUnregisteredAccountCreatesIt() {
        authProvider.checkLogin("masterbaggins", "whatever")
        val info = storageProvider.getAccountInfo("masterbaggins")
        Assertions.assertEquals(2, info.rights)
    }
}
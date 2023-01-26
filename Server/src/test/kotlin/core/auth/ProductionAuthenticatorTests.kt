package core.auth

import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import core.auth.AuthResponse
import core.auth.ProductionAuthenticator
import core.auth.UserAccountInfo
import core.storage.InMemoryStorageProvider

class ProductionAuthenticatorTests {
    companion object {
        private val authProvider = ProductionAuthenticator()
        private val storageProvider = InMemoryStorageProvider()

        init {
            authProvider.configureFor(storageProvider)
        }

        @BeforeAll @JvmStatic fun createTestAccount() {
            val details = UserAccountInfo.createDefault()
            details.username = "test"
            details.password = "testing"
            if(!storageProvider.checkUsernameTaken("test")) {
                authProvider.createAccountWith(details)
            }
        }
    }

    @Test fun shouldRejectLoginWithInvalidDetails() {
        Assertions.assertEquals(AuthResponse.InvalidCredentials, authProvider.checkLogin("test", "test2").first)
    }

    @Test fun loginUsernameIsNotCaseSensitive() {
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("Test", "testing").first)
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("test", "testing").first)
    }

    @Test fun shouldHashPasswords() {
        Assertions.assertNotEquals("testing", storageProvider.getAccountInfo("test").password)
    }

    @Test fun shouldNotAllowBannedLogin() {
        val info = storageProvider.getAccountInfo("test")
        info.banEndTime = System.currentTimeMillis() + 1000L
        storageProvider.update(info)
        Assertions.assertEquals(AuthResponse.AccountDisabled, authProvider.checkLogin("test", "testing").first)
        info.banEndTime = 0L
        storageProvider.update(info)
        Assertions.assertEquals(AuthResponse.Success, authProvider.checkLogin("test", "testing").first)
    }

    @Test fun shouldNotAllowAlreadyOnlineLogin() {
        val info = storageProvider.getAccountInfo("test")
        info.online = true
        storageProvider.update(info)
        Assertions.assertEquals(AuthResponse.AlreadyOnline, authProvider.checkLogin("test", "testing").first)
        info.online = false
        storageProvider.update(info)
    }
}
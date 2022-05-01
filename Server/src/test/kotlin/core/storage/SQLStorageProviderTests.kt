package core.storage

import core.auth.ProductionAuthenticatorTests
import org.junit.After
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.UserAccountInfo
import rs09.game.system.SystemLogger
import rs09.storage.SQLStorageProvider
import java.sql.SQLDataException

class SQLStorageProviderTests {
    companion object {
        val storage = SQLStorageProvider()
        val testAccountNames = ArrayList<String>()
        init {
            storage.configure("localhost", "global", "root", "")
            val details = UserAccountInfo.createDefault()
            details.rights = 2
            details.username = "test"
        }

        @AfterAll @JvmStatic fun cleanup() {
            SystemLogger.logInfo("Cleaning up unit test accounts")
            testAccountNames.forEach {name ->
                SystemLogger.logInfo("Removing test account $name")
                val info = UserAccountInfo.createDefault()
                info.username = name
                storage.remove(info)
            }
        }
    }

    @Test
    fun shouldReturnTrueIfUsernameExists() {
        val data = UserAccountInfo.createDefault()
        data.username = "test123123"
        data.password = "test"
        storage.store(data)
        val exists = storage.checkUsernameTaken("test")
        testAccountNames.add("test123123")
        Assertions.assertEquals(true, exists)
    }

    @Test fun shouldReturnCorrectUserData() {
        val data = UserAccountInfo.createDefault()
        data.username = "test111"
        data.password = "test"
        data.rights = 2
        storage.store(data)
        val accountInfo = storage.getAccountInfo("test111")
        testAccountNames.add("test111")
        Assertions.assertEquals(2, accountInfo.rights)
    }

    @Test fun shouldAllowStoreUserData() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "storageTest"
        userData.password = "test"
        testAccountNames.add("storageTest")
        storage.store(userData)
        val exists = storage.checkUsernameTaken("storageTest")
        Assertions.assertEquals(true, exists)
    }

    @Test fun shouldNotAllowDuplicateAccountStorage() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "bilbo111"
        userData.password = "test"
        testAccountNames.add("bilbo111")
        storage.store(userData)
        Assertions.assertThrows(SQLDataException::class.java) {
            storage.store(userData)
        }
    }

    @Test fun shouldAllowRemoveUserInfo() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "bepis"
        userData.password = "test"
        storage.store(userData)
        storage.remove(userData)
        Assertions.assertEquals(false, storage.checkUsernameTaken("bepis"))
    }

    @Test fun shouldUpdateUserData() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "borpis"
        userData.password = "test"
        testAccountNames.add("borpis")
        storage.store(userData)
        userData.credits = 2
        storage.update(userData)
        val data = storage.getAccountInfo(userData.username)
        Assertions.assertEquals(2, data.credits, "Wrong data: $data")
    }

    @Test fun shouldNotAllowStoreOrUpdateEmptyData() {
        val info = UserAccountInfo.createDefault()
        Assertions.assertThrows(IllegalStateException::class.java) {
            storage.store(info)
        }
        Assertions.assertThrows(IllegalStateException::class.java) {
            storage.update(info)
        }
        info.username = "test"
        Assertions.assertThrows(IllegalStateException::class.java) {
            storage.store(info)
        }
        Assertions.assertThrows(IllegalStateException::class.java) {
            storage.update(info)
        }
    }
}
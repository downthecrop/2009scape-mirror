package core.storage

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.auth.UserAccountInfo
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
            testAccountNames.forEach { storage.remove(UserAccountInfo.createDefault().also { info -> info.username = it })}
        }
    }

    @Test
    fun shouldReturnTrueIfUsernameExists() {
        val data = UserAccountInfo.createDefault()
        data.username = "test"
        storage.store(data)
        val exists = storage.checkUsernameTaken("test")
        testAccountNames.add("test")
        Assertions.assertEquals(true, exists)
    }

    @Test fun shouldReturnCorrectUserData() {
        val data = UserAccountInfo.createDefault()
        data.username = "test111"
        data.rights = 2
        storage.store(data)
        val accountInfo = storage.getAccountInfo("test111")
        testAccountNames.add("test111")
        Assertions.assertEquals(2, accountInfo.rights)
    }

    @Test fun shouldAllowStoreUserData() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "storageTest"
        testAccountNames.add("storageTest")
        storage.store(userData)
        val exists = storage.checkUsernameTaken("storageTest")
        Assertions.assertEquals(true, exists)
    }

    @Test fun shouldNotAllowDuplicateAccountStorage() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "bilbo111"
        testAccountNames.add("bilbo111")
        storage.store(userData)
        Assertions.assertThrows(SQLDataException::class.java) {
            storage.store(userData)
        }
    }

    @Test fun shouldAllowRemoveUserInfo() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "bepis"
        storage.store(userData)
        storage.remove(userData)
        Assertions.assertEquals(false, storage.checkUsernameTaken("bepis"))
    }

    @Test fun shouldUpdateUserData() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "borpis"
        testAccountNames.add("borpis")
        storage.store(userData)
        userData.rights = 2
        storage.update(userData)
        val data = storage.getAccountInfo(userData.username)
        Assertions.assertEquals(2, data.rights)
    }
}
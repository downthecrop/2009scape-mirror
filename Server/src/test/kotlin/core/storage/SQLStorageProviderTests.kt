package core.storage

import core.auth.ProductionAuthenticatorTests
import org.junit.After
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import rs09.ServerConstants
import rs09.auth.UserAccountInfo
import rs09.game.system.SystemLogger
import rs09.storage.SQLStorageProvider
import java.sql.SQLDataException

class SQLStorageProviderTests {
    companion object {
        val storage = SQLStorageProvider()
        val testAccountNames = ArrayList<String>()
        init {
            storage.configure("localhost", ServerConstants.DATABASE_NAME!!, ServerConstants.DATABASE_USER!!, ServerConstants.DATABASE_PASS!!)
            val details = UserAccountInfo.createDefault()
            details.rights = 2
            details.username = "test"
        }

        @AfterAll @JvmStatic fun cleanup() {
            SystemLogger.logInfo(this::class.java, "Cleaning up unit test accounts")
            testAccountNames.forEach {name ->
                SystemLogger.logInfo(this::class.java, "Removing test account $name")
                val info = UserAccountInfo.createDefault()
                info.username = name
                storage.remove(info)
            }
        }
    }

    @Test fun shouldReturnCorrectUserData() {
        val data = UserAccountInfo.createDefault()
        data.username = "test111"
        data.password = "test"
        data.rights = 2
        testAccountNames.add("test111")
        storage.store(data)
        val accountInfo = storage.getAccountInfo("test111")
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
        testAccountNames.add("bepis")
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

    @Test fun shouldSetDefaultValuesWhenDBFieldIsNull() {
        val defaultData = UserAccountInfo.createDefault()

        //manually insert a definitely-mostly-null entry into the DB
        val conn = storage.getConnection()
        conn.use {
            val stmt = conn.prepareStatement("INSERT INTO members (username) VALUES (?);")
            stmt.setString(1, "nulltestacc")
            testAccountNames.add("nulltestacc")
            stmt.execute()
        }

        var data: UserAccountInfo = UserAccountInfo.createDefault()
        Assertions.assertDoesNotThrow {
            data = storage.getAccountInfo("nulltestacc")
        }

        Assertions.assertEquals(defaultData.password, data.password)
        Assertions.assertEquals(defaultData.rights, data.rights)
        Assertions.assertEquals(defaultData.credits, data.credits)
        Assertions.assertEquals(defaultData.ip, data.ip)
        Assertions.assertEquals(defaultData.lastUsedIp, data.lastUsedIp)
        Assertions.assertEquals(defaultData.muteEndTime, data.muteEndTime)
        Assertions.assertEquals(defaultData.banEndTime, data.banEndTime)
        Assertions.assertEquals(defaultData.contacts, data.contacts)
        Assertions.assertEquals(defaultData.blocked, data.blocked)
        Assertions.assertEquals(defaultData.clanName, data.clanName)
        Assertions.assertEquals(defaultData.currentClan, data.currentClan)
        Assertions.assertEquals(defaultData.clanReqs, data.clanReqs)
        Assertions.assertEquals(defaultData.timePlayed, data.timePlayed)
        Assertions.assertEquals(defaultData.lastLogin, data.lastLogin)
        Assertions.assertEquals(defaultData.online, data.online)
    }

    @Test fun updatingPropertiesOnTheDatabaseEndShouldBePreservedWhenFetchingAccountInfo() {
        val conn = storage.getConnection()
        conn.use {
            val stmt = conn.prepareStatement("INSERT INTO members (username) VALUES (?);")
            stmt.setString(1, "dbupdateacc")
            testAccountNames.add("dbupdateacc")
            stmt.execute()
            stmt.close()

            val stmt2 = conn.prepareStatement("UPDATE members SET rights = 2 WHERE username = \"dbupdateacc\";")
            stmt2.execute()
        }

        val info = storage.getAccountInfo("dbupdateacc")
        Assertions.assertEquals(2, info.rights)
        info.rights = 1
        storage.update(info)

        val info2 = storage.getAccountInfo("dbupdateacc")
        Assertions.assertEquals(1, info2.rights)

        val conn2 = storage.getConnection()
        conn2.use {
            val stmt = conn2.prepareStatement("UPDATE members SET rights = 2 WHERE username = \"dbupdateacc\";")
            stmt.execute()
        }

        val info3 = storage.getAccountInfo("dbupdateacc")
        Assertions.assertEquals(2, info3.rights)
    }

    @Test fun shouldCorrectlyUpdateMultipleChangedValues() {
        val userData = UserAccountInfo.createDefault()
        userData.username = "borpis2"
        userData.password = "test"
        testAccountNames.add("borpis2")
        storage.store(userData)

        val lastLogin = System.currentTimeMillis()

        userData.credits = 2
        userData.lastLogin = lastLogin
        userData.currentClan = "3009scape"
        storage.update(userData)

        val data = storage.getAccountInfo(userData.username)
        Assertions.assertEquals(2, data.credits, "Wrong data: $data")
        Assertions.assertEquals(lastLogin, data.lastLogin, "Wrong data: $data")
        Assertions.assertEquals("3009scape", data.currentClan, "Wrong data: $data")
    }
}

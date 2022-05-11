package rs09.storage

import rs09.auth.UserAccountInfo
import java.lang.Long.max
import java.sql.*

class SQLStorageProvider : AccountStorageProvider {
    var connectionString = ""
    var connectionUsername = ""
    var connectionPassword = ""

    fun getConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(connectionString, connectionUsername, connectionPassword)
    }

    fun configure(host: String, databaseName: String, username: String, password: String) {
        connectionString = "jdbc:mysql://$host/$databaseName?useTimezone=true&serverTimezone=UTC"
        connectionUsername = username
        connectionPassword = password
    }

    override fun checkUsernameTaken(username: String): Boolean {
        val conn = getConnection()
        conn.use {
            val compiledUsernameQuery = it.prepareStatement(usernameQuery)
            compiledUsernameQuery.setString(1, username.toLowerCase())
            val result = compiledUsernameQuery.executeQuery()
            val exists = result.next()
            result.close()
            return exists
        }
    }

    override fun getAccountInfo(username: String): UserAccountInfo {
        val conn = getConnection()
        conn.use { con ->
            val compiledAccountInfoQuery = con.prepareStatement(accountInfoQuery)
            compiledAccountInfoQuery.setString(1, username.toLowerCase())
            val result = compiledAccountInfoQuery.executeQuery()
            if (result.next()) {
                val userData = UserAccountInfo.createDefault()
                userData.username = username

                result.getString(2) ?.let { userData.password = it }
                result.getInt(3)     .let { userData.uid = it }
                result.getInt(4)     .let { userData.rights = it }
                result.getInt(5)     .let { userData.credits = it }
                result.getString(6) ?.let { userData.ip = it }
                result.getString(7) ?.let { userData.lastUsedIp = it }
                result.getLong(8)    .let { userData.muteEndTime = max(0L, it) }
                result.getLong(9)    .let { userData.banEndTime = max(0L, it) }
                result.getString(10)?.let { userData.contacts = it }
                result.getString(11)?.let { userData.blocked = it }
                result.getString(12)?.let { userData.clanName = it }
                result.getString(13)?.let { userData.currentClan = it }
                result.getString(14)?.let { userData.clanReqs = it }
                result.getLong(15)   .let { userData.timePlayed = max(0L, it) }
                result.getLong(16)   .let { userData.lastLogin = max(0L, it) }
                result.getBoolean(17).let { userData.online = it }

                return userData
            } else {
                return UserAccountInfo.createDefault()
            }
        }
    }

    override fun store(info: UserAccountInfo) {
        val conn = getConnection()
        conn.use {
            val compiledInsertInfoQuery = it.prepareStatement(insertInfoQuery, Statement.RETURN_GENERATED_KEYS)
            val emptyInfo = UserAccountInfo.createDefault()
            if (info == emptyInfo) {
                throw IllegalStateException("Tried to store empty data!")
            }
            emptyInfo.username = info.username
            if (info == emptyInfo) {
                throw IllegalStateException("Tried to store empty data!")
            }

            if (checkUsernameTaken(info.username)) {
                throw SQLDataException("Account already exists!")
            }
            compiledInsertInfoQuery.setString(1, info.username)
            compiledInsertInfoQuery.setString(2, info.password)
            compiledInsertInfoQuery.setInt(3, info.rights)
            compiledInsertInfoQuery.setInt(4, info.credits)
            compiledInsertInfoQuery.setString(5, info.ip)
            compiledInsertInfoQuery.setString(6, info.ip)
            compiledInsertInfoQuery.setLong(7, info.muteEndTime)
            compiledInsertInfoQuery.setLong(8, info.banEndTime)
            compiledInsertInfoQuery.setString(9, info.contacts)
            compiledInsertInfoQuery.setString(10, info.blocked)
            compiledInsertInfoQuery.setString(11, info.clanName)
            compiledInsertInfoQuery.setString(12, info.currentClan)
            compiledInsertInfoQuery.setString(13, info.clanReqs)
            compiledInsertInfoQuery.setLong(14, info.timePlayed)
            compiledInsertInfoQuery.setLong(15, info.lastLogin)
            compiledInsertInfoQuery.setBoolean(16, info.online)
            compiledInsertInfoQuery.execute()
            val result = compiledInsertInfoQuery.generatedKeys
            if (result.next()) {
                info.uid = result.getInt(1)
            }
        }
    }

    override fun update(info: UserAccountInfo) {
        val conn = getConnection()
        conn.use {
            val compiledUpdateInfoQuery = it.prepareStatement(updateInfoQuery)
            val emptyInfo = UserAccountInfo.createDefault()
            if (info == emptyInfo) {
                throw IllegalStateException("Tried to store empty data!")
            }
            emptyInfo.username = info.username
            if (info == emptyInfo) {
                throw IllegalStateException("Tried to store empty data!")
            }
            emptyInfo.password = info.password
            if (info == emptyInfo) {
                throw IllegalStateException("Tried to store empty data!")
            }

            compiledUpdateInfoQuery.setString(1, info.username)
            compiledUpdateInfoQuery.setString(2, info.password)
            compiledUpdateInfoQuery.setInt(3, info.rights)
            compiledUpdateInfoQuery.setInt(4, info.credits)
            compiledUpdateInfoQuery.setString(5, info.ip)
            compiledUpdateInfoQuery.setLong(6, info.muteEndTime)
            compiledUpdateInfoQuery.setLong(7, info.banEndTime)
            compiledUpdateInfoQuery.setString(8, info.contacts)
            compiledUpdateInfoQuery.setString(9, info.blocked)
            compiledUpdateInfoQuery.setString(10, info.clanName)
            compiledUpdateInfoQuery.setString(11, info.currentClan)
            compiledUpdateInfoQuery.setString(12, info.clanReqs)
            compiledUpdateInfoQuery.setLong(13, info.timePlayed)
            compiledUpdateInfoQuery.setLong(14, info.lastLogin)
            compiledUpdateInfoQuery.setBoolean(15, info.online)
            compiledUpdateInfoQuery.setInt(16, info.uid)
            compiledUpdateInfoQuery.execute()
        }
    }

    override fun remove(info: UserAccountInfo) {
        val conn = getConnection()
        conn.use {
            val compiledRemoveInfoQuery = it.prepareStatement(removeInfoQuery)
            compiledRemoveInfoQuery.setString(1, info.username)
            compiledRemoveInfoQuery.execute()
        }
    }

    companion object {
        private const val usernameQuery = "SELECT username FROM members WHERE username = ?;"
        private const val removeInfoQuery = "DELETE FROM members WHERE username = ?;"
        private const val accountInfoQuery = "SELECT " +
                "username," +
                "password," +
                "UID," +
                "rights," +
                "credits," +
                "ip," +
                "lastGameIp," +
                "muteTime," +
                "banTime," +
                "contacts," +
                "blocked," +
                "clanName," +
                "currentClan," +
                "clanReqs," +
                "timePlayed," +
                "lastLogin," +
                "online" +
                " FROM members WHERE username = ?;"
        private const val insertInfoQuery = "INSERT INTO members (" +
                "username," +
                "password," +
                "rights," +
                "credits," +
                "ip," +
                "lastGameIp," +
                "muteTime," +
                "banTime," +
                "contacts," +
                "blocked," +
                "clanName," +
                "currentClan," +
                "clanReqs," +
                "timePlayed," +
                "lastLogin," +
                "online" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"

        private const val updateInfoQuery = "UPDATE members SET " +
                "username = ?," +
                "password = ?," +
                "rights = ?," +
                "credits = ?," +
                "lastGameIp = ?," +
                "muteTime = ?," +
                "banTime = ?," +
                "contacts = ?," +
                "blocked = ?," +
                "clanName = ?," +
                "currentClan = ?," +
                "clanReqs = ?," +
                "timePlayed = ?," +
                "lastLogin = ?," +
                "online = ?" +
                " WHERE uid = ?;"
    }
}

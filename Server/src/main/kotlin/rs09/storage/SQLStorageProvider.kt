package rs09.storage

import rs09.auth.UserAccountInfo
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLDataException

class SQLStorageProvider : AccountStorageProvider {
    lateinit var connection: Connection
    lateinit var compiledUsernameQuery: PreparedStatement
    lateinit var compiledAccountInfoQuery: PreparedStatement
    lateinit var compiledInsertInfoQuery: PreparedStatement
    lateinit var compiledRemoveInfoQuery: PreparedStatement
    lateinit var compiledUpdateInfoQuery: PreparedStatement

    fun configure(host: String, databaseName: String, username: String, password: String) {
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection("jdbc:mysql://$host/$databaseName?useTimezone=true&serverTimezone=UTC", username, password)
        compiledUsernameQuery = connection.prepareStatement("SELECT username FROM members WHERE username = ?;")
        compiledRemoveInfoQuery = connection.prepareStatement("DELETE FROM members WHERE username = ?;")
        compiledAccountInfoQuery = connection.prepareStatement("SELECT " +
                "username," +
                "password," +
                "UID," +
                "rights," +
                "credits," +
                "icon," +
                "ip," +
                "lastGameIp," +
                "computerName," +
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
                " FROM members WHERE username = ?;")
        compiledInsertInfoQuery = connection.prepareStatement("INSERT INTO members (" +
                "username," +
                "password," +
                "rights," +
                "credits," +
                "icon," +
                "ip," +
                "lastGameIp," +
                "computerName," +
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
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);")
        compiledUpdateInfoQuery = connection.prepareStatement("UPDATE members SET " +
                "username = ?," +
                "password = ?," +
                "rights = ?," +
                "credits = ?," +
                "icon = ?," +
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
        )
    }

    override fun checkUsernameTaken(username: String): Boolean {
        compiledUsernameQuery.setString(1, username.toLowerCase())
        val result = compiledUsernameQuery.executeQuery()
        val exists = result.next()
        result.close()
        return exists
    }

    override fun getAccountInfo(username: String): UserAccountInfo {
        compiledAccountInfoQuery.setString(1, username.toLowerCase())
        val result = compiledAccountInfoQuery.executeQuery()
        if (result.next()) {
            return UserAccountInfo(
                username,
                result.getString(2),
                result.getInt(3),
                result.getInt(4),
                result.getInt(5),
                result.getInt(6),
                result.getString(7),
                result.getString(8),
                result.getString(9),
                result.getLong(10),
                result.getLong(11),
                result.getString(12),
                result.getString(13),
                result.getString(14),
                result.getString(15),
                result.getString(16),
                result.getLong(17),
                result.getLong(18),
                result.getBoolean(19)
            )
        } else {
            return UserAccountInfo.createDefault()
        }
    }

    override fun store(info: UserAccountInfo) {
        val emptyInfo = UserAccountInfo.createDefault()
        if(info == emptyInfo) {
            throw IllegalStateException("Tried to store empty data!")
        }
        emptyInfo.username = info.username
        if(info == emptyInfo) {
            throw IllegalStateException("Tried to store empty data!")
        }

        if (checkUsernameTaken(info.username)) {
            throw SQLDataException("Account already exists!")
        }
        compiledInsertInfoQuery.setString(1, info.username)
        compiledInsertInfoQuery.setString(2, info.password)
        compiledInsertInfoQuery.setInt(3, info.rights)
        compiledInsertInfoQuery.setInt(4, info.credits)
        compiledInsertInfoQuery.setInt(5, info.icon)
        compiledInsertInfoQuery.setString(6, info.ip)
        compiledInsertInfoQuery.setString(7, info.ip)
        compiledInsertInfoQuery.setString(8, info.computerName)
        compiledInsertInfoQuery.setLong(9, info.muteEndTime)
        compiledInsertInfoQuery.setLong(10, info.banEndTime)
        compiledInsertInfoQuery.setString(11, info.contacts)
        compiledInsertInfoQuery.setString(12, info.blocked)
        compiledInsertInfoQuery.setString(13, info.clanName)
        compiledInsertInfoQuery.setString(14, info.currentClan)
        compiledInsertInfoQuery.setString(15, info.clanReqs)
        compiledInsertInfoQuery.setLong(16, info.timePlayed)
        compiledInsertInfoQuery.setLong(17, info.lastLogin)
        compiledInsertInfoQuery.setBoolean(18, info.online)
        compiledInsertInfoQuery.execute()
    }

    override fun update(info: UserAccountInfo) {
        val emptyInfo = UserAccountInfo.createDefault()
        if(info == emptyInfo) {
            throw IllegalStateException("Tried to store empty data!")
        }
        emptyInfo.username = info.username
        if(info == emptyInfo) {
            throw IllegalStateException("Tried to store empty data!")
        }
        emptyInfo.password = info.password
        if(info == emptyInfo) {
            throw IllegalStateException("Tried to store empty data!")
        }

        compiledUpdateInfoQuery.setString(1, info.username)
        compiledUpdateInfoQuery.setString(2, info.password)
        compiledUpdateInfoQuery.setInt(3, info.rights)
        compiledUpdateInfoQuery.setInt(4, info.credits)
        compiledUpdateInfoQuery.setInt(5, info.credits)
        compiledUpdateInfoQuery.setString(6, info.ip)
        compiledUpdateInfoQuery.setLong(7, info.muteEndTime)
        compiledUpdateInfoQuery.setLong(8, info.banEndTime)
        compiledUpdateInfoQuery.setString(9, info.contacts)
        compiledUpdateInfoQuery.setString(10, info.blocked)
        compiledUpdateInfoQuery.setString(11, info.clanName)
        compiledUpdateInfoQuery.setString(12, info.currentClan)
        compiledUpdateInfoQuery.setString(13, info.clanReqs)
        compiledUpdateInfoQuery.setLong(14, info.timePlayed)
        compiledUpdateInfoQuery.setLong(15, info.lastLogin)
        compiledUpdateInfoQuery.setBoolean(16, info.online)
        compiledUpdateInfoQuery.setInt(17, info.uid)
        compiledUpdateInfoQuery.execute()
    }

    override fun remove(info: UserAccountInfo) {
        compiledRemoveInfoQuery.setString(1, info.username)
        compiledRemoveInfoQuery.execute()
    }
}

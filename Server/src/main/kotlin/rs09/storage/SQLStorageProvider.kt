package rs09.storage

import rs09.auth.UserAccountInfo
import java.sql.*

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
                " FROM members WHERE username = ?;")
        compiledInsertInfoQuery = connection.prepareStatement("INSERT INTO members (" +
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
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS)
        compiledUpdateInfoQuery = connection.prepareStatement("UPDATE members SET " +
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
                result.getString(6),
                result.getString(7),
                result.getLong(8),
                result.getLong(9),
                result.getString(10),
                result.getString(11),
                result.getString(12),
                result.getString(13),
                result.getString(14),
                result.getLong(15),
                result.getLong(16),
                result.getBoolean(17)
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
        if(result.next()){
            info.uid = result.getInt(1)
        }
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

    override fun remove(info: UserAccountInfo) {
        compiledRemoveInfoQuery.setString(1, info.username)
        compiledRemoveInfoQuery.execute()
    }
}

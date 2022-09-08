package rs09.storage

import core.game.system.communication.CommunicationInfo
import rs09.auth.UserAccountInfo
import rs09.game.system.SystemLogger
import rs09.game.world.repository.Repository
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

                result.getString(2)    ?.let { userData.password = it }
                result.getInt(3)        .let { userData.uid = it }
                result.getInt(4)        .let { userData.rights = it }
                result.getInt(5)        .let { userData.credits = it }
                result.getString(6)    ?.let { userData.ip = it }
                result.getString(7)    ?.let { userData.lastUsedIp = it }
                result.getLong(8)       .let { userData.muteEndTime = max(0L, it) }
                result.getLong(9)       .let { userData.banEndTime = max(0L, it) }
                result.getString(10)   ?.let { userData.contacts = it }
                result.getString(11)   ?.let { userData.blocked = it }
                result.getString(12)   ?.let { userData.clanName = it }
                result.getString(13)   ?.let { userData.currentClan = it }
                result.getString(14)   ?.let { userData.clanReqs = it }
                result.getLong(15)      .let { userData.timePlayed = max(0L, it) }
                result.getLong(16)      .let { userData.lastLogin = max(0L, it) }
                result.getBoolean(17)   .let { userData.online = it }
                result.getTimestamp(18) .let { userData.joinDate = it ?: Timestamp(System.currentTimeMillis()) }

                userData.setInitialReferenceValues()
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
            compiledInsertInfoQuery.setTimestamp(17, info.joinDate)
            compiledInsertInfoQuery.execute()
            val result = compiledInsertInfoQuery.generatedKeys
            if (result.next()) {
                info.uid = result.getInt(1)
            }
            info.setInitialReferenceValues()
        }
    }

    override fun update(info: UserAccountInfo) {
        val (updatedFields, rawData) = info.getChangedFields()
        val updateQueryString = buildUpdateInfoQuery(updatedFields)
        val conn = getConnection()

        conn.use {
            val compiledUpdateInfoQuery = it.prepareStatement(updateQueryString)
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
            if (updatedFields.isEmpty()) return

            var fieldIndex = 1
            for (updatedFieldIndex in updatedFields) {
                when(val data = rawData[updatedFieldIndex]) {
                    is String -> compiledUpdateInfoQuery.setString(fieldIndex++, data)
                    is Int -> compiledUpdateInfoQuery.setInt(fieldIndex++, data)
                    is Boolean -> compiledUpdateInfoQuery.setBoolean(fieldIndex++, data)
                    is Long -> compiledUpdateInfoQuery.setLong(fieldIndex++, data)
                }
            }
            compiledUpdateInfoQuery.setInt(fieldIndex, info.uid)
            compiledUpdateInfoQuery.execute()

            info.initialValues = rawData
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

    override fun getOnlineFriends(username: String): List<String> {
        val friends = ArrayList<String>()
        var fTokens = ""
        val conn = getConnection()
        conn.use {
            val friendsQuery = it.prepareStatement(GET_ALL_FRIENDS_QUERY)
            friendsQuery.setString(1, username)
            val f = friendsQuery.executeQuery()
            if (f.next()) {
                fTokens = f.getString(1)
            }
        }
        val contacts = CommunicationInfo.parseContacts(fTokens)
        for ((friendName, _) in contacts) if (Repository.getPlayerByName(friendName) != null) friends.add(friendName)

        return friends
    }

    override fun getUsernamesWithIP(ip: String): List<String> {
        val conn = getConnection()
        val res = ArrayList<String>()
        conn.use {
            val query = it.prepareStatement(accountsByIPQuery)
            query.setString(1, ip)
            val r = query.executeQuery()
            while (r.next()) {
                res.add(r.getString(1))
            }
        }
        return res
    }

    companion object {
        private const val usernameQuery = "SELECT username FROM members WHERE username = ?;"
        private const val removeInfoQuery = "DELETE FROM members WHERE username = ?;"
        private const val accountsByIPQuery = "SELECT username FROM members WHERE lastGameIp = ?;"
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
                "online," +
                "joined_date" +
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
                "online," +
                "joined_date" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"

        private fun buildUpdateInfoQuery(updatedIndices: ArrayList<Int>) : String {
            val sb = StringBuilder("UPDATE members SET ")
            val validIndices = updatedIndices.filter { it in UPDATE_QUERY_FIELDS.keys }
            for ((index, updatedIndex) in validIndices.withIndex()) {
                sb.append(UPDATE_QUERY_FIELDS[updatedIndex] ?: continue)
                sb.append(" = ?")
                if (index < validIndices.size - 1)
                    sb.append(",")
            }
            sb.append(" WHERE uid = ?;")
            return sb.toString()
        }

        //Maps UserAccountInfo updated field indices to the column name for SQL. Mini ORM I guess?
        private val UPDATE_QUERY_FIELDS = mapOf<Int, String>(
            0 to "username",
            1 to "password",
            3 to "rights",
            4 to "credits",
            6 to "lastGameIp",
            7 to "muteTime",
            8 to "banTime",
            9 to "contacts",
            10 to "blocked",
            11 to "clanName",
            12 to "currentClan",
            13 to "clanReqs",
            14 to "timePlayed",
            15 to "lastLogin",
            16 to "online"
        )

        private val GET_ALL_FRIENDS_QUERY = "SELECT contacts FROM players WHERE username = ?;"
    }
}

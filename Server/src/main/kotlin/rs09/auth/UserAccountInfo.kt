package rs09.auth

class UserAccountInfo(
    var username: String,
    var password: String,
    var uid: Int,
    var rights: Int,
    var credits: Int,
    var icon: Int,
    var ip: String,
    var lastUsedIp: String,
    var computerName: String,
    var muteEndTime: Long,
    var banEndTime: Long,
    var contacts: String,
    var blocked: String,
    var clanName: String,
    var currentClan: String,
    var clanReqs: String,
    var timePlayed: Long,
    var lastLogin: Long,
    var online: Boolean
) {
    companion object {
        @JvmStatic fun createDefault() : UserAccountInfo {
            return UserAccountInfo("", "", 0, 0, 0, 0, "", "", "", 0L, 0L, "", "", "", "", "", 0L, 0L, false)
        }
    }

    override fun toString(): String {
        return "USER:$username,PASS:$password,UID:$uid,RIGHTS:$rights,CREDITS:$credits,ICON:$icon,IP:$ip,LASTIP:$lastUsedIp"
    }
}
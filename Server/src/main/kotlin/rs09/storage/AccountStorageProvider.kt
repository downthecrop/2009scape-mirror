package rs09.storage

import rs09.auth.UserAccountInfo

interface AccountStorageProvider {
    fun checkUsernameTaken(username: String): Boolean
    fun getAccountInfo(username: String): UserAccountInfo
    fun getUsernamesWithIP(ip: String) : List<String>
    fun store(info: UserAccountInfo)
    fun update(info: UserAccountInfo)
    fun remove(info: UserAccountInfo)
    fun getOnlineFriends(username: String) : List<String>
}

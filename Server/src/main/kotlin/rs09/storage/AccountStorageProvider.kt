package rs09.storage

import rs09.auth.UserAccountInfo

interface AccountStorageProvider {
    fun checkUsernameTaken(username: String): Boolean
    fun getAccountInfo(username: String): UserAccountInfo
    fun store(info: UserAccountInfo)
}

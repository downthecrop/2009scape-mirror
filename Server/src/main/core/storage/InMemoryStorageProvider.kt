package core.storage

import core.auth.UserAccountInfo

class InMemoryStorageProvider : AccountStorageProvider {
    private val storage = HashMap<String, UserAccountInfo>()

    override fun checkUsernameTaken(username: String): Boolean {
        return storage[username] != null
    }

    override fun getAccountInfo(username: String): UserAccountInfo {
        return storage[username] ?: UserAccountInfo.createDefault().also { it.uid = username.hashCode(); storage[username] = it }
    }

    override fun store(info: UserAccountInfo) {
        info.uid = info.username.hashCode()
        storage[info.username] = info
    }

    override fun update(info: UserAccountInfo) {
        storage[info.username] = info
    }

    override fun remove(info: UserAccountInfo) {
        storage.remove(info.username)
    }

    override fun getOnlineFriends(username: String): List<String> {
        return ArrayList()
    }

    override fun getUsernamesWithIP(ip: String): List<String> {
        return ArrayList()
    }
}

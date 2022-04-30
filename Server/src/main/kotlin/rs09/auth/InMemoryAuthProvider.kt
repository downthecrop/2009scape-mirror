package rs09.auth

import rs09.storage.InMemoryStorageProvider

class InMemoryAuthProvider : AuthProvider<InMemoryStorageProvider>() {
    override fun configureFor(provider: InMemoryStorageProvider) {
        storageProvider = provider
    }

    override fun checkLogin(username: String, password: String): LoginResponse {
        val info = storageProvider.getAccountInfo(username)
        if(info.online || info.password != password || info.username != username) return LoginResponse.CouldNotLogin
        return LoginResponse.Success
    }

    override fun createAccountWith(info: UserAccountInfo): Boolean {
        storageProvider.store(info)
        return true
    }
}

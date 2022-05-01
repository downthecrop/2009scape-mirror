package rs09.auth

import rs09.storage.InMemoryStorageProvider

class DevelopmentAuthenticator : AuthProvider<InMemoryStorageProvider>() {
    override fun configureFor(provider: InMemoryStorageProvider) {
        storageProvider = provider
    }

    override fun checkLogin(username: String, password: String): LoginResponse {
        val info = storageProvider.getAccountInfo(username.toLowerCase())
        if(info.online || info.password != password || info.username != username.toLowerCase()) return LoginResponse.CouldNotLogin
        return LoginResponse.Success
    }

    override fun createAccountWith(info: UserAccountInfo): Boolean {
        info.username = info.username.toLowerCase()
        info.rights = 2
        storageProvider.store(info)
        return true
    }
}

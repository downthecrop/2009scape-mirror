package rs09.auth

import rs09.storage.AccountStorageProvider

class ProductionAuthenticator : AuthProvider<AccountStorageProvider>() {
    override fun configureFor(provider: AccountStorageProvider) {
        storageProvider = provider
    }

    override fun createAccountWith(info: UserAccountInfo): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkLogin(username: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }
}
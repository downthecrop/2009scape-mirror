package rs09.game.system

import rs09.ServerConstants
import rs09.auth.AuthProvider
import rs09.auth.DevelopmentAuthenticator
import rs09.auth.ProductionAuthenticator
import rs09.storage.AccountStorageProvider
import rs09.storage.InMemoryStorageProvider
import rs09.storage.SQLStorageProvider

object Auth {
    lateinit var authenticator: AuthProvider<*>
    lateinit var storageProvider: AccountStorageProvider

    fun configure() {
        storageProvider = if (ServerConstants.PERSIST_ACCOUNTS)
            SQLStorageProvider()
        else
            InMemoryStorageProvider()

        authenticator = if (ServerConstants.USE_AUTH)
            ProductionAuthenticator().also { it.configureFor(storageProvider) }
        else
            DevelopmentAuthenticator().also { it.configureFor(storageProvider) }
    }
}
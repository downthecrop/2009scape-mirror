package rs09.game.system

import rs09.auth.AuthProvider
import rs09.auth.DevelopmentAuthenticator
import rs09.auth.ProductionAuthenticator
import rs09.storage.AccountStorageProvider
import rs09.storage.InMemoryStorageProvider
import rs09.storage.SQLStorageProvider

object Auth {
    lateinit var authenticator: AuthProvider<*>
    lateinit var storageProvider: AccountStorageProvider

    fun configureFor(devMode: Boolean) {
        if (devMode) {
            authenticator = DevelopmentAuthenticator()
            storageProvider = InMemoryStorageProvider()
            (authenticator as DevelopmentAuthenticator).configureFor(storageProvider as InMemoryStorageProvider)
            SystemLogger.logInfo("[AUTH] Using Development Authenticator with In-Memory Storage")
        } else {
            authenticator = ProductionAuthenticator()
            storageProvider = SQLStorageProvider()
            (authenticator as ProductionAuthenticator).configureFor(storageProvider as SQLStorageProvider)
            SystemLogger.logInfo("[AUTH] Using Production Authenticator with SQL Storage")
        }
    }
}
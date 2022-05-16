package rs09.auth

import core.game.node.entity.player.Player
import rs09.storage.InMemoryStorageProvider

class DevelopmentAuthenticator : AuthProvider<InMemoryStorageProvider>() {
    override fun configureFor(provider: InMemoryStorageProvider) {
        storageProvider = provider
    }

    override fun checkLogin(username: String, password: String): Pair<AuthResponse, UserAccountInfo?> {
        val info: UserAccountInfo
        if(!storageProvider.checkUsernameTaken(username.toLowerCase())) {
            info = UserAccountInfo.createDefault()
            info.username = username
            createAccountWith(info)
        } else {
            info = storageProvider.getAccountInfo(username.toLowerCase())
        }
        return Pair(AuthResponse.Success, storageProvider.getAccountInfo(username))
    }

    override fun createAccountWith(info: UserAccountInfo): Boolean {
        info.username = info.username.toLowerCase()
        info.rights = 2
        storageProvider.store(info)
        return true
    }

    override fun checkPassword(player: Player, password: String): Boolean {
        return password == player.details.password
    }

    override fun updatePassword(username: String, newPassword: String) {
        val info = storageProvider.getAccountInfo(username)
        info.password = newPassword
        storageProvider.update(info)
    }
}

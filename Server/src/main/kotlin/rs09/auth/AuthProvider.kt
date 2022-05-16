package rs09.auth

import core.game.node.entity.player.Player
import rs09.storage.AccountStorageProvider

abstract class AuthProvider<T: AccountStorageProvider> {
    lateinit var storageProvider: T

    abstract fun configureFor(provider: T)

    fun canCreateAccountWith(info: UserAccountInfo) : Boolean {
        return !storageProvider.checkUsernameTaken(info.username)
    }

    abstract fun createAccountWith(info: UserAccountInfo) : Boolean

    abstract fun checkLogin(username: String, password: String) : Pair<AuthResponse,UserAccountInfo?>

    abstract fun checkPassword(player: Player, password: String) : Boolean

    abstract fun updatePassword(username: String, newPassword: String)
}

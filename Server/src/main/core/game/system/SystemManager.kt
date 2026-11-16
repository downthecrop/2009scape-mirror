package core.game.system

import core.ServerConstants
import core.api.delayWorldScript
import core.api.queueWorldScript
import core.api.sendNews
import core.game.system.security.EncryptionManager
import core.game.world.GameWorld.majorUpdateWorker
import core.game.world.repository.Repository
import core.tools.colorize
import kotlin.system.exitProcess

/**
 * Manages the "game system" states, such as updating or terminating.
 */
object SystemManager {
    /**
     * The system state.
     */
    private var state: SystemState? = SystemState.TERMINATED

    /**
     * The system update handler.
     */
    val updater: SystemUpdate = SystemUpdate()

    /**
     * The system termination handler.
     */
    @JvmStatic
    val terminator: SystemTermination = SystemTermination()

    /**
     * The system configurator.
     */
    @JvmStatic
    val systemConfig: SystemConfig = SystemConfig()

    /**
     * The encryption the server uses.
     */
    val encryption: EncryptionManager = EncryptionManager()

    /**
     * Sets the current state and handles it accordingly.
     * @param state The system state.
     */
    @JvmStatic
    fun flag(state: SystemState) {
        if (SystemManager.state == state) {
            return
        }
        SystemManager.state = state
        when (state) {
            SystemState.ACTIVE, SystemState.PRIVATE -> majorUpdateWorker.start()
            SystemState.UPDATING -> updater.schedule()
            SystemState.TERMINATED -> terminator.terminate()
        }
    }

    val isActive: Boolean
        /**
         * Checks if the system is still active (updating keeps the system active
         * until termination).
         * @return `True` if the state does not equal
         * [SystemState.TERMINATED].
         */
        get() = state != SystemState.TERMINATED

    val isUpdating: Boolean
        /**
         * Checks if the system is being updated.
         * @return `True` if so.
         */
        get() = state == SystemState.UPDATING

    val isPrivate: Boolean
        /**
         * Checks if the system is private, so only developers can connect.
         * @return `True` if so.
         */
        get() = state == SystemState.PRIVATE

    @JvmStatic
    val isTerminated: Boolean
        /**
         * Checks if the system has been terminated.
         * @return `True` if so.
         */
        get() = state == SystemState.TERMINATED

    fun dailyRestart() {
        if (ServerConstants.DAILY_RESTART) {
            for (player in Repository.players.filter { !it.isArtificial }) {
                player.packetDispatch.sendSystemUpdate(500)
            }
            sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN 5 MINUTES!"))
            ServerConstants.DAILY_RESTART = false
            queueWorldScript(100) { stage ->
                for (player in Repository.players.filter { !it.isArtificial }) {
                    player.packetDispatch.sendSystemUpdate((4 - stage) * 100)
                }
                if (stage == 4) {
                    exitProcess(0)
                }
                sendNews(colorize("%RSERVER GOING DOWN FOR DAILY RESTART IN ${4 - stage} MINUTE${if (stage < 3) "S" else ""}!"))
                return@queueWorldScript delayWorldScript(100)
            }
        }
    }
}

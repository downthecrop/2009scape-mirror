package core.api

/**
 * An interface for writing content that allows the class to execute code as the server is shutting down
 */
interface ShutdownListener : ContentInterface {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     */
    fun shutdown()
}
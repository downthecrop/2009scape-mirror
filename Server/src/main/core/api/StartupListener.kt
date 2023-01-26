package core.api

/**
 * An interface for writing content that allows the class to execute code when the server is started.
 */
interface StartupListener : ContentInterface {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     */
    fun startup()
}
package api

interface TickListener {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     * TickListeners are generally for NON-player, WORLD tick events.
     * Examples: Fishing spot rotation, grand exchange updates, puro puro randomization, etc.
     * If you need something (player/entity)-specific, use an [api.events.EventHook] with the [api.events.TickEvent]
     */
    fun tick()
}
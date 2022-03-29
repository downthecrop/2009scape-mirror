package api

interface TickListener {
    /**
     * NOTE: This should NOT reference nonstatic class-local variables.
     */
    fun tick()
}
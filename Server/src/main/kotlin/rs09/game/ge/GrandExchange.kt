package rs09.game.ge

import core.game.world.callback.CallBack
import rs09.game.system.SystemLogger
import rs09.tools.secondsToTicks

object GrandExchange : CallBack {
    /**
     * Fallback safety check to make sure we don't start the GE twice under any circumstance
     */
    var isRunning = false

    /**
     * Initializes the offer manager and spawns an update thread.
     * @param local whether or not the GE should be the local in-code server rather than some hypothetical remote implementation.
     */
    fun boot(local: Boolean){
        if(isRunning) return

        if(!local){
            TODO("Remote GE server stuff")
        }

        SystemLogger.logGE("Initializing GE...")
        OfferManager.init()
        SystemLogger.logGE("GE Initialized.")

        SystemLogger.logGE("Initializing GE Update Worker")

        val t = Thread {
            Thread.currentThread().name = "GE Update Worker"
            while(true) {
                SystemLogger.logGE("Updating offers...")
                OfferManager.update()
                if(OfferManager.dumpDatabase){
                    SystemLogger.logGE("Saving GE...")
                    OfferManager.save()
                    OfferManager.dumpDatabase = false
                }
                Thread.sleep(60_000) //sleep for 60 seconds
            }
        }.start()

        isRunning = true
    }

    override fun call(): Boolean {
        boot(true)
        return true
    }
}
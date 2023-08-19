package content.global.activity.penguinhns

import core.ServerStore
import core.api.StartupListener
import core.api.log
import core.plugin.ClassScanner
import core.tools.Log
import org.json.simple.JSONObject

class PenguinHNSEvent : StartupListener {
    val manager = PenguinManager()

    override fun startup() {
        manager.rebuildVars()
        ClassScanner.definePlugins(LarryHandler(), NotebookHandler())
        log(this::class.java, Log.FINE, "Penguin HNS initialized.")
    }

    companion object {
        @JvmStatic
        fun getStoreFile() : JSONObject {
            return ServerStore.getArchive("weekly-penguinhns")
        }
    }

}
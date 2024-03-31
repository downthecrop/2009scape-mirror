package core.game.worldevents

import core.ServerStore
import core.api.ContentInterface
import core.plugin.ClassScanner
import core.plugin.Plugin
import core.tools.Log
import org.json.simple.JSONObject
import java.util.*

/**
 * The class other world events should extend off of.
 * @author Ceikry
 */
abstract class WorldEvent(var name: String) : ContentInterface {
    var plugins = PluginSet()

    /**
     * if the event is active or not. Can be used to check dates or just always return true
     * whatever you need for this specific event
     */
    open fun checkActive(cal: Calendar): Boolean {
        return false
    }

    /**
     * Used to initialize the event
     * The WorldEventInitializer runs this if checkActive() returns true.
     */
    open fun initEvent(){
    }

    /**
     * Used to log world event messages in a standard and organized way.
     */
    fun log(message: String){
        core.api.log(this::class.java, Log.FINE,  "[World Events($name)] $message")
    }
}


/**
 * A class that holds a set of plugins that shouldn't be initialized by default.
 * Can be used to initialize all of its plugins cleanly.
 */
class PluginSet(vararg val plugins: Plugin<*>){
    val set = ArrayList(plugins.asList())
    fun initialize() {
        ClassScanner.definePlugins(*set.toTypedArray())
    }
    fun add(plugin: Plugin<*>){
        set.add(plugin)
    }
}

/**
 * Static object for storing instances of loaded events.
 */
object WorldEvents {
    private var events = hashMapOf<String, WorldEvent>()

    fun add(event: WorldEvent){
        events[event.name.lowercase(Locale.getDefault())] = event
    }

    fun get(name: String): WorldEvent?{
        return events[name.lowercase(Locale.getDefault())]
    }

    fun getArchive() : JSONObject {
        return ServerStore.getArchive("world-event-status")
    }
}
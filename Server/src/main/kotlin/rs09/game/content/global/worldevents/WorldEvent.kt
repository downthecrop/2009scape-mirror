package rs09.game.content.global.worldevents

import rs09.game.system.SystemLogger
import core.plugin.Plugin
import rs09.plugin.ClassScanner
import java.util.*

/**
 * The class other world events should extend off of.
 * @author Ceikry
 */
open class WorldEvent(var name: String) {
    var plugins = PluginSet()

    /**
     * if the event is active or not. Can be used to check dates or just always return true
     * whatever you need for this specific event
     */
    open fun checkActive(): Boolean {
        return false
    }

    /**
     * Check to see if the event should trigger. An event trigger could spawn shooting stars, or a world boss
     * or whatever kind of whacky shit you need it to do.
     */
    open fun checkTrigger(): Boolean{
        return true
    }

    /**
     * Used to initialize the event, which loads all associated plugins.
     * The WorldEventInitializer runs this if checkActive() returns true.
     */
    open fun initialize(){
        plugins.initialize()
    }

    /**
     * Used to log world event messages in a standard and organized way.
     */
    fun log(message: String){
        SystemLogger.logInfo("[World Events($name)] $message")
    }

    /**
     * Used to start, or "fire", world events.
     */
    open fun fireEvent() {}
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
    private var events = hashMapOf<String,WorldEvent>()

    fun add(event: WorldEvent){
        events.put(event.name.toLowerCase(),event)
    }

    fun get(name: String): WorldEvent?{
        return events.get(name.toLowerCase())
    }
}
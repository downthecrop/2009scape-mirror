package rs09.plugin

import core.game.content.activity.ActivityManager
import core.game.content.activity.ActivityPlugin
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.plugin.Plugin
import core.plugin.PluginManifest
import core.plugin.PluginType
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InterfaceListener
import rs09.game.node.entity.skill.magic.SpellListener
import rs09.game.system.SystemLogger
import rs09.game.system.command.Command
import java.util.*
import java.util.function.Consumer

/**
 * Represents a class used to handle the loading of all plugins.
 * @author Ceikry
 */
object PluginManager {
    var disabledPlugins = HashMap<String, Boolean>()
    /**
     * The amount of plugins loaded.
     */
    var amountLoaded = 0
        private set

    /**
     * The currently loaded plugin names.
     */
    private var loadedPlugins: MutableList<String>? = ArrayList()

    /**
     * The last loaded plugin.
     */
    private val lastLoaded: String? = null

    /**
     * Initializes the plugin manager.
     */
	@JvmStatic
	fun init() {
        try {
            load()
            loadedPlugins!!.clear()
            loadedPlugins = null
            SystemLogger.logInfo("Initialized $amountLoaded plugins...")
        } catch (t: Throwable) {
            SystemLogger.logErr("Error initializing Plugins -> " + t.localizedMessage + " for file -> " + lastLoaded)
            t.printStackTrace()
        }
    }

    fun load() {
        var result = ClassGraph().enableClassInfo().enableAnnotationInfo().scan()
        result.getClassesWithAnnotation("core.plugin.Initializable").forEach(Consumer { p: ClassInfo ->
            try {
                definePlugin(p.loadClass().newInstance() as Plugin<Object>)
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        })
        result.getSubclasses("rs09.game.system.command.Command").forEach {
            try {
                definePlugin(it.loadClass().newInstance() as Plugin<Command>).also { System.out.println("Initializing $it") }
            } catch (e: Exception) {e.printStackTrace()}
        }
        result.getSubclasses("rs09.game.interaction.InteractionListener").forEach {
            val clazz = it.loadClass().newInstance() as InteractionListener
            clazz.defineListeners()
            clazz.defineDestinationOverrides()
        }
        result.getSubclasses("rs09.game.interaction.InterfaceListener").forEach {
            val clazz = it.loadClass().newInstance() as InterfaceListener
            clazz.defineListeners()
        }
        result.getSubclasses("rs09.game.node.entity.skill.magic.SpellListener").forEach {
            val clazz = it.loadClass().newInstance() as SpellListener
            clazz.defineListeners()
        }
    }

    /**
     * Defines a list of plugins.
     * @param plugins the plugins.
     */
    @JvmStatic
    fun definePlugins(vararg plugins: Plugin<*>) {
        val pluginsLength = plugins.size
        for (i in 0 until pluginsLength) {
            val p = plugins[i]
            definePlugin(p)
        }
    }

    /**
     * Defines the plugin.
     * @param plugin The plugin.
     */
	@JvmStatic
	fun definePlugin(plugin: Plugin<*>) {
        try {
            var manifest = plugin.javaClass.getAnnotation(PluginManifest::class.java)
            if (manifest == null) {
                manifest = plugin.javaClass.superclass.getAnnotation(PluginManifest::class.java)
            } else {
                if (disabledPlugins[manifest.name] != null) {
                    return
                }
            }
            if (manifest == null || manifest.type == PluginType.ACTION) {
                plugin.newInstance(null)
            } else {
                when (manifest.type) {
                    PluginType.DIALOGUE -> (plugin as DialoguePlugin).init()
                    PluginType.ACTIVITY -> ActivityManager.register(plugin as ActivityPlugin)
                    PluginType.LOGIN -> LoginConfiguration.getLoginPlugins().add(plugin as Plugin<Any?>)
                    PluginType.QUEST -> {
                        plugin.newInstance(null)
                        QuestRepository.register(plugin as Quest)
                    }
                    else -> SystemLogger.logWarn("Unknown Manifest: " + manifest.type)
                }
            }
            amountLoaded++
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

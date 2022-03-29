package rs09.plugin

import api.*
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
import rs09.game.ai.general.scriptrepository.PlayerScripts
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InterfaceListener
import rs09.game.node.entity.skill.magic.SpellListener
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.util.*
import java.util.function.Consumer

/**
 * A class used to reflectively scan the classpath and load classes
 * @author Ceikry
 */
object ClassScanner {
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
     * Scan the classpath for reflection-loaded content classes such as listeners, "plugins", etc
     */
	@JvmStatic
	fun scanAndLoad() {
        try {
            load()
            loadedPlugins!!.clear()
            loadedPlugins = null
            SystemLogger.logInfo("Initialized $amountLoaded plugins...")
        } catch (t: Throwable) {
            SystemLogger.logErr("Error initializing Plugins -> " + t.localizedMessage + " for file -> " + lastLoaded)
            t.printStackTrace()
        } catch (e: Exception) {
            SystemLogger.logErr("Error initializing Plugins -> " + e.localizedMessage + " for file -> " + lastLoaded)
            e.printStackTrace()
        }
    }

    fun load() {
        val result = ClassGraph().enableClassInfo().enableAnnotationInfo().scan()
        result.getClassesWithAnnotation("core.plugin.Initializable").forEach(Consumer { p: ClassInfo ->
            try {
                definePlugin(p.loadClass().newInstance() as Plugin<Object>)
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        })
        result.getClassesWithAnnotation("rs09.game.ai.general.scriptrepository.PlayerCompatible").forEach { res ->
            val description = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptDescription").parameterValues[0].value as Array<String>
            val identifier = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptIdentifier").parameterValues[0].value.toString()
            val name = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptName").parameterValues[0].value.toString()
            PlayerScripts.identifierMap[identifier] =
                PlayerScripts.PlayerScript(identifier, description, name, res.loadClass())
        }
        result.getClassesImplementing("api.StartupListener").filter { !it.isAbstract }.forEach {
            try {
                val clazz = it.loadClass().newInstance() as StartupListener
                GameWorld.startupListeners.add(clazz)
            } catch (e: Exception)
            {
                SystemLogger.logErr("Error loading startup listener: ${it.simpleName}, ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
        result.getClassesImplementing("api.ShutdownListener").filter { !it.isAbstract }.forEach {
            val clazz = it.loadClass().newInstance() as ShutdownListener
            GameWorld.shutdownListeners.add(clazz)
        }
        result.getClassesImplementing("api.LoginListener").filter { !it.isAbstract }.forEach {
            val clazz = it.loadClass().newInstance() as LoginListener
            GameWorld.loginListeners.add(clazz)
        }
        result.getClassesImplementing("api.LogoutListener").filter { !it.isAbstract }.forEach {
            val clazz = it.loadClass().newInstance() as LogoutListener
            GameWorld.logoutListeners.add(clazz)
        }
        result.getClassesImplementing("api.TickListener").filter { !it.isAbstract }.forEach {
            val clazz = it.loadClass().newInstance() as TickListener
            GameWorld.tickListeners.add(clazz)
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

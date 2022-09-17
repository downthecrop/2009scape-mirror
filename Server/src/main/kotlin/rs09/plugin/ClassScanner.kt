package rs09.plugin

import api.*
import core.game.content.activity.ActivityManager
import core.game.content.activity.ActivityPlugin
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.Entity
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin
import core.plugin.PluginManifest
import core.plugin.PluginType
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import rs09.game.ai.general.scriptrepository.PlayerScripts
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import rs09.game.interaction.InterfaceListener
import rs09.game.node.entity.player.info.login.PlayerSaveParser
import rs09.game.node.entity.player.info.login.PlayerSaver
import rs09.game.system.SystemLogger
import rs09.game.system.SystemLogger.logStartup
import rs09.game.world.GameWorld
import java.util.function.Consumer

/**
 * A class used to reflectively scan the classpath and load classes
 * @author Ceikry
 */
object ClassScanner {
    var disabledPlugins = HashMap<String, Boolean>()
    /**
     * The amount of content interfaces loaded.
     */
    var amountLoaded = 0
        private set

    var numPlugins = 0
        private set

    /**
     * The currently loaded plugin names.
     */
    private var loadedPlugins: MutableList<String>? = ArrayList()

    /**
     * The last loaded plugin.
     */
    private val lastLoaded: String? = null

    lateinit var scanResults: ScanResult

    @JvmStatic fun scanClasspath() {
        scanResults = ClassGraph().enableClassInfo().enableAnnotationInfo().scan()
    }

    /**
     * Scan the classpath for reflection-loaded content classes such as listeners, "plugins", etc
     */
	@JvmStatic
	fun loadPureInterfaces() {
        try {
            loadContentInterfacesFrom(scanResults)
            logStartup("Loaded $amountLoaded content interfaces.")
        } catch (t: Throwable) {
            SystemLogger.logErr(this::class.java, "Error initializing Plugins -> " + t.localizedMessage + " for file -> " + lastLoaded)
            t.printStackTrace()
        } catch (e: Exception) {
            SystemLogger.logErr(this::class.java, "Error initializing Plugins -> " + e.localizedMessage + " for file -> " + lastLoaded)
            e.printStackTrace()
        }
    }

    private fun loadContentInterfacesFrom(scanResults: ScanResult) {
        scanResults.getClassesImplementing("api.ContentInterface").filter { !it.isAbstract }.forEach {
            try {
                val clazz = it.loadClass().newInstance()
                if(clazz is LoginListener) GameWorld.loginListeners.add(clazz)
                if(clazz is LogoutListener) GameWorld.logoutListeners.add(clazz)
                if(clazz is TickListener) GameWorld.tickListeners.add(clazz)
                if(clazz is StartupListener) GameWorld.startupListeners.add(clazz)
                if(clazz is ShutdownListener) GameWorld.shutdownListeners.add(clazz)
                if(clazz is PersistWorld) GameWorld.worldPersists.add(clazz)
                if(clazz is InteractionListener) clazz.defineListeners().also { clazz.defineDestinationOverrides() }
                if(clazz is InterfaceListener) clazz.defineInterfaceListeners()
                if(clazz is Commands) clazz.defineCommands()
                if(clazz is PersistPlayer) {
                    PlayerSaver.contentHooks.add(clazz)
                    PlayerSaveParser.contentHooks.add(clazz)
                }
                if(clazz is MapArea)
                {
                    val zone = object : MapZone(clazz.javaClass.simpleName + "MapArea", true, *clazz.getRestrictions()){
                        override fun enter(e: Entity?): Boolean {
                            clazz.areaEnter(e ?: return super.enter(null))
                            return super.enter(e)
                        }

                        override fun leave(e: Entity?, logout: Boolean): Boolean {
                            clazz.areaLeave(e ?: return super.leave(null, logout), logout)
                            return super.leave(e, logout)
                        }

                        override fun move(e: Entity?, from: Location?, to: Location?): Boolean {
                            if(e != null && from != null && to != null) clazz.entityStep(e, to, from)
                            return super.move(e, from, to)
                        }
                    }
                    for(border in clazz.defineAreaBorders()) zone.register(border)
                    ZoneBuilder.configure(zone)
                    SystemLogger.logInfo(this::class.java, "Configured zone: ${clazz.javaClass.simpleName + "MapArea"}")
                    MapArea.zoneMaps[clazz.javaClass.simpleName + "MapArea"] = zone
                }
                amountLoaded++
            } catch (e: Exception) {
                SystemLogger.logErr(this::class.java, "Error loading content: ${it.simpleName}, ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun loadSideEffectfulPlugins() {
        loadedPlugins!!.clear()
        loadPluginsFrom(scanResults)
        logStartup("We still have $numPlugins legacy plugins being loaded.")
    }

    private fun loadPluginsFrom(scanResults: ScanResult) {
        scanResults.getClassesWithAnnotation("core.plugin.Initializable").forEach(Consumer { p: ClassInfo ->
            try {
                val clazz = p.loadClass().getDeclaredConstructor().newInstance()
                if (clazz is Plugin<*>) {
                    definePlugin(clazz)
                }
            } catch (t: Throwable) {
                SystemLogger.logErr(this::class.java, "Failed to load plugin ${p.name}.");

                if (t is NoSuchMethodException && p.superclass.simpleName == DialoguePlugin::class.simpleName) {
                    SystemLogger.logErr(this::class.java,
                        "Make sure the constructor signature matches " +
                        "`${p.simpleName}(player: Player? = null) : DialoguePlugin(player)'."
                    )
                }

                t.printStackTrace()
            }
        })

        scanResults.getClassesWithAnnotation("rs09.game.ai.general.scriptrepository.PlayerCompatible").forEach { res ->
            val description = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptDescription").parameterValues[0].value as Array<String>
            val identifier = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptIdentifier").parameterValues[0].value.toString()
            val name = res.getAnnotationInfo("rs09.game.ai.general.scriptrepository.ScriptName").parameterValues[0].value.toString()
            PlayerScripts.identifierMap[identifier] = PlayerScripts.PlayerScript(identifier, description, name, res.loadClass())
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
                    else -> SystemLogger.logWarn(this::class.java, "Unknown Manifest: " + manifest.type)
                }
            }
            numPlugins++
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

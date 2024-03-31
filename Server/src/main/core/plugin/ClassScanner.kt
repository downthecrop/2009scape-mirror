package core.plugin

import core.ServerConstants
import core.api.*
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.bots.PlayerScripts
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.info.login.PlayerSaveParser
import core.game.node.entity.player.info.login.PlayerSaver
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.system.timer.RSTimer
import core.game.system.timer.TimerRegistry
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.game.worldevents.WorldEvent
import core.game.worldevents.WorldEvents
import core.tools.Log
import core.tools.SystemLogger.logStartup
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
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
            log(this::class.java, Log.ERR,  "Error initializing Plugins -> " + t.localizedMessage + " for file -> " + lastLoaded)
            t.printStackTrace()
        } catch (e: Exception) {
            log(this::class.java, Log.ERR,  "Error initializing Plugins -> " + e.localizedMessage + " for file -> " + lastLoaded)
            e.printStackTrace()
        }
    }

    fun loadTimers () {
        scanResults.getSubclasses ("core.game.system.timer.RSTimer").filter { !it.isAbstract }.forEach {
            try {
                val clazz = it.loadClass().newInstance() as RSTimer
                TimerRegistry.registerTimer (clazz)
            } catch (e: Exception) {
                log(this::class.java, Log.ERR, "Error registering timer instance: ${it.simpleName}")
                e.printStackTrace()
            }
        }
    }

    private fun loadContentInterfacesFrom(scanResults: ScanResult) {
        scanResults.getClassesImplementing("core.api.ContentInterface").filter { !it.isAbstract }.forEach {
            try {
                val clazz = it.loadClass().newInstance()
                if(clazz is WorldEvent) { //Check world event first so if it's not active we don't register tick listeners, etc.
                    if (!clazz.checkActive(ServerConstants.STARTUP_MOMENT)) return@forEach
                    WorldEvents.add(clazz)
                }
                if(clazz is LoginListener) GameWorld.loginListeners.add(clazz)
                if(clazz is LogoutListener) GameWorld.logoutListeners.add(clazz)
                if(clazz is TickListener) GameWorld.tickListeners.add(clazz)
                if(clazz is StartupListener) GameWorld.startupListeners.add(clazz)
                if(clazz is ShutdownListener) GameWorld.shutdownListeners.add(clazz)
                if(clazz is PersistWorld) GameWorld.worldPersists.add(clazz)
                if(clazz is InteractionListener) clazz.defineListeners().also { clazz.defineDestinationOverrides() }
                if(clazz is InterfaceListener) clazz.defineInterfaceListeners()
                if(clazz is Commands) clazz.defineCommands()
                if(clazz is NPCBehavior) NPCBehavior.register(clazz.ids, clazz)
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
                    log(this::class.java, Log.FINE,  "Configured zone: ${clazz.javaClass.simpleName + "MapArea"}")
                    MapArea.zoneMaps[clazz.javaClass.simpleName + "MapArea"] = zone
                }
                amountLoaded++
            } catch (e: Exception) {
                log(this::class.java, Log.ERR,  "Error loading content: ${it.simpleName}, ${e.localizedMessage}")
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
                log(this::class.java, Log.ERR,  "Failed to load plugin ${p.name}.")

                if (t is NoSuchMethodException && p.superclass.simpleName == core.game.dialogue.DialoguePlugin::class.simpleName) {
                    log(this::class.java, Log.ERR, 
                        "Make sure the constructor signature matches " +
                        "`${p.simpleName}(player: Player? = null) : DialoguePlugin(player)'."
                    )
                }

                t.printStackTrace()
            }
        })

        scanResults.getClassesWithAnnotation("core.game.bots.PlayerCompatible").forEach { res ->
            val description = res.getAnnotationInfo("core.game.bots.ScriptDescription").parameterValues[0].value as Array<String>
            val identifier = res.getAnnotationInfo("core.game.bots.ScriptIdentifier").parameterValues[0].value.toString()
            val name = res.getAnnotationInfo("core.game.bots.ScriptName").parameterValues[0].value.toString()
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
                    PluginType.DIALOGUE -> (plugin as core.game.dialogue.DialoguePlugin).init()
                    PluginType.ACTIVITY -> ActivityManager.register(plugin as ActivityPlugin)
                    PluginType.LOGIN -> LoginConfiguration.getLoginPlugins().add(plugin as Plugin<Any?>)
                    PluginType.QUEST -> {
                        plugin.newInstance(null)
                        QuestRepository.register(plugin as Quest)
                    }
                    else -> log(this::class.java, Log.WARN,  "Unknown Manifest: " + manifest.type)
                }
            }
            numPlugins++
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

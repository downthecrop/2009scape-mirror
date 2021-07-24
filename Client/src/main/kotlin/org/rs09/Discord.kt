package org.rs09

import net.arikia.dev.drpc.DiscordEventHandlers
import net.arikia.dev.drpc.DiscordRPC
import net.arikia.dev.drpc.DiscordRichPresence

/**
 * Handles discord rich presence integration
 * @author Ceikry
 * library thanks to Vatuu on Github
 */
object Discord {
    const val APP_ID = "812421731238019073";
    var presence = DiscordRichPresence()
    var initialized = false

    @JvmStatic
    //Removed support for Linux due to the outlined issue on line 37. TODO: Revisit this when the RPC library updates. There's a PR there that *should* fix it.
    fun checkInitializable() : Boolean{
        val OS = System.getProperty("os.name")
        if(OS.toLowerCase().startsWith("windows") || OS.toLowerCase().contains("nux")){
            val arch = System.getProperty("os.arch")
            return arch != "aarch64" && !initialized
        }
        return false
    }

    @JvmStatic
    fun initialize() {
        val handlers = DiscordEventHandlers.Builder().setReadyEventHandler { user ->
            SystemLogger.logDiscord("Discord RPC Initialized, registered to " + user.username + "#" + user.discriminator)
        }.setErroredEventHandler { errorCode, message ->
            SystemLogger.logDiscord("Discord error encountered: [$errorCode] $message")
            DiscordRPC.discordShutdown()
        }.build()

        //On Linux, when the discord library initializes, it can crash any other client loading the same library.
        //Not sure what causes this, likely a JVM bug to be honest.
        DiscordRPC.discordInitialize(APP_ID, handlers, true)
        initialized = true
    }

    @JvmStatic
    fun updatePresence(stateMessage: String,details: String,imageKey : String) {
        if(presence.state == stateMessage && presence.details == details && imageKey == presence.smallImageKey) return
        val newPresence = DiscordRichPresence.Builder(stateMessage).setDetails(details).build()
        newPresence.startTimestamp = System.currentTimeMillis()
        if(newPresence.details.startsWith("Training")){
            newPresence.smallImageKey = imageKey
        }
        newPresence.largeImageKey = "logo"
        newPresence.largeImageText = "2009Scape, a free MMO."
        DiscordRPC.discordUpdatePresence(newPresence)
        presence = newPresence
        SystemLogger.logDiscord("Updating presence ($stateMessage,$details,$imageKey)")
    }
}
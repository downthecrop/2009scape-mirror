package org.rs09.client.config

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.rs09.SlayerTracker
import java.io.FileReader
import java.math.BigInteger
import java.util.*

/**
 * Handles the client's config loading
 * @author Ceikry
 */
class GameConfig {
    companion object {

        /**
         * Debug Booleans
         */
        @JvmField
        var ITEM_DEBUG_ENABLED = false

        @JvmField
        var OBJECT_DEBUG_ENABLED = false

        @JvmField
        var NPC_DEBUG_ENABLED = false

        @JvmField
        var HD_LOGIN_DEBUG = false

        @JvmField
        var HD_LOGIN_VERBOSE = false

        @JvmField
        var CACHE_DEBUG = false

        @JvmField
        var WORLD_MAP_DEBUG = false

        /**
         * Context Menu Presets
         */
        @JvmField
        var RCM_STYLE_PRESET = "classic"

        /**
         * Context Menu Customization
         */
        @JvmField
        var RCM_BG_COLOR = 6116423

        @JvmField
        var RCM_BG_OPACITY  = 255

        @JvmField
        var RCM_TITLE_COLOR = 0

        @JvmField
        var RCM_TITLE_OPACITY = 255

        @JvmField
        var RCM_BORDER_COLOR = 0

        @JvmField
        var RCM_BORDER_OPACITY = 255

        @JvmField
        var RCM_TITLE = "<col=0>Choose Option</col>"

        @JvmField
        var RS3_CONTEXT_STYLE = false

        /**
         * Render distance
         */
        @JvmField
        var RENDER_DISTANCE_INCREASE = false

        @JvmField
        var RENDER_DISTANCE_VALUE = 3584f

        @JvmField
        var RENDER_DISTANCE_TILE_VALUE = 28

        @JvmField
        var RENDER_DISTANCE_FOG_FIX = 3328.0f

        @JvmField
        var SKYBOX_COLOR = "float"

        /**
         * Client Info
         * Editable
         */
        @JvmField
        var IP_ADDRESS = "localhost"

        @JvmField
        var IP_MANAGEMENT = "localhost"

        var JS5_SERVER_PORT = 43593

        @JvmField
        var SERVER_PORT = 43594

        @JvmField
        var WL_PORT = 5555

        @JvmField
        var WORLD = 1

        @JvmField
        var WORLD_OVERRIDE = -1

        @JvmField
        var LOGIN_THEME = "scape main"

        @JvmField
        var xpDropsEnabled = true

        @JvmField
        var xpDropMode = 0

        @JvmField
        var xpTrackMode = 0

        @JvmField
        var slayerCountEnabled = true

        @JvmField
        var slayerTaskID = 0

        @JvmField
        var slayerTaskAmount = 0

        @JvmField
        var VERBOSE_LOGGING = false

        @JvmStatic
        fun setSlayerAmount(amount : Int){
            slayerTaskAmount = amount
            SlayerTracker.lastUpdate = System.currentTimeMillis()
        }

        /**
         * Json config Parser
         */
        @JvmStatic
        fun parse(path: String){
            val reader = FileReader(path)
            val parser = JSONParser()
            val data = parser.parse(reader) as JSONObject

            //Networking
            if(data.containsKey("ip_address")) IP_ADDRESS = data["ip_address"].toString() else IP_ADDRESS = "play.2009scape.org"
            if(data.containsKey("ip_management")) IP_MANAGEMENT = data["ip_management"].toString() else IP_MANAGEMENT = IP_ADDRESS
            if(data.containsKey("wl_port")) WL_PORT = data["wl_port"].toString().toInt()
            if(data.containsKey("server_port")) SERVER_PORT = data["server_port"].toString().toInt()
            if(data.containsKey("js5_port")) JS5_SERVER_PORT = data["js5_port"].toString().toInt()
            if(data.containsKey("world")) WORLD = data["world"].toString().toInt()

            //Parse customization options
            if(data.containsKey("customization")){
                val custom = data["customization"] as JSONObject
                if(custom.containsKey("login_theme")) LOGIN_THEME = custom["login_theme"].toString()

                //Right-click menu customizations
                if(custom.containsKey("right_click_menu")){
                    val rcm = custom["right_click_menu"] as JSONObject

                    //background
                    if(rcm.containsKey("background")){
                        val bg = rcm["background"] as JSONObject
                        if(bg.containsKey("color")) RCM_BG_COLOR = bg["color"].toString().replace("#", "").toInt(16) //convert hex -> deci
                        if(bg.containsKey("opacity")) RCM_BG_OPACITY = bg["opacity"].toString().toInt()
                    }

                    //title bar
                    if(rcm.containsKey("title_bar")){
                        val tb = rcm["title_bar"] as JSONObject
                        if(tb.containsKey("font_color")) RCM_TITLE = RCM_TITLE.replace("0", tb["font_color"].toString().replace("#", ""))
                        if(tb.containsKey("color")) RCM_TITLE_COLOR = tb["color"].toString().replace("#", "").toInt(16) //convert hex -> deci
                        if(tb.containsKey("opacity")) RCM_TITLE_OPACITY = tb["opacity"].toString().toInt()
                    }

                    //border
                    if(rcm.containsKey("border")){
                        val border = rcm["border"] as JSONObject
                        if(border.containsKey("color")) RCM_BORDER_COLOR = border["color"].toString().replace("#", "").toInt(16) //convert hex -> deci
                        if(border.containsKey("opacity")) RCM_BORDER_OPACITY = border["opacity"].toString().toInt()
                    }

                    //styles (changes how things are drawn)
                    if(rcm.containsKey("styles")){
                        val style = rcm["styles"] as JSONObject
                        if(style.containsKey("presets")) RCM_STYLE_PRESET = style["presets"].toString()
                        if(style.containsKey("rs3border")) RS3_CONTEXT_STYLE = style["rs3border"] as Boolean
                    }
                }
                if(custom.containsKey("rendering_options")) {
                    val hdoptions = custom["rendering_options"] as JSONObject

                    if(hdoptions.containsKey("technical")) {
                        val renderIncrease = hdoptions["technical"] as JSONObject
                        if(renderIncrease.containsKey("render_distance_increase")) RENDER_DISTANCE_INCREASE = renderIncrease["render_distance_increase"] as Boolean
                    }
                    if(hdoptions.containsKey("skybox")) {
                        val skyboxColor = hdoptions["skybox"] as JSONObject
                        if(skyboxColor.containsKey("skybox_color")) SKYBOX_COLOR
                    }
                }
            }

            //Parse debug options
            if(data.containsKey("debug")){
                val debug = data["debug"] as JSONObject
                if(debug.containsKey("item_debug")) ITEM_DEBUG_ENABLED = debug["item_debug"] as Boolean
                if(debug.containsKey("npc_debug")) NPC_DEBUG_ENABLED = debug["npc_debug"] as Boolean
                if(debug.containsKey("object_debug")) OBJECT_DEBUG_ENABLED = debug["object_debug"] as Boolean
                if(debug.containsKey("hd_login_region_debug"))  HD_LOGIN_DEBUG = debug["hd_login_region_debug"] as Boolean
                if(debug.containsKey("hd_login_region_debug_verbose")) HD_LOGIN_VERBOSE = debug["hd_login_region_debug_verbose"] as Boolean
                if(debug.containsKey("cache_debug")) CACHE_DEBUG = debug["cache_debug"] as Boolean
                if(debug.containsKey("world_map_debug")) WORLD_MAP_DEBUG = debug["world_map_debug"] as Boolean
            }


            /**
             * Style Overrides (Still working on this system. We should allow for maximum creativity
             * The way that it will be setup is a style type 1st
             * ie, classicbox, rs3, rounded, rounded2
             * Then we introduce color schemes that a user could select
             * ie, classic, rs3, alternate, alternate2, custom
             * @author Woah
             */
            when (RCM_STYLE_PRESET) {
                "classic" -> {
                    RS3_CONTEXT_STYLE = false
                    RCM_BG_COLOR = 6116423
                    RCM_BG_OPACITY = 255
                    RCM_TITLE = "<col=5d5447>Choose Option</col>"
                    RCM_TITLE_COLOR = 0
                    RCM_TITLE_OPACITY = 255
                    RCM_BORDER_COLOR = 0
                    RCM_BORDER_OPACITY = 255
                }
                "rs3" -> {
                    RS3_CONTEXT_STYLE = true
                    RCM_BG_COLOR = 662822
                    RCM_BG_OPACITY = 255
                    RCM_TITLE = "<col=C6B895>Choose Option</col>"
                    RCM_TITLE_COLOR = 1512718
                    RCM_TITLE_OPACITY = 165
                    RCM_BORDER_COLOR = 16777215
                    RCM_BORDER_OPACITY = 255
                }
            }


        }

        fun extendRenderDistance() {
            if (RENDER_DISTANCE_INCREASE) {
                /** **DO NOT CHANGE THESE NUMBERS UNLESS YOU KNOW WHAT YOU ARE DOING**
                 * Render Distance Overrides
                 *
                 * (Simple formula) Tile amount * 512
                 * Default: 7 * 512 = 3584
                 * Extended(max): 56 * 512 = 28672
                 *
                 * Files + methods effected by these values:
                 * HDToolKit METHOD viewport
                 * Class140_Sub1_Sub1 METHOD animate
                 * Class3_Sub22 METHOD method398 * value as short
                 * Class40 METHOD method1046 * using RENDER_DISTANCE_TILE_VALUE
                 */
                RENDER_DISTANCE_VALUE = if (RENDER_DISTANCE_INCREASE) 28672F else 3584.0f
                RENDER_DISTANCE_TILE_VALUE = if (RENDER_DISTANCE_INCREASE) 56 else 28
                RENDER_DISTANCE_FOG_FIX = if (RENDER_DISTANCE_INCREASE) 28672F else 3328.0f
            }
        }

        /**
         * Client Info
         * Not Editable
         */
        @JvmField
        var CLIENT_BUILD = 530

        @JvmField
        var CLIENT_VERSION = 1

        @JvmField
        var PACKAGE_NAME = "org.runite.client"

        @JvmField
        var RSA = true

        @JvmField
        var ISAAC = false

        @JvmField
        var EXPONENT = BigInteger("65537")

        @JvmField
        var MODULUS = BigInteger("96982303379631821170939875058071478695026608406924780574168393250855797534862289546229721580153879336741968220328805101128831071152160922518190059946555203865621183480223212969502122536662721687753974815205744569357388338433981424032996046420057284324856368815997832596174397728134370577184183004453899764051")

        @JvmField
        var SERVER_NAME = "2009scape"

        /**
         * Path to config
         */
        @JvmField
        var configLocation = "config.json"

        /**
         * Holiday Event Toggles
         */
        @JvmField
        var HOLIDAYS_ENABLED = true

        /**
         * Halloween event NPC Definitions are handled inside of NPCDefinition.java
         */
        @JvmField
        var HALLOWEEN_EVENT_ENABLED = false

        @JvmField
        var THANKSGIVING_EVENT_ENABLED = false

        @JvmField
        var CHRISTMAS_EVENT_ENABLED = false

        private val calendar: Calendar = Calendar.getInstance()
        private val month = calendar.get(Calendar.MONTH)

        @JvmStatic
        fun implementHoliday() {
            if (HOLIDAYS_ENABLED) {
                when (month) {
                    9 -> HALLOWEEN_EVENT_ENABLED = true
                    10 -> THANKSGIVING_EVENT_ENABLED = true
                    11 -> CHRISTMAS_EVENT_ENABLED = true
                }
            }
        }
    }
}
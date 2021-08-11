package ms.system.util

import org.json.simple.JSONObject

object ManagementConstants {

    //MySQL main database name
    var DATABASE_NAME: String = "global"

    //MySQL database username
    var DATABASE_USERNAME: String = "root"

    //MySQL database password
    var DATABASE_PASSWORD: String = ""

    //MySQL host
    var DATABASE_HOST_ADDRESS: String = "127.0.0.1"

    //MySQL port
    var DATABASE_PORT: Int = 3306

    //Max amount of worlds supported on the world list
    var MAX_WORLD_AMOUNT: Int = 10

    //User world hop delay in seconds
    var WORLD_HOP_DELAY: Long = 20_000L

    @JvmStatic
    var SECRET_KEY: String = ""

    fun parseDBProp(data: JSONObject) {
        DATABASE_NAME = data["database_name"].toString()
        DATABASE_USERNAME = data["database_username"].toString()
        DATABASE_PASSWORD = data["database_password"].toString()
        DATABASE_HOST_ADDRESS = data["database_host"].toString()
        DATABASE_PORT = data["database_port"].toString().toInt()
    }

    fun parseWTIProp(data: JSONObject) {
        MAX_WORLD_AMOUNT = data["world_limit"].toString().toInt()
        WORLD_HOP_DELAY = data["worldhop_delay"].toString().toLong()
    }

}
package ms.system.mysql

import ms.system.util.ManagementConstants
import ms.system.util.ManagementConstants.DATABASE_HOST_ADDRESS
import ms.system.util.ManagementConstants.DATABASE_NAME
import ms.system.util.ManagementConstants.DATABASE_PASSWORD
import ms.system.util.ManagementConstants.DATABASE_PORT
import ms.system.util.ManagementConstants.DATABASE_USERNAME
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Manages the sql connections.
 * @author Vexia
 */
object SQLManager {

    /**
     * IF the sql manager is initialized.
     */
    var isInitialized = false

    /**
     * Initializes the sql manager.
     */
    @JvmStatic
    fun init() {
        isInitialized = true
        WorldListSQLHandler.clearWorldList()
    }

    /**
     * Gets a connection from the pool.
     * @return The connection.
     */
    @JvmStatic
    val connection: Connection?
        get() {
            try {
                return DriverManager.getConnection(
                    "jdbc:mysql://$DATABASE_HOST_ADDRESS:$DATABASE_PORT/$DATABASE_NAME?useTimezone=true&serverTimezone=UTC",
                    DATABASE_USERNAME,
                    DATABASE_PASSWORD
                )
            } catch (e: SQLException) {
                println("Error: Mysql error message=" + e.message + ".")
            }
            return null
        }

    /**
     * Releases the connection so it's available for usage.
     * @param connection The connection.
     */
    @JvmStatic
    fun close(connection: Connection) {
        try {
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

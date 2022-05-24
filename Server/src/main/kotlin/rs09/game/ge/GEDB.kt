package rs09.game.ge

import core.cache.def.impl.ItemDefinition
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.sqlite.SQLiteDataSource
import rs09.ServerConstants
import rs09.game.system.SystemLogger
import java.io.File
import java.io.FileReader
import java.sql.Connection
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * Collection of methods for interacting with the grand exchange databases
 * @author Ceikry
 */
object GEDB {
    private var pathString = ""
    public var connection: Connection? = null
    private var initialized = false
    private var connectionRefs = 0
    private var obtainConnectionLock = ReentrantLock()
    private var dbRunLock = ReentrantLock()

    fun init() {
        init(File(ServerConstants.GRAND_EXCHANGE_DATA_PATH + "grandexchange.db").absolutePath)
    }

    //This needs to be a separate method, so we can call it after the server config has been parsed
    fun init(path: String)
    {
        if(initialized) return
        pathString = path
        //Check if the grandexchange.db file already exists. If not, create it and create the tables.
        if(!File(path).exists())
            generateAndTransfer()
    }

    @JvmStatic fun run(closure: (conn: Connection) -> Unit) {
        dbRunLock.tryLock(10000L, TimeUnit.MILLISECONDS)

        connectionRefs++
        val con = connect()
        closure.invoke(con)
        connectionRefs--

        if(connectionRefs == 0) {
            con.close()
        }

        dbRunLock.unlock()
    }

    private fun connect(): Connection
    {
        obtainConnectionLock.tryLock(10000L, TimeUnit.MILLISECONDS)

        if (connection == null || connection!!.isClosed)
        {
            val ds = SQLiteDataSource()
            ds.url = "jdbc:sqlite:$pathString"
            connection = ds.connection
        }

        obtainConnectionLock.unlock()
        return connection!!
    }

    private fun convertJsonArray(arr: JSONArray): String
    {
        val sb = StringBuilder()
        for ((index, data) in arr.withIndex())
        {
            val item = data as JSONObject
            sb.append("$index")
            sb.append(",")
            sb.append(item["id"])
            sb.append(",")
            sb.append(item["amount"])
            if(index + 1 < arr.size)
                sb.append(":")
        }
        return sb.toString()
    }

    private fun generateAndTransfer()
    {
        val conn = connect() //for sqlite jdbc, attempting to connect to a non-existing .db file creates it.
        val statement = conn.createStatement()

        //table for tracking player offers - replaces offer_dispatch.json
        statement.execute(
                "CREATE TABLE player_offers(" +
                        "uid INTEGER PRIMARY KEY ASC," +
                        "player_uid      INTEGER," +
                        "item_id         INTEGER," +
                        "amount_total    INTEGER," +
                        "amount_complete INTEGER," +
                        "offered_value   INTEGER," +
                        "time_stamp      INTEGER," +
                        "offer_state     INTEGER," +
                        "is_sale         INTEGER," +
                        "withdraw_items  STRING ," +
                        "slot_index      INTEGER," +
                        "total_coin_xc   INTEGER)"
        )

        //table for tracking bot offers - replaces bot_offers.json
        statement.execute(
                "CREATE TABLE bot_offers(" +
                        "item_id         INTEGER," +
                        "amount          INTEGER)"
        )

        //table for tracking price index data - replaces gedb.xml
        statement.execute(
                "CREATE TABLE price_index(" +
                        "item_id         INTEGER," +
                        "value           INTEGER," +
                        "total_value     INTEGER," +
                        "unique_trades   INTEGER," +
                        "last_update     INTEGER)"
        )

        //check if the old .json and .xml files exist, and if they do, read them into the sqlite database and remove them.
        val playerJson = ServerConstants.GRAND_EXCHANGE_DATA_PATH + "offer_dispatch.json"
        val botJson = ServerConstants.GRAND_EXCHANGE_DATA_PATH + "bot_offers.json"

        if(File(playerJson).exists())
        {
            val parser = JSONParser()
            val reader = FileReader(playerJson)
            val saveFile = parser.parse(reader) as JSONObject

            if (saveFile.containsKey("offers")) {
                val offers = saveFile["offers"] as JSONArray

                for (offer in offers) {
                    val o = offer as JSONObject
                    statement.execute("insert into player_offers(player_uid,item_id,amount_total,amount_complete,offered_value,time_stamp,offer_state,is_sale,withdraw_items,total_coin_xc,slot_index) " +
                            "values(" +
                            "${o["playerUID"]}," +
                            "${o["itemId"]}," +
                            "${o["amount"]}," +
                            "${o["completedAmount"]}," +
                            "${o["offeredValue"]}," +
                            "${o["timeStamp"]}," +
                            "${o["offerState"]}," +
                            "${if (o["sale"] as Boolean) 1 else 0}," +
                            "'" + convertJsonArray(o["withdrawItems"] as JSONArray) + "'," +
                            "${o["totalCoinExchange"]}," +
                            "-1)"
                    )

                }
            }

            reader.close()
            //File(playerJson).delete()
        }

        if(File(botJson).exists())
        {
            val parser = JSONParser()
            val reader = FileReader(botJson)
            val saveFile = parser.parse(reader) as JSONObject

            if (saveFile.containsKey("offers")) {
                val offers = saveFile["offers"] as JSONArray

                for (offer in offers) {
                    val o = offer as JSONObject
                    statement.execute("insert into bot_offers " +
                            "values(${o["item"]},${o["qty"]})")
                }
            }
            reader.close()
        }
        statement.close()


        //price index isn't worth transferring, so we're just going to make a new one.
        ItemDefinition.getDefinitions().values.forEach { def ->
            if(def.isTradeable){
                PriceIndex.allowItem(def.id)
            }
        }
    }
}
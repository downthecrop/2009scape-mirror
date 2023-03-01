package content.data.tables

import core.ServerConstants
import core.api.StartupListener
import core.api.log
import core.api.shouldRemoveNothings
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.Log
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Handles the rare drop table.
 * @author Ceikry
 */
class RareDropTable : StartupListener {
    override fun startup() {
        if (ServerConstants.RDT_DATA_PATH != null && !File(ServerConstants.RDT_DATA_PATH).exists()) {
            log(this::class.java, Log.ERR, "Can't locate RDT file at " + ServerConstants.RDT_DATA_PATH)
            return
        }
        parse(ServerConstants.RDT_DATA_PATH)
        log(this::class.java, Log.FINE, "Initialized Rare Drop Table from " + ServerConstants.RDT_DATA_PATH)
    }

    companion object {
        private val TABLE: WeightBasedTable = object : WeightBasedTable() {
            override fun roll(receiver: Entity?): ArrayList<Item> {
                val items = ArrayList(guaranteedItems)
                var effectiveWeight = totalWeight
                val p = if (receiver is Player) receiver else null
                if (p != null && shouldRemoveNothings(p))
                    effectiveWeight -= nothingWeight

                if (this.size == 1) {
                    items.add(get(0))
                } else if (!this.isEmpty()) {
                    var rngWeight = RandomFunction.randomDouble(effectiveWeight)
                    for (item in this.shuffled()) {
                        if (item.id == Items.DWARF_REMAINS_0) continue
                        rngWeight -= item.weight
                        if (rngWeight <= 0) {
                            items.add(item)
                            break
                        }
                    }
                }
                return convertWeightedItems(items, receiver)
            }

            private val nothingWeight: Double
                get() {
                    var sum = 0.0
                    for (i in this) {
                        if (i.id == Items.DWARF_REMAINS_0)
                            sum += i.weight
                    }
                    return sum
                }
        }

        /**
         * Initialize needed objects for xml reading/writing
         */
        var factory = DocumentBuilderFactory.newInstance()
        var builder: DocumentBuilder? = null

        init {
            try {
                builder = factory.newDocumentBuilder()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            }
        }

        /**
         * Parses the xml file for the RDT.
         * @param file the .xml file containing the RDT.
         */
        fun parse(file: String?) {
            try {
                val doc = builder!!.parse(file)
                val itemNodes = doc.getElementsByTagName("item")
                for (i in 0 until itemNodes.length) {
                    val itemNode = itemNodes.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val item = itemNode as Element
                        val itemId = item.getAttribute("id").toInt()
                        val minAmt = item.getAttribute("minAmt").toInt()
                        val maxAmt = item.getAttribute("maxAmt").toInt()
                        val weight = item.getAttribute("weight").toInt()
                        TABLE.add(WeightedItem(itemId, minAmt, maxAmt, weight.toDouble(), false))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun retrieve(receiver: Entity?): Item? {
            return TABLE.roll(receiver).getOrNull(0)
        }
    }
}
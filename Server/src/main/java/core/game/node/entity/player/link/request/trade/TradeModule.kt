package core.game.node.entity.player.link.request.trade

import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerType
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestModule
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import rs09.game.ai.AIRepository
import rs09.game.ai.general.scriptrepository.DoublingMoney
import rs09.game.node.entity.player.info.PlayerMonitor
import java.text.DecimalFormat
import java.util.*

/**
 * Represents the module use to handle a request between trading of two players.
 * @author Vexia
 * @author dginovker
 * @version 2.0
 */
class TradeModule
/**
 * Constructs a new `TradeModule` `Object`.
 * @param player the player.
 * @param target the target.
 */(player: Player?, target: Player?) : RequestModule {
    /**
     * If the container has been retained.
     */
    var isRetained = false
    /**
     * Represents the player instance.
     */
    var player: Player? = player
        private set
    /**
     * Represents the target instance.
     */
    var target: Player? = target
        private set
    /**
     * Represents the container of this session.
     */
    var container: TradeContainer? = null
        private set
    /**
     * Represents if this session has accepted.
     */
    var isAccepted = false
        private set
    /**
     * Represents if the trade is modified.
     */
    var isModified = false
    /**
     * Represents the stage of the trade(0=started, 1=second accept)
     */
    var stage = 0
        private set

    override fun open(player: Player?, target: Player?) {
        extend(player, target)
        if (getExtension(target) == null || getExtension(player) == null) {
            return
        }
        getExtension(player)!!.openInterface(getInterface(stage)).display(stage)
        if (getExtension(target) == null || getExtension(player) == null) {
            return
        }
        getExtension(target)!!.openInterface(getInterface(stage)).display(stage)
        player!!.dialogueInterpreter.close()
        target!!.dialogueInterpreter.close()
    }

    /**
     * Method used to update the trade interfaces.
     */
    fun update() {
        display(stage)
        getExtension(target)!!.display(stage)
    }

    /**
     * Method used to open an interface at a stage.
     * @param component the component.
     * @return the module instance for chaining.
     */
    private fun openInterface(component: Component): TradeModule {
        return if (component === MAIN_INTERFACE) openMain() else openSecond()
    }

    /**
     * Gets the accepting message to display.
     * @return the message.
     */
    private val acceptMessage: String
        get() {
            val otherAccept = getExtension(target)!!.isAccepted
            return if (!otherAccept && !isAccepted) "" else if (otherAccept) "Other player has accepted." else "Waiting for other player..."
        }

    /**
     * Method used to display the interface display depending on stage.
     * @param stage the stage.
     * @return module the module instance for chaining.
     */
    private fun display(stage: Int): TradeModule {
        when (stage) {
            0 -> {
                for (i in HIDDEN_CHILDS) {
                    player!!.packetDispatch.sendString("", MAIN_INTERFACE.id, i)
                }
                player!!.packetDispatch.sendString("Trading With: " + target!!.username, 335, 15)
                player!!.packetDispatch.sendString(
                    target!!.username + " has " + (if (target!!.inventory.freeSlots() == 0) "no" else target!!.inventory.freeSlots()) + " free inventory slots.",
                    335,
                    21
                )
                player!!.packetDispatch.sendString(acceptMessage, 335, 36)
            }

            1 -> {
                player!!.packetDispatch.sendString(
                    "<col=00FFFF>Trading with:<br>" + "<col=00FFFF>" + target!!.username.substring(
                        0,
                        1
                    ).uppercase(Locale.getDefault()) + target!!.username.substring(1), 334, 2
                )
                val acceptMessage = acceptMessage
                if (acceptMessage !== "") {
                    player!!.packetDispatch.sendString(acceptMessage, 334, 33)
                }
                player!!.interfaceManager.restoreTabs()
            }
        }
        displayModified(stage)
        container!!.update(true)
        return this
    }

    /**
     * Gets the interface for the current stage.
     * @param stage the stage, defaults to this TradeModule's stage.
     * @return the component.
     */
    fun getInterface(stage: Int = this.stage): Component {
        return if (stage == 0) MAIN_INTERFACE else ACCEPT_INTERFACE
    }

    /**
     * Gets the interface for the TradeModule stage
     * @return the component
     */
    fun getInterface(): Component {
        return getInterface(stage)
    }

    /**
     * Method used to display red text at the top of the trade window when the trade has been modified
     * (Red text appears at the bottom in second trade screen)
     * @param stage the sytage.
     */
    private fun displayModified(stage: Int) {
        val otherModified = getExtension(target)!!.isModified
        if (stage == 0) {
            // Main interface
            if (otherModified) {
                 player!!.configManager[1043] = 1
            }
            if (isModified) {
                player!!.configManager[1042] = 1
            }
        } else {
            // Accept interface
            player!!.packetDispatch.sendInterfaceConfig(334, 45, !isModified)
            player!!.packetDispatch.sendInterfaceConfig(334, 46, !otherModified)
        }
    }

    /**
     * Method used to display flashing alert if the trade has been modified.
     * @param slot the slot.
     */
    fun alert(slot: Int) {
        player!!.packetDispatch.sendRunScript(143, "Iiii", *arrayOf<Any>(slot, 7, 4, 21954594))
        target!!.packetDispatch.sendRunScript(143, "Iiii", *arrayOf<Any>(slot, 7, 4, 21954593))
    }


    /**
     * Method used to decline this offer.
     */
    fun decline() {
        player!!.interfaceManager.close()
        target!!.packetDispatch.sendMessage("<col=FF0000>Other player has declined trade!</col>")
    }

    /**
     * Method used to check if the next stage can proceed.
     */
    private fun nextStage() {
        if (isAccepted && getExtension(target)!!.isAccepted) {
            if (stage == 0) {
                if (!hasSpace()) {
                    return
                }
                incrementStage()
                openInterface(getInterface(stage))
                getExtension(target)!!.incrementStage()
                getExtension(target)!!.openInterface(getInterface(stage))
                getExtension(target)!!.setAccepted(false, false)
                setAccepted(false, false)
            } else {
                giveContainers(this)
                incrementStage()
                getExtension(target)!!.incrementStage()
                getExtension(target)!!.setAccepted(false, false)
                setAccepted(false, false)
                player!!.interfaceManager.close()
                return
            }
        }
        update()
    }

    /**
     * Method used to open the main interface.
     * @return the module instance for chaining.
     */
    private fun openMain(): TradeModule {
        player!!.interfaceManager.closeDefaultTabs()
        player!!.interfaceManager.open(MAIN_INTERFACE)
        player!!.interfaceManager.openSingleTab(OVERLAY_INTERFACE)
        player!!.inventory.refresh()
        player!!.packetDispatch.sendIfaceSettings(1278, 30, 335, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(1026, 32, 335, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(1278, 0, 336, 0, 27)
        player!!.packetDispatch.sendIfaceSettings(2360446, 0, 335, 0, 27)
        player!!.packetDispatch.sendRunScript(150, "IviiiIsssssssss", *INVENTORY_PARAMS)
        player!!.packetDispatch.sendRunScript(150, "IviiiIsssssssss", *TRADE_PARAMS)
        player!!.packetDispatch.sendRunScript(695, "IviiiIsssssssss", *PARTENER_PARAMS)
        return this
    }

    /**
     * Method used to open the second interface.
     * @return the module instance for chaining.
     */
    private fun openSecond(): TradeModule {
        player!!.interfaceManager.open(ACCEPT_INTERFACE)
        player!!.interfaceManager.closeSingleTab()
        displayOffer(container, 37)
        displayOffer(getExtension(target)!!.container, 41)
        return this
    }

    /**
     * Method used to set the value of accepted.
     * @param accept whether the trade was accepted or declined
     */
    fun setAccepted(accept: Boolean, update: Boolean) {
        isAccepted = accept
        if (update) {
            nextStage()
        }
    }

    /**
     * Method used to display an offer.
     * @param container the container.
     */
    private fun displayOffer(container: Container?, child: Int) {
        val split = container!!.itemCount() > 14
        if (split) {
            player!!.packetDispatch.sendInterfaceConfig(334, child + 1, false)
            player!!.packetDispatch.sendInterfaceConfig(334, child + 2, false)
            val messages = arrayOf(
                getDisplayMessage(splitList(container.toArray(), 0, 14)), getDisplayMessage(
                    splitList(
                        container.toArray(), 14, container.toArray().size
                    )
                )
            )
            player!!.packetDispatch.sendString(messages[0], 334, child + 1)
            player!!.packetDispatch.sendString(messages[1], 334, child + 2)
        } else {
            player!!.packetDispatch.sendInterfaceConfig(334, child, false)
            player!!.packetDispatch.sendString(getDisplayMessage(container.toArray()), 334, child)
        }
    }

    /**
     * Method used to get the display message.
     * @param items the items.
     * @return the message.
     */
    private fun getDisplayMessage(items: Array<Item?>): String {
        var message = "Absolutely nothing!"
        if (items.isNotEmpty()) {
            message = ""
            for (i in items.indices) {
                if (items[i] == null) {
                    continue
                }
                message = message + "<col=FF9040>" + items[i]!!.name
                if (items[i]!!.amount > 1) {
                    message = "$message<col=FFFFFF> x "
                    message = message + "<col=FFFF00>" + getFormattedNumber(items[i]!!.amount) + "<br>"
                } else {
                    message = "$message<br>"
                }
            }
        }
        return message
    }

    /**
     * Method used to check if the players & targets have enough space.
     * @return `True` if so.
     */
    private fun hasSpace(): Boolean {
        var hasSpace = true
        if (!player!!.inventory.hasSpaceFor(getExtension(target)!!.container)) {
            target!!.packetDispatch.sendMessage("Other player doesn't have enough space in their inventory for this trade.")
            player!!.packetDispatch.sendMessage("You don't have enough inventory space for this.")
            hasSpace = false
        } else if (!target!!.inventory.hasSpaceFor(container)) {
            player!!.packetDispatch.sendMessage("Other player doesn't have enough space in their inventory for this trade.")
            target!!.packetDispatch.sendMessage("You don't have enough inventory space for this.")
            hasSpace = false
        }
        if (!hasSpace) {
            setAccepted(false, true)
            getExtension(target)!!.setAccepted(false, true)
        }
        return hasSpace
    }

    /**
     * Gets the formatted number.
     * @param amount the amount.
     * @return the formatted number.
     */
    private fun getFormattedNumber(amount: Int): String {
        return DecimalFormat("#,###,##0").format(amount.toLong()).toString()
    }

    /**
     * Method used to give the containers to each participants.
     * @param module the module.
     */
    private fun giveContainers(module: TradeModule) {
        val pContainer: Container = module.container ?: return
        val oContainer: Container = getExtension(module.target)!!.container ?: return

        PlayerMonitor.logTrade(module.player!!, module.target!!, pContainer, oContainer)

        (AIRepository.PulseRepository[module.player!!.username.lowercase()]?.botScript as DoublingMoney?)?.itemsReceived(module.target!!, oContainer)
        (AIRepository.PulseRepository[module.target!!.username.lowercase()]?.botScript as DoublingMoney?)?.itemsReceived(module.player!!, pContainer)

        addContainer(module.player, oContainer)
        addContainer(module.target, pContainer)
        module.target!!.packetDispatch.sendMessage("Accepted trade.")
        module.player!!.packetDispatch.sendMessage("Accepted trade.")
    }

    /**
     * Method used to add a container for a player.
     * @param player the player who loses the items.
     * @param target the target who gains the items.
     * @param container the container.
     */
    private fun addContainer(player: Player?, container: Container?) {
        val c = Container(container!!.itemCount(), ContainerType.ALWAYS_STACK)
        c.addAll(container)
        for (i in container.toArray()) {
            if (i == null) {
                continue
            }
            if (i.amount > player!!.inventory.getMaximumAdd(i)) {
                i.amount = player.inventory.getMaximumAdd(i)
            }
            if (!player.inventory.add(i)) {
                GroundItemManager.create(i, player)
            }
        }
    }

    /**
     * Gets the split list as an array.
     * @param items the items.
     * @param min the min.
     * @param max the max.
     * @return the split item array.
     */
    private fun splitList(items: Array<Item?>, min: Int, max: Int): Array<Item?> {
        val list: MutableList<Item?> = ArrayList(20)
        for (i in min until max) {
            if (items[i] == null) {
                continue
            }
            list.add(items[i])
        }
        val array = arrayOfNulls<Item>(list.size)
        for (i in list.indices) {
            if (list[i] == null) {
                continue
            }
            array[i] = list[i]
        }
        return array
    }

    /**
     * Method used to increment the stage.
     */
    fun incrementStage() {
        stage++
    }

    companion object {
        /**
         * Represents the overlay interface component.
         */
        val OVERLAY_INTERFACE = Component(336)

        /**
         * Represents the main interface component.
         */
        val MAIN_INTERFACE: Component = Component(335).setCloseEvent(TradeCloseEvent())

        /**
         * Represents the "accept" interface component.
         */
        val ACCEPT_INTERFACE: Component = Component(334).setCloseEvent(TradeCloseEvent())

        /**
         * The inventory params for the runscript.
         */
        val INVENTORY_PARAMS = arrayOf<Any>(
            "",
            "",
            "",
            "Lend",
            "Offer-X",
            "Offer-All",
            "Offer-10",
            "Offer-5",
            "Offer",
            -1,
            0,
            7,
            4,
            93,
            336 shl 16
        )

        /**
         * The trade params for the run script.
         */
        val TRADE_PARAMS = arrayOf<Any>(
            "",
            "",
            "",
            "",
            "Remove-X",
            "Remove-All",
            "Remove-10",
            "Remove-5",
            "Remove",
            -1,
            0,
            7,
            4,
            90,
            335 shl 16 or 30
        )

        /**
         * The partener params for the run script.
         */
        val PARTENER_PARAMS = arrayOf<Any>("", "", "", "", "", "", "", "", "", -1, 0, 7, 4, 91, 335 shl 16 or 32)

        /**
         * Represents the childs that should be hidden.
         */
        val HIDDEN_CHILDS = intArrayOf(42, 43, 44, 42, 44, 40, 41)

        /**
         * Method used to extend the module for each participant.
         * @param player the player.
         * @param target the target.
         */
        fun extend(player: Player?, target: Player?) {
            player!!.addExtension(TradeModule::class.java, TradeModule(player, target))
            target!!.addExtension(TradeModule::class.java, TradeModule(target, player))
        }

        /**
         * Method used to get the extension from the player.
         * @param player the player.
         * @return the module instance.
         */
		@JvmStatic
		fun getExtension(player: Player?): TradeModule? {
            return player!!.getExtension(TradeModule::class.java)
        }
    }

    init {
        container = TradeContainer(player)
    }
}
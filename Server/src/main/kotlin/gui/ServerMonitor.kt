package gui

import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.Border

object ServerMonitor : JFrame("Server Monitor"){

    val eventQueue = ArrayList<GuiEvent>()
    val pulseCount = JLabel("0")
    val tickTime = JLabel("0")
    val queuedPackets = JLabel("0")
    val pulseList = JTextArea("")
    val debugLog = JTextArea("")
    val defaultLog = JTextArea("")
    val AIPlog = JTextArea("")
    val tickTimeGraph = GraphView()
    val pulseCountGraph = GraphView()
    val packetQueueGraph = GraphView()

    init {
        isVisible = false
        layout = BorderLayout()
        size = Dimension(800,640)
        preferredSize = Dimension(800,640)
        minimumSize = Dimension(800,640)
        isResizable = false

        val tabs = JTabbedPane()
        tabs.addTab("Overview", constructOverviewTab())
        tabs.addTab("Graphs", constructGraphsTab())
        add(tabs)

        pack()
    }

    private fun constructGraphsTab(): JPanel {
        val graphPanel = JPanel()

        graphPanel.placeAt(0,10,800,590)

        val tickTimeBorder = BorderFactory.createTitledBorder("Tick Time")
        val pulseCountBorder = BorderFactory.createTitledBorder("Pulse Count")
        val packetQueueBorder = BorderFactory.createTitledBorder("Queued Packets")

        tickTimeGraph.placeAt(0, 20, 300, 200)
        tickTimeGraph.border = tickTimeBorder

        pulseCountGraph.placeAt(300,20,300,200)
        pulseCountGraph.border = pulseCountBorder

        packetQueueGraph.placeAt(0,220,300,200)
        packetQueueGraph.border = packetQueueBorder

        graphPanel.add(tickTimeGraph)
        graphPanel.add(pulseCountGraph)
        graphPanel.add(packetQueueGraph)
        return graphPanel
    }

    private fun constructOverviewTab(): JPanel {
        val overviewPanel = JPanel(null)
        overviewPanel.placeAt(0,10,800,590)

        val pulseLabel = JLabel("Pulse Count: ")
        pulseLabel.placeAt(0,20,100,10)
        pulseCount.placeAt(150,20,100,10)

        val tickLabel = JLabel("Tick Time: ")
        tickLabel.placeAt(0,35,100,10)
        tickTime.placeAt(150,35,100,10)

        val packetLabel = JLabel("Queued Packets: ")
        packetLabel.placeAt(0,50,200,10)
        queuedPackets.placeAt(150, 50, 100, 10)

        val scrollPane = JScrollPane(pulseList)
        scrollPane.placeAt(400,10,390,275)

        val border = BorderFactory.createTitledBorder("Pulses")
        scrollPane.border = border

        overviewPanel.add(pulseLabel)
        overviewPanel.add(pulseCount)
        overviewPanel.add(tickLabel)
        overviewPanel.add(tickTime)
        overviewPanel.add(packetLabel)
        overviewPanel.add(queuedPackets)
        overviewPanel.add(scrollPane)

        val logPane = constructLogPane()
        logPane.placeAt(0,280,800,300)
        overviewPanel.add(logPane)

        return overviewPanel
    }

    private fun constructLogPane() : JTabbedPane {
        val logPane = JTabbedPane()

        debugLog.isEditable = false
        val debugPanel = JPanel(BorderLayout())
        val debugScrollpane = JScrollPane(debugLog)
        debugScrollpane.placeAt(0,300,800,300)
        debugPanel.add(debugScrollpane, BorderLayout.CENTER)

        defaultLog.isEditable = false
        val defaultPanel = JPanel(BorderLayout())
        val defaultScrollpane = JScrollPane(defaultLog)
        defaultScrollpane.placeAt(0,300,800,300)
        defaultPanel.add(defaultScrollpane, BorderLayout.CENTER)

        AIPlog.isEditable = false
        val AIPPanel = JPanel(BorderLayout())
        val AIPscrollpane = JScrollPane(AIPlog)
        AIPscrollpane.placeAt(0,300,800,300)
        AIPPanel.add(AIPscrollpane, BorderLayout.CENTER)

        logPane.addTab("Default",defaultPanel)
        logPane.addTab("Debug",debugPanel)
        logPane.addTab("AIP",AIPPanel)

        return logPane
    }


    private fun Component.placeAt(x: Int, y: Int, width: Int, height: Int){
        this.setBounds(x,y,width,height)
    }

    fun open() {
        isVisible = true
        Thread() {
            while (true) {
                SwingUtilities.invokeLater {
                    val removeList = ArrayList<GuiEvent>()
                    eventQueue.toTypedArray().forEach { event ->
                        when (event) {
                            is GuiEvent.UpdatePulseCount -> {
                                pulseCount.text = event.amount.toString()
                                pulseCountGraph.addValues(intArrayOf(event.amount).toList())
                            }
                            is GuiEvent.UpdateTickTime -> {
                                tickTime.text = event.time.toString() + "ms"
                                tickTimeGraph.addValues(intArrayOf(event.time.toInt()).toList())
                            }
                            is GuiEvent.AddDefaultMessage -> defaultLog.text = defaultLog.text + event.message + "\n"
                            is GuiEvent.AddAIPMessage -> AIPlog.text = AIPlog.text + event.message + "\n"
                            is GuiEvent.AddDebugMessage -> debugLog.text = debugLog.text + event.message + "\n"
                            is GuiEvent.UpdateQueuedPackets -> {
                                queuedPackets.text = event.amount.toString()
                                packetQueueGraph.addValues(intArrayOf(event.amount).toList())
                            }
                        }
                        removeList.add(event)
                    }

                    pulseList.text = GameWorld.Pulser.TASKS.toArray().filterNotNull().map { it.javaClass.simpleName + "\n" }
                        .filter { it.replace("\n", "").isNotEmpty() }.sorted().joinToString("")
                    eventQueue.removeAll(removeList)
                    repaint()
                }
                Thread.sleep(600L)
            }
        }.start()
    }
}
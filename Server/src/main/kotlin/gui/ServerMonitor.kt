package gui

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
    val pulseList = JTextArea()

    init {
        isVisible = false
        layout = BorderLayout()
        size = Dimension(800,600)
        preferredSize = Dimension(800,600)
        minimumSize = Dimension(800,600)
        val tabs = JTabbedPane()
        tabs.addTab("Overview", constructOverviewTab())

        add(tabs)
        pack()
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

        val scrollPane = JScrollPane(pulseList)
        scrollPane.placeAt(400,10,390,275)

        overviewPanel.add(pulseLabel)
        overviewPanel.add(pulseCount)
        overviewPanel.add(tickLabel)
        overviewPanel.add(tickTime)
        overviewPanel.add(scrollPane)

        return overviewPanel
    }


    private fun Component.placeAt(x: Int, y: Int, width: Int, height: Int){
        this.setBounds(x,y,width,height)
    }

    fun open() {
        isVisible = true
        Thread() {
            while(true){
                val removeList = ArrayList<GuiEvent>()
                eventQueue.forEach { event ->
                    when(event){
                        is GuiEvent.UpdatePulseCount -> pulseCount.text = event.amount.toString()
                        is GuiEvent.UpdateTickTime -> tickTime.text = event.time.toString() + "ms"
                    }
                    removeList.add(event)
                }

                pulseList.text = GameWorld.Pulser.TASKS.toArray().map { it.javaClass.simpleName + "\n" }.filter { it.replace("\n","").isNotEmpty() }.sorted().joinToString("")
                eventQueue.removeAll(removeList)
                repaint()
                Thread.sleep(600L)
            }
        }.start()
    }
}
package core.gui;


import core.gui.tab.GrandExchangeTab;
import core.gui.tab.PlayerTab;
import core.gui.tab.StatisticsTab;
import core.gui.tab.UtilityTab;

import javax.swing.*;
import java.awt.*;

/**
 * The console frame tool.
 *
 * @author Vexia
 */
public class ConsoleFrame extends JFrame {

    /**
     * The console frame instance.
     */
    public static final ConsoleFrame INSTANCE = new ConsoleFrame();

    /**
     * The serail UID.
     */
    private static final long serialVersionUID = 6368064564449356833L;

    /**
     * The size of the frame.
     */
    private static final Dimension SIZE = new Dimension(1074, 664);

    /**
     * The tabbed pane.
     */
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

    /**
     * The statistics tab.
     */
    private final StatisticsTab statisticsTab = StatisticsTab.INSTANCE.init();

    /**
     * The player tab.
     */
    private final PlayerTab playerTab = new PlayerTab();

    /**
     * The grand exchange tab.
     */
    private final GrandExchangeTab grandExchangeTab = new GrandExchangeTab();

    /**
     * The utility tab.
     */
    private final UtilityTab utilityTab = UtilityTab.getInstance();

    /**
     * Constructs a new {@Code consoleFrame} {@Code Object}
     */
    private ConsoleFrame() {
        super("Console");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the console frame.
     *
     * @return the frame.
     */
    public ConsoleFrame init() {
        setLocationRelativeTo(null);
        setSize(SIZE);
        getContentPane().setLayout(null);
        tabbedPane.setBounds(0, 0, 1068, 636);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        getContentPane().add(tabbedPane);
        addTabs(statisticsTab, utilityTab, playerTab, grandExchangeTab);
        setResizable(false);
        setVisible(true);
        return this;
    }

    public static void main(String... args) {
        INSTANCE.init();
    }

    /**
     * Adds tabs.
     *
     * @param tabs the tabs.
     */
    public void addTabs(ConsoleTab... tabs) {
        for (ConsoleTab tab : tabs) {
            addTab(tab);
        }
    }

    /**
     * Adds a tab to the tabbed pane.
     *
     * @param tab the tab.
     */
    public void addTab(ConsoleTab tab) {
        tabbedPane.add(tab.getName(), tab);
    }

    /**
     * Gets the statisticsTab.
     *
     * @return the statisticsTab
     */
    public StatisticsTab getStatisticsTab() {
        return statisticsTab;
    }

    /**
     * Gets the tabbedPane.
     *
     * @return the tabbedPane
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Gets the playerTab.
     *
     * @return the playerTab
     */
    public PlayerTab getPlayerTab() {
        return playerTab;
    }

    /**
     * Gets the utilityTab.
     *
     * @return the utilityTab
     */
    public UtilityTab getUtilityTab() {
        return utilityTab;
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static ConsoleFrame getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the grandExchangeTab.
     *
     * @return the grandExchangeTab
     */
    public GrandExchangeTab getGrandExchangeTab() {
        return grandExchangeTab;
    }

}

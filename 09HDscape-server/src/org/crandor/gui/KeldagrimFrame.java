package org.crandor.gui;

import org.crandor.gui.component.ConsoleLogger;
import org.crandor.gui.tab.StatisticsTab;

import javax.swing.*;
import java.awt.*;

/**
 * The Keldagrim frame tool.
 * @author Austin
 */
public class KeldagrimFrame extends JFrame {

	/**
	 * The Keldagrim frame instance.
	 */
	public static final KeldagrimFrame INSTANCE = new KeldagrimFrame();

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
	 * Constructs a new {@Code KeldagrimFrame} {@Code Object}
	 */
	private KeldagrimFrame() {
		super("Keldagrim Frame");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the Keldagrim frame.
	 * @return the frame.
	 */
	public KeldagrimFrame init() {
		setLocationRelativeTo(null);
		setSize(SIZE);
		getContentPane().setLayout(null);
		tabbedPane.setBounds(0, 0, 1068, 636);
		getContentPane().add(tabbedPane);
		addTabs(statisticsTab/* , playerTab, grandExchangeTab, utilityTab */);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		System.setOut(new ConsoleLogger(System.out));
		return this;
	}

	/**
	 * Adds tabs.
	 * @param tabs the tabs.
	 */
	public void addTabs(KeldagrimTab... tabs) {
		for (KeldagrimTab tab : tabs) {
			addTab(tab);
		}
	}

	/**
	 * Adds a tab to the tabbed pane.
	 * @param tab the tab.
	 */
	public void addTab(KeldagrimTab tab) {
		tabbedPane.add(tab.getName(), tab);
	}

	/**
	 * Gets the statisticsTab.
	 * @return the statisticsTab
	 */
	public StatisticsTab getStatisticsTab() {
		return statisticsTab;
	}

	/**
	 * Gets the tabbedPane.
	 * @return the tabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Gets the instance.
	 * @return the instance.
	 */
	public static KeldagrimFrame getInstance() {
		return INSTANCE;
	}

}

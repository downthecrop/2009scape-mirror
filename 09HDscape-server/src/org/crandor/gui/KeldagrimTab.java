package org.crandor.gui;

import javax.swing.JPanel;
import java.awt.Rectangle;

/**
 * The Keldagrim tab.
 * @author Austin
 */
public class KeldagrimTab extends JPanel {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 2899642836866716523L;

	/**
	 * The name of the Keldagrim tab.
	 */
	private final String name;

	/**
	 * Constructs a new {@Code KeldagrimTab} {@Code Object}
	 * @param name the name.
	 */
	public KeldagrimTab(String name) {
		super();
		this.name = name;
		setLayout(null);
		setBounds(new Rectangle(0, 0, 1068, 663));
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}

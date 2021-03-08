package core.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The console tab.
 *
 * @author Vexia
 */
public class ConsoleTab extends JPanel {

    /**
     * The serial UID.
     */
    private static final long serialVersionUID = 2899642836866716523L;

    /**
     * The name of the console tab.
     */
    private final String name;

    /**
     * Constructs a new {@Code consoleTab} {@Code Object}
     *
     * @param name the name.
     */
    public ConsoleTab(String name) {
        super();
        this.name = name;
        setLayout(null);
        setBounds(new Rectangle(0, 0, 1068, 663));
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

}

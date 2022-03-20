package org.runite.client;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;

final class ComponentWrappedCanvas extends Canvas {

    private final Component component;

    public final void update(Graphics g) {
        this.component.update(g);
    }

    public final void paint(Graphics g) {
        this.component.paint(g);
    }

    public ComponentWrappedCanvas(Component component) {
        this.component = component;
    }

}

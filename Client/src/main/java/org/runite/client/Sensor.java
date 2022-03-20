package org.runite.client;

import java.awt.*;
import java.awt.image.BufferedImage;

class Sensor {

    private final Robot robot = new Robot();
    private Component component;

    public Sensor() throws Exception {
    }

    public void setCursor(Component component, Point hotSpot, int width, int height, int[] rgb) {
        if (rgb == null) {
            component.setCursor(null);
        } else {
            BufferedImage image = new BufferedImage(width, height, 2);
            image.setRGB(0, 0, width, height, rgb, 0, width);
            component.setCursor(component.getToolkit().createCustomCursor(image, hotSpot, null));
        }
    }

    public void moveMouse(int x, int y) {
        this.robot.mouseMove(x, y);
    }

    // TODO Is unsetComponent the right name?
    public void updateComponent(Component component, boolean unsetComponent) {
        if (unsetComponent) {
            component = null;
        } else if (component == null) {
            throw new NullPointerException();
        }

        if (component != this.component) {
            if (this.component != null) {
                this.component.setCursor(null);
                this.component = null;
            }

            if (component != null) {
                component.setCursor(component.getToolkit().createCustomCursor(new BufferedImage(1, 1, 2), new Point(0, 0), null));
                this.component = component;
            }
        }

    }
}

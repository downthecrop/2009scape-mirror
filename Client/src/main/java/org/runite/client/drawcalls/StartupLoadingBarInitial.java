package org.runite.client.drawcalls;

import org.runite.client.*;

import java.awt.*;

public class StartupLoadingBarInitial {


    public static FontMetrics aFontMetrics1822;
    public static Image anImage2695;

    public static void draw(Color var0, boolean var2, RSString var3, int var4) {

        try {
            Graphics var5 = GameShell.canvas.getGraphics();
            aFontMetrics1822 = GameShell.canvas.getFontMetrics(TextCore.Helvetica);
            if (var2) {
                var5.setColor(Color.black);
                var5.fillRect(0, 0, Class23.canvasWidth, GroundItem.canvasHeight);
            }

            try {
                if (anImage2695 == null) {
                    anImage2695 = GameShell.canvas.createImage(304, 34);
                }

                Graphics var6 = anImage2695.getGraphics();
                var6.setColor(var0);
                var6.drawRect(0, 0, 303, 33);
                var6.fillRect(2, 2, var4 * 3, 30);
                var6.setColor(Color.black);
                var6.drawRect(1, 1, 301, 31);
                var6.fillRect(3 * var4 + 2, 2, -(3 * var4) + 300, 30);
                var6.setFont(TextCore.Helvetica);
                var6.setColor(Color.white);
                var3.drawString(var6, 22, (-var3.method1575(aFontMetrics1822) + 304) / 2);
                var5.drawImage(anImage2695, Class23.canvasWidth / 2 - 152, -18 + GroundItem.canvasHeight / 2, null);
            } catch (Exception var9) {
                int var7 = -152 + Class23.canvasWidth / 2;
                int var8 = -18 + GroundItem.canvasHeight / 2;
                var5.setColor(var0);
                var5.drawRect(var7, var8, 303, 33);
                var5.fillRect(var7 + 2, 2 + var8, 3 * var4, 30);
                var5.setColor(Color.black);
                var5.drawRect(1 + var7, var8 - -1, 301, 31);
                var5.fillRect(3 * var4 + (var7 - -2), 2 + var8, 300 - var4 * 3, 30);
                var5.setFont(TextCore.Helvetica);
                var5.setColor(Color.white);
                var3.drawString(var5, 22 + var8, var7 + (-var3.method1575(aFontMetrics1822) + 304) / 2);
            }

            if (Class167.aString_2083 != null) {
                var5.setFont(TextCore.Helvetica);
                var5.setColor(Color.white);
                Class167.aString_2083.drawString(var5, GroundItem.canvasHeight / 2 - 26, Class23.canvasWidth / 2 - Class167.aString_2083.method1575(aFontMetrics1822) / 2);
            }

        } catch (Exception var10) {
            GameShell.canvas.repaint();
        }
    }
}

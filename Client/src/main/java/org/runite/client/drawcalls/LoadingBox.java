package org.runite.client.drawcalls;

import org.rs09.client.rendering.Toolkit;
import org.runite.client.*;

import java.awt.*;

public class LoadingBox {

    public static void draw(boolean var1, RSString var2) {
        try {
            byte var3 = 4;
            int var4 = var3 + 6;
            int var5 = var3 + 6;
            int var6 = FontType.plainFont.method680(var2, 250);
            int var7 = FontType.plainFont.method684(var2, 250) * 13;
            //Used for the top left (please wait...)
            Toolkit.getActiveToolkit().method934(var4 - var3, -var3 + var5, var3 + var6 - -var3, var3 + var3 + var7, 0);
            Toolkit.getActiveToolkit().drawRect(-var3 + var4, -var3 + var5, var6 + var3 - -var3, var3 + var7 + var3, 16777215, 255);

            FontType.plainFont.method676(var2, var4, var5, var6, var7, 16777215, -1, 1, 1, 0);

            Class21.method1340(var4 + -var3, var6 + (var3 - -var3), -var3 + var5, var3 + var7 + var3);
            if (var1) {
                if (HDToolKit.highDetail) {
                    HDToolKit.bufferSwap();
                } else {
                    try {
                        Graphics var8 = GameShell.canvas.getGraphics();
                        Unsorted.aClass158_3009.method2179(var8);
                    } catch (Exception var9) {
                        GameShell.canvas.repaint();
                    }
                }
            } else {
                Unsorted.method1282(var4, var5, var7, var6);
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "j.TA(" + ',' + var1 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }
}

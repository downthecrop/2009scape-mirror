package org.runite.client.drawcalls;

import org.rs09.client.rendering.Toolkit;
import org.runite.client.*;

public final class StartupLoadingBar {

    public static void draw(boolean var1, Font var2) {
        int var3;
        if (HDToolKit.highDetail || var1) {
            var3 = GroundItem.canvasHeight;
            int var4 = var3 * 956 / 503;
            Class40.aAbstractSprite_680.method639((Class23.canvasWidth + -var4) / 2, 0, var4, var3);
            SequenceDefinition.aClass109_1856.method1667(-(SequenceDefinition.aClass109_1856.width / 2) + Class23.canvasWidth / 2, 18);
        }

        var2.method699(TextCore.RSLoadingPleaseWait, Class23.canvasWidth / 2, GroundItem.canvasHeight / 2 - 26, 16777215, -1);
        var3 = GroundItem.canvasHeight / 2 + -18;
        Toolkit.getActiveToolkit().drawRect(Class23.canvasWidth / 2 - 152, var3, 304, 34, 9179409, 255);
        Toolkit.getActiveToolkit().drawRect(-151 + Class23.canvasWidth / 2, var3 - -1, 302, 32, 0, 255);
        Toolkit.getActiveToolkit().method934(Class23.canvasWidth / 2 - 150, var3 + 2, Client.LoadingStageNumber * 3, 30, 9179409);
        Toolkit.getActiveToolkit().method934(Class23.canvasWidth / 2 + -150 - -(3 * Client.LoadingStageNumber), 2 + var3, 300 + -(3 * Client.LoadingStageNumber), 30, 0);

        var2.method699(Client.loadingBarTextToDisplay, Class23.canvasWidth / 2, 4 + GroundItem.canvasHeight / 2, 16777215, -1);
    }
}

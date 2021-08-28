package org.runite.client.drawcalls;

import org.rs09.client.config.GameConfig;
import org.rs09.client.rendering.Toolkit;
import org.runite.client.*;

public final class ContextMenu {

    public static void draw() {
        int x = Class21.anInt1462;
        int y = Class21.anInt3395;
        int width = Class21.anInt3552;
        int height = Class21.anInt3537;
        int contextMenuColor = 6116423; //Context Menu RGB || 6116423 Classic || Old var5 || ColorCore.getHexColors()
        Toolkit.getActiveToolkit().fillRect(1 + x, y + 18, width + -2, -19 + height, GameConfig.RCM_BG_COLOR, GameConfig.getRCM_BG_OPACITY());
        if (GameConfig.RS3_CONTEXT_STYLE) {
            Toolkit.getActiveToolkit().fillRect(1 + x, 2 + y, width + -2, 16, GameConfig.RCM_TITLE_COLOR, GameConfig.getRCM_TITLE_OPACITY());
            Toolkit.getActiveToolkit().drawRect(1 + x, 1 + y, width + -2, height, GameConfig.RCM_BORDER_COLOR, GameConfig.getRCM_BORDER_OPACITY());
        } else {
            Toolkit.getActiveToolkit().fillRect(1 + x, 2 + y, width + -2, 16, GameConfig.RCM_TITLE_COLOR, GameConfig.getRCM_TITLE_OPACITY());
            Toolkit.getActiveToolkit().drawRect(1 + x, y + 18, width + -2, -19 + height, GameConfig.RCM_BORDER_COLOR, GameConfig.getRCM_BORDER_OPACITY());
        }

        FontType.bold.method681(RSString.parse(GameConfig.RCM_TITLE), x - -3, y + 14, contextMenuColor, -1);
        int var7 = Unsorted.anInt1709;
        int var6 = Class126.anInt1676;

        for (int var8 = 0; var8 < Unsorted.menuOptionCount; ++var8) {
            int var9 = (-var8 + -1 + Unsorted.menuOptionCount) * 15 + y - -31;
            int var10 = 16777215;
            if (var6 > x && x - -width > var6 && -13 + var9 < var7 && 3 + var9 > var7) {
                var10 = 16776960;
            }

            FontType.bold.method681(Unsorted.method802(var8), x - -3, var9, var10, 0);
        }

        Unsorted.method1282(Class21.anInt1462, Class21.anInt3395, Class21.anInt3537, Class21.anInt3552);
    }
}

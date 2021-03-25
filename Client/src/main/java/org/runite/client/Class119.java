package org.runite.client;

import java.awt.*;

final class Class119 {

    static Class131 aClass131_1624;
    static Class33 aClass33_1626;
    static CacheIndex aClass153_1628;


    static void method1729() {
        try {
            Object var1;
            if (null == TextureOperation30.fullScreenFrame) {
                if (GameShell.frame == null) {
                    var1 = Class38.gameSignlink.gameApplet;
                } else {
                    var1 = GameShell.frame;
                }
            } else {
                var1 = TextureOperation30.fullScreenFrame;
            }

            Unsorted.frameWidth = ((Container) var1).getSize().width;
            Class70.frameHeight = ((Container) var1).getSize().height;
            Insets var2;
            if (var1 == GameShell.frame) {
                var2 = GameShell.frame.getInsets();
                Class70.frameHeight -= var2.bottom + var2.top;
                Unsorted.frameWidth -= var2.right + var2.left;
            }

            if (Class83.getWindowType() >= 2) {
                Class23.canvasWidth = Unsorted.frameWidth;
                Class84.leftMargin = 0;
                Class106.rightMargin = 0;
                Class140_Sub7.canvasHeight = Class70.frameHeight;
            } else {
                Class106.rightMargin = 0;
                Class84.leftMargin = (-765 + Unsorted.frameWidth) / 2;
                Class140_Sub7.canvasHeight = 503;
                Class23.canvasWidth = 765;
            }

            if (HDToolKit.highDetail) {
                HDToolKit.method1854(Class23.canvasWidth, Class140_Sub7.canvasHeight);
            }

            GameShell.canvas.setSize(Class23.canvasWidth, Class140_Sub7.canvasHeight);
            if (var1 == GameShell.frame) {
                var2 = GameShell.frame.getInsets();
                GameShell.canvas.setLocation(var2.left + Class84.leftMargin, Class106.rightMargin + var2.top);
            } else {
                GameShell.canvas.setLocation(Class84.leftMargin, Class106.rightMargin);
            }

            if (ConfigInventoryDefinition.anInt3655 != -1) {
                Class124.method1746(true, (byte) -125);
            }

            Unsorted.method1396(-1);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "qh.C(" + true + ')');
        }
    }

    static void method1730(Signlink var0) {
        try {
            RandomAccessFileWrapper var2 = null;

            try {
                Class64 var3 = var0.method1433("runescape", 12);

                while (0 == var3.anInt978) {
                    TimeUtils.sleep(1L);
                }

                if (var3.anInt978 == 1) {
                    var2 = (RandomAccessFileWrapper) var3.anObject974;
                    DataBuffer var4 = Class23.method939();
                    var2.write(var4.buffer, var4.index, 0);
                }
            } catch (Exception var6) {
            }

            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (Exception var5) {
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "qh.A(" + (var0 != null ? "{...}" : "null") + ',' + (byte) 14 + ')');
        }
    }

}

package org.runite.client;

import java.awt.*;
import java.lang.reflect.Field;

public class Display {

    private GraphicsDevice graphicsDevice;
    private DisplayMode displayMode;


    public Display() throws Exception {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = env.getDefaultScreenDevice();

        if (!this.graphicsDevice.isFullScreenSupported()) {
            for (int i = 0; env.getScreenDevices().length > i; ++i) {
                GraphicsDevice device = env.getScreenDevices()[i];
                if (device != null && device.isFullScreenSupported()) {
                    this.graphicsDevice = device;
                    return;
                }
            }

            throw new Exception();
        }
    }

    private void method917(Frame var1) {
        boolean var3 = false;

        Field var4;
        boolean var5;
        try {
            var4 = Class.forName("sun.awt.Win32GraphicsDevice").getDeclaredField("valid");
            var4.setAccessible(true);
            var5 = ((Boolean) var4.get(this.graphicsDevice)).booleanValue();
            if (var5) {
                var4.set(this.graphicsDevice, Boolean.FALSE);
                var3 = true;
            }
        } catch (Throwable var15) {
        }

        var5 = false;

        try {
            var5 = true;
            this.graphicsDevice.setFullScreenWindow(var1);
            var5 = false;
        } finally {
            if (var5 && var3) {
                try {
                    Field var7 = Class.forName("sun.awt.Win32GraphicsDevice").getDeclaredField("valid");
                    var7.set(this.graphicsDevice, Boolean.TRUE);
                } catch (Throwable var13) {
                }
            }

        }

        if (var3) {
            try {
                var4 = Class.forName("sun.awt.Win32GraphicsDevice").getDeclaredField("valid");
                var4.set(this.graphicsDevice, Boolean.TRUE);
            } catch (Throwable var14) {
            }
        }

    }

    public void method918(int var1, int var2, int var3, int var4, Frame var5, int var6) {
        this.displayMode = this.graphicsDevice.getDisplayMode();
        if (var1 > -6) {
            this.method919(false);
        }

        if (this.displayMode == null) {
            throw new NullPointerException();
        } else {
            var5.setUndecorated(true);
            var5.enableInputMethods(false);
            this.method917(var5);
            if (var2 == 0) {
                int var7 = this.displayMode.getRefreshRate();
                DisplayMode[] var8 = this.graphicsDevice.getDisplayModes();
                boolean var9 = false;

                for (int var10 = 0; var10 < var8.length; ++var10) {
                    if (var8[var10].getWidth() == var6 && var8[var10].getHeight() == var4 && var3 == var8[var10].getBitDepth()) {
                        int var11 = var8[var10].getRefreshRate();
                        if (!var9 || Math.abs(var2 - var7) > Math.abs(var11 - var7)) {
                            var9 = true;
                            var2 = var11;
                        }
                    }
                }

                if (!var9) {
                    var2 = var7;
                }
            }

            this.graphicsDevice.setDisplayMode(new DisplayMode(var6, var4, var3, var2));
        }
    }

    public int[] method919(boolean var1) {
        if (var1) {
            DisplayMode[] var2 = this.graphicsDevice.getDisplayModes();
            int[] var3 = new int[var2.length << 2];

            for (int var4 = 0; var4 < var2.length; ++var4) {
                var3[var4 << 2] = var2[var4].getWidth();
                var3[1 + (var4 << 2)] = var2[var4].getHeight();
                var3[(var4 << 2) - -2] = var2[var4].getBitDepth();
                var3[3 + (var4 << 2)] = var2[var4].getRefreshRate();
            }

            return var3;
        } else {
            return null;
        }
    }

    public void method920() {
        if (this.displayMode != null) {
            this.graphicsDevice.setDisplayMode(this.displayMode);
            if (!this.graphicsDevice.getDisplayMode().equals(this.displayMode)) {
                throw new RuntimeException("Did not return to correct resolution!");
            }

            this.displayMode = null;
        }

        this.method917(null);
    }
}

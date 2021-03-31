package org.runite.client;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;

public abstract class GameShell extends Applet implements Runnable, FocusListener, WindowListener {

    public static Canvas canvas;
    public static Frame frame;
    static Thread aThread409;
    private static int anInt950;
    private static volatile boolean hasWindowFocus = true;
    private static long aLong2313 = 0L;
    private static int anInt1737 = 1;
    private static boolean aBoolean1784 = false;
    private boolean displayError = false;

    private static void getMaxMemory() {
        Class3_Sub24_Sub3.maxClientMemory = (int) (Runtime.getRuntime().maxMemory() / 1048576L) + 1;
    }

    static void method34() {
        try {
            if (null != WorldListEntry.aAudioChannel_2627) {
                WorldListEntry.aAudioChannel_2627.method2163();
            }

            if (Class3_Sub21.aAudioChannel_2491 != null) {
                Class3_Sub21.aAudioChannel_2491.method2163();
            }

            Class140_Sub3.method1959(TextureOperation17.stereoSound);
            WorldListEntry.aAudioChannel_2627 = AudioChannel.method1195(22050, Class38.gameSignlink, canvas, 0);
            WorldListEntry.aAudioChannel_2627.method2154(Client.aClass3_Sub24_Sub4_1193);
            Class3_Sub21.aAudioChannel_2491 = AudioChannel.method1195(2048, Class38.gameSignlink, canvas, 1);
            Class3_Sub21.aAudioChannel_2491.method2154(Class3_Sub26.aClass3_Sub24_Sub2_2563);
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.DA(" + -32589 + ')');
        }
    }

    public static void provideSignlink(Signlink var0) {
        try {
            Class38.gameSignlink = var0;
            TextureOperation30.signlink = var0;
            TextureOperation12.method445();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.providesignlink(" + (var0 != null ? "{...}" : "null") + ')');
        }
    }

    public static void launchDesktop() {
        //GameShell.setDesktop(true);
        ClientLoader.create().launch();
    }

    public final void focusLost(FocusEvent var1) {
        try {
            hasWindowFocus = false;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.focusLost(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    abstract void method25();

    public final void windowClosing(WindowEvent var1) {
        try {
            this.destroy();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.windowClosing(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public final void windowIconified(WindowEvent var1) {
    }

    public final void windowDeactivated(WindowEvent var1) {
    }

    public final AppletContext getAppletContext() {
        try {
            return null != frame ? null : (Class38.gameSignlink != null && this != Class38.gameSignlink.gameApplet ? Class38.gameSignlink.gameApplet.getAppletContext() : super.getAppletContext());
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.getAppletContext()");
        }
    }

    public final void focusGained(FocusEvent var1) {
        try {
            hasWindowFocus = true;
            TextureOperation30.fullRedraw = true;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.focusGained(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public final void windowClosed(WindowEvent var1) {
    }

    final synchronized void addCanvas() {
            if (canvas != null) {
                canvas.removeFocusListener(this);
                canvas.getParent().remove(canvas);
            }

            Container var2;
            if (TextureOperation30.fullScreenFrame != null) {
                var2 = TextureOperation30.fullScreenFrame;
            } else if (null == frame) {
                var2 = Class38.gameSignlink.gameApplet;
            } else {
                var2 = frame;
            }

            var2.setLayout(null);
            canvas = new ComponentWrappedCanvas(this);
            var2.add(canvas);
            canvas.setSize(Class23.canvasWidth, Class140_Sub7.canvasHeight);
            canvas.setVisible(true);
            if (var2 == frame) {
                Insets var3 = frame.getInsets();
                canvas.setLocation(Class84.leftMargin + var3.left, var3.top + Class106.rightMargin);
            } else {
                canvas.setLocation(Class84.leftMargin, Class106.rightMargin);
            }

            canvas.addFocusListener(this);
            canvas.requestFocus();
            hasWindowFocus = true;
            TextureOperation30.fullRedraw = true;
            TextureOperation26.aBoolean3078 = true;
            Class3_Sub28_Sub5.forceReplaceCanvasEnable = false;
            SequenceDefinition.aLong1847 = TimeUtils.time();
            ClientCommands.tweeningEnabled = true;
    }

    public final void destroy() {
        try {
            if (this == LinkableRSString.anApplet_Sub1_2588 && !Class29.aBoolean554) {
                aLong2313 = TimeUtils.time();
                TimeUtils.sleep(5000L);
                TextureOperation30.signlink = null;
                this.method35(false);
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.destroy()");
        }
    }

    public final void update(Graphics g) {
        this.paint(g);
    }

    final void errorPrint(String var1) {
        try {
            if (!this.displayError) {
                this.displayError = true;
                System.out.println("error_game_" + var1);
                JOptionPane.showMessageDialog(frame, "Error: " + var1 + (var1.contains("js5connect") ? ". The game is likely down." : "") + "\nCheck Discord (https://discord.gg/43YPGND) in a relevant #help channel, and a kind user might be able to help you out.");
                try {
                    Objects.requireNonNull(this.getAppletContext()).showDocument(new URL(this.getCodeBase(), "error_game_" + var1 + ".ws"), "_top");
                } catch (Exception var4) {
                }

            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "rc.U(" + (var1 != null ? "{...}" : "null") + ',' + -48 + ')');
        }
    }

    abstract void method32();

    abstract void method33();

    public final URL getDocumentBase() {
        try {
            return null != frame ? null : (Class38.gameSignlink != null && this != Class38.gameSignlink.gameApplet ? Class38.gameSignlink.gameApplet.getDocumentBase() : super.getDocumentBase());
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.getDocumentBase()");
        }
    }

    public final synchronized void paint(Graphics g) {
        if (LinkableRSString.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
            TextureOperation30.fullRedraw = true;

            if (aBoolean1784 && !HDToolKit.highDetail && -SequenceDefinition.aLong1847 + TimeUtils.time() > 1000) {
                Rectangle var2 = g.getClipBounds();

                if (var2 == null || Unsorted.frameWidth <= var2.width && var2.height >= Class70.frameHeight) {
                    Class3_Sub28_Sub5.forceReplaceCanvasEnable = true;
                }
            }
        }
    }

    public final void windowDeiconified(WindowEvent var1) {
    }

    private void method35(boolean var2) {
        try {
            synchronized (this) {
                if (Class29.aBoolean554) {
                    return;
                }

                Class29.aBoolean554 = true;
            }

            if (Class38.gameSignlink.gameApplet != null) {
                Class38.gameSignlink.gameApplet.destroy();
            }

            try {
                this.method32();
            } catch (Exception var8) {
            }

            if (canvas != null) {
                try {
                    canvas.removeFocusListener(this);
                    canvas.getParent().remove(canvas);
                } catch (Exception var7) {
                }
            }

            if (null != Class38.gameSignlink) {
                try {
                    Class38.gameSignlink.method1445(0);
                } catch (Exception var6) {
                }
            }

            this.method33();

            if (null != frame) {
                try {
                    System.exit(0);
                } catch (Throwable var5) {
                }
            }

            System.out.println("Shutdown complete - clean:" + var2);
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "rc.EA(" + ',' + var2 + ')');
        }
    }

    public final void windowActivated(WindowEvent var1) {
    }

    private void method36() {
        try {
            long var2 = TimeUtils.time();
            Class134.aLongArray1766[Unsorted.anInt1953] = var2;
            Unsorted.anInt1953 = 31 & Unsorted.anInt1953 - -1;
            synchronized (this) {

                TextureOperation26.aBoolean3078 = hasWindowFocus;
            }

            this.method25();

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "rc.R(" + true + ')');
        }
    }

    private void method37() {
        try {
            long var2 = TimeUtils.time();
            long var4 = Class163_Sub1.aLongArray2986[anInt950];

            Class163_Sub1.aLongArray2986[anInt950] = var2;
            anInt950 = 31 & anInt950 + 1;
            if (var4 != 0 && var2 > var4) {
                int var6 = (int) (var2 + -var4);
                SequenceDefinition.anInt1862 = (32000 + (var6 >> 1)) / var6;
            }

            if (50 < TextureOperation28.anInt3313++) {
                TextureOperation30.fullRedraw = true;
                TextureOperation28.anInt3313 -= 50;
                canvas.setSize(Class23.canvasWidth, Class140_Sub7.canvasHeight);
                canvas.setVisible(true);
                if (frame != null && null == TextureOperation30.fullScreenFrame) {
                    Insets var8 = frame.getInsets();
                    canvas.setLocation(var8.left + Class84.leftMargin, Class106.rightMargin + var8.top);
                } else {
                    canvas.setLocation(Class84.leftMargin, Class106.rightMargin);
                }
            }

            this.method38();
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "rc.AA(" + 0 + ')');
        }
    }

    abstract void method38();

    public final URL getCodeBase() {
        return frame == null ? (null != Class38.gameSignlink && this != Class38.gameSignlink.gameApplet ? Class38.gameSignlink.gameApplet.getCodeBase() : super.getCodeBase()) : null;
    }

    public final void run() {
        try {
            try {
                if (Signlink.javaVendor != null) {

                    String vendor = Signlink.javaVendor.toLowerCase();

                    if (vendor.contains("sun") || vendor.contains("apple")) {

                        String var2 = Signlink.javaVersion;

                        if (var2.equals("1.1") || var2.startsWith("1.1.") || var2.equals("1.2") || var2.startsWith("1.2.")) {
                            this.errorPrint("wrongjava");
                            return;
                        }

                        anInt1737 = 5;
                    } else if (vendor.contains("ibm") && Signlink.javaVersion.equals("1.4.2")) {
                        this.errorPrint("wrongjava");
                        return;
                    }
                }

                int var7;
                if (null != Signlink.javaVersion && Signlink.javaVersion.startsWith("1.")) {
                    var7 = 2;

                    int var9;
                    for (var9 = 0; Signlink.javaVersion.length() > var7; ++var7) {
                        char var3 = Signlink.javaVersion.charAt(var7);
                        if (var3 < 48 || var3 > 57) {
                            break;
                        }

                        var9 = var9 * 10 - (-var3 + 48);
                    }

                    if (var9 >= 5) {
                        aBoolean1784 = true;
                    }
                }

                if (Class38.gameSignlink.gameApplet != null) {
                    Method var8 = Signlink.setFocusCycleRoot;
                    if (null != var8) {
                        try {
                            var8.invoke(Class38.gameSignlink.gameApplet, Boolean.TRUE);
                        } catch (Throwable var4) {
                        }
                    }
                }

                getMaxMemory();
                this.addCanvas();
                Unsorted.aClass158_3009 = TextureOperation18.method285(Class140_Sub7.canvasHeight, Class23.canvasWidth, canvas);
                this.method39();
                Class3_Sub25.aClass129_2552 = Class36.method1012();

                while (aLong2313 == 0 || aLong2313 > TimeUtils.time()) {
                    Class133.anInt1754 = Class3_Sub25.aClass129_2552.method1767(-1, anInt1737, WorldListEntry.anInt2626);

                    for (var7 = 0; var7 < Class133.anInt1754; ++var7) {
                        this.method36();
                    }

                    this.method37();
                    Class81.method1400(Class38.gameSignlink, canvas, -80);
                }
            } catch (Exception var5) {
                Class49.reportError(null, var5);
                this.errorPrint("crash");
            }

            this.method35(true);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "rc.run()");
        }
    }


    public final String getParameter(String var1) {
        try {
            return frame == null ? (Class38.gameSignlink != null && this != Class38.gameSignlink.gameApplet ? Class38.gameSignlink.gameApplet.getParameter(var1) : super.getParameter(var1)) : null;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.getParameter(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    abstract void method39();

    public final void stop() {
        try {
            if (LinkableRSString.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
                aLong2313 = 4000L + TimeUtils.time();
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.stop()");
        }
    }

    public abstract void init();

    final void launch() {
        try {
            Class140_Sub7.canvasHeight = 768;
            Class70.frameHeight = 768;
            Class84.leftMargin = 0;
            Class23.canvasWidth = 1024;
            Unsorted.frameWidth = 1024;
            Class106.rightMargin = 0;
            LinkableRSString.anApplet_Sub1_2588 = this;
            Frame frame = new Frame();
            frame.setTitle("Jagex");
            frame.setResizable(true);
            frame.addWindowListener(this);
            frame.setVisible(true);
            frame.toFront();
            Insets insets = frame.getInsets();
            frame.setSize(insets.left + Unsorted.frameWidth + insets.right, insets.top + Class70.frameHeight + insets.bottom);
            TextureOperation30.signlink = Class38.gameSignlink = new Signlink(null, 32 + TextureOperation20.paramModeWhat, "runescape", 29);
            Class64 var10 = Class38.gameSignlink.startThread(1, this);

            while (0 == Objects.requireNonNull(var10).anInt978) {
                TimeUtils.sleep(10L);
            }

            aThread409 = (Thread) var10.anObject974;
            //ClientLoader.create().launch();
        } catch (Exception var11) {
            Class49.reportError(null, var11);
        }
    }

    public final void windowOpened(WindowEvent var1) {
    }

    public final void start() {
        try {
            if (LinkableRSString.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
                aLong2313 = 0L;
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.start()");
        }
    }

    final void method41(int var3) {
        try {
            try {
                if (LinkableRSString.anApplet_Sub1_2588 != null) {
                    ++Class36.anInt639;
                    if (Class36.anInt639 >= 3) {
                        this.errorPrint("alreadyloaded");
                        return;
                    }

                    Objects.requireNonNull(this.getAppletContext()).showDocument(this.getDocumentBase(), "_self");
                    return;
                }
                LinkableRSString.anApplet_Sub1_2588 = this;
                Class106.rightMargin = 0;
                Class23.canvasWidth = 765;
                Unsorted.frameWidth = 765;
                Class84.leftMargin = 0;
                Class140_Sub7.canvasHeight = 503;
                Class70.frameHeight = 503;
                String var6 = this.getParameter("openwinjs");
                InterfaceWidget.aBoolean3594 = var6 != null && var6.equals("1");

                if (null == Class38.gameSignlink) {
                    TextureOperation30.signlink = Class38.gameSignlink = new Signlink(this, var3, null, 0);
                }

                Class64 var7 = Class38.gameSignlink.startThread(1, this);

                while (Objects.requireNonNull(var7).anInt978 == 0) {
                    TimeUtils.sleep(10L);
                }

                aThread409 = (Thread) var7.anObject974;
            } catch (Exception var8) {
                Class49.reportError(null, var8);
                this.errorPrint("crash");
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "rc.CA(" + (byte) -56 + ',' + 765 + ',' + var3 + ',' + 1530 + ',' + 503 + ')');
        }
    }

}

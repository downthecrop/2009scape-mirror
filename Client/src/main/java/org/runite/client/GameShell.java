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
    static int anInt950;
    static volatile boolean hasWindowFocus = true;
    static long aLong2313 = 0L;
    static int anInt4033;
    static int anInt1737 = 1;
    static boolean aBoolean1784 = false;
    private boolean aBoolean1 = false;
    static int anInt3 = 0;
    static Frame frame;


    static boolean aBoolean6 = false;
    static RSString aClass94_8 = RSString.parse("");
    static RSString aClass94_9 = RSString.parse(")3)3)3");
    static boolean aBoolean11 = false;

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

    static RSString method27(RSString var0) {
        try {

            int var2 = Unsorted.method1602(var0);
            return var2 != -1 ? Class119.aClass131_1624.aClass94Array1721[var2].method1560(TextCore.aClass94_3192, TextCore.aClass94_4066) : TextCore.aClass94_4049;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.V(" + (var0 != null ? "{...}" : "null") + ',' + true + ')');
        }
    }

    public final AppletContext getAppletContext() {
        try {
            return null != frame ? null : (Class38.aClass87_665 != null && this != Class38.aClass87_665.applet ? Class38.aClass87_665.applet.getAppletContext() : super.getAppletContext());
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.getAppletContext()");
        }
    }

    public final void focusGained(FocusEvent var1) {
        try {
            hasWindowFocus = true;
            Class3_Sub13_Sub10.aBoolean3116 = true;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.focusGained(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method28() {
        try {
            Class143.aReferenceCache_1874.clear();

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.Q(" + true + ')');
        }
    }

    public final void windowClosed(WindowEvent var1) {
    }

    final synchronized void method30(byte var1) {
        try {
            if (canvas != null) {
                canvas.removeFocusListener(this);
                canvas.getParent().remove(canvas);
            }

            Object var2;
            if (Class3_Sub13_Sub10.aFrame3121 != null) {
                var2 = Class3_Sub13_Sub10.aFrame3121;
            } else if (null == frame) {
                var2 = Class38.aClass87_665.applet;
            } else {
                var2 = frame;
            }

            ((Container) var2).setLayout((LayoutManager) null);
            canvas = new ComponentWrappedCanvas(this);
            if (var1 >= 30) {
                ((Container) var2).add(canvas);
                canvas.setSize(Class23.anInt454, Class140_Sub7.anInt2934);
                canvas.setVisible(true);
                if (var2 == frame) {
                    Insets var3 = frame.getInsets();
                    canvas.setLocation(Class84.anInt1164 + var3.left, var3.top + Class106.anInt1442);
                } else {
                    canvas.setLocation(Class84.anInt1164, Class106.anInt1442);
                }

                canvas.addFocusListener(this);
                canvas.requestFocus();
                hasWindowFocus = true;
                Class3_Sub13_Sub10.aBoolean3116 = true;
                Class3_Sub13_Sub6.aBoolean3078 = true;
                Class3_Sub28_Sub5.forceReplaceCanvasEnable = false;
                SequenceDefinition.aLong1847 = TimeUtils.time();
                ClientCommands.tweeningEnabled = true;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "rc.BA(" + var1 + ')');
        }
    }

    public final void destroy() {
        try {
            if (this == LinkableRSString.anApplet_Sub1_2588 && !Class29.aBoolean554) {
                aLong2313 = TimeUtils.time();
                TimeUtils.sleep(5000L);
                Class3_Sub13_Sub10.aClass87_3125 = null;
                this.method35(46, false);
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.destroy()");
        }
    }

    public final void update(Graphics g) {
        this.paint(g);
    }

    final void method31(String var1) {
        try {
            if (!this.aBoolean1) {
                this.aBoolean1 = true;
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
            return null != frame ? null : (Class38.aClass87_665 != null && this != Class38.aClass87_665.applet ? Class38.aClass87_665.applet.getDocumentBase() : super.getDocumentBase());
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.getDocumentBase()");
        }
    }

    public final synchronized void paint(Graphics g) {
        if (LinkableRSString.anApplet_Sub1_2588 == this && !Class29.aBoolean554) {
            Class3_Sub13_Sub10.aBoolean3116 = true;

            if (aBoolean1784 && !HDToolKit.highDetail && -SequenceDefinition.aLong1847 + TimeUtils.time() > 1000) {
                Rectangle var2 = g.getClipBounds();

                if (var2 == null || Unsorted.anInt2334 <= var2.width && var2.height >= Class70.anInt1047) {
                    Class3_Sub28_Sub5.forceReplaceCanvasEnable = true;
                }
            }
        }
    }

    public final void windowDeiconified(WindowEvent var1) {
    }

    static void method34() {
        try {
            if (null != WorldListEntry.aClass155_2627) {
                WorldListEntry.aClass155_2627.method2163();
            }

            if (Class3_Sub21.aClass155_2491 != null) {
                Class3_Sub21.aClass155_2491.method2163();
            }

            Class140_Sub3.method1959(Class3_Sub13_Sub15.aBoolean3184);
            WorldListEntry.aClass155_2627 = Class58.method1195(22050, Class38.aClass87_665, canvas, 0);
            WorldListEntry.aClass155_2627.method2154(114, Client.aClass3_Sub24_Sub4_1193);
            Class3_Sub21.aClass155_2491 = Class58.method1195(2048, Class38.aClass87_665, canvas, 1);
            Class3_Sub21.aClass155_2491.method2154(-126, Class3_Sub26.aClass3_Sub24_Sub2_2563);
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.DA(" + -32589 + ')');
        }
    }

    private void method35(int var1, boolean var2) {
        try {
            synchronized (this) {
                if (Class29.aBoolean554) {
                    return;
                }

                Class29.aBoolean554 = true;
            }

            if (Class38.aClass87_665.applet != null) {
                Class38.aClass87_665.applet.destroy();
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

            if (null != Class38.aClass87_665) {
                try {
                    Class38.aClass87_665.method1445(0);
                } catch (Exception var6) {
                }
            }

            this.method33();
            if (var1 <= 31) {
                this.launch();
            }

            if (null != frame) {
                try {
                    System.exit(0);
                } catch (Throwable var5) {
                }
            }

            System.out.println("Shutdown complete - clean:" + var2);
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "rc.EA(" + var1 + ',' + var2 + ')');
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

                Class3_Sub13_Sub6.aBoolean3078 = hasWindowFocus;
            }

            this.method25();

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "rc.R(" + true + ')');
        }
    }

    public static void providesignlink(Signlink var0) {
        try {
            Class38.aClass87_665 = var0;
            Class3_Sub13_Sub10.aClass87_3125 = var0;
            Class3_Sub13_Sub1.method445();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.providesignlink(" + (var0 != null ? "{...}" : "null") + ')');
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

            if (50 < Class3_Sub13_Sub25.anInt3313++) {
                Class3_Sub13_Sub10.aBoolean3116 = true;
                Class3_Sub13_Sub25.anInt3313 -= 50;
                canvas.setSize(Class23.anInt454, Class140_Sub7.anInt2934);
                canvas.setVisible(true);
                if (frame != null && null == Class3_Sub13_Sub10.aFrame3121) {
                    Insets var8 = frame.getInsets();
                    canvas.setLocation(var8.left + Class84.anInt1164, Class106.anInt1442 + var8.top);
                } else {
                    canvas.setLocation(Class84.anInt1164, Class106.anInt1442);
                }
            }

            this.method38();
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "rc.AA(" + 0 + ')');
        }
    }

    abstract void method38();

    public final URL getCodeBase() {
        return frame == null ? (null != Class38.aClass87_665 && this != Class38.aClass87_665.applet ? Class38.aClass87_665.applet.getCodeBase() : super.getCodeBase()) : null;
    }


    public static void launchDesktop() {
        //GameShell.setDesktop(true);
        ClientLoader.create().launch();
    }


    public final void run() {
        try {
            try {
                if (null != Signlink.javaVendor) {
                    String var1 = Signlink.javaVendor.toLowerCase();
                    if (!var1.contains("sun") && !var1.contains("apple")) {
                        if (var1.contains("ibm") && Signlink.javaVendor.equals("1.4.2")) {
                            this.method31("wrongjava");
                            return;
                        }
                    } else {
                        String var2 = Signlink.javaVendor;
                        if (var2.equals("1.1") || var2.startsWith("1.1.") || var2.equals("1.2") || var2.startsWith("1.2.")) {
                            this.method31("wrongjava");
                            return;
                        }

                        anInt1737 = 5;
                    }
                }

                int var7;
                if (null != Signlink.javaVendor && Signlink.javaVendor.startsWith("1.")) {
                    var7 = 2;

                    int var9;
                    for (var9 = 0; Signlink.javaVendor.length() > var7; ++var7) {
                        char var3 = Signlink.javaVendor.charAt(var7);
                        if (var3 < 48 || 57 < var3) {
                            break;
                        }

                        var9 = var9 * 10 - (-var3 - -48);
                    }

                    if (var9 >= 5) {
                        aBoolean1784 = true;
                    }
                }

                if (null != Class38.aClass87_665.applet) {
                    Method var8 = Signlink.setFocusCycleRoot;
                    if (null != var8) {
                        try {
                            var8.invoke(Class38.aClass87_665.applet, new Object[]{Boolean.TRUE});
                        } catch (Throwable var4) {
                        }
                    }
                }

                Class3_Sub28_Sub18.method713();
                this.method30((byte) 120);
                Class164_Sub1.aClass158_3009 = Class3_Sub13_Sub23_Sub1.method285(Class140_Sub7.anInt2934, Class23.anInt454, canvas);
                this.method39();
                Class3_Sub25.aClass129_2552 = Class36.method1012();

                while (aLong2313 == 0 || aLong2313 > TimeUtils.time()) {
                    Class133.anInt1754 = Class3_Sub25.aClass129_2552.method1767(-1, anInt1737, WorldListEntry.anInt2626);

                    for (var7 = 0; var7 < Class133.anInt1754; ++var7) {
                        this.method36();
                    }

                    this.method37();
                    Class81.method1400(Class38.aClass87_665, canvas, -80);
                }
            } catch (Exception var5) {
                Class49.method1125(null, var5, (byte) 127);
                this.method31("crash");
            }

            this.method35(107, true);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "rc.run()");
        }
    }


    public final String getParameter(String var1) {
        try {
            return frame == null ? (Class38.aClass87_665 != null && this != Class38.aClass87_665.applet ? Class38.aClass87_665.applet.getParameter(var1) : super.getParameter(var1)) : null;
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

            try {
                Class140_Sub7.anInt2934 = 768;
                Class70.anInt1047 = 768;
                Class84.anInt1164 = 0;
                anInt4033 = 530;
                Class23.anInt454 = 1024;
                Unsorted.anInt2334 = 1024;
                Class106.anInt1442 = 0;
                LinkableRSString.anApplet_Sub1_2588 = this;
                Frame frame = new Frame();
                frame.setTitle("Jagex");
                frame.setResizable(true);
                frame.addWindowListener(this);
                frame.setVisible(true);
                frame.toFront();
                Insets var9 = frame.getInsets();
                frame.setSize(var9.left + Unsorted.anInt2334 + var9.right, var9.top + Class70.anInt1047 + var9.bottom);
                Class3_Sub13_Sub10.aClass87_3125 = Class38.aClass87_665 = new Signlink((Applet) null, 32 - -Class3_Sub13_Sub13.anInt3148, "runescape", 29);
                Class64 var10 = Class38.aClass87_665.method1451(1, this);

                while (0 == Objects.requireNonNull(var10).anInt978) {
                    TimeUtils.sleep(10L);
                }

                Class17.aThread409 = (Thread) var10.anObject974;
                ClientLoader.create().launch();
            } catch (Exception var11) {
                Class49.method1125((String) null, var11, (byte) 115);
            }

        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "rc.S(" + ',' + 1024 + ',' + "{...}" + ',' + 768 + ',' + -8057 + ',' + 28 + ')');
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
                        this.method31("alreadyloaded");
                        return;
                    }

                    Objects.requireNonNull(this.getAppletContext()).showDocument(this.getDocumentBase(), "_self");
                    return;
                }
                LinkableRSString.anApplet_Sub1_2588 = this;
                Class106.anInt1442 = 0;
                anInt4033 = 1530;
                Class23.anInt454 = 765;
                Unsorted.anInt2334 = 765;
                Class84.anInt1164 = 0;
                Class140_Sub7.anInt2934 = 503;
                Class70.anInt1047 = 503;
                String var6 = this.getParameter("openwinjs");
                InterfaceWidget.aBoolean3594 = var6 != null && var6.equals("1");

                if (null == Class38.aClass87_665) {
                    Class3_Sub13_Sub10.aClass87_3125 = Class38.aClass87_665 = new Signlink(this, var3, null, 0);
                }

                Class64 var7 = Class38.aClass87_665.method1451(1, this);

                while (Objects.requireNonNull(var7).anInt978 == 0) {
                    TimeUtils.sleep(10L);
                }

                Class17.aThread409 = (Thread) var7.anObject974;
            } catch (Exception var8) {
                Class49.method1125(null, var8, (byte) 113);
                this.method31("crash");
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "rc.CA(" + (byte) -56 + ',' + 765 + ',' + var3 + ',' + 1530 + ',' + 503 + ')');
        }
    }

}

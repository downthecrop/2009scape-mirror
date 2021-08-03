package org.runite.client;

import org.rs09.SystemLogger;

import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class Signlink implements Runnable {

    private static final Hashtable<String, File> cachedFiles = new Hashtable<>(18);
    public static String javaVersion;
    public static String osName;
    public static String osNameCS;
    public static int anInt1214 = 1;
    public static String javaVendor;
    public static String osArchitecture;
    public static Method setFocusCycleRoot;
    public static Method setTraversalKeysEnabled;
    static volatile long aLong1221 = 0L;
    private static String homeDirectory;
    private final Thread thread;
    private final String gameName;
    private final int anInt1215;
    public RandomAccessFileWrapper[] cacheIndicesFiles;
    public RandomAccessFileWrapper cacheDataFile;
    public EventQueue systemEventQueue;
    public RandomAccessFileWrapper cacheChecksumFile;
    public RandomAccessFileWrapper randomDatFile;
    public Applet gameApplet;
    private boolean stopped;
    private Class64 aClass64_1203 = null;
    private Sensor sensor;
    private Display display;
    private Class64 aClass64_1213 = null;
    private Interface1 anInterface1_1217;


    public Signlink(Applet applet, int var2, String gameName, int cacheIndexes) throws Exception {
        javaVersion = "1.1";
        this.gameName = gameName;
        this.anInt1215 = var2;
        this.gameApplet = applet;
        javaVendor = "Unknown";

        try {
            javaVendor = System.getProperty("java.vendor");
            javaVersion = System.getProperty("java.version");
        } catch (Exception var17) {
        }

        try {
            osNameCS = System.getProperty("os.name");
        } catch (Exception var16) {
            osNameCS = "Unknown";
        }

        osName = osNameCS.toLowerCase();

        try {
            osArchitecture = System.getProperty("os.arch").toLowerCase();
        } catch (Exception var15) {
            osArchitecture = "";
        }

        try {
            homeDirectory = System.getProperty("user.home");
            if (homeDirectory != null) {
                homeDirectory = homeDirectory + "/";
            }
        } catch (Exception var13) {
        }

        if (homeDirectory == null) {
            homeDirectory = "~/";
        }

        try {
            this.systemEventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        } catch (Throwable var12) {
        }

        try {
            if (applet == null) {
                setTraversalKeysEnabled = Class.forName("java.awt.Component").getDeclaredMethod("setFocusTraversalKeysEnabled", Boolean.TYPE);
            } else {
                setTraversalKeysEnabled = applet.getClass().getMethod("setFocusTraversalKeysEnabled", Boolean.TYPE);
            }
        } catch (Exception var11) {
        }

        try {
            if (applet == null) {
                setFocusCycleRoot = Class.forName("java.awt.Container").getDeclaredMethod("setFocusCycleRoot", Boolean.TYPE);
            } else {
                setFocusCycleRoot = applet.getClass().getMethod("setFocusCycleRoot", Boolean.TYPE);
            }
        } catch (Exception var10) {
        }

        this.randomDatFile = new RandomAccessFileWrapper(method1448(null, this.anInt1215, "random.dat"), "rw", 25L);
        this.cacheDataFile = new RandomAccessFileWrapper(method1448(this.gameName, this.anInt1215, "main_file_cache.dat2"), "rw", 104857600L);
        this.cacheChecksumFile = new RandomAccessFileWrapper(method1448(this.gameName, this.anInt1215, "main_file_cache.idx255"), "rw", 1048576L);
        this.cacheIndicesFiles = new RandomAccessFileWrapper[cacheIndexes];

        for (int i = 0; i < cacheIndexes; ++i) {
            this.cacheIndicesFiles[i] = new RandomAccessFileWrapper(method1448(this.gameName, this.anInt1215, "main_file_cache.idx" + i), "rw", 1048576L);
        }

        try {
            this.display = new Display();
        } catch (Throwable var9) {
            var9.printStackTrace();
        }

        try {
            this.sensor = new Sensor();
        } catch (Throwable var8) {
        }

        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

        for (ThreadGroup parent = threadGroup.getParent(); parent != null; parent = parent.getParent()) {
            threadGroup = parent;
        }

        Thread[] threads = new Thread[1000];
        threadGroup.enumerate(threads);

        for (Thread value : threads) {
            if (value != null && value.getName().startsWith("AWT")) {
                value.setPriority(1);
            }
        }

        this.stopped = false;
        this.thread = new Thread(this);
        this.thread.setPriority(10);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    private static RandomAccessFileWrapper method1438(boolean var0, String var1) {
        if (var0) {
            method1438(true, null);
        }

        String[] var2 = new String[]{"c:/rscache/", "/rscache/", homeDirectory, "c:/windows/", "c:/winnt/", "c:/", "/tmp/", ""};

        for (String var4 : var2) {
            if (var4.length() <= 0 || (new File(var4)).exists()) {
                try {
                    return new RandomAccessFileWrapper(new File(var4, "jagex_" + var1 + "_preferences.dat"), "rw", 10000L);
                } catch (Exception var6) {
                }
            }
        }

        return null;
    }

    public static File method1448(String gameName, int var1, String filename) {
        File cachedFile = cachedFiles.get(filename);
        if (cachedFile == null) {
            String[] basePaths = new String[]{homeDirectory, "c:/rscache/", "/rscache/", "c:/windows/", "c:/winnt/", "c:/", "/tmp/", ""};
            String[] folders = new String[]{".runite_rs", ".530file_store_" + var1};
            for (int i = 0; i < 2; ++i) {
                for (String folder : folders) {
                    for (String basePath : basePaths) {
                        String fullPath = basePath + folder + "/" + (gameName != null ? gameName + "/" : "") + filename;
                        RandomAccessFile raf = null;
                        try {
                            File file = new File(fullPath);
                            if (i != 0 || file.exists()) {
                                if (i != 1 || basePath.length() <= 0 || (new File(basePath)).exists()) {
                                    (new File(basePath + folder)).mkdir();
                                    if (gameName != null) {
                                        (new File(basePath + folder + "/" + gameName)).mkdir();
                                    }
                                    //								ClientLoader.getLibraryDownloader().updateDlls(var10.toString());
                                    raf = new RandomAccessFile(file, "rw");
                                    int var14 = raf.read();
                                    raf.seek(0L);
                                    raf.write(var14);
                                    raf.seek(0L);
                                    raf.close();
                                    cachedFiles.put(filename, file);
                                    return file;
                                }
                            }
                        } catch (Exception e) {
                            try {
                                if (raf != null) {
                                    raf.close();
                                }
                            } catch (Exception e2) {
                            }
                        }
                    }
                }
            }

            throw new RuntimeException();
        } else {
            return cachedFile;
        }
    }

    public final void method1431() {
        aLong1221 = TimeUtils.time() + 5000L;
    }

    public final boolean method1432(boolean var1) {
        if (var1) {
            this.cacheIndicesFiles = null;
        }

        return this.display != null;
    }

    public final Class64 method1433(String var1, int var2) {
        if (var2 != 12) {
            this.randomDatFile = null;
        }

        return this.method1435(12, 0, var1, 0);
    }

    public final void method1434(int[] var1, int var2, int var3, Component var4, Point var5, int var6) {
        if (var2 == 10000) {
            this.method1435(17, var6, new Object[]{var4, var1, var5}, var3);
        }
    }

    private Class64 method1435(int var1, int var2, Object var3, int var4) {
        Class64 var6 = new Class64();
        var6.anInt980 = var2;
        var6.anInt979 = var4;
        var6.anInt975 = var1;
        var6.anObject977 = var3;
        synchronized (this) {
            if (this.aClass64_1203 == null) {
                this.aClass64_1203 = this.aClass64_1213 = var6;
            } else {
                this.aClass64_1203.aClass64_976 = var6;
                this.aClass64_1203 = var6;
            }

            this.notify();
            return var6;
        }
    }

    public final Class64 method1436(Frame var1, int var2) {
        if (var2 <= 78) {
            this.gameApplet = null;
        }

        return this.method1435(7, 0, var1, 0);
    }

    public final Class64 method1439(boolean var1, URL var2) {
        if (var1) {
            this.cacheChecksumFile = null;
        }

        return this.method1435(4, 0, var2, 0);
    }

    public final Class64 method1441(byte var1, String address, int port) {
        //System.out.println("var1: " + var1 + ", add: " + address + ":" + port);
        return var1 != 8 ? null : this.method1435(1, 0, address, port);
    }

    public final void method1442(Class var1, int var2) {
        if (var2 == 0) {
            this.method1435(11, 0, var1, 0);
        }
    }

    public final Class64 method1443(Class var1, Class[] var2, int var3, String var4) {
        if (var3 > -7) {
            homeDirectory = null;
        }

        return this.method1435(8, 0, new Object[]{var1, var4, var2}, 0);
    }

    public final void run() {
//      byte dat2status = Update.updateExists(1, "main_file_cache.dat2");

        while (true) {
            Class64 var1;
            synchronized (this) {
                while (true) {
                    if (this.stopped) {
                        return;
                    }

                    if (this.aClass64_1213 != null) {
                        var1 = this.aClass64_1213;
                        this.aClass64_1213 = this.aClass64_1213.aClass64_976;
                        if (this.aClass64_1213 == null) {
                            this.aClass64_1203 = null;
                        }
                        break;
                    }

                    try {
                        this.wait();
                    } catch (InterruptedException var11) {
                    }
                }
            }

            try {
                int stage = var1.anInt975;
                if (stage == 1) {
                    if (TimeUtils.time() < aLong1221) {
                        throw new IOException();
                    }
//               System.out.println("Roar " + (String)var1.anObject977 + ", port " + var1.anInt979);
                    var1.anObject974 = new Socket(InetAddress.getByName((String) var1.anObject977), var1.anInt979);
                } else if (2 == stage) {
                    Thread var16 = new Thread((Runnable) var1.anObject977);
                    var16.setDaemon(true);
                    var16.start();
                    var16.setPriority(var1.anInt979);
                    var1.anObject974 = var16;
                } else if (stage == 4) {
                    if (TimeUtils.time() < aLong1221) {
                        throw new IOException();
                    }

                    var1.anObject974 = new DataInputStream(((URL) var1.anObject977).openStream());
                } else {
                    Object[] var3;
                    if (stage == 8) {
                        var3 = (Object[]) var1.anObject977;
                        if (((Class) var3[0]).getClassLoader() == null) {
                            throw new SecurityException();
                        }

                        var1.anObject974 = ((Class) var3[0]).getDeclaredMethod((String) var3[1], (Class[]) var3[2]);
                    } else if (stage == 9) {
                        var3 = (Object[]) var1.anObject977;
                        if (((Class) var3[0]).getClassLoader() == null) {
                            throw new SecurityException();
                        }

                        var1.anObject974 = ((Class) var3[0]).getDeclaredField((String) var3[1]);
                    } else {
                        String var4;
                        if (stage == 3) {
                            if (aLong1221 > TimeUtils.time()) {
                                throw new IOException();
                            }

                            var4 = (var1.anInt979 >> 24 & 0xFF) + "." + (var1.anInt979 >> 16 & 0xFF) + "." + (var1.anInt979 >> 8 & 0xFF) + "." + (255 & var1.anInt979);
                            var1.anObject974 = InetAddress.getByName(var4).getHostName();
                        } else if (stage == 5) {
                            var1.anObject974 = this.display.method919(true);
                        } else if (stage == 6) {
                            Frame var5 = new Frame("Jagex Full Screen");
                            var1.anObject974 = var5;
                            var5.setResizable(false);
                            this.display.method918(-56, var1.anInt980 & 65535, var1.anInt980 >> 16, 65535 & var1.anInt979, var5, var1.anInt979 >>> 16);
                        } else if (stage == 7) {
                            this.display.method920();
                        } else if (10 == stage) {
                            Class[] var17 = new Class[]{Class.forName("java.lang.Class"), Class.forName("java.lang.String")};
                            Runtime runtime = Runtime.getRuntime();
                            Method var7;
                            if (!osName.startsWith("mac")) {
                                var7 = Class.forName("java.lang.Runtime").getDeclaredMethod("loadLibrary0", var17);
                                var7.setAccessible(true);
                                var7.invoke(runtime, var1.anObject977, "jawt");
                                var7.setAccessible(false);
                            }
                            boolean is64Bit = osArchitecture.contains("64");
                            boolean isSunOS = osName.startsWith("sunos");
                            var7 = Class.forName("java.lang.Runtime").getDeclaredMethod("load0", var17);
                            var7.setAccessible(true);

                            SystemLogger.logInfo("Signlink - os Name: " + osName);
                            SystemLogger.logInfo("Signlink - os Arch: " + osArchitecture);
                            if (osArchitecture.equals("aarch64"))
                                SystemLogger.logWarn("Going into HD will fail - current libs do not support ARM.");
                            if (osName.startsWith("linux") || isSunOS) {
                                String[] libs = createLibs(isSunOS ? (is64Bit ? 7 : 6) : (is64Bit ? 5 : 4));
                                var7.invoke(runtime, var1.anObject977, libs[2]);
                                Class var8 = getClass().getClassLoader().loadClass("com.sun.opengl.impl.x11.DRIHack");
                                var8.getMethod("begin", new Class[0]).invoke(null);
                                var7.invoke(runtime, var1.anObject977, libs[0]);
                                var8.getMethod("end", new Class[0]).invoke(null);
                                var7.invoke(runtime, var1.anObject977, libs[1]);


                            } else if (osName.startsWith("mac")) {
                                String[] libs = createLibs(is64Bit ? 2 : 3);
                                try {
                                    var7.invoke(runtime, var1.anObject977, libs[0]);
                                    var7.invoke(runtime, var1.anObject977, libs[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                if (!osName.startsWith("win")) {
                                    throw new Exception();
                                }
                                String[] libs = createLibs(is64Bit ? 1 : 0);
                                //Windows has to load them this way because temporary files are illegal.
                                var7.invoke(runtime, var1.anObject977, method1448(this.gameName, this.anInt1215, libs[0]).toString());
                                var7.invoke(runtime, var1.anObject977, method1448(this.gameName, this.anInt1215, libs[1]).toString());
                            }

                            var7.setAccessible(false);
                        } else {
                            int var18;
                            if (stage == 11) {
                                Field var20 = Class.forName("java.lang.ClassLoader").getDeclaredField("nativeLibraries");
                                var20.setAccessible(true);
                                Vector var24 = (Vector) var20.get(((Class) var1.anObject977).getClassLoader());

                                for (var18 = 0; var18 < var24.size(); ++var18) {
                                    Object var26 = var24.elementAt(var18);
                                    Method var9 = var26.getClass().getDeclaredMethod("finalize");
                                    var9.setAccessible(true);
                                    var9.invoke(var26);
                                    var9.setAccessible(false);
                                    Field var10 = var26.getClass().getDeclaredField("handle");
                                    var10.setAccessible(true);
                                    var10.set(var26, new Integer(0));
                                    var10.setAccessible(false);
                                }

                                var20.setAccessible(false);
                            } else if (stage == 12) {
                                var4 = (String) var1.anObject977;
                                var1.anObject974 = method1438(false, var4);
                            } else if (stage == 14) {
                                int var22 = var1.anInt980;
                                int var23 = var1.anInt979;
                                this.sensor.moveMouse(var23, var22);
                            } else if (15 == stage) {
                                boolean var21 = var1.anInt979 != 0;
                                Component var27 = (Component) var1.anObject977;
                                this.sensor.updateComponent(var27, var21);
                            } else if (17 == stage) {
                                var3 = (Object[]) var1.anObject977;
                                this.sensor.setCursor((Component) var3[0], (Point) var3[2], var1.anInt979, var1.anInt980, (int[]) var3[1]);
                            } else {
                                if (16 != stage) {
                                    throw new Exception();
                                }

                                try {
                                    if (!osName.startsWith("win")) {
                                        throw new Exception();
                                    }

                                    var4 = (String) var1.anObject977;
                                    if (!var4.startsWith("http://") && !var4.startsWith("https://")) {
                                        throw new Exception();
                                    }

                                    String var25 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?&=,.%+-_#:/*";

                                    for (var18 = 0; var18 < var4.length(); ++var18) {
                                        if (var25.indexOf(var4.charAt(var18)) == -1) {
                                            throw new Exception();
                                        }
                                    }

                                    Runtime.getRuntime().exec("cmd /c start \"j\" \"" + var4 + "\"");
                                    var1.anObject974 = null;
                                } catch (Exception var12) {
                                    var1.anObject974 = var12;
                                }
                            }
                        }
                    }
                }

                var1.anInt978 = 1;
            } catch (ThreadDeath var13) {
                throw var13;
            } catch (Throwable var14) {
                var1.anInt978 = 2;
            }
        }
    }

    public String[] createLibs(int archive) throws Throwable {
        ArrayList<String> filenames = new ArrayList<>();
        String jogl;
        String awt;
        String glueGen = "";
        boolean isGluegenRequired = false;
        boolean is64Bit = osArchitecture.contains("64");

        jogl = (archive < 2 ? "jogl" : "libjogl") +
               (is64Bit ? "_64" : "_32") +
               (archive < 2 ? ".dll" : archive < 4 ? ".jnilib" : ".so");

        awt = (archive < 2 ? "jogl_awt" : "libjogl_awt") +
              (is64Bit ? "_64" : "_32") +
              (archive < 2 ? ".dll" : archive < 4 ? ".jnilib" : ".so");

        if(archive > 3) isGluegenRequired = true;

        if(isGluegenRequired) glueGen = "libgluegen-rt_" + (is64Bit ? "64" : "32") + ".so";

        if(archive >= 4) { //good, proper, correct Linux loading to allow multiple HD sessions
            File joglLib = File.createTempFile("jogl", "." + jogl.split("\\.")[1]);
            try (InputStream in = getClass().getResourceAsStream("/lib/" + jogl); OutputStream out = openOutputStream(joglLib)) {
                if (in == null) throw new FileNotFoundException("Needed library does not exist: " + jogl);
                copyFile(in, out);
                joglLib.deleteOnExit();
                filenames.add(joglLib.getAbsolutePath());
            }

            File awtLib = File.createTempFile("jogl_awt", "." + awt.split("\\.")[1]);
            try (InputStream in = getClass().getResourceAsStream("/lib/" + awt); OutputStream out = openOutputStream(awtLib)) {
                if (in == null) throw new FileNotFoundException("Needed library does not exist: " + awt);
                copyFile(in, out);
                awtLib.deleteOnExit();
                filenames.add(awtLib.getAbsolutePath());
            }

            if (isGluegenRequired) {
                File glueLib = File.createTempFile("libgluegen", ".so");
                try (InputStream in = getClass().getResourceAsStream("/lib/" + glueGen); OutputStream out = openOutputStream(glueLib)) {
                    if (in == null) throw new FileNotFoundException("Needed library does not exist: " + glueGen);
                    copyFile(in, out);
                    glueLib.deleteOnExit();
                    filenames.add(glueLib.getAbsolutePath());
                }
            }
        } else { //Shitty, fucking headass windows-specific loading that is backwards as fuck
            File jogLib = method1448(this.gameName, this.anInt1215, jogl);
            if(!jogLib.exists()){
                try (InputStream in = getClass().getResourceAsStream("/lib/" + jogl); OutputStream out = openOutputStream(jogLib)) {
                    if (in == null) throw new FileNotFoundException("Needed library does not exist: " + jogl);
                    copyFile(in, out);
                    filenames.add(jogLib.getName());
                }
            }

            File awtLib = method1448(this.gameName, this.anInt1215, awt);
            if(!awtLib.exists()){
                try (InputStream in = getClass().getResourceAsStream("/lib/" + awt); OutputStream out = openOutputStream(awtLib)) {
                    if (in == null) throw new FileNotFoundException("Needed library does not exist: " + awt);
                    copyFile(in, out);
                    filenames.add(awtLib.getName());
                }
            }
        }

        return filenames.toArray(new String[]{});

        /*String jogl = archive < 2 ? "jogl.dll" : archive < 4 ? "libjogl.jnilib" : "libjogl.so";
        String joglAwt = archive < 2 ? "jogl_awt.dll" : archive < 4 ? "libjogl_awt.jnilib" : "libjogl_awt.so";
        byte[] bs = CacheIndex.libIndex.getFile(archive, 0);
        if (bs == null || bs.length < 1) {
            System.err.println("Could not create native lib " + joglAwt + ", archive=" + archive + "!");
            return new String[]{};
        }
        FileOutputStream fos = new FileOutputStream(method1448(this.gameName, this.anInt1215, joglAwt));
        fos.write(bs);
        fos.flush();
        fos.close();
        bs = CacheIndex.libIndex.getFile(archive, 1);
        if (bs == null || bs.length < 1) {
            System.err.println("Could not create native lib " + jogl + ", archive=" + archive + "!");
            return;
        }
        fos = new FileOutputStream(method1448(this.gameName, this.anInt1215, jogl));
        fos.write(bs);
        fos.flush();
        fos.close();
        if (archive > 3) {
            bs = CacheIndex.libIndex.getFile(archive, 2);
            if (bs == null || bs.length < 1) {
                System.err.println("Could not create native lib libgluegen-rt.so, archive=" + archive + "!");
                return;
            }
            fos = new FileOutputStream(method1448(this.gameName, this.anInt1215, "libgluegen-rt.so"));
            fos.write(bs);
            fos.flush();
            fos.close();
        }*/
    }

    public final Class64 method1444(int var1, Class var2) {
        if (var1 > -13) {
            this.method1435(88, -20, null, 76);
        }

        return this.method1435(10, 0, var2, 0);
    }

    public final void method1445(int var1) {
        synchronized (this) {
            this.stopped = true;
            this.notifyAll();
        }

        try {
            this.thread.join();
        } catch (InterruptedException var8) {
        }

        if (var1 != 0) {
            method1438(false, null);
        }

        if (this.cacheDataFile != null) {
            try {
                this.cacheDataFile.close();
            } catch (IOException var7) {
            }
        }

        if (this.cacheChecksumFile != null) {
            try {
                this.cacheChecksumFile.close();
            } catch (IOException var6) {
            }
        }

        if (this.cacheIndicesFiles != null) {
            for (int var2 = 0; var2 < this.cacheIndicesFiles.length; ++var2) {
                if (this.cacheIndicesFiles[var2] != null) {
                    try {
                        this.cacheIndicesFiles[var2].close();
                    } catch (IOException var5) {
                    }
                }
            }
        }

        if (this.randomDatFile != null) {
            try {
                this.randomDatFile.close();
            } catch (IOException var4) {
            }
        }

    }

    public final Interface1 method1446(byte var1) {
        if (var1 < 71) {
            this.method1452(null, true);
        }

        //return this.anInterface1_1217;
        return null;
    }

    public final Class64 method1447(int var1, String var2, Class var3) {
        if (var1 > -39) {
            this.method1452(null, true);
        }

        return this.method1435(9, 0, new Object[]{var3, var2}, 0);
    }

    public final Class64 method1449(int var1, int var2) {
        if (var1 != 3) {
            this.cacheChecksumFile = null;
        }

        return this.method1435(3, 0, null, var2);
    }

    public final Class64 method1450(int var1, int var2, int var3, int var4) {
        return this.method1435(6, var1 + (var2 << 16), null, (var4 << 16) + var3);
    }

    public final Class64 startThread(int var2, Runnable var3) {
        return this.method1435(2, 0, var3, var2);
    }

    public final Class64 method1452(String var1, boolean var2) {
        if (!var2) {
            this.method1436(null, 101);
        }

        return this.method1435(16, 0, var1, 0);
    }

    public final Class64 method1453(byte var1) {
        if (var1 < 7) {
            this.method1443(null, null, -91, null);
        }

        return this.method1435(5, 0, null, 0);
    }

    private static FileOutputStream openOutputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    private static void copyFile(final InputStream input, final OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }
}

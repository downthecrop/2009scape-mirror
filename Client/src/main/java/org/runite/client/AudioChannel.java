package org.runite.client;

import org.rs09.client.util.ArrayUtils;

import java.awt.*;

class AudioChannel {

    public static boolean stereo;
    static int[] anIntArray1969 = new int[100];
    static int threadPriority;
    static AudioThread aAudioThread_2613;
    private final Class3_Sub24[] aClass3_Sub24Array1980 = new Class3_Sub24[8];
    private final Class3_Sub24[] aClass3_Sub24Array1983 = new Class3_Sub24[8];
    int[] samples;
    int sampleRate;
    int bufferSize;
    private long aLong1972 = TimeUtils.time();
    private Class3_Sub24 aClass3_Sub24_1973;
    private long aLong1979 = 0L;
    private int anInt1981 = 0;
    private long aLong1982 = 0L;
    private boolean aBoolean1984 = true;
    private int anInt1985 = 0;
    private int anInt1986;
    private int anInt1987 = 0;
    private int anInt1988 = 0;
    private int anInt1968 = 32;

    static void method2162(GameObject var0, int var1, int var2, int var3) {
        TileData var4;
        if (var2 < Unsorted.width1234) {
            var4 = TileData.aTileDataArrayArrayArray2638[var1][var2 + 1][var3];
            if (var4 != null && var4.aClass12_2230 != null && var4.aClass12_2230.object.method1865()) {
                var0.method1866(var4.aClass12_2230.object, 128, 0, 0, true);
            }
        }

        if (var3 < Unsorted.width1234) {
            var4 = TileData.aTileDataArrayArrayArray2638[var1][var2][var3 + 1];
            if (var4 != null && var4.aClass12_2230 != null && var4.aClass12_2230.object.method1865()) {
                var0.method1866(var4.aClass12_2230.object, 0, 0, 128, true);
            }
        }

        if (var2 < Unsorted.width1234 && var3 < TextureOperation17.height3179) {
            var4 = TileData.aTileDataArrayArrayArray2638[var1][var2 + 1][var3 + 1];
            if (var4 != null && var4.aClass12_2230 != null && var4.aClass12_2230.object.method1865()) {
                var0.method1866(var4.aClass12_2230.object, 128, 0, 128, true);
            }
        }

        if (var2 < Unsorted.width1234 && var3 > 0) {
            var4 = TileData.aTileDataArrayArrayArray2638[var1][var2 + 1][var3 - 1];
            if (var4 != null && var4.aClass12_2230 != null && var4.aClass12_2230.object.method1865()) {
                var0.method1866(var4.aClass12_2230.object, 128, 0, -128, true);
            }
        }

    }

    static AudioChannel method1195(int sampleRate, Signlink signlink, Component component, int channelID) {
        try {
            if (Class21.sampleRate == 0) {
                throw new IllegalStateException();
            } else if (0 <= channelID && 2 > channelID) {
                if (sampleRate < 256) {
                    sampleRate = 256;
                }

                try {
                    AudioChannel channel = new JavaAudioChannel();
                    channel.sampleRate = sampleRate;
                    channel.samples = new int[(!stereo ? 1 : 2) * 256];
                    channel.init(component);
                    channel.bufferSize = (sampleRate & 0xfffffc00) + 1024;
                    if (channel.bufferSize > 16384) {
                        channel.bufferSize = 16384;
                    }

                    channel.open(channel.bufferSize);
                    if (threadPriority > 0 && null == aAudioThread_2613) {
                        aAudioThread_2613 = new AudioThread();
                        aAudioThread_2613.aClass87_350 = signlink;
                        signlink.startThread(threadPriority, aAudioThread_2613);
                    }

                    if (aAudioThread_2613 != null) {
                        if (null != aAudioThread_2613.channels[channelID]) {
                            throw new IllegalArgumentException();
                        }

                        aAudioThread_2613.channels[channelID] = channel;
                    }

                    return channel;
                } catch (Throwable var7) {
                    try {
                        SignLinkAudioChannel signLinkAudioChannel = new SignLinkAudioChannel(signlink, channelID);
                        signLinkAudioChannel.samples = new int[256 * (stereo ? 2 : 1)];
                        signLinkAudioChannel.sampleRate = sampleRate;
                        signLinkAudioChannel.init(component);
                        signLinkAudioChannel.bufferSize = 16384;
                        signLinkAudioChannel.open(signLinkAudioChannel.bufferSize);
                        if (threadPriority > 0 && null == aAudioThread_2613) {
                            aAudioThread_2613 = new AudioThread();
                            aAudioThread_2613.aClass87_350 = signlink;
                            signlink.startThread(threadPriority, aAudioThread_2613);
                        }

                        if (aAudioThread_2613 != null) {
                            if (aAudioThread_2613.channels[channelID] != null) {
                                throw new IllegalArgumentException();
                            }

                            aAudioThread_2613.channels[channelID] = signLinkAudioChannel;
                        }

                        return signLinkAudioChannel;
                    } catch (Throwable var6) {
                        return new AudioChannel();
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "id.D(" + sampleRate + ',' + (signlink != null ? "{...}" : "null") + ',' + (component != null ? "{...}" : "null") + ',' + channelID + ',' + 14 + ')');
        }
    }

    void write() {
    }

    void open(int var1) throws Exception {
    }

    void flush() throws Exception {
    }

    private void method2152(int[] var1) {
        int var3 = 256;
        if (stereo) {
            var3 = 256 << 1;
        }

        ArrayUtils.zero(var1, 0, var3);
        this.anInt1987 -= 256;
        if (this.aClass3_Sub24_1973 != null && this.anInt1987 <= 0) {
            this.anInt1987 += Class21.sampleRate >> 4;
            Class97.method1591(this.aClass3_Sub24_1973);
            this.method2155(this.aClass3_Sub24_1973, this.aClass3_Sub24_1973.method412());
            int var4 = 0;
            int var5 = 255;

            int var6;
            label101:
            for (var6 = 7; var5 != 0; --var6) {
                int var7;
                int var8;
                if (var6 < 0) {
                    var7 = var6 & 0x3;
                    var8 = -(var6 >> 2);
                } else {
                    var7 = var6;
                    var8 = 0;
                }

                for (int var9 = var5 >>> var7 & 0x11111111; var9 != 0; var9 >>>= 4) {
                    if ((var9 & 0x1) != 0) {
                        var5 &= ~(1 << var7);
                        Class3_Sub24 var10 = null;
                        Class3_Sub24 var11 = this.aClass3_Sub24Array1980[var7];

                        while (var11 != null) {
                            Sound var12 = var11.aSound_2544;
                            if (var12 != null && var12.anInt2374 > var8) {
                                var5 |= 1 << var7;
                                var10 = var11;
                                var11 = var11.aClass3_Sub24_2546;
                            } else {
                                var11.aBoolean2545 = true;
                                int var13 = var11.method409();
                                var4 += var13;
                                if (var12 != null) {
                                    var12.anInt2374 += var13;
                                }

                                if (var4 >= anInt1968) {
                                    break label101;
                                }

                                Class3_Sub24 var14 = var11.method411();
                                if (var14 != null) {
                                    for (int var15 = var11.anInt2543; var14 != null; var14 = var11.method414()) {
                                        this.method2155(var14, var15 * var14.method412() >> 8);
                                    }
                                }

                                Class3_Sub24 var18 = var11.aClass3_Sub24_2546;
                                var11.aClass3_Sub24_2546 = null;
                                if (var10 == null) {
                                    this.aClass3_Sub24Array1980[var7] = var18;
                                } else {
                                    var10.aClass3_Sub24_2546 = var18;
                                }

                                if (var18 == null) {
                                    this.aClass3_Sub24Array1983[var7] = var10;
                                }

                                var11 = var18;
                            }
                        }
                    }

                    var7 += 4;
                    ++var8;
                }
            }

            for (var6 = 0; var6 < 8; ++var6) {
                Class3_Sub24 var16 = this.aClass3_Sub24Array1980[var6];

                Class3_Sub24 var17;
                for (this.aClass3_Sub24Array1980[var6] = this.aClass3_Sub24Array1983[var6] = null; var16 != null; var16 = var17) {
                    var17 = var16.aClass3_Sub24_2546;
                    var16.aClass3_Sub24_2546 = null;
                }
            }
        }

        if (this.anInt1987 < 0) {
            this.anInt1987 = 0;
        }

        if (this.aClass3_Sub24_1973 != null) {
            this.aClass3_Sub24_1973.method413(var1, 0, 256);
        }

        this.aLong1972 = TimeUtils.time();
    }

    final synchronized void method2153() {
        try {
            if (null != this.samples) {
                long var2 = TimeUtils.time();

                try {
                    if (0L != this.aLong1982) {
                        if (var2 < this.aLong1982) {
                            return;
                        }

                        this.open(this.bufferSize);
                        this.aBoolean1984 = true;
                        this.aLong1982 = 0L;
                    }

                    int var4 = this.method2157();
                    if (-var4 + this.anInt1985 > this.anInt1981) {
                        this.anInt1981 = -var4 + this.anInt1985;
                    }

                    int var5 = this.sampleRate - -this.anInt1986;
                    if (256 + var5 > 16384) {
                        var5 = 16128;
                    }

                    if (this.bufferSize < var5 + 256) {
                        this.bufferSize += 1024;
                        if (this.bufferSize > 16384) {
                            this.bufferSize = 16384;
                        }

                        this.close();
                        var4 = 0;
                        this.open(this.bufferSize);
                        if (this.bufferSize < 256 + var5) {
                            var5 = -256 + this.bufferSize;
                            this.anInt1986 = -this.sampleRate + var5;
                        }

                        this.aBoolean1984 = true;
                    }

                    while (var4 < var5) {
                        var4 += 256;
                        this.method2152(this.samples);
                        this.write();
                    }

                    if (this.aLong1979 < var2) {
                        if (this.aBoolean1984) {
                            this.aBoolean1984 = false;
                        } else {
                            if (this.anInt1981 == 0 && this.anInt1988 == 0) {
                                this.close();
                                this.aLong1982 = var2 - -2000L;
                                return;
                            }

                            this.anInt1986 = Math.min(this.anInt1988, this.anInt1981);
                            this.anInt1988 = this.anInt1981;
                        }

                        this.aLong1979 = 2000L + var2;
                        this.anInt1981 = 0;
                    }

                    this.anInt1985 = var4;
                } catch (Exception var7) {
                    this.close();
                    this.aLong1982 = var2 + 2000L;
                }

                try {
                    if (var2 > 500000L + this.aLong1972) {
                        var2 = this.aLong1972;
                    }

                    while (var2 > this.aLong1972 + 5000L) {
                        this.method2161();
                        this.aLong1972 += 256000 / Class21.sampleRate;
                    }
                } catch (Exception var6) {
                    this.aLong1972 = var2;
                }

            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "vh.Q(" + (byte) -34 + ')');
        }
    }

    final synchronized void method2154(Class3_Sub24 var2) {
        this.aClass3_Sub24_1973 = var2;
    }

    private void method2155(Class3_Sub24 var1, int var2) {
        try {
            int var4 = var2 >> 5;
            Class3_Sub24 var5 = this.aClass3_Sub24Array1983[var4];
            if (null == var5) {
                this.aClass3_Sub24Array1980[var4] = var1;
            } else {
                var5.aClass3_Sub24_2546 = var1;
            }

            this.aClass3_Sub24Array1983[var4] = var1;
            var1.anInt2543 = var2;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "vh.H(" + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + (byte) -24 + ')');
        }
    }

    int method2157() {
        try {
            return this.bufferSize;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "vh.B()");
        }
    }

    final synchronized void method2158() {
        try {
            this.aBoolean1984 = true;

            try {
                this.flush();
            } catch (Exception var3) {
                this.close();
                this.aLong1982 = TimeUtils.time() + 2000L;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vh.L(" + (byte) -78 + ')');
        }
    }

    final void method2159() {
        this.aBoolean1984 = true;
    }

    void close() {
    }

    private void method2161() {
        try {
            this.anInt1987 -= 256;
            if (0 > this.anInt1987) {
                this.anInt1987 = 0;
            }

            if (null != this.aClass3_Sub24_1973) {
                this.aClass3_Sub24_1973.method415(256);
            }

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vh.K(" + 256 + ',' + 1 + ')');
        }
    }

    final synchronized void method2163() {
        try {
            if (null != aAudioThread_2613) {
                boolean var2 = true;

                for (int var3 = 0; var3 < 2; ++var3) {
                    if (this == aAudioThread_2613.channels[var3]) {
                        aAudioThread_2613.channels[var3] = null;
                    }

                    if (null != aAudioThread_2613.channels[var3]) {
                        var2 = false;
                    }
                }

                if (var2) {
                    aAudioThread_2613.aBoolean345 = true;

                    while (aAudioThread_2613.aBoolean353) {
                        TimeUtils.sleep(50L);
                    }

                    aAudioThread_2613 = null;
                }
            }

            this.close();
            this.samples = null;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vh.P(" + false + ')');
        }
    }

    void init(Component var1) throws Exception {
    }

}

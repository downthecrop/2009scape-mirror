package org.runite.client;

import org.rs09.client.config.GameConfig;

public final class Class131 {

    public static int x1716;
    static RSString password = RSString.parse("");
    static RSString username = RSString.parse("");
    static int anInt1719 = -1;
    static CacheIndex skeletonsReferenceIndex;
    public short[] yArray1718;
    public int anInt1720;
    public RSString[] aStringArray1721;
    public int[] anIntArray1725;
    public short[] xArray1727;
    byte[] aByteArray1730;

    Class131(int var1) {
        try {
            this.anInt1720 = var1;
            this.aStringArray1721 = new RSString[this.anInt1720];
            this.yArray1718 = new short[this.anInt1720];
            this.anIntArray1725 = new int[this.anInt1720];
            this.aByteArray1730 = new byte[this.anInt1720];
            this.xArray1727 = new short[this.anInt1720];
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "se.<init>(" + var1 + ')');
        }
    }

    static int method1788(int var0, int var1, int var2, int var3, boolean var4) {
        try {
            if (var4) {
                int var5 = 15 & var3;
                int var7 = var5 >= 4 ? (var5 != 12 && var5 != 14 ? var1 : var0) : var2;
                int var6 = var5 < 8 ? var0 : var2;
                return ((var5 & 1) != 0 ? -var6 : var6) - -((2 & var5) != 0 ? -var7 : var7);
            } else {
                return 127;
            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "se.H(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method1790(int var0, int var1) {
        try {
            InterfaceWidget var3 = InterfaceWidget.getWidget(5, var0);
            var3.flagUpdate();
            var3.anInt3598 = var1;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "se.B(" + var0 + ',' + var1 + ',' + 95 + ')');
        }
    }

    static void method1793(RSString var0, RSString var1, int var2) {
        try {
            password = var1;
            Class7.anInt2161 = var2;
            username = var0;
            if (username.equalsString(RSString.parse("")) || password.equalsString(RSString.parse(""))) {
                Client.messageToDisplay = 3;
            } else if (CS2Script.userCurrentWorldID == -1) {
                Class163_Sub1_Sub1.anInt2246 = 0;
                Class163_Sub1_Sub1.anInt1616 = 0;
                Client.messageToDisplay = -3;
                Class163_Sub1_Sub1.adminLoginStage = 1;
                DataBuffer var4 = new DataBuffer(128);
                var4.writeByte(10);
                var4.writeShort((int) (Math.random() * 99999.0D));
                var4.writeShort(GameConfig.CLIENT_BUILD);
                var4.writeLong(username.toLong());
                var4.writeInt((int) (Math.random() * 9.9999999E7D));
                var4.writeString(password);
                var4.writeInt((int) (Math.random() * 9.9999999E7D));
                var4.rsaEncrypt(TextureOperation10.EXPONENT, TextureOperation31.MODULUS);
                TextureOperation12.outgoingBuffer.index = 0;
                TextureOperation12.outgoingBuffer.writeByte(210);
                TextureOperation12.outgoingBuffer.writeByte(var4.index);
                TextureOperation12.outgoingBuffer.putBytes(var4.buffer, var4.index);
            } else {
                Class24.method951();
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "se.C(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + (byte) -38 + ')');
        }
    }

    final boolean method1787(int var1) {
        try {

            return (this.aByteArray1730[var1] & 8) != 0;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "se.G(" + var1 + ',' + (byte) -124 + ')');
        }
    }

    public final boolean method1789(int var1, int var2) {
        try {
            if (var2 != 530) {
                this.method1794(-111, 26);
            }

            return (4 & this.aByteArray1730[var1]) != 0;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "se.A(" + var1 + ',' + var2 + ')');
        }
    }

    public final int method1791(int var1, int var2) {
        try {
            return var2 != 8 ? 35 : this.aByteArray1730[var1] & 3;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "se.I(" + var1 + ',' + var2 + ')');
        }
    }

    final boolean method1794(int var1, int var2) {
        try {
            if (var2 != -20138) {
                method1788(122, 38, -120, -29, false);
            }

            return 0 == (this.aByteArray1730[var1] & 16);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "se.E(" + var1 + ',' + var2 + ')');
        }
    }

}

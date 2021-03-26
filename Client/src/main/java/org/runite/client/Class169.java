package org.runite.client;


import javax.media.opengl.GL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class Class169 {

    private static final byte[] aByteArray2111 = new byte[16384];
    private final int anInt2108;
    boolean aBoolean2106 = true;
    private Class156 aClass156_2105;
    private ByteBuffer aByteBuffer2107;
    private ByteBuffer aByteBuffer2109;
    private Class156 aClass156_2110;
    private int anInt2112 = -1;


    public Class169() {
        GL var1 = HDToolKit.gl;
        int[] var2 = new int[1];
        var1.glGenTextures(1, var2, 0);
        this.anInt2108 = var2[0];
        Class31.anInt580 += 16384;
        HDToolKit.bindTexture2D(this.anInt2108);
        var1.glTexParameteri(3553, 10241, 9729);
        var1.glTexParameteri(3553, 10240, 9729);
        var1.glTexParameteri(3553, 10242, '\u812f');
        var1.glTexParameteri(3553, 10243, '\u812f');
    }

    final void method2281(int[][] var1, int var2, int var3) {
        DataBuffer var4 = new DataBuffer(1620);

        int var6;
        for (int var5 = 0; var5 <= 8; ++var5) {
            for (var6 = 0; var6 <= 8; ++var6) {
                if (HDToolKit.aBoolean1790) {
                    var4.writeFloat((float) var6 / 8.0F);
                    var4.writeFloat((float) var5 / 8.0F);
                    var4.writeFloat((float) (var6 * 128));
                    var4.writeFloat((float) var1[var6 + var2][var5 + var3]);
                    var4.writeFloat((float) (var5 * 128));
                } else {
                    var4.writeFloatLE((float) var6 / 8.0F);
                    var4.writeFloatLE((float) var5 / 8.0F);
                    var4.writeFloatLE((float) (var6 * 128));
                    var4.writeFloatLE((float) var1[var6 + var2][var5 + var3]);
                    var4.writeFloatLE((float) (var5 * 128));
                }
            }
        }

        if (HDToolKit.supportVertexBufferObject) {
            ByteBuffer var9 = ByteBuffer.wrap(var4.buffer, 0, var4.index);
            this.aClass156_2110 = new Class156();
            this.aClass156_2110.method2172(var9);
        } else {
            this.aByteBuffer2109 = ByteBuffer.allocateDirect(var4.index).order(ByteOrder.nativeOrder());
            this.aByteBuffer2109.put(var4.buffer, 0, var4.index);
            this.aByteBuffer2109.flip();
        }

        DataBuffer var8 = new DataBuffer(1536);

        for (var6 = 0; var6 < 8; ++var6) {
            for (int var7 = 0; var7 < 8; ++var7) {
                if (HDToolKit.aBoolean1790) {
                    var8.writeInt(var7 + (var6 + 1) * 9);
                    var8.writeInt(var7 + var6 * 9);
                    var8.writeInt(var7 + 1 + var6 * 9);
                    var8.writeInt(var7 + (var6 + 1) * 9);
                    var8.writeInt(var7 + 1 + var6 * 9);
                    var8.writeInt(var7 + 1 + (var6 + 1) * 9);
                } else {
                    var8.writeIntLE(var7 + (var6 + 1) * 9);
                    var8.writeIntLE(var7 + var6 * 9);
                    var8.writeIntLE(var7 + 1 + var6 * 9);
                    var8.writeIntLE(var7 + (var6 + 1) * 9);
                    var8.writeIntLE(var7 + 1 + var6 * 9);
                    var8.writeIntLE(var7 + 1 + (var6 + 1) * 9);
                }
            }
        }

        if (HDToolKit.supportVertexBufferObject) {
            ByteBuffer var10 = ByteBuffer.wrap(var8.buffer, 0, var8.index);
            this.aClass156_2105 = new Class156();
            this.aClass156_2105.method2170(var10);
        } else {
            this.aByteBuffer2107 = ByteBuffer.allocateDirect(var8.index).order(ByteOrder.nativeOrder());
            this.aByteBuffer2107.put(var8.buffer, 0, var8.index);
            this.aByteBuffer2107.flip();
        }

    }

    final void method2282(LDIndexedSprite var1, int var2, int var3) {
        byte[] var4 = var1.raster;
        int var5 = var1.width;
        int var6 = var2 * 128 + 1 + (var3 * 128 + 1) * var5;
        int var7 = 0;

        int var8;
        int var9;
        for (var8 = -128; var8 < 0; ++var8) {
            var7 = (var7 << 8) - var7;

            for (var9 = -128; var9 < 0; ++var9) {
                if (var4[var6++] != 0) {
                    ++var7;
                }
            }

            var6 += var5 - 128;
        }

        if (var7 != this.anInt2112) {
            this.anInt2112 = var7;
            var6 = var2 * 128 + 1 + (var3 * 128 + 1) * var5;
            var8 = 0;

            for (var9 = -128; var9 < 0; ++var9) {
                for (int var10 = -128; var10 < 0; ++var10) {
                    if (var4[var6] == 0) {
                        int var11 = 0;
                        if (var4[var6 - 1] != 0) {
                            ++var11;
                        }

                        if (var4[var6 + 1] != 0) {
                            ++var11;
                        }

                        if (var4[var6 - var5] != 0) {
                            ++var11;
                        }

                        if (var4[var6 + var5] != 0) {
                            ++var11;
                        }

                        aByteArray2111[var8++] = (byte) (17 * var11);
                    } else {
                        aByteArray2111[var8++] = 68;
                    }

                    ++var6;
                }

                var6 += var5 - 128;
            }

            GL var12 = HDToolKit.gl;
            ByteBuffer var13 = ByteBuffer.wrap(aByteArray2111);
            var13.limit(16384);
            HDToolKit.bindTexture2D(this.anInt2108);
            var12.glTexImage2D(3553, 0, 6406, 128, 128, 0, 6406, 5121, var13);

        }
    }

    final void method2284() {
        GL var1 = HDToolKit.gl;
        HDToolKit.bindTexture2D(this.anInt2108);
        if (this.aClass156_2110 == null) {
            if (HDToolKit.supportVertexBufferObject) {
                var1.glBindBufferARB('\u8892', 0);
            }

            var1.glInterleavedArrays(10791, 20, this.aByteBuffer2109);
        } else {
            this.aClass156_2110.method2169();
            var1.glInterleavedArrays(10791, 20, 0L);
        }
        HDToolKit.aBoolean1798 = false;

        if (this.aClass156_2105 == null) {
            if (HDToolKit.supportVertexBufferObject) {
                var1.glBindBufferARB('\u8893', 0);
            }

            var1.glDrawElements(4, 384, 5125, this.aByteBuffer2107);
        } else {
            this.aClass156_2105.method2171();
            var1.glDrawElements(4, 384, 5125, 0L);
        }

    }

}

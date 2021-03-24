package org.runite.client;


import org.rs09.client.LinkableInt;
import org.rs09.client.data.HashTable;

import javax.media.opengl.GL;
import java.nio.ByteBuffer;


final class Class37 {

    int anInt651;
    int anInt653;
    int anInt655;
    int anInt657;
    private Class156 aClass156_642;
    private int[] anIntArray643;
    private byte[] aByteArray644;
    private int[] anIntArray645;
    private Class156 aClass156_646;
    private ByteBuffer aByteBuffer647;
    private int[] anIntArray648;
    private int[] anIntArray649;
    private byte[] aByteArray650;
    private ByteBuffer aByteBuffer652;
    private byte[] aByteArray654;
    private HashTable aHashTable_656;

    final int method1018(Class43 var1, int var2, int var3, int var4, float var5, float var6, float var7) {
        long var8 = 0L;
        if ((var2 & 127) == 0 || (var4 & 127) == 0) {
            var8 = var2 + (var4 << 16);
            LinkableInt var10 = (LinkableInt) this.aHashTable_656.get(var8);
            if (var10 != null) {
                return var10.value;
            }
        }

        int var23 = var1.anInt713;
        float var11 = (float) (var1.anInt703 - var2);
        float var12 = (float) (var1.anInt697 - var3);
        float var13 = (float) (var1.anInt708 - var4);
        float var14 = (float) Math.sqrt(var11 * var11 + var12 * var12 + var13 * var13);
        float var15 = 1.0F / var14;
        var11 *= var15;
        var12 *= var15;
        var13 *= var15;
        float var16 = var14 / (float) ((var1.anInt698 << 7) + 64);
        float var17 = 1.0F - var16 * var16;
        if (var17 < 0.0F) {
            var17 = 0.0F;
        }

        float var18 = var11 * var5 + var12 * var6 + var13 * var7;
        if (var18 < 0.0F) {
            var18 = 0.0F;
        }

        float var19 = var18 * var17 * 2.0F;
        if (var19 > 1.0F) {
            var19 = 1.0F;
        }

        int var20 = (int) (var19 * (float) (var23 >> 16 & 255));
        if (var20 > 255) {
            var20 = 255;
        }

        int var21 = (int) (var19 * (float) (var23 >> 8 & 255));
        if (var21 > 255) {
            var21 = 255;
        }

        int var22 = (int) (var19 * (float) (var23 & 255));
        if (var22 > 255) {
            var22 = 255;
        }

        this.aByteArray654[this.anInt653] = (byte) var20;
        this.aByteArray644[this.anInt653] = (byte) var21;
        this.aByteArray650[this.anInt653] = (byte) var22;
        this.anIntArray645[this.anInt653] = var2;
        this.anIntArray649[this.anInt653] = var3;
        this.anIntArray648[this.anInt653] = var4;
        this.aHashTable_656.put(var8, new LinkableInt(this.anInt653));
        return this.anInt653++;
    }

    final void method1019() {
        DataBuffer var1 = new DataBuffer(this.anInt655 * 4);
        DataBuffer var2 = new DataBuffer(this.anInt653 * 16);
        int var3;
        if (HDToolKit.aBoolean1790) {
            for (var3 = 0; var3 < this.anInt653; ++var3) {
                var2.writeByte(this.aByteArray654[var3]);
                var2.writeByte(this.aByteArray644[var3]);
                var2.writeByte(this.aByteArray650[var3]);
                var2.writeByte(255);
                var2.writeFloat((float) this.anIntArray645[var3]);
                var2.writeFloat((float) this.anIntArray649[var3]);
                var2.writeFloat((float) this.anIntArray648[var3]);
            }

            for (var3 = 0; var3 < this.anInt655; ++var3) {
                var1.writeInt(this.anIntArray643[var3]);
            }
        } else {
            for (var3 = 0; var3 < this.anInt653; ++var3) {
                var2.writeByte(this.aByteArray654[var3]);
                var2.writeByte(this.aByteArray644[var3]);
                var2.writeByte(this.aByteArray650[var3]);
                var2.writeByte(255);
                var2.writeFloatLE((float) this.anIntArray645[var3]);
                var2.writeFloatLE((float) this.anIntArray649[var3]);
                var2.writeFloatLE((float) this.anIntArray648[var3]);
            }

            for (var3 = 0; var3 < this.anInt655; ++var3) {
                var1.writeIntLE(this.anIntArray643[var3]);
            }
        }

        if (HDToolKit.supportVertexBufferObject) {
            this.aClass156_642 = new Class156();
            ByteBuffer var4 = ByteBuffer.wrap(var2.buffer);
            this.aClass156_642.method2172(var4);
            this.aClass156_646 = new Class156();
            var4 = ByteBuffer.wrap(var1.buffer);
            this.aClass156_646.method2170(var4);
        } else {
            this.aByteBuffer647 = ByteBuffer.allocateDirect(var2.index);
            this.aByteBuffer647.put(var2.buffer);
            this.aByteBuffer647.flip();
            this.aByteBuffer652 = ByteBuffer.allocateDirect(var1.index);
            this.aByteBuffer652.put(var1.buffer);
            this.aByteBuffer652.flip();
        }

        this.anIntArray645 = null;
        this.anIntArray649 = null;
        this.anIntArray648 = null;
        this.aByteArray654 = null;
        this.aByteArray644 = null;
        this.aByteArray650 = null;
        this.anIntArray643 = null;
        this.aHashTable_656 = null;
    }

    final void method1020() {
        this.anIntArray643 = new int[this.anInt651];
        this.anIntArray645 = new int[this.anInt657];
        this.anIntArray649 = new int[this.anInt657];
        this.anIntArray648 = new int[this.anInt657];
        this.aByteArray654 = new byte[this.anInt657];
        this.aByteArray644 = new byte[this.anInt657];
        this.aByteArray650 = new byte[this.anInt657];
        this.aHashTable_656 = new HashTable(Class95.method1585((byte) 70, this.anInt657));
    }

    final void method1021() {
        GL var1 = HDToolKit.gl;
        if (HDToolKit.supportVertexBufferObject) {
            this.aClass156_642.method2169();
            var1.glInterleavedArrays(10787, 16, 0L);
            HDToolKit.aBoolean1798 = false;
            this.aClass156_646.method2171();
            var1.glDrawElements(4, this.anInt655, 5125, 0L);
        } else {

            var1.glInterleavedArrays(10787, 16, this.aByteBuffer647);
            HDToolKit.aBoolean1798 = false;
            var1.glDrawElements(4, this.anInt655, 5125, this.aByteBuffer652);
        }

    }

    final void method1022(int[] var1) {
        for (int var2 = 1; var2 < var1.length - 1; ++var2) {
            this.anIntArray643[this.anInt655++] = var1[0];
            this.anIntArray643[this.anInt655++] = var1[var2];
            this.anIntArray643[this.anInt655++] = var1[var2 + 1];
        }

    }

}

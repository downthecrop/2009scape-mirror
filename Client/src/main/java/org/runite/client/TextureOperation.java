package org.runite.client;

import org.rs09.client.Linkable;
import org.runite.client.drawcalls.LoadingBox;

import java.util.Random;

public abstract class TextureOperation extends Linkable {

    boolean aBoolean2375;
    Class97 aClass97_2376;
    TextureOperation[] subOperations;
    static int anInt2378 = 0;
    int imageCacheCapacity;
    Class114 aClass114_2382;
    static int anInt2383 = 0;

    static int method1603(byte var0, int var1, Random var2) {
        try {
            if (var1 <= 0) {
                throw new IllegalArgumentException();
            } else if (Class140_Sub6.method2021((byte) -115, var1)) {
                return (int) (((long) var2.nextInt() & 4294967295L) * (long) var1 >> 32);
            } else {
                int var3 = -((int) (4294967296L % (long) var1)) + Integer.MIN_VALUE;

                int var4;
                do {
                    var4 = var2.nextInt();
                } while (var3 <= var4);

                return TextureOperation27.method201(var4, var1);
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ni.C(" + var0 + ',' + var1 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }


    final int[] method152(int var1, int var2) {
        return this.subOperations[var1].aBoolean2375 ? this.subOperations[var1].method154(var2, (byte) -118) : this.subOperations[var1].method166(var2)[0];
    }

    static void method153() {
        Class3_Sub26.aLinkedList_2557 = new LinkedList();
    }

    int[] method154(int var1, byte var2) {
        try {
            throw new IllegalStateException("This operation does not have a monochrome output");
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "j.D(" + var1 + ',' + var2 + ')');
        }
    }

    int getSpriteFrame() {
        return -1;
    }

    void decode(int var1, DataBuffer var2) {
    }

    void postDecode() {
    }

    int method159(int var1) {
        try {
            if (var1 != 4) {
                LoadingBox.draw(true, null);
            }

            return -1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "j.GA(" + var1 + ')');
        }
    }

    final void method160(int var1, int var2) {
        try {

            int var4 = 255 == this.imageCacheCapacity ? var1 : this.imageCacheCapacity;
            if (this.aBoolean2375) {
                this.aClass114_2382 = new Class114(var4, var1, var2);
            } else {
                this.aClass97_2376 = new Class97(var4, var1, var2);
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "j.SA(" + var1 + ',' + var2 + ',' + 250 + ')');
        }
    }

    void method161(byte var1) {
        try {
            if (var1 != -45) {
                anInt2383 = 16;
            }

            if (this.aBoolean2375) {
                this.aClass114_2382.method1706();
                this.aClass114_2382 = null;
            } else {
                this.aClass97_2376.method1590();
                this.aClass97_2376 = null;
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "j.BA(" + var1 + ')');
        }
    }

    final int[][] method162(int var1, int var2, byte var3) {
        try {
            if (var3 > -45) {
                return null;
            } else if (this.subOperations[var2].aBoolean2375) {
                int[] var4 = this.subOperations[var2].method154(var1, (byte) -105);
                return new int[][]{var4, var4, var4};
            } else {
                return this.subOperations[var2].method166(var1);
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "j.UA(" + var1 + ',' + var2 + ',' + var3 + ')');
        }
    }

    static Class3_Sub28_Sub17_Sub1 method163(byte[] var0) {
        try {
            if (var0 == null) {
                return null;
            } else {

                Class3_Sub28_Sub17_Sub1 var2 = new Class3_Sub28_Sub17_Sub1(var0, Class164.anIntArray2048, Unsorted.anIntArray2591, Class140_Sub7.anIntArray2931, Unsorted.anIntArray3076, Class163_Sub1.aByteArrayArray2987);
                Class39.method1035((byte) 126);
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "j.WA(" + "{...}" + ',' + 25208 + ')');
        }
    }

    TextureOperation(int var1, boolean var2) {
        try {
            this.subOperations = new TextureOperation[var1];
            this.aBoolean2375 = var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "j.<init>(" + var1 + ',' + var2 + ')');
        }
    }

    int[][] method166(int var2) {
        throw new IllegalStateException("This operation does not have a colour output");
    }

}

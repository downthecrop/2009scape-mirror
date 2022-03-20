package org.runite.client;

import org.rs09.client.util.ArrayUtils;

public final class TextureOperation0 extends TextureOperation {

    private int anInt3276;

    protected TextureOperation0() {
        super(0, true);
        this.anInt3276 = 4096;

        try {
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "mi.<init>(" + 4096 + ')');
        }
    }

    final void decode(int var1, DataBuffer var2) {
        if (var1 == 0) {
            this.anInt3276 = (var2.readUnsignedByte() << 12) / 255;
        }
    }

    final int[] method154(int var1, byte var2) {
        try {
            int[] var4 = this.aClass114_2382.method1709(var1);
            if (this.aClass114_2382.aBoolean1580) {
                ArrayUtils.fill(var4, 0, Class113.anInt1559, this.anInt3276);
            }

            return var4;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "mi.D(" + var1 + ',' + var2 + ')');
        }
    }

}

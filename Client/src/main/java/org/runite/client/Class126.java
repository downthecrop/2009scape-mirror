package org.runite.client;

import org.rs09.client.data.Queue;

public final class Class126 {

    static int anInt1665;
    static Queue aClass13_1666 = new Queue();
    public static int anInt1676 = 0;
    int anInt1663;
    int anInt1664;
    int anInt1667;
    int anInt1670;
    int anInt1673;
    boolean aBoolean1674;
    int anInt1675;


    Class126(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
        try {
            this.anInt1667 = var2;
            this.anInt1664 = var3;
            this.anInt1675 = var1;
            this.anInt1673 = var6;
            this.aBoolean1674 = var7;
            this.anInt1663 = var4;
            this.anInt1670 = var5;
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "rh.<init>(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
        }
    }

}

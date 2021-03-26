package org.runite.client;

import org.rs09.client.data.HashTable;

final class Class124 {

    static HashTable aHashTable_1659 = new HashTable(512);
    static CacheIndex aClass153_1661;


    static void method1745() {
        try {
            for (int var1 = 0; var1 < 104; ++var1) {
                for (int var2 = 0; 104 > var2; ++var2) {
                    Class163_Sub1_Sub1.anIntArrayArray4010[var1][var2] = 0;
                }
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rb.B(" + 0 + ')');
        }
    }

    static void method1746(boolean var0, byte var1) {
        try {
            if (var1 > -31) {
                aClass153_1661 = null;
            }

            Class75_Sub4.method1352(Class140_Sub7.canvasHeight, var0, ConfigInventoryDefinition.anInt3655, Class23.canvasWidth);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rb.C(" + var0 + ',' + var1 + ')');
        }
    }

    static Class3_Sub28_Sub3 method1747(DataBuffer var0) {
        try {
            Class3_Sub28_Sub3 var2 = new Class3_Sub28_Sub3(var0.readString(), var0.readString(), var0.readUnsignedShort(), var0.readUnsignedShort(), var0.readInt(), var0.readUnsignedByte() == 1, var0.readUnsignedByte());
            int var3 = var0.readUnsignedByte();

            for (int var4 = 0; var3 > var4; ++var4) {
                var2.aLinkedList_3560.method1215(new Class3_Sub21(var0.readUnsignedShort(), var0.readUnsignedShort(), var0.readUnsignedShort(), var0.readUnsignedShort()));
            }

            var2.method538();
            return var2;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "rb.D(" + (var0 != null ? "{...}" : "null") + ',' + true + ')');
        }
    }

}

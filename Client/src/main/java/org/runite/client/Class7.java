package org.runite.client;

import org.rs09.client.filestore.resources.configs.enums.EnumDefinition;
import org.rs09.client.filestore.resources.configs.enums.EnumDefinitionProvider;

public final class Class7 implements Interface4 {

    public static int anInt2166 = 0;
    static CacheIndex skinsReferenceIndex;
    static int anInt2161 = -1;
    static int anInt2162;
    static short[] aShortArray2167 = new short[]{(short) 30, (short) 6, (short) 31, (short) 29, (short) 10, (short) 44, (short) 37, (short) 57};


    public final RSString method20(int var1, int[] var2, int var3, long var4) {
        try {
            if (var1 == 0) {
                EnumDefinition var6 = EnumDefinitionProvider.provide(var2[0]);
                return var6.getString((int) var4);
            } else if (var1 == 1 || var1 == 10) {
                ItemDefinition var8 = ItemDefinition.getItemDefinition((int) var4);
                return var8.name;
            } else {
                return var1 != 6 && var1 != 7 && 11 != var1 ? (var3 != 4936 ? null : null) : EnumDefinitionProvider.provide(var2[0]).getString((int) var4);
            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "af.A(" + var1 + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ',' + var4 + ')');
        }
    }

}

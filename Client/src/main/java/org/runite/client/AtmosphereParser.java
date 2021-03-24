package org.runite.client;

import org.rs09.client.data.HashTable;

public final class AtmosphereParser {

    static HashTable aHashTable_3679 = new HashTable(16);
    static AtmosphereParser[][] aAtmosphereParserArrayArray1581 = new AtmosphereParser[13][13];
    int anInt1175;
    int anInt1177;
    int anInt1178;
    int anInt1181;
    static Class91[] aClass91Array1182 = new Class91[4];
    int anInt1184;
    int anInt1185;
    static Class41 aClass41_1186;
    float aFloat1187;
    float aFloat1189;
    float aFloat1190;
    static int anInt1191;
    static int screenLowerY;

    public static void musicHandler(int var1) {
        try {
            if (-1 == var1 && !Class83.aBoolean1158) {
                GameObject.method1870();
            } else if (var1 != -1 && (Class129.anInt1691 != var1 || Class79.method1391(-1)) && Unsorted.anInt120 != 0 && !Class83.aBoolean1158) {
                Unsorted.method2099(var1, CacheIndex.musicIndex, Unsorted.anInt120);
            }
            Class129.anInt1691 = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "li.B(" + true + ',' + var1 + ')');
        }
    }

    static void method1428(int var0, int var2) {
        try {
            ItemDefinition.ram[var0] = var2;
            Class3_Sub7 var3 = (Class3_Sub7) aHashTable_3679.get(var0);
            if (var3 == null) {
                var3 = new Class3_Sub7(TimeUtils.time() - -500L);
                aHashTable_3679.put(var0, var3);
            } else {
                var3.aLong2295 = 500L + TimeUtils.time();
            }

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "li.D(" + var0 + ',' + var2 + ')');
        }
    }

    static Class3_Sub28_Sub17_Sub1 method1430(int var1) {
        try {
            Class3_Sub28_Sub17_Sub1 var2 = (Class3_Sub28_Sub17_Sub1) Unsorted.aReferenceCache_1135.get(var1);
            if (var2 == null) {
                byte[] var3 = CacheIndex.fontsIndex.getFile(var1, 0);
                var2 = new Class3_Sub28_Sub17_Sub1(var3);
                var2.method697(Sprites.nameIconsSpriteArray, null);
                Unsorted.aReferenceCache_1135.put(var2, var1);
            }
            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "li.A(" + -28922 + ',' + var1 + ')');
        }
    }

    public AtmosphereParser() {
        try {
            this.anInt1177 = Class92.defaultScreenColorRgb;
            this.aFloat1189 = 1.2F;
            this.anInt1178 = -50;
            this.aFloat1187 = 1.1523438F;
            this.anInt1175 = Class92.defaultRegionAmbientRGB;
            this.anInt1181 = -60;
            this.aFloat1190 = 0.69921875F;
            this.anInt1184 = 0;
            this.anInt1185 = -50;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "li.<init>()");
        }
    }

    AtmosphereParser(DataBuffer var1) {
        try {
            int var2 = var1.readUnsignedByte();
            if ((var2 & 1) == 0) {
                this.anInt1177 = Class92.defaultScreenColorRgb;
            } else {
                this.anInt1177 = var1.readInt();
            }

            if ((2 & var2) == 0) {
                this.aFloat1187 = 1.1523438F;
            } else {
                this.aFloat1187 = (float) var1.readUnsignedShort() / 256.0F;
            }

            if ((var2 & 4) == 0) {
                this.aFloat1190 = 0.69921875F;
            } else {
                this.aFloat1190 = (float) var1.readUnsignedShort() / 256.0F;
            }

            if ((var2 & 8) == 0) {
                this.aFloat1189 = 1.2F;
            } else {
                this.aFloat1189 = (float) var1.readUnsignedShort() / 256.0F;
            }

            if ((16 & var2) == 0) {
                this.anInt1178 = -50;
                this.anInt1185 = -50;
                this.anInt1181 = -60;
            } else {
                this.anInt1185 = var1.readSignedShort();
                this.anInt1181 = var1.readSignedShort();
                this.anInt1178 = var1.readSignedShort();
            }

            if ((32 & var2) == 0) {
                this.anInt1175 = Class92.defaultRegionAmbientRGB;
            } else {
                this.anInt1175 = var1.readInt();
            }

            if ((64 & var2) == 0) {
                this.anInt1184 = 0;
            } else {
                this.anInt1184 = var1.readUnsignedShort();
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "li.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

}

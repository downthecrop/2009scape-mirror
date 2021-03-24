package org.runite.client;

final class Class70 {

    static RSString[] aClass94Array1046 = new RSString[200];
    static int anInt1047;
    static double aDouble1050 = -1.0D;
    static int anInt1053 = 0;
    static Class10 aClass10_1056;
    int anInt1045;
    long aLong1048 = 0L;
    GameObject aClass140_1049;
    GameObject aClass140_1052;
    int anInt1054;
    int anInt1055;
    int anInt1057;
    int anInt1059;


    static void method1285(CacheIndex var0, int var2, int var5) {
        try {
            Class101.aClass153_1423 = var0;
            Class10.anInt154 = 1;
            Class3_Sub13_Sub36.anInt3423 = var5;
            Class132.anInt1741 = 0;
            Class3_Sub13_Sub39.anInt3463 = var2;
            Unsorted.aBoolean2311 = false;

            GraphicDefinition.anInt546 = 10000;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "jh.D(" + (var0 != null ? "{...}" : "null") + ',' + false + ',' + var2 + ',' + 0 + ',' + false + ',' + var5 + ')');
        }
    }

    static void method1286(int var0, ObjectDefinition var2, int var3, NPC var4, int var5, int var6, Player var7) {
        try {

            Class3_Sub9 var8 = new Class3_Sub9();
            var8.anInt2308 = var0 * 128;
            var8.anInt2326 = 128 * var5;
            var8.anInt2314 = var6;
            if (null != var2) {
                var8.anIntArray2333 = var2.anIntArray1539;
                var8.anInt2328 = var2.anInt1484 * 128;
                var8.anInt2325 = var2.anInt1515;
                var8.aClass111_2320 = var2;
                var8.anInt2332 = var2.anInt1512;
                var8.anInt2310 = var2.anInt1518;
                int var9 = var2.SizeX;
                int var10 = var2.SizeY;
                if (var3 == 1 || 3 == var3) {
                    var9 = var2.SizeY;
                    var10 = var2.SizeX;
                }

                var8.anInt2307 = (var10 + var0) * 128;
                var8.anInt2321 = (var5 + var9) * 128;
                if (var2.ChildrenIds != null) {
                    var8.aBoolean2329 = true;
                    var8.method134();
                }

                if (null != var8.anIntArray2333) {
                    var8.anInt2316 = var8.anInt2310 - -((int) (Math.random() * (double) (-var8.anInt2310 + var8.anInt2325)));
                }

                Unsorted.aClass61_78.method1215(var8);
            } else if (null != var4) {
                var8.aClass140_Sub4_Sub2_2324 = var4;
                NPCDefinition var12 = var4.definition;
                if (null != var12.childNPCs) {
                    var8.aBoolean2329 = true;
                    var12 = var12.method1471((byte) -112);
                }

                if (var12 != null) {
                    var8.anInt2307 = 128 * (var12.size + var0);
                    var8.anInt2321 = 128 * (var5 - -var12.size);
                    var8.anInt2332 = method1232(var4);
                    var8.anInt2328 = 128 * var12.anInt1291;
                }

                Unsorted.aClass61_1242.method1215(var8);
            } else if (null != var7) {
                var8.aClass140_Sub4_Sub1_2327 = var7;
                var8.anInt2321 = (var7.getSize() + var5) * 128;
                var8.anInt2307 = 128 * (var7.getSize() + var0);
                var8.anInt2332 = Class81.method1398(var7);
                var8.anInt2328 = 128 * var7.anInt3969;
                Unsorted.aHashTable_4046.put(var7.displayName.toLong(), var8);
            }

        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "jh.C(" + var0 + ',' + false + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ',' + var5 + ',' + var6 + ',' + (var7 != null ? "{...}" : "null") + ')');
        }
    }

    static Class3_Sub28_Sub17_Sub1 method1287(int var0, CacheIndex var2, CacheIndex var3) {
        try {
            // System.out.println("Class 70 " + var0);
            if (Class75_Sub4.method1351(var3, 0, var0)) {

                return TextureOperation.method163(var2.getFile(var0, 0));
            } else {
                return null;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "jh.B(" + var0 + ',' + 0 + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ',' + -1 + ')');
        }
    }

    static int method1232(NPC var0) {
        try {
            NPCDefinition var2 = var0.definition;
            if (null != var2.childNPCs) {
                var2 = var2.method1471((byte) -108);
                if (var2 == null) {
                    return -1;
                }
            }

            int var3 = var2.anInt1293;
            RenderAnimationDefinition var4 = var0.method1965();
            if (var0.anInt2764 == var4.anInt368) {
                var3 = var2.anInt1262;
            } else if (var4.anInt393 != var0.anInt2764 && var4.anInt386 != var0.anInt2764 && var0.anInt2764 != var4.anInt375 && var0.anInt2764 != var4.anInt373) {
                if (var0.anInt2764 == var4.anInt398 || var0.anInt2764 == var4.anInt372 || var4.anInt379 == var0.anInt2764 || var4.anInt406 == var0.anInt2764) {
                    var3 = var2.anInt1290;
                }
            } else {
                var3 = var2.anInt1276;
            }

            return var3;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ij.B(" + (var0 != null ? "{...}" : "null") + ',' + -1 + ')');
        }
    }
}

package org.runite.jagex;

final class Class70 {

   int anInt1045;
   static RSString[] aClass94Array1046 = new RSString[200];
   static int anInt1047;
   long aLong1048 = 0L;
   GameObject aClass140_1049;
   static double aDouble1050 = -1.0D;
   static RSString aClass94_1051 = RSString.createRSString("(Udns");
   GameObject aClass140_1052;
   static int anInt1053 = 0;
   int anInt1054;
   int anInt1055;
   static Class10 aClass10_1056;
   int anInt1057;
   static CacheIndex aClass153_1058;
   int anInt1059;
   static int anInt1060;


   public static void method1284(byte var0) {
      try {
         if(var0 != -87) {
            aClass94Array1046 = (RSString[])null;
         }

         aClass153_1058 = null;
         aClass94_1051 = null;
         aClass94Array1046 = null;
         aClass10_1056 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "jh.A(" + var0 + ')');
      }
   }

   static final void method1285(CacheIndex var0, boolean var1, int var2, int var3, boolean var4, int var5) {
      try {
         Class101.aClass153_1423 = var0;
         Class10.anInt154 = 1;
         Class3_Sub13_Sub36.anInt3423 = var5;
         Class132.anInt1741 = var3;
         Class3_Sub13_Sub39.anInt3463 = var2;
         Class3_Sub9.aBoolean2311 = var4;
         if(var1) {
            anInt1053 = 125;
         }

         GraphicDefinition.anInt546 = 10000;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "jh.D(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   static final void method1286(int var0, boolean var1, ObjectDefinition var2, int var3, NPC var4, int var5, int var6, Player var7) {
      try {
         if(var1) {
            anInt1047 = 94;
         }

         Class3_Sub9 var8 = new Class3_Sub9();
         var8.anInt2308 = var0 * 128;
         var8.anInt2326 = 128 * var5;
         var8.anInt2314 = var6;
         if(null != var2) {
            var8.anIntArray2333 = var2.anIntArray1539;
            var8.anInt2328 = var2.anInt1484 * 128;
            var8.anInt2325 = var2.anInt1515;
            var8.aClass111_2320 = var2;
            var8.anInt2332 = var2.anInt1512;
            var8.anInt2310 = var2.anInt1518;
            int var9 = var2.anInt1480;
            int var10 = var2.anInt1485;
            if(-2 == ~var3 || 3 == var3) {
               var9 = var2.anInt1485;
               var10 = var2.anInt1480;
            }

            var8.anInt2307 = (var10 + var0) * 128;
            var8.anInt2321 = (var5 + var9) * 128;
            if(var2.anIntArray1524 != null) {
               var8.aBoolean2329 = true;
               var8.method134(1);
            }

            if(null != var8.anIntArray2333) {
               var8.anInt2316 = var8.anInt2310 - -((int)(Math.random() * (double)(-var8.anInt2310 + var8.anInt2325)));
            }

            Class3.aClass61_78.method1215(!var1, var8);
         } else if(null != var4) {
            var8.aClass140_Sub4_Sub2_2324 = var4;
            NPCDefinition var12 = var4.definition;
            if(null != var12.childNPCs) {
               var8.aBoolean2329 = true;
               var12 = var12.method1471((byte)-112);
            }

            if(var12 != null) {
               var8.anInt2307 = 128 * (var12.size + var0);
               var8.anInt2321 = 128 * (var5 - -var12.size);
               var8.anInt2332 = ISAACCipher.method1232(var4, -1);
               var8.anInt2328 = 128 * var12.anInt1291;
            }

            IOHandler.aClass61_1242.method1215(true, var8);
         } else if(null != var7) {
            var8.aClass140_Sub4_Sub1_2327 = var7;
            var8.anInt2321 = (var7.getSize((byte)114) + var5) * 128;
            var8.anInt2307 = 128 * (var7.getSize((byte)114) + var0);
            var8.anInt2332 = Class81.method1398(0, var7);
            var8.anInt2328 = 128 * var7.anInt3969;
            Class3_Sub28_Sub7_Sub1.aClass130_4046.method1779(1, var8, var7.displayName.toLong(-112));
         }

      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "jh.C(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ',' + var6 + ',' + (var7 != null?"{...}":"null") + ')');
      }
   }

   static final Class3_Sub28_Sub17_Sub1 method1287(int var0, int var1, CacheIndex var2, CacheIndex var3, int var4) {
      try {
    	// System.out.println("Class 70 " + var0);
         if(Class75_Sub4.method1351(var3, var1, var0, var4 ^ 30900)) {
            if(var4 != -1) {
               method1284((byte)-124);
            }

            return Class3_Sub13.method163(var2.getFile(var0, (byte)-122, var1), 25208);
         } else {
            return null;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "jh.B(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

}

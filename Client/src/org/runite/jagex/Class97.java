package org.runite.jagex;

final class Class97 {

   private int[][][] anIntArrayArrayArray1362;
   static RSString aClass94_1363 = RSString.createRSString("Clientscript error in: ");
   static byte[] aByteArray1364 = new byte['\u8080'];
   private int anInt1365 = -1;
   private Class61 aClass61_1366 = new Class61();
   private int anInt1367;
   private int anInt1368 = 0;
   private int anInt1369;
   static CacheIndex aClass153_1370;
   private Class3_Sub20[] aClass3_Sub20Array1371;
   static CacheIndex aClass153_1372;
   static int[][] anIntArrayArray1373;
   static RSString aClass94_1374;
   static int anInt1375;
   static CacheIndex aClass153_1376;
   static RSString aClass94_1377;
   static CacheIndex aClass153_1378;
   boolean aBoolean1379 = false;
   static RSString aClass94_1380;
   static Class3_Sub28_Sub16_Sub2 aClass3_Sub28_Sub16_Sub2_1381;


   final int[][][] method1589(byte var1) {
      try {
         if(~this.anInt1369 != ~this.anInt1367) {
            throw new RuntimeException("Can only retrieve a full image cache");
         } else {
            if(var1 > -12) {
               this.anInt1365 = -104;
            }

            for(int var2 = 0; ~this.anInt1367 < ~var2; ++var2) {
               this.aClass3_Sub20Array1371[var2] = Class3_Sub28_Sub1.aClass3_Sub20_3532;
            }

            return this.anIntArrayArrayArray1362;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nd.F(" + var1 + ')');
      }
   }

   final void method1590(byte var1) {
      try {
         if(var1 < -1) {
            for(int var2 = 0; ~this.anInt1367 < ~var2; ++var2) {
               this.anIntArrayArrayArray1362[var2][0] = null;
               this.anIntArrayArrayArray1362[var2][1] = null;
               this.anIntArrayArrayArray1362[var2][2] = null;
               this.anIntArrayArrayArray1362[var2] = (int[][])null;
            }

            this.aClass3_Sub20Array1371 = null;
            this.anIntArrayArrayArray1362 = (int[][][])null;
            this.aClass61_1366.method1211(-118);
            this.aClass61_1366 = null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nd.E(" + var1 + ')');
      }
   }

   static final void method1591(boolean var0, Class3_Sub24 var1) {
      try {
         if(var1.aClass3_Sub12_2544 != null) {
            var1.aClass3_Sub12_2544.anInt2374 = 0;
         }

         var1.aBoolean2545 = false;

         for(Class3_Sub24 var2 = var1.method411(); var2 != null; var2 = var1.method414()) {
            method1591(true, var2);
         }

         if(!var0) {
            anInt1375 = -103;
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nd.A(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method1592(byte var0) {
      try {
         aClass153_1376 = null;
         aClass153_1378 = null;
         anIntArrayArray1373 = (int[][])null;
         aClass94_1363 = null;
         aClass3_Sub28_Sub16_Sub2_1381 = null;
         if(var0 > 25) {
            aClass94_1377 = null;
            aClass94_1380 = null;
            aClass153_1372 = null;
            aClass153_1370 = null;
            aByteArray1364 = null;
            aClass94_1374 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "nd.B(" + var0 + ')');
      }
   }

   static final void method1593(int var0, CacheIndex var1) {
      try {
         Class154.anInt1966 = var1.getArchiveForName(Class3_Sub28_Sub4.aClass94_3574, (byte)-30);
         Class79.anInt1124 = var1.getArchiveForName(Class95.aClass94_1341, (byte)-30);
         if(var0 <= 108) {
            method1593(14, (CacheIndex)null);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nd.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   final int[][] method1594(byte var1, int var2) {
      try {
         int var3 = -50 % ((var1 - -57) / 57);
         if(this.anInt1367 != this.anInt1369) {
            if(1 == this.anInt1367) {
               this.aBoolean1379 = this.anInt1365 != var2;
               this.anInt1365 = var2;
               return this.anIntArrayArrayArray1362[0];
            } else {
               Class3_Sub20 var4 = this.aClass3_Sub20Array1371[var2];
               if(null == var4) {
                  this.aBoolean1379 = true;
                  if(~this.anInt1368 > ~this.anInt1367) {
                     var4 = new Class3_Sub20(var2, this.anInt1368);
                     ++this.anInt1368;
                  } else {
                     Class3_Sub20 var5 = (Class3_Sub20)this.aClass61_1366.method1212(2);
                     var4 = new Class3_Sub20(var2, var5.anInt2483);
                     this.aClass3_Sub20Array1371[var5.anInt2489] = null;
                     var5.method86(-1024);
                  }

                  this.aClass3_Sub20Array1371[var2] = var4;
               } else {
                  this.aBoolean1379 = false;
               }

               this.aClass61_1366.method1216(64, var4);
               return this.anIntArrayArrayArray1362[var4.anInt2483];
            }
         } else {
            this.aBoolean1379 = null == this.aClass3_Sub20Array1371[var2];
            this.aClass3_Sub20Array1371[var2] = Class3_Sub28_Sub1.aClass3_Sub20_3532;
            return this.anIntArrayArrayArray1362[var2];
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "nd.D(" + var1 + ',' + var2 + ')');
      }
   }

   Class97(int var1, int var2, int var3) {
      try {
         this.anInt1369 = var2;
         this.anInt1367 = var1;
         this.aClass3_Sub20Array1371 = new Class3_Sub20[this.anInt1369];
         this.anIntArrayArrayArray1362 = new int[this.anInt1367][3][var3];
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "nd.<init>(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static {
      int var0 = 0;

      for(int var1 = 0; var1 < 256; ++var1) {
         for(int var2 = 0; var2 <= var1; ++var2) {
            aByteArray1364[var0++] = (byte)((int)(255.0D / Math.sqrt((double)((float)('\uffff' + var2 * var2 + var1 * var1) / 65535.0F))));
         }
      }

      aClass94_1374 = RSString.createRSString("zap");
      aClass94_1377 = RSString.createRSString("Abbrechen");
      anIntArrayArray1373 = new int[104][104];
      aClass94_1380 = RSString.createRSString(")4p=");
   }
}

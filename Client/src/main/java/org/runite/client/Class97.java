package org.runite.client;

import java.util.Objects;

public final class Class97 {

   private int[][][] anIntArrayArrayArray1362;
   static byte[] aByteArray1364 = new byte['\u8080'];
   private int anInt1365 = -1;
   private Class61 aClass61_1366 = new Class61();
   private final int anInt1367;
   private int anInt1368 = 0;
   private final int anInt1369;
   static CacheIndex aClass153_1370;
   private Class3_Sub20[] aClass3_Sub20Array1371;
   static CacheIndex aClass153_1372;
   static int[][] anIntArrayArray1373;
   static RSString aClass94_1374;
   static int anInt1375;
   public static CacheIndex aClass153_1378;
   boolean aBoolean1379 = false;
   static RSString aClass94_1380;


   final int[][][] method1589() {
      try {
         if(this.anInt1367 == this.anInt1369) {

            for(int var2 = 0; var2 < this.anInt1367; ++var2) {
               this.aClass3_Sub20Array1371[var2] = Class3_Sub28_Sub1.aClass3_Sub20_3532;
            }

            return this.anIntArrayArrayArray1362;
         } else {
            throw new RuntimeException("Can only retrieve a full image cache");
         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "nd.F(" + (byte) -56 + ')');
      }
   }

   final void method1590() {
      try {
         for(int var2 = 0; var2 < this.anInt1367; ++var2) {
            this.anIntArrayArrayArray1362[var2][0] = null;
            this.anIntArrayArrayArray1362[var2][1] = null;
            this.anIntArrayArrayArray1362[var2][2] = null;
            this.anIntArrayArrayArray1362[var2] = (int[][])null;
         }

         this.aClass3_Sub20Array1371 = null;
         this.anIntArrayArrayArray1362 = (int[][][])null;
         this.aClass61_1366.method1211(-118);
         this.aClass61_1366 = null;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "nd.E(" + (byte) -22 + ')');
      }
   }

   static void method1591(Class3_Sub24 var1) {
      try {
         if(var1.aClass3_Sub12_2544 != null) {
            var1.aClass3_Sub12_2544.anInt2374 = 0;
         }

         var1.aBoolean2545 = false;

         for(Class3_Sub24 var2 = var1.method411(); var2 != null; var2 = var1.method414()) {
            method1591(var2);
         }

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "nd.A(" + true + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static void method1593(int var0, CacheIndex var1) {
      try {
         Class154.anInt1966 = var1.getArchiveForName(TextCore.aClass94_3574);
         Class79.anInt1124 = var1.getArchiveForName(TextCore.aClass94_1341);
         if(var0 <= 108) {
            method1593(14, (CacheIndex)null);
         }

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "nd.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   final int[][] method1594(byte var1, int var2) {
      try {
         if(this.anInt1367 == this.anInt1369) {
            this.aBoolean1379 = null == this.aClass3_Sub20Array1371[var2];
            this.aClass3_Sub20Array1371[var2] = Class3_Sub28_Sub1.aClass3_Sub20_3532;
            return this.anIntArrayArrayArray1362[var2];
         } else if (1 == this.anInt1367) {
            this.aBoolean1379 = this.anInt1365 != var2;
            this.anInt1365 = var2;
            return this.anIntArrayArrayArray1362[0];
         } else {
            Class3_Sub20 var4 = this.aClass3_Sub20Array1371[var2];
            if (null == var4) {
               this.aBoolean1379 = true;
               if (this.anInt1367 > this.anInt1368) {
                  var4 = new Class3_Sub20(var2, this.anInt1368);
                  ++this.anInt1368;
               } else {
                  Class3_Sub20 var5 = (Class3_Sub20) this.aClass61_1366.method1212();
                  var4 = new Class3_Sub20(var2, Objects.requireNonNull(var5).anInt2483);
                  this.aClass3_Sub20Array1371[var5.anInt2489] = null;
                  var5.unlink();
               }

               this.aClass3_Sub20Array1371[var2] = var4;
            } else {
               this.aBoolean1379 = false;
            }

            this.aClass61_1366.method1216(var4);
            return this.anIntArrayArrayArray1362[var4.anInt2483];
         }
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "nd.D(" + var1 + ',' + var2 + ')');
      }
   }

   Class97(int var1, int var2, int var3) {
      try {
         this.anInt1369 = var2;
         this.anInt1367 = var1;
         this.aClass3_Sub20Array1371 = new Class3_Sub20[this.anInt1369];
         this.anIntArrayArrayArray1362 = new int[this.anInt1367][3][var3];
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "nd.<init>(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static {
      int var0 = 0;

      for(int var1 = 0; var1 < 256; ++var1) {
         for(int var2 = 0; var2 <= var1; ++var2) {
            aByteArray1364[var0++] = (byte)((int)(255.0D / Math.sqrt((double)((float)('\uffff' + var2 * var2 + var1 * var1) / 65535.0F))));
         }
      }

      aClass94_1374 = RSString.parse("zap");
      anIntArrayArray1373 = new int[104][104];
      aClass94_1380 = RSString.parse(")4p=");
   }
}

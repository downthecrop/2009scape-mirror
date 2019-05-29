package org.runite.jagex;

final class Class95 {

   static RSString aClass94_1333 = RSString.createRSString("um");
   private static RSString aClass94_1334 = RSString.createRSString("You can(Wt add yourself to your own ignore list)3");
   static RSString aClass94_1335 = aClass94_1334;
   static int anInt1336 = 0;
   static RSString aClass94_1337 = RSString.createRSString("::breakcon");
   static int anInt1338;
   static Class3_Sub28_Sub16 aClass3_Sub28_Sub16_1339;
   static volatile int anInt1340 = -1;
   static RSString aClass94_1341 = RSString.createRSString("logo");
   static RSString aClass94_1342 = RSString.createRSString("details");
   static int anInt1343;
   static int anInt1344;


   public static void method1582(int var0) {
      try {
         aClass94_1335 = null;
         aClass3_Sub28_Sub16_1339 = null;
         aClass94_1342 = null;
         aClass94_1333 = null;
         aClass94_1341 = null;
         if(var0 != 3) {
            anInt1340 = -18;
         }

         aClass94_1334 = null;
         aClass94_1337 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "nb.D(" + var0 + ')');
      }
   }

   static final void method1583(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      try {
         if(var3 != 3) {
            method1582(45);
         }

         if(~var0 == ~var1 && ~var9 == ~var2 && var8 == var7 && var5 == var6) {
            Class3_Sub13_Sub34.method330(var4, var3 + -111, var5, var0, var9, var7);
         } else {
            int var10 = var0;
            int var13 = 3 * var9;
            int var12 = 3 * var0;
            int var11 = var9;
            int var14 = var1 * 3;
            int var15 = 3 * var2;
            int var16 = 3 * var8;
            int var17 = var6 * 3;
            int var18 = var14 + var7 - (var16 + var0);
            int var20 = var16 - (var14 - -var14) - -var12;
            int var19 = var15 + -var17 + (var5 - var9);
            int var21 = var13 + -var15 + -var15 + var17;
            int var22 = var14 + -var12;
            int var23 = -var13 + var15;

            for(int var24 = 128; -4097 <= ~var24; var24 += 128) {
               int var25 = var24 * var24 >> 12;
               int var26 = var24 * var25 >> 12;
               int var30 = var21 * var25;
               int var28 = var26 * var19;
               int var29 = var25 * var20;
               int var27 = var18 * var26;
               int var31 = var22 * var24;
               int var33 = var0 - -(var29 + (var27 - -var31) >> 12);
               int var32 = var24 * var23;
               int var34 = var9 - -(var32 + var28 - -var30 >> 12);
               Class3_Sub13_Sub34.method330(var4, -88, var34, var10, var11, var33);
               var10 = var33;
               var11 = var34;
            }
         }

      } catch (RuntimeException var35) {
         throw Class44.method1067(var35, "nb.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
      }
   }

   static final void method1584(int var0, int var1, int var2, int var3, int var4, int var5) {
      try {
         if(var4 != -26571) {
            method1586(92, 25);
         }

         if(~var1 == ~var5) {
            Class43.method1058(var5, var2, var0, var3, (byte)-47);
         } else {
            if(Class101.anInt1425 <= var2 - var5 && var2 + var5 <= Class3_Sub28_Sub18.anInt3765 && -var1 + var3 >= Class159.anInt2020 && ~(var3 - -var1) >= ~Class57.anInt902) {
               Class3_Sub9.method135(var3, var2, var5, -111, var1, var0);
            } else {
               Class3_Sub28_Sub5.method556(var0, var1, var5, (byte)-123, var2, var3);
            }

         }
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "nb.E(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   static final int method1585(byte var0, int var1) {
      try {
         --var1;
         var1 |= var1 >>> 1;
         if(var0 < 51) {
            aClass94_1335 = (RSString)null;
         }

         var1 |= var1 >>> 2;
         var1 |= var1 >>> 4;
         var1 |= var1 >>> 8;
         var1 |= var1 >>> 16;
         return 1 + var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nb.C(" + var0 + ',' + var1 + ')');
      }
   }

   static final RSString method1586(int var0, int var1) {
      try {
         if(-1 > ~var1 && -256 <= ~var1) {
            RSString var2 = new RSString();
            if(var0 != 23161) {
               aClass3_Sub28_Sub16_1339 = (Class3_Sub28_Sub16)null;
            }

            var2.length = 1;
            var2.byteArray = new byte[1];
            var2.byteArray[0] = (byte)var1;
            return var2;
         } else {
            throw new IllegalArgumentException();
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "nb.A(" + var0 + ',' + var1 + ')');
      }
   }

}

package org.runite.jagex;
import java.awt.Component;

import org.runite.Configurations;

final class Class58 {

   static int anInt909 = -1;
   static RSString aClass94_910 = RSString.createRSString("::fpson");
   static int[][][] anIntArrayArrayArray911 = new int[2][][];
   static int[] anIntArray912 = new int[14];
   static boolean aBoolean913 = false;
   static int[][][] anIntArrayArrayArray914;
   static Interface4 anInterface4_915 = null;
   static int anInt916;
   static Class66 aClass66_917;


   public static void method1193(int var0) {
      try {
         anInterface4_915 = null;
         anIntArrayArrayArray911 = (int[][][])null;
         anIntArrayArrayArray914 = (int[][][])null;
         aClass66_917 = null;
         anIntArray912 = null;
         if(var0 != -26723) {
            anIntArrayArrayArray914 = (int[][][])((int[][][])null);
         }

         aClass94_910 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "id.C(" + var0 + ')');
      }
   }

   static final void method1194(int var0) {
      try {
         if(var0 == -16385) {
            if(null != Class3_Sub21.aClass155_2491) {
               Class3_Sub21.aClass155_2491.method2153((byte)-34);
            }

            if(null != WorldListEntry.aClass155_2627) {
               WorldListEntry.aClass155_2627.method2153((byte)-34);
            }

         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "id.A(" + var0 + ')');
      }
   }

   static final Class155 method1195(int var0, Signlink var1, Component var2, int var3, int var4) {
      try {
         if(Class21.anInt443 == 0) {
            throw new IllegalStateException();
         } else if(0 <= var3 && 2 > var3) {
            if(-257 < ~var0) {
               var0 = 256;
            }

            try {
               Class155 var9 = (Class155)Class.forName(Configurations.PACKAGE_JAGEX + ".Class155_Sub2").newInstance();
               var9.anInt1989 = var0;
               var9.anIntArray1975 = new int[(!RSString.aBoolean2150?1:2) * 256];
               var9.method2164(var2);
               var9.anInt1990 = (var0 & -1024) - -1024;
               if(-16385 > ~var9.anInt1990) {
                  var9.anInt1990 = 16384;
               }

               var9.method2150(var9.anInt1990);
               if(Class3_Sub24_Sub4.anInt3507 > 0 && null == Class38_Sub1.aClass15_2613) {
                  Class38_Sub1.aClass15_2613 = new Class15();
                  Class38_Sub1.aClass15_2613.aClass87_350 = var1;
                  var1.method1451(0, Class3_Sub24_Sub4.anInt3507, Class38_Sub1.aClass15_2613);
               }

               if(Class38_Sub1.aClass15_2613 != null) {
                  if(null != Class38_Sub1.aClass15_2613.aClass155Array352[var3]) {
                     throw new IllegalArgumentException();
                  }

                  Class38_Sub1.aClass15_2613.aClass155Array352[var3] = var9;
               }

               return var9;
            } catch (Throwable var7) {
               if(var4 != 14) {
                  anInt909 = 124;
               }

               try {
                  Class155_Sub1 var5 = new Class155_Sub1(var1, var3);
                  var5.anIntArray1975 = new int[256 * (RSString.aBoolean2150?2:1)];
                  var5.anInt1989 = var0;
                  var5.method2164(var2);
                  var5.anInt1990 = 16384;
                  var5.method2150(var5.anInt1990);
                  if(~Class3_Sub24_Sub4.anInt3507 < -1 && null == Class38_Sub1.aClass15_2613) {
                     Class38_Sub1.aClass15_2613 = new Class15();
                     Class38_Sub1.aClass15_2613.aClass87_350 = var1;
                     var1.method1451(var4 ^ 14, Class3_Sub24_Sub4.anInt3507, Class38_Sub1.aClass15_2613);
                  }

                  if(Class38_Sub1.aClass15_2613 != null) {
                     if(Class38_Sub1.aClass15_2613.aClass155Array352[var3] != null) {
                        throw new IllegalArgumentException();
                     }

                     Class38_Sub1.aClass15_2613.aClass155Array352[var3] = var5;
                  }

                  return var5;
               } catch (Throwable var6) {
                  return new Class155();
               }
            }
         } else {
            throw new IllegalArgumentException();
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "id.D(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final void method1196(int var0, int var1, byte var2, int var3, int var4) {
      try {
         Class3_Sub28_Sub18.anInt3765 = var4;
         if(var2 != 111) {
            aBoolean913 = true;
         }

         Class101.anInt1425 = var1;
         Class159.anInt2020 = var0;
         Class57.anInt902 = var3;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "id.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final void method1197(CacheIndex var0, byte var1) {
      try {
         if(var1 != 69) {
            method1195(-53, (Signlink)null, (Component)null, 79, 12);
         }

         Class46.aClass153_737 = var0;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "id.E(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

}

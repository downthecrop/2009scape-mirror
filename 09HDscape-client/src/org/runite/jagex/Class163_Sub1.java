package org.runite.jagex;
import java.io.IOException;

class Class163_Sub1 extends Class163 {

   static Class93 aClass93_2984 = new Class93(2);
   static int[] anIntArray2985 = new int[2500];
   static long[] aLongArray2986 = new long[32];
   static byte[][] aByteArrayArray2987;
   private static RSString aClass94_2988 = RSString.createRSString("glow3:");
   static int anInt2989 = 0;
   static Class61 aClass61_2990 = new Class61();
   static RSString aClass94_2991 = aClass94_2988;
   static RSString aClass94_2992 = aClass94_2988;
   static int anInt2993 = 0;
   static int anInt2994;
   static RSString aClass94_2995 = RSString.createRSString("Veuillez commencer par supprimer ");


   static final void method2210(byte var0, boolean var1) {
      try {
         Class58.method1194(-16385);
         if(30 == Class143.loadingStage || ~Class143.loadingStage == -26) {
            ++Class3_Sub13_Sub23_Sub1.anInt4032;
            if(~Class3_Sub13_Sub23_Sub1.anInt4032 <= -51 || var1) {
               Class3_Sub13_Sub23_Sub1.anInt4032 = 0;
               if(var0 == -90) {
                  if(!Class3_Sub28_Sub18.aBoolean3769 && Class3_Sub15.aClass89_2429 != null) {
                     Class3_Sub13_Sub1.outgoingBuffer.putOpcode(93);
                      try {
                        Class3_Sub15.aClass89_2429.sendBytes(false, 0, Class3_Sub13_Sub1.outgoingBuffer.buffer, Class3_Sub13_Sub1.outgoingBuffer.index);
                        Class3_Sub13_Sub1.outgoingBuffer.index = 0;
                     } catch (IOException var3) {
                        Class3_Sub28_Sub18.aBoolean3769 = true;
                     }

                     ++Class3_Sub28_Sub4.anInt3569;
                  }

                  Class58.method1194(var0 ^ 16473);
               }
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ah.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method2211(int var0) {
      try {
         if(var0 == -48) {
            if(null == Class67.aClass11_1017) {
               if(null == Class56.aClass11_886) {
                  int var1 = Class3_Sub28_Sub11.anInt3644;
                  int var3;
                  int var4;
                  if(!Class38_Sub1.aBoolean2615) {
                     if(~var1 == -2 && 0 < Class3_Sub13_Sub34.anInt3415) {
                        short var2 = Class3_Sub13_Sub7.aShortArray3095[-1 + Class3_Sub13_Sub34.anInt3415];
                        if(-26 == ~var2 || var2 == 23 || 48 == var2 || ~var2 == -8 || 13 == var2 || ~var2 == -48 || -6 == ~var2 || var2 == 43 || -36 == ~var2 || ~var2 == -59 || ~var2 == -23 || var2 == 1006) {
                           var3 = Class117.anIntArray1613[-1 + Class3_Sub13_Sub34.anInt3415];
                           var4 = Class27.anIntArray512[Class3_Sub13_Sub34.anInt3415 + -1];
                           RSInterface var5 = Class7.getRSInterface((byte)113, var4);
                           Class3_Sub1 var6 = Client.method44(var5);
                           if(var6.method100((byte)-9) || var6.method93(572878952)) {
                              Class40.anInt677 = 0;
                              Class72.aBoolean1074 = false;
                              if(Class67.aClass11_1017 != null) {
                                 Class20.method909(-67, Class67.aClass11_1017);
                              }

                              Class67.aClass11_1017 = Class7.getRSInterface((byte)119, var4);
                              Class129_Sub1.anInt2693 = anInt2993;
                              InputStream_Sub1.anInt40 = Class38_Sub1.anInt2614;
                              PacketParser.anInt86 = var3;
                              Class20.method909(var0 + 166, Class67.aClass11_1017);
                              return;
                           }
                        }
                     }

                     if(-2 == ~var1 && (-2 == ~Class66.anInt998 && 2 < Class3_Sub13_Sub34.anInt3415 || Class3_Sub13_Sub39.method353(Class3_Sub13_Sub34.anInt3415 + -1, 0))) {
                        var1 = 2;
                     }

                     if(~var1 == -3 && -1 > ~Class3_Sub13_Sub34.anInt3415 || -2 == ~Class3_Sub28_Sub13.anInt3660) {
                        Class132.method1801((byte)-105);
                     }

                     if(1 == var1 && Class3_Sub13_Sub34.anInt3415 > 0 || -3 == ~Class3_Sub28_Sub13.anInt3660) {
                        Class3_Sub13_Sub8.method203(100);
                     }
                  } else {
                     int var11;
                     if(-2 != ~var1) {
                        var3 = Class130.anInt1709;
                        var11 = Class126.anInt1676;
                        if(~var11 > ~(AbstractIndexedSprite.anInt1462 - 10) || ~var11 < ~(Class3_Sub28_Sub3.anInt3552 + (AbstractIndexedSprite.anInt1462 - -10)) || ~(-10 + Class3_Sub13_Sub33.anInt3395) < ~var3 || ~var3 < ~(Class3_Sub28_Sub1.anInt3537 + (Class3_Sub13_Sub33.anInt3395 - -10))) {
                           Class38_Sub1.aBoolean2615 = false;
                           Class75.method1340(AbstractIndexedSprite.anInt1462, Class3_Sub28_Sub3.anInt3552, (byte)-40, Class3_Sub13_Sub33.anInt3395, Class3_Sub28_Sub1.anInt3537);
                        }
                     }

                     if(-2 == ~var1) {
                        var11 = AbstractIndexedSprite.anInt1462;
                        var3 = Class3_Sub13_Sub33.anInt3395;
                        var4 = Class3_Sub28_Sub3.anInt3552;
                        int var12 = anInt2993;
                        int var13 = Class38_Sub1.anInt2614;
                        int var7 = -1;

                        for(int var8 = 0; var8 < Class3_Sub13_Sub34.anInt3415; ++var8) {
                           int var9;
                           if(CacheIndex.aBoolean1951) {
                              var9 = 15 * (Class3_Sub13_Sub34.anInt3415 + -1 + -var8) + 35 + var3;
                           } else {
                              var9 = 15 * (-var8 + (Class3_Sub13_Sub34.anInt3415 - 1)) + var3 + 31;
                           }

                           if(~var12 < ~var11 && ~(var11 - -var4) < ~var12 && var9 + -13 < var13 && ~(3 + var9) < ~var13) {
                              var7 = var8;
                           }
                        }

                        if(~var7 != 0) {
                           Class3_Sub30_Sub1.method806(2597, var7);
                        }

                        Class38_Sub1.aBoolean2615 = false;
                        Class75.method1340(AbstractIndexedSprite.anInt1462, Class3_Sub28_Sub3.anInt3552, (byte)-40, Class3_Sub13_Sub33.anInt3395, Class3_Sub28_Sub1.anInt3537);
                     }
                  }

               }
            }
         }
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "ah.A(" + var0 + ')');
      }
   }

   public static void method2212(boolean var0) {
      try {
         anIntArray2985 = null;
         aClass94_2988 = null;
         aClass93_2984 = null;
         aClass61_2990 = null;
         aLongArray2986 = null;
         if(var0) {
            method2211(68);
         }

         aClass94_2992 = null;
         aByteArrayArray2987 = (byte[][])null;
         aClass94_2995 = null;
         aClass94_2991 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ah.C(" + var0 + ')');
      }
   }

}

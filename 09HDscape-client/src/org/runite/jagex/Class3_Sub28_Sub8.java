package org.runite.jagex;

final class Class3_Sub28_Sub8 extends Node {

   static int anInt3609;
   static Class113[] aClass113Array3610;
   static int anInt3611;
   byte[] aByteArray3612;
   static int anInt3613;


   static final int method571(int var0) {
      try {
         return var0 >= -57?-107:((double)NPC.aFloat3979 == 3.0D?37:((double)NPC.aFloat3979 == 4.0D?50:(6.0D != (double)NPC.aFloat3979?((double)NPC.aFloat3979 == 8.0D?100:200):75)));
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hc.E(" + var0 + ')');
      }
   }

   static final Class3_Sub28_Sub15 method572(int var0, byte var1) {
      try {
         Class3_Sub28_Sub15 var2 = (Class3_Sub28_Sub15)Class56.aClass47_885.method1092((long)var0, 1400);
         if(var2 != null) {
            return var2;
         } else {
            byte[] var3 = Class3_Sub1.interfaceScriptsIndex.getFile(var0, (byte)-122, 0);
            if(var3 != null) {
               var2 = new Class3_Sub28_Sub15();
               if(var1 != -91) {
                  aClass113Array3610 = (Class113[])null;
               }

               RSByteBuffer var4 = new RSByteBuffer(var3);
               var4.index = -2 + var4.buffer.length;
               int var5 = var4.getShort(1);
               int var6 = -12 + var4.buffer.length + -2 - var5;
               var4.index = var6;
               int var7 = var4.getInt();
               var2.anInt3680 = var4.getShort(var1 ^ -92);
               var2.anInt3687 = var4.getShort(1);
               var2.anInt3678 = var4.getShort(var1 ^ -92);
               var2.anInt3682 = var4.getShort(1);
               int var8 = var4.getByte((byte)-70);
               int var9;
               int var10;
               if(var8 > 0) {
                  var2.aClass130Array3685 = new Class130[var8];

                  for(var9 = 0; var9 < var8; ++var9) {
                     var10 = var4.getShort(1);
                     Class130 var11 = new Class130(Class95.method1585((byte)119, var10));
                     var2.aClass130Array3685[var9] = var11;

                     while(-1 > ~(var10--)) {
                        int var12 = var4.getInt();
                        int var13 = var4.getInt();
                        var11.method1779(1, new Class3_Sub18(var13), (long)var12);
                     }
                  }
               }

               var4.index = 0;
               var2.aClass94_3686 = var4.method750((byte)78);
               var2.anIntArray3683 = new int[var7];
               var2.aClass94Array3688 = new RSString[var7];
               var9 = 0;

               for(var2.anIntArray3690 = new int[var7]; ~var6 < ~var4.index; var2.anIntArray3683[var9++] = var10) {
                  var10 = var4.getShort(var1 ^ -92);
                  if(var10 != 3) {
                     if(var10 < 100 && 21 != var10 && -39 != ~var10 && 39 != var10) {
                        var2.anIntArray3690[var9] = var4.getInt();
                     } else {
                        var2.anIntArray3690[var9] = var4.getByte((byte)-67);
                     }
                  } else {
                     var2.aClass94Array3688[var9] = var4.getString();
                  }
               }

               Class56.aClass47_885.method1097(var2, (long)var0, (byte)-87);
               return var2;
            } else {
               return null;
            }
         }
      } catch (RuntimeException var14) {
         throw Class44.method1067(var14, "hc.O(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method573(int var0) {
      try {
         if(var0 != -11346) {
            anInt3611 = -69;
         }

         aClass113Array3610 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hc.A(" + var0 + ')');
      }
   }

   static final void method574(NPC var0, boolean var1) {
      try {
         if(!var1) {
            for(Class3_Sub9 var2 = (Class3_Sub9)IOHandler.aClass61_1242.method1222(); var2 != null; var2 = (Class3_Sub9)IOHandler.aClass61_1242.method1221()) {
               if(var0 == var2.aClass140_Sub4_Sub2_2324) {
                  if(var2.aClass3_Sub24_Sub1_2312 != null) {
                     Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var2.aClass3_Sub24_Sub1_2312);
                     var2.aClass3_Sub24_Sub1_2312 = null;
                  }

                  var2.method86(-1024);
                  return;
               }
            }

         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hc.D(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method575(CacheIndex var0, int var1) {
      try {
         if(var1 != -1) {
            method575((CacheIndex)null, -38);
         }

         Class3_Sub23.aClass153_2536 = var0;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hc.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final int method576(RSString var0, boolean var1) {
      try {
         if(Class119.aClass131_1624 != null && var0.length(-65) != 0) {
            if(var1) {
               return -117;
            } else {
               for(int var2 = 0; var2 < Class119.aClass131_1624.anInt1720; ++var2) {
                  if(Class119.aClass131_1624.aClass94Array1721[var2].method1560(Class3_Sub13_Sub16.aClass94_3192, !var1, Class3_Sub28_Sub10_Sub2.aClass94_4066).method1528((byte)-42, var0)) {
                     return var2;
                  }
               }

               return -1;
            }
         } else {
            return -1;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hc.F(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   Class3_Sub28_Sub8(byte[] var1) {
      try {
         this.aByteArray3612 = var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hc.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method577(int var0, int var1, int var2, int var3, GameObject var4, GameObject var5, int var6, int var7, int var8, int var9, long var10) {
      if(var4 != null) {
         Class19 var12 = new Class19();
         var12.aLong428 = var10;
         var12.anInt424 = var1 * 128 + 64;
         var12.anInt427 = var2 * 128 + 64;
         var12.anInt425 = var3;
         var12.aClass140_429 = var4;
         var12.aClass140_423 = var5;
         var12.anInt432 = var6;
         var12.anInt420 = var7;
         var12.anInt430 = var8;
         var12.anInt426 = var9;

         for(int var13 = var0; var13 >= 0; --var13) {
            if(Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var13][var1][var2] == null) {
               Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var13][var1][var2] = new Class3_Sub2(var13, var1, var2);
            }
         }

         Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass19_2233 = var12;
      }
   }

}

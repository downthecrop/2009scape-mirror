package org.runite.jagex;

class Class163_Sub2 extends Class163 {

   static RSString aClass94_2996 = null;
   static RSString aClass94_2997 = RSString.createRSString("blinken1:");
   static Class30[] aClass30Array2998 = new Class30[29]; //TODO


   static final Class25 method2217(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if(var3 == null) {
         return null;
      } else {
         for(int var4 = 0; var4 < var3.anInt2223; ++var4) {
            Class25 var5 = var3.aClass25Array2221[var4];
            if((var5.aLong498 >> 29 & 3L) == 2L && var5.anInt483 == var1 && var5.anInt478 == var2) {
               Class158.method2186(var5);
               return var5;
            }
         }

         return null;
      }
   }

   public static void method2218(byte var0) {
      try {
         aClass94_2996 = null;
         aClass94_2997 = null;
         if(var0 != -83) {
            method2218((byte)-9);
         }

         aClass30Array2998 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "dk.B(" + var0 + ')');
      }
   }

   static final void method2219(RSByteBuffer var0, boolean var1) {
      try {
         if(var1) {
            aClass30Array2998 = (Class30[])null;
         }

         while(~var0.index > ~var0.buffer.length) {
            int var4 = 0;
            boolean var3 = false;
            int var5 = 0;
            if(var0.getByte((byte)-94) == 1) {
               var3 = true;
               var4 = var0.getByte((byte)-40);
               var5 = var0.getByte((byte)-102);
            }

            int var6 = var0.getByte((byte)-104);
            int var7 = var0.getByte((byte)-71);
            int var9 = -(var7 * 64) - (-Class2.anInt65 - Class108.anInt1460 + 1);
            int var8 = var6 * 64 + -Class3_Sub13_Sub21.anInt3256;
            byte var2;
            int var10;
            if(var8 >= 0 && var9 + -63 >= 0 && ~(var8 - -63) > ~Class23.anInt455 && var9 < Class108.anInt1460) {
               var10 = var8 >> 6;
               int var11 = var9 >> 6;

               for(int var12 = 0; ~var12 > -65; ++var12) {
                  for(int var13 = 0; var13 < 64; ++var13) {
                     if(!var3 || ~var12 <= ~(8 * var4) && var12 < 8 + var4 * 8 && var13 >= var5 * 8 && ~var13 > ~(8 + var5 * 8)) {
                        var2 = var0.getByte();
                        if(-1 != ~var2) {
                           if(RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11] == null) {
                              RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11] = new byte[4096];
                           }

                           RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11][var12 + (-var13 + 63 << 6)] = var2;
                           byte var14 = var0.getByte();
                           if(Class3_Sub10.aByteArrayArrayArray2339[var10][var11] == null) {
                              Class3_Sub10.aByteArrayArrayArray2339[var10][var11] = new byte[4096];
                           }

                           Class3_Sub10.aByteArrayArrayArray2339[var10][var11][var12 + (63 - var13 << 6)] = var14;
                        }
                     }
                  }
               }
            } else {
               for(var10 = 0; ~(var3?64:4096) < ~var10; ++var10) {
                  var2 = var0.getByte();
                  if(0 != var2) {
                     ++var0.index;
                  }
               }
            }
         }

      } catch (RuntimeException var15) {
         throw Class44.method1067(var15, "dk.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

}

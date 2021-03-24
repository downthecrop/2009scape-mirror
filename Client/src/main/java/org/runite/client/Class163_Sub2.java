package org.runite.client;

class Class163_Sub2 extends Class163 {

   static RSString paramSettings = null;
   static Class30[] aClass30Array2998 = new Class30[29]; //TODO


   static Class25 method2217(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if (var3 != null) {
         for (int var4 = 0; var4 < var3.anInt2223; ++var4) {
            Class25 var5 = var3.aClass25Array2221[var4];
            if ((var5.aLong498 >> 29 & 3L) == 2L && var5.anInt483 == var1 && var5.anInt478 == var2) {
               Class158.method2186(var5);
               return var5;
            }
         }

      }
      return null;
   }

   static void method2219(DataBuffer var0) {
      try {

          while(var0.buffer.length > var0.index) {
            int var4 = 0;
            boolean var3 = false;
            int var5 = 0;
            if(var0.readUnsignedByte() == 1) {
               var3 = true;
               var4 = var0.readUnsignedByte();
               var5 = var0.readUnsignedByte();
            }

            int var6 = var0.readUnsignedByte();
            int var7 = var0.readUnsignedByte();
            int var9 = -(var7 * 64) - (-Unsorted.anInt65 - Class108.anInt1460 + 1);
            int var8 = var6 * 64 + -TextureOperation37.anInt3256;
            byte var2;
            int var10;
            if(var8 >= 0 && (var9 - 63) >= 0 && Class23.anInt455 > (var8 + 63) && var9 < Class108.anInt1460) {
               var10 = var8 >> 6;
               int var11 = var9 >> 6;

               for(int var12 = 0; var12 < 64; ++var12) {
                  for(int var13 = 0; var13 < 64; ++var13) {
                     if(!var3 || (8 * var4) <= var12 && var12 < 8 + var4 * 8 && var13 >= var5 * 8 && (8 + var5 * 8) > var13) {
                        var2 = var0.readSignedByte();
                        if(var2 != 0) {
                           if(RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11] == null) {
                              RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11] = new byte[4096];
                           }

                           RenderAnimationDefinition.aByteArrayArrayArray383[var10][var11][var12 + (-var13 + 63 << 6)] = var2;
                           byte var14 = var0.readSignedByte();
                           if(Class3_Sub10.aByteArrayArrayArray2339[var10][var11] == null) {
                              Class3_Sub10.aByteArrayArrayArray2339[var10][var11] = new byte[4096];
                           }

                           Class3_Sub10.aByteArrayArrayArray2339[var10][var11][var12 + (63 - var13 << 6)] = var14;
                        }
                     }
                  }
               }
            } else {
               for(var10 = 0; var10 < (var3 ? 64 : 4096); ++var10) {
                  var2 = var0.readSignedByte();
                  if(0 != var2) {
                     ++var0.index;
                  }
               }
            }
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "dk.C(" + (var0 != null?"{...}":"null") + ',' + false + ')');
      }
   }

}

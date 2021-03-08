package org.runite.client;

import org.rs09.client.util.ArrayUtils;

public final class Class3_Sub13_Sub22 extends Class3_Sub13 {

   static Player[] players = new Player[2048];
   public static AbstractIndexedSprite[] nameIconsSpriteArray;
   static long[] aLongArray3271 = new long[500];
   static boolean[] aBooleanArray3272;
   static Class61[][][] aClass61ArrayArrayArray3273 = new Class61[4][104][104];
   static boolean aBoolean3275 = true;
   private int anInt3276;


   protected Class3_Sub13_Sub22() {
      super(0, true);
      this.anInt3276 = 4096;

      try {
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "mi.<init>(" + 4096 + ')');
      }
   }

   final void method157(int var1, DataBuffer var2, boolean var3) {
      try {
         if(!var3) {
            this.method154(121, (byte)52);
         }

         if(var1 == 0) {
            this.anInt3276 = (var2.readUnsignedByte() << 12) / 255;
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "mi.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static void method273(int[] var0, NPC var2, int[] var3, int[] var4) {
      try {
         int var5 = 0;
         while(var5 < var4.length) {
            int var6 = var4[var5];
            int var7 = var0[var5];
            int var8 = var3[var5];

            for(int var9 = 0; var7 != 0 && var9 < var2.aClass145Array2809.length; ++var9) {
               if((1 & var7) != 0) {
                  if(var6 == -1) {
                     var2.aClass145Array2809[var9] = null;
                  } else {
                     SequenceDefinition var10 = SequenceDefinition.getAnimationDefinition(var6);
                     Class145 var12 = var2.aClass145Array2809[var9];
                     int var11 = var10.delayType;
                     if(null != var12) {
                        if(var12.animationId != var6) {
                           if(SequenceDefinition.getAnimationDefinition(var12.animationId).forcedPriority <= var10.forcedPriority) {
                              var12 = var2.aClass145Array2809[var9] = null;
                           }
                        } else if(var11 == 0) {
                           var12 = var2.aClass145Array2809[var9] = null;
                        } else if(var11 == 1) {
                           var12.anInt1893 = 0;
                           var12.anInt1894 = 0;
                           var12.anInt1891 = 1;
                           var12.anInt1897 = 0;
                           var12.anInt1900 = var8;
                           Unsorted.method1470(var2.anInt2829, var10, 183921384, var2.anInt2819, false, 0);
                        } else if(var11 == 2) {
                           var12.anInt1894 = 0;
                        }
                     }

                     if(null == var12) {
                        var12 = var2.aClass145Array2809[var9] = new Class145();
                        var12.anInt1891 = 1;
                        var12.anInt1897 = 0;
                        var12.anInt1900 = var8;
                        var12.animationId = var6;
                        var12.anInt1894 = 0;
                        var12.anInt1893 = 0;
                        Unsorted.method1470(var2.anInt2829, var10, 183921384, var2.anInt2819, false, 0);
                     }
                  }
               }

               var7 >>>= 1;
            }

            ++var5;
         }

      } catch (RuntimeException var13) {
         throw ClientErrorException.clientError(var13, "mi.B(" + (var0 != null?"{...}":"null") + ',' + (byte) 92 + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + (var4 != null?"{...}":"null") + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int[] var4 = this.aClass114_2382.method1709(var1);
         if(this.aClass114_2382.aBoolean1580) {
            ArrayUtils.fill(var4, 0, Class113.anInt1559, this.anInt3276);
         }

         return var4;
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "mi.D(" + var1 + ',' + var2 + ')');
      }
   }

}

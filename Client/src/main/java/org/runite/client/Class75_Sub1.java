package org.runite.client;

final class Class75_Sub1 extends Class75 {

   private final int anInt2629;
   private final int anInt2630;
   private final int anInt2632;
   private final int anInt2635;


   final void method1335(int var1, int var2, int var3) {
      try {
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ci.D(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final void method1341(int var2, int var3) {
      try {

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ci.A(" + 2 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static void method1342(int[] var0, int[] var1, Player var2, int[] var4) {
      try {
         int var5 = 0;

          while(var1.length > var5) {
            int var6 = var1[var5];
            int var7 = var4[var5];
            int var8 = var0[var5];

            for(int var9 = 0; var7 != 0 && var2.aClass145Array2809.length > var9; var7 >>>= 1) {
               if((1 & var7) != 0) {
                  if(var6 == -1) {
                     var2.aClass145Array2809[var9] = null;
                  } else {
                     SequenceDefinition var10 = SequenceDefinition.getAnimationDefinition(var6);
                     int var11 = var10.delayType;
                     Class145 var12 = var2.aClass145Array2809[var9];
                     if(var12 != null) {
                        if(var12.animationId == var6) {
                           if(var11 == 0) {
                              var12 = var2.aClass145Array2809[var9] = null;
                           } else if (1 == var11) {
                              var12.anInt1894 = 0;
                              var12.anInt1891 = 1;
                              var12.anInt1893 = 0;
                              var12.anInt1900 = var8;
                              var12.anInt1897 = 0;
                              Unsorted.method1470(var2.zAxis, var10, var2.xAxis, var2 == Class102.player, 0);
                           } else if (var11 == 2) {
                              var12.anInt1894 = 0;
                           }
                        } else if(var10.forcedPriority >= SequenceDefinition.getAnimationDefinition(var12.animationId).forcedPriority) {
                           var12 = var2.aClass145Array2809[var9] = null;
                        }
                     }

                     if(null == var12) {
                        var12 = var2.aClass145Array2809[var9] = new Class145();
                        var12.animationId = var6;
                        var12.anInt1891 = 1;
                        var12.anInt1897 = 0;
                        var12.anInt1900 = var8;
                        var12.anInt1893 = 0;
                        var12.anInt1894 = 0;
                        Unsorted.method1470(var2.zAxis, var10, var2.xAxis, var2 == Class102.player, 0);
                     }
                  }
               }

               ++var9;
            }

            ++var5;
         }

      } catch (RuntimeException var13) {
         throw ClientErrorException.clientError(var13, "ci.B(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + (byte) -113 + ',' + (var4 != null?"{...}":"null") + ')');
      }
   }

   final void method1337(int var1, boolean var2, int var3) {
      try {
         int var4 = var3 * this.anInt2629 >> 12;
         if(!var2) {
            this.method1335(67, -82, -112);
         }

         int var5 = this.anInt2635 * var3 >> 12;
         int var6 = var1 * this.anInt2630 >> 12;
         int var7 = var1 * this.anInt2632 >> 12;
         TextureOperation25.method330(this.anInt1104, -111, var7, var4, var6, var5);
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ci.E(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   Class75_Sub1(int var1, int var2, int var3, int var4, int var5, int var6) {
      super(-1, var5, var6);

      try {
         this.anInt2632 = var4;
         this.anInt2630 = var2;
         this.anInt2629 = var1;
         this.anInt2635 = var3;
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ci.<init>(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

}

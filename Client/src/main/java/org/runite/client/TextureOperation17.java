package org.runite.client;

import java.util.Objects;

final class TextureOperation17 extends TextureOperation {

   private int anInt3174;
   private int anInt3175 = 0;
   private int anInt3176 = 0;
   static LinkedList aLinkedList_3177 = new LinkedList();
   private int anInt3178 = 0;
   static int anInt3179;
   private int anInt3180;
   static int[] anIntArray3181;
   private int anInt3182;
   static boolean stereoSound = true;
   static int[] anIntArray3185 = new int[25];
   private int anInt3186;
   private int anInt3188;
   private int anInt3189;


   final void decode(int var1, DataBuffer var2) {
      try {
         if(var1 == 0) {
            this.anInt3175 = var2.readSignedShort();
         } else if(var1 == 1) {
            this.anInt3178 = (var2.readSignedByte() << 12) / 100;
         } else if (var1 == 2) {
             this.anInt3176 = (var2.readSignedByte() << 12) / 100;
         }

         if(!true) {
            this.method240(-114, 127, 95);
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "hk.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
      }
   }

   private void method239(int var1, int var2, int var4) {
      try {
         int var5 = var1 <= 2048?var1 * (4096 + var2) >> 12:-(var1 * var2 >> 12) + var1 + var2;
         if(var5 > 0) {
            var4 *= 6;
            int var7 = -var5 + var1 + var1;
            int var9 = var4 >> 12;
            int var8 = (-var7 + var5 << 12) / var5;
            int var10 = var4 - (var9 << 12);
            int var11 = var5 * var8 >> 12;
            var11 = var11 * var10 >> 12;
            int var12 = var11 + var7;
            int var13 = -var11 + var5;
            if(0 == var9) {
               this.anInt3182 = var7;
               this.anInt3186 = var5;
               this.anInt3174 = var12;
            } else if(var9 == 1) {
               this.anInt3182 = var7;
               this.anInt3174 = var5;
               this.anInt3186 = var13;
            } else if (var9 == 2) {
                this.anInt3186 = var7;
                this.anInt3174 = var5;
                this.anInt3182 = var12;
            } else if (var9 == 3) {
                this.anInt3174 = var13;
                this.anInt3182 = var5;
                this.anInt3186 = var7;
            } else if (var9 == 4) {
                this.anInt3182 = var5;
                this.anInt3186 = var12;
                this.anInt3174 = var7;
            } else if (var9 == 5) {
                this.anInt3174 = var7;
                this.anInt3186 = var5;
                this.anInt3182 = var13;
            }
         } else {
            this.anInt3186 = this.anInt3174 = this.anInt3182 = var1;
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "hk.C(" + var1 + ',' + var2 + ',' + 107 + ',' + var4 + ')');
      }
   }

   public TextureOperation17() {
      super(1, false);
   }

   private void method240(int var2, int var3, int var4) {
      try {
         int var5 = var2 > var3?var2:var3;
         var5 = var5 >= var4 ?var5:var4;
         int var6 = var3 > var2?var2:var3;
         var6 = var6 <= var4 ?var6:var4;
         int var7 = -var6 + var5;
         if(0 < var7) {
            int var9 = (var5 - var3 << 12) / var7;
            int var8 = (var5 + -var2 << 12) / var7;
            int var10 = (-var4 + var5 << 12) / var7;
            if(var2 == var5) {
               this.anInt3180 = var3 == var6 ?var10 + 20480:4096 + -var9;
            } else if (var3 == var5) {
                this.anInt3180 = var4 == var6 ? var8 + 4096 : -var10 + 12288;
            } else {
                this.anInt3180 = var6 != var2 ? -var8 + 20480 : 12288 + var9;
            }

            this.anInt3180 /= 6;
         } else {
            this.anInt3180 = 0;
         }

         this.anInt3188 = (var6 - -var5) / 2;
         if(this.anInt3188 > 0 && 4096 > this.anInt3188) {
            this.anInt3189 = (var7 << 12) / (this.anInt3188 > 2048?8192 - 2 * this.anInt3188:this.anInt3188 * 2);
         } else {
            this.anInt3189 = 0;
         }

      } catch (RuntimeException var11) {
         throw ClientErrorException.clientError(var11, "hk.E(" + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final int[][] method166(int var2) {
      try {
         if(-1 != -1) {
            TextCore.COMMAND_HIGHRES_GRAPHICS_FULLSCREEN = null;
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)-118, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[][] var4 = this.method162(var2, 0, (byte)-72);
            int[] var5 = Objects.requireNonNull(var4)[0];
            int[] var6 = var4[1];
            int[] var7 = var4[2];
            int[] var9 = var3[1];
            int[] var10 = var3[2];
            int[] var8 = var3[0];

            for(int var11 = 0; Class113.anInt1559 > var11; ++var11) {
               this.method240(var5[var11], var6[var11], var7[var11]);
               this.anInt3188 += this.anInt3176;
               if(0 > this.anInt3188) {
                  this.anInt3188 = 0;
               }

               this.anInt3189 += this.anInt3178;
               if(this.anInt3188 > 4096) {
                  this.anInt3188 = 4096;
               }

               if(this.anInt3189 < 0) {
                  this.anInt3189 = 0;
               }

               if(4096 < this.anInt3189) {
                  this.anInt3189 = 4096;
               }

               for(this.anInt3180 += this.anInt3175; this.anInt3180 < 0; this.anInt3180 += 4096) {
               }

               while(this.anInt3180 > 4096) {
                  this.anInt3180 -= 4096;
               }

               this.method239(this.anInt3188, this.anInt3189, this.anInt3180);
               var8[var11] = this.anInt3186;
               var9[var11] = this.anInt3174;
               var10[var11] = this.anInt3182;
            }
         }

         return var3;
      } catch (RuntimeException var12) {
         throw ClientErrorException.clientError(var12, "hk.T(" + -1 + ',' + var2 + ')');
      }
   }

}

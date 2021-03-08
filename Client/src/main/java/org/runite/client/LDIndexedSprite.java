package org.runite.client;

import org.rs09.client.rendering.Toolkit;

final class LDIndexedSprite extends AbstractIndexedSprite {

   private final int[] anIntArray2673;
   byte[] raster;


   final void method1668(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.anIntArray2673.length; ++var4) {
         int var5 = this.anIntArray2673[var4] >> 16 & 255;
         var5 += var1;
         if(var5 < 0) {
            var5 = 0;
         } else if(var5 > 255) {
            var5 = 255;
         }

         int var6 = this.anIntArray2673[var4] >> 8 & 255;
         var6 += var2;
         if(var6 < 0) {
            var6 = 0;
         } else if(var6 > 255) {
            var6 = 255;
         }

         int var7 = this.anIntArray2673[var4] & 255;
         var7 += var3;
         if(var7 < 0) {
            var7 = 0;
         } else if(var7 > 255) {
            var7 = 255;
         }

         this.anIntArray2673[var4] = (var5 << 16) + (var6 << 8) + var7;
      }

   }

   final void method1669(int var1, int var2, int var3, int var4, int var5) {
      int var6 = this.width;
      int var7 = this.height;
      int var8 = 0;
      int var9 = 0;
      int var10 = this.anInt1469;
      int var11 = this.anInt1467;
      int var12 = (var10 << 16) / var3;
      int var13 = (var11 << 16) / var4;
      int var14;
      if(this.anInt1470 > 0) {
         var14 = ((this.anInt1470 << 16) + var12 - 1) / var12;
         var1 += var14;
         var8 += var14 * var12 - (this.anInt1470 << 16);
      }

      if(this.anInt1464 > 0) {
         var14 = ((this.anInt1464 << 16) + var13 - 1) / var13;
         var2 += var14;
         var9 += var14 * var13 - (this.anInt1464 << 16);
      }

      if(var6 < var10) {
         var3 = ((var6 << 16) - var8 + var12 - 1) / var12;
      }

      if(var7 < var11) {
         var4 = ((var7 << 16) - var9 + var13 - 1) / var13;
      }

      var14 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;
      int var15 = Toolkit.JAVA_TOOLKIT.width - var3;
      if(var2 + var4 > Toolkit.JAVA_TOOLKIT.clipBottom) {
         var4 -= var2 + var4 - Toolkit.JAVA_TOOLKIT.clipBottom;
      }

      int var16;
      if(var2 < Toolkit.JAVA_TOOLKIT.clipTop) {
         var16 = Toolkit.JAVA_TOOLKIT.clipTop - var2;
         var4 -= var16;
         var14 += var16 * Toolkit.JAVA_TOOLKIT.width;
         var9 += var13 * var16;
      }

      if(var1 + var3 > Toolkit.JAVA_TOOLKIT.clipRight) {
         var16 = var1 + var3 - Toolkit.JAVA_TOOLKIT.clipRight;
         var3 -= var16;
         var15 += var16;
      }

      if(var1 < Toolkit.JAVA_TOOLKIT.clipLeft) {
         var16 = Toolkit.JAVA_TOOLKIT.clipLeft - var1;
         var3 -= var16;
         var14 += var16;
         var8 += var12 * var16;
         var15 += var16;
      }

      method1673(Toolkit.JAVA_TOOLKIT.getBuffer(), this.raster, this.anIntArray2673, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
   }

   private static void method1670(int[] var0, byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var12 = var3;

      for(int var13 = -var8; var13 < 0; ++var13) {
         int var14 = (var4 >> 16) * var11;

         for(int var15 = -var7; var15 < 0; ++var15) {
            byte var16 = var1[(var3 >> 16) + var14];
            if(var16 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var16 & 255];
            }

             var3 += var9;
         }

         var4 += var10;
         var3 = var12;
         var5 += var6;
      }

   }

   final void method1671() {
      int var1 = 0;

      int var2;
      for(var2 = this.raster.length - 7; var1 < var2; this.raster[var1++] = 0) {
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
         this.raster[var1++] = 0;
      }

      for(var2 += 7; var1 < var2; this.raster[var1++] = 0) {
      }

   }

   private static void method1672(int[] var0, byte[] var1, int[] var2, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10 = -(var6 >> 2);
      var6 = -(var6 & 3);

      for(int var11 = -var7; var11 < 0; ++var11) {
         int var12;
         byte var13;
         for(var12 = var10; var12 < 0; ++var12) {
            var13 = var1[var4++];
            if(var13 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var13 & 255];
            }

             var13 = var1[var4++];
            if(var13 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var13 & 255];
            }

             var13 = var1[var4++];
            if(var13 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var13 & 255];
            }

             var13 = var1[var4++];
            if(var13 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var13 & 255];
            }
         }

         for(var12 = var6; var12 < 0; ++var12) {
            var13 = var1[var4++];
            if(var13 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2[var13 & 255];
            }
         }

         var5 += var8;
         var4 += var9;
      }

   }

   private static void method1673(int[] var0, byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      int var13 = var3;
      int var14 = var12 >> 16 & 255;
      int var15 = var12 >> 8 & 255;
      int var16 = var12 & 255;

      for(int var17 = -var8; var17 < 0; ++var17) {
         int var18 = (var4 >> 16) * var11;

         for(int var19 = -var7; var19 < 0; ++var19) {
            byte var20 = var1[(var3 >> 16) + var18];
            if(var20 == 0) {
               ++var5;
            } else {
               int var24 = var2[var20 & 255];
               int var21 = var24 >> 16 & 255;
               int var22 = var24 >> 8 & 255;
               int var23 = var24 & 255;
               var0[var5++] = (var21 * var14 >> 8 << 16) + (var22 * var15 >> 8 << 8) + (var23 * var16 >> 8);
            }

             var3 += var9;
         }

         var4 += var10;
         var3 = var13;
         var5 += var6;
      }

   }

   public final void rotateClockwise() {
      byte[] rotated = new byte[this.width * this.height];
      int index = 0;

      for(int x = 0; x < this.width; ++x) {
         for(int y = this.height - 1; y >= 0; --y) {
            rotated[index++] = this.raster[x + y * this.width];
         }
      }

      int var3;
      this.raster = rotated;
      var3 = this.anInt1464;
      this.anInt1464 = this.anInt1470;
      this.anInt1470 = this.anInt1467 - this.height - var3;
      var3 = this.height;
      this.height = this.width;
      this.width = var3;
      var3 = this.anInt1467;
      this.anInt1467 = this.anInt1469;
      this.anInt1469 = var3;
   }

   final void method1666(int var1, int var2, int var3) {
      var1 += this.anInt1470;
      var2 += this.anInt1464;
      int var4 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;
      int var5 = 0;
      int var6 = this.height;
      int var7 = this.width;
      int var8 = Toolkit.JAVA_TOOLKIT.width - var7;
      int var9 = 0;
      int var10;
      if(var2 < Toolkit.JAVA_TOOLKIT.clipTop) {
         var10 = Toolkit.JAVA_TOOLKIT.clipTop - var2;
         var6 -= var10;
         var2 = Toolkit.JAVA_TOOLKIT.clipTop;
         var5 += var10 * var7;
         var4 += var10 * Toolkit.JAVA_TOOLKIT.width;
      }

      if(var2 + var6 > Toolkit.JAVA_TOOLKIT.clipBottom) {
         var6 -= var2 + var6 - Toolkit.JAVA_TOOLKIT.clipBottom;
      }

      if(var1 < Toolkit.JAVA_TOOLKIT.clipLeft) {
         var10 = Toolkit.JAVA_TOOLKIT.clipLeft - var1;
         var7 -= var10;
         var1 = Toolkit.JAVA_TOOLKIT.clipLeft;
         var5 += var10;
         var4 += var10;
         var9 += var10;
         var8 += var10;
      }

      if(var1 + var7 > Toolkit.JAVA_TOOLKIT.clipRight) {
         var10 = var1 + var7 - Toolkit.JAVA_TOOLKIT.clipRight;
         var7 -= var10;
         var9 += var10;
         var8 += var10;
      }

      if(var7 > 0 && var6 > 0) {
         method1676(Toolkit.JAVA_TOOLKIT.getBuffer(), this.raster, this.anIntArray2673, var5, var4, var7, var6, var8, var9, var3);
      }
   }

   final void method1675() {
      if(this.width != this.anInt1469 || this.height != this.anInt1467) {
         byte[] var1 = new byte[this.anInt1469 * this.anInt1467];
         int var2 = 0;

         for(int var3 = 0; var3 < this.height; ++var3) {
            for(int var4 = 0; var4 < this.width; ++var4) {
               var1[var4 + this.anInt1470 + (var3 + this.anInt1464) * this.anInt1469] = this.raster[var2++];
            }
         }

         this.raster = var1;
         this.width = this.anInt1469;
         this.height = this.anInt1467;
         this.anInt1470 = 0;
         this.anInt1464 = 0;
      }
   }

   private static void method1676(int[] var0, byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10 = 256 - var9;

      for(int var11 = -var6; var11 < 0; ++var11) {
         for(int var12 = -var5; var12 < 0; ++var12) {
            byte var13 = var1[var3++];
            if(var13 == 0) {
               ++var4;
            } else {
               int var15 = var2[var13 & 255];
               int var14 = var0[var4];
               var0[var4++] = ((var15 & 16711935) * var9 + (var14 & 16711935) * var10 & -16711936) + ((var15 & '\uff00') * var9 + (var14 & '\uff00') * var10 & 16711680) >> 8;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   final void method1677(int var1, int var2, int var3, int var4) {
      int var5 = this.width;
      int var6 = this.height;
      int var7 = 0;
      int var8 = 0;
      int var9 = this.anInt1469;
      int var10 = this.anInt1467;
      int var11 = (var9 << 16) / var3;
      int var12 = (var10 << 16) / var4;
      int var13;
      if(this.anInt1470 > 0) {
         var13 = ((this.anInt1470 << 16) + var11 - 1) / var11;
         var1 += var13;
         var7 += var13 * var11 - (this.anInt1470 << 16);
      }

      if(this.anInt1464 > 0) {
         var13 = ((this.anInt1464 << 16) + var12 - 1) / var12;
         var2 += var13;
         var8 += var13 * var12 - (this.anInt1464 << 16);
      }

      if(var5 < var9) {
         var3 = ((var5 << 16) - var7 + var11 - 1) / var11;
      }

      if(var6 < var10) {
         var4 = ((var6 << 16) - var8 + var12 - 1) / var12;
      }

      var13 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;
      int var14 = Toolkit.JAVA_TOOLKIT.width - var3;
      if(var2 + var4 > Toolkit.JAVA_TOOLKIT.clipBottom) {
         var4 -= var2 + var4 - Toolkit.JAVA_TOOLKIT.clipBottom;
      }

      int var15;
      if(var2 < Toolkit.JAVA_TOOLKIT.clipTop) {
         var15 = Toolkit.JAVA_TOOLKIT.clipTop - var2;
         var4 -= var15;
         var13 += var15 * Toolkit.JAVA_TOOLKIT.width;
         var8 += var12 * var15;
      }

      if(var1 + var3 > Toolkit.JAVA_TOOLKIT.clipRight) {
         var15 = var1 + var3 - Toolkit.JAVA_TOOLKIT.clipRight;
         var3 -= var15;
         var14 += var15;
      }

      if(var1 < Toolkit.JAVA_TOOLKIT.clipLeft) {
         var15 = Toolkit.JAVA_TOOLKIT.clipLeft - var1;
         var3 -= var15;
         var13 += var15;
         var7 += var11 * var15;
         var14 += var15;
      }

      method1670(Toolkit.JAVA_TOOLKIT.getBuffer(), this.raster, this.anIntArray2673, var7, var8, var13, var14, var3, var4, var11, var12, var5);
   }

   final void method1667(int var1, int var2) {
      var1 += this.anInt1470;
      var2 += this.anInt1464;
      int var3 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;
      int var4 = 0;
      int var5 = this.height;
      int var6 = this.width;
      int var7 = Toolkit.JAVA_TOOLKIT.width - var6;
      int var8 = 0;
      int var9;
      if(var2 < Toolkit.JAVA_TOOLKIT.clipTop) {
         var9 = Toolkit.JAVA_TOOLKIT.clipTop - var2;
         var5 -= var9;
         var2 = Toolkit.JAVA_TOOLKIT.clipTop;
         var4 += var9 * var6;
         var3 += var9 * Toolkit.JAVA_TOOLKIT.width;
      }

      if(var2 + var5 > Toolkit.JAVA_TOOLKIT.clipBottom) {
         var5 -= var2 + var5 - Toolkit.JAVA_TOOLKIT.clipBottom;
      }

      if(var1 < Toolkit.JAVA_TOOLKIT.clipLeft) {
         var9 = Toolkit.JAVA_TOOLKIT.clipLeft - var1;
         var6 -= var9;
         var1 = Toolkit.JAVA_TOOLKIT.clipLeft;
         var4 += var9;
         var3 += var9;
         var8 += var9;
         var7 += var9;
      }

      if(var1 + var6 > Toolkit.JAVA_TOOLKIT.clipRight) {
         var9 = var1 + var6 - Toolkit.JAVA_TOOLKIT.clipRight;
         var6 -= var9;
         var8 += var9;
         var7 += var9;
      }

      if(var6 > 0 && var5 > 0) {
         method1672(Toolkit.JAVA_TOOLKIT.getBuffer(), this.raster, this.anIntArray2673, var4, var3, var6, var5, var7, var8);
      }
   }

   LDIndexedSprite(int var1, int var2, int var3, int var4, int var5, int var6, byte[] var7, int[] var8) {
      this.anInt1469 = var1;
      this.anInt1467 = var2;
      this.anInt1470 = var3;
      this.anInt1464 = var4;
      this.width = var5;
      this.height = var6;
      this.raster = var7;
      this.anIntArray2673 = var8;
   }

   LDIndexedSprite(int var1, int var2) {
      this.anInt1469 = this.width = var1;
      this.anInt1467 = this.height = var2;
      this.anInt1470 = this.anInt1464 = 0;
      this.raster = new byte[var1 * var2];
      this.anIntArray2673 = new int[0];
   }
}

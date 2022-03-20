package org.runite.client;

import org.rs09.client.rendering.Toolkit;

public final class Class3_Sub28_Sub16_Sub2_Sub1 extends SoftwareSprite {

   final void method660(int var1, int var2, double var7) {
      try {
         int var10 = -20 / 2;
         int var11 = -20 / 2;
         int var12 = (int)(Math.sin(var7) * 65536.0D);
         int var13 = (int)(Math.cos(var7) * 65536.0D);
         var12 = var12 * 256 >> 8;
         var13 = var13 * 256 >> 8;
         int var14 = (15 << 16) + var11 * var12 + var10 * var13;
         int var15 = (15 << 16) + (var11 * var13 - var10 * var12);
         int var16 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;

         for(var2 = 0; var2 < 20; ++var2) {
            int var17 = var16;
            int var18 = var14;
            int var19 = var15;

            for(var1 = -20; var1 < 0; ++var1) {
               int var20 = this.anIntArray4081[(var18 >> 16) + (var19 >> 16) * this.width];
               int var21 = Toolkit.JAVA_TOOLKIT.getBuffer()[var17];
               int var22 = var20 >>> 24;
               int var23 = 256 - var22;
               Toolkit.JAVA_TOOLKIT.getBuffer()[var17++] = ((var20 & 16711935) * var22 + (var21 & 16711935) * var23 & -16711936) + ((var20 & 65280) * var22 + (var21 & 65280) * var23 & 16711680) >>> 8;
               var18 += var13;
               var19 -= var12;
            }

            var14 += var12;
            var15 += var13;
            var16 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var24) {
      }

   }

   public final void drawAt(int var1, int var2) {
      var1 += this.anInt3701;
      var2 += this.anInt3698;
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
         method673(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   final void method635(int var1, int var2) {
      var1 += this.anInt3701;
      var2 += this.anInt3698;
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
         method673(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   private static void method671(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      int var13 = var3;

      for(int var14 = -var8; var14 < 0; ++var14) {
         int var15 = (var4 >> 16) * var11;

         for(int var16 = -var7; var16 < 0; ++var16) {
            int var17 = var1[(var3 >> 16) + var15];
            int var18 = var0[var5];
            int var19 = (var17 >>> 24) * var12 >> 8;
            int var20 = 256 - var19;
            var0[var5++] = ((var17 & 16711935) * var19 + (var18 & 16711935) * var20 & -16711936) + ((var17 & 65280) * var19 + (var18 & 65280) * var20 & 16711680) >>> 8;
            var3 += var9;
         }

         var4 += var10;
         var3 = var13;
         var5 += var6;
      }

   }

   final void method636(int var1, int var2, int var3, int var4, int var5, int var6) {
      if(var6 != 0) {
         var1 -= this.anInt3701 << 4;
         var2 -= this.anInt3698 << 4;
         double var7 = (double)(var5 & 65535) * 9.587379924285257E-5D;
         int var9 = (int)Math.floor(Math.sin(var7) * (double)var6 + 0.5D);
         int var10 = (int)Math.floor(Math.cos(var7) * (double)var6 + 0.5D);
         int var11 = -var1 * var10 + -var2 * var9;
         int var12 = -(-var1) * var9 + -var2 * var10;
         int var13 = ((this.width << 4) - var1) * var10 + -var2 * var9;
         int var14 = -((this.width << 4) - var1) * var9 + -var2 * var10;
         int var15 = -var1 * var10 + ((this.height << 4) - var2) * var9;
         int var16 = -(-var1) * var9 + ((this.height << 4) - var2) * var10;
         int var17 = ((this.width << 4) - var1) * var10 + ((this.height << 4) - var2) * var9;
         int var18 = -((this.width << 4) - var1) * var9 + ((this.height << 4) - var2) * var10;
         int var19;
         int var20;
         if(var11 < var13) {
            var19 = var11;
            var20 = var13;
         } else {
            var19 = var13;
            var20 = var11;
         }

         if(var15 < var19) {
            var19 = var15;
         }

         if(var17 < var19) {
            var19 = var17;
         }

         if(var15 > var20) {
            var20 = var15;
         }

         if(var17 > var20) {
            var20 = var17;
         }

         int var21;
         int var22;
         if(var12 < var14) {
            var21 = var12;
            var22 = var14;
         } else {
            var21 = var14;
            var22 = var12;
         }

         if(var16 < var21) {
            var21 = var16;
         }

         if(var18 < var21) {
            var21 = var18;
         }

         if(var16 > var22) {
            var22 = var16;
         }

         if(var18 > var22) {
            var22 = var18;
         }

         var19 >>= 12;
         var20 = var20 + 4095 >> 12;
         var21 >>= 12;
         var22 = var22 + 4095 >> 12;
         var19 += var3;
         var20 += var3;
         var21 += var4;
         var22 += var4;
         var19 >>= 4;
         var20 = var20 + 15 >> 4;
         var21 >>= 4;
         var22 = var22 + 15 >> 4;
         if(var19 < Toolkit.JAVA_TOOLKIT.clipLeft) {
            var19 = Toolkit.JAVA_TOOLKIT.clipLeft;
         }

         if(var20 > Toolkit.JAVA_TOOLKIT.clipRight) {
            var20 = Toolkit.JAVA_TOOLKIT.clipRight;
         }

         if(var21 < Toolkit.JAVA_TOOLKIT.clipTop) {
            var21 = Toolkit.JAVA_TOOLKIT.clipTop;
         }

         if(var22 > Toolkit.JAVA_TOOLKIT.clipBottom) {
            var22 = Toolkit.JAVA_TOOLKIT.clipBottom;
         }

         var20 = var19 - var20;
         if(var20 < 0) {
            var22 = var21 - var22;
            if(var22 < 0) {
               int var23 = var21 * Toolkit.JAVA_TOOLKIT.width + var19;
               double var24 = 1.6777216E7D / (double)var6;
               int var26 = (int)Math.floor(Math.sin(var7) * var24 + 0.5D);
               int var27 = (int)Math.floor(Math.cos(var7) * var24 + 0.5D);
               int var28 = (var19 << 4) + 8 - var3;
               int var29 = (var21 << 4) + 8 - var4;
               int var30 = (var1 << 8) - (var29 * var26 >> 4);
               int var31 = (var2 << 8) + (var29 * var27 >> 4);
               int var34;
               int var35;
               int var32;
               int var33;
               int var38;
               int var39;
               int var36;
               int var37;
               int var40;
               int var41;
               if(var27 == 0) {
                  if(var26 == 0) {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30;
                        var36 = var31;
                        var37 = var20;
                        if(var30 >= 0 && var31 >= 0 && var30 - (this.width << 12) < 0 && var31 - (this.height << 12) < 0) {
                           while(var37 < 0) {
                              var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                              var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                              var40 = var38 >>> 24;
                              var41 = 256 - var40;
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                              ++var37;
                           }
                        }

                        ++var33;
                     }
                  } else if(var26 < 0) {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30;
                        var36 = var31 + (var28 * var26 >> 4);
                        var37 = var20;
                        if(var30 >= 0 && var30 - (this.width << 12) < 0) {
                           if((var32 = var36 - (this.height << 12)) >= 0) {
                              var32 = (var26 - var32) / var26;
                              var37 = var20 + var32;
                              var36 += var26 * var32;
                              var34 = var23 + var32;
                           }

                           if((var32 = (var36 - var26) / var26) > var37) {
                              var37 = var32;
                           }

                           while(var37 < 0) {
                              var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                              var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                              var40 = var38 >>> 24;
                              var41 = 256 - var40;
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                              var36 += var26;
                              ++var37;
                           }
                        }

                        ++var33;
                        var30 -= var26;
                     }
                  } else {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30;
                        var36 = var31 + (var28 * var26 >> 4);
                        var37 = var20;
                        if(var30 >= 0 && var30 - (this.width << 12) < 0) {
                           if(var36 < 0) {
                              var32 = (var26 - 1 - var36) / var26;
                              var37 = var20 + var32;
                              var36 += var26 * var32;
                              var34 = var23 + var32;
                           }

                           if((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                              var37 = var32;
                           }

                           while(var37 < 0) {
                              var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                              var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                              var40 = var38 >>> 24;
                              var41 = 256 - var40;
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                              var36 += var26;
                              ++var37;
                           }
                        }

                        ++var33;
                        var30 -= var26;
                     }
                  }
               } else if(var27 < 0) {
                  if(var26 == 0) {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30 + (var28 * var27 >> 4);
                        var36 = var31;
                        var37 = var20;
                        if(var31 >= 0 && var31 - (this.height << 12) < 0) {
                           if((var32 = var35 - (this.width << 12)) >= 0) {
                              var32 = (var27 - var32) / var27;
                              var37 = var20 + var32;
                              var35 += var27 * var32;
                              var34 = var23 + var32;
                           }

                           if((var32 = (var35 - var27) / var27) > var37) {
                              var37 = var32;
                           }

                           while(var37 < 0) {
                              var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                              var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                              var40 = var38 >>> 24;
                              var41 = 256 - var40;
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                              var35 += var27;
                              ++var37;
                           }
                        }

                        ++var33;
                        var31 += var27;
                     }
                  } else if(var26 < 0) {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30 + (var28 * var27 >> 4);
                        var36 = var31 + (var28 * var26 >> 4);
                        var37 = var20;
                        if((var32 = var35 - (this.width << 12)) >= 0) {
                           var32 = (var27 - var32) / var27;
                           var37 = var20 + var32;
                           var35 += var27 * var32;
                           var36 += var26 * var32;
                           var34 = var23 + var32;
                        }

                        if((var32 = (var35 - var27) / var27) > var37) {
                           var37 = var32;
                        }

                        if((var32 = var36 - (this.height << 12)) >= 0) {
                           var32 = (var26 - var32) / var26;
                           var37 += var32;
                           var35 += var27 * var32;
                           var36 += var26 * var32;
                           var34 += var32;
                        }

                        if((var32 = (var36 - var26) / var26) > var37) {
                           var37 = var32;
                        }

                        while(var37 < 0) {
                           var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                           var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                           var40 = var38 >>> 24;
                           var41 = 256 - var40;
                           Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                           var35 += var27;
                           var36 += var26;
                           ++var37;
                        }

                        ++var33;
                        var30 -= var26;
                        var31 += var27;
                     }
                  } else {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30 + (var28 * var27 >> 4);
                        var36 = var31 + (var28 * var26 >> 4);
                        var37 = var20;
                        if((var32 = var35 - (this.width << 12)) >= 0) {
                           var32 = (var27 - var32) / var27;
                           var37 = var20 + var32;
                           var35 += var27 * var32;
                           var36 += var26 * var32;
                           var34 = var23 + var32;
                        }

                        if((var32 = (var35 - var27) / var27) > var37) {
                           var37 = var32;
                        }

                        if(var36 < 0) {
                           var32 = (var26 - 1 - var36) / var26;
                           var37 += var32;
                           var35 += var27 * var32;
                           var36 += var26 * var32;
                           var34 += var32;
                        }

                        if((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                           var37 = var32;
                        }

                        while(var37 < 0) {
                           var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                           var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                           var40 = var38 >>> 24;
                           var41 = 256 - var40;
                           Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                           var35 += var27;
                           var36 += var26;
                           ++var37;
                        }

                        ++var33;
                        var30 -= var26;
                        var31 += var27;
                     }
                  }
               } else if(var26 == 0) {
                  for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                     var34 = var23;
                     var35 = var30 + (var28 * var27 >> 4);
                     var36 = var31;
                     var37 = var20;
                     if(var31 >= 0 && var31 - (this.height << 12) < 0) {
                        if(var35 < 0) {
                           var32 = (var27 - 1 - var35) / var27;
                           var37 = var20 + var32;
                           var35 += var27 * var32;
                           var34 = var23 + var32;
                        }

                        if((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                           var37 = var32;
                        }

                        while(var37 < 0) {
                           var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                           var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                           var40 = var38 >>> 24;
                           var41 = 256 - var40;
                           Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                           var35 += var27;
                           ++var37;
                        }
                     }

                     ++var33;
                     var31 += var27;
                  }
               } else if(var26 < 0) {
                  for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                     var34 = var23;
                     var35 = var30 + (var28 * var27 >> 4);
                     var36 = var31 + (var28 * var26 >> 4);
                     var37 = var20;
                     if(var35 < 0) {
                        var32 = (var27 - 1 - var35) / var27;
                        var37 = var20 + var32;
                        var35 += var27 * var32;
                        var36 += var26 * var32;
                        var34 = var23 + var32;
                     }

                     if((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                        var37 = var32;
                     }

                     if((var32 = var36 - (this.height << 12)) >= 0) {
                        var32 = (var26 - var32) / var26;
                        var37 += var32;
                        var35 += var27 * var32;
                        var36 += var26 * var32;
                        var34 += var32;
                     }

                     if((var32 = (var36 - var26) / var26) > var37) {
                        var37 = var32;
                     }

                     while(var37 < 0) {
                        var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                        var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                        var40 = var38 >>> 24;
                        var41 = 256 - var40;
                        Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                        var35 += var27;
                        var36 += var26;
                        ++var37;
                     }

                     ++var33;
                     var30 -= var26;
                     var31 += var27;
                  }
               } else {
                  for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                     var34 = var23;
                     var35 = var30 + (var28 * var27 >> 4);
                     var36 = var31 + (var28 * var26 >> 4);
                     var37 = var20;
                     if(var35 < 0) {
                        var32 = (var27 - 1 - var35) / var27;
                        var37 = var20 + var32;
                        var35 += var27 * var32;
                        var36 += var26 * var32;
                        var34 = var23 + var32;
                     }

                     if((var32 = (1 + var35 - (this.width << 12) - var27) / var27) > var37) {
                        var37 = var32;
                     }

                     if(var36 < 0) {
                        var32 = (var26 - 1 - var36) / var26;
                        var37 += var32;
                        var35 += var27 * var32;
                        var36 += var26 * var32;
                        var34 += var32;
                     }

                     if((var32 = (1 + var36 - (this.height << 12) - var26) / var26) > var37) {
                        var37 = var32;
                     }

                     while(var37 < 0) {
                        var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                        var39 = Toolkit.JAVA_TOOLKIT.getBuffer()[var34];
                        var40 = var38 >>> 24;
                        var41 = 256 - var40;
                        Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = ((var38 & 16711935) * var40 + (var39 & 16711935) * var41 & -16711936) + ((var38 & 65280) * var40 + (var39 & 65280) * var41 & 16711680) >>> 8;
                        var35 += var27;
                        var36 += var26;
                        ++var37;
                     }

                     ++var33;
                     var30 -= var26;
                     var31 += var27;
                  }
               }

            }
         }
      }
   }

   public final void drawMinimapRegion(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int[] var9, int[] var10) {
      try {
         int var11 = -var3 / 2;
         int var12 = -var4 / 2;
         int var13 = (int)(Math.sin((double)var7 / 326.11D) * 65536.0D);
         int var14 = (int)(Math.cos((double)var7 / 326.11D) * 65536.0D);
         var13 = var13 * var8 >> 8;
         var14 = var14 * var8 >> 8;
         int var15 = (var5 << 16) + var12 * var13 + var11 * var14;
         int var16 = (var6 << 16) + (var12 * var14 - var11 * var13);
         int var17 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;

         for(var2 = 0; var2 < var4; ++var2) {
            int var18 = var9[var2];
            int var19 = var17 + var18;
            int var20 = var15 + var14 * var18;
            int var21 = var16 - var13 * var18;

            for(var1 = -var10[var2]; var1 < 0; ++var1) {
               int var22 = this.anIntArray4081[(var20 >> 16) + (var21 >> 16) * this.width];
               int var23 = Toolkit.JAVA_TOOLKIT.getBuffer()[var19];
               int var24 = var22 >>> 24;
               int var25 = 256 - var24;
               Toolkit.JAVA_TOOLKIT.getBuffer()[var19++] = ((var22 & 16711935) * var24 + (var23 & 16711935) * var25 & -16711936) + ((var22 & 65280) * var24 + (var23 & 65280) * var25 & 16711680) >>> 8;
               var20 += var14;
               var21 -= var13;
            }

            var15 += var13;
            var16 += var14;
            var17 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var26) {
      }

   }

   public final void method667(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var9, int[] var10) {
      try {
         int var11 = -var3 / 2;
         int var12 = -var4 / 2;
         int var13 = (int)(Math.sin((double)var7 / 326.11D) * 65536.0D);
         int var14 = (int)(Math.cos((double)var7 / 326.11D) * 65536.0D);
         var13 = var13 * 256 >> 8;
         var14 = var14 * 256 >> 8;
         int var15 = (var5 << 16) + var12 * var13 + var11 * var14;
         int var16 = (var6 << 16) + (var12 * var14 - var11 * var13);
         int var17 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;

         for(var2 = 0; var2 < var4; ++var2) {
            int var18 = var9[var2];
            int var19 = var17 + var18;
            int var20 = var15 + var14 * var18;
            int var21 = var16 - var13 * var18;

            for(var1 = -var10[var2]; var1 < 0; ++var1) {
               int var22 = this.anIntArray4081[(var20 >> 16) + (var21 >> 16) * this.width];
               int var23 = Toolkit.JAVA_TOOLKIT.getBuffer()[var19];
               int var24 = var22 >>> 24;
               int var25 = 256 - var24;
               Toolkit.JAVA_TOOLKIT.getBuffer()[var19++] = ((var22 & 16711935) * var24 + (var23 & 16711935) * var25 & -16711936) + ((var22 & 65280) * var24 + (var23 & 65280) * var25 & 16711680) >>> 8;
               var20 += var14;
               var21 -= var13;
            }

            var15 += var13;
            var16 += var14;
            var17 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var26) {
      }

   }

   final void method642(int var1, int var2, int var3, int var4, int var5) {
      if(var3 > 0 && var4 > 0) {
         int var6 = this.width;
         int var7 = this.height;
         int var8 = 0;
         int var9 = 0;
         int var10 = this.anInt3697;
         int var11 = this.anInt3706;
         int var12 = (var10 << 16) / var3;
         int var13 = (var11 << 16) / var4;
         int var14;
         if(this.anInt3701 > 0) {
            var14 = ((this.anInt3701 << 16) + var12 - 1) / var12;
            var1 += var14;
            var8 += var14 * var12 - (this.anInt3701 << 16);
         }

         if(this.anInt3698 > 0) {
            var14 = ((this.anInt3698 << 16) + var13 - 1) / var13;
            var2 += var14;
            var9 += var14 * var13 - (this.anInt3698 << 16);
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

         method671(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
      }
   }

   Class3_Sub28_Sub16_Sub2_Sub1(int var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   private static void method672(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -var5;

      for(int var10 = -var6; var10 < 0; ++var10) {
         for(int var11 = var9; var11 < 0; ++var11) {
            int var2 = var1[var3--];
            int var12 = var2 >>> 24;
            if(var12 == 0) {
               ++var4;
            } else {
               int var13 = 256 - var12;
               int var14 = var0[var4];
               var0[var4++] = ((var2 & 16711935) * var12 + (var14 & 16711935) * var13 & -16711936) + ((var2 & 65280) * var12 + (var14 & 65280) * var13 & 16711680) >>> 8;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public final void method639(int var1, int var2, int var3, int var4) {
      if(var3 > 0 && var4 > 0) {
         int var5 = this.width;
         int var6 = this.height;
         int var7 = 0;
         int var8 = 0;
         int var9 = this.anInt3697;
         int var10 = this.anInt3706;
         int var11 = (var9 << 16) / var3;
         int var12 = (var10 << 16) / var4;
         int var13;
         if(this.anInt3701 > 0) {
            var13 = ((this.anInt3701 << 16) + var11 - 1) / var11;
            var1 += var13;
            var7 += var13 * var11 - (this.anInt3701 << 16);
         }

         if(this.anInt3698 > 0) {
            var13 = ((this.anInt3698 << 16) + var12 - 1) / var12;
            var2 += var13;
            var8 += var13 * var12 - (this.anInt3698 << 16);
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

         method675(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var7, var8, var13, var14, var3, var4, var11, var12, var5);
      }
   }

   final void method641(int var1, int var2) {
      var1 += this.anInt3697 - this.width - this.anInt3701;
      var2 += this.anInt3698;
      int var3 = var1 + var2 * Toolkit.JAVA_TOOLKIT.width;
      int var4 = this.width - 1;
      int var5 = this.height;
      int var6 = this.width;
      int var7 = Toolkit.JAVA_TOOLKIT.width - var6;
      int var8 = var6 + var6;
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
         var4 -= var9;
         var3 += var9;
         var8 -= var9;
         var7 += var9;
      }

      if(var1 + var6 > Toolkit.JAVA_TOOLKIT.clipRight) {
         var9 = var1 + var6 - Toolkit.JAVA_TOOLKIT.clipRight;
         var6 -= var9;
         var8 -= var9;
         var7 += var9;
      }

      if(var6 > 0 && var5 > 0) {
         method672(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   final void method637(int var1, int var2, int var3) {
      var1 += this.anInt3701;
      var2 += this.anInt3698;
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
         method674(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var5, var4, var7, var6, var8, var9, var3);
      }
   }

   private static void method673(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -var5;

      for(int var10 = -var6; var10 < 0; ++var10) {
         for(int var11 = var9; var11 < 0; ++var11) {
            int var2 = var1[var3++];
            int var12 = var2 >>> 24;
            if(var12 == 0) {
               ++var4;
            } else {
               int var13 = 256 - var12;
               int var14 = var0[var4];
               var0[var4++] = ((var2 & 16711935) * var12 + (var14 & 16711935) * var13 & -16711936) + ((var2 & 65280) * var12 + (var14 & 65280) * var13 & 16711680) >>> 8;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   private static void method674(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      for(int var10 = -var6; var10 < 0; ++var10) {
         for(int var11 = -var5; var11 < 0; ++var11) {
            int var12 = (var1[var3] >>> 24) * var9 >> 8;
            int var13 = 256 - var12;
            int var14 = var1[var3++];
            int var15 = var0[var4];
            var0[var4++] = ((var14 & 16711935) * var12 + (var15 & 16711935) * var13 & -16711936) + ((var14 & 65280) * var12 + (var15 & 65280) * var13 & 16711680) >>> 8;
         }

         var4 += var7;
         var3 += var8;
      }

   }

   private static void method675(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var12 = var3;

      for(int var13 = -var8; var13 < 0; ++var13) {
         int var14 = (var4 >> 16) * var11;

         for(int var15 = -var7; var15 < 0; ++var15) {
            int var2 = var1[(var3 >> 16) + var14];
            int var16 = var2 >>> 24;
            if(var16 == 0) {
               ++var5;
            } else {
               int var17 = 256 - var16;
               int var18 = var0[var5];
               var0[var5++] = ((var2 & 16711935) * var16 + (var18 & 16711935) * var17 & -16711936) + ((var2 & 65280) * var16 + (var18 & 65280) * var17 & 16711680) >>> 8;
            }

             var3 += var9;
         }

         var4 += var10;
         var3 = var12;
         var5 += var6;
      }

   }
}

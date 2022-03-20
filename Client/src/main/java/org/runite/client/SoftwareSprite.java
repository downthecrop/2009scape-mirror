package org.runite.client;

import org.rs09.client.rendering.Toolkit;

public class SoftwareSprite extends AbstractSprite {

   int[] anIntArray4081;


   final void method652(int var1) {
      if(this.width != this.anInt3697 || this.height != this.anInt3706) {
         int var2 = var1;
         if(var1 > this.anInt3701) {
            var2 = this.anInt3701;
         }

         int var3 = var1;
         if(var1 + this.anInt3701 + this.width > this.anInt3697) {
            var3 = this.anInt3697 - this.anInt3701 - this.width;
         }

         int var4 = var1;
         if(var1 > this.anInt3698) {
            var4 = this.anInt3698;
         }

         int var5 = var1;
         if(var1 + this.anInt3698 + this.height > this.anInt3706) {
            var5 = this.anInt3706 - this.anInt3698 - this.height;
         }

         int var6 = this.width + var2 + var3;
         int var7 = this.height + var4 + var5;
         int[] var8 = new int[var6 * var7];

         for(int var9 = 0; var9 < this.height; ++var9) {
            for(int var10 = 0; var10 < this.width; ++var10) {
               var8[(var9 + var4) * var6 + var10 + var2] = this.anIntArray4081[var9 * this.width + var10];
            }
         }

         this.anIntArray4081 = var8;
         this.width = var6;
         this.height = var7;
         this.anInt3701 -= var2;
         this.anInt3698 -= var4;
      }
   }

   final void method653() {
      int[] var1 = new int[this.width * this.height];
      int var2 = 0;

      for(int var3 = 0; var3 < this.height; ++var3) {
         for(int var4 = this.width - 1; var4 >= 0; --var4) {
            var1[var2++] = this.anIntArray4081[var4 + var3 * this.width];
         }
      }

      this.anIntArray4081 = var1;
      this.anInt3701 = this.anInt3697 - this.width - this.anInt3701;
   }

   void method635(int var1, int var2) {
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
         method659(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   private static void method654(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -(var5 >> 2);
      var5 = -(var5 & 3);

      for(int var10 = -var6; var10 < 0; ++var10) {
         int var11;
         int var2;
         for(var11 = var9; var11 < 0; ++var11) {
            var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }
         }

         for(var11 = var5; var11 < 0; ++var11) {
            var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   final int[] method655() {
      int[] var4 = new int[this.anInt3697 * this.anInt3706];

      for(int var5 = 0; var5 < this.height; ++var5) {
         int var1 = var5 * this.width;
         int var2 = this.anInt3701 + (var5 + this.anInt3698) * this.anInt3697;

         for(int var6 = 0; var6 < this.width; ++var6) {
            int var3 = this.anIntArray4081[var1++];
            var4[var2++] = var3 != 0?-16777216 | var3:0;
         }
      }

      return var4;
   }

   void method636(int var1, int var2, int var3, int var4, int var5, int var6) {
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
               int var36;
               int var37;
               if(var27 == 0) {
                  if(var26 == 0) {
                     for(var33 = var22; var33 < 0; var23 += Toolkit.JAVA_TOOLKIT.width) {
                        var34 = var23;
                        var35 = var30;
                        var36 = var31;
                        var37 = var20;
                        if(var30 >= 0 && var31 >= 0 && var30 - (this.width << 12) < 0 && var31 - (this.height << 12) < 0) {
                           for(; var37 < 0; ++var37) {
                              var38 = this.anIntArray4081[(var36 >> 12) * this.width + (var35 >> 12)];
                              if(var38 == 0) {
                                 ++var34;
                              } else {
                                 Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                              }
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
                              if(var38 == 0) {
                                 ++var34;
                              } else {
                                 Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                              }

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
                              if(var38 == 0) {
                                 ++var34;
                              } else {
                                 Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                              }

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
                              if(var38 == 0) {
                                 ++var34;
                              } else {
                                 Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                              }

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
                           if(var38 == 0) {
                              ++var34;
                           } else {
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                           }

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
                           if(var38 == 0) {
                              ++var34;
                           } else {
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                           }

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
                           if(var38 == 0) {
                              ++var34;
                           } else {
                              Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                           }

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
                        if(var38 == 0) {
                           ++var34;
                        } else {
                           Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                        }

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
                        if(var38 == 0) {
                           ++var34;
                        } else {
                           Toolkit.JAVA_TOOLKIT.getBuffer()[var34++] = var38;
                        }

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

   private static void method656(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8) {
      int var9 = -(var5 >> 2);
      var5 = -(var5 & 3);

      for(int var10 = -var6; var10 < 0; ++var10) {
         int var11;
         int var2;
         for(var11 = var9; var11 < 0; ++var11) {
            var2 = var1[var3--];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3--];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3--];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }

             var2 = var1[var3--];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }
         }

         for(var11 = var5; var11 < 0; ++var11) {
            var2 = var1[var3--];
            if(var2 == 0) {
               ++var4;
            } else {
               var0[var4++] = var2;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   final void method657(int var1) {
      int[] var2 = new int[this.width * this.height];
      int var3 = 0;

      for(int var4 = 0; var4 < this.height; ++var4) {
         for(int var5 = 0; var5 < this.width; ++var5) {
            int var6 = this.anIntArray4081[var3];
            if(var6 == 0) {
               if(var5 > 0 && this.anIntArray4081[var3 - 1] != 0) {
                  var6 = var1;
               } else if(var4 > 0 && this.anIntArray4081[var3 - this.width] != 0) {
                  var6 = var1;
               } else if(var5 < this.width - 1 && this.anIntArray4081[var3 + 1] != 0) {
                  var6 = var1;
               } else if(var4 < this.height - 1 && this.anIntArray4081[var3 + this.width] != 0) {
                  var6 = var1;
               }
            }

            var2[var3++] = var6;
         }
      }

      this.anIntArray4081 = var2;
   }

   final void method658() {
      Class74.setBuffer(this.anIntArray4081, this.width, this.height);
   }

   private static void method659(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      for(int var8 = -var5; var8 < 0; ++var8) {
         int var9;
         for(var9 = var3 + var4 - 3; var3 < var9; var0[var3++] = var1[var2++]) {
            var0[var3++] = var1[var2++];
            var0[var3++] = var1[var2++];
            var0[var3++] = var1[var2++];
         }

         for(var9 += 3; var3 < var9; var0[var3++] = var1[var2++]) {
         }

         var3 += var6;
         var2 += var7;
      }

   }

   void method660(int var1, int var2, double var7) {
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
               if(var20 == 0) {
                  ++var17;
               } else {
                  Toolkit.JAVA_TOOLKIT.getBuffer()[var17++] = var20;
               }

                var18 += var13;
               var19 -= var12;
            }

            var14 += var12;
            var15 += var13;
            var16 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var21) {
      }

   }

   void method641(int var1, int var2) {
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
         method656(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   SoftwareSprite(int var1, int var2, int var3, int var4, int var5, int var6, int[] var7) {
      this.anInt3697 = var1;
      this.anInt3706 = var2;
      this.anInt3701 = var3;
      this.anInt3698 = var4;
      this.width = var5;
      this.height = var6;
      this.anIntArray4081 = var7;
   }

   void method637(int var1, int var2, int var3) {
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
         method662(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var5, var4, var7, var6, var8, var9, var3);
      }
   }

   private static void method661(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12) {
      int var13 = 256 - var12;
      int var14 = var3;

      for(int var15 = -var8; var15 < 0; ++var15) {
         int var16 = (var4 >> 16) * var11;

         for(int var17 = -var7; var17 < 0; ++var17) {
            int var2 = var1[(var3 >> 16) + var16];
            if(var2 == 0) {
               ++var5;
            } else {
               int var18 = var0[var5];
               var0[var5++] = ((var2 & 16711935) * var12 + (var18 & 16711935) * var13 & -16711936) + ((var2 & 65280) * var12 + (var18 & 65280) * var13 & 16711680) >> 8;
            }

             var3 += var9;
         }

         var4 += var10;
         var3 = var14;
         var5 += var6;
      }

   }

   private static void method662(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      int var10 = 256 - var9;

      for(int var11 = -var6; var11 < 0; ++var11) {
         for(int var12 = -var5; var12 < 0; ++var12) {
            int var2 = var1[var3++];
            if(var2 == 0) {
               ++var4;
            } else {
               int var13 = var0[var4];
               var0[var4++] = ((var2 & 16711935) * var9 + (var13 & 16711935) * var10 & -16711936) + ((var2 & 65280) * var9 + (var13 & 65280) * var10 & 16711680) >> 8;
            }
         }

         var4 += var7;
         var3 += var8;
      }

   }

   public void drawAt(int var1, int var2) {
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
         method654(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var4, var3, var6, var5, var7, var8);
      }
   }

   final void method663() {
      int[] var1 = new int[this.width * this.height];
      int var2 = 0;

      for(int var3 = this.height - 1; var3 >= 0; --var3) {
         for(int var4 = 0; var4 < this.width; ++var4) {
            var1[var2++] = this.anIntArray4081[var4 + var3 * this.width];
         }
      }

      this.anIntArray4081 = var1;
      this.anInt3698 = this.anInt3706 - this.height - this.anInt3698;
   }

   public void drawMinimapRegion(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int[] var9, int[] var10) {
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
               Toolkit.JAVA_TOOLKIT.getBuffer()[var19++] = this.anIntArray4081[(var20 >> 16) + (var21 >> 16) * this.width];
               var20 += var14;
               var21 -= var13;
            }

            var15 += var13;
            var16 += var14;
            var17 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var22) {
      }

   }

   final void method665() {
      if(this.width != this.anInt3697 || this.height != this.anInt3706) {
         int[] var1 = new int[this.anInt3697 * this.anInt3706];

         for(int var2 = 0; var2 < this.height; ++var2) {
            for(int var3 = 0; var3 < this.width; ++var3) {
               var1[(var2 + this.anInt3698) * this.anInt3697 + var3 + this.anInt3701] = this.anIntArray4081[var2 * this.width + var3];
            }
         }

         this.anIntArray4081 = var1;
         this.width = this.anInt3697;
         this.height = this.anInt3706;
         this.anInt3701 = 0;
         this.anInt3698 = 0;
      }
   }

   final void drawMinimapIcons(int interfaceWidth, int interfaceHeight, int[] var3, int[] var4) {
      if(Toolkit.JAVA_TOOLKIT.clipBottom - Toolkit.JAVA_TOOLKIT.clipTop == var3.length) {
         interfaceWidth += this.anInt3701;
         interfaceHeight += this.anInt3698;
         int var5 = 0;
         int var6 = this.height;
         int var7 = this.width;
         int var8 = Toolkit.JAVA_TOOLKIT.width - var7;
         int var9 = 0;
         int var10 = interfaceWidth + interfaceHeight * Toolkit.JAVA_TOOLKIT.width;
         int var11;
         if(interfaceHeight < Toolkit.JAVA_TOOLKIT.clipTop) {
            var11 = Toolkit.JAVA_TOOLKIT.clipTop - interfaceHeight;
            var6 -= var11;
            interfaceHeight = Toolkit.JAVA_TOOLKIT.clipTop;
            var5 += var11 * var7;
            var10 += var11 * Toolkit.JAVA_TOOLKIT.width;
         }

         if(interfaceHeight + var6 > Toolkit.JAVA_TOOLKIT.clipBottom) {
            var6 -= interfaceHeight + var6 - Toolkit.JAVA_TOOLKIT.clipBottom;
         }

         if(interfaceWidth < Toolkit.JAVA_TOOLKIT.clipLeft) {
            var11 = Toolkit.JAVA_TOOLKIT.clipLeft - interfaceWidth;
            var7 -= var11;
            interfaceWidth = Toolkit.JAVA_TOOLKIT.clipLeft;
            var5 += var11;
            var10 += var11;
            var9 += var11;
            var8 += var11;
         }

         if(interfaceWidth + var7 > Toolkit.JAVA_TOOLKIT.clipRight) {
            var11 = interfaceWidth + var7 - Toolkit.JAVA_TOOLKIT.clipRight;
            var7 -= var11;
            var9 += var11;
            var8 += var11;
         }

         if(var7 > 0 && var6 > 0) {
            var11 = interfaceWidth - Toolkit.JAVA_TOOLKIT.clipLeft;
            int var12 = interfaceHeight - Toolkit.JAVA_TOOLKIT.clipTop;

            for(int var13 = var12; var13 < var12 + var6; ++var13) {
               int var14 = var3[var13];
               int var15 = var4[var13];
               int var16 = var7;
               int var17;
               if(var11 > var14) {
                  var17 = var11 - var14;
                  if(var17 >= var15) {
                     var5 += var7 + var9;
                     var10 += var7 + var8;
                     continue;
                  }

                  var15 -= var17;
               } else {
                  var17 = var14 - var11;
                  if(var17 >= var7) {
                     var5 += var7 + var9;
                     var10 += var7 + var8;
                     continue;
                  }

                  var5 += var17;
                  var16 = var7 - var17;
                  var10 += var17;
               }

               var17 = 0;
               if(var16 < var15) {
                  var15 = var16;
               } else {
                  var17 = var16 - var15;
               }

               for(int var18 = -var15; var18 < 0; ++var18) {
                  int var19 = this.anIntArray4081[var5++];
                  if(var19 == 0) {
                     ++var10;
                  } else {
                     Toolkit.JAVA_TOOLKIT.getBuffer()[var10++] = var19;
                  }
               }

               var5 += var17 + var9;
               var10 += var17 + var8;
            }

         }
      } else {
         throw new IllegalStateException();
      }
   }

   SoftwareSprite(int var1, int var2) {
      this.anIntArray4081 = new int[var1 * var2];
      this.width = this.anInt3697 = var1;
      this.height = this.anInt3706 = var2;
      this.anInt3701 = this.anInt3698 = 0;
   }

   public void method667(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var9, int[] var10) {
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
               if(var22 == 0) {
                  ++var19;
               } else {
                  Toolkit.JAVA_TOOLKIT.getBuffer()[var19++] = var22;
               }

                var20 += var14;
               var21 -= var13;
            }

            var15 += var13;
            var16 += var14;
            var17 += Toolkit.JAVA_TOOLKIT.width;
         }
      } catch (Exception var23) {
      }

   }

   final void method668(int var1) {
      for(int var2 = this.height - 1; var2 > 0; --var2) {
         int var3 = var2 * this.width;

         for(int var4 = this.width - 1; var4 > 0; --var4) {
            if(this.anIntArray4081[var4 + var3] == 0 && this.anIntArray4081[var4 + var3 - 1 - this.width] != 0) {
               this.anIntArray4081[var4 + var3] = var1;
            }
         }
      }

   }

   void method642(int var1, int var2, int var3, int var4, int var5) {
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

         method661(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var8, var9, var14, var15, var3, var4, var12, var13, var6, var5);
      }
   }

   final void method669(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.anIntArray4081.length; ++var4) {
         int var5 = this.anIntArray4081[var4];
         if(var5 != 0) {
            int var6 = var5 >> 16 & 0xFF;
            var6 += var1;
            if(var6 < 1) {
               var6 = 1;
            } else if(var6 > 255) {
               var6 = 255;
            }

            int var7 = var5 >> 8 & 0xFF;
            var7 += var2;
            if(var7 < 1) {
               var7 = 1;
            } else if(var7 > 255) {
               var7 = 255;
            }

            int var8 = var5 & 0xFF;
            var8 += var3;
            if(var8 < 1) {
               var8 = 1;
            } else if(var8 > 255) {
               var8 = 255;
            }

            this.anIntArray4081[var4] = (var6 << 16) + (var7 << 8) + var8;
         }
      }

   }

   public void method639(int var1, int var2, int var3, int var4) {
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

         method670(Toolkit.JAVA_TOOLKIT.getBuffer(), this.anIntArray4081, var7, var8, var13, var14, var3, var4, var11, var12, var5);
      }
   }

   private static void method670(int[] var0, int[] var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      int var12 = var3;

      for(int var13 = -var8; var13 < 0; ++var13) {
         int var14 = (var4 >> 16) * var11;

         for(int var15 = -var7; var15 < 0; ++var15) {
            int var2 = var1[(var3 >> 16) + var14];
            if(var2 == 0) {
               ++var5;
            } else {
               var0[var5++] = var2;
            }

             var3 += var9;
         }

         var4 += var10;
         var3 = var12;
         var5 += var6;
      }

   }
}

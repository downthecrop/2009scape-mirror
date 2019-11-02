package org.runite.jagex;

import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

final class Class3_Sub28_Sub20 extends Node {

   private boolean aBoolean3781;
   private float aFloat3782;
   private int anInt3783;
   private int anInt3784;
   static RSString aClass94_3785 = RSString.createRSString("Benutzeroberfl-=che geladen)3");
   static CacheIndex aClass153_3786;
   private boolean aBoolean3787;
   private int anInt3788;
   private boolean aBoolean3789;
   private Class82 aClass82_3790;
   private int anInt3791;
   static RSString aClass94_3792 = RSString.createRSString("huffman");
   private int[] anIntArray3793;
   static Class118 aClass118_3794 = Class21.method913(31431);
   private int anInt3795 = -1;
   private int anInt3796 = 0;
   boolean aBoolean3797 = false;
   static RSString aClass94_3798 = RSString.createRSString("RuneScape wird geladen )2 bitte warten)3)3)3");
   private int anInt3799;
   private boolean aBoolean3800;


   final int[] method718(Interface2 var1, int var2, float var3, CacheIndex var4, boolean var5) {
      try {
         if(var2 != 0) {
            return (int[])null;
         } else {
            if(null == this.anIntArray3793 || this.aFloat3782 != var3) {
               if(!this.aClass82_3790.method1408(true, var1, var4)) {
                  return null;
               }

               int var6 = !var5?128:64;
               this.anIntArray3793 = this.aClass82_3790.method1404(var6, this.aBoolean3800, var6, (double)var3, var2 ^ 327680, var4, var1, true);
               this.aFloat3782 = var3;
               if(this.aBoolean3789) {
                  int[] var7 = new int[var6];
                  int[] var10 = new int[var6 * var6];
                  int[] var8 = new int[var6];
                  int[] var9 = new int[var6];
                  int var15 = var6;
                  int var14;
                  int var13 = var14 = var6 * 1;
                  int var19 = var6 * var6;
                  int var18 = -1 + var6;
                  int var17 = var6 - 1;

                  int var11;
                  int var20;
                  for(var20 = 2; var20 >= 0; --var20) {
                     for(var11 = var17; var11 >= 0; --var11) {
                        --var14;
                        int var12 = this.anIntArray3793[var14];
                        var7[var11] += Class3_Sub28_Sub15.method633(var12, 16726965) >> 16;
                        var8[var11] += Class3_Sub28_Sub15.method633('\uff72', var12) >> 8;
                        var9[var11] += Class3_Sub28_Sub15.method633(var12, 255);
                     }

                     if(var14 == 0) {
                        var14 = var19;
                     }
                  }

                  int var25 = var19;

                  for(int var31 = var18; ~var31 <= -1; --var31) {
                     int var30 = 0;
                     int var29 = 0;
                     int var28 = 0;
                     int var26 = 1;
                     int var27 = 1;

                     for(var11 = 2; ~var11 <= -1; --var11) {
                        --var27;
                        var30 += var8[var27];
                        var29 += var9[var27];
                        var28 += var7[var27];
                        if(var27 == 0) {
                           var27 = var15;
                        }
                     }

                     for(var11 = var17; -1 >= ~var11; --var11) {
                        --var26;
                        int var21 = var30 / 9;
                        int var22 = var29 / 9;
                        --var27;
                        var20 = var28 / 9;
                        --var25;
                        var10[var25] = Class3_Sub13_Sub29.bitwiseOr(var22, Class3_Sub13_Sub29.bitwiseOr(var20 << 16, var21 << 8));
                        var28 += var7[var27] + -var7[var26];
                        var29 += var9[var27] - var9[var26];
                        var30 += -var8[var26] + var8[var27];
                        if(-1 == ~var26) {
                           var26 = var15;
                        }

                        if(~var27 == -1) {
                           var27 = var15;
                        }
                     }

                     for(var11 = var17; ~var11 <= -1; --var11) {
                        --var13;
                        int var23 = this.anIntArray3793[var13];
                        --var14;
                        int var24 = this.anIntArray3793[var14];
                        var7[var11] += (Class3_Sub28_Sub15.method633(var24, 16729186) >> 16) + -Class3_Sub28_Sub15.method633(var23 >> 16, 255);
                        var8[var11] += (Class3_Sub28_Sub15.method633('\uff8b', var24) >> 8) + -Class3_Sub28_Sub15.method633(255, var23 >> 8);
                        var9[var11] += -Class3_Sub28_Sub15.method633(var23, 255) + Class3_Sub28_Sub15.method633(255, var24);
                     }

                     if(0 == var13) {
                        var13 = var19;
                     }

                     if(0 == var14) {
                        var14 = var19;
                     }
                  }

                  this.anIntArray3793 = var10;
               }
            }

            return this.anIntArray3793;
         }
      } catch (RuntimeException var32) {
         throw Class44.method1067(var32, "uh.D(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ')');
      }
   }

   final boolean method719(CacheIndex var1, Interface2 var2, int var3, boolean var4) {
      try {
         if(var3 != 579100487) {
            return false;
         } else if(this.aClass82_3790.method1408(true, var2, var1)) {
            GL var5 = HDToolKit.gl;
            int var6 = !var4?128:64;
            int var7 = Class27.method961(1536);
            if(~(1 & var7) == -1) {
               if(0 != ~this.anInt3795) {
                  HDToolKit.bindTexture2D(this.anInt3795);
               } else {
                  int[] var8 = new int[1];
                  var5.glGenTextures(1, var8, 0);
                  this.anInt3791 = Class31.anInt582;
                  this.anInt3795 = var8[0];
                  HDToolKit.bindTexture2D(this.anInt3795);
                  ByteBuffer var9 = ByteBuffer.wrap(this.aClass82_3790.method1407(var6, var6, this.aBoolean3800, var2, 0.7D, 8839, var1));
                  if(2 != this.anInt3788) {
                     if(~this.anInt3788 != -2) {
                        var5.glTexImage2D(3553, 0, 6408, var6, var6, 0, 6408, 5121, var9);
                        var5.glTexParameteri(3553, 10241, 9729);
                        var5.glTexParameteri(3553, 10240, 9729);
                        Class31.anInt580 += var9.limit() - this.anInt3796;
                        this.anInt3796 = var9.limit();
                     } else {
                        int var10 = 0;

                        while(true) {
                           var5.glTexImage2D(3553, var10++, 6408, var6, var6, 0, 6408, 5121, var9);
                           var6 >>= 1;
                           if(0 == var6) {
                              var5.glTexParameteri(3553, 10241, 9987);
                              var5.glTexParameteri(3553, 10240, 9729);
                              Class31.anInt580 += var9.limit() * 4 / 3 - this.anInt3796;
                              this.anInt3796 = 4 * var9.limit() / 3;
                              break;
                           }

                           var9 = ByteBuffer.wrap(this.aClass82_3790.method1407(var6, var6, this.aBoolean3800, var2, 0.7D, 8839, var1));
                        }
                     }
                  } else {
                     GLU var14 = new GLU();
                     var14.gluBuild2DMipmaps(3553, 6408, var6, var6, 6408, 5121, var9);
                     var5.glTexParameteri(3553, 10241, 9987);
                     var5.glTexParameteri(3553, 10240, 9729);
                     Class31.anInt580 += 4 * var9.limit() / 3 - this.anInt3796;
                     this.anInt3796 = var9.limit() * 4 / 3;
                  }

                  var5.glTexParameteri(3553, 10242, !this.aBoolean3787?'\u812f':10497);
                  var5.glTexParameteri(3553, 10243, this.aBoolean3781?10497:'\u812f');
               }
            }

            if(~(2 & var7) == -1) {
               HDToolKit.method1856(this.anInt3784);
            }

            if((4 & var7) == 0) {
               HDToolKit.method1847(0);
            }

            if((var7 & 8) == 0) {
               if(-1 == ~this.anInt3799 && 0 == this.anInt3783) {
                  HDToolKit.method1823();
               } else {
                  float var12 = (float)(this.anInt3799 * HDToolKit.anInt1791) / (float)var6;
                  float var13 = (float)(this.anInt3783 * HDToolKit.anInt1791) / (float)var6;
                  HDToolKit.method1843(var13, var12, 0.0F);
               }
            }

            return true;
         } else {
            return false;
         }
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "uh.F(" + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ')');
      }
   }

   final int[] method720(boolean var1, boolean var2, Interface2 var3, CacheIndex var4) {
      try {
         if(var1) {
            this.method723(45, (byte)75);
         }

         if(!this.aClass82_3790.method1408(true, var3, var4)) {
            return null;
         } else {
            int var5 = !var2?128:64;
            return this.aClass82_3790.method1404(var5, this.aBoolean3800, var5, 1.0D, 327680, var4, var3, false);
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "uh.O(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + (var4 != null?"{...}":"null") + ')');
      }
   }

   public static void method721(int var0) {
      try {
         aClass94_3785 = null;
         aClass94_3792 = null;
         aClass94_3798 = null;
         aClass153_3786 = null;
         aClass118_3794 = null;
         if(var0 != 20413) {
            aClass118_3794 = (Class118)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "uh.C(" + var0 + ')');
      }
   }

   final boolean method722(int var1, Interface2 var2, CacheIndex var3) {
      try {
         return var1 != -5?true:this.aClass82_3790.method1408(true, var2, var3);
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "uh.P(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ')');
      }
   }

   final void method723(int var1, byte var2) {
      try {
         if(var2 != -120) {
            method721(-42);
         }

         if(this.anIntArray3793 != null) {
            if(0 != this.anInt3799 || ~this.anInt3783 != -1) {
               if(null == Class3_Sub23.anIntArray2533 || ~Class3_Sub23.anIntArray2533.length > ~this.anIntArray3793.length) {
                  Class3_Sub23.anIntArray2533 = new int[this.anIntArray3793.length];
               }

               int var5 = var1 * this.anInt3783;
               int var3 = ~this.anIntArray3793.length == -4097?64:128;
               int var4 = this.anIntArray3793.length;
               int var6 = var3 + -1;
               int var7 = this.anInt3799 * var1 * var3;
               int var8 = -1 + var4;

               for(int var9 = 0; var4 > var9; var9 += var3) {
                  int var10 = var7 + var9 & var8;

                  for(int var11 = 0; var3 > var11; ++var11) {
                     int var13 = (var6 & var11 - -var5) + var10;
                     int var12 = var11 + var9;
                     Class3_Sub23.anIntArray2533[var12] = this.anIntArray3793[var13];
                  }
               }

               int[] var15 = this.anIntArray3793;
               this.anIntArray3793 = Class3_Sub23.anIntArray2533;
               Class3_Sub23.anIntArray2533 = var15;
            }

         }
      } catch (RuntimeException var14) {
         throw Class44.method1067(var14, "uh.E(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method724(int var0) {
      try {
         Class163_Sub2_Sub1.aClass93_4015.method1523((byte)-102);
         if(var0 > -106) {
            method725(71);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "uh.B(" + var0 + ')');
      }
   }

   protected final void finalize() throws Throwable {
      try {
         if(this.anInt3795 != -1) {
            Class31.method985(this.anInt3795, this.anInt3796, this.anInt3791);
            this.anInt3796 = 0;
            this.anInt3795 = -1;
         }

         super.finalize();
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "uh.finalize()");
      }
   }

   static final void method725(int var0) {
      try {
         int var1 = 67 / ((-60 - var0) / 41);
         if(~Class137.method1817((byte)70) == -3) {
            byte var2 = (byte)(255 & Class79.anInt1127 + -4);
            int var3 = Class79.anInt1127 % 104;

            int var4;
            int var5;
            for(var4 = 0; -5 < ~var4; ++var4) {
               for(var5 = 0; 104 > var5; ++var5) {
                  Class158.aByteArrayArrayArray2008[var4][var3][var5] = var2;
               }
            }

            if(WorldListCountry.localPlane != 3) {
               for(var4 = 0; -3 < ~var4; ++var4) {
                  Class41.anIntArray686[var4] = -1000000;
                  Class129_Sub1.anIntArray2696[var4] = 1000000;
                  Class159.anIntArray2021[var4] = 0;
                  AnimationDefinition.anIntArray1871[var4] = 1000000;
                  Player.anIntArray3959[var4] = 0;
               }

               if(1 == Class133.anInt1753) {
                  if((4 & Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][Class102.player.anInt2819 >> 7][Class102.player.anInt2829 >> 7]) != 0) {
                     Class140_Sub7.method2031((byte)-85, false, Class102.player.anInt2819 >> 7, Class102.player.anInt2829 >> 7, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 0);
                  }

                  if(-311 < ~Class139.anInt1823) {
                     int var7 = Class102.player.anInt2829 >> 7;
                     var5 = Class77.anInt1111 >> 7;
                     int var9;
                     if(var5 < var7) {
                        var9 = var7 - var5;
                     } else {
                        var9 = -var7 + var5;
                     }

                     var4 = NPC.anInt3995 >> 7;
                     int var6 = Class102.player.anInt2819 >> 7;
                     int var8;
                     if(~var6 < ~var4) {
                        var8 = -var4 + var6;
                     } else {
                        var8 = -var6 + var4;
                     }

                     int var10;
                     int var11;
                     if(var8 > var9) {
                        var11 = '\u8000';
                        var10 = var9 * 65536 / var8;

                        while(var6 != var4) {
                           if(var6 <= var4) {
                              if(var4 > var6) {
                                 --var4;
                              }
                           } else {
                              ++var4;
                           }

                           if(~(Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][var4][var5] & 4) != -1) {
                              Class140_Sub7.method2031((byte)-29, false, var4, var5, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 1);
                              break;
                           }

                           var11 += var10;
                           if(var11 >= 65536) {
                              if(var5 >= var7) {
                                 if(var7 < var5) {
                                    --var5;
                                 }
                              } else {
                                 ++var5;
                              }

                              var11 -= 65536;
                              if(~(4 & Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][var4][var5]) != -1) {
                                 Class140_Sub7.method2031((byte)-120, false, var4, var5, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 1);
                                 break;
                              }
                           }
                        }
                     } else {
                        var11 = '\u8000';
                        var10 = 65536 * var8 / var9;

                        while(~var5 != ~var7) {
                           if(var5 < var7) {
                              ++var5;
                           } else if(var5 > var7) {
                              --var5;
                           }

                           if(~(4 & Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][var4][var5]) != -1) {
                              Class140_Sub7.method2031((byte)-13, false, var4, var5, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 1);
                              break;
                           }

                           var11 += var10;
                           if(~var11 <= -65537) {
                              if(var6 > var4) {
                                 ++var4;
                              } else if(var6 < var4) {
                                 --var4;
                              }

                              var11 -= 65536;
                              if((4 & Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][var4][var5]) != 0) {
                                 Class140_Sub7.method2031((byte)-37, false, var4, var5, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 1);
                                 break;
                              }
                           }
                        }
                     }
                  }
               } else {
                  var4 = Class121.method1736(WorldListCountry.localPlane, 1, NPC.anInt3995, Class77.anInt1111);
                  if(800 > var4 + -Class7.anInt2162 && (4 & Class9.aByteArrayArrayArray113[WorldListCountry.localPlane][NPC.anInt3995 >> 7][Class77.anInt1111 >> 7]) != 0) {
                     Class140_Sub7.method2031((byte)-107, false, NPC.anInt3995 >> 7, Class77.anInt1111 >> 7, Class75_Sub2.aClass3_Sub2ArrayArrayArray2638, 1);
                  }
               }

            }
         }
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "uh.A(" + var0 + ')');
      }
   }

   Class3_Sub28_Sub20(RSByteBuffer var1) {
      try {
         this.aClass82_3790 = new Class82(var1);
         this.aBoolean3789 = var1.getByte((byte)-86) == 1;
         this.aBoolean3800 = 1 == var1.getByte((byte)-80);
         this.aBoolean3787 = -2 == ~var1.getByte((byte)-67);
         this.aBoolean3781 = ~var1.getByte((byte)-114) == -2;
         int var2 = 3 & var1.getByte((byte)-35);
         this.anInt3783 = var1.getByte();
         this.anInt3799 = var1.getByte();
         int var3 = var1.getByte((byte)-86);
         var1.getByte((byte)-87);
         if(var2 == 1) {
            this.anInt3784 = 2;
         } else if(-3 == ~var2) {
            this.anInt3784 = 3;
         } else if(~var2 != -4) {
            this.anInt3784 = 0;
         } else {
            this.anInt3784 = 4;
         }

         this.anInt3788 = (var3 & 240) >> 4;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "uh.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

}

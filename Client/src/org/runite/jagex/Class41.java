package org.runite.jagex;
import java.io.EOFException;
import java.io.IOException;

final class Class41 {

   private Class30 aClass30_681 = null;
   int cacheIndex;
   private Class30 aClass30_683 = null;
   static Class93 aClass93_684 = new Class93(64);
   static int anInt685;
   static int[] anIntArray686 = new int[2];
   private int anInt687 = '\ufde8';
   static int anInt688 = 0;
   static int anInt689;


   static final void method1047(int var0, int var1, int var2, boolean var3, int var4, boolean var5, boolean var6) {
      try {
         if(!var6) {
            if(var2 > var4) {
               int var7 = (var2 + var4) / 2;
               int var8 = var4;
               WorldListEntry var9 = Class3_Sub13_Sub16.aClass44_Sub1Array3201[var7];
               Class3_Sub13_Sub16.aClass44_Sub1Array3201[var7] = Class3_Sub13_Sub16.aClass44_Sub1Array3201[var2];
               Class3_Sub13_Sub16.aClass44_Sub1Array3201[var2] = var9;

               for(int var10 = var4; var10 < var2; ++var10) {
                  if(~RSString.method1535(var9, Class3_Sub13_Sub16.aClass44_Sub1Array3201[var10], 5730, var0, var1, var3, var5) >= -1) {
                     WorldListEntry var11 = Class3_Sub13_Sub16.aClass44_Sub1Array3201[var10];
                     Class3_Sub13_Sub16.aClass44_Sub1Array3201[var10] = Class3_Sub13_Sub16.aClass44_Sub1Array3201[var8];
                     Class3_Sub13_Sub16.aClass44_Sub1Array3201[var8++] = var11;
                  }
               }

               Class3_Sub13_Sub16.aClass44_Sub1Array3201[var2] = Class3_Sub13_Sub16.aClass44_Sub1Array3201[var8];
               Class3_Sub13_Sub16.aClass44_Sub1Array3201[var8] = var9;
               method1047(var0, var1, -1 + var8, var3, var4, var5, false);
               method1047(var0, var1, var2, var3, var8 - -1, var5, false);
            }

         }
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "ge.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   public final String toString() {
      try {
         return "Cache:" + this.cacheIndex;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ge.toString()");
      }
   }

   static final void method1048(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      try {
         if(var5 > -15) {
            anInt688 = -64;
         }

         if(-2 >= ~var1 && var4 >= 1 && 102 >= var1 && var4 <= 102) {
            int var8;
            if(!NPC.method1986(41) && 0 == (2 & Class9.aByteArrayArrayArray113[0][var1][var4])) {
               var8 = var2;
               if((8 & Class9.aByteArrayArrayArray113[var2][var1][var4]) != 0) {
                  var8 = 0;
               }

               if(var8 != Class140_Sub3.anInt2745) {
                  return;
               }
            }

            var8 = var2;
            if(-4 < ~var2 && -3 == ~(2 & Class9.aByteArrayArrayArray113[1][var1][var4])) {
               var8 = var2 + 1;
            }

            Class20.method910(-96, var4, var1, var2, var7, var8, Class86.aClass91Array1182[var2]);
            if(0 <= var0) {
               boolean var9 = KeyboardListener.aBoolean1905;
               KeyboardListener.aBoolean1905 = true;
               Class110.method1683(var8, false, var2, false, Class86.aClass91Array1182[var2], var0, var6, var1, (byte)50, var4, var3);
               KeyboardListener.aBoolean1905 = var9;
            }
         }

      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "ge.H(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
      }
   }

   public static void method1049(boolean var0) {
      try {
         anIntArray686 = null;
         aClass93_684 = null;
         if(!var0) {
            aClass93_684 = (Class93)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ge.G(" + var0 + ')');
      }
   }

   final boolean method1050(int var1, int var2, byte[] var3, byte var4) {
      try {
         Class30 var5 = this.aClass30_681;
         synchronized(var5) {
            if(var4 != -41) {
               return true;
            } else if(0 <= var2 && var2 <= this.anInt687) {
               boolean var6 = this.method1054((byte)87, var2, var1, var3, true);
               if(!var6) {
                  var6 = this.method1054((byte)87, var2, var1, var3, false);
               }

               return var6;
            } else {
               throw new IllegalArgumentException();
            }
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "ge.D(" + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

   final byte[] method1051(int var1, byte var2) {
      try {
         Class30 var3 = this.aClass30_681;
         synchronized(var3) {
            Object var10000;
            try {
               if(~this.aClass30_683.method976(0) > ~((long)(var1 * 6 + 6))) {
                  var10000 = null;
                  return (byte[])var10000;
               }

               this.aClass30_683.method984(-35, (long)(6 * var1));
               this.aClass30_683.method978(0, Class162.aByteArray2040, 6, 0);
               int var5 = ((255 & Class162.aByteArray2040[3]) << 16) - (-(Class162.aByteArray2040[4] << 8 & '\uff00') + -(255 & Class162.aByteArray2040[5]));
               int var6 = 24 / ((-4 - var2) / 40);
               int var4 = (Class162.aByteArray2040[2] & 255) + ('\uff00' & Class162.aByteArray2040[1] << 8) + (16711680 & Class162.aByteArray2040[0] << 16);
               if(var4 < 0 || this.anInt687 < var4) {
                  var10000 = null;
                  return (byte[])var10000;
               }

               if(0 < var5 && ~((long)var5) >= ~(this.aClass30_681.method976(0) / 520L)) {
                  byte[] var7 = new byte[var4];
                  int var8 = 0;

                  int var13;
                  for(int var9 = 0; ~var8 > ~var4; var5 = var13) {
                     if(0 == var5) {
                        var10000 = null;
                        return (byte[])var10000;
                     }

                     int var10 = -var8 + var4;
                     this.aClass30_681.method984(-113, (long)(520 * var5));
                     if(-513 > ~var10) {
                        var10 = 512;
                     }

                     this.aClass30_681.method978(0, Class162.aByteArray2040, 8 + var10, 0);
                     int var11 = (Class162.aByteArray2040[0] << 8 & '\uff00') - -(255 & Class162.aByteArray2040[1]);
                     int var12 = (Class162.aByteArray2040[3] & 255) + ('\uff00' & Class162.aByteArray2040[2] << 8);
                     int var14 = 255 & Class162.aByteArray2040[7];
                     var13 = (Class162.aByteArray2040[6] & 255) + ('\uff00' & Class162.aByteArray2040[5] << 8) + (Class162.aByteArray2040[4] << 16 & 16711680);
                     if(var1 != var11 || var9 != var12 || this.cacheIndex != var14) {
                        var10000 = null;
                        return (byte[])var10000;
                     }

                     if(var13 < 0 || (long)var13 > this.aClass30_681.method976(0) / 520L) {
                        var10000 = null;
                        return (byte[])var10000;
                     }

                     for(int var15 = 0; ~var15 > ~var10; ++var15) {
                        var7[var8++] = Class162.aByteArray2040[var15 + 8];
                     }

                     ++var9;
                  }

                  byte[] var20 = var7;
                  return var20;
               }

               var10000 = null;
            } catch (IOException var17) {
               return null;
            }

            return (byte[])var10000;
         }
      } catch (RuntimeException var19) {
         throw Class44.method1067(var19, "ge.C(" + var1 + ',' + var2 + ')');
      }
   }

   static final RSString method1052(int var0, long var1) {
      try {
         if(-1L > ~var1 && -6582952005840035282L < ~var1) {
            if(-1L == ~(var1 % 37L)) {
               return null;
            } else {
               int var3 = 0;

               for(long var4 = var1; var4 != 0L; ++var3) {
                  var4 /= 37L;
               }

               byte[] var6 = new byte[var3];
               if(var0 != -29664) {
                  method1047(2, -55, -50, false, 52, false, false);
               }

               while(0L != var1) {
                  long var7 = var1;
                  var1 /= 37L;
                  --var3;
                  var6[var3] = Class163_Sub1_Sub1.aByteArray4005[(int)(-(var1 * 37L) + var7)];
               }

               RSString var10 = new RSString();
               var10.byteArray = var6;
               var10.length = var6.length;
               return var10;
            }
         } else {
            return null;
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "ge.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method1053(byte var0, CacheIndex var1) {
      try {
         Class97.aClass153_1372 = var1;
         int var2 = -52 / ((var0 - -55) / 36);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ge.F(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   Class41(int var1, Class30 var2, Class30 var3, int var4) {
      try {
         this.anInt687 = var4;
         this.aClass30_683 = var3;
         this.cacheIndex = var1;
         this.aClass30_681 = var2;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ge.<init>(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

   private final boolean method1054(byte var1, int var2, int var3, byte[] var4, boolean var5) {
      try {
         Class30 var6 = this.aClass30_681;
         synchronized(var6) {
            try {
               int var7;
               boolean var10000;
               if(var5) {
                  if(this.aClass30_683.method976(var1 ^ 87) < (long)(6 + var3 * 6)) {
                     var10000 = false;
                     return var10000;
                  }

                  this.aClass30_683.method984(-116, (long)(6 * var3));
                  this.aClass30_683.method978(0, Class162.aByteArray2040, 6, 0);
                  var7 = (16711680 & Class162.aByteArray2040[3] << 16) + ('\uff00' & Class162.aByteArray2040[4] << 8) + (Class162.aByteArray2040[5] & 255);
                  if(~var7 >= -1 || ~(this.aClass30_681.method976(0) / 520L) > ~((long)var7)) {
                     var10000 = false;
                     return var10000;
                  }
               } else {
                  var7 = (int)((this.aClass30_681.method976(var1 + -87) - -519L) / 520L);
                  if(-1 == ~var7) {
                     var7 = 1;
                  }
               }

               Class162.aByteArray2040[0] = (byte)(var2 >> 16);
               Class162.aByteArray2040[4] = (byte)(var7 >> 8);
               int var8 = 0;
               Class162.aByteArray2040[5] = (byte)var7;
               Class162.aByteArray2040[2] = (byte)var2;
               Class162.aByteArray2040[3] = (byte)(var7 >> 16);
               if(var1 != 87) {
                  this.method1054((byte)41, 108, -107, (byte[])null, true);
               }

               int var9 = 0;
               Class162.aByteArray2040[1] = (byte)(var2 >> 8);
               this.aClass30_683.method984(-14, (long)(var3 * 6));
               this.aClass30_683.method983(Class162.aByteArray2040, 0, var1 ^ -903171097, 6);

               while(true) {
                  if(~var8 > ~var2) {
                     label146: {
                        int var10 = 0;
                        int var11;
                        if(var5) {
                           this.aClass30_681.method984(-116, (long)(520 * var7));

                           try {
                              this.aClass30_681.method978(0, Class162.aByteArray2040, 8, 0);
                           } catch (EOFException var15) {
                              break label146;
                           }

                           var10 = ((Class162.aByteArray2040[4] & 255) << 16) + ('\uff00' & Class162.aByteArray2040[5] << 8) - -(Class162.aByteArray2040[6] & 255);
                           var11 = (255 & Class162.aByteArray2040[1]) + ((Class162.aByteArray2040[0] & 255) << 8);
                           int var13 = 255 & Class162.aByteArray2040[7];
                           int var12 = (Class162.aByteArray2040[3] & 255) + (Class162.aByteArray2040[2] << 8 & '\uff00');
                           if(var11 != var3 || ~var9 != ~var12 || ~this.cacheIndex != ~var13) {
                              var10000 = false;
                              return var10000;
                           }

                           if(var10 < 0 || ~((long)var10) < ~(this.aClass30_681.method976(0) / 520L)) {
                              var10000 = false;
                              return var10000;
                           }
                        }

                        var11 = -var8 + var2;
                        if(~var10 == -1) {
                           var5 = false;
                           var10 = (int)((this.aClass30_681.method976(0) - -519L) / 520L);
                           if(~var10 == -1) {
                              ++var10;
                           }

                           if(~var10 == ~var7) {
                              ++var10;
                           }
                        }

                        Class162.aByteArray2040[7] = (byte)this.cacheIndex;
                        Class162.aByteArray2040[0] = (byte)(var3 >> 8);
                        if(~(-var8 + var2) >= -513) {
                           var10 = 0;
                        }

                        Class162.aByteArray2040[4] = (byte)(var10 >> 16);
                        if(~var11 < -513) {
                           var11 = 512;
                        }

                        Class162.aByteArray2040[1] = (byte)var3;
                        Class162.aByteArray2040[6] = (byte)var10;
                        Class162.aByteArray2040[2] = (byte)(var9 >> 8);
                        Class162.aByteArray2040[3] = (byte)var9;
                        ++var9;
                        Class162.aByteArray2040[5] = (byte)(var10 >> 8);
                        this.aClass30_681.method984(var1 + -128, (long)(var7 * 520));
                        var7 = var10;
                        this.aClass30_681.method983(Class162.aByteArray2040, 0, -903171152, 8);
                        this.aClass30_681.method983(var4, var8, -903171152, var11);
                        var8 += var11;
                        continue;
                     }
                  }

                  var10000 = true;
                  return var10000;
               }
            } catch (IOException var16) {
               return false;
            }
         }
      } catch (RuntimeException var18) {
         throw Class44.method1067(var18, "ge.E(" + var1 + ',' + var2 + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ',' + var5 + ')');
      }
   }

}

package org.runite.jagex;
import java.util.Random;

final class Class3_Sub13_Sub16 extends Class3_Sub13 {

   static RSString aClass94_3190 = RSString.createRSString("<img=1>");
   private int anInt3191 = 2;
   static RSString aClass94_3192 = RSString.createRSString(" ");
   private int anInt3193 = 2048;
   private int anInt3194 = 1;
   private byte[] aByteArray3195 = new byte[512];
   static RSString aClass94_3196 = RSString.createRSString("Fps:");
   private int anInt3197 = 0;
   static int anInt3198 = 0;
   static int anInt3199;
   private short[] aShortArray3200 = new short[512];
   static WorldListEntry[] aClass44_Sub1Array3201 = new WorldListEntry[0];
   static long aLong3202;
   private int anInt3203 = 5;
   private int anInt3204 = 5;
   static int anInt3205;
   static RSString aClass94_3206 = RSString.createRSString("Moteur son pr-Bpar-B");


   private final void method242(byte var1) {
      try {
         Random var2 = new Random((long)this.anInt3197);
         this.aShortArray3200 = new short[512];
         if(var1 == 37) {
            if(0 < this.anInt3193) {
               for(int var3 = 0; 512 > var3; ++var3) {
                  this.aShortArray3200[var3] = (short)Class100.method1603((byte)23, this.anInt3193, var2);
               }
            }

         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "hm.C(" + var1 + ')');
      }
   }

   final void method158(int var1) {
      try {
         if(var1 != 16251) {
            aClass44_Sub1Array3201 = (WorldListEntry[])null;
         }

         this.aByteArray3195 = Class49.method1123(var1 ^ 16727940, this.anInt3197);
         this.method242((byte)37);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hm.P(" + var1 + ')');
      }
   }

   static final int method243(RSString var0, byte var1) {
      try {
         if(var1 != 13) {
            method245(-29);
         }

         for(int var2 = 0; ~var2 > ~Class3_Sub13_Sub19.aClass94Array3238.length; ++var2) {
            if(Class3_Sub13_Sub19.aClass94Array3238[var2].equals(var1 + 40, var0)) {
               return var2;
            }
         }

         return -1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "hm.F(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = 68 % ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = this.anInt3204 * Class163_Sub3.anIntArray2999[var1] + 2048;
            int var6 = var5 >> 12;
            int var7 = var6 - -1;

            for(int var15 = 0; var15 < Class113.anInt1559; ++var15) {
               Class3_Sub13_Sub36.anInt3422 = Integer.MAX_VALUE;
               KeyboardListener.anInt1914 = Integer.MAX_VALUE;
               Class69.anInt1042 = Integer.MAX_VALUE;
               Class3_Sub28_Sub5.anInt3589 = Integer.MAX_VALUE;
               int var16 = this.anInt3203 * Class102.anIntArray2125[var15] + 2048;
               int var17 = var16 >> 12;
               int var18 = 1 + var17;

               int var19;
               for(int var9 = var6 - 1; ~var9 >= ~var7; ++var9) {
                  int var13 = 255 & this.aByteArray3195[(~this.anInt3204 >= ~var9?-this.anInt3204 + var9:var9) & 255];

                  for(int var8 = var17 + -1; var18 >= var8; ++var8) {
                     int var14 = (255 & this.aByteArray3195[(~this.anInt3203 >= ~var8?-this.anInt3203 + var8:var8) + var13 & 255]) * 2;
                     int var10 = -(var8 << 12) - (this.aShortArray3200[var14++] - var16);
                     int var11 = var5 - (this.aShortArray3200[var14] + (var9 << 12));
                     var19 = this.anInt3194;
                     int var12;
                     if(~var19 != -2) {
                        if(3 == var19) {
                           var10 = var10 < 0?-var10:var10;
                           var11 = -1 >= ~var11?var11:-var11;
                           var12 = var11 >= var10?var11:var10;
                        } else if(4 == var19) {
                           var10 = (int)(Math.sqrt((double)((float)(0 > var10?-var10:var10) / 4096.0F)) * 4096.0D);
                           var11 = (int)(Math.sqrt((double)((float)(~var11 <= -1?var11:-var11) / 4096.0F)) * 4096.0D);
                           var12 = var11 + var10;
                           var12 = var12 * var12 >> 12;
                        } else if(-6 == ~var19) {
                           var10 *= var10;
                           var11 *= var11;
                           var12 = (int)(Math.sqrt(Math.sqrt((double)((float)(var11 + var10) / 1.6777216E7F))) * 4096.0D);
                        } else if(2 != var19) {
                           var12 = (int)(4096.0D * Math.sqrt((double)((float)(var11 * var11 + var10 * var10) / 1.6777216E7F)));
                        } else {
                           var12 = (~var10 <= -1?var10:-var10) - -(-1 < ~var11?-var11:var11);
                        }
                     } else {
                        var12 = var11 * var11 + var10 * var10 >> 12;
                     }

                     if(var12 >= Class3_Sub28_Sub5.anInt3589) {
                        if(~var12 > ~Class69.anInt1042) {
                           Class3_Sub13_Sub36.anInt3422 = KeyboardListener.anInt1914;
                           KeyboardListener.anInt1914 = Class69.anInt1042;
                           Class69.anInt1042 = var12;
                        } else if(KeyboardListener.anInt1914 <= var12) {
                           if(var12 < Class3_Sub13_Sub36.anInt3422) {
                              Class3_Sub13_Sub36.anInt3422 = var12;
                           }
                        } else {
                           Class3_Sub13_Sub36.anInt3422 = KeyboardListener.anInt1914;
                           KeyboardListener.anInt1914 = var12;
                        }
                     } else {
                        Class3_Sub13_Sub36.anInt3422 = KeyboardListener.anInt1914;
                        KeyboardListener.anInt1914 = Class69.anInt1042;
                        Class69.anInt1042 = Class3_Sub28_Sub5.anInt3589;
                        Class3_Sub28_Sub5.anInt3589 = var12;
                     }
                  }
               }

               var19 = this.anInt3191;
               if(-1 == ~var19) {
                  var3[var15] = Class3_Sub28_Sub5.anInt3589;
               } else if(var19 != 1) {
                  if(-4 == ~var19) {
                     var3[var15] = KeyboardListener.anInt1914;
                  } else if(var19 == 4) {
                     var3[var15] = Class3_Sub13_Sub36.anInt3422;
                  } else if(-3 == ~var19) {
                     var3[var15] = Class69.anInt1042 + -Class3_Sub28_Sub5.anInt3589;
                  }
               } else {
                  var3[var15] = Class69.anInt1042;
               }
            }
         }

         return var3;
      } catch (RuntimeException var20) {
         throw Class44.method1067(var20, "hm.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method244(int var0, int var1, int var2, int var3, int var4) {
      try {
         int var5;
         if(~var1 >= ~var3) {
            for(var5 = var1; var5 < var3; ++var5) {
               Class38.anIntArrayArray663[var5][var2] = var4;
            }
         } else {
            for(var5 = var3; ~var5 > ~var1; ++var5) {
               Class38.anIntArrayArray663[var5][var2] = var4;
            }
         }

         if(var0 != 2) {
            anInt3198 = -110;
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "hm.E(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(!var3) {
            this.method157(-122, (RSByteBuffer)null, false);
         }

         if(-1 == ~var1) {
            this.anInt3203 = this.anInt3204 = var2.getByte((byte)-57);
         } else if(-2 == ~var1) {
            this.anInt3197 = var2.getByte((byte)-57);
         } else if(2 != var1) {
            if(-4 == ~var1) {
               this.anInt3191 = var2.getByte((byte)-55);
            } else if(var1 == 4) {
               this.anInt3194 = var2.getByte((byte)-124);
            } else if(var1 == 5) {
               this.anInt3203 = var2.getByte((byte)-51);
            } else if(var1 == 6) {
               this.anInt3204 = var2.getByte((byte)-64);
            }
         } else {
            this.anInt3193 = var2.getShort(1);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "hm.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public Class3_Sub13_Sub16() {
      super(0, true);
   }

   public static void method245(int var0) {
      try {
         aClass44_Sub1Array3201 = null;
         aClass94_3192 = null;
         aClass94_3206 = null;
         if(var0 != 0) {
            method245(111);
         }

         aClass94_3190 = null;
         aClass94_3196 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "hm.B(" + var0 + ')');
      }
   }

}

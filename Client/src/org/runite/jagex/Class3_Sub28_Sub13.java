package org.runite.jagex;

final class Class3_Sub28_Sub13 extends Node {

   static int anInt3657;
   int anInt3658;
   static int anInt3659;
   static int anInt3660 = 0;
   static RSString aClass94_3661 = RSString.createRSString("T");
   int anInt3662;
   Class130 aClass130_3663;
   private RSString aClass94_3664;
   static boolean aBoolean3665 = true;
   private Class130 aClass130_3666;
   private int anInt3667;


   private final void method615(int var1, RSByteBuffer var2, byte var3) {
      try {
         if(var3 > -29) {
            anInt3657 = 70;
         }

         if(~var1 != -2) {
            if(~var1 == -3) {
               this.anInt3658 = var2.getByte((byte)-52);
            } else if(3 != var1) {
               if(var1 != 4) {
                  if(5 == var1 || -7 == ~var1) {
                     int var4 = var2.getShort(1);
                     this.aClass130_3663 = new Class130(Class95.method1585((byte)94, var4));

                     for(int var5 = 0; var5 < var4; ++var5) {
                        int var6 = var2.getInt();
                        Object var7;
                        if(~var1 == -6) {
                           var7 = new Class3_Sub29(var2.getString());
                        } else {
                           var7 = new Class3_Sub18(var2.getInt());
                        }

                        this.aClass130_3663.method1779(1, (Class3)var7, (long)var6);
                     }
                  }
               } else {
                  this.anInt3667 = var2.getInt();
               }
            } else {
               this.aClass94_3664 = var2.getString();
            }
         } else {
            this.anInt3662 = var2.getByte((byte)-47);
         }

      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ml.C(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   final RSString method616(int var1, byte var2) {
      try {
         int var3 = 10 / ((var2 - 68) / 50);
         if(null == this.aClass130_3663) {
            return this.aClass94_3664;
         } else {
            Class3_Sub29 var4 = (Class3_Sub29)this.aClass130_3663.method1780((long)var1, 0);
            return null == var4?this.aClass94_3664:var4.aClass94_2586;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ml.S(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method617(RSString var1, int var2) {
      try {
         if(null == this.aClass130_3663) {
            return false;
         } else {
            if(var2 != 8729) {
               this.method615(-97, (RSByteBuffer)null, (byte)-91);
            }

            if(null == this.aClass130_3666) {
               this.method618(0);
            }

            for(Class3_Sub10 var3 = (Class3_Sub10)this.aClass130_3666.method1780(var1.method1538(23), var2 + -8729); var3 != null; var3 = (Class3_Sub10)this.aClass130_3666.method1784(0)) {
               if(var3.aClass94_2341.method1528((byte)-42, var1)) {
                  return true;
               }
            }

            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.F(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   private final void method618(int var1) {
      try {
         this.aClass130_3666 = new Class130(this.aClass130_3663.method1785(81));
         Class3_Sub29 var2 = (Class3_Sub29)this.aClass130_3663.method1776(var1 + 88);
         if(var1 == 0) {
            while(var2 != null) {
               Class3_Sub10 var3 = new Class3_Sub10(var2.aClass94_2586, (int)var2.aLong71);
               this.aClass130_3666.method1779(1, var3, var2.aClass94_2586.method1538(61));
               var2 = (Class3_Sub29)this.aClass130_3663.method1778(-99);
            }

         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.O(" + var1 + ')');
      }
   }

   static final LDIndexedSprite[] method619(byte var0, int var1, CacheIndex var2) {
      try {
         return Class140_Sub7.method2029((byte)-119, var2, var1)?(var0 <= 52?(LDIndexedSprite[])null:Class69.method1281(0)):null;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.A(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final int method620(int var1, int var2) {
      try {
         if(this.aClass130_3663 != null) {
            Class3_Sub18 var3 = (Class3_Sub18)this.aClass130_3663.method1780((long)var2, var1);
            return var3 != null?var3.anInt2467:this.anInt3667;
         } else {
            return this.anInt3667;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.E(" + var1 + ',' + var2 + ')');
      }
   }

   final boolean method621(int var1, int var2) {
      try {
         if(null != this.aClass130_3663) {
            if(this.aClass130_3666 == null) {
               this.method622(109);
            }

            if(var1 != -8143) {
               method619((byte)68, -100, (CacheIndex)null);
            }

            Class3_Sub18 var3 = (Class3_Sub18)this.aClass130_3666.method1780((long)var2, 0);
            return var3 != null;
         } else {
            return false;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.B(" + var1 + ',' + var2 + ')');
      }
   }

   private final void method622(int var1) {
      try {
         this.aClass130_3666 = new Class130(this.aClass130_3663.method1785(93));
         int var3 = -48 % ((26 - var1) / 58);

         for(Class3_Sub18 var2 = (Class3_Sub18)this.aClass130_3663.method1776(123); null != var2; var2 = (Class3_Sub18)this.aClass130_3663.method1778(-88)) {
            Class3_Sub18 var4 = new Class3_Sub18((int)var2.aLong71);
            this.aClass130_3666.method1779(1, var4, (long)var2.anInt2467);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ml.D(" + var1 + ')');
      }
   }

   static final byte[] method623(byte var0, byte[] var1) {
      try {
         if(var0 > -112) {
            method619((byte)43, -121, (CacheIndex)null);
         }

         RSByteBuffer var2 = new RSByteBuffer(var1);
         int var3 = var2.getByte((byte)-67);
         int var4 = var2.getInt();
         if(0 <= var4 && (-1 == ~Class75.anInt1108 || ~Class75.anInt1108 <= ~var4)) {
            if(-1 == ~var3) {
               byte[] var8 = new byte[var4];
               var2.method764(0, var4, var8, (byte)93);
               return var8;
            } else {
               int var5 = var2.getInt();
               if(0 <= var5 && (Class75.anInt1108 == 0 || ~Class75.anInt1108 <= ~var5)) {
                  byte[] var6 = new byte[var5];
                  if(1 != var3) {
                     Class3_Sub22.aClass49_2505.method1128(var6, var2, false);
                  } else {
                     Class105.method1640(var6, var5, var1, var4, 9);
                  }

                  return var6;
               } else {
                  throw new RuntimeException("Error G-zip unpacking!");
               }
            }
         } else {
            throw new RuntimeException();
         }
      } catch (Throwable var7) {
    	  return new byte[0];
//         throw Class44.method1067(var7, "ml.R(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method624(int var0) {
      try {
         aClass94_3661 = null;
         if(var0 != -1) {
            method623((byte)-86, (byte[])null);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ml.P(" + var0 + ')');
      }
   }

   final void method625(RSByteBuffer var1, int var2) {
      try {
         while(true) {
            int var3 = var1.getByte((byte)-83);
            if(-1 == ~var3) {
               var3 = 68 % ((-84 - var2) / 35);
               return;
            }

            this.method615(var3, var1, (byte)-84);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ml.Q(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   public Class3_Sub28_Sub13() {
      this.aClass94_3664 = Class47.aClass94_750;
   }

}

package org.runite.jagex;

final class Class61 {

   static int anInt937;
   static int anInt938;
   static Class93 aClass93_939 = new Class93(4);
   Class3 aClass3_940 = new Class3();
   private Class3 aClass3_941;


   static final Class70 method1209(int var0, int var1, int var2) {
      Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
      if(var3 == null) {
         return null;
      } else {
         Class70 var4 = var3.aClass70_2234;
         var3.aClass70_2234 = null;
         return var4;
      }
   }

   static final Class3_Sub28_Sub9 method1210(int var0, int var1) {
      try {
         Class3_Sub28_Sub9 var2 = (Class3_Sub28_Sub9)Class163.aClass47_2041.method1092((long)var1, 1400);
         if(var0 != 64) {
            method1218(false, -77, -82);
         }

         if(null == var2) {
            byte[] var3 = Class3_Sub13_Sub7.aClass153_3098.getFile(11, (byte)-122, var1);
            var2 = new Class3_Sub28_Sub9();
            if(var3 != null) {
               var2.method583(207, new RSByteBuffer(var3));
            }

            Class163.aClass47_2041.method1097(var2, (long)var1, (byte)98);
            return var2;
         } else {
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ih.G(" + var0 + ',' + var1 + ')');
      }
   }

   final void method1211(int var1) {
      try {
         while(true) {
            Class3 var2 = this.aClass3_940.aClass3_74;
            if(var2 == this.aClass3_940) {
               if(var1 > -47) {
                  this.aClass3_940 = (Class3)null;
               }

               this.aClass3_941 = null;
               return;
            }

            var2.method86(-1024);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.C(" + var1 + ')');
      }
   }

   final Class3 method1212(int var1) {
      try {
         Class3 var2 = this.aClass3_940.aClass3_76;
         if(var1 != 2) {
            method1213(56, (Class3_Sub11[])null);
         }

         if(this.aClass3_940 == var2) {
            this.aClass3_941 = null;
            return null;
         } else {
            this.aClass3_941 = var2.aClass3_76;
            return var2;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.B(" + var1 + ')');
      }
   }

   static final void method1213(int var0, Class3_Sub11[] var1) {
      Class3_Sub23.aClass3_Sub11ArrayArray2542[var0] = var1;
   }

   static final void method1214(int var0, int var1, int var2, int var3, int var4) {
      try {
         Class3_Sub9 var5;
         for(var5 = (Class3_Sub9)Class3.aClass61_78.method1222(); var5 != null; var5 = (Class3_Sub9)Class3.aClass61_78.method1221()) {
            Class3_Sub28_Sub11.method606(var1, var5, var3, var0, var2, 126);
         }

         byte var6;
         RenderAnimationDefinition var7;
         int var8;
         for(var5 = (Class3_Sub9)IOHandler.aClass61_1242.method1222(); var5 != null; var5 = (Class3_Sub9)IOHandler.aClass61_1242.method1221()) {
            var6 = 1;
            var7 = var5.aClass140_Sub4_Sub2_2324.method1965(false);
            if(~var7.anInt368 != ~var5.aClass140_Sub4_Sub2_2324.anInt2764) {
               if(~var7.anInt393 != ~var5.aClass140_Sub4_Sub2_2324.anInt2764 && ~var7.anInt386 != ~var5.aClass140_Sub4_Sub2_2324.anInt2764 && ~var7.anInt375 != ~var5.aClass140_Sub4_Sub2_2324.anInt2764 && ~var7.anInt373 != ~var5.aClass140_Sub4_Sub2_2324.anInt2764) {
                  if(var7.anInt398 == var5.aClass140_Sub4_Sub2_2324.anInt2764 || var7.anInt372 == var5.aClass140_Sub4_Sub2_2324.anInt2764 || var5.aClass140_Sub4_Sub2_2324.anInt2764 == var7.anInt379 || ~var7.anInt406 == ~var5.aClass140_Sub4_Sub2_2324.anInt2764) {
                     var6 = 3;
                  }
               } else {
                  var6 = 2;
               }
            } else {
               var6 = 0;
            }

            if(var5.anInt2322 != var6) {
               var8 = ISAACCipher.method1232(var5.aClass140_Sub4_Sub2_2324, -1);
               if(var8 != var5.anInt2332) {
                  if(var5.aClass3_Sub24_Sub1_2312 != null) {
                     Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var5.aClass3_Sub24_Sub1_2312);
                     var5.aClass3_Sub24_Sub1_2312 = null;
                  }

                  var5.anInt2332 = var8;
               }

               var5.anInt2322 = var6;
            }

            var5.anInt2326 = var5.aClass140_Sub4_Sub2_2324.anInt2819;
            var5.anInt2321 = var5.aClass140_Sub4_Sub2_2324.anInt2819 + var5.aClass140_Sub4_Sub2_2324.getSize((byte)114) * 64;
            var5.anInt2308 = var5.aClass140_Sub4_Sub2_2324.anInt2829;
            var5.anInt2307 = var5.aClass140_Sub4_Sub2_2324.anInt2829 + var5.aClass140_Sub4_Sub2_2324.getSize((byte)114) * 64;
            Class3_Sub28_Sub11.method606(var1, var5, var3, var0, var2, var4 ^ 113);
         }

         if(var4 == 1) {
            for(var5 = (Class3_Sub9)Class3_Sub28_Sub7_Sub1.aClass130_4046.method1776(88); var5 != null; var5 = (Class3_Sub9)Class3_Sub28_Sub7_Sub1.aClass130_4046.method1778(var4 ^ -92)) {
               var6 = 1;
               var7 = var5.aClass140_Sub4_Sub1_2327.method1965(false);
               if(var5.aClass140_Sub4_Sub1_2327.anInt2764 != var7.anInt368) {
                  if(var5.aClass140_Sub4_Sub1_2327.anInt2764 != var7.anInt393 && var5.aClass140_Sub4_Sub1_2327.anInt2764 != var7.anInt386 && ~var5.aClass140_Sub4_Sub1_2327.anInt2764 != ~var7.anInt375 && var7.anInt373 != var5.aClass140_Sub4_Sub1_2327.anInt2764) {
                     if(var7.anInt398 == var5.aClass140_Sub4_Sub1_2327.anInt2764 || var5.aClass140_Sub4_Sub1_2327.anInt2764 == var7.anInt372 || ~var5.aClass140_Sub4_Sub1_2327.anInt2764 == ~var7.anInt379 || ~var5.aClass140_Sub4_Sub1_2327.anInt2764 == ~var7.anInt406) {
                        var6 = 3;
                     }
                  } else {
                     var6 = 2;
                  }
               } else {
                  var6 = 0;
               }

               if(~var5.anInt2322 != ~var6) {
                  var8 = Class81.method1398(0, var5.aClass140_Sub4_Sub1_2327);
                  if(~var5.anInt2332 != ~var8) {
                     if(var5.aClass3_Sub24_Sub1_2312 != null) {
                        Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var5.aClass3_Sub24_Sub1_2312);
                        var5.aClass3_Sub24_Sub1_2312 = null;
                     }

                     var5.anInt2332 = var8;
                  }

                  var5.anInt2322 = var6;
               }

               var5.anInt2326 = var5.aClass140_Sub4_Sub1_2327.anInt2819;
               var5.anInt2321 = var5.aClass140_Sub4_Sub1_2327.anInt2819 + 64 * var5.aClass140_Sub4_Sub1_2327.getSize((byte)114);
               var5.anInt2308 = var5.aClass140_Sub4_Sub1_2327.anInt2829;
               var5.anInt2307 = var5.aClass140_Sub4_Sub1_2327.anInt2829 + var5.aClass140_Sub4_Sub1_2327.getSize((byte)114) * 64;
               Class3_Sub28_Sub11.method606(var1, var5, var3, var0, var2, 110);
            }

         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "ih.K(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final void method1215(boolean var1, Class3 var2) {
      try {
         if(null != var2.aClass3_76) {
            var2.method86(-1024);
         }

         var2.aClass3_74 = this.aClass3_940;
         var2.aClass3_76 = this.aClass3_940.aClass3_76;
         if(var1) {
            var2.aClass3_76.aClass3_74 = var2;
            var2.aClass3_74.aClass3_76 = var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ih.D(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   final void method1216(int var1, Class3 var2) {
      try {
         if(null != var2.aClass3_76) {
            var2.method86(-1024);
         }

         var2.aClass3_74 = this.aClass3_940.aClass3_74;
         var2.aClass3_76 = this.aClass3_940;
         var2.aClass3_76.aClass3_74 = var2;
         var2.aClass3_74.aClass3_76 = var2;
         if(var1 != 64) {
            this.method1221();
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ih.N(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   public static void method1217(int var0) {
      try {
         aClass93_939 = null;
         if(var0 != 0) {
            method1213(56, (Class3_Sub11[])null);
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ih.J(" + var0 + ')');
      }
   }

   static final RSString method1218(boolean var0, int var1, int var2) {
      try {
         return var1 <= 122?(RSString)null:Class118.method1723((byte)-128, var0, 10, var2);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "ih.A(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   final Class3 method1219(int var1) {
      try {
         if(var1 < 13) {
            this.aClass3_940 = (Class3)null;
         }

         Class3 var2 = this.aClass3_941;
         if(this.aClass3_940 == var2) {
            this.aClass3_941 = null;
            return null;
         } else {
            this.aClass3_941 = var2.aClass3_76;
            return var2;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.H(" + var1 + ')');
      }
   }

   final Class3 method1220(byte var1) {
      try {
         Class3 var2 = this.aClass3_940.aClass3_74;
         if(var1 != -3) {
            return (Class3)null;
         } else if(this.aClass3_940 != var2) {
            var2.method86(var1 + -1021);
            return var2;
         } else {
            return null;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.E(" + var1 + ')');
      }
   }

   public Class61() {
      try {
         this.aClass3_940.aClass3_76 = this.aClass3_940;
         this.aClass3_940.aClass3_74 = this.aClass3_940;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ih.<init>()");
      }
   }

   final Class3 method1221() {
      try {
            Class3 var2 = this.aClass3_941;
            if(var2 != this.aClass3_940) {
               this.aClass3_941 = var2.aClass3_74;
               return var2;
            } else {
               this.aClass3_941 = null;
               return null;
            }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.L(" + 1 + ')');
      }
   }

   final Class3 method1222() {
      try {
         Class3 var2 = this.aClass3_940.aClass3_74;
         if(this.aClass3_940 == var2) {
            this.aClass3_941 = null;
            return null;
         } else {
            this.aClass3_941 = var2.aClass3_74;
            return var2;
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ih.F(" + 1 + ')');
      }
   }

}

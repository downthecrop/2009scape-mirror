package org.runite.jagex;

class Class3 {

   long aLong71;
   static int anInt72 = 0;
   static boolean[] aBooleanArray73 = new boolean[200];
   Class3 aClass3_74;
   static RSString[] aClass94Array75 = new RSString[1000];
   Class3 aClass3_76;
   static RSString aClass94_77 = RSString.createRSString("Objet d(Wabonn-Bs");
   static Class61 aClass61_78 = new Class61();


   final boolean method82(int var1) {
      try {
         return var1 != 0?false:null != this.aClass3_76;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ab.I(" + var1 + ')');
      }
   }

   public static void method83(byte var0) {
      try {
         aClass94_77 = null;
         aClass94Array75 = null;
         if(var0 != 30) {
            method84((RSString)null, 89);
         }

         aClass61_78 = null;
         aBooleanArray73 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ab.H(" + var0 + ')');
      }
   }

   static final void method84(RSString var0, int var1) {
      try {
         if(var1 != -801) {
            aClass94_77 = (RSString)null;
         }

         int var2 = Class100.method1602(0, var0);
         if(~var2 != 0) {
            Class3_Sub28_Sub7.method565((byte)86, Class119.aClass131_1624.aShortArray1727[var2], Class119.aClass131_1624.aShortArray1718[var2]);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ab.N(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final void method85(byte var0) {
      try {
         int var1 = -122 % ((var0 - -63) / 48);
         Class136.aClass93_1772.method1523((byte)-99);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ab.G(" + var0 + ')');
      }
   }

   final void method86(int var1) {
      try {
         if(null != this.aClass3_76) {
            this.aClass3_76.aClass3_74 = this.aClass3_74;
            this.aClass3_74.aClass3_76 = this.aClass3_76;
            if(var1 != -1024) {
               this.method86(-35);
            }

            this.aClass3_76 = null;
            this.aClass3_74 = null;
         }
      } catch (RuntimeException var3) {
    	  var3.printStackTrace();
         throw Class44.method1067(var3, "ab.L(" + var1 + ')');
      }
   }

   static final void method87(int var0, int var1) {
      try {
         if(var0 >= -20) {
            method83((byte)44);
         }

         if(0 != var1) {
            if(~var1 != -2) {
               if(2 != var1) {
                  throw new RuntimeException();
               }

               Class9.method850((byte)121);
            } else {
               Class3_Sub28_Sub11.method607(false);
            }

         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "ab.K(" + var0 + ',' + var1 + ')');
      }
   }

   static final Class106[] method88(byte var0) {
      try {
         if(var0 != 28) {
            aBooleanArray73 = (boolean[])null;
         }

         if(Class56.aClass106Array890 == null) {
            Class106[] var1 = Class3_Sub28_Sub10_Sub2.method596(var0 ^ 22, Class38.aClass87_665);
            Class106[] var2 = new Class106[var1.length];
            int var3 = 0;

            label58:
            for(int var4 = 0; var4 < var1.length; ++var4) {
               Class106 var5 = var1[var4];
               if((0 >= var5.anInt1450 || var5.anInt1450 >= 24) && ~var5.anInt1447 <= -801 && 600 <= var5.anInt1449) {
                  for(int var6 = 0; var3 > var6; ++var6) {
                     Class106 var7 = var2[var6];
                     if(var5.anInt1447 == var7.anInt1447 && ~var7.anInt1449 == ~var5.anInt1449) {
                        if(~var5.anInt1450 < ~var7.anInt1450) {
                           var2[var6] = var5;
                        }
                        continue label58;
                     }
                  }

                  var2[var3] = var5;
                  ++var3;
               }
            }

            Class56.aClass106Array890 = new Class106[var3];
            Class76.method1362(var2, 0, Class56.aClass106Array890, 0, var3);
            int[] var9 = new int[Class56.aClass106Array890.length];

            for(int var10 = 0; Class56.aClass106Array890.length > var10; ++var10) {
               Class106 var11 = Class56.aClass106Array890[var10];
               var9[var10] = var11.anInt1449 * var11.anInt1447;
            }

            Class108.method1658(21, var9, Class56.aClass106Array890);
         }

         return Class56.aClass106Array890;
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ab.M(" + var0 + ')');
      }
   }

   static final void method89(boolean var0, CacheIndex var1, CacheIndex var2, CacheIndex var3, CacheIndex var4) {
      try {
         Class12.aClass153_323 = var2;
         Class97.aClass153_1378 = var1;
         Class3_Sub13_Sub29.aClass153_3361 = var3;
         Class119.aClass153_1628 = var4;
         if(!var0) {
            method87(-98, 11);
         }

         GameObject.aClass11ArrayArray1834 = new RSInterface[Class3_Sub13_Sub29.aClass153_3361.method2121(0)][];
         Class130.aBooleanArray1703 = new boolean[Class3_Sub13_Sub29.aClass153_3361.method2121(0)];
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ab.J(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + (var4 != null?"{...}":"null") + ')');
      }
   }

}

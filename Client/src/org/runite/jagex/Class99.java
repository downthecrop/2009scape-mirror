package org.runite.jagex;
import java.awt.Frame;

final class Class99 {

   static short[] aShortArray1398;
   static Class33 aClass33_1399;
   static int anInt1400;
   static Class93 aClass93_1401 = new Class93(500);
   static RSInterface aClass11_1402;
   static int anInt1403 = -1;


   static final void method1596(RSString var0, byte var1, boolean var2) {
      try {
         if(var1 < 124) {
            aClass93_1401 = (Class93)null;
         }

         if(!var2) {
            try {
               Class3_Sub29.anApplet_Sub1_2588.getAppletContext().showDocument(var0.method1547(Class3_Sub29.anApplet_Sub1_2588.getCodeBase(), true), "_top");
            } catch (Exception var5) {
               ;
            }
         } else {
            if(HDToolKit.highDetail && Class3_Sub28_Sub6.aBoolean3594) {
               try {
                  Class42.method1056(Class38.aClass87_665.anApplet1219, "openjs", new Object[]{var0.method1547(Class3_Sub29.anApplet_Sub1_2588.getCodeBase(), true).toString()}, (byte)117);
                  return;
               } catch (Throwable var6) {
                  ;
               }
            }

            try {
               Class3_Sub29.anApplet_Sub1_2588.getAppletContext().showDocument(var0.method1547(Class3_Sub29.anApplet_Sub1_2588.getCodeBase(), true), "_blank");
            } catch (Exception var4) {
               ;
            }
         }

      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "nf.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final Frame method1597(int var0, int var1, int var2, int var3, int var4, Signlink var5) {
      try {
         if(!var5.method1432(false)) {
            return null;
         } else {
            if(0 == var2) {
               Class106[] var6 = Class3_Sub28_Sub10_Sub2.method596(10, var5);
               if(null == var6) {
                  return null;
               }

               boolean var7 = false;

               for(int var8 = 0; ~var8 > ~var6.length; ++var8) {
                  if(var4 == var6[var8].anInt1447 && var3 == var6[var8].anInt1449 && (~var1 == -1 || var1 == var6[var8].anInt1448) && (!var7 || var6[var8].anInt1450 > var2)) {
                     var2 = var6[var8].anInt1450;
                     var7 = true;
                  }
               }

               if(!var7) {
                  return null;
               }
            }

            Class64 var10 = var5.method1450(var1, var2, var3, var4, -121);

            while(0 == var10.anInt978) {
               Class3_Sub13_Sub34.method331(10L, 64);
            }

            Frame var11 = (Frame)var10.anObject974;
            if(null != var11) {
               if(var0 != var10.anInt978) {
                  return var11;
               } else {
                  Class3_Sub28_Sub10_Sub1.method593(var11, true, var5);
                  return null;
               }
            } else {
               return null;
            }
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "nf.D(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ')');
      }
   }

   public static void method1598(int var0) {
      try {
         if(var0 <= -106) {
            aShortArray1398 = null;
            aClass11_1402 = null;
            aClass93_1401 = null;
            aClass33_1399 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "nf.B(" + var0 + ')');
      }
   }

   static final int method1599(int var0, int var1, byte[] var2, byte var3) {
      try {
         int var4 = -1;
         int var5 = -16 % ((var3 - 61) / 57);

         for(int var6 = var0; var1 > var6; ++var6) {
            var4 = var4 >>> 8 ^ Class36.anIntArray634[255 & (var4 ^ var2[var6])];
         }

         var4 = ~var4;
         return var4;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "nf.A(" + var0 + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

}

package org.runite.jagex;

final class Class106 {

   static int anInt1439;
   private static RSString aClass94_1440 = RSString.createRSString("Connected to update server");
   static boolean aBoolean1441 = true;
   static int anInt1442 = 0;
   static Class67 aClass67_1443;
   static short aShort1444 = 256;
   static RSString aClass94_1445 = aClass94_1440;
   static int anInt1446 = 0;
   int anInt1447;
   int anInt1448;
   int anInt1449;
   int anInt1450;
   static boolean hasInternetExplorer6 = false;


   static final void method1642(int var0, RSString var1) {
      try {
         if(null != Class3_Sub28_Sub15.aClass3_Sub19Array3694) {
            if(var0 != 3803) {
               aClass67_1443 = (Class67)null;
            }

            long var3 = var1.toLong(var0 + -3930);
            int var2 = 0;
            if(var3 != 0L) {
               while(Class3_Sub28_Sub15.aClass3_Sub19Array3694.length > var2 && ~Class3_Sub28_Sub15.aClass3_Sub19Array3694[var2].aLong71 != ~var3) {
                  ++var2;
               }

               if(var2 < Class3_Sub28_Sub15.aClass3_Sub19Array3694.length && null != Class3_Sub28_Sub15.aClass3_Sub19Array3694[var2]) {
                  ++Class39.anInt671;
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(162);
                  Class3_Sub13_Sub1.outgoingBuffer.putLong(Class3_Sub28_Sub15.aClass3_Sub19Array3694[var2].aLong71, -2037491440);
               }
            }
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "od.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final int method1643(int var0, boolean var1, int var2, int var3) {
      try {
         if(var0 != 10131) {
            method1644((byte)95);
         }

         Class3_Sub25 var4 = (Class3_Sub25)Class3_Sub2.aClass130_2220.method1780((long)var2, var0 + -10131);
         if(null != var4) {
            int var5 = 0;

            for(int var6 = 0; ~var4.anIntArray2547.length < ~var6; ++var6) {
               if(var4.anIntArray2547[var6] >= 0 && Class3_Sub13_Sub23.itemDefinitionSize > var4.anIntArray2547[var6]) {
                  ItemDefinition var7 = Class38.getItemDefinition(var4.anIntArray2547[var6], (byte)79);
                  if(null != var7.aClass130_798) {
                     Class3_Sub18 var8 = (Class3_Sub18)var7.aClass130_798.method1780((long)var3, 0);
                     if(null != var8) {
                        if(var1) {
                           var5 += var4.anIntArray2551[var6] * var8.anInt2467;
                        } else {
                           var5 += var8.anInt2467;
                        }
                     }
                  }
               }
            }

            return var5;
         } else {
            return 0;
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "od.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   public static void method1644(byte var0) {
      try {
         aClass67_1443 = null;
         if(var0 != 121) {
            aClass67_1443 = (Class67)null;
         }

         aClass94_1445 = null;
         aClass94_1440 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "od.A(" + var0 + ')');
      }
   }

}

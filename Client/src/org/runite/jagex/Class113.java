package org.runite.jagex;

final class Class113 {

   static int interfacePacketCounter = 0;
   int anInt1544;
   int anInt1545;
   static RSString aClass94_1546 = RSString.createRSString("welle:");
   int anInt1547;
   int anInt1548;
   int anInt1549;
   int anInt1550;
   int anInt1551;
   static int anInt1552 = 0;
   int anInt1553;
   int anInt1554;
   int anInt1555;
   static RSString aClass94_1556 = RSString.createRSString("hint_mapmarkers");
   int anInt1557;
   static RSString aClass94_1558 = RSString.createRSString(": ");
   static int anInt1559;
   int anInt1560;
   int anInt1561;
   int anInt1562;
   int anInt1563;
   int anInt1564;
   int anInt1565;
   int anInt1566;
   static RSString aClass94_1567 = RSString.createRSString("mapdots");


   static final void method1702(byte var0, boolean var1) {
      try {
         if(!var1 != !Class14.aBoolean337) {
            Class14.aBoolean337 = var1;
            if(var0 > -117) {
               aClass94_1546 = (RSString)null;
            }

            Class3_Sub10.method139(76);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "pe.B(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method1703(int var0) {
      try {
         aClass94_1558 = null;
         if(var0 != 10967) {
            method1703(-117);
         }

         aClass94_1546 = null;
         aClass94_1556 = null;
         aClass94_1567 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "pe.A(" + var0 + ')');
      }
   }

}

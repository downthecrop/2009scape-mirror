package org.runite.jagex;

final class Class133 {

   private byte aByte1742;
   static int[] anIntArray1743 = new int[25];
   static int anInt1744 = 0;
   static RSString aClass94_1745 = RSString.createRSString("settings");
   int anInt1746;
   int anInt1747;
   static int anInt1748;
   static RSInterface aClass11_1749;
   int anInt1750;
   static CacheIndex aClass153_1751;
   int anInt1752;
   static int anInt1753;
   static int anInt1754;
   static int[] anIntArray1755 = new int[128];
   static int anInt1756;
   int anInt1757;


   public static void method1802(int var0) {
      try {
         aClass94_1745 = null;
         anIntArray1743 = null;
         anIntArray1755 = null;
         aClass153_1751 = null;
         aClass11_1749 = null;
         if(var0 != 25) {
            aClass153_1751 = (CacheIndex)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "sg.A(" + var0 + ')');
      }
   }

   static final void method1803(byte var0) {
      try {
         if(var0 >= 4) {
            Class82.aClass93_1146.method1523((byte)-119);
            Class159.aClass93_2016.method1523((byte)-103);
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "sg.D(" + var0 + ')');
      }
   }

   final int method1804(boolean var1) {
      try {
         if(var1) {
            anInt1753 = -21;
         }

         return this.aByte1742 & 7;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "sg.B(" + var1 + ')');
      }
   }

   final int method1805(byte var1) {
      try {
         int var2 = -74 % ((var1 - 73) / 35);
         return 8 != (this.aByte1742 & 8)?0:1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "sg.C(" + var1 + ')');
      }
   }

   public Class133() {}

   Class133(RSByteBuffer var1) {
      try {
         this.aByte1742 = var1.getByte();
         this.anInt1752 = var1.getShort(1);
         this.anInt1757 = var1.getInt();
         this.anInt1747 = var1.getInt();
         this.anInt1746 = var1.getInt();
         this.anInt1750 = var1.getInt();
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "sg.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

}

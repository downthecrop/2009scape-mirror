package org.runite.jagex;

final class Class124 {

   static Class130 aClass130_1659 = new Class130(512);
   static RSString aClass94_1660 = RSString.createRSString("Versteckt");
   static CacheIndex aClass153_1661;


   public static void method1744(boolean var0) {
      try {
         aClass130_1659 = null;
         aClass153_1661 = null;
         aClass94_1660 = null;
         if(!var0) {
            aClass130_1659 = (Class130)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "rb.A(" + var0 + ')');
      }
   }

   static final void method1745(int var0) {
      try {
         for(int var1 = var0; ~var1 > -105; ++var1) {
            for(int var2 = 0; 104 > var2; ++var2) {
               Class163_Sub1_Sub1.anIntArrayArray4010[var1][var2] = 0;
            }
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rb.B(" + var0 + ')');
      }
   }

   static final void method1746(boolean var0, byte var1) {
      try {
         if(var1 > -31) {
            aClass153_1661 = (CacheIndex)null;
         }

         Class75_Sub4.method1352(Class140_Sub7.anInt2934, var0, -1, Class3_Sub28_Sub12.anInt3655, Class23.anInt454);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "rb.C(" + var0 + ',' + var1 + ')');
      }
   }

   static final Class3_Sub28_Sub3 method1747(RSByteBuffer var0, boolean var1) {
      try {
         Class3_Sub28_Sub3 var2 = new Class3_Sub28_Sub3(var0.getString(), var0.getString(), var0.getShort(1), var0.getShort(1), var0.getInt(), var0.getByte((byte)-91) == 1, var0.getByte((byte)-112));
         int var3 = var0.getByte((byte)-83);

         for(int var4 = 0; ~var4 > ~var3; ++var4) {
            var2.aClass61_3560.method1215(var1, new Class3_Sub21(var0.getShort(1), var0.getShort(1), var0.getShort(1), var0.getShort(1)));
         }

         var2.method538((byte)103);
         return var2;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "rb.D(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

}

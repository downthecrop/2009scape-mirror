package org.runite.jagex;

final class Class143 {

   static int anInt1873;
   static Class93 aClass93_1874 = new Class93(50);
   static int loadingStage = 0;
   private static RSString aClass94_1876 = RSString.createRSString("Loaded fonts");
   static RSString aClass94_1877 = RSString.createRSString(":assist:");
   static RSString aClass94_1878 = RSString.createRSString("tremblement:");
   static RSString aClass94_1879 = aClass94_1876;
   static RSString aClass94_1880 = RSString.createRSString(")1");


   static final void method2062(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      try {
         int var12 = Class40.method1040(Class57.anInt902, var6, (byte)0, Class159.anInt2020);
         int var13 = Class40.method1040(Class57.anInt902, var3, (byte)0, Class159.anInt2020);
         int var14 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var7, (byte)0, Class101.anInt1425);
         int var15 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var0, (byte)0, Class101.anInt1425);
         int var8 = Class40.method1040(Class57.anInt902, var4 + var6, (byte)0, Class159.anInt2020);
         int var9 = Class40.method1040(Class57.anInt902, -var4 + var3, (byte)0, Class159.anInt2020);

         int var16;
         for(var16 = var12; ~var16 > ~var8; ++var16) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var16], var14, 97, var15, var5);
         }

         for(var16 = var13; ~var16 < ~var9; --var16) {
            Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var16], var14, 94, var15, var5);
         }

         int var10 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4 + var7, (byte)0, Class101.anInt1425);
         int var11 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var4 + var0, (byte)0, Class101.anInt1425);
         if(var2 >= -88) {
            aClass94_1876 = (RSString)null;
         }

         for(var16 = var8; ~var16 >= ~var9; ++var16) {
            int[] var17 = Class38.anIntArrayArray663[var16];
            Class3_Sub13_Sub23_Sub1.method282(var17, var14, 105, var10, var5);
            Class3_Sub13_Sub23_Sub1.method282(var17, var10, 111, var11, var1);
            Class3_Sub13_Sub23_Sub1.method282(var17, var11, 109, var15, var5);
         }

      } catch (RuntimeException var18) {
         throw Class44.method1067(var18, "tl.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
      }
   }

   public static void method2063(int var0) {
      try {
         aClass94_1878 = null;
         aClass93_1874 = null;
         aClass94_1880 = null;
         aClass94_1877 = null;
         aClass94_1876 = null;
         if(var0 == 0) {
            aClass94_1879 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "tl.A(" + var0 + ')');
      }
   }

}

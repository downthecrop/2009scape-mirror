package org.runite.jagex;

final class Class3_Sub20 extends Class3 {

   static int[] anIntArray2480 = new int[25];
   
   static RSString aClass94_2482 = RSString.createRSString(":clan:");
   int anInt2483;
   static int wlPacketIndex = 0;
   static int language = 0;
   private static RSString aClass94_2486 = RSString.createRSString("red:");
   static int anInt2487;
   static int anInt2488 = 0;
   int anInt2489;
   static RSString aClass94_2490 = aClass94_2486;
static RSString aClass94_2481 = aClass94_2486;

   static final void method388(byte var0) {
      try {
         //int var1 = 44 / ((-2 - var0) / 52);
         if(Class3_Sub13_Sub19.aClass94_3220 != null) {
            Class3_Sub10.method138(Class3_Sub13_Sub19.aClass94_3220, 0);
            Class3_Sub13_Sub19.aClass94_3220 = null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "lb.A(" + var0 + ')');
      }
   }
   
   static final void method389(boolean var0) {
	      try {
	         Class32.method995();
	         Class49.aClass3_Sub28_Sub16_812 = null;
	         Class58.anInt909 = -1;
	         Class3_Sub13_Sub30.method313((byte)55);
	         Class3_Sub28_Sub21.aClass47_3801.method1101(2);
	         Class100.aClass136_1413 = new Class136();
	         ((Class102)Class51.anInterface2_838).method1618(0);
	         Class68.anInt1032 = 0;
	         Class68.aClass43Array1021 = new Class43[255];
	         Class140_Sub1_Sub1.method1929();
	         Class141.method2043();
	         Class65.method1240(false);
	         Class66.method1250(17, var0);
	         Class3_Sub13_Sub17.method247((byte)51);

	         for(int var1 = 0; 2048 > var1; ++var1) {
	            Player var2 = Class3_Sub13_Sub22.players[var1];
	            if(null != var2) {
	               var2.anObject2796 = null;
	            }
	         }

	         if(HDToolKit.highDetail) {
	            Class141.method2041(104, 104);
	            Class127_Sub1.method1755();
	         }

	         Class3_Sub28_Sub9.method581(CacheIndex.aClass153_1948, 0, Class140_Sub6.spritesCacheIndex);
	         Class14.method887(21, Class140_Sub6.spritesCacheIndex);
	         Class3_Sub26.aClass3_Sub28_Sub16_2560 = null;
	         Class3_Sub13_Sub7.aClass3_Sub28_Sub16_3099 = null;
	         Class50.aClass3_Sub28_Sub16_824 = null;
	         Class95.aClass3_Sub28_Sub16_1339 = null;
	         Class108.aClass3_Sub28_Sub16_1457 = null;
	         if(Class143.loadingStage == 5) {
	            Class108.method1656(Class140_Sub6.spritesCacheIndex, (byte)-60);
	         }

	         if(10 == Class143.loadingStage) {
	            Class3_Sub13_Sub11.method219(false, 3000);
	         }

	         if(Class143.loadingStage == 30) {
	            Class117.method1719(25, 5);
	         }

	      } catch (RuntimeException var3) {
	         throw Class44.method1067(var3, "lb.C(" + var0 + ')');
	      }
	   }

   static final void method390(boolean var0, int var1, int var2, int var3, byte var4, int var5, int var6) {
      try {
         Class3_Sub28_Sub10.anInt3631 = var3;
         Class3_Sub13_Sub34.anInt3414 = var2;
         Canvas_Sub2.anInt30 = var6;
         Class163_Sub2_Sub1.anInt4021 = var1;
         Class146.anInt1904 = var5;
         if(var0 && Class3_Sub28_Sub10.anInt3631 >= 100) {
            NPC.anInt3995 = 128 * Canvas_Sub2.anInt30 + 64;
            Class77.anInt1111 = 128 * Class146.anInt1904 + 64;
            Class7.anInt2162 = Class121.method1736(WorldListCountry.localPlane, 1, NPC.anInt3995, Class77.anInt1111) + -Class3_Sub13_Sub34.anInt3414;
         }

        // int var7 = 76 % ((-79 - var4) / 35);
         Class133.anInt1753 = 2;
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "lb.D(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
      }
   }

   public static void method391(int var0) {
      try {
         aClass94_2490 = null;
         anIntArray2480 = null;
         aClass94_2481 = null;
         if(var0 != 25) {
            method388((byte)-58);
         }

         aClass94_2486 = null;
         aClass94_2482 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "lb.B(" + var0 + ')');
      }
   }

   Class3_Sub20(int var1, int var2) {
      try {
         this.anInt2483 = var2;
         this.anInt2489 = var1;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "lb.<init>(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method392(CacheIndex var0, CacheIndex var1, int var2, CacheIndex var3) {
      try {
         AnimationDefinition.aClass153_1860 = var1;
        // int var4 = -110 / ((-7 - var2) / 43);
         Class7.aClass153_2160 = var0;
         Class131.aClass153_1723 = var3;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "lb.E(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ')');
      }
   }

}

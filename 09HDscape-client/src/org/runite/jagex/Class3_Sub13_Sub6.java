package org.runite.jagex;

final class Class3_Sub13_Sub6 extends Class3_Sub13 {

   private int anInt3073 = 0;
   private int anInt3074 = 4096;
   static Class61 aClass61_3075 = new Class61();
   static int[] anIntArray3076;
   static CacheIndex aClass153_3077;
   static boolean aBoolean3078;
   static RSString aClass94_3079 = RSString.createRSString("Examiner");
   static RSString aClass94_3080 = Class95.method1586(23161, 160);
   static int anInt3081 = 0;
   static int[] anIntArray3082 = new int[100];
   static int[] anIntArray3083 = new int[50];
   static int anInt3084;


   static final void method195(int var0) {
      try {
         int var1 = (NPC.anInt3995 >> 10) - -(Class131.anInt1716 >> 3);
         int var2 = (Class77.anInt1111 >> 10) - -(Class82.anInt1152 >> 3);
         if(var0 == 20479) {
            byte var3 = 0;
            byte var4 = 8;
            byte var6 = 18;
            Class3_Sub22.aByteArrayArray2521 = new byte[var6][];
            Class3_Sub28_Sub5.anIntArray3587 = new int[var6];
            Class3_Sub13_Sub26.aByteArrayArray3335 = new byte[var6][];
            Client.anIntArray2200 = new int[var6];
            Class3_Sub9.regionXteaKeys = new int[var6][4];
            for (int var5 = 0; var5 < var6; var5++) {
            	 Class3_Sub9.regionXteaKeys[var5] = new int[] { 14881828, -6662814, 58238456, 146761213 };
            }
            Class3_Sub28_Sub14.aByteArrayArray3669 = new byte[var6][];
            Class3_Sub24_Sub3.anIntArray3494 = new int[var6];
            Class164_Sub2.aByteArrayArray3027 = new byte[var6][];
            Class3_Sub13_Sub24.npcSpawnCacheIndices = new int[var6];
            Class3_Sub13_Sub15.anIntArray3181 = new int[var6];
            Class101.anIntArray1426 = new int[var6];
            byte var5 = 8;
            Class3_Sub13_Sub4.aByteArrayArray3057 = new byte[var6][];
            int var11 = 0;

            int var7;
            for(var7 = (-6 + var1) / 8; ~var7 >= ~((6 + var1) / 8); ++var7) {
               for(int var8 = (-6 + var2) / 8; ~((var2 + 6) / 8) <= ~var8; ++var8) {
                  int var9 = (var7 << 8) - -var8;
                  Class3_Sub24_Sub3.anIntArray3494[var11] = var9;
                  Client.anIntArray2200[var11] = aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class3_Sub30_Sub1.aClass94_3807, Class72.method1298((byte)9, var7), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var8)}, (byte)-128), (byte)-30);
                  Class101.anIntArray1426[var11] = aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class161.aClass94_2029, Class72.method1298((byte)9, var7), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var8)}, (byte)-73), (byte)-30);
                  Class3_Sub13_Sub24.npcSpawnCacheIndices[var11] = aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Canvas_Sub1.aClass94_26, Class72.method1298((byte)9, var7), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var8)}, (byte)-82), (byte)-30);
                  Class3_Sub13_Sub15.anIntArray3181[var11] = aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class95.aClass94_1333, Class72.method1298((byte)9, var7), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var8)}, (byte)-104), (byte)-30);
                  Class3_Sub28_Sub5.anIntArray3587[var11] = aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class167.aClass94_2084, Class72.method1298((byte)9, var7), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var8)}, (byte)-114), (byte)-30);
                  if(~Class3_Sub13_Sub24.npcSpawnCacheIndices[var11] == 0) {
                     Client.anIntArray2200[var11] = -1;
                     Class101.anIntArray1426[var11] = -1;
                     Class3_Sub13_Sub15.anIntArray3181[var11] = -1;
                     Class3_Sub28_Sub5.anIntArray3587[var11] = -1;
                  }

                  ++var11;
               }
            }

            for(var7 = var11; ~var7 > ~Class3_Sub13_Sub24.npcSpawnCacheIndices.length; ++var7) {
               Class3_Sub13_Sub24.npcSpawnCacheIndices[var7] = -1;
               Client.anIntArray2200[var7] = -1;
               Class101.anIntArray1426[var7] = -1;
               Class3_Sub13_Sub15.anIntArray3181[var7] = -1;
               Class3_Sub28_Sub5.anIntArray3587[var7] = -1;
            }

            Class73.method1301(var3, var2, var1, var5, true, var4, true);
         }
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "ca.F(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var3 = 69 / ((var2 - 30) / 36);
         int[] var4 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var5 = this.method152(0, var1, 32755);

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               int var7 = var5[var6];
               var4[var6] = ~this.anInt3073 >= ~var7 && ~var7 >= ~this.anInt3074?4096:0;
            }
         }

         return var4;
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ca.D(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method196(boolean var0) {
      try {
         Class41.aClass93_684.method1523((byte)-117);
         Class163_Sub1.aClass93_2984.method1523((byte)-111);
         if(var0) {
            anIntArray3083 = (int[])null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ca.B(" + var0 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(!var3) {
            method196(true);
         }

         if(~var1 != -1) {
            if(1 == var1) {
               this.anInt3074 = var2.getShort(1);
            }
         } else {
            this.anInt3073 = var2.getShort(1);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ca.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public Class3_Sub13_Sub6() {
      super(1, true);
   }

   public static void method197(int var0) {
      try {
         aClass94_3079 = null;
         anIntArray3076 = null;
         aClass61_3075 = null;
         anIntArray3082 = null;
         aClass94_3080 = null;
         if(var0 == 1) {
            anIntArray3083 = null;
            aClass153_3077 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ca.O(" + var0 + ')');
      }
   }

   static final void method198(boolean var0, int var1) {
      try {
         if(var1 != -32624) {
            method196(true);
         }

         int var3 = Class164_Sub2.aByteArrayArray3027.length;
         byte[][] var2;
         if(HDToolKit.highDetail && var0) {
            var2 = Class3_Sub13_Sub4.aByteArrayArray3057;
         } else {
            var2 = Class3_Sub22.aByteArrayArray2521;
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            byte[] var5 = var2[var4];
            if(var5 != null) {
               int var6 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var4] >> 8);
               int var7 = (Class3_Sub24_Sub3.anIntArray3494[var4] & 255) * 64 + -Class82.anInt1152;
               Class58.method1194(var1 + 16239);
               Class3_Sub15.method374(var6, var0, var5, var7, 0, Class86.aClass91Array1182);
            }
         }

      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ca.E(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method199(int var0, int var1, int var2, int var3) {
      try {
         if(var3 != -799) {
            aBoolean3078 = true;
         }

         if(-1 != ~CS2Script.anInt2453 && var0 != 0 && ~Class113.anInt1552 > -51 && 0 != ~var1) {
            Class3_Sub25.anIntArray2550[Class113.anInt1552] = var1;
            Class166.anIntArray2068[Class113.anInt1552] = var0;
            RSString.anIntArray2157[Class113.anInt1552] = var2;
            Class102.aClass135Array2131[Class113.anInt1552] = null;
            anIntArray3083[Class113.anInt1552] = 0;
            ++Class113.anInt1552;
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ca.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

}

package org.runite.jagex;

final class Class3_Sub13_Sub35 extends Class3_Sub13 {

   static RSString aClass94_3418 = RSString.createRSString("(U5");
   static int anInt3419 = 0;
   static int anInt3420;
   static Class131 aClass131_3421;


   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var4 = this.method152(0, var1, 32755);

            for(int var5 = 0; var5 < Class113.anInt1559; ++var5) {
               var3[var5] = 4096 - var4[var5];
            }
         }

         int var7 = 59 % ((30 - var2) / 36);
         return var3;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "tb.D(" + var1 + ',' + var2 + ')');
      }
   }

   public Class3_Sub13_Sub35() {
      super(1, false);
   }

   static final int method335(int var0) {
      try {
         if(var0 != 16859) {
            aClass131_3421 = (Class131)null;
         }

         return Class101.aBoolean1419 && ObjectDefinition.aBooleanArray1490[81] && 2 < Class3_Sub13_Sub34.anInt3415?Class114.anIntArray1578[-2 + Class3_Sub13_Sub34.anInt3415]:Class114.anIntArray1578[Class3_Sub13_Sub34.anInt3415 - 1];
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "tb.C(" + var0 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(var3) {
            if(var1 == 0) {
               this.aBoolean2375 = -2 == ~var2.getByte((byte)-80);
            }

         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "tb.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            return (int[][])((int[][])null);
         } else {
            int[][] var3 = this.aClass97_2376.method1594((byte)-128, var2);
            if(this.aClass97_2376.aBoolean1379) {
               int[][] var4 = this.method162(var2, 0, (byte)-51);
               int[] var7 = var4[2];
               int[] var5 = var4[0];
               int[] var6 = var4[1];
               int[] var8 = var3[0];
               int[] var9 = var3[1];
               int[] var10 = var3[2];

               for(int var11 = 0; ~Class113.anInt1559 < ~var11; ++var11) {
                  var8[var11] = -var5[var11] + 4096;
                  var9[var11] = 4096 - var6[var11];
                  var10[var11] = 4096 - var7[var11];
               }
            }

            return var3;
         }
      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "tb.T(" + var1 + ',' + var2 + ')');
      }
   }

   static final Class3_Sub28_Sub1 getQuickChatMessage(int fileId, byte var1) {
      try {
         Class3_Sub28_Sub1 var2 = (Class3_Sub28_Sub1)Class3_Sub13_Sub11.aClass47_3137.method1092((long)fileId, 1400);
         if(null != var2) {
            return var2;
         } else {
            byte[] var3;
            if(fileId < '\u8000') {
               var3 = Class47.quickChatMessages.getFile(0, (byte)-122, fileId);
            } else {
               var3 = NodeList.aClass153_332.getFile(0, (byte)-122, fileId & 32767);
            }

            var2 = new Class3_Sub28_Sub1();
            if(null != var3) {
               var2.method530(new RSByteBuffer(var3), (byte)116);
            }

            if(var1 != -54) {
               method337(19);
            }

            if(-32769 >= ~fileId) {
               var2.method525(-85);
            }

            Class3_Sub13_Sub11.aClass47_3137.method1097(var2, (long)fileId, (byte)40);
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "tb.B(" + fileId + ',' + var1 + ')');
      }
   }

   public static void method337(int var0) {
      try {
         aClass94_3418 = null;
         if(var0 != 2) {
            anInt3419 = -53;
         }

         aClass131_3421 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "tb.E(" + var0 + ')');
      }
   }

}

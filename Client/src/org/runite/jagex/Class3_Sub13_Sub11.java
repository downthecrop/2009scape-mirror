package org.runite.jagex;

final class Class3_Sub13_Sub11 extends Class3_Sub13 {

   private int anInt3129;
   static Class93 aClass93_3130 = new Class93(4);
   static RSString aClass94_3131 = RSString.createRSString(" ");
   static int anInt3132;
   static RSString aClass94_3133 = RSString.createRSString(")2");
   private int anInt3134;
   private int anInt3135;
   static int anInt3136;
   static Class47 aClass47_3137 = new Class47(64);
   static RSString aClass94_3138 = RSString.createRSString("b12_full");
   static int[] anIntArray3139 = new int[14];
   static RSString aClass94_3140 = RSString.createRSString("overlay2");


   private Class3_Sub13_Sub11(int var1) {
      super(0, false);

      try {
         this.method218((byte)75, var1);
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "fm.<init>(" + var1 + ')');
      }
   }

   public static void method217(int var0) {
      try {
         aClass94_3138 = null;
         aClass94_3133 = null;
         aClass94_3140 = null;
         aClass93_3130 = null;
         anIntArray3139 = null;
         aClass94_3131 = null;
         if(var0 != 1) {
            aClass94_3138 = (RSString)null;
         }

         aClass47_3137 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "fm.B(" + var0 + ')');
      }
   }

   public Class3_Sub13_Sub11() {
      this(0);
   }

   private final void method218(byte var1, int var2) {
      try {
         this.anInt3134 = 4080 & var2 >> 4;
         this.anInt3135 = var2 << 4 & 4080;
         if(var1 == 75) {
            this.anInt3129 = (var2 & 16711680) >> 12;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "fm.Q(" + var1 + ',' + var2 + ')');
      }
   }

   final int[][] method166(int var1, int var2) {
      try {
         if(var1 != -1) {
            method222(-87, 26, 75, -56, 22, -68);
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)-123, var2);
         if(this.aClass97_2376.aBoolean1379) {
            int[] var4 = var3[0];
            int[] var5 = var3[1];
            int[] var6 = var3[2];

            for(int var7 = 0; ~var7 > ~Class113.anInt1559; ++var7) {
               var4[var7] = this.anInt3129;
               var5[var7] = this.anInt3134;
               var6[var7] = this.anInt3135;
            }
         }

         return var3;
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "fm.T(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method219(boolean var0, int var1) {
      try {
         if(var0) {
            if(-1 != Class3_Sub28_Sub12.anInt3655) {
               Class60.method1208((byte)-128, Class3_Sub28_Sub12.anInt3655);
            }

            for(Class3_Sub31 var2 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1776(var1 + -2908); null != var2; var2 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1778(-122)) {
               Class3_Sub13_Sub18.method254(true, var2, false);
            }

            Class3_Sub28_Sub12.anInt3655 = -1;
            Class3_Sub13_Sub17.aClass130_3208 = new Class130(8);
            Class3_Sub7.method122(var1 + -2918);
            Class3_Sub28_Sub12.anInt3655 = Class3_Sub22.anInt2529;
            Class124.method1746(false, (byte)-36);
            Class47.method1093(false);
            Class3_Sub13_Sub12.method226(Class3_Sub28_Sub12.anInt3655, var1 ^ 2960);
         }

         Class3_Sub28_Sub5.anInt3590 = -1;
         Class3_Sub13_Sub13.method229(Class161.anInt2027, 20827);
         Class102.player = new Player();
         Class102.player.anInt2829 = 3000;
         Class102.player.anInt2819 = var1;
         if(HDToolKit.highDetail) {
            if(-3 == ~Class133.anInt1753) {
               NPC.anInt3995 = Canvas_Sub2.anInt30 << 7;
               Class77.anInt1111 = Class146.anInt1904 << 7;
            } else {
               Class3_Sub28_Sub6.d(var1 ^ '\uf447');
            }

            Class3_Sub13_Sub14.method236((byte)64);
            Class3_Sub13_Sub6.method195(var1 + 17479);
            Class117.method1719(28, 5);
         } else {
            Class84.method1418(-110, Class140_Sub6.spritesCacheIndex);
            Class117.method1719(10, 5);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "fm.E(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method220(boolean var0, int var1, int var2) {
      try {
         Class46.anInt741 = Class115.aClass86ArrayArray1581[var2][var1].anInt1185;
         Class3_Sub13_Sub22.anInt3274 = Class115.aClass86ArrayArray1581[var2][var1].anInt1181;
         if(!var0) {
            anIntArray3139 = (int[])null;
         }

         Class86.anInt1191 = Class115.aClass86ArrayArray1581[var2][var1].anInt1178;
         Class92.method1509((float)Class46.anInt741, (float)Class3_Sub13_Sub22.anInt3274, (float)Class86.anInt1191);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "fm.C(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final void method221(int var0, RSString var1, RSString var2, RSString var3, int var4) {
      try {
         Class3_Sub28_Sub12.sendGameMessage(var0, var4, var1, var3, (byte)50, var2);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "fm.F(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ')');
      }
   }

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(!var3) {
            method221(-64, (RSString)null, (RSString)null, (RSString)null, 34);
         }

         if(-1 == ~var1) {
            this.method218((byte)75, var2.getTriByte((byte)82));
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "fm.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final boolean method222(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6;
      int var7;
      if(var1 == var2 && var3 == var4) {
         if(!Class8.method846(var0, var1, var3)) {
            return false;
         } else {
            var6 = var1 << 7;
            var7 = var3 << 7;
            return Class3_Sub13_Sub37.method349(var6 + 1, Class44.anIntArrayArrayArray723[var0][var1][var3] + var5, var7 + 1) && Class3_Sub13_Sub37.method349(var6 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var3] + var5, var7 + 1) && Class3_Sub13_Sub37.method349(var6 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var3 + 1] + var5, var7 + 128 - 1) && Class3_Sub13_Sub37.method349(var6 + 1, Class44.anIntArrayArrayArray723[var0][var1][var3 + 1] + var5, var7 + 128 - 1);
         }
      } else {
         for(var6 = var1; var6 <= var2; ++var6) {
            for(var7 = var3; var7 <= var4; ++var7) {
               if(Class81.anIntArrayArrayArray1142[var0][var6][var7] == -Class3_Sub28_Sub1.anInt3539) {
                  return false;
               }
            }
         }

         var6 = (var1 << 7) + 1;
         var7 = (var3 << 7) + 2;
         int var8 = Class44.anIntArrayArrayArray723[var0][var1][var3] + var5;
         if(!Class3_Sub13_Sub37.method349(var6, var8, var7)) {
            return false;
         } else {
            int var9 = (var2 << 7) - 1;
            if(!Class3_Sub13_Sub37.method349(var9, var8, var7)) {
               return false;
            } else {
               int var10 = (var4 << 7) - 1;
               if(!Class3_Sub13_Sub37.method349(var6, var8, var10)) {
                  return false;
               } else if(!Class3_Sub13_Sub37.method349(var9, var8, var10)) {
                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

}

package org.runite.jagex;

final class Class86 {

   int anInt1175;
   private static RSString aClass94_1176 = RSString.createRSString("Loading wordpack )2 ");
   int anInt1177;
   int anInt1178;
   static RSString aClass94_1179 = RSString.createRSString("Veuillez patienter)3)3)3");
  
   int anInt1181;
   static Class91[] aClass91Array1182 = new Class91[4];
   static RSString aClass94_1183 = aClass94_1176;
   int anInt1184;
   int anInt1185;
   static Class41 aClass41_1186;
   float aFloat1187;
   static RSString aClass94_1188 = RSString.createRSString("::rebuild");
   float aFloat1189;
   float aFloat1190;
   static int anInt1191;
   private static RSString aClass94_1192 = RSString.createRSString("Examine");
   static Class3_Sub24_Sub4 aClass3_Sub24_Sub4_1193;
   static Class130 aClass130_1194;
   static int anInt1195;
 static RSString aClass94_1180 = aClass94_1192;

   static final void method1427(boolean var0, int var1) {
      try {
         if(-1 == var1 && !Class83.aBoolean1158) {
            GameObject.method1870(false);
         } else if(var1 != -1 && (Class129.anInt1691 != var1 || !Class79.method1391(-1)) && Class9.anInt120 != 0 && !Class83.aBoolean1158) {
            Class151.method2099(true, var1, 0, Class75_Sub2.aClass153_2645, false, Class9.anInt120, 2);
         }

         if(!var0) {
            aClass94_1192 = (RSString)null;
         }

         Class129.anInt1691 = var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "li.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method1428(int var0, int var1, int var2) {
      try {
         if(var1 < 21) {
            method1430(34, -13);
         }

         Class163_Sub1.anIntArray2985[var0] = var2;
         Class3_Sub7 var3 = (Class3_Sub7)Class3_Sub28_Sub15.aClass130_3679.method1780((long)var0, 0);
         if(var3 == null) {
            var3 = new Class3_Sub7(Class5.method830((byte)-55) - -500L);
            Class3_Sub28_Sub15.aClass130_3679.method1779(1, var3, (long)var0);
         } else {
            var3.aLong2295 = 500L + Class5.method830((byte)-55);
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "li.D(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   public static void method1429(byte var0) {
      try {
         aClass3_Sub24_Sub4_1193 = null;
         aClass94_1188 = null;
         aClass91Array1182 = null;
         aClass94_1192 = null;
         aClass94_1183 = null;
         aClass94_1179 = null;
         aClass94_1176 = null;
         aClass41_1186 = null;
         aClass130_1194 = null;
         aClass94_1180 = null;
         if(var0 != 53) {
            aClass3_Sub24_Sub4_1193 = (Class3_Sub24_Sub4)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "li.C(" + var0 + ')');
      }
   }

   static final Class3_Sub28_Sub17_Sub1 method1430(int var0, int var1) {
      try {
         if(var0 != -28922) {
            return (Class3_Sub28_Sub17_Sub1)null;
         } else {
            Class3_Sub28_Sub17_Sub1 var2 = (Class3_Sub28_Sub17_Sub1)Class80.aClass93_1135.get((long)var1, (byte)121);
            if(var2 != null) {
               return var2;
            } else {
               byte[] var3 = CacheIndex.aClass153_1948.getFile(var1, (byte)-122, 0);
               var2 = new Class3_Sub28_Sub17_Sub1(var3);
               var2.method697(Class3_Sub13_Sub22.aClass109Array3270, (int[])null);
               Class80.aClass93_1135.put((byte)-96, var2, (long)var1);
               return var2;
            }
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "li.A(" + var0 + ',' + var1 + ')');
      }
   }

   public Class86() {
      try {
         this.anInt1177 = Class92.anInt1322;
         this.aFloat1189 = 1.2F;
         this.anInt1178 = -50;
         this.aFloat1187 = 1.1523438F;
         this.anInt1175 = Class92.anInt1316;
         this.anInt1181 = -60;
         this.aFloat1190 = 0.69921875F;
         this.anInt1184 = 0;
         this.anInt1185 = -50;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "li.<init>()");
      }
   }

   Class86(RSByteBuffer var1) {
      try {
         int var2 = var1.getByte((byte)-92);
         if(~(var2 & 1) == -1) {
            this.anInt1177 = Class92.anInt1322;
         } else {
            this.anInt1177 = var1.getInt();
         }

         if(~(2 & var2) == -1) {
            this.aFloat1187 = 1.1523438F;
         } else {
            this.aFloat1187 = (float)var1.getShort(1) / 256.0F;
         }

         if((var2 & 4) == 0) {
            this.aFloat1190 = 0.69921875F;
         } else {
            this.aFloat1190 = (float)var1.getShort(1) / 256.0F;
         }

         if(~(var2 & 8) != -1) {
            this.aFloat1189 = (float)var1.getShort(1) / 256.0F;
         } else {
            this.aFloat1189 = 1.2F;
         }

         if(-1 == ~(16 & var2)) {
            this.anInt1178 = -50;
            this.anInt1185 = -50;
            this.anInt1181 = -60;
         } else {
            this.anInt1185 = var1.getShort((byte)53);
            this.anInt1181 = var1.getShort((byte)15);
            this.anInt1178 = var1.getShort((byte)50);
         }

         if((32 & var2) == 0) {
            this.anInt1175 = Class92.anInt1316;
         } else {
            this.anInt1175 = var1.getInt();
         }

         if(-1 == ~(64 & var2)) {
            this.anInt1184 = 0;
         } else {
            this.anInt1184 = var1.getShort(1);
         }

      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "li.<init>(" + (var1 != null?"{...}":"null") + ')');
      }
   }

}

package org.runite.jagex;

abstract class Class3_Sub28_Sub10 extends Node {

   static int anInt3625 = 3;
   private static RSString aClass94_3626 = RSString.createRSString("Created gameworld");
   static int anInt3627;
   boolean aBoolean3628;
   static RSString aClass94_3629 = aClass94_3626;
   static int anInt3630;
   static int anInt3631;
   volatile boolean aBoolean3632 = true;
   static RSString aClass94_3633 = RSString.createRSString("Textures charg-Bes");
   static RSString aClass94_3634 = RSString.createRSString("Liste des serveurs charg-Be");
   boolean aBoolean3635;


   abstract int method586(boolean var1);

   abstract byte[] method587(boolean var1);

   public static void method588(byte var0) {
      try {
         aClass94_3626 = null;
         aClass94_3634 = null;
         if(var0 != 120) {
            aClass94_3633 = (RSString)null;
         }

         aClass94_3633 = null;
         aClass94_3629 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "il.C(" + var0 + ')');
      }
   }

   static final void method589(int var0, int var1, int var2) {
      Class3_Sub13_Sub21.aBoolean3261 = true;
      Class91.anInt1302 = var0;
      Class49.anInt819 = var1;
      Class3_Sub13_Sub23_Sub1.anInt4039 = var2;
      Class27.anInt515 = -1;
      Class66.anInt999 = -1;
   }

   static final boolean method590(byte var0, int var1, int var2) {
      try {
         if(11 == var2) {
            var2 = 10;
         }

         int var3 = -59 % ((var0 - 26) / 55);
         if(-6 >= ~var2 && var2 <= 8) {
            var2 = 4;
         }

         ObjectDefinition var4 = Class162.getObjectDefinition(4, var1);
         return var4.method1684(115, var2);
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "il.D(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

}

package org.runite.jagex;

abstract class Class3_Sub28_Sub16 extends Node {

   int anInt3696;
   int anInt3697;
   int anInt3698;
   static int anInt3699 = 0;
   static RSString aClass94_3700 = RSString.createRSString("::cardmem");
   int anInt3701;
   static RSString aClass94_3702 = RSString.createRSString(" )2> <col=ffff00>");
   static RSString aClass94_3703 = RSString.createRSString(" )2> ");
   static int anInt3704;
   static RSString aClass94_3705 = RSString.createRSString("W-=hlen Sie eine Option");
   int anInt3706;
   int anInt3707;
   static RSInterface aClass11_3708 = null;


   public static void method634(byte var0) {
      try {
         aClass11_3708 = null;
         aClass94_3702 = null;
         aClass94_3705 = null;
         if(var0 == 108) {
            aClass94_3703 = null;
            aClass94_3700 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "qf.R(" + var0 + ')');
      }
   }

   abstract void method635(int var1, int var2);

   abstract void method636(int var1, int var2, int var3, int var4, int var5, int var6);

   abstract void method637(int var1, int var2, int var3);

   static final RSInterface method638(byte var0, int var1, int var2) {
      try {
         RSInterface var3 = Class7.getRSInterface((byte)109, var1);
         return 0 == ~var2?var3:(var0 != -19?(RSInterface)null:(var3 != null && var3.aClass11Array262 != null && ~var3.aClass11Array262.length < ~var2?var3.aClass11Array262[var2]:null));
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "qf.P(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   abstract void method639(int var1, int var2, int var3, int var4);

   final void method640(int var1, int var2, int var3, int var4, int var5) {
      try {
         if(var5 == -1470985020) {
            int var6 = this.anInt3697 << 3;
            int var7 = this.anInt3706 << 3;
            var4 = (var4 << 4) + (var6 & 15);
            var1 = (var1 << 4) + (15 & var7);
            this.method636(var6, var7, var4, var1, var2, var3);
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "qf.F(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   abstract void method641(int var1, int var2);

   abstract void method642(int var1, int var2, int var3, int var4, int var5);

   abstract void method643(int var1, int var2);

}

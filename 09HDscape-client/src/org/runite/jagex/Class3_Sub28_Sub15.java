package org.runite.jagex;

final class Class3_Sub28_Sub15 extends Node {

   int anInt3678;
   static Class130 aClass130_3679 = new Class130(16);
   int anInt3680;
   static int anInt3681;
   int anInt3682;
   int[] anIntArray3683;
   static int anInt3684 = 10;
   Class130[] aClass130Array3685;
   RSString aClass94_3686;
   int anInt3687;
   RSString[] aClass94Array3688;
   static int anInt3689 = 0;
   int[] anIntArray3690;
   
   private static RSString aClass94_3692 = RSString.createRSString("Members object");
   static int[] anIntArray3693 = new int[1000];
   static Class3_Sub19[] aClass3_Sub19Array3694;
   static int anInt3695;
static RSString aClass94_3691 = aClass94_3692;

   static final Class100 method629(boolean var0, int var1) {
      try {
         Class100 var2 = (Class100)Class44.aClass93_725.get((long)var1, (byte)121);
         if(var2 == null) {
            if(!var0) {
               return (Class100)null;
            } else {
               byte[] var3 = Class3_Sub23.aClass153_2536.getFile(1, (byte)-122, var1);
               var2 = new Class100();
               if(null != var3) {
                  var2.method1601(var1, new RSByteBuffer(var3), 255);
               }

               Class44.aClass93_725.put((byte)-104, var2, (long)var1);
               return var2;
            }
         } else {
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "qc.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final int method630(byte var0, int var1) {
      try {
         if(var0 != -34) {
            aClass94_3692 = (RSString)null;
         }

         return 127 & var1 >> 11;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "qc.A(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method631(boolean var0, CacheIndex var1) {
      try {
         if(!var0) {
            Class3_Sub28_Sub5.aClass153_3580 = var1;
            Class54.anInt869 = Class3_Sub28_Sub5.aClass153_3580.getFileAmount(4, (byte)112);
         }
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "qc.D(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   public static void method632(int var0) {
      try {
         aClass94_3692 = null;
         aClass94_3691 = null;
         aClass3_Sub19Array3694 = null;
         aClass130_3679 = null;
         anIntArray3693 = null;
         if(var0 != -30497) {
            aClass3_Sub19Array3694 = (Class3_Sub19[])null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "qc.E(" + var0 + ')');
      }
   }

   static int method633(int var0, int var1) {
      try {
         return var0 & var1;
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "qc.C(" + var0 + ',' + var1 + ')');
      }
   }

}

package org.runite.jagex;
import java.awt.Canvas;

public final class Class3_Sub28_Sub12 extends Node {

   static RSString aClass94_3646 = RSString.createRSString("D-Bmarrage de la librairie 3D");
   int anInt3647 = 0;
   public static Canvas aCanvas3648;
   static RSString aClass94_3649 = RSString.createRSString("Fichiers config charg-Bs");
   static RSString aClass94_3650 = RSString.createRSString("Fermer");
   static RSString aClass94_3651 = RSString.createRSString("::mm");
   static int anInt3652;
   static RSString aClass94_3653 = RSString.createRSString("Shift)2click disabled)3");
   static short[][] aShortArrayArray3654 = new short[][]{{(short)6798, (short)107, (short)10283, (short)16, (short)4797, (short)7744, (short)5799, (short)4634, (short)-31839, (short)22433, (short)2983, (short)-11343, (short)8, (short)5281, (short)10438, (short)3650, (short)-27322, (short)-21845, (short)200, (short)571, (short)908, (short)21830, (short)28946, (short)-15701, (short)-14010}, {(short)8741, (short)12, (short)-1506, (short)-22374, (short)7735, (short)8404, (short)1701, (short)-27106, (short)24094, (short)10153, (short)-8915, (short)4783, (short)1341, (short)16578, (short)-30533, (short)25239, (short)8, (short)5281, (short)10438, (short)3650, (short)-27322, (short)-21845, (short)200, (short)571, (short)908, (short)21830, (short)28946, (short)-15701, (short)-14010}, {(short)25238, (short)8742, (short)12, (short)-1506, (short)-22374, (short)7735, (short)8404, (short)1701, (short)-27106, (short)24094, (short)10153, (short)-8915, (short)4783, (short)1341, (short)16578, (short)-30533, (short)8, (short)5281, (short)10438, (short)3650, (short)-27322, (short)-21845, (short)200, (short)571, (short)908, (short)21830, (short)28946, (short)-15701, (short)-14010}, {(short)4626, (short)11146, (short)6439, (short)12, (short)4758, (short)10270}, {(short)4550, (short)4537, (short)5681, (short)5673, (short)5790, (short)6806, (short)8076, (short)4574}};
   static int anInt3655 = -1;
   static RSString aClass94_3656 = RSString.createRSString("Impossible de trouver ");


   static final boolean method609(RSInterface var0, int var1) {
      try {
         if(null != var0.anIntArray275) {
            int var2 = 0;
            if(var1 <= 20) {
               aClass94_3651 = (RSString)null;
            }

            for(; ~var2 > ~var0.anIntArray275.length; ++var2) {
               int var3 = Class164_Sub2.method2247((byte)119, var2, var0);
               int var4 = var0.anIntArray307[var2];
               if(~var0.anIntArray275[var2] != -3) {
                  if(~var0.anIntArray275[var2] != -4) {
                     if(4 == var0.anIntArray275[var2]) {
                        if(~var3 == ~var4) {
                           return false;
                        }
                     } else if(~var4 != ~var3) {
                        return false;
                     }
                  } else if(var3 <= var4) {
                     return false;
                  }
               } else if(~var4 >= ~var3) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "md.C(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   final void method610(RSByteBuffer var1, int var2) {
      try {
         if(var2 != 0) {
            aClass94_3649 = (RSString)null;
         }

         while(true) {
            int var3 = var1.getByte((byte)-97);
            if(0 == var3) {
               return;
            }

            this.method614(var1, var3, false);
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "md.E(" + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }
   
   /**
    * Sends a default chatbox game message.
    */
   static final void sendMessage(String str){
	   sendGameMessage(-1, 11, RSString.createRSString(str), RSString.createRSString(""), (byte) 50, RSString.createRSString(""));
   }

   static final void sendGameMessage(int var0, int var1, RSString message, RSString var3, byte var4, RSString var5) {
      try {
         if(var4 != 50) {
            aShortArrayArray3654 = (short[][])((short[][])null);
         }

         for(int var6 = 99; var6 > 0; --var6) {
            Class3_Sub13_Sub6.anIntArray3082[var6] = Class3_Sub13_Sub6.anIntArray3082[var6 - 1];
            Class3_Sub13_Sub19.aClass94Array3226[var6] = Class3_Sub13_Sub19.aClass94Array3226[var6 - 1];
            Class3_Sub29.aClass94Array2580[var6] = Class3_Sub29.aClass94Array2580[-1 + var6];
            Class163_Sub3.aClass94Array3003[var6] = Class163_Sub3.aClass94Array3003[var6 + -1];
            GameObject.anIntArray1835[var6] = GameObject.anIntArray1835[var6 - 1];
         }

         ++Class3_Sub13_Sub9.anInt3114;
         Class3_Sub13_Sub6.anIntArray3082[0] = var1;
         Class3_Sub13_Sub19.aClass94Array3226[0] = var5;
         Class24.anInt472 = Class3_Sub13_Sub17.anInt3213;
         GameObject.anIntArray1835[0] = var0;
         Class3_Sub29.aClass94Array2580[0] = message;
         Class163_Sub3.aClass94Array3003[0] = var3;
      } catch (RuntimeException var7) {
         throw Class44.method1067(var7, "md.D(" + var0 + ',' + var1 + ',' + (message != null?"{...}":"null") + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + (var5 != null?"{...}":"null") + ')');
      }
   }

   static final RSString method612(long var0, byte var2) {
      try {
         return var2 <= 85?(RSString)null:Class3_Sub13_Sub8.method207(10, false, 116, var0);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "md.F(" + var0 + ',' + var2 + ')');
      }
   }

   public static void method613(int var0) {
      try {
         aClass94_3646 = null;
         aClass94_3656 = null;
         aCanvas3648 = null;
         aClass94_3649 = null;
         aClass94_3653 = null;
         aShortArrayArray3654 = (short[][])null;
         if(var0 > 22) {
            aClass94_3651 = null;
            aClass94_3650 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "md.B(" + var0 + ')');
      }
   }

   private final void method614(RSByteBuffer var1, int var2, boolean var3) {
      try {
         if(var3) {
            this.method610((RSByteBuffer)null, -89);
         }

         if(~var2 == -3) {
            this.anInt3647 = var1.getShort(1);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "md.A(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

}

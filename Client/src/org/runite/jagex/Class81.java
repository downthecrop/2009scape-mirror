package org.runite.jagex;
import java.awt.event.ActionEvent;
import java.io.IOException;

final class Class81 {

   static NodeList aClass13_1139 = new NodeList();
   static RSString aClass94_1140 = RSString.createRSString(" steht bereits auf Ihrer Ignorieren)2Liste(Q");
   static int anInt1141;
   static int[][][] anIntArrayArrayArray1142;
   static RSString aClass94_1143 = RSString.createRSString("Chargement du module texte )2 ");


   static final void putRandomDataFile(RSByteBuffer var0, boolean var1) {
      try {
         if(!var1) {
            anIntArrayArrayArray1142 = (int[][][])((int[][][])null);
         }

         byte[] var2 = new byte[24];
         if(null != Class69.aClass30_1039) {
            try {
               Class69.aClass30_1039.method984(-41, 0L);
               Class69.aClass30_1039.method982(false, var2);

               int var3;
               for(var3 = 0; -25 < ~var3 && ~var2[var3] == -1; ++var3) {
                  ;
               }

               if(-25 >= ~var3) {
                  throw new IOException();
               }
            } catch (Exception var5) {
               for(int var4 = 0; 24 > var4; ++var4) {
                  var2[var4] = -1;
               }
            }
         }

         var0.putBytes(var2, 0, 24, 88);
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "la.G(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
      }
   }

   static final int method1398(int var0, Player var1) {
      try {
         if(var0 != 0) {
            putRandomDataFile((RSByteBuffer)null, false);
         }

         int var2 = var1.anInt3963;
         RenderAnimationDefinition var3 = var1.method1965(false);
         if(~var3.anInt368 == ~var1.anInt2764) {
            var2 = var1.anInt3952;
         } else if(var3.anInt393 != var1.anInt2764 && var1.anInt2764 != var3.anInt386 && var1.anInt2764 != var3.anInt375 && var3.anInt373 != var1.anInt2764) {
            if(~var1.anInt2764 == ~var3.anInt398 || ~var1.anInt2764 == ~var3.anInt372 || var1.anInt2764 == var3.anInt379 || ~var1.anInt2764 == ~var3.anInt406) {
               var2 = var1.anInt3966;
            }
         } else {
            var2 = var1.anInt3973;
         }

         return var2;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "la.A(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
      }
   }

   static final void method1399(int var0, long var1) {
      try {
         if(var1 != 0L) {
            if(Class3_Sub28_Sub5.anInt3591 < 100) {
               int var3 = -103 / ((var0 - -20) / 41);
               RSString var4 = Class41.method1052(-29664, var1).method1545((byte)-50);

               int var5;
               for(var5 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var5; ++var5) {
                  if(Class114.aLongArray1574[var5] == var1) {
                     Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, RenderAnimationDefinition.method903(new RSString[]{var4, Class3_Sub13_Sub25.aClass94_3311}, (byte)-108), -1);
                     return;
                  }
               }

               for(var5 = 0; Class8.anInt104 > var5; ++var5) {
                  if(~Class50.aLongArray826[var5] == ~var1) {
                     Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, RenderAnimationDefinition.method903(new RSString[]{Class38.aClass94_666, var4, Class3_Sub28_Sub10_Sub2.aClass94_4071}, (byte)-65), -1);
                     return;
                  }
               }

               if(var4.method1528((byte)-42, Class102.player.displayName)) {
                  Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, Class95.aClass94_1335, -1);
               } else {
                  ++Class3_Sub13_Sub22.anInt3267;
                  Class114.aLongArray1574[Class3_Sub28_Sub5.anInt3591] = var1;
                  Class3_Sub13_Sub27.aClass94Array3341[Class3_Sub28_Sub5.anInt3591++] = Class41.method1052(-29664, var1);
                  Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(34);
                  Class3_Sub13_Sub1.outgoingBuffer.putLong(var1, -2037491440);
               }
            } else {
               Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, Class144.aClass94_1884, -1);
            }
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "la.B(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method1400(Signlink var0, Object var1, int var2) {
      try {
         if(var2 >= -29) {
            anIntArrayArrayArray1142 = (int[][][])((int[][][])null);
         }

         if(null != var0.anEventQueue1199) {
            for(int var3 = 0; var3 < 50 && null != var0.anEventQueue1199.peekEvent(); ++var3) {
               Class3_Sub13_Sub34.method331(1L, 64);
            }

            if(var1 != null) {
               var0.anEventQueue1199.postEvent(new ActionEvent(var1, 1001, "dummy"));
            }

         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "la.E(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
      }
   }

   static final Class57 method1401(int var0, int var1) {
      try {
         Class57 var2 = (Class57)Class128.aClass93_1683.get((long)var1, (byte)121);
         if(var2 != null) {
            return var2;
         } else {
            if(var0 != 1001) {
               aClass94_1143 = (RSString)null;
            }

            byte[] var3 = Class46.aClass153_737.getFile(31, (byte)-122, var1);
            var2 = new Class57();
            if(var3 != null) {
               var2.method1190(2, new RSByteBuffer(var3), var1);
            }

            Class128.aClass93_1683.put((byte)-75, var2, (long)var1);
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "la.D(" + var0 + ',' + var1 + ')');
      }
   }

   public static void method1402(byte var0) {
      try {
         aClass13_1139 = null;
         anIntArrayArrayArray1142 = (int[][][])null;
         aClass94_1143 = null;
         aClass94_1140 = null;
         if(var0 <= 56) {
            aClass94_1140 = (RSString)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "la.F(" + var0 + ')');
      }
   }

   static final Class131 method1403(int var0, RSString var1, CacheIndex var2) {
      try {
         int var3 = var2.getArchiveForName(var1, (byte)-30);
         if(var3 == -1) {
            return new Class131(0);
         } else if(var0 > -38) {
            return (Class131)null;
         } else {
            int[] var4 = var2.getFileIds((byte)-128, var3);
            Class131 var5 = new Class131(var4.length);

            for(int var6 = 0; ~var6 > ~var5.anInt1720; ++var6) {
               RSByteBuffer var7 = new RSByteBuffer(var2.getFile(var3, (byte)-122, var4[var6]));
               var5.aClass94Array1721[var6] = var7.getString();
               var5.aByteArray1730[var6] = var7.getByte();
               var5.aShortArray1727[var6] = (short)var7.getShort(1);
               var5.aShortArray1718[var6] = (short)var7.getShort(1);
               var5.anIntArray1725[var6] = var7.getInt();
            }

            return var5;
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "la.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

}

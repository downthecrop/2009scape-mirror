package org.runite.jagex;

final class Class131 {

   static int anInt1716;
   private static RSString aClass94_1717 = RSString.createRSString("Prepared sound engine");
   short[] aShortArray1718;
   static int anInt1719 = -1;
   int anInt1720;
   RSString[] aClass94Array1721;
   
   static CacheIndex aClass153_1723;
   static RSString aClass94_1724 = RSString.createRSString(" )2>");
   int[] anIntArray1725;
   private static RSString aClass94_1726 = RSString.createRSString("Select");
   short[] aShortArray1727;
   static RSString aClass94_1728 = RSString.createRSString("Eingabeprozedur geladen)3");
   static int[] anIntArray1729 = new int[]{12543016, 15504954, 15914854, 16773818};
   byte[] aByteArray1730;
   static RSString aClass94_1731 = aClass94_1717;
static RSString aClass94_1722 = aClass94_1726;

   static final void addLocalPlayers(int var0) {
      try {
         while(true) {
            if(~GraphicDefinition.incomingBuffer.method815(Class130.incomingPacketLength, 32666) <= -12) {
               int index = GraphicDefinition.incomingBuffer.getBits((byte)-11, 11);
               if(index != 2047) {
                  boolean var2 = false;
                  if(null == Class3_Sub13_Sub22.players[index]) {
                     Class3_Sub13_Sub22.players[index] = new Player();
                     var2 = true;
                     if(null != Class65.aClass3_Sub30Array986[index]) {
                        Class3_Sub13_Sub22.players[index].parseAppearance(-54, Class65.aClass3_Sub30Array986[index]);
                     }
                  }

                  Class56.localPlayerIndexes[Class159.localPlayerCount++] = index;
                  Player var3 = Class3_Sub13_Sub22.players[index];
                  var3.anInt2838 = Class44.anInt719;
                  int var4 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 1);
                  if(~var4 == -2) {
                     Class21.maskUpdateIndexes[Class66.maskUpdateCount++] = index;
                  }

                  int var5 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 5);
                  int var6 = Class27.anIntArray510[GraphicDefinition.incomingBuffer.getBits((byte)-11, 3)];
                  if(var5 > 15) {
                     var5 -= 32;
                  }

                  if(var2) {
                     var3.anInt2806 = var3.anInt2785 = var6;
                  }

                  int var7 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 1);
                  int var8 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 5);
                  if(var8 > 15) {
                     var8 -= 32;
                  }

                  var3.method1981((byte)126, var5 + Class102.player.anIntArray2767[0], ~var7 == -2, Class102.player.anIntArray2755[0] + var8);
                  continue;
               }
            }

            if(var0 >= -46) {
               return;
            }

            GraphicDefinition.incomingBuffer.method818(false);
            return;
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "se.D(" + var0 + ')');
      }
   }

   final boolean method1787(int var1, byte var2) {
      try {
         if(var2 != -124) {
            method1793((RSString)null, (RSString)null, -17, (byte)94);
         }

         return (this.aByteArray1730[var1] & 8) != 0;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "se.G(" + var1 + ',' + var2 + ')');
      }
   }

   static final int method1788(int var0, int var1, int var2, int var3, boolean var4) {
      try {
         if(!var4) {
            return 127;
         } else {
            int var5 = 15 & var3;
            int var7 = -5 >= ~var5?(~var5 != -13 && -15 != ~var5?var1:var0):var2;
            int var6 = ~var5 > -9?var0:var2;
            return (-1 != ~(var5 & 1)?-var6:var6) - -(~(2 & var5) != -1?-var7:var7);
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "se.H(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   final boolean method1789(int var1, int var2) {
      try {
         if(var2 != 530) {
            this.method1794(-111, 26);
         }

         return ~(4 & this.aByteArray1730[var1]) != -1;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "se.A(" + var1 + ',' + var2 + ')');
      }
   }

   static final void method1790(int var0, int var1, int var2) {
      try {
         if(var2 < 90) {
            aClass94_1731 = (RSString)null;
         }

         Class3_Sub28_Sub6 var3 = Class3_Sub24_Sub3.method466(4, 5, var0);
         var3.g((byte)33);
         var3.anInt3598 = var1;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "se.B(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   final int method1791(int var1, int var2) {
      try {
         return var2 != 8?35:this.aByteArray1730[var1] & 3;
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "se.I(" + var1 + ',' + var2 + ')');
      }
   }

   public static void method1792(int var0) {
      try {
         anIntArray1729 = null;
         if(var0 == 0) {
            aClass153_1723 = null;
            aClass94_1726 = null;
            aClass94_1731 = null;
            aClass94_1717 = null;
            aClass94_1722 = null;
            aClass94_1728 = null;
            aClass94_1724 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "se.F(" + var0 + ')');
      }
   }

   static final void method1793(RSString var0, RSString var1, int var2, byte var3) {
      try {
         Class3_Sub28_Sub14.password = var1;
         Class7.anInt2161 = var2;
         Class3_Sub28_Sub14.username = var0;
         if(!Class3_Sub28_Sub14.username.method1528((byte)-42, Class3_Sub28_Sub14.aClass94_3672) && !Class3_Sub28_Sub14.password.method1528((byte)-42, Class3_Sub28_Sub14.aClass94_3672)) {
            if(0 != ~CS2Script.anInt2451) {
               Class24.method951(0);
            } else {
               Class3_Sub2.anInt2246 = 0;
               Class117.anInt1616 = 0;
               Class158.anInt2005 = -3;
               Class3_Sub13_Sub31.anInt3375 = 1;
               RSByteBuffer var4 = new RSByteBuffer(128);
               var4.putByte((byte)-97, 10);
               var4.putShort((int)(Math.random() * 99999.0D));
               var4.putShort(530);
               var4.putLong(Class3_Sub28_Sub14.username.toLong(-117), -2037491440);
               var4.putInt(-123, (int)(Math.random() * 9.9999999E7D));
               var4.putString(0, Class3_Sub28_Sub14.password);
               var4.putInt(-128, (int)(Math.random() * 9.9999999E7D));
               int var5 = 9 / ((var3 - 29) / 60);
               var4.encryptRSA(Class3_Sub13_Sub14.aBigInteger3162, Class3_Sub13_Sub37.aBigInteger3441, -296);
               Class3_Sub13_Sub1.outgoingBuffer.index = 0;
               Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-29, 210);
               Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-121, var4.index);
               Class3_Sub13_Sub1.outgoingBuffer.putBytes(var4.buffer, 0, var4.index, 125);
            }
         } else {
            Class158.anInt2005 = 3;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "se.C(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ')');
      }
   }

   final boolean method1794(int var1, int var2) {
      try {
         if(var2 != -20138) {
            method1788(122, 38, -120, -29, false);
         }

         return 0 == (this.aByteArray1730[var1] & 16);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "se.E(" + var1 + ',' + var2 + ')');
      }
   }

   Class131(int var1) {
      try {
         this.anInt1720 = var1;
         this.aClass94Array1721 = new RSString[this.anInt1720];
         this.aShortArray1718 = new short[this.anInt1720];
         this.anIntArray1725 = new int[this.anInt1720];
         this.aByteArray1730 = new byte[this.anInt1720];
         this.aShortArray1727 = new short[this.anInt1720];
      } catch (RuntimeException var3) {
         throw Class44.method1067(var3, "se.<init>(" + var1 + ')');
      }
   }

}

package org.runite.jagex;

final class CS2Script extends Class3 {

   static int anInt2437;
   RSInterface aClass11_2438;
   RSString aClass94_2439;
   static int anInt2440 = 0;
   int anInt2441;
   static Class93 aClass93_2442 = new Class93(50);
   int anInt2443;
   int anInt2444;
   int anInt2445;
   boolean aBoolean2446;
   int anInt2447;
   Object[] arguments;
   RSInterface aClass11_2449;
   static Class93 aClass93_2450 = new Class93(64);
   static int anInt2451 = -1;
   static byte[][][] aByteArrayArrayArray2452;
   static int anInt2453 = 127;


   static final void method375(int var0, CacheIndex var1, CacheIndex var2) {
      try {
         Class10.aClass153_152 = var1;
         Class127.aClass153_1680 = var2;
         Class25.anInt497 = Class127.aClass153_1680.getFileAmount(var0, (byte)95);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "jl.D(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   public static void method376(boolean var0) {
      try {
         aClass93_2442 = null;
         if(var0) {
            method378(97, (byte)-80);
         }

         aByteArrayArrayArray2452 = (byte[][][])null;
         aClass93_2450 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "jl.E(" + var0 + ')');
      }
   }

   static final void sendRegistryRequest(int year, int country, int day, int month, int stage) {
      try {
    	//  System.out.println("CS2Script year=" + year + ", country=" + country + ", day=" + day + ", month=" + month + ", stage=" + stage + ", " + System.currentTimeMillis());
         Class3_Sub13_Sub1.outgoingBuffer.index = 0;
         Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-119, 147);//Handshake opcode
         Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-128, day);
         Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-105, month);
         Class3_Sub13_Sub1.outgoingBuffer.putShort(year);
         Class3_Sub13_Sub1.outgoingBuffer.putShort(country);
         Class132.anInt1734 = 0;
         GraphicDefinition.anInt548 = 0;
         Canvas_Sub1.registryStage = stage;
         Class130.anInt1711 = -3;
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "jl.C(" + year + ',' + country + ',' + day + ',' + month + ',' + stage + ')');
      }
   }

   static final Class79 method378(int var0, byte var1) {
      try {
         Class79 var2 = (Class79)aClass93_2450.get((long)var0, (byte)121);
         if(var2 == null) {
            if(var1 < 126) {
               return (Class79)null;
            } else {
               byte[] var3 = Class101.aClass153_1420.getFile(Class140_Sub7.method2032(var0, 5439488), (byte)-122, Class164.method2234(var0, -127));
               var2 = new Class79();
               if(var3 != null) {
                  var2.method1387(new RSByteBuffer(var3), -111);
               }

               aClass93_2450.put((byte)-84, var2, (long)var0);
               return var2;
            }
         } else {
            return var2;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "jl.A(" + var0 + ',' + var1 + ')');
      }
   }

   static final void method379(int var0) {
      try {
         int var2 = Class146.anInt1904 * 128 - -64;
         int var1 = 128 * Canvas_Sub2.anInt30 + 64;
         int var3 = Class121.method1736(WorldListCountry.localPlane, 1, var1, var2) - Class3_Sub13_Sub34.anInt3414;
         if(100 <= Class3_Sub28_Sub10.anInt3631) {
            NPC.anInt3995 = 64 + Canvas_Sub2.anInt30 * 128;
            Class77.anInt1111 = 64 + Class146.anInt1904 * 128;
            Class7.anInt2162 = Class121.method1736(WorldListCountry.localPlane, var0 + -1023, NPC.anInt3995, Class77.anInt1111) + -Class3_Sub13_Sub34.anInt3414;
         } else {
            if(NPC.anInt3995 < var1) {
               NPC.anInt3995 += Class163_Sub2_Sub1.anInt4021 + Class3_Sub28_Sub10.anInt3631 * (-NPC.anInt3995 + var1) / 1000;
               if(~NPC.anInt3995 < ~var1) {
                  NPC.anInt3995 = var1;
               }
            }

            if(~Class7.anInt2162 > ~var3) {
               Class7.anInt2162 += (-Class7.anInt2162 + var3) * Class3_Sub28_Sub10.anInt3631 / 1000 + Class163_Sub2_Sub1.anInt4021;
               if(Class7.anInt2162 > var3) {
                  Class7.anInt2162 = var3;
               }
            }

            if(~NPC.anInt3995 < ~var1) {
               NPC.anInt3995 -= Class163_Sub2_Sub1.anInt4021 + (NPC.anInt3995 + -var1) * Class3_Sub28_Sub10.anInt3631 / 1000;
               if(NPC.anInt3995 < var1) {
                  NPC.anInt3995 = var1;
               }
            }

            if(Class77.anInt1111 < var2) {
               Class77.anInt1111 += Class163_Sub2_Sub1.anInt4021 + Class3_Sub28_Sub10.anInt3631 * (var2 - Class77.anInt1111) / 1000;
               if(~var2 > ~Class77.anInt1111) {
                  Class77.anInt1111 = var2;
               }
            }

            if(var3 < Class7.anInt2162) {
               Class7.anInt2162 -= (Class7.anInt2162 - var3) * Class3_Sub28_Sub10.anInt3631 / 1000 + Class163_Sub2_Sub1.anInt4021;
               if(~var3 < ~Class7.anInt2162) {
                  Class7.anInt2162 = var3;
               }
            }

            if(~Class77.anInt1111 < ~var2) {
               Class77.anInt1111 -= Class163_Sub2_Sub1.anInt4021 - -((-var2 + Class77.anInt1111) * Class3_Sub28_Sub10.anInt3631 / 1000);
               if(~var2 < ~Class77.anInt1111) {
                  Class77.anInt1111 = var2;
               }
            }
         }

         var2 = Class157.anInt1996 * 128 - -64;
         var1 = MouseListeningClass.anInt1923 * 128 + 64;
         var3 = Class121.method1736(WorldListCountry.localPlane, 1, var1, var2) + -GraphicDefinition.anInt529;
         int var5 = var3 + -Class7.anInt2162;
         int var6 = -Class77.anInt1111 + var2;
         int var4 = -NPC.anInt3995 + var1;
         int var7 = (int)Math.sqrt((double)(var4 * var4 + var6 * var6));
         int var8 = (int)(325.949D * Math.atan2((double)var5, (double)var7)) & 2047;
         if(128 > var8) {
            var8 = 128;
         }

         if(~var8 < -384) {
            var8 = 383;
         }

         int var9 = (int)(-325.949D * Math.atan2((double)var4, (double)var6)) & 2047;
         if(~Class139.anInt1823 > ~var8) {
            Class139.anInt1823 += Class75.anInt1105 + Class163_Sub2_Sub1.anInt4014 * (-Class139.anInt1823 + var8) / 1000;
            if(Class139.anInt1823 > var8) {
               Class139.anInt1823 = var8;
            }
         }

         if(Class139.anInt1823 > var8) {
            Class139.anInt1823 -= (Class139.anInt1823 - var8) * Class163_Sub2_Sub1.anInt4014 / 1000 + Class75.anInt1105;
            if(~Class139.anInt1823 > ~var8) {
               Class139.anInt1823 = var8;
            }
         }

         int var10 = -Class3_Sub13_Sub25.anInt3315 + var9;
         if(var10 > var0) {
            var10 -= 2048;
         }

         if(-1024 > var10) {
            var10 += 2048;
         }

         if(~var10 < -1) {
            Class3_Sub13_Sub25.anInt3315 += var10 * Class163_Sub2_Sub1.anInt4014 / 1000 + Class75.anInt1105;
            Class3_Sub13_Sub25.anInt3315 &= 2047;
         }

         if(-1 < ~var10) {
            Class3_Sub13_Sub25.anInt3315 -= Class163_Sub2_Sub1.anInt4014 * -var10 / 1000 + Class75.anInt1105;
            Class3_Sub13_Sub25.anInt3315 &= 2047;
         }

         int var11 = -Class3_Sub13_Sub25.anInt3315 + var9;
         if(1024 < var11) {
            var11 -= 2048;
         }

         if(~var11 > 1023) {
            var11 += 2048;
         }

         if(var11 < 0 && -1 > ~var10 || var11 > 0 && ~var10 > -1) {
            Class3_Sub13_Sub25.anInt3315 = var9;
         }

      } catch (RuntimeException var12) {
         throw Class44.method1067(var12, "jl.B(" + var0 + ')');
      }
   }

}

package org.runite.client;

final class Class115 {


   static void method1713() {
      try {

          while(true) {
            Class3_Sub5 var1 = (Class3_Sub5) Unsorted.aClass61_2468.method1220();
            if(var1 == null) {
               return;
            }

            Class140_Sub4 var2;
            int var3;
            if(0 > var1.anInt2273) {
               var3 = -var1.anInt2273 - 1;
               if(Class3_Sub1.localIndex == var3) {
                  var2 = Class102.player;
               } else {
                  var2 = Class3_Sub13_Sub22.players[var3];
               }
            } else {
               var3 = var1.anInt2273 - 1;
               var2 = NPC.npcs[var3];
            }

            if(var2 != null) {
               ObjectDefinition var20 = ObjectDefinition.getObjectDefinition(var1.anInt2270);

               int var4;
               int var5;
               if(var1.anInt2284 == 1 || var1.anInt2284 == 3) {
                  var5 = var20.SizeX;
                  var4 = var20.SizeY;
               } else {
                  var4 = var20.SizeX;
                  var5 = var20.SizeY;
               }

               int var7 = (var4 - -1 >> 1) + var1.anInt2271;
               int var6 = (var4 >> 1) + var1.anInt2271;
               int var8 = (var5 >> 1) + var1.anInt2282;
               int var9 = (var5 + 1 >> 1) + var1.anInt2282;
               int[][] var10 = Class44.anIntArrayArrayArray723[WorldListCountry.localPlane];
               int var11 = var10[var7][var9] + var10[var6][var9] + (var10[var6][var8] - -var10[var7][var8]) >> 2;
               GameObject var12 = null;
               int var13 = Class75.anIntArray1107[var1.anInt2278];
               if(var13 == 0) {
                  Class70 var14 = Class154.method2147(WorldListCountry.localPlane, var1.anInt2271, var1.anInt2282);
                  if(var14 != null) {
                     var12 = var14.aClass140_1049;
                  }
               } else if(var13 == 1) {
                  Class19 var21 = Class44.method1068(WorldListCountry.localPlane, var1.anInt2271, var1.anInt2282);
                  if(null != var21) {
                     var12 = var21.aClass140_429;
                  }
               } else if (2 == var13) {
                  Class25 var23 = Class75.method1336(WorldListCountry.localPlane, var1.anInt2271, var1.anInt2282);
                  if (null != var23) {
                     var12 = var23.aClass140_479;
                  }
               } else if (var13 == 3) {
                  Class12 var24 = Unsorted.method784(WorldListCountry.localPlane, var1.anInt2271, var1.anInt2282);
                  if (null != var24) {
                     var12 = var24.object;
                  }
               }

               if(null != var12) {
                  Unsorted.method881(WorldListCountry.localPlane, var1.anInt2282, -96, 0, var1.anInt2271, var1.anInt2283 - -1, -1, var13, 0, var1.anInt2266 - -1);
                  var2.anInt2778 = var1.anInt2283 + Class44.anInt719;
                  var2.anInt2833 = 64 * var5 + var1.anInt2282 * 128;
                  var2.anInt2782 = var4 * 64 + 128 * var1.anInt2271;
                  var2.anObject2796 = var12;
                  int var22 = var1.anInt2268;
                  var2.anInt2812 = var11;
                  var2.anInt2797 = Class44.anInt719 + var1.anInt2266;
                  int var15 = var1.anInt2272;
                  int var16 = var1.anInt2277;
                  int var17 = var1.anInt2279;
                  int var18;
                  if(var22 > var15) {
                     var18 = var22;
                     var22 = var15;
                     var15 = var18;
                  }

                  var2.anInt2818 = var1.anInt2271 + var15;
                  if(var17 < var16) {
                     var18 = var16;
                     var16 = var17;
                     var17 = var18;
                  }

                  var2.anInt2777 = var1.anInt2282 + var16;
                  var2.anInt2817 = var17 + var1.anInt2282;
                  var2.anInt2788 = var1.anInt2271 - -var22;
               }
            }
         }
      } catch (RuntimeException var19) {
         throw ClientErrorException.clientError(var19, "ph.A(" + (byte) -91 + ')');
      }
   }

//   static void method1714() {
//      try {
//         KeyboardListener.aReferenceCache_1911.clearSoftReferences();
//
//          Unsorted.aReferenceCache_1131.clearSoftReferences();
//      } catch (RuntimeException var2) {
//         throw ClientErrorException.clientError(var2, "ph.C(" + (byte) -6 + ')');
//      }
//   }

}

package org.runite.jagex;

final class Class40 {

   static RSString aClass94_672 = RSString.createRSString("null");
   static RSString aClass94_673 = RSString.createRSString(")0");
   static int anInt674;
   static int[] anIntArray675 = new int[]{16, 32, 64, 128};
   static RSString aClass94_676 = null;
   static int anInt677 = 0;
   static int anInt678;
   static CacheIndex aClass153_679;
   static Class3_Sub28_Sub16 aClass3_Sub28_Sub16_680;


   static final int method1040(int var0, int var1, byte var2, int var3) {
      try {
         return var2 != 0?-127:(~var3 < ~var1?var3:(var1 > var0?var0:var1));
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "gd.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method1041(long var0, int var2, RSString name) {
      try {
    	 // System.out.println("Class 40 " + var0 + ", " + var2 + ", " + name.toString());
         Class3_Sub13_Sub1.outgoingBuffer.index = 0;
         Class3_Sub13_Sub1.outgoingBuffer.putByte((byte)-33, 186);
         Class3_Sub13_Sub1.outgoingBuffer.putString(0, name);
        // Class3_Sub13_Sub1.outgoingBuffer.putLong(var0, var2 + -2037463204);
         Canvas_Sub1.registryStage = 1;
         if(var2 == -28236) {
            Class132.anInt1734 = 0;
            GraphicDefinition.anInt548 = 0;
            Class130.anInt1711 = -3;
         }
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "gd.D(" + var0 + ',' + var2 + ')');
      }
   }

   public static void method1042(boolean var0) {
      try {
         aClass153_679 = null;
         aClass94_676 = null;
         if(!var0) {
            aClass153_679 = (CacheIndex)null;
         }

         aClass94_672 = null;
         anIntArray675 = null;
         aClass94_673 = null;
         aClass3_Sub28_Sub16_680 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gd.A(" + var0 + ')');
      }
   }

   static final Class3_Sub28_Sub16_Sub2 method1043(int var0, CacheIndex var1, int var2, int archiveId) {
      try {
         if(var2 != -3178) {
            method1044(-55);
         }
        // System.out.println("Class 40 " + archiveId);
         return Class75_Sub4.method1351(var1, var0, archiveId, -30901)?Class117.method1722(-53):null;
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "gd.G(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ',' + archiveId + ')');
      }
   }

   static final void method1044(int var0) {
      try {
         if(var0 != -3782) {
            aClass94_672 = (RSString)null;
         }

         CS2Script.aClass93_2450.method1523((byte)-109);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gd.E(" + var0 + ')');
      }
   }

   static final void method1045(int var0) {
      try {
         if(var0 != -19761) {
            method1046(-110);
         }

         Class128.aClass93_1683.method1524(3);
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gd.B(" + var0 + ')');
      }
   }

   static final void method1046(int var0) {
      try {
         Class163_Sub1.method2210((byte)-90, false);
         Class3_Sub13_Sub24.anInt3293 = 0;
         boolean var1 = true;

         int var2;
         for(var2 = 0; ~var2 > ~Class164_Sub2.aByteArrayArray3027.length; ++var2) {
            if(0 != ~Client.anIntArray2200[var2] && null == Class164_Sub2.aByteArrayArray3027[var2]) {
               Class164_Sub2.aByteArrayArray3027[var2] = Class3_Sub13_Sub6.aClass153_3077.getFile(Client.anIntArray2200[var2], (byte)-122, 0);
               if(Class164_Sub2.aByteArrayArray3027[var2] == null) {
                  ++Class3_Sub13_Sub24.anInt3293;
                  var1 = false;
               }
            }

            if(-1 != Class101.anIntArray1426[var2] && null == Class3_Sub22.aByteArrayArray2521[var2]) {
               Class3_Sub22.aByteArrayArray2521[var2] = Class3_Sub13_Sub6.aClass153_3077.getFile(Class101.anIntArray1426[var2], Class3_Sub9.regionXteaKeys[var2], 37, 0);
               if(null == Class3_Sub22.aByteArrayArray2521[var2]) {
                  var1 = false;
                  ++Class3_Sub13_Sub24.anInt3293;
               }
            }

            if(HDToolKit.highDetail) {
               if(~Class3_Sub13_Sub15.anIntArray3181[var2] != 0 && Class3_Sub28_Sub14.aByteArrayArray3669[var2] == null) {
                  Class3_Sub28_Sub14.aByteArrayArray3669[var2] = Class3_Sub13_Sub6.aClass153_3077.getFile(Class3_Sub13_Sub15.anIntArray3181[var2], (byte)-122, 0);
                  if(null == Class3_Sub28_Sub14.aByteArrayArray3669[var2]) {
                     var1 = false;
                     ++Class3_Sub13_Sub24.anInt3293;
                  }
               }

               if(Class3_Sub28_Sub5.anIntArray3587[var2] != -1 && null == Class3_Sub13_Sub4.aByteArrayArray3057[var2]) {
                  Class3_Sub13_Sub4.aByteArrayArray3057[var2] = Class3_Sub13_Sub6.aClass153_3077.getFile(Class3_Sub28_Sub5.anIntArray3587[var2], (byte)-122, 0);
                  if(null == Class3_Sub13_Sub4.aByteArrayArray3057[var2]) {
                     ++Class3_Sub13_Sub24.anInt3293;
                     var1 = false;
                  }
               }
            }

            if(null != Class3_Sub13_Sub24.npcSpawnCacheIndices && null == Class3_Sub13_Sub26.aByteArrayArray3335[var2] && Class3_Sub13_Sub24.npcSpawnCacheIndices[var2] != -1) {
               Class3_Sub13_Sub26.aByteArrayArray3335[var2] = Class3_Sub13_Sub6.aClass153_3077.getFile(Class3_Sub13_Sub24.npcSpawnCacheIndices[var2], Class3_Sub9.regionXteaKeys[var2], 92, 0);
               if(Class3_Sub13_Sub26.aByteArrayArray3335[var2] == null) {
                  ++Class3_Sub13_Sub24.anInt3293;
                  var1 = false;
               }
            }
         }

         if(Class3_Sub13_Sub35.aClass131_3421 == null) {
            if(null != Class3_Sub13_Sub21.aClass3_Sub28_Sub3_3264 && Class133.aClass153_1751.method2135(RenderAnimationDefinition.method903(new RSString[]{Class3_Sub13_Sub21.aClass3_Sub28_Sub3_3264.aClass94_3561, Player.aClass94_3964}, (byte)-77), -104)) {
               if(!Class133.aClass153_1751.method2127((byte)-83, RenderAnimationDefinition.method903(new RSString[]{Class3_Sub13_Sub21.aClass3_Sub28_Sub3_3264.aClass94_3561, Player.aClass94_3964}, (byte)-95))) {
                  var1 = false;
                  ++Class3_Sub13_Sub24.anInt3293;
               } else {
                  Class3_Sub13_Sub35.aClass131_3421 = Class81.method1403(-41, RenderAnimationDefinition.method903(new RSString[]{Class3_Sub13_Sub21.aClass3_Sub28_Sub3_3264.aClass94_3561, Player.aClass94_3964}, (byte)-92), Class133.aClass153_1751);
               }
            } else {
               Class3_Sub13_Sub35.aClass131_3421 = new Class131(0);
            }
         }

         if(!var1) {
            Class163_Sub2_Sub1.anInt4019 = 1;
         } else {
            Class162.anInt2038 = 0;
            var1 = true;

            int var4;
            int var5;
            for(var2 = 0; ~Class164_Sub2.aByteArrayArray3027.length < ~var2; ++var2) {
               byte[] var3 = Class3_Sub22.aByteArrayArray2521[var2];
               if(null != var3) {
                  var5 = -Class82.anInt1152 + (Class3_Sub24_Sub3.anIntArray3494[var2] & 255) * 64;
                  var4 = -Class131.anInt1716 + (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8) * 64;
                  if(Class3_Sub29.isDynamicSceneGraph) {
                     var5 = 10;
                     var4 = 10;
                  }

                  var1 &= Class24.isValidObjectMapping((byte)-97, var4, var5, var3);
               }

               if(HDToolKit.highDetail) {
                  var3 = Class3_Sub13_Sub4.aByteArrayArray3057[var2];
                  if(null != var3) {
                     var4 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8);
                     var5 = -Class82.anInt1152 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] & 255);
                     if(Class3_Sub29.isDynamicSceneGraph) {
                        var5 = 10;
                        var4 = 10;
                     }

                     var1 &= Class24.isValidObjectMapping((byte)-74, var4, var5, var3);
                  }
               }
            }

            if(var0 >= -92) {
               method1042(true);
            }

            if(!var1) {
               Class163_Sub2_Sub1.anInt4019 = 2;
            } else {
               if(~Class163_Sub2_Sub1.anInt4019 != -1) {
                  Class3_Sub13.method164((byte)-24, true, RenderAnimationDefinition.method903(new RSString[]{Class3_Sub13_Sub23.aClass94_3282, Class140_Sub2.aClass94_2707}, (byte)-127));
               }

               Class58.method1194(-16385);
               Class3_Sub13_Sub30.method313((byte)58);
               boolean var11 = false;
               int var12;
               if(HDToolKit.highDetail && Class128.aBoolean1685) {
                  for(var12 = 0; ~Class164_Sub2.aByteArrayArray3027.length < ~var12; ++var12) {
                     if(null != Class3_Sub13_Sub4.aByteArrayArray3057[var12] || Class3_Sub28_Sub14.aByteArrayArray3669[var12] != null) {
                        var11 = true;
                        break;
                     }
                  }
               }

               Class3_Sub4.method110(4, 104, 104, HDToolKit.highDetail?28:25, var11);

               for(var12 = 0; 4 > var12; ++var12) {
                  Class86.aClass91Array1182[var12].method1496(0);
               }

               for(var12 = 0; ~var12 > -5; ++var12) {
                  for(var4 = 0; var4 < 104; ++var4) {
                     for(var5 = 0; -105 < ~var5; ++var5) {
                        Class9.aByteArrayArrayArray113[var12][var4][var5] = 0;
                     }
                  }
               }

               Class164_Sub1.method2241((byte)-115, false);
               if(HDToolKit.highDetail) {
                  Class141.aClass109_Sub1_1840.method1671();

                  for(var12 = 0; var12 < 13; ++var12) {
                     for(var4 = 0; var4 < 13; ++var4) {
                        Class141.aClass169ArrayArray1841[var12][var4].aBoolean2106 = true;
                     }
                  }
               }

               if(HDToolKit.highDetail) {
                  Class68.method1279();
               }

               if(HDToolKit.highDetail) {
                  Class39.method1036(118);
               }

               Class58.method1194(-16385);
               System.gc();
               Class163_Sub1.method2210((byte)-90, true);
               Class117.method1720(false, 105);
               if(!Class3_Sub29.isDynamicSceneGraph) {
                  Class47.method1091(false, -93);
                  Class163_Sub1.method2210((byte)-90, true);
                  if(HDToolKit.highDetail) {
                     var12 = Class102.player.anIntArray2767[0] >> 3;
                     var4 = Class102.player.anIntArray2755[0] >> 3;
                     Class3_Sub13_Sub11.method220(true, var4, var12);
                  }

                  Class3_Sub13_Sub6.method198(false, -32624);
                  if(null != Class3_Sub13_Sub26.aByteArrayArray3335) {
                     Class3_Sub13_Sub21.method272((byte)-124);
                  }
               }

               if(Class3_Sub29.isDynamicSceneGraph) {
                  Class49.method1121(false, (byte)98);
                  Class163_Sub1.method2210((byte)-90, true);
                  if(HDToolKit.highDetail) {
                     var12 = Class102.player.anIntArray2767[0] >> 3;
                     var4 = Class102.player.anIntArray2755[0] >> 3;
                     Class3_Sub13_Sub11.method220(true, var4, var12);
                  }

                  Class163_Sub2_Sub1.method2223(false, (byte)-121);
               }

               Class3_Sub13_Sub30.method313((byte)90);
               Class163_Sub1.method2210((byte)-90, true);
               Class158_Sub1.method2189(Class86.aClass91Array1182, false, 66);
               if(HDToolKit.highDetail) {
                  Class68.method1270();
               }

               Class163_Sub1.method2210((byte)-90, true);
               var12 = Class85.anInt1174;
               if(var12 > WorldListCountry.localPlane) {
                  var12 = WorldListCountry.localPlane;
               }

               if(~var12 > ~(WorldListCountry.localPlane + -1)) {
                  var12 = -1 + WorldListCountry.localPlane;
               }

               if(!NPC.method1986(39)) {
                  Class85.method1425(Class85.anInt1174);
               } else {
                  Class85.method1425(0);
               }

               Class56.method1188(-113);
               if(HDToolKit.highDetail && var11) {
                  Class167.method2264(true);
                  Class117.method1720(true, 105);
                  if(!Class3_Sub29.isDynamicSceneGraph) {
                     Class47.method1091(true, -121);
                     Class163_Sub1.method2210((byte)-90, true);
                     Class3_Sub13_Sub6.method198(true, -32624);
                  }

                  if(Class3_Sub29.isDynamicSceneGraph) {
                     Class49.method1121(true, (byte)56);
                     Class163_Sub1.method2210((byte)-90, true);
                     Class163_Sub2_Sub1.method2223(true, (byte)-105);
                  }

                  Class3_Sub13_Sub30.method313((byte)102);
                  Class163_Sub1.method2210((byte)-90, true);
                  Class158_Sub1.method2189(Class86.aClass91Array1182, true, 112);
                  Class163_Sub1.method2210((byte)-90, true);
                  Class56.method1188(-113);
                  Class167.method2264(false);
               }

               if(HDToolKit.highDetail) {
                  for(var4 = 0; var4 < 13; ++var4) {
                     for(var5 = 0; ~var5 > -14; ++var5) {
                        Class141.aClass169ArrayArray1841[var4][var5].method2281(Class44.anIntArrayArrayArray723[0], var4 * 8, var5 * 8);
                     }
                  }
               }

               for(var4 = 0; var4 < 104; ++var4) {
                  for(var5 = 0; 104 > var5; ++var5) {
                     Class128.method1760(var5, (byte)65, var4);
                  }
               }

               RSByteBuffer.method792(9179409);
               Class58.method1194(-16385);
               Class3_Sub13_Sub31.method318(7759444);
               Class3_Sub13_Sub30.method313((byte)100);
               Class3_Sub13_Sub34.aBoolean3416 = false;
               if(GameShell.frame != null && null != Class3_Sub15.aClass89_2429 && 25 == Class143.loadingStage) {
                  Class3_Sub13_Sub1.outgoingBuffer.putOpcode(20);
                  Class3_Sub13_Sub1.outgoingBuffer.putInt(-126, 1057001181);
                  ++Class3_Sub13_Sub30.anInt3365;
               }

               if(!Class3_Sub29.isDynamicSceneGraph) {
                  int var7 = (Class3_Sub7.anInt2294 + 6) / 8;
                  int var6 = (Class3_Sub7.anInt2294 - 6) / 8;
                  var4 = (Class3_Sub28_Sub7.anInt3606 - 6) / 8;
                  var5 = (Class3_Sub28_Sub7.anInt3606 - -6) / 8;

                  for(int var8 = var4 - 1; ~var8 >= ~(var5 - -1); ++var8) {
                     for(int var9 = -1 + var6; ~var9 >= ~(var7 - -1); ++var9) {
                        if(~var8 > ~var4 || var8 > var5 || ~var9 > ~var6 || var9 > var7) {
                           Class3_Sub13_Sub6.aClass153_3077.method2124(-124, RenderAnimationDefinition.method903(new RSString[]{Class3_Sub30_Sub1.aClass94_3807, Class72.method1298((byte)9, var8), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var9)}, (byte)-76));
                           Class3_Sub13_Sub6.aClass153_3077.method2124(-123, RenderAnimationDefinition.method903(new RSString[]{Class161.aClass94_2029, Class72.method1298((byte)9, var8), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var9)}, (byte)-121));
                        }
                     }
                  }
               }

               if(Class143.loadingStage == 28) {
                  Class117.method1719(10, 5);
               } else {
                  Class117.method1719(30, 5);
                  if(null != Class3_Sub15.aClass89_2429) {
                     Class3_Sub13_Sub1.outgoingBuffer.putOpcode(110);
                  }
               }

               Class3_Sub20.method388((byte)116);
               Class58.method1194(-16385);
               Class75_Sub4.method1355(true);
            }
         }
      } catch (RuntimeException var10) {
         throw Class44.method1067(var10, "gd.F(" + var0 + ')');
      }
   }

}

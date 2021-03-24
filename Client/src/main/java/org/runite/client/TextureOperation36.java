package org.runite.client;

import org.rs09.client.config.GameConfig;

public final class TextureOperation36 extends TextureOperation {

   static int anInt3422;
   static int anInt3423;
   static float aFloat3424;
    static boolean aBoolean3094 = false;
    private int[] anIntArray3425;
   static byte[][][] aByteArrayArrayArray3430;
   private int anInt3431;
   private int anInt3433;
   private int anInt3434 = -1;
   static float aFloat3435;

   static void method1628(int var0, int var1, int var2, int var3, int var4, int var5) {
      try {
         int var9;
         int var12;
         if(Class164_Sub1.anInt3012 == 0) {
            int var10 = AtmosphereParser.screenLowerY;
            var9 = Class1.screenUpperY;
            int var8 = Class145.screenUpperX;
            int var7 = Class139.screenLowerX;
            int var11 = (var5 - var3) * (-var7 + var8) / var1 - -var7;
            var12 = var9 + (var10 + -var9) * (-var0 + var4) / var2;
            if(GameObject.aBoolean1837 && (64 & Class164.anInt2051) != 0) {
               RSInterface var13 = AbstractSprite.method638(BufferedDataStream.anInt872, RSInterface.anInt278);
               if(var13 == null) {
                  Class25.method958((byte)-87);
               } else {
                  Class3_Sub24_Sub4.method1177(Unsorted.anInt1887, 0L, (byte)-53, TextCore.aClass94_1724, var11, (short)11, Class3_Sub28_Sub9.aClass94_3621, var12);
               }
            } else {
               if(Class158.paramGameTypeID == 1) {
                  Class3_Sub24_Sub4.method1177(-1, 0L, (byte)-62, TextCore.emptyJagexString, var11, (short)36, TextCore.HasFaceHere, var12);
               }

               Class3_Sub24_Sub4.method1177(-1, 0L, (byte)-75, TextCore.emptyJagexString, var11, (short)60, TextureOperation32.aClass94_3353, var12);
            }
         }

          long var25 = -1L;

          for(var9 = 0; Unsorted.anInt59 > var9; ++var9) {
             long var26 = TextureOperation38.aLongArray3448[var9];
             var12 = (int)var26 & 127;
             int var14 = ((int)var26 & 2009320690) >> 29;
             int var15 = (int)(var26 >>> 32) & Integer.MAX_VALUE;
             int var27 = 127 & (int)var26 >> 7;
             if(var25 != var26) {
                var25 = var26;
                int var18;
                if(var14 == 2 && Unsorted.method2096(WorldListCountry.localPlane, var12, var27, var26)) {
                   ObjectDefinition var16 = ObjectDefinition.getObjectDefinition(var15);
                   if(null != var16.ChildrenIds) {
                      var16 = var16.method1685(0);
                   }

                   if(null == var16) {
                      continue;
                   }

                   if(Class164_Sub1.anInt3012 == 1) {
                      Class3_Sub24_Sub4.method1177(Class99.anInt1403, var26, (byte)-58, RSString.stringCombiner(new RSString[]{RenderAnimationDefinition.aClass94_378, ColorCore.PMColor, var16.name}), var12, (short)14, TextCore.HasUse, var27);
                   } else if(GameObject.aBoolean1837) {
                      Class3_Sub28_Sub9 var17 = -1 == Unsorted.anInt1038?null: LinkedList.method1210(Unsorted.anInt1038);
                      if(0 != (Class164.anInt2051 & 4) && (var17 == null || var17.anInt3614 != var16.method1691(var17.anInt3614, Unsorted.anInt1038, (byte) 98))) {
                         Class3_Sub24_Sub4.method1177(Unsorted.anInt1887, var26, (byte)-77, RSString.stringCombiner(new RSString[]{TextCore.aClass94_676, ColorCore.PMColor, var16.name}), var12, (short)38, Class3_Sub28_Sub9.aClass94_3621, var27);
                      }
                   } else {
                      RSString[] var29 = var16.options;
                      if(Class123.aBoolean1656) {
                         var29 = Class3_Sub31.optionsArrayStringConstructor(var29);
                      }

                      if(var29 != null) {
                         for(var18 = 4; var18 >= 0; --var18) {
                            if(null != var29[var18]) {
                               short var19 = 0;
                               if(var18 == 0) {
                                  var19 = 42;
                               }

                               if(var18 == 1) {
                                  var19 = 50;
                               }

                               int var20 = -1;
                               if(2 == var18) {
                                  var19 = 49;
                               }

                               if(var16.anInt1493 == var18) {
                                  var20 = var16.anInt1517;
                               }

                               if(var18 == 3) {
                                  var19 = 46;
                               }

                               if(var18 == var16.anInt1520) {
                                  var20 = var16.anInt1522;
                               }

                               if(var18 == 4) {
                                  var19 = 1001;
                               }


                               if (GameConfig.OBJECT_DEBUG_ENABLED) {
                                  Class3_Sub24_Sub4.method1177(var20, var26, (byte) -91, RSString.stringCombiner(new RSString[]{ColorCore.ObjectNameColor, var16.name}), var12, var19, var29[var18], var27);
                               } else {
                                  Class3_Sub24_Sub4.method1177(var20, var26, (byte) -91, RSString.stringCombiner(new RSString[]{ColorCore.ObjectNameColor, var16.name}), var12, var19, var29[var18], var27);
                               }

                            }
                         }
                      }
                      if (GameConfig.OBJECT_DEBUG_ENABLED) {
                         Class3_Sub24_Sub4.method1177(Class131.anInt1719, var16.objectId, (byte) -26, RSString.stringCombiner(new RSString[]{ColorCore.ObjectNameColor, var16.name}), var12, (short) 1004, RSString.parse("Examine" + "<br>" + " ID: (X" + var16.objectId + "(Y"), var27);
                      } else {
                         Class3_Sub24_Sub4.method1177(Class131.anInt1719, var16.objectId, (byte) -26, RSString.stringCombiner(new RSString[]{ColorCore.ObjectNameColor, var16.name}), var12, (short) 1004, TextCore.HasExamine, var27);
                      }
                   }
                }

                int var21;
                int var22;
                int var33;
                Player var38;
                NPC var36;
                int var37;
                if(var14 == 1) {
                   NPC var31 = NPC.npcs[var15];
                   if((var31.definition.size & 1) == 0 && (127 & var31.anInt2819) == 0 && (var31.anInt2829 & 127) == 0 || 1 == (var31.definition.size & 1) && (127 & var31.anInt2819) == 64 && (var31.anInt2829 & 127) == 64) {
                      var33 = var31.anInt2819 - -64 - 64 * var31.definition.size;
                      var18 = -((-1 + var31.definition.size) * 64) + var31.anInt2829;

                      for(var37 = 0; var37 < Class163.localNPCCount; ++var37) {
                         var36 = NPC.npcs[Class15.localNPCIndexes[var37]];
                         var21 = -(var36.definition.size * 64) - -64 + var36.anInt2819;
                         var22 = var36.anInt2829 + -(var36.definition.size * 64) - -64;
                         if(var31 != var36 && var33 <= var21 && var31.definition.size - (-var33 + var21 >> 7) >= var36.definition.size && var18 <= var22 && var36.definition.size <= -(-var18 + var22 >> 7) + var31.definition.size) {
                            Unsorted.drawNpcRightClickOptions(var36.definition, var12, -126, Class15.localNPCIndexes[var37], var27);
                         }
                      }

                      for(var37 = 0; var37 < Class159.localPlayerCount; ++var37) {
                         var38 = Unsorted.players[Class56.localPlayerIndexes[var37]];
                         var21 = var38.anInt2819 + 64 + -(64 * var38.getSize());
                         var22 = var38.anInt2829 - (var38.getSize() * 64 + -64);
                         if(var21 >= var33 && var31.definition.size - (var21 - var33 >> 7) >= var38.getSize() && var18 <= var22 && var38.getSize() <= -(-var18 + var22 >> 7) + var31.definition.size) {
                            TextureOperation13.method312(Class56.localPlayerIndexes[var37], 5, var27, var38, var12);
                         }
                      }
                   }

                   Unsorted.drawNpcRightClickOptions(var31.definition, var12, -108, var15, var27);
                }

                if(var14 == 0) {
                   Player var30 = Unsorted.players[var15];
                   if((127 & var30.anInt2819) == 64 && 64 == (127 & var30.anInt2829)) {
                      var33 = var30.anInt2819 + -(64 * (-1 + var30.getSize()));
                      var18 = var30.anInt2829 + 64 + -(var30.getSize() * 64);

                      for(var37 = 0; var37 < Class163.localNPCCount; ++var37) {
                         var36 = NPC.npcs[Class15.localNPCIndexes[var37]];
                         var21 = var36.anInt2819 + -(var36.definition.size * 64) - -64;
                         var22 = var36.anInt2829 - 64 * var36.definition.size - -64;
                         if(var21 >= var33 && var36.definition.size <= -(var21 - var33 >> 7) + var30.getSize() && var18 <= var22 && -(-var18 + var22 >> 7) + var30.getSize() >= var36.definition.size) {
                            Unsorted.drawNpcRightClickOptions(var36.definition, var12, -121, Class15.localNPCIndexes[var37], var27);
                         }
                      }

                      for(var37 = 0; var37 < Class159.localPlayerCount; ++var37) {
                         var38 = Unsorted.players[Class56.localPlayerIndexes[var37]];
                         var21 = var38.anInt2819 - (var38.getSize() + -1) * 64;
                         var22 = var38.anInt2829 - (-64 + 64 * var38.getSize());
                         if(var38 != var30 && var21 >= var33 && var38.getSize() <= var30.getSize() - (var21 - var33 >> 7) && var18 <= var22 && -(var22 + -var18 >> 7) + var30.getSize() >= var38.getSize()) {
                            TextureOperation13.method312(Class56.localPlayerIndexes[var37], 9, var27, var38, var12);
                         }
                      }
                   }

                   TextureOperation13.method312(var15, 31, var27, var30, var12);
                }

                if(var14 == 3) {
                   LinkedList var28 = Class39.aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var12][var27];
                   if(null != var28) {
                      for(WorldMap var32 = (WorldMap)var28.method1212(); null != var32; var32 = (WorldMap)var28.method1219(41)) {
                         var18 = var32.aClass140_Sub7_3676.anInt2936;
                         ItemDefinition var40 = ItemDefinition.getItemDefinition(var18);
                         if(Class164_Sub1.anInt3012 == 1) {
                            Class3_Sub24_Sub4.method1177(Class99.anInt1403, var18, (byte)-75, RSString.stringCombiner(new RSString[]{RenderAnimationDefinition.aClass94_378, ColorCore.BankItemColor, var40.name}), var12, (short)33, TextCore.HasUse, var27);
                         } else if(GameObject.aBoolean1837) {
                            Class3_Sub28_Sub9 var39 = Unsorted.anInt1038 == -1?null: LinkedList.method1210(Unsorted.anInt1038);
                            if((Class164.anInt2051 & 1) != 0 && (null == var39 || var39.anInt3614 != var40.method1115(var39.anInt3614, 100, Unsorted.anInt1038))) {
                               Class3_Sub24_Sub4.method1177(Unsorted.anInt1887, var18, (byte)-70, RSString.stringCombiner(new RSString[]{TextCore.aClass94_676, ColorCore.BankItemColor, var40.name}), var12, (short)39, Class3_Sub28_Sub9.aClass94_3621, var27);
                            }
                         } else {
                            RSString[] var34 = var40.groundOptions;
                            if(Class123.aBoolean1656) {
                               var34 = Class3_Sub31.optionsArrayStringConstructor(var34);
                            }

                            for(var21 = 4; var21 >= 0; --var21) {
                               if(var34 != null && null != var34[var21]) {
                                  byte var35 = 0;
                                  if(var21 == 0) {
                                     var35 = 21;
                                  }

                                  if(1 == var21) {
                                     var35 = 34;
                                  }

                                  int var23 = -1;
                                  if(var40.anInt767 == var21) {
                                     var23 = var40.anInt758;
                                  }

                                  if(var21 == 2) {
                                     var35 = 18;
                                  }

                                  if(var40.anInt788 == var21) {
                                     var23 = var40.anInt756;
                                  }

                                  if(var21 == 3) {
                                     var35 = 20;
                                  }

                                  if(var21 == 4) {
                                     var35 = 24;
                                  }

                                  Class3_Sub24_Sub4.method1177(var23, var18, (byte)-43, RSString.stringCombiner(new RSString[]{ColorCore.GroundItemColor, var40.name}), var12, var35, var34[var21], var27);
                               }
                            }
                            if (GameConfig.ITEM_DEBUG_ENABLED) {
                               Class3_Sub24_Sub4.method1177(Class131.anInt1719, var18, (byte) -43, RSString.stringCombiner(new RSString[]{ColorCore.GroundItemColor, var40.name}), var12, (short) 1002, RSString.parse("Examine" + "<br>" + " ID: (X" + var40.itemId + "(Y"), var27);
                            } else {
                               Class3_Sub24_Sub4.method1177(Class131.anInt1719, var18, (byte) -43, RSString.stringCombiner(new RSString[]{ColorCore.GroundItemColor, var40.name}), var12, (short) 1002, TextCore.HasExamine, var27);
                            }
                         }
                      }
                   }
                }
             }
          }

      } catch (RuntimeException var24) {
         throw ClientErrorException.clientError(var24, "ob.K(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + (byte) 97 + ')');
      }
   }

   final int[][] method166(int var2) {
      try {
         if(-1 != -1) {
            this.anInt3434 = 6;
         }

         int[][] var3 = this.aClass97_2376.method1594((byte)-120, var2);
         if(this.aClass97_2376.aBoolean1379 && this.method339()) {
            int var4 = (this.anInt3433 != Class101.anInt1427?this.anInt3433 * var2 / Class101.anInt1427:var2) * this.anInt3431;
            int[] var5 = var3[0];
            int[] var6 = var3[1];
            int[] var7 = var3[2];
            int var8;
            int var9;
            if(this.anInt3431 == Class113.anInt1559) {
               for(var8 = 0; var8 < Class113.anInt1559; ++var8) {
                  var9 = this.anIntArray3425[var4++];
                  var7[var8] = Unsorted.bitwiseAnd(var9 << 4, 4080);
                  var6[var8] = Unsorted.bitwiseAnd(var9, 65280) >> 4;
                  var5[var8] = Unsorted.bitwiseAnd(var9, 16711680) >> 12;
               }
            } else {
               for(var8 = 0; var8 < Class113.anInt1559; ++var8) {
                  var9 = this.anInt3431 * var8 / Class113.anInt1559;
                  int var10 = this.anIntArray3425[var4 - -var9];
                  var7[var8] = Unsorted.bitwiseAnd(var10 << 4, 4080);
                  var6[var8] = Unsorted.bitwiseAnd(65280, var10) >> 4;
                  var5[var8] = Unsorted.bitwiseAnd(var10 >> 12, 4080);
               }
            }
         }

         return var3;
      } catch (RuntimeException var11) {
         throw ClientErrorException.clientError(var11, "ui.T(" + -1 + ',' + var2 + ')');
      }
   }

   private boolean method339() {
      try {
         if(this.anIntArray3425 == null) {
            if(this.anInt3434 < 0) {
               return false;
            } else {
               int var2 = Class113.anInt1559;
               int var3 = Class101.anInt1427;
               int var4 = !Class17.anInterface2_408.method14((byte)-104, this.anInt3434)?128:64;
               this.anIntArray3425 = Class17.anInterface2_408.method16(64, this.anInt3434);
               this.anInt3433 = var4;
               this.anInt3431 = var4;
               TextureOperation33.method180(18, var3, var2);
               return this.anIntArray3425 != null;
            }
         } else {
            return true;
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ui.LA(" + false + ')');
      }
   }

   static int method340(int var0) {
      try {
         return var0 >>> 8;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ui.NA(" + var0 + ',' + -51 + ')');
      }
   }

   final void decode(int var1, DataBuffer var2) {
      try {
         if(!true) {
            CacheIndex.animationIndex = null;
         }

         if(0 == var1) {
            this.anInt3434 = var2.readUnsignedShort();
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ui.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
      }
   }

   public TextureOperation36() {
      super(0, false);
   }

   final void method161(byte var1) {
      try {
         super.method161(var1);
         this.anIntArray3425 = null;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ui.BA(" + var1 + ')');
      }
   }

   static AbstractIndexedSprite[] method343() {
	      try {
	         AbstractIndexedSprite[] var1 = new AbstractIndexedSprite[Class95.anInt1338];

	         for(int var2 = 0; var2 < Class95.anInt1338; ++var2) {
	            if(HDToolKit.highDetail) {
	               var1[var2] = new HDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], Unsorted.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Unsorted.anIntArray3076[var2], Class163_Sub1.aByteArrayArray2987[var2], TextureOperation38.spritePalette);
	            } else {
	               var1[var2] = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], Unsorted.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Unsorted.anIntArray3076[var2], Class163_Sub1.aByteArrayArray2987[var2], TextureOperation38.spritePalette);
	            }
             }
	         Class39.method1035((byte)113);
	         return var1;
	      } catch (RuntimeException var3) {
	         throw ClientErrorException.clientError(var3, "ui.JA(" + 1854847236 + ')');
	      }
	   }

   final int getSpriteFrame() {
      try {
         return this.anInt3434;
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ui.HA(" + (byte) 19 + ')');
      }
   }

   static void method344(int var0, int var1) {
      try {
         if(0 <= var0 && Class3_Sub24_Sub4.aBooleanArray3503.length > var0) {
            Class3_Sub24_Sub4.aBooleanArray3503[var0] = !Class3_Sub24_Sub4.aBooleanArray3503[var0];
            if(var1 != 4) {
               aByteArrayArrayArray3430 = null;
            }

         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "ui.KA(" + var0 + ',' + var1 + ')');
      }
   }

}

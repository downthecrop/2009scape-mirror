package org.runite.jagex;

final class Class167 {

   static RSString aClass94_2082 = RSString.createRSString(" <col=ffff00>");
   static RSString aClass94_2083 = null;
   static RSString aClass94_2084 = RSString.createRSString("ul");
   static int anInt2085;
   static RSString aClass94_2086 = RSString.createRSString("Continuer");
   static int anInt2087 = 0;


   static final void addLocalNPCs(int var0) {
      try {
         while(true) {
            if(GraphicDefinition.incomingBuffer.method815(Class130.incomingPacketLength, 32666) >= 27) {
               int var1 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 15);
               if(32767 != var1) {
                  boolean var2 = false;
                  if(null == Class3_Sub13_Sub24.npcs[var1]) {
                     var2 = true;
                     Class3_Sub13_Sub24.npcs[var1] = new NPC();
                  }

                  NPC var3 = Class3_Sub13_Sub24.npcs[var1];
                  Class15.localNPCIndexes[Class163.localNPCCount++] = var1;
                  var3.anInt2838 = Class44.anInt719;
                  if(null != var3.definition && var3.definition.method1474(-1)) {
                     Class3_Sub28_Sub8.method574(var3, false);
                  }

                  int var4 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 1);
                  int var5 = Class27.anIntArray510[GraphicDefinition.incomingBuffer.getBits((byte)-11, 3)];
                  if(var2) {
                     var3.anInt2806 = var3.anInt2785 = var5;
                  }

                  int var6 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 1);
                  if(~var6 == -2) {
                     Class21.maskUpdateIndexes[Class66.maskUpdateCount++] = var1;
                  }

                  int var7 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 5);
                  var3.setDefinitions(-1, Node.method522(GraphicDefinition.incomingBuffer.getBits((byte)-11, 14), 27112));
                  if(15 < var7) {
                     var7 -= 32;
                  }

                  int var8 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 5);
                  if(15 < var8) {
                     var8 -= 32;
                  }

                  var3.setSize(var3.definition.size, 2);
                  var3.renderAnimationId = var3.definition.renderAnimationId;
                  var3.anInt2779 = var3.definition.anInt1274;
                  if(~var3.anInt2779 == -1) {
                     var3.anInt2785 = 0;
                  }

                  var3.method1967(-2, var3.getSize((byte)114), Class102.player.anIntArray2767[0] + var8, var7 + Class102.player.anIntArray2755[0], ~var4 == -2);
                  if(var3.definition.method1474(-1)) {
                     Class70.method1286(var3.anIntArray2755[0], false, (ObjectDefinition)null, 0, var3, var3.anIntArray2767[0], WorldListCountry.localPlane, (Player)null);
                  }
                  continue;
               }
            }

            GraphicDefinition.incomingBuffer.method818(false);
            if(var0 <= 0) {
               method2265(-16);
            }

            return;
         }
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "wj.E(" + var0 + ')');
      }
   }

   public static void method2262(byte var0) {
      try {
         aClass94_2083 = null;
         if(var0 > 0) {
            aClass94_2086 = null;
            aClass94_2082 = null;
            aClass94_2084 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wj.F(" + var0 + ')');
      }
   }

   static final void method2263(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      Class113 var7 = new Class113();
      var7.anInt1553 = var1 / 128;
      var7.anInt1547 = var2 / 128;
      var7.anInt1563 = var3 / 128;
      var7.anInt1566 = var4 / 128;
      var7.anInt1554 = var0;
      var7.anInt1562 = var1;
      var7.anInt1545 = var2;
      var7.anInt1560 = var3;
      var7.anInt1550 = var4;
      var7.anInt1544 = var5;
      var7.anInt1548 = var6;
      Class3_Sub28_Sub8.aClass113Array3610[Class3_Sub4.anInt2249++] = var7;
   }

   static final void method2264(boolean var0) {
      if(var0) {
         Class75_Sub2.aClass3_Sub2ArrayArrayArray2638 = Class166.aClass3_Sub2ArrayArrayArray2065;
         Class44.anIntArrayArrayArray723 = Class3_Sub28_Sub7.anIntArrayArrayArray3605;
         Class3_Sub23.aClass3_Sub11ArrayArray2542 = Class3_Sub13_Sub28.aClass3_Sub11ArrayArray3346;
      } else {
         Class75_Sub2.aClass3_Sub2ArrayArrayArray2638 = Class3_Sub28_Sub10_Sub2.aClass3_Sub2ArrayArrayArray4070;
         Class44.anIntArrayArrayArray723 = Class58.anIntArrayArrayArray914;
         Class3_Sub23.aClass3_Sub11ArrayArray2542 = Client.aClass3_Sub11ArrayArray2199;
      }

      Class3_Sub17.anInt2456 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638.length;
   }

   static final void method2265(int var0) {
      try {
         CS2Script.aClass93_2442.method1524(3);
         if(var0 != 0) {
            aClass94_2084 = (RSString)null;
         }

      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "wj.B(" + var0 + ')');
      }
   }

   static final void method2266(int var0, int var1, byte var2) {
      try {
         if(Class9.anInt120 != 0 && var1 != -1) {
            Class70.method1285(Node.aClass153_2573, false, var1, 0, false, Class9.anInt120);
            Class83.aBoolean1158 = true;
         }

         if(var2 != -1) {
            aClass94_2084 = (RSString)null;
         }

      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wj.D(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final void method2267(int var0, int var1, boolean var2, RSByteBuffer var3, int var4, int var5, byte var6, int var7, int var8) {
      try {
         int var9;
         if(-1 >= ~var5 && ~var5 > -105 && var4 >= 0 && -105 < ~var4) {
            if(!var2) {
               Class9.aByteArrayArrayArray113[var8][var5][var4] = 0;
            }

            while(true) {
               var9 = var3.getByte((byte)-111);
               if(-1 == ~var9) {
                  if(!var2) {
                     if(~var8 != -1) {
                        Class44.anIntArrayArrayArray723[var8][var5][var4] = -240 + Class44.anIntArrayArrayArray723[var8 - 1][var5][var4];
                     } else {
                        Class44.anIntArrayArrayArray723[0][var5][var4] = 8 * -Class32.method993(var4 + 556238 + var1, 125, var0 + var5 + 932731);
                     }
                  } else {
                     Class44.anIntArrayArrayArray723[0][var5][var4] = Class58.anIntArrayArrayArray914[0][var5][var4];
                  }
                  break;
               }

               if(var9 == 1) {
                  int var10 = var3.getByte((byte)-110);
                  if(!var2) {
                     if(~var10 == -2) {
                        var10 = 0;
                     }

                     if(-1 == ~var8) {
                        Class44.anIntArrayArrayArray723[0][var5][var4] = 8 * -var10;
                     } else {
                        Class44.anIntArrayArrayArray723[var8][var5][var4] = -(var10 * 8) + Class44.anIntArrayArrayArray723[-1 + var8][var5][var4];
                     }
                  } else {
                     Class44.anIntArrayArrayArray723[0][var5][var4] = Class58.anIntArrayArrayArray914[0][var5][var4] - -(var10 * 8);
                  }
                  break;
               }

               if(49 >= var9) {
                  Class139.aByteArrayArrayArray1828[var8][var5][var4] = var3.getByte();
                  Class93.aByteArrayArrayArray1328[var8][var5][var4] = (byte)((-2 + var9) / 4);
                  PacketParser.aByteArrayArrayArray81[var8][var5][var4] = (byte)Class3_Sub28_Sub15.method633(-2 + var9 + var7, 3);
               } else if(var9 > 81) {
                  Class3_Sub13_Sub36.aByteArrayArrayArray3430[var8][var5][var4] = (byte)(-81 + var9);
               } else if(!var2) {
                  Class9.aByteArrayArrayArray113[var8][var5][var4] = (byte)(var9 - 49);
               }
            }
         } else {
            while(true) {
               var9 = var3.getByte((byte)-103);
               if(~var9 == -1) {
                  break;
               }

               if(~var9 == -2) {
                  var3.getByte((byte)-48);
                  break;
               }

               if(-50 <= ~var9) {
                  var3.getByte((byte)-68);
               }
            }
         }

         if(var6 < 58) {
            anInt2087 = 87;
         }

      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "wj.A(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null?"{...}":"null") + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
      }
   }

   static final int method2268(byte var0, int var1, int var2) {
      try {
         Class3_Sub25 var3 = (Class3_Sub25)Class3_Sub2.aClass130_2220.method1780((long)var1, 0);
         if(var3 != null) {
            if(var2 != -1) {
               int var4 = 0;

               for(int var5 = 0; ~var5 > ~var3.anIntArray2551.length; ++var5) {
                  if(~var2 == ~var3.anIntArray2547[var5]) {
                     var4 += var3.anIntArray2551[var5];
                  }
               }

               if(var0 > -45) {
                  aClass94_2086 = (RSString)null;
               }

               return var4;
            } else {
               return 0;
            }
         } else {
            return 0;
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "wj.H(" + var0 + ',' + var1 + ',' + var2 + ')');
      }
   }

   static final void method2269(byte var0) {
      try {
         if(null != Class3_Sub15.aClass89_2429) {
            Class3_Sub15.aClass89_2429.close(14821);
            Class3_Sub15.aClass89_2429 = null;
         }

         Class3_Sub13_Sub30.method313((byte)110);
         Class32.method995();

         int var1;
         for(var1 = 0; ~var1 > -5; ++var1) {
            Class86.aClass91Array1182[var1].method1496(0);
         }

         Class66.method1250(62, false);
         System.gc();
         NodeList.method882(-1, 2);
         Class83.aBoolean1158 = false;
         Class129.anInt1691 = -1;
         Class164_Sub1.method2241((byte)-77, true);
         Class3_Sub29.isDynamicSceneGraph = false;
         Class82.anInt1152 = 0;
         Class3_Sub28_Sub7.anInt3606 = 0;
         Class3_Sub7.anInt2294 = 0;
         Class131.anInt1716 = 0;

         for(var1 = 0; RuntimeException_Sub1.aClass96Array2114.length > var1; ++var1) {
            RuntimeException_Sub1.aClass96Array2114[var1] = null;
         }

         Class159.localPlayerCount = 0;
         Class163.localNPCCount = 0;
         if(var0 != 46) {
            method2269((byte)43);
         }

         for(var1 = 0; var1 < 2048; ++var1) {
            Class3_Sub13_Sub22.players[var1] = null;
            Class65.aClass3_Sub30Array986[var1] = null;
         }

         for(var1 = 0; -32769 < ~var1; ++var1) {
            Class3_Sub13_Sub24.npcs[var1] = null;
         }

         for(var1 = 0; 4 > var1; ++var1) {
            for(int var2 = 0; -105 < ~var2; ++var2) {
               for(int var3 = 0; ~var3 > -105; ++var3) {
                  Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[var1][var2][var3] = null;
               }
            }
         }

         Class3_Sub28_Sub5.method560(-21556);
         Class113.interfacePacketCounter = 0;
         Class3_Sub13_Sub2.method176(var0 + -161);
         Class3_Sub13_Sub11.method219(true, 3000);
      } catch (RuntimeException var4) {
         throw Class44.method1067(var4, "wj.C(" + var0 + ')');
      }
   }

}

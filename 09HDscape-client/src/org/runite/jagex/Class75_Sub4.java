package org.runite.jagex;

final class Class75_Sub4 extends Class75 {

   static RSString aClass94_2662 = RSString.createRSString("Zugewiesener Speicher)3");
   static int[] anIntArray2663;
   static int[] anIntArray2664;
   private static RSString aClass94_2665 = RSString.createRSString("Choose Option");
   private int anInt2666;
   static RSString aClass94_2667 = aClass94_2665;
   static int[] anIntArray2668 = new int[]{-1, 0, 8, 0, 2, 0, 0, 0, 0, 12, 0, 1, 0, 3, 7, 0, 15, 6, 0, 0, 4, 7, -2, -1, 2, 0, 2, 8, 0, 0, 0, 0, -2, 5, 0, 0, 8, 3, 6, 0, 0, 0, -1, 0, -1, 0, 0, 6, -2, 0, 12, 0, 0, 0, -1, -2, 10, 0, 0, 0, 3, 0, -1, 0, 0, 5, 6, 0, 0, 8, -1, -1, 0, 8, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 6, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, 0, -2, 0, 0, 0, 0, 0, 12, 2, 0, -2, -2, 20, 0, 0, 10, 0, 15, 0, -1, 0, 8, -2, 0, 0, 0, 8, 0, 12, 0, 0, 7, 0, 0, 0, 0, 0, -1, -1, 0, 4, 5, 0, 0, 0, 6, 0, 0, 0, 0, 8, 9, 0, 0, 0, 2, -1, 0, -2, 0, 4, 14, 0, 0, 0, 24, 0, -2, 5, 0, 0, 0, 10, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 2, 1, 0, 0, 2, -1, 1, 0, 0, 0, 0, 14, 0, 0, 0, 0, 10, 5, 0, 0, 0, 0, 0, -2, 0, 0, 9, 0, 0, 8, 0, 0, 0, 0, -2, 6, 0, 0, 0, -2, 0, 3, 0, 1, 7, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 3, 0, 0};
   private int anInt2669;
   static int anInt2670 = 0;
   private int anInt2671;
   private int anInt2672;


   static final void renderNPCMasks(int var0) {
      try {
         int i;
         for(i = 0; Class66.maskUpdateCount > i; ++i) {
            int mask = Class21.maskUpdateIndexes[i];
            NPC npc = Class3_Sub13_Sub24.npcs[mask];
            int var4 = GraphicDefinition.incomingBuffer.getByte((byte)-122);
            if((var4 & 8) != 0) {
               var4 += GraphicDefinition.incomingBuffer.getByte((byte)-90) << 8;
            }

            int var5;
            int var6;
            //Ordinal: 0 Hit
            if(-1 != ~(64 & var4)) {
               var5 = GraphicDefinition.incomingBuffer.getByte((byte)-57);
               var6 = GraphicDefinition.incomingBuffer.getByteC(true);
               npc.method1970(var6, -8, Class44.anInt719, var5);
               npc.anInt2781 = 300 + Class44.anInt719;
               npc.anInt2775 = GraphicDefinition.incomingBuffer.getByteS(true);
            }

            //Ordinal: 1 Hit 2
            if((var4 & 2) != 0) {
               var5 = GraphicDefinition.incomingBuffer.getByteC(true);
               var6 = GraphicDefinition.incomingBuffer.getByteS(true);
               npc.method1970(var6, -8, Class44.anInt719, var5);
            }

            //Ordinal: 2 Animation
            if((var4 & 16) != 0) {
               var5 = GraphicDefinition.incomingBuffer.getShort(1);
               var6 = GraphicDefinition.incomingBuffer.getByte((byte)-83);
               if('\uffff' == var5) {
                  var5 = -1;
               }

               Class130.method1772(var6, var5, 39, npc);
            }

            //Ordinal: 3 Face entity
            if(-1 != ~(var4 & 4)) {
               npc.anInt2772 = GraphicDefinition.incomingBuffer.getShortA(-117);
               if(-65536 == ~npc.anInt2772) {
                  npc.anInt2772 = -1;
               }
            }

            //Ordinal: 4 Graphic
            if(0 != (var4 & 128)) {
               var5 = GraphicDefinition.incomingBuffer.getShortA(46);
               if(var5 == '\uffff') {
                  var5 = -1;
               }

               var6 = GraphicDefinition.incomingBuffer.getLEInt(-46);
               boolean var7 = true;
               if(0 != ~var5 && 0 != ~npc.anInt2842 && Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, var5).anInt542, (byte)-20).anInt1857 < Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, npc.anInt2842).anInt542, (byte)-20).anInt1857) {
                  var7 = false;
               }

               if(var7) {
                  npc.anInt2842 = var5;
                  npc.anInt2759 = ('\uffff' & var6) + Class44.anInt719;
                  npc.anInt2761 = 0;
                  npc.anInt2805 = 0;
                  npc.anInt2799 = var6 >> 16;
                  npc.anInt2826 = 1;
                  if(npc.anInt2759 > Class44.anInt719) {
                     npc.anInt2805 = -1;
                  }

                  if(npc.anInt2842 != -1 && ~npc.anInt2759 == ~Class44.anInt719) {
                     int var8 = RenderAnimationDefinition.getGraphicDefinition((byte)42, npc.anInt2842).anInt542;
                     if(0 != ~var8) {
                        AnimationDefinition var9 = Client.getAnimationDefinition(var8, (byte)-20);
                        if(null != var9 && var9.frames != null) {
                           IOHandler.method1470(npc.anInt2829, var9, 183921384, npc.anInt2819, false, 0);
                        }
                     }
                  }
               }
            }

            //Ordinal: 5 Transform
            if((1 & var4) != 0) {
               if(npc.definition.method1474(-1)) {
                  Class3_Sub28_Sub8.method574(npc, false);
               }

               npc.setDefinitions(-1, Node.method522(GraphicDefinition.incomingBuffer.getLEShort(-84), 27112));
               npc.setSize(npc.definition.size, 2);
               npc.renderAnimationId = npc.definition.renderAnimationId;
               if(npc.definition.method1474(-1)) {
                  Class70.method1286(npc.anIntArray2755[0], false, (ObjectDefinition)null, 0, npc, npc.anIntArray2767[0], WorldListCountry.localPlane, (Player)null);
               }
            }

            //Ordinal: 6 Force chat
            if(-1 != ~(var4 & 32)) {
               npc.textSpoken = GraphicDefinition.incomingBuffer.getString();
               npc.textCycle = 100;
            }

            //Ordinal: 7
            if((256 & var4) != 0) {
               var5 = GraphicDefinition.incomingBuffer.getByteC(true);
               int[] var12 = new int[var5];
               int[] var13 = new int[var5];
               int[] var14 = new int[var5];

               for(int var15 = 0; ~var15 > ~var5; ++var15) {
                  int var10 = GraphicDefinition.incomingBuffer.getLEShort(-101);
                  if(var10 == '\uffff') {
                     var10 = -1;
                  }

                  var12[var15] = var10;
                  var13[var15] = GraphicDefinition.incomingBuffer.getByteS(true);
                  var14[var15] = GraphicDefinition.incomingBuffer.getShort(1);
               }

               Class3_Sub13_Sub22.method273(var14, (byte)92, npc, var13, var12);
            }

            //Ordinal: 8 Face location
            if((var4 & 512) != 0) {
               npc.anInt2786 = GraphicDefinition.incomingBuffer.getShortA(-103);
               npc.anInt2762 = GraphicDefinition.incomingBuffer.getShort(1);
            }
         }

         i = 44 % ((27 - var0) / 39);
      } catch (RuntimeException var11) {
         throw Class44.method1067(var11, "ta.M(" + var0 + ')');
      }
   }

   final void method1337(int var1, boolean var2, int var3) {
      try {
         int var5 = var3 * this.anInt2666 >> 12;
         int var7 = this.anInt2669 * var1 >> 12;
         int var4 = this.anInt2671 * var3 >> 12;
         int var6 = this.anInt2672 * var1 >> 12;
         if(var2) {
            Class145.method2072(this.anInt1104, var4, var6, var5, var7, this.anInt1106, -2);
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ta.E(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   public static void method1350(byte var0) {
      try {
         anIntArray2664 = null;
         aClass94_2662 = null;
         aClass94_2665 = null;
         if(var0 != 75) {
            method1350((byte)-116);
         }

         aClass94_2667 = null;
         anIntArray2668 = null;
         anIntArray2663 = null;
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ta.C(" + var0 + ')');
      }
   }

   Class75_Sub4(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      super(var5, var6, var7);

      try {
         this.anInt2672 = var2;
         this.anInt2666 = var3;
         this.anInt2671 = var1;
         this.anInt2669 = var4;
      } catch (RuntimeException var9) {
         throw Class44.method1067(var9, "ta.<init>(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
      }
   }

   final void method1341(int var1, int var2, int var3) {
      try {
         int var4 = this.anInt2671 * var2 >> 12;
         int var5 = var2 * this.anInt2666 >> 12;
         int var6 = var3 * this.anInt2672 >> 12;
         int var7 = var3 * this.anInt2669 >> 12;
         Class3_Sub29.method730(var4, this.anInt1101, (byte)121, var7, var5, var6);
         if(var1 != 2) {
            aClass94_2665 = (RSString)null;
         }

      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ta.A(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final boolean method1351(CacheIndex var0, int var1, int archiveId, int var3) {
      try {
         byte[] var4 = var0.getFile(archiveId, (byte)-122, var1);
         if(var3 != -30901) {
            aClass94_2662 = (RSString)null;
         }

         if(var4 != null) {
            Class45.method1082(var4, 98);
            return true;
         } else {
            return false;
         }
      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "ta.N(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + archiveId + ',' + var3 + ')');
      }
   }

   final void method1335(int var1, int var2, int var3) {
      try {
         if(var3 == 4898) {
            int var4 = var2 * this.anInt2671 >> 12;
            int var6 = this.anInt2672 * var1 >> 12;
            int var5 = var2 * this.anInt2666 >> 12;
            int var7 = this.anInt2669 * var1 >> 12;
            Class3_Sub13_Sub5.method194(this.anInt1106, var7, this.anInt1101, this.anInt1104, var6, 4096, var5, var4);
         }
      } catch (RuntimeException var8) {
         throw Class44.method1067(var8, "ta.D(" + var1 + ',' + var2 + ',' + var3 + ')');
      }
   }

   static final void method1352(int var0, boolean var1, int var2, int var3, int var4) {
      try {
         if(Canvas_Sub2.loadInterface(var3, 104)) {
            Class158.method2183(var2, var1, var4, 235, var0, GameObject.aClass11ArrayArray1834[var3]);
         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ta.K(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final void method1353(Class3_Sub28_Sub16_Sub2[] var0, int var1, CacheIndex var2) {
      try {
         Class3_Sub13_Sub17.aClass153_3210 = var2;
         GameObject.aClass3_Sub28_Sub16_Sub2Array1839 = var0;
         if(var1 == -11931) {
            Class3_Sub24_Sub4.aBooleanArray3503 = new boolean[GameObject.aClass3_Sub28_Sub16_Sub2Array1839.length];
            Class134.aClass61_1758.method1211(-68);
            int var3 = Class3_Sub13_Sub17.aClass153_3210.getArchiveForName(Class3_Sub8.aClass94_2304, (byte)-30);
            int[] var4 = Class3_Sub13_Sub17.aClass153_3210.getFileIds((byte)-128, var3);

            for(int var5 = 0; ~var4.length < ~var5; ++var5) {
               Class134.aClass61_1758.method1215(true, Class124.method1747(new RSByteBuffer(Class3_Sub13_Sub17.aClass153_3210.getFile(var3, (byte)-122, var4[var5])), true));
            }

         }
      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ta.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + (var2 != null?"{...}":"null") + ')');
      }
   }

   static final void method1354(int var0, int var1, boolean var2, int var3, int var4) {
      try {
         if(var4 >= Class159.anInt2020 && var4 <= Class57.anInt902) {
            var0 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var0, (byte)0, Class101.anInt1425);
            var3 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var3, (byte)0, Class101.anInt1425);
            Class3_Sub13_Sub32.method320(var1, var4, var3, (byte)-123, var0);
         }

         if(!var2) {
            aClass94_2665 = (RSString)null;
         }

      } catch (RuntimeException var6) {
         throw Class44.method1067(var6, "ta.L(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
      }
   }

   static final void method1355(boolean var0) {
      try {
         Class3_Sub25.aClass129_2552.method1770(-124);

         int var1;
         for(var1 = 0; var1 < 32; ++var1) {
            Class163_Sub1.aLongArray2986[var1] = 0L;
         }

         if(var0) {
            for(var1 = 0; var1 < 32; ++var1) {
               Class134.aLongArray1766[var1] = 0L;
            }

            Class133.anInt1754 = 0;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "ta.O(" + var0 + ')');
      }
   }

}

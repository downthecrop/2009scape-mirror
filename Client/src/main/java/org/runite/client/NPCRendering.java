package org.runite.client;

public final class NPCRendering {


    static final int[] NPC_RENDER_LOG = new int[3];

    static void renderNPCs(int var0) {
       try {
          Unsorted.maskUpdateCount = 0;
          Class139.anInt1829 = 0;
          renderLocalNPCs();
          NPC_RENDER_LOG[0] = BufferedDataStream.incomingBuffer.index;
          addLocalNPCs();
          NPC_RENDER_LOG[1] = BufferedDataStream.incomingBuffer.index;
          renderNPCMasks();
          NPC_RENDER_LOG[2] = BufferedDataStream.incomingBuffer.index;

          int var1;
          for(var1 = 0; Class139.anInt1829 > var1; ++var1) {
             int var2 = Class3_Sub7.anIntArray2292[var1];
             if(Class44.anInt719 != NPC.npcs[var2].anInt2838) {
                if(NPC.npcs[var2].definition.method1474()) {
                   Class3_Sub28_Sub8.method574(NPC.npcs[var2]);
                }

                NPC.npcs[var2].setDefinitions(null);
                NPC.npcs[var2] = null;
             }
          }

          if(var0 != 8169) {
             renderNPCs(96);
          }

          if(Unsorted.incomingPacketLength == BufferedDataStream.incomingBuffer.index) {
             for(var1 = 0; var1 < Class163.localNPCCount; ++var1) {
                if(null == NPC.npcs[Class15.localNPCIndexes[var1]]) {
 //            	   System.err.println("gnp2 pos:" + var1 + " size:" + Class163.anInt2046);
                       System.err.println("Local NPC was null - index: " + Class15.localNPCIndexes[var1] + ", list index: " + var1 + ", list size: " + Class163.localNPCCount);
                }
             }

          } else {
                 System.err.println("NPC rendering packet size mismatch - size log: local=" + NPC_RENDER_LOG[0] + ", add global=" + NPC_RENDER_LOG[1] + ", masks=" + NPC_RENDER_LOG[2] + ".");
 //         System.err.println("gnp1 pos:" + GraphicDefinition.incomingBuffer.index + " psize:" + Class130.incomingPacketLength);
 //            throw new RuntimeException("gnp1 pos:" + Class28.incomingBuffer.index + " psize:" + Class130.incomingPacketLength);
           }
       } catch (RuntimeException var3) {
          throw ClientErrorException.clientError(var3, "gm.B(" + var0 + ')');
       }
    }

   static void renderLocalNPCs() {
       try {
           BufferedDataStream.incomingBuffer.setBitAccess();
           int var1 = BufferedDataStream.incomingBuffer.getBits(8);
           int var2;
           if (var1 < Class163.localNPCCount) {
               for (var2 = var1; var2 < Class163.localNPCCount; ++var2) {
                   Class3_Sub7.anIntArray2292[Class139.anInt1829++] = Class15.localNPCIndexes[var2];
               }
           }

           if (Class163.localNPCCount < var1) {
               throw new RuntimeException("gnpov1");
           } else {
               Class163.localNPCCount = 0;

               for (var2 = 0; var1 > var2; ++var2) {
                   int var3 = Class15.localNPCIndexes[var2];
                   NPC var4 = NPC.npcs[var3];
                   int var5 = BufferedDataStream.incomingBuffer.getBits(1);
                   if (0 == var5) {
                       Class15.localNPCIndexes[Class163.localNPCCount++] = var3;
                       var4.anInt2838 = Class44.anInt719;
                   } else {
                       int var6 = BufferedDataStream.incomingBuffer.getBits(2);
                       if (var6 == 0) {
                           Class15.localNPCIndexes[Class163.localNPCCount++] = var3;
                           var4.anInt2838 = Class44.anInt719;
                           Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                       } else {
                           int var7;
                           int var8;
                           if (1 == var6) {
                               Class15.localNPCIndexes[Class163.localNPCCount++] = var3;
                               var4.anInt2838 = Class44.anInt719;
                               var7 = BufferedDataStream.incomingBuffer.getBits(3);
                               var4.walkStep(1, (byte) 32, var7);
                               var8 = BufferedDataStream.incomingBuffer.getBits(1);
                               if (1 == var8) {
                                   Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                               }
                           } else if (var6 == 2) {
                               Class15.localNPCIndexes[Class163.localNPCCount++] = var3;
                               var4.anInt2838 = Class44.anInt719;
                               if (BufferedDataStream.incomingBuffer.getBits(1) == 1) {
                                   var7 = BufferedDataStream.incomingBuffer.getBits(3);
                                   var4.walkStep(2, (byte) -122, var7);
                                   var8 = BufferedDataStream.incomingBuffer.getBits(3);
                                   var4.walkStep(2, (byte) 85, var8);
                               } else {
                                   var7 = BufferedDataStream.incomingBuffer.getBits(3);
                                   var4.walkStep(0, (byte) -80, var7);
                               }

                               var7 = BufferedDataStream.incomingBuffer.getBits(1);
                               if (var7 == 1) {
                                   Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                               }
                           } else if (var6 == 3) {
                               Class3_Sub7.anIntArray2292[Class139.anInt1829++] = var3;
                           }
                       }
                   }
               }

           }
       } catch (RuntimeException var9) {
           throw ClientErrorException.clientError(var9, "dm.E(" + (byte) -11 + ')');
       }
   }

   static void addLocalNPCs() {
       try {
           while (true) {
               if (BufferedDataStream.incomingBuffer.method815(Unsorted.incomingPacketLength) >= 27) {
                   int var1 = BufferedDataStream.incomingBuffer.getBits(15);
                   if (32767 != var1) {
                       boolean var2 = false;
                       if (null == NPC.npcs[var1]) {
                           var2 = true;
                           NPC.npcs[var1] = new NPC();
                       }

                       NPC var3 = NPC.npcs[var1];
                       Class15.localNPCIndexes[Class163.localNPCCount++] = var1;
                       var3.anInt2838 = Class44.anInt719;
                       if (null != var3.definition && var3.definition.method1474()) {
                           Class3_Sub28_Sub8.method574(var3);
                       }

                       int var4 = BufferedDataStream.incomingBuffer.getBits(1);
                       int var5 = Class27.anIntArray510[BufferedDataStream.incomingBuffer.getBits(3)];
                       if (var2) {
                           var3.anInt2806 = var3.anInt2785 = var5;
                       }

                       int var6 = BufferedDataStream.incomingBuffer.getBits(1);
                       if (var6 == 1) {
                           Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var1;
                       }

                       int var7 = BufferedDataStream.incomingBuffer.getBits(5);
                       var3.setDefinitions(NPCDefinition.getNPCDefinition(BufferedDataStream.incomingBuffer.getBits(14)));
                       if (15 < var7) {
                           var7 -= 32;
                       }

                       int var8 = BufferedDataStream.incomingBuffer.getBits(5);
                       if (15 < var8) {
                           var8 -= 32;
                       }

                       var3.setSize(var3.definition.size, 2);
                       var3.renderAnimationId = var3.definition.renderAnimationId;
                       var3.anInt2779 = var3.definition.anInt1274;
                       if (var3.anInt2779 == 0) {
                           var3.anInt2785 = 0;
                       }

                       var3.method1967(var3.getSize(), Class102.player.anIntArray2767[0] + var8, var7 + Class102.player.anIntArray2755[0], var4 == 1);
                       if (var3.definition.method1474()) {
                           Class70.method1286(var3.anIntArray2755[0], null, 0, var3, var3.anIntArray2767[0], WorldListCountry.localPlane, null);
                       }
                       continue;
                   }
               }

               BufferedDataStream.incomingBuffer.method818();

               return;
           }
       } catch (RuntimeException var9) {
           throw ClientErrorException.clientError(var9, "wj.E(" + 113 + ')');
       }
   }

   static void renderNPCMasks() {
       try {
           int i;
           for (i = 0; Unsorted.maskUpdateCount > i; ++i) {
               int mask = Class21.maskUpdateIndexes[i];
               NPC npc = NPC.npcs[mask];
               int var4 = BufferedDataStream.incomingBuffer.readUnsignedByte();
               if ((var4 & 8) != 0) {
                   var4 += BufferedDataStream.incomingBuffer.readUnsignedByte() << 8;
               }

               int var5;
               int var6;
               //Ordinal: 0 Hit
               if ((64 & var4) != 0) {
                   var5 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                   var6 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                   npc.addHit(var6, Class44.anInt719, var5);
                   npc.anInt2781 = 300 + Class44.anInt719;
                   npc.anInt2775 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
               }

               //Ordinal: 1 Hit 2
               if ((var4 & 2) != 0) {
                   var5 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                   var6 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                   npc.addHit(var6, Class44.anInt719, var5);
               }

               //Ordinal: 2 Animation
               if ((var4 & 16) != 0) {
                   var5 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                   var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                   if (65535 == var5) {
                       var5 = -1;
                   }

                   Unsorted.method1772(var6, var5, 39, npc);
               }

               //Ordinal: 3 Face entity
               if ((var4 & 4) != 0) {
                   npc.anInt2772 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                   if (npc.anInt2772 == 65535) {
                       npc.anInt2772 = -1;
                   }
               }

               //Ordinal: 4 Graphic
               if (0 != (var4 & 128)) {
                   var5 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                   if (var5 == 65535) {
                       var5 = -1;
                   }

                   var6 = BufferedDataStream.incomingBuffer.readIntLE();
                   boolean var7 = true;
                   if (var5 != -1 && npc.anInt2842 != -1 && SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, var5).anInt542).forcedPriority < SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, npc.anInt2842).anInt542).forcedPriority) {
                       var7 = false;
                   }

                   if (var7) {
                       npc.anInt2842 = var5;
                       npc.anInt2759 = (65535 & var6) + Class44.anInt719;
                       npc.anInt2761 = 0;
                       npc.anInt2805 = 0;
                       npc.anInt2799 = var6 >> 16;
                       npc.anInt2826 = 1;
                       if (npc.anInt2759 > Class44.anInt719) {
                           npc.anInt2805 = -1;
                       }

                       if (npc.anInt2842 != -1 && Class44.anInt719 == npc.anInt2759) {
                           int var8 = GraphicDefinition.getGraphicDefinition((byte) 42, npc.anInt2842).anInt542;
                           if (var8 != -1) {
                               SequenceDefinition var9 = SequenceDefinition.getAnimationDefinition(var8);
                               if (var9.frames != null) {
                                   Unsorted.method1470(npc.anInt2829, var9, 183921384, npc.anInt2819, false, 0);
                               }
                           }
                       }
                   }
               }

               //Ordinal: 5 Transform
               if ((1 & var4) != 0) {
                   if (npc.definition.method1474()) {
                       Class3_Sub28_Sub8.method574(npc);
                   }

                   npc.setDefinitions(NPCDefinition.getNPCDefinition(BufferedDataStream.incomingBuffer.readUnsignedShortLE()));
                   npc.setSize(npc.definition.size, 2);
                   npc.renderAnimationId = npc.definition.renderAnimationId;
                   if (npc.definition.method1474()) {
                       Class70.method1286(npc.anIntArray2755[0], null, 0, npc, npc.anIntArray2767[0], WorldListCountry.localPlane, null);
                   }
               }

               //Ordinal: 6 Force chat
               if ((var4 & 32) != 0) {
                   npc.textSpoken = BufferedDataStream.incomingBuffer.readString();
                   npc.textCycle = 100;
               }

               //Ordinal: 7
               if ((256 & var4) != 0) {
                   var5 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                   int[] var12 = new int[var5];
                   int[] var13 = new int[var5];
                   int[] var14 = new int[var5];

                   for (int var15 = 0; var5 > var15; ++var15) {
                       int var10 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                       if (var10 == 65535) {
                           var10 = -1;
                       }

                       var12[var15] = var10;
                       var13[var15] = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                       var14[var15] = BufferedDataStream.incomingBuffer.readUnsignedShort();
                   }

                   NPC.method273(var14, npc, var13, var12);
               }

               //Ordinal: 8 Face location
               if ((var4 & 512) != 0) {
                   npc.anInt2786 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                   npc.anInt2762 = BufferedDataStream.incomingBuffer.readUnsignedShort();
               }
           }

       } catch (RuntimeException var11) {
           throw ClientErrorException.clientError(var11, "ta.M(" + ')');
       }
   }
}

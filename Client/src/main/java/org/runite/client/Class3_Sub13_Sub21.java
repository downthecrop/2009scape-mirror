package org.runite.client;
import org.rs09.client.net.Connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

final class Class3_Sub13_Sub21 extends TextureOperation {
//Class3_Sub13_Sub21
   private int anInt3253 = 0;
   private int anInt3254 = 4096;
   static int anInt3256;
   private int anInt3257 = 12288;
   private int anInt3258 = 0;
   static int anInt3260 = -1;
   static boolean aBoolean3261 = false;
   private int anInt3262 = 2048;
   static int anInt3263 = 0;
   static Class3_Sub28_Sub3 aClass3_Sub28_Sub3_3264;
   private int anInt3265 = 2048;
   private int anInt3266 = 8192;


   final void method157(int var1, DataBuffer var2, boolean var3) {
      try {
         if(var1 == 0) {
            this.anInt3265 = var2.readUnsignedShort();
         } else if(var1 == 1) {
            this.anInt3253 = var2.readUnsignedShort();
         } else if(var1 == 2) {
            this.anInt3258 = var2.readUnsignedShort();
         } else if(var1 == 3) {
            this.anInt3262 = var2.readUnsignedShort();
         } else if (var1 == 4) {
            this.anInt3257 = var2.readUnsignedShort();
         } else if (var1 == 5) {
            this.anInt3254 = var2.readUnsignedShort();
         } else if (var1 == 6) {
            this.anInt3266 = var2.readUnsignedShort();
         }

         if(!var3) {
            this.anInt3266 = 85;
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "mh.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static void method267() {
	  try {
         if(Unsorted.registryStage != 0) {
            try {
               if(++Class132.anInt1734 > 2000) {
                  if(null != Class3_Sub15.activeConnection) {
                     Class3_Sub15.activeConnection.close();
                     Class3_Sub15.activeConnection = null;
                  }

                  if(GraphicDefinition.anInt548 >= 1) {
                     Unsorted.anInt1711 = -5;
                     Unsorted.registryStage = 0;
                     return;
                  }

                  Unsorted.registryStage = 1;
                  Class132.anInt1734 = 0;
                  ++GraphicDefinition.anInt548;
                  if(Class140_Sub6.accRegistryPort == Class162.anInt2036) {
                     Class140_Sub6.accRegistryPort = Client.currentPort;
                  } else {
                     Class140_Sub6.accRegistryPort = Class162.anInt2036;
                  }
               }

               if(Unsorted.registryStage == 1) {
            	   //Ip & Port
                  Class3_Sub9.aClass64_2318 = Class38.signlink.method1441((byte)8, Class38_Sub1.accRegistryIp, Class140_Sub6.accRegistryPort);
                  Unsorted.registryStage = 2;
               }

               int response;
               if(Unsorted.registryStage == 2) {
                  if(Objects.requireNonNull(Class3_Sub9.aClass64_2318).anInt978 == 2) {
                     throw new IOException();
                  }
                  if(1 != Class3_Sub9.aClass64_2318.anInt978) {
                     return;
                  }
                  Class3_Sub15.activeConnection = new Connection((Socket)Class3_Sub9.aClass64_2318.anObject974, Class38.signlink);
                  Class3_Sub9.aClass64_2318 = null;
                  Class3_Sub15.activeConnection.sendBytes(Class3_Sub13_Sub1.outgoingBuffer.buffer, Class3_Sub13_Sub1.outgoingBuffer.index);
                   if(WorldListEntry.aClass155_2627 != null) {
                     WorldListEntry.aClass155_2627.method2159(63);
                  }
                  if(null != Class3_Sub21.aClass155_2491) {
                     Class3_Sub21.aClass155_2491.method2159(66);
                  }
                  response = Class3_Sub15.activeConnection.readByte();
                  System.out.println("Response = " + response);
                  if(WorldListEntry.aClass155_2627 != null) {
                     WorldListEntry.aClass155_2627.method2159(64);
                  }
                  if(Class3_Sub21.aClass155_2491 != null) {
                     Class3_Sub21.aClass155_2491.method2159(111);
                  }
                  if(response != 21) {
                     Unsorted.anInt1711 = response;
                     Unsorted.registryStage = 0;
                     Class3_Sub15.activeConnection.close();
                     Class3_Sub15.activeConnection = null;
                     return;
                  }
                  Unsorted.registryStage = 3;
               }

               if(3 == Unsorted.registryStage) {
                  if(Class3_Sub15.activeConnection.availableBytes() < 1) {
                     return;
                  }

                  Class3_Sub13_Sub33.aClass94Array3391 = new RSString[Class3_Sub15.activeConnection.readByte()];
                  Unsorted.registryStage = 4;
               }

               if(Unsorted.registryStage == 4) {
                  if(8 * Class3_Sub13_Sub33.aClass94Array3391.length > Class3_Sub15.activeConnection.availableBytes()) {
                     return;
                  }

                  GraphicDefinition.incomingBuffer.index = 0;
                  Class3_Sub15.activeConnection.readBytes(GraphicDefinition.incomingBuffer.buffer, 0, 8 * Class3_Sub13_Sub33.aClass94Array3391.length);

                  for(response = 0; response < Class3_Sub13_Sub33.aClass94Array3391.length; ++response) {
                     Class3_Sub13_Sub33.aClass94Array3391[response] = Unsorted.method1052(GraphicDefinition.incomingBuffer.readLong());
                  }

                  Unsorted.anInt1711 = 21;
                  Unsorted.registryStage = 0;
                  Class3_Sub15.activeConnection.close();
                  Class3_Sub15.activeConnection = null;
                  return;
               }
            } catch (IOException var2) {
               if(Class3_Sub15.activeConnection != null) {
                  Class3_Sub15.activeConnection.close();
                  Class3_Sub15.activeConnection = null;
               }

               if(GraphicDefinition.anInt548 < 1) {
                  ++GraphicDefinition.anInt548;
                  if(Class162.anInt2036 == Class140_Sub6.accRegistryPort) {
                     Class140_Sub6.accRegistryPort = Client.currentPort;
                  } else {
                     Class140_Sub6.accRegistryPort = Class162.anInt2036;
                  }

                  Class132.anInt1734 = 0;
                  Unsorted.registryStage = 1;
               } else {
                  Unsorted.anInt1711 = -4;
                  Unsorted.registryStage = 0;
               }
            }

         }
      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "mh.Q(" + (byte) 36 + ')');
      }
   }

   public Class3_Sub13_Sub21() {
      super(0, true);
   }

   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(var1);
         if(this.aClass114_2382.aBoolean1580) {
            int var5 = -2048 + Class163_Sub3.anIntArray2999[var1];

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               int var9 = var5 + this.anInt3253;
               int var7 = Class102.anIntArray2125[var6] + -2048;
               int var8 = this.anInt3265 + var7;
               var9 = var9 < -2048 ?var9 - -4096:var9;
               var9 = 2048 < var9?-4096 + var9:var9;
               int var10 = var7 + this.anInt3258;
               var8 = var8 < -2048?var8 + 4096:var8;
               var8 = var8 <= 2048 ?var8:-4096 + var8;
               var10 = var10 >= -2048 ?var10:4096 + var10;
               var10 = var10 > 2048 ?var10 - 4096:var10;
               int var11 = var5 - -this.anInt3262;
               var11 = -2048 > var11?var11 + 4096:var11;
               var11 = var11 > 2048 ?var11 - 4096:var11;
               var3[var6] = !this.method271(var8, var9) && !this.method270(var10, var11)?0:4096;
            }
         }

         return var3;
      } catch (RuntimeException var12) {
         throw ClientErrorException.clientError(var12, "mh.D(" + var1 + ',' + var2 + ')');
      }
   }

   private boolean method270(int var2, int var3) {
      try {
         int var4 = this.anInt3257 * (var2 + var3) >> 12;

         int var5 = Class75_Sub2.anIntArray2639[var4 * 255 >> 12 & 255];
         var5 = (var5 << 12) / this.anInt3257;
         var5 = (var5 << 12) / this.anInt3266;
         var5 = var5 * this.anInt3254 >> 12;
         return var5 > -var2 + var3 && var3 + -var2 > -var5;
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "mh.S(" + (byte) -44 + ',' + var2 + ',' + var3 + ')');
      }
   }

   final void method158(int var1) {
      try {
         Class8.method844((byte)-9);
         if(var1 != 16251) {
            method272((byte)-85);
         }

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "mh.P(" + var1 + ')');
      }
   }

   private boolean method271(int var1, int var2) {
      try {
         int var4 = (var2 - var1) * this.anInt3257 >> 12;

         int var5 = Class75_Sub2.anIntArray2639[(1047948 & var4 * 255) >> 12];
         var5 = (var5 << 12) / this.anInt3257;
         var5 = (var5 << 12) / this.anInt3266;
         var5 = var5 * this.anInt3254 >> 12;
         return var5 > var2 + var1 && -var5 < var2 + var1;
      } catch (RuntimeException var6) {
         throw ClientErrorException.clientError(var6, "mh.F(" + var1 + ',' + var2 + ',' + (byte) 113 + ')');
      }
   }

   static void method272(byte var0) {
      try {
         if(var0 != -124) {
            aClass3_Sub28_Sub3_3264 = null;
         }

         int var1 = Class3_Sub13_Sub26.aByteArrayArray3335.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            if(Class3_Sub13_Sub26.aByteArrayArray3335[var2] != null) {
               int var3 = -1;

               for(int var4 = 0; Class3_Sub13_Sub20.anInt3244 > var4; ++var4) {
                  if(Class3_Sub13_Sub30.anIntArray3367[var4] == Class3_Sub24_Sub3.anIntArray3494[var2]) {
                     var3 = var4;
                     break;
                  }
               }

               if(var3 == -1) {
                  Class3_Sub13_Sub30.anIntArray3367[Class3_Sub13_Sub20.anInt3244] = Class3_Sub24_Sub3.anIntArray3494[var2];
                  var3 = Class3_Sub13_Sub20.anInt3244++;
               }

               int var5 = 0;
               DataBuffer var16 = new DataBuffer(Class3_Sub13_Sub26.aByteArrayArray3335[var2]);

               while(Class3_Sub13_Sub26.aByteArrayArray3335[var2].length > var16.index && 511 > var5) {
                  int var6 = var5++ << 6 | var3;
                  int var7 = var16.readUnsignedShort();
                  int var8 = var7 >> 14;
                  int var9 = 63 & var7 >> 7;
                  int var11 = var9 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8) - Class131.anInt1716;
                  int var10 = var7 & 63;
                  int var12 = var10 + -Class82.anInt1152 + 64 * (255 & Class3_Sub24_Sub3.anIntArray3494[var2]);
                  NPCDefinition var13 = NPCDefinition.getNPCDefinition(var16.readUnsignedShort());
                  if(NPC.npcs[var6] == null && (var13.aByte1267 & 1) > 0 && Class140_Sub3.anInt2745 == var8 && var11 >= 0 && 104 > var13.size + var11 && var12 >= 0 && 104 > var12 - -var13.size) {
                     NPC.npcs[var6] = new NPC();
                     NPC npc = NPC.npcs[var6];
                     Class15.localNPCIndexes[Class163.localNPCCount++] = var6;
                     npc.anInt2838 = Class44.anInt719;
                     npc.setDefinitions(var13);
                     npc.setSize(npc.definition.size, 2);
                     npc.anInt2806 = npc.anInt2785 = Class27.anIntArray510[npc.definition.aByte1268];
                     npc.anInt2779 = npc.definition.anInt1274;
                     if(npc.anInt2779 == 0) {
                        npc.anInt2785 = 0;
                     }

                     npc.renderAnimationId = npc.definition.renderAnimationId;
                     npc.method1967(npc.getSize(), var11, var12, true);
                  }
               }
            }
         }

      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "mh.E(" + var0 + ')');
      }
   }

}

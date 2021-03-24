package org.runite.client;
import org.rs09.client.config.GameConfig;

import java.math.BigInteger;

final class TextureOperation31 extends TextureOperation {

   static CacheResourceWorker aCacheResourceWorker_3159;
   private int anInt3160 = 0;
   static BigInteger MODULUS = GameConfig.MODULUS;
   private int anInt3163 = 20;
   private int anInt3164 = 1365;
   private int anInt3165 = 0;
   static boolean aBoolean3166 = false;

   final void method157(int var1, DataBuffer var2, boolean var3) {
      try {
         if(var1 == 0) {
            this.anInt3164 = var2.readUnsignedShort();
         } else if (var1 == 1) {
            this.anInt3163 = var2.readUnsignedShort();
         } else if (var1 == 2) {
            this.anInt3160 = var2.readUnsignedShort();
         } else if (var1 == 3) {
            this.anInt3165 = var2.readUnsignedShort();
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "gm.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static void method236() {
      try {
          TextureOperation14.aBoolean3387 = true;
      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "gm.C(" + (byte) 64 + ')');
      }
   }

   static final int[] NPC_RENDER_LOG = new int[3];
   
   static void renderNPCs(int var0) {
      try {
         Unsorted.maskUpdateCount = 0;
         Class139.anInt1829 = 0;
         Class24.renderLocalNPCs();
         NPC_RENDER_LOG[0] = GraphicDefinition.incomingBuffer.index;
         Class167.addLocalNPCs();
         NPC_RENDER_LOG[1] = GraphicDefinition.incomingBuffer.index;
         NPC.renderNPCMasks(var0 ^ 8106);
         NPC_RENDER_LOG[2] = GraphicDefinition.incomingBuffer.index;

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

         if(Unsorted.incomingPacketLength == GraphicDefinition.incomingBuffer.index) {
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

   final int[] method154(int var1, byte var2) {
      try {
         int[] var3 = this.aClass114_2382.method1709(var1);
         if(this.aClass114_2382.aBoolean1580) {
            for(int var5 = 0; var5 < Class113.anInt1559; ++var5) {
               int var7 = this.anInt3165 + (Class163_Sub3.anIntArray2999[var1] << 12) / this.anInt3164;
               int var6 = this.anInt3160 + (Class102.anIntArray2125[var5] << 12) / this.anInt3164;
               int var10 = var6;
               int var11 = var7;
               int var14 = 0;
               int var12 = var6 * var6 >> 12;

               for(int var13 = var7 * var7 >> 12; var12 - -var13 < 16384 && var14 < this.anInt3163; var12 = var10 * var10 >> 12) {
                  var11 = (var10 * var11 >> 12) * 2 + var7;
                  ++var14;
                  var10 = var12 + -var13 + var6;
                  var13 = var11 * var11 >> 12;
               }

               var3[var5] = this.anInt3163 + -1 <= var14 ?0:(var14 << 12) / this.anInt3163;
            }
         }

         return var3;
      } catch (RuntimeException var15) {
         throw ClientErrorException.clientError(var15, "gm.D(" + var1 + ',' + var2 + ')');
      }
   }

   public TextureOperation31() {
      super(0, true);
   }

}

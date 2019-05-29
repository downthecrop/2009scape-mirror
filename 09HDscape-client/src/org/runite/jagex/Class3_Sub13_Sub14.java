package org.runite.jagex;
import java.math.BigInteger;

final class Class3_Sub13_Sub14 extends Class3_Sub13 {

   static int anInt3158 = -8 + (int)(17.0D * Math.random());
   static Class73 aClass73_3159;
   private int anInt3160 = 0;
   static RSString aClass94_3161 = RSString.createRSString("_");
   static BigInteger aBigInteger3162 = new BigInteger("58778699976184461502525193738213253649000149147835990136706041084440742975821");
   private int anInt3163 = 20;
   private int anInt3164 = 1365;
   private int anInt3165 = 0;
   static boolean aBoolean3166 = false;

   static RSString aClass94_3168 = RSString.createRSString("cross");
   static RSString aClass94_3169 = RSString.createRSString("Lade Sprites )2 ");
   private static RSString aClass94_3170 = RSString.createRSString("Loaded textures");
   static int[] anIntArray3171 = new int[]{0, 4, 4, 8, 0, 0, 8, 0, 0};
   static RSString aClass94_3172 = RSString.createRSString("Regarder dans cette direction");
   static CacheIndex aClass153_3173;
   static RSString aClass94_3167 = aClass94_3170;

   final void method157(int var1, RSByteBuffer var2, boolean var3) {
      try {
         if(!var3) {
            aClass94_3168 = (RSString)null;
         }

         if(-1 != ~var1) {
            if(-2 != ~var1) {
               if(~var1 != -3) {
                  if(var1 == 3) {
                     this.anInt3165 = var2.getShort(1);
                  }
               } else {
                  this.anInt3160 = var2.getShort(1);
               }
            } else {
               this.anInt3163 = var2.getShort(1);
            }
         } else {
            this.anInt3164 = var2.getShort(1);
         }

      } catch (RuntimeException var5) {
         throw Class44.method1067(var5, "gm.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   static final void method236(byte var0) {
      try {
         if(var0 == 64) {
            Class3_Sub13_Sub32.aBoolean3387 = true;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gm.C(" + var0 + ')');
      }
   }

   static final int[] NPC_RENDER_LOG = new int[3];
   
   static final void renderNPCs(int var0) {
      try {
         Class66.maskUpdateCount = 0;
         Class139.anInt1829 = 0;
         Class24.renderLocalNPCs((byte)-11);
         NPC_RENDER_LOG[0] = GraphicDefinition.incomingBuffer.index;
         Class167.addLocalNPCs(113);
         NPC_RENDER_LOG[1] = GraphicDefinition.incomingBuffer.index;
         Class75_Sub4.renderNPCMasks(var0 ^ 8106);
         NPC_RENDER_LOG[2] = GraphicDefinition.incomingBuffer.index;

         int var1;
         for(var1 = 0; ~var1 > ~Class139.anInt1829; ++var1) {
            int var2 = Class3_Sub7.anIntArray2292[var1];
            if(~Class3_Sub13_Sub24.npcs[var2].anInt2838 != ~Class44.anInt719) {
               if(Class3_Sub13_Sub24.npcs[var2].definition.method1474(-1)) {
                  Class3_Sub28_Sub8.method574(Class3_Sub13_Sub24.npcs[var2], false);
               }

               Class3_Sub13_Sub24.npcs[var2].setDefinitions(-1, (NPCDefinition)null);
               Class3_Sub13_Sub24.npcs[var2] = null;
            }
         }

         if(var0 != 8169) {
            renderNPCs(96);
         }

         if(Class130.incomingPacketLength == GraphicDefinition.incomingBuffer.index) {
            for(var1 = 0; var1 < Class163.localNPCCount; ++var1) {
               if(null == Class3_Sub13_Sub24.npcs[Class15.localNPCIndexes[var1]]) {
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
         throw Class44.method1067(var3, "gm.B(" + var0 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int var4 = -72 % ((30 - var2) / 36);
         int[] var3 = this.aClass114_2382.method1709(-16409, var1);
         if(this.aClass114_2382.aBoolean1580) {
            for(int var5 = 0; ~Class113.anInt1559 < ~var5; ++var5) {
               int var7 = this.anInt3165 + (Class163_Sub3.anIntArray2999[var1] << 12) / this.anInt3164;
               int var6 = this.anInt3160 + (Class102.anIntArray2125[var5] << 12) / this.anInt3164;
               int var8 = var6;
               int var10 = var6;
               int var9 = var7;
               int var11 = var7;
               int var14 = 0;
               int var12 = var6 * var6 >> 12;

               for(int var13 = var7 * var7 >> 12; ~(var12 - -var13) > -16385 && ~this.anInt3163 < ~var14; var12 = var10 * var10 >> 12) {
                  var11 = (var10 * var11 >> 12) * 2 + var9;
                  ++var14;
                  var10 = var12 + -var13 + var8;
                  var13 = var11 * var11 >> 12;
               }

               var3[var5] = ~var14 <= ~(this.anInt3163 + -1)?0:(var14 << 12) / this.anInt3163;
            }
         }

         return var3;
      } catch (RuntimeException var15) {
         throw Class44.method1067(var15, "gm.D(" + var1 + ',' + var2 + ')');
      }
   }

   public static void method238(int var0) {
      try {
         if(var0 == 9423) {
            aBigInteger3162 = null;
            aClass94_3169 = null;
            anIntArray3171 = null;
            aClass73_3159 = null;
            aClass153_3173 = null;
            aClass94_3168 = null;
            aClass94_3167 = null;
            aClass94_3161 = null;
            aClass94_3170 = null;
            aClass94_3172 = null;
         }
      } catch (RuntimeException var2) {
         throw Class44.method1067(var2, "gm.E(" + var0 + ')');
      }
   }

   public Class3_Sub13_Sub14() {
      super(0, true);
   }

}

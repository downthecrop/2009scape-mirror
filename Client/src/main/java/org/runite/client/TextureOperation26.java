package org.runite.client;

import org.rs09.client.config.GameConfig;

final class TextureOperation26 extends TextureOperation {

   private int anInt3073 = 0;
   private int anInt3074 = 4096;
   static Class61 aClass61_3075 = new Class61();
   static boolean aBoolean3078;
   static int anInt3081 = 0;


   static void method195() {
      try {
         int regionX = (NPC.anInt3995 >> 10) - -(Class131.anInt1716 >> 3);
         int regionY = (Class77.anInt1111 >> 10) - -(Texture.anInt1152 >> 3);
         byte plane = 0;
         byte sceneX = 8;
         byte var6 = 18;
         Class3_Sub22.aByteArrayArray2521 = new byte[var6][];
         Class3_Sub28_Sub5.anIntArray3587 = new int[var6];
         TextureOperation35.aByteArrayArray3335 = new byte[var6][];
         Client.anIntArray2200 = new int[var6];
         Class39.regionXteaKeys = new int[var6][4];
         Class40.aByteArrayArray3669 = new byte[var6][];
         Class3_Sub24_Sub3.anIntArray3494 = new int[var6];
         Class164_Sub2.aByteArrayArray3027 = new byte[var6][];
         NPC.npcSpawnCacheIndices = new int[var6];
         TextureOperation17.anIntArray3181 = new int[var6];
         Class101.anIntArray1426 = new int[var6];
         byte sceneY = 8;
         Class40.aByteArrayArray3057 = new byte[var6][];
         int var11 = 0;

         int var7;
         for(var7 = (-6 + regionX) / 8; (6 + regionX) / 8 >= var7; ++var7) {
            for(int var8 = (-6 + regionY) / 8; var8 <= (regionY + 6) / 8; ++var8) {
               int var9 = (var7 << 8) - -var8;
               Class3_Sub24_Sub3.anIntArray3494[var11] = var9;

               /**
                * This block is used to control what is displayed on the HD login screen fly over
                * 1. Gets the archives for the map
                * 2. Gets the archives for the landscape
                * 3. Gets the archives for the NPC spawns
                * 4. Gets the archives for the map underlays (this would be like water for example
                * 5. Gets the archives for the landscape underlays (things that (appear to be/are) -1 on the plane)
                */
               Client.anIntArray2200[var11] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("m"), RSString.stringAnimator(var7), RSString.parse("_"), RSString.stringAnimator(var8)}));
               if (GameConfig.HD_LOGIN_DEBUG) {
                  System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting archive for map: " + Client.anIntArray2200[var11]);
                  if (GameConfig.HD_LOGIN_VERBOSE) {
                     System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting specific info for map: " + "Type: " + RSString.parse("m") + " Place in cache: " + RSString.stringAnimator(var7) + RSString.parse("_") + RSString.stringAnimator(var8));
                  }
               }



               Class101.anIntArray1426[var11] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("l"), RSString.stringAnimator(var7), RSString.parse("_"), RSString.stringAnimator(var8)}));
               if (GameConfig.HD_LOGIN_DEBUG) {
                  System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting archive for landscape: " + Class101.anIntArray1426[var11]);
                  if (GameConfig.HD_LOGIN_VERBOSE) {
                     System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting specific info for landscape: " + "Type: " + RSString.parse("l") + " Place in cache: " + RSString.stringAnimator(var7) + RSString.parse("_") + RSString.stringAnimator(var8));
                  }
               }


               NPC.npcSpawnCacheIndices[var11] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("n"), RSString.stringAnimator(var7), RSString.parse("_"), RSString.stringAnimator(var8)}));
               if (GameConfig.HD_LOGIN_DEBUG) {
                  System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting archive for packed NPCs: " + NPC.npcSpawnCacheIndices[var11]);
                  if (GameConfig.HD_LOGIN_VERBOSE) {
                     System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting specific info for packed NPCs: " + "Type: " + RSString.parse("n") + " Place in cache: " + RSString.stringAnimator(var7) + RSString.parse("_") + RSString.stringAnimator(var8));
                  }
               }


               TextureOperation17.anIntArray3181[var11] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("um"), RSString.stringAnimator(var7), RSString.parse("_"), RSString.stringAnimator(var8)}));
               if (GameConfig.HD_LOGIN_DEBUG) {
                  System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting archive for map underlays: " + TextureOperation17.anIntArray3181[var11]);
                  if (GameConfig.HD_LOGIN_VERBOSE) {
                     System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting specific info for map underlays: " + "Type: " + RSString.parse("um") + " Place in cache: " + RSString.stringAnimator(var7) + RSString.parse("_") + RSString.stringAnimator(var8));
                  }
               }


               Class3_Sub28_Sub5.anIntArray3587[var11] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("ul"), RSString.stringAnimator(var7), RSString.parse("_"), RSString.stringAnimator(var8)}));
               if (GameConfig.HD_LOGIN_DEBUG) {
                  System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting archive for landscape underlays: " + Class3_Sub28_Sub5.anIntArray3587[var11]);
                  if (GameConfig.HD_LOGIN_VERBOSE) {
                     System.out.println("Class3_Sub13_sub6: HD Login Screen Debug: Getting specific info for landscape underlays: " + "Type: " + RSString.parse("ul") + " Place in cache: " + RSString.stringAnimator(var7) + RSString.parse("_") + RSString.stringAnimator(var8));
                  }
               }
               /* End login screen index lookup */


                  if(NPC.npcSpawnCacheIndices[var11] == -1) {
                  Client.anIntArray2200[var11] = -1;
                  Class101.anIntArray1426[var11] = -1;
                  TextureOperation17.anIntArray3181[var11] = -1;
                  Class3_Sub28_Sub5.anIntArray3587[var11] = -1;
               }

               ++var11;
            }
         }

         for(var7 = var11; NPC.npcSpawnCacheIndices.length > var7; ++var7) {
            NPC.npcSpawnCacheIndices[var7] = -1;
            Client.anIntArray2200[var7] = -1;
            Class101.anIntArray1426[var7] = -1;
            TextureOperation17.anIntArray3181[var7] = -1;
            Class3_Sub28_Sub5.anIntArray3587[var7] = -1;
         }

         Unsorted.method1301(plane, regionY, regionX, sceneY, true, sceneX);
      } catch (RuntimeException var10) {
         throw ClientErrorException.clientError(var10, "ca.F(" + 20479 + ')');
      }
   }

   final int[] method154(int var1, byte var2) {
      try {
         int[] var4 = this.aClass114_2382.method1709(var1);
         if(this.aClass114_2382.aBoolean1580) {
            int[] var5 = this.method152(0, var1, 32755);

            for(int var6 = 0; var6 < Class113.anInt1559; ++var6) {
               int var7 = var5[var6];
               var4[var6] = var7 >= this.anInt3073 && this.anInt3074 >= var7 ?4096:0;
            }
         }

         return var4;
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ca.D(" + var1 + ',' + var2 + ')');
      }
   }

   static void method196() {
      try {
         Class3_Sub8.anIntArray3083 = null;

      } catch (RuntimeException var2) {
         throw ClientErrorException.clientError(var2, "ca.B(" + ')');
      }
   }

   final void method157(int var1, DataBuffer var2, boolean var3) {
      try {
         if(!var3) {
            method196();
         }

         if(var1 == 0) {
            this.anInt3073 = var2.readUnsignedShort();
         } else if (1 == var1) {
            this.anInt3074 = var2.readUnsignedShort();
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ca.A(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
      }
   }

   public TextureOperation26() {
      super(1, true);
   }

   static void method198(boolean var0) {
      try {

         int var3 = Class164_Sub2.aByteArrayArray3027.length;
         byte[][] var2;
         if(HDToolKit.highDetail && var0) {
            var2 = Class40.aByteArrayArray3057;
         } else {
            var2 = Class3_Sub22.aByteArrayArray2521;
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            byte[] var5 = var2[var4];
            if(var5 != null) {
               int var6 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var4] >> 8);
               int var7 = (Class3_Sub24_Sub3.anIntArray3494[var4] & 0xFF) * 64 + -Texture.anInt1152;
               Class58.method1194();
               Class3_Sub15.method374(var6, var0, var5, var7, AtmosphereParser.aClass91Array1182);
            }
         }

      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "ca.E(" + var0 + ',' + -32624 + ')');
      }
   }

   static void method199(int var0, int var1, int var2) {
      try {

         if(CS2Script.anInt2453 != 0 && var0 != 0 && Class113.anInt1552 < 50 && var1 != -1) {
            Class3_Sub25.anIntArray2550[Class113.anInt1552] = var1;
            Class166.anIntArray2068[Class113.anInt1552] = var0;
            Unsorted.anIntArray2157[Class113.anInt1552] = var2;
            Class102.aClass135Array2131[Class113.anInt1552] = null;
            Class3_Sub8.anIntArray3083[Class113.anInt1552] = 0;
            ++Class113.anInt1552;
         }

      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "ca.C(" + var0 + ',' + var1 + ',' + var2 + ',' + -799 + ')');
      }
   }

}

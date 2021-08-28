package org.runite.client;


import org.rs09.SystemLogger;
import org.rs09.client.Linkable;
import org.rs09.client.config.GameConfig;
import org.rs09.client.data.HashTable;
import org.rs09.client.LinkableInt;

import java.util.Objects;

public final class NPCDefinition {

   static SequenceDefinition[] aClass142Array1168 = new SequenceDefinition[14];
    static Class3_Sub28_Sub5[] aClass3_Sub28_Sub5Array3041 = new Class3_Sub28_Sub5[14];
    static CacheIndex aClass153_3173;
    static Class3_Sub28_Sub5[] aClass3_Sub28_Sub5Array4031 = new Class3_Sub28_Sub5[14];
    static int[] anIntArray912 = new int[14];
    int size = 1;
   private short[] aShortArray1246;
   private byte[] aByteArray1247;
   private short[] aShortArray1248;
   boolean aBoolean1249;
   private int[] anIntArray1250;
   private int anInt1251;
   static int anInt1252 = -1;
   int anInt1253;
   private short[] aShortArray1254;
   boolean aBoolean1255 = true;
   short aShort1256 = 0;
   private int configId;
   private int[][] anIntArrayArray1258;
   public RSString[] options;
   int anInt1260;
   private int[][] anIntArrayArray1261;
   int anInt1262 = -1;
   boolean aBoolean1263;
   private int anInt1264;
   int anInt1265;
   private int anInt1266;
   byte aByte1267;
   byte aByte1268;
   int anInt1269;
   public boolean aBoolean1270 = true;
   private short[] aShortArray1271;
   private HashTable aHashTable_1272;
   RSString NPCName;
   int anInt1274;
   byte aByte1275;
   int anInt1276 = -1;
   static int[] anIntArray1277 = new int[2000];
   int anInt1278;
   int anInt1279;
   int renderAnimationId;
   private int anInt1282;
   public int anInt1283;
   public int npcId;
   public boolean aBoolean1285;
   short aShort1286;
   byte aByte1287;
   private int[] models;
   int anInt1289;
   int anInt1290;
   int anInt1291;
   public int[] childNPCs;
   int anInt1293;
   private int configFileId;
   int anInt1296;
   static int anInt1297;
   int anInt1298;

   static NPCDefinition getNPCDefinition(int npcID) {
       try {
           NPCDefinition def = (NPCDefinition) Unsorted.aReferenceCache_4043.get(npcID);
           if (null == def) {
               byte[] var3 = Class29.aClass153_557.getFile(Class38_Sub1.method1031(npcID), Unsorted.method54(npcID));
               def = new NPCDefinition();

               def.npcId = npcID;
               if (null != var3) {
                   def.method1478(new DataBuffer(var3));
               }
               Unsorted.aReferenceCache_4043.put(def, npcID);
           }
           return def;
       } catch (RuntimeException var4) {
          SystemLogger.logErr("Unable to parse NPC definition for ID " + npcID + " trace: " + var4);
       }
       return null;
   }

   public final NPCDefinition method1471(byte var1) {
      try {
         int var2 = -1;
         if(this.configId == -1) {
            if(this.configFileId != -1) {
               var2 = ItemDefinition.ram[this.configFileId];
            }
         } else {
            var2 = method1484(this.configId);
         }

         int var3;
         if(0 <= var2 && -1 + this.childNPCs.length > var2 && this.childNPCs[var2] != -1) {
             return getNPCDefinition(this.childNPCs[var2]);
         } else {
            var3 = this.childNPCs[-1 + this.childNPCs.length];
            return var3 == -1 ?null: getNPCDefinition(var3);
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "me.G(" + var1 + ')');
      }
   }

   final boolean method1472() {
      try {
         if(null == this.childNPCs) {
            return true;
         } else {
            int var2 = -1;
            if(-1 == this.configId) {
               if(this.configFileId != -1) {
                  var2 = ItemDefinition.ram[this.configFileId];
               }
            } else {
               var2 = method1484(this.configId);
            }

            if(var2 >= 0 && var2 < -1 + this.childNPCs.length && -1 != this.childNPCs[var2]) {
               return true;
            } else {
               int var3 = this.childNPCs[-1 + this.childNPCs.length];
               return var3 != -1;
            }
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "me.L(" + (byte) 74 + ')');
      }
   }

   final boolean method1474() {
      try {

         if(this.childNPCs == null) {
            return -1 != this.anInt1262 || this.anInt1293 != -1 || this.anInt1276 != -1;
         } else {
            for(int var2 = 0; var2 < this.childNPCs.length; ++var2) {
               if(this.childNPCs[var2] != -1) {
                  NPCDefinition var3 = getNPCDefinition(this.childNPCs[var2]);
                  if(var3.anInt1262 != -1 || var3.anInt1293 != -1 || var3.anInt1276 != -1) {
                     return true;
                  }
               }
            }

            return false;
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "me.E(" + -1 + ')');
      }
   }

   final int method1475(int var1, int var3) {
      try {
         if(null == this.aHashTable_1272) {
            return var3;
         } else {
            LinkableInt var4 = (LinkableInt)this.aHashTable_1272.get(var1);
            return var4 != null?var4.value :var3;
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "me.N(" + var1 + ',' + -26460 + ',' + var3 + ')');
      }
   }

   final Model method1476(Class145[] var1, int var2, byte var3, int var4, int var5, int var6, int var7, SequenceDefinition var8, int var9, SequenceDefinition var10) {
      try {
         if(this.childNPCs == null) {
            Model var11 = (Model)CS2Script.aReferenceCache_2442.get(this.npcId);
            boolean var12;
            int var17;
            int var16;
            int var19;
            int var18;
            int var21;
            int var20;
            int var22;
            int var24;
            int var27;
            int var29;
            int var28;
            if(null == var11) {
               var12 = false;

               for (int model : this.models) {
                  if (model != -1 && !aClass153_3173.method2129((byte) 102, 0, model)) {
                     var12 = true;
                  }
               }

               if(var12) {
                  return null;
               }

               Model_Sub1[] var14 = new Model_Sub1[this.models.length];

               for(int var15 = 0; var15 < this.models.length; ++var15) {
                  if(this.models[var15] != -1) {
                     var14[var15] = Model_Sub1.method2015(aClass153_3173, this.models[var15]);
                     if(null != this.anIntArrayArray1261 && this.anIntArrayArray1261[var15] != null && var14[var15] != null) {
                        var14[var15].method2001(this.anIntArrayArray1261[var15][0], this.anIntArrayArray1261[var15][1], this.anIntArrayArray1261[var15][2]);
                     }
                  }
               }

               RenderAnimationDefinition render = null;
               if(-1 != this.renderAnimationId) {
                  render = Class3_Sub10.getRenderAnimationDefinition(this.renderAnimationId);
               }

               if(render != null && null != render.equipment_transforms) {
                  for(var16 = 0; var16 < render.equipment_transforms.length; ++var16) {
                     if(render.equipment_transforms[var16] != null && var14.length > var16 && var14[var16] != null) {
                        var19 = render.equipment_transforms[var16][2];
                        var20 = render.equipment_transforms[var16][3];
                        var21 = render.equipment_transforms[var16][4];
                        var18 = render.equipment_transforms[var16][1];
                        var22 = render.equipment_transforms[var16][5];
                        var17 = render.equipment_transforms[var16][0];
                        if(this.anIntArrayArray1258 == null) {
                           this.anIntArrayArray1258 = new int[render.equipment_transforms.length][];
                        }

                        if(null == this.anIntArrayArray1258[var16]) {
                           int[] var23 = this.anIntArrayArray1258[var16] = new int[15];
                           if(var20 == 0 && var21 == 0 && var22 == 0) {
                              var23[13] = -var18;
                              var23[14] = -var19;
                              var23[0] = var23[4] = var23[8] = 32768;
                              var23[12] = -var17;
                           } else {
                              var24 = Class51.anIntArray851[var20] >> 1;
                              int var25 = Class51.anIntArray840[var20] >> 1;
                              int var26 = Class51.anIntArray851[var21] >> 1;
                              var28 = Class51.anIntArray851[var22] >> 1;
                              var27 = Class51.anIntArray840[var21] >> 1;
                              var29 = Class51.anIntArray840[var22] >> 1;
                              var23[3] = var24 * var29 - -16384 >> 15;
                              var23[8] = 16384 + var24 * var26 >> 15;
                              var23[5] = -var25;
                              int var31 = 16384 + var25 * var29 >> 15;
                              int var30 = var28 * var25 + 16384 >> 15;
                              var23[1] = 16384 + -var29 * var26 - -(var30 * var27) >> 15;
                              var23[2] = 16384 + var27 * var24 >> 15;
                              var23[6] = 16384 + -var27 * var28 + var31 * var26 >> 15;
                              var23[14] = 16384 + var23[8] * -var19 + -var18 * var23[5] + var23[2] * -var17 >> 15;
                              var23[4] = 16384 + var24 * var28 >> 15;
                              var23[7] = 16384 + -var27 * -var29 + var30 * var26 >> 15;
                              var23[0] = var27 * var31 + var26 * var28 + 16384 >> 15;
                              var23[12] = 16384 + var23[6] * -var19 + var23[3] * -var18 + -var17 * var23[0] >> 15;
                              var23[13] = -var19 * var23[7] + -var17 * var23[1] + (-var18 * var23[4] - -16384) >> 15;
                           }

                           var23[10] = var18;
                           var23[9] = var17;
                           var23[11] = var19;
                        }

                        if(var20 != 0 || var21 != 0 || var22 != 0) {
                           var14[var16].method2013(var20, var21, var22);
                        }

                        if(var17 != 0 || 0 != var18 || var19 != 0) {
                           var14[var16].method2001(var17, var18, var19);
                        }
                     }
                  }
               }

               Model_Sub1 var34;
               if(var14.length == 1) {
                  var34 = var14[0];
               } else {
                  var34 = new Model_Sub1(var14, var14.length);
               }

               if(this.aShortArray1248 != null) {
                  for(var16 = 0; var16 < this.aShortArray1248.length; ++var16) {
                     if(null != this.aByteArray1247 && var16 < this.aByteArray1247.length) {
                        Objects.requireNonNull(var34).method2016(this.aShortArray1248[var16], Class136.aShortArray1779[this.aByteArray1247[var16] & 0xFF]);
                     } else {
                        Objects.requireNonNull(var34).method2016(this.aShortArray1248[var16], this.aShortArray1254[var16]);
                     }
                  }
               }

               if(null != this.aShortArray1271) {
                  for(var16 = 0; this.aShortArray1271.length > var16; ++var16) {
                     Objects.requireNonNull(var34).method1998(this.aShortArray1271[var16], this.aShortArray1246[var16]);
                  }
               }

               var11 = Objects.requireNonNull(var34).method2008(this.anInt1251 + 64, this.anInt1282 + 850, -30, -50, -30);
               if(HDToolKit.highDetail) {
                  ((Class140_Sub1_Sub1)var11).method1920(false, false, false, false, false, true);
               }

               CS2Script.aReferenceCache_2442.put(var11, this.npcId);
            }

            var12 = false;
            boolean var37 = false;
            boolean var35 = false;
            boolean var36 = false;
            var16 = null != var1?var1.length:0;
            for(var17 = 0; var17 < var16; ++var17) {
               if(var1[var17] != null) {
                  SequenceDefinition def = SequenceDefinition.getAnimationDefinition(var1[var17].animationId);
                  if(null != def.frames) {
                     aClass142Array1168[var17] = def;
                     var20 = var1[var17].anInt1891;
                     var12 = true;
                     var19 = var1[var17].anInt1893;
                     var21 = def.frames[var19];
                     aClass3_Sub28_Sub5Array3041[var17] = Class3_Sub9.method133(var21 >>> 16);
                     var21 &= 65535;
                     anIntArray912[var17] = var21;
                     if(aClass3_Sub28_Sub5Array3041[var17] != null) {
                        var35 |= aClass3_Sub28_Sub5Array3041[var17].method561(var21, (byte)124);
                        var37 |= aClass3_Sub28_Sub5Array3041[var17].method559(var21);
                        var36 |= def.aBoolean1848;
                     }

                     if((def.aBoolean1846 || ClientCommands.tweeningEnabled) && var20 != -1 && var20 < def.frames.length) {
                        Class38.anIntArray664[var17] = def.duration[var19];
                        Unsorted.anIntArray2574[var17] = var1[var17].anInt1897;
                        var22 = def.frames[var20];
                        aClass3_Sub28_Sub5Array4031[var17] = Class3_Sub9.method133(var22 >>> 16);
                        var22 &= 65535;
                        Class30.anIntArray574[var17] = var22;
                        if(null != aClass3_Sub28_Sub5Array4031[var17]) {
                           var35 |= aClass3_Sub28_Sub5Array4031[var17].method561(var22, (byte)124);
                           var37 |= aClass3_Sub28_Sub5Array4031[var17].method559(var22);
                        }
                     } else {
                        Class38.anIntArray664[var17] = 0;
                        Unsorted.anIntArray2574[var17] = 0;
                        aClass3_Sub28_Sub5Array4031[var17] = null;
                        Class30.anIntArray574[var17] = -1;
                     }
                  }
               }
            }
            if(!var12 && null == var10 && var8 == null) {
               Model var41 = var11.method1894(true, true, true);
               if(this.anInt1264 != 128 || this.anInt1266 != 128) {
                  var41.resize(this.anInt1264, this.anInt1266, this.anInt1264);
               }

               return var41;
            } else {
               var18 = -1;
               var17 = -1;
               var19 = 0;
               Class3_Sub28_Sub5 var40 = null;
               Class3_Sub28_Sub5 var43 = null;
               int var42;
               if(var10 != null) {
                  var17 = var10.frames[var7];
                  var22 = var17 >>> 16;
                  var17 &= 65535;
                  var40 = Class3_Sub9.method133(var22);
                  if(null != var40) {
                     var35 |= var40.method561(var17, (byte)126);
                     var37 |= var40.method559(var17);
                     var36 |= var10.aBoolean1848;
                  }

                  if((var10.aBoolean1846 || ClientCommands.tweeningEnabled) && var5 != -1 && var5 < var10.frames.length) {
                     var19 = var10.duration[var7];
                     var18 = var10.frames[var5];
                     var42 = var18 >>> 16;
                     var18 &= 65535;
                     if(var22 == var42) {
                        var43 = var40;
                     } else {
                        var43 = Class3_Sub9.method133(var18 >>> 16);
                     }

                     if(var43 != null) {
                        var35 |= var43.method561(var18, (byte)115);
                        var37 |= var43.method559(var18);
                     }
                  }
               }

               var22 = -1;
               if(var3 > -63) {
                  this.parseOpcode(79, 73, null);
               }

               var42 = -1;
               Class3_Sub28_Sub5 var44 = null;
               var24 = 0;
               Class3_Sub28_Sub5 var46 = null;
               if(var8 != null) {
                  var22 = var8.frames[var4];
                  var27 = var22 >>> 16;
                  var22 &= 65535;
                  var44 = Class3_Sub9.method133(var27);
                  if(var44 != null) {
                     var35 |= var44.method561(var22, (byte)124);
                     var37 |= var44.method559(var22);
                     var36 |= var8.aBoolean1848;
                  }

                  if((var8.aBoolean1846 || ClientCommands.tweeningEnabled) && var2 != -1 && var2 < var8.frames.length) {
                     var24 = var8.duration[var4];
                     var42 = var8.frames[var2];
                     var28 = var42 >>> 16;
                     var42 &= 65535;
                     if(var27 == var28) {
                        var46 = var44;
                     } else {
                        var46 = Class3_Sub9.method133(var42 >>> 16);
                     }

                     if(null != var46) {
                        var35 |= var46.method561(var42, (byte)124);
                        var37 |= var46.method559(var42);
                     }
                  }
               }

               Model var45 = var11.method1894(!var37, !var35, !var36);
               var29 = 1;

               for(var28 = 0; var28 < var16; ++var28) {
                  if(aClass3_Sub28_Sub5Array3041[var28] != null) {
                     var45.method1887(aClass3_Sub28_Sub5Array3041[var28], anIntArray912[var28], aClass3_Sub28_Sub5Array4031[var28], Class30.anIntArray574[var28], -1 + Unsorted.anIntArray2574[var28], Class38.anIntArray664[var28], var29, aClass142Array1168[var28].aBoolean1848, this.anIntArrayArray1258[var28]);
                  }

                  var29 <<= 1;
               }

               if(var40 != null && var44 != null) {
                  var45.method1892(var40, var17, var43, var18, var6 + -1, var19, var44, var22, var46, var42, var9 + -1, var24, var10.aBooleanArray1855, var10.aBoolean1848 | var8.aBoolean1848);
               } else if(var40 == null) {
                  if(null != var44) {
                     var45.method1880(var44, var22, var46, var42, -1 + var9, var24, var8.aBoolean1848);
                  }
               } else {
                  var45.method1880(var40, var17, var43, var18, var6 + -1, var19, var10.aBoolean1848);
               }

               for(var28 = 0; var16 > var28; ++var28) {
                  aClass3_Sub28_Sub5Array3041[var28] = null;
                  aClass3_Sub28_Sub5Array4031[var28] = null;
                  aClass142Array1168[var28] = null;
               }

               if(this.anInt1264 != 128 || this.anInt1266 != 128) {
                  var45.resize(this.anInt1264, this.anInt1266, this.anInt1264);
               }

               return var45;
            }
         } else {
            NPCDefinition var33 = this.method1471((byte)32);
            return null != var33?var33.method1476(var1, var2, (byte)-102, var4, var5, var6, var7, var8, var9, var10):null;
         }
      } catch (RuntimeException var32) {
         throw ClientErrorException.clientError(var32, "me.M(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + (var8 != null?"{...}":"null") + ',' + var9 + ',' + (var10 != null?"{...}":"null") + ')');
      }
   }

   final RSString method1477(int var1, RSString var2) {
      try {
         if(null == this.aHashTable_1272) {
            return var2;
         } else {
            LinkableRSString var4 = (LinkableRSString)this.aHashTable_1272.get(var1);
            return (null == var4?var2:var4.value);
         }
      } catch (RuntimeException var5) {
         throw ClientErrorException.clientError(var5, "me.I(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + true + ')');
      }
   }

   final void method1478(DataBuffer var1) {
      try {
         while(true) {
            int var3 = var1.readUnsignedByte();
            if(var3 == 0) {
               return;
            }

            this.parseOpcode(27, var3, var1);
         }
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "me.F(" + (var1 != null?"{...}":"null") + ',' + 74 + ')');
      }
   }

   static void method1479(int var0) {
      try {
         TextureOperation13.anInt3362 = -1;

         if(var0 == 37) {
            NPC.aFloat3979 = 3.0F;
         } else if(50 == var0) {
            NPC.aFloat3979 = 4.0F;
         } else if (var0 == 75) {
            NPC.aFloat3979 = 6.0F;
         } else if (var0 == 100) {
            NPC.aFloat3979 = 8.0F;
         } else if (var0 == 200) {
            NPC.aFloat3979 = 16.0F;
         }

      } catch (RuntimeException var3) {
         throw ClientErrorException.clientError(var3, "me.C(" + var0 + ',' + (byte) 56 + ')');
      }
   }

   static void method1480(boolean var0, RSString var1) {
      try {
         short[] var3 = new short[16];
         var1 = var1.toLowercase();
         int var4 = 0;

         for(int var5 = 0; TextureOperation39.itemDefinitionSize > var5; ++var5) {
            ItemDefinition var6 = ItemDefinition.getItemDefinition(var5);
            if((!var0 || var6.aBoolean807) && var6.anInt791 == -1 && -1 == var6.anInt762 && var6.anInt800 == 0 && var6.name.toLowercase().indexOf(var1, 116) != -1) {
               if(var4 >= 250) {
                  Class99.aShortArray1398 = null;
                  Unsorted.anInt952 = -1;
                  return;
               }

               if(var3.length <= var4) {
                  short[] var7 = new short[2 * var3.length];

                  System.arraycopy(var3, 0, var7, 0, var4);

                  var3 = var7;
               }

               var3[var4++] = (short)var5;
            }
         }

         Class99.aShortArray1398 = var3;
         Class140_Sub4.anInt2756 = 0;
         Unsorted.anInt952 = var4;
         RSString[] var10 = new RSString[Unsorted.anInt952];

         for(int var11 = 0; Unsorted.anInt952 > var11; ++var11) {
            var10[var11] = ItemDefinition.getItemDefinition(var3[var11]).name;
         }

         TextureOperation3.method307(var10, Class99.aShortArray1398, 77);
      } catch (RuntimeException var9) {
         throw ClientErrorException.clientError(var9, "me.J(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + 102 + ')');
      }
   }

   final Model getChatModel(SequenceDefinition var1, int var2, int var3, int var4, int var5) {
      try {
         if(this.childNPCs == null) {
            if(null == this.anIntArray1250) {
               return null;
            } else {
               Model var12 = (Model)Class154.aReferenceCache_1964.get(this.npcId);
               if(var12 == null) {
                  boolean var7 = false;

                  for(int var8 = 0; this.anIntArray1250.length > var8; ++var8) {
                     if(!aClass153_3173.method2129((byte)-69, 0, this.anIntArray1250[var8])) {
                        var7 = true;
                     }
                  }

                  if(var7) {
                     return null;
                  }

                  Model_Sub1[] var14 = new Model_Sub1[this.anIntArray1250.length];

                  for(int var9 = 0; this.anIntArray1250.length > var9; ++var9) {
                     var14[var9] = Model_Sub1.method2015(aClass153_3173, this.anIntArray1250[var9]);
                  }

                  Model_Sub1 var15;
                  if(var14.length == 1) {
                     var15 = var14[0];
                  } else {
                     var15 = new Model_Sub1(var14, var14.length);
                  }

                  int var10;
                  if(null != this.aShortArray1248) {
                     for(var10 = 0; var10 < this.aShortArray1248.length; ++var10) {
                        if(this.aByteArray1247 != null && this.aByteArray1247.length > var10) {
                           Objects.requireNonNull(var15).method2016(this.aShortArray1248[var10], Class136.aShortArray1779[255 & this.aByteArray1247[var10]]);
                        } else {
                           Objects.requireNonNull(var15).method2016(this.aShortArray1248[var10], this.aShortArray1254[var10]);
                        }
                     }
                  }

                  if(this.aShortArray1271 != null) {
                     for(var10 = 0; this.aShortArray1271.length > var10; ++var10) {
                        Objects.requireNonNull(var15).method1998(this.aShortArray1271[var10], this.aShortArray1246[var10]);
                     }
                  }

                  var12 = Objects.requireNonNull(var15).method2008(64, 768, -50, -10, -50);
                  Class154.aReferenceCache_1964.put(var12, this.npcId);
               }

               if(null != var1) {
                  var12 = var1.method2055(var12, (byte)-75, var3, var2, var5);
               }

               return var12;
            }
         } else {
            NPCDefinition var6 = this.method1471((byte)-100);
            return null == var6?null:var6.getChatModel(var1, var2, var3, 54, var5);
         }
      } catch (RuntimeException var11) {
         throw ClientErrorException.clientError(var11, "me.A(" + (var1 != null?"{...}":"null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
      }
   }

   private void parseOpcode(int var1, int opcode, DataBuffer buffer) {
      try {
         int var4;
         int var5;
         if(1 == opcode) {
            var4 = buffer.readUnsignedByte();
            this.models = new int[var4];

            for(var5 = 0; var4 > var5; ++var5) {
               this.models[var5] = buffer.readUnsignedShort();
               if(this.models[var5] == 65535) {
                  this.models[var5] = -1;
               }
            }
         } else if(opcode == 2) {
            this.NPCName = buffer.readString();
         } else if (opcode == 12) {
            this.size = buffer.readUnsignedByte();
         } else if (opcode >= 30 && opcode < 35) {
            this.options[-30 + opcode] = buffer.readString();
            if (this.options[-30 + opcode].equalsStringIgnoreCase(TextCore.HasHidden)) {
               this.options[opcode - 30] = null;
            }
         } else if (opcode == 40) {
            var4 = buffer.readUnsignedByte();
            this.aShortArray1254 = new short[var4];
            this.aShortArray1248 = new short[var4];

            for (var5 = 0; var4 > var5; ++var5) {
               this.aShortArray1248[var5] = (short) buffer.readUnsignedShort();
               this.aShortArray1254[var5] = (short) buffer.readUnsignedShort();
            }
         } else if (opcode == 41) {
            var4 = buffer.readUnsignedByte();
            this.aShortArray1246 = new short[var4];
            this.aShortArray1271 = new short[var4];

            for (var5 = 0; var5 < var4; ++var5) {
               this.aShortArray1271[var5] = (short) buffer.readUnsignedShort();
               this.aShortArray1246[var5] = (short) buffer.readUnsignedShort();
            }
         } else if (opcode == 42) {
            var4 = buffer.readUnsignedByte();
            this.aByteArray1247 = new byte[var4];

            for (var5 = 0; var4 > var5; ++var5) {
               this.aByteArray1247[var5] = buffer.readSignedByte();
            }
         } else if (opcode == 60) {
            var4 = buffer.readUnsignedByte();
            this.anIntArray1250 = new int[var4];

            for (var5 = 0; var5 < var4; ++var5) {
               this.anIntArray1250[var5] = buffer.readUnsignedShort();
            }
         } else if (93 == opcode) {
            this.aBoolean1285 = false;
         } else if (opcode == 95) {
            this.anInt1260 = buffer.readUnsignedShort();
         } else if (opcode == 97) {
            this.anInt1264 = buffer.readUnsignedShort();
         } else if (opcode == 98) {
            this.anInt1266 = buffer.readUnsignedShort();
         } else if (opcode == 99) {
            this.aBoolean1263 = true;
         } else if (opcode == 100) {
            this.anInt1251 = buffer.readSignedByte();
         } else if (opcode == 101) {
            this.anInt1282 = buffer.readSignedByte() * 5;
         } else if (opcode == 102) {
            this.anInt1269 = buffer.readUnsignedShort();
         } else if (103 == opcode) {
            this.anInt1274 = buffer.readUnsignedShort();
         } else {
            int var6;
            if (106 == opcode || opcode == 118) {
               this.configId = buffer.readUnsignedShort();
               var4 = -1;
               if (this.configId == 65535) {
                  this.configId = -1;
               }

               this.configFileId = buffer.readUnsignedShort();
               if (this.configFileId == 65535) {
                  this.configFileId = -1;
               }

               if (opcode == 118) {
                  var4 = buffer.readUnsignedShort();
                  if (var4 == 65535) {
                     var4 = -1;
                  }
               }

               var5 = buffer.readUnsignedByte();
               this.childNPCs = new int[2 + var5];

               for (var6 = 0; var6 <= var5; ++var6) {
                  this.childNPCs[var6] = buffer.readUnsignedShort();
                  if (this.childNPCs[var6] == 65535) {
                     this.childNPCs[var6] = -1;
                  }
               }

               this.childNPCs[1 + var5] = var4;
            } else if (opcode == 107) {
               this.aBoolean1270 = false;
            } else if (opcode == 109) {
               this.aBoolean1255 = false;
            } else if (opcode == 111) {
               this.aBoolean1249 = false;
            } else if (opcode == 113) {
               this.aShort1286 = (short) buffer.readUnsignedShort();
               this.aShort1256 = (short) buffer.readUnsignedShort();
            } else if (opcode == 114) {
               this.aByte1287 = buffer.readSignedByte();
               this.aByte1275 = buffer.readSignedByte();
            } else if (opcode == 115) {
               buffer.readUnsignedByte();
               buffer.readUnsignedByte();
            } else if (119 == opcode) {
               this.aByte1267 = buffer.readSignedByte();
            } else if (121 == opcode) {
               this.anIntArrayArray1261 = new int[this.models.length][];
               var4 = buffer.readUnsignedByte();

               for (var5 = 0; var5 < var4; ++var5) {
                  var6 = buffer.readUnsignedByte();
                  int[] var7 = this.anIntArrayArray1261[var6] = new int[3];
                  var7[0] = buffer.readSignedByte();
                  var7[1] = buffer.readSignedByte();
                  var7[2] = buffer.readSignedByte();
               }
            } else if (122 == opcode) {
               this.anInt1279 = buffer.readUnsignedShort();
            } else if (opcode == 123) {
               this.anInt1265 = buffer.readUnsignedShort();
            } else if (opcode == 125) {
               this.aByte1268 = buffer.readSignedByte();
            } else if (126 == opcode) {
               this.anInt1283 = buffer.readUnsignedShort();
            } else if (127 == opcode) {
               this.renderAnimationId = buffer.readUnsignedShort();
            } else if (128 == opcode) {
               buffer.readUnsignedByte();
            } else if (opcode == 134) {
               this.anInt1262 = buffer.readUnsignedShort();
               if (this.anInt1262 == 65535) {
                  this.anInt1262 = -1;
               }

               this.anInt1290 = buffer.readUnsignedShort();
               if (this.anInt1290 == 65535) {
                  this.anInt1290 = -1;
               }

               this.anInt1293 = buffer.readUnsignedShort();
               if (this.anInt1293 == 65535) {
                  this.anInt1293 = -1;
               }

               this.anInt1276 = buffer.readUnsignedShort();
               if (this.anInt1276 == 65535) {
                  this.anInt1276 = -1;
               }

               this.anInt1291 = buffer.readUnsignedByte();
            } else if (opcode == 135) {
               this.anInt1296 = buffer.readUnsignedByte();
               this.anInt1253 = buffer.readUnsignedShort();
            } else if (opcode == 136) {
               this.anInt1289 = buffer.readUnsignedByte();
               this.anInt1278 = buffer.readUnsignedShort();
            } else if (opcode == 137) {
               this.anInt1298 = buffer.readUnsignedShort();
            } else if (opcode == 249) {
               var4 = buffer.readUnsignedByte();
               if (null == this.aHashTable_1272) {
                  var5 = Class95.method1585((byte) 109, var4);
                  this.aHashTable_1272 = new HashTable(var5);
               }

               for (var5 = 0; var4 > var5; ++var5) {
                  boolean var11 = 1 == buffer.readUnsignedByte();
                  int var10 = buffer.readMedium();
                  Object var8;
                  if (var11) {
                     var8 = new LinkableRSString(buffer.readString());
                  } else {
                     var8 = new LinkableInt(buffer.readInt());
                  }

                  this.aHashTable_1272.put(var10, (Linkable) var8);
               }
            }
         }

      } catch (RuntimeException var9) {
         throw ClientErrorException.clientError(var9, "me.H(" + var1 + ',' + opcode + ',' + (buffer != null?"{...}":"null") + ')');
      }

      /**
       * Will find a better way to incorporate this without tacking it onto the end of NPCDefinitions at some point.
       * Will *Actually* worry about it and make it 100x better when working on the new client. ~ Woah
       *
       * Checks to see if the Halloween Event is enabled, and then applies the "Trick-or-Treat" option to the NPC's that
       * had it during the 2008 Halloween Event
       */
      if (GameConfig.HALLOWEEN_EVENT_ENABLED) {
         //First Options
         /*
            None
          */
         //Second Options
         if (npcId == 307 || npcId == 375 || npcId == 743 || npcId == 744 || npcId == 755 || npcId == 2634 || npcId == 2690 || npcId == 2691 || npcId == 2692) {
            options[2] = TextCore.TrickorTreat;
         }
         //Third Options
         if (npcId == 530 || npcId == 531 || npcId == 556 || npcId == 557 || npcId == 558 || npcId == 559 || npcId == 583 || npcId == 585 || npcId == 1860 || npcId == 3299 || npcId == 3671) {
            options[3] = TextCore.TrickorTreat;
         }
         //Fourth Options
         if (npcId == 922 || npcId == 970) {
            options[4] = TextCore.TrickorTreat;
         }


      }

   }

   public NPCDefinition() {
      this.NPCName = TextCore.aClass94_2006;
      this.anInt1260 = -1;
      this.aBoolean1285 = true;
      this.anInt1253 = -1;
      this.anInt1282 = 0;
      this.anInt1283 = -1;
      this.anInt1264 = 128;
      this.aByte1275 = -16;
      this.anInt1269 = -1;
      this.aByte1267 = 0;
      this.aBoolean1249 = true;
      this.aShort1286 = 0;
      this.anInt1289 = -1;
      this.anInt1279 = -1;
      this.anInt1251 = 0;
      this.aBoolean1263 = false;
      this.anInt1274 = 32;
      this.options = new RSString[5];
      this.anInt1293 = -1;
      this.aByte1287 = -96;
      this.aByte1268 = 7;
      this.renderAnimationId = -1;
      this.anInt1296 = -1;
      this.anInt1291 = 0;
      this.anInt1266 = 128;
      this.configId = -1;
      this.anInt1290 = -1;
      this.anInt1265 = -1;
      this.anInt1278 = -1;
      this.configFileId = -1;
      this.anInt1298 = -1;
   }

   static int method1484(int var1) {
      try {

         CSConfigCachefile var2 = CSConfigCachefile.getCSConfigFileFromVarbitID(var1);
         int var3 = Objects.requireNonNull(var2).parentVarpIndex;
         int var5 = var2.upperBound;
         int var4 = var2.lowerBound;
         int var6 = Class3_Sub6.expectedMinimumValues[var5 + -var4];
         return ItemDefinition.ram[var3] >> var4 & var6;
      } catch (RuntimeException var7) {
         throw ClientErrorException.clientError(var7, "me.B(" + 64835055 + ',' + var1 + ')');
      }
   }

}

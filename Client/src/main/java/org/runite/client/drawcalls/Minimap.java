package org.runite.client.drawcalls;

import org.rs09.client.rendering.Toolkit;
import org.runite.client.*;

import java.util.Objects;

public class Minimap {
    public static void displayMinimap(int var0, byte var1, int var2, int var3, RSInterface var4) {
       try {
          Class58.method1194();

          if(HDToolKit.highDetail) {
             Class22.setClipping(var3, var2, var3 + var4.width, var2 + var4.height);
          } else {
             Class74.setClipping(var3, var2, var3 - -var4.width, var2 + var4.height);
          }

          if(2 != Class161.anInt2028 && 5 != Class161.anInt2028 && Class49.aAbstractSprite_812 != null) {
             int var19 = TextureOperation9.anInt3102 + GraphicDefinition.CAMERA_DIRECTION & 0x7FF;//Region Rotation (relative to player camera)
             int var6 = Class102.player.anInt2819 / 32 + 48;//Minimap X Axis (relative to player) Used as offset
             int var7 = -(Class102.player.anInt2829 / 32) + 464;//Minimap Y Axis (relative to player) Used as offset
             if(HDToolKit.highDetail) {
                ((HDSprite)Class49.aAbstractSprite_812).drawMinimapRegion(var3, var2, var4.width, var4.height, var6, var7, var19, Class164_Sub2.anInt3020 + 256, (HDSprite)var4.method866(false));
             } else {
                ((Class3_Sub28_Sub16_Sub2)Class49.aAbstractSprite_812).drawMinimapRegion(var3, var2, var4.width, var4.height, var6, var7, var19, 256 - -Class164_Sub2.anInt3020, var4.anIntArray207, var4.anIntArray291);
             }

             int var9;
             int var10;
             int var11;
             int var12;
             int var13;
             int var14;
             int var17;
             int var16;
             if(null != TextureOperation22.aClass131_3421) {
                for(int var8 = 0; var8 < TextureOperation22.aClass131_3421.anInt1720; ++var8) {
                   if(TextureOperation22.aClass131_3421.method1789(var8, var1 ^ 553)) {
                      var9 = 2 + 4 * (TextureOperation22.aClass131_3421.aShortArray1727[var8] + -Class131.anInt1716) + -(Class102.player.anInt2819 / 32);
                      var11 = Class51.anIntArray840[var19];
                      var12 = Class51.anIntArray851[var19];
                      Font var15 = FontType.smallFont;
                      var11 = var11 * 256 / (256 + Class164_Sub2.anInt3020);
                      var10 = 2 + 4 * (-Texture.anInt1152 + TextureOperation22.aClass131_3421.aShortArray1718[var8]) - Class102.player.anInt2829 / 32;
                      var12 = var12 * 256 / (256 + Class164_Sub2.anInt3020);
                      var14 = -(var9 * var11) + var10 * var12 >> 16;
                      if(TextureOperation22.aClass131_3421.method1791(var8, var1 + -51) == 1) {
                         var15 = FontType.plainFont;
                      }

                      if(2 == TextureOperation22.aClass131_3421.method1791(var8, 8)) {
                         var15 = FontType.bold;
                      }

                      var13 = var11 * var10 - -(var12 * var9) >> 16;
                      var16 = var15.method680(TextureOperation22.aClass131_3421.aClass94Array1721[var8], 100);
                      var13 -= var16 / 2;
                      if(-var4.width <= var13 && var13 <= var4.width && var14 >= -var4.height && var14 <= var4.height) {
                         var17 = 16777215;
                         if(TextureOperation22.aClass131_3421.anIntArray1725[var8] != -1) {
                            var17 = TextureOperation22.aClass131_3421.anIntArray1725[var8];
                         }

                         if(HDToolKit.highDetail) {
                            Class22.method936((HDSprite) Objects.requireNonNull(var4.method866(false)));
                         } else {
                            Class74.method1314(var4.anIntArray207, var4.anIntArray291);
                         }

                         var15.method693(TextureOperation22.aClass131_3421.aClass94Array1721[var8], var3 + var13 + var4.width / 2, var2 + var4.height / 2 + -var14, var16, 50, var17, 0, 1, 0, 0);
                         if(HDToolKit.highDetail) {
                            Class22.method921();
                         } else {
                            Class74.method1310();
                         }
                      }
                   }
                }
             }

             for(var9 = 0; MouseListeningClass.anInt1924 > var9; ++var9) {
                var10 = -(Class102.player.anInt2819 / 32) + 2 + 4 * Class84.anIntArray1163[var9];
                var11 = -(Class102.player.anInt2829 / 32) + 2 + (Unsorted.anIntArray4050[var9] * 4);
                ObjectDefinition var20 = ObjectDefinition.getObjectDefinition(Class3_Sub19.anIntArray3693[var9]);
                if(null != var20.ChildrenIds) {
                   var20 = var20.method1685(var1 + -59);
                   if(null == var20 || var20.MapIcon == -1) {
                      continue;
                   }
                }

                Class38_Sub1.minimapIcons(var4, Class140_Sub4.aAbstractSpriteArray2839[var20.MapIcon], var11, var10, var2, var3);
             }

             for(var9 = 0; 104 > var9; ++var9) {
                for(var10 = 0; var10 < 104; ++var10) {
                   LinkedList var25 = Class39.aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var9][var10];
                   if(null != var25) {
                      var12 = 2 + var9 * 4 + -(Class102.player.anInt2819 / 32);
                      var13 = -(Class102.player.anInt2829 / 32) + 2 + 4 * var10;
                      Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[0], var13, var12, var2, var3);
                   }
                }
             }

             for(var9 = 0; var9 < Class163.localNPCCount; ++var9) {
                NPC var21 = NPC.npcs[Class15.localNPCIndexes[var9]];
                if(var21 != null && var21.hasDefinitions()) {
                   NPCDefinition var22 = var21.definition;
                   if(null != var22 && null != var22.childNPCs) {
                      var22 = var22.method1471((byte)-3);
                   }

                   if(var22 != null && var22.aBoolean1285 && var22.aBoolean1270) {
                      var12 = var21.anInt2819 / 32 - Class102.player.anInt2819 / 32;
                      var13 = var21.anInt2829 / 32 + -(Class102.player.anInt2829 / 32);
                      if(var22.anInt1283 == -1) {
                         Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[1], var13, var12, var2, var3);
                      } else {
                         Class38_Sub1.minimapIcons(var4, Class140_Sub4.aAbstractSpriteArray2839[var22.anInt1283], var13, var12, var2, var3);
                      }
                   }
                }
             }

             for(var9 = 0; var9 < Class159.localPlayerCount; ++var9) {
                Player var23 = Unsorted.players[Class56.localPlayerIndexes[var9]];
                if(null != var23 && var23.hasDefinitions()) {
                   var12 = var23.anInt2829 / 32 - Class102.player.anInt2829 / 32;
                   var11 = -(Class102.player.anInt2819 / 32) + var23.anInt2819 / 32;
                   long var29 = var23.displayName.toLong();
                   boolean var28 = false;

                   for(var16 = 0; var16 < Class8.anInt104; ++var16) {
                      if(Class50.aLongArray826[var16] == var29 && 0 != Unsorted.anIntArray882[var16]) {
                         var28 = true;
                         break;
                      }
                   }

                   boolean var31 = false;

                   for(var17 = 0; Unsorted.clanSize > var17; ++var17) {
                      if(var29 == PacketParser.aClass3_Sub19Array3694[var17].linkableKey) {
                         var31 = true;
                         break;
                      }
                   }

                   boolean var32 = false;
                   if(Class102.player.teamId != 0 && 0 != var23.teamId && var23.teamId == Class102.player.teamId) {
                      var32 = true;
                   }

                   if(var28) {
                      Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[3], var12, var11, var2, var3);
                   } else if(var31) {
                      Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[5], var12, var11, var2, var3);
                   } else if (var32) {
                      Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[4], var12, var11, var2, var3);
                   } else {
                      Class38_Sub1.minimapIcons(var4, Unsorted.aAbstractSpriteArray1136[2], var12, var11, var2, var3);
                   }
                }
             }

             Class96[] var24 = ClientErrorException.aClass96Array2114;

             for(var10 = 0; var24.length > var10; ++var10) {
                Class96 var26 = var24[var10];
                if(null != var26 && var26.anInt1360 != 0 && Class44.anInt719 % 20 < 10) {
                   if(var26.anInt1360 == 1 && var26.anInt1359 >= 0 && var26.anInt1359 < NPC.npcs.length) {
                      NPC var27 = NPC.npcs[var26.anInt1359];
                      if(null != var27) {
                         var13 = -(Class102.player.anInt2819 / 32) + var27.anInt2819 / 32;
                         var14 = var27.anInt2829 / 32 + -(Class102.player.anInt2829 / 32);
                         Class53.method1171(var26.anInt1351, var2, var3, var13, var14, var4);
                      }
                   }

                   if(var26.anInt1360 == 2) {
                      var12 = (-Class131.anInt1716 + var26.anInt1356) * 4 + 2 - Class102.player.anInt2819 / 32;
                      var13 = -(Class102.player.anInt2829 / 32) + 2 + (-Texture.anInt1152 + var26.anInt1347) * 4;
                      Class53.method1171(var26.anInt1351, var2, var3, var12, var13, var4);
                   }

                   if(var26.anInt1360 == 10 && var26.anInt1359 >= 0 && Unsorted.players.length > var26.anInt1359) {
                      Player var30 = Unsorted.players[var26.anInt1359];
                      if(null != var30) {
                         var14 = var30.anInt2829 / 32 + -(Class102.player.anInt2829 / 32);
                         var13 = var30.anInt2819 / 32 + -(Class102.player.anInt2819 / 32);
                         Class53.method1171(var26.anInt1351, var2, var3, var13, var14, var4);
                      }
                   }
                }
             }

             if(Class65.anInt987 != 0) {
                var9 = 4 * Class65.anInt987 + (2 - Class102.player.anInt2819 / 32);
                var10 = 2 + 4 * Class45.anInt733 - Class102.player.anInt2829 / 32;
                Class38_Sub1.minimapIcons(var4, Class45.aAbstractSprite_736, var10, var9, var2, var3);
             }
             Toolkit.getActiveToolkit().method934(-1 + (var3 - -(var4.width / 2)), -1 + var2 - -(var4.height / 2), 3, 3, 16777215);

          } else if(HDToolKit.highDetail) {
             AbstractSprite var5 = var4.method866(false);
             if(null != var5) {
                var5.drawAt(var3, var2);
             }
          } else {
             Class74.method1332(var3, var2, var4.anIntArray207, var4.anIntArray291);
          }

          if(var1 == 59) {
             Class163_Sub1_Sub1.aBooleanArray4008[var0] = true;
          }
       } catch (RuntimeException var18) {
          throw ClientErrorException.clientError(var18, "ed.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + (var4 != null?"{...}":"null") + ')');
       }
    }
}

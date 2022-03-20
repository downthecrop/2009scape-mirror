package org.runite.client;

import org.rs09.client.Node;
import org.rs09.client.rendering.Toolkit;

import java.util.Objects;

public abstract class AbstractSprite extends Node {

   public int height;
   int anInt3697;
   int anInt3698;
   public static int anInt3699 = 0;
   int anInt3701;
   static int anInt3704;
   int anInt3706;
   public int width;
   static RSInterface aClass11_3708 = null;

   public static AbstractSprite constructItemSprite(int var0, boolean useHDSprite, int itemID, boolean var4, int outlineType, int itemCount, boolean shrinkInSprite) {
       try {
           ItemDefinition item = ItemDefinition.getItemDefinition(itemID);
           if (itemCount > 1 && item.anIntArray804 != null) {
               int var9 = -1;

               for (int var10 = 0; var10 < 10; ++var10) {
                   if (item.anIntArray766[var10] <= itemCount && item.anIntArray766[var10] != 0) {
                       var9 = item.anIntArray804[var10];
                   }
               }

               if (var9 != -1) {
                   item = ItemDefinition.getItemDefinition(var9);
               }
           }

           Class140_Sub1_Sub2 var21 = item.method1120();
           if (null == var21) {
               return null;
           } else {
               SoftwareSprite var22 = null;
               if (item.anInt791 == -1) {
                   if (item.anInt762 != -1) {
                       var22 = (SoftwareSprite) constructItemSprite(var0, true, item.anInt795, false, outlineType, itemCount, false);
                       if (null == var22) {
                           return null;
                       }
                   }
               } else {
                   var22 = (SoftwareSprite) constructItemSprite(0, true, item.noteID, false, 1, 10, true);
                   if (null == var22) {
                       return null;
                   }
               }

               int[] var11 = Toolkit.JAVA_TOOLKIT.getBuffer();
               int var12 = Toolkit.JAVA_TOOLKIT.width;
               int var13 = Toolkit.JAVA_TOOLKIT.height;
               int[] var14 = new int[4];
               Class74.method1325(var14);
               SoftwareSprite var15 = new SoftwareSprite(36, 32);
               Class74.setBuffer(var15.anIntArray4081, 36, 32);
               Class51.method1134();
               Class51.method1145(16, 16);
               int var16 = item.modelZoom;
               Class51.aBoolean843 = false;
               if (shrinkInSprite) {
                   var16 = (int) ((double) var16 * 1.5D);
               } else if (outlineType == 2) {
                   var16 = (int) (1.04D * (double) var16);
               }

               int var18 = Class51.anIntArray851[item.modelRotation1] * var16 >> 16;
               int var17 = Class51.anIntArray840[item.modelRotation1] * var16 >> 16;
               var21.method1893(item.modelRotation2, item.anInt768, item.modelRotation1, item.anInt792, var17 - (var21.method1871() / 2 + -item.anInt754), item.anInt754 + var18);
               if (outlineType >= 1) {
                   var15.method657(1);
                   if (outlineType >= 2) {
                       var15.method657(16777215);
                   }

                   Class74.setBuffer(var15.anIntArray4081, 36, 32);
               }

               if (var0 != 0) {
                   var15.method668(var0);
               }

               if (item.anInt791 != -1) {
                   Objects.requireNonNull(var22).drawAt(0, 0);
               } else if (-1 != item.anInt762) {
                   Class74.setBuffer(Objects.requireNonNull(var22).anIntArray4081, 36, 32);
                   var15.drawAt(0, 0);
                   var15 = var22;
               }

               if (var4 && (item.stackingType == 1 || itemCount != 1) && itemCount != -1) {
                   TextureOperation10.aClass3_Sub28_Sub17_Sub1_3440.method681(Class3_Sub7.itemStackColor(1000, itemCount), 0, 9, 16776960, 1);
               }

               Class74.setBuffer(var11, var12, var13);
               Class74.setClipping(var14);
               Class51.method1134();
               Class51.aBoolean843 = true;
               return HDToolKit.highDetail && !useHDSprite ? new HDSprite(var15) : var15;
           }
       } catch (RuntimeException var20) {
           throw ClientErrorException.clientError(var20, "na.WA(" + var0 + ',' + useHDSprite + ',' + itemID + ',' + var4 + ',' + outlineType + ',' + itemCount + ',' + shrinkInSprite + ')');
       }
   }

   abstract void method635(int var1, int var2);

   abstract void method636(int var1, int var2, int var3, int var4, int var5, int var6);

   abstract void method637(int var1, int var2, int var3);

   public static RSInterface method638(int var1, int var2) {
      try {
         RSInterface var3 = Unsorted.getRSInterface(var1);
         return var2 == -1 ?var3: var3 != null && var3.aClass11Array262 != null && var2 < var3.aClass11Array262.length ?var3.aClass11Array262[var2]:null;
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "qf.P(" + (byte) -19 + ',' + var1 + ',' + var2 + ')');
      }
   }

   public abstract void method639(int var1, int var2, int var3, int var4);

   public final void drawScaledOrRotated(int x, int y, int angle, int scale) {
      try {
          int var6 = this.anInt3697 << 3;
          int var7 = this.anInt3706 << 3;
          scale = (scale << 4) + (var6 & 15);
          x = (x << 4) + (15 & var7);
          this.method636(var6, var7, scale, x, y, angle);
      } catch (RuntimeException var8) {
         throw ClientErrorException.clientError(var8, "qf.F(" + x + ',' + y + ',' + angle + ',' + scale + ',' + -1470985020 + ')');
      }
   }

   abstract void method641(int var1, int var2);

   abstract void method642(int var1, int var2, int var3, int var4, int var5);

   public abstract void drawAt(int var1, int var2);

}

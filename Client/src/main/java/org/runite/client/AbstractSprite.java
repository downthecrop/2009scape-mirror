package org.runite.client;

import org.rs09.client.Node;

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

   abstract void method635(int var1, int var2);

   abstract void method636(int var1, int var2, int var3, int var4, int var5, int var6);

   abstract void method637(int var1, int var2, int var3);

   public static RSInterface method638(int var1, int var2) {
      try {
         RSInterface var3 = Class7.getRSInterface(var1);
         return var2 == -1 ?var3: var3 != null && var3.aClass11Array262 != null && var2 < var3.aClass11Array262.length ?var3.aClass11Array262[var2]:null;
      } catch (RuntimeException var4) {
         throw ClientErrorException.clientError(var4, "qf.P(" + (byte) -19 + ',' + var1 + ',' + var2 + ')');
      }
   }

   abstract void method639(int var1, int var2, int var3, int var4);

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

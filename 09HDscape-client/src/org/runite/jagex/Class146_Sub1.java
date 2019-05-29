package org.runite.jagex;
import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

final class Class146_Sub1 extends Class146 implements MouseWheelListener {

   private int anInt2941 = 0;
   static boolean isMoved = false;
   static int moveAmt = 0;


   final void method2082(boolean var1, Component var2) {
      if(var1) {
         this.mouseWheelMoved((MouseWheelEvent)null);
      }

      var2.removeMouseWheelListener(this);
   }

   final synchronized int method2078(int var1) {
      int var2 = this.anInt2941;
      this.anInt2941 = 0;
      isMoved = false;
      if(var1 != -1) {
         this.anInt2941 = -53;
      }

      return var2;
   }

   public final synchronized void mouseWheelMoved(MouseWheelEvent var1) {
      this.anInt2941 += var1.getWheelRotation();
      moveAmt = var1.getWheelRotation();
      isMoved = true;
   }

   final void method2084(Component var1, int var2) {
      if(var2 < -70) {
         var1.addMouseWheelListener(this);
      }
   }

}

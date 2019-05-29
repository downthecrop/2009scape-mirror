package org.runite.jagex;
import java.applet.Applet;
import netscape.javascript.JSObject;

final class Class42 {

   static final Object method1055(String var0, byte var1, Applet var2) throws Throwable {
      return var1 > -87?(Object)null:JSObject.getWindow(var2).call(var0, (Object[])null);
   }

   static final Object method1056(Applet var0, String var1, Object[] var2, byte var3) throws Throwable {
      int var4 = -90 / ((var3 - 40) / 55);
      return JSObject.getWindow(var0).call(var1, var2);
   }

   static final void method1057(Applet var0, boolean var1, String var2) throws Throwable {
      JSObject.getWindow(var0).eval(var2);
      if(!var1) {
         ;
      }
   }
}

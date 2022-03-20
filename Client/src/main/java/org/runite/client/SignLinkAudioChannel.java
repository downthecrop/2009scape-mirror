package org.runite.client;
import java.awt.Component;

final class SignLinkAudioChannel extends AudioChannel {

   private final int anInt2969;
   private static Interface1 anInterface1_2970;


   final int method2157() {
      return anInterface1_2970.method2((byte)118, this.anInt2969);
   }

   final void write() {
      anInterface1_2970.method6(this.anInt2969, this.samples);
   }

   final void init(Component var1) throws Exception {
      anInterface1_2970.method5(Class21.sampleRate, (byte)-39, var1, AudioChannel.stereo);
   }

   final void flush() {
      anInterface1_2970.method1(this.anInt2969, 28544);
   }

   SignLinkAudioChannel(Signlink var1, int var2) {
      anInterface1_2970 = var1.method1446((byte)99);
      this.anInt2969 = var2;
   }

   final void open(int var1) throws Exception {
      if(var1 > 32768) {
         throw new IllegalArgumentException();
      } else {
         anInterface1_2970.method3(this.anInt2969, 25349, var1);
      }
   }

   final void close() {
      anInterface1_2970.method4((byte)20, this.anInt2969);
   }
}

package com.alex.loaders.animations;

import com.alex.io.InputStream;
import com.alex.store.Store;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationDefinitions {
   public static Store cache;
   public static int id;
   public int loopCycles = 99;
   public int anInt2137;
   public static int[] frames;
   public int anInt2140 = -1;
   public boolean aBoolean2141 = false;
   public int priority = 5;
   public int leftHandEquip = -1;
   public int rightHandEquip = -1;
   public int anInt2145;
   public int[][] handledSounds;
   public boolean[] aBooleanArray2149;
   public int[] anIntArray2151;
   public boolean aBoolean2152 = false;
   public static int[] delays;
   public int anInt2155 = 2;
   public boolean aBoolean2158 = false;
   public boolean aBoolean2159 = false;
   public int anInt2162 = -1;
   public int loopDelay = -1;
   public int[] soundMinDelay;
   public int[] soundMaxDelay;
   public int[] anIntArray1362;
   public boolean effect2Sound;
   private static final ConcurrentHashMap animDefs = new ConcurrentHashMap();

   public static void main(String[] args) throws IOException {
      cache = new Store("C:/Users/yvonne � christer/Dropbox/Source/data/562cache/");

      label55:
      for(int i = 0; i < 1; ++i) {
         System.out.println("Emote ID: " + i);
         int k = 0;

         while(true) {
            getAnimationDefinitions(i);
            PrintStream var10000;
            StringBuilder var10001;
            if(k >= delays.length) {
               k = 0;

               while(true) {
                  getAnimationDefinitions(i);
                  if(k >= frames.length) {
                     System.out.println("loopDelay = " + getAnimationDefinitions(i).loopDelay);
                     System.out.println("leftHandEquip = " + getAnimationDefinitions(i).leftHandEquip);
                     System.out.println("priority = " + getAnimationDefinitions(i).priority);
                     System.out.println("rightHandEquip = " + getAnimationDefinitions(i).rightHandEquip);
                     System.out.println("loopCycles = " + getAnimationDefinitions(i).loopCycles);
                     System.out.println("anInt2140 = " + getAnimationDefinitions(i).anInt2140);
                     System.out.println("anInt2162 = " + getAnimationDefinitions(i).anInt2162);
                     System.out.println("anInt2155 = " + getAnimationDefinitions(i).anInt2155);
                     System.out.println("anInt2145 = " + getAnimationDefinitions(i).anInt2145);

                     for(k = 0; k < getAnimationDefinitions(i).anIntArray2151.length; ++k) {
                        System.out.println("anIntArray2151[" + k + "] = " + getAnimationDefinitions(i).anIntArray2151[k]);
                     }

                     for(k = 0; k < getAnimationDefinitions(i).aBooleanArray2149.length; ++k) {
                        System.out.println("aBooleanArray2149[" + k + "] = " + getAnimationDefinitions(i).aBooleanArray2149[k]);
                     }

                     System.out.println("aBoolean2152 = " + getAnimationDefinitions(i).aBoolean2152);

                     for(k = 0; k < getAnimationDefinitions(i).anIntArray1362.length; ++k) {
                        System.out.println("anIntArray1362[" + k + "] = " + getAnimationDefinitions(i).anIntArray1362[k]);
                     }
                     continue label55;
                  }

                  var10000 = System.out;
                  var10001 = (new StringBuilder()).append("frames[").append(k).append("] = ");
                  getAnimationDefinitions(i);
                  var10000.println(var10001.append(frames[k]).toString());
                  ++k;
               }
            }

            var10000 = System.out;
            var10001 = (new StringBuilder()).append("delays[").append(k).append("] = ");
            getAnimationDefinitions(i);
            var10000.println(var10001.append(delays[k]).toString());
            ++k;
         }
      }

   }

   public static final AnimationDefinitions getAnimationDefinitions(int emoteId) {
      try {
         AnimationDefinitions var3 = (AnimationDefinitions)animDefs.get(Integer.valueOf(emoteId));
         if(var3 != null) {
            return var3;
         } else {
            byte[] data = cache.getIndexes()[20].getFile(emoteId >>> 7, emoteId & 127);
            var3 = new AnimationDefinitions();
            if(data != null) {
               var3.readValueLoop(new InputStream(data));
            }

            var3.method2394();
            animDefs.put(Integer.valueOf(emoteId), var3);
            id = emoteId;
            return var3;
         }
      } catch (Throwable var31) {
         return null;
      }
   }

   private void readValueLoop(InputStream stream) {
      while(true) {
         int opcode = stream.readUnsignedByte();
         if(opcode == 0) {
            return;
         }

         this.readValues(stream, opcode);
      }
   }

   public int getEmoteTime() {
      if(delays == null) {
         return 0;
      } else {
         int ms = 0;
         int[] arr$ = delays;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int i = arr$[i$];
            ms += i;
         }

         return ms * 30;
      }
   }

   public int getEmoteGameTickets() {
      return this.getEmoteTime() / 1000;
   }

   private void readValues(InputStream stream, int opcode) {
      int index;
      int i_21_;
      if(opcode == 1) {
         index = stream.readUnsignedShort();
         delays = new int[index];

         for(i_21_ = 0; ~index < ~i_21_; ++i_21_) {
            delays[i_21_] = stream.readUnsignedShort();
         }

         frames = new int[index];

         for(i_21_ = 0; ~i_21_ > ~index; ++i_21_) {
            frames[i_21_] = stream.readUnsignedShort();
         }

         for(i_21_ = 0; i_21_ < index; ++i_21_) {
            frames[i_21_] += stream.readUnsignedShort() << 16;
         }
      } else if(opcode == 2) {
         this.loopDelay = stream.readUnsignedShort();
      } else if(opcode == 3) {
         this.aBooleanArray2149 = new boolean[256];
         index = stream.readUnsignedByte();

         for(i_21_ = 0; i_21_ < index; ++i_21_) {
            this.aBooleanArray2149[stream.readUnsignedByte()] = true;
         }
      } else if(opcode == 4) {
         this.aBoolean2152 = true;
      } else if(opcode == 5) {
         this.priority = stream.readUnsignedByte();
      } else if(opcode == 6) {
         this.rightHandEquip = stream.readUnsignedShort();
      } else if(opcode == 7) {
         this.leftHandEquip = stream.readUnsignedShort();
      } else if(opcode == 8) {
         this.loopCycles = stream.readUnsignedByte();
      } else if(opcode == 9) {
         this.anInt2140 = stream.readUnsignedByte();
      } else if(opcode == 10) {
         this.anInt2162 = stream.readUnsignedByte();
      } else if(opcode == 11) {
         this.anInt2155 = stream.readUnsignedByte();
      } else if(opcode == 12) {
         index = stream.readUnsignedByte();
         this.anIntArray2151 = new int[index];

         for(i_21_ = 0; ~i_21_ > ~index; ++i_21_) {
            this.anIntArray2151[i_21_] = stream.readUnsignedShort();
         }

         for(i_21_ = 0; index > i_21_; ++i_21_) {
            this.anIntArray2151[i_21_] += stream.readUnsignedShort() << 16;
         }
      } else if(opcode == 13) {
         index = stream.readUnsignedShort();
         this.handledSounds = new int[index][];

         for(i_21_ = 0; i_21_ < index; ++i_21_) {
            int i_22_ = stream.readUnsignedByte();
            if(~i_22_ < -1) {
               this.handledSounds[i_21_] = new int[i_22_];
               this.handledSounds[i_21_][0] = stream.read24BitInt();

               for(int i_23_ = 1; ~i_22_ < ~i_23_; ++i_23_) {
                  this.handledSounds[i_21_][i_23_] = stream.readUnsignedShort();
               }
            }
         }
      } else if(opcode == 14) {
         this.aBoolean2141 = true;
      } else if(opcode == 15) {
         this.aBoolean2159 = true;
      } else if(opcode == 16) {
         this.aBoolean2158 = true;
      } else if(opcode == 17) {
         this.anInt2145 = stream.readUnsignedByte();
      } else if(opcode == 18) {
         this.effect2Sound = true;
      } else if(opcode == 19) {
         if(this.anIntArray1362 == null) {
            this.anIntArray1362 = new int[this.handledSounds.length];

            for(index = 0; index < this.handledSounds.length; ++index) {
               this.anIntArray1362[index] = 255;
            }
         }

         this.anIntArray1362[stream.readUnsignedByte()] = stream.readUnsignedByte();
      } else if(opcode == 20) {
         if(this.soundMaxDelay == null || this.soundMinDelay == null) {
            this.soundMaxDelay = new int[this.handledSounds.length];
            this.soundMinDelay = new int[this.handledSounds.length];

            for(index = 0; index < this.handledSounds.length; ++index) {
               this.soundMaxDelay[index] = 256;
               this.soundMinDelay[index] = 256;
            }
         }

         index = stream.readUnsignedByte();
         this.soundMaxDelay[index] = stream.readUnsignedShort();
         this.soundMinDelay[index] = stream.readUnsignedShort();
      }

   }

   public void method2394() {
      if(this.anInt2140 == -1) {
         if(this.aBooleanArray2149 == null) {
            this.anInt2140 = 0;
         } else {
            this.anInt2140 = 2;
         }
      }

      if(this.anInt2162 == -1) {
         if(this.aBooleanArray2149 == null) {
            this.anInt2162 = 0;
         } else {
            this.anInt2162 = 2;
         }
      }

   }
}

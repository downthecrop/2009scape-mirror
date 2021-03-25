package org.runite.client;

public final class AudioHandler {


    static int currentSoundEffectCount = 0;
    static int soundEffectVolume = 127;

    public static void musicHandler(int var1) {
        try {
            if (-1 == var1 && !Class83.aBoolean1158) {
                GameObject.method1870();
            } else if (var1 != -1 && (Class129.anInt1691 != var1 || Class79.method1391(-1)) && Unsorted.anInt120 != 0 && !Class83.aBoolean1158) {
                Unsorted.method2099(var1, CacheIndex.musicIndex, Unsorted.anInt120);
            }
            Class129.anInt1691 = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "li.B(" + true + ',' + var1 + ')');
        }
    }

    static void soundEffectHandler(int soundEffectID, int soundEffectDelay, int soundEffectVolume) {
       try {
          if(soundEffectVolume != 0 && soundEffectID != 0 && currentSoundEffectCount < 50 && soundEffectDelay != -1) {
              Class166.anIntArray2068[currentSoundEffectCount] = soundEffectID;
             Class3_Sub25.anIntArray2550[currentSoundEffectCount] = soundEffectDelay;
             Unsorted.anIntArray2157[currentSoundEffectCount] = soundEffectVolume;
             Class102.aClass135Array2131[currentSoundEffectCount] = null;
             Class3_Sub8.anIntArray3083[currentSoundEffectCount] = 0;
             currentSoundEffectCount++;
          }

       } catch (RuntimeException var5) {
          throw ClientErrorException.clientError(var5, "ca.C(" + soundEffectID + ',' + soundEffectDelay + ',' + soundEffectVolume + ',' + -799 + ')');
       }
    }
}

package org.runite.client;

import java.util.Objects;

public final class AudioHandler {


    public static int[] soundEffectDelayArray = new int[50];
    public static int musicVolume = 255;
    static int currentSoundEffectCount = 0;
    static int soundEffectVolume = 127;
    static int currentTrack = -1;
    static boolean musicEffectPlaying = false;
    static int[] soundEffectVolumeArray = new int[50];
    static int[] soundEffectIDs = new int[50];
    static SynthSound[] soundEffects = new SynthSound[50];
    static int[] soundEffectCoordinates = new int[50];

    public static void musicHandler(int var1) {
        try {
            if (-1 == var1 && !musicEffectPlaying) {
                GameObject.method1870();
            } else if (var1 != -1 && (currentTrack != var1 || Class79.method1391(-1)) && musicVolume != 0 && !musicEffectPlaying) {
                method2099(var1, CacheIndex.musicIndex, musicVolume);
            }
            currentTrack = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "li.B(" + true + ',' + var1 + ')');
        }
    }

    public static void soundEffectHandler(int soundEffectVolume, int soundEffectID, int soundEffectDelay) {
        try {
            System.out.println("Playing " + soundEffectID + " DELAY: " + soundEffectDelay + " VOLUME: " + soundEffectVolume);
            if (soundEffectDelay != -1 && soundEffectVolume != 0 && currentSoundEffectCount < 50 && soundEffectID != -1) {
                soundEffectVolumeArray[currentSoundEffectCount] = soundEffectVolume;
                soundEffectIDs[currentSoundEffectCount] = soundEffectID;
                soundEffectDelayArray[currentSoundEffectCount] = soundEffectDelay;
                soundEffects[currentSoundEffectCount] = null;
                soundEffectCoordinates[currentSoundEffectCount] = 0;
                currentSoundEffectCount++;
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ca.C(" + soundEffectVolume + ',' + soundEffectID + ',' + soundEffectDelay + ',' + -799 + ')');
        }
    }

    static void method132() {
        try {
            for (int var1 = 0; var1 < currentSoundEffectCount; ++var1) {
                --soundEffectDelayArray[var1];
                if (soundEffectDelayArray[var1] < -10) {
                    --currentSoundEffectCount;

                    for (int var2 = var1; var2 < currentSoundEffectCount; ++var2) {
                        soundEffectIDs[var2] = soundEffectIDs[var2 + 1];
                        soundEffects[var2] = soundEffects[var2 + 1];
                        soundEffectVolumeArray[var2] = soundEffectVolumeArray[1 + var2];
                        soundEffectDelayArray[var2] = soundEffectDelayArray[1 + var2];
                        soundEffectCoordinates[var2] = soundEffectCoordinates[var2 + 1];
                    }

                    --var1;
                } else {
                    SynthSound var11 = soundEffects[var1];
                    if (null == var11) {
                        var11 = SynthSound.create(CacheIndex.soundFXIndex, soundEffectIDs[var1], 0);
                        if (null == var11) {
                            continue;
                        }

                        soundEffectDelayArray[var1] += var11.getStart();
                        soundEffects[var1] = var11;
                    }

                    if (0 > soundEffectDelayArray[var1]) {
                        int var3;
                        if (soundEffectCoordinates[var1] == 0) {
                            var3 = soundEffectVolume;
                        } else {
                            int var4 = (soundEffectCoordinates[var1] & 0xFF) * 128;
                            int var7 = soundEffectCoordinates[var1] >> 8 & 0xFF;
                            int var5 = soundEffectCoordinates[var1] >> 16 & 0xFF;
                            int var8 = -Class102.player.zAxis + 64 + 128 * var7;
                            if (var8 < 0) {
                                var8 = -var8;
                            }

                            int var6 = -Class102.player.xAxis + 64 + var5 * 128;
                            if (0 > var6) {
                                var6 = -var6;
                            }

                            int var9 = -128 + var6 + var8;
                            if (var9 > var4) {
                                soundEffectDelayArray[var1] = -100;
                                continue;
                            }

                            if (var9 < 0) {
                                var9 = 0;
                            }

                            var3 = Sprites.ambientVolume * (var4 + -var9) / var4;
                        }

                        if (var3 > 0) {
                            PcmSound var12 = var11.toPCMSound().method151(Class27.resampler);
                            Class3_Sub24_Sub1 var13 = Class3_Sub24_Sub1.method437(var12, var3);
                            Objects.requireNonNull(var13).method429(soundEffectVolumeArray[var1] - 1);
                            Class3_Sub26.aClass3_Sub24_Sub2_2563.method457(var13);
                        }

                        soundEffectDelayArray[var1] = -100;
                    }
                }
            }

            if (musicEffectPlaying && Class79.method1391(-1)) {
                if (0 != musicVolume && currentTrack != -1) {
                    Class70.method1285(CacheIndex.musicIndex, currentTrack, musicVolume);
                }

                musicEffectPlaying = false;
            } else if (musicVolume != 0 && currentTrack != -1 && Class79.method1391((byte) -92 + 91)) {
                TextureOperation12.outgoingBuffer.putOpcode(137);
                TextureOperation12.outgoingBuffer.writeInt(currentTrack);
                currentTrack = -1;
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "ed.C(" + (byte) -92 + ')');
        }
    }

    public static void method2099(int var1, CacheIndex var3, int var5) {
        try {
            Class101.aClass153_1423 = var3;
            Class132.anInt1741 = 0;
            TextureOperation8.anInt3463 = var1;
            Unsorted.aBoolean2311 = false;
            Unsorted.anInt154 = 1;
            GraphicDefinition.anInt546 = 2;

            TextureOperation36.anInt3423 = var5;
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "v.Q(" + true + ',' + var1 + ',' + 0 + ',' + (var3 != null ? "{...}" : "null") + ',' + false + ',' + var5 + ',' + 2 + ')');
        }
    }

    static void method897(Class3_Sub24_Sub4 var1, CacheIndex var2, CacheIndex var3, CacheIndex var4) {
        try {
            Class124.aClass153_1661 = var2;
            Class40.aClass153_679 = var4;
            Class3_Sub28_Sub20.aClass153_3786 = var3;
            Class101.aClass3_Sub24_Sub4_1421 = var1;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ck.C(" + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ',' + (var4 != null ? "{...}" : "null") + ')');
        }
    }

    public static void musicEffectHandler(int var1) {
        try {
            if (musicVolume != 0 && var1 != -1) {
                Class70.method1285(CacheIndex.music2Index, var1, musicVolume);
                musicEffectPlaying = true;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "wj.D(" + ',' + var1 + ',' + (byte) -1 + ')');
        }
    }
}

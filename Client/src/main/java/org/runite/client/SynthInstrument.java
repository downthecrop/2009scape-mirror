package org.runite.client;

import org.rs09.client.util.ArrayUtils;

import java.util.Random;

final class SynthInstrument {

    private static final int[] anIntArray1591;
    private static final int[] anIntArray1594 = new int[32768];
    private static final int[] output;
    private static final int[] anIntArray1599;
    private static final int[] anIntArray1600;
    private static final int[] anIntArray1601;
    private static final int[] anIntArray1602;
    private static final int[] anIntArray1603;
    static int[][] coefficency = new int[2][8];
    static int inverseA0;

    static {
        Random var0 = new Random(0L);

        int var1;
        for (var1 = 0; var1 < 32768; ++var1) {
            anIntArray1594[var1] = (var0.nextInt() & 2) - 1;
        }

        anIntArray1591 = new int[32768];

        for (var1 = 0; var1 < 32768; ++var1) {
            anIntArray1591[var1] = (int) (Math.sin((double) var1 / 5215.1903D) * 16384.0D);
        }

        output = new int[220500];
        anIntArray1599 = new int[5];
        anIntArray1600 = new int[5];
        anIntArray1601 = new int[5];
        anIntArray1603 = new int[5];
        anIntArray1602 = new int[5];
    }

    private final int[] oscillatorAmplitudes = new int[]{0, 0, 0, 0, 0};
    private final int[] oscillatorStartMillis = new int[]{0, 0, 0, 0, 0};
    private final int[] anIntArray1605 = new int[]{0, 0, 0, 0, 0};
    int duration = 500;
    int start = 0;
    private Envelope amplitudeModulationAmplitudeEnvelope;
    private Envelope gateClosedPhaseEnvelope;
    private Envelope amplitudeEnvelope;
    private Envelope gateOpenPhaseEnvelope;
    private Envelope phaseEnvelope;
    private int delayTime = 0;
    private Envelope filterEnvelope;
    private Envelope amplitudeModulationEnvelope;
    private int delayMix = 100;
    private Filter filter;
    private Envelope pitch_mod_amp_env;
    private Envelope pitch_mod_env;

    private int method1716(int var1, int var2, int var3) {
        return var3 == 1 ? ((var1 & 32767) < 16384 ? var2 : -var2) : (var3 == 2 ? anIntArray1591[var1 & 32767] * var2 >> 14 : (var3 == 3 ? ((var1 & 32767) * var2 >> 14) - var2 : (var3 == 4 ? anIntArray1594[var1 / 2607 & 32767] * var2 : 0)));
    }

    final int[] synthesize(int num_samples, int dt) {
        ArrayUtils.zero(output, 0, num_samples);
        if (dt >= 10) {
            double sample_rate = (double) num_samples / ((double) dt + 0.0D);
            this.phaseEnvelope.reset();
            this.amplitudeEnvelope.reset();
            int pm_phase_delta = 0;
            int pm_phase_delta_base = 0;
            int pm_phase = 0;
            if (this.pitch_mod_env != null) {
                this.pitch_mod_env.reset();
                this.pitch_mod_amp_env.reset();
                pm_phase_delta = (int) ((double) (this.pitch_mod_env.maxInterval - this.pitch_mod_env.minInterval) * 32.768D / sample_rate);
                pm_phase_delta_base = (int) ((double) this.pitch_mod_env.minInterval * 32.768D / sample_rate);
            }

            int var8 = 0;
            int var9 = 0;
            int var10 = 0;
            if (this.amplitudeModulationEnvelope != null) {
                this.amplitudeModulationEnvelope.reset();
                this.amplitudeModulationAmplitudeEnvelope.reset();
                var8 = (int) ((double) (this.amplitudeModulationEnvelope.maxInterval - this.amplitudeModulationEnvelope.minInterval) * 32.768D / sample_rate);
                var9 = (int) ((double) this.amplitudeModulationEnvelope.minInterval * 32.768D / sample_rate);
            }

            int var11;
            for (var11 = 0; var11 < 5; ++var11) {
                if (this.oscillatorAmplitudes[var11] != 0) {
                    anIntArray1601[var11] = 0;
                    anIntArray1602[var11] = (int) ((double) this.oscillatorStartMillis[var11] * sample_rate);
                    anIntArray1603[var11] = (this.oscillatorAmplitudes[var11] << 14) / 100;
                    anIntArray1599[var11] = (int) ((double) (this.phaseEnvelope.maxInterval - this.phaseEnvelope.minInterval) * 32.768D * Math.pow(1.0057929410678534D, this.anIntArray1605[var11]) / sample_rate);
                    anIntArray1600[var11] = (int) ((double) this.phaseEnvelope.minInterval * 32.768D / sample_rate);
                }
            }

            int var12;
            int var13;
            int var14;
            int delay;
            for (var11 = 0; var11 < num_samples; ++var11) {
                var12 = this.phaseEnvelope.nextLevel(num_samples);
                var13 = this.amplitudeEnvelope.nextLevel(num_samples);
                if (this.pitch_mod_env != null) {
                    var14 = this.pitch_mod_env.nextLevel(num_samples);
                    delay = this.pitch_mod_amp_env.nextLevel(num_samples);
                    var12 += this.method1716(pm_phase, delay, this.pitch_mod_env.waveTable) >> 1;
                    pm_phase += (var14 * pm_phase_delta >> 16) + pm_phase_delta_base;
                }

                if (this.amplitudeModulationEnvelope != null) {
                    var14 = this.amplitudeModulationEnvelope.nextLevel(num_samples);
                    delay = this.amplitudeModulationAmplitudeEnvelope.nextLevel(num_samples);
                    var13 = var13 * ((this.method1716(var10, delay, this.amplitudeModulationEnvelope.waveTable) >> 1) + 32768) >> 15;
                    var10 += (var14 * var8 >> 16) + var9;
                }

                for (var14 = 0; var14 < 5; ++var14) {
                    if (this.oscillatorAmplitudes[var14] != 0) {
                        delay = var11 + anIntArray1602[var14];
                        if (delay < num_samples) {
                            output[delay] += this.method1716(anIntArray1601[var14], var13 * anIntArray1603[var14] >> 15, this.phaseEnvelope.waveTable);
                            anIntArray1601[var14] += (var12 * anIntArray1599[var14] >> 16) + anIntArray1600[var14];
                        }
                    }
                }
            }

            int var16;
            if (this.gateClosedPhaseEnvelope != null) {
                this.gateClosedPhaseEnvelope.reset();
                this.gateOpenPhaseEnvelope.reset();
                var11 = 0;

                for (var14 = 0; var14 < num_samples; ++var14) {
                    delay = this.gateClosedPhaseEnvelope.nextLevel(num_samples);
                    var16 = this.gateOpenPhaseEnvelope.nextLevel(num_samples);
                    var12 = this.gateClosedPhaseEnvelope.minInterval + ((this.gateClosedPhaseEnvelope.maxInterval - this.gateClosedPhaseEnvelope.minInterval) * delay >> 8);
                    var11 += 256;
                    if (var11 >= var12) {
                        var11 = 0;
                    } else {
                        output[var14] = 0;
                    }
                }
            }

            if (this.delayTime > 0 && this.delayMix > 0) {
                var11 = (int) ((double) this.delayTime * sample_rate);

                for (var12 = var11; var12 < num_samples; ++var12) {
                    output[var12] += output[var12 - var11] * this.delayMix / 100;
                }
            }

            if (this.filter.pairs[0] > 0 || this.filter.pairs[1] > 0) {
                this.filterEnvelope.reset();
                var11 = this.filterEnvelope.nextLevel(num_samples + 1);
                var12 = this.filter.compute(0, (float) var11 / 65536.0F);
                var13 = this.filter.compute(1, (float) var11 / 65536.0F);
                if (num_samples >= var12 + var13) {
                    var14 = 0;
                    delay = var13;
                    if (var13 > num_samples - var12) {
                        delay = num_samples - var12;
                    }

                    int var17;
                    while (var14 < delay) {
                        var16 = (int) ((long) output[var14 + var12] * (long) inverseA0 >> 16);

                        for (var17 = 0; var17 < var12; ++var17) {
                            var16 += (int) ((long) output[var14 + var12 - 1 - var17] * (long) coefficency[0][var17] >> 16);
                        }

                        for (var17 = 0; var17 < var14; ++var17) {
                            var16 -= (int) ((long) output[var14 - 1 - var17] * (long) coefficency[1][var17] >> 16);
                        }

                        output[var14] = var16;
                        var11 = this.filterEnvelope.nextLevel(num_samples + 1);
                        ++var14;
                    }

                    delay = 128;

                    while (true) {
                        if (delay > num_samples - var12) {
                            delay = num_samples - var12;
                        }

                        while (var14 < delay) {
                            var16 = (int) ((long) output[var14 + var12] * (long) inverseA0 >> 16);

                            for (var17 = 0; var17 < var12; ++var17) {
                                var16 += (int) ((long) output[var14 + var12 - 1 - var17] * (long) coefficency[0][var17] >> 16);
                            }

                            for (var17 = 0; var17 < var13; ++var17) {
                                var16 -= (int) ((long) output[var14 - 1 - var17] * (long) coefficency[1][var17] >> 16);
                            }

                            output[var14] = var16;
                            var11 = this.filterEnvelope.nextLevel(num_samples + 1);
                            ++var14;
                        }

                        if (var14 >= num_samples - var12) {
                            while (var14 < num_samples) {
                                var16 = 0;

                                for (var17 = var14 + var12 - num_samples; var17 < var12; ++var17) {
                                    var16 += (int) ((long) output[var14 + var12 - 1 - var17] * (long) coefficency[0][var17] >> 16);
                                }

                                for (var17 = 0; var17 < var13; ++var17) {
                                    var16 -= (int) ((long) output[var14 - 1 - var17] * (long) coefficency[1][var17] >> 16);
                                }

                                output[var14] = var16;
                                this.filterEnvelope.nextLevel(num_samples + 1);
                                ++var14;
                            }
                            break;
                        }

                        var12 = this.filter.compute(0, (float) var11 / 65536.0F);
                        var13 = this.filter.compute(1, (float) var11 / 65536.0F);
                        delay += 128;
                    }
                }
            }

            for (var11 = 0; var11 < num_samples; ++var11) {
                if (output[var11] < -32768) {
                    output[var11] = -32768;
                }

                if (output[var11] > 32767) {
                    output[var11] = 32767;
                }
            }

        }
        return output;
    }

    final void decode(DataBuffer buffer) {
        this.phaseEnvelope = new Envelope();
        this.phaseEnvelope.decode(buffer);
        this.amplitudeEnvelope = new Envelope();
        this.amplitudeEnvelope.decode(buffer);

        int phaseModulationWaveTable = buffer.readUnsignedByte();
        if (phaseModulationWaveTable != 0) {
            --buffer.index;
            this.pitch_mod_env = new Envelope();
            this.pitch_mod_env.decode(buffer);
            this.pitch_mod_amp_env = new Envelope();
            this.pitch_mod_amp_env.decode(buffer);
        }

        int amplitudeModulationWaveTable = buffer.readUnsignedByte();
        if (amplitudeModulationWaveTable != 0) {
            --buffer.index;
            this.amplitudeModulationEnvelope = new Envelope();
            this.amplitudeModulationEnvelope.decode(buffer);
            this.amplitudeModulationAmplitudeEnvelope = new Envelope();
            this.amplitudeModulationAmplitudeEnvelope.decode(buffer);
        }

        int gateWaveTable = buffer.readUnsignedByte();
        if (gateWaveTable != 0) {
            --buffer.index;
            this.gateClosedPhaseEnvelope = new Envelope();
            this.gateClosedPhaseEnvelope.decode(buffer);
            this.gateOpenPhaseEnvelope = new Envelope();
            this.gateOpenPhaseEnvelope.decode(buffer);
        }

        for (int var3 = 0; var3 < 10; ++var3) {
            int amplitude = buffer.getSmart();
            if (amplitude == 0) {
                break;
            }

            this.oscillatorAmplitudes[var3] = amplitude;
            this.anIntArray1605[var3] = buffer.getByteOrShort();
            this.oscillatorStartMillis[var3] = buffer.getSmart();
        }

        this.delayTime = buffer.getSmart();
        this.delayMix = buffer.getSmart();
        this.duration = buffer.readUnsignedShort();
        this.start = buffer.readUnsignedShort();
        this.filter = new Filter();
        this.filterEnvelope = new Envelope();
        this.filter.decode(buffer, this.filterEnvelope);
    }
}

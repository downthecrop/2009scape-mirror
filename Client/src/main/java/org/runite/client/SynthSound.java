package org.runite.client;

public final class SynthSound {

    private final SynthInstrument[] synthInstruments = new SynthInstrument[10];
    private int end;
    private int start;

    private SynthSound(DataBuffer buffer) {
        for (int var2 = 0; var2 < 10; ++var2) {
            int waveTable = buffer.readUnsignedByte();
            if (waveTable != 0) {
                --buffer.index;
                this.synthInstruments[var2] = new SynthInstrument();
                this.synthInstruments[var2].decode(buffer);
            }
        }

        this.start = buffer.readUnsignedShort();
        this.end = buffer.readUnsignedShort();
    }

    public static SynthSound create(CacheIndex var0, int var1, int var2) {
        byte[] var3 = var0.getFile(var1, var2);
        return var3 == null ? null : new SynthSound(new DataBuffer(var3));
    }

    private byte[] getSamples() {
        int var1 = 0;

        int var2;
        for (var2 = 0; var2 < 10; ++var2) {
            if (this.synthInstruments[var2] != null && this.synthInstruments[var2].duration + this.synthInstruments[var2].start > var1) {
                var1 = this.synthInstruments[var2].duration + this.synthInstruments[var2].start;
            }
        }

        if (var1 == 0) {
            return new byte[0];
        } else {
            var2 = 22050 * var1 / 1000;
            byte[] var3 = new byte[var2];

            for (int var4 = 0; var4 < 10; ++var4) {
                if (this.synthInstruments[var4] != null) {
                    int var5 = this.synthInstruments[var4].duration * 22050 / 1000;
                    int var6 = this.synthInstruments[var4].start * 22050 / 1000;
                    int[] var7 = this.synthInstruments[var4].synthesize(var5, this.synthInstruments[var4].duration);

                    for (int var8 = 0; var8 < var5; ++var8) {
                        int var9 = var3[var8 + var6] + (var7[var8] >> 8);
                        if ((var9 + 128 & -256) != 0) {
                            var9 = var9 >> 31 ^ 127;
                        }

                        var3[var8 + var6] = (byte) var9;
                    }
                }
            }

            return var3;
        }
    }

    public final PcmSound toPCMSound() {
        byte[] var1 = this.getSamples();
        return new PcmSound(var1, 22050 * this.start / 1000, 22050 * this.end / 1000);
    }

    final int getStart() {
        int start = 9999999;

        for (int var2 = 0; var2 < 10; ++var2) {
            if (this.synthInstruments[var2] != null && this.synthInstruments[var2].start / 20 < start) {
                start = this.synthInstruments[var2].start / 20;
            }
        }

        if (this.start < this.end && this.start / 20 < start) {
            start = this.start / 20;
        }

        if (start == 9999999 || start == 0) {
            return 0;
        } else {
            for (int var2 = 0; var2 < 10; ++var2) {
                if (this.synthInstruments[var2] != null) {
                    this.synthInstruments[var2].start -= start * 20;
                }
            }

            if (this.start < this.end) {
                this.start -= start * 20;
                this.end -= start * 20;
            }

            return start;
        }
    }

}

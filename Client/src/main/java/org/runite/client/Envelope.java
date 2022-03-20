package org.runite.client;

final class Envelope {

    int waveTable;
    int minInterval;
    int maxInterval;
    private int stages = 2;
    private int[] levels = new int[2];
    private int[] times = new int[2];
    private int phaseIndex;
    private int level;
    private int slope;
    private int nextTime;
    private int time;

    public Envelope() {
        this.times[0] = 0;
        this.times[1] = 65535;
        this.levels[0] = 0;
        this.levels[1] = 65535;
    }

    final int nextLevel(int duration) {
        if (this.time >= this.nextTime) {
            this.level = this.levels[this.phaseIndex++] << 15;
            if (this.phaseIndex >= this.stages) {
                this.phaseIndex = this.stages - 1;
            }

            this.nextTime = (int) ((double) this.times[this.phaseIndex] / 65536.0D * (double) duration);
            if (this.nextTime > this.time) {
                this.slope = ((this.levels[this.phaseIndex] << 15) - this.level) / (this.nextTime - this.time);
            }
        }

        this.level += this.slope;
        ++this.time;
        return this.level - this.slope >> 15;
    }

    final void reset() {
        this.nextTime = 0;
        this.phaseIndex = 0;
        this.slope = 0;
        this.level = 0;
        this.time = 0;
    }

    final void decodeStages(DataBuffer buffer) {
        this.stages = buffer.readUnsignedByte();
        this.times = new int[this.stages];
        this.levels = new int[this.stages];

        for (int var2 = 0; var2 < this.stages; ++var2) {
            this.times[var2] = buffer.readUnsignedShort();
            this.levels[var2] = buffer.readUnsignedShort();
        }
    }

    final void decode(DataBuffer buffer) {
        this.waveTable = buffer.readUnsignedByte();
        this.minInterval = buffer.readInt();
        this.maxInterval = buffer.readInt();
        this.decodeStages(buffer);
    }
}

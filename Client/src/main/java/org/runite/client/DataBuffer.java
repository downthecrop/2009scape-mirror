package org.runite.client;

import kotlin.text.Charsets;
import org.rs09.client.Linkable;
import org.rs09.client.config.GameConfig;
import org.rs09.client.util.ByteArrayPool;
import org.rs09.client.util.CRC;

import java.math.BigInteger;
import java.util.Objects;

public class DataBuffer extends Linkable {

    public byte[] buffer;
    public int index;

    public DataBuffer(int capacity) {
        this.buffer = ByteArrayPool.INSTANCE.getByteArray(capacity);
        this.index = 0;
    }
    public DataBuffer(byte[] buffer) {
        this.index = 0;
        this.buffer = buffer;
    }

    public final int readUnsignedShort() {
        this.index += 2;
        return (this.buffer[-2 + this.index] << 8 & 0xff00) + (this.buffer[-1 + this.index] & 255);
    }

    public final void writeInt(int value) {
        this.buffer[this.index++] = (byte) (value >> 24);
        this.buffer[this.index++] = (byte) (value >> 16);
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) value;
    }

    final void writeLong(long value) {
        this.buffer[this.index++] = (byte) ((int) (value >> 56));
        this.buffer[this.index++] = (byte) ((int) (value >> 48));
        this.buffer[this.index++] = (byte) ((int) (value >> 40));
        this.buffer[this.index++] = (byte) ((int) (value >> 32));
        this.buffer[this.index++] = (byte) ((int) (value >> 24));
        this.buffer[this.index++] = (byte) ((int) (value >> 16));
        this.buffer[this.index++] = (byte) ((int) (value >> 8));
        this.buffer[this.index++] = (byte) ((int) value);
    }

    // TODO Rename / refactor
    final void method739(int var2, long var3) {
        --var2;
        if (var2 >= 0 && var2 <= 7) {
            for (int var5 = var2 * 8; 0 <= var5; var5 -= 8) {
                this.buffer[this.index++] = (byte) ((int) (var3 >> var5));
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    // TODO Rename / refactor
    final int method741() {
        byte var2 = this.buffer[this.index++];

        int var3;
        for (var3 = 0; 0 > var2; var2 = this.buffer[this.index++]) {
            var3 = (127 & var2 | var3) << 7;
        }

        return var2 | var3;
    }

    // TODO Rename / refactor
    final void method742(int var2) {
        this.buffer[this.index + -var2 - 4] = (byte) (var2 >> 24);
        this.buffer[this.index + -var2 - 3] = (byte) (var2 >> 16);
        this.buffer[this.index + -var2 - 2] = (byte) (var2 >> 8);
        this.buffer[this.index + -var2 - 1] = (byte) var2;
    }

    final void write128Byte(int value) {
        this.buffer[this.index++] = (byte) (128 - value);
    }

    // TODO Rename / refactor
    public final void writeString(RSString var2) {
        this.index += var2.method1580(this.buffer, this.index, var2.length());
        this.buffer[this.index++] = 0;
    }

    public final void writeString(String string) {
        byte[] bytes = string.getBytes(Charsets.ISO_8859_1);
        System.arraycopy(bytes, 0, buffer, index, bytes.length);
        index += bytes.length;
        buffer[index++] = 0;
    }

    final int readSignedShort128() {
        this.index += 2;
        int value = ((this.buffer[this.index - 2] & 0xff) << 8) +
                (this.buffer[this.index - 1] - 128 & 0xff);

        if (value > 32767) {
            value -= 65536;
        }

        return value;
    }

    public final int readInt() {
        this.index += 4;
        return ((this.buffer[this.index - 4] & 0xff) << 24) +
                ((this.buffer[this.index - 3] & 0xff) << 16) +
                ((this.buffer[this.index - 2] & 0xff) << 8) +
                (this.buffer[this.index - 1] & 255);
    }

    final byte readSigned128Byte() {
        return (byte) (128 - this.buffer[this.index++]);
    }

    // TODO Refactor / rename method
    final RSString method750() {
        if (this.buffer[this.index] == 0) {
            this.index++;
            return null;
        } else {
            return this.readString();
        }
    }

    final int readUnsignedByte128() {
        return this.buffer[this.index++] - 128 & 0xff;
    }

    public final void writeByte(int value) {
        this.buffer[this.index++] = (byte) value;
    }

    final void putBytes(byte[] bytes, int length) {
        int i = 0;
        while (length > i) {
            this.buffer[this.index++] = bytes[i];
            i++;
        }
    }

    final int readUnsigned128Byte() {
        return 128 - this.buffer[this.index++] & 255;
    }

    // TODO
    final int getTriByte2() {
        try {
            this.index += 3;
            return ((this.buffer[this.index + -2] & 255) << 8) + ((this.buffer[-1 + this.index] & 255) << 16) + (this.buffer[-3 + this.index] & 255);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "wa.BA(" + (byte) -118 + ')');
        }
    }

    final long readLong() {
        long msb = (long) this.readInt() & 0xFFFFFFFFL;
        long lsb = (long) this.readInt() & 0xFFFFFFFFL;
        return lsb + (msb << 32);
    }

    final void writeIntLE(int value) {
        this.buffer[this.index++] = (byte) value;
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) (value >> 16);
        this.buffer[this.index++] = (byte) (value >> 24);
    }

    final int readUnsignedShort128() {
        this.index += 2;
        return (this.buffer[this.index - 1] - 128 & 0xff)
                + ('\uff00' & this.buffer[this.index - 2] << 8);
    }

    final void writeIntV2(int value) {
        this.buffer[this.index++] = (byte) (value >> 16);
        this.buffer[this.index++] = (byte) (value >> 24);
        this.buffer[this.index++] = (byte) value;
        this.buffer[this.index++] = (byte) (value >> 8);
    }

    final byte readSignedByte() {
        return this.buffer[this.index++];
    }

    // TODO
    final RSString getGJString2(int var1) {
        try {
            byte var2 = this.buffer[this.index++];
            if (var1 < 50) {
                this.buffer = null;
            }

            if (0 == var2) {
                int var3 = this.index;

                while (Objects.requireNonNull(this.buffer)[this.index++] != 0) {
                }

                return Class3_Sub13_Sub3.bufferToString(this.buffer, this.index - (var3 - -1), var3);
            } else {
                throw new IllegalStateException("Bad version number in gjstr2");
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "wa.DA(" + var1 + ')');
        }
    }

    final void writeFloatLE(float value) {
        int bits = Float.floatToRawIntBits(value);

        this.buffer[this.index++] = (byte) bits;
        this.buffer[this.index++] = (byte) (bits >> 8);
        this.buffer[this.index++] = (byte) (bits >> 16);
        this.buffer[this.index++] = (byte) (bits >> 24);
    }

    final byte readSignedNegativeByte() {
        return (byte) -this.buffer[this.index++];
    }

    final void readBytes(byte[] out, int length) {
        for (int i = 0; length > i; ++i) {
            out[i] = this.buffer[this.index++];
        }
    }

    final void writeShort128LE(int value) {
        this.buffer[this.index++] = (byte) (value + 128);
        this.buffer[this.index++] = (byte) (value >> 8);
    }

    final int readUnsignedShortLE() {
        this.index += 2;
        return (255 & this.buffer[this.index - 2])
                + ('\uff00' & this.buffer[this.index - 1] << 8);
    }

    // TODO Some smart
    final void method768(int var2) {
        if (0 <= var2 && var2 < 128) {
            this.writeByte(var2);
        } else if (0 <= var2 && var2 < 32768) {
            this.writeShort('\u8000' + var2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // TODO finish varbyte length?
    final void method769(int value) {
        this.buffer[-1 + -value + this.index] = (byte) value;
    }

    public final void finishVarshortPacket(int value) {
        this.buffer[this.index + -value - 2] = (byte) (value >> 8);
        this.buffer[this.index + -value - 1] = (byte) value;
    }

    // TODO XTEA crypt?
    final void method770(int[] var1, int var4) {
        int var5 = this.index;
        this.index = 5;
        int var6 = (-5 + var4) / 8;

        for (int var7 = 0; var6 > var7; ++var7) {
            int var10 = -957401312;
            int var8 = this.readInt();
            int var9 = this.readInt();
            int var12 = 32;

            for (int var11 = -1640531527; var12-- > 0; var8 -= (var9 >>> 5 ^ var9 << 4) + var9 ^ var1[var10 & 3] + var10) {
                var9 -= var1[(6754 & var10) >>> 11] + var10 ^ var8 + (var8 >>> 5 ^ var8 << 4);
                var10 -= var11;
            }

            this.index -= 8;
            this.writeInt(var8);
            this.writeInt(var9);
        }

        this.index = var5;
    }

    // TODO
    final void method771(int var2) {
        if ((-128 & var2) != 0) {
            if ((-16384 & var2) != 0) {
                if ((var2 & -2097152) != 0) {
                    if (0 != (-268435456 & var2)) {
                        this.writeByte(var2 >>> 28 | 128);
                    }

                    this.writeByte(128 | var2 >>> 21);
                }

                this.writeByte(128 | var2 >>> 14);
            }

            this.writeByte(var2 >>> 7 | 128);
        }

        this.writeByte(var2 & 127);
    }

    // TODO
    final long method772(int var1) {
        --var1;
        if (0 <= var1 && var1 <= 7) {
            long var4 = 0L;

            for (int var3 = var1 * 8; var3 >= 0; var3 -= 8) {
                var4 |= ((long) this.buffer[this.index++] & 255L) << var3;
            }

            return var4;
        } else {
            throw new IllegalArgumentException();
        }
    }

    // TODO
    final int method773() {
        int var3 = this.getSmart();

        int var2;
        for (var2 = 0; 32767 == var3; var2 += 32767) {
            var3 = this.getSmart();
        }

        var2 += var3;
        return var2;
    }

    final void readBytesReverse(int length, byte[] out) {
        for (int i = -(-length - -1); i >= 0; --i) {
            out[i] = this.buffer[this.index++];
        }
    }

    final void writeIntV1(int value) {
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) value;
        this.buffer[this.index++] = (byte) (value >> 24);
        this.buffer[this.index++] = (byte) (value >> 16);
    }

    public final RSString readString() {
        int startIndex = this.index;

        while (this.buffer[this.index++] != 0) ;

        return Class3_Sub13_Sub3.bufferToString(this.buffer, (this.index - 1) - startIndex, startIndex);
    }

    final int getSmart() {
        int var2 = this.buffer[this.index] & 255;
        return 128 <= var2 ? -32768 + this.readUnsignedShort() : this.readUnsignedByte();
    }

    final void writeMedium(int value) {
        this.buffer[this.index++] = (byte) (value >> 16);
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) value;
    }

    final int readIntV1() {
        this.index += 4;
        return ((this.buffer[this.index - 2] & 255) << 24) +
                ((this.buffer[this.index - 1] & 255) << 16) +
                ('\uff00' & this.buffer[this.index - 4] << 8) +
                (this.buffer[this.index + -3] & 255);
    }

    final int readUnsignedShortLE128() {
        this.index += 2;
        return (this.buffer[this.index - 1] << 8 & '\uff00') +
                (255 & this.buffer[this.index - 2] - 128);
    }

    final int readIntLE() {
        this.index += 4;
        return (255 & this.buffer[-4 + this.index]) +
                (16711680 & this.buffer[this.index - 2] << 16) +
                ((255 & this.buffer[this.index + -1]) << 24) +
                ((this.buffer[-3 + this.index] & 255) << 8);
    }

    final void putShortA(int var1) {
        this.buffer[this.index++] = (byte) (var1 >> 8);
        this.buffer[this.index++] = (byte) (128 + var1);
    }

    // TODO What is the difference between this and WriteIntLE?
    final void writeIntLE2(int value) {
        this.buffer[this.index++] = (byte) value;
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) (value >> 16);
        this.buffer[this.index++] = (byte) (value >> 24);
    }

    final int readUnsignedNegativeByte() {
        return -this.buffer[this.index++] & 0xff;
    }

    final int readSignedShort() {
        this.index += 2;
        int value = (this.buffer[-1 + this.index] & 255) + ((255 & this.buffer[this.index + -2]) << 8);
        if (value > 32767) {
            value -= 65536;
        }
        return value;
    }

    final int readSignedShortLE128() {
        this.index += 2;
        int var2 = ((this.buffer[this.index - 1] & 255) << 8) + (this.buffer[-2 + this.index] - 128 & 255);

        if (32767 < var2) {
            var2 -= 65536;
        }

        return var2;
    }

    final byte readSignedByte128() {
        return (byte) (this.buffer[this.index++] - 128);
    }

    final void writeByte128(int value) {
        this.buffer[this.index++] = (byte) (128 + value);
    }

    final int readSignedShortLE() {
        this.index += 2;

        int value = (this.buffer[-2 + this.index] & 255) + ('\uff00' & this.buffer[this.index + -1] << 8);
        if (value > 32767) {
            value -= 65536;
        }

        return value;
    }

    final void writeCRC(int startOffset) {
        int crc = CRC.INSTANCE.crc32(this.buffer, startOffset, this.index);
        this.writeInt(crc);
    }

    public final int readMedium() {
        this.index += 3;
        return (16711680 & this.buffer[this.index + -3] << 16) +
                (('\uff00' & this.buffer[-2 + this.index] << 8) +
                        (this.buffer[this.index + -1] & 255));
    }

    final void writeShortLE(int value) {
        this.buffer[this.index++] = (byte) value;
        this.buffer[this.index++] = (byte) (value >> 8);
    }

    // TODO
    final int getSmart(int var1) {
        int var2 = this.buffer[this.index] & 255;
        return var2 < 128 ? -64 + this.readUnsignedByte() : this.readUnsignedShort() - '\uc000';
    }

    final int readIntV2() {
        this.index += 4;
        return ((this.buffer[this.index + -3] & 255) << 24) +
                (16711680 & this.buffer[this.index + -4] << 16) +
                ((this.buffer[this.index + -1] & 255) << 8) +
                (255 & this.buffer[this.index + -2]);
    }

    final void rsaEncrypt(BigInteger var1, BigInteger var2) {
        int var4 = this.index;
        this.index = 0;
        byte[] var5 = new byte[var4];
        this.readBytes(var5, var4);
        BigInteger var6 = new BigInteger(var5);
        BigInteger var7;
        if (GameConfig.RSA)
            var7 = var6.modPow(var1, var2);
        else
            var7 = var6;
        byte[] var8 = var7.toByteArray();
        this.index = 0;
        this.writeByte(var8.length);
        this.putBytes(var8, var8.length);
    }

    final void writeFloat(float value) {
        int bits = Float.floatToRawIntBits(value);

        this.buffer[this.index++] = (byte) (bits >> 24);
        this.buffer[this.index++] = (byte) (bits >> 16);
        this.buffer[this.index++] = (byte) (bits >> 8);
        this.buffer[this.index++] = (byte) bits;
    }

    public final int readUnsignedByte() {
        return this.buffer[this.index++] & 255;
    }

    public final void writeShort(int value) {
        this.buffer[this.index++] = (byte) (value >> 8);
        this.buffer[this.index++] = (byte) value;
    }

}

package org.arios.cache.misc;

public abstract class Stream {

	protected int offset;
	protected int length;
	protected byte[] buffer;
	protected int bitPosition;
	
	public int getLength() {
		return length;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public final void decodeXTEA(int keys[], int start, int end) {
		int l = offset;
		offset = start;
		int i1 = (end - start) / 8;
		for (int j1 = 0; j1 < i1; j1++) {
			int k1 = readInt();
			int l1 = readInt();
			int sum = 0xc6ef3720;
			int delta = 0x9e3779b9;
			for (int k2 = 32; k2-- > 0;) {
				l1 -= keys[(sum & 0x1c84) >>> 11] + sum ^ (k1 >>> 5 ^ k1 << 4)
						+ k1;
				sum -= delta;
				k1 -= (l1 >>> 5 ^ l1 << 4) + l1 ^ keys[sum & 3] + sum;
			}

			offset -= 8;
			writeInt(k1);
			writeInt(l1);
		}
		offset = l;
	}
	
	private final int readInt() {
		offset += 4;
		return ((0xff & buffer[-3 + offset]) << 16)
				+ ((((0xff & buffer[-4 + offset]) << 24) + ((buffer[-2
						+ offset] & 0xff) << 8)) + (buffer[-1 + offset] & 0xff));
	}
	

    public void readBytes(byte abyte0[], int i, int j) {
        for(int k = j; k < j + i; k++){
            abyte0[k] = buffer[offset++];
        }
    }
	private final void writeInt(int value) {
		buffer[offset++] = (byte) (value >> 24);
		buffer[offset++] = (byte) (value >> 16);
		buffer[offset++] = (byte) (value >> 8);
		buffer[offset++] = (byte) value;
	}
	
	public final void getBytes(byte data[], int off, int len) {
		for (int k = off; k < len + off; k++) {
			data[k] = buffer[offset++];
		}
	}
}

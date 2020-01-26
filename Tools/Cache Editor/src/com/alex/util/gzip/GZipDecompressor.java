package com.alex.util.gzip;

import java.util.zip.Inflater;

import com.alex.io.Stream;

public class GZipDecompressor {

	private static final Inflater inflaterInstance = new Inflater(true);
	
	public static final boolean decompress(Stream stream, byte data[]) {
		synchronized(inflaterInstance) {
			if (stream.getBuffer()[stream.getOffset()] != 31 || stream.getBuffer()[stream.getOffset() + 1] != -117)
				return false;
				//throw new RuntimeException("Invalid GZIP header!");
			try {
				inflaterInstance.setInput(stream.getBuffer(), stream.getOffset() + 10, -stream.getOffset() - 18 + stream.getBuffer().length);
				inflaterInstance.inflate(data);
			} catch (Exception e) {
				inflaterInstance.reset();
				return false;
				//throw new RuntimeException("Invalid GZIP compressed data!");
			}
			inflaterInstance.reset();
			return true;
		}
	}
	
	public static final boolean decompress(byte[] compressed, byte data[], int offset, int length) {
		synchronized(inflaterInstance) {
			if (data[offset] != 31 || data[offset + 1] != -117)
				return false;
				//throw new RuntimeException("Invalid GZIP header!");
			try {
				inflaterInstance.setInput(data, offset + 10, -offset - 18 + length);
				inflaterInstance.inflate(compressed);
			} catch (Exception e) {
				inflaterInstance.reset();
				e.printStackTrace();
				return false;
				//throw new RuntimeException("Invalid GZIP compressed data!");
			}
			inflaterInstance.reset();
			return true;
		}
	}
	
}

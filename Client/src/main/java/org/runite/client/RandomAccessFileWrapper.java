package org.runite.client;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class RandomAccessFileWrapper {

    private final long limit;
    private final File path;
    private RandomAccessFile raf;
    private long pos;


    public final void seek(long pos) throws IOException {
        this.raf.seek(pos);
        this.pos = pos;
    }

    protected void finalize() throws Throwable {
        if (this.raf != null) {
            System.out.println("Warning! fileondisk " + this.path + " not closed correctly using close(). Auto-closing instead. ");
            this.close();
        }
    }

    public final void write(byte[] buffer, int length, int offset) throws IOException {
        if (this.limit < this.pos + (long) length) {
            this.raf.seek(1L + this.limit);
            this.raf.write(1);
            throw new EOFException();
        } else {
            this.raf.write(buffer, offset, length);
            this.pos += length;
        }
    }

    public final int read(byte[] buffer, int offset, int length, int minBytesRead) throws IOException {
        int bytesRead = this.raf.read(buffer, offset, length);
        if (bytesRead > minBytesRead) {
            this.pos += bytesRead;
        }

        return bytesRead;
    }

    public final void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
            this.raf = null;
        }
    }

    public final long getLength() throws IOException {
        return this.raf.length();
    }

    public final File getPath() {
        return this.path;
    }

    public RandomAccessFileWrapper(File path, String mode, long limit) throws IOException {
        if (limit == -1) {
            limit = Long.MAX_VALUE;
        }

        if (path.length() >= limit) {
            path.delete();
        }

        this.raf = new RandomAccessFile(path, mode);
        this.path = path;
        this.limit = limit;
        this.pos = 0L;
        int var5 = this.raf.read();
        if (var5 != -1 && !mode.equals("r")) {
            this.raf.seek(0L);
            this.raf.write(var5);
        }

        this.raf.seek(0L);
    }
}

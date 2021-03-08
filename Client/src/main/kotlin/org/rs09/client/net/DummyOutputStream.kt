package org.rs09.client.net

import java.io.IOException
import java.io.OutputStream

class DummyOutputStream: OutputStream() {
    override fun write(b: Int) {
        throw IOException("Attempted to write to DummyOutputStream")
    }
}
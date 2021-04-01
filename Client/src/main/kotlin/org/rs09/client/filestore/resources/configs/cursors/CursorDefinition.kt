package org.rs09.client.filestore.resources.configs.cursors

import org.runite.client.*

class CursorDefinition {
    var hotspotX = 0
    var hotspotY = 0
    var imageId = 0

    fun getImage(): SoftwareSprite {
        var image = Class163_Sub1.aReferenceCache_2984.get(imageId.toLong()) as? SoftwareSprite
        if (image != null) return image

        image = Unsorted.method562(SequenceDefinition.aClass153_1852, imageId)
        if (image != null) Class163_Sub1.aReferenceCache_2984.put(image, imageId.toLong())
        return image
    }

    fun decode(buffer: DataBuffer) {
        while (true) {
            val opcode = buffer.readUnsignedByte()
            if (opcode == 0) break

            when (opcode) {
                1 -> imageId = buffer.readUnsignedShort()
                2 -> {
                    hotspotX = buffer.readUnsignedByte()
                    hotspotY = buffer.readUnsignedByte()
                }
                else -> throw IllegalArgumentException("unknown CursorDefinition opcode $opcode")
            }
        }
    }
}
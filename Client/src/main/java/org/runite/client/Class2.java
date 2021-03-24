package org.runite.client;

// TODO MapSceneDefinition or something
public final class Class2 {

    public int color;
    public int sprite;
    public boolean aBoolean69 = false;

    public final void decode(DataBuffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            this.decode(buffer, opcode);
        }
    }

    /**
     * @param rotations The amount of rotations the sprite should be turned clockwise. Each rotation rotates the sprite
     *                  by 90 degrees clockwise.
     */
    public final LDIndexedSprite getSprite(int rotations) {
        LDIndexedSprite sprite = (LDIndexedSprite) TextureOperation2.aReferenceCache_3369.get(rotations << 16 | this.sprite);
        if (sprite != null) {
            return sprite;
        }

        KeyboardListener.aClass153_1916.retrieveSpriteFile(this.sprite);
        sprite = Unsorted.method1539(this.sprite, KeyboardListener.aClass153_1916);
        if (sprite != null) {
            sprite.method1668(Class102.anInt2136, Class46.anInt740, Class158.anInt2015);
            sprite.anInt1469 = sprite.width;
            sprite.anInt1467 = sprite.height;

            for (int var5 = 0; var5 < rotations; ++var5) {
                sprite.rotateClockwise();
            }

            TextureOperation2.aReferenceCache_3369.put(sprite, rotations << 16 | this.sprite);
        }

        return sprite;
    }

    private void decode(DataBuffer buffer, int opcode) {
        if (opcode == 1) {
            this.sprite = buffer.readUnsignedShort();
        } else if (opcode == 2) {
            this.color = buffer.readMedium();
        } else if (opcode == 3) {
            this.aBoolean69 = true;
        } else if (opcode == 4) {
            this.sprite = -1;
        }
    }

}

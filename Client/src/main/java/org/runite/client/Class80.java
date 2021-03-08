package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.HashTable;

final class Class80<T extends Linkable> {

    private Linkable aClass3_1130;
    private int anInt1132 = 0;
    private final HashTable<T> table;

    @SuppressWarnings("unchecked")
    final T method1392() {
        Linkable var2;
        if (this.anInt1132 > 0 && this.table.getBuckets()[this.anInt1132 + -1] != this.aClass3_1130) {
            var2 = this.aClass3_1130;
        } else {
            do {
                if (this.anInt1132 >= this.table.getCapacity()) {
                    return null;
                }

                var2 = this.table.getBuckets()[this.anInt1132++].next;
            } while (var2 == this.table.getBuckets()[-1 + this.anInt1132]);

        }
        this.aClass3_1130 = var2.next;
        return (T) var2;
    }

    final T method1393() {
        this.anInt1132 = 0;
        return this.method1392();
    }

    Class80(HashTable<T> var1) {
        this.table = var1;
    }

}

package org.runite.client;

import org.rs09.client.data.reference.ObjectReference;
import org.rs09.client.data.reference.SoftObjectReference;

public final class Class118_Sub1 extends Class118 {

   public final <T> ObjectReference<T> method1725(ObjectReference<T> var1) {
      return new SoftObjectReference<>(var1.getValue());
   }
}

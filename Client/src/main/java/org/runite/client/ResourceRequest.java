package org.runite.client;

import org.rs09.client.Node;

abstract class ResourceRequest extends Node {

   boolean priority;
   boolean aBoolean3635;
   volatile boolean waiting = true;

   abstract int getCompletion();

   abstract byte[] getData();

}

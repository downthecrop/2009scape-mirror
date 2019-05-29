package org.crandor.tools;

import org.crandor.cache.Cache;
import org.crandor.cache.def.impl.ItemDefinition;


public class Test {
	
	//@SuppressWarnings("unused")
	public static void main(String...args) {
		Cache.init();
	/*	for (int i = 0; i < Cache.getNPCDefinitionsSize(); i++) {
			if (NPCDefinition.forId(i).getName().toLowerCase().contains("beaver")) {
				//System.out.println(i + ", " + NPCDefinition.forId(i).getName());
			}
		}
		System.out.println("---Objects----");
		for (int i = 0; i < Cache.getObjectDefinitionsSize(); i++) {
			ObjectDefinition def = ObjectDefinition.forId(i);
			if (def.getName().toLowerCase().contains("snow")) {
				//System.out.println(i + ", " + def.getName() + ", " + Arrays.toString(def.getOptions()));
			}
		}*/
		System.out.println("------Items-------");
		for (int i = 0; i < Cache.getItemDefinitionsSize(); i++) {
			ItemDefinition def = ItemDefinition.forId(i);
			System.out.println(def.getName() + ", " + i);
		}
	}
}

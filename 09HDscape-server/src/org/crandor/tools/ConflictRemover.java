package org.crandor.tools;

import java.io.File;

/**
 * Removes conflicted copies from the source (fuck you dropbox).
 * @author Emperor
 *
 */
public final class ConflictRemover {

	/**
	 * The main method.
	 * @param args The arguments cast on runtime.
	 */
	public static void main(String[] args) {
		clean(new File("./"));
	}
	
	/**
	 * Cleans the files in the given directory.
	 * @param dir The directory file.
	 */
	private static void clean(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				clean(f);
				continue;
			}
			if (f.getName().contains("met conflict van") || f.getName().contains("conflicted ")) {
				if (f.delete()) {
					System.out.println("Removed " + f.getName() + "!");
				}
			}
		}
	}

}
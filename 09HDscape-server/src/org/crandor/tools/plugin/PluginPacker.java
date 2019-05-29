package org.crandor.tools.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;

import org.crandor.plugin.PluginManager;

/**
 * Packs all plugins.
 * @author Emperor
 */
public final class PluginPacker {

	/**
	 * The amount of packed plugins.
	 */
	private static int packAmount = 0;

	/**
	 * The amount of plugins that failed to be packed.
	 */
	private static int failAmount = 0;

	/**
	 * The main method.
	 * @param args The arguments cast on run-time.
	 * @throws Throwable When an exception occurs.
	 */
	public static void main(String[] args) throws Throwable {
		packPlugins(new File("./plugin/"));
		System.out.println("Finished - packed " + packAmount + " plugins; failed to pack " + failAmount + ".");
	}

	/**
	 * Packs the plugins in the given directory.
	 * @param dir The directory.
	 * @throws Throwable When an exception occurs.
	 */
	private static void packPlugins(File dir) throws Throwable {
		for (File plugin : dir.listFiles()) {
			if (plugin.getPath().contains("quest")) {
				continue;
			}
			if (plugin.isDirectory()) {
				packPlugins(plugin);
				continue;
			}
			if (!plugin.getName().contains(".jar") || PluginManager.isAutoPacked(plugin.getName())) {
				continue;
			}
			String name = plugin.getName().replace(".jar", "");
			int count = 0;
			JarFile jar = null;
			try {
				jar = new JarFile(plugin);
			} catch (ZipException exception) {
				System.err.println("Error for file " + name + ", " + plugin.getAbsolutePath());
				exception.printStackTrace();
			}
			Enumeration<JarEntry> entries = jar.entries();
			boolean missing = false;
			List<String> classes = new ArrayList<>();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
					File clazz = new File("./bin/" + entry.getName());
					if (!clazz.exists()) {
						System.err.println("Missing class " + entry.getName() + "!");
						missing = true;
						break;
					}
					String className = entry.getName().substring(entry.getName().lastIndexOf("/") + 1, entry.getName().length()).replace(".class", "");
					for (File f : new File("./bin/" + entry.getName().substring(0, entry.getName().indexOf(className))).listFiles()) {
						if (!f.isDirectory() && f.getName().startsWith(className)) {
							String pckg = entry.getName().substring(0, entry.getName().indexOf(className));
							if (f.getName().contains("$") || f.getName().startsWith(className + ".")) {
								String entryName = pckg + f.getName();
								classes.add(entryName);
								count++;
							} else {
								System.err.println("Couldn't find class " + pckg + f.getName() + ", " + className + " in plugin " + plugin.getPath());
							}
						}
					}
				}
			}
			jar.close();
			if (missing || count < 1) {
				failAmount++;
				System.err.println("Failed to pack plugin " + name + "!");
				continue;
			}
			FileOutputStream out = new FileOutputStream(plugin);
			JarOutputStream stream = new JarOutputStream(out);
			for (String clazz : classes) {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream("./bin/" + clazz));
				byte[] bs = new byte[1 << 10];
				stream.putNextEntry(new JarEntry(clazz));
				while (true) {
					int written = 0;
					if ((written = in.read(bs)) == -1) {
						break;
					}
					stream.write(bs, 0, written);
				}
				in.close();
				stream.closeEntry();
			}
			stream.close();
			packAmount++;
			// System.out.println("Packed plugin \"" + name +
			// "\" - class count: " + count + ".");
		}
	}
}
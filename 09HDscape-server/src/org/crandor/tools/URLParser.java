package org.crandor.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the parsing of a website.
 * @author Emperor
 */
public class URLParser {

	/**
	 * The maximum timeout in milliseconds.
	 */
	private static final int TIMEOUT = 2000;

	/**
	 * The countdown latch to use.
	 */
	private final CountDownLatch latch;

	/**
	 * The list of the URLs we have to parse.
	 */
	private final List<String> urls;

	/**
	 * The executor service to use.
	 */
	private final ExecutorService service;

	/**
	 * The data mapping.
	 */
	private final Map<String, List<String>> data = new HashMap<String, List<String>>();

	/**
	 * The start index of the strings to read.
	 */
	private final int startIndex;

	/**
	 * If the URL parser is done parsing.
	 */
	private boolean finished;

	/**
	 * Constructs a new {@code URLParser} {@code Object}.
	 * @param urls The list of the urls we have to parse.
	 * @param startIndex The start index of the strings to read.
	 */
	public URLParser(List<String> urls, int startIndex) {
		this(urls, startIndex, Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Constructs a new {@code URLParser} {@code Object}.
	 * @param urls The list of the urls we have to parse.
	 * @param startIndex The start index of the strings to read.
	 * @param threadCount The amount of threads to use.
	 */
	public URLParser(List<String> urls, int startIndex, int threadCount) {
		this.urls = urls;
		this.latch = new CountDownLatch(urls.size());
		this.startIndex = startIndex;
		this.service = Executors.newFixedThreadPool(threadCount);
	}

	/**
	 * Parses the websites and puts them on the data mapping. When this method
	 * is done all URLs have been parsed and the {@link #finished} boolean is
	 * set to true.
	 */
	public void parse() {
		for (final String url : urls) {
			service.submit(new Runnable() {
				@Override
				public void run() {
					if (URLParser.this.data.containsKey(url)) {
						latch.countDown();
						return;
					}
					URLConnection resource;
					InputStream is = null;
					BufferedReader reader = null;
					try {
						resource = new URL(url).openConnection();
						resource.setConnectTimeout(TIMEOUT);
						is = resource.getInputStream();
						reader = new BufferedReader(new InputStreamReader(is));
						List<String> data = new ArrayList<String>();
						String s;
						int count = 0;
						while ((s = reader.readLine()) != null) {
							if (count++ < startIndex) {
								continue;
							}
							data.add(s);
						}
						URLParser.this.data.put(url, data);
						if (latch.getCount() % 100 == 0) {
							System.out.println("Dumped " + (urls.size() - latch.getCount()) + " definitions.");
						}
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					latch.countDown();
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finished = true;
	}

	/**
	 * Terminates the url parser.
	 */
	public void terminate() {
		service.shutdown();
		urls.clear();
		data.clear();
		setFinished(true);
	}

	/**
	 * Gets the finished.
	 * @return The finished.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Sets the finished.
	 * @param finished The finished to set.
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Gets the data.
	 * @return The data.
	 */
	public Map<String, List<String>> getData() {
		if (!finished) {
			throw new IllegalStateException("Parser has not finished!");
		}
		return data;
	}
}
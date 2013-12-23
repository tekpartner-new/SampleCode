package net.tekpartner.sample.coherence.cacheanddatabase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.extractor.IdentityExtractor;
import com.tangosol.util.filter.LikeFilter;

public class DatabaseCacheWithDBDown {
	NamedCache cache;
	static int delayInSeconds = 120;
	static int firstDelayInSeconds = Double.valueOf(delayInSeconds)
			.intValue();
	static int secondDelayInSeconds = Double.valueOf(delayInSeconds * 0.2)
			.intValue();
	static int firstDelayInMilliSeconds = Double.valueOf(
			delayInSeconds * 0.8 * 1000).intValue();
	static int secondDelayInMilliSeconds = Double.valueOf(
			delayInSeconds * 0.2 * 1000).intValue();

	public DatabaseCacheWithDBDown() {
	}

	public void createCache() {
		cache = CacheFactory.getCache("DBBackedCache");
	}

	public void loadEntry() {
		System.out.println("Begin -> loadEntry()");

		HashSet hashSet = new HashSet();
		hashSet.add(new String("catalog1"));
		hashSet.add(new String("catalog2"));
		cache.getAll(hashSet);
		System.out.println("End -> loadEntry()");
	}

	public void addEntry() {
		System.out.println("Begin -> addEntry()");
		cache.put(new String("catalog3"), new String("Tuning Grid Management"));
		cache.put(new String("catalog4"), new String("Tuning Coherence"));
		cache.put(new String("catalog5"), new String("Tuning Database"));
		System.out.println("End -> addEntry()");
	}

	public void retrieveEntry() {
		System.out.println((String) cache.get("catalog3"));
	}

	public void eraseEntry() {
		cache.remove(new String("catalog3"));
	}

	public void queryCache() {
		Filter filter = new LikeFilter(IdentityExtractor.INSTANCE, "Tuning%",
				'\\', true);
		HashSet hashSet = new HashSet();
		hashSet.add(new String("catalog3"));
		hashSet.add(new String("catalog4"));
		hashSet.add(new String("catalog5"));
		Map map = cache.getAll(hashSet);
		Set results = cache.entrySet(filter);
		for (Iterator i = results.iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			System.out.println("Catalog ID: " + e.getKey() + ", Title: "
					+ e.getValue());
		}
	}

	public void updateEntry() {
		System.out.println("Begin -> updateEntry()");
		cache.put(new String("catalog3"), new String(
				"Tuning Grid Management - Update"));
		cache.put(new String("catalog4"), new String(
				"Tuning Coherence - Update"));
		cache.put(new String("catalog5"),
				new String("Tuning Database - Update"));
		System.out.println("End -> updateEntry()");
	}

	public static void main(String[] args) {
		DatabaseCacheWithDBDown databaseCache = new DatabaseCacheWithDBDown();
		databaseCache.createCache();
		databaseCache.loadEntry();
		databaseCache.queryCache();
		databaseCache.addEntry();
		databaseCache.queryCache();
		try {
			System.out.println(firstDelayInSeconds
					+ " Seconds before COMMIT of INSERT...........");
			Thread.sleep(firstDelayInMilliSeconds);
			System.out.println(secondDelayInSeconds
					+ " Seconds before COMMIT of INSERT...........");
			Thread.sleep(secondDelayInMilliSeconds);
			System.out.println("Confirm that the INSERT is COMMITTED.");
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		try {

			System.out
					.println((delayInSeconds * 0.7)
							+ " Seconds before START of Next Transaction. Bring Down the Database Now.");
			Thread.sleep(firstDelayInMilliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		databaseCache.updateEntry();
		databaseCache.queryCache();
		try {
			System.out.println(firstDelayInSeconds
					+ " Seconds before COMMIT of UPDATE...........");
			Thread.sleep(firstDelayInMilliSeconds);
			System.out.println(secondDelayInSeconds
					+ " Seconds before COMMIT of UPDATE...........");
			Thread.sleep(secondDelayInMilliSeconds);
			System.out.println("Confirm that the UPDATE is COMMITTED.");
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		// databaseCache.eraseEntry();
		// databaseCache.queryCache();
		// try {
		// System.out
		// .println("10 Seconds before COMMIT of DELETE for catalog3...........");
		// Thread.sleep(7000);
		// System.out
		// .println("3 Seconds before COMMIT of DELETE for catalog3...........");
		// Thread.sleep(4000);
		// System.out.println("Confirm that catalog3 is DELETED.");
		// } catch (InterruptedException ex) {
		// Thread.currentThread().interrupt();
		// }

		for (int i = 0; i < 100; i++) {
			System.out.println("5 Seconds before Cache Refresh...........");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

			databaseCache.queryCache();
		}
	}
}

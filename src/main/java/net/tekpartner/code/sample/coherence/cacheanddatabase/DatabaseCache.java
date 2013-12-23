package net.tekpartner.code.sample.coherence.cacheanddatabase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.extractor.IdentityExtractor;
import com.tangosol.util.filter.LikeFilter;

public class DatabaseCache {
	NamedCache cache;
	// NamedCache writeThroughCache;

	static int delayInSeconds = 10;
	static int firstDelayInSeconds = Double.valueOf(delayInSeconds).intValue();
	static int secondDelayInSeconds = Double.valueOf(delayInSeconds * 0.2)
			.intValue();
	static int firstDelayInMilliSeconds = Double.valueOf(
			delayInSeconds * 0.8 * 1000).intValue();
	static int secondDelayInMilliSeconds = Double.valueOf(
			delayInSeconds * 0.2 * 1000).intValue();

	public DatabaseCache() {
	}

	public void createCache(Enum cacheType) {
		if (cacheType.compareTo(CacheType.WRITE_BEHIND) == 0) {
			this.cache = CacheFactory.getCache("DBBackedCache-Write-Behind");
		} else if (cacheType.compareTo(CacheType.WRITE_THROUGH) == 0) {
			this.cache = CacheFactory.getCache("DBBackedCache-Write-Through");
		} else {
			this.cache = CacheFactory.getCache("DBBackedCache-Write-Through");
		}
	}

	private void loadEntry(HashSet hashSet) {
		System.out.println("Begin -> loadEntry()");

		hashSet.add(new String("catalog1"));
		hashSet.add(new String("catalog2"));
		cache.getAll(hashSet);
		System.out.println("End -> loadEntry()");
		System.out.println();
	}

	private void addEntries(NamedCache namedCache) {
		System.out.println("Begin -> addEntry()");
		namedCache.put(new String("catalog3"), new String(
				"Tuning Grid Management"));
		namedCache.put(new String("catalog4"), new String("Tuning Coherence"));
		namedCache.put(new String("catalog5"), new String("Tuning Database"));
		System.out.println("End -> addEntry()");
		System.out.println();
	}

	private void addAdditionalEntries(NamedCache namedCache) {
		System.out.println("Begin -> addAdditionalEntries()");
		namedCache.put(new String("catalog6"), new String(
				"Tuning Grid Management-6"));
		namedCache
				.put(new String("catalog7"), new String("Tuning Coherence-7"));
		namedCache.put(new String("catalog8"), new String("Tuning Database-8"));
		System.out.println("End -> addAdditionalEntries()");
		System.out.println();
	}

	private void retrieveEntry() {
		System.out.println((String) cache.get("catalog3"));
	}

	private void eraseEntry() {
		cache.remove(new String("catalog3"));
	}

	private void queryCache() {
		Set results = cache.entrySet();
		for (Iterator i = results.iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			System.out.println("Catalog ID: " + e.getKey() + ", Title: "
					+ e.getValue());
		}
		System.out.println();
	}

	private void queryCache(String pattern) {
		Filter filter = new LikeFilter(IdentityExtractor.INSTANCE, pattern,
				'\\', true);
		Set results = cache.entrySet();
		for (Iterator i = results.iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			System.out.println("Catalog ID: " + e.getKey() + ", Title: "
					+ e.getValue());
		}
		System.out.println();
	}

	private void updateValues(NamedCache namedCache) {
		System.out.println("Begin -> updateEntry()");
		namedCache.put(new String("catalog3"), new String(
				"Tuning Grid Management - Update"));
		namedCache.put(new String("catalog4"), new String(
				"Tuning Coherence - Update"));
		namedCache.put(new String("catalog5"), new String(
				"Tuning Database - Update"));
		System.out.println("End -> updateEntry()");
		System.out.println();
	}

	private void introduceDelay() {
		try {
			System.out.println("PAUSE..............");
			System.out.println(firstDelayInSeconds + " Seconds...........");
			Thread.sleep(firstDelayInMilliSeconds);
			System.out.println(secondDelayInSeconds + " Seconds...........");
			Thread.sleep(secondDelayInMilliSeconds);
			System.out.println("RESUME..............");
			System.out.println();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public void readExistingValues() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(new String("catalog1"));
		hashSet.add(new String("catalog2"));

		this.loadEntry(hashSet);

		this.queryCache();
	}

	public void writeFewValues() {
		this.addEntries(this.cache);

		this.queryCache();
	}

	public void writeFewAdditionalValues() {
		this.addAdditionalEntries(this.cache);

		this.queryCache();
	}

	public void updateFewValues() {
		this.updateValues(this.cache);

		this.queryCache();
	}

	public static void main(String[] args) {
		DatabaseCache databaseCache = new DatabaseCache();
		databaseCache.createCache(CacheType.WRITE_BEHIND);
		databaseCache.readExistingValues();
		databaseCache.writeFewValues();
		databaseCache.introduceDelay();
		
		try {
			System.out.println("PAUSE..............");
			Thread.sleep(firstDelayInMilliSeconds * 5);
			System.out.println("RESUME..............");
			System.out.println();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		
		
		databaseCache.writeFewAdditionalValues();
		databaseCache.introduceDelay();
//		databaseCache.updateFewValues();
//		databaseCache.introduceDelay();

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

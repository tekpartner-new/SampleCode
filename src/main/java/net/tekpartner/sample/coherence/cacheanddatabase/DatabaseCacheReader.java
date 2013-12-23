package net.tekpartner.sample.coherence.cacheanddatabase;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class DatabaseCacheReader {
	NamedCache cache;

	public DatabaseCacheReader() {
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

	public void loadEntry() {
		System.out.println("Begin -> loadEntry()");
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(new String("catalog1"));
		hashSet.add(new String("catalog2"));
		hashSet.add(new String("catalog3"));
		hashSet.add(new String("catalog4"));
		hashSet.add(new String("catalog5"));
		cache.getAll(hashSet);
		System.out.println("End -> loadEntry()");
		System.out.println();
	}

	public void queryCache() {
		Set results = cache.entrySet();
		for (Iterator i = results.iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			System.out.println("Catalog ID: " + e.getKey() + ", Title: "
					+ e.getValue());
		}
		System.out.println();
	}

	public static void main(String[] args) {
		DatabaseCacheReader databaseCache = new DatabaseCacheReader();
		databaseCache.createCache(CacheType.WRITE_BEHIND);
		databaseCache.loadEntry();

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

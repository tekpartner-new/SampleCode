package net.tekpartner.sample.coherence.helloworld;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class HelloWorld {

	public static void main(String[] args) {

		String key = "k1";
		String value = "Hello World!";

		CacheFactory.ensureCluster();
		NamedCache cache = CacheFactory.getCache("hello-example");

		cache.put(key, value);
		System.out.println((String) cache.get(key));

		CacheFactory.shutdown();
	}
}
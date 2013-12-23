package net.tekpartner.sample.coherence.cacheanddatabase;

import java.io.Serializable;
import java.util.Date;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.AbstractCacheStore;
import com.tangosol.util.Base;

public class Main extends Base {
	/**
	 * A cache controlled CacheStore implementation
	 */
	public static class ControllableCacheStore1 extends AbstractCacheStore {
		public static final String CONTROL_CACHE = "cachestorecontrol";
		String m_sName;

		public static void enable(String sName) {
			CacheFactory.getCache(CONTROL_CACHE).put(sName, Boolean.TRUE);
		}

		public static void disable(String sName) {
			CacheFactory.getCache(CONTROL_CACHE).put(sName, Boolean.FALSE);
		}

		public void store(Object oKey, Object oValue) {
			Boolean isEnabled = (Boolean) CacheFactory.getCache(CONTROL_CACHE)
					.get(m_sName);
			if (isEnabled != null && isEnabled.booleanValue()) {
				log("controllablecachestore1: enabled " + oKey + " = " + oValue);
			} else {
				log("controllablecachestore1: disabled " + oKey + " = "
						+ oValue);
			}
		}

		public Object load(Object oKey) {
			log("controllablecachestore1: load:" + oKey);
			return new MyValue1(oKey);
		}

		public ControllableCacheStore1(String sName) {
			m_sName = sName;
		}
	}

	/**
	 * a valued controlled CacheStore implementation that implements the
	 * CacheStoreAware interface
	 */
	public static class ControllableCacheStore2 extends AbstractCacheStore {
		public void store(Object oKey, Object oValue) {
			boolean isEnabled = oValue instanceof CacheStoreAware ? !((CacheStoreAware) oValue)
					.isSkipStore() : true;
			if (isEnabled) {
				log("controllablecachestore2: enabled " + oKey + " = " + oValue);
			} else {
				log("controllablecachestore2: disabled " + oKey + " = "
						+ oValue);
			}
		}

		public Object load(Object oKey) {
			log("controllablecachestore2: load:" + oKey);
			return new MyValue2(oKey);
		}
	}

	public static class MyValue1 implements Serializable {
		String m_sValue;

		public String getValue() {
			return m_sValue;
		}

		public String toString() {
			return "MyValue1[" + getValue() + "]";
		}

		public MyValue1(Object obj) {
			m_sValue = "value:" + obj;
		}
	}

	public static class MyValue2 extends MyValue1 implements CacheStoreAware {
		boolean m_isSkipStore = false;

		public boolean isSkipStore() {
			return m_isSkipStore;
		}

		public void skipStore() {
			m_isSkipStore = true;
		}

		public String toString() {
			return "MyValue2[" + getValue() + "]";
		}

		public MyValue2(Object obj) {
			super(obj);
		}
	}

	public static void main(String[] args) {
		try {
			// example 1
			NamedCache cache1 = CacheFactory.getCache("cache1");
			// disable cachestore
			ControllableCacheStore1.disable("cache1");
			for (int i = 0; i < 5; i++) {
				cache1.put(new Integer(i), new MyValue1(new Date()));
			}
			// enable cachestore
			ControllableCacheStore1.enable("cache1");
			for (int i = 0; i < 5; i++) {
				cache1.put(new Integer(i), new MyValue1(new Date()));
			}
			// example 2
			NamedCache cache2 = CacheFactory.getCache("cache2");
			// add some values with cachestore disabled
			for (int i = 0; i < 5; i++) {
				MyValue2 value = new MyValue2(new Date());
				value.skipStore();
				cache2.put(new Integer(i), value);
			}
			// add some values with cachestore enabled
			for (int i = 0; i < 5; i++) {
				cache2.put(new Integer(i), new MyValue2(new Date()));
			}
		} catch (Throwable oops) {
			err(oops);
		} finally {
			CacheFactory.shutdown();
		}
	}
}

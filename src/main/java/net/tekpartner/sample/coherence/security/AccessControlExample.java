/*
 * AccessControlExample.java
 *
 * Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle is a registered trademarks of Oracle Corporation and/or its
 * affiliates.
 *
 * This software is the confidential and proprietary information of Oracle
 * Corporation. You shall not disclose such confidential and proprietary
 * information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Oracle.
 *
 * This notice may not be removed or altered.
 */
package net.tekpartner.sample.coherence.security;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.util.MapEvent;
import com.tangosol.util.MapListener;

import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;

import net.tekpartner.sample.coherence.pof.ExampleInvocable;

/**
 * This class demonstrates simplified role based access control.
 * <p>
 * The role policies are defined in SecurityExampleHelper. Enforcmenent is done
 * by EntitledCacheService, EntitledNamedCache, and EntitledMapListener.
 * 
 * @author dag 2010.04.16
 */
@SuppressWarnings(value = "unchecked")
public class AccessControlExample {
	// ----- static methods -------------------------------------------------

	/**
	 * Demonstrate role based access to the cache.
	 */
	public static void accessCache() {
		System.out.println("------cache access control example begins------");

		Subject subject = SecurityExampleHelper.login("JohnWhorfin");

		// Someone with writer role can write and read
		try {
			NamedCache cache = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_NAME);
						}
					});
			cache.put("myKey", "myValue");
			cache.get("myKey");
			System.out.println("    Success: read and write allowed");
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			e.printStackTrace();
		}

		// Someone with reader role can read but not write
		subject = SecurityExampleHelper.login("JohnBigboote");
		try {
			NamedCache cache = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_NAME);
						}
					});
			cache.get("myKey");
			System.out.println("    Success: read allowed");
			cache.put("anotherKey", "anotherValue");
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			System.out.println("    Success: Correctly cannot write");
		}

		// Someone with writer role cannot call destroy
		subject = SecurityExampleHelper.login("JohnWhorfin");
		try {
			NamedCache cache = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_NAME);
						}
					});
			cache.destroy();
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			System.out.println("    Success: Correctly cannot "
					+ "destroy the cache");
		}

		// Someone with admin role can call destroy
		subject = SecurityExampleHelper.login("BuckarooBanzai");
		try {
			NamedCache cache = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_NAME);
						}
					});
			cache.destroy();
			System.out.println("    Success: Correctly allowed to "
					+ "destroy the cache");
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			e.printStackTrace();
		}
		System.out
				.println("------cache access control example completed------");
	}

	public static void accessInvocationService() {
		System.out.println("------InvocationService access control example "
				+ "begins------");

		// Someone with writer role can run invocables
		Subject subject = SecurityExampleHelper.login("JohnWhorfin");

		try {
			InvocationService service = (InvocationService) Subject.doAs(
					subject, new PrivilegedExceptionAction() {
						public Object run() {
							return CacheFactory
									.getService(SecurityExampleHelper.INVOCATION_SERVICE_NAME);
						}
					});
			service.query(new ExampleInvocable(), null);
			System.out.println("    Success: Correctly allowed to "
					+ "use the invocation service");
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			e.printStackTrace();
		}

		// Someone with reader role cannot cannot run invocables
		subject = SecurityExampleHelper.login("JohnBigboote");
		try {
			InvocationService service = (InvocationService) Subject.doAs(
					subject, new PrivilegedExceptionAction() {
						public Object run() {
							return CacheFactory
									.getService(SecurityExampleHelper.INVOCATION_SERVICE_NAME);
						}
					});
			service.query(new ExampleInvocable(), null);
		} catch (Exception ee) {
			System.out.println("    Success: Correctly unable to "
					+ "use the invocation service");
		}
		System.out.println("------InvocationService access control example "
				+ "completed------");
	}

	public static void accessMapListener() {
		System.out.println("------MapListener access control example "
				+ "begins------");

		// Someone with reader role tries to listen for map events
		NamedCache cacheReader;
		MapListener listener = new ExampleMapListener();
		Subject subject = SecurityExampleHelper.login("JohnBigboote");

		try {
			cacheReader = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_B_NAME);
						}
					});
			cacheReader.addMapListener(listener);
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			System.out.println("    Failure: " + e);
		}

		// Someone with writer role generates map events
		subject = SecurityExampleHelper.login("JohnWhorfin");
		try {
			NamedCache cache = (NamedCache) Subject.doAs(subject,
					new PrivilegedExceptionAction() {
						public Object run() throws Exception {
							return CacheFactory
									.getCache(SecurityExampleHelper.SECURITY_CACHE_B_NAME);
						}
					});
			// generate a map event
			cache.put("yetAnotherKey", "yetAnotherValue");
		} catch (Exception e) {
			// get exception if not allowed to perform the operation
			System.out.println("    Failure: " + e);
		}

		// map events occur asynchronously, so let's take a nap to make sure
		// we get the event
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		// someone in the reader role should fail the access check, so the
		// map listener should not get an event
		if (s_fMapEvent == false) {
			System.out.println("    Success: Correctly unable to "
					+ "get map events");
		}

		System.out.println("------MapListener access control example "
				+ "ends------");
	}

	// ----- inner class: ExampleMapListener -------------------------

	/**
	 * ExampleMapListener listens for map events.
	 * 
	 * @author dag 2010.07.21
	 */
	public static class ExampleMapListener implements MapListener {
		// ----- MapListener interface ----------------------------------
		/**
		 * {@inheritDoc}
		 */
		public void entryInserted(MapEvent event) {
			s_fMapEvent = true;
		}

		/**
		 * {@inheritDoc}
		 */
		public void entryUpdated(MapEvent event) {
			s_fMapEvent = true;
		}

		/**
		 * {@inheritDoc}
		 */
		public void entryDeleted(MapEvent event) {
			s_fMapEvent = true;
		}
	}

	// ----- data members --------------------------------------------------

	/**
	 * Did the MapListener get map events?
	 */
	private static volatile boolean s_fMapEvent = false;
}

/*
 * EntitledCacheService.java
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
package net.tekpartner.code.sample.coherence.security;

import com.tangosol.net.CacheService;
import com.tangosol.net.NamedCache;
import com.tangosol.net.WrapperCacheService;

/**
 * Example WrapperCacheService that demonstrates how entitlements can be applied
 * to a wrapped CacheService using the Subject passed from the client via
 * Coherence*Extend. This implementation delegates access control for cache
 * operations to the EntitledNamedCache.
 * 
 * @author dag 2010.04.16
 */
public class EntitledCacheService extends WrapperCacheService {
	/**
	 * Create a new EntitledCacheService.
	 * 
	 * @param service
	 *            the wrapped CacheService
	 */
	public EntitledCacheService(CacheService service) {
		super(service);
	}

	// ----- CacheService interface -----------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public NamedCache ensureCache(String sName, ClassLoader loader) {
		SecurityExampleHelper.checkAccess(SecurityExampleHelper.ROLE_READER);
		return new EntitledNamedCache(super.ensureCache(sName, loader));
	}

	/**
	 * {@inheritDoc}
	 */
	public void releaseCache(NamedCache map) {
		if (map instanceof EntitledNamedCache) {
			EntitledNamedCache cache = (EntitledNamedCache) map;
			SecurityExampleHelper
					.checkAccess(SecurityExampleHelper.ROLE_READER);
			map = cache.getNamedCache();
		}
		super.releaseCache(map);
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroyCache(NamedCache map) {
		if (map instanceof EntitledNamedCache) {
			EntitledNamedCache cache = (EntitledNamedCache) map;
			SecurityExampleHelper.checkAccess(SecurityExampleHelper.ROLE_ADMIN);
			map = cache.getNamedCache();
		}
		super.destroyCache(map);
	}
}

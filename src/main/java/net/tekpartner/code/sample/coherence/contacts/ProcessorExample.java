/*
 * ProcessorExample.java
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
package net.tekpartner.code.sample.coherence.contacts;

import net.tekpartner.code.sample.coherence.pof.Address;
import net.tekpartner.code.sample.coherence.pof.OfficeUpdater;

import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EqualsFilter;

/**
 * ProcessorExample demonstrates how to use a processor to modify data in the
 * cache. All Contacts who live in MA will have their work address updated.
 * 
 * @author dag 2009.02.26
 */
public class ProcessorExample {
	// ----- ProcessorExample methods -----------------------------------

	/**
	 * Perform the example updates to contacts.
	 * 
	 * @param cache
	 *            Cache
	 */
	public void execute(NamedCache cache) {
		System.out.println("------ProcessorExample begins------");
		// People who live in Massachusetts moved to an in-state office
		Address addrWork = new Address("200 Newbury St.", "Yoyodyne, Ltd.",
				"Boston", "MA", "02116", "US");
		// Apply the OfficeUpdater on all contacts which lives in MA
		cache.invokeAll(new EqualsFilter("getHomeAddress.getState", "MA"),
				new OfficeUpdater(addrWork));
		System.out.println("------ProcessorExample completed------");
	}
}

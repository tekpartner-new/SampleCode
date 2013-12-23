/*
 * EntitledInvocationService.java
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
package com.pge.ei.demo.coherence.security;

import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationObserver;
import com.tangosol.net.InvocationService;
import com.tangosol.net.WrapperInvocationService;

import java.util.Map;
import java.util.Set;

/**
 * Example WrapperInvocationService that demonstrates how entitlements can be
 * applied to a wrapped InvocationService using the Subject passed from the
 * client via Coherence*Extend. This implementation only allows clients with a
 * specified role to access the wrapped InvocationService.
 * 
 * @author dag 2010.04.19
 */
public class EntitledInvocationService extends WrapperInvocationService {
	/**
	 * Create a new EntitledInvocationService.
	 * 
	 * @param service
	 *            the wrapped InvocationService
	 */
	public EntitledInvocationService(InvocationService service) {
		super(service);
	}

	// ----- InvocationService interface ------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void execute(Invocable task, Set setMembers,
			InvocationObserver observer) {
		SecurityExampleHelper.checkAccess(SecurityExampleHelper.ROLE_WRITER);
		super.execute(task, setMembers, observer);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map query(Invocable task, Set setMembers) {
		SecurityExampleHelper.checkAccess(SecurityExampleHelper.ROLE_WRITER);
		return super.query(task, setMembers);
	}
}

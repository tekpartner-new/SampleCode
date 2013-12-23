/*
 * PasswordIdentityAsserter.java
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

import com.tangosol.io.pof.PofPrincipal;

import com.tangosol.net.Service;
import com.tangosol.net.security.IdentityAsserter;

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * PasswordIdentityAsserter asserts that the security token contains the
 * required password and then constructs a Subject based on a list of Principal
 * names.
 * 
 * @author dag 2010.04.15
 */
@SuppressWarnings(value = "unchecked")
public class PasswordIdentityAsserter implements IdentityAsserter {
	// ----- IdentityAsserter interface -------------------------------------

	/**
	 * Asserts an identity based on a token-based identity assertion.
	 * 
	 * @param oToken
	 *            the token that asserts identity.
	 * 
	 * @return a Subject representing the identity.
	 * 
	 * @throws SecurityException
	 *             if the identity assertion fails.
	 */
	public Subject assertIdentity(Object oToken) throws SecurityException {
		if (oToken instanceof Object[]) {
			String sPassword = System.getProperty("coherence.password",
					"secret-password");
			Set setPrincipalUser = new HashSet();
			Object[] asName = (Object[]) oToken;

			// first name must be password
			if (((String) asName[0]).equals(sPassword)) {
				for (int i = 1, len = asName.length; i < len; i++) {
					setPrincipalUser.add(new PofPrincipal((String) asName[i]));
				}

				return new Subject(true, setPrincipalUser, new HashSet(),
						new HashSet());
			}
		}
		throw new SecurityException("Access denied");
	}

	public Subject assertIdentity(Object arg0, Service arg1)
			throws SecurityException {
		// TODO Auto-generated method stub
		return null;
	}
}

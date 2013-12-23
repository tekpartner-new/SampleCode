/*
 * PasswordIdentityTransformer.java
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

import com.tangosol.net.Service;
import com.tangosol.net.security.IdentityTransformer;

import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * PasswordIdentityTransformer creates a security token that contains the
 * required password and then adds a list of Principal names.
 * 
 * @author dag 2010.04.15
 */
public class PasswordIdentityTransformer implements IdentityTransformer

{
	// ----- IdentityTransformer interface ----------------------------------

	/**
	 * Transform a Subject to a token that asserts an identity.
	 * 
	 * @param subject
	 *            the Subject representing a user.
	 * 
	 * @return the token that asserts identity.
	 * 
	 * @throws SecurityException
	 *             if the identity transformation fails.
	 */
	public Object transformIdentity(Subject subject) throws SecurityException {
		if (subject == null) {
			throw new SecurityException("Incomplete Subject");
		}

		Set setPrincipals = subject.getPrincipals();

		if (setPrincipals.isEmpty()) {
			throw new SecurityException("Incomplete Subject");
		}

		String[] asPrincipalName = new String[setPrincipals.size() + 1];
		int i = 0;

		asPrincipalName[i++] = System.getProperty("coherence.password",
				"secret-password");

		for (Iterator iter = setPrincipals.iterator(); iter.hasNext();) {
			asPrincipalName[i++] = ((Principal) iter.next()).getName();
		}

		// The token consists of the password plus the principal names as an
		// array of pof-able types, in this case strings.
		return asPrincipalName;
	}

	public Object transformIdentity(Subject arg0, Service arg1)
			throws SecurityException {
		// TODO Auto-generated method stub
		return null;
	}
}

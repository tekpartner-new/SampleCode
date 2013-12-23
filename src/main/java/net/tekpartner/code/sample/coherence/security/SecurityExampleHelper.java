/*
 * SecurityExampleHelper.java
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

import com.tangosol.net.security.SecurityHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.security.Principal;

import javax.security.auth.Subject;

/**
 * This class provides extremely simplified role based policies and access
 * control.
 * 
 * @author dag 2010.04.19
 */
@SuppressWarnings(value = "unchecked")
public class SecurityExampleHelper {
	// ----- static methods -------------------------------------------------

	/**
	 * Login the user.
	 * 
	 * @param sName
	 *            the user name
	 * 
	 * @return the authenticated user
	 */
	public static Subject login(String sName) {
		// For simplicity, just create a Subject. Normally, this would be
		// done using JAAS.
		String sUserDN = "CN=" + sName + ",OU=Yoyodyne";
		Set setPrincipalUser = new HashSet();

		setPrincipalUser.add(new PofPrincipal(sUserDN));
		// Map the user to a role
		setPrincipalUser.add(new PofPrincipal((String) s_mapUserToRole
				.get(sName)));

		return new Subject(true, setPrincipalUser, new HashSet(), new HashSet());
	}

	/**
	 * Assert that a Subject is associated with the calling thread with a
	 * Principal representing the required role.
	 * 
	 * @param sRoleRequired
	 *            the role required for the operation
	 * 
	 * @throws SecurityException
	 *             if a Subject is not associated with the calling thread or
	 *             does not have the specified role Principal
	 */
	public static void checkAccess(String sRoleRequired) {
		checkAccess(sRoleRequired, SecurityHelper.getCurrentSubject());
	}

	/**
	 * Assert that a Subject contains a Principal representing the required
	 * role.
	 * 
	 * @param sRoleRequired
	 *            the role required for the operation
	 * 
	 * @param subject
	 *            the Subject requesting access
	 * 
	 * @throws SecurityException
	 *             if a Subject is null or does not have the specified role
	 *             Principal
	 */
	public static void checkAccess(String sRoleRequired, Subject subject) {
		if (subject == null) {
			throw new SecurityException(
					"Access denied, authentication required");
		}

		Map mapRoleToId = s_mapRoleToId;
		Integer nRoleRequired = (Integer) mapRoleToId.get(sRoleRequired);

		for (Iterator iter = subject.getPrincipals().iterator(); iter.hasNext();) {
			Principal principal = (Principal) iter.next();
			String sName = principal.getName();

			if (sName.startsWith("role_")) {
				Integer nRolePrincipal = (Integer) mapRoleToId.get(sName);

				if (nRolePrincipal == null) {
					// invalid role
					break;
				}

				if (nRolePrincipal.intValue() >= nRoleRequired.intValue()) {
					return;
				}
			}
		}

		throw new SecurityException("Access denied, insufficient privileges");
	}

	// ----- constants -----------------------------------------------------

	public static final String ROLE_READER = "role_reader";

	public static final String ROLE_WRITER = "role_writer";

	public static final String ROLE_ADMIN = "role_admin";

	/**
	 * A cache name for security examples
	 */
	public static final String SECURITY_CACHE_NAME = "security_a";

	/**
	 * Another cache name for security examples
	 */
	public static final String SECURITY_CACHE_B_NAME = "security_b";

	/**
	 * The name of the InvocationService used by security examples.
	 */
	public static String INVOCATION_SERVICE_NAME = "ExtendTcpInvocationService";

	// ----- static data ---------------------------------------------------

	/**
	 * The map keyed by user name with the value being the user's role.
	 * Represents which user is in which role.
	 */
	private static Map s_mapUserToRole = new HashMap();

	/**
	 * The map keyed by role name with the value the role id. Represents the
	 * numeric role identifier.
	 */
	private static Map s_mapRoleToId = new HashMap();

	// ----- static initializer ---------------------------------------------

	static {
		// User to role mapping
		s_mapUserToRole.put("BuckarooBanzai", ROLE_ADMIN);
		s_mapUserToRole.put("JohnWhorfin", ROLE_WRITER);
		s_mapUserToRole.put("JohnBigboote", ROLE_READER);

		// Role to Id mapping
		s_mapRoleToId.put(ROLE_ADMIN, Integer.valueOf(9));
		s_mapRoleToId.put(ROLE_WRITER, Integer.valueOf(2));
		s_mapRoleToId.put(ROLE_READER, Integer.valueOf(1));
	}
}

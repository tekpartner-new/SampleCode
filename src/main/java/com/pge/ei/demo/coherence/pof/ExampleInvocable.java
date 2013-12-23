/*
 * ExampleInvocable.java
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
package com.pge.ei.demo.coherence.pof;

import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;

import com.tangosol.net.Invocable;
import com.tangosol.net.InvocationService;

import java.io.IOException;

/**
 * Invocable implementation that increments and returns a given integer.
 */
public class ExampleInvocable implements Invocable, PortableObject {
	// ----- constructors ---------------------------------------------

	/**
	 * Default constructor.
	 */
	public ExampleInvocable() {
	}

	// ----- Invocable interface --------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void init(InvocationService service) {
		m_service = service;
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		if (m_service != null) {
			m_nValue++;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getResult() {
		return new Integer(m_nValue);
	}

	// ----- PortableObject interface ---------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void readExternal(PofReader in) throws IOException {
		m_nValue = in.readInt(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExternal(PofWriter out) throws IOException {
		out.writeInt(0, m_nValue);
	}

	// ----- data members ---------------------------------------------

	/**
	 * The integer value to increment.
	 */
	private int m_nValue;

	/**
	 * The InvocationService that is executing this Invocable.
	 */
	private transient InvocationService m_service;
}
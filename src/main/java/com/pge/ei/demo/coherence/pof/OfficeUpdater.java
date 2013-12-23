/*
 * OfficeUpdater.java
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

import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.InvocableMap;

import java.io.IOException;

/**
 * OfficeUpdater updates a contact's office address.
 * 
 * @author dag 2009.02.26
 */
public class OfficeUpdater extends AbstractProcessor implements PortableObject {
	// ----- constructors -------------------------------------------

	/**
	 * Default constructor (necessary for PortableObject implementation).
	 */
	public OfficeUpdater() {
	}

	/**
	 * Construct an OfficeUpdater with a new work Address.
	 * 
	 * @param addrWork
	 *            the new work address.
	 */
	public OfficeUpdater(Address addrWork) {
		m_addrWork = addrWork;
	}

	// ----- InvocableMap.EntryProcessor interface ------------------

	/**
	 * {@inheritDoc}
	 */
	public Object process(InvocableMap.Entry entry) {
		Contact contact = (Contact) entry.getValue();

		contact.setWorkAddress(m_addrWork);
		entry.setValue(contact);
		return null;
	}

	// ----- PortableObject interface -------------------------------

	/**
	 * {@inheritDoc}
	 */
	public void readExternal(PofReader reader) throws IOException {
		m_addrWork = (Address) reader.readObject(WORK_ADDRESS);
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeExternal(PofWriter writer) throws IOException {
		writer.writeObject(WORK_ADDRESS, m_addrWork);
	}

	// ----- constants ----------------------------------------------

	/**
	 * The POF index for the WorkAddress property
	 */
	public static final int WORK_ADDRESS = 0;

	// ----- data members -------------------------------------------

	/**
	 * New work address.
	 */
	private Address m_addrWork;
}
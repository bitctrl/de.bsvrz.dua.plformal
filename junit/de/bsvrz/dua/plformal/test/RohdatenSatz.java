/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.test;

import stauma.dav.configuration.interfaces.SystemObject;

/**
 * Wie Rohdatensatz in Tabelle 5-3 (S.15)
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class RohdatenSatz {

	/**
	 * Das Testobjekt
	 */
	public SystemObject obj;

	/**
	 * Der Wert, der in das 'AttributTest1' geschrieben werden soll
	 */
	public long att1;

	/**
	 * Der Wert, der in das 'AttributTest2' geschrieben werden soll
	 */
	public double att2;

	/**
	 * Standardkonstruktor
	 * 
	 * @param obj
	 *            das Testobjekt
	 * @param att1
	 *            Wert, der in das 'AttributTest1' geschrieben werden soll
	 * @param att2
	 *            Wert, der in das 'AttributTest2' geschrieben werden soll
	 */
	public RohdatenSatz(SystemObject obj, long att1, double att2) {
		this.obj = obj;
		this.att1 = att1;
		this.att2 = att2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Objekt: " + obj + ", Wert1: " + //$NON-NLS-1$ //$NON-NLS-2$
				att1 + ", Wert2: " + att2 + "\n"; //$NON-NLS-1$//$NON-NLS-2$
	}
}



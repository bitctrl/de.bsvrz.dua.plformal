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
 * Klasse für die Parametersätze, die zur formalen Prüfung der Testobjekte
 * herangezogen werden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class ParameterSatz {

	/**
	 * das Testobjekt
	 */
	public SystemObject obj;

	/**
	 * Test-Intervall für Ganzzahl-Attribut
	 */
	public long min1, max1;

	/**
	 * Test-Intervall für Kommazahl-Attribut
	 */
	public long min2, max2;

	/**
	 * Standardkonstruktor
	 * 
	 * @param obj
	 *            das Testobjekt
	 * @param min1
	 *            Test-Intervallanfang für Ganzzahl-Attribut
	 * @param max1
	 *            Test-Intervallende für Ganzzahl-Attribut
	 * @param min2
	 *            Test-Intervallanfang für Kommazahl-Attribut
	 * @param max2
	 *            Test-Intervallende für Kommazahl-Attribut
	 */
	public ParameterSatz(SystemObject obj, long min1, long max1, long min2,
			long max2) {
		this.obj = obj;
		this.min1 = min1;
		this.max1 = max1;
		this.min2 = min2;
		this.max2 = max2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return obj + " --> Attribut1[" + min1 + ", " + max1 + "], " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"Attribut2[" + min2 + ", " + max2 + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}	

/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.1 Plausibilit�tspr�fung formal
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.test;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Hilfsklasse f�r eigentlichen Test <code>PlPruefungFormalTest</code>
 *
 * Klasse f�r die Parameters�tze, die zur formalen Pr�fung der Testobjekte
 * herangezogen werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public class ParameterSatz {

	/**
	 * das Testobjekt.
	 */
	public SystemObject obj;

	/**
	 * Test-Intervall f�r Ganzzahl-Attribut.
	 */
	public long min1, max1;

	/**
	 * Test-Intervall f�r Kommazahl-Attribut.
	 */
	public long min2, max2;

	/**
	 * Standardkonstruktor.
	 *
	 * @param obj
	 *            das Testobjekt
	 * @param min1
	 *            Test-Intervallanfang f�r Ganzzahl-Attribut
	 * @param max1
	 *            Test-Intervallende f�r Ganzzahl-Attribut
	 * @param min2
	 *            Test-Intervallanfang f�r Kommazahl-Attribut
	 * @param max2
	 *            Test-Intervallende f�r Kommazahl-Attribut
	 */
	public ParameterSatz(final SystemObject obj, final long min1,
			final long max1, final long min2, final long max2) {
		this.obj = obj;
		this.min1 = min1;
		this.max1 = max1;
		this.min2 = min2;
		this.max2 = max2;
	}

	@Override
	public String toString() {
		return obj + " --> Attribut1[" + min1 + ", " + max1 + "], "
				+ "Attribut2[" + min2 + ", " + max2 + "]";
	}
}

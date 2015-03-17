/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
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
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.test;

/**
 * Hilfsklasse für eigentlichen Test <code>PlPruefungFormalTest</code>.
 *
 * Wie Durchlauf in Tabelle 5-4
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public class Durchlauf {

	/**
	 * Parameter für die Plausibilisierung (Manipulation) eines als implausibel
	 * erkannten Wertes von AttributTest1.
	 */
	public long testAtt1;

	/**
	 * Parameter für die Plausibilisierung (Manipulation) eines als implausibel
	 * erkannten Wertes von AttributTest2.
	 */
	public long testAtt2;

	/**
	 * Standardkonstruktor.
	 *
	 * @param testAtt1
	 *            Plausibilisierungsparameter für AttributTest1
	 * @param testAtt2
	 *            Plausibilisierungsparameter für AttributTest2
	 */
	public Durchlauf(final long testAtt1, final long testAtt2) {
		this.testAtt1 = testAtt1;
		this.testAtt2 = testAtt2;
	}

}

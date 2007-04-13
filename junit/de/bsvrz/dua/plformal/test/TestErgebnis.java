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

/**
 * Hilfsklasse für eigentlichen Test <code>PlPruefungFormalTest</code>
 * 
 * Klasse zur Repräsentation eines Ergebnisses wie in Tabelle 5-5 (S.16)
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class TestErgebnis {

	/**
	 * Ergebniswert von Attribut1
	 */
	public long wert1;

	/**
	 * Ergebniskennung von Attribut1
	 */
	public boolean[] kennung1;

	/**
	 * Ergebniswert von Attribut2
	 */
	public double wert2;

	/**
	 * Ergebniskennung von Attribut2
	 */
	public boolean[] kennung2;

	/**
	 * Standardkonstruktor
	 * 
	 * @param wert1
	 *            Ergebniswert von Attribut1
	 * @param kennung1
	 *            Ergebniskennung von Attribut1
	 * @param wert2
	 *            Ergebniswert von Attribut2
	 * @param kennung2
	 *            Ergebniskennung von Attribut2
	 */
	public TestErgebnis(long wert1, boolean[] kennung1, double wert2,
			boolean[] kennung2) {
		this.wert1 = wert1;
		this.kennung1 = kennung1;
		this.wert2 = wert2;
		this.kennung2 = kennung2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ergebnis = false;

		if (obj != null && obj instanceof TestErgebnis) {
			TestErgebnis that = (TestErgebnis) obj;

			ergebnis = this.wert1 == that.wert1 & this.wert2 == that.wert2;

			for (int i = 0; i < 3; i++) {
				ergebnis &= this.kennung1[i] == that.kennung1[i]
						& this.kennung2[i] == that.kennung2[i];
			}
		}

		return ergebnis;
	}

	/**
	 * Ermittelt die Kennung aus dem Bool-Array
	 * 
	 * @param k
	 *            Bool-Array mit verschlüsselter Kennung
	 * @return die Kennung
	 */
	private String getKennung(boolean[] k) {
		String s = "UNBEKANNT"; //$NON-NLS-1$

		if (k[0] & !k[1] & !k[2]) {
			s = "WertMin"; //$NON-NLS-1$
		}
		if (!k[0] & k[1] & !k[2]) {
			s = "WertMax"; //$NON-NLS-1$
		}
		if (!k[0] & !k[1] & k[2]) {
			s = "Implausibel"; //$NON-NLS-1$
		}
		if (!k[0] & !k[1] & !k[2]) {
			s = "\"-\""; //$NON-NLS-1$
		}

		return s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Wert1: " + wert1 + " - " //$NON-NLS-1$//$NON-NLS-2$
				+ getKennung(kennung1) + ", Wert2: " + wert2 + //$NON-NLS-1$
				" - " + getKennung(kennung2); //$NON-NLS-1$
	}
}

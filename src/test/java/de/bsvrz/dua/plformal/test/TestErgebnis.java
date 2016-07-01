/*
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

import java.util.Arrays;

/**
 * Hilfsklasse für eigentlichen Test <code>PlPruefungFormalTest</code>.
 *
 * Klasse zur Repräsentation eines Ergebnisses wie in Tabelle 5-5 (S.16)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class TestErgebnis {

	/**
	 * Ergebniswert von Attribut1.
	 */
	public long wert1;

	/**
	 * Ergebniskennung von Attribut1.
	 */
	public boolean[] kennung1;

	/**
	 * Ergebniswert von Attribut2.
	 */
	public double wert2;

	/**
	 * Ergebniskennung von Attribut2.
	 */
	public boolean[] kennung2;

	/**
	 * Standardkonstruktor.
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
	public TestErgebnis(final long wert1, final boolean[] kennung1, final double wert2, final boolean[] kennung2) {
		this.wert1 = wert1;
		this.kennung1 = kennung1;
		this.wert2 = wert2;
		this.kennung2 = kennung2;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ergebnis = false;

		if ((obj != null) && (obj instanceof TestErgebnis)) {
			final TestErgebnis that = (TestErgebnis) obj;

			ergebnis = (this.wert1 == that.wert1) & (this.wert2 == that.wert2);

			for (int i = 0; i < 3; i++) {
				ergebnis &= (this.kennung1[i] == that.kennung1[i]) & (this.kennung2[i] == that.kennung2[i]);
			}
		}

		return ergebnis;
	}

	/**
	 * Ermittelt die Kennung aus dem Bool-Array.
	 *
	 * @param k
	 *            Bool-Array mit verschlüsselter Kennung
	 * @return die Kennung
	 */
	private String getKennung(final boolean[] k) {
		String s = "UNBEKANNT";

		if (k[0] & !k[1] & !k[2]) {
			s = "WertMin";
		}
		if (!k[0] & k[1] & !k[2]) {
			s = "WertMax";
		}
		if (!k[0] & !k[1] & k[2]) {
			s = "Implausibel";
		}
		if (!k[0] & !k[1] & !k[2]) {
			s = "\"-\"";
		}

		return s;
	}

	@Override
	public String toString() {
		return "Wert1: " + wert1 + " - " + getKennung(kennung1) + ", Wert2: " + wert2 + " - " + getKennung(kennung2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(kennung1);
		result = (prime * result) + Arrays.hashCode(kennung2);
		result = (prime * result) + (int) (wert1 ^ (wert1 >>> 32));
		long temp;
		temp = Double.doubleToLongBits(wert2);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

}

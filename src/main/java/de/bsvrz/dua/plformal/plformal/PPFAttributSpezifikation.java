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

package de.bsvrz.dua.plformal.plformal;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dua.plformal.plformal.typen.PlausibilisierungsMethode;

/**
 * Diese Klasse repräsentiert alle Informationen, die innerhalb <b>eines</b>
 * Datensatzes <code>AttributSpezifikation</code> in der Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> enthalten sind.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public class PPFAttributSpezifikation {

	/**
	 * der Attributpfad.
	 */
	private String attributPfad;

	/**
	 * Min-Wert.
	 */
	private long min = -1;

	/**
	 * Max-Wert.
	 */
	private long max = -1;

	/**
	 * Vergleichs- bzw. Ersetzungsmethode
	 */
	private PlausibilisierungsMethode methode = PlausibilisierungsMethode.KEINE_PRUEFUNG;

	/**
	 * Standardkonstruktor.
	 *
	 * @param attributSpezifikation
	 *            <b>ein</b> DAV-Datensatz der Liste
	 *            <code>AttributSpezifikation</code> der Attributgruppe
	 *            <code>atg.plausibilitätsPrüfungFormal</code>
	 * @throws PlFormalException
	 *             falls die Parameter fehlerhaft sind
	 */
	public PPFAttributSpezifikation(final Data attributSpezifikation)
			throws PlFormalException {
		this.attributPfad = attributSpezifikation
				.getTextValue(PPFKonstanten.ATT_PARA_SATZ_ATT_SPEZ_PFAD)
				.getText().toString();
		this.min = attributSpezifikation.getUnscaledValue(
				PPFKonstanten.ATT_PARA_SATZ_ATT_SPEZ_MIN).longValue();
		this.max = attributSpezifikation.getUnscaledValue(
				PPFKonstanten.ATT_PARA_SATZ_ATT_SPEZ_MAX).longValue();
		this.methode = PlausibilisierungsMethode
				.getZustand(attributSpezifikation.getUnscaledValue(
						PPFKonstanten.ATT_PARA_SATZ_ATT_SPEZ_OPT).intValue());
		if (min > max) {
			throw new PlFormalException("MIN (" + this.min
					+ ") ist größer als MAX (" + max + ")");
		}
	}

	/**
	 * Erfragt den Attributpfad.
	 *
	 * @return der Attributpfad
	 */
	public final String getAttributPfad() {
		return this.attributPfad;
	}

	/**
	 * Erfragt Max-Wert.
	 *
	 * @return der Max-Wert
	 */
	public final long getMax() {
		return this.max;
	}

	/**
	 * Erfragt die Plausibilisierungsmethode.
	 *
	 * @return die Plausibilisierungsmethode
	 */
	public final PlausibilisierungsMethode getMethode() {
		return this.methode;
	}

	/**
	 * Erfragt Min-Wert.
	 *
	 * @return der Min-Wert
	 */
	public final long getMin() {
		return this.min;
	}

	@Override
	public String toString() {
		String s = "unbekannt";

		if (this.attributPfad != null) {
			s = "Attributpfad: " + this.attributPfad + "\n";
			s += "Min: " + this.min + "\n";
			s += "Max: " + this.max + "\n";
			s += "Methode: " + this.methode + "\n";
		}

		return s;
	}
}

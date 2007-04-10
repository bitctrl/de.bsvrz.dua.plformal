/**
 * Segment 4 Datenübernahme und Aufbereitung, SWE 4.1 Plausibilitätsprüfung formal
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

package de.bsvrz.dua.plformal.plformal;

import stauma.dav.clientside.Data;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.plformal.typen.PlausibilisierungsMethode;

/**
 * Diese Klasse repräsentiert alle Informationen, die innerhalb <b>eines</b>
 * Datensatzes <code>AttributSpezifikation</code> in der Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> enthalten sind.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class PPFAttributSpezifikation {
	
	/**
	 * der Attributpfad
	 */
	private String attributPfad = null;

	/**
	 * Min-Wert
	 */
	private long min = DUAKonstanten.LONG_UNDEFINIERT;

	/**
	 * Max-Wert
	 */
	private long max = DUAKonstanten.LONG_UNDEFINIERT;

	/**
	 * Vergleichs- bzw. Ersetzungsmethode
	 */
	private PlausibilisierungsMethode methode
					= PlausibilisierungsMethode.KEINE_PRUEFUNG;

	
	/**
	 * Standardkonstruktor
	 * 
	 * @param attributSpezifikation <b>ein</b> DAV-Datensatz der Liste
	 * <code>AttributSpezifikation</code> der Attributgruppe
	 * <code>atg.plausibilitätsPrüfungFormal</code>
	 * @throws Exception falls Fehler beim Auslesen des
	 * DAV-Datensatzes auftreten
	 */
	public PPFAttributSpezifikation(final Data attributSpezifikation)
	throws Exception{
		this.attributPfad = attributSpezifikation.getTextValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_PFAD).getText().toString();
		this.min = attributSpezifikation.getUnscaledValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MIN).longValue();
		this.max = attributSpezifikation.getUnscaledValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MAX).longValue();
		this.methode = PlausibilisierungsMethode.getZustand(attributSpezifikation
				.getUnscaledValue(DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_OPT).
						intValue());
		if(min > max){
			throw new Exception("MIN (" + this.min +  //$NON-NLS-1$
					") ist größer als MAX (" + max + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Erfragt den Attributpfad
	 * 
	 * @return der Attributpfad
	 */
	public final String getAttributPfad() {
		return this.attributPfad;
	}

	/**
	 * Erfragt Max-Wert
	 * 
	 * @return der Max-Wert
	 */
	public final long getMax() {
		return this.max;
	}

	/**
	 * Erfragt die Plausibilisierungsmethode 
	 * 
	 * @return die Plausibilisierungsmethode
	 */
	public final PlausibilisierungsMethode getMethode() {
		return this.methode;
	}

	/**
	 * Erfragt Min-Wert
	 * 
	 * @return der Min-Wert
	 */
	public final long getMin() {
		return this.min;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = DUAKonstanten.STR_UNDEFINIERT;

		if(this.attributPfad != null){
			s = "Attributpfad: " + this.attributPfad + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Min: " + this.min + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Max: " + this.max + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Methode: " + this.methode + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return s;
	}
}

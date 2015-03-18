/*
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

package de.bsvrz.dua.plformal.plformal.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * �ber diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.optionenPlausibilit�tsPr�fungFormalGlobal</code> beschriebenen
 * Werte zur Verf�gung gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public final class PlausibilisierungsMethode extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, PlausibilisierungsMethode> werteBereich = new HashMap<Integer, PlausibilisierungsMethode>();

	/**
	 * Wertebereichspr�fung wird NICHT durchgef�hrt. Wert wird nicht ver�ndert,
	 * es werden keine Statusflags gesetzt
	 */
	public static final PlausibilisierungsMethode KEINE_PRUEFUNG = new PlausibilisierungsMethode(
			"Keine Pr�fung", 0);

	/**
	 * Wertebereichspr�fung wird durchgef�hrt. Fehlerhafte Werte werden nicht
	 * ver�ndert, es werden nur die Statusflags gesetzt
	 */
	public static final PlausibilisierungsMethode NUR_PRUEFUNG = new PlausibilisierungsMethode(
			"NurPr�fung", 1);

	/**
	 * Wertebereichspr�fung wird durchgef�hrt. Bei Bereichsunter- bzw.
	 * �berschreitung wird der Wert auf den parametrierten Min- bzw. /Max-Wert
	 * korrigiert und die Statusflags gesetzt
	 */
	public static final PlausibilisierungsMethode SETZE_MIN_MAX = new PlausibilisierungsMethode(
			"Setze MinMax", 2);

	/**
	 * Wertebereichspr�fung wird durchgef�hrt. Bei Bereichsunterschreitung wird
	 * der Wert auf den parametrierten Min-Wert korrigiert und die Statusflags
	 * gesetzt, ansonsten Verhalten wie bei Option "NurPr�fen"
	 */
	public static final PlausibilisierungsMethode SETZE_MIN = new PlausibilisierungsMethode(
			"Setze Min", 3);

	/**
	 * Wertebereichspr�fung wird durchgef�hrt. Bei Bereichs�berschreitung wird
	 * der Wert auf den parametrierten Max-Wert korrigiert und die Statusflags
	 * gesetzt, ansonsten Verhalten wie bei Option "NurPr�fen"
	 */
	public static final PlausibilisierungsMethode SETZE_MAX = new PlausibilisierungsMethode(
			"Setze Max", 4);

	/**
	 * Standardkonstruktor.
	 *
	 * @param name
	 *            der Name
	 * @param code
	 *            der Code
	 */
	private PlausibilisierungsMethode(final String name, final int code) {
		super(code, name);
		PlausibilisierungsMethode.werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem �bergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem �bergebenen Code.
	 */
	public static PlausibilisierungsMethode getZustand(final int code) {
		return PlausibilisierungsMethode.werteBereich.get(code);
	}
}

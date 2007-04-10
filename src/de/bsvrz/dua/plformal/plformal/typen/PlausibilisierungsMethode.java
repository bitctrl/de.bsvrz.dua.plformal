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

package de.bsvrz.dua.plformal.plformal.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.optionenPlausibilitätsPrüfungFormalGlobal</code>
 * beschriebenen Werte zur Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlausibilisierungsMethode
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, PlausibilisierungsMethode> WERTE_BEREICH = 
						new HashMap<Integer, PlausibilisierungsMethode>();
	
	/**
	 * Wertebereichsprüfung wird NICHT durchgeführt. Wert wird nicht verändert,
	 * es werden keine Statusflags gesetzt
	 */
	public static final PlausibilisierungsMethode KEINE_PRUEFUNG = 
		new PlausibilisierungsMethode("Keine Prüfung", 0); //$NON-NLS-1$
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Fehlerhafte Werte werden 
	 * nicht verändert, es werden nur die Statusflags gesetzt
	 */
	public static final PlausibilisierungsMethode NUR_PRUEFUNG = 
		new PlausibilisierungsMethode("NurPrüfung", 1); //$NON-NLS-1$
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsunter- bzw.
	 * überschreitung wird der Wert auf den parametrierten Min- bzw. /Max-Wert
	 * korrigiert und die Statusflags gesetzt 
	 */
	public static final PlausibilisierungsMethode SETZE_MIN_MAX = 
		new PlausibilisierungsMethode("Setze MinMax", 2); //$NON-NLS-1$
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsunterschreitung
	 * wird der Wert auf den parametrierten Min-Wert korrigiert und die
	 * Statusflags gesetzt, ansonsten Verhalten wie bei Option "NurPrüfen"
	 */
	public static final PlausibilisierungsMethode SETZE_MIN = 
		new PlausibilisierungsMethode("Setze Min", 3); //$NON-NLS-1$

	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsüberschreitung wird
	 * der Wert auf den parametrierten Max-Wert korrigiert und die Statusflags
	 * gesetzt, ansonsten Verhalten wie bei Option "NurPrüfen"
	 */
	public static final PlausibilisierungsMethode SETZE_MAX = 
		new PlausibilisierungsMethode("Setze Max", 4); //$NON-NLS-1$
		
	
	/**
	 * {@inheritDoc}
	 */
	private PlausibilisierungsMethode(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param der Code des Enumerations-Wertes
	 */
	public static final PlausibilisierungsMethode getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}


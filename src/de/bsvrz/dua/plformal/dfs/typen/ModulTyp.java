/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
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

package de.bsvrz.dua.plformal.dfs.typen;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.modulTyp</code> beschriebenen Werte zur
 * Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class ModulTyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, ModulTyp> WERTE_BEREICH = 
						new HashMap<Integer, ModulTyp>();

	/**
	 * Alle Werte
	 */
	public static final ModulTyp PL_PRUEFUNG_FORMAL = 
		new ModulTyp("PlPrüfungFormal", 1); //$NON-NLS-1$
	
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_UFD = 
		new ModulTyp("PlPrüfungLogischUFD", 2); //$NON-NLS-1$
	
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_WZG = 
		new ModulTyp("PlPrüfungLogischWZG", 3); //$NON-NLS-1$
	
	public static final ModulTyp MESSWERTERSETZUNG_LVE = 
		new ModulTyp("PlPrüfungMesswertErsetzungLVE", 4); //$NON-NLS-1$

	public static final ModulTyp MESSWERTERSETZUNG_UFD = 
		new ModulTyp("PlPrüfungMesswertErsetzungUFD", 5); //$NON-NLS-1$

	public static final ModulTyp PL_PRUEFUNG_LANGZEIT_UFD = 
		new ModulTyp("PlPrüfungLangZeitUFD", 6); //$NON-NLS-1$
		
	
	/**
	 * {@inheritDoc}
	 */
	private ModulTyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param der Code des Enumerations-Wertes
	 */
	public static final ModulTyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}


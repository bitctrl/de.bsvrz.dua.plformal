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

package de.bsvrz.dua.plformal.allgemein.schnittstellen;

import stauma.dav.clientside.ResultData;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerungsListener;
import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;

/**
 * Allgemeine Beschreibung der Schnittstelle Berarbeitungsknoten.
 * Diese Schnittstelle wird zur Initialisierung und Verkettung
 * von verschiedenen Modulen der DUA verwendet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IBearbeitungsKnoten
extends IDatenFlussSteuerungsListener{
	
	/**
	 * Setzt die Verbindung zum Verwaltungsmodul und
	 * initialisiert diesen Bearbeitungsknoten. Nach
	 * dem Aufruf dieser Methode wird davon ausgegangen,
	 * dass der Knoten voll funktionsfähig ist. Also
	 * zum Beispiel alle Sendeanmeldungen durchgeführt
	 * wurden. 
	 * 
	 * @param verwaltung eine Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException wird ausgelöst,
	 * wenn dieser Knoten nicht vollständig initialisiert 
	 * werden konnte (z.B. wenn als Parameter <code>null</code>
	 * übergeben wurde).
	 */
	public void initialisiere(IVerwaltung verwaltung)
	throws DUAInitialisierungsException;

	/**
	 * Teilt diesem Knoten mit, an welchen Knoten die Daten nach der
	 * vollständigen Bearbeitung durch diesen Knoten weitergeleitet
	 * werden sollen.
	 * 
	 * @param knoten der chronologisch nachgeordnete Bearbeitungsknoten
	 * oder <code>null</code>, wenn dieser Knoten der Letzte ist.
	 */
	public void setNaechstenBearbeitungsKnoten(IBearbeitungsKnoten knoten);

	/**
	 * Legt fest, ob eine Publikation der in diesem
	 * Bearbeitungsknoten aufbereiteten Daten in den
	 * Datenverteiler stattfinden soll. 
	 * 
	 * @param publizieren <code>true</code>, wenn 
	 * publiziert werden soll
	 */
	public void setPublikation(boolean publizieren);
	
	/**
	 * Aktualisierungsmethode. Über diese Methode sollten
	 * dem Objekt, das dieses Interface implementiert alle
	 * zu bearbeitenden Daten zur Verfügung gestellt werden.  
	 * 
	 * @param resultate aktuelle Daten vom Vorgängerknoten.
	 */
	public void aktualisiereDaten(ResultData[] resultate);
	
	/**
	 * Erfragt den Typen des Moduls, das dieses
	 * Interface implementiert
	 * 
	 * @return der Name dieses Moduls
	 */
	public ModulTyp getModulTyp();

}

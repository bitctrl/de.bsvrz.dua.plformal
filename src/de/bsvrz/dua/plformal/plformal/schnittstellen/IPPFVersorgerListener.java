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

package de.bsvrz.dua.plformal.plformal.schnittstellen;

import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;

/**
 * Dieses Interface muss von allen Klassen implementiert
 * werden, die aktuellen Informationen über die Parameter
 * zur formalen Plausibilisierung über das Interface
 * <code>IPPFVersorger</code> empfangen wollen.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IPPFVersorgerListener {

	/**
	 * Aktualisiert die Parameter zur formalen
	 * Plausibilisierung
	 * 
	 * @param parameter Zugriff auf alle aktuellen
	 * Parameter der formalen Plausibilisierung
	 * (niemals <code>null</code>)
	 */
	public void aktualisiereParameter(final IPPFVersorger parameter);
	
}

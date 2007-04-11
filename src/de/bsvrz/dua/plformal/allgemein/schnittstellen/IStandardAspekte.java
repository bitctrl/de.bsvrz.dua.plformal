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

import java.util.Collection;

import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Über diese Schnittstelle sollen die Standardaspekte für
 * die Publikation innerhalb von bestimmten SWE-Modul-Typ-Kombinationen
 * zur Verfügung gestellt werden. 
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation für ein
	 * bestimmtes empfangenes Originaldatum.
	 * 
	 * @param originalDatum das Originaldatum
	 * @return der Standardpublikationsaspekt des übergebenen
	 * Datums oder <code>null</code>, wenn kein Aspekt ermittelt
	 * werden konnte
	 */
	public Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die für die Publikation
	 * unter den Standardaspekten durchgeführt werden müssen. 
	 * 
	 * @param objektFilter die Systemobjekte, die betrachtet werden
	 * sollen. Wenn dieser Array leer (oder <code>null</code>) ist,
	 * werden alle definierten Standardanmeldungen zurückgegeben. Sonst
	 * nur solche, die die in diesem Array enthaltenen Systemobjekte
	 * beinhalten.
	 * @return die Objektanmeldungen, die für die Publikation unter
	 * den Standardaspekten durchgeführt werden müssen (ggf. leere
	 * Menge)
	 */
	public Collection<DAVObjektAnmeldung> getStandardAnmeldungen(
			final SystemObject[] objektFilter);

}

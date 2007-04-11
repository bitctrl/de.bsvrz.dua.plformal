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

package de.bsvrz.dua.plformal.dfs.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Dieses Interface stellt alle Informationen über die aktuelle
 * Datenflusssteuerung <b>für eine bestimmte SWE und einen
 * bestimmten Modul-Typ</b> zur Verfügung. Im Wesentlichen
 * stellt es den Zugriff auf ein Objekt des Typs
 * <code>DatenFlussSteuerung</code> sicher.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public interface IDatenFlussSteuerungFuerModul {

	/**
	 * Erfragt die Menge aller Datenanmeldungen die in
	 * Bezug auf die übergebenen Objekte durchgeführt
	 * werden müssen, um diese nach der Plausibilisierung
	 * publizieren zu können.<br>
	 * <b>Achtung:</b> Wenn eine Überschneidung von
	 * Anmeldungen für Standard-Publikationsaspekte mit
	 * Anmeldungen aus der Datenflusssteuerung besteht,
	 * für die <b>nicht publizieren</b> gesetzt ist,
	 * so gilt hier auch die Standardpublikation als 
	 * ausgeschaltet.
	 * 
	 * @param filterObjekte
	 *            Liste mit (finalen) Objekten. Diese Liste gilt als Filter,
	 *            durch den alle innerhalb dieser Publikationszuordnung
	 *            definierten Datenanmeldungen geschickt werden, bevor diese
	 *            Methode ein Ergebnis zurückgibt. <code>null</code> = kein
	 *            Filter
	 * @param standardAnmeldungen Menge der Anmeldungen unter den
	 * 			  Standard-Publikationsaspekten
	 * @return eine ggf. leere Menge mit Datenanmeldungen
	 */
	public Collection<DAVObjektAnmeldung> getDatenAnmeldungen(
			final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen);

	/**
	 * Erfragt eine publikationsfähige Modifikation des übergebenen
	 * Original-Datums. Es wird ein Datum zurückgegeben, das nach der
	 * Plausibilisierung so publiziert werden muss.
	 * 
	 * @param originalDatum
	 *            das Originaldatum, wie es vom plausibilisierenden Modul
	 *            empfangen wurde
	 * @param plausibilisiertesDatum
	 *            dessen <code>Data</code>-Objekt nach der Plausibilisierung
	 * @param standardAspekt
	 *            der Standardaspekt der Publikation für dieses Datum oder
	 *            <code>null</code>, wenn es keinen Standardaspekt gibt
	 * @return ein <code>ResultData</code>-Objekt, das so publiziert werden
	 *         kann oder <code>null</code>, wenn keine Publikation notwendig
	 *         ist (dies ist z.B. auch der Fall, wenn innerhalb der
	 *         Datenflusssteuerung der übergebene Standardaspekt explizit von
	 *         der Publikation ausgeschlossen wurde)
	 */
	public ResultData getPublikationsDatum(final ResultData originalDatum,
			final Data plausibilisiertesDatum, final Aspect standardAspekt);

}

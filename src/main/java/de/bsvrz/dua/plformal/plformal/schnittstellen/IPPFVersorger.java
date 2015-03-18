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

package de.bsvrz.dua.plformal.plformal.schnittstellen;

import java.util.Collection;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;

/**
 * Dieses Interface stellt alle Informationen der Attributgruppe
 * <code>atg.plausibilit�tsPr�fungFormal</code> eines Objekts vom Typ
 * <code>typ.plausibilit�tsPr�fungFormal</code> zur Verf�gung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public interface IPPFVersorger {

	/**
	 * F�hrt die formale Plausibilisierung f�r ein bestimmtes Datum durch,
	 * welches innerhalb des �bergebenen <code>ResultData</code>-Objektes
	 * enthalten ist.
	 *
	 * @param resultat
	 *            das Datum
	 * @return wenn die formale Plausibilisierung dieses Datum bearbeiten
	 *         konnte, so wird das ggf. ver�nderte Datum zur�ckgegeben. Sonst
	 *         <code>null</code>.
	 */
	Data plausibilisiere(final ResultData resultat);

	/**
	 * Erfragt alle Anmeldungen f�r alle (finalen) Objekte (also Instanzen von
	 * <code>DAVObjektAnmeldung</code>), die durchgef�hrt werden m�ssen, um alle
	 * Plausibilit�tspr�fungen so wie in
	 * <code>atg.plausibilit�tsPr�fungFormal</code> beschrieben, durchf�hren zu
	 * k�nnen.
	 *
	 * @return eine (ggf. leere) Menge von Objektanmeldungen
	 */
	Collection<DAVObjektAnmeldung> getObjektAnmeldungen();

	/**
	 * Erfragt alle zur formalen Plausibilisierung vorgesehenen (finalen)
	 * Objekte. Diese Objekte werden von den Parametern der Attributgruppe
	 * <code>atg.plausibilit�tsPr�fungFormal</code> bereitgestellt.
	 *
	 * @return alle zur formalen Plausibilisierung vorgesehenen (finalen)
	 *         Objekte in einem Array oder ein leeres Array.
	 */
	SystemObject[] getBetrachteteObjekte();

}

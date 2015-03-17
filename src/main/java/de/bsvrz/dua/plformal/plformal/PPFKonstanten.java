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

/**
 * Konstanten, die für die Pl-Prüfung formal benötigt werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public final class PPFKonstanten {

	/**
	 * Pid <code>typ.plausibilitätsPrüfungFormal</code>.
	 */
	public static final String TYP = "typ.plausibilitätsPrüfungFormal";

	/**
	 * Pid <code>atg.plausibilitätsPrüfungFormal</code>.
	 */
	public static final String ATG = "atg.plausibilitätsPrüfungFormal";

	/**
	 * Name <code>ParameterSatzPlausibilitätsPrüfungFormal</code>.
	 */
	public static final String ATL_PARA_SATZ = "ParameterSatzPlausibilitätsPrüfungFormal";

	/**
	 * Name <code>Attributgruppe</code>.
	 */
	public static final String ATT_PARA_SATZ_ATG = "Attributgruppe";

	/**
	 * Name <code>Aspekt</code>.
	 */
	public static final String ATT_PARA_SATZ_ASP = "Aspekt";

	/**
	 * Name <code>Objekt</code>.
	 */
	public static final String ATL_PARA_SATZ_OBJ = "Objekt";

	/**
	 * Name <code>AttributSpezifikation</code>.
	 */
	public static final String ATL_PARA_SATZ_ATT_SPEZ = "AttributSpezifikation";

	/**
	 * Name <code>AttributPfad</code>.
	 */
	public static final String ATT_PARA_SATZ_ATT_SPEZ_PFAD = "AttributPfad";

	/**
	 * Name <code>Min</code>.
	 */
	public static final String ATT_PARA_SATZ_ATT_SPEZ_MIN = "Min";

	/**
	 * Name <code>Max</code>.
	 */
	public static final String ATT_PARA_SATZ_ATT_SPEZ_MAX = "Max";

	/**
	 * Name <code>Optionen</code>.
	 */
	public static final String ATT_PARA_SATZ_ATT_SPEZ_OPT = "Optionen";

	/**
	 * Attribut-Pfad <code>Status.PlFormal.WertMin</code>.
	 */
	public static final String ATT_STATUS_MIN = "Status.PlFormal.WertMin";

	/**
	 * Attribut-Pfad <code>Status.PlFormal.WertMax</code>.
	 */
	public static final String ATT_STATUS_MAX = "Status.PlFormal.WertMax";

	/**
	 * Attribut-Pfad <code>Status.MessWertErsetzung.Implausibel</code>.
	 */
	public static final String ATT_STATUS_IMPLAUSIBEL = "Status.MessWertErsetzung.Implausibel";

	/**
	 * Attribut-Pfad <code>Status.MessWertErsetzung.Implausibel</code>.
	 */
	public static final String ATT_GUETE = "Güte.Index";

	/**
	 * Standardkonstruktor.
	 */
	private PPFKonstanten() {

	}

}

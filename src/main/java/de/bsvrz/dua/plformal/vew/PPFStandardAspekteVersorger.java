/*
 * Segment Datenübernahme und Aufbereitung (DUA), SWE PL-Prüfung formal
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * Copyright 2016 by Kappich Systemberatung Aachen
 * 
 * This file is part of de.bsvrz.dua.plformal.
 * 
 * de.bsvrz.dua.plformal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * de.bsvrz.dua.plformal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with de.bsvrz.dua.plformal.  If not, see <http://www.gnu.org/licenses/>.

 * Contact Information:
 * Kappich Systemberatung
 * Martin-Luther-Straße 14
 * 52062 Aachen, Germany
 * phone: +49 241 4090 436 
 * mail: <info@kappich.de>
 */

package de.bsvrz.dua.plformal.vew;

import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.StandardAspekteVersorger;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Diese Klasse repräsentiert die Versorgung des Moduls Pl-Prüfung formal
 * (innerhalb der SWE Pl-Prüfung formal) mit Standard-Publikationsinformationen
 * (Zuordnung von Objekt-Datenbeschreibung-Kombination zu Standard-
 * Publikationsaspekt).
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class PPFStandardAspekteVersorger extends StandardAspekteVersorger {

	/**
	 * Standardkonstruktor.
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException wird weitergereicht
	 */
	public PPFStandardAspekteVersorger(IVerwaltung verwaltung)
			throws DUAInitialisierungsException {
		super(verwaltung);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() throws DUAInitialisierungsException {
		this.standardAspekte = new StandardAspekteAdapter(
				new StandardPublikationsZuordnung[] {
						new StandardPublikationsZuordnung("typ.fahrStreifen", 
								"atg.verkehrsDatenKurzZeitIntervall", 
								"asp.externeErfassung", 
								"asp.plausibilitätsPrüfungFormal"), 
						new StandardPublikationsZuordnung("typ.fahrStreifen", 
								"atg.verkehrsDatenLangZeitIntervall", 
								"asp.externeErfassung", 
								"asp.plausibilitätsPrüfungFormal") }); 

	}
}

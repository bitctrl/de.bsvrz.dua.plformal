/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
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

package de.bsvrz.dua.plformal.plformal;

import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.StandardAspekteVersorger;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;

/**
 * Diese Klasse repräsentiert die Versorgung des Moduls
 * PL-Prüfung formal mit Standard-Publikationsinformationen
 * (Zuordnung von Objekt-Datenbeschreibung-Kombination zu 
 * Standard-Publikationsaspekt) für alle SWE innerhalb der
 * DUA.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PPFStandardAspekteVersorger 
extends StandardAspekteVersorger{

	/**
	 * {@inheritDoc}
	 */
	public PPFStandardAspekteVersorger(IVerwaltung verwaltung)
	throws DUAInitialisierungsException {
		super(verwaltung);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init(SWETyp swe)
	throws DUAInitialisierungsException{
		
		if(SWETyp.PL_PRUEFUNG_FORMAL.equals(swe)){

//			/**
//			 * Test Standardaspekte
//			 */			
//			this.standardAspekte = new StandardAspekteAdapter(
//					new StandardPublikationsZuordnung[] {
//							new StandardPublikationsZuordnung("typ.testPlPrüfungFormal", //$NON-NLS-1$
//									"atg.testPlPrüfungFormal", //$NON-NLS-1$
//									"asp.testAusgang", //$NON-NLS-1$
//									"asp.testEingang") }); //$NON-NLS-1$
			
			this.standardAspekte = new StandardAspekteAdapter(
					new StandardPublikationsZuordnung[] {
							new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
									"atg.verkehrsDatenKurzZeitIntervall", //$NON-NLS-1$
									"asp.externeErfassung", //$NON-NLS-1$
									"asp.plausibilitätsPrüfungFormal"), //$NON-NLS-1$
							new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
									"atg.verkehrsDatenLangZeitIntervall", //$NON-NLS-1$
									"asp.externeErfassung", //$NON-NLS-1$
									"asp.plausibilitätsPrüfungFormal") }); //$NON-NLS-1$
			
		}else if(SWETyp.PL_PRUEFUNG_LOGISCH_LVE.equals(swe)){
			// TODO
		}else{
			LOGGER.warning("Es wurden für das Modul Pl-Prüfung formal und die SWE " + //$NON-NLS-1$
					swe + " keine Standard-Publikationsinformationen angegeben"); //$NON-NLS-1$
		}
		
	}	
}

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorgerListener;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DFSKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Implementierung des Moduls PL-Prüfung formal.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PlPruefungFormal
extends AbstraktBearbeitungsKnotenAdapter
implements IPPFVersorgerListener{
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Parameter der formalen Plausibilisierung
	 */
	private IPPFVersorger ppfParameter = null;
		
	/**
	 * Parameter zur Datenflusssteuerung für diese
	 * SWE und dieses Modul
	 */
	private IDatenFlussSteuerungFuerModul iDfsMod
								= DFSKonstanten.STANDARD;
	
	/**
	 * Mapt jedes hier betrachtete Objekt auf seinen aktuellen
	 * Zustanz <code>keine Daten</code>
	 */
	private Map<SystemObject, Boolean> keineDaten = new HashMap<SystemObject, Boolean>();
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param stdAspekte Informationen zu den
	 * Standardpublikationsaspekten für diese
	 * Instanz des Moduls Pl-Prüfung formal
	 */
	public PlPruefungFormal(final IStandardAspekte stdAspekte){
		if(stdAspekte != null){
			this.standardAspekte = stdAspekte;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialisiere(IVerwaltung dieVerwaltung)
	throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		PPFVersorger.getInstanz(verwaltung).addListener(this);
		this.aktualisierePublikationIntern();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public ModulTyp getModulTyp() {
		return ModulTyp.PL_PRUEFUNG_FORMAL;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisierePublikation(IDatenFlussSteuerung iDfs) {
		this.iDfsMod = iDfs.getDFSFuerModul(this.verwaltung.getSWETyp(),
													this.getModulTyp());
		aktualisierePublikationIntern();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(IPPFVersorger parameter) {
		ppfParameter = parameter;
		this.aktualisierePublikationIntern();
	}
	
	
	/**
	 * Diese Methode verändert die Anmeldungen für die Publikation
	 * der plausibilisierten Daten.<br>
	 * Sie wird nur innerhalb dieses Moduls benötigt, da die Menge
	 * der betrachteten Objekte nicht (wie bei anderen Modulen der
	 * DUA) statisch ist, sondern sich mit der Parametrierung der
	 * formalen Plausibilitätsprüfung ändert. 
	 */
	private void aktualisierePublikationIntern(){
		if(this.publizieren){
			SystemObject[] objektFilter = new SystemObject[0];
			
			if(this.ppfParameter != null){
				objektFilter = this.ppfParameter.getBetrachteteObjekte();
			}
			
			Collection<DAVObjektAnmeldung> anmeldungenStd =
							new ArrayList<DAVObjektAnmeldung>();

			if(this.standardAspekte != null){
				anmeldungenStd = this.standardAspekte.
									getStandardAnmeldungen(
									objektFilter);
			}
			
			Collection<DAVObjektAnmeldung> anmeldungen = 
					this.iDfsMod.getDatenAnmeldungen(objektFilter, 
							anmeldungenStd);
			
			synchronized(this){
				this.publikationsAnmeldungen.modifiziereObjektAnmeldung(anmeldungen);
			}
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(ResultData[] resultate) {
		if(this.ppfParameter == null){
			LOGGER.fine("Es wurden noch keine" + //$NON-NLS-1$
						" Plausibilisierungsparameter empfangen"); //$NON-NLS-1$
			if(this.knoten != null){
				LOGGER.fine("Die Datenwerden nur" + //$NON-NLS-1$
						" weitergereicht an: " + this.knoten); //$NON-NLS-1$
				this.knoten.aktualisiereDaten(resultate);
			}
		}else
		if(resultate != null && resultate.length > 0){
			Collection<ResultData> weiterzuleitendeResultate =
				new ArrayList<ResultData>();

			for(ResultData resultat:resultate){

				if(resultat.getData() != null){
					Data pData = this.ppfParameter.plausibilisiere(resultat);
					if(pData != null){
						ResultData ersetztesResultat = new ResultData(
								resultat.getObject(),
								resultat.getDataDescription(),
								resultat.getDataTime(),
								pData);
						weiterzuleitendeResultate.add(ersetztesResultat);
	
						if(this.publizieren){
							ResultData publikationsDatum = 
								iDfsMod.getPublikationsDatum(resultat,
									pData, standardAspekte.getStandardAspekt(resultat));
							if(publikationsDatum != null){
								this.sendeSinnvoll(publikationsDatum);
							}
						}
					}else{
						weiterzuleitendeResultate.add(resultat);						
					}
				}else{
					weiterzuleitendeResultate.add(resultat);
	
					if(this.publizieren){
						ResultData publikationsDatum = 
							iDfsMod.getPublikationsDatum(resultat,
								null, standardAspekte.getStandardAspekt(resultat));
						if(publikationsDatum != null){
							this.sendeSinnvoll(publikationsDatum);
						}
					}					
				}
			}

			/**
			 * Weiterreichen der Daten an den nächsten Bearbeitungsknoten
			 */
			if(this.knoten != null){
				this.knoten.aktualisiereDaten(weiterzuleitendeResultate.
						toArray(new ResultData[0]));
			}
		}else{
			LOGGER.fine("Es wurden keine sinnvollen Daten empfangen"); //$NON-NLS-1$
		}				
	}
	
	
	/**
	 * Sendet das übergebene Datum (wenn nötig)
	 * 
	 * @param resultat ein zu publizierendes DAV-Datum
	 */
	private final void sendeSinnvoll(ResultData resultat){
		if(resultat.getData() == null){
			Boolean keineDaten = this.keineDaten.get(resultat.getObject());
			if(keineDaten != null && !keineDaten){
				this.publikationsAnmeldungen.sende(resultat);		
			}			
		}else{
			this.publikationsAnmeldungen.sende(resultat);
		}
		this.keineDaten.put(resultat.getObject(), resultat.getData() == null);
	}
	
}

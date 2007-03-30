package de.bsvrz.dua.plformal.plformal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import de.bsvrz.dua.plformal.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungFuerModul;
import de.bsvrz.dua.plformal.dfs.IDatenFlussSteuerung;
import de.bsvrz.dua.plformal.dfs.IDatenFlussSteuerungFuerModul;
import de.bsvrz.dua.plformal.schnittstellen.IVerwaltung;

/**
 * Implementierung des Moduls PL-Prüfung formal.
 * 
 * @author Thierfelder
 *
 */
public class PlPruefungFormal
extends AbstraktBearbeitungsKnotenAdapter
implements IPPFHilfeListener{

	/**
	 * die Parameter der formalen Plausibilisierung
	 */
	private IPPFHilfe ppfParameter = null;
		
	/**
	 * Parameter zur Datenflusssteuerung für diese SWE und dieses Modul
	 */
	private IDatenFlussSteuerungFuerModul iDfsMod = DatenFlussSteuerungFuerModul.STANDARD;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialisiere(IVerwaltung dieVerwaltung)
	throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		PPFHilfe.getInstanz(verwaltung).addListener(this);
		this.aktualisierePublikationIntern();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getModulName() {
		return DAVKonstanten.CONST_MODUL_TYP_PLPruefungFormal;
	}

	/**
	 * {@inheritDoc}
	 */
	public void aktualisierePublikation(IDatenFlussSteuerung iDfs) {
		if(iDfs != null){
			this.iDfsMod = iDfs.getDFSFuerModul(this.verwaltung.getApplikationsName(),
													this.getModulName());
			aktualisierePublikationIntern();
		}
	}
	
	/**
	 * Diese Routine wird nur innerhalb dieses Moduls benötigt, da die Menge der betrachteten Objekte
	 * nicht statisch ist, sondern sich mit der Parametrierung der formalen Plausibilitätsprüfung ändert
	 */
	private void aktualisierePublikationIntern(){
		if(this.publizieren){
			Collection<DAVDatenAnmeldung> anmeldungen = new ArrayList<DAVDatenAnmeldung>();
	
			Collection<DAVDatenAnmeldung> anmeldungenPara = 
					this.iDfsMod.getDatenAnmeldungen(this.ppfParameter != null?
							this.ppfParameter.getBetrachteteObjekte():null);
			Collection<DAVDatenAnmeldung> anmeldungenStd = 
					this.standardAspekte != null?this.standardAspekte.getStandardAnmeldungen():null;
			
			if(anmeldungenPara != null){
				anmeldungen.addAll(anmeldungenPara);
			}
			
			if(anmeldungenStd != null){
				anmeldungen.addAll(anmeldungenStd);				
			}
			this.publikationsAnmeldungen.modifiziereDatenAnmeldung(anmeldungen);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(IPPFHilfe parameter) {
		ppfParameter = parameter;
		this.aktualisierePublikationIntern();
	}

	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(ResultData[] resultate) {
		Collection<ResultData> weiterzuleitendeResultate = null;
		
		if(resultate != null && resultate.length > 0){		
			weiterzuleitendeResultate = new ArrayList<ResultData>();
			
			for(ResultData resultat:resultate){
				boolean resultatErsetzt = false;
				if(this.ppfParameter != null){
					Data pData = this.ppfParameter.plausibilisiere(resultat);
					
					if(pData != null){
						ResultData ersetztesResultat = new ResultData(
								resultat.getObject(),
								resultat.getDataDescription(),
								resultat.getDataTime(),
								pData);
						resultatErsetzt = true;
						weiterzuleitendeResultate.add(ersetztesResultat);
						
						if(this.publizieren){
							ResultData publikationsDatum = iDfsMod.getPublikationsDatum(resultat,
																	pData, standardAspekte.getStandardAspekt(resultat));
							this.publiziere(publikationsDatum);
						}
					}
				}
				
				if(!resultatErsetzt){
					/**
					 * Original weiterleiten
					 */
					weiterzuleitendeResultate.add(resultat);
				}
			}
		}
		
		/**
		 * Weiterreichen der Daten an den nächsten Bearbeitungsknoten
		 */
		if(this.knoten != null){
			this.knoten.aktualisiereDaten(weiterzuleitendeResultate != null?
					weiterzuleitendeResultate.toArray(new ResultData[0]):null);
		}
	}

}

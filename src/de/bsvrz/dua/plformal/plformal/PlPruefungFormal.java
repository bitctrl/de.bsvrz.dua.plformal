package de.bsvrz.dua.plformal.plformal;

import java.util.ArrayList;
import java.util.Collection;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungFuerModul;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;

/**
 * Implementierung des Moduls PL-Pr�fung formal.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
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
	 * Parameter zur Datenflusssteuerung f�r diese
	 * SWE und dieses Modul
	 */
	private IDatenFlussSteuerungFuerModul iDfsMod
						= DatenFlussSteuerungFuerModul.STANDARD;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialisiere(IVerwaltung dieVerwaltung)
	throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		this.standardAspekte = new PPFStandardAspekteVersorger(
				verwaltung).getStandardPubInfos();
		PPFHilfe.getInstanz(verwaltung).addListener(this);
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
	 * Diese Methode ver�ndert die Anmeldungen f�r die Publikation
	 * der plausibilisierten Daten.<br>
	 * Sie wird nur innerhalb dieses Moduls ben�tigt, da die Menge
	 * der betrachteten Objekte nicht (wie bei anderen Modulen der
	 * DUA) statisch ist, sondern sich mit der Parametrierung der
	 * formalen Plausibilit�tspr�fung �ndert. 
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
		 * Weiterreichen der Daten an den n�chsten Bearbeitungsknoten
		 */
		if(this.knoten != null){
			this.knoten.aktualisiereDaten(weiterzuleitendeResultate != null?
					weiterzuleitendeResultate.toArray(new ResultData[0]):null);
		}
	}

}

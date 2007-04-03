package de.bsvrz.dua.plformal.plformal;

import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.StandardAspekteVersorger;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.dfs.SWETyp;

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
	public void init(SWETyp swe)
	throws DUAInitialisierungsException{
		
		if(SWETyp.PL_PRUEFUNG_FORMAL.equals(swe)){

			/**
			 * Test Standardaspekte
			 */			
			this.standardAspekte = new StandardAspekteAdapter(
					new StandardPublikationsZuordnung[] {
							new StandardPublikationsZuordnung("typ.testPlPrüfungFormal", //$NON-NLS-1$
									"atg.testPlPrüfungFormal", //$NON-NLS-1$
									"asp.testAusgang", //$NON-NLS-1$
									"asp.testEingang") }); //$NON-NLS-1$
			
//			this.standardAspekte = new StandardAspekteAdapter(
//					new StandardPublikationsZuordnung[] {
//							new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
//									"atg.verkehrsDatenKurzZeitIntervall", //$NON-NLS-1$
//									"asp.externeErfassung", //$NON-NLS-1$
//									"asp.plausibilitätsPrüfungFormal"), //$NON-NLS-1$
//							new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
//									"atg.verkehrsDatenLangZeitIntervall", //$NON-NLS-1$
//									"asp.externeErfassung", //$NON-NLS-1$
//									"asp.plausibilitätsPrüfungFormal") }); //$NON-NLS-1$
			
		}else if(SWETyp.PL_PRUEFUNG_LOGISCH_LVE.equals(swe)){
			// TODO
		}else{
			LOGGER.warning("Es wurden für das Modul Pl-Prüfung formal und die SWE " + //$NON-NLS-1$
					swe + " keine Standard-Publikationsinformationen angegeben"); //$NON-NLS-1$
		}
		
	}	
}

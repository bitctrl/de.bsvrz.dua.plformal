package de.bsvrz.dua.plformal.av;

import java.util.Collection;
import java.util.TreeSet;

import stauma.dav.clientside.ClientDavInterface;
import sys.funclib.debug.Debug;

/**
 * Verwaltungsklasse f�r Datenanmeldungen.
 * 
 * @author Thierfelder
 *
 */
public abstract class DAVAnmeldungsVerwaltung {
		
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Baum der Datenanmeldungen, die im Moment aktuell sind
	 */
	protected Collection<DAVObjektAnmeldung> 
					aktuelleObjektAnmeldungen = new TreeSet<DAVObjektAnmeldung>();
	
	/**
	 * Datenverteilerverbindung
	 */
	protected ClientDavInterface dav = null;
	

	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteilerverbindung
	 */
	protected DAVAnmeldungsVerwaltung(final ClientDavInterface dav){
		this.dav = dav;
	}
	
	/**
	 * Modifiziert die hier verwalteten Datenanmeldungen dergestalt, dass
	 * nur die innerhalb der �bergebenen Liste beschriebenen Anmeldungen
	 * bestehen bleiben.
	 * 
	 * @param neueAnmeldungen die neue Liste mit Datenanmeldungen
	 */
	public final void modifiziereDatenAnmeldung(final
							Collection<DAVDatenAnmeldung> neueAnmeldungen){
		Collection<DAVObjektAnmeldung> neueObjektAnmeldungen =  
			new TreeSet<DAVObjektAnmeldung>();
		
		for(DAVDatenAnmeldung neueAnmeldung:neueAnmeldungen){
			neueObjektAnmeldungen.addAll(neueAnmeldung.getObjektAnmeldungen());
		}
		
		// Debug Anfang
		String info = "Verlangte Anmeldungen: "; //$NON-NLS-1$
		if(neueObjektAnmeldungen.size() == 0){
			info += "keine\n"; //$NON-NLS-1$
		}else{
			info += "\n"; //$NON-NLS-1$
		}
		for(DAVObjektAnmeldung neueObjektAnmeldung:neueObjektAnmeldungen){
			info += neueObjektAnmeldung;
		}
		info += "Bisherige Anmeldungen: "; //$NON-NLS-1$
		if(aktuelleObjektAnmeldungen.size() == 0){
			info += "keine\n"; //$NON-NLS-1$
		}else{
			info += "\n"; //$NON-NLS-1$
		}
		for(DAVObjektAnmeldung aktuelleObjektAnmeldung:aktuelleObjektAnmeldungen){
			info += aktuelleObjektAnmeldung;
		}
		// Debug Ende
			
		Collection<DAVObjektAnmeldung> diffObjekteAnmeldungen =  
			new TreeSet<DAVObjektAnmeldung>();
		for(DAVObjektAnmeldung neueAnmeldung:neueObjektAnmeldungen){
			if(!aktuelleObjektAnmeldungen.contains(neueAnmeldung)){
				diffObjekteAnmeldungen.add(neueAnmeldung);
			}
		}

		Collection<DAVObjektAnmeldung> diffObjekteAbmeldungen =  
			new TreeSet<DAVObjektAnmeldung>();
		for(DAVObjektAnmeldung aktuelleAnmeldung:aktuelleObjektAnmeldungen){
			if(!neueObjektAnmeldungen.contains(aktuelleAnmeldung)){
				diffObjekteAbmeldungen.add(aktuelleAnmeldung);
			}
		}		
		
		info += "--------\nABmeldungen: "; //$NON-NLS-1$
		info += abmelden(diffObjekteAbmeldungen);
		info += "ANmeldungen: "; //$NON-NLS-1$
		info += anmelden(diffObjekteAnmeldungen);
		LOGGER.info(info);
	}
		
	/**
	 * F�hrt alle �bergebenen Daten<b>ab</b>meldungen durch
	 * 
	 * @param abmeldungen durchzuf�hrende Daten<b>ab</b>meldungen
	 * @return eine Liste aller <b>ab</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String abmelden(final
								Collection<DAVObjektAnmeldung> abmeldungen);
	
	/**
	 * F�hrt alle �bergebenen Daten<b>an</b>meldungen durch.
	 * 
	 * @param anmeldungen durchzuf�hrende Daten<b>an</b>meldungen
	 * @return eine Liste aller neu <b>an</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String anmelden(final 
								Collection<DAVObjektAnmeldung> anmeldungen);

}

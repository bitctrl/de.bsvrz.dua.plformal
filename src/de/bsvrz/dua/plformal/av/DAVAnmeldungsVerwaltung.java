package de.bsvrz.dua.plformal.av;

import java.util.Collection;
import java.util.TreeSet;

import stauma.dav.clientside.ClientDavInterface;
import sys.funclib.debug.Debug;

/**
 * Abstrakte Verwaltungsklasse für Datenanmeldungen.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
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
	 * Modifiziert die hier verwalteten Datenanmeldungen
	 * dergestalt, dass nur die innerhalb der übergebenen
	 * Liste beschriebenen Anmeldungen bestehen bleiben.<br>
	 * D.h. insbesondere, dass eine übergebene leere Liste
	 * alle bereits durchgeführten Anmeldungen wieder 
	 * rückgängig macht.
	 * 
	 * @param neueAnmeldungen die neue Liste mit Datenanmeldungen
	 */
	public final void modifiziereDatenAnmeldung(final
							Collection<DAVDatenAnmeldung> neueAnmeldungen){
		Collection<DAVObjektAnmeldung> neueObjektAnmeldungen =  
			new TreeSet<DAVObjektAnmeldung>();

		if(neueAnmeldungen != null){
			for(DAVDatenAnmeldung neueAnmeldung:neueAnmeldungen){
				neueObjektAnmeldungen.addAll(neueAnmeldung.getObjektAnmeldungen());
			}
		}
		this.modifiziereObjektAnmeldung(neueObjektAnmeldungen);
	}

	/**
	 * Modifiziert die hier verwalteten Objektanmeldungen
	 * dergestalt, dass nur die innerhalb der übergebenen
	 * Liste beschriebenen Anmeldungen bestehen bleiben.<br>
	 * D.h. insbesondere, dass eine übergebene leere Liste
	 * alle bereits durchgeführten Anmeldungen wieder 
	 * rückgängig macht.
	 * 
	 * @param neueObjektAnmeldungen die neue Liste mit
	 * Objektanmeldungen
	 */
	public final void modifiziereObjektAnmeldung(final
			Collection<DAVObjektAnmeldung> neueObjektAnmeldungen){

//		Debug Anfang
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
		System.out.println(info);
//		Debug Ende

		synchronized (this) {
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
	}
	
	/**
	 * Führt alle übergebenen Daten<b>ab</b>meldungen durch
	 * 
	 * @param abmeldungen durchzuführende Daten<b>ab</b>meldungen
	 * @return eine Liste aller <b>ab</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String abmelden(final
								Collection<DAVObjektAnmeldung> abmeldungen);
	
	/**
	 * Führt alle übergebenen Daten<b>an</b>meldungen durch.
	 * 
	 * @param anmeldungen durchzuführende Daten<b>an</b>meldungen
	 * @return eine Liste aller neu <b>an</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String anmelden(final 
								Collection<DAVObjektAnmeldung> anmeldungen);

}

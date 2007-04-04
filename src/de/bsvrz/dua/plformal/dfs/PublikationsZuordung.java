package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * In dieser Klasse sind alle Informationen zusammengefasst, die das
 * Publikationsverhalten bezüglich <b>einer</b> bestimmten SWE, <b>einem</b>
 * bestimmten Modul-Typ und <b>einem</b> Publikationsaspekt beschreiben.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class PublikationsZuordung {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Der Modul-Typ
	 */
	private ModulTyp modulTyp = null;

	/**
	 * Der Publikationsaspekt
	 */
	private Aspect aspekt = null;

	/**
	 * die Objekte, für die ein Publikationsverhalten beschrieben ist
	 */
	private Collection<SystemObject> objekte = new HashSet<SystemObject>();

	/**
	 * die Attributgruppen, für die ein Publikationsverhalten vorgesehen ist
	 */
	private Collection<AttributeGroup> atgs = new HashSet<AttributeGroup>();

	/**
	 * soll publiziert werden
	 */
	private boolean publizieren = false;
	
	/**
	 * Mapt eine Objektanmeldung, d.h. eine Systemobjekt-Attributgruppen-
	 * Aspekt-Kombination auf das Flag <code>publizieren</code> 
	 */
	private Collection<DAVObjektAnmeldung> anmeldungen = 
									new TreeSet<DAVObjektAnmeldung>();

	
	/**
	 * Standardkonstruktor<br>
	 * <b>Achtung:</b> Sollte die Menge der übergebenen
	 * Objekte bzw. Attributgruppen leer sein, so werden
	 * <b>alle</b> Objekte bzw. Attributgruppen in den
	 * übergebenen Konfigurationskereichen (bzw. im 
	 * Standardkonfigurationsbereich) angenommen.
	 * 
	 * @param data ein Datenverteiler-Datum mit den
	 * mit einer Publikationszuordnung assoziierten Daten
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @throws Exception wenn einer der Parameter nicht
	 * ausgelesen werden konnte
	 */
	public PublikationsZuordung(Data data, IVerwaltung verwaltung)
	throws Exception{
		this.aspekt = (Aspect)data.getReferenceValue(
				DUAKonstanten.ATT_DFS_ASP).getSystemObject();
		this.modulTyp = ModulTyp.getZustand((int)data.getUnscaledValue(
				DUAKonstanten.ATT_DFS_MODUL_TYP).getState().getValue());
		this.publizieren = data.getTextValue(
				DUAKonstanten.ATT_DFS_PUBLIZIEREN).getText()
				.toLowerCase().equals("ja"); //$NON-NLS-1$

		for (int iObj = 0; iObj < data.getArray(
				DUAKonstanten.ATT_DFS_OBJ).getLength(); iObj++) {
			SystemObject dummy = data.getArray(DUAKonstanten.ATT_DFS_OBJ)
									.getReferenceValue(iObj).getSystemObject();
			this.objekte.addAll(DUAHilfe.getFinaleObjekte(dummy,
											verwaltung.getVerbindung()));
		}
		
		if(data.getArray(DUAKonstanten.ATT_DFS_ATG).getLength() == 0){
			this.atgs.add(null);
		}else{
			for (int iAtg = 0; iAtg < data.getArray(
					DUAKonstanten.ATT_DFS_ATG).getLength(); iAtg++) {
				this.atgs.add((AttributeGroup) data.getArray(
						DUAKonstanten.ATT_DFS_ATG)
						.getReferenceValue(iAtg)
						.getSystemObject());
			}
		}
		
		for(AttributeGroup atg:this.atgs){
			DataDescription datenBeschreibung = new DataDescription(atg, this.aspekt, (short)0);
			try{
				DAVDatenAnmeldung neueDatenAnmeldung = new DAVDatenAnmeldung(objekte.toArray(
							new SystemObject[0]), datenBeschreibung,
							DatenFlussSteuerungsHilfe.INSTANZ.getVerwaltung().getVerbindung());
				
				for(DAVObjektAnmeldung neueObjektAnmeldung:neueDatenAnmeldung.getObjektAnmeldungen()){
					if(this.anmeldungen.contains(neueObjektAnmeldung)){
						LOGGER.warning("Objektanmeldung bereits vorhanden: " + //$NON-NLS-1$
								neueObjektAnmeldung);
					}else{
						this.anmeldungen.add(neueObjektAnmeldung);
					}
				}
			}catch(Exception ex){
				LOGGER.warning("Vorgesehene Publikationsanmeldung" + //$NON-NLS-1$
						" ist nicht gültig", ex); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<DAVObjektAnmeldung> getObjektAnmeldungen(){
		return this.anmeldungen;
	}
	
	/**
	 * Erfragt den Aspekt
	 * 
	 * @return den Aspekt
	 */
	public Aspect getAspekt() {
		return aspekt;
	}

	/**
	 * Erfragt den Modul-Typ, für den diese Piblikationszuordnung gilt
	 * 
	 * @return der Modul-Typs
	 */
	public final ModulTyp getModulTyp() {
		return modulTyp;
	}

	/**
	 * Erfragt das Publikations-FLAG
	 * 
	 * @return das Publikations-FLAG
	 */
	public final boolean isPublizieren() {
		return publizieren;
	}

	/**
	 * Erfragt alle hier definierten Attributgruppen
	 * 
	 * @return alle hier definierten Attributgruppen
	 */
	public final Collection<AttributeGroup> getAtgs() {
		return atgs;
	}

	/**
	 * Erfragt die Menge aller hier definierten Objekte
	 * 
	 * @return die Menge aller hier definierten Objekte
	 */
	public final Collection<SystemObject> getObjekte() {
		return objekte;
	}

	/**
	 * Fragt, ob eine bestimmte Publikationszuordnung mit dieser hier kompatibel
	 * ist. Ob sie sich also widersprechen. Ein Widerspruch liegt vor, wenn:<br>
	 * 1. der Modul-Typ identisch ist UND<br>
	 * 2. für <b>beide</b> Objekte die Publikation eingeschalten ist UND<br>
	 * 3. der Publikationsaspekt der beiden Objekte <code>this</code> und
	 * <code>vergleichsObj</code> nicht identisch ist UND<br>
	 * 4. eine Objekt-Überschneidung innerhalb der Member-SystemObjekte von
	 * <code>this</code> und <code>vergleichsObj</code> besteht UND<br>
	 * 5. die Schnittmenge der Member-Attributgruppen nicht leer ist.<br>
	 * 
	 * @param vergleichsObj
	 *            das Objekt, mit dem dieses verglichen werden soll
	 * @return <code>null</code> wenn kein Widerspruch vorliegt und eine den
	 *         Widerspruch illustrierende Fehlermeldung sonst.
	 */
	public final String isKompatibelMit(final PublikationsZuordung vergleichsObj) {
		try{
			if (this.modulTyp.equals(vergleichsObj.getModulTyp()) && // 1.
					this.isPublizieren() && vergleichsObj.isPublizieren() && // 2.
					!this.getAspekt().equals(vergleichsObj.getAspekt())) { // 3.
	
				for(SystemObject thisObj:this.getObjekte()){	// 4.
					for(SystemObject thatObj:vergleichsObj.getObjekte()){
						String fehler = DUAHilfe.hasSchnittMenge(thisObj, thatObj);
						
						if(fehler != null){	// 5.
							if (this.getAtgs() != null && vergleichsObj.getAtgs() != null) {
								for (AttributeGroup thisAtg : this.getAtgs()) {
									for (AttributeGroup thatAtg : vergleichsObj.getAtgs()) {
										if (thisAtg.equals(thatAtg)) {
											return fehler
												+ "\nDie Attributgruppe " + thisAtg +  //$NON-NLS-1$
												" ist in beiden Publikationszuordnungen" + //$NON-NLS-1$
												" enthalten.\n(1)\n" + this //$NON-NLS-1$
												+ "\n(2)\n" + vergleichsObj; //$NON-NLS-1$
										}
									}
								}
							}							
						}
					}
				}
			}
		}catch(Exception ex){
			return "Die Kompatibilitätsprüfung konnte nicht" + //$NON-NLS-1$
					" vollständig durchgeführt werden: " + ex.getMessage(); //$NON-NLS-1$
		}
			
		return null; // keine Widersprüche
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "Modul-Typ: " + modulTyp + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Aspekt: " + aspekt + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Publizieren: " + (publizieren ? "ja" : "nein") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		for (SystemObject obj : objekte) {
			s += "Objekt: " + obj + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		for (AttributeGroup atg : atgs) {
			s += "Atg: " + atg + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
}

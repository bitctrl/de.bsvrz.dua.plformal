package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;
import java.util.HashSet;

import stauma.dav.clientside.Data;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;

/**
 * In dieser Klasse sind alle Informationen zusammengefasst, die das
 * Publikationsverhalten bez�glich <b>einer</b> bestimmten SWE, <b>einem</b>
 * bestimmten Modul-Typ und <b>einem</b> Publikationsaspekt beschreiben.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * @version 1.0
 */
public class PublikationsZuordung {

	/**
	 * Der Modul-Typ
	 */
	private ModulTyp modulTyp = null;

	/**
	 * Der Publikationsaspekt
	 */
	private Aspect aspekt = null;

	/**
	 * die Objekte, f�r die ein Publikationsverhalten beschrieben ist
	 */
	private Collection<SystemObject> objekte = new HashSet<SystemObject>();

	/**
	 * die Attributgruppen, f�r die ein Publikationsverhalten vorgesehen ist
	 */
	private Collection<AttributeGroup> atgs = new HashSet<AttributeGroup>();

	/**
	 * soll publiziert werden
	 */
	private boolean publizieren = false;

	
	/**
	 * Standardkonstruktor<br>
	 * <b>Achtung:</b> Sollte die Menge der �bergebenen
	 * Objekte bzw. Attributgruppen leer sein, so werden
	 * <b>alle</b> Objekte bzw. Attributgruppen in den
	 * �bergebenen Konfigurationskereichen (bzw. im 
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

		if(data.getArray(DUAKonstanten.ATT_DFS_OBJ).getLength() == 0){
			SystemObjectType typTyp = verwaltung.getVerbindung().
										getDataModel().getType(DUAKonstanten.TYP_TYP);
			
			for(SystemObject typ:typTyp.getObjects()){
				if(typ instanceof SystemObjectType){
					SystemObjectType objTyp = (SystemObjectType)typ;
					for(SystemObject obj:DUAHilfe.getAlleObjekteVomTypImKonfigBereich(
							verwaltung, objTyp, verwaltung.getKonfigurationsBereiche())){
						this.objekte.add(obj);				
					}			
				}
			}
		}else{
			for (int iObj = 0; iObj < data.getArray(
					DUAKonstanten.ATT_DFS_OBJ).getLength(); iObj++) {
				this.objekte.add(data.getArray(DUAKonstanten.ATT_DFS_OBJ)
						.getReferenceValue(iObj)
						.getSystemObject());
			}
		}
		
		if(data.getArray(DUAKonstanten.ATT_DFS_ATG).getLength() == 0){
			SystemObjectType typAtg = verwaltung.getVerbindung().
										getDataModel().getType(DUAKonstanten.TYP_ATG);
			
			for(SystemObject obj:typAtg.getObjects()){
				if(obj instanceof AttributeGroup){
					AttributeGroup atg = (AttributeGroup)obj;
					this.atgs.add(atg);				
				}
			}
		}else{
			for (int iAtg = 0; iAtg < data.getArray(
					DUAKonstanten.ATT_DFS_ATG).getLength(); iAtg++) {
				this.atgs.add((AttributeGroup) data.getArray(
						DUAKonstanten.ATT_DFS_ATG)
						.getReferenceValue(iAtg)
						.getSystemObject());
			}
		}
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
	 * Erfragt den Modul-Typ, f�r den diese Piblikationszuordnung gilt
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
	 * 2. f�r <b>beide</b> Objekte die Publikation eingeschalten ist UND<br>
	 * 3. der Publikationsaspekt der beiden Objekte <code>this</code> und
	 * <code>vergleichsObj</code> nicht identisch ist UND<br>
	 * 4. eine Objekt-�berschneidung innerhalb der Member-SystemObjekte von
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
			return "Die Kompatibilit�tspr�fung konnte nicht" + //$NON-NLS-1$
					" vollst�ndig durchgef�hrt werden: " + ex.getMessage(); //$NON-NLS-1$
		}
			
		return null; // keine Widerspr�che
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

package de.bsvrz.dua.plformal.av;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;

/**
 * Beschreibung einer Datenanmeldung mit (potentiell)
 * mehreren Systemobjekten und einer Datenbeschreibung.
 * Die Systemobjekte können hier sowohl Typen wie auch
 * Instanzen sein.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class DAVDatenAnmeldung{

	/**
	 * Menge aller einzelnen Objektanmeldungen
	 */
	protected Collection<DAVObjektAnmeldung> objektAnmeldungen = 
									new TreeSet<DAVObjektAnmeldung>();
	
	/**
	 * Die übergebenen Konfigurationsbereiche
	 */
	protected Collection<ConfigurationArea> konfigurationsBereiche = null;
	

	/**
	 * Standardkonstruktor (alle übergebenen Typen werden
	 * in ihre Instanzen aufgelöst). Sollten innerhalb der
	 * Datenbeschreibung sog. Wildcards (in Form von <code>
	 * null</code>) vorkommen, so werden diese
	 * hier aufgelöst.
	 * 
	 * @param objekte Objekte, für die die Anmeldung gilt
	 * @param desc Datenbeschreibung, für die die Objekte
	 * angemeldet sind
	 * @throws NullPointerException
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung,
							 final ClientDavInterface dav){
		if(objekte == null || objekte.length == 0){
			this.objektAnmeldungen.addAll(DUAHilfe.
					getAlleObjektAnmeldungen(null, datenBeschreibung, dav));
		}else{
			for(SystemObject obj:objekte){
				this.objektAnmeldungen.addAll(DUAHilfe.
						getAlleObjektAnmeldungen(obj, datenBeschreibung, dav));
			}
		}
	}
	
	/**
	 * Erweiterter Konstruktor mit Konfigurationsbereichen.
	 * Es werden hier nur Datenanmeldungen erzeugt, deren
	 * Objekte innerhalb der übergebenen Konfigurationsbereiche
	 * definiert sind. (alle übergebenen Typen werden in ihre
	 * Instanzen aufgelöst)
	 * 
	 * @param objekte Objekte, für die die Anmeldung gilt
	 * @param desc Datenbeschreibung, für die die Objekte angemeldet sind
	 * @param kb Liste mit Konfigurationsbereichen, durch die die Objektliste
	 * noch gefiltert werden soll
	 * @throws NullPointerException 
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung,
							 final Collection<ConfigurationArea> kBereiche,
							 final ClientDavInterface dav)
	throws NullPointerException{
		this.konfigurationsBereiche = kBereiche;
		
		if(objekte == null || objekte.length == 0){
			for(SystemObject finObj:DUAHilfe.getFinaleObjekte(null, dav)){
				if(kBereiche == null || kBereiche.size() == 0){
					this.objektAnmeldungen.addAll(DUAHilfe.
							getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
				}else{
					if(kBereiche.contains(finObj.getConfigurationArea())){
						this.objektAnmeldungen.addAll(
								DUAHilfe.
								getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
					}
				}
			}
		}else{
			for(SystemObject obj:objekte){
				for(SystemObject finObj:DUAHilfe.getFinaleObjekte(obj, dav)){
					if(kBereiche == null || kBereiche.size() == 0){
						this.objektAnmeldungen.addAll(DUAHilfe.
								getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
					}else{
						if(kBereiche.contains(finObj.getConfigurationArea())){
							this.objektAnmeldungen.addAll(
									DUAHilfe.
									getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
						}
					}

				}
			}
		}
	}
	
	/**
	 * Erfragt die Datenbeschreibung, für die die
	 * Objekte angemeldet sind
	 * 
	 * @return die Datenbeschreibung, für die die
	 * Objekte angemeldet sind
	 */
	public final DataDescription getDatenBeschreibung(){
		DataDescription datenBeschreibung = null;
		for(DAVObjektAnmeldung objektAnmeldung:this.objektAnmeldungen){
			datenBeschreibung = objektAnmeldung.getDatenBeschreibung();
			break;
		}
		return datenBeschreibung;
	}	

	/**
	 * Erfragt die Objekte, für die die Anmeldung gilt
	 * 
	 * @return Objekte, für die die Anmeldung gilt
	 */
	public final Collection<SystemObject> getObjekte() {
		Collection<SystemObject> objekte = 
							new HashSet<SystemObject>();
		for(DAVObjektAnmeldung objektAnmeldung:this.objektAnmeldungen){
			objekte.add(objektAnmeldung.getObjekt());
		}
		return objekte;
	}

	/**
	 * Erfragt die Objekte, für die die Anmeldung gilt als Array
	 * 
	 * @return die Objekte, für die die Anmeldung gilt als Array
	 */
	public final SystemObject[] getObjekteAlsArray() {
		return getObjekte().toArray(new SystemObject[0]);
	}
	
	/**
	 * Erfragt alle einzelnen Objekt-Anmeldungen
	 * die innerhalb von diesem Objekt definiert sind
	 * 
	 * @return eine (ggf. leere) Menge von Objekt-Anmeldungen
	 */
	public final Collection<DAVObjektAnmeldung> getObjektAnmeldungen(){
		return this.objektAnmeldungen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "Datenanmeldungsbeschreibung\n"; //$NON-NLS-1$
		String bereiche = "keine\n"; //$NON-NLS-1$
		
		if(this.konfigurationsBereiche != null){
			bereiche = DUAKonstanten.EMPTY_STR;
			for(ConfigurationArea kb:this.konfigurationsBereiche){
				bereiche += kb.toString() + "\n"; //$NON-NLS-1$
			}
		}
		s += "Konfigurationsbereiche:\n" + bereiche;  //$NON-NLS-1$
		s += "Datenbeschreibung:\n" + getDatenBeschreibung() + "\nObjekte:\n"; //$NON-NLS-1$ //$NON-NLS-2$
		for(SystemObject obj:getObjekte()){
			s += obj + "\n"; //$NON-NLS-1$
		}
		
		return s;
	}
	
}

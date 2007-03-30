package de.bsvrz.dua.plformal.av;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;

/**
 * Beschreibung einer Datenanmeldung mit mehreren
 * Objekten und einer Datenbeschreibung 
 * 
 * @author Thierfelder
 *
 */
public class DAVDatenAnmeldung{

	/**
	 * Menge aller einzelnen Objektanmeldungen
	 */
	protected Collection<DAVObjektAnmeldung> objektAnmeldungen = 
									new TreeSet<DAVObjektAnmeldung>();
	
	/**
	 * Die �bergebenen Konfigurationsbereiche
	 */
	protected Collection<ConfigurationArea> konfigurationsBereiche = null;
	

	/**
	 * Standardkonstruktor (alle �bergebenen Typen werden
	 * in ihre Instanzen aufgel�st)
	 * 
	 * @param objekte Objekte, f�r die die Anmeldung gilt
	 * @param desc Datenbeschreibung, f�r die die Objekte
	 * angemeldet sind
	 * @throws Exception wenn entweder das Systemobjekt,
	 * die Datenbeschreibung, deren Attributgruppe oder deren
	 * Aspekt <code>null</code> ist 
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung)
	throws Exception{
		for(SystemObject obj:objekte){
			if(obj.isOfType(DAVKonstanten.TYP_TYP)){
				SystemObjectType typ = (SystemObjectType)obj;
				for(SystemObject element:typ.getObjects()){
					this.objektAnmeldungen.add(
							new DAVObjektAnmeldung(element, datenBeschreibung));
				}
			}else{
				this.objektAnmeldungen.add(
						new DAVObjektAnmeldung(obj, datenBeschreibung));
			}
		}				
	}
	
	/**
	 * Erweiterter Konstruktor mit Konfigurationsbereichen.
	 * Es werden hier nur Datenanmeldungen erzeugt, deren
	 * Objekte innerhalb der �bergebenen Konfigurationsbereiche
	 * definiert sind. (alle �bergebenen Typen werden in ihre
	 * Instanzen aufgel�st)
	 * 
	 * @param objekte Objekte, f�r die die Anmeldung gilt
	 * @param desc Datenbeschreibung, f�r die die Objekte angemeldet sind
	 * @param kb Liste mit Konfigurationsbereichen, durch die die Objektliste
	 * noch gefiltert werden soll
	 * @throws Exception wenn entweder das Systemobjekt,
	 * die Datenbeschreibung, deren Attributgruppe oder deren
	 * Aspekt <code>null</code> ist
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung,
							 final Collection<ConfigurationArea> kBereiche)
	throws Exception{
		this.konfigurationsBereiche = kBereiche;
		
		for(SystemObject obj:objekte){
			if(kBereiche == null || kBereiche.size() == 0){
				if(obj.isOfType(DAVKonstanten.TYP_TYP)){
					SystemObjectType typ = (SystemObjectType)obj;
					for(SystemObject element:typ.getObjects()){
						this.objektAnmeldungen.add(
								new DAVObjektAnmeldung(element, datenBeschreibung));
					}
				}else{
					this.objektAnmeldungen.add(
							new DAVObjektAnmeldung(obj, datenBeschreibung));
				}				
			}else{
				if(obj.isOfType(DAVKonstanten.TYP_TYP)){
					SystemObjectType typ = (SystemObjectType)obj;
					for(SystemObject element:typ.getObjects()){
						if(kBereiche.contains(element.getConfigurationArea())){
							this.objektAnmeldungen.add(
									new DAVObjektAnmeldung(element, datenBeschreibung));
						}						
					}
				}else{
					if(kBereiche.contains(obj.getConfigurationArea())){
						this.objektAnmeldungen.add(
								new DAVObjektAnmeldung(obj, datenBeschreibung));
					}
				}				
			}
		}
	}
	
	/**
	 * Erfragt die Datenbeschreibung, f�r die die
	 * Objekte angemeldet sind
	 * 
	 * @return die Datenbeschreibung, f�r die die
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
	 * Erfragt die Objekte, f�r die die Anmeldung gilt
	 * 
	 * @return Objekte, f�r die die Anmeldung gilt
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
	 * Erfragt die Objekte, f�r die die Anmeldung gilt als Array
	 * 
	 * @return die Objekte, f�r die die Anmeldung gilt als Array
	 */
	public final SystemObject[] getObjekteAlsArray() {
		return getObjekte().toArray(new SystemObject[0]);
	}
	
	/**
	 * Erfragt alle einzelnen Objekt-Anmeldungen
	 * die innerhalb von diesem Objekt definiert sind
	 * 
	 * @return eine Menge von Objekt-Anmeldungen
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
			bereiche = DAVKonstanten.EMPTY_STR;
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

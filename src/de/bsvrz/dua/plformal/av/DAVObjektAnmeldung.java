package de.bsvrz.dua.plformal.av;

import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.SystemObject;

/**
 * Repräsentiert die Anmeldung eines Systemobjekts
 * unter einer Datenbeschreibung. Die Elemente dieser
 * Klasse lassen sich korrekt und ohne Doppelungen
 * in ein <code>TreeSet</code>-Objekt einspeisen.
 * 
 * @author Thierfelder
 *
 */
public class DAVObjektAnmeldung
implements Comparable<DAVObjektAnmeldung>{

	/**
	 * Das Systemobjekt
	 */
	private SystemObject objekt = null;
	
	/**
	 * Die Datenbeschreibung unter der das Systemobjekt 
	 * angemeldet werden soll bzw. ist 
	 */
	private DataDescription datenBeschreibung = null;
		
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param objekt das Systemobjekt
	 * @param datenBeschreibung die Datenbeschreibung unter
	 * der das Systemobjekt angemeldet werden soll bzw. ist
	 * @throws Exception wenn entweder das Systemobjekt,
	 * die Datenbeschreibung, deren Attributgruppe oder deren
	 * Aspekt <code>null</code> ist 
	 */
	public DAVObjektAnmeldung(final SystemObject objekt,
							  final DataDescription datenBeschreibung)
	throws Exception{
		if(objekt == null){
			throw new Exception("Übergebenes Objekt" + //$NON-NLS-1$
					" ist <<null>>"); //$NON-NLS-1$
		}
		if(datenBeschreibung == null){
			throw new Exception("Übergebene Datenbeschreibung" + //$NON-NLS-1$
					" ist <<null>>"); //$NON-NLS-1$
		}
		if(datenBeschreibung.getAttributeGroup() == null){
			throw new Exception("Übergebene Attributgruppe" + //$NON-NLS-1$
			" ist <<null>>"); //$NON-NLS-1$
		}
		if(datenBeschreibung.getAspect() == null){
			throw new Exception("Übergebener Aspekt" + //$NON-NLS-1$
			" ist <<null>>"); //$NON-NLS-1$
		}
		this.objekt = objekt;
		this.datenBeschreibung = datenBeschreibung;
	}
	
	/**
	 * Erfragt die Datenbeschreibung unter der das Systemobjekt 
	 * angemeldet werden soll bzw. ist 
	 * 
	 * @return datenBeschreibung eine Datenbeschreibung
	 */
	public DataDescription getDatenBeschreibung() {
		return datenBeschreibung;
	}

	/**
	 * Erfragt das Systemobjekt
	 * 
	 * @return objekt ein Systenobjekt
	 */
	public SystemObject getObjekt() {
		return objekt;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(DAVObjektAnmeldung that) {
		int result = Long.valueOf(this.getObjekt().getId())
							.compareTo(that.getObjekt().getId());
		
		if(result == 0){
			result = Long.valueOf(this.getDatenBeschreibung().
							getAttributeGroup().getId()).compareTo(that.
							getDatenBeschreibung().getAttributeGroup().getId());
		}
		if(result == 0){
			result = Long.valueOf(this.getDatenBeschreibung().
							getAspect().getId()).compareTo(that.
							getDatenBeschreibung().getAspect().getId());
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Objekt: " + this.objekt + "\nDatenbeschreibung: " //$NON-NLS-1$ //$NON-NLS-2$
					+ this.datenBeschreibung + "\n"; //$NON-NLS-1$
	}
}

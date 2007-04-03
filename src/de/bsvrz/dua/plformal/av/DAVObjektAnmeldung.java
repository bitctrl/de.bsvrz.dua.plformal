package de.bsvrz.dua.plformal.av;

import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;

/**
 * Repräsentiert die Anmeldung eines Systemobjekts
 * unter einer Datenbeschreibung. Die Elemente dieser
 * Klasse lassen sich korrekt und ohne Doppelungen
 * in ein <code>TreeSet</code>-Objekt einspeisen
 * oder als Schlüssel für eine <code>TreeMap</code>
 * benutzen.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
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
	 * Aspekt <code>null</code> ist. Oder, wenn die Objekt-
	 * Attributgruppen-Aspekt-Kombination an sich ungültig ist. 
	 */
	public DAVObjektAnmeldung(final SystemObject objekt,
							  final DataDescription datenBeschreibung)
	throws Exception{
		String fehler = DUAHilfe.isKombinationOk(objekt, datenBeschreibung);
		if(fehler != null){
			throw new Exception(fehler);
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
	 * 
	 * Diese Methode muss implementiert werden, da nach der
	 * Exploration des Baums über <code>compareTo(..)</code>
	 * nochmals mit <code>equals(..)</code> explizit auf
	 * Gleichheit getestet wird.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if(obj instanceof DAVObjektAnmeldung){
			DAVObjektAnmeldung that = (DAVObjektAnmeldung)obj;
			result = this.getObjekt().equals(that.getObjekt()) &&
					 this.getDatenBeschreibung().getAttributeGroup().equals(
							 that.getDatenBeschreibung().getAttributeGroup()) &&
					 this.getDatenBeschreibung().getAspect().equals(
							 that.getDatenBeschreibung().getAspect());
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

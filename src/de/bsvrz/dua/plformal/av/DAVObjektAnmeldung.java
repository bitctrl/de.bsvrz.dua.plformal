package de.bsvrz.dua.plformal.av;

import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;

/**
 * Repr�sentiert die Anmeldung eines <b>finalen</b> Systemobjekts
 * (ein finales Systemobjekt ist entweder ein Konfigurationsobjekt
 * oder ein Dynamisches Objekt) unter einer bestimmten Datenbeschreibung.<br>
 * <b>Achtung:</b>
 * <ul>
 * <li>
 * Diese Klasse ist so entworfen, dass nur im
 * Sinne des Datenverteilers kompatible Objekt-Attributgruppe-
 * Aspekt-Kombinationen akzeptiert werden (via Konstruktor).
 * </li>
 * <li>
 * Weiterhin ist diese Klasse so entworfen, dass beim 
 * Einspeisen ihrer Elemente in <code>TreeSet</code>- oder 
 * <code>TreeMap</code>-Strukturen keine Datenverteiler-spezifischen
 * Widerspr�che innerhalb dieser Strukturen auftreten k�nnen. D.h.
 * insbesondere, dass alle Elemente einer solchen Struktur 
 * konfliktfrei zum Senden oder Empfangen von Daten angemeldet
 * werden k�nnen.<br>
 * Mit konfliktfrei im Sinne des Datenverteilers
 * ist gemeint, dass in einer solchen Struktur keine Objekt-
 * Attributgruppe-Aspekt-Kombinationen doppelt auftreten.
 * </li>
 * </ul>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DAVObjektAnmeldung
implements Comparable<DAVObjektAnmeldung>{

	/**
	 * Das (finale) Systemobjekt
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
	 * @param objekt das (finale) Systemobjekt
	 * @param datenBeschreibung die Datenbeschreibung unter
	 * der das Systemobjekt angemeldet werden soll bzw. ist
	 * @throws Exception wenn entweder das Systemobjekt,
	 * die Datenbeschreibung, deren Attributgruppe oder deren
	 * Aspekt <code>null</code> ist, wenn die Objekt-
	 * Attributgruppen-Aspekt-Kombination an sich ung�ltig bzw.
	 * inkompatibel ist, oder wenn das �bergebene Systemobjekt
	 * kein Konfigurationsobjekt oder Dynamisches Objekt ist.
	 **/
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
	public final DataDescription getDatenBeschreibung() {
		return datenBeschreibung;
	}

	/**
	 * Erfragt das Systemobjekt
	 * 
	 * @return objekt ein Systenobjekt
	 */
	public final SystemObject getObjekt() {
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
	 * Exploration des Baums �ber <code>compareTo(..)</code>
	 * (bspw. beim Aufruf von <code>contains()</code>) nochmals
	 * mit <code>equals(..)</code> explizit auf Gleichheit
	 * getestet wird.
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
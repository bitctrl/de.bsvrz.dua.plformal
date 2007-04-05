package de.bsvrz.dua.plformal.plformal;

import stauma.dav.clientside.Data;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;

/**
 * Diese Klasse repräsentiert alle Informationen, die innerhalb eines
 * Datensatzes <code>AttributSpezifikation</code> in der Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> enthalten sind.
 * 
 * @author Thierfelder
 * 
 */
public class PPFAttributSpezifikation {
	
	/**
	 * der Attributpfad
	 */
	private String attributPfad = null;

	/**
	 * Min-Wert
	 */
	private long min = DUAKonstanten.LONG_UNDEFINIERT;

	/**
	 * Max-Wert
	 */
	private long max = DUAKonstanten.LONG_UNDEFINIERT;

	/**
	 * Vergleichs- bzw. Ersetzungsmethode
	 */
	private long methode = DUAKonstanten.LONG_UNDEFINIERT;

	
	/**
	 * Standardkonstruktor
	 * 
	 * @param attributSpezifikation DAV-Datensatzes der Liste <code>AttributSpezifikation</code>
	 * @throws Exception falls Fehler beim Auslesen des DAV-Datensatzes auftreten
	 */
	public PPFAttributSpezifikation(final Data attributSpezifikation)
	throws Exception{
		this.attributPfad = attributSpezifikation.getTextValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_PFAD).getText().toString();
		this.min = attributSpezifikation.getUnscaledValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MIN).longValue();
		this.max = attributSpezifikation.getUnscaledValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MAX).longValue();
		this.methode = attributSpezifikation.getUnscaledValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_OPT).longValue();
	}

	/**
	 * Erfragt den Attributpfad
	 * 
	 * @return der Attributpfad
	 */
	public String getAttributPfad() {
		return attributPfad;
	}

	/**
	 * Erfragt Max-Wert
	 * 
	 * @return der Max-Wert
	 */
	public long getMax() {
		return max;
	}

	/**
	 * Erfragt die 
	 * 
	 * @return
	 */
	public long getMethode() {
		return methode;
	}

	/**
	 * Erfragt Min-Wert
	 * 
	 * @return der Min-Wert
	 */
	public long getMin() {
		return min;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = DUAKonstanten.STR_UNDEFINIERT;

		if(this.attributPfad != null){
			s = "Attributpfad: " + this.attributPfad + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Min: " + this.min + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Max: " + this.max + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			s += "Methode: " + DUAKonstanten.ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT. //$NON-NLS-1$
									get(new Long(this.methode)) + "\n"; //$NON-NLS-1$
		}

		return s;
	}

	/**
	 * Vergleicht dieses Objekt mit einem anderen vom Typ <code>BeschreibungFuerAttribut</code>.
	 * Beide Objekte gelten als identisch, wenn deren Attributpfade identisch sind.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj != null && obj instanceof PPFAttributSpezifikation && 
				( this.getAttributPfad().equals( ((PPFAttributSpezifikation)obj).getAttributPfad() ) ));
	}
}

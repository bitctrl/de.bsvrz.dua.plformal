package de.bsvrz.dua.plformal.plformal;

/**
 * Dieses Interface muss von allen Klassen implementiert werden, die
 * aktuelle Informationen �ber die Parameter zur formalen Plausibilisierung
 * �ber das Interface <code>IPPFHilfe</code> empfangen wollen.
 * 
 * @author Thierfelder
 *
 */
public interface IPPFHilfeListener {

	/**
	 * Aktualisiert die Parameter zur formalen Plausibilisierung
	 * 
	 * @param parameter Zugriff auf alle Parameter der formalen Plausibilisierung
	 */
	public void aktualisiereParameter(final IPPFHilfe parameter);
	
}

package de.bsvrz.dua.plformal.dfs.schnittstellen;

/**
 * Interface das alle Klassen implementieren müssen,
 * die Änderungen innerhalb der Datenflusssteuerung
 * empfangen wollen.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public interface IDatenFlussSteuerungsListener {

	/**
	 * Aktualisiert alle Parameter zur Datenflusssteuerung.
	 * Wenn innerhalb der Datenflusssteuerung für ein bestimtes
	 * Datum eine Publikation vorgesehen wird, die vom
	 * Standardpublikationsaspekt dieses Datums abweicht, so
	 * wird unter diesem Aspekt publiziert.<br> 
	 * <b>Achtung:</b> Sollte ein Datum innerhalb der
	 * Datenflusssteuerung nicht zur Publikation vorgesehen
	 * sein, so wird auch nicht unter dem Standardaspekt publiziert.
	 * Weiterhin wird auch nicht unter dem Standardaspekt
	 * publiziert, wenn dieser innerhalb der Datenflusssteuerung
	 * explizit auf <code>nicht publizieren</code> gesetzt wurde.
	 * 
	 * @param dfs
	 *            Schnittstelle zur Datenflusssteuerung
	 */
	public void aktualisierePublikation(
							 final IDatenFlussSteuerung dfs);

}

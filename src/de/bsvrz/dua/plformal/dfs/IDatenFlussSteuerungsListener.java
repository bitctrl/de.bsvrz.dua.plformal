package de.bsvrz.dua.plformal.dfs;

/**
 * Interface das alle Klassen implementieren müssen, die Änderungen innerhalb
 * der Datenflusssteuerung empfangen wollen.
 * 
 * @author Thierfelder
 * 
 */
public interface IDatenFlussSteuerungsListener {

	/**
	 * Aktualisiert alle Parameter zur Datenflusssteuerung. Wenn innerhalb der
	 * Datenflusssteuerung für ein bestimtes Daten eine Publikation vorgesehen
	 * wird, die sich vom Standardpublikationsaspekt eines Datums abweicht, so
	 * wird unter diesem Aspekt publiziert. Sollte ein Datum innerhalb der
	 * Datenflusssteuerung nicht zur Publikation vorgesehen sein, so wird auch
	 * nicht unter dem Standardaspekt publiziert.
	 * 
	 * @param dfs
	 *            Schnittstelle zur Datenflusssteuerung
	 */
	public void aktualisierePublikation(final IDatenFlussSteuerung dfs);

}

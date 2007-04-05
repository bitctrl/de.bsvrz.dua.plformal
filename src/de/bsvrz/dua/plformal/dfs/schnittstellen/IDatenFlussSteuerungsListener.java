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
	 * 
	 * @param dfs
	 *            Schnittstelle zur Datenflusssteuerung (dieses
	 *            Objekt ist <b>immer</b> ungleich <code>null</code>)
	 */
	public void aktualisierePublikation(
							 final IDatenFlussSteuerung dfs);

}
